package me.anarchiacore.stats;

import me.anarchiacore.config.ConfigManager;
import me.anarchiacore.storage.DataStore;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class StatsManager implements Listener {
    private final ConfigManager configManager;
    private final StatsStorage storage;
    private final Map<UUID, Map<UUID, Long>> killCreditCooldowns = new ConcurrentHashMap<>();
    private final TopCacheManager topCacheManager;

    public StatsManager(JavaPlugin plugin, ConfigManager configManager, DataStore dataStore) {
        this.configManager = configManager;
        this.storage = new StatsStorage(plugin, dataStore);
        this.topCacheManager = new TopCacheManager(plugin, configManager, storage);
        this.topCacheManager.start();
    }

    public void reload() {
        storage.reload();
        killCreditCooldowns.clear();
        topCacheManager.reload();
    }

    public void stop() {
        topCacheManager.stop();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (storage.updateName(player.getUniqueId(), player.getName())) {
            storage.savePlayer(player.getUniqueId());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player killer = victim.getKiller();
        boolean credited = killer != null && isKillCreditAllowed(victim, killer);
        PlayerStats victimStats = storage.getOrCreate(victim.getUniqueId(), victim.getName());
        victimStats.setDeaths(victimStats.getDeaths() + 1);
        victimStats.setKillstreak(0);
        if (credited) {
            PlayerStats killerStats = storage.getOrCreate(killer.getUniqueId(), killer.getName());
            killerStats.setKills(killerStats.getKills() + 1);
            int newStreak = killerStats.getKillstreak() + 1;
            killerStats.setKillstreak(newStreak);
            if (newStreak > killerStats.getBestKillstreak()) {
                killerStats.setBestKillstreak(newStreak);
            }
            storage.savePlayer(killer.getUniqueId());
        }
        storage.savePlayer(victim.getUniqueId());
    }

    private boolean isKillCreditAllowed(Player victim, Player attacker) {
        if (victim.getUniqueId().equals(attacker.getUniqueId())) {
            return false;
        }
        if (!checkSameVictimCooldown(attacker.getUniqueId(), victim.getUniqueId())) {
            return false;
        }
        if (!configManager.isKillCreditIgnoreSameIp()) {
            return true;
        }
        String attackerIp = readIp(attacker);
        String victimIp = readIp(victim);
        if (victimIp == null || attackerIp == null) {
            return configManager.getKillCreditOnMissingIp() == MissingIpPolicy.COUNT;
        }
        return !victimIp.equals(attackerIp);
    }

    private boolean checkSameVictimCooldown(UUID killer, UUID victim) {
        int cooldownSeconds = Math.max(0, configManager.getKillCreditCooldownSeconds());
        if (cooldownSeconds == 0) {
            return true;
        }
        long now = System.currentTimeMillis();
        Map<UUID, Long> killerMap = killCreditCooldowns.computeIfAbsent(killer, key -> new ConcurrentHashMap<>());
        Long last = killerMap.get(victim);
        if (last != null && (now - last) < cooldownSeconds * 1000L) {
            return false;
        }
        killerMap.put(victim, now);
        return true;
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
}
