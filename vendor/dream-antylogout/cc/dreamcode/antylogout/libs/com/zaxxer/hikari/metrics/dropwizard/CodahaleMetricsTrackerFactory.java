package cc.dreamcode.antylogout.libs.com.zaxxer.hikari.metrics.dropwizard;

import cc.dreamcode.antylogout.libs.com.zaxxer.hikari.metrics.IMetricsTracker;
import cc.dreamcode.antylogout.libs.com.zaxxer.hikari.metrics.PoolStats;
import com.codahale.metrics.MetricRegistry;
import cc.dreamcode.antylogout.libs.com.zaxxer.hikari.metrics.MetricsTrackerFactory;

public final class CodahaleMetricsTrackerFactory implements MetricsTrackerFactory
{
    private final MetricRegistry registry;
    
    public CodahaleMetricsTrackerFactory(final MetricRegistry registry) {
        this.registry = registry;
    }
    
    public MetricRegistry getRegistry() {
        return this.registry;
    }
    
    @Override
    public IMetricsTracker create(final String poolName, final PoolStats poolStats) {
        return new CodaHaleMetricsTracker(poolName, poolStats, this.registry);
    }
}
