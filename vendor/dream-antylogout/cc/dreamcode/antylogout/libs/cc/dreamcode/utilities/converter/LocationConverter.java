package cc.dreamcode.antylogout.libs.cc.dreamcode.utilities.converter;

import org.bukkit.World;
import org.bukkit.Bukkit;
import com.sk89q.worldedit.math.BlockVector3;
import org.bukkit.Location;

public class LocationConverter
{
    public static BlockVector3 convertToBlockVector3(final Location loc) {
        return BlockVector3.at(loc.getX(), loc.getY(), loc.getZ());
    }
    
    public static Location convertToLocation(final String worldName, final BlockVector3 vector) {
        return convertToLocation(Bukkit.getWorld(worldName), vector);
    }
    
    public static Location convertToLocation(final World world, final BlockVector3 vector) {
        return new Location(world, (double)vector.getX(), (double)vector.getY(), (double)vector.getZ());
    }
}
