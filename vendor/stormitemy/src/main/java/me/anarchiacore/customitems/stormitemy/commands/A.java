/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.io.File
 *  java.lang.Boolean
 *  java.lang.CharSequence
 *  java.lang.Class
 *  java.lang.Exception
 *  java.lang.Integer
 *  java.lang.NumberFormatException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.lang.reflect.Method
 *  java.util.ArrayList
 *  java.util.Arrays
 *  java.util.Collection
 *  java.util.Collections
 *  java.util.HashMap
 *  java.util.List
 *  java.util.Map
 *  java.util.function.Consumer
 *  java.util.stream.Collectors
 *  org.bukkit.Bukkit
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 */
package me.anarchiacore.customitems.stormitemy.commands;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import me.anarchiacore.customitems.stormitemy.Main;
import me.anarchiacore.customitems.stormitemy.config.B;
import me.anarchiacore.customitems.stormitemy.core.ItemRegistry;
import me.anarchiacore.customitems.stormitemy.items.EA;
import me.anarchiacore.customitems.stormitemy.ui.gui.E;

public class A {
    private final Main C;
    private final me.anarchiacore.customitems.stormitemy.messages.B A;
    private final me.anarchiacore.customitems.stormitemy.books.A B;

    public A(Main main, me.anarchiacore.customitems.stormitemy.messages.B b2, me.anarchiacore.customitems.stormitemy.books.A a2) {
        this.C = main;
        this.A = b2;
        this.B = a2;
    }

