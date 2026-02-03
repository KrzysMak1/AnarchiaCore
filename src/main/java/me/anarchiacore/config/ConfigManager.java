package me.anarchiacore.config;

import me.anarchiacore.customitems.CustomItemDefinition;
import me.anarchiacore.customitems.CustomItemsConfig;
import me.anarchiacore.hearts.AnarchiczneSerceDefinition;
import me.anarchiacore.stats.MissingIpPolicy;
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
    private Set<World.Environment> blockedCrystalEnvironments = new LinkedHashSet<>();
    private String trashTitle;
    private int trashRows;
    private String trashClearedMessage;
    private AnarchiczneSerceDefinition serceDefinition;
    private CustomItemsConfig customItemsConfig;
    private int statsTopCacheSeconds;
    private int statsTopLimit;
    private int killCreditCooldownSeconds;
    private boolean killCreditIgnoreSameIp;
    private MissingIpPolicy killCreditOnMissingIp;

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void reload() {
        plugin.reloadConfig();
        defaultHearts = plugin.getConfig().getInt("defaultHearts", 30);
        maxHearts = plugin.getConfig().getInt("maxHearts", 40);
        prefix = plugin.getConfig().getString("messages.prefix", "");
        blockedCrystalEnvironments = new LinkedHashSet<>();
        List<String> rawDimensions = plugin.getConfig().getStringList("blockedEndCrystalDimensions");
        if (rawDimensions.isEmpty()) {
            rawDimensions = plugin.getConfig().getStringList("blockedEndCrystalEnvironments");
        }
        for (String envName : rawDimensions) {
            if (envName == null) {
                continue;
            }
            String token = envName.trim();
            if (token.isEmpty()) {
                continue;
            }
            try {
                blockedCrystalEnvironments.add(World.Environment.valueOf(token.toUpperCase(Locale.ROOT)));
            } catch (IllegalArgumentException ex) {
                plugin.getLogger().warning("Unknown environment in blockedEndCrystalDimensions: " + token);
            }
        }
        trashTitle = plugin.getConfig().getString("trash.title", "Kosz");
        trashRows = plugin.getConfig().getInt("trash.rows", 5);
        trashClearedMessage = plugin.getConfig().getString("trash.clearMessage");
        serceDefinition = loadSerceDefinition();
        customItemsConfig = loadCustomItems();
        loadStatsSettings();
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

    private void loadStatsSettings() {
        ConfigurationSection statsSection = plugin.getConfig().getConfigurationSection("stats");
        if (statsSection == null) {
            statsTopCacheSeconds = 60;
            statsTopLimit = 10;
            killCreditCooldownSeconds = 30;
            killCreditIgnoreSameIp = true;
            killCreditOnMissingIp = MissingIpPolicy.COUNT;
            return;
        }
        statsTopCacheSeconds = statsSection.getInt("topCacheSeconds", 60);
        statsTopLimit = Math.max(10, statsSection.getInt("topLimit", 10));
        ConfigurationSection killCreditSection = statsSection.getConfigurationSection("killCredit");
        if (killCreditSection == null) {
            killCreditCooldownSeconds = 30;
            killCreditIgnoreSameIp = true;
            killCreditOnMissingIp = MissingIpPolicy.COUNT;
            return;
        }
        killCreditCooldownSeconds = killCreditSection.getInt("sameVictimCooldownSeconds", 30);
        killCreditIgnoreSameIp = killCreditSection.getBoolean("ignoreSameIp", true);
        String onMissingIp = killCreditSection.getString("onMissingIp", "count");
        killCreditOnMissingIp = MissingIpPolicy.fromString(onMissingIp, MissingIpPolicy.COUNT);
    }

    private Map<String, ConfigurationSection> loadEventItems() {
        Map<String, ConfigurationSection> sections = new LinkedHashMap<>();
        File itemsDir = new File(plugin.getDataFolder(), "configs/customitems");
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
            String fileKey = file.getName().toLowerCase(Locale.ROOT);
            if (fileKey.endsWith(".yml")) {
                fileKey = fileKey.substring(0, fileKey.length() - 4);
            }
            if (!fileKey.isBlank()) {
                sections.putIfAbsent(fileKey, section);
            }
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
        return Collections.unmodifiableSet(blockedCrystalEnvironments);
    }

    public boolean isCrystalBlocked(World world) {
        return blockedCrystalEnvironments.contains(world.getEnvironment());
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

    public int getStatsTopCacheSeconds() {
        return statsTopCacheSeconds;
    }

    public int getStatsTopLimit() {
        return statsTopLimit;
    }

    public int getKillCreditCooldownSeconds() {
        return killCreditCooldownSeconds;
    }

    public boolean isKillCreditIgnoreSameIp() {
        return killCreditIgnoreSameIp;
    }

    public MissingIpPolicy getKillCreditOnMissingIp() {
        return killCreditOnMissingIp;
    }
}
