package cc.dreamcode.antylogout.libs.cc.dreamcode.utilities.enums;

import lombok.Generated;
import org.bukkit.util.Vector;

public enum Direction
{
    NORTH(0, 0, -1), 
    SOUTH(0, 0, 1), 
    WEST(-1, 0, 0), 
    EAST(1, 0, 0);
    
    private final Vector vector;
    
    private Direction(final int x, final int y, final int z) {
        this.vector = new Vector(x, y, z);
    }
    
    @Generated
    public Vector getVector() {
        return this.vector;
    }
}
