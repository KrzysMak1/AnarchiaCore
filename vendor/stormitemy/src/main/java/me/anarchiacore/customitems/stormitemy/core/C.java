/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Exception
 *  java.lang.NoClassDefFoundError
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.util.Map
 *  java.util.function.Supplier
 *  org.bukkit.Bukkit
 *  org.bukkit.event.Listener
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 */
package me.anarchiacore.customitems.stormitemy.core;

import java.util.Map;
import java.util.function.Supplier;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import me.anarchiacore.customitems.stormitemy.Main;
import me.anarchiacore.customitems.stormitemy.core.ItemRegistry;
import me.anarchiacore.customitems.stormitemy.items.$A;
import me.anarchiacore.customitems.stormitemy.items.A;
import me.anarchiacore.customitems.stormitemy.items.AA;
import me.anarchiacore.customitems.stormitemy.items.B;
import me.anarchiacore.customitems.stormitemy.items.BA;
import me.anarchiacore.customitems.stormitemy.items.CA;
import me.anarchiacore.customitems.stormitemy.items.D;
import me.anarchiacore.customitems.stormitemy.items.DA;
import me.anarchiacore.customitems.stormitemy.items.EA;
import me.anarchiacore.customitems.stormitemy.items.F;
import me.anarchiacore.customitems.stormitemy.items.FA;
import me.anarchiacore.customitems.stormitemy.items.G;
import me.anarchiacore.customitems.stormitemy.items.GA;
import me.anarchiacore.customitems.stormitemy.items.H;
import me.anarchiacore.customitems.stormitemy.items.HA;
import me.anarchiacore.customitems.stormitemy.items.I;
import me.anarchiacore.customitems.stormitemy.items.IA;
import me.anarchiacore.customitems.stormitemy.items.J;
import me.anarchiacore.customitems.stormitemy.items.JA;
import me.anarchiacore.customitems.stormitemy.items.K;
import me.anarchiacore.customitems.stormitemy.items.KA;
import me.anarchiacore.customitems.stormitemy.items.L;
import me.anarchiacore.customitems.stormitemy.items.M;
import me.anarchiacore.customitems.stormitemy.items.N;
import me.anarchiacore.customitems.stormitemy.items.O;
import me.anarchiacore.customitems.stormitemy.items.P;
import me.anarchiacore.customitems.stormitemy.items.Q;
import me.anarchiacore.customitems.stormitemy.items.R;
import me.anarchiacore.customitems.stormitemy.items.S;
import me.anarchiacore.customitems.stormitemy.items.T;
import me.anarchiacore.customitems.stormitemy.items.U;
import me.anarchiacore.customitems.stormitemy.items.V;
import me.anarchiacore.customitems.stormitemy.items.W;
import me.anarchiacore.customitems.stormitemy.items.X;
import me.anarchiacore.customitems.stormitemy.items.Y;
import me.anarchiacore.customitems.stormitemy.items.Z;
import me.anarchiacore.customitems.stormitemy.items.Kosa;
import me.anarchiacore.customitems.stormitemy.items.a;
import me.anarchiacore.customitems.stormitemy.items.b;
import me.anarchiacore.customitems.stormitemy.items.c;
import me.anarchiacore.customitems.stormitemy.items.d;
import me.anarchiacore.customitems.stormitemy.items.e;
import me.anarchiacore.customitems.stormitemy.items.f;
import me.anarchiacore.customitems.stormitemy.items.g;
import me.anarchiacore.customitems.stormitemy.items.h;
import me.anarchiacore.customitems.stormitemy.items.i;
import me.anarchiacore.customitems.stormitemy.items.j;
import me.anarchiacore.customitems.stormitemy.items.k;
import me.anarchiacore.customitems.stormitemy.items.l;
import me.anarchiacore.customitems.stormitemy.items.m;
import me.anarchiacore.customitems.stormitemy.items.n;
import me.anarchiacore.customitems.stormitemy.items.o;
import me.anarchiacore.customitems.stormitemy.items.p;
import me.anarchiacore.customitems.stormitemy.items.q;
import me.anarchiacore.customitems.stormitemy.items.R;
import me.anarchiacore.customitems.stormitemy.items.s;
import me.anarchiacore.customitems.stormitemy.items.t;
import me.anarchiacore.customitems.stormitemy.items.u;
import me.anarchiacore.customitems.stormitemy.items.v;
import me.anarchiacore.customitems.stormitemy.items.x;
import me.anarchiacore.customitems.stormitemy.items.y;
import me.anarchiacore.customitems.stormitemy.items.z;

