package me.anarchiacore.storage;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DataStore {
    private final JavaPlugin plugin;
    private final File file;
    private YamlConfiguration config;
    private StorageType storageType = StorageType.YAML;
    private HikariDataSource dataSource;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    public DataStore(JavaPlugin plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "data.yml");
        reload();
    }

    public void reload() {
        StorageConfig storageConfig = StorageConfig.from(plugin.getConfig());
        storageType = storageConfig.type();
        if (storageType == StorageType.MYSQL) {
            initDataSource(storageConfig);
            ensureMysqlSchema();
        } else {
            closeDataSource();
            lock.writeLock().lock();
            try {
                loadYaml();
            } finally {
                lock.writeLock().unlock();
            }
        }
    }

    private void loadYaml() {
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe("Nie można utworzyć data.yml: " + e.getMessage());
            }
        }
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public void save() {
        if (storageType == StorageType.MYSQL) {
            return;
        }
        lock.writeLock().lock();
        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().severe("Nie można zapisać data.yml: " + e.getMessage());
        } finally {
            lock.writeLock().unlock();
        }
    }

    public int getHearts(UUID uuid, int defaultHearts) {
        if (storageType == StorageType.MYSQL) {
            if (dataSource == null) {
                return defaultHearts;
            }
            String sql = "SELECT hearts FROM hearts WHERE uuid = ?";
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, uuid.toString());
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt("hearts");
                    }
                }
            } catch (SQLException ex) {
                plugin.getLogger().severe("Nie można odczytać serc z MySQL: " + ex.getMessage());
            }
            return defaultHearts;
        }
        lock.readLock().lock();
        try {
            return config.getInt("players." + uuid + ".hearts", defaultHearts);
        } finally {
            lock.readLock().unlock();
        }
    }

    public boolean hasHearts(UUID uuid) {
        if (storageType == StorageType.MYSQL) {
            if (dataSource == null) {
                return false;
            }
            String sql = "SELECT 1 FROM hearts WHERE uuid = ?";
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, uuid.toString());
                try (ResultSet resultSet = statement.executeQuery()) {
                    return resultSet.next();
                }
            } catch (SQLException ex) {
                plugin.getLogger().severe("Nie można sprawdzić serc w MySQL: " + ex.getMessage());
                return false;
            }
        }
        lock.readLock().lock();
        try {
            return config.contains("players." + uuid + ".hearts");
        } finally {
            lock.readLock().unlock();
        }
    }

    public void setHearts(UUID uuid, int hearts) {
        if (storageType == StorageType.MYSQL) {
            if (dataSource == null) {
                return;
            }
            String sql = """
                INSERT INTO hearts (uuid, hearts, spawn_on_join)
                VALUES (?, ?, COALESCE((SELECT spawn_on_join FROM hearts WHERE uuid = ?), false))
                ON DUPLICATE KEY UPDATE hearts = VALUES(hearts)
                """;
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, uuid.toString());
                statement.setInt(2, hearts);
                statement.setString(3, uuid.toString());
                statement.executeUpdate();
            } catch (SQLException ex) {
                plugin.getLogger().severe("Nie można zapisać serc do MySQL: " + ex.getMessage());
            }
            return;
        }
        lock.writeLock().lock();
        try {
            config.set("players." + uuid + ".hearts", hearts);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public boolean getSpawnOnJoin(UUID uuid) {
        if (storageType == StorageType.MYSQL) {
            if (dataSource == null) {
                return false;
            }
            String sql = "SELECT spawn_on_join FROM hearts WHERE uuid = ?";
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, uuid.toString());
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getBoolean("spawn_on_join");
                    }
                }
            } catch (SQLException ex) {
                plugin.getLogger().severe("Nie można odczytać flagi spawnOnJoin z MySQL: " + ex.getMessage());
            }
            return false;
        }
        lock.readLock().lock();
        try {
            return config.getBoolean("players." + uuid + ".spawnOnJoin", false);
        } finally {
            lock.readLock().unlock();
        }
    }

    public void setSpawnOnJoin(UUID uuid, boolean value) {
        if (storageType == StorageType.MYSQL) {
            if (dataSource == null) {
                return;
            }
            String sql = """
                INSERT INTO hearts (uuid, hearts, spawn_on_join)
                VALUES (?, ?, ?)
                ON DUPLICATE KEY UPDATE spawn_on_join = VALUES(spawn_on_join)
                """;
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, uuid.toString());
                statement.setInt(2, getHearts(uuid, 0));
                statement.setBoolean(3, value);
                statement.executeUpdate();
            } catch (SQLException ex) {
                plugin.getLogger().severe("Nie można zapisać flagi spawnOnJoin do MySQL: " + ex.getMessage());
            }
            return;
        }
        lock.writeLock().lock();
        try {
            config.set("players." + uuid + ".spawnOnJoin", value);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void clearSpawnOnJoin(UUID uuid) {
        if (storageType == StorageType.MYSQL) {
            setSpawnOnJoin(uuid, false);
            return;
        }
        lock.writeLock().lock();
        try {
            config.set("players." + uuid + ".spawnOnJoin", null);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void setValue(String path, Object value) {
        if (storageType == StorageType.MYSQL) {
            return;
        }
        lock.writeLock().lock();
        try {
            config.set(path, value);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public Object getValue(String path) {
        if (storageType == StorageType.MYSQL) {
            return null;
        }
        lock.readLock().lock();
        try {
            return config.get(path);
        } finally {
            lock.readLock().unlock();
        }
    }

    public long getLong(String path, long defaultValue) {
        if (storageType == StorageType.MYSQL) {
            return defaultValue;
        }
        lock.readLock().lock();
        try {
            return config.getLong(path, defaultValue);
        } finally {
            lock.readLock().unlock();
        }
    }

    public ConfigurationSection getSection(String path) {
        if (storageType == StorageType.MYSQL) {
            return null;
        }
        lock.readLock().lock();
        try {
            return config.getConfigurationSection(path);
        } finally {
            lock.readLock().unlock();
        }
    }

    public StorageType getStorageType() {
        return storageType;
    }

    public HikariDataSource getDataSource() {
        return dataSource;
    }

    public void close() {
        closeDataSource();
    }

    private void initDataSource(StorageConfig storageConfig) {
        closeDataSource();
        HikariConfig hikariConfig = new HikariConfig();
        String jdbc = String.format(Locale.ROOT, "jdbc:mysql://%s:%d/%s?useSSL=%s&characterEncoding=utf8",
            storageConfig.host(), storageConfig.port(), storageConfig.database(), storageConfig.useSSL());
        hikariConfig.setJdbcUrl(jdbc);
        hikariConfig.setUsername(storageConfig.user());
        hikariConfig.setPassword(storageConfig.password());
        hikariConfig.setMaximumPoolSize(storageConfig.poolSize());
        hikariConfig.setPoolName("AnarchiaCore");
        dataSource = new HikariDataSource(hikariConfig);
    }

    private void closeDataSource() {
        if (dataSource != null) {
            dataSource.close();
            dataSource = null;
        }
    }

    private void ensureMysqlSchema() {
        if (dataSource == null) {
            return;
        }
        String sql = """
            CREATE TABLE IF NOT EXISTS hearts (
                uuid VARCHAR(36) PRIMARY KEY,
                hearts INT NOT NULL,
                spawn_on_join BOOLEAN NOT NULL DEFAULT FALSE
            )
            """;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.execute();
        } catch (SQLException ex) {
            plugin.getLogger().severe("Nie można utworzyć tabeli hearts: " + ex.getMessage());
        }
    }
}
