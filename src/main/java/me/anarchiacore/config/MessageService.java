package me.anarchiacore.config;

import me.anarchiacore.util.MiniMessageUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class MessageService {
    private final ConfigManager configManager;

    public MessageService(ConfigManager configManager) {
        this.configManager = configManager;
    }

    public Component parse(String raw, Map<String, String> placeholders) {
        Map<String, String> merged = new HashMap<>();
        if (placeholders != null) {
            merged.putAll(placeholders);
        }
        merged.put("prefix", configManager.getPrefix());
        return MiniMessageUtil.parseComponent(raw, merged);
    }

    public void send(CommandSender sender, String raw, Map<String, String> placeholders) {
        if (sender == null) {
            return;
        }
        Component component = parse(raw, placeholders);
        if (sender instanceof Player player) {
            player.sendMessage(component);
        } else {
            sender.sendMessage(component);
        }
    }

    public void send(CommandSender sender, String raw) {
        send(sender, raw, null);
    }
}
