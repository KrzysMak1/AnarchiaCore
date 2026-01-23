package me.anarchiacore.combatlog;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.Set;

public class WorldGuardRegion implements DisabledRegion {
    private final String worldName;
    private final String regionName;
    private final RegionContainer regionContainer;

    public WorldGuardRegion(String worldName, String regionName) {
        this.worldName = worldName;
        this.regionName = regionName;
        this.regionContainer = WorldGuard.getInstance().getPlatform().getRegionContainer();
    }

    @Override
    public boolean contains(Location location) {
        if (location == null) {
            return false;
        }
        World world = location.getWorld();
        if (world == null || !world.getName().equalsIgnoreCase(worldName)) {
            return false;
        }
        RegionManager regionManager = regionContainer.get(BukkitAdapter.adapt(world));
        if (regionManager == null) {
            return false;
        }
        ProtectedRegion region = regionManager.getRegion(regionName);
        if (region == null) {
            return false;
        }
        RegionQuery query = regionContainer.createQuery();
        ApplicableRegionSet regions = query.getApplicableRegions(BukkitAdapter.adapt(location));
        Set<ProtectedRegion> set = regions.getRegions();
        return set.stream().anyMatch(candidate -> candidate.getId().equalsIgnoreCase(region.getId()));
    }
}
