package me.anarchiacore.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class MiniMessageUtil {
    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
    private static final LegacyComponentSerializer LEGACY = LegacyComponentSerializer.builder()
        .character('&')
        .hexColors()
        .useUnusualXRepeatedCharacterHexFormat()
        .build();
    private static final LegacyComponentSerializer LEGACY_SECTION = LegacyComponentSerializer.builder()
        .character('§')
        .hexColors()
        .useUnusualXRepeatedCharacterHexFormat()
        .build();
    private static final Pattern LEGACY_HEX_PATTERN = Pattern.compile("&#([A-Fa-f0-9]{6})");

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
        String normalized = normalizeLegacyHex(resolved);
        if (normalized.contains("&") || normalized.contains("§")) {
            return LEGACY.deserialize(normalized);
        }
        return MINI_MESSAGE.deserialize(normalized);
    }

    public static Component parseComponent(String raw) {
        return parseComponent(raw, null);
    }

    public static String parseLegacy(String raw, Map<String, String> placeholders) {
        return LEGACY_SECTION.serialize(parseComponent(raw, placeholders));
    }

    public static String parseLegacy(String raw) {
        return parseLegacy(raw, null);
    }

    public static String serialize(Component component) {
        if (component == null) {
            return "";
        }
        return MINI_MESSAGE.serialize(component);
    }

    private static String normalizeLegacyHex(String input) {
        if (input == null) {
            return "";
        }
        Matcher matcher = LEGACY_HEX_PATTERN.matcher(input);
        StringBuffer buffer = new StringBuffer();
        while (matcher.find()) {
            String hex = matcher.group(1);
            StringBuilder replacement = new StringBuilder("&x");
            for (char c : hex.toCharArray()) {
                replacement.append('&').append(c);
            }
            matcher.appendReplacement(buffer, replacement.toString());
        }
        matcher.appendTail(buffer);
        return buffer.toString();
    }
}
