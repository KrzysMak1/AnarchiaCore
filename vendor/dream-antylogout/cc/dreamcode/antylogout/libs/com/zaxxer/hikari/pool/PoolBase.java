package cc.dreamcode.antylogout.libs.com.zaxxer.hikari.pool;

import cc.dreamcode.antylogout.libs.com.zaxxer.hikari.metrics.IMetricsTracker;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.Executors;
import java.sql.SQLTransientConnectionException;
import cc.dreamcode.antylogout.libs.com.zaxxer.hikari.util.ClockSource;
import java.util.Properties;
import javax.naming.NamingException;
import javax.naming.InitialContext;
import cc.dreamcode.antylogout.libs.com.zaxxer.hikari.util.DriverDataSource;
import cc.dreamcode.antylogout.libs.com.zaxxer.hikari.util.PropertyElf;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;
import java.sql.Connection;
import cc.dreamcode.antylogout.libs.com.zaxxer.hikari.util.UtilityElf;
import cc.dreamcode.antylogout.libs.org.slf4j.LoggerFactory;
import javax.sql.DataSource;
import java.util.concurrent.Executor;
import cc.dreamcode.antylogout.libs.com.zaxxer.hikari.SQLExceptionOverride;
import java.util.concurrent.atomic.AtomicReference;
import cc.dreamcode.antylogout.libs.com.zaxxer.hikari.HikariConfig;
import cc.dreamcode.antylogout.libs.org.slf4j.Logger;

abstract class PoolBase
{
    private final Logger logger;
    public final HikariConfig config;
    IMetricsTrackerDelegate metricsTracker;
    protected final String poolName;
    volatile String catalog;
    final AtomicReference<Exception> lastConnectionFailure;
    long connectionTimeout;
    long validationTimeout;
    SQLExceptionOverride exceptionOverride;
    private static final String[] RESET_STATES;
    private static final int UNINITIALIZED = -1;
    private static final int TRUE = 1;
    private static final int FALSE = 0;
    private static final int MINIMUM_LOGIN_TIMEOUT;
    private int networkTimeout;
    private int isNetworkTimeoutSupported;
    private int isQueryTimeoutSupported;
    private int defaultTransactionIsolation;
    private int transactionIsolation;
    private Executor netTimeoutExecutor;
    private DataSource dataSource;
    private final String schema;
    private final boolean isReadOnly;
    private final boolean isAutoCommit;
    private final boolean isUseJdbc4Validation;
    private final boolean isIsolateInternalQueries;
    private volatile boolean isValidChecked;
    
    PoolBase(final HikariConfig config) {
        this.logger = LoggerFactory.getLogger(PoolBase.class);
        this.config = config;
        this.networkTimeout = -1;
        this.catalog = config.getCatalog();
        this.schema = config.getSchema();
        this.isReadOnly = config.isReadOnly();
        this.isAutoCommit = config.isAutoCommit();
        this.exceptionOverride = UtilityElf.createInstance(config.getExceptionOverrideClassName(), SQLExceptionOverride.class, new Object[0]);
        this.transactionIsolation = UtilityElf.getTransactionIsolation(config.getTransactionIsolation());
        this.isQueryTimeoutSupported = -1;
        this.isNetworkTimeoutSupported = -1;
        this.isUseJdbc4Validation = (config.getConnectionTestQuery() == null);
        this.isIsolateInternalQueries = config.isIsolateInternalQueries();
        this.poolName = config.getPoolName();
        this.connectionTimeout = config.getConnectionTimeout();
        this.validationTimeout = config.getValidationTimeout();
        this.lastConnectionFailure = (AtomicReference<Exception>)new AtomicReference();
        this.initializeDataSource();
    }
    
    @Override
    public String toString() {
        return this.poolName;
    }
    
    abstract void recycle(final PoolEntry p0);
    
    void quietlyCloseConnection(final Connection connection, final String closureReason) {
        if (connection != null) {
            try {
                this.logger.debug("{} - Closing connection {}: {}", this.poolName, connection, closureReason);
                try (connection;
                     connection) {
                    if (!connection.isClosed()) {
                        this.setNetworkTimeout(connection, TimeUnit.SECONDS.toMillis(15L));
                    }
                }
                catch (final SQLException ex) {}
            }
            catch (final Exception e) {
                this.logger.debug("{} - Closing connection {} failed", this.poolName, connection, e);
            }
        }
    }
    
