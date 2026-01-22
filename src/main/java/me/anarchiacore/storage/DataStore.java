package me.anarchiacore.storage;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class DataStore {
    private final JavaPlugin plugin;
    private final File file;
    private YamlConfiguration config;

    public DataStore(JavaPlugin plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "data.yml");
        reload();
    }

    public void reload() {
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
        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().severe("Nie można zapisać data.yml: " + e.getMessage());
        }
    }

    public int getHearts(UUID uuid, int defaultHearts) {
        return config.getInt("players." + uuid + ".hearts", defaultHearts);
    }

    public boolean hasHearts(UUID uuid) {
        return config.contains("players." + uuid + ".hearts");
    }

    public void setHearts(UUID uuid, int hearts) {
        config.set("players." + uuid + ".hearts", hearts);
    }

    public boolean getSpawnOnJoin(UUID uuid) {
        return config.getBoolean("players." + uuid + ".spawnOnJoin", false);
    }

    public void setSpawnOnJoin(UUID uuid, boolean value) {
        config.set("players." + uuid + ".spawnOnJoin", value);
    }

    public void clearSpawnOnJoin(UUID uuid) {
        config.set("players." + uuid + ".spawnOnJoin", null);
    }
}
