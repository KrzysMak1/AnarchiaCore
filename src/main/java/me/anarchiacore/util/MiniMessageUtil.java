package me.anarchiacore.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.Map;

public final class MiniMessageUtil {
    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
    private static final LegacyComponentSerializer LEGACY = LegacyComponentSerializer.legacyAmpersand();

    private MiniMessageUtil() {
    }

    public static Component parseComponent(String raw, Map<String, String> placeholders) {
        if (raw == null) {
            return Component.empty();
        }
        String resolved = raw;
        if (placeholders != null) {
            for (Map.Entry<String, String> entry : placeholders.entrySet()) {
                resolved = resolved.replace("{" + entry.getKey() + "}", entry.getValue());
            }
        }
        if (resolved.contains("&")) {
            return LEGACY.deserialize(resolved);
        }
        return MINI_MESSAGE.deserialize(resolved);
    }

    public static Component parseComponent(String raw) {
        return parseComponent(raw, null);
    }

    public static String serialize(Component component) {
        if (component == null) {
            return "";
        }
        return MINI_MESSAGE.serialize(component);
    }
}
