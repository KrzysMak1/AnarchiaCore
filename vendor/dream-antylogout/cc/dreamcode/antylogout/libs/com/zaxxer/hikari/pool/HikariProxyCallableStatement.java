package cc.dreamcode.antylogout.libs.com.zaxxer.hikari.pool;

import java.util.Map;
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
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Wrapper;

public final class HikariProxyCallableStatement extends ProxyCallableStatement implements Wrapper, AutoCloseable, Statement, PreparedStatement, CallableStatement
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
    
    public ResultSet executeQuery() throws SQLException {
        try {
            return super.executeQuery();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
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
    
    public long executeLargeUpdate() throws SQLException {
        try {
            return ((PreparedStatement)super.delegate).executeLargeUpdate();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void registerOutParameter(final int n, final int n2) throws SQLException {
        try {
            ((CallableStatement)super.delegate).registerOutParameter(n, n2);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void registerOutParameter(final int n, final int n2, final int n3) throws SQLException {
        try {
            ((CallableStatement)super.delegate).registerOutParameter(n, n2, n3);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean wasNull() throws SQLException {
        try {
            return ((CallableStatement)super.delegate).wasNull();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public String getString(final int n) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getString(n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean getBoolean(final int n) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getBoolean(n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public byte getByte(final int n) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getByte(n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public short getShort(final int n) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getShort(n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public int getInt(final int n) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getInt(n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public long getLong(final int n) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getLong(n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public float getFloat(final int n) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getFloat(n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public double getDouble(final int n) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getDouble(n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public BigDecimal getBigDecimal(final int n, final int n2) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getBigDecimal(n, n2);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public byte[] getBytes(final int n) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getBytes(n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public Date getDate(final int n) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getDate(n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public Time getTime(final int n) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getTime(n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public Timestamp getTimestamp(final int n) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getTimestamp(n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public Object getObject(final int n) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getObject(n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public BigDecimal getBigDecimal(final int n) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getBigDecimal(n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public Object getObject(final int n, final Map map) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getObject(n, map);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public Ref getRef(final int n) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getRef(n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public Blob getBlob(final int n) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getBlob(n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public Clob getClob(final int n) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getClob(n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public Array getArray(final int n) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getArray(n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public Date getDate(final int n, final Calendar calendar) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getDate(n, calendar);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public Time getTime(final int n, final Calendar calendar) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getTime(n, calendar);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public Timestamp getTimestamp(final int n, final Calendar calendar) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getTimestamp(n, calendar);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void registerOutParameter(final int n, final int n2, final String s) throws SQLException {
        try {
            ((CallableStatement)super.delegate).registerOutParameter(n, n2, s);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void registerOutParameter(final String s, final int n) throws SQLException {
        try {
            ((CallableStatement)super.delegate).registerOutParameter(s, n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void registerOutParameter(final String s, final int n, final int n2) throws SQLException {
        try {
            ((CallableStatement)super.delegate).registerOutParameter(s, n, n2);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void registerOutParameter(final String s, final int n, final String s2) throws SQLException {
        try {
            ((CallableStatement)super.delegate).registerOutParameter(s, n, s2);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public URL getURL(final int n) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getURL(n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setURL(final String s, final URL url) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setURL(s, url);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setNull(final String s, final int n) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setNull(s, n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setBoolean(final String s, final boolean b) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setBoolean(s, b);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setByte(final String s, final byte b) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setByte(s, b);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setShort(final String s, final short n) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setShort(s, n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setInt(final String s, final int n) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setInt(s, n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setLong(final String s, final long n) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setLong(s, n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setFloat(final String s, final float n) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setFloat(s, n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setDouble(final String s, final double n) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setDouble(s, n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setBigDecimal(final String s, final BigDecimal bigDecimal) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setBigDecimal(s, bigDecimal);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setString(final String s, final String s2) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setString(s, s2);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setBytes(final String s, final byte[] array) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setBytes(s, array);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setDate(final String s, final Date date) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setDate(s, date);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setTime(final String s, final Time time) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setTime(s, time);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setTimestamp(final String s, final Timestamp timestamp) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setTimestamp(s, timestamp);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setAsciiStream(final String s, final InputStream inputStream, final int n) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setAsciiStream(s, inputStream, n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setBinaryStream(final String s, final InputStream inputStream, final int n) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setBinaryStream(s, inputStream, n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setObject(final String s, final Object o, final int n, final int n2) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setObject(s, o, n, n2);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setObject(final String s, final Object o, final int n) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setObject(s, o, n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setObject(final String s, final Object o) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setObject(s, o);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setCharacterStream(final String s, final Reader reader, final int n) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setCharacterStream(s, reader, n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setDate(final String s, final Date date, final Calendar calendar) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setDate(s, date, calendar);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setTime(final String s, final Time time, final Calendar calendar) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setTime(s, time, calendar);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setTimestamp(final String s, final Timestamp timestamp, final Calendar calendar) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setTimestamp(s, timestamp, calendar);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setNull(final String s, final int n, final String s2) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setNull(s, n, s2);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public String getString(final String s) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getString(s);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean getBoolean(final String s) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getBoolean(s);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public byte getByte(final String s) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getByte(s);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public short getShort(final String s) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getShort(s);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public int getInt(final String s) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getInt(s);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public long getLong(final String s) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getLong(s);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public float getFloat(final String s) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getFloat(s);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public double getDouble(final String s) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getDouble(s);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public byte[] getBytes(final String s) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getBytes(s);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public Date getDate(final String s) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getDate(s);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public Time getTime(final String s) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getTime(s);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public Timestamp getTimestamp(final String s) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getTimestamp(s);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public Object getObject(final String s) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getObject(s);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public BigDecimal getBigDecimal(final String s) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getBigDecimal(s);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public Object getObject(final String s, final Map map) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getObject(s, map);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public Ref getRef(final String s) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getRef(s);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public Blob getBlob(final String s) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getBlob(s);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public Clob getClob(final String s) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getClob(s);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public Array getArray(final String s) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getArray(s);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public Date getDate(final String s, final Calendar calendar) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getDate(s, calendar);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public Time getTime(final String s, final Calendar calendar) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getTime(s, calendar);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public Timestamp getTimestamp(final String s, final Calendar calendar) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getTimestamp(s, calendar);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public URL getURL(final String s) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getURL(s);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public RowId getRowId(final int n) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getRowId(n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public RowId getRowId(final String s) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getRowId(s);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setRowId(final String s, final RowId rowId) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setRowId(s, rowId);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setNString(final String s, final String s2) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setNString(s, s2);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setNCharacterStream(final String s, final Reader reader, final long n) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setNCharacterStream(s, reader, n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setNClob(final String s, final NClob nClob) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setNClob(s, nClob);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setClob(final String s, final Reader reader, final long n) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setClob(s, reader, n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setBlob(final String s, final InputStream inputStream, final long n) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setBlob(s, inputStream, n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setNClob(final String s, final Reader reader, final long n) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setNClob(s, reader, n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public NClob getNClob(final int n) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getNClob(n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public NClob getNClob(final String s) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getNClob(s);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setSQLXML(final String s, final SQLXML sqlxml) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setSQLXML(s, sqlxml);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public SQLXML getSQLXML(final int n) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getSQLXML(n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public SQLXML getSQLXML(final String s) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getSQLXML(s);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public String getNString(final int n) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getNString(n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public String getNString(final String s) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getNString(s);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public Reader getNCharacterStream(final int n) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getNCharacterStream(n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public Reader getNCharacterStream(final String s) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getNCharacterStream(s);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public Reader getCharacterStream(final int n) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getCharacterStream(n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public Reader getCharacterStream(final String s) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getCharacterStream(s);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setBlob(final String s, final Blob blob) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setBlob(s, blob);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setClob(final String s, final Clob clob) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setClob(s, clob);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setAsciiStream(final String s, final InputStream inputStream, final long n) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setAsciiStream(s, inputStream, n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setBinaryStream(final String s, final InputStream inputStream, final long n) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setBinaryStream(s, inputStream, n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setCharacterStream(final String s, final Reader reader, final long n) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setCharacterStream(s, reader, n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setAsciiStream(final String s, final InputStream inputStream) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setAsciiStream(s, inputStream);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setBinaryStream(final String s, final InputStream inputStream) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setBinaryStream(s, inputStream);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setCharacterStream(final String s, final Reader reader) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setCharacterStream(s, reader);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setNCharacterStream(final String s, final Reader reader) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setNCharacterStream(s, reader);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setClob(final String s, final Reader reader) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setClob(s, reader);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setBlob(final String s, final InputStream inputStream) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setBlob(s, inputStream);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setNClob(final String s, final Reader reader) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setNClob(s, reader);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public Object getObject(final int n, final Class clazz) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getObject(n, clazz);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public Object getObject(final String s, final Class clazz) throws SQLException {
        try {
            return ((CallableStatement)super.delegate).getObject(s, clazz);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setObject(final String s, final Object o, final SQLType sqlType, final int n) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setObject(s, o, sqlType, n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void setObject(final String s, final Object o, final SQLType sqlType) throws SQLException {
        try {
            ((CallableStatement)super.delegate).setObject(s, o, sqlType);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void registerOutParameter(final int n, final SQLType sqlType) throws SQLException {
        try {
            ((CallableStatement)super.delegate).registerOutParameter(n, sqlType);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void registerOutParameter(final int n, final SQLType sqlType, final int n2) throws SQLException {
        try {
            ((CallableStatement)super.delegate).registerOutParameter(n, sqlType, n2);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void registerOutParameter(final int n, final SQLType sqlType, final String s) throws SQLException {
        try {
            ((CallableStatement)super.delegate).registerOutParameter(n, sqlType, s);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void registerOutParameter(final String s, final SQLType sqlType) throws SQLException {
        try {
            ((CallableStatement)super.delegate).registerOutParameter(s, sqlType);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void registerOutParameter(final String s, final SQLType sqlType, final int n) throws SQLException {
        try {
            ((CallableStatement)super.delegate).registerOutParameter(s, sqlType, n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void registerOutParameter(final String s, final SQLType sqlType, final String s2) throws SQLException {
        try {
            ((CallableStatement)super.delegate).registerOutParameter(s, sqlType, s2);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    protected HikariProxyCallableStatement(final ProxyConnection connection, final CallableStatement statement) {
        super(connection, statement);
    }
}
