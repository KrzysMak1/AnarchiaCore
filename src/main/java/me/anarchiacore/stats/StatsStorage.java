package me.anarchiacore.stats;

import com.zaxxer.hikari.HikariDataSource;
import me.anarchiacore.storage.DataStore;
import me.anarchiacore.storage.StorageType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StatsStorage {
    private final JavaPlugin plugin;
    private final DataStore dataStore;
    private final File statsFile;
    private YamlConfiguration statsConfig;
    private StorageType storageType;
    private HikariDataSource dataSource;
    private final Map<UUID, PlayerStats> statsByPlayer = new HashMap<>();

    public StatsStorage(JavaPlugin plugin, DataStore dataStore) {
        this.plugin = plugin;
        this.dataStore = dataStore;
        this.statsFile = new File(plugin.getDataFolder(), "stats.yml");
        reload();
    }

    public void reload() {
        this.storageType = dataStore.getStorageType();
        this.dataSource = dataStore.getDataSource();
        ensureStatsFile();
        statsConfig = YamlConfiguration.loadConfiguration(statsFile);
        statsByPlayer.clear();
        if (storageType == StorageType.MYSQL) {
            ensureMysqlSchema();
            loadFromDatabase();
        } else {
            loadSection(dataStore.getSection("stats"));
            loadSection(dataStore.getSection("stats.players"));
            loadSection(statsConfig.getConfigurationSection("stats"));
            loadSection(statsConfig.getConfigurationSection("players"));
        }
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

    public void savePlayer(UUID uuid) {
        if (uuid == null) {
            return;
        }
        PlayerStats stats = statsByPlayer.get(uuid);
        if (stats == null) {
            return;
        }
        if (storageType == StorageType.MYSQL) {
            savePlayerMysql(uuid, stats);
        } else {
            savePlayerYaml(uuid, stats);
        }
    }

    private void savePlayerYaml(UUID uuid, PlayerStats stats) {
        String basePath = "stats." + uuid;
        statsConfig.set(basePath + ".name", stats.getName());
        statsConfig.set(basePath + ".kills", stats.getKills());
        statsConfig.set(basePath + ".deaths", stats.getDeaths());
        statsConfig.set(basePath + ".killstreak", stats.getKillstreak());
        statsConfig.set(basePath + ".bestKillstreak", stats.getBestKillstreak());
        if (dataStore.getStorageType() == StorageType.YAML) {
            String dataPath = "stats." + uuid;
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

    private void savePlayerMysql(UUID uuid, PlayerStats stats) {
        if (dataSource == null) {
            return;
        }
        String sql = """
            INSERT INTO stats (uuid, name, kills, deaths, killstreak, bestKillstreak)
            VALUES (?, ?, ?, ?, ?, ?)
            ON DUPLICATE KEY UPDATE
                name = VALUES(name),
                kills = VALUES(kills),
                deaths = VALUES(deaths),
                killstreak = VALUES(killstreak),
                bestKillstreak = VALUES(bestKillstreak)
            """;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, uuid.toString());
            statement.setString(2, stats.getName());
            statement.setInt(3, stats.getKills());
            statement.setInt(4, stats.getDeaths());
            statement.setInt(5, stats.getKillstreak());
            statement.setInt(6, stats.getBestKillstreak());
            statement.executeUpdate();
        } catch (SQLException ex) {
            plugin.getLogger().severe("Nie można zapisać statystyk do MySQL: " + ex.getMessage());
        }
    }

    private void ensureMysqlSchema() {
        if (dataSource == null) {
            return;
        }
        String sql = """
            CREATE TABLE IF NOT EXISTS stats (
                uuid VARCHAR(36) PRIMARY KEY,
                name VARCHAR(64),
                kills INT NOT NULL,
                deaths INT NOT NULL,
                killstreak INT NOT NULL,
                bestKillstreak INT NOT NULL
            )
            """;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.execute();
        } catch (SQLException ex) {
            plugin.getLogger().severe("Nie można utworzyć tabeli stats: " + ex.getMessage());
        }
    }

    private void loadFromDatabase() {
        if (dataSource == null) {
            return;
        }
        String sql = "SELECT uuid, name, kills, deaths, killstreak, bestKillstreak FROM stats";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                UUID uuid = UUID.fromString(resultSet.getString("uuid"));
                String name = resultSet.getString("name");
                int kills = resultSet.getInt("kills");
                int deaths = resultSet.getInt("deaths");
                int killstreak = resultSet.getInt("killstreak");
                int bestKillstreak = resultSet.getInt("bestKillstreak");
                statsByPlayer.put(uuid, new PlayerStats(name, kills, deaths, killstreak, bestKillstreak));
            }
        } catch (SQLException ex) {
            plugin.getLogger().severe("Nie można wczytać statystyk z MySQL: " + ex.getMessage());
        }
    }
}
