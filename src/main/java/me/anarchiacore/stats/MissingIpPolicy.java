package me.anarchiacore.stats;

import java.util.Locale;

public enum MissingIpPolicy {
    COUNT,
    IGNORE;

    public static MissingIpPolicy fromString(String value, MissingIpPolicy defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        String normalized = value.trim().toUpperCase(Locale.ROOT);
        if (normalized.isEmpty()) {
            return defaultValue;
        }
        if (normalized.equals("ALLOW")) {
            return COUNT;
        }
        if (normalized.equals("DENY")) {
            return IGNORE;
        }
        try {
            return MissingIpPolicy.valueOf(normalized);
        } catch (IllegalArgumentException ex) {
            return defaultValue;
        }
    }
}
