package cc.dreamcode.antylogout.libs.com.zaxxer.hikari.pool;

import java.util.List;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import javax.sql.DataSource;
import java.sql.SQLTransientConnectionException;
import cc.dreamcode.antylogout.libs.com.zaxxer.hikari.metrics.PoolStats;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.Optional;
import java.util.Iterator;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.Callable;
import cc.dreamcode.antylogout.libs.com.zaxxer.hikari.metrics.dropwizard.CodahaleHealthChecker;
import com.codahale.metrics.health.HealthCheckRegistry;
import cc.dreamcode.antylogout.libs.com.zaxxer.hikari.metrics.micrometer.MicrometerMetricsTrackerFactory;
import io.micrometer.core.instrument.MeterRegistry;
import cc.dreamcode.antylogout.libs.com.zaxxer.hikari.metrics.MetricsTrackerFactory;
import cc.dreamcode.antylogout.libs.com.zaxxer.hikari.metrics.dropwizard.CodahaleMetricsTrackerFactory;
import com.codahale.metrics.MetricRegistry;
import java.util.concurrent.ExecutorService;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.concurrent.ThreadFactory;
import cc.dreamcode.antylogout.libs.com.zaxxer.hikari.util.ClockSource;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.BlockingQueue;
import cc.dreamcode.antylogout.libs.com.zaxxer.hikari.util.UtilityElf;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import cc.dreamcode.antylogout.libs.org.slf4j.LoggerFactory;
import cc.dreamcode.antylogout.libs.com.zaxxer.hikari.HikariConfig;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledExecutorService;
import cc.dreamcode.antylogout.libs.com.zaxxer.hikari.util.SuspendResumeLock;
import java.util.concurrent.ThreadPoolExecutor;
import cc.dreamcode.antylogout.libs.org.slf4j.Logger;
import cc.dreamcode.antylogout.libs.com.zaxxer.hikari.util.ConcurrentBag;
import cc.dreamcode.antylogout.libs.com.zaxxer.hikari.HikariPoolMXBean;

public final class HikariPool extends PoolBase implements HikariPoolMXBean, ConcurrentBag.IBagStateListener
{
    private final Logger logger;
    public static final int POOL_NORMAL = 0;
    public static final int POOL_SUSPENDED = 1;
    public static final int POOL_SHUTDOWN = 2;
    public volatile int poolState;
    private final long aliveBypassWindowMs;
    private final long housekeepingPeriodMs;
    private static final String EVICTED_CONNECTION_MESSAGE = "(connection was evicted)";
    private static final String DEAD_CONNECTION_MESSAGE = "(connection is dead)";
    private final PoolEntryCreator poolEntryCreator;
    private final PoolEntryCreator postFillPoolEntryCreator;
    private final ThreadPoolExecutor addConnectionExecutor;
    private final ThreadPoolExecutor closeConnectionExecutor;
    private final ConcurrentBag<PoolEntry> connectionBag;
    private final ProxyLeakTaskFactory leakTaskFactory;
    private final SuspendResumeLock suspendResumeLock;
    private final ScheduledExecutorService houseKeepingExecutorService;
    private ScheduledFuture<?> houseKeeperTask;
    
