package me.anarchiacore.papi;

import me.anarchiacore.combatlog.CombatLogManager;
import me.anarchiacore.config.ConfigManager;
import me.anarchiacore.stats.PlayerStats;
import me.anarchiacore.stats.StatsManager;
import me.anarchiacore.stats.TopCacheManager;
import me.anarchiacore.storage.DataStore;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Locale;

public class AnarchiaCorePlaceholderExpansion extends PlaceholderExpansion {
    private final JavaPlugin plugin;
    private final ConfigManager configManager;
    private final DataStore dataStore;
    private final CombatLogManager combatLogManager;
    private final StatsManager statsManager;

    public AnarchiaCorePlaceholderExpansion(JavaPlugin plugin,
                                            ConfigManager configManager,
                                            DataStore dataStore,
                                            CombatLogManager combatLogManager,
                                            StatsManager statsManager) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.dataStore = dataStore;
        this.combatLogManager = combatLogManager;
        this.statsManager = statsManager;
    }

    @Override
    public String getIdentifier() {
        return "anarchiacore";
    }

    @Override
    public String getAuthor() {
        if (plugin.getDescription().getAuthors() == null || plugin.getDescription().getAuthors().isEmpty()) {
            return "unknown";
        }
        return String.join(", ", plugin.getDescription().getAuthors());
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        if (params == null) {
            return null;
        }
        String key = params.toLowerCase(Locale.ROOT);
        switch (key) {
            case "hearts" -> {
                if (player == null) {
                    return "0";
                }
                return String.valueOf(dataStore.getHearts(player.getUniqueId(), configManager.getDefaultHearts()));
            }
            case "defaulthearts" -> {
                return String.valueOf(configManager.getDefaultHearts());
            }
            case "maxhearts" -> {
                return String.valueOf(configManager.getMaxHearts());
            }
            case "combat_tagged" -> {
                if (player == null || !player.isOnline()) {
                    return "false";
                }
                return String.valueOf(combatLogManager.isCombatTagged(player.getUniqueId()));
            }
            case "combat_timeleft" -> {
                if (player == null || !player.isOnline()) {
                    return "0";
                }
                return String.valueOf(combatLogManager.getCombatTimeLeftSeconds(player.getUniqueId()));
            }
            case "combat_timeleft_mmss" -> {
                if (player == null || !player.isOnline()) {
                    return "00:00";
                }
                return formatTime(combatLogManager.getCombatTimeLeftSeconds(player.getUniqueId()));
            }
            case "kills" -> {
                return String.valueOf(getStatValue(player, StatType.KILLS));
            }
            case "deaths" -> {
                return String.valueOf(getStatValue(player, StatType.DEATHS));
            }
            case "killstreak" -> {
                return String.valueOf(getStatValue(player, StatType.KILLSTREAK));
            }
            case "bestkillstreak" -> {
                return String.valueOf(getStatValue(player, StatType.BEST_KILLSTREAK));
            }
            case "kdr" -> {
                return formatKdr(getStatValue(player, StatType.KILLS), getStatValue(player, StatType.DEATHS));
            }
            default -> {
                return resolveTopPlaceholder(key);
            }
        }
    }

    private int getStatValue(OfflinePlayer player, StatType type) {
        if (player == null) {
            return 0;
        }
        PlayerStats stats = statsManager.getStats(player.getUniqueId());
        if (stats == null) {
            return 0;
        }
        return switch (type) {
            case KILLS -> stats.getKills();
            case DEATHS -> stats.getDeaths();
            case KILLSTREAK -> stats.getKillstreak();
            case BEST_KILLSTREAK -> stats.getBestKillstreak();
        };
    }

    private String resolveTopPlaceholder(String key) {
        int topIndex = key.indexOf("_top_");
        if (topIndex <= 0) {
            return null;
        }
        String typeKey = key.substring(0, topIndex);
        String remainder = key.substring(topIndex + "_top_".length());
        int splitIndex = remainder.indexOf('_');
        if (splitIndex <= 0) {
            return null;
        }
        String positionToken = remainder.substring(0, splitIndex);
        String field = remainder.substring(splitIndex + 1);
        int position;
        try {
            position = Integer.parseInt(positionToken);
        } catch (NumberFormatException ex) {
            return null;
        }
        if (position < 1) {
            return null;
        }
        boolean isName = field.equals("name");
        boolean isValue = field.equals("value");
        if (!isName && !isValue) {
            return null;
        }
        TopCacheManager.TopType type = switch (typeKey) {
            case "kills" -> TopCacheManager.TopType.KILLS;
            case "deaths" -> TopCacheManager.TopType.DEATHS;
            case "kdr" -> TopCacheManager.TopType.KDR;
            case "killstreak" -> TopCacheManager.TopType.KILLSTREAK;
            case "bestkillstreak" -> TopCacheManager.TopType.BEST_KILLSTREAK;
            default -> null;
        };
        if (type == null) {
            return null;
        }
        TopCacheManager.TopEntry entry = statsManager.getTopCacheManager().getTopEntry(type, position);
        if (entry == null) {
            return isName ? "-" : "0";
        }
        return isName ? entry.name() : entry.valueString();
    }

    private String formatKdr(int kills, int deaths) {
        double kdr = (double) kills / Math.max(1, deaths);
        return String.format(Locale.ROOT, "%.2f", kdr);
    }

    private String formatTime(int totalSeconds) {
        int seconds = Math.max(0, totalSeconds);
        int minutes = seconds / 60;
        int secs = seconds % 60;
        return String.format(Locale.ROOT, "%02d:%02d", minutes, secs);
    }

    private enum StatType {
        KILLS,
        DEATHS,
        KILLSTREAK,
        BEST_KILLSTREAK
    }
}
