/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.io.File
 *  java.io.IOException
 *  java.lang.Exception
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.util.ArrayList
 *  java.util.Collection
 *  java.util.HashMap
 *  java.util.List
 *  java.util.Map
 *  java.util.UUID
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.configuration.ConfigurationSection
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.entity.Player
 */
package me.anarchiacore.customitems.stormitemy.regions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import me.anarchiacore.customitems.stormitemy.Main;

public class C {
    private final Main C;
    private File B;
    private FileConfiguration A;
    private final Map<UUID, Location[]> D;

    public C(Main main) {
        block2: {
            this.D = new HashMap();
            this.C = main;
            try {
                this.C();
                this.B();
            }
            catch (Exception exception) {
                exception.printStackTrace();
                if (this.A != null) break block2;
                this.A = new YamlConfiguration();
            }
        }
    }

    private void C() {
        try {
            if (this.C.getDataFolder().exists() || this.C.getDataFolder().mkdirs()) {
                // empty if block
            }
            this.B = new File(this.C.getDataFolder(), "regions.yml");
            if (!this.B.exists()) {
                try {
                    if (this.B.createNewFile()) {
                        this.A = YamlConfiguration.loadConfiguration((File)this.B);
                        this.A.createSection("regions");
                        this.A.save(this.B);
                    }
                }
                catch (IOException iOException) {
                    iOException.printStackTrace();
                }
            }
            this.A = YamlConfiguration.loadConfiguration((File)this.B);
        }
        catch (Exception exception) {
            this.A = new YamlConfiguration();
        }
    }

    public void B() {
        try {
            this.A = YamlConfiguration.loadConfiguration((File)this.B);
        }
        catch (Exception exception) {
            exception.printStackTrace();
            this.A = new YamlConfiguration();
        }
    }

    public void D() {
        try {
            this.A.save(this.B);
        }
        catch (IOException iOException) {
            // empty catch block
        }
    }

