package me.anarchiacore.stats;

import me.anarchiacore.config.ConfigManager;
import me.anarchiacore.storage.DataStore;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.UUID;

public class StatsManager implements Listener {
    private final JavaPlugin plugin;
    private final ConfigManager configManager;
    private final StatsStorage storage;
    private final Map<UUID, LastHit> lastHits = new java.util.HashMap<>();
    private final TopCacheManager topCacheManager;

    public StatsManager(JavaPlugin plugin, ConfigManager configManager, DataStore dataStore) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.storage = new StatsStorage(plugin, dataStore);
        this.topCacheManager = new TopCacheManager(plugin, configManager, storage);
        this.topCacheManager.start();
    }

    public void reload() {
        storage.reload();
        lastHits.clear();
        topCacheManager.reload();
    }

    public void stop() {
        topCacheManager.stop();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (storage.updateName(player.getUniqueId(), player.getName())) {
            storage.save();
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player victim)) {
            return;
        }
        Player attacker = resolveAttacker(event.getDamager());
        if (attacker == null || attacker.getUniqueId().equals(victim.getUniqueId())) {
            return;
        }
        lastHits.put(victim.getUniqueId(), new LastHit(attacker.getUniqueId(), System.currentTimeMillis(), readIp(attacker)));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player creditedKiller = resolveKiller(victim);
        PlayerStats victimStats = storage.getOrCreate(victim.getUniqueId(), victim.getName());
        victimStats.setDeaths(victimStats.getDeaths() + 1);
        victimStats.setKillstreak(0);
        if (creditedKiller != null) {
            PlayerStats killerStats = storage.getOrCreate(creditedKiller.getUniqueId(), creditedKiller.getName());
            killerStats.setKills(killerStats.getKills() + 1);
            int newStreak = killerStats.getKillstreak() + 1;
            killerStats.setKillstreak(newStreak);
            if (newStreak > killerStats.getBestKillstreak()) {
                killerStats.setBestKillstreak(newStreak);
            }
        }
        storage.save();
        lastHits.remove(victim.getUniqueId());
    }

    private Player resolveKiller(Player victim) {
        Player killer = victim.getKiller();
        if (killer != null && isKillCreditAllowed(victim, killer, readIp(killer))) {
            return killer;
        }
        LastHit lastHit = lastHits.get(victim.getUniqueId());
        if (lastHit == null) {
            return null;
        }
        long ageMillis = System.currentTimeMillis() - lastHit.timestamp();
        if (ageMillis > configManager.getKillCreditCooldownSeconds() * 1000L) {
            return null;
        }
        Player attacker = Bukkit.getPlayer(lastHit.attacker());
        if (attacker == null || !attacker.isOnline()) {
            return null;
        }
        if (!isKillCreditAllowed(victim, attacker, lastHit.ip())) {
            return null;
        }
        return attacker;
    }

    private boolean isKillCreditAllowed(Player victim, Player attacker, String attackerIp) {
        if (victim.getUniqueId().equals(attacker.getUniqueId())) {
            return false;
        }
        if (!configManager.isKillCreditIgnoreSameIp()) {
            return true;
        }
        String victimIp = readIp(victim);
        if (victimIp == null || attackerIp == null) {
            return configManager.getKillCreditOnMissingIp() == MissingIpPolicy.ALLOW;
        }
        return !victimIp.equals(attackerIp);
    }

    private Player resolveAttacker(Entity damager) {
        if (damager instanceof Player player) {
            return player;
        }
        if (damager instanceof Projectile projectile) {
            if (projectile.getShooter() instanceof Player shooter) {
                return shooter;
            }
        }
        return null;
    }

    private String readIp(Player player) {
        InetSocketAddress address = player.getAddress();
        if (address == null) {
            return null;
        }
        if (address.getAddress() == null) {
            return null;
        }
        return address.getAddress().getHostAddress();
    }

    public PlayerStats getStats(UUID uuid) {
        return storage.getAllStats().get(uuid);
    }

    public TopCacheManager getTopCacheManager() {
        return topCacheManager;
    }

    private record LastHit(UUID attacker, long timestamp, String ip) {
    }
}
