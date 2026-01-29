/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Exception
 *  java.lang.IllegalStateException
 *  java.lang.Object
 *  java.lang.String
 *  java.util.function.Supplier
 *  org.bukkit.Bukkit
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.TabCompleter
 *  org.bukkit.event.Listener
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 */
package me.anarchiacore.customitems.stormitemy.core;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import me.anarchiacore.customitems.stormitemy.Main;
import me.anarchiacore.customitems.stormitemy.bstats.bukkit.Metrics;
import me.anarchiacore.customitems.stormitemy.commands.G;
import me.anarchiacore.customitems.stormitemy.core.C;
import me.anarchiacore.customitems.stormitemy.core.ItemRegistry;
import me.anarchiacore.customitems.stormitemy.listeners.F;
import me.anarchiacore.customitems.stormitemy.regions.A;
import me.anarchiacore.customitems.stormitemy.ui.gui.E;
import me.anarchiacore.customitems.stormitemy.zaczarowania.D;

public class B {
    private final Main j;
    private final String N;
    public me.anarchiacore.customitems.stormitemy.config.C D;
    public me.anarchiacore.customitems.stormitemy.messages.B L;
    public me.anarchiacore.customitems.stormitemy.config.A B;
    public me.anarchiacore.customitems.stormitemy.ui.menu.A T;
    public me.anarchiacore.customitems.stormitemy.config.D M;
    public me.anarchiacore.customitems.stormitemy.regions.C h;
    public me.anarchiacore.customitems.stormitemy.regions.B K;
    public me.anarchiacore.customitems.stormitemy.items.E c;
    public A O;
    public me.anarchiacore.customitems.stormitemy.zaczarowania.C l;
    public D f;
    public me.anarchiacore.customitems.stormitemy.zaczarowania.A A;
    public me.anarchiacore.customitems.stormitemy.commands.C J;
    public me.anarchiacore.customitems.stormitemy.zaczarowania.B R;
    public me.anarchiacore.customitems.stormitemy.books.A S;
    public me.anarchiacore.customitems.stormitemy.books.C U;
    public me.anarchiacore.customitems.stormitemy.books.B F;
    public me.anarchiacore.customitems.stormitemy.commands.D E;
    public G G;
    public me.anarchiacore.customitems.stormitemy.commands.F I;
    public me.anarchiacore.customitems.stormitemy.listeners.B n;
    public me.anarchiacore.customitems.stormitemy.commands.B H;
    public me.anarchiacore.customitems.stormitemy.ui.gui.A mainGui;
    public me.anarchiacore.customitems.stormitemy.listeners.D e;
    public F m;
    public me.anarchiacore.customitems.stormitemy.ui.gui.B i;
    public me.anarchiacore.customitems.stormitemy.listeners.C d;
    public me.anarchiacore.customitems.stormitemy.commands.A Z;
    public me.anarchiacore.customitems.stormitemy.commands.E menuCommand;
    public E Y;
    public me.anarchiacore.customitems.stormitemy.ui.gui.C V;
    public me.anarchiacore.customitems.stormitemy.ui.gui.D P;
    public me.anarchiacore.customitems.stormitemy.listeners.E X;
    public me.anarchiacore.customitems.stormitemy.listeners.A C;
    public me.anarchiacore.customitems.stormitemy.texturepack.B g;
    public me.anarchiacore.customitems.stormitemy.texturepack.A b;
    public me.anarchiacore.customitems.stormitemy.premium.A W;
    public boolean Q = false;
    public boolean a = false;
    public boolean k = false;

    public B(Main main) {
        this.j = main;
        this.N = main.getDescription().getVersion();
    }

    public void K() {
        try {
            this.J();
            this.N();
            this.A();
            this.I();
            this.F();
            this.L();
            this.O();
            this.E();
        }
        catch (Exception exception) {
            this.j.getLogger().severe("B\u0142\u0105d podczas inicjalizacji pluginu: " + exception.getMessage());
            exception.printStackTrace();
        }
    }

    private void J() {
        this.j.saveDefaultConfig();
        this.j.reloadConfig();
        this.B = new me.anarchiacore.customitems.stormitemy.config.A(this.j);
        this.B.G();
        this.B.A();
    }

    private void N() {
        if (this.j.getConfig().getBoolean("bstats.enabled", true)) {
            try {
                int n2 = 25765;
                new Metrics(this.j, n2);
                this.A("bStats w\u0142\u0105czony!");
            }
            catch (IllegalStateException illegalStateException) {
                this.j.getLogger().warning("Nie uda\u0142o si\u0119 zainicjalizowa\u0107 bStats: " + illegalStateException.getMessage());
            }
        } else {
            this.A("bStats wy\u0142\u0105czony!");
        }
    }

