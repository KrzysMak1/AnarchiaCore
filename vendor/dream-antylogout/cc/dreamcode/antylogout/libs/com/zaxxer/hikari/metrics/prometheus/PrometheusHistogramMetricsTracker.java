package cc.dreamcode.antylogout.libs.com.zaxxer.hikari.metrics.prometheus;

import java.util.concurrent.ConcurrentHashMap;
import io.prometheus.client.CollectorRegistry;
import java.util.Map;
import io.prometheus.client.Histogram;
import io.prometheus.client.Counter;
import cc.dreamcode.antylogout.libs.com.zaxxer.hikari.metrics.IMetricsTracker;

class PrometheusHistogramMetricsTracker implements IMetricsTracker
{
    private static final Counter CONNECTION_TIMEOUT_COUNTER;
    private static final Histogram ELAPSED_ACQUIRED_HISTOGRAM;
    private static final Histogram ELAPSED_BORROWED_HISTOGRAM;
    private static final Histogram ELAPSED_CREATION_HISTOGRAM;
    private final Counter.Child connectionTimeoutCounterChild;
    private static final Map<CollectorRegistry, PrometheusMetricsTrackerFactory.RegistrationStatus> registrationStatuses;
    private final String poolName;
    private final HikariCPCollector hikariCPCollector;
    private final Histogram.Child elapsedAcquiredHistogramChild;
    private final Histogram.Child elapsedBorrowedHistogramChild;
    private final Histogram.Child elapsedCreationHistogramChild;
    
    private static Histogram registerHistogram(final String name, final String help, final double bucketStart) {
        return ((Histogram.Builder)((Histogram.Builder)((Histogram.Builder)Histogram.build().name(name)).labelNames(new String[] { "pool" })).help(help)).exponentialBuckets(bucketStart, 2.0, 11).create();
    }
    
    PrometheusHistogramMetricsTracker(final String poolName, final CollectorRegistry collectorRegistry, final HikariCPCollector hikariCPCollector) {
        this.registerMetrics(collectorRegistry);
        this.poolName = poolName;
        this.hikariCPCollector = hikariCPCollector;
        this.connectionTimeoutCounterChild = (Counter.Child)PrometheusHistogramMetricsTracker.CONNECTION_TIMEOUT_COUNTER.labels(new String[] { poolName });
        this.elapsedAcquiredHistogramChild = (Histogram.Child)PrometheusHistogramMetricsTracker.ELAPSED_ACQUIRED_HISTOGRAM.labels(new String[] { poolName });
        this.elapsedBorrowedHistogramChild = (Histogram.Child)PrometheusHistogramMetricsTracker.ELAPSED_BORROWED_HISTOGRAM.labels(new String[] { poolName });
        this.elapsedCreationHistogramChild = (Histogram.Child)PrometheusHistogramMetricsTracker.ELAPSED_CREATION_HISTOGRAM.labels(new String[] { poolName });
    }
    
    private void registerMetrics(final CollectorRegistry collectorRegistry) {
        if (PrometheusHistogramMetricsTracker.registrationStatuses.putIfAbsent((Object)collectorRegistry, (Object)PrometheusMetricsTrackerFactory.RegistrationStatus.REGISTERED) == null) {
            PrometheusHistogramMetricsTracker.CONNECTION_TIMEOUT_COUNTER.register(collectorRegistry);
            PrometheusHistogramMetricsTracker.ELAPSED_ACQUIRED_HISTOGRAM.register(collectorRegistry);
            PrometheusHistogramMetricsTracker.ELAPSED_BORROWED_HISTOGRAM.register(collectorRegistry);
            PrometheusHistogramMetricsTracker.ELAPSED_CREATION_HISTOGRAM.register(collectorRegistry);
        }
    }
    
    @Override
    public void recordConnectionAcquiredNanos(final long elapsedAcquiredNanos) {
        this.elapsedAcquiredHistogramChild.observe((double)elapsedAcquiredNanos);
    }
    
    @Override
    public void recordConnectionUsageMillis(final long elapsedBorrowedMillis) {
        this.elapsedBorrowedHistogramChild.observe((double)elapsedBorrowedMillis);
    }
    
    @Override
    public void recordConnectionCreatedMillis(final long connectionCreatedMillis) {
        this.elapsedCreationHistogramChild.observe((double)connectionCreatedMillis);
    }
    
    @Override
    public void recordConnectionTimeout() {
        this.connectionTimeoutCounterChild.inc();
    }
    
    @Override
    public void close() {
        this.hikariCPCollector.remove(this.poolName);
        PrometheusHistogramMetricsTracker.CONNECTION_TIMEOUT_COUNTER.remove(new String[] { this.poolName });
        PrometheusHistogramMetricsTracker.ELAPSED_ACQUIRED_HISTOGRAM.remove(new String[] { this.poolName });
        PrometheusHistogramMetricsTracker.ELAPSED_BORROWED_HISTOGRAM.remove(new String[] { this.poolName });
        PrometheusHistogramMetricsTracker.ELAPSED_CREATION_HISTOGRAM.remove(new String[] { this.poolName });
    }
    
    static {
        CONNECTION_TIMEOUT_COUNTER = ((Counter.Builder)((Counter.Builder)((Counter.Builder)Counter.build().name("hikaricp_connection_timeout_total")).labelNames(new String[] { "pool" })).help("Connection timeout total count")).create();
        ELAPSED_ACQUIRED_HISTOGRAM = registerHistogram("hikaricp_connection_acquired_nanos", "Connection acquired time (ns)", 1000.0);
        ELAPSED_BORROWED_HISTOGRAM = registerHistogram("hikaricp_connection_usage_millis", "Connection usage (ms)", 1.0);
        ELAPSED_CREATION_HISTOGRAM = registerHistogram("hikaricp_connection_creation_millis", "Connection creation (ms)", 1.0);
        registrationStatuses = (Map)new ConcurrentHashMap();
    }
}