public class C {
    private final Main C;
    private final Map<String, Object> B;
    private final Object A;
    private final boolean E;
    private final boolean D;

    public C(Main main, Map<String, Object> map, Object object, boolean bl, boolean bl2) {
        this.C = main;
        this.B = map;
        this.A = object;
        this.E = bl;
        this.D = bl2;
    }

    public void A() {
        long l2 = System.currentTimeMillis();
        Bukkit.getScheduler().runTaskAsynchronously((Plugin)this.C, () -> {
            try {
                this.A("\u0141adowanie przedmiot\u00f3w... (0%)");
                this.A("lopatagrincha", new H((Plugin)this.C));
                this.A("piernik", new d((Plugin)this.C));
                this.A("cieplemleko", new u((Plugin)this.C));
                this.A("boskitopor", new n(this.C));
                this.A("sniezka", new l(this.C));
                this.A("sniezynka", new IA(this.C));
                this.A("olaf", new O(this.C));
                this.A("rozakupidyna", new p(this.C));
                this.A("parawan", new T(this.C));
                this.A("rozga", new X(this.C));
                this.A("kupaanarchi", new B(this.C));
                this.A("lizak", new a(this.C));
                this.A("koronaanarchi", new W(this.C));
                this.A("totemulaskawienia", new R(this.C));
                this.A("\u0141adowanie przedmiot\u00f3w... (20%)");
                this.A("wedkasurferka", new o((Plugin)this.C));
                this.A("siekieragrincha", new y((Plugin)this.C));
                this.A("smoczymiecz", new c((Plugin)this.C));
                this.A("bombardamaxima", new HA(this.C));
                this.A("lukkupidyna", new e(this.C));
                this.A("marchewkowymiecz", new f(this.C));
                this.A("kosa", new Kosa((Plugin)this.C));
                this.A("excalibur", new EA((Plugin)this.C));
                this.A("krewwampira", new AA((Plugin)this.C));
                this.A("marchewkowakusza", new q(this.C));
                this.A("lewejajko", new FA(this.C));
                this.A("zajeczymiecz", new F(this.C));
                this.A("\u0141adowanie przedmiot\u00f3w... (40%)");
                this.A("zatrutyolowek", new $A(this.C));
                this.A("splesnialakanapka", new U(this.C));
                this.A("wampirzejablko", new h((Plugin)this.C));
                this.A("arcusmagnus", new A(this.C));
                this.A("piekielnatarcza", new GA(this.C));
                this.A("kostkarubika", new Q(this.C));
                this.A("wedkanielota", new R((Plugin)this.C));
                this.A("zlamaneserce", new Z(this.C));
                this.A("dynamit", new P(this.C));
                this.A("rozgotowanakukurydza", new BA((Plugin)this.C));
                this.A("\u0141adowanie przedmiot\u00f3w... (60%)");
                this.A("anarchicznyhelm", new G(this.C));
                this.A("anarchicznaklata", new t(this.C));
                this.A("anarchicznespodnie", new v(this.C));
                this.A("anarchicznebuty", new i(this.C));
                this.A("anarchicznyhelm2", new z(this.C));
                this.A("anarchicznaklata2", new KA(this.C));
                this.A("anarchicznespodnie2", new x(this.C));
                this.A("anarchicznebuty2", new J(this.C));
                this.A("anarchicznymiecz", new I(this.C));
                this.A("anarchicznykilof", new M(this.C));
                this.A("anarchicznyluk", new V(this.C));
                this.A("\u0141adowanie przedmiot\u00f3w... (80%)");
                this.A("zmutowanycreeper", new s(this.C));
                this.A("wzmocnionaeltra", new S((Plugin)this.C));
                this.A("antycobweb", new b((Plugin)this.C));
                this.A("balonikzhelem", new L(this.C));
                this.A("blokwidmo", new me.anarchiacore.customitems.stormitemy.items.C(this.C));
                this.A("cudownalatarnia", new m(this.C));
                this.A("przeterminowanytrunek", new Y(this.C));
                this.A("trojzabposejdona", new k(this.C));
                this.A("wyrzutniahydroklatki", new j(this.C));
                this.A("watacukrowa", new g((Plugin)this.C));
                this.A("zaczarowanie", new K((Plugin)this.C));
                if (!this.D) {
                    this.A("rozdzkailuzjonisty", new DA(this.C));
                }
                this.A("\u0141adowanie przedmiot\u00f3w... (95%)");
                if (this.E) {
                    try {
                        this.A("turbotrap", new CA(this.C));
                        this.A("turbodomek", new JA(this.C));
                    }
                    catch (NoClassDefFoundError noClassDefFoundError) {
                        this.C.getLogger().warning("Nie uda\u0142o si\u0119 zainicjalizowa\u0107 przedmiot\u00f3w zale\u017cnych od WorldEdit: " + noClassDefFoundError.getMessage());
                    }
                }
                Bukkit.getScheduler().runTask((Plugin)this.C, () -> {
                    try {
                        Object object;
                        N n2 = new N(this.C);
                        this.A("sakiewkadropu", n2);
                        this.C.getServer().getPluginManager().registerEvents((Listener)n2, (Plugin)this.C);
                        D d2 = new D(this.C);
                        this.A("plecakdrakuli", d2);
                        this.C.getServer().getPluginManager().registerEvents((Listener)d2, (Plugin)this.C);
                        if (!this.D && (object = this.B("rozdzkailuzjonisty")) != null && object instanceof Listener) {
                            this.C.getServer().getPluginManager().registerEvents((Listener)object, (Plugin)this.C);
                        }
                        long l3 = System.currentTimeMillis() - l2;
                        this.A("Wszystkie przedmioty zosta\u0142y zainicjalizowane! (100%) Czas: " + l3 + "ms");
                        this.C();
                    }
                    catch (Exception exception) {
                        this.C.getLogger().severe("B\u0142\u0105d podczas rejestracji listener\u00f3w: " + exception.getMessage());
                        exception.printStackTrace();
                        this.C();
                    }
                });
            }
            catch (Exception exception) {
                this.C.getLogger().severe("B\u0142\u0105d podczas asynchronicznej inicjalizacji przedmiot\u00f3w: " + exception.getMessage());
                exception.printStackTrace();
                this.C();
            }
        });
    }

