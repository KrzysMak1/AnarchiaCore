package me.anarchiacore.config;

import me.anarchiacore.customitems.CustomItemDefinition;
import me.anarchiacore.customitems.CustomItemsConfig;
import me.anarchiacore.hearts.AnarchiczneSerceDefinition;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;

public class ConfigManager {
    private final JavaPlugin plugin;
    private int defaultHearts;
    private int maxHearts;
    private String prefix;
    private Set<World.Environment> blockedCrystalDimensions = new LinkedHashSet<>();
    private Set<String> blockedCrystalWorldNames = new LinkedHashSet<>();
    private String trashTitle;
    private int trashRows;
    private String trashClearedMessage;
    private AnarchiczneSerceDefinition serceDefinition;
    private CustomItemsConfig customItemsConfig;

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void reload() {
        plugin.reloadConfig();
        defaultHearts = plugin.getConfig().getInt("defaultHearts", 30);
        maxHearts = plugin.getConfig().getInt("maxHearts", 40);
        prefix = plugin.getConfig().getString("messages.prefix", "");
        blockedCrystalDimensions = new LinkedHashSet<>();
        blockedCrystalWorldNames = new LinkedHashSet<>();
        for (String envName : plugin.getConfig().getStringList("blockedEndCrystalDimensions")) {
            if (envName == null) {
                continue;
            }
            String token = envName.trim();
            if (token.isEmpty()) {
                continue;
            }
            try {
                blockedCrystalDimensions.add(World.Environment.valueOf(token.toUpperCase(Locale.ROOT)));
            } catch (IllegalArgumentException ex) {
                blockedCrystalWorldNames.add(token.toLowerCase(Locale.ROOT));
            }
        }
        trashTitle = plugin.getConfig().getString("trash.title", "Kosz");
        trashRows = plugin.getConfig().getInt("trash.rows", 5);
        trashClearedMessage = plugin.getConfig().getString("trash.clearMessage");
        serceDefinition = loadSerceDefinition();
        customItemsConfig = loadCustomItems();
    }

    private AnarchiczneSerceDefinition loadSerceDefinition() {
        ConfigurationSection section = plugin.getConfig().getConfigurationSection("anarchiczneSerce");
        if (section == null) {
            return new AnarchiczneSerceDefinition(Material.GUNPOWDER, null, null, new LinkedHashMap<>(), new ArrayList<>(), 0);
        }
        String materialName = section.getString("material", "GUNPOWDER");
        Material material = Material.matchMaterial(materialName);
        if (material == null) {
            material = Material.GUNPOWDER;
        }
        ConfigurationSection metaSection = section.getConfigurationSection("meta");
        String displayName = null;
        List<String> lore = new ArrayList<>();
        Map<String, Integer> enchants = new LinkedHashMap<>();
        List<String> flags = new ArrayList<>();
        if (metaSection != null) {
            displayName = metaSection.getString("display-name");
            lore = metaSection.getStringList("lore");
            ConfigurationSection enchantSection = metaSection.getConfigurationSection("enchantments");
            if (enchantSection != null) {
                for (String key : enchantSection.getKeys(false)) {
                    enchants.put(key, enchantSection.getInt(key));
                }
            }
            flags = metaSection.getStringList("flags");
        }
        int customModelData = plugin.getConfig().getInt("anarchiczneSerceCustomModelData", 0);
        return new AnarchiczneSerceDefinition(material, displayName, lore, enchants, flags, customModelData);
    }

    private CustomItemsConfig loadCustomItems() {
        ConfigurationSection section = plugin.getConfig().getConfigurationSection("customItems");
        Map<String, ConfigurationSection> eventItemSections = loadEventItems();
        return new CustomItemsConfig(section, eventItemSections);
    }

    private Map<String, ConfigurationSection> loadEventItems() {
        Map<String, ConfigurationSection> sections = new LinkedHashMap<>();
        File itemsDir = new File(plugin.getDataFolder(), "custom-items");
        if (!itemsDir.exists() || !itemsDir.isDirectory()) {
            return sections;
        }
        File[] files = itemsDir.listFiles((dir, name) -> name.toLowerCase(Locale.ROOT).endsWith(".yml"));
        if (files == null) {
            return sections;
        }
        for (File file : files) {
            YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
            if (yaml.getKeys(false).isEmpty()) {
                continue;
            }
            String key = yaml.getKeys(false).iterator().next();
            ConfigurationSection section = yaml.getConfigurationSection(key);
            if (section == null) {
                continue;
            }
            sections.put(key, section);
        }
        return sections;
    }

    public int getDefaultHearts() {
        return defaultHearts;
    }

    public int getMaxHearts() {
        return maxHearts;
    }

    public String getPrefix() {
        return prefix;
    }

    public Set<World.Environment> getBlockedCrystalDimensions() {
        return Collections.unmodifiableSet(blockedCrystalDimensions);
    }

    public Set<String> getBlockedCrystalWorldNames() {
        return Collections.unmodifiableSet(blockedCrystalWorldNames);
    }

    public boolean isCrystalBlocked(World world) {
        if (blockedCrystalDimensions.contains(world.getEnvironment())) {
            return true;
        }
        return blockedCrystalWorldNames.contains(world.getName().toLowerCase(Locale.ROOT));
    }

    public String getTrashTitle() {
        return trashTitle;
    }

    public int getTrashRows() {
        return trashRows;
    }

    public String getTrashClearedMessage() {
        return trashClearedMessage;
    }

    public AnarchiczneSerceDefinition getSerceDefinition() {
        return serceDefinition;
    }

    public CustomItemsConfig getCustomItemsConfig() {
        return customItemsConfig;
    }
}
