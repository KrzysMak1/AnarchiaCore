package me.anarchiacore.combatlog;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CombatLogStorage {
    private final JavaPlugin plugin;
    private final File file;
    private YamlConfiguration config;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

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
        lock.writeLock().lock();
        try {
            this.config = YamlConfiguration.loadConfiguration(file);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void save() {
        lock.writeLock().lock();
        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().severe("Nie można zapisać combatlog-data.yml: " + e.getMessage());
        } finally {
            lock.writeLock().unlock();
        }
    }

    public boolean hasJoined(UUID uuid) {
        lock.readLock().lock();
        try {
            return config.getBoolean("players." + uuid + ".joined", false);
        } finally {
            lock.readLock().unlock();
        }
    }

    public void setJoined(UUID uuid) {
        lock.writeLock().lock();
        try {
            config.set("players." + uuid + ".joined", true);
        } finally {
            lock.writeLock().unlock();
        }
    }
}
