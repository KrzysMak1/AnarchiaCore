/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  org.bukkit.Location
 *  org.bukkit.entity.Player
 */
package me.anarchiacore.customitems.stormitemy.regions;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import me.anarchiacore.customitems.stormitemy.Main;
import me.anarchiacore.customitems.stormitemy.regions.B;
import me.anarchiacore.customitems.stormitemy.regions.C;

public class A {
    private final Main A;
    private final C C;
    private final B B;

    public A(Main main, C c2, B b2) {
        this.A = main;
        this.C = c2;
        this.B = b2;
    }

    public boolean A(Location location, Player player, String string) {
        if (this.B != null && this.B.A(location, player)) {
            return false;
        }
        return string == null || string.isEmpty() || this.C == null || !this.C.A(location, string);
    }

    public boolean B(Location location) {
        if (location == null) {
            return false;
        }
        try {
            return this.B != null && this.B.A(location, null);
        }
        catch (Exception exception) {
            this.A.getLogger().severe("B\u0142\u0105d podczas sprawdzania regionu dla lokalizacji: " + exception.getMessage());
            exception.printStackTrace();
            return false;
        }
    }

    public boolean A(Location location) {
        if (location == null) {
            return false;
        }
        try {
            return this.B != null && this.B.B(location);
        }
        catch (Exception exception) {
            this.A.getLogger().severe("B\u0142\u0105d podczas sprawdzania bloku docelowego w regionie: " + exception.getMessage());
            exception.printStackTrace();
            return false;
        }
    }

    public boolean A(Player player) {
        if (player == null) {
            return false;
        }
        try {
            return this.B != null && this.B.A(player.getLocation(), player);
        }
        catch (Exception exception) {
            this.A.getLogger().severe("B\u0142\u0105d podczas sprawdzania gracza w regionie: " + exception.getMessage());
            exception.printStackTrace();
            return false;
        }
    }

    public String A() {
        return me.anarchiacore.customitems.stormitemy.utils.color.A.C(this.A.getConfig().getString("messages.region", "&cNie mo\u017cesz u\u017cy\u0107 tego przedmiotu w tym regionie!"));
    }

    public String B() {
        return me.anarchiacore.customitems.stormitemy.utils.color.A.C(this.A.getConfig().getString("messages.region_target", "&cNie mo\u017cesz u\u017cy\u0107 tego przedmiotu na bloki w chronionym regionie!"));
    }
}

