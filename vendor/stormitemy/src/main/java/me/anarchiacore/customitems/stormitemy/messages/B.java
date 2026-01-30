/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.CharSequence
 *  java.lang.IllegalArgumentException
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.lang.System
 *  java.util.HashMap
 *  java.util.Iterator
 *  java.util.Map
 *  java.util.Map$Entry
 *  java.util.UUID
 *  java.util.concurrent.ConcurrentHashMap
 *  java.util.regex.Pattern
 *  net.md_5.bungee.api.ChatMessageType
 *  net.md_5.bungee.api.chat.TextComponent
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.configuration.ConfigurationSection
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package me.anarchiacore.customitems.stormitemy.messages;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import me.anarchiacore.customitems.stormitemy.utils.color.A;

public class B
implements Listener {
    private static final String D = "c3Rvcm1jb2Rl";
    private static final long L = 300000L;
    private final Plugin H;
    private final Map<UUID, Map<String, Integer>> J = new ConcurrentHashMap();
    private final Map<UUID, String> P = new ConcurrentHashMap();
    private volatile boolean N = false;
    private BukkitRunnable M;
    private boolean I;
    private final Map<String, String> F = new HashMap();
    private String G;
    private String E;
    private int B;
    private final Map<String, String> O = new HashMap();
    private final Map<String, String> C = new HashMap();
    private final Pattern K = Pattern.compile((String)"&([0-9a-fA-F]).*");
    private volatile long A = 0L;

    public B(Plugin plugin) {
        this.H = plugin;
        this.A();
        this.loadConfig();
        this.B();
        plugin.getServer().getPluginManager().registerEvents((Listener)this, plugin);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent playerQuitEvent) {
        UUID uUID = playerQuitEvent.getPlayer().getUniqueId();
        this.J.remove((Object)uUID);
        this.P.remove((Object)uUID);
    }

    private void A() {
        this.C.put("0", "&#000000");
        this.C.put("1", "&#0000AA");
        this.C.put("2", "&#00AA00");
        this.C.put("3", "&#00AAAA");
        this.C.put("4", "&#AA0000");
        this.C.put("5", "&#AA00AA");
        this.C.put("6", "&#FFAA00");
        this.C.put("7", "&#AAAAAA");
        this.C.put("8", "&#555555");
        this.C.put("9", "&#5555FF");
        this.C.put("a", "&#55FF55");
        this.C.put("b", "&#55FFFF");
        this.C.put("c", "&#FF5555");
        this.C.put("d", "&#FF55FF");
        this.C.put("e", "&#FFFF55");
        this.C.put("f", "&#FFFFFF");
    }

    public void loadConfig() {
        long l2 = System.currentTimeMillis();
        if (l2 - this.A < 300000L && this.A > 0L) {
            return;
        }
        ConfigurationSection configurationSection = this.H.getConfig().getConfigurationSection("actionbar");
        if (configurationSection == null) {
            configurationSection = this.H.getConfig().createSection("actionbar");
            configurationSection.set("enabled", (Object)true);
            configurationSection.set("separator", (Object)"&7\u00d7");
            configurationSection.set("time_format", (Object)"&8({color}{time}s&8)");
            this.H.saveConfig();
        }
        this.I = configurationSection.getBoolean("enabled", true);
        this.G = me.anarchiacore.customitems.stormitemy.utils.color.A.C(configurationSection.getString("separator", "&7\u00d7"));
        this.E = configurationSection.getString("time_format", "&8({color}{time}s&8)");
        this.B = configurationSection.getInt("max_length", 120);
        ConfigurationSection configurationSection2 = configurationSection.getConfigurationSection("colors");
        if (configurationSection2 == null) {
            configurationSection2 = configurationSection.createSection("colors");
        }
        this.F.clear();
        for (String string : configurationSection2.getKeys(false)) {
            String string2 = configurationSection2.getString(string);
            this.F.put(string, string2);
        }
        ConfigurationSection displayNames = configurationSection.getConfigurationSection("display_names");
        if (displayNames == null) {
            displayNames = configurationSection.createSection("display_names");
        }
        this.O.clear();
        for (String string2 : displayNames.getKeys(false)) {
            String string = displayNames.getString(string2);
            this.O.put(string2, string);
        }
        this.A("smoczymiecz", "&#FF5722");
        this.A("bombardamaxima", "&#9C27B0");
        this.A("boskitopor", "&#FFC107");
        this.A("sniezka", "&#E0F7FA");
        this.A("splesnialakanapka", "&#8BC34A");
        this.A("turbotrap", "&#F44336");
        this.A("turbodomek", "&#4CAF50");
        this.A("wedkasurferka", "&#2196F3");
        this.A("wedkanielota", "&#3F51B5");
        this.A("lopatagrincha", "&#9E9E9E");
        this.A("piernik", "&#795548");
        this.A("cieplemleko", "&#FAFAFA");
        this.A("rozakupidyna", "&#E91E63");
        this.A("parawan", "&#00ACC1");
        this.A("lizak", "&#FF4081");
        this.A("rozga", "&#A5D6A7");
        this.A("kupaanarchi", "&#6D4C41");
        this.A("koronaanarchi", "&#FFD600");
        this.A("totemuaskawienia", "&#66BB6A");
        this.A("siekieragrincha", "&#BDBDBD");
        this.A("lukkupidyna", "&#EC407A");
        this.A("marchewkowymiecz", "&#FF8A65");
        this.A("kosa", "&#78909C");
        this.A("excalibur", "&#1976D2");
        this.A("krewwampira", "&#D32F2F");
        this.A("marchewkowakusza", "&#FFA726");
        this.A("lewejajko", "&#FFEE58");
        this.A("zajeczymiecz", "&#D4E157");
        this.A("zatrutyolowek", "&#8E24AA");
        this.A("sakiewkadropu", "&#FFEB3B");
        this.A("wampirzejablko", "&#C62828");
        this.A("arcusmagnus", "&#673AB7");
        this.A("piekielnatarcza", "&#BF360C");
        this.A("kostkarubika", "&#00897B");
        this.A("zlamaneserce", "&#AD1457");
        this.A("dynamit", "&#E53935");
        this.A("rozgotowanakukurydza", "&#FDD835");
        this.A("zmutowanycreeper", "&#7CB342");
        this.A("wzmocnionaeltra", "&#1E88E5");
        this.A("anarchicznymiecz", "&#546E7A");
        this.A("anarchicznykilof", "&#455A64");
        this.A("anarchicznyluk", "&#37474F");
        this.A("antycobweb", "&#CFD8DC");
        this.A("anarchicznyhelm", "&#424242");
        this.A("anarchicznaklata", "&#616161");
        this.A("anarchicznespodnie", "&#757575");
        this.A("anarchicznebuty", "&#9E9E9E");
        this.A("anarchicznyhelm2", "&#212121");
        this.A("anarchicznaklata2", "&#414141");
        this.A("anarchicznespodnie2", "&#515151");
        this.A("anarchicznebuty2", "&#717171");
        this.A("creeper_spawn_egg", "&#7CB342");
        this.A("balonikzhelem", "&#00AFFF");
        this.A("blokwidmo", "&#E15A5A");
        this.A("przeterminowanytrunek", "&#2E7D32");
        this.A("watacukrowa", "&#FFB6C1");
        this.A("olaf", "&#55FFFF");
        this.A("cudownalatarnia", "&#FF00CA");
        this.A = System.currentTimeMillis();
        if (!this.N) {
            this.C();
            this.N = true;
            this.H.saveConfig();
        }
    }

    private void C() {
        this.B("smoczymiecz", "Smoczy miecz");
        this.B("bombardamaxima", "Bombarda Maxima");
        this.B("boskitopor", "Boski Top\u00f3r");
        this.B("sniezka", "\u015anie\u017cka");
        this.B("splesnialakanapka", "Sple\u015bnia\u0142a Kanapka");
        this.B("turbotrap", "Turbo Trap");
        this.B("turbodomek", "Turbo Domek");
        this.B("wedkasurferka", "W\u0119dka Surferka");
        this.B("wedkanielota", "W\u0119dka Nielota");
        this.B("lopatagrincha", "\u0141opata Grincha");
        this.B("piernik", "Piernik");
        this.B("cieplemleko", "Ciep\u0142e Mleko");
        this.B("rozakupidyna", "R\u00f3\u017ca Kupidyna");
        this.B("parawan", "Parawan");
        this.B("lizak", "Lizak");
        this.B("rozga", "R\u00f3zga");
        this.B("kupaanarchi", "Kupa Anarchi");
        this.B("koronaanarchi", "Korona Anarchi");
        this.B("totemuaskawienia", "Totem U\u0142askawienia");
        this.B("siekieragrincha", "Siekiera Grincha");
        this.B("lukkupidyna", "\u0141uk Kupidyna");
        this.B("marchewkowymiecz", "Marchewkowy Miecz");
        this.B("kosa", "Kosa");
        this.B("excalibur", "Excalibur");
        this.B("krewwampira", "Krew Wampira");
        this.B("marchewkowakusza", "Marchewkowa Kusza");
        this.B("lewejajko", "Lewe Jajko");
        this.B("zajeczymiecz", "Zaj\u0119czy Miecz");
        this.B("zatrutyolowek", "Zatruty O\u0142\u00f3wek");
        this.B("sakiewkadropu", "Sakiewka Dropu");
        this.B("wampirzejablko", "Wampirze Jab\u0142ko");
        this.B("arcusmagnus", "Arcus Magnus");
        this.B("piekielnatarcza", "Piekielna Tarcza");
        this.B("kostkarubika", "Kostka Rubika");
        this.B("zlamaneserce", "Z\u0142amane Serce");
        this.B("dynamit", "Dynamit");
        this.B("rozgotowanakukurydza", "Rozgotowana Kukurydza");
        this.B("zmutowanycreeper", "Zmutowany Creeper");
        this.B("wzmocnionaeltra", "Wzmocniona Elytra");
        this.B("anarchicznymiecz", "Anarchiczny Miecz");
        this.B("anarchicznykilof", "Anarchiczny Kilof");
        this.B("anarchicznyluk", "Anarchiczny \u0141uk");
        this.B("antycobweb", "AntyCobweb");
        this.B("anarchicznyhelm", "Anarchiczny He\u0142m");
        this.B("anarchicznaklata", "Anarchiczna Klata");
        this.B("anarchicznespodnie", "Anarchiczne Spodnie");
        this.B("anarchicznebuty", "Anarchiczne Buty");
        this.B("anarchicznyhelm2", "Anarchiczny He\u0142m II");
        this.B("anarchicznaklata2", "Anarchiczna Klata II");
        this.B("anarchicznespodnie2", "Anarchiczne Spodnie II");
        this.B("anarchicznebuty2", "Anarchiczne Buty II");
        this.B("creeper_spawn_egg", "Zmutowany Creeper");
        this.B("balonikzhelem", "Balonik z helem");
        this.B("blokwidmo", "Blok Widmo");
        this.B("przeterminowanytrunek", "Przeterminowany Trunek");
        this.B("watacukrowa", "Wata Cukrowa");
        this.B("olaf", "Olaf");
        this.B("cudownalatarnia", "Cudowna Latarnia");
    }

    private void A(String string, String string2) {
        ConfigurationSection configurationSection = this.H.getConfig().getConfigurationSection("actionbar.colors");
        if (configurationSection != null && !configurationSection.contains(string)) {
            configurationSection.set(string, (Object)string2);
            this.F.put(string, string2);
        }
    }

    private void B(String string, String string2) {
        ConfigurationSection configurationSection = this.H.getConfig().getConfigurationSection("actionbar.display_names");
        if (configurationSection != null && !configurationSection.contains(string)) {
            configurationSection.set(string, (Object)string2);
            this.O.put(string, string2);
        }
    }

    /*
     * Exception decompiling
     */
    private String B(String var1_1) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * java.lang.NullPointerException: Attempt to invoke virtual method 'kc.o kc.a.d(kc.r)' on a null object reference
         *     at nb.j0.c(SourceFile:16)
         *     at nb.j0.b(SourceFile:85)
         *     at nb.j0.a(SourceFile:36)
         *     at ib.f.d(SourceFile:207)
         *     at ib.f.e(SourceFile:7)
         *     at ib.f.c(SourceFile:95)
         *     at rc.f.n(SourceFile:11)
         *     at pc.i.m(SourceFile:5)
         *     at pc.d.K(SourceFile:92)
         *     at pc.d.g0(SourceFile:1)
         *     at fb.b.d(SourceFile:191)
         *     at fb.b.c(SourceFile:145)
         *     at fb.a.a(SourceFile:108)
         *     at com.thesourceofcode.jadec.decompilers.JavaExtractionWorker.decompileWithCFR(SourceFile:76)
         *     at com.thesourceofcode.jadec.decompilers.JavaExtractionWorker.doWork(SourceFile:110)
         *     at com.thesourceofcode.jadec.decompilers.BaseDecompiler.withAttempt(SourceFile:3)
         *     at com.thesourceofcode.jadec.workers.DecompilerWorker.d(SourceFile:53)
         *     at com.thesourceofcode.jadec.workers.DecompilerWorker.b(SourceFile:1)
         *     at e7.a.run(SourceFile:1)
         *     at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1154)
         *     at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:652)
         *     at java.lang.Thread.run(Thread.java:1563)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    public void registerCooldown(Player player, String string, int n2) {
        String string2;
        Material material;
        if (!this.I) {
            return;
        }
        if (string.equals((Object)"player_head") && player.getInventory().getItemInMainHand().getType() == Material.PLAYER_HEAD && (material = player.getInventory().getItemInMainHand()).hasItemMeta() && material.getItemMeta().hasDisplayName() && (string2 = material.getItemMeta().getDisplayName().toLowerCase()).contains((CharSequence)"balonik") && string2.contains((CharSequence)"helem")) {
            string = "balonikzhelem";
        }
        if (!this.isPluginItem(string)) {
            return;
        }
        try {
            material = Material.valueOf((String)string.toUpperCase());
            return;
        }
        catch (IllegalArgumentException illegalArgumentException) {
            UUID uUID2 = player.getUniqueId();
            ((Map)this.J.computeIfAbsent((Object)uUID2, uUID -> new HashMap())).put(string, n2);
            this.A(player);
            return;
        }
    }

    public void registerStatusMessage(Player player, String string) {
        if (!this.I) {
            return;
        }
        UUID uUID = player.getUniqueId();
        this.P.put(uUID, string);
        this.A(player);
    }

    public void removeStatusMessage(Player player) {
        UUID uUID = player.getUniqueId();
        this.P.remove((Object)uUID);
        this.A(player);
    }

    public void removeCooldown(Player player, String string) {
        UUID uUID = player.getUniqueId();
        if (this.J.containsKey((Object)uUID)) {
            ((Map)this.J.get((Object)uUID)).remove((Object)string);
            if (((Map)this.J.get((Object)uUID)).isEmpty()) {
                this.J.remove((Object)uUID);
            }
        }
    }

    private void B() {
        if (this.M != null) {
            this.M.cancel();
        }
        this.M = new BukkitRunnable(){

            public void run() {
                if (!B.this.I) {
                    return;
                }
                for (UUID uUID : B.this.J.keySet()) {
                    Player player = Bukkit.getPlayer((UUID)uUID);
                    if (player == null || !player.isOnline()) {
                        B.this.J.remove((Object)uUID);
                        continue;
                    }
                    B.this.A(player);
                }
            }
        };
        this.M.runTaskTimer(this.H, 0L, 20L);
    }

    private void A(Player player) {
        String string;
        if (player == null || !player.isOnline()) {
            return;
        }
        UUID uUID = player.getUniqueId();
        Map map = (Map)this.J.get((Object)uUID);
        String string2 = (String)this.P.get((Object)uUID);
        if ((map == null || map.isEmpty()) && string2 == null) {
            this.J.remove((Object)uUID);
            this.P.remove((Object)uUID);
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        if (string2 != null && !string2.isEmpty()) {
            stringBuilder.append(string2);
        }
        if (map != null && !map.isEmpty()) {
            string = new HashMap(map);
            if (stringBuilder.length() > 0) {
                stringBuilder.append(" &8| ");
            }
            boolean bl = true;
            for (Map.Entry entry : string.entrySet()) {
                String string3 = (String)entry.getKey();
                int n2 = (Integer)entry.getValue();
                --n2;
                String string4 = this.A(string3);
                String string5 = this.B(string3);
                if (!bl) {
                    stringBuilder.append(" ").append(this.G).append(" ");
                }
                if (string3.equals((Object)"trojzabposejdona_riptide")) {
                    var14_14 = n2 < 1 ? "<1" : String.valueOf((int)n2);
                    stringBuilder.append("&3Tr\u00f3jz\u0105b &fdost\u0119pny za &b").append(var14_14).append("s&f!");
                } else {
                    var14_14 = n2 < 1 ? "<1" : String.valueOf((int)n2);
                    stringBuilder.append("&f").append(string4).append(" ").append(this.E.replace((CharSequence)"{color}", (CharSequence)string5).replace((CharSequence)"{time}", (CharSequence)var14_14));
                }
                bl = false;
                if (n2 <= 0) {
                    map.remove((Object)string3);
                    continue;
                }
                map.put(string3, n2);
            }
        }
        if (stringBuilder.length() > 0) {
            string = stringBuilder.toString();
            if (this.B > 0 && string.length() > this.B) {
                string = string.substring(0, this.B - 3) + "...";
            }
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText((String)me.anarchiacore.customitems.stormitemy.utils.color.A.C(string)));
        }
        if (map != null && map.isEmpty()) {
            this.J.remove((Object)uUID);
        }
    }

    /*
     * Exception decompiling
     */
    private String A(String var1_1) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * ob.f0$e
         *     at ob.f0.e(SourceFile:35)
         *     at ob.f0.a(SourceFile:1)
         *     at ob.f0$d.a(SourceFile:68)
         *     at qb.n.i(SourceFile:13)
         *     at qb.e.i(SourceFile:9)
         *     at qb.l.i(SourceFile:14)
         *     at qb.n.i(SourceFile:3)
         *     at ob.f0.g(SourceFile:649)
         *     at ob.f0.d(SourceFile:37)
         *     at ib.f.d(SourceFile:235)
         *     at ib.f.e(SourceFile:7)
         *     at ib.f.c(SourceFile:95)
         *     at rc.f.n(SourceFile:11)
         *     at pc.i.m(SourceFile:5)
         *     at pc.d.K(SourceFile:92)
         *     at pc.d.g0(SourceFile:1)
         *     at fb.b.d(SourceFile:191)
         *     at fb.b.c(SourceFile:145)
         *     at fb.a.a(SourceFile:108)
         *     at com.thesourceofcode.jadec.decompilers.JavaExtractionWorker.decompileWithCFR(SourceFile:76)
         *     at com.thesourceofcode.jadec.decompilers.JavaExtractionWorker.doWork(SourceFile:110)
         *     at com.thesourceofcode.jadec.decompilers.BaseDecompiler.withAttempt(SourceFile:3)
         *     at com.thesourceofcode.jadec.workers.DecompilerWorker.d(SourceFile:53)
         *     at com.thesourceofcode.jadec.workers.DecompilerWorker.b(SourceFile:1)
         *     at e7.a.run(SourceFile:1)
         *     at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1154)
         *     at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:652)
         *     at java.lang.Thread.run(Thread.java:1563)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    public void disable() {
        if (this.M != null) {
            this.M.cancel();
            this.M = null;
        }
        this.J.clear();
    }

    public boolean isEnabled() {
        return this.I;
    }

    public void setEnabled(boolean bl) {
        this.I = bl;
        ConfigurationSection configurationSection = this.H.getConfig().getConfigurationSection("actionbar");
        if (configurationSection != null) {
            configurationSection.set("enabled", (Object)bl);
            this.H.saveConfig();
        }
        if (bl) {
            this.loadConfig();
        }
    }

    /*
     * Exception decompiling
     */
    public boolean isPluginItem(String var1_1) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * ob.f0$e
         *     at ob.f0.e(SourceFile:35)
         *     at ob.f0.a(SourceFile:1)
         *     at ob.f0$d.a(SourceFile:68)
         *     at qb.n.i(SourceFile:13)
         *     at qb.e.i(SourceFile:9)
         *     at qb.l.i(SourceFile:14)
         *     at qb.n.i(SourceFile:3)
         *     at ob.f0.g(SourceFile:649)
         *     at ob.f0.d(SourceFile:37)
         *     at ib.f.d(SourceFile:235)
         *     at ib.f.e(SourceFile:7)
         *     at ib.f.c(SourceFile:95)
         *     at rc.f.n(SourceFile:11)
         *     at pc.i.m(SourceFile:5)
         *     at pc.d.K(SourceFile:92)
         *     at pc.d.g0(SourceFile:1)
         *     at fb.b.d(SourceFile:191)
         *     at fb.b.c(SourceFile:145)
         *     at fb.a.a(SourceFile:108)
         *     at com.thesourceofcode.jadec.decompilers.JavaExtractionWorker.decompileWithCFR(SourceFile:76)
         *     at com.thesourceofcode.jadec.decompilers.JavaExtractionWorker.doWork(SourceFile:110)
         *     at com.thesourceofcode.jadec.decompilers.BaseDecompiler.withAttempt(SourceFile:3)
         *     at com.thesourceofcode.jadec.workers.DecompilerWorker.d(SourceFile:53)
         *     at com.thesourceofcode.jadec.workers.DecompilerWorker.b(SourceFile:1)
         *     at e7.a.run(SourceFile:1)
         *     at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1154)
         *     at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:652)
         *     at java.lang.Thread.run(Thread.java:1563)
         */
        throw new IllegalStateException("Decompilation failed");
    }
}
