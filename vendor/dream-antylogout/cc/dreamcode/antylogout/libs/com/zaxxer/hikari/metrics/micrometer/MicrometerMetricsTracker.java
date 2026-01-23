package cc.dreamcode.antylogout.libs.com.zaxxer.hikari.metrics.micrometer;

import io.micrometer.core.instrument.Meter;
import java.util.concurrent.TimeUnit;
import cc.dreamcode.antylogout.libs.com.zaxxer.hikari.metrics.PoolStats;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Timer;
import cc.dreamcode.antylogout.libs.com.zaxxer.hikari.metrics.IMetricsTracker;

public class MicrometerMetricsTracker implements IMetricsTracker
{
    public static final String HIKARI_METRIC_NAME_PREFIX = "hikaricp";
    private static final String METRIC_CATEGORY = "pool";
    private static final String METRIC_NAME_WAIT = "hikaricp.connections.acquire";
    private static final String METRIC_NAME_USAGE = "hikaricp.connections.usage";
    private static final String METRIC_NAME_CONNECT = "hikaricp.connections.creation";
    private static final String METRIC_NAME_TIMEOUT_RATE = "hikaricp.connections.timeout";
    private static final String METRIC_NAME_TOTAL_CONNECTIONS = "hikaricp.connections";
    private static final String METRIC_NAME_IDLE_CONNECTIONS = "hikaricp.connections.idle";
    private static final String METRIC_NAME_ACTIVE_CONNECTIONS = "hikaricp.connections.active";
    private static final String METRIC_NAME_PENDING_CONNECTIONS = "hikaricp.connections.pending";
    private static final String METRIC_NAME_MAX_CONNECTIONS = "hikaricp.connections.max";
    private static final String METRIC_NAME_MIN_CONNECTIONS = "hikaricp.connections.min";
    private final Timer connectionObtainTimer;
    private final Counter connectionTimeoutCounter;
    private final Timer connectionUsage;
    private final Timer connectionCreation;
    private final Gauge totalConnectionGauge;
    private final Gauge idleConnectionGauge;
    private final Gauge activeConnectionGauge;
    private final Gauge pendingConnectionGauge;
    private final Gauge maxConnectionGauge;
    private final Gauge minConnectionGauge;
    private final MeterRegistry meterRegistry;
    private final PoolStats poolStats;
    
    MicrometerMetricsTracker(final String poolName, final PoolStats poolStats, final MeterRegistry meterRegistry) {
        this.poolStats = poolStats;
        this.meterRegistry = meterRegistry;
        this.connectionObtainTimer = Timer.builder("hikaricp.connections.acquire").description("Connection acquire time").tags(new String[] { "pool", poolName }).register(meterRegistry);
        this.connectionCreation = Timer.builder("hikaricp.connections.creation").description("Connection creation time").tags(new String[] { "pool", poolName }).register(meterRegistry);
        this.connectionUsage = Timer.builder("hikaricp.connections.usage").description("Connection usage time").tags(new String[] { "pool", poolName }).register(meterRegistry);
        this.connectionTimeoutCounter = Counter.builder("hikaricp.connections.timeout").description("Connection timeout total count").tags(new String[] { "pool", poolName }).register(meterRegistry);
        this.totalConnectionGauge = Gauge.builder("hikaricp.connections", (Object)poolStats, PoolStats::getTotalConnections).description("Total connections").tags(new String[] { "pool", poolName }).register(meterRegistry);
        this.idleConnectionGauge = Gauge.builder("hikaricp.connections.idle", (Object)poolStats, PoolStats::getIdleConnections).description("Idle connections").tags(new String[] { "pool", poolName }).register(meterRegistry);
        this.activeConnectionGauge = Gauge.builder("hikaricp.connections.active", (Object)poolStats, PoolStats::getActiveConnections).description("Active connections").tags(new String[] { "pool", poolName }).register(meterRegistry);
        this.pendingConnectionGauge = Gauge.builder("hikaricp.connections.pending", (Object)poolStats, PoolStats::getPendingThreads).description("Pending threads").tags(new String[] { "pool", poolName }).register(meterRegistry);
        this.maxConnectionGauge = Gauge.builder("hikaricp.connections.max", (Object)poolStats, PoolStats::getMaxConnections).description("Max connections").tags(new String[] { "pool", poolName }).register(meterRegistry);
        this.minConnectionGauge = Gauge.builder("hikaricp.connections.min", (Object)poolStats, PoolStats::getMinConnections).description("Min connections").tags(new String[] { "pool", poolName }).register(meterRegistry);
    }
    
    @Override
    public void recordConnectionAcquiredNanos(final long elapsedAcquiredNanos) {
        this.connectionObtainTimer.record(elapsedAcquiredNanos, TimeUnit.NANOSECONDS);
    }
    
    @Override
    public void recordConnectionUsageMillis(final long elapsedBorrowedMillis) {
        this.connectionUsage.record(elapsedBorrowedMillis, TimeUnit.MILLISECONDS);
    }
    
    @Override
    public void recordConnectionTimeout() {
        this.connectionTimeoutCounter.increment();
    }
    
    @Override
    public void recordConnectionCreatedMillis(final long connectionCreatedMillis) {
        this.connectionCreation.record(connectionCreatedMillis, TimeUnit.MILLISECONDS);
    }
    
    @Override
    public void close() {
        this.meterRegistry.remove((Meter)this.connectionObtainTimer);
        this.meterRegistry.remove((Meter)this.connectionTimeoutCounter);
        this.meterRegistry.remove((Meter)this.connectionUsage);
        this.meterRegistry.remove((Meter)this.connectionCreation);
        this.meterRegistry.remove((Meter)this.totalConnectionGauge);
        this.meterRegistry.remove((Meter)this.idleConnectionGauge);
        this.meterRegistry.remove((Meter)this.activeConnectionGauge);
        this.meterRegistry.remove((Meter)this.pendingConnectionGauge);
        this.meterRegistry.remove((Meter)this.maxConnectionGauge);
        this.meterRegistry.remove((Meter)this.minConnectionGauge);
    }
}
