package cc.dreamcode.antylogout.libs.com.mongodb.internal.thread;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.ThreadFactory;

public class DaemonThreadFactory implements ThreadFactory
{
    private static final AtomicInteger POOL_NUMBER;
    private final AtomicInteger threadNumber;
    private final String namePrefix;
    
    public DaemonThreadFactory(final String prefix) {
        this.threadNumber = new AtomicInteger(1);
        this.namePrefix = prefix + "-" + DaemonThreadFactory.POOL_NUMBER.getAndIncrement() + "-thread-";
    }
    
    public Thread newThread(final Runnable runnable) {
        final Thread t = new Thread(runnable, this.namePrefix + this.threadNumber.getAndIncrement());
        t.setDaemon(true);
        return t;
    }
    
    static {
        POOL_NUMBER = new AtomicInteger(1);
    }
}
