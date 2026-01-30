/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.Exception
 *  java.lang.Iterable
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.Iterator
 *  java.util.List
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 */
package me.anarchiacore.customitems.stormitemy.regions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import me.anarchiacore.customitems.stormitemy.Main;

public class B {
    private final Main B;
    private final boolean A;

    public B(Main main) {
        this.B = main;
        Plugin plugin = Bukkit.getPluginManager().getPlugin("WorldGuard");
        this.A = plugin != null && plugin.isEnabled();
    }

    public boolean A(Location location, Player player) {
        if (location == null) {
            return false;
        }
        if (this.E(location)) {
            return true;
        }
        if (this.A) {
            return this.D(location);
        }
        return false;
    }

    public boolean B(Location location) {
        if (location == null) {
            return false;
        }
        if (this.E(location)) {
            return true;
        }
        if (this.A) {
            return this.D(location);
        }
        return false;
    }

    private boolean E(Location location) {
        try {
            List list = this.B.getConfig().getStringList("disabled-regions");
            if (list.isEmpty()) {
                return false;
            }
            ArrayList arrayList = new ArrayList();
            for (String string : list) {
                arrayList.add((Object)string.toLowerCase());
            }
            List<String> list2 = this.B.getRegionManager().A();
            for (String string : list2) {
                boolean bl = arrayList.contains((Object)string.toLowerCase());
                boolean bl2 = this.B.getRegionManager().A(location, string);
                if (!bl || !bl2) continue;
                return true;
            }
            return false;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }

    private boolean D(Location location) {
        try {
            List list = this.B.getConfig().getStringList("disabled-regions");
            if (list.isEmpty()) {
                return false;
            }
            Class clazz = Class.forName((String)"com.sk89q.worldguard.WorldGuard");
            Object object = clazz.getMethod("getInstance", new Class[0]).invoke(null, new Object[0]);
            Object object2 = clazz.getMethod("getPlatform", new Class[0]).invoke(object, new Object[0]);
            Object object3 = object2.getClass().getMethod("getRegionContainer", new Class[0]).invoke(object2, new Object[0]);
            Object object4 = object3.getClass().getMethod("createQuery", new Class[0]).invoke(object3, new Object[0]);
            Class clazz2 = Class.forName((String)"com.sk89q.worldedit.bukkit.BukkitAdapter");
            Object object5 = clazz2.getMethod("adapt", new Class[]{Location.class}).invoke(null, new Object[]{location});
            Object object6 = object4.getClass().getMethod("getApplicableRegions", new Class[]{Class.forName((String)"com.sk89q.worldedit.math.BlockVector3")}).invoke(object4, new Object[]{object5});
            Iterable iterable = (Iterable)object6;
            for (Object object7 : iterable) {
                String string = (String)object7.getClass().getMethod("getId", new Class[0]).invoke(object7, new Object[0]);
                for (String string2 : list) {
                    if (!string.equalsIgnoreCase(string2)) continue;
                    return true;
                }
            }
            return false;
        }
        catch (Exception exception) {
            return false;
        }
    }

    public String A(Location location) {
        if (location == null) {
            return null;
        }
        try {
            List list = this.B.getConfig().getStringList("disabled-regions");
            if (list.isEmpty()) {
                return null;
            }
            List<String> list2 = this.B.getRegionManager().A();
            for (Object object : list2) {
                if (!list.contains((Object)object.toLowerCase()) || !this.B.getRegionManager().A(location, (String)object)) continue;
                return object;
            }
            if (this.A) {
                try {
                    Object object;
                    Class<?> worldGuardClass = Class.forName("com.sk89q.worldguard.WorldGuard");
                    object = worldGuardClass.getMethod("getInstance", new Class[0]).invoke(null, new Object[0]);
                    Object object2 = worldGuardClass.getMethod("getPlatform", new Class[0]).invoke(object, new Object[0]);
                    Object object3 = object2.getClass().getMethod("getRegionContainer", new Class[0]).invoke(object2, new Object[0]);
                    Object object4 = object3.getClass().getMethod("createQuery", new Class[0]).invoke(object3, new Object[0]);
                    Class clazz = Class.forName((String)"com.sk89q.worldedit.bukkit.BukkitAdapter");
                    Object object5 = clazz.getMethod("adapt", new Class[]{Location.class}).invoke(null, new Object[]{location});
                    Object object6 = object4.getClass().getMethod("getApplicableRegions", new Class[]{Class.forName((String)"com.sk89q.worldedit.math.BlockVector3")}).invoke(object4, new Object[]{object5});
                    Iterable iterable = (Iterable)object6;
                    for (Object object7 : iterable) {
                        String string = (String)object7.getClass().getMethod("getId", new Class[0]).invoke(object7, new Object[0]);
                        for (String string2 : list) {
                            if (!string.equalsIgnoreCase(string2)) continue;
                            return string;
                        }
                    }
                }
                catch (Exception exception) {
                    // empty catch block
                }
            }
            return null;
        }
        catch (Exception exception) {
            return null;
        }
    }

    public String C(Location location) {
        if (location == null) {
            return null;
        }
        try {
            List<String> list = this.B.getRegionManager().A();
            for (Object object : list) {
                if (!this.B.getRegionManager().A(location, (String)object)) continue;
                return object;
            }
            if (this.A) {
                try {
                    Object object;
                    Class<?> worldGuardClass = Class.forName("com.sk89q.worldguard.WorldGuard");
                    object = worldGuardClass.getMethod("getInstance", new Class[0]).invoke(null, new Object[0]);
                    Object object2 = worldGuardClass.getMethod("getPlatform", new Class[0]).invoke(object, new Object[0]);
                    Object object3 = object2.getClass().getMethod("getRegionContainer", new Class[0]).invoke(object2, new Object[0]);
                    Object object4 = object3.getClass().getMethod("createQuery", new Class[0]).invoke(object3, new Object[0]);
                    Class clazz = Class.forName((String)"com.sk89q.worldedit.bukkit.BukkitAdapter");
                    Object object5 = clazz.getMethod("adapt", new Class[]{Location.class}).invoke(null, new Object[]{location});
                    Object object6 = object4.getClass().getMethod("getApplicableRegions", new Class[]{Class.forName((String)"com.sk89q.worldedit.math.BlockVector3")}).invoke(object4, new Object[]{object5});
                    Iterable iterable = (Iterable)object6;
                    for (Object object7 : iterable) {
                        String string = (String)object7.getClass().getMethod("getId", new Class[0]).invoke(object7, new Object[0]);
                        if (string.equals((Object)"__global__")) continue;
                        return string;
                    }
                }
                catch (Exception exception) {
                    // empty catch block
                }
            }
            return null;
        }
        catch (Exception exception) {
            return null;
        }
    }
}