    public void A(Player player, int n2, Location location) {
        try {
            UUID uUID = player.getUniqueId();
            if (!this.D.containsKey((Object)uUID)) {
                this.D.put((Object)uUID, (Object)new Location[2]);
            }
            ((Location[])this.D.get((Object)uUID))[n2 - 1] = location;
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public Location[] A(Player player) {
        try {
            return (Location[])this.D.getOrDefault((Object)player.getUniqueId(), (Object)new Location[2]);
        }
        catch (Exception exception) {
            return new Location[2];
        }
    }

    public boolean B(Player player) {
        try {
            Location[] locationArray = this.A(player);
            return locationArray[0] != null && locationArray[1] != null;
        }
        catch (Exception exception) {
            return false;
        }
    }

    public boolean A(Player player, String string) {
        try {
            if (!this.B(player)) {
                return false;
            }
            Location[] locationArray = this.A(player);
            String string2 = "regions." + string;
            this.A.set(string2 + ".world", (Object)locationArray[0].getWorld().getName());
            this.A.set(string2 + ".pos1.x", (Object)locationArray[0].getBlockX());
            this.A.set(string2 + ".pos1.y", (Object)locationArray[0].getBlockY());
            this.A.set(string2 + ".pos1.z", (Object)locationArray[0].getBlockZ());
            this.A.set(string2 + ".pos2.x", (Object)locationArray[1].getBlockX());
            this.A.set(string2 + ".pos2.y", (Object)locationArray[1].getBlockY());
            this.A.set(string2 + ".pos2.z", (Object)locationArray[1].getBlockZ());
            this.A.set(string2 + ".creator", (Object)player.getName());
            this.A.set(string2 + ".created", (Object)System.currentTimeMillis());
            this.D();
            return true;
        }
        catch (Exception exception) {
            return false;
        }
    }

    public boolean B(Player player, String string) {
        try {
            if (!this.B(player) || !this.C(string)) {
                return false;
            }
            Location[] locationArray = this.A(player);
            String string2 = "regions." + string;
            this.A.set(string2 + ".world", (Object)locationArray[0].getWorld().getName());
            this.A.set(string2 + ".pos1.x", (Object)locationArray[0].getBlockX());
            this.A.set(string2 + ".pos1.y", (Object)locationArray[0].getBlockY());
            this.A.set(string2 + ".pos1.z", (Object)locationArray[0].getBlockZ());
            this.A.set(string2 + ".pos2.x", (Object)locationArray[1].getBlockX());
            this.A.set(string2 + ".pos2.y", (Object)locationArray[1].getBlockY());
            this.A.set(string2 + ".pos2.z", (Object)locationArray[1].getBlockZ());
            this.A.set(string2 + ".modified_by", (Object)player.getName());
            this.A.set(string2 + ".modified", (Object)System.currentTimeMillis());
            this.D();
            return true;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public boolean B(String string) {
        try {
            if (!this.C(string)) {
                return false;
            }
            this.A.set("regions." + string, null);
            this.D();
            return true;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public boolean C(String string) {
        try {
            return this.A.contains("regions." + string);
        }
        catch (Exception exception) {
            return false;
        }
    }

    public boolean A(Location location, String string) {
        try {
            if (!this.C(string)) {
                return false;
            }
            String string2 = "regions." + string;
            String string3 = this.A.getString(string2 + ".world");
            if (location.getWorld() == null || !location.getWorld().getName().equals((Object)string3)) {
                return false;
            }
            int n2 = this.A.getInt(string2 + ".pos1.x");
            int n3 = this.A.getInt(string2 + ".pos1.y");
            int n4 = this.A.getInt(string2 + ".pos1.z");
            int n5 = this.A.getInt(string2 + ".pos2.x");
            int n6 = this.A.getInt(string2 + ".pos2.y");
            int n7 = this.A.getInt(string2 + ".pos2.z");
            int n8 = Math.min((int)n2, (int)n5);
            int n9 = Math.max((int)n2, (int)n5);
            int n10 = Math.min((int)n3, (int)n6);
            int n11 = Math.max((int)n3, (int)n6);
            int n12 = Math.min((int)n4, (int)n7);
            int n13 = Math.max((int)n4, (int)n7);
            int n14 = location.getBlockX();
            int n15 = location.getBlockY();
            int n16 = location.getBlockZ();
            boolean bl = n14 >= n8 && n14 <= n9 && n15 >= n10 && n15 <= n11 && n16 >= n12 && n16 <= n13;
            return bl;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public List<String> A() {
        try {
            ConfigurationSection configurationSection = this.A.getConfigurationSection("regions");
            if (configurationSection == null) {
                return new ArrayList();
            }
            return new ArrayList((Collection)configurationSection.getKeys(false));
        }
        catch (Exception exception) {
            return new ArrayList();
        }
    }

    public void C(Player player) {
        try {
            this.D.remove((Object)player.getUniqueId());
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public Location[] A(String string) {
        try {
            if (!this.C(string)) {
                return null;
            }
            String string2 = "regions." + string;
            String string3 = this.A.getString(string2 + ".world");
            World world = Bukkit.getWorld((String)string3);
            if (world == null) {
                return null;
            }
            int n2 = this.A.getInt(string2 + ".pos1.x");
            int n3 = this.A.getInt(string2 + ".pos1.y");
            int n4 = this.A.getInt(string2 + ".pos1.z");
            int n5 = this.A.getInt(string2 + ".pos2.x");
            int n6 = this.A.getInt(string2 + ".pos2.y");
            int n7 = this.A.getInt(string2 + ".pos2.z");
            Location location = new Location(world, (double)n2, (double)n3, (double)n4);
            Location location2 = new Location(world, (double)n5, (double)n6, (double)n7);
            return new Location[]{location, location2};
        }
        catch (Exception exception) {
            return null;
        }
    }

    public boolean A(Location location) {
        try {
            if (location == null || location.getWorld() == null) {
                return false;
            }
            List<String> list = this.A();
            for (String string : list) {
                if (!this.A(location, string)) continue;
                return true;
            }
            return false;
        }
        catch (Exception exception) {
            return false;
        }
    }
}

