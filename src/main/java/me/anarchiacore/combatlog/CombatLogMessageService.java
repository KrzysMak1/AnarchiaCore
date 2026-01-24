package me.anarchiacore.combatlog;

import me.anarchiacore.util.MiniMessageUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class CombatLogMessageService {
    private final CombatLogMessageConfig messageConfig;
    private String prefix;

    public CombatLogMessageService(CombatLogMessageConfig messageConfig) {
        this.messageConfig = messageConfig;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void send(CommandSender sender, String key, Map<String, String> placeholders) {
        if (sender == null) {
            return;
        }
        messageConfig.getMessage(key).ifPresent(template -> sendTemplate(sender, template, placeholders));
    }

    public void send(CommandSender sender, String key) {
        send(sender, key, null);
    }

    public void broadcast(String key, Map<String, String> placeholders) {
        messageConfig.getMessage(key).ifPresent(template -> {
            Map<String, String> merged = mergePlaceholders(placeholders);
            Component component = MiniMessageUtil.parseComponent(template.text(), merged);
            for (Player player : Bukkit.getOnlinePlayers()) {
                sendTemplate(player, template, merged, component);
            }
        });
    }

    public String getText(String key) {
        return messageConfig.getText(key);
    }

    private void sendTemplate(CommandSender sender, CombatLogMessageConfig.MessageTemplate template, Map<String, String> placeholders) {
        Map<String, String> merged = mergePlaceholders(placeholders);
        Component component = MiniMessageUtil.parseComponent(template.text(), merged);
        sendTemplate(sender, template, merged, component);
    }

    private void sendTemplate(CommandSender sender, CombatLogMessageConfig.MessageTemplate template, Map<String, String> placeholders, Component component) {
        if (template.type() == CombatLogMessageConfig.MessageType.DO_NOT_SEND) {
            return;
        }
        if (template.type() == CombatLogMessageConfig.MessageType.CHAT) {
            sender.sendMessage(component);
            return;
        }
        if (!(sender instanceof Player player)) {
            sender.sendMessage(component);
            return;
        }
        switch (template.type()) {
            case ACTION_BAR -> player.sendActionBar(component);
            case SUBTITLE -> player.showTitle(Title.title(Component.empty(), component, Title.Times.times(Duration.ZERO, Duration.ofSeconds(2), Duration.ofMillis(250))));
            case TITLE -> player.showTitle(Title.title(component, Component.empty(), Title.Times.times(Duration.ZERO, Duration.ofSeconds(2), Duration.ofMillis(250))));
            case TITLE_SUBTITLE -> player.showTitle(Title.title(component, component, Title.Times.times(Duration.ZERO, Duration.ofSeconds(2), Duration.ofMillis(250))));
            default -> sender.sendMessage(component);
        }
    }

    private Map<String, String> mergePlaceholders(Map<String, String> placeholders) {
        Map<String, String> merged = new HashMap<>();
        if (placeholders != null) {
            merged.putAll(placeholders);
        }
        if (prefix != null && !prefix.isBlank()) {
            merged.put("prefix", prefix);
        }
        return merged;
    }
}
