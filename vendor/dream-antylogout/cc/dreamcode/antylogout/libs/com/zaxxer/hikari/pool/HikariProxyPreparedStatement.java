package cc.dreamcode.antylogout.libs.com.zaxxer.hikari.pool;

import java.sql.SQLType;
import java.sql.SQLXML;
import java.sql.NClob;
import java.sql.RowId;
import java.sql.ParameterMetaData;
import java.net.URL;
import java.util.Calendar;
import java.sql.ResultSetMetaData;
import java.sql.Array;
import java.sql.Clob;
import java.sql.Blob;
import java.sql.Ref;
import java.io.Reader;
import java.io.InputStream;
import java.sql.Timestamp;
import java.sql.Time;
import java.sql.Date;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLWarning;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Wrapper;

public final class HikariProxyPreparedStatement extends ProxyPreparedStatement implements Wrapper, AutoCloseable, Statement, PreparedStatement
{
    public boolean isWrapperFor(final Class clazz) throws SQLException {
        try {
            return ((Wrapper)super.delegate).isWrapperFor(clazz);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public ResultSet executeQuery(final String sql) throws SQLException {
        try {
            return super.executeQuery(sql);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
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
            return ((Statement)super.delegate).getMaxFieldSize();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setMaxFieldSize(final int maxFieldSize) throws SQLException {
        try {
            ((Statement)super.delegate).setMaxFieldSize(maxFieldSize);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public int getMaxRows() throws SQLException {
        try {
            return ((Statement)super.delegate).getMaxRows();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setMaxRows(final int maxRows) throws SQLException {
        try {
            ((Statement)super.delegate).setMaxRows(maxRows);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setEscapeProcessing(final boolean escapeProcessing) throws SQLException {
        try {
            ((Statement)super.delegate).setEscapeProcessing(escapeProcessing);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public int getQueryTimeout() throws SQLException {
        try {
            return ((Statement)super.delegate).getQueryTimeout();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setQueryTimeout(final int queryTimeout) throws SQLException {
        try {
            ((Statement)super.delegate).setQueryTimeout(queryTimeout);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void cancel() throws SQLException {
        try {
            ((Statement)super.delegate).cancel();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public SQLWarning getWarnings() throws SQLException {
        try {
            return ((Statement)super.delegate).getWarnings();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void clearWarnings() throws SQLException {
        try {
            ((Statement)super.delegate).clearWarnings();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setCursorName(final String cursorName) throws SQLException {
        try {
            ((Statement)super.delegate).setCursorName(cursorName);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean execute(final String sql) throws SQLException {
        try {
            return super.execute(sql);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
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
            return ((Statement)super.delegate).getUpdateCount();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean getMoreResults() throws SQLException {
        try {
            return ((Statement)super.delegate).getMoreResults();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setFetchDirection(final int fetchDirection) throws SQLException {
        try {
            ((Statement)super.delegate).setFetchDirection(fetchDirection);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public int getFetchDirection() throws SQLException {
        try {
            return ((Statement)super.delegate).getFetchDirection();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setFetchSize(final int fetchSize) throws SQLException {
        try {
            ((Statement)super.delegate).setFetchSize(fetchSize);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public int getFetchSize() throws SQLException {
        try {
            return ((Statement)super.delegate).getFetchSize();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public int getResultSetConcurrency() throws SQLException {
        try {
            return ((Statement)super.delegate).getResultSetConcurrency();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public int getResultSetType() throws SQLException {
        try {
            return ((Statement)super.delegate).getResultSetType();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void addBatch(final String s) throws SQLException {
        try {
            ((Statement)super.delegate).addBatch(s);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void clearBatch() throws SQLException {
        try {
            ((Statement)super.delegate).clearBatch();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public int[] executeBatch() throws SQLException {
        try {
            return super.executeBatch();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
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
            return ((Statement)super.delegate).getMoreResults(n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public ResultSet getGeneratedKeys() throws SQLException {
        try {
            return super.getGeneratedKeys();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public int executeUpdate(final String sql, final int autoGeneratedKeys) throws SQLException {
        try {
            return super.executeUpdate(sql, autoGeneratedKeys);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public int executeUpdate(final String sql, final int[] columnIndexes) throws SQLException {
        try {
            return super.executeUpdate(sql, columnIndexes);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public int executeUpdate(final String sql, final String[] columnNames) throws SQLException {
        try {
            return super.executeUpdate(sql, columnNames);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean execute(final String sql, final int autoGeneratedKeys) throws SQLException {
        try {
            return super.execute(sql, autoGeneratedKeys);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean execute(final String sql, final int[] columnIndexes) throws SQLException {
        try {
            return super.execute(sql, columnIndexes);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
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
            return ((Statement)super.delegate).getResultSetHoldability();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean isClosed() throws SQLException {
        try {
            return ((Statement)super.delegate).isClosed();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setPoolable(final boolean poolable) throws SQLException {
        try {
            ((Statement)super.delegate).setPoolable(poolable);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean isPoolable() throws SQLException {
        try {
            return ((Statement)super.delegate).isPoolable();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void closeOnCompletion() throws SQLException {
        try {
            ((Statement)super.delegate).closeOnCompletion();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean isCloseOnCompletion() throws SQLException {
        try {
            return ((Statement)super.delegate).isCloseOnCompletion();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public long getLargeUpdateCount() throws SQLException {
        try {
            return ((Statement)super.delegate).getLargeUpdateCount();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setLargeMaxRows(final long largeMaxRows) throws SQLException {
        try {
            ((Statement)super.delegate).setLargeMaxRows(largeMaxRows);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public long getLargeMaxRows() throws SQLException {
        try {
            return ((Statement)super.delegate).getLargeMaxRows();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public long[] executeLargeBatch() throws SQLException {
        try {
            return ((Statement)super.delegate).executeLargeBatch();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public long executeLargeUpdate(final String s) throws SQLException {
        try {
            return ((Statement)super.delegate).executeLargeUpdate(s);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public long executeLargeUpdate(final String s, final int n) throws SQLException {
        try {
            return ((Statement)super.delegate).executeLargeUpdate(s, n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public long executeLargeUpdate(final String s, final int[] array) throws SQLException {
        try {
            return ((Statement)super.delegate).executeLargeUpdate(s, array);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public long executeLargeUpdate(final String s, final String[] array) throws SQLException {
        try {
            return ((Statement)super.delegate).executeLargeUpdate(s, array);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public String enquoteLiteral(final String s) throws SQLException {
        try {
            return ((Statement)super.delegate).enquoteLiteral(s);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public String enquoteIdentifier(final String s, final boolean b) throws SQLException {
        try {
            return ((Statement)super.delegate).enquoteIdentifier(s, b);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean isSimpleIdentifier(final String s) throws SQLException {
        try {
            return ((Statement)super.delegate).isSimpleIdentifier(s);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public String enquoteNCharLiteral(final String s) throws SQLException {
        try {
            return ((Statement)super.delegate).enquoteNCharLiteral(s);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public ResultSet executeQuery() throws SQLException {
        try {
            return super.executeQuery();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public int executeUpdate() throws SQLException {
        try {
            return super.executeUpdate();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setNull(final int n, final int n2) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setNull(n, n2);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setBoolean(final int n, final boolean b) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setBoolean(n, b);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setByte(final int n, final byte b) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setByte(n, b);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setShort(final int n, final short n2) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setShort(n, n2);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setInt(final int n, final int n2) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setInt(n, n2);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setLong(final int n, final long n2) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setLong(n, n2);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setFloat(final int n, final float n2) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setFloat(n, n2);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setDouble(final int n, final double n2) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setDouble(n, n2);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setBigDecimal(final int n, final BigDecimal bigDecimal) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setBigDecimal(n, bigDecimal);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setString(final int n, final String s) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setString(n, s);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setBytes(final int n, final byte[] array) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setBytes(n, array);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setDate(final int n, final Date date) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setDate(n, date);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setTime(final int n, final Time time) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setTime(n, time);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setTimestamp(final int n, final Timestamp timestamp) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setTimestamp(n, timestamp);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setAsciiStream(final int n, final InputStream inputStream, final int n2) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setAsciiStream(n, inputStream, n2);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setUnicodeStream(final int n, final InputStream inputStream, final int n2) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setUnicodeStream(n, inputStream, n2);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setBinaryStream(final int n, final InputStream inputStream, final int n2) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setBinaryStream(n, inputStream, n2);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void clearParameters() throws SQLException {
        try {
            ((PreparedStatement)super.delegate).clearParameters();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setObject(final int n, final Object o, final int n2) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setObject(n, o, n2);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setObject(final int n, final Object o) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setObject(n, o);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public boolean execute() throws SQLException {
        try {
            return super.execute();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void addBatch() throws SQLException {
        try {
            ((PreparedStatement)super.delegate).addBatch();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setCharacterStream(final int n, final Reader reader, final int n2) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setCharacterStream(n, reader, n2);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setRef(final int n, final Ref ref) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setRef(n, ref);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setBlob(final int n, final Blob blob) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setBlob(n, blob);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setClob(final int n, final Clob clob) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setClob(n, clob);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setArray(final int n, final Array array) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setArray(n, array);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public ResultSetMetaData getMetaData() throws SQLException {
        try {
            return ((PreparedStatement)super.delegate).getMetaData();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setDate(final int n, final Date date, final Calendar calendar) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setDate(n, date, calendar);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setTime(final int n, final Time time, final Calendar calendar) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setTime(n, time, calendar);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setTimestamp(final int n, final Timestamp timestamp, final Calendar calendar) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setTimestamp(n, timestamp, calendar);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setNull(final int n, final int n2, final String s) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setNull(n, n2, s);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setURL(final int n, final URL url) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setURL(n, url);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public ParameterMetaData getParameterMetaData() throws SQLException {
        try {
            return ((PreparedStatement)super.delegate).getParameterMetaData();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setRowId(final int n, final RowId rowId) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setRowId(n, rowId);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setNString(final int n, final String s) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setNString(n, s);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setNCharacterStream(final int n, final Reader reader, final long n2) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setNCharacterStream(n, reader, n2);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setNClob(final int n, final NClob nClob) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setNClob(n, nClob);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setClob(final int n, final Reader reader, final long n2) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setClob(n, reader, n2);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setBlob(final int n, final InputStream inputStream, final long n2) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setBlob(n, inputStream, n2);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setNClob(final int n, final Reader reader, final long n2) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setNClob(n, reader, n2);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setSQLXML(final int n, final SQLXML sqlxml) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setSQLXML(n, sqlxml);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setObject(final int n, final Object o, final int n2, final int n3) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setObject(n, o, n2, n3);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setAsciiStream(final int n, final InputStream inputStream, final long n2) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setAsciiStream(n, inputStream, n2);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setBinaryStream(final int n, final InputStream inputStream, final long n2) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setBinaryStream(n, inputStream, n2);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setCharacterStream(final int n, final Reader reader, final long n2) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setCharacterStream(n, reader, n2);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setAsciiStream(final int n, final InputStream inputStream) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setAsciiStream(n, inputStream);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setBinaryStream(final int n, final InputStream inputStream) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setBinaryStream(n, inputStream);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setCharacterStream(final int n, final Reader reader) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setCharacterStream(n, reader);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setNCharacterStream(final int n, final Reader reader) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setNCharacterStream(n, reader);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setClob(final int n, final Reader reader) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setClob(n, reader);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setBlob(final int n, final InputStream inputStream) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setBlob(n, inputStream);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setNClob(final int n, final Reader reader) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setNClob(n, reader);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setObject(final int n, final Object o, final SQLType sqlType, final int n2) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setObject(n, o, sqlType, n2);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setObject(final int n, final Object o, final SQLType sqlType) throws SQLException {
        try {
            ((PreparedStatement)super.delegate).setObject(n, o, sqlType);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public long executeLargeUpdate() throws SQLException {
        try {
            return ((PreparedStatement)super.delegate).executeLargeUpdate();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    HikariProxyPreparedStatement(final ProxyConnection connection, final PreparedStatement statement) {
        super(connection, statement);
    }
}
