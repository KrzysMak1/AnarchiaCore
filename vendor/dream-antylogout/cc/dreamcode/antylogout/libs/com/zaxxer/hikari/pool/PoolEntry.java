package cc.dreamcode.antylogout.libs.com.zaxxer.hikari.pool;

import cc.dreamcode.antylogout.libs.org.slf4j.LoggerFactory;
import java.sql.SQLException;
import cc.dreamcode.antylogout.libs.com.zaxxer.hikari.util.ClockSource;
import java.sql.Statement;
import cc.dreamcode.antylogout.libs.com.zaxxer.hikari.util.FastList;
import java.util.concurrent.ScheduledFuture;
import java.sql.Connection;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import cc.dreamcode.antylogout.libs.org.slf4j.Logger;
import cc.dreamcode.antylogout.libs.com.zaxxer.hikari.util.ConcurrentBag;

final class PoolEntry implements ConcurrentBag.IConcurrentBagEntry
{
    private static final Logger LOGGER;
    private static final AtomicIntegerFieldUpdater<PoolEntry> stateUpdater;
    Connection connection;
    long lastAccessed;
    long lastBorrowed;
    private volatile int state;
    private volatile boolean evict;
    private volatile ScheduledFuture<?> endOfLife;
    private volatile ScheduledFuture<?> keepalive;
    private final FastList<Statement> openStatements;
    private final HikariPool hikariPool;
    private final boolean isReadOnly;
    private final boolean isAutoCommit;
    
    PoolEntry(final Connection connection, final PoolBase pool, final boolean isReadOnly, final boolean isAutoCommit) {
        this.state = 0;
        this.connection = connection;
        this.hikariPool = (HikariPool)pool;
        this.isReadOnly = isReadOnly;
        this.isAutoCommit = isAutoCommit;
        this.lastAccessed = ClockSource.currentTime();
        this.openStatements = new FastList<Statement>(Statement.class, 16);
    }
    
    void recycle() {
        if (this.connection != null) {
            this.lastAccessed = ClockSource.currentTime();
            this.hikariPool.recycle(this);
        }
    }
    
    void setFutureEol(final ScheduledFuture<?> endOfLife) {
        this.endOfLife = endOfLife;
    }
    
    public void setKeepalive(final ScheduledFuture<?> keepalive) {
        this.keepalive = keepalive;
    }
    
    Connection createProxyConnection(final ProxyLeakTask leakTask) {
        return (Connection)ProxyFactory.getProxyConnection(this, this.connection, this.openStatements, leakTask, this.isReadOnly, this.isAutoCommit);
    }
    
    void resetConnectionState(final ProxyConnection proxyConnection, final int dirtyBits) throws SQLException {
        this.hikariPool.resetConnectionState(this.connection, proxyConnection, dirtyBits);
    }
    
    String getPoolName() {
        return this.hikariPool.toString();
    }
    
    boolean isMarkedEvicted() {
        return this.evict;
    }
    
    void markEvicted() {
        this.evict = true;
    }
    
    void evict(final String closureReason) {
        this.hikariPool.closeConnection(this, closureReason);
    }
    
    long getMillisSinceBorrowed() {
        return ClockSource.elapsedMillis(this.lastBorrowed);
    }
    
    PoolBase getPoolBase() {
        return this.hikariPool;
    }
    
    @Override
    public String toString() {
        final long now = ClockSource.currentTime();
        return this.connection + ", accessed " + ClockSource.elapsedDisplayString(this.lastAccessed, now) + " ago, " + this.stateToString();
    }
    
    @Override
    public int getState() {
        return PoolEntry.stateUpdater.get((Object)this);
    }
    
    @Override
    public boolean compareAndSet(final int expect, final int update) {
        return PoolEntry.stateUpdater.compareAndSet((Object)this, expect, update);
    }
    
    @Override
    public void setState(final int update) {
        PoolEntry.stateUpdater.set((Object)this, update);
    }
    
    Connection close() {
        final ScheduledFuture<?> eol = this.endOfLife;
        if (eol != null && !eol.isDone() && !eol.cancel(false)) {
            PoolEntry.LOGGER.warn("{} - maxLifeTime expiration task cancellation unexpectedly returned false for connection {}", this.getPoolName(), this.connection);
        }
        final ScheduledFuture<?> ka = this.keepalive;
        if (ka != null && !ka.isDone() && !ka.cancel(false)) {
            PoolEntry.LOGGER.warn("{} - keepalive task cancellation unexpectedly returned false for connection {}", this.getPoolName(), this.connection);
        }
        final Connection con = this.connection;
        this.connection = null;
        this.endOfLife = null;
        this.keepalive = null;
        return con;
    }
    
    private String stateToString() {
        switch (this.state) {
            case 1: {
                return "IN_USE";
            }
            case 0: {
                return "NOT_IN_USE";
            }
            case -1: {
                return "REMOVED";
            }
            case -2: {
                return "RESERVED";
            }
            default: {
                return "Invalid";
            }
        }
    }
    
    static {
        LOGGER = LoggerFactory.getLogger(PoolEntry.class);
        stateUpdater = AtomicIntegerFieldUpdater.newUpdater((Class)PoolEntry.class, "state");
    }
}
