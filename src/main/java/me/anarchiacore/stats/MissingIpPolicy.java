package me.anarchiacore.stats;

import java.util.Locale;

public enum MissingIpPolicy {
    ALLOW,
    DENY;

    public static MissingIpPolicy fromString(String value, MissingIpPolicy defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        String normalized = value.trim().toUpperCase(Locale.ROOT);
        if (normalized.isEmpty()) {
            return defaultValue;
        }
        try {
            return MissingIpPolicy.valueOf(normalized);
        } catch (IllegalArgumentException ex) {
            return defaultValue;
        }
    }
}
