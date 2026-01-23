package cc.dreamcode.antylogout.libs.com.zaxxer.hikari.pool;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.HashSet;
import cc.dreamcode.antylogout.libs.org.slf4j.LoggerFactory;
import java.util.concurrent.Executor;
import java.sql.Savepoint;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.CallableStatement;
import cc.dreamcode.antylogout.libs.com.zaxxer.hikari.SQLExceptionOverride;
import java.sql.SQLTimeoutException;
import java.sql.SQLException;
import java.sql.Statement;
import cc.dreamcode.antylogout.libs.com.zaxxer.hikari.util.FastList;
import java.util.Set;
import cc.dreamcode.antylogout.libs.org.slf4j.Logger;
import java.sql.Connection;

public abstract class ProxyConnection implements Connection
{
    static final int DIRTY_BIT_READONLY = 1;
    static final int DIRTY_BIT_AUTOCOMMIT = 2;
    static final int DIRTY_BIT_ISOLATION = 4;
    static final int DIRTY_BIT_CATALOG = 8;
    static final int DIRTY_BIT_NETTIMEOUT = 16;
    static final int DIRTY_BIT_SCHEMA = 32;
    private static final Logger LOGGER;
    private static final Set<String> ERROR_STATES;
    private static final Set<Integer> ERROR_CODES;
    protected Connection delegate;
    private final PoolEntry poolEntry;
    private final ProxyLeakTask leakTask;
    private final FastList<Statement> openStatements;
    private int dirtyBits;
    private boolean isCommitStateDirty;
    private boolean isReadOnly;
    private boolean isAutoCommit;
    private int networkTimeout;
    private int transactionIsolation;
    private String dbcatalog;
    private String dbschema;
    
    protected ProxyConnection(final PoolEntry poolEntry, final Connection connection, final FastList<Statement> openStatements, final ProxyLeakTask leakTask, final boolean isReadOnly, final boolean isAutoCommit) {
        this.poolEntry = poolEntry;
        this.delegate = connection;
        this.openStatements = openStatements;
        this.leakTask = leakTask;
        this.isReadOnly = isReadOnly;
        this.isAutoCommit = isAutoCommit;
    }
    
    @Override
    public final String toString() {
        return this.getClass().getSimpleName() + "@" + System.identityHashCode((Object)this) + " wrapping " + this.delegate;
    }
    
    final boolean getAutoCommitState() {
        return this.isAutoCommit;
    }
    
    final String getCatalogState() {
        return this.dbcatalog;
    }
    
    final String getSchemaState() {
        return this.dbschema;
    }
    
    final int getTransactionIsolationState() {
        return this.transactionIsolation;
    }
    
    final boolean getReadOnlyState() {
        return this.isReadOnly;
    }
    
    final int getNetworkTimeoutState() {
        return this.networkTimeout;
    }
    
    final PoolEntry getPoolEntry() {
        return this.poolEntry;
    }
    
    final SQLException checkException(final SQLException sqle) {
        boolean evict = false;
        SQLException nse = sqle;
        final SQLExceptionOverride exceptionOverride = this.poolEntry.getPoolBase().exceptionOverride;
        int depth = 0;
        while (this.delegate != ClosedConnection.CLOSED_CONNECTION && nse != null && depth < 10) {
            final String sqlState = nse.getSQLState();
            if ((sqlState != null && sqlState.startsWith("08")) || nse instanceof SQLTimeoutException || ProxyConnection.ERROR_STATES.contains((Object)sqlState) || ProxyConnection.ERROR_CODES.contains((Object)nse.getErrorCode())) {
                if (exceptionOverride != null && exceptionOverride.adjudicate(nse) == SQLExceptionOverride.Override.DO_NOT_EVICT) {
                    break;
                }
                evict = true;
                break;
            }
            else {
                nse = nse.getNextException();
                ++depth;
            }
        }
        if (evict) {
            final SQLException exception = (nse != null) ? nse : sqle;
            ProxyConnection.LOGGER.warn("{} - Connection {} marked as broken because of SQLSTATE({}), ErrorCode({})", this.poolEntry.getPoolName(), this.delegate, exception.getSQLState(), exception.getErrorCode(), exception);
            this.leakTask.cancel();
            this.poolEntry.evict("(connection is broken)");
            this.delegate = ClosedConnection.CLOSED_CONNECTION;
        }
        return sqle;
    }
    
    final synchronized void untrackStatement(final Statement statement) {
        this.openStatements.remove(statement);
    }
    
    final void markCommitStateDirty() {
        if (!this.isAutoCommit) {
            this.isCommitStateDirty = true;
        }
    }
    