    boolean isConnectionDead(final Connection connection) {
        try {
            this.setNetworkTimeout(connection, this.validationTimeout);
            try {
                final int validationSeconds = (int)Math.max(1000L, this.validationTimeout) / 1000;
                if (this.isUseJdbc4Validation) {
                    return !connection.isValid(validationSeconds);
                }
                try (final Statement statement = connection.createStatement()) {
                    if (this.isNetworkTimeoutSupported != 1) {
                        this.setQueryTimeout(statement, validationSeconds);
                    }
                    statement.execute(this.config.getConnectionTestQuery());
                }
            }
            finally {
                this.setNetworkTimeout(connection, this.networkTimeout);
                if (this.isIsolateInternalQueries && !this.isAutoCommit) {
                    connection.rollback();
                }
            }
            return false;
        }
        catch (final Exception e) {
            this.lastConnectionFailure.set((Object)e);
            this.logger.warn("{} - Failed to validate connection {} ({}). Possibly consider using a shorter maxLifetime value.", this.poolName, connection, e.getMessage());
            return true;
        }
    }
    
    Exception getLastConnectionFailure() {
        return (Exception)this.lastConnectionFailure.get();
    }
    
    public DataSource getUnwrappedDataSource() {
        return this.dataSource;
    }
    
    PoolEntry newPoolEntry() throws Exception {
        return new PoolEntry(this.newConnection(), this, this.isReadOnly, this.isAutoCommit);
    }
    
    void resetConnectionState(final Connection connection, final ProxyConnection proxyConnection, final int dirtyBits) throws SQLException {
        int resetBits = 0;
        if ((dirtyBits & 0x1) != 0x0 && proxyConnection.getReadOnlyState() != this.isReadOnly) {
            connection.setReadOnly(this.isReadOnly);
            resetBits |= 0x1;
        }
        if ((dirtyBits & 0x2) != 0x0 && proxyConnection.getAutoCommitState() != this.isAutoCommit) {
            connection.setAutoCommit(this.isAutoCommit);
            resetBits |= 0x2;
        }
        if ((dirtyBits & 0x4) != 0x0 && proxyConnection.getTransactionIsolationState() != this.transactionIsolation) {
            connection.setTransactionIsolation(this.transactionIsolation);
            resetBits |= 0x4;
        }
        if ((dirtyBits & 0x8) != 0x0 && this.catalog != null && !this.catalog.equals((Object)proxyConnection.getCatalogState())) {
            connection.setCatalog(this.catalog);
            resetBits |= 0x8;
        }
        if ((dirtyBits & 0x10) != 0x0 && proxyConnection.getNetworkTimeoutState() != this.networkTimeout) {
            this.setNetworkTimeout(connection, this.networkTimeout);
            resetBits |= 0x10;
        }
        if ((dirtyBits & 0x20) != 0x0 && this.schema != null && !this.schema.equals((Object)proxyConnection.getSchemaState())) {
            connection.setSchema(this.schema);
            resetBits |= 0x20;
        }
        if (resetBits != 0 && this.logger.isDebugEnabled()) {
            this.logger.debug("{} - Reset ({}) on connection {}", this.poolName, this.stringFromResetBits(resetBits), connection);
        }
    }
    
    void shutdownNetworkTimeoutExecutor() {
        if (this.netTimeoutExecutor instanceof ThreadPoolExecutor) {
            ((ThreadPoolExecutor)this.netTimeoutExecutor).shutdownNow();
        }
    }
    
    long getLoginTimeout() {
        try {
            return (this.dataSource != null) ? this.dataSource.getLoginTimeout() : TimeUnit.SECONDS.toSeconds(5L);
        }
        catch (final SQLException e) {
            return TimeUnit.SECONDS.toSeconds(5L);
        }
    }
    
