package cc.dreamcode.antylogout.libs.com.zaxxer.hikari.metrics.prometheus;

import java.util.concurrent.ConcurrentHashMap;
import io.prometheus.client.Collector;
import cc.dreamcode.antylogout.libs.com.zaxxer.hikari.metrics.IMetricsTracker;
import cc.dreamcode.antylogout.libs.com.zaxxer.hikari.metrics.PoolStats;
import io.prometheus.client.CollectorRegistry;
import java.util.Map;
import cc.dreamcode.antylogout.libs.com.zaxxer.hikari.metrics.MetricsTrackerFactory;

public class PrometheusHistogramMetricsTrackerFactory implements MetricsTrackerFactory
{
    private static final Map<CollectorRegistry, PrometheusMetricsTrackerFactory.RegistrationStatus> registrationStatuses;
    private final HikariCPCollector collector;
    private final CollectorRegistry collectorRegistry;
    
    public PrometheusHistogramMetricsTrackerFactory() {
        this(CollectorRegistry.defaultRegistry);
    }
    
    public PrometheusHistogramMetricsTrackerFactory(final CollectorRegistry collectorRegistry) {
        this.collector = new HikariCPCollector();
        this.collectorRegistry = collectorRegistry;
    }
    
    @Override
    public IMetricsTracker create(final String poolName, final PoolStats poolStats) {
        this.registerCollector(this.collector, this.collectorRegistry);
        this.collector.add(poolName, poolStats);
        return new PrometheusHistogramMetricsTracker(poolName, this.collectorRegistry, this.collector);
    }
    
    private void registerCollector(final Collector collector, final CollectorRegistry collectorRegistry) {
        if (PrometheusHistogramMetricsTrackerFactory.registrationStatuses.putIfAbsent((Object)collectorRegistry, (Object)PrometheusMetricsTrackerFactory.RegistrationStatus.REGISTERED) == null) {
            collector.register(collectorRegistry);
        }
    }
    
    static {
        registrationStatuses = (Map)new ConcurrentHashMap();
    }
}