    void cancelLeakTask() {
        this.leakTask.cancel();
    }
    
    private synchronized <T extends Statement> T trackStatement(final T statement) {
        this.openStatements.add(statement);
        return statement;
    }
    
    private synchronized void closeStatements() {
        final int size = this.openStatements.size();
        if (size > 0) {
            for (int i = 0; i < size && this.delegate != ClosedConnection.CLOSED_CONNECTION; ++i) {
                try (final Statement ignored = this.openStatements.get(i)) {}
                catch (final SQLException e) {
                    ProxyConnection.LOGGER.warn("{} - Connection {} marked as broken because of an exception closing open statements during Connection.close()", this.poolEntry.getPoolName(), this.delegate);
                    this.leakTask.cancel();
                    this.poolEntry.evict("(exception closing Statements during Connection.close())");
                    this.delegate = ClosedConnection.CLOSED_CONNECTION;
                }
            }
            this.openStatements.clear();
        }
    }
    
    public final void close() throws SQLException {
        this.closeStatements();
        if (this.delegate != ClosedConnection.CLOSED_CONNECTION) {
            this.leakTask.cancel();
            try {
                if (this.isCommitStateDirty && !this.isAutoCommit) {
                    this.delegate.rollback();
                    ProxyConnection.LOGGER.debug("{} - Executed rollback on connection {} due to dirty commit state on close().", this.poolEntry.getPoolName(), this.delegate);
                }
                if (this.dirtyBits != 0) {
                    this.poolEntry.resetConnectionState(this, this.dirtyBits);
                }
                this.delegate.clearWarnings();
            }
            catch (final SQLException e) {
                if (!this.poolEntry.isMarkedEvicted()) {
                    throw this.checkException(e);
                }
            }
            finally {
                this.delegate = ClosedConnection.CLOSED_CONNECTION;
                this.poolEntry.recycle();
            }
        }
    }
    
    public boolean isClosed() throws SQLException {
        return this.delegate == ClosedConnection.CLOSED_CONNECTION;
    }
    
    public Statement createStatement() throws SQLException {
        return ProxyFactory.getProxyStatement(this, this.trackStatement(this.delegate.createStatement()));
    }
    
    public Statement createStatement(final int resultSetType, final int concurrency) throws SQLException {
        return ProxyFactory.getProxyStatement(this, this.trackStatement(this.delegate.createStatement(resultSetType, concurrency)));
    }
    
    public Statement createStatement(final int resultSetType, final int concurrency, final int holdability) throws SQLException {
        return ProxyFactory.getProxyStatement(this, this.trackStatement(this.delegate.createStatement(resultSetType, concurrency, holdability)));
    }
    
    public CallableStatement prepareCall(final String sql) throws SQLException {
        return ProxyFactory.getProxyCallableStatement(this, this.trackStatement(this.delegate.prepareCall(sql)));
    }
    
    public CallableStatement prepareCall(final String sql, final int resultSetType, final int concurrency) throws SQLException {
        return ProxyFactory.getProxyCallableStatement(this, this.trackStatement(this.delegate.prepareCall(sql, resultSetType, concurrency)));
    }
    
    public CallableStatement prepareCall(final String sql, final int resultSetType, final int concurrency, final int holdability) throws SQLException {
        return ProxyFactory.getProxyCallableStatement(this, this.trackStatement(this.delegate.prepareCall(sql, resultSetType, concurrency, holdability)));
    }
    
    public PreparedStatement prepareStatement(final String sql) throws SQLException {
        return ProxyFactory.getProxyPreparedStatement(this, this.trackStatement(this.delegate.prepareStatement(sql)));
    }
    
    public PreparedStatement prepareStatement(final String sql, final int autoGeneratedKeys) throws SQLException {
        return ProxyFactory.getProxyPreparedStatement(this, this.trackStatement(this.delegate.prepareStatement(sql, autoGeneratedKeys)));
    }
    
    public PreparedStatement prepareStatement(final String sql, final int resultSetType, final int concurrency) throws SQLException {
        return ProxyFactory.getProxyPreparedStatement(this, this.trackStatement(this.delegate.prepareStatement(sql, resultSetType, concurrency)));
    }
    
    public PreparedStatement prepareStatement(final String sql, final int resultSetType, final int concurrency, final int holdability) throws SQLException {
        return ProxyFactory.getProxyPreparedStatement(this, this.trackStatement(this.delegate.prepareStatement(sql, resultSetType, concurrency, holdability)));
    }
    
    public PreparedStatement prepareStatement(final String sql, final int[] columnIndexes) throws SQLException {
        return ProxyFactory.getProxyPreparedStatement(this, this.trackStatement(this.delegate.prepareStatement(sql, columnIndexes)));
    }
    
