package cc.dreamcode.antylogout.libs.com.zaxxer.hikari.metrics.micrometer;

import cc.dreamcode.antylogout.libs.com.zaxxer.hikari.metrics.IMetricsTracker;
import cc.dreamcode.antylogout.libs.com.zaxxer.hikari.metrics.PoolStats;
import io.micrometer.core.instrument.MeterRegistry;
import cc.dreamcode.antylogout.libs.com.zaxxer.hikari.metrics.MetricsTrackerFactory;

public class MicrometerMetricsTrackerFactory implements MetricsTrackerFactory
{
    private final MeterRegistry registry;
    
    public MicrometerMetricsTrackerFactory(final MeterRegistry registry) {
        this.registry = registry;
    }
    
    @Override
    public IMetricsTracker create(final String poolName, final PoolStats poolStats) {
        return new MicrometerMetricsTracker(poolName, poolStats, this.registry);
    }
}
