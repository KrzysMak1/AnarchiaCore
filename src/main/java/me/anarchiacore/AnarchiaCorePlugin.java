package me.anarchiacore;

import me.anarchiacore.combatlog.CombatLogManager;
import me.anarchiacore.config.ConfigManager;
import me.anarchiacore.config.MessageService;
import me.anarchiacore.customitems.CustomItemsConfig;
import me.anarchiacore.customitems.CustomItemsManager;
import me.anarchiacore.customitems.config.CustomItemsConfigInstaller;
import me.anarchiacore.dripstone.DripstoneDamageManager;
import me.anarchiacore.hearts.HeartsManager;
import me.anarchiacore.papi.AnarchiaCorePlaceholderExpansion;
import me.anarchiacore.stats.StatsManager;
import me.anarchiacore.storage.DataStore;
import me.anarchiacore.trash.TrashCommand;
import me.anarchiacore.trash.TrashManager;
import me.anarchiacore.util.EndCrystalBlocker;
import me.anarchiacore.util.MiniMessageUtil;
import me.anarchiacore.util.ResourceExporter;
import me.anarchiacore.util.ZipExtractor;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.event.Listener;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class AnarchiaCorePlugin extends JavaPlugin implements CommandExecutor, TabCompleter {
    private ConfigManager configManager;
    private MessageService messageService;
    private DataStore dataStore;
    private HeartsManager heartsManager;
    private TrashManager trashManager;
    private CustomItemsManager customItemsManager;
    private CombatLogManager combatLogManager;
    private StatsManager statsManager;
    private DripstoneDamageManager dripstoneDamageManager;
    private Object stormItemyMain;
    private Object stormItemyInitializer;
    private CustomItemsConfigInstaller customItemsConfigInstaller;
    private AnarchiaCorePlaceholderExpansion placeholderExpansion;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        ResourceExporter exporter = new ResourceExporter(this);
        exporter.exportAlways("bundles/dream-antylogout-10-beta3jar-e28960d6.zip", "dream-antylogout-10-beta3jar-e28960d6.zip");
        exporter.exportAlways("bundles/stormitemy_115-1jar-3723efab.zip", "stormitemy_115-1jar-3723efab.zip");

        ZipExtractor zipExtractor = new ZipExtractor(this);
        zipExtractor.extractAlways(
            "configs/STORMITEMY.zip",
            new java.io.File(getDataFolder(), "configs/STORMITEMY"),
            entryName -> !entryName.endsWith(".schem") && !entryName.endsWith("data.db")
        );

        customItemsConfigInstaller = new CustomItemsConfigInstaller(this);
        customItemsConfigInstaller.installMissing();

        configManager = new ConfigManager(this);
        configManager.reload();
        messageService = new MessageService(configManager);
        dataStore = new DataStore(this);
        heartsManager = new HeartsManager(this, configManager, messageService, dataStore);
        trashManager = new TrashManager(this, configManager, messageService);
        customItemsManager = new CustomItemsManager(this, configManager, messageService);
        combatLogManager = new CombatLogManager(this, configManager.getPrefix(), customItemsManager, dataStore);
        statsManager = new StatsManager(this, configManager, dataStore);
        dripstoneDamageManager = new DripstoneDamageManager(this, messageService);
        int customItemsCount = configManager.getCustomItemsConfig().getAllItemIds().size();
        if (customItemsCount == 0) {
            getLogger().severe("CustomItems configs are empty: " + new java.io.File(getDataFolder(), "configs/customitems").getAbsolutePath());
        }

        getServer().getPluginManager().registerEvents(heartsManager, this);
        getServer().getPluginManager().registerEvents(trashManager, this);
        getServer().getPluginManager().registerEvents(customItemsManager, this);
        getServer().getPluginManager().registerEvents(combatLogManager, this);
        getServer().getPluginManager().registerEvents(statsManager, this);
        getServer().getPluginManager().registerEvents(dripstoneDamageManager, this);
        getServer().getPluginManager().registerEvents(new EndCrystalBlocker(this, configManager, messageService), this);

        registerCommand("anarchiacore", List.of("acore", "anarchia"), this, this);
        registerCommand("antylogout", List.of("combatlog"), this, this);
        registerCommand("kosz", List.of("trash", "bin"), new TrashCommand(trashManager, messageService, this), null);

        initializeStormItemy();

        try {
            combatLogManager.start();
            int combatLogCommands = 2;
            getLogger().info("CombatLog integrated: listeners=" + combatLogManager.getListenerCount()
                + ", commands=" + combatLogCommands + ", tasks=" + combatLogManager.getActiveTaskCount());
        } catch (Exception ex) {
            getLogger().severe("CombatLog failed to start: " + ex.getMessage());
            ex.printStackTrace();
        }

        registerPlaceholders();
    }

    @Override
    public void onDisable() {
        if (combatLogManager != null) {
            combatLogManager.stop();
        }
        if (statsManager != null) {
            statsManager.stop();
        }
        shutdownStormItemy();
        if (dataStore != null) {
            dataStore.close();
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("antylogout")) {
            if (combatLogManager != null) {
                return combatLogManager.handleCommand(sender, args);
            }
            return true;
        }
        if (args.length == 0) {
            return false;
        }
        if (args[0].equalsIgnoreCase("combatlog")) {
            return handleCombatlogCommand(sender, Arrays.copyOfRange(args, 1, args.length));
        }
        if (!sender.hasPermission("anarchiacore.admin")) {
            messageService.send(sender, getConfig().getString("messages.noPermission"));
            return true;
        }
        if (args[0].equalsIgnoreCase("reload")) {
            reloadAll(sender);
            return true;
        }
        if (args[0].equalsIgnoreCase("heart")) {
            return handleHeartCommand(sender, Arrays.copyOfRange(args, 1, args.length));
        }
        if (args[0].equalsIgnoreCase("customitems")) {
            return handleCustomItemsCommand(sender, Arrays.copyOfRange(args, 1, args.length));
        }
        if (args[0].equalsIgnoreCase("dripstone")) {
            return handleDripstoneCommand(sender, Arrays.copyOfRange(args, 1, args.length));
        }
        return false;
    }

    private void registerPlaceholders() {
        var plugin = getServer().getPluginManager().getPlugin("PlaceholderAPI");
        if (placeholderExpansion != null) {
            placeholderExpansion.unregister();
            placeholderExpansion = null;
        }
        if (plugin != null && plugin.isEnabled()) {
            placeholderExpansion = new AnarchiaCorePlaceholderExpansion(this, configManager, dataStore, combatLogManager, statsManager);
            placeholderExpansion.register();
            getLogger().info("PlaceholderAPI found, placeholders registered.");
        } else {
            getLogger().info("PlaceholderAPI not found, skipping placeholders.");
        }
    }

    private void reloadAll(CommandSender sender) {
        if (customItemsConfigInstaller != null) {
            customItemsConfigInstaller.installMissing();
        }
        configManager.reload();
        dataStore.reload();
        if (heartsManager != null) {
            heartsManager.reload();
        }
        if (trashManager != null) {
            trashManager.reload();
        }
        if (customItemsManager != null) {
            customItemsManager.reload();
        }
        if (statsManager != null) {
            statsManager.reload();
        }
        if (dripstoneDamageManager != null) {
            dripstoneDamageManager.reload();
        }
        if (combatLogManager != null) {
            combatLogManager.reload();
            combatLogManager.updatePrefix(configManager.getPrefix());
            combatLogManager.start();
        }
        reloadStormItemy(sender);
        registerPlaceholders();
        messageService.send(sender, getConfig().getString("messages.reloadDone"));
    }

    private void reloadStormItemy(CommandSender sender) {
        if (stormItemyMain == null) {
            return;
        }
        invokeStormItemyReload(sender);
    }

    private void initializeStormItemy() {
        int stormListeners = 0;
        int stormCommands = 0;
        int stormConfigs = 0;
        int expectedStormConfigs = 4;
        String stormSource = customItemsConfigInstaller.isResourceSourceDirectory() ? "source" : "jar";
        try {
            Class<?> mainClass = Class.forName("me.anarchiacore.customitems.stormitemy.Main");
            Constructor<?> constructor = mainClass.getConstructor(JavaPlugin.class);
            stormItemyMain = constructor.newInstance(this);
            Method onEnable = mainClass.getMethod("onEnable");
            onEnable.invoke(stormItemyMain);
            Method getInitializer = mainClass.getMethod("getInitializer");
            stormItemyInitializer = getInitializer.invoke(stormItemyMain);

            stormListeners = registerStormItemyListeners();
            stormCommands = registerStormItemyCommands();
            stormConfigs = getStormItemyConfigCount();

            if (stormItemyMain instanceof CommandExecutor) {
                registerCommand("stormitemy", List.of("stormitems"), (CommandExecutor) stormItemyMain,
                    stormItemyMain instanceof TabCompleter ? (TabCompleter) stormItemyMain : null);
            }

            getLogger().info("StormItemy integrated: " + stormSource + "; registered listeners: " + stormListeners
                + "; commands: " + stormCommands + "; configs loaded: " + stormConfigs);
            if (stormConfigs < expectedStormConfigs) {
                getLogger().severe("StormItemy configs loaded < expected: " + stormConfigs + "/" + expectedStormConfigs);
            }
            int missingEventConfigs = customItemsConfigInstaller.getMissingEventConfigCount();
            if (missingEventConfigs == expectedStormConfigs) {
                getLogger().severe("StormItemy event configs missing: " + missingEventConfigs + "/" + expectedStormConfigs);
            }
        } catch (ClassNotFoundException ex) {
            getLogger().warning("StormItemy classes not found; skipping integration.");
        } catch (Exception ex) {
            getLogger().severe("StormItemy failed to initialize: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private int registerStormItemyListeners() throws Exception {
        if (stormItemyInitializer == null) {
            return 0;
        }
        Method getListeners = stormItemyInitializer.getClass().getMethod("getListeners");
        Object listenerList = getListeners.invoke(stormItemyInitializer);
        if (!(listenerList instanceof List<?> listeners)) {
            return 0;
        }
        for (Object listener : listeners) {
            if (listener instanceof Listener) {
                getServer().getPluginManager().registerEvents((Listener) listener, this);
            }
        }
        return listeners.size();
    }

    private int registerStormItemyCommands() throws Exception {
        if (stormItemyInitializer == null) {
            return 1;
        }
        Method getMenuCommand = stormItemyInitializer.getClass().getMethod("getMenuCommand");
        Object menuCommand = getMenuCommand.invoke(stormItemyInitializer);
        if (menuCommand instanceof CommandExecutor) {
            registerCommand("menuprzedmioty", List.of("przedmioty"), (CommandExecutor) menuCommand,
                menuCommand instanceof TabCompleter ? (TabCompleter) menuCommand : null);
            return 2;
        }
        return 1;
    }

    private void registerCommand(String name, Collection<String> aliases, CommandExecutor executor, TabCompleter completer) {
        Command command = new Command(name) {
            @Override
            public boolean execute(CommandSender sender, String label, String[] args) {
                return executor.onCommand(sender, this, label, args);
            }

            @Override
            public List<String> tabComplete(CommandSender sender, String label, String[] args) {
                if (completer == null) {
                    return Collections.emptyList();
                }
                List<String> results = completer.onTabComplete(sender, this, label, args);
                return results != null ? results : Collections.emptyList();
            }
        };

        BasicCommand basicCommand = new BasicCommand() {
            @Override
            public void execute(CommandSourceStack stack, String[] args) {
                executor.onCommand(stack.getSender(), command, name, args);
            }

            @Override
            public Collection<String> suggest(CommandSourceStack stack, String[] args) {
                if (completer == null) {
                    return Collections.emptyList();
                }
                List<String> results = completer.onTabComplete(stack.getSender(), command, name, args);
                return results != null ? results : Collections.emptyList();
            }
        };

        if (aliases == null || aliases.isEmpty()) {
            registerCommand(name, basicCommand);
        } else {
            registerCommand(name, aliases, basicCommand);
        }
    }

    private int getStormItemyConfigCount() throws Exception {
        if (stormItemyInitializer == null) {
            return 0;
        }
        Method getLoadedConfigCount = stormItemyInitializer.getClass().getMethod("getLoadedConfigCount");
        Object count = getLoadedConfigCount.invoke(stormItemyInitializer);
        if (count instanceof Number) {
            return ((Number) count).intValue();
        }
        return 0;
    }

    private void invokeStormItemyReload(CommandSender sender) {
        try {
            Method getConfigManager = stormItemyMain.getClass().getMethod("getConfigManager");
            Object configManager = getConfigManager.invoke(stormItemyMain);
            if (configManager != null) {
                Method reloadConfig = configManager.getClass().getMethod("A");
                reloadConfig.invoke(configManager);
            }
            Method getActionbarManager = stormItemyMain.getClass().getMethod("getActionbarManager");
            Object actionbarManager = getActionbarManager.invoke(stormItemyMain);
            if (actionbarManager != null) {
                Method loadConfig = actionbarManager.getClass().getMethod("loadConfig");
                loadConfig.invoke(actionbarManager);
            }
            if (stormItemyInitializer != null) {
                Field field = stormItemyInitializer.getClass().getField("Z");
                Object reloadable = field.get(stormItemyInitializer);
                if (reloadable != null) {
                    Method reloadCommand = reloadable.getClass().getMethod("B", CommandSender.class);
                    reloadCommand.invoke(reloadable, sender);
                }
            }
        } catch (NoSuchFieldException | NoSuchMethodException ex) {
            getLogger().warning("StormItemy reload hook missing: " + ex.getMessage());
        } catch (Exception ex) {
            getLogger().severe("StormItemy reload failed: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void shutdownStormItemy() {
        if (stormItemyMain == null) {
            return;
        }
        try {
            Method onDisable = stormItemyMain.getClass().getMethod("onDisable");
            onDisable.invoke(stormItemyMain);
        } catch (Exception ex) {
            getLogger().severe("StormItemy shutdown failed: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private boolean handleHeartCommand(CommandSender sender, String[] args) {
        if (args.length == 0) {
            return false;
        }
        if (args[0].equalsIgnoreCase("give")) {
            if (args.length < 2) {
                messageService.send(sender, getConfig().getString("messages.giveUsage"));
                return true;
            }
            Player target = Bukkit.getPlayerExact(args[1]);
            if (target == null) {
                messageService.send(sender, "Gracz offline.");
                return true;
            }
            int amount = 1;
            if (args.length >= 3) {
                try {
                    amount = Integer.parseInt(args[2]);
                } catch (NumberFormatException ex) {
                    amount = 1;
                }
            }
            ItemStack item = heartsManager.createHeartItem();
            item.setAmount(amount);
            target.getInventory().addItem(item);
            return true;
        }
        if (args[0].equalsIgnoreCase("setitem")) {
            if (!(sender instanceof Player player)) {
                messageService.send(sender, getConfig().getString("messages.notAPlayer"));
                return true;
            }
            ItemStack item = player.getInventory().getItemInMainHand();
            if (item.getType().isAir()) {
                messageService.send(sender, getConfig().getString("messages.setItemUsage"));
                return true;
            }
            saveHeartDefinition(item);
            configManager.reload();
            messageService.send(sender, getConfig().getString("messages.setItemDone"));
            return true;
        }
        return false;
    }

    private void saveHeartDefinition(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        getConfig().set("anarchiczneSerce.material", item.getType().name());
        if (meta != null) {
            Component display = meta.displayName();
            if (display != null) {
                getConfig().set("anarchiczneSerce.meta.display-name", MiniMessageUtil.serialize(display));
            }
            if (meta.lore() != null) {
                List<String> lore = new ArrayList<>();
                for (Component component : meta.lore()) {
                    lore.add(MiniMessageUtil.serialize(component));
                }
                getConfig().set("anarchiczneSerce.meta.lore", lore);
            }
            Map<String, Integer> enchantments = new LinkedHashMap<>();
            for (Map.Entry<Enchantment, Integer> entry : meta.getEnchants().entrySet()) {
                enchantments.put(entry.getKey().getKey().toString(), entry.getValue());
            }
            getConfig().set("anarchiczneSerce.meta.enchantments", enchantments);
            List<String> flags = new ArrayList<>();
            meta.getItemFlags().forEach(flag -> flags.add(flag.name()));
            getConfig().set("anarchiczneSerce.meta.flags", flags);
            getConfig().set("anarchiczneSerceCustomModelData", meta.hasCustomModelData() ? meta.getCustomModelData() : 0);
        }
        saveConfig();
    }

    private boolean handleCombatlogCommand(CommandSender sender, String[] args) {
        if (combatLogManager == null) {
            return false;
        }
        return combatLogManager.handleCommand(sender, args);
    }

    private boolean handleDripstoneCommand(CommandSender sender, String[] args) {
        if (dripstoneDamageManager == null) {
            return false;
        }
        return dripstoneDamageManager.handleCommand(sender, args);
    }

    private boolean handleCustomItemsCommand(CommandSender sender, String[] args) {
        if (args.length == 0) {
            return false;
        }
        if (args[0].equalsIgnoreCase("give")) {
            if (args.length < 2) {
                messageService.send(sender, getConfig().getString("messages.customItems.giveUsage"));
                return true;
            }
            String itemId = args[1];
            Player target = null;
            if (args.length >= 3) {
                target = Bukkit.getPlayerExact(args[2]);
            } else if (sender instanceof Player player) {
                target = player;
            }
            if (target == null) {
                messageService.send(sender, getConfig().getString("messages.customItems.invalidPlayer"));
                return true;
            }
            CustomItemsConfig.EventItemDefinition eventItem = configManager.getCustomItemsConfig().getEventItemDefinition(itemId);
            if (eventItem != null && stormItemyMain != null) {
                String command = "stormitemy give " + eventItem.id() + " " + target.getName();
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                Map<String, String> placeholders = new HashMap<>();
                placeholders.put("item", itemId);
                placeholders.put("player", target.getName());
                messageService.send(sender, getConfig().getString("messages.customItems.give"), placeholders);
                return true;
            }
            ItemStack item = customItemsManager.createItem(itemId);
            if (item == null) {
                Map<String, String> placeholders = new HashMap<>();
                placeholders.put("item", itemId);
                messageService.send(sender, getConfig().getString("messages.customItems.notFound"), placeholders);
                return true;
            }
            target.getInventory().addItem(item);
            Map<String, String> placeholders = new HashMap<>();
            placeholders.put("item", itemId);
            placeholders.put("player", target.getName());
            messageService.send(sender, getConfig().getString("messages.customItems.give"), placeholders);
            return true;
        }
        if (args[0].equalsIgnoreCase("list")) {
            String items = String.join(", ", configManager.getCustomItemsConfig().getAllItemIds());
            Map<String, String> placeholders = new HashMap<>();
            placeholders.put("items", items);
            messageService.send(sender, getConfig().getString("messages.customItems.list"), placeholders);
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("antylogout") && combatLogManager != null) {
            return combatLogManager.tabComplete(args);
        }
        if (!command.getName().equalsIgnoreCase("anarchiacore")) {
            return Collections.emptyList();
        }
        if (args.length == 1) {
            return List.of("reload", "heart", "combatlog", "customitems", "dripstone");
        }
        if (args.length == 2 && args[0].equalsIgnoreCase("heart")) {
            return List.of("give", "setitem");
        }
        if (args.length == 3 && args[0].equalsIgnoreCase("heart") && args[1].equalsIgnoreCase("give")) {
            List<String> names = new ArrayList<>();
            for (Player player : Bukkit.getOnlinePlayers()) {
                names.add(player.getName());
            }
            return names;
        }
        if (args.length == 2 && args[0].equalsIgnoreCase("customitems")) {
            return List.of("give", "list");
        }
        if (args.length == 3 && args[0].equalsIgnoreCase("customitems") && args[1].equalsIgnoreCase("give")) {
            return new ArrayList<>(configManager.getCustomItemsConfig().getAllItemIds());
        }
        if (args.length == 4 && args[0].equalsIgnoreCase("customitems") && args[1].equalsIgnoreCase("give")) {
            List<String> names = new ArrayList<>();
            for (Player player : Bukkit.getOnlinePlayers()) {
                names.add(player.getName());
            }
            return names;
        }
        if (args.length >= 2 && args[0].equalsIgnoreCase("combatlog") && combatLogManager != null) {
            return combatLogManager.tabComplete(Arrays.copyOfRange(args, 1, args.length));
        }
        if (args.length >= 2 && args[0].equalsIgnoreCase("dripstone") && dripstoneDamageManager != null) {
            return dripstoneDamageManager.tabComplete(Arrays.copyOfRange(args, 1, args.length));
        }
        return Collections.emptyList();
    }
}
