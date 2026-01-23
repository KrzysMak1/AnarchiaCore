package cc.dreamcode.antylogout.libs.com.zaxxer.hikari.metrics.dropwizard;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;
import com.codahale.metrics.Metric;
import java.util.SortedMap;
import java.util.Properties;
import java.util.Map;
import com.codahale.metrics.Timer;
import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheckRegistry;
import cc.dreamcode.antylogout.libs.com.zaxxer.hikari.HikariConfig;
import cc.dreamcode.antylogout.libs.com.zaxxer.hikari.pool.HikariPool;

public final class CodahaleHealthChecker
{
    public static void registerHealthChecks(final HikariPool pool, final HikariConfig hikariConfig, final HealthCheckRegistry registry) {
        final Properties healthCheckProperties = hikariConfig.getHealthCheckProperties();
        final long checkTimeoutMs = Long.parseLong(healthCheckProperties.getProperty("connectivityCheckTimeoutMs", String.valueOf(hikariConfig.getConnectionTimeout())));
        registry.register(MetricRegistry.name(hikariConfig.getPoolName(), new String[] { "pool", "ConnectivityCheck" }), (HealthCheck)new ConnectivityHealthCheck(pool, checkTimeoutMs));
        final long expected99thPercentile = Long.parseLong(healthCheckProperties.getProperty("expected99thPercentileMs", "0"));
        final Object metricRegistryObj = hikariConfig.getMetricRegistry();
        if (expected99thPercentile > 0L && metricRegistryObj instanceof MetricRegistry) {
            final MetricRegistry metricRegistry = (MetricRegistry)metricRegistryObj;
            final SortedMap<String, Timer> timers = (SortedMap<String, Timer>)metricRegistry.getTimers((name, metric) -> name.equals((Object)MetricRegistry.name(hikariConfig.getPoolName(), new String[] { "pool", "Wait" })));
            if (!timers.isEmpty()) {
                final Timer timer = (Timer)((Map.Entry)timers.entrySet().iterator().next()).getValue();
                registry.register(MetricRegistry.name(hikariConfig.getPoolName(), new String[] { "pool", "Connection99Percent" }), (HealthCheck)new Connection99Percent(timer, expected99thPercentile));
            }
        }
    }
    
    private CodahaleHealthChecker() {
    }
    
    private static class ConnectivityHealthCheck extends HealthCheck
    {
        private final HikariPool pool;
        private final long checkTimeoutMs;
        
        ConnectivityHealthCheck(final HikariPool pool, final long checkTimeoutMs) {
            this.pool = pool;
            this.checkTimeoutMs = ((checkTimeoutMs > 0L && checkTimeoutMs != 2147483647L) ? checkTimeoutMs : TimeUnit.SECONDS.toMillis(10L));
        }
        
        protected HealthCheck.Result check() throws Exception {
            try (final Connection connection = this.pool.getConnection(this.checkTimeoutMs)) {
                return HealthCheck.Result.healthy();
            }
            catch (final SQLException e) {
                return HealthCheck.Result.unhealthy((Throwable)e);
            }
        }
    }
    
    private static class Connection99Percent extends HealthCheck
    {
        private final Timer waitTimer;
        private final long expected99thPercentile;
        
        Connection99Percent(final Timer waitTimer, final long expected99thPercentile) {
            this.waitTimer = waitTimer;
            this.expected99thPercentile = expected99thPercentile;
        }
        
        protected HealthCheck.Result check() throws Exception {
            final long the99thPercentile = TimeUnit.NANOSECONDS.toMillis(Math.round(this.waitTimer.getSnapshot().get99thPercentile()));
            return (the99thPercentile <= this.expected99thPercentile) ? HealthCheck.Result.healthy() : HealthCheck.Result.unhealthy("99th percentile connection wait time of %dms exceeds the threshold %dms", new Object[] { the99thPercentile, this.expected99thPercentile });
        }
    }
}
