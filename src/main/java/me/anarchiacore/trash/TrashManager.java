package me.anarchiacore.trash;

import me.anarchiacore.config.ConfigManager;
import me.anarchiacore.config.MessageService;
import me.anarchiacore.util.MiniMessageUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.Plugin;

public class TrashManager implements Listener {
    private final Plugin plugin;
    private final ConfigManager configManager;
    private final MessageService messageService;

    public TrashManager(Plugin plugin, ConfigManager configManager, MessageService messageService) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.messageService = messageService;
    }

    public void openTrash(Player player) {
        int size = Math.max(1, Math.min(6, configManager.getTrashRows())) * 9;
        Component title = MiniMessageUtil.parseComponent(configManager.getTrashTitle());
        Inventory inventory = Bukkit.createInventory(new TrashHolder(), size, title);
        player.openInventory(inventory);
        messageService.send(player, plugin.getConfig().getString("messages.trashOpened"));
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();
        if (!(holder instanceof TrashHolder)) {
            return;
        }
        event.getInventory().clear();
        if (event.getPlayer() instanceof Player player) {
            String clearMessage = configManager.getTrashClearedMessage();
            if (clearMessage != null && !clearMessage.isBlank()) {
                messageService.send(player, clearMessage);
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (player.getOpenInventory().getTopInventory().getHolder() instanceof TrashHolder) {
            player.getOpenInventory().getTopInventory().clear();
        }
    }
}
