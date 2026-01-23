package cc.dreamcode.antylogout.libs.com.zaxxer.hikari.pool;

import java.sql.SQLType;
import java.sql.SQLXML;
import java.sql.NClob;
import java.sql.RowId;
import java.net.URL;
import java.util.Calendar;
import java.sql.Array;
import java.sql.Clob;
import java.sql.Blob;
import java.sql.Ref;
import java.util.Map;
import java.io.Reader;
import java.sql.ResultSetMetaData;
import java.sql.SQLWarning;
import java.io.InputStream;
import java.sql.Timestamp;
import java.sql.Time;
import java.sql.Date;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Wrapper;

public final class HikariProxyResultSet extends ProxyResultSet implements Wrapper, AutoCloseable, ResultSet
{
    public boolean isWrapperFor(final Class clazz) throws SQLException {
        try {
            return ((Wrapper)super.delegate).isWrapperFor(clazz);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void close() throws Exception {
        super.delegate.close();
    }
    
    public boolean next() throws SQLException {
        try {
            return super.delegate.next();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean wasNull() throws SQLException {
        try {
            return super.delegate.wasNull();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public String getString(final int n) throws SQLException {
        try {
            return super.delegate.getString(n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean getBoolean(final int n) throws SQLException {
        try {
            return super.delegate.getBoolean(n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public byte getByte(final int n) throws SQLException {
        try {
            return super.delegate.getByte(n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public short getShort(final int n) throws SQLException {
        try {
            return super.delegate.getShort(n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public int getInt(final int n) throws SQLException {
        try {
            return super.delegate.getInt(n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public long getLong(final int n) throws SQLException {
        try {
            return super.delegate.getLong(n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public float getFloat(final int n) throws SQLException {
        try {
            return super.delegate.getFloat(n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public double getDouble(final int n) throws SQLException {
        try {
            return super.delegate.getDouble(n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public BigDecimal getBigDecimal(final int n, final int n2) throws SQLException {
        try {
            return super.delegate.getBigDecimal(n, n2);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public byte[] getBytes(final int n) throws SQLException {
        try {
            return super.delegate.getBytes(n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public Date getDate(final int n) throws SQLException {
        try {
            return super.delegate.getDate(n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public Time getTime(final int n) throws SQLException {
        try {
            return super.delegate.getTime(n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public Timestamp getTimestamp(final int n) throws SQLException {
        try {
            return super.delegate.getTimestamp(n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public InputStream getAsciiStream(final int n) throws SQLException {
        try {
            return super.delegate.getAsciiStream(n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public InputStream getUnicodeStream(final int n) throws SQLException {
        try {
            return super.delegate.getUnicodeStream(n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public InputStream getBinaryStream(final int n) throws SQLException {
        try {
            return super.delegate.getBinaryStream(n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public String getString(final String s) throws SQLException {
        try {
            return super.delegate.getString(s);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean getBoolean(final String s) throws SQLException {
        try {
            return super.delegate.getBoolean(s);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public byte getByte(final String s) throws SQLException {
        try {
            return super.delegate.getByte(s);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public short getShort(final String s) throws SQLException {
        try {
            return super.delegate.getShort(s);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public int getInt(final String s) throws SQLException {
        try {
            return super.delegate.getInt(s);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public long getLong(final String s) throws SQLException {
        try {
            return super.delegate.getLong(s);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public float getFloat(final String s) throws SQLException {
        try {
            return super.delegate.getFloat(s);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public double getDouble(final String s) throws SQLException {
        try {
            return super.delegate.getDouble(s);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public BigDecimal getBigDecimal(final String s, final int n) throws SQLException {
        try {
            return super.delegate.getBigDecimal(s, n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public byte[] getBytes(final String s) throws SQLException {
        try {
            return super.delegate.getBytes(s);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public Date getDate(final String s) throws SQLException {
        try {
            return super.delegate.getDate(s);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public Time getTime(final String s) throws SQLException {
        try {
            return super.delegate.getTime(s);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public Timestamp getTimestamp(final String s) throws SQLException {
        try {
            return super.delegate.getTimestamp(s);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public InputStream getAsciiStream(final String s) throws SQLException {
        try {
            return super.delegate.getAsciiStream(s);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public InputStream getUnicodeStream(final String s) throws SQLException {
        try {
            return super.delegate.getUnicodeStream(s);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public InputStream getBinaryStream(final String s) throws SQLException {
        try {
            return super.delegate.getBinaryStream(s);
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
    
    public String getCursorName() throws SQLException {
        try {
            return super.delegate.getCursorName();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public ResultSetMetaData getMetaData() throws SQLException {
        try {
            return super.delegate.getMetaData();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public Object getObject(final int n) throws SQLException {
        try {
            return super.delegate.getObject(n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public Object getObject(final String s) throws SQLException {
        try {
            return super.delegate.getObject(s);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public int findColumn(final String s) throws SQLException {
        try {
            return super.delegate.findColumn(s);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public Reader getCharacterStream(final int n) throws SQLException {
        try {
            return super.delegate.getCharacterStream(n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public Reader getCharacterStream(final String s) throws SQLException {
        try {
            return super.delegate.getCharacterStream(s);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public BigDecimal getBigDecimal(final int n) throws SQLException {
        try {
            return super.delegate.getBigDecimal(n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public BigDecimal getBigDecimal(final String s) throws SQLException {
        try {
            return super.delegate.getBigDecimal(s);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean isBeforeFirst() throws SQLException {
        try {
            return super.delegate.isBeforeFirst();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean isAfterLast() throws SQLException {
        try {
            return super.delegate.isAfterLast();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean isFirst() throws SQLException {
        try {
            return super.delegate.isFirst();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean isLast() throws SQLException {
        try {
            return super.delegate.isLast();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void beforeFirst() throws SQLException {
        try {
            super.delegate.beforeFirst();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void afterLast() throws SQLException {
        try {
            super.delegate.afterLast();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean first() throws SQLException {
        try {
            return super.delegate.first();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean last() throws SQLException {
        try {
            return super.delegate.last();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public int getRow() throws SQLException {
        try {
            return super.delegate.getRow();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean absolute(final int n) throws SQLException {
        try {
            return super.delegate.absolute(n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean relative(final int n) throws SQLException {
        try {
            return super.delegate.relative(n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean previous() throws SQLException {
        try {
            return super.delegate.previous();
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
    
    public int getType() throws SQLException {
        try {
            return super.delegate.getType();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public int getConcurrency() throws SQLException {
        try {
            return super.delegate.getConcurrency();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean rowUpdated() throws SQLException {
        try {
            return super.delegate.rowUpdated();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean rowInserted() throws SQLException {
        try {
            return super.delegate.rowInserted();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean rowDeleted() throws SQLException {
        try {
            return super.delegate.rowDeleted();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void updateNull(final int n) throws SQLException {
        try {
            super.delegate.updateNull(n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void updateBoolean(final int n, final boolean b) throws SQLException {
        try {
            super.delegate.updateBoolean(n, b);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void updateByte(final int n, final byte b) throws SQLException {
        try {
            super.delegate.updateByte(n, b);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void updateShort(final int n, final short n2) throws SQLException {
        try {
            super.delegate.updateShort(n, n2);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void updateInt(final int n, final int n2) throws SQLException {
        try {
            super.delegate.updateInt(n, n2);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void updateLong(final int n, final long n2) throws SQLException {
        try {
            super.delegate.updateLong(n, n2);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void updateFloat(final int n, final float n2) throws SQLException {
        try {
            super.delegate.updateFloat(n, n2);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void updateDouble(final int n, final double n2) throws SQLException {
        try {
            super.delegate.updateDouble(n, n2);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void updateBigDecimal(final int n, final BigDecimal bigDecimal) throws SQLException {
        try {
            super.delegate.updateBigDecimal(n, bigDecimal);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void updateString(final int n, final String s) throws SQLException {
        try {
            super.delegate.updateString(n, s);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void updateBytes(final int n, final byte[] array) throws SQLException {
        try {
            super.delegate.updateBytes(n, array);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void updateDate(final int n, final Date date) throws SQLException {
        try {
            super.delegate.updateDate(n, date);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void updateTime(final int n, final Time time) throws SQLException {
        try {
            super.delegate.updateTime(n, time);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void updateTimestamp(final int n, final Timestamp timestamp) throws SQLException {
        try {
            super.delegate.updateTimestamp(n, timestamp);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void updateAsciiStream(final int n, final InputStream inputStream, final int n2) throws SQLException {
        try {
            super.delegate.updateAsciiStream(n, inputStream, n2);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void updateBinaryStream(final int n, final InputStream inputStream, final int n2) throws SQLException {
        try {
            super.delegate.updateBinaryStream(n, inputStream, n2);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void updateCharacterStream(final int n, final Reader reader, final int n2) throws SQLException {
        try {
            super.delegate.updateCharacterStream(n, reader, n2);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void updateObject(final int n, final Object o, final int n2) throws SQLException {
        try {
            super.delegate.updateObject(n, o, n2);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void updateObject(final int n, final Object o) throws SQLException {
        try {
            super.delegate.updateObject(n, o);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void updateNull(final String s) throws SQLException {
        try {
            super.delegate.updateNull(s);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void updateBoolean(final String s, final boolean b) throws SQLException {
        try {
            super.delegate.updateBoolean(s, b);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void updateByte(final String s, final byte b) throws SQLException {
        try {
            super.delegate.updateByte(s, b);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void updateShort(final String s, final short n) throws SQLException {
        try {
            super.delegate.updateShort(s, n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void updateInt(final String s, final int n) throws SQLException {
        try {
            super.delegate.updateInt(s, n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void updateLong(final String s, final long n) throws SQLException {
        try {
            super.delegate.updateLong(s, n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void updateFloat(final String s, final float n) throws SQLException {
        try {
            super.delegate.updateFloat(s, n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void updateDouble(final String s, final double n) throws SQLException {
        try {
            super.delegate.updateDouble(s, n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void updateBigDecimal(final String s, final BigDecimal bigDecimal) throws SQLException {
        try {
            super.delegate.updateBigDecimal(s, bigDecimal);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void updateString(final String s, final String s2) throws SQLException {
        try {
            super.delegate.updateString(s, s2);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void updateBytes(final String s, final byte[] array) throws SQLException {
        try {
            super.delegate.updateBytes(s, array);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void updateDate(final String s, final Date date) throws SQLException {
        try {
            super.delegate.updateDate(s, date);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void updateTime(final String s, final Time time) throws SQLException {
        try {
            super.delegate.updateTime(s, time);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void updateTimestamp(final String s, final Timestamp timestamp) throws SQLException {
        try {
            super.delegate.updateTimestamp(s, timestamp);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void updateAsciiStream(final String s, final InputStream inputStream, final int n) throws SQLException {
        try {
            super.delegate.updateAsciiStream(s, inputStream, n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void updateBinaryStream(final String s, final InputStream inputStream, final int n) throws SQLException {
        try {
            super.delegate.updateBinaryStream(s, inputStream, n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void updateCharacterStream(final String s, final Reader reader, final int n) throws SQLException {
        try {
            super.delegate.updateCharacterStream(s, reader, n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void updateObject(final String s, final Object o, final int n) throws SQLException {
        try {
            super.delegate.updateObject(s, o, n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void updateObject(final String s, final Object o) throws SQLException {
        try {
            super.delegate.updateObject(s, o);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void insertRow() throws SQLException {
        try {
            super.insertRow();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void updateRow() throws SQLException {
        try {
            super.updateRow();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public void deleteRow() throws SQLException {
        try {
            super.deleteRow();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void refreshRow() throws SQLException {
        try {
            super.delegate.refreshRow();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void cancelRowUpdates() throws SQLException {
        try {
            super.delegate.cancelRowUpdates();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void moveToInsertRow() throws SQLException {
        try {
            super.delegate.moveToInsertRow();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void moveToCurrentRow() throws SQLException {
        try {
            super.delegate.moveToCurrentRow();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public Object getObject(final int n, final Map map) throws SQLException {
        try {
            return super.delegate.getObject(n, map);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public Ref getRef(final int n) throws SQLException {
        try {
            return super.delegate.getRef(n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public Blob getBlob(final int n) throws SQLException {
        try {
            return super.delegate.getBlob(n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public Clob getClob(final int n) throws SQLException {
        try {
            return super.delegate.getClob(n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public Array getArray(final int n) throws SQLException {
        try {
            return super.delegate.getArray(n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public Object getObject(final String s, final Map map) throws SQLException {
        try {
            return super.delegate.getObject(s, map);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public Ref getRef(final String s) throws SQLException {
        try {
            return super.delegate.getRef(s);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public Blob getBlob(final String s) throws SQLException {
        try {
            return super.delegate.getBlob(s);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public Clob getClob(final String s) throws SQLException {
        try {
            return super.delegate.getClob(s);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public Array getArray(final String s) throws SQLException {
        try {
            return super.delegate.getArray(s);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public Date getDate(final int n, final Calendar calendar) throws SQLException {
        try {
            return super.delegate.getDate(n, calendar);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public Date getDate(final String s, final Calendar calendar) throws SQLException {
        try {
            return super.delegate.getDate(s, calendar);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public Time getTime(final int n, final Calendar calendar) throws SQLException {
        try {
            return super.delegate.getTime(n, calendar);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public Time getTime(final String s, final Calendar calendar) throws SQLException {
        try {
            return super.delegate.getTime(s, calendar);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public Timestamp getTimestamp(final int n, final Calendar calendar) throws SQLException {
        try {
            return super.delegate.getTimestamp(n, calendar);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public Timestamp getTimestamp(final String s, final Calendar calendar) throws SQLException {
        try {
            return super.delegate.getTimestamp(s, calendar);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public URL getURL(final int n) throws SQLException {
        try {
            return super.delegate.getURL(n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public URL getURL(final String s) throws SQLException {
        try {
            return super.delegate.getURL(s);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void updateRef(final int n, final Ref ref) throws SQLException {
        try {
            super.delegate.updateRef(n, ref);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void updateRef(final String s, final Ref ref) throws SQLException {
        try {
            super.delegate.updateRef(s, ref);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void updateBlob(final int n, final Blob blob) throws SQLException {
        try {
            super.delegate.updateBlob(n, blob);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void updateBlob(final String s, final Blob blob) throws SQLException {
        try {
            super.delegate.updateBlob(s, blob);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void updateClob(final int n, final Clob clob) throws SQLException {
        try {
            super.delegate.updateClob(n, clob);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void updateClob(final String s, final Clob clob) throws SQLException {
        try {
            super.delegate.updateClob(s, clob);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void updateArray(final int n, final Array array) throws SQLException {
        try {
            super.delegate.updateArray(n, array);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void updateArray(final String s, final Array array) throws SQLException {
        try {
            super.delegate.updateArray(s, array);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public RowId getRowId(final int n) throws SQLException {
        try {
            return super.delegate.getRowId(n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public RowId getRowId(final String s) throws SQLException {
        try {
            return super.delegate.getRowId(s);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void updateRowId(final int n, final RowId rowId) throws SQLException {
        try {
            super.delegate.updateRowId(n, rowId);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void updateRowId(final String s, final RowId rowId) throws SQLException {
        try {
            super.delegate.updateRowId(s, rowId);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public int getHoldability() throws SQLException {
        try {
            return super.delegate.getHoldability();
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
    
    public void updateNString(final int n, final String s) throws SQLException {
        try {
            super.delegate.updateNString(n, s);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void updateNString(final String s, final String s2) throws SQLException {
        try {
            super.delegate.updateNString(s, s2);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void updateNClob(final int n, final NClob nClob) throws SQLException {
        try {
            super.delegate.updateNClob(n, nClob);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void updateNClob(final String s, final NClob nClob) throws SQLException {
        try {
            super.delegate.updateNClob(s, nClob);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public NClob getNClob(final int n) throws SQLException {
        try {
            return super.delegate.getNClob(n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public NClob getNClob(final String s) throws SQLException {
        try {
            return super.delegate.getNClob(s);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public SQLXML getSQLXML(final int n) throws SQLException {
        try {
            return super.delegate.getSQLXML(n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public SQLXML getSQLXML(final String s) throws SQLException {
        try {
            return super.delegate.getSQLXML(s);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void updateSQLXML(final int n, final SQLXML sqlxml) throws SQLException {
        try {
            super.delegate.updateSQLXML(n, sqlxml);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void updateSQLXML(final String s, final SQLXML sqlxml) throws SQLException {
        try {
            super.delegate.updateSQLXML(s, sqlxml);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public String getNString(final int n) throws SQLException {
        try {
            return super.delegate.getNString(n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public String getNString(final String s) throws SQLException {
        try {
            return super.delegate.getNString(s);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public Reader getNCharacterStream(final int n) throws SQLException {
        try {
            return super.delegate.getNCharacterStream(n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public Reader getNCharacterStream(final String s) throws SQLException {
        try {
            return super.delegate.getNCharacterStream(s);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void updateNCharacterStream(final int n, final Reader reader, final long n2) throws SQLException {
        try {
            super.delegate.updateNCharacterStream(n, reader, n2);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void updateNCharacterStream(final String s, final Reader reader, final long n) throws SQLException {
        try {
            super.delegate.updateNCharacterStream(s, reader, n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void updateAsciiStream(final int n, final InputStream inputStream, final long n2) throws SQLException {
        try {
            super.delegate.updateAsciiStream(n, inputStream, n2);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void updateBinaryStream(final int n, final InputStream inputStream, final long n2) throws SQLException {
        try {
            super.delegate.updateBinaryStream(n, inputStream, n2);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void updateCharacterStream(final int n, final Reader reader, final long n2) throws SQLException {
        try {
            super.delegate.updateCharacterStream(n, reader, n2);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void updateAsciiStream(final String s, final InputStream inputStream, final long n) throws SQLException {
        try {
            super.delegate.updateAsciiStream(s, inputStream, n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void updateBinaryStream(final String s, final InputStream inputStream, final long n) throws SQLException {
        try {
            super.delegate.updateBinaryStream(s, inputStream, n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void updateCharacterStream(final String s, final Reader reader, final long n) throws SQLException {
        try {
            super.delegate.updateCharacterStream(s, reader, n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void updateBlob(final int n, final InputStream inputStream, final long n2) throws SQLException {
        try {
            super.delegate.updateBlob(n, inputStream, n2);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void updateBlob(final String s, final InputStream inputStream, final long n) throws SQLException {
        try {
            super.delegate.updateBlob(s, inputStream, n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void updateClob(final int n, final Reader reader, final long n2) throws SQLException {
        try {
            super.delegate.updateClob(n, reader, n2);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void updateClob(final String s, final Reader reader, final long n) throws SQLException {
        try {
            super.delegate.updateClob(s, reader, n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void updateNClob(final int n, final Reader reader, final long n2) throws SQLException {
        try {
            super.delegate.updateNClob(n, reader, n2);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void updateNClob(final String s, final Reader reader, final long n) throws SQLException {
        try {
            super.delegate.updateNClob(s, reader, n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void updateNCharacterStream(final int n, final Reader reader) throws SQLException {
        try {
            super.delegate.updateNCharacterStream(n, reader);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void updateNCharacterStream(final String s, final Reader reader) throws SQLException {
        try {
            super.delegate.updateNCharacterStream(s, reader);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void updateAsciiStream(final int n, final InputStream inputStream) throws SQLException {
        try {
            super.delegate.updateAsciiStream(n, inputStream);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void updateBinaryStream(final int n, final InputStream inputStream) throws SQLException {
        try {
            super.delegate.updateBinaryStream(n, inputStream);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void updateCharacterStream(final int n, final Reader reader) throws SQLException {
        try {
            super.delegate.updateCharacterStream(n, reader);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void updateAsciiStream(final String s, final InputStream inputStream) throws SQLException {
        try {
            super.delegate.updateAsciiStream(s, inputStream);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void updateBinaryStream(final String s, final InputStream inputStream) throws SQLException {
        try {
            super.delegate.updateBinaryStream(s, inputStream);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void updateCharacterStream(final String s, final Reader reader) throws SQLException {
        try {
            super.delegate.updateCharacterStream(s, reader);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void updateBlob(final int n, final InputStream inputStream) throws SQLException {
        try {
            super.delegate.updateBlob(n, inputStream);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void updateBlob(final String s, final InputStream inputStream) throws SQLException {
        try {
            super.delegate.updateBlob(s, inputStream);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void updateClob(final int n, final Reader reader) throws SQLException {
        try {
            super.delegate.updateClob(n, reader);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void updateClob(final String s, final Reader reader) throws SQLException {
        try {
            super.delegate.updateClob(s, reader);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void updateNClob(final int n, final Reader reader) throws SQLException {
        try {
            super.delegate.updateNClob(n, reader);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void updateNClob(final String s, final Reader reader) throws SQLException {
        try {
            super.delegate.updateNClob(s, reader);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public Object getObject(final int n, final Class clazz) throws SQLException {
        try {
            return super.delegate.getObject(n, clazz);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public Object getObject(final String s, final Class clazz) throws SQLException {
        try {
            return super.delegate.getObject(s, clazz);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void updateObject(final int n, final Object o, final SQLType sqlType, final int n2) throws SQLException {
        try {
            super.delegate.updateObject(n, o, sqlType, n2);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void updateObject(final String s, final Object o, final SQLType sqlType, final int n) throws SQLException {
        try {
            super.delegate.updateObject(s, o, sqlType, n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void updateObject(final int n, final Object o, final SQLType sqlType) throws SQLException {
        try {
            super.delegate.updateObject(n, o, sqlType);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public void updateObject(final String s, final Object o, final SQLType sqlType) throws SQLException {
        try {
            super.delegate.updateObject(s, o, sqlType);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    protected HikariProxyResultSet(final ProxyConnection connection, final ProxyStatement statement, final ResultSet resultSet) {
        super(connection, statement, resultSet);
    }
}