    public HikariPool(final HikariConfig config) {
        super(config);
        this.logger = LoggerFactory.getLogger(HikariPool.class);
        this.aliveBypassWindowMs = Long.getLong("cc.dreamcode.antylogout.libs.com.zaxxer.hikari.aliveBypassWindowMs", TimeUnit.MILLISECONDS.toMillis(500L));
        this.housekeepingPeriodMs = Long.getLong("cc.dreamcode.antylogout.libs.com.zaxxer.hikari.housekeeping.periodMs", TimeUnit.SECONDS.toMillis(30L));
        this.poolEntryCreator = new PoolEntryCreator();
        this.postFillPoolEntryCreator = new PoolEntryCreator("After adding ");
        this.connectionBag = new ConcurrentBag<PoolEntry>(this);
        this.suspendResumeLock = (config.isAllowPoolSuspension() ? new SuspendResumeLock() : SuspendResumeLock.FAUX_LOCK);
        this.houseKeepingExecutorService = this.initializeHouseKeepingExecutorService();
        this.checkFailFast();
        if (config.getMetricsTrackerFactory() != null) {
            this.setMetricsTrackerFactory(config.getMetricsTrackerFactory());
        }
        else {
            this.setMetricRegistry(config.getMetricRegistry());
        }
        this.setHealthCheckRegistry(config.getHealthCheckRegistry());
        this.handleMBeans(this, true);
        final ThreadFactory threadFactory = config.getThreadFactory();
        final int maxPoolSize = config.getMaximumPoolSize();
        final LinkedBlockingQueue<Runnable> addConnectionQueue = (LinkedBlockingQueue<Runnable>)new LinkedBlockingQueue(maxPoolSize);
        this.addConnectionExecutor = UtilityElf.createThreadPoolExecutor((BlockingQueue<Runnable>)addConnectionQueue, this.poolName + " connection adder", threadFactory, (RejectedExecutionHandler)new UtilityElf.CustomDiscardPolicy());
        this.closeConnectionExecutor = UtilityElf.createThreadPoolExecutor(maxPoolSize, this.poolName + " connection closer", threadFactory, (RejectedExecutionHandler)new ThreadPoolExecutor.CallerRunsPolicy());
        this.leakTaskFactory = new ProxyLeakTaskFactory(config.getLeakDetectionThreshold(), this.houseKeepingExecutorService);
        this.houseKeeperTask = (ScheduledFuture<?>)this.houseKeepingExecutorService.scheduleWithFixedDelay((Runnable)new HouseKeeper(), 100L, this.housekeepingPeriodMs, TimeUnit.MILLISECONDS);
        if (Boolean.getBoolean("cc.dreamcode.antylogout.libs.com.zaxxer.hikari.blockUntilFilled") && config.getInitializationFailTimeout() > 1L) {
            this.addConnectionExecutor.setMaximumPoolSize(Math.min(16, Runtime.getRuntime().availableProcessors()));
            this.addConnectionExecutor.setCorePoolSize(Math.min(16, Runtime.getRuntime().availableProcessors()));
            final long startTime = ClockSource.currentTime();
            while (ClockSource.elapsedMillis(startTime) < config.getInitializationFailTimeout() && this.getTotalConnections() < config.getMinimumIdle()) {
                UtilityElf.quietlySleep(TimeUnit.MILLISECONDS.toMillis(100L));
            }
            this.addConnectionExecutor.setCorePoolSize(1);
            this.addConnectionExecutor.setMaximumPoolSize(1);
        }
    }
    
    public Connection getConnection() throws SQLException {
        return this.getConnection(this.connectionTimeout);
    }
    
    public Connection getConnection(final long hardTimeout) throws SQLException {
        this.suspendResumeLock.acquire();
        final long startTime = ClockSource.currentTime();
        try {
            long timeout = hardTimeout;
            do {
                final PoolEntry poolEntry = this.connectionBag.borrow(timeout, TimeUnit.MILLISECONDS);
                if (poolEntry == null) {
                    break;
                }
                final long now = ClockSource.currentTime();
                if (!poolEntry.isMarkedEvicted() && (ClockSource.elapsedMillis(poolEntry.lastAccessed, now) <= this.aliveBypassWindowMs || !this.isConnectionDead(poolEntry.connection))) {
                    this.metricsTracker.recordBorrowStats(poolEntry, startTime);
                    return poolEntry.createProxyConnection(this.leakTaskFactory.schedule(poolEntry));
                }
                this.closeConnection(poolEntry, poolEntry.isMarkedEvicted() ? "(connection was evicted)" : "(connection is dead)");
                timeout = hardTimeout - ClockSource.elapsedMillis(startTime);
            } while (timeout > 0L);
            this.metricsTracker.recordBorrowTimeoutStats(startTime);
            throw this.createTimeoutException(startTime);
        }
        catch (final InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new SQLException(this.poolName + " - Interrupted during connection acquisition", (Throwable)e);
        }
        finally {
            this.suspendResumeLock.release();
        }
    }
    
