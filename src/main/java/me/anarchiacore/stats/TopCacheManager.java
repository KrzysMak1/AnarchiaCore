package me.anarchiacore.stats;

import me.anarchiacore.config.ConfigManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class TopCacheManager {
    private final JavaPlugin plugin;
    private final ConfigManager configManager;
    private final StatsStorage storage;
    private final EnumMap<TopType, List<TopEntry>> topCache = new EnumMap<>(TopType.class);
    private BukkitTask task;

    public TopCacheManager(JavaPlugin plugin, ConfigManager configManager, StatsStorage storage) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.storage = storage;
    }

    public void start() {
        stop();
        refreshCache();
        int seconds = Math.max(1, configManager.getStatsTopCacheSeconds());
        long period = seconds * 20L;
        task = plugin.getServer().getScheduler().runTaskTimer(plugin, this::refreshCache, period, period);
    }

    public void stop() {
        if (task != null) {
            task.cancel();
            task = null;
        }
    }

    public void reload() {
        start();
    }

    public TopEntry getTopEntry(TopType type, int position) {
        if (position < 1) {
            return null;
        }
        int limit = Math.max(10, configManager.getStatsTopLimit());
        if (position > limit) {
            return null;
        }
        List<TopEntry> entries = topCache.get(type);
        if (entries == null || position > entries.size()) {
            return null;
        }
        return entries.get(position - 1);
    }

    public void refreshCache() {
        EnumMap<TopType, List<TopEntry>> newCache = new EnumMap<>(TopType.class);
        for (TopType type : TopType.values()) {
            newCache.put(type, new ArrayList<>());
        }
        for (Map.Entry<UUID, PlayerStats> entry : storage.getAllStats().entrySet()) {
            UUID uuid = entry.getKey();
            PlayerStats stats = entry.getValue();
            String name = normalizeName(stats.getName());
            long kills = stats.getKills();
            long deaths = stats.getDeaths();
            long killstreak = stats.getKillstreak();
            long bestKillstreak = stats.getBestKillstreak();
            double kdr = calculateKdr(kills, deaths);
            newCache.get(TopType.KILLS).add(new TopEntry(uuid, name, kills, String.valueOf(kills)));
            newCache.get(TopType.DEATHS).add(new TopEntry(uuid, name, deaths, String.valueOf(deaths)));
            newCache.get(TopType.KILLSTREAK).add(new TopEntry(uuid, name, killstreak, String.valueOf(killstreak)));
            newCache.get(TopType.BEST_KILLSTREAK).add(new TopEntry(uuid, name, bestKillstreak, String.valueOf(bestKillstreak)));
            newCache.get(TopType.KDR).add(new TopEntry(uuid, name, kdr, formatKdr(kdr)));
        }
        Comparator<TopEntry> comparator = Comparator.comparingDouble(TopEntry::valueNumber)
            .reversed()
            .thenComparing(TopEntry::name, String.CASE_INSENSITIVE_ORDER);
        for (Map.Entry<TopType, List<TopEntry>> entry : newCache.entrySet()) {
            List<TopEntry> entries = entry.getValue();
            entries.sort(comparator);
            int limit = Math.max(10, configManager.getStatsTopLimit());
            if (entries.size() > limit) {
                entries = entries.subList(0, limit);
            }
            entry.setValue(List.copyOf(entries));
        }
        topCache.clear();
        topCache.putAll(newCache);
    }

    private String normalizeName(String name) {
        if (name == null || name.isBlank()) {
            return "-";
        }
        return name;
    }

    private double calculateKdr(long kills, long deaths) {
        return (double) kills / Math.max(1, deaths);
    }

    private String formatKdr(double kdr) {
        return String.format(Locale.ROOT, "%.2f", kdr);
    }

    public enum TopType {
        KILLS,
        DEATHS,
        KDR,
        KILLSTREAK,
        BEST_KILLSTREAK
    }

    public record TopEntry(UUID uuid, String name, double valueNumber, String valueString) {
    }
}
