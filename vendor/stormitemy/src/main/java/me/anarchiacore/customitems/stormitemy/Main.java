/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.io.File
 *  java.lang.Boolean
 *  java.lang.CharSequence
 *  java.lang.Class
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.reflect.Method
 *  java.util.ArrayList
 *  java.util.Collection
 *  java.util.Collections
 *  java.util.HashMap
 *  java.util.List
 *  java.util.Map
 *  java.util.Set
 *  java.util.function.Consumer
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 *  org.bukkit.command.TabExecutor
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.HandlerList
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerJoinEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 */
package me.anarchiacore.customitems.stormitemy;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Locale;
import java.util.Set;
import java.util.function.Consumer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.Server;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.InputStream;
import java.util.logging.Logger;
import me.anarchiacore.customitems.stormitemy.core.ItemRegistry;
import me.anarchiacore.customitems.stormitemy.items.CA;
import me.anarchiacore.customitems.stormitemy.items.D;
import me.anarchiacore.customitems.stormitemy.items.HA;
import me.anarchiacore.customitems.stormitemy.items.JA;
import me.anarchiacore.customitems.stormitemy.items.R;
import me.anarchiacore.customitems.stormitemy.items.U;
import me.anarchiacore.customitems.stormitemy.items.C;
import me.anarchiacore.customitems.stormitemy.items.L;
import me.anarchiacore.customitems.stormitemy.items.N;
import me.anarchiacore.customitems.stormitemy.items.O;
import me.anarchiacore.customitems.stormitemy.regions.B;
import me.anarchiacore.customitems.stormitemy.ui.gui.E;
import me.anarchiacore.customitems.stormitemy.utils.update.A;
import me.anarchiacore.customitems.stormitemy.zaczarowania.C;

public class Main extends JavaPlugin implements TabExecutor, Listener {
    private static final String C = "c3Rvcm1jb2Rl";
    private static final String CUSTOM_GUI_ITEMS_PATH = "configs/STORMITEMY/gui-custom-items.yml";
    private final JavaPlugin plugin;
    private me.anarchiacore.customitems.stormitemy.core.B E;
    private final Map<String, Object> B = new HashMap();
    private final Map<String, ItemStack> customGuiItems = new LinkedHashMap();
    private String D;
    private volatile boolean F = false;
    private final Object A = new Object();

    public Main() {
        this.plugin = this;
    }