    public synchronized void shutdown() throws InterruptedException {
        try {
            this.poolState = 2;
            if (this.addConnectionExecutor == null) {
                return;
            }
            this.logPoolState("Before shutdown ");
            if (this.houseKeeperTask != null) {
                this.houseKeeperTask.cancel(false);
                this.houseKeeperTask = null;
            }
            this.softEvictConnections();
            this.addConnectionExecutor.shutdown();
            if (!this.addConnectionExecutor.awaitTermination(this.getLoginTimeout(), TimeUnit.SECONDS)) {
                this.logger.warn("Timed-out waiting for add connection executor to shutdown");
            }
            this.destroyHouseKeepingExecutorService();
            this.connectionBag.close();
            final ThreadPoolExecutor assassinExecutor = UtilityElf.createThreadPoolExecutor(this.config.getMaximumPoolSize(), this.poolName + " connection assassinator", this.config.getThreadFactory(), (RejectedExecutionHandler)new ThreadPoolExecutor.CallerRunsPolicy());
            try {
                final long start = ClockSource.currentTime();
                do {
                    this.abortActiveConnections((ExecutorService)assassinExecutor);
                    this.softEvictConnections();
                } while (this.getTotalConnections() > 0 && ClockSource.elapsedMillis(start) < TimeUnit.SECONDS.toMillis(10L));
            }
            finally {
                assassinExecutor.shutdown();
                if (!assassinExecutor.awaitTermination(10L, TimeUnit.SECONDS)) {
                    this.logger.warn("Timed-out waiting for connection assassin to shutdown");
                }
            }
            this.shutdownNetworkTimeoutExecutor();
            this.closeConnectionExecutor.shutdown();
            if (!this.closeConnectionExecutor.awaitTermination(10L, TimeUnit.SECONDS)) {
                this.logger.warn("Timed-out waiting for close connection executor to shutdown");
            }
        }
        finally {
            this.logPoolState("After shutdown ");
            this.handleMBeans(this, false);
            this.metricsTracker.close();
        }
    }
    
    public void evictConnection(final Connection connection) {
        final ProxyConnection proxyConnection = (ProxyConnection)connection;
        proxyConnection.cancelLeakTask();
        try {
            this.softEvictConnection(proxyConnection.getPoolEntry(), "(connection evicted by user)", !connection.isClosed());
        }
        catch (final SQLException ex) {}
    }
    
    public void setMetricRegistry(final Object metricRegistry) {
        if (metricRegistry != null && UtilityElf.safeIsAssignableFrom(metricRegistry, "com.codahale.metrics.MetricRegistry")) {
            this.setMetricsTrackerFactory(new CodahaleMetricsTrackerFactory((MetricRegistry)metricRegistry));
        }
        else if (metricRegistry != null && UtilityElf.safeIsAssignableFrom(metricRegistry, "io.micrometer.core.instrument.MeterRegistry")) {
            this.setMetricsTrackerFactory(new MicrometerMetricsTrackerFactory((MeterRegistry)metricRegistry));
        }
        else {
            this.setMetricsTrackerFactory(null);
        }
    }
    
    public void setMetricsTrackerFactory(final MetricsTrackerFactory metricsTrackerFactory) {
        if (metricsTrackerFactory != null) {
            this.metricsTracker = new MetricsTrackerDelegate(metricsTrackerFactory.create(this.config.getPoolName(), this.getPoolStats()));
        }
        else {
            this.metricsTracker = new NopMetricsTrackerDelegate();
        }
    }
    
    public void setHealthCheckRegistry(final Object healthCheckRegistry) {
        if (healthCheckRegistry != null) {
            CodahaleHealthChecker.registerHealthChecks(this, this.config, (HealthCheckRegistry)healthCheckRegistry);
        }
    }
    
