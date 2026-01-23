package me.anarchiacore.util;

import me.anarchiacore.config.ConfigManager;
import me.anarchiacore.config.MessageService;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class EndCrystalBlocker implements Listener {
    private final Plugin plugin;
    private final ConfigManager configManager;
    private final MessageService messageService;

    public EndCrystalBlocker(Plugin plugin, ConfigManager configManager, MessageService messageService) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.messageService = messageService;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) {
            return;
        }
        if (event.getClickedBlock() == null) {
            return;
        }
        ItemStack item = event.getItem();
        if (item == null || item.getType() != Material.END_CRYSTAL) {
            return;
        }
        World world = event.getClickedBlock().getWorld();
        if (configManager.isCrystalBlocked(world)) {
            event.setCancelled(true);
            messageService.send(event.getPlayer(), plugin.getConfig().getString("messages.crystalBlocked"));
        }
    }

    @EventHandler
    public void onDispense(BlockDispenseEvent event) {
        if (event.getItem().getType() != Material.END_CRYSTAL) {
            return;
        }
        World world = event.getBlock().getWorld();
        if (configManager.isCrystalBlocked(world)) {
            event.setCancelled(true);
        }
    }
}
