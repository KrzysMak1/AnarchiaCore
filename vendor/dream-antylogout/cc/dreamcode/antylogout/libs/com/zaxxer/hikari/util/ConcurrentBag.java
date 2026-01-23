package cc.dreamcode.antylogout.libs.com.zaxxer.hikari.util;

import cc.dreamcode.antylogout.libs.org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.concurrent.locks.LockSupport;
import java.util.Iterator;
import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import cc.dreamcode.antylogout.libs.org.slf4j.Logger;

public class ConcurrentBag<T extends IConcurrentBagEntry> implements AutoCloseable
{
    private static final Logger LOGGER;
    private final CopyOnWriteArrayList<T> sharedList;
    private final boolean weakThreadLocals;
    private final ThreadLocal<List<Object>> threadList;
    private final IBagStateListener listener;
    private final AtomicInteger waiters;
    private volatile boolean closed;
    private final SynchronousQueue<T> handoffQueue;
    
    public ConcurrentBag(final IBagStateListener listener) {
        this.listener = listener;
        this.weakThreadLocals = this.useWeakThreadLocals();
        this.handoffQueue = (SynchronousQueue<T>)new SynchronousQueue(true);
        this.waiters = new AtomicInteger();
        this.sharedList = (CopyOnWriteArrayList<T>)new CopyOnWriteArrayList();
        if (this.weakThreadLocals) {
            this.threadList = (ThreadLocal<List<Object>>)ThreadLocal.withInitial(() -> new ArrayList(16));
        }
        else {
            this.threadList = (ThreadLocal<List<Object>>)ThreadLocal.withInitial(() -> new FastList(IConcurrentBagEntry.class, 16));
        }
    }
    
    public T borrow(long timeout, final TimeUnit timeUnit) throws InterruptedException {
        final List<Object> list = (List<Object>)this.threadList.get();
        for (int i = list.size() - 1; i >= 0; --i) {
            final Object entry = list.remove(i);
            final T bagEntry = (T)(this.weakThreadLocals ? ((WeakReference)entry).get() : ((IConcurrentBagEntry)entry));
            if (bagEntry != null && bagEntry.compareAndSet(0, 1)) {
                return bagEntry;
            }
        }
        final int waiting = this.waiters.incrementAndGet();
        try {
            for (final T bagEntry : this.sharedList) {
                if (bagEntry.compareAndSet(0, 1)) {
                    if (waiting > 1) {
                        this.listener.addBagItem(waiting - 1);
                    }
                    return bagEntry;
                }
            }
            this.listener.addBagItem(waiting);
            timeout = timeUnit.toNanos(timeout);
            do {
                final long start = ClockSource.currentTime();
                final T bagEntry2 = (T)this.handoffQueue.poll(timeout, TimeUnit.NANOSECONDS);
                if (bagEntry2 == null || bagEntry2.compareAndSet(0, 1)) {
                    return bagEntry2;
                }
                timeout -= ClockSource.elapsedNanos(start);
            } while (timeout > 10000L);
            return null;
        }
        finally {
            this.waiters.decrementAndGet();
        }
    }
    
    public void requite(final T bagEntry) {
        bagEntry.setState(0);
        int i = 0;
        while (this.waiters.get() > 0) {
            if (bagEntry.getState() != 0 || this.handoffQueue.offer((Object)bagEntry)) {
                return;
            }
            if ((i & 0xFF) == 0xFF) {
                LockSupport.parkNanos(TimeUnit.MICROSECONDS.toNanos(10L));
            }
            else {
                Thread.yield();
            }
            ++i;
        }
        final List<Object> threadLocalList = (List<Object>)this.threadList.get();
        if (threadLocalList.size() < 50) {
            threadLocalList.add(this.weakThreadLocals ? new WeakReference((Object)bagEntry) : bagEntry);
        }
    }
    
    public void add(final T bagEntry) {
        if (this.closed) {
            ConcurrentBag.LOGGER.info("ConcurrentBag has been closed, ignoring add()");
            throw new IllegalStateException("ConcurrentBag has been closed, ignoring add()");
        }
        this.sharedList.add((Object)bagEntry);
        while (this.waiters.get() > 0 && bagEntry.getState() == 0 && !this.handoffQueue.offer((Object)bagEntry)) {
            Thread.yield();
        }
    }
    
    public boolean remove(final T bagEntry) {
        if (!bagEntry.compareAndSet(1, -1) && !bagEntry.compareAndSet(-2, -1) && !this.closed) {
            ConcurrentBag.LOGGER.warn("Attempt to remove an object from the bag that was not borrowed or reserved: {}", bagEntry);
            return false;
        }
        final boolean removed = this.sharedList.remove((Object)bagEntry);
        if (!removed && !this.closed) {
            ConcurrentBag.LOGGER.warn("Attempt to remove an object from the bag that does not exist: {}", bagEntry);
        }
        ((List)this.threadList.get()).remove((Object)bagEntry);
        return removed;
    }
    
    public void close() {
        this.closed = true;
    }
    
    public List<T> values(final int state) {
        final List<T> list = (List<T>)this.sharedList.stream().filter(e -> e.getState() == state).collect(Collectors.toList());
        Collections.reverse((List)list);
        return list;
    }
    
    public List<T> values() {
        return (List<T>)this.sharedList.clone();
    }
    
    public boolean reserve(final T bagEntry) {
        return bagEntry.compareAndSet(0, -2);
    }
    
    public void unreserve(final T bagEntry) {
        if (bagEntry.compareAndSet(-2, 0)) {
            while (this.waiters.get() > 0 && !this.handoffQueue.offer((Object)bagEntry)) {
                Thread.yield();
            }
        }
        else {
            ConcurrentBag.LOGGER.warn("Attempt to relinquish an object to the bag that was not reserved: {}", bagEntry);
        }
    }
    
    public int getWaitingThreadCount() {
        return this.waiters.get();
    }
    
    public int getCount(final int state) {
        int count = 0;
        for (final T e : this.sharedList) {
            if (e.getState() == state) {
                ++count;
            }
        }
        return count;
    }
    
    public int[] getStateCounts() {
        final int[] states = new int[6];
        for (final T e : this.sharedList) {
            final int[] array = states;
            final int state = e.getState();
            ++array[state];
        }
        states[4] = this.sharedList.size();
        states[5] = this.waiters.get();
        return states;
    }
    
    public int size() {
        return this.sharedList.size();
    }
    
    public void dumpState() {
        this.sharedList.forEach(entry -> ConcurrentBag.LOGGER.info(entry.toString()));
    }
    
    private boolean useWeakThreadLocals() {
        try {
            if (System.getProperty("cc.dreamcode.antylogout.libs.com.zaxxer.hikari.useWeakReferences") != null) {
                return Boolean.getBoolean("cc.dreamcode.antylogout.libs.com.zaxxer.hikari.useWeakReferences");
            }
            return this.getClass().getClassLoader() != ClassLoader.getSystemClassLoader();
        }
        catch (final SecurityException se) {
            return true;
        }
    }
    
    static {
        LOGGER = LoggerFactory.getLogger(ConcurrentBag.class);
    }
    
    public interface IBagStateListener
    {
        void addBagItem(final int p0);
    }
    
    public interface IConcurrentBagEntry
    {
        public static final int STATE_NOT_IN_USE = 0;
        public static final int STATE_IN_USE = 1;
        public static final int STATE_REMOVED = -1;
        public static final int STATE_RESERVED = -2;
        
        boolean compareAndSet(final int p0, final int p1);
        
        void setState(final int p0);
        
        int getState();
    }
}