    public Main(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void onEnable() {
        this.D = this.getDescription().getVersion();
        Bukkit.getConsoleSender().sendMessage("\u00a78[\u00a72StormItemy\u00a78] \u00a77Plugin \u00a7fStormItemy \u00a77plugin \u00a7aw\u0142\u0105czony\u00a77! Wersja: \u00a7f" + this.D);
        this.E = new me.anarchiacore.customitems.stormitemy.core.B(this);
        this.E.K();
        this.loadCustomGuiItems();
        this.B();
    }

    private void B() {
        A a2 = new A(this, 122499);
        a2.A(this.D, (Consumer<Boolean>)((Consumer)bl -> {
            String string;
            if (bl == null) {
                this.getLogger().warning("Nie uda\u0142o si\u0119 sprawdzi\u0107 aktualizacji.");
                return;
            }
            boolean bl2 = this.D.toUpperCase().endsWith("T");
            String string3 = string = bl2 ? " \u00a78(\u00a7cWERSJA TESTOWA\u00a78)" : "";
            if (!bl.booleanValue()) {
                Bukkit.getConsoleSender().sendMessage("\u00a78[\u00a72StormItemy\u00a78] \u00a77Plugin jest aktualny (wersja \u00a7f" + this.D + string + "\u00a77)");
            } else {
                a2.A((Consumer<String>)((Consumer)string2 -> {
                    if (string2 != null) {
                        Bukkit.getConsoleSender().sendMessage("\u00a78[\u00a72StormItemy\u00a78] \u00a77Znaleziono now\u0105 wersj\u0119: \u00a7f" + string2 + " \u00a77(aktualna: \u00a7f" + this.D + string + "\u00a77)");
                        Bukkit.getConsoleSender().sendMessage("\u00a78[\u00a72StormItemy\u00a78] \u00a77Pobierz aktualizacj\u0119 ze strony: \u00a7fhttps://www.spigotmc.org/resources/122499/");
                    }
                }));
            }
        }));
    }

    public Object getItem(String string) {
        return this.B.get((Object)string);
    }

    private void A(String string) {
        Bukkit.getConsoleSender().sendMessage("\u00a78[\u00a72StormItemy\u00a78] \u00a77" + string);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void A() {
        Object object = this.A;
        synchronized (object) {
            this.F = true;
            this.A.notifyAll();
        }
    }

    public boolean areItemsInitialized() {
        return this.F;
    }

    public void setItemsInitialized(boolean bl) {
        this.F = bl;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent playerJoinEvent) {
        D d2;
        Player player = playerJoinEvent.getPlayer();
        Object object = this.getItem("plecakdrakuli");
        if (object instanceof D) {
            d2 = (D)object;
            Bukkit.getScheduler().runTaskLater(this.plugin, () -> d2.restoreBackpackOnRespawn(player), 1L);
        }
        if (this.getConfig().getBoolean("updates.check-updates", true)) {
            Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
                int n2 = 122499;
                A a2 = new A(this, 122499);
                a2.A(this.D, (Consumer<Boolean>)((Consumer)bl -> {
                    if (bl != null && bl.booleanValue()) {
                        Bukkit.getScheduler().runTask(this.plugin, () -> {
                            if (player.hasPermission("stormitemy.admin") || player.isOp()) {
                                a2.A((Consumer<String>)((Consumer)string -> {
                                    if (string != null) {
                                        Bukkit.getScheduler().runTask(this.plugin, () -> player.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C("\u00a78[\u00a7x\u00a7B\u00a73\u00a70\u00a70\u00a7F\u00a7F\ud83e\ude93\u00a78] \u00a77Dost\u0119pna jest nowa wersja pluginu: \u00a7x\u00a7D\u00a70\u00a76\u00a70\u00a7F\u00a7F" + string + " \u00a77(aktualna: \u00a7f" + this.D + "\u00a77). Pobierz aktualizacj\u0119 ze strony: \u00a7fhttps://www.spigotmc.org/resources/122499/")));
                                    }
                                }));
                            }
                        });
                    }
                }));
            }, 40L);
        }
        if (this.E != null && !this.E.Q) {
            Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
                if (player.hasPermission("stormitemy.admin") || player.isOp()) {
                    player.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C("\u00a78[\u00a7x\u00a7F\u00a7F\u00a7C\u00a7F\u00a70\u00a70\u26a0\u00a78] \u00a77Nie wykryto pluginu \u00a7x\u00a7F\u00a7F\u00a7E\u00a70\u00a75\u00a7CWorldEdit\u00a77! Przedmioty \u00a7fTurboTrap \u00a77i \u00a7fTurboDomek \u00a77s\u0105 wy\u0142\u0105czone."));
                }
            }, 60L);
        }
        if (!((d2 = Bukkit.getPluginManager().getPlugin("Citizens")) != null && d2.isEnabled() || this.E != null && this.E.k)) {
            Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
                if (player.hasPermission("stormitemy.admin") || player.isOp()) {
                    player.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C("\u00a78[\u00a7x\u00a7F\u00a7F\u00a7C\u00a7F\u00a70\u00a70\u26a0\u00a78] \u00a77Nie wykryto pluginu \u00a7x\u00a7F\u00a7F\u00a7E\u00a70\u00a75\u00a7CCitizens\u00a77! \u00a7fR\u00f3\u017cd\u017cka Iluzjonisty \u00a77wymaga Citizens do dzia\u0142ania."));
                }
            }, 80L);
        }
        if (this.E != null && this.E.R != null && this.E.R.D()) {
            this.E.R.A(player);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent playerQuitEvent) {
        Player player = playerQuitEvent.getPlayer();
        Object object = this.getItem("plecakdrakuli");
        if (object instanceof D) {
            D d2 = (D)object;
            d2.clearPlayerData(player);
        }
    }

    /*
     * Exception decompiling
     */
    public boolean onCommand(CommandSender var1_1, Command var2_2, String var3_3, String[] var4_4) {
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

    public List<ItemStack> getAllItems() {
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList((Collection)this.B.keySet());
        arrayList2.sort((string, string2) -> {
            boolean bl = string.toLowerCase().contains((CharSequence)"anarchiczn");
            boolean bl2 = string2.toLowerCase().contains((CharSequence)"anarchiczn");
            if (bl && !bl2) {
                return 1;
            }
            if (!bl && bl2) {
                return -1;
            }
            return string.compareToIgnoreCase(string2);
        });
        for (String string3 : arrayList2) {
            try {
                Object object;
                ItemStack itemStack = ItemRegistry.getItemStack(string3);
                if (itemStack == null && (object = this.B.get((Object)string3)) != null) {
                    try {
                        Method method = object.getClass().getMethod("getItem", new Class[0]);
                        itemStack = (ItemStack)method.invoke(object, new Object[0]);
                    }
                    catch (Exception exception) {
                        // empty catch block
                    }
                }
                if (itemStack == null) continue;
                arrayList.add((Object)itemStack);
            }
            catch (Exception exception) {}
        }
        return arrayList;
    }

    public boolean addCustomGuiItem(String string, ItemStack itemStack) {
        if (string == null || string.isBlank() || itemStack == null) {
            return false;
        }
        String string2 = string.trim().toLowerCase(Locale.ROOT);
        return this.registerCustomGuiItem(string2, itemStack, true);
    }

    private void loadCustomGuiItems() {
        File file = this.getCustomGuiItemsFile();
        if (!file.exists()) {
            return;
        }
        FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration((File)file);
        ConfigurationSection configurationSection = fileConfiguration.getConfigurationSection("items");
        if (configurationSection == null) {
            return;
        }
        for (String string : configurationSection.getKeys(false)) {
            ItemStack itemStack = configurationSection.getItemStack(string);
            if (itemStack == null) continue;
            if (this.registerCustomGuiItem(string, itemStack, false)) continue;
            this.getLogger().warning("Nie uda\u0142o si\u0119 doda\u0107 przedmiotu GUI: " + string + " (konflikt ID).");
        }
    }

    private boolean registerCustomGuiItem(String string, ItemStack itemStack, boolean bl) {
        if (string == null || string.isBlank()) {
            return false;
        }
        String string2 = string.trim().toLowerCase(Locale.ROOT);
        if (ItemRegistry.isRegistered(string2) || this.B.containsKey((Object)string2) || this.customGuiItems.containsKey((Object)string2)) {
            return false;
        }
        ItemStack itemStack2 = itemStack.clone();
        this.customGuiItems.put(string2, itemStack2);
        this.B.put(string2, itemStack2);
        ItemRegistry.register(string2, () -> itemStack2.clone());
        if (bl) {
            this.saveCustomGuiItems();
        }
        return true;
    }

    private void saveCustomGuiItems() {
        File file = this.getCustomGuiItemsFile();
        File file2 = file.getParentFile();
        if (file2 != null && !file2.exists() && !file2.mkdirs()) {
            this.getLogger().warning("Nie uda\u0142o si\u0119 utworzy\u0107 katalogu: " + file2.getAbsolutePath());
            return;
        }
        YamlConfiguration yamlConfiguration = new YamlConfiguration();
        for (Map.Entry<String, ItemStack> entry : this.customGuiItems.entrySet()) {
            yamlConfiguration.set("items." + entry.getKey(), (Object)entry.getValue());
        }
        try {
            yamlConfiguration.save(file);
        }
        catch (IOException iOException) {
            this.getLogger().warning("Nie uda\u0142o si\u0119 zapisa\u0107 listy przedmiot\u00f3w GUI: " + iOException.getMessage());
        }
    }

    private File getCustomGuiItemsFile() {
        return new File(this.getDataFolder(), CUSTOM_GUI_ITEMS_PATH);
    }

    public List<String> onTabComplete(CommandSender commandSender, Command command, String string, String[] stringArray) {
        if (command.getName().equalsIgnoreCase("stormitemy")) {
            if (!commandSender.hasPermission("stormitemy.admin")) {
                return Collections.emptyList();
            }
            if (this.E != null && this.E.Z != null) {
                return this.E.Z.A(commandSender, command, string, stringArray);
            }
        }
        return Collections.emptyList();
    }

    public me.anarchiacore.customitems.stormitemy.messages.B getActionbarManager() {
        return this.E != null ? this.E.L : null;
    }

    public me.anarchiacore.customitems.stormitemy.config.C getConfigManager() {
        return this.E != null ? this.E.D : null;
    }

    public me.anarchiacore.customitems.stormitemy.regions.C getRegionManager() {
        return this.E != null ? this.E.h : null;
    }

    public boolean isPlayerInBlockedRegion(Location location) {
        return this.E != null && this.E.O != null && this.E.O.B(location);
    }

    public boolean isTargetBlockInProtectedRegion(Location location) {
        return this.E != null && this.E.O != null && this.E.O.A(location);
    }

    public boolean isPlayerInBlockedRegion(Player player) {
        return this.E != null && this.E.O != null && this.E.O.A(player);
    }

    public B getRegionChecker() {
        return this.E != null ? this.E.K : null;
    }

    public me.anarchiacore.customitems.stormitemy.books.A getEnchantedBooksManager() {
        return this.E != null ? this.E.S : null;
    }

    public void onDisable() {
        if (this.E != null) {
            this.E.G();
        }
        me.anarchiacore.customitems.stormitemy.npc.A.A();
        if (this.E != null && this.E.R != null) {
            this.E.R.F();
        }
        if (this.E != null && this.E.L != null) {
            this.E.L.disable();
        }
        this.C();
        if (this.E != null && this.E.h != null) {
            this.E.h.D();
        }
        Bukkit.getScheduler().cancelTasks(this.plugin);
        HandlerList.unregisterAll(this.plugin);
        Bukkit.getConsoleSender().sendMessage("\u00a78\u00a7l[\u00a74StormItemy\u00a78\u00a7l] \u00a77Plugin \u00a7fStormItemy \u00a77plugin \u00a7cwy\u0142\u0105czony\u00a77!");
    }

    private void C() {
        try {
            Object object7 = this.getItem("arcusmagnus");
            if (object7 != null && object7 instanceof me.anarchiacore.customitems.stormitemy.items.A) {
                ((me.anarchiacore.customitems.stormitemy.items.A)object7).cleanup();
            }
            this.invokeItemMethod("przeterminowanytrunek", "onDisable");
            this.invokeItemMethod("wyrzutniahydroklatki", "cleanupAllCages");
            this.invokeItemMethod("sakiewkadropu", "closeDatabase");
            this.invokeItemMethod("balonikzhelem", "cleanup");
            this.invokeItemMethod("blokwidmo", "cleanup");
            this.invokeItemMethod("cudownalatarnia", "cleanup");
        }
        catch (Exception exception) {
            this.getLogger().warning("B\u0142\u0105d podczas czyszczenia przedmiot\u00f3w: " + exception.getMessage());
        }
    }

    private void invokeItemMethod(String key, String methodName) {
        Object object = this.getItem(key);
        if (object == null) {
            return;
        }
        try {
            Method method = object.getClass().getMethod(methodName, new Class[0]);
            method.invoke(object, new Object[0]);
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public me.anarchiacore.customitems.stormitemy.ui.menu.A getMenuManager() {
        return this.E != null ? this.E.T : null;
    }

    public C getZaczarowaniaManager() {
        return this.E != null ? this.E.l : null;
    }

    public me.anarchiacore.customitems.stormitemy.zaczarowania.B getLuckyEventManager() {
        return this.E != null ? this.E.R : null;
    }

    public me.anarchiacore.customitems.stormitemy.core.B getInitializer() {
        return this.E;
    }

    public me.anarchiacore.customitems.stormitemy.commands.B getPanelCommand() {
        return this.E != null ? this.E.H : null;
    }

    public boolean isWorldGuardPresent() {
        return this.E != null && this.E.a;
    }

    public me.anarchiacore.customitems.stormitemy.commands.D getBooksCommand() {
        return this.E != null ? this.E.E : null;
    }

    public E getCustomItemsManager() {
        return this.E != null ? this.E.Y : null;
    }

    public me.anarchiacore.customitems.stormitemy.texturepack.B getTexturePackManager() {
        return this.E != null ? this.E.g : null;
    }

    public Map<String, Object> getItems() {
        return this.B;
    }

    public Object getInitializationLock() {
        return this.A;
    }

    public boolean isWorldEditPresent() {
        return this.E != null && this.E.Q;
    }

    public boolean isCitizensPresent() {
        return this.E != null && this.E.k;
    }

    public boolean isItemDisabled(String string) {
        try {
            File file = new File(this.getDataFolder(), "items");
            if (!file.exists()) {
                return false;
            }
            File file2 = new File(file, string + ".yml");
            if (!file2.exists()) {
                String string2 = string.replace((CharSequence)"_", (CharSequence)"");
                file2 = new File(file, string2 + ".yml");
            }
            if (!file2.exists()) {
                String string2 = string.replace((CharSequence)"_", (CharSequence)"").toLowerCase();
                File[] files = file.listFiles();
                if (files != null) {
                    for (File file3 : files) {
                        if (!file3.getName().endsWith(".yml")) continue;
                        String string3 = file3.getName().replace((CharSequence)".yml", (CharSequence)"").toLowerCase();
                        if (string3.equals((Object)string2) || string3.replace((CharSequence)"_", (CharSequence)"").equals((Object)string2)) {
                            file2 = file3;
                            break;
                        }
                        try {
                            YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file3);
                            Set<String> set = yamlConfiguration.getKeys(false);
                            if (set.isEmpty()) continue;
                            String string4 = set.iterator().next();
                            String string5 = string4.replace((CharSequence)"_", (CharSequence)"").toLowerCase();
                            if (!string5.equals((Object)string2)) continue;
                            file2 = file3;
                            break;
                        }
                        catch (Exception exception) {
                            // empty catch block
                        }
                    }
                }
            }
            if (!file2.exists()) {
                return false;
            }
            YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration((File)file2);
            Set<String> set = yamlConfiguration.getKeys(false);
            if (set.isEmpty()) {
                return false;
            }
            String string6 = (String)set.iterator().next();
            return yamlConfiguration.getBoolean(string6 + ".disabled", false);
        }
        catch (Exception exception) {
            this.getLogger().warning("[ItemEditor] B\u0142\u0105d przy sprawdzaniu stanu przedmiotu: " + exception.getMessage());
            return false;
        }
    }

    public C getSmoczyMiecz() {
        Object object = this.getItem("smoczymiecz");
        return object instanceof C ? (C)object : null;
    }

    public HA getBombardaMaxima() {
        Object object = this.getItem("bombardamaxima");
        return object instanceof HA ? (HA)object : null;
    }

    public N getBoskiTopor() {
        Object object = this.getItem("boskitopor");
        return object instanceof N ? (N)object : null;
    }

    public L getSniezka() {
        Object object = this.getItem("sniezka");
        return object instanceof L ? (L)object : null;
    }

    public U getSplesnialaKanapka() {
        Object object = this.getItem("splesnialakanapka");
        return object instanceof U ? (U)object : null;
    }

    public CA getTurboTrap() {
        Object object = this.getItem("turbotrap");
        return object instanceof CA ? (CA)object : null;
    }

    public JA getTurboDomek() {
        Object object = this.getItem("turbodomek");
        return object instanceof JA ? (JA)object : null;
    }

    public O getWedkaSurferka() {
        Object object = this.getItem("wedkasurferka");
        return object instanceof O ? (O)object : null;
    }

    public R getWedkaNielota() {
        Object object = this.getItem("wedkanielota");
        return object instanceof R ? (R)object : null;
    }

    public boolean isItemDisabledByKey(String string) {
        return this.isItemDisabled(string);
    }

    @Override
    public File getDataFolder() {
        return this.plugin.getDataFolder();
    }

    @Override
    public PluginDescriptionFile getDescription() {
        return this.plugin.getDescription();
    }

    @Override
    public FileConfiguration getConfig() {
        return this.plugin.getConfig();
    }

    @Override
    public InputStream getResource(String filename) {
        return this.plugin.getResource(filename);
    }

    @Override
    public void saveConfig() {
        this.plugin.saveConfig();
    }

    @Override
    public void saveDefaultConfig() {
        this.plugin.saveDefaultConfig();
    }

    @Override
    public void saveResource(String resourcePath, boolean replace) {
        this.plugin.saveResource(resourcePath, replace);
    }

    @Override
    public void reloadConfig() {
        this.plugin.reloadConfig();
    }

    @Override
    public PluginLoader getPluginLoader() {
        return this.plugin.getPluginLoader();
    }

    @Override
    public Server getServer() {
        return this.plugin.getServer();
    }

    @Override
    public boolean isEnabled() {
        return this.plugin.isEnabled();
    }

    @Override
    public void onLoad() {
        this.plugin.onLoad();
    }

    @Override
    public boolean isNaggable() {
        return this.plugin.isNaggable();
    }

    @Override
    public void setNaggable(boolean canNag) {
        this.plugin.setNaggable(canNag);
    }

    @Override
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
        return this.plugin.getDefaultWorldGenerator(worldName, id);
    }

    @Override
    public Logger getLogger() {
        return this.plugin.getLogger();
    }

    @Override
    public String getName() {
        return this.plugin.getName();
    }

    public PluginCommand getCommand(String name) {
        return this.plugin.getCommand(name);
    }
}
