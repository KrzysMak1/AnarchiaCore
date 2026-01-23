package me.anarchiacore.customitems;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.util.*;

public class CustomItemsConfig {
    private final Map<String, CustomItemDefinition> items = new LinkedHashMap<>();
    private final Map<String, EventItemDefinition> eventItems = new LinkedHashMap<>();
    private final WorldGuardRules worldGuardRules;

    public CustomItemsConfig(ConfigurationSection section, Map<String, ConfigurationSection> eventItemSections) {
        if (section == null) {
            worldGuardRules = new WorldGuardRules(false, List.of(), List.of());
            return;
        }
        ConfigurationSection itemsSection = section.getConfigurationSection("items");
        if (itemsSection != null) {
            for (String key : itemsSection.getKeys(false)) {
                ConfigurationSection itemSection = itemsSection.getConfigurationSection(key);
                if (itemSection == null) {
                    continue;
                }
                Material material = Material.matchMaterial(itemSection.getString("material", "STONE"));
                if (material == null) {
                    material = Material.STONE;
                }
                String displayName = itemSection.getString("meta.display-name");
                List<String> lore = itemSection.getStringList("meta.lore");
                Map<String, Integer> enchants = new LinkedHashMap<>();
                ConfigurationSection enchSection = itemSection.getConfigurationSection("meta.enchantments");
                if (enchSection != null) {
                    for (String enchKey : enchSection.getKeys(false)) {
                        enchants.put(enchKey, enchSection.getInt(enchKey));
                    }
                }
                List<String> flags = itemSection.getStringList("meta.flags");
                int cmd = itemSection.getInt("customModelData", 0);
                boolean unbreakable = itemSection.getBoolean("unbreakable", false);
                items.put(key.toLowerCase(Locale.ROOT), new CustomItemDefinition(key, material, displayName, lore, enchants, flags, cmd, unbreakable));
            }
        }
        ConfigurationSection eventSection = section.getConfigurationSection("eventItems");
        if (eventSection != null) {
            for (String key : eventSection.getKeys(false)) {
                ConfigurationSection itemSection = eventSection.getConfigurationSection(key);
                if (itemSection == null) {
                    continue;
                }
                eventItems.put(key.toLowerCase(Locale.ROOT), readEventItem(key, itemSection));
            }
        }
        if (eventItemSections != null) {
            for (Map.Entry<String, ConfigurationSection> entry : eventItemSections.entrySet()) {
                String key = entry.getKey();
                if (key == null) {
                    continue;
                }
                eventItems.putIfAbsent(key.toLowerCase(Locale.ROOT), readEventItem(key, entry.getValue()));
            }
        }
        ConfigurationSection wgSection = section.getConfigurationSection("worldguard");
        boolean wgEnabled = wgSection != null && wgSection.getBoolean("enabled", true);
        List<String> allowAll = wgSection != null ? wgSection.getStringList("allowAllEventItemsRegions") : List.of();
        List<String> allowExceptExplosives = wgSection != null ? wgSection.getStringList("allowEventItemsExceptExplosivesRegions") : List.of();
        worldGuardRules = new WorldGuardRules(wgEnabled, allowAll, allowExceptExplosives);
    }

    private EventItemDefinition readEventItem(String id, ConfigurationSection section) {
        Material material = Material.matchMaterial(section.getString("material", "STONE"));
        if (material == null) {
            material = Material.STONE;
        }
        String displayName = section.getString("name");
        List<String> lore = section.getStringList("lore");
        Map<String, Integer> enchants = parseEnchantments(section.getStringList("enchantments"));
        List<String> flags = section.getStringList("flags");
        int cmd = section.getInt("customModelData", 0);
        boolean unbreakable = section.getBoolean("unbreakable", false);
        int cooldown = section.getInt("cooldown", 0);
        int explosionRadius = section.getInt("explosionRadius", 0);
        int explosionParticleCount = section.getInt("explosionParticleCount", 0);
        boolean useProjectileMode = section.getBoolean("useProjectileMode", false);
        double projectileSpeed = section.getDouble("projectileSpeed", 1.5);
        int projectileLifeTimeTicks = section.getInt("lifeTimeTicks", 40);
        List<String> protectedBlocks = section.getStringList("protectedBlocks");
        boolean spawnFire = section.getBoolean("spawnFire", false);
        double fireChance = section.getDouble("fireChance", 0.0);
        MessageDefinition message = readMessage(section.getConfigurationSection("messages"));
        List<String> noDestroyRegions = section.getStringList("noDestroyRegions");
        List<String> noPlaceRegions = section.getStringList("noPlaceRegions");
        boolean preventRegionDestruction = section.getBoolean("preventRegionDestruction", false);
        return new EventItemDefinition(id, material, displayName, lore, enchants, flags, cmd, unbreakable,
                cooldown, explosionRadius, explosionParticleCount, useProjectileMode, projectileSpeed, projectileLifeTimeTicks,
                protectedBlocks, spawnFire, fireChance, message, noDestroyRegions, noPlaceRegions, preventRegionDestruction);
    }