    private void A(String string, Object object) {
        if (object != null) {
            this.B.put((Object)string, object);
            this.B(string, object);
        }
    }

    private void B(String string, Object object) {
        Supplier<ItemStack> supplier = this.A(object);
        if (supplier != null) {
            ItemRegistry.register(string, supplier, object);
        }
    }

    private Supplier<ItemStack> A(Object object) {
        if (object instanceof H) {
            return ((H)object)::getItem;
        }
        if (object instanceof d) {
            return ((d)object)::getItem;
        }
        if (object instanceof u) {
            return ((u)object)::getItem;
        }
        if (object instanceof n) {
            return ((n)object)::getItem;
        }
        if (object instanceof l) {
            return ((l)object)::getItem;
        }
        if (object instanceof IA) {
            return ((IA)object)::getItem;
        }
        if (object instanceof O) {
            return ((O)object)::getItem;
        }
        if (object instanceof p) {
            return ((p)object)::getItem;
        }
        if (object instanceof T) {
            return ((T)object)::getItem;
        }
        if (object instanceof X) {
            return ((X)object)::getItem;
        }
        if (object instanceof B) {
            return ((B)object)::getItem;
        }
        if (object instanceof a) {
            return ((a)object)::getItem;
        }
        if (object instanceof W) {
            return ((W)object)::getItem;
        }
        if (object instanceof R) {
            return ((R)object)::getItem;
        }
        if (object instanceof o) {
            return ((o)object)::getItem;
        }
        if (object instanceof y) {
            return ((y)object)::getItem;
        }
        if (object instanceof c) {
            return ((c)object)::getItem;
        }
        if (object instanceof HA) {
            return ((HA)object)::getItem;
        }
        if (object instanceof e) {
            return ((e)object)::getItem;
        }
        if (object instanceof f) {
            return ((f)object)::getItem;
        }
        if (object instanceof Kosa) {
            return ((Kosa)object)::getItem;
        }
        if (object instanceof EA) {
            return ((EA)object)::getItem;
        }
        if (object instanceof AA) {
            return ((AA)object)::getItem;
        }
        if (object instanceof q) {
            return ((q)object)::getItem;
        }
        if (object instanceof FA) {
            return ((FA)object)::getItem;
        }
        if (object instanceof F) {
            return ((F)object)::getItem;
        }
        if (object instanceof $A) {
            return (($A)object)::getItem;
        }
        if (object instanceof U) {
            return ((U)object)::getItem;
        }
        if (object instanceof h) {
            return ((h)object)::getItem;
        }
        if (object instanceof A) {
            return ((A)object)::getItem;
        }
        if (object instanceof GA) {
            return ((GA)object)::getItem;
        }
        if (object instanceof Q) {
            return ((Q)object)::getItem;
        }
        if (object instanceof R) {
            return ((R)object)::getItem;
        }
        if (object instanceof Z) {
            return ((Z)object)::getItem;
        }
        if (object instanceof P) {
            return ((P)object)::getItem;
        }
        if (object instanceof BA) {
            return ((BA)object)::getItem;
        }
        if (object instanceof G) {
            return ((G)object)::A;
        }
        if (object instanceof t) {
            return ((t)object)::A;
        }
        if (object instanceof v) {
            return ((v)object)::A;
        }
        if (object instanceof i) {
            return ((i)object)::A;
        }
        if (object instanceof z) {
            return ((z)object)::A;
        }
        if (object instanceof KA) {
            return ((KA)object)::A;
        }
        if (object instanceof x) {
            return ((x)object)::A;
        }
        if (object instanceof J) {
            return ((J)object)::A;
        }
        if (object instanceof I) {
            return ((I)object)::A;
        }
        if (object instanceof M) {
            return ((M)object)::A;
        }
        if (object instanceof V) {
            return ((V)object)::A;
        }
        if (object instanceof s) {
            return ((s)object)::getItem;
        }
        if (object instanceof S) {
            return ((S)object)::getItem;
        }
        if (object instanceof b) {
            return ((b)object)::getItem;
        }
        if (object instanceof L) {
            return ((L)object)::getItem;
        }
        if (object instanceof me.anarchiacore.customitems.stormitemy.items.C) {
            return ((me.anarchiacore.customitems.stormitemy.items.C)object)::getItem;
        }
        if (object instanceof m) {
            return ((m)object)::getItem;
        }
        if (object instanceof Y) {
            return ((Y)object)::getItem;
        }
        if (object instanceof k) {
            return ((k)object)::getItem;
        }
        if (object instanceof j) {
            return ((j)object)::getItem;
        }
        if (object instanceof g) {
            return ((g)object)::getItem;
        }
        if (object instanceof K) {
            return ((K)object)::getItem;
        }
        if (object instanceof DA) {
            return ((DA)object)::getItem;
        }
        if (object instanceof CA) {
            return ((CA)object)::getItem;
        }
        if (object instanceof JA) {
            return ((JA)object)::getItem;
        }
        if (object instanceof N) {
            return ((N)object)::getItem;
        }
        if (object instanceof D) {
            return ((D)object)::getItem;
        }
        return null;
    }

    private Object B(String string) {
        return this.B.get((Object)string);
    }

    private void A(String string) {
        Bukkit.getConsoleSender().sendMessage("\u00a78[\u00a72StormItemy\u00a78] \u00a77" + string);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void C() {
        Object object = this.A;
        synchronized (object) {
            this.C.setItemsInitialized(true);
            this.A.notifyAll();
        }
    }

    public void B() {
        try {
            Object object = this.B("plecakdrakuli");
            if (object instanceof D) {
                D d2 = (D)object;
                d2.saveAllBackpacksOnShutdown();
            }
        }
        catch (Exception exception) {
            this.C.getLogger().warning("B\u0142\u0105d przy zapisywaniu plecak\u00f3w: " + exception.getMessage());
        }
    }
}
