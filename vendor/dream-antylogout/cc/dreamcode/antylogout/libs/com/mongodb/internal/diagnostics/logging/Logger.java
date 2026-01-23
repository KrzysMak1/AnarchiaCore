package cc.dreamcode.antylogout.libs.com.mongodb.internal.diagnostics.logging;

public interface Logger
{
    String getName();
    
    default boolean isTraceEnabled() {
        return false;
    }
    
    default void trace(final String msg) {
    }
    
    default void trace(final String msg, final Throwable t) {
    }
    
    default boolean isDebugEnabled() {
        return false;
    }
    
    default void debug(final String msg) {
    }
    
    default void debug(final String msg, final Throwable t) {
    }
    
    default boolean isInfoEnabled() {
        return false;
    }
    
    default void info(final String msg) {
    }
    
    default void info(final String msg, final Throwable t) {
    }
    
    default boolean isWarnEnabled() {
        return false;
    }
    
    default void warn(final String msg) {
    }
    
    default void warn(final String msg, final Throwable t) {
    }
    
    default boolean isErrorEnabled() {
        return false;
    }
    
    default void error(final String msg) {
    }
    
    default void error(final String msg, final Throwable t) {
    }
}