    private MessageDefinition readMessage(ConfigurationSection section) {
        if (section == null) {
            return new MessageDefinition("", new TitleMessage("", ""), "");
        }
        String cooldown = section.getString("cooldown", "");
        ConfigurationSection consumer = section.getConfigurationSection("consumer");
        if (consumer == null) {
            consumer = section.getConfigurationSection("shooter");
        }
        TitleMessage titleMessage = readTitleMessage(consumer);
        String chat = section.getString("chatMessage", "");
        if ((chat == null || chat.isBlank()) && consumer != null) {
            chat = consumer.getString("chatMessage", "");
        }
        return new MessageDefinition(cooldown, titleMessage, chat);
    }

    private TitleMessage readTitleMessage(ConfigurationSection section) {
        if (section == null) {
            return new TitleMessage("", "");
        }
        return new TitleMessage(section.getString("title", ""), section.getString("subtitle", ""));
    }

    private Map<String, Integer> parseEnchantments(List<String> enchantments) {
        Map<String, Integer> parsed = new LinkedHashMap<>();
        if (enchantments == null) {
            return parsed;
        }
        for (String entry : enchantments) {
            if (entry == null || entry.isBlank()) {
                continue;
            }
            String[] parts = entry.split(":", 2);
            if (parts.length != 2) {
                continue;
            }
            try {
                parsed.put(parts[0], Integer.parseInt(parts[1]));
            } catch (NumberFormatException ignored) {
                parsed.put(parts[0], 1);
            }
        }
        return parsed;
    }

    public CustomItemDefinition getItemDefinition(String id) {
        if (id == null) {
            return null;
        }
        return items.get(id.toLowerCase(Locale.ROOT));
    }

    public Collection<CustomItemDefinition> getAllItems() {
        return items.values();
    }

    public Set<String> getItemIds() {
        return items.keySet();
    }

    public Set<String> getAllItemIds() {
        Set<String> ids = new LinkedHashSet<>(items.keySet());
        ids.addAll(eventItems.keySet());
        return ids;
    }

    public EventItemDefinition getEventItemDefinition(String id) {
        if (id == null) {
            return null;
        }
        return eventItems.get(id.toLowerCase(Locale.ROOT));
    }

    public Collection<EventItemDefinition> getEventItems() {
        return eventItems.values();
    }

    public WorldGuardRules getWorldGuardRules() {
        return worldGuardRules;
    }

    public record WorldGuardRules(boolean enabled, List<String> allowAllRegions, List<String> allowExceptExplosivesRegions) {
    }

    public record EventItemDefinition(
            String id,
            Material material,
            String displayName,
            List<String> lore,
            Map<String, Integer> enchantments,
            List<String> flags,
            int customModelData,
            boolean unbreakable,
            int cooldown,
            int explosionRadius,
            int explosionParticleCount,
            boolean useProjectileMode,
            double projectileSpeed,
            int projectileLifeTimeTicks,
            List<String> protectedBlocks,
            boolean spawnFire,
            double fireChance,
            MessageDefinition messages,
            List<String> noDestroyRegions,
            List<String> noPlaceRegions,
            boolean preventRegionDestruction
    ) {
    }

    public record MessageDefinition(String cooldown, TitleMessage consumer, String chatMessage) {
    }

    public record TitleMessage(String title, String subtitle) {
    }
}
