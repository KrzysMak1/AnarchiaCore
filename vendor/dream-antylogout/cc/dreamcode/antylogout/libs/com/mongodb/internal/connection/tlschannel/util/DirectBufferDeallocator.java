package cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.tlschannel.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.diagnostics.logging.Loggers;
import java.nio.ByteBuffer;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.diagnostics.logging.Logger;

public class DirectBufferDeallocator
{
    private static final Logger LOGGER;
    private final Deallocator deallocator;
    
    public DirectBufferDeallocator() {
        if (Util.getJavaMajorVersion() >= 9) {
            this.deallocator = new Java9Deallocator();
            DirectBufferDeallocator.LOGGER.debug("initialized direct buffer deallocator for java >= 9");
        }
        else {
            this.deallocator = new Java8Deallocator();
            DirectBufferDeallocator.LOGGER.debug("initialized direct buffer deallocator for java < 9");
        }
    }
    
    public void deallocate(final ByteBuffer buffer) {
        this.deallocator.free(buffer);
    }
    
    static {
        LOGGER = Loggers.getLogger("connection.tls");
    }
    
    private static class Java8Deallocator implements Deallocator
    {
        final Method cleanerAccessor;
        final Method clean;
        
        Java8Deallocator() {
            try {
                this.cleanerAccessor = Class.forName("sun.nio.ch.DirectBuffer").getMethod("cleaner", (Class<?>[])new Class[0]);
                this.clean = Class.forName("sun.misc.Cleaner").getMethod("clean", (Class<?>[])new Class[0]);
            }
            catch (final NoSuchMethodException | ClassNotFoundException t) {
                throw new RuntimeException((Throwable)t);
            }
        }
        
        @Override
        public void free(final ByteBuffer bb) {
            try {
                this.clean.invoke(this.cleanerAccessor.invoke((Object)bb, new Object[0]), new Object[0]);
            }
            catch (final IllegalAccessException | InvocationTargetException t) {
                throw new RuntimeException((Throwable)t);
            }
        }
    }
    
    private static class Java9Deallocator implements Deallocator
    {
        final Object unsafe;
        final Method invokeCleaner;
        
        Java9Deallocator() {
            try {
                final Class<?> unsafeClass = Class.forName("sun.misc.Unsafe");
                final Field theUnsafe = unsafeClass.getDeclaredField("theUnsafe");
                theUnsafe.setAccessible(true);
                this.unsafe = theUnsafe.get((Object)null);
                this.invokeCleaner = unsafeClass.getMethod("invokeCleaner", ByteBuffer.class);
            }
            catch (final NoSuchMethodException | ClassNotFoundException | IllegalAccessException | NoSuchFieldException t) {
                throw new RuntimeException((Throwable)t);
            }
        }
        
        @Override
        public void free(final ByteBuffer bb) {
            try {
                this.invokeCleaner.invoke(this.unsafe, new Object[] { bb });
            }
            catch (final IllegalAccessException | InvocationTargetException t) {
                throw new RuntimeException((Throwable)t);
            }
        }
    }
    
    private interface Deallocator
    {
        void free(final ByteBuffer p0);
    }
}
