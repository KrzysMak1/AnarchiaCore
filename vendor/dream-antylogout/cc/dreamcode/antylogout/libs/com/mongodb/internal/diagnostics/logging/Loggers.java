package cc.dreamcode.antylogout.libs.com.mongodb.internal.diagnostics.logging;

import cc.dreamcode.antylogout.libs.com.mongodb.assertions.Assertions;

public final class Loggers
{
    private static final String PREFIX = "org.mongodb.driver";
    private static final boolean USE_SLF4J;
    
    public static Logger getLogger(final String suffix) {
        Assertions.notNull("suffix", suffix);
        if (suffix.startsWith(".") || suffix.endsWith(".")) {
            throw new IllegalArgumentException("The suffix can not start or end with a '.'");
        }
        final String name = "org.mongodb.driver." + suffix;
        if (Loggers.USE_SLF4J) {
            return new SLF4JLogger(name);
        }
        return new NoOpLogger(name);
    }
    
    private Loggers() {
    }
    
    private static boolean shouldUseSLF4J() {
        try {
            Class.forName("cc.dreamcode.antylogout.libs.org.slf4j.Logger");
            return true;
        }
        catch (final ClassNotFoundException e) {
            java.util.logging.Logger.getLogger("org.mongodb.driver").warning(String.format("SLF4J not found on the classpath.  Logging is disabled for the '%s' component", new Object[] { "org.mongodb.driver" }));
            return false;
        }
    }
    
    static {
        USE_SLF4J = shouldUseSLF4J();
    }
}
