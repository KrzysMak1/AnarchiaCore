package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.profiles.mojang;

import java.util.concurrent.Executors;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.profiles.ProfileLogger;
import org.jetbrains.annotations.NotNull;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.ExecutorService;
import org.jetbrains.annotations.ApiStatus;
import java.util.concurrent.ThreadFactory;

@ApiStatus.Internal
public final class PlayerProfileFetcherThread implements ThreadFactory
{
    public static final ExecutorService EXECUTOR;
    private static final AtomicInteger COUNT;
    
    public Thread newThread(@NotNull final Runnable run) {
        final Thread thread = new Thread(run);
        thread.setName("Profile Lookup Executor #" + PlayerProfileFetcherThread.COUNT.getAndIncrement());
        thread.setUncaughtExceptionHandler((t, throwable) -> ProfileLogger.LOGGER.error("Uncaught exception in thread {}", (Object)t.getName(), (Object)throwable));
        return thread;
    }
    
    static {
        EXECUTOR = Executors.newFixedThreadPool(10, (ThreadFactory)new PlayerProfileFetcherThread());
        COUNT = new AtomicInteger();
    }
}
