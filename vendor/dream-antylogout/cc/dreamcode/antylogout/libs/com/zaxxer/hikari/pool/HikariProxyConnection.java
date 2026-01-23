package cc.dreamcode.antylogout.libs.com.zaxxer.hikari.pool;

import cc.dreamcode.antylogout.libs.com.zaxxer.hikari.util.FastList;
import java.sql.ShardingKey;
import java.util.concurrent.Executor;
import java.sql.Struct;
import java.sql.Array;
import java.util.Properties;
import java.sql.SQLClientInfoException;
import java.sql.SQLXML;
import java.sql.NClob;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Savepoint;
import java.util.Map;
import java.sql.SQLWarning;
import java.sql.DatabaseMetaData;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.Wrapper;

public final class HikariProxyConnection extends ProxyConnection implements Wrapper, AutoCloseable, Connection
{
    @Override
    public Statement createStatement() throws SQLException {
        try {
            return super.createStatement();
        }
        catch (final SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    @Override
    public PreparedStatement prepareStatement(final String sql) throws SQLException {
        try {
            return super.prepareStatement(sql);
        }
        catch (final SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    @Override
    public CallableStatement prepareCall(final String sql) throws SQLException {
        try {
            return super.prepareCall(sql);
        }
        catch (final SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    public String nativeSQL(final String s) throws SQLException {
        try {
            return super.delegate.nativeSQL(s);
        }
        catch (final SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    @Override
    public void setAutoCommit(final boolean autoCommit) throws SQLException {
        try {
            super.setAutoCommit(autoCommit);
        }
        catch (final SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    public boolean getAutoCommit() throws SQLException {
        try {
            return super.delegate.getAutoCommit();
        }
        catch (final SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    @Override
    public void commit() throws SQLException {
        try {
            super.commit();
        }
        catch (final SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    @Override
    public void rollback() throws SQLException {
        try {
            super.rollback();
        }
        catch (final SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    @Override
    public boolean isClosed() throws SQLException {
        try {
            return super.isClosed();
        }
        catch (final SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        try {
            return super.getMetaData();
        }
        catch (final SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    @Override
    public void setReadOnly(final boolean readOnly) throws SQLException {
        try {
            super.setReadOnly(readOnly);
        }
        catch (final SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    public boolean isReadOnly() throws SQLException {
        try {
            return super.delegate.isReadOnly();
        }
        catch (final SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    @Override
    public void setCatalog(final String catalog) throws SQLException {
        try {
            super.setCatalog(catalog);
        }
        catch (final SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    public String getCatalog() throws SQLException {
        try {
            return super.delegate.getCatalog();
        }
        catch (final SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    @Override
    public void setTransactionIsolation(final int transactionIsolation) throws SQLException {
        try {
            super.setTransactionIsolation(transactionIsolation);
        }
        catch (final SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    public int getTransactionIsolation() throws SQLException {
        try {
            return super.delegate.getTransactionIsolation();
        }
        catch (final SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    public SQLWarning getWarnings() throws SQLException {
        try {
            return super.delegate.getWarnings();
        }
        catch (final SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    public void clearWarnings() throws SQLException {
        try {
            super.delegate.clearWarnings();
        }
        catch (final SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    @Override
    public Statement createStatement(final int resultSetType, final int concurrency) throws SQLException {
        try {
            return super.createStatement(resultSetType, concurrency);
        }
        catch (final SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    @Override
    public PreparedStatement prepareStatement(final String sql, final int resultSetType, final int concurrency) throws SQLException {
        try {
            return super.prepareStatement(sql, resultSetType, concurrency);
        }
        catch (final SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    @Override
    public CallableStatement prepareCall(final String sql, final int resultSetType, final int concurrency) throws SQLException {
        try {
            return super.prepareCall(sql, resultSetType, concurrency);
        }
        catch (final SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    public Map getTypeMap() throws SQLException {
        try {
            return super.delegate.getTypeMap();
        }
        catch (final SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    public void setTypeMap(final Map typeMap) throws SQLException {
        try {
            super.delegate.setTypeMap(typeMap);
        }
        catch (final SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    public void setHoldability(final int holdability) throws SQLException {
        try {
            super.delegate.setHoldability(holdability);
        }
        catch (final SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    public int getHoldability() throws SQLException {
        try {
            return super.delegate.getHoldability();
        }
        catch (final SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    public Savepoint setSavepoint() throws SQLException {
        try {
            return super.delegate.setSavepoint();
        }
        catch (final SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    public Savepoint setSavepoint(final String savepoint) throws SQLException {
        try {
            return super.delegate.setSavepoint(savepoint);
        }
        catch (final SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    @Override
    public void rollback(final Savepoint savepoint) throws SQLException {
        try {
            super.rollback(savepoint);
        }
        catch (final SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    public void releaseSavepoint(final Savepoint savepoint) throws SQLException {
        try {
            super.delegate.releaseSavepoint(savepoint);
        }
        catch (final SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    @Override
    public Statement createStatement(final int resultSetType, final int concurrency, final int holdability) throws SQLException {
        try {
            return super.createStatement(resultSetType, concurrency, holdability);
        }
        catch (final SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    @Override
    public PreparedStatement prepareStatement(final String sql, final int resultSetType, final int concurrency, final int holdability) throws SQLException {
        try {
            return super.prepareStatement(sql, resultSetType, concurrency, holdability);
        }
        catch (final SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    @Override
    public CallableStatement prepareCall(final String sql, final int resultSetType, final int concurrency, final int holdability) throws SQLException {
        try {
            return super.prepareCall(sql, resultSetType, concurrency, holdability);
        }
        catch (final SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    @Override
    public PreparedStatement prepareStatement(final String sql, final int autoGeneratedKeys) throws SQLException {
        try {
            return super.prepareStatement(sql, autoGeneratedKeys);
        }
        catch (final SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    @Override
    public PreparedStatement prepareStatement(final String sql, final int[] columnIndexes) throws SQLException {
        try {
            return super.prepareStatement(sql, columnIndexes);
        }
        catch (final SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    @Override
    public PreparedStatement prepareStatement(final String sql, final String[] columnNames) throws SQLException {
        try {
            return super.prepareStatement(sql, columnNames);
        }
        catch (final SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    public Clob createClob() throws SQLException {
        try {
            return super.delegate.createClob();
        }
        catch (final SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    public Blob createBlob() throws SQLException {
        try {
            return super.delegate.createBlob();
        }
        catch (final SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    public NClob createNClob() throws SQLException {
        try {
            return super.delegate.createNClob();
        }
        catch (final SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    public SQLXML createSQLXML() throws SQLException {
        try {
            return super.delegate.createSQLXML();
        }
        catch (final SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    public boolean isValid(final int n) throws SQLException {
        try {
            return super.delegate.isValid(n);
        }
        catch (final SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    public void setClientInfo(final String s, final String s2) throws SQLClientInfoException {
        super.delegate.setClientInfo(s, s2);
    }
    
    public void setClientInfo(final Properties clientInfo) throws SQLClientInfoException {
        super.delegate.setClientInfo(clientInfo);
    }
    
    public String getClientInfo(final String s) throws SQLException {
        try {
            return super.delegate.getClientInfo(s);
        }
        catch (final SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    public Properties getClientInfo() throws SQLException {
        try {
            return super.delegate.getClientInfo();
        }
        catch (final SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    public Array createArrayOf(final String s, final Object[] array) throws SQLException {
        try {
            return super.delegate.createArrayOf(s, array);
        }
        catch (final SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    public Struct createStruct(final String s, final Object[] array) throws SQLException {
        try {
            return super.delegate.createStruct(s, array);
        }
        catch (final SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    @Override
    public void setSchema(final String schema) throws SQLException {
        try {
            super.setSchema(schema);
        }
        catch (final SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    public String getSchema() throws SQLException {
        try {
            return super.delegate.getSchema();
        }
        catch (final SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    public void abort(final Executor executor) throws SQLException {
        try {
            super.delegate.abort(executor);
        }
        catch (final SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    @Override
    public void setNetworkTimeout(final Executor executor, final int milliseconds) throws SQLException {
        try {
            super.setNetworkTimeout(executor, milliseconds);
        }
        catch (final SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    public int getNetworkTimeout() throws SQLException {
        try {
            return super.delegate.getNetworkTimeout();
        }
        catch (final SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    public void beginRequest() throws SQLException {
        try {
            super.delegate.beginRequest();
        }
        catch (final SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    public void endRequest() throws SQLException {
        try {
            super.delegate.endRequest();
        }
        catch (final SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    public boolean setShardingKeyIfValid(final ShardingKey shardingKey, final ShardingKey shardingKey2, final int n) throws SQLException {
        try {
            return super.delegate.setShardingKeyIfValid(shardingKey, shardingKey2, n);
        }
        catch (final SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    public boolean setShardingKeyIfValid(final ShardingKey shardingKey, final int n) throws SQLException {
        try {
            return super.delegate.setShardingKeyIfValid(shardingKey, n);
        }
        catch (final SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    public void setShardingKey(final ShardingKey shardingKey, final ShardingKey shardingKey2) throws SQLException {
        try {
            super.delegate.setShardingKey(shardingKey, shardingKey2);
        }
        catch (final SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    public void setShardingKey(final ShardingKey shardingKey) throws SQLException {
        try {
            super.delegate.setShardingKey(shardingKey);
        }
        catch (final SQLException sqle) {
            throw this.checkException(sqle);
        }
    }
    
    protected HikariProxyConnection(final PoolEntry poolEntry, final Connection connection, final FastList openStatements, final ProxyLeakTask leakTask, final boolean isReadOnly, final boolean isAutoCommit) {
        super(poolEntry, connection, openStatements, leakTask, isReadOnly, isAutoCommit);
    }
}
