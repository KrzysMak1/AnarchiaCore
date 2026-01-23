package me.anarchiacore.combatlog;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CombatLogRegionService {
    private final CombatLogConfig config;
    private final boolean worldGuardAvailable;
    private final List<DisabledRegion> regions = new ArrayList<>();

    public CombatLogRegionService(CombatLogConfig config, Plugin plugin) {
        this.config = config;
        this.worldGuardAvailable = Bukkit.getPluginManager().getPlugin("WorldGuard") != null;
        load();
    }

    public void load() {
        regions.clear();
        for (String entry : config.getDisabledRegions()) {
            if (entry == null) {
                continue;
            }
            String value = entry.trim();
            if (value.isEmpty()) {
                continue;
            }
            if (value.toLowerCase(Locale.ROOT).startsWith("wg:")) {
                if (!worldGuardAvailable) {
                    continue;
                }
                String[] split = value.substring(3).split(":");
                if (split.length >= 2) {
                    regions.add(new WorldGuardRegion(split[0], split[1]));
                }
            } else if (value.toLowerCase(Locale.ROOT).startsWith("rg:")) {
                String[] split = value.substring(3).split(":");
                if (split.length >= 7) {
                    try {
                        String worldName = split[0];
                        int minX = Integer.parseInt(split[1]);
                        int minY = Integer.parseInt(split[2]);
                        int minZ = Integer.parseInt(split[3]);
                        int maxX = Integer.parseInt(split[4]);
                        int maxY = Integer.parseInt(split[5]);
                        int maxZ = Integer.parseInt(split[6]);
                        regions.add(new CuboidRegion(worldName, minX, minY, minZ, maxX, maxY, maxZ));
                    } catch (NumberFormatException ignored) {
                        // skip invalid cuboid
                    }
                }
            }
        }
    }

    public boolean isDisabled(Location location) {
        for (DisabledRegion region : regions) {
            if (region.contains(location)) {
                return true;
            }
        }
        return false;
    }
}