    void handleMBeans(final HikariPool hikariPool, final boolean register) {
        if (!this.config.isRegisterMbeans()) {
            return;
        }
        try {
            final MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
            ObjectName beanConfigName;
            ObjectName beanPoolName;
            if ("true".equals((Object)System.getProperty("hikaricp.jmx.register2.0"))) {
                beanConfigName = new ObjectName("cc.dreamcode.antylogout.libs.com.zaxxer.hikari:type=PoolConfig,name=" + this.poolName);
                beanPoolName = new ObjectName("cc.dreamcode.antylogout.libs.com.zaxxer.hikari:type=Pool,name=" + this.poolName);
            }
            else {
                beanConfigName = new ObjectName("cc.dreamcode.antylogout.libs.com.zaxxer.hikari:type=PoolConfig (" + this.poolName);
                beanPoolName = new ObjectName("cc.dreamcode.antylogout.libs.com.zaxxer.hikari:type=Pool (" + this.poolName);
            }
            if (register) {
                if (!mBeanServer.isRegistered(beanConfigName)) {
                    mBeanServer.registerMBean((Object)this.config, beanConfigName);
                    mBeanServer.registerMBean((Object)hikariPool, beanPoolName);
                }
                else {
                    this.logger.error("{} - JMX name ({}) is already registered.", this.poolName, this.poolName);
                }
            }
            else if (mBeanServer.isRegistered(beanConfigName)) {
                mBeanServer.unregisterMBean(beanConfigName);
                mBeanServer.unregisterMBean(beanPoolName);
            }
        }
        catch (final Exception e) {
            this.logger.warn("{} - Failed to {} management beans.", this.poolName, register ? "register" : "unregister", e);
        }
    }
    
    private void initializeDataSource() {
        final String jdbcUrl = this.config.getJdbcUrl();
        final String username = this.config.getUsername();
        final String password = this.config.getPassword();
        final String dsClassName = this.config.getDataSourceClassName();
        final String driverClassName = this.config.getDriverClassName();
        final String dataSourceJNDI = this.config.getDataSourceJNDI();
        final Properties dataSourceProperties = this.config.getDataSourceProperties();
        DataSource ds = this.config.getDataSource();
        if (dsClassName != null && ds == null) {
            ds = UtilityElf.createInstance(dsClassName, DataSource.class, new Object[0]);
            PropertyElf.setTargetFromProperties(ds, dataSourceProperties);
        }
        else if (jdbcUrl != null && ds == null) {
            ds = (DataSource)new DriverDataSource(jdbcUrl, driverClassName, dataSourceProperties, username, password);
        }
        else if (dataSourceJNDI != null && ds == null) {
            try {
                final InitialContext ic = new InitialContext();
                ds = (DataSource)ic.lookup(dataSourceJNDI);
            }
            catch (final NamingException e) {
                throw new HikariPool.PoolInitializationException((Throwable)e);
            }
        }
        if (ds != null) {
            this.setLoginTimeout(ds);
            this.createNetworkTimeoutExecutor(ds, dsClassName, jdbcUrl);
        }
        this.dataSource = ds;
    }
    
    private Connection newConnection() throws Exception {
        final long start = ClockSource.currentTime();
        Connection connection = null;
        try {
            final String username = this.config.getUsername();
            final String password = this.config.getPassword();
            connection = ((username == null) ? this.dataSource.getConnection() : this.dataSource.getConnection(username, password));
            if (connection == null) {
                throw new SQLTransientConnectionException("DataSource returned null unexpectedly");
            }
            this.setupConnection(connection);
            this.lastConnectionFailure.set((Object)null);
            return connection;
        }
        catch (final Exception e) {
            if (connection != null) {
                this.quietlyCloseConnection(connection, "(Failed to create/setup connection)");
            }
            else if (this.getLastConnectionFailure() == null) {
                this.logger.debug("{} - Failed to create/setup connection: {}", this.poolName, e.getMessage());
            }
            this.lastConnectionFailure.set((Object)e);
            throw e;
        }
        finally {
            if (this.metricsTracker != null) {
                this.metricsTracker.recordConnectionCreated(ClockSource.elapsedMillis(start));
            }
        }
    }
    
    private void setupConnection(final Connection connection) throws ConnectionSetupException {
        try {
            if (this.networkTimeout == -1) {
                this.networkTimeout = this.getAndSetNetworkTimeout(connection, this.validationTimeout);
            }
            else {
                this.setNetworkTimeout(connection, this.validationTimeout);
            }
            if (connection.isReadOnly() != this.isReadOnly) {
                connection.setReadOnly(this.isReadOnly);
            }
            if (connection.getAutoCommit() != this.isAutoCommit) {
                connection.setAutoCommit(this.isAutoCommit);
            }
            this.checkDriverSupport(connection);
            if (this.transactionIsolation != this.defaultTransactionIsolation) {
                connection.setTransactionIsolation(this.transactionIsolation);
            }
            if (this.catalog != null) {
                connection.setCatalog(this.catalog);
            }
            if (this.schema != null) {
                connection.setSchema(this.schema);
            }
            this.executeSql(connection, this.config.getConnectionInitSql(), true);
            this.setNetworkTimeout(connection, this.networkTimeout);
        }
        catch (final SQLException e) {
            throw new ConnectionSetupException((Throwable)e);
        }
    }
    
