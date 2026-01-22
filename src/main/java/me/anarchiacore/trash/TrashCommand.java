package me.anarchiacore.trash;

import me.anarchiacore.config.MessageService;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class TrashCommand implements CommandExecutor {
    private final TrashManager trashManager;
    private final MessageService messageService;
    private final Plugin plugin;

    public TrashCommand(TrashManager trashManager, MessageService messageService, Plugin plugin) {
        this.trashManager = trashManager;
        this.messageService = messageService;
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            messageService.send(sender, plugin.getConfig().getString("messages.notAPlayer"));
            return true;
        }
        trashManager.openTrash(player);
        return true;
    }
}