    @Override
    public void addBagItem(final int waiting) {
        if (waiting > this.addConnectionExecutor.getQueue().size()) {
            this.addConnectionExecutor.submit((Callable)this.poolEntryCreator);
        }
    }
    
    @Override
    public int getActiveConnections() {
        return this.connectionBag.getCount(1);
    }
    
    @Override
    public int getIdleConnections() {
        return this.connectionBag.getCount(0);
    }
    
    @Override
    public int getTotalConnections() {
        return this.connectionBag.size();
    }
    
    @Override
    public int getThreadsAwaitingConnection() {
        return this.connectionBag.getWaitingThreadCount();
    }
    
    @Override
    public void softEvictConnections() {
        this.connectionBag.values().forEach(poolEntry -> this.softEvictConnection(poolEntry, "(connection evicted)", false));
    }
    
    @Override
    public synchronized void suspendPool() {
        if (this.suspendResumeLock == SuspendResumeLock.FAUX_LOCK) {
            throw new IllegalStateException(this.poolName + " - is not suspendable");
        }
        if (this.poolState != 1) {
            this.suspendResumeLock.suspend();
            this.poolState = 1;
        }
    }
    
    @Override
    public synchronized void resumePool() {
        if (this.poolState == 1) {
            this.poolState = 0;
            this.fillPool(false);
            this.suspendResumeLock.resume();
        }
    }
    
