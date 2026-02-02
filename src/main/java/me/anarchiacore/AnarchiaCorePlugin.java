package me.anarchiacore;

import me.anarchiacore.combatlog.CombatLogManager;
import me.anarchiacore.config.ConfigManager;
import me.anarchiacore.config.MessageService;
import me.anarchiacore.customitems.CustomItemsManager;
import me.anarchiacore.customitems.stormitemy.Main;
import me.anarchiacore.customitems.stormitemy.StormItemyConfigInstaller;
import me.anarchiacore.customitems.stormitemy.core.B;
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
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

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
    private Main stormItemyMain;
    private B stormItemyInitializer;
    private StormItemyConfigInstaller stormItemyConfigInstaller;
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

        stormItemyConfigInstaller = new StormItemyConfigInstaller(this);
        stormItemyConfigInstaller.installMissing();

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

        Objects.requireNonNull(getCommand("anarchiacore")).setExecutor(this);
        Objects.requireNonNull(getCommand("anarchiacore")).setTabCompleter(this);
        Objects.requireNonNull(getCommand("antylogout")).setExecutor(this);
        Objects.requireNonNull(getCommand("antylogout")).setTabCompleter(this);
        Objects.requireNonNull(getCommand("kosz")).setExecutor(new TrashCommand(trashManager, messageService, this));

        stormItemyMain = new Main(this);
        stormItemyMain.onEnable();
        stormItemyInitializer = stormItemyMain.getInitializer();
        int stormListeners = 0;
        if (stormItemyInitializer != null) {
            List<org.bukkit.event.Listener> listeners = stormItemyInitializer.getListeners();
            stormListeners = listeners.size();
            for (org.bukkit.event.Listener listener : listeners) {
                getServer().getPluginManager().registerEvents(listener, this);
            }
            if (stormItemyInitializer.getMenuCommand() != null) {
                Objects.requireNonNull(getCommand("menuprzedmioty")).setExecutor(stormItemyInitializer.getMenuCommand());
            }
        }
        Objects.requireNonNull(getCommand("stormitemy")).setExecutor(stormItemyMain);
        Objects.requireNonNull(getCommand("stormitemy")).setTabCompleter(stormItemyMain);

        int stormCommands = stormItemyInitializer != null && stormItemyInitializer.getMenuCommand() != null ? 2 : 1;
        int stormConfigs = stormItemyInitializer != null ? stormItemyInitializer.getLoadedConfigCount() : 0;
        int expectedStormConfigs = 4;
        String stormSource = stormItemyConfigInstaller.isResourceSourceDirectory() ? "source" : "jar";
        getLogger().info("StormItemy integrated: " + stormSource + "; registered listeners: " + stormListeners
            + "; commands: " + stormCommands + "; configs loaded: " + stormConfigs);
        if (stormConfigs < expectedStormConfigs) {
            getLogger().severe("StormItemy configs loaded < expected: " + stormConfigs + "/" + expectedStormConfigs);
        }
        int missingEventConfigs = stormItemyConfigInstaller.getMissingEventConfigCount();
        if (missingEventConfigs == 4) {
            getLogger().severe("StormItemy event configs missing: " + missingEventConfigs + "/4");
        }

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
        if (stormItemyMain != null) {
            stormItemyMain.onDisable();
        }
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
        if (stormItemyConfigInstaller != null) {
            stormItemyConfigInstaller.installMissing();
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
        if (stormItemyMain.getConfigManager() != null) {
            stormItemyMain.getConfigManager().A();
        }
        if (stormItemyMain.getActionbarManager() != null) {
            stormItemyMain.getActionbarManager().loadConfig();
        }
        if (stormItemyInitializer != null && stormItemyInitializer.Z != null) {
            stormItemyInitializer.Z.B(sender);
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