    private void A() {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("WorldGuard");
        if (plugin == null || !plugin.isEnabled()) {
            this.A("Nie wykryto pluginu WorldGuard! U\u017cywanie wbudowanego systemu region\u00f3w.");
            this.a = false;
        } else {
            this.a = true;
            this.A("Wykryto plugin WorldGuard. Mo\u017cna u\u017cywa\u0107 obu system\u00f3w region\u00f3w.");
        }
        Plugin plugin2 = Bukkit.getPluginManager().getPlugin("WorldEdit");
        if (plugin2 == null || !plugin2.isEnabled()) {
            this.A("Nie wykryto pluginu WorldEdit! Przedmioty TurboTrap i TurboDomek b\u0119d\u0105 wy\u0142\u0105czone.");
            this.Q = false;
        } else {
            this.Q = true;
            this.A("Wykryto plugin WorldEdit. Przedmioty TurboTrap i TurboDomek s\u0105 dost\u0119pne.");
        }
        Plugin plugin3 = Bukkit.getPluginManager().getPlugin("Citizens");
        if (plugin3 == null || !plugin3.isEnabled()) {
            this.A("Nie wykryto pluginu Citizens! R\u00f3\u017cd\u017cka Iluzjonisty b\u0119dzie u\u017cywa\u0107 w\u0142asnego systemu NPC.");
            this.k = false;
        } else {
            this.k = true;
            this.A("Wykryto plugin Citizens! R\u00f3\u017cd\u017cka Iluzjonisty zosta\u0142a wy\u0142\u0105czona aby unikn\u0105\u0107 konflikt\u00f3w.");
        }
    }

    private void I() {
        this.D = new me.anarchiacore.customitems.stormitemy.config.C((Plugin)this.j);
        this.M = new me.anarchiacore.customitems.stormitemy.config.D((Plugin)this.j);
        this.L = new me.anarchiacore.customitems.stormitemy.messages.B((Plugin)this.j);
        this.T = new me.anarchiacore.customitems.stormitemy.ui.menu.A(this.j);
        this.h = new me.anarchiacore.customitems.stormitemy.regions.C(this.j);
        this.g = new me.anarchiacore.customitems.stormitemy.texturepack.B(this.j);
        this.b = new me.anarchiacore.customitems.stormitemy.texturepack.A(this.j, this.g);
        this.W = new me.anarchiacore.customitems.stormitemy.premium.A(this.j);
    }

    private void F() {
        this.m = new F(this.j);
        this.I = new me.anarchiacore.customitems.stormitemy.commands.F(this.j);
        this.n = new me.anarchiacore.customitems.stormitemy.listeners.B(this.j);
        this.Z = new me.anarchiacore.customitems.stormitemy.commands.A(this.j, this.L, null);
        this.l = new me.anarchiacore.customitems.stormitemy.zaczarowania.C(this.j);
        this.f = new D(this.j, this.l);
        this.l.A(this.f);
        this.A = new me.anarchiacore.customitems.stormitemy.zaczarowania.A(this.j, this.l);
        this.R = new me.anarchiacore.customitems.stormitemy.zaczarowania.B(this.j);
        this.J = new me.anarchiacore.customitems.stormitemy.commands.C(this.j, this.l);
        this.J.setEventManager(this.R);
        this.S = new me.anarchiacore.customitems.stormitemy.books.A(this.j);
        this.U = new me.anarchiacore.customitems.stormitemy.books.C(this.j, this.S);
        this.F = new me.anarchiacore.customitems.stormitemy.books.B(this.j, this.S);
        this.E = new me.anarchiacore.customitems.stormitemy.commands.D(this.j, this.S);
        this.C();
        this.Z = new me.anarchiacore.customitems.stormitemy.commands.A(this.j, this.L, this.S);
    }

