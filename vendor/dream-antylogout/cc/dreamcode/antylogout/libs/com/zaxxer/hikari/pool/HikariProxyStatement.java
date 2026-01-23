package cc.dreamcode.antylogout.libs.com.zaxxer.hikari.pool;

import java.sql.Connection;
import java.sql.SQLWarning;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Wrapper;

public final class HikariProxyStatement extends ProxyStatement implements Wrapper, AutoCloseable, Statement
{
    public boolean isWrapperFor(final Class clazz) throws SQLException {
        try {
            return ((Wrapper)super.delegate).isWrapperFor(clazz);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public ResultSet executeQuery(final String sql) throws SQLException {
        try {
            return super.executeQuery(sql);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public int executeUpdate(final String sql) throws SQLException {
        try {
            return super.executeUpdate(sql);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public int getMaxFieldSize() throws SQLException {
        try {
            return super.delegate.getMaxFieldSize();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setMaxFieldSize(final int maxFieldSize) throws SQLException {
        try {
            super.delegate.setMaxFieldSize(maxFieldSize);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public int getMaxRows() throws SQLException {
        try {
            return super.delegate.getMaxRows();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setMaxRows(final int maxRows) throws SQLException {
        try {
            super.delegate.setMaxRows(maxRows);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setEscapeProcessing(final boolean escapeProcessing) throws SQLException {
        try {
            super.delegate.setEscapeProcessing(escapeProcessing);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public int getQueryTimeout() throws SQLException {
        try {
            return super.delegate.getQueryTimeout();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setQueryTimeout(final int queryTimeout) throws SQLException {
        try {
            super.delegate.setQueryTimeout(queryTimeout);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void cancel() throws SQLException {
        try {
            super.delegate.cancel();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public SQLWarning getWarnings() throws SQLException {
        try {
            return super.delegate.getWarnings();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void clearWarnings() throws SQLException {
        try {
            super.delegate.clearWarnings();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setCursorName(final String cursorName) throws SQLException {
        try {
            super.delegate.setCursorName(cursorName);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public boolean execute(final String sql) throws SQLException {
        try {
            return super.execute(sql);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public ResultSet getResultSet() throws SQLException {
        try {
            return super.getResultSet();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public int getUpdateCount() throws SQLException {
        try {
            return super.delegate.getUpdateCount();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean getMoreResults() throws SQLException {
        try {
            return super.delegate.getMoreResults();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setFetchDirection(final int fetchDirection) throws SQLException {
        try {
            super.delegate.setFetchDirection(fetchDirection);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public int getFetchDirection() throws SQLException {
        try {
            return super.delegate.getFetchDirection();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setFetchSize(final int fetchSize) throws SQLException {
        try {
            super.delegate.setFetchSize(fetchSize);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public int getFetchSize() throws SQLException {
        try {
            return super.delegate.getFetchSize();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public int getResultSetConcurrency() throws SQLException {
        try {
            return super.delegate.getResultSetConcurrency();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public int getResultSetType() throws SQLException {
        try {
            return super.delegate.getResultSetType();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void addBatch(final String s) throws SQLException {
        try {
            super.delegate.addBatch(s);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void clearBatch() throws SQLException {
        try {
            super.delegate.clearBatch();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public int[] executeBatch() throws SQLException {
        try {
            return super.executeBatch();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public Connection getConnection() throws SQLException {
        try {
            return super.getConnection();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean getMoreResults(final int n) throws SQLException {
        try {
            return super.delegate.getMoreResults(n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public ResultSet getGeneratedKeys() throws SQLException {
        try {
            return super.getGeneratedKeys();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public int executeUpdate(final String sql, final int autoGeneratedKeys) throws SQLException {
        try {
            return super.executeUpdate(sql, autoGeneratedKeys);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public int executeUpdate(final String sql, final int[] columnIndexes) throws SQLException {
        try {
            return super.executeUpdate(sql, columnIndexes);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public int executeUpdate(final String sql, final String[] columnNames) throws SQLException {
        try {
            return super.executeUpdate(sql, columnNames);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public boolean execute(final String sql, final int autoGeneratedKeys) throws SQLException {
        try {
            return super.execute(sql, autoGeneratedKeys);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public boolean execute(final String sql, final int[] columnIndexes) throws SQLException {
        try {
            return super.execute(sql, columnIndexes);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public boolean execute(final String sql, final String[] columnNames) throws SQLException {
        try {
            return super.execute(sql, columnNames);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public int getResultSetHoldability() throws SQLException {
        try {
            return super.delegate.getResultSetHoldability();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean isClosed() throws SQLException {
        try {
            return super.delegate.isClosed();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setPoolable(final boolean poolable) throws SQLException {
        try {
            super.delegate.setPoolable(poolable);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean isPoolable() throws SQLException {
        try {
            return super.delegate.isPoolable();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void closeOnCompletion() throws SQLException {
        try {
            super.delegate.closeOnCompletion();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean isCloseOnCompletion() throws SQLException {
        try {
            return super.delegate.isCloseOnCompletion();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public long getLargeUpdateCount() throws SQLException {
        try {
            return super.delegate.getLargeUpdateCount();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setLargeMaxRows(final long largeMaxRows) throws SQLException {
        try {
            super.delegate.setLargeMaxRows(largeMaxRows);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public long getLargeMaxRows() throws SQLException {
        try {
            return super.delegate.getLargeMaxRows();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public long[] executeLargeBatch() throws SQLException {
        try {
            return super.delegate.executeLargeBatch();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public long executeLargeUpdate(final String s) throws SQLException {
        try {
            return super.delegate.executeLargeUpdate(s);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public long executeLargeUpdate(final String s, final int n) throws SQLException {
        try {
            return super.delegate.executeLargeUpdate(s, n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public long executeLargeUpdate(final String s, final int[] array) throws SQLException {
        try {
            return super.delegate.executeLargeUpdate(s, array);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public long executeLargeUpdate(final String s, final String[] array) throws SQLException {
        try {
            return super.delegate.executeLargeUpdate(s, array);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public String enquoteLiteral(final String s) throws SQLException {
        try {
            return super.delegate.enquoteLiteral(s);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public String enquoteIdentifier(final String s, final boolean b) throws SQLException {
        try {
            return super.delegate.enquoteIdentifier(s, b);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean isSimpleIdentifier(final String s) throws SQLException {
        try {
            return super.delegate.isSimpleIdentifier(s);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public String enquoteNCharLiteral(final String s) throws SQLException {
        try {
            return super.delegate.enquoteNCharLiteral(s);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    HikariProxyStatement(final ProxyConnection connection, final Statement statement) {
        super(connection, statement);
    }
}
