package me.anarchiacore;

import me.anarchiacore.combatlog.CombatLogManager;
import me.anarchiacore.config.ConfigManager;
import me.anarchiacore.config.MessageService;
import me.anarchiacore.customitems.CustomItemsManager;
import me.anarchiacore.hearts.HeartsManager;
import me.anarchiacore.storage.DataStore;
import me.anarchiacore.trash.TrashCommand;
import me.anarchiacore.trash.TrashManager;
import me.anarchiacore.util.EndCrystalBlocker;
import me.anarchiacore.util.MiniMessageUtil;
import me.anarchiacore.util.ResourceExporter;
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

    @Override
    public void onEnable() {
        saveDefaultConfig();
        configManager = new ConfigManager(this);
        configManager.reload();
        messageService = new MessageService(configManager);
        dataStore = new DataStore(this);
        heartsManager = new HeartsManager(this, configManager, messageService, dataStore);
        trashManager = new TrashManager(this, configManager, messageService);
        customItemsManager = new CustomItemsManager(this, configManager, messageService);
        combatLogManager = new CombatLogManager(this, messageService, dataStore, customItemsManager);

        getServer().getPluginManager().registerEvents(heartsManager, this);
        getServer().getPluginManager().registerEvents(trashManager, this);
        getServer().getPluginManager().registerEvents(customItemsManager, this);
        getServer().getPluginManager().registerEvents(combatLogManager, this);
        getServer().getPluginManager().registerEvents(new EndCrystalBlocker(this, configManager, messageService), this);

        Objects.requireNonNull(getCommand("anarchiacore")).setExecutor(this);
        Objects.requireNonNull(getCommand("anarchiacore")).setTabCompleter(this);
        Objects.requireNonNull(getCommand("kosz")).setExecutor(new TrashCommand(trashManager, messageService, this));

        ResourceExporter exporter = new ResourceExporter(this);
        exporter.exportAlways("bundles/dream-antylogout-10-beta3jar-e28960d6.zip", "dream-antylogout-10-beta3jar-e28960d6.zip");
        exporter.exportAlways("bundles/stormitemy_115-1jar-3723efab.zip", "stormitemy_115-1jar-3723efab.zip");

        combatLogManager.start();
    }

    @Override
    public void onDisable() {
        if (combatLogManager != null) {
            combatLogManager.stop();
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            return false;
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
        if (args[0].equalsIgnoreCase("combatlog")) {
            return handleCombatlogCommand(sender, Arrays.copyOfRange(args, 1, args.length));
        }
        if (args[0].equalsIgnoreCase("customitems")) {
            return handleCustomItemsCommand(sender, Arrays.copyOfRange(args, 1, args.length));
        }
        return false;
    }

    private void reloadAll(CommandSender sender) {
        configManager.reload();
        messageService.send(sender, getConfig().getString("messages.reloadDone"));
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
                sender.sendMessage("Gracz offline.");
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
        if (args.length == 0) {
            return false;
        }
        if (args[0].equalsIgnoreCase("clear") && args.length >= 2) {
            Player target = Bukkit.getPlayerExact(args[1]);
            if (target == null) {
                sender.sendMessage("Gracz offline.");
                return true;
            }
            combatLogManager.clearCombat(target.getUniqueId());
            sender.sendMessage("Wyczyszczono tag combatlog.");
            return true;
        }
        return false;
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
            String items = String.join(", ", configManager.getCustomItemsConfig().getItemIds());
            Map<String, String> placeholders = new HashMap<>();
            placeholders.put("items", items);
            messageService.send(sender, getConfig().getString("messages.customItems.list"), placeholders);
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!command.getName().equalsIgnoreCase("anarchiacore")) {
            return Collections.emptyList();
        }
        if (args.length == 1) {
            return List.of("reload", "heart", "combatlog", "customitems");
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
            return new ArrayList<>(configManager.getCustomItemsConfig().getItemIds());
        }
        if (args.length == 4 && args[0].equalsIgnoreCase("customitems") && args[1].equalsIgnoreCase("give")) {
            List<String> names = new ArrayList<>();
            for (Player player : Bukkit.getOnlinePlayers()) {
                names.add(player.getName());
            }
            return names;
        }
        return Collections.emptyList();
    }
}
