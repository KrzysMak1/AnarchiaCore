package me.anarchiacore.customhits;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class CustomHitManager implements Listener {
    private final JavaPlugin plugin;
    private boolean enabled;
    private boolean useStormItemy;
    private double multiplier;
    private double fallbackMultiplier;
    private String stormItemyConfigPath;

    public CustomHitManager(JavaPlugin plugin) {
        this.plugin = plugin;
        reload();
    }

    public void reload() {
        enabled = plugin.getConfig().getBoolean("customHits.enabled", false);
        useStormItemy = plugin.getConfig().getBoolean("customHits.useStormItemy", true);
        fallbackMultiplier = plugin.getConfig().getDouble("customHits.fallbackMultiplier", 1.0);
        stormItemyConfigPath = plugin.getConfig().getString("customHits.stormItemyConfigPath", "configs/STORMITEMY/config.yml");
        multiplier = fallbackMultiplier;
        if (!enabled || !useStormItemy) {
            return;
        }
        if (stormItemyConfigPath == null || stormItemyConfigPath.isBlank()) {
            return;
        }
        File stormConfig = new File(plugin.getDataFolder(), stormItemyConfigPath);
        if (!stormConfig.exists()) {
            return;
        }
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(stormConfig);
        multiplier = yaml.getDouble("customhit.damage_multiplier", fallbackMultiplier);
        if (multiplier < 0.0) {
            multiplier = 0.0;
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!enabled) {
            return;
        }
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        Player attacker = extractPlayer(event.getDamager());
        if (attacker == null) {
            return;
        }
        if (multiplier == 1.0) {
            return;
        }
        double baseDamage = event.getDamage();
        event.setDamage(baseDamage * multiplier);
    }

    private Player extractPlayer(Entity damager) {
        if (damager instanceof Player player) {
            return player;
        }
        if (damager instanceof Projectile projectile && projectile.getShooter() instanceof Player player) {
            return player;
        }
        return null;
    }
}
