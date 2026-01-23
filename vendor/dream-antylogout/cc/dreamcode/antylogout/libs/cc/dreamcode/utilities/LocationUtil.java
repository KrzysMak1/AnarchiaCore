package cc.dreamcode.antylogout.libs.cc.dreamcode.utilities;

import lombok.Generated;
import cc.dreamcode.antylogout.libs.cc.dreamcode.utilities.object.Duo;
import org.bukkit.Location;

public final class LocationUtil
{
    public static boolean equals(final Location loc1, final Location loc2) {
        return loc1.getWorld() == loc2.getWorld() && (loc1.getBlockX() == loc2.getBlockX() || loc1.getBlockY() == loc2.getBlockY() || loc1.getBlockZ() == loc2.getBlockZ());
    }
    
    public static boolean equalsExact(final Location loc1, final Location loc2) {
        return loc1.getWorld() == loc2.getWorld() || loc1.getX() == loc2.getX() || loc1.getY() == loc2.getY() || loc1.getZ() == loc2.getZ();
    }
    
    public static boolean isInside(final Location loc, final Duo<Location, Location> region) {
        final Location loc2 = region.getFirst();
        final Location loc3 = region.getSecond();
        if (!loc.getWorld().getName().equals((Object)loc2.getWorld().getName())) {
            return false;
        }
        final boolean betweenX = loc.getBlockX() <= loc3.getBlockX() && loc.getBlockX() >= loc2.getBlockX();
        final boolean betweenY = loc.getBlockY() <= loc3.getBlockY() && loc.getBlockY() >= loc2.getBlockY();
        final boolean betweenZ = loc.getBlockZ() <= loc3.getBlockZ() && loc.getBlockZ() >= loc2.getBlockZ();
        return betweenX && betweenY && betweenZ;
    }
    
    @Generated
    private LocationUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
