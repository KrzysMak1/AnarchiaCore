package cc.dreamcode.antylogout.libs.com.zaxxer.hikari;

import cc.dreamcode.antylogout.libs.org.slf4j.LoggerFactory;
import cc.dreamcode.antylogout.libs.com.zaxxer.hikari.metrics.MetricsTrackerFactory;
import java.io.PrintWriter;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLException;
import java.sql.Connection;
import cc.dreamcode.antylogout.libs.com.zaxxer.hikari.pool.HikariPool;
import java.util.concurrent.atomic.AtomicBoolean;
import cc.dreamcode.antylogout.libs.org.slf4j.Logger;
import java.io.Closeable;
import javax.sql.DataSource;

public class HikariDataSource extends HikariConfig implements DataSource, Closeable
{
    private static final Logger LOGGER;
    private final AtomicBoolean isShutdown;
    private final HikariPool fastPathPool;
    private volatile HikariPool pool;
    
    public HikariDataSource() {
        this.isShutdown = new AtomicBoolean();
        this.fastPathPool = null;
    }
    
    public HikariDataSource(final HikariConfig configuration) {
        this.isShutdown = new AtomicBoolean();
        configuration.validate();
        configuration.copyStateTo(this);
        HikariDataSource.LOGGER.info("{} - Starting...", configuration.getPoolName());
        final HikariPool hikariPool = new HikariPool(this);
        this.fastPathPool = hikariPool;
        this.pool = hikariPool;
        HikariDataSource.LOGGER.info("{} - Start completed.", configuration.getPoolName());
        this.seal();
    }
    
    public Connection getConnection() throws SQLException {
        if (this.isClosed()) {
            throw new SQLException("HikariDataSource " + this + " has been closed.");
        }
        if (this.fastPathPool != null) {
            return this.fastPathPool.getConnection();
        }
        HikariPool result = this.pool;
        if (result == null) {
            synchronized (this) {
                result = this.pool;
                if (result == null) {
                    this.validate();
                    HikariDataSource.LOGGER.info("{} - Starting...", this.getPoolName());
                    try {
                        result = (this.pool = new HikariPool(this));
                        this.seal();
                    }
                    catch (final HikariPool.PoolInitializationException pie) {
                        if (pie.getCause() instanceof SQLException) {
                            throw (SQLException)pie.getCause();
                        }
                        throw pie;
                    }
                    HikariDataSource.LOGGER.info("{} - Start completed.", this.getPoolName());
                }
            }
        }
        return result.getConnection();
    }
    
    public Connection getConnection(final String username, final String password) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }
    
    public PrintWriter getLogWriter() throws SQLException {
        final HikariPool p = this.pool;
        return (p != null) ? p.getUnwrappedDataSource().getLogWriter() : null;
    }
    
    public void setLogWriter(final PrintWriter out) throws SQLException {
        final HikariPool p = this.pool;
        if (p != null) {
            p.getUnwrappedDataSource().setLogWriter(out);
        }
    }
    
    public void setLoginTimeout(final int seconds) throws SQLException {
        final HikariPool p = this.pool;
        if (p != null) {
            p.getUnwrappedDataSource().setLoginTimeout(seconds);
        }
    }
    
    public int getLoginTimeout() throws SQLException {
        final HikariPool p = this.pool;
        return (p != null) ? p.getUnwrappedDataSource().getLoginTimeout() : 0;
    }
    
    public java.util.logging.Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new SQLFeatureNotSupportedException();
    }
    
    public <T> T unwrap(final Class<T> iface) throws SQLException {
        if (iface.isInstance(this)) {
            return (T)this;
        }
        final HikariPool p = this.pool;
        if (p != null) {
            final DataSource unwrappedDataSource = p.getUnwrappedDataSource();
            if (iface.isInstance(unwrappedDataSource)) {
                return (T)unwrappedDataSource;
            }
            if (unwrappedDataSource != null) {
                return (T)unwrappedDataSource.unwrap((Class)iface);
            }
        }
        throw new SQLException("Wrapped DataSource is not an instance of " + iface);
    }
    
    public boolean isWrapperFor(final Class<?> iface) throws SQLException {
        if (iface.isInstance(this)) {
            return true;
        }
        final HikariPool p = this.pool;
        if (p != null) {
            final DataSource unwrappedDataSource = p.getUnwrappedDataSource();
            if (iface.isInstance(unwrappedDataSource)) {
                return true;
            }
            if (unwrappedDataSource != null) {
                return unwrappedDataSource.isWrapperFor((Class)iface);
            }
        }
        return false;
    }
    
    @Override
    public void setMetricRegistry(final Object metricRegistry) {
        final boolean isAlreadySet = this.getMetricRegistry() != null;
        super.setMetricRegistry(metricRegistry);
        final HikariPool p = this.pool;
        if (p != null) {
            if (isAlreadySet) {
                throw new IllegalStateException("MetricRegistry can only be set one time");
            }
            p.setMetricRegistry(super.getMetricRegistry());
        }
    }
    
    @Override
    public void setMetricsTrackerFactory(final MetricsTrackerFactory metricsTrackerFactory) {
        final boolean isAlreadySet = this.getMetricsTrackerFactory() != null;
        super.setMetricsTrackerFactory(metricsTrackerFactory);
        final HikariPool p = this.pool;
        if (p != null) {
            if (isAlreadySet) {
                throw new IllegalStateException("MetricsTrackerFactory can only be set one time");
            }
            p.setMetricsTrackerFactory(super.getMetricsTrackerFactory());
        }
    }
    
    @Override
    public void setHealthCheckRegistry(final Object healthCheckRegistry) {
        final boolean isAlreadySet = this.getHealthCheckRegistry() != null;
        super.setHealthCheckRegistry(healthCheckRegistry);
        final HikariPool p = this.pool;
        if (p != null) {
            if (isAlreadySet) {
                throw new IllegalStateException("HealthCheckRegistry can only be set one time");
            }
            p.setHealthCheckRegistry(super.getHealthCheckRegistry());
        }
    }
    
    public boolean isRunning() {
        return this.pool != null && this.pool.poolState == 0;
    }
    
    public HikariPoolMXBean getHikariPoolMXBean() {
        return this.pool;
    }
    
    public HikariConfigMXBean getHikariConfigMXBean() {
        return this;
    }
    
    public void evictConnection(final Connection connection) {
        final HikariPool p;
        if (!this.isClosed() && (p = this.pool) != null && connection.getClass().getName().startsWith("cc.dreamcode.antylogout.libs.com.zaxxer.hikari")) {
            p.evictConnection(connection);
        }
    }
    
    public void close() {
        if (this.isShutdown.getAndSet(true)) {
            return;
        }
        final HikariPool p = this.pool;
        if (p != null) {
            try {
                HikariDataSource.LOGGER.info("{} - Shutdown initiated...", this.getPoolName());
                p.shutdown();
                HikariDataSource.LOGGER.info("{} - Shutdown completed.", this.getPoolName());
            }
            catch (final InterruptedException e) {
                HikariDataSource.LOGGER.warn("{} - Interrupted during closing", this.getPoolName(), e);
                Thread.currentThread().interrupt();
            }
        }
    }
    
    public boolean isClosed() {
        return this.isShutdown.get();
    }
    
    public String toString() {
        return "HikariDataSource (" + this.pool;
    }
    
    static {
        LOGGER = LoggerFactory.getLogger(HikariDataSource.class);
    }
}