    void logPoolState(final String... prefix) {
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("{} - {}stats (total={}, active={}, idle={}, waiting={})", this.poolName, (prefix.length > 0) ? prefix[0] : "", this.getTotalConnections(), this.getActiveConnections(), this.getIdleConnections(), this.getThreadsAwaitingConnection());
        }
    }
    
    @Override
    void recycle(final PoolEntry poolEntry) {
        this.metricsTracker.recordConnectionUsage(poolEntry);
        this.connectionBag.requite(poolEntry);
    }
    
    void closeConnection(final PoolEntry poolEntry, final String closureReason) {
        if (this.connectionBag.remove(poolEntry)) {
            final Connection connection = poolEntry.close();
            this.closeConnectionExecutor.execute(() -> {
                this.quietlyCloseConnection(connection, closureReason);
                if (this.poolState == 0) {
                    this.fillPool(false);
                }
            });
        }
    }
    
    int[] getPoolStateCounts() {
        return this.connectionBag.getStateCounts();
    }
    
    private PoolEntry createPoolEntry() {
        try {
            final PoolEntry poolEntry = this.newPoolEntry();
            final long maxLifetime = this.config.getMaxLifetime();
            if (maxLifetime > 0L) {
                final long variance = (maxLifetime > 10000L) ? ThreadLocalRandom.current().nextLong(maxLifetime / 40L) : 0L;
                final long lifetime = maxLifetime - variance;
                poolEntry.setFutureEol((ScheduledFuture<?>)this.houseKeepingExecutorService.schedule((Runnable)new MaxLifetimeTask(poolEntry), lifetime, TimeUnit.MILLISECONDS));
            }
            final long keepaliveTime = this.config.getKeepaliveTime();
            if (keepaliveTime > 0L) {
                final long variance2 = ThreadLocalRandom.current().nextLong(keepaliveTime / 10L);
                final long heartbeatTime = keepaliveTime - variance2;
                poolEntry.setKeepalive((ScheduledFuture<?>)this.houseKeepingExecutorService.scheduleWithFixedDelay((Runnable)new KeepaliveTask(poolEntry), heartbeatTime, heartbeatTime, TimeUnit.MILLISECONDS));
            }
            return poolEntry;
        }
        catch (final ConnectionSetupException e) {
            if (this.poolState == 0) {
                this.logger.error("{} - Error thrown while acquiring connection from data source", this.poolName, e.getCause());
                this.lastConnectionFailure.set((Object)e);
            }
        }
        catch (final Exception e2) {
            if (this.poolState == 0) {
                this.logger.debug("{} - Cannot acquire connection from data source", this.poolName, e2);
            }
        }
        return null;
    }
    
    private synchronized void fillPool(final boolean isAfterAdd) {
        final int idle = this.getIdleConnections();
        final boolean shouldAdd = this.getTotalConnections() < this.config.getMaximumPoolSize() && idle < this.config.getMinimumIdle();
        if (shouldAdd) {
            for (int countToAdd = this.config.getMinimumIdle() - idle, i = 0; i < countToAdd; ++i) {
                this.addConnectionExecutor.submit((Callable)(isAfterAdd ? this.postFillPoolEntryCreator : this.poolEntryCreator));
            }
        }
        else if (isAfterAdd) {
            this.logger.debug("{} - Fill pool skipped, pool has sufficient level or currently being filled.", this.poolName);
        }
    }
    
    private void abortActiveConnections(final ExecutorService assassinExecutor) {
        for (final PoolEntry poolEntry : this.connectionBag.values(1)) {
            final Connection connection = poolEntry.close();
            try {
                connection.abort((Executor)assassinExecutor);
            }
            catch (final Throwable e) {
                this.quietlyCloseConnection(connection, "(connection aborted during shutdown)");
            }
            finally {
                this.connectionBag.remove(poolEntry);
            }
        }
    }
    
    private void checkFailFast() {
        final long initializationTimeout = this.config.getInitializationFailTimeout();
        if (initializationTimeout < 0L) {
            return;
        }
        final long startTime = ClockSource.currentTime();
        do {
            final PoolEntry poolEntry = this.createPoolEntry();
            if (poolEntry != null) {
                if (this.config.getMinimumIdle() > 0) {
                    this.connectionBag.add(poolEntry);
                    this.logger.info("{} - Added connection {}", this.poolName, poolEntry.connection);
                }
                else {
                    this.quietlyCloseConnection(poolEntry.close(), "(initialization check complete and minimumIdle is zero)");
                }
                return;
            }
            if (this.getLastConnectionFailure() instanceof ConnectionSetupException) {
                this.throwPoolInitializationException(this.getLastConnectionFailure().getCause());
            }
            UtilityElf.quietlySleep(TimeUnit.SECONDS.toMillis(1L));
        } while (ClockSource.elapsedMillis(startTime) < initializationTimeout);
        if (initializationTimeout > 0L) {
            this.throwPoolInitializationException((Throwable)this.getLastConnectionFailure());
        }
    }
    
    private void throwPoolInitializationException(final Throwable t) {
        this.destroyHouseKeepingExecutorService();
        throw new PoolInitializationException(t);
    }
    
    private boolean softEvictConnection(final PoolEntry poolEntry, final String reason, final boolean owner) {
        poolEntry.markEvicted();
        if (owner || this.connectionBag.reserve(poolEntry)) {
            this.closeConnection(poolEntry, reason);
            return true;
        }
        return false;
    }
    
    private ScheduledExecutorService initializeHouseKeepingExecutorService() {
        if (this.config.getScheduledExecutor() == null) {
            final ThreadFactory threadFactory = (ThreadFactory)Optional.ofNullable((Object)this.config.getThreadFactory()).orElseGet(() -> new UtilityElf.DefaultThreadFactory(this.poolName + " housekeeper"));
            final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1, threadFactory, (RejectedExecutionHandler)new ThreadPoolExecutor.DiscardPolicy());
            executor.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
            executor.setRemoveOnCancelPolicy(true);
            return (ScheduledExecutorService)executor;
        }
        return this.config.getScheduledExecutor();
    }
    
    private void destroyHouseKeepingExecutorService() {
        if (this.config.getScheduledExecutor() == null) {
            this.houseKeepingExecutorService.shutdownNow();
        }
    }
    
    private PoolStats getPoolStats() {
        return new PoolStats(TimeUnit.SECONDS.toMillis(1L)) {
            @Override
            protected void update() {
                this.pendingThreads = HikariPool.this.getThreadsAwaitingConnection();
                this.idleConnections = HikariPool.this.getIdleConnections();
                this.totalConnections = HikariPool.this.getTotalConnections();
                this.activeConnections = HikariPool.this.getActiveConnections();
                this.maxConnections = HikariPool.this.config.getMaximumPoolSize();
                this.minConnections = HikariPool.this.config.getMinimumIdle();
            }
        };
    }
    
    private SQLException createTimeoutException(final long startTime) {
        this.logPoolState("Timeout failure ");
        this.metricsTracker.recordConnectionTimeout();
        String sqlState = null;
        final Exception originalException = this.getLastConnectionFailure();
        if (originalException instanceof SQLException) {
            sqlState = ((SQLException)originalException).getSQLState();
        }
        final SQLTransientConnectionException connectionException = new SQLTransientConnectionException(this.poolName + " - Connection is not available, request timed out after " + ClockSource.elapsedMillis(startTime) + "ms (total=" + this.getTotalConnections() + ", active=" + this.getActiveConnections() + ", idle=" + this.getIdleConnections() + ", waiting=" + this.getThreadsAwaitingConnection(), sqlState, (Throwable)originalException);
        if (originalException instanceof SQLException) {
            connectionException.setNextException((SQLException)originalException);
        }
        return (SQLException)connectionException;
    }
    
    private final class PoolEntryCreator implements Callable<Boolean>
    {
        private final String loggingPrefix;
        
        PoolEntryCreator(final HikariPool hikariPool) {
            this(hikariPool, null);
        }
        
        PoolEntryCreator(final String loggingPrefix) {
            this.loggingPrefix = loggingPrefix;
        }
        
        public Boolean call() {
            long backoffMs = 10L;
            boolean added = false;
            try {
                while (this.shouldContinueCreating()) {
                    final PoolEntry poolEntry = HikariPool.this.createPoolEntry();
                    if (poolEntry != null) {
                        added = true;
                        HikariPool.this.connectionBag.add(poolEntry);
                        HikariPool.this.logger.debug("{} - Added connection {}", HikariPool.this.poolName, poolEntry.connection);
                        UtilityElf.quietlySleep(30L);
                        break;
                    }
                    if (this.loggingPrefix != null && backoffMs % 50L == 0L) {
                        HikariPool.this.logger.debug("{} - Connection add failed, sleeping with backoff: {}ms", HikariPool.this.poolName, backoffMs);
                    }
                    UtilityElf.quietlySleep(backoffMs);
                    backoffMs = Math.min(TimeUnit.SECONDS.toMillis(5L), backoffMs * 2L);
                }
            }
            finally {
                if (added && this.loggingPrefix != null) {
                    HikariPool.this.logPoolState(this.loggingPrefix);
                }
                else {
                    HikariPool.this.logPoolState("Connection not added, ");
                }
            }
            return Boolean.FALSE;
        }
        
        private synchronized boolean shouldContinueCreating() {
            return HikariPool.this.poolState == 0 && HikariPool.this.getTotalConnections() < HikariPool.this.config.getMaximumPoolSize() && (HikariPool.this.getIdleConnections() < HikariPool.this.config.getMinimumIdle() || HikariPool.this.connectionBag.getWaitingThreadCount() > HikariPool.this.getIdleConnections());
        }
    }
    
    private final class HouseKeeper implements Runnable
    {
        private volatile long previous;
        private final AtomicReferenceFieldUpdater<PoolBase, String> catalogUpdater;
        
        private HouseKeeper() {
            this.previous = ClockSource.plusMillis(ClockSource.currentTime(), -HikariPool.this.housekeepingPeriodMs);
            this.catalogUpdater = (AtomicReferenceFieldUpdater<PoolBase, String>)AtomicReferenceFieldUpdater.newUpdater((Class)PoolBase.class, (Class)String.class, "catalog");
        }
        
        public void run() {
            try {
                HikariPool.this.connectionTimeout = HikariPool.this.config.getConnectionTimeout();
                HikariPool.this.validationTimeout = HikariPool.this.config.getValidationTimeout();
                HikariPool.this.leakTaskFactory.updateLeakDetectionThreshold(HikariPool.this.config.getLeakDetectionThreshold());
                if (HikariPool.this.config.getCatalog() != null && !HikariPool.this.config.getCatalog().equals((Object)HikariPool.this.catalog)) {
                    this.catalogUpdater.set((Object)HikariPool.this, (Object)HikariPool.this.config.getCatalog());
                }
                final long idleTimeout = HikariPool.this.config.getIdleTimeout();
                final long now = ClockSource.currentTime();
                if (ClockSource.plusMillis(now, 128L) < ClockSource.plusMillis(this.previous, HikariPool.this.housekeepingPeriodMs)) {
                    HikariPool.this.logger.warn("{} - Retrograde clock change detected (housekeeper delta={}), soft-evicting connections from pool.", HikariPool.this.poolName, ClockSource.elapsedDisplayString(this.previous, now));
                    this.previous = now;
                    HikariPool.this.softEvictConnections();
                    return;
                }
                if (now > ClockSource.plusMillis(this.previous, 3L * HikariPool.this.housekeepingPeriodMs / 2L)) {
                    HikariPool.this.logger.warn("{} - Thread starvation or clock leap detected (housekeeper delta={}).", HikariPool.this.poolName, ClockSource.elapsedDisplayString(this.previous, now));
                }
                this.previous = now;
                if (idleTimeout > 0L && HikariPool.this.config.getMinimumIdle() < HikariPool.this.config.getMaximumPoolSize()) {
                    HikariPool.this.logPoolState("Before cleanup ");
                    final List<PoolEntry> notInUse = HikariPool.this.connectionBag.values(0);
                    int maxToRemove = notInUse.size() - HikariPool.this.config.getMinimumIdle();
                    for (final PoolEntry entry : notInUse) {
                        if (maxToRemove > 0 && ClockSource.elapsedMillis(entry.lastAccessed, now) > idleTimeout && HikariPool.this.connectionBag.reserve(entry)) {
                            HikariPool.this.closeConnection(entry, "(connection has passed idleTimeout)");
                            --maxToRemove;
                        }
                    }
                    HikariPool.this.logPoolState("After cleanup  ");
                }
                else {
                    HikariPool.this.logPoolState("Pool ");
                }
                HikariPool.this.fillPool(true);
            }
            catch (final Exception e) {
                HikariPool.this.logger.error("Unexpected exception in housekeeping task", (Throwable)e);
            }
        }
    }
    
    private final class MaxLifetimeTask implements Runnable
    {
        private final PoolEntry poolEntry;
        
        MaxLifetimeTask(final PoolEntry poolEntry) {
            this.poolEntry = poolEntry;
        }
        
        public void run() {
            if (HikariPool.this.softEvictConnection(this.poolEntry, "(connection has passed maxLifetime)", false)) {
                HikariPool.this.addBagItem(HikariPool.this.connectionBag.getWaitingThreadCount());
            }
        }
    }
    
    private final class KeepaliveTask implements Runnable
    {
        private final PoolEntry poolEntry;
        
        KeepaliveTask(final PoolEntry poolEntry) {
            this.poolEntry = poolEntry;
        }
        
        public void run() {
            if (HikariPool.this.connectionBag.reserve(this.poolEntry)) {
                if (HikariPool.this.isConnectionDead(this.poolEntry.connection)) {
                    HikariPool.this.softEvictConnection(this.poolEntry, "(connection is dead)", true);
                    HikariPool.this.addBagItem(HikariPool.this.connectionBag.getWaitingThreadCount());
                }
                else {
                    HikariPool.this.connectionBag.unreserve(this.poolEntry);
                    HikariPool.this.logger.debug("{} - keepalive: connection {} is alive", HikariPool.this.poolName, this.poolEntry.connection);
                }
            }
        }
    }
    
    public static class PoolInitializationException extends RuntimeException
    {
        private static final long serialVersionUID = 929872118275916520L;
        
        public PoolInitializationException(final Throwable t) {
            super("Failed to initialize pool: " + t.getMessage(), t);
        }
    }
}
