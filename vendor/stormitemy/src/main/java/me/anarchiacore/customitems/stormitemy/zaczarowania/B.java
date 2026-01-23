/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Exception
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.lang.System
 *  org.bukkit.Bukkit
 *  org.bukkit.boss.BarColor
 *  org.bukkit.boss.BarFlag
 *  org.bukkit.boss.BarStyle
 *  org.bukkit.boss.BossBar
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 */
package me.anarchiacore.customitems.stormitemy.zaczarowania;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import me.anarchiacore.customitems.stormitemy.Main;
import me.anarchiacore.customitems.stormitemy.utils.color.A;

public class B {
    private final Main G;
    private BossBar C;
    private int K = -1;
    private long J = -1L;
    private long H = -1L;
    private long B = -1L;
    private int E = -1;
    private int L = -1;
    private final BarColor[] A = new BarColor[]{BarColor.RED, BarColor.PINK, BarColor.PURPLE, BarColor.BLUE, BarColor.GREEN, BarColor.YELLOW};
    private int F = 0;
    private final int[][] D = new int[][]{{255, 0, 0}, {255, 151, 0}, {255, 246, 0}, {34, 255, 0}, {0, 181, 255}, {152, 0, 255}, {255, 0, 234}};
    private int I = 0;

    public B(Main main) {
        this.G = main;
    }

    public void A(long l2) {
        if (this.D()) {
            this.F();
        }
        this.H = System.currentTimeMillis();
        this.B = l2 * 1000L;
        this.J = this.H + this.B;
        this.C = Bukkit.createBossBar((String)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&cSZCZ\u0118\u015aLIWA ZMIANKA (00:00)"), (BarColor)BarColor.RED, (BarStyle)BarStyle.SOLID, (BarFlag[])new BarFlag[0]);
        this.C.setProgress(1.0);
        for (Player player : Bukkit.getOnlinePlayers()) {
            this.C.addPlayer(player);
        }
        this.K = Bukkit.getScheduler().scheduleSyncRepeatingTask((Plugin)this.G, () -> this.H(), 0L, 20L);
        this.E = Bukkit.getScheduler().scheduleSyncRepeatingTask((Plugin)this.G, () -> this.C(), 0L, 10L);
        this.L = Bukkit.getScheduler().scheduleSyncRepeatingTask((Plugin)this.G, () -> this.E(), 0L, 2L);
        this.G.getLogger().info("[LuckyEvent] Event szcz\u0119\u015bliwej zmianki rozpocz\u0105\u0142 si\u0119 na " + l2 + " sekund!");
    }

    private void H() {
        if (!this.D()) {
            this.F();
            return;
        }
        long l2 = this.J - System.currentTimeMillis();
        if (l2 <= 0L) {
            this.F();
            return;
        }
        long l3 = l2 / 1000L;
        long l4 = l3 / 60L;
        long l5 = l3 % 60L;
        double d2 = Math.max((double)0.0, (double)((double)l2 / (double)this.B));
        this.C.setProgress(d2);
    }

    private void C() {
        if (this.C == null || !this.D()) {
            return;
        }
        this.C.setColor(this.A[this.F]);
        this.F = (this.F + 1) % this.A.length;
    }

    private void E() {
        if (this.C == null || !this.D()) {
            return;
        }
        long l2 = this.J - System.currentTimeMillis();
        if (l2 <= 0L) {
            this.F();
            return;
        }
        long l3 = l2 / 1000L;
        long l4 = l3 / 60L;
        long l5 = l3 % 60L;
        String string = String.format((String)"%02d:%02d", (Object[])new Object[]{l4, l5});
        String string2 = "SZCZ\u0118\u015aLIWA ZMIANKA (" + string + ")";
        StringBuilder stringBuilder = new StringBuilder();
        for (int i2 = 0; i2 < string2.length(); ++i2) {
            String string3 = this.A(i2);
            stringBuilder.append(string3).append(string2.charAt(i2));
        }
        this.C.setTitle(me.anarchiacore.customitems.stormitemy.utils.color.A.C(stringBuilder.toString()));
        this.I = (this.I + 3) % (this.D.length * 10);
    }

    private String A(int n2) {
        int n3 = this.D.length * 10;
        int n4 = (n2 * 3 + this.I) % n3;
        int n5 = n4 / 10;
        int n6 = (n5 + 1) % this.D.length;
        int n7 = n4 % 10;
        int[] nArray = this.D[n5];
        int[] nArray2 = this.D[n6];
        int n8 = this.A(nArray[0], nArray2[0], n7, 10);
        int n9 = this.A(nArray[1], nArray2[1], n7, 10);
        int n10 = this.A(nArray[2], nArray2[2], n7, 10);
        return String.format((String)"&#%02X%02X%02X", (Object[])new Object[]{n8, n9, n10});
    }

    private int A(int n2, int n3, int n4, int n5) {
        return n2 + (n3 - n2) * n4 / n5;
    }

    public void F() {
        if (this.K != -1) {
            Bukkit.getScheduler().cancelTask(this.K);
            this.K = -1;
        }
        if (this.E != -1) {
            Bukkit.getScheduler().cancelTask(this.E);
            this.E = -1;
        }
        if (this.L != -1) {
            Bukkit.getScheduler().cancelTask(this.L);
            this.L = -1;
        }
        if (this.C != null) {
            try {
                this.C.removeAll();
                this.C.setVisible(false);
            }
            catch (Exception exception) {
                this.G.getLogger().warning("[LuckyEvent] B\u0142\u0105d przy usuwaniu bossbara: " + exception.getMessage());
            }
            this.C = null;
        }
        this.J = -1L;
        this.H = -1L;
        this.B = -1L;
        this.F = 0;
        this.I = 0;
        this.G.getLogger().info("[LuckyEvent] Event szcz\u0119\u015bliwej zmianki zosta\u0142 zatrzymany!");
    }

    public boolean D() {
        return this.J > 0L && System.currentTimeMillis() < this.J;
    }

    public void A(Player player) {
        if (this.C != null && this.D()) {
            this.C.addPlayer(player);
        }
    }
}

