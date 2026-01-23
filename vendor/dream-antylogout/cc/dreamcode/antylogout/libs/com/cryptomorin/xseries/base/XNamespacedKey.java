package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.base;

import com.google.common.base.Preconditions;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

class XNamespacedKey
{
    private static final boolean SUPPORTS_NamespacedKey_fromString;
    
    private static boolean isValidNamespaceChar(final char c) {
        return (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9') || c == '.' || c == '_' || c == '-';
    }
    
    private static boolean isValidKeyChar(final char c) {
        return isValidNamespaceChar(c) || c == '/';
    }
    
    private static boolean isValidNamespace(final String namespace) {
        final int len = namespace.length();
        if (len == 0) {
            return false;
        }
        for (int i = 0; i < len; ++i) {
            if (!isValidNamespaceChar(namespace.charAt(i))) {
                return false;
            }
        }
        return true;
    }
    
    private static boolean isValidKey(final String key) {
        final int len = key.length();
        if (len == 0) {
            return false;
        }
        for (int i = 0; i < len; ++i) {
            if (!isValidKeyChar(key.charAt(i))) {
                return false;
            }
        }
        return true;
    }
    
    protected static NamespacedKey fromString(@NotNull final String string) {
        if (XNamespacedKey.SUPPORTS_NamespacedKey_fromString) {
            return NamespacedKey.fromString(string);
        }
        Preconditions.checkArgument(string != null && !string.isEmpty(), (Object)"Input string must not be empty or null");
        final String[] components = string.split(":", 3);
        if (components.length > 2) {
            return null;
        }
        final String key = (components.length == 2) ? components[1] : "";
        if (components.length == 1) {
            final String namespace = components[0];
            if (!namespace.isEmpty() && isValidKey(namespace)) {
                return NamespacedKey.minecraft(namespace);
            }
            return null;
        }
        else {
            if (components.length == 2 && !isValidKey(key)) {
                return null;
            }
            final String namespace = components[0];
            if (namespace.isEmpty()) {
                return NamespacedKey.minecraft(key);
            }
            return isValidNamespace(namespace) ? new NamespacedKey(namespace, key) : null;
        }
    }
    
    static {
        boolean supportsFromString;
        try {
            final Class<?> NamespacedKey = Class.forName("org.bukkit.NamespacedKey");
            NamespacedKey.getDeclaredMethod("fromString", String.class);
            supportsFromString = true;
        }
        catch (final Throwable ex) {
            supportsFromString = false;
        }
        SUPPORTS_NamespacedKey_fromString = supportsFromString;
    }
}
