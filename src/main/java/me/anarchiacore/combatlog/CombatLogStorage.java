package me.anarchiacore.combatlog;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class CombatLogStorage {
    private final JavaPlugin plugin;
    private final File file;
    private YamlConfiguration config;

    public CombatLogStorage(JavaPlugin plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "combatlog-data.yml");
        reload();
    }

    public void reload() {
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe("Nie można utworzyć combatlog-data.yml: " + e.getMessage());
            }
        }
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().severe("Nie można zapisać combatlog-data.yml: " + e.getMessage());
        }
    }

    public boolean hasJoined(UUID uuid) {
        return config.getBoolean("players." + uuid + ".joined", false);
    }

    public void setJoined(UUID uuid) {
        config.set("players." + uuid + ".joined", true);
    }
}
