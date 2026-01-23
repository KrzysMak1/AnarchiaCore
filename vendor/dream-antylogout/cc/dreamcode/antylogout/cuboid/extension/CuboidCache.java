package cc.dreamcode.antylogout.cuboid.extension;

import cc.dreamcode.antylogout.libs.cc.dreamcode.utilities.ParseUtil;
import org.bukkit.Bukkit;
import lombok.Generated;
import cc.dreamcode.antylogout.libs.eu.okaeri.injector.annotation.Inject;
import java.util.ArrayList;
import cc.dreamcode.antylogout.libs.eu.okaeri.injector.annotation.PostConstruct;
import java.util.Collection;
import cc.dreamcode.antylogout.config.PluginConfig;
import org.bukkit.Location;
import cc.dreamcode.antylogout.libs.cc.dreamcode.utilities.object.Duo;
import java.util.List;

public class CuboidCache
{
    private final List<Duo<Location, Location>> regionList;
    private final PluginConfig pluginConfig;
    
    @PostConstruct
    public void loadCuboids() {
        this.regionList.addAll((Collection)this.pluginConfig.antylogout.disabledRegions.stream().filter(entry -> entry.startsWith("rg")).map(rgString -> {
            final String[] split = rgString.split(":");
            final Location minLoc = new Location(Bukkit.getWorld(split[1]), (double)ParseUtil.parseDouble(split[2]).get(), (double)ParseUtil.parseDouble(split[3]).get(), (double)ParseUtil.parseDouble(split[4]).get());
            final Location maxLoc = new Location(Bukkit.getWorld(split[1]), (double)ParseUtil.parseDouble(split[5]).get(), (double)ParseUtil.parseDouble(split[6]).get(), (double)ParseUtil.parseDouble(split[7]).get());
            return Duo.of(minLoc, maxLoc);
        }).toList());
    }
    
    public boolean inCuboid(final Location loc, final Duo<Location, Location> region) {
        final Location minLoc = region.getFirst();
        final Location maxLoc = region.getSecond();
        return loc.getWorld() == minLoc.getWorld() && loc.getX() >= minLoc.getX() && loc.getX() <= maxLoc.getX() && loc.getY() >= minLoc.getY() && loc.getY() <= maxLoc.getY() && loc.getZ() >= minLoc.getZ() && loc.getZ() <= maxLoc.getZ();
    }
    
    public List<Duo<Location, Location>> getCuboids(final Location loc) {
        return (List<Duo<Location, Location>>)this.getRegionList().stream().filter(region -> this.inCuboid(loc, region)).toList();
    }
    
    public boolean inCuboid(final Location loc) {
        return this.getRegionList().stream().anyMatch(pair -> this.inCuboid(loc, pair));
    }
    
    @Inject
    @Generated
    public CuboidCache(final PluginConfig pluginConfig) {
        this.regionList = (List<Duo<Location, Location>>)new ArrayList();
        this.pluginConfig = pluginConfig;
    }
    
    @Generated
    public List<Duo<Location, Location>> getRegionList() {
        return this.regionList;
    }
}
