package me.anarchiacore.hearts;

import me.anarchiacore.config.ConfigManager;
import me.anarchiacore.config.MessageService;
import me.anarchiacore.storage.DataStore;
import me.anarchiacore.util.ItemUtil;
import org.bukkit.GameMode;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HeartsManager implements Listener {
    private final Plugin plugin;
    private final ConfigManager configManager;
    private final MessageService messageService;
    private final DataStore dataStore;
    private final NamespacedKey heartKey;

    public HeartsManager(Plugin plugin, ConfigManager configManager, MessageService messageService, DataStore dataStore) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.messageService = messageService;
        this.dataStore = dataStore;
        this.heartKey = new NamespacedKey(plugin, "anarchiczne_serce");
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        int hearts;
        if (!dataStore.hasHearts(uuid)) {
            hearts = configManager.getDefaultHearts();
            dataStore.setHearts(uuid, hearts);
            dataStore.save();
            applyMaxHealth(player, hearts);
            applyMaxHealthNextTick(player, hearts, true);
        } else {
            hearts = dataStore.getHearts(uuid, configManager.getDefaultHearts());
            applyMaxHealth(player, hearts);
            applyMaxHealthNextTick(player, hearts, false);
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        UUID uuid = player.getUniqueId();
        int hearts = dataStore.getHearts(uuid, configManager.getDefaultHearts());
        if (hearts > configManager.getDefaultHearts()) {
            hearts = Math.max(configManager.getDefaultHearts(), hearts - 1);
            dataStore.setHearts(uuid, hearts);
            dataStore.save();
            applyMaxHealth(player, hearts);
            applyMaxHealthNextTick(player, hearts, false);
            Map<String, String> placeholders = new HashMap<>();
            placeholders.put("hearts", String.valueOf(hearts));
            messageService.send(player, plugin.getConfig().getString("messages.heartLostOnDeath"), placeholders);
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        int hearts = dataStore.getHearts(player.getUniqueId(), configManager.getDefaultHearts());
        applyMaxHealthNextTick(player, hearts, true);
    }

    @EventHandler
    public void onUseHeart(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) {
            return;
        }
        ItemStack item = event.getItem();
        if (item == null || item.getType().isAir()) {
            return;
        }
        if (!isHeartItem(item)) {
            return;
        }
        event.setCancelled(true);
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        int hearts = dataStore.getHearts(uuid, configManager.getDefaultHearts());
        int maxHearts = configManager.getMaxHearts();
        if (hearts >= maxHearts) {
            Map<String, String> placeholders = new HashMap<>();
            placeholders.put("maxHearts", String.valueOf(maxHearts));
            messageService.send(player, plugin.getConfig().getString("messages.heartMax"), placeholders);
            return;
        }
        hearts += 1;
        dataStore.setHearts(uuid, hearts);
        dataStore.save();
        applyMaxHealth(player, hearts);
        applyMaxHealthNextTick(player, hearts, false);
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("hearts", String.valueOf(hearts));
        messageService.send(player, plugin.getConfig().getString("messages.heartUsed"), placeholders);
        if (player.getGameMode() != GameMode.CREATIVE) {
            item.setAmount(item.getAmount() - 1);
        }
    }

    public ItemStack createHeartItem() {
        AnarchiczneSerceDefinition def = configManager.getSerceDefinition();
        ItemStack item = ItemUtil.createItem(def.material());
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("maxHearts", String.valueOf(configManager.getMaxHearts()));
        ItemUtil.applyMeta(item, def.displayName(), def.lore(), def.enchantments(), def.flags(), def.customModelData(), plugin, heartKey, "1", placeholders);
        return item;
    }

    public boolean isHeartItem(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return false;
        }
        String value = meta.getPersistentDataContainer().get(heartKey, PersistentDataType.STRING);
        return "1".equals(value);
    }

    public void applyMaxHealth(Player player, int hearts) {
        double maxHealth = hearts * 2.0;
        AttributeInstance attribute = player.getAttribute(Attribute.MAX_HEALTH);
        if (attribute != null) {
            attribute.setBaseValue(maxHealth);
        }
        if (player.getHealth() > maxHealth) {
            player.setHealth(maxHealth);
        }
    }

    private void applyMaxHealthNextTick(Player player, int hearts, boolean setFullHealth) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.isOnline()) {
                    return;
                }
                applyMaxHealth(player, hearts);
                if (setFullHealth) {
                    player.setHealth(player.getMaxHealth());
                }
            }
        }.runTaskLater(plugin, 1L);
    }

    public NamespacedKey getHeartKey() {
        return heartKey;
    }
}