    public boolean C(CommandSender commandSender, String[] stringArray) {
        Player player;
        if (stringArray.length < 2) {
            me.anarchiacore.customitems.stormitemy.utils.language.A.A(commandSender, "usage");
            return true;
        }
        if (!this.C.areItemsInitialized()) {
            commandSender.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C("\u00a78[\u00a74B\u0141\u0104D\u00a78] \u00a77Przedmioty nie zosta\u0142y jeszcze za\u0142adowane. Spr\u00f3buj ponownie za chwil\u0119."));
            return true;
        }
        String string = stringArray[1].toLowerCase();
        Object object = stringArray.length >= 3 ? Bukkit.getPlayer((String)stringArray[2]) : (player = commandSender instanceof Player ? (Player)commandSender : null);
        if (player == null) {
            me.anarchiacore.customitems.stormitemy.utils.language.A.A(commandSender, "invalidPlayer");
            return true;
        }
        ItemStack itemStack = this.A(string);
        if (itemStack == null) {
            HashMap hashMap = new HashMap();
            hashMap.put((Object)"item", (Object)string);
            me.anarchiacore.customitems.stormitemy.utils.language.A.A(commandSender, "notFound", (Map<String, String>)hashMap);
            return true;
        }
        player.getInventory().addItem(new ItemStack[]{itemStack});
        HashMap hashMap = new HashMap();
        hashMap.put((Object)"item", (Object)string);
        hashMap.put((Object)"player", (Object)player.getName());
        me.anarchiacore.customitems.stormitemy.utils.language.A.A(commandSender, "give", (Map<String, String>)hashMap);
        return true;
    }

    public ItemStack A(String string) {
        ItemStack itemStack;
        E e2;
        if ((string.equals((Object)"turbotrap") || string.equals((Object)"turbodomek")) && !this.C.isWorldEditPresent()) {
            return null;
        }
        if (string.equals((Object)"rozdzka") || string.equals((Object)"rozdzkailuzjonisty") || string.equals((Object)"rozdzka_iluzjonisty") || string.equals((Object)"r\u00f3\u017cd\u017cka") || string.equals((Object)"r\u00f3\u017cd\u017ckailuzjonisty") || string.equals((Object)"r\u00f3\u017cd\u017cka_iluzjonisty")) {
            if (this.C.isCitizensPresent()) {
                return null;
            }
            string = "rozdzkailuzjonisty";
        }
        if (string.equals((Object)"sniezzynka")) {
            string = "sniezynka";
        }
        if (string.equals((Object)"przeterminowany_trunek")) {
            string = "przeterminowanytrunek";
        }
        if (string.startsWith("ksiega") || string.startsWith("ksi\u0119ga")) {
            return this.C(string);
        }
        Object object = this.C.getItem(string);
        if (object != null) {
            try {
                if (this.C.isItemDisabled(string)) {
                    return null;
                }
                ItemStack itemStack2 = ItemRegistry.getItemStack(string);
                if (itemStack2 != null) {
                    return itemStack2;
                }
                Method method = object.getClass().getMethod("getItem", new Class[0]);
                return (ItemStack)method.invoke(object, new Object[0]);
            }
            catch (Exception exception) {
                this.C.getLogger().warning("B\u0142\u0105d przy pobieraniu przedmiotu " + string + ": " + exception.getMessage());
            }
        }
        if ((e2 = this.C.getCustomItemsManager()) != null && (itemStack = e2.G(string)) != null && !e2.B(string)) {
            return itemStack.clone();
        }
        return null;
    }

    public ItemStack C(String string) {
        try {
            String string2 = null;
            if (string.contains((CharSequence)"zatrucia")) {
                string2 = "zatrucie";
            } else if (string.contains((CharSequence)"niepdopalenie")) {
                string2 = "niepodpalenie";
            } else if (string.contains((CharSequence)"regeneracji")) {
                string2 = "regeneracja";
            } else if (string.contains((CharSequence)"szybko")) {
                string2 = "szybkosc";
            } else if (string.contains((CharSequence)"pospiech")) {
                string2 = "pospiech";
            } else if (string.contains((CharSequence)"spowolnienia")) {
                string2 = "spowolnienie";
            } else if (string.contains((CharSequence)"wampiryzmu")) {
                string2 = "wampiryzm";
            } else if (string.contains((CharSequence)"uniku")) {
                string2 = "unik";
            } else if (string.contains((CharSequence)"obrazen")) {
                string2 = "obrazenia_krytyczne";
            } else if (string.contains((CharSequence)"dodatkowego")) {
                string2 = "dodatkowy_drop";
            } else if (string.contains((CharSequence)"ulaskawienia")) {
                string2 = "ulaskawienie";
            } else if (string.contains((CharSequence)"wytrzyma")) {
                string2 = "wytrzymalosc";
            }
            if (string2 != null && this.B != null) {
                return this.B.A(string2).getBookItem();
            }
        }
        catch (Exception exception) {
            this.C.getLogger().warning("B\u0142\u0105d przy pobieraniu ksi\u0119gi: " + exception.getMessage());
        }
        return null;
    }

    public boolean B(CommandSender commandSender) {
        long l2 = System.currentTimeMillis();
        me.anarchiacore.customitems.stormitemy.core.A a2 = new me.anarchiacore.customitems.stormitemy.core.A(this.C, this.C.getItems(), this.C.getInitializationLock());
        a2.A();
        if (this.C.getTexturePackManager() != null) {
            this.C.getTexturePackManager().H();
        }
        long l3 = System.currentTimeMillis() - l2;
        HashMap hashMap = new HashMap();
        hashMap.put((Object)"time", (Object)String.valueOf((long)l3));
        me.anarchiacore.customitems.stormitemy.utils.language.A.A(commandSender, "reload", (Map<String, String>)hashMap);
        return true;
    }

    public boolean C(CommandSender commandSender) {
        String string = this.C.getDescription().getVersion();
        me.anarchiacore.customitems.stormitemy.utils.update.A a2 = new me.anarchiacore.customitems.stormitemy.utils.update.A(this.C, 122499);
        a2.A(string, (Consumer<Boolean>)((Consumer)bl -> {
            if (bl == null) {
                commandSender.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8[&x&B&3&0&0&F&F\ud83e\ude93&8] &7Nie uda\u0142o si\u0119 sprawdzi\u0107 aktualizacji."));
                return;
            }
            if (bl.booleanValue()) {
                a2.A((Consumer<String>)((Consumer)string2 -> {
                    if (string2 != null) {
                        commandSender.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8[&x&B&3&0&0&F&F\ud83e\ude93&8] &7Dost\u0119pna jest nowa wersja pluginu: &x&D&0&6&0&F&F" + string2 + " &7(aktualna: &f" + string + "&7)"));
                        commandSender.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8[&x&B&3&0&0&F&F\ud83e\ude93&8] &7Pobierz aktualizacj\u0119 ze strony: &fhttps://www.spigotmc.org/resources/122499/"));
                    }
                }));
            } else {
                commandSender.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8[&x&B&3&0&0&F&F\ud83e\ude93&8] &7Plugin &x&D&0&6&0&F&FSTORMITEMY &7jest aktualny (wersja: &f" + string + "&7)"));
            }
        }));
        return true;
    }

    public boolean A(CommandSender commandSender) {
        if (!commandSender.hasPermission("stormitemy.actionbar")) {
            me.anarchiacore.customitems.stormitemy.utils.language.A.A(commandSender, "noPermission");
            return true;
        }
        boolean bl = !this.A.isEnabled();
        this.A.setEnabled(bl);
        if (bl) {
            me.anarchiacore.customitems.stormitemy.utils.language.A.A(commandSender, "actionbarEnabled");
        } else {
            me.anarchiacore.customitems.stormitemy.utils.language.A.A(commandSender, "actionbarDisabled");
        }
        return true;
    }

    public boolean B(CommandSender commandSender, String[] stringArray) {
        int n2;
        if (!(commandSender instanceof Player)) {
            me.anarchiacore.customitems.stormitemy.utils.language.A.A(commandSender, "onlyPlayers");
            return true;
        }
        if (stringArray.length < 2) {
            commandSender.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C("\u00a78\u00bb \u00a77Poprawne u\u017cycie: \u00a7f/stormitemy kills \u00a78[\u00a7fliczba\u00a78]"));
            return true;
        }
        try {
            n2 = Integer.parseInt((String)stringArray[1]);
        }
        catch (NumberFormatException numberFormatException) {
            me.anarchiacore.customitems.stormitemy.utils.language.A.A(commandSender, "invalidNumber");
            return true;
        }
        Player player = (Player)commandSender;
        Object object = this.C.getItem("excalibur");
        if (object instanceof EA) {
            EA eA = (EA)object;
            boolean bl = eA.Kills(player, n2);
            if (bl) {
                HashMap hashMap = new HashMap();
                hashMap.put((Object)"kills", (Object)String.valueOf((int)n2));
                me.anarchiacore.customitems.stormitemy.utils.language.A.A(commandSender, "excaliburKills", (Map<String, String>)hashMap);
            } else {
                me.anarchiacore.customitems.stormitemy.utils.language.A.A(commandSender, "notExcalibur");
            }
        }
        return true;
    }

    public boolean D(CommandSender commandSender, String[] stringArray) {
        if (stringArray.length < 2) {
            commandSender.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C("\u00a78[\u00a7x\u00a7B\u00a73\u00a70\u00a70\u00a7F\u00a7F\ud83e\ude93\u00a78] \u00a77U\u017cycie: \u00a7f/stormitemy recover <nazwa_pliku>"));
            commandSender.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C("\u00a78[\u00a7x\u00a7B\u00a73\u00a70\u00a70\u00a7F\u00a7F\ud83e\ude93\u00a78] \u00a77Przyk\u0142ad: \u00a7f/stormitemy recover data.yml.corrupted_1760373992865"));
            return true;
        }
        String string = stringArray[1];
        File file = new File(this.C.getDataFolder(), string);
        File file2 = new File(this.C.getDataFolder(), "sakiewka_data.db");
        if (!file.exists()) {
            commandSender.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C("\u00a78[\u00a74B\u0141\u0104D\u00a78] \u00a77Plik \u00a7f" + string + " \u00a77nie istnieje!"));
            return true;
        }
        commandSender.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C("\u00a78[\u00a7x\u00a7B\u00a73\u00a70\u00a70\u00a7F\u00a7F\ud83e\ude93\u00a78] \u00a77Rozpoczynam odzyskiwanie danych z pliku \u00a7f" + string + "\u00a77..."));
        Bukkit.getScheduler().runTaskAsynchronously((Plugin)this.C, () -> {
            B b2 = new B((Plugin)this.C);
            int n2 = b2.A(file, file2);
            Bukkit.getScheduler().runTask((Plugin)this.C, () -> {
                if (n2 > 0) {
                    commandSender.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C("\u00a78[\u00a72\u2713\u00a78] \u00a7aOdzyskano \u00a7f" + n2 + " \u00a7aprzedmiot\u00f3w!"));
                    commandSender.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C("\u00a78[\u00a72StormItemy\u00a78] \u00a77Prze\u0142aduj plugin aby zobaczy\u0107 odzyskane przedmioty: \u00a7f/stormitemy reload"));
                } else {
                    commandSender.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C("\u00a78[\u00a74\u2717\u00a78] \u00a7cNie uda\u0142o si\u0119 odzyska\u0107 \u017cadnych przedmiot\u00f3w."));
                    commandSender.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C("\u00a78[\u00a74UWAGA\u00a78] \u00a77Plik mo\u017ce by\u0107 zbyt uszkodzony lub pusty."));
                }
            });
        });
        return true;
    }

    public void A(List<ItemStack> list, Object object) {
        if (object == null) {
            return;
        }
        try {
            Method method = object.getClass().getMethod("getItem", new Class[0]);
            ItemStack itemStack = (ItemStack)method.invoke(object, new Object[0]);
            if (itemStack != null) {
                list.add((Object)itemStack);
            }
        }
        catch (Exception exception) {
            this.C.getLogger().warning("B\u0142\u0105d przy dodawaniu przedmiotu do listy: " + exception.getMessage());
        }
    }

    public List<String> A(CommandSender commandSender, Command command, String string, String[] stringArray) {
        if (stringArray.length == 1) {
            return this.B(stringArray[0]);
        }
        if (stringArray.length >= 2) {
            return this.A(stringArray[0], stringArray);
        }
        return Collections.emptyList();
    }

    private List<String> B(String string) {
        List list = Arrays.asList((Object[])new String[]{"reload", "give", "kills", "actionbar", "region", "zaczarowania", "version", "panel", "customhit", "texturepack"});
        return (List)list.stream().filter(string2 -> string2.toLowerCase().startsWith(string.toLowerCase())).collect(Collectors.toList());
    }

    /*
     * Exception decompiling
     */
    private List<String> A(String var1_1, String[] var2_2) {
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

    private List<String> C(String[] stringArray) {
        if (stringArray.length == 2) {
            List list = Arrays.asList((Object[])new String[]{"wand", "create", "recreate", "delete", "list"});
            String string = stringArray[1].toLowerCase();
            return (List)list.stream().filter(string2 -> string2.toLowerCase().startsWith(string)).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    private List<String> A(String[] stringArray) {
        if (stringArray.length == 2) {
            List list = Arrays.asList((Object[])new String[]{"give", "set", "event"});
            String string = stringArray[1].toLowerCase();
            return (List)list.stream().filter(string2 -> string2.toLowerCase().startsWith(string)).collect(Collectors.toList());
        }
        if (stringArray.length == 3) {
            if (stringArray[1].equalsIgnoreCase("give")) {
                return Arrays.asList((Object[])new String[]{"1", "5", "10", "64"});
            }
            if (stringArray[1].equalsIgnoreCase("set")) {
                return Arrays.asList((Object[])new String[]{"-20", "0", "25", "50", "100"});
            }
            if (stringArray[1].equalsIgnoreCase("event")) {
                return Arrays.asList((Object[])new String[]{"30s", "1m", "5m", "10m", "1h", "stop"});
            }
        }
        return Collections.emptyList();
    }

    private List<String> E(String[] stringArray) {
        if (stringArray.length == 2) {
            List list = Arrays.asList((Object[])new String[]{"set", "info"});
            String string = stringArray[1].toLowerCase();
            return (List)list.stream().filter(string2 -> string2.toLowerCase().startsWith(string)).collect(Collectors.toList());
        }
        if (stringArray.length == 3 && stringArray[1].equalsIgnoreCase("set")) {
            return Arrays.asList((Object[])new String[]{"1.0", "1.5", "2.0", "2.5", "3.0", "5.0", "10.0"});
        }
        return Collections.emptyList();
    }

    private List<String> D(String[] stringArray) {
        if (stringArray.length == 2) {
            List list = Arrays.asList((Object[])new String[]{"accept", "deny", "status"});
            String string = stringArray[1].toLowerCase();
            return (List)list.stream().filter(string2 -> string2.toLowerCase().startsWith(string)).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    /*
     * Exception decompiling
     */
    public boolean A(CommandSender var1_1, String[] var2_2) {
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

    private List<String> B(String[] stringArray) {
        if (stringArray.length < 2) {
            return Collections.emptyList();
        }
        String string = stringArray[1].toLowerCase();
        ArrayList arrayList = new ArrayList((Collection)this.C.getItems().keySet().stream().filter(string2 -> string2.toLowerCase().startsWith(string)).sorted().collect(Collectors.toList()));
        E e2 = this.C.getCustomItemsManager();
        if (e2 != null) {
            arrayList.addAll((Collection)e2.B().keySet().stream().filter(string2 -> string2.toLowerCase().startsWith(string)).sorted().collect(Collectors.toList()));
        }
        return arrayList;
    }

    private List<String> A() {
        File file2 = this.C.getDataFolder();
        if (!file2.exists()) {
            return Collections.emptyList();
        }
        Object[] objectArray = file2.listFiles((file, string) -> string.contains((CharSequence)"corrupted"));
        if (objectArray == null) {
            return Collections.emptyList();
        }
        return (List)Arrays.stream((Object[])objectArray).map(File::getName).sorted().collect(Collectors.toList());
    }
}

