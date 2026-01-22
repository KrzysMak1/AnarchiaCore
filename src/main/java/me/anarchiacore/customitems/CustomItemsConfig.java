package me.anarchiacore.customitems;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.util.*;

public class CustomItemsConfig {
    private final Map<String, CustomItemDefinition> items = new LinkedHashMap<>();
    private final double bombardaSpeed;
    private final float bombardaPower;
    private final int bombardaLifeTimeTicks;
    private final boolean bombardaBreakBlocks;
    private final boolean bombardaSetFire;
    private final int dynamitFuseTicks;
    private final float dynamitPower;
    private final boolean dynamitBreakBlocks;
    private final List<String> turboTrapBlocks;
    private final WorldGuardRules worldGuardRules;

    public CustomItemsConfig(ConfigurationSection section) {
        if (section == null) {
            bombardaSpeed = 1.5;
            bombardaPower = 4.0f;
            bombardaLifeTimeTicks = 60;
            bombardaBreakBlocks = true;
            bombardaSetFire = false;
            dynamitFuseTicks = 40;
            dynamitPower = 4.0f;
            dynamitBreakBlocks = true;
            turboTrapBlocks = List.of();
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
                items.put(key.toLowerCase(Locale.ROOT), new CustomItemDefinition(key, material, displayName, lore, enchants, flags, cmd));
            }
        }
        ConfigurationSection bombardaSection = section.getConfigurationSection("bombardaMaxima");
        bombardaSpeed = bombardaSection != null ? bombardaSection.getDouble("speed", 1.5) : 1.5;
        bombardaPower = bombardaSection != null ? (float) bombardaSection.getDouble("power", 4.0) : 4.0f;
        bombardaLifeTimeTicks = bombardaSection != null ? bombardaSection.getInt("lifeTimeTicks", 60) : 60;
        bombardaBreakBlocks = bombardaSection == null || bombardaSection.getBoolean("breakBlocks", true);
        bombardaSetFire = bombardaSection != null && bombardaSection.getBoolean("setFire", false);

        ConfigurationSection dynamitSection = section.getConfigurationSection("dynamit");
        dynamitFuseTicks = dynamitSection != null ? dynamitSection.getInt("fuseTicks", 40) : 40;
        dynamitPower = dynamitSection != null ? (float) dynamitSection.getDouble("power", 4.0) : 4.0f;
        dynamitBreakBlocks = dynamitSection == null || dynamitSection.getBoolean("breakBlocks", true);

        ConfigurationSection trapSection = section.getConfigurationSection("turboTrap");
        turboTrapBlocks = trapSection != null ? trapSection.getStringList("blocks") : List.of();

        ConfigurationSection wgSection = section.getConfigurationSection("worldguard");
        boolean wgEnabled = wgSection != null && wgSection.getBoolean("enabled", true);
        List<String> allowAll = wgSection != null ? wgSection.getStringList("allowAllEventItemsRegions") : List.of();
        List<String> allowExceptExplosives = wgSection != null ? wgSection.getStringList("allowEventItemsExceptExplosivesRegions") : List.of();
        worldGuardRules = new WorldGuardRules(wgEnabled, allowAll, allowExceptExplosives);
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

    public double getBombardaSpeed() {
        return bombardaSpeed;
    }

    public float getBombardaPower() {
        return bombardaPower;
    }

    public int getBombardaLifeTimeTicks() {
        return bombardaLifeTimeTicks;
    }

    public boolean isBombardaBreakBlocks() {
        return bombardaBreakBlocks;
    }

    public boolean isBombardaSetFire() {
        return bombardaSetFire;
    }

    public int getDynamitFuseTicks() {
        return dynamitFuseTicks;
    }

    public float getDynamitPower() {
        return dynamitPower;
    }

    public boolean isDynamitBreakBlocks() {
        return dynamitBreakBlocks;
    }

    public List<String> getTurboTrapBlocks() {
        return turboTrapBlocks;
    }

    public WorldGuardRules getWorldGuardRules() {
        return worldGuardRules;
    }

    public record WorldGuardRules(boolean enabled, List<String> allowAllRegions, List<String> allowExceptExplosivesRegions) {
    }
}
