package cc.dreamcode.antylogout.libs.com.zaxxer.hikari.metrics;

import cc.dreamcode.antylogout.libs.com.zaxxer.hikari.util.ClockSource;
import java.util.concurrent.atomic.AtomicLong;

public abstract class PoolStats
{
    private final AtomicLong reloadAt;
    private final long timeoutMs;
    protected volatile int totalConnections;
    protected volatile int idleConnections;
    protected volatile int activeConnections;
    protected volatile int pendingThreads;
    protected volatile int maxConnections;
    protected volatile int minConnections;
    
    public PoolStats(final long timeoutMs) {
        this.timeoutMs = timeoutMs;
        this.reloadAt = new AtomicLong();
    }
    
    public int getTotalConnections() {
        if (this.shouldLoad()) {
            this.update();
        }
        return this.totalConnections;
    }
    
    public int getIdleConnections() {
        if (this.shouldLoad()) {
            this.update();
        }
        return this.idleConnections;
    }
    
    public int getActiveConnections() {
        if (this.shouldLoad()) {
            this.update();
        }
        return this.activeConnections;
    }
    
    public int getPendingThreads() {
        if (this.shouldLoad()) {
            this.update();
        }
        return this.pendingThreads;
    }
    
    public int getMaxConnections() {
        if (this.shouldLoad()) {
            this.update();
        }
        return this.maxConnections;
    }
    
    public int getMinConnections() {
        if (this.shouldLoad()) {
            this.update();
        }
        return this.minConnections;
    }
    
    protected abstract void update();
    
    private boolean shouldLoad() {
        while (true) {
            final long now = ClockSource.currentTime();
            final long reloadTime = this.reloadAt.get();
            if (reloadTime > now) {
                return false;
            }
            if (this.reloadAt.compareAndSet(reloadTime, ClockSource.plusMillis(now, this.timeoutMs))) {
                return true;
            }
        }
    }
}
