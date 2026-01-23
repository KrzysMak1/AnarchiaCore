package cc.dreamcode.antylogout.libs.com.mongodb.internal.logging;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.Objects;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.ClusterId;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.diagnostics.logging.Loggers;
import cc.dreamcode.antylogout.libs.com.mongodb.lang.Nullable;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.diagnostics.logging.Logger;
import java.util.concurrent.ConcurrentHashMap;

public final class StructuredLogger
{
    private static final ConcurrentHashMap<String, LoggingInterceptor> INTERCEPTORS;
    private final Logger logger;
    
    public static void addInterceptor(final String clusterDescription, final LoggingInterceptor interceptor) {
        StructuredLogger.INTERCEPTORS.put((Object)clusterDescription, (Object)interceptor);
    }
    
    public static void removeInterceptor(final String clusterDescription) {
        StructuredLogger.INTERCEPTORS.remove((Object)clusterDescription);
    }
    
    @Nullable
    private static LoggingInterceptor getInterceptor(@Nullable final String clusterDescription) {
        if (clusterDescription == null) {
            return null;
        }
        return (LoggingInterceptor)StructuredLogger.INTERCEPTORS.get((Object)clusterDescription);
    }
    
    public StructuredLogger(final String suffix) {
        this(Loggers.getLogger(suffix));
    }
    
    public StructuredLogger(final Logger logger) {
        this.logger = logger;
    }
    
    public boolean isRequired(final LogMessage.Level level, final ClusterId clusterId) {
        if (getInterceptor(clusterId.getDescription()) != null) {
            return true;
        }
        switch (level) {
            case DEBUG: {
                return this.logger.isDebugEnabled();
            }
            case INFO: {
                return this.logger.isInfoEnabled();
            }
            default: {
                throw new UnsupportedOperationException();
            }
        }
    }
    
    public void log(final LogMessage logMessage) {
        final LoggingInterceptor interceptor = getInterceptor(logMessage.getClusterId().getDescription());
        if (interceptor != null) {
            interceptor.intercept(logMessage);
        }
        switch (logMessage.getLevel()) {
            case DEBUG: {
                final Logger logger = this.logger;
                Objects.requireNonNull((Object)logger);
                final Supplier loggingEnabled = logger::isDebugEnabled;
                final Logger logger2 = this.logger;
                Objects.requireNonNull((Object)logger2);
                final Consumer doLog = logger2::debug;
                final Logger logger3 = this.logger;
                Objects.requireNonNull((Object)logger3);
                logUnstructured(logMessage, (Supplier<Boolean>)loggingEnabled, (Consumer<String>)doLog, (BiConsumer<String, Throwable>)logger3::debug);
                break;
            }
            case INFO: {
                final Logger logger4 = this.logger;
                Objects.requireNonNull((Object)logger4);
                final Supplier loggingEnabled2 = logger4::isInfoEnabled;
                final Logger logger5 = this.logger;
                Objects.requireNonNull((Object)logger5);
                final Consumer doLog2 = logger5::info;
                final Logger logger6 = this.logger;
                Objects.requireNonNull((Object)logger6);
                logUnstructured(logMessage, (Supplier<Boolean>)loggingEnabled2, (Consumer<String>)doLog2, (BiConsumer<String, Throwable>)logger6::info);
                break;
            }
            default: {
                throw new UnsupportedOperationException();
            }
        }
    }
    
    private static void logUnstructured(final LogMessage logMessage, final Supplier<Boolean> loggingEnabled, final Consumer<String> doLog, final BiConsumer<String, Throwable> doLogWithException) {
        if (loggingEnabled.get()) {
            final LogMessage.UnstructuredLogMessage unstructuredLogMessage = logMessage.toUnstructuredLogMessage();
            final String message = unstructuredLogMessage.interpolate();
            final Throwable exception = logMessage.getException();
            if (exception == null) {
                doLog.accept((Object)message);
            }
            else {
                doLogWithException.accept((Object)message, (Object)exception);
            }
        }
    }
    
    static {
        INTERCEPTORS = new ConcurrentHashMap();
    }
}
