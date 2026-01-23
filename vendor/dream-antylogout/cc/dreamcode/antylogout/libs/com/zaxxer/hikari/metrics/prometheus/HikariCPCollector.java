package cc.dreamcode.antylogout.libs.com.zaxxer.hikari.metrics.prometheus;

import java.util.Collections;
import io.prometheus.client.GaugeMetricFamily;
import java.util.Arrays;
import java.util.function.Function;
import java.util.concurrent.ConcurrentHashMap;
import cc.dreamcode.antylogout.libs.com.zaxxer.hikari.metrics.PoolStats;
import java.util.Map;
import java.util.List;
import io.prometheus.client.Collector;

class HikariCPCollector extends Collector
{
    private static final List<String> LABEL_NAMES;
    private final Map<String, PoolStats> poolStatsMap;
    
    HikariCPCollector() {
        this.poolStatsMap = (Map<String, PoolStats>)new ConcurrentHashMap();
    }
    
    public List<Collector.MetricFamilySamples> collect() {
        return (List<Collector.MetricFamilySamples>)Arrays.asList((Object[])new Collector.MetricFamilySamples[] { (Collector.MetricFamilySamples)this.createGauge("hikaricp_active_connections", "Active connections", (Function<PoolStats, Integer>)PoolStats::getActiveConnections), (Collector.MetricFamilySamples)this.createGauge("hikaricp_idle_connections", "Idle connections", (Function<PoolStats, Integer>)PoolStats::getIdleConnections), (Collector.MetricFamilySamples)this.createGauge("hikaricp_pending_threads", "Pending threads", (Function<PoolStats, Integer>)PoolStats::getPendingThreads), (Collector.MetricFamilySamples)this.createGauge("hikaricp_connections", "The number of current connections", (Function<PoolStats, Integer>)PoolStats::getTotalConnections), (Collector.MetricFamilySamples)this.createGauge("hikaricp_max_connections", "Max connections", (Function<PoolStats, Integer>)PoolStats::getMaxConnections), (Collector.MetricFamilySamples)this.createGauge("hikaricp_min_connections", "Min connections", (Function<PoolStats, Integer>)PoolStats::getMinConnections) });
    }
    
    void add(final String name, final PoolStats poolStats) {
        this.poolStatsMap.put((Object)name, (Object)poolStats);
    }
    
    void remove(final String name) {
        this.poolStatsMap.remove((Object)name);
    }
    
    private GaugeMetricFamily createGauge(final String metric, final String help, final Function<PoolStats, Integer> metricValueFunction) {
        final GaugeMetricFamily metricFamily = new GaugeMetricFamily(metric, help, (List)HikariCPCollector.LABEL_NAMES);
        this.poolStatsMap.forEach((k, v) -> metricFamily.addMetric(Collections.singletonList((Object)k), (double)(int)metricValueFunction.apply((Object)v)));
        return metricFamily;
    }
    
    static {
        LABEL_NAMES = Collections.singletonList((Object)"pool");
    }
}
