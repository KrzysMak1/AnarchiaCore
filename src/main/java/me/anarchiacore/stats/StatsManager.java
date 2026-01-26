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
import org.bukkit.plugin.java.JavaPlugin;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class StatsManager implements Listener {
    private final JavaPlugin plugin;
    private final ConfigManager configManager;
    private final StatsStorage storage;
    private final Map<UUID, LastHit> lastHits = new java.util.HashMap<>();
    private final EnumMap<TopType, TopCache> topCache = new EnumMap<>(TopType.class);

    public StatsManager(JavaPlugin plugin, ConfigManager configManager, DataStore dataStore) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.storage = new StatsStorage(plugin, dataStore);
    }

    public void reload() {
        storage.reload();
        topCache.clear();
        lastHits.clear();
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
        topCache.clear();
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

    public List<TopEntry> getTop(TopType type) {
        TopCache cache = topCache.get(type);
        long now = System.currentTimeMillis();
        if (cache != null && now - cache.refreshedAt() < configManager.getStatsTopCacheSeconds() * 1000L) {
            return cache.entries();
        }
        List<TopEntry> entries = new ArrayList<>();
        for (Map.Entry<UUID, PlayerStats> entry : storage.getAllStats().entrySet()) {
            PlayerStats stats = entry.getValue();
            int value = switch (type) {
                case KILLS -> stats.getKills();
                case DEATHS -> stats.getDeaths();
                case KILLSTREAK -> stats.getKillstreak();
                case BEST_KILLSTREAK -> stats.getBestKillstreak();
            };
            if (value <= 0) {
                continue;
            }
            entries.add(new TopEntry(entry.getKey(), stats.getName(), value));
        }
        entries.sort(Comparator.comparingInt(TopEntry::value).reversed().thenComparing(TopEntry::name, Comparator.nullsLast(String::compareToIgnoreCase)));
        int limit = Math.max(1, configManager.getStatsTopLimit());
        if (entries.size() > limit) {
            entries = entries.subList(0, limit);
        }
        TopCache refreshed = new TopCache(now, List.copyOf(entries));
        topCache.put(type, refreshed);
        return refreshed.entries();
    }

    private record LastHit(UUID attacker, long timestamp, String ip) {
    }

    public enum TopType {
        KILLS,
        DEATHS,
        KILLSTREAK,
        BEST_KILLSTREAK
    }

    public record TopEntry(UUID uuid, String name, int value) {
    }

    private record TopCache(long refreshedAt, List<TopEntry> entries) {
    }
}
