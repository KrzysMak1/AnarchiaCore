package cc.dreamcode.antylogout.hook.worldguard.extension;

import cc.dreamcode.antylogout.libs.cc.dreamcode.utilities.LocationUtil;
import cc.dreamcode.antylogout.libs.cc.dreamcode.utilities.converter.LocationConverter;
import lombok.Generated;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import java.util.List;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import lombok.NonNull;
import org.bukkit.Location;
import cc.dreamcode.antylogout.libs.eu.okaeri.injector.annotation.PostConstruct;
import java.util.Collection;
import cc.dreamcode.antylogout.libs.eu.okaeri.injector.annotation.Inject;
import com.sk89q.worldguard.WorldGuard;
import java.util.HashSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.World;
import cc.dreamcode.antylogout.libs.cc.dreamcode.utilities.object.Duo;
import java.util.Set;
import cc.dreamcode.antylogout.libs.cc.dreamcode.platform.DreamLogger;
import org.bukkit.Server;
import cc.dreamcode.antylogout.config.PluginConfig;
import com.sk89q.worldguard.protection.regions.RegionContainer;

public class RegionService
{
    private final RegionContainer regionContainer;
    private final PluginConfig pluginConfig;
    private final Server server;
    private final DreamLogger logger;
    private final Set<Duo<World, ProtectedRegion>> cachedRegions;
    
    @Inject
    public RegionService(final PluginConfig pluginConfig, final Server server, final DreamLogger logger) {
        this.cachedRegions = (Set<Duo<World, ProtectedRegion>>)new HashSet();
        this.regionContainer = WorldGuard.getInstance().getPlatform().getRegionContainer();
        this.pluginConfig = pluginConfig;
        this.server = server;
        this.logger = logger;
    }
    
    @PostConstruct
    public void loadRegions() {
        this.cachedRegions.clear();
        final Set<Duo<World, ProtectedRegion>> regionSet = this.getDisabledRegions();
        this.cachedRegions.addAll((Collection)regionSet);
        this.logger.info("Loaded " + regionSet.size() + " regions");
    }
    
    public boolean isDisabledRegion(final Location location) {
        return this.cachedRegions.stream().anyMatch(duo -> {
            final ProtectedRegion region = duo.getSecond();
            final Duo<Location, Location> locations = Duo.of(LocationConverter.convertToLocation(duo.getFirst(), region.getMinimumPoint()), LocationConverter.convertToLocation(duo.getFirst(), region.getMaximumPoint()));
            return LocationUtil.isInside(location, locations);
        });
    }
    
    public ProtectedRegion getRegion(@NonNull final String id, final World world) {
        if (id == null) {
            throw new NullPointerException("id is marked non-null but is null");
        }
        final RegionManager regionManager = this.regionContainer.get(BukkitAdapter.adapt(world));
        if (regionManager == null) {
            return null;
        }
        return regionManager.getRegion(id);
    }
    
    public Set<Duo<World, ProtectedRegion>> getDisabledRegions() {
        final List<Duo<String, World>> regionIds = (List<Duo<String, World>>)this.pluginConfig.antylogout.disabledRegions.stream().filter(entry -> entry.startsWith("wg:")).map(str -> str.substring(3).split(":")).map(split -> Duo.of(split[1], this.server.getWorld(split[0]))).toList();
        final Set<Duo<World, ProtectedRegion>> regionSet = (Set<Duo<World, ProtectedRegion>>)new HashSet();
        regionIds.forEach(duo -> {
            final ProtectedRegion region = this.getRegion(duo.getFirst(), duo.getSecond());
            if (region != null) {
                regionSet.add((Object)Duo.of((Object)duo.getSecond(), region));
            }
        });
        return regionSet;
    }
    
    public Set<ProtectedRegion> getRegions(@NonNull final Location location) {
        if (location == null) {
            throw new NullPointerException("location is marked non-null but is null");
        }
        final RegionQuery regionQuery = this.regionContainer.createQuery();
        final ApplicableRegionSet regionSet = regionQuery.getApplicableRegions(BukkitAdapter.adapt(location));
        return (Set<ProtectedRegion>)regionSet.getRegions();
    }
    
    public boolean isAntyLogoutDisabled(@NonNull final ProtectedRegion region) {
        if (region == null) {
            throw new NullPointerException("region is marked non-null but is null");
        }
        return this.cachedRegions.stream().anyMatch(duo -> duo.getSecond().equals(region));
    }
    
    public boolean isAntyLogoutDisabled(@NonNull final Set<ProtectedRegion> regions) {
        if (regions == null) {
            throw new NullPointerException("regions is marked non-null but is null");
        }
        return regions.stream().anyMatch(this::isAntyLogoutDisabled);
    }
    
    public boolean isAntyLogoutDisabled(@NonNull final Location location) {
        if (location == null) {
            throw new NullPointerException("location is marked non-null but is null");
        }
        return this.isDisabledRegion(location);
    }
    
    @Generated
    public Set<Duo<World, ProtectedRegion>> getCachedRegions() {
        return this.cachedRegions;
    }
}
