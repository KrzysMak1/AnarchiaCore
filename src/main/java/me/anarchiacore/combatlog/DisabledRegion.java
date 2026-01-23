package me.anarchiacore.combatlog;

import org.bukkit.Location;

public interface DisabledRegion {
    boolean contains(Location location);
}