    public PreparedStatement prepareStatement(final String sql, final String[] columnNames) throws SQLException {
        return ProxyFactory.getProxyPreparedStatement(this, this.trackStatement(this.delegate.prepareStatement(sql, columnNames)));
    }
    
    public DatabaseMetaData getMetaData() throws SQLException {
        this.markCommitStateDirty();
        return ProxyFactory.getProxyDatabaseMetaData(this, this.delegate.getMetaData());
    }
    
    public void commit() throws SQLException {
        this.delegate.commit();
        this.isCommitStateDirty = false;
    }
    
    public void rollback() throws SQLException {
        this.delegate.rollback();
        this.isCommitStateDirty = false;
    }
    
    public void rollback(final Savepoint savepoint) throws SQLException {
        this.delegate.rollback(savepoint);
        this.isCommitStateDirty = false;
    }
    
    public void setAutoCommit(final boolean autoCommit) throws SQLException {
        this.delegate.setAutoCommit(autoCommit);
        this.isAutoCommit = autoCommit;
        this.dirtyBits |= 0x2;
    }
    
    public void setReadOnly(final boolean readOnly) throws SQLException {
        this.delegate.setReadOnly(readOnly);
        this.isReadOnly = readOnly;
        this.isCommitStateDirty = false;
        this.dirtyBits |= 0x1;
    }
    
    public void setTransactionIsolation(final int level) throws SQLException {
        this.delegate.setTransactionIsolation(level);
        this.transactionIsolation = level;
        this.dirtyBits |= 0x4;
    }
    
    public void setCatalog(final String catalog) throws SQLException {
        this.delegate.setCatalog(catalog);
        this.dbcatalog = catalog;
        this.dirtyBits |= 0x8;
    }
    
    public void setNetworkTimeout(final Executor executor, final int milliseconds) throws SQLException {
        this.delegate.setNetworkTimeout(executor, milliseconds);
        this.networkTimeout = milliseconds;
        this.dirtyBits |= 0x10;
    }
    
    public void setSchema(final String schema) throws SQLException {
        this.delegate.setSchema(schema);
        this.dbschema = schema;
        this.dirtyBits |= 0x20;
    }
    
    public final boolean isWrapperFor(final Class<?> iface) throws SQLException {
        return iface.isInstance(this.delegate) || (this.delegate != null && this.delegate.isWrapperFor((Class)iface));
    }
    
    public final <T> T unwrap(final Class<T> iface) throws SQLException {
        if (iface.isInstance(this.delegate)) {
            return (T)this.delegate;
        }
        if (this.delegate != null) {
            return (T)this.delegate.unwrap((Class)iface);
        }
        throw new SQLException("Wrapped connection is not an instance of " + iface);
    }
    
    static {
        LOGGER = LoggerFactory.getLogger(ProxyConnection.class);
        (ERROR_STATES = (Set)new HashSet()).add((Object)"0A000");
        ProxyConnection.ERROR_STATES.add((Object)"57P01");
        ProxyConnection.ERROR_STATES.add((Object)"57P02");
        ProxyConnection.ERROR_STATES.add((Object)"57P03");
        ProxyConnection.ERROR_STATES.add((Object)"01002");
        ProxyConnection.ERROR_STATES.add((Object)"JZ0C0");
        ProxyConnection.ERROR_STATES.add((Object)"JZ0C1");
        (ERROR_CODES = (Set)new HashSet()).add((Object)500150);
        ProxyConnection.ERROR_CODES.add((Object)2399);
        ProxyConnection.ERROR_CODES.add((Object)1105);
    }
    
    private static final class ClosedConnection
    {
        static final Connection CLOSED_CONNECTION;
        
        private static Connection getClosedConnection() {
            final InvocationHandler handler = (proxy, method, args) -> {
                final String methodName = method.getName();
                if ("isClosed".equals((Object)methodName)) {
                    return Boolean.TRUE;
                }
                if ("isValid".equals((Object)methodName)) {
                    return Boolean.FALSE;
                }
                if ("abort".equals((Object)methodName)) {
                    return Void.TYPE;
                }
                if ("close".equals((Object)methodName)) {
                    return Void.TYPE;
                }
                if ("toString".equals((Object)methodName)) {
                    return ClosedConnection.class.getCanonicalName();
                }
                throw new SQLException("Connection is closed");
            };
            return (Connection)Proxy.newProxyInstance(Connection.class.getClassLoader(), new Class[] { Connection.class }, handler);
        }
        
        static {
            CLOSED_CONNECTION = getClosedConnection();
        }
    }
}
