package cc.dreamcode.antylogout.libs.cc.dreamcode.utilities;

import lombok.Generated;
import cc.dreamcode.antylogout.libs.cc.dreamcode.utilities.enums.Direction;
import org.bukkit.util.Vector;
import org.bukkit.Location;

public final class VectorUtil
{
    public static Vector getPushVector(final Location loc, final Location minLoc, final Location maxLoc, final double knockbackY, final double knockbackStrength) {
        final Vector vector = getDirection(loc, minLoc, maxLoc).getVector();
        vector.setY(knockbackY);
        return vector.multiply(knockbackStrength);
    }
    
    public static Direction getDirection(final Location loc, final Location minLoc, final Location maxLoc) {
        final double minX = minLoc.getX();
        final double maxX = maxLoc.getX();
        final double minZ = minLoc.getZ();
        final double maxZ = maxLoc.getZ();
        final double distNorth = loc.getZ() - minZ;
        final double distSouth = maxZ - loc.getZ();
        final double distWest = loc.getX() - minX;
        final double distEast = maxX - loc.getX();
        final double minDist = Math.min(Math.min(distWest, distEast), Math.min(distNorth, distSouth));
        Direction direction;
        if (minDist == distNorth) {
            direction = Direction.NORTH;
        }
        else if (minDist == distSouth) {
            direction = Direction.SOUTH;
        }
        else if (minDist == distWest) {
            direction = Direction.WEST;
        }
        else {
            direction = Direction.EAST;
        }
        return direction;
    }
    
    @Generated
    private VectorUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