    private void checkDriverSupport(final Connection connection) throws SQLException {
        if (!this.isValidChecked) {
            this.checkValidationSupport(connection);
            this.checkDefaultIsolation(connection);
            this.isValidChecked = true;
        }
    }
    
    private void checkValidationSupport(final Connection connection) throws SQLException {
        try {
            if (this.isUseJdbc4Validation) {
                connection.isValid(1);
            }
            else {
                this.executeSql(connection, this.config.getConnectionTestQuery(), false);
            }
        }
        catch (final Exception | AbstractMethodError e) {
            this.logger.error("{} - Failed to execute{} connection test query ({}).", this.poolName, this.isUseJdbc4Validation ? " isValid() for connection, configure" : "", e.getMessage());
            throw e;
        }
    }
    
    private void checkDefaultIsolation(final Connection connection) throws SQLException {
        try {
            this.defaultTransactionIsolation = connection.getTransactionIsolation();
            if (this.transactionIsolation == -1) {
                this.transactionIsolation = this.defaultTransactionIsolation;
            }
        }
        catch (final SQLException e) {
            this.logger.warn("{} - Default transaction isolation level detection failed ({}).", this.poolName, e.getMessage());
            if (e.getSQLState() != null && !e.getSQLState().startsWith("08")) {
                throw e;
            }
        }
    }
    
    private void setQueryTimeout(final Statement statement, final int timeoutSec) {
        if (this.isQueryTimeoutSupported != 0) {
            try {
                statement.setQueryTimeout(timeoutSec);
                this.isQueryTimeoutSupported = 1;
            }
            catch (final Exception e) {
                if (this.isQueryTimeoutSupported == -1) {
                    this.isQueryTimeoutSupported = 0;
                    this.logger.info("{} - Failed to set query timeout for statement. ({})", this.poolName, e.getMessage());
                }
            }
        }
    }
    
    private int getAndSetNetworkTimeout(final Connection connection, final long timeoutMs) {
        if (this.isNetworkTimeoutSupported != 0) {
            try {
                final int originalTimeout = connection.getNetworkTimeout();
                connection.setNetworkTimeout(this.netTimeoutExecutor, (int)timeoutMs);
                this.isNetworkTimeoutSupported = 1;
                return originalTimeout;
            }
            catch (final Exception | AbstractMethodError e) {
                if (this.isNetworkTimeoutSupported == -1) {
                    this.isNetworkTimeoutSupported = 0;
                    this.logger.info("{} - Driver does not support get/set network timeout for connections. ({})", this.poolName, e.getMessage());
                    if (this.validationTimeout < TimeUnit.SECONDS.toMillis(1L)) {
                        this.logger.warn("{} - A validationTimeout of less than 1 second cannot be honored on drivers without setNetworkTimeout() support.", this.poolName);
                    }
                    else if (this.validationTimeout % TimeUnit.SECONDS.toMillis(1L) != 0L) {
                        this.logger.warn("{} - A validationTimeout with fractional second granularity cannot be honored on drivers without setNetworkTimeout() support.", this.poolName);
                    }
                }
            }
        }
        return 0;
    }
    
    private void setNetworkTimeout(final Connection connection, final long timeoutMs) throws SQLException {
        if (this.isNetworkTimeoutSupported == 1) {
            connection.setNetworkTimeout(this.netTimeoutExecutor, (int)timeoutMs);
        }
    }
    
    private void executeSql(final Connection connection, final String sql, final boolean isCommit) throws SQLException {
        if (sql != null) {
            try (final Statement statement = connection.createStatement()) {
                statement.execute(sql);
            }
            if (this.isIsolateInternalQueries && !this.isAutoCommit) {
                if (isCommit) {
                    connection.commit();
                }
                else {
                    connection.rollback();
                }
            }
        }
    }
    
