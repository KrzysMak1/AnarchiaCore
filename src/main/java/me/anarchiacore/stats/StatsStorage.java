package me.anarchiacore.stats;

import me.anarchiacore.storage.DataStore;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StatsStorage {
    private final JavaPlugin plugin;
    private final DataStore dataStore;
    private final File statsFile;
    private YamlConfiguration statsConfig;
    private final Map<UUID, PlayerStats> statsByPlayer = new HashMap<>();

    public StatsStorage(JavaPlugin plugin, DataStore dataStore) {
        this.plugin = plugin;
        this.dataStore = dataStore;
        this.statsFile = new File(plugin.getDataFolder(), "stats.yml");
        reload();
    }

    public void reload() {
        ensureStatsFile();
        statsConfig = YamlConfiguration.loadConfiguration(statsFile);
        statsByPlayer.clear();
        loadSection(dataStore.getSection("stats"));
        loadSection(dataStore.getSection("stats.players"));
        loadSection(statsConfig.getConfigurationSection("stats"));
        loadSection(statsConfig.getConfigurationSection("players"));
    }

    private void ensureStatsFile() {
        if (statsFile.exists()) {
            return;
        }
        try {
            if (statsFile.getParentFile() != null) {
                statsFile.getParentFile().mkdirs();
            }
            statsFile.createNewFile();
        } catch (IOException e) {
            plugin.getLogger().severe("Nie można utworzyć stats.yml: " + e.getMessage());
        }
    }

    private void loadSection(ConfigurationSection section) {
        if (section == null) {
            return;
        }
        for (String key : section.getKeys(false)) {
            UUID uuid;
            try {
                uuid = UUID.fromString(key);
            } catch (IllegalArgumentException ex) {
                continue;
            }
            ConfigurationSection playerSection = section.getConfigurationSection(key);
            if (playerSection == null) {
                continue;
            }
            String name = playerSection.getString("name");
            int kills = playerSection.getInt("kills", 0);
            int deaths = playerSection.getInt("deaths", 0);
            int killstreak = playerSection.getInt("killstreak", 0);
            int bestKillstreak = playerSection.getInt("bestKillstreak", 0);
            statsByPlayer.put(uuid, new PlayerStats(name, kills, deaths, killstreak, bestKillstreak));
        }
    }

    public PlayerStats getOrCreate(UUID uuid, String name) {
        PlayerStats stats = statsByPlayer.get(uuid);
        if (stats == null) {
            stats = new PlayerStats(name, 0, 0, 0, 0);
            statsByPlayer.put(uuid, stats);
        }
        if (name != null && !name.isBlank() && (stats.getName() == null || !stats.getName().equals(name))) {
            stats.setName(name);
        }
        return stats;
    }

    public boolean updateName(UUID uuid, String name) {
        if (name == null || name.isBlank()) {
            return false;
        }
        PlayerStats stats = statsByPlayer.get(uuid);
        if (stats == null) {
            statsByPlayer.put(uuid, new PlayerStats(name, 0, 0, 0, 0));
            return true;
        }
        if (!name.equals(stats.getName())) {
            stats.setName(name);
            return true;
        }
        return false;
    }

    public Map<UUID, PlayerStats> getAllStats() {
        return Collections.unmodifiableMap(statsByPlayer);
    }

    public void save() {
        statsConfig = new YamlConfiguration();
        statsConfig.set("stats", null);
        for (Map.Entry<UUID, PlayerStats> entry : statsByPlayer.entrySet()) {
            String basePath = "stats." + entry.getKey();
            PlayerStats stats = entry.getValue();
            statsConfig.set(basePath + ".name", stats.getName());
            statsConfig.set(basePath + ".kills", stats.getKills());
            statsConfig.set(basePath + ".deaths", stats.getDeaths());
            statsConfig.set(basePath + ".killstreak", stats.getKillstreak());
            statsConfig.set(basePath + ".bestKillstreak", stats.getBestKillstreak());
            String dataPath = "stats." + entry.getKey();
            dataStore.setValue(dataPath + ".name", stats.getName());
            dataStore.setValue(dataPath + ".kills", stats.getKills());
            dataStore.setValue(dataPath + ".deaths", stats.getDeaths());
            dataStore.setValue(dataPath + ".killstreak", stats.getKillstreak());
            dataStore.setValue(dataPath + ".bestKillstreak", stats.getBestKillstreak());
        }
        try {
            statsConfig.save(statsFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Nie można zapisać stats.yml: " + e.getMessage());
        }
        dataStore.save();
    }
}
