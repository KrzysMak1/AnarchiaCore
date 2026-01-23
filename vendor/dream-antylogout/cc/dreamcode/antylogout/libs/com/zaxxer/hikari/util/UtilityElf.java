package cc.dreamcode.antylogout.libs.com.zaxxer.hikari.util;

import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.lang.reflect.Constructor;

public final class UtilityElf
{
    private UtilityElf() {
    }
    
    public static String getNullIfEmpty(final String text) {
        return (text == null) ? null : (text.trim().isEmpty() ? null : text.trim());
    }
    
    public static void quietlySleep(final long millis) {
        try {
            Thread.sleep(millis);
        }
        catch (final InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    public static boolean safeIsAssignableFrom(final Object obj, final String className) {
        try {
            final Class<?> clazz = Class.forName(className);
            return clazz.isAssignableFrom(obj.getClass());
        }
        catch (final ClassNotFoundException ignored) {
            return false;
        }
    }
    
    public static <T> T createInstance(final String className, final Class<T> clazz, final Object... args) {
        if (className == null) {
            return null;
        }
        try {
            final Class<?> loaded = UtilityElf.class.getClassLoader().loadClass(className);
            if (args.length == 0) {
                return clazz.cast(loaded.getDeclaredConstructor((Class<?>[])new Class[0]).newInstance(new Object[0]));
            }
            final Class<?>[] argClasses = new Class[args.length];
            for (int i = 0; i < args.length; ++i) {
                argClasses[i] = args[i].getClass();
            }
            final Constructor<?> constructor = loaded.getConstructor(argClasses);
            return clazz.cast(constructor.newInstance(args));
        }
        catch (final Exception e) {
            throw new RuntimeException((Throwable)e);
        }
    }
    
    public static ThreadPoolExecutor createThreadPoolExecutor(final int queueSize, final String threadName, final ThreadFactory threadFactory, final RejectedExecutionHandler policy) {
        return createThreadPoolExecutor((BlockingQueue<Runnable>)new LinkedBlockingQueue(queueSize), threadName, threadFactory, policy);
    }
    
    public static ThreadPoolExecutor createThreadPoolExecutor(final BlockingQueue<Runnable> queue, final String threadName, ThreadFactory threadFactory, final RejectedExecutionHandler policy) {
        if (threadFactory == null) {
            threadFactory = (ThreadFactory)new DefaultThreadFactory(threadName);
        }
        final ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1, 5L, TimeUnit.SECONDS, (BlockingQueue)queue, threadFactory, policy);
        executor.allowCoreThreadTimeOut(true);
        return executor;
    }
    
    public static int getTransactionIsolation(final String transactionIsolationName) {
        if (transactionIsolationName != null) {
            try {
                final String upperCaseIsolationLevelName = transactionIsolationName.toUpperCase(Locale.ENGLISH);
                return IsolationLevel.valueOf(upperCaseIsolationLevelName).getLevelId();
            }
            catch (final IllegalArgumentException e) {
                try {
                    final int level = Integer.parseInt(transactionIsolationName);
                    for (final IsolationLevel iso : IsolationLevel.values()) {
                        if (iso.getLevelId() == level) {
                            return iso.getLevelId();
                        }
                    }
                    throw new IllegalArgumentException("Invalid transaction isolation value: " + transactionIsolationName);
                }
                catch (final NumberFormatException nfe) {
                    throw new IllegalArgumentException("Invalid transaction isolation value: " + transactionIsolationName, (Throwable)nfe);
                }
            }
        }
        return -1;
    }
    
    public static class CustomDiscardPolicy implements RejectedExecutionHandler
    {
        public void rejectedExecution(final Runnable r, final ThreadPoolExecutor executor) {
        }
    }
    
    public static final class DefaultThreadFactory implements ThreadFactory
    {
        private final String threadName;
        private final boolean daemon;
        
        public DefaultThreadFactory(final String threadName) {
            this.threadName = threadName;
            this.daemon = true;
        }
        
        public Thread newThread(final Runnable r) {
            final Thread thread = new Thread(r, this.threadName);
            thread.setDaemon(this.daemon);
            return thread;
        }
    }
}