    private void createNetworkTimeoutExecutor(final DataSource dataSource, final String dsClassName, final String jdbcUrl) {
        if ((dsClassName != null && dsClassName.contains((CharSequence)"Mysql")) || (jdbcUrl != null && jdbcUrl.contains((CharSequence)"mysql")) || (dataSource != null && dataSource.getClass().getName().contains((CharSequence)"Mysql"))) {
            this.netTimeoutExecutor = (Executor)new SynchronousExecutor();
        }
        else {
            ThreadFactory threadFactory = this.config.getThreadFactory();
            threadFactory = (ThreadFactory)((threadFactory != null) ? threadFactory : new UtilityElf.DefaultThreadFactory(this.poolName + " network timeout executor"));
            final ThreadPoolExecutor executor = (ThreadPoolExecutor)Executors.newCachedThreadPool(threadFactory);
            executor.setKeepAliveTime(15L, TimeUnit.SECONDS);
            executor.allowCoreThreadTimeOut(true);
            this.netTimeoutExecutor = (Executor)executor;
        }
    }
    
    private void setLoginTimeout(final DataSource dataSource) {
        if (this.connectionTimeout != 2147483647L) {
            try {
                dataSource.setLoginTimeout(Math.max(PoolBase.MINIMUM_LOGIN_TIMEOUT, (int)TimeUnit.MILLISECONDS.toSeconds(500L + this.connectionTimeout)));
            }
            catch (final Exception e) {
                this.logger.info("{} - Failed to set login timeout for data source. ({})", this.poolName, e.getMessage());
            }
        }
    }
    
    private String stringFromResetBits(final int bits) {
        final StringBuilder sb = new StringBuilder();
        for (int ndx = 0; ndx < PoolBase.RESET_STATES.length; ++ndx) {
            if ((bits & 1 << ndx) != 0x0) {
                sb.append(PoolBase.RESET_STATES[ndx]).append(", ");
            }
        }
        sb.setLength(sb.length() - 2);
        return sb.toString();
    }
    
    static {
        RESET_STATES = new String[] { "readOnly", "autoCommit", "isolation", "catalog", "netTimeout", "schema" };
        MINIMUM_LOGIN_TIMEOUT = Integer.getInteger("cc.dreamcode.antylogout.libs.com.zaxxer.hikari.minimumLoginTimeoutSecs", 1);
    }
    
    static class ConnectionSetupException extends Exception
    {
        private static final long serialVersionUID = 929872118275916521L;
        
        ConnectionSetupException(final Throwable t) {
            super(t);
        }
    }
    
    private static class SynchronousExecutor implements Executor
    {
        public void execute(final Runnable command) {
            try {
                command.run();
            }
            catch (final Exception t) {
                LoggerFactory.getLogger(PoolBase.class).debug("Failed to execute: {}", command, t);
            }
        }
    }
    
    interface IMetricsTrackerDelegate extends AutoCloseable
    {
        default void recordConnectionUsage(final PoolEntry poolEntry) {
        }
        
        default void recordConnectionCreated(final long connectionCreatedMillis) {
        }
        
        default void recordBorrowTimeoutStats(final long startTime) {
        }
        
        default void recordBorrowStats(final PoolEntry poolEntry, final long startTime) {
        }
        
        default void recordConnectionTimeout() {
        }
        
        default void close() {
        }
    }
    
    static class MetricsTrackerDelegate implements IMetricsTrackerDelegate
    {
        final IMetricsTracker tracker;
        
        MetricsTrackerDelegate(final IMetricsTracker tracker) {
            this.tracker = tracker;
        }
        
        @Override
        public void recordConnectionUsage(final PoolEntry poolEntry) {
            this.tracker.recordConnectionUsageMillis(poolEntry.getMillisSinceBorrowed());
        }
        
        @Override
        public void recordConnectionCreated(final long connectionCreatedMillis) {
            this.tracker.recordConnectionCreatedMillis(connectionCreatedMillis);
        }
        
        @Override
        public void recordBorrowTimeoutStats(final long startTime) {
            this.tracker.recordConnectionAcquiredNanos(ClockSource.elapsedNanos(startTime));
        }
        
        @Override
        public void recordBorrowStats(final PoolEntry poolEntry, final long startTime) {
            final long now = ClockSource.currentTime();
            poolEntry.lastBorrowed = now;
            this.tracker.recordConnectionAcquiredNanos(ClockSource.elapsedNanos(startTime, now));
        }
        
        @Override
        public void recordConnectionTimeout() {
            this.tracker.recordConnectionTimeout();
        }
        
        @Override
        public void close() {
            this.tracker.close();
        }
    }
    
    static final class NopMetricsTrackerDelegate implements IMetricsTrackerDelegate
    {
    }
}
