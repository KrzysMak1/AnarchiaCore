package me.anarchiacore.customitems;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class WorldGuardIntegration {
    private final Plugin plugin;

    public WorldGuardIntegration(Plugin plugin) {
        this.plugin = plugin;
    }

    public RegionResult checkRegion(Location location, CustomItemsConfig.WorldGuardRules rules) {
        if (rules == null || !rules.enabled()) {
            return RegionResult.NO_RULES;
        }
        if (plugin.getServer().getPluginManager().getPlugin("WorldGuard") == null) {
            return RegionResult.NO_RULES;
        }
        RegionManager manager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(location.getWorld()));
        if (manager == null) {
            return RegionResult.NO_RULES;
        }
        ApplicableRegionSet set = manager.getApplicableRegions(BukkitAdapter.asBlockVector(location));
        if (set == null || set.size() == 0) {
            return RegionResult.NO_RULES;
        }
        int maxPriority = set.getRegions().stream().mapToInt(ProtectedRegion::getPriority).max().orElse(Integer.MIN_VALUE);
        Set<String> highest = new HashSet<>();
        for (ProtectedRegion region : set.getRegions()) {
            if (region.getPriority() == maxPriority) {
                highest.add(region.getId().toLowerCase(Locale.ROOT));
            }
        }
        if (containsAny(highest, rules.allowAllRegions())) {
            return RegionResult.ALLOW_ALL;
        }
        if (containsAny(highest, rules.allowExceptExplosivesRegions())) {
            return RegionResult.ALLOW_NON_EXPLOSIVES;
        }
        return RegionResult.NO_RULES;
    }

    public boolean isInRegions(Location location, Set<String> regionIds) {
        if (regionIds == null || regionIds.isEmpty()) {
            return false;
        }
        if (plugin.getServer().getPluginManager().getPlugin("WorldGuard") == null) {
            return false;
        }
        RegionManager manager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(location.getWorld()));
        if (manager == null) {
            return false;
        }
        ApplicableRegionSet set = manager.getApplicableRegions(BukkitAdapter.asBlockVector(location));
        if (set == null || set.size() == 0) {
            return false;
        }
        return set.getRegions().stream()
                .anyMatch(region -> regionIds.contains(region.getId().toLowerCase(Locale.ROOT)));
    }

    private boolean containsAny(Set<String> regionIds, List<String> allowed) {
        if (allowed == null || allowed.isEmpty()) {
            return false;
        }
        for (String id : allowed) {
            if (regionIds.contains(id.toLowerCase(Locale.ROOT))) {
                return true;
            }
        }
        return false;
    }

    public enum RegionResult {
        NO_RULES,
        ALLOW_ALL,
        ALLOW_NON_EXPLOSIVES
    }
}
