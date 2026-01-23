package me.anarchiacore.combatlog;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CombatLogMessageConfig {
    private final File file;
    private YamlConfiguration config;
    private final Map<String, MessageTemplate> messages = new HashMap<>();

    public CombatLogMessageConfig(File file) {
        this.file = file;
    }

    public void load() {
        this.config = YamlConfiguration.loadConfiguration(file);
        messages.clear();
        for (String key : config.getKeys(false)) {
            Object value = config.get(key);
            if (value instanceof ConfigurationSection section) {
                String type = section.getString("type", "CHAT");
                String text = section.getString("text", "");
                messages.put(key, new MessageTemplate(MessageType.fromString(type), text));
            } else if (value instanceof String text) {
                messages.put(key, new MessageTemplate(MessageType.CHAT, text));
            }
        }
    }

    public void save() throws IOException {
        if (config != null) {
            config.save(file);
        }
    }

    public Optional<MessageTemplate> getMessage(String key) {
        return Optional.ofNullable(messages.get(key));
    }

    public String getText(String key) {
        MessageTemplate template = messages.get(key);
        return template != null ? template.text() : "";
    }

    public enum MessageType {
        DO_NOT_SEND,
        CHAT,
        ACTION_BAR,
        SUBTITLE,
        TITLE,
        TITLE_SUBTITLE;

        public static MessageType fromString(String raw) {
            if (raw == null) {
                return CHAT;
            }
            try {
                return MessageType.valueOf(raw.trim().toUpperCase());
            } catch (IllegalArgumentException ex) {
                return CHAT;
            }
        }
    }

    public record MessageTemplate(MessageType type, String text) {
    }
}