    private void L() {
        this.mainGui = new me.anarchiacore.customitems.stormitemy.ui.gui.A(this.j, this.h);
        this.H = new me.anarchiacore.customitems.stormitemy.commands.B(this.j, this.mainGui);
        this.i = new me.anarchiacore.customitems.stormitemy.ui.gui.B(this.j);
        this.d = new me.anarchiacore.customitems.stormitemy.listeners.C(this.j, this.i);
        this.i.A(this.d);
        this.e = new me.anarchiacore.customitems.stormitemy.listeners.D(this.j, this.mainGui, this.i);
        if (this.W != null && this.W.G()) {
            this.A("Premium aktywne - inicjalizacja kreatora w\u0142asnych przedmiot\u00f3w...");
            this.Y = new E(this.j);
            this.V = new me.anarchiacore.customitems.stormitemy.ui.gui.C(this.j, this.Y);
            this.P = new me.anarchiacore.customitems.stormitemy.ui.gui.D(this.j, this.Y);
            this.X = new me.anarchiacore.customitems.stormitemy.listeners.E(this.j, this.Y, this.V, this.P);
            this.C = new me.anarchiacore.customitems.stormitemy.listeners.A(this.j, this.Y);
            this.H.setCustomItemsListGUI(this.V);
        }
        this.c = new me.anarchiacore.customitems.stormitemy.items.E(this.j, this.h);
        this.G = new G(this.j, this.h, this.c);
        this.K = new me.anarchiacore.customitems.stormitemy.regions.B(this.j);
        this.O = new A(this.j, this.h, this.K);
    }

    private void O() {
        this.menuCommand = new me.anarchiacore.customitems.stormitemy.commands.E(this.j);
    }

    private void E() {
        C c2 = new C(this.j, this.j.getItems(), this.j.getInitializationLock(), this.Q, this.k);
        c2.A();
    }

    private void A(String string) {
        Bukkit.getConsoleSender().sendMessage("\u00a78[\u00a72StormItemy\u00a78] \u00a77" + string);
    }

    public void G() {
        this.A("Zapisywanie wszystkich danych przed wy\u0142\u0105czeniem...");
        C c2 = new C(this.j, this.j.getItems(), this.j.getInitializationLock(), this.Q, this.k);
        c2.B();
        if (this.g != null) {
            this.g.G();
        }
        this.A("Wszystkie dane zosta\u0142y zapisane!");
    }

    public void D() {
        if (this.g != null) {
            this.g.H();
        }
    }

    public List<Listener> getListeners() {
        List<Listener> listeners = new ArrayList<>();
        listeners.add(this.j);
        listeners.add(this.n);
        listeners.add(this.e);
        listeners.add(this.d);
        listeners.add(new me.anarchiacore.customitems.stormitemy.ui.menu.B(this.j));
        if (this.X != null) {
            listeners.add(this.X);
        }
        if (this.C != null) {
            listeners.add(this.C);
        }
        if (this.b != null) {
            listeners.add(this.b);
        }
        return listeners;
    }

    public me.anarchiacore.customitems.stormitemy.commands.E getMenuCommand() {
        return this.menuCommand;
    }

    public int getLoadedConfigCount() {
        int count = 0;
        if (this.B != null) {
            count++;
        }
        if (this.D != null) {
            count++;
        }
        if (this.M != null) {
            count++;
        }
        if (this.L != null) {
            count++;
        }
        return count;
    }

    public void M() {
        if (this.Y != null) {
            this.A("Kreator w\u0142asnych przedmiot\u00f3w ju\u017c zainicjalizowany.");
            return;
        }
        if (this.W == null || !this.W.G()) {
            this.A("Premium nieaktywne - nie mo\u017cna zainicjalizowa\u0107 kreatora.");
            return;
        }
        this.A("Inicjalizacja kreatora w\u0142asnych przedmiot\u00f3w po aktywacji premium...");
        this.Y = new E(this.j);
        this.V = new me.anarchiacore.customitems.stormitemy.ui.gui.C(this.j, this.Y);
        this.P = new me.anarchiacore.customitems.stormitemy.ui.gui.D(this.j, this.Y);
        this.X = new me.anarchiacore.customitems.stormitemy.listeners.E(this.j, this.Y, this.V, this.P);
        this.C = new me.anarchiacore.customitems.stormitemy.listeners.A(this.j, this.Y);
        if (this.H != null) {
            this.H.setCustomItemsListGUI(this.V);
        }
        this.j.getServer().getPluginManager().registerEvents((Listener)this.X, (Plugin)this.j);
        this.j.getServer().getPluginManager().registerEvents((Listener)this.C, (Plugin)this.j);
        this.A("Kreator w\u0142asnych przedmiot\u00f3w zainicjalizowany pomy\u015blnie!");
    }

    public boolean B() {
        return this.Y != null;
    }

    private void C() {
        if (this.S == null) {
            return;
        }
        for (me.anarchiacore.customitems.stormitemy.books.D d2 : this.S.C()) {
            String string = d2.getIdentifier();
            ItemRegistry.register(string, (Supplier<ItemStack>)((Supplier)d2::getBookItem), d2);
        }
    }
}
