package cc.dreamcode.antylogout.libs.com.zaxxer.hikari.metrics.prometheus;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import io.prometheus.client.CollectorRegistry;
import java.util.Map;
import io.prometheus.client.Summary;
import io.prometheus.client.Counter;
import cc.dreamcode.antylogout.libs.com.zaxxer.hikari.metrics.IMetricsTracker;

class PrometheusMetricsTracker implements IMetricsTracker
{
    private static final Counter CONNECTION_TIMEOUT_COUNTER;
    private static final Summary ELAPSED_ACQUIRED_SUMMARY;
    private static final Summary ELAPSED_USAGE_SUMMARY;
    private static final Summary ELAPSED_CREATION_SUMMARY;
    private static final Map<CollectorRegistry, PrometheusMetricsTrackerFactory.RegistrationStatus> registrationStatuses;
    private final String poolName;
    private final HikariCPCollector hikariCPCollector;
    private final Counter.Child connectionTimeoutCounterChild;
    private final Summary.Child elapsedAcquiredSummaryChild;
    private final Summary.Child elapsedUsageSummaryChild;
    private final Summary.Child elapsedCreationSummaryChild;
    
    PrometheusMetricsTracker(final String poolName, final CollectorRegistry collectorRegistry, final HikariCPCollector hikariCPCollector) {
        this.registerMetrics(collectorRegistry);
        this.poolName = poolName;
        this.hikariCPCollector = hikariCPCollector;
        this.connectionTimeoutCounterChild = (Counter.Child)PrometheusMetricsTracker.CONNECTION_TIMEOUT_COUNTER.labels(new String[] { poolName });
        this.elapsedAcquiredSummaryChild = (Summary.Child)PrometheusMetricsTracker.ELAPSED_ACQUIRED_SUMMARY.labels(new String[] { poolName });
        this.elapsedUsageSummaryChild = (Summary.Child)PrometheusMetricsTracker.ELAPSED_USAGE_SUMMARY.labels(new String[] { poolName });
        this.elapsedCreationSummaryChild = (Summary.Child)PrometheusMetricsTracker.ELAPSED_CREATION_SUMMARY.labels(new String[] { poolName });
    }
    
    private void registerMetrics(final CollectorRegistry collectorRegistry) {
        if (PrometheusMetricsTracker.registrationStatuses.putIfAbsent((Object)collectorRegistry, (Object)PrometheusMetricsTrackerFactory.RegistrationStatus.REGISTERED) == null) {
            PrometheusMetricsTracker.CONNECTION_TIMEOUT_COUNTER.register(collectorRegistry);
            PrometheusMetricsTracker.ELAPSED_ACQUIRED_SUMMARY.register(collectorRegistry);
            PrometheusMetricsTracker.ELAPSED_USAGE_SUMMARY.register(collectorRegistry);
            PrometheusMetricsTracker.ELAPSED_CREATION_SUMMARY.register(collectorRegistry);
        }
    }
    
    @Override
    public void recordConnectionAcquiredNanos(final long elapsedAcquiredNanos) {
        this.elapsedAcquiredSummaryChild.observe((double)elapsedAcquiredNanos);
    }
    
    @Override
    public void recordConnectionUsageMillis(final long elapsedBorrowedMillis) {
        this.elapsedUsageSummaryChild.observe((double)elapsedBorrowedMillis);
    }
    
    @Override
    public void recordConnectionCreatedMillis(final long connectionCreatedMillis) {
        this.elapsedCreationSummaryChild.observe((double)connectionCreatedMillis);
    }
    
    @Override
    public void recordConnectionTimeout() {
        this.connectionTimeoutCounterChild.inc();
    }
    
    private static Summary createSummary(final String name, final String help) {
        return ((Summary.Builder)((Summary.Builder)((Summary.Builder)Summary.build().name(name)).labelNames(new String[] { "pool" })).help(help)).quantile(0.5, 0.05).quantile(0.95, 0.01).quantile(0.99, 0.001).maxAgeSeconds(TimeUnit.MINUTES.toSeconds(5L)).ageBuckets(5).create();
    }
    
    @Override
    public void close() {
        this.hikariCPCollector.remove(this.poolName);
        PrometheusMetricsTracker.CONNECTION_TIMEOUT_COUNTER.remove(new String[] { this.poolName });
        PrometheusMetricsTracker.ELAPSED_ACQUIRED_SUMMARY.remove(new String[] { this.poolName });
        PrometheusMetricsTracker.ELAPSED_USAGE_SUMMARY.remove(new String[] { this.poolName });
        PrometheusMetricsTracker.ELAPSED_CREATION_SUMMARY.remove(new String[] { this.poolName });
    }
    
    static {
        CONNECTION_TIMEOUT_COUNTER = ((Counter.Builder)((Counter.Builder)((Counter.Builder)Counter.build().name("hikaricp_connection_timeout_total")).labelNames(new String[] { "pool" })).help("Connection timeout total count")).create();
        ELAPSED_ACQUIRED_SUMMARY = createSummary("hikaricp_connection_acquired_nanos", "Connection acquired time (ns)");
        ELAPSED_USAGE_SUMMARY = createSummary("hikaricp_connection_usage_millis", "Connection usage (ms)");
        ELAPSED_CREATION_SUMMARY = createSummary("hikaricp_connection_creation_millis", "Connection creation (ms)");
        registrationStatuses = (Map)new ConcurrentHashMap();
    }
}
