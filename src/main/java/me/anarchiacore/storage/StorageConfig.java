package me.anarchiacore.storage;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public record StorageConfig(
    StorageType type,
    String host,
    int port,
    String database,
    String user,
    String password,
    boolean useSSL,
    int poolSize
) {
    public static StorageConfig from(FileConfiguration configuration) {
        if (configuration == null) {
            return defaultYaml();
        }
        ConfigurationSection section = configuration.getConfigurationSection("storage");
        if (section == null) {
            return defaultYaml();
        }
        String typeValue = section.getString("type", "YAML");
        StorageType type = StorageType.YAML;
        if (typeValue != null && typeValue.trim().equalsIgnoreCase("MYSQL")) {
            type = StorageType.MYSQL;
        }
        ConfigurationSection mysql = section.getConfigurationSection("mysql");
        String host = mysql != null ? mysql.getString("host", "localhost") : "localhost";
        int port = mysql != null ? mysql.getInt("port", 3306) : 3306;
        String database = mysql != null ? mysql.getString("database", "anarchiacore") : "anarchiacore";
        String user = mysql != null ? mysql.getString("user", "root") : "root";
        String password = mysql != null ? mysql.getString("password", "") : "";
        boolean useSSL = mysql != null && mysql.getBoolean("useSSL", false);
        int poolSize = mysql != null ? mysql.getInt("poolSize", 10) : 10;
        return new StorageConfig(type, host, port, database, user, password, useSSL, poolSize);
    }

    private static StorageConfig defaultYaml() {
        return new StorageConfig(StorageType.YAML, "localhost", 3306, "anarchiacore", "root", "", false, 10);
    }
}
