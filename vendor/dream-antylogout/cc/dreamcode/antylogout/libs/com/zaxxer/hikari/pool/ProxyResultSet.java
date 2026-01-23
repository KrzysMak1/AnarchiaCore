package cc.dreamcode.antylogout.libs.com.zaxxer.hikari.pool;

import java.sql.Statement;
import java.sql.SQLException;
import java.sql.ResultSet;

public abstract class ProxyResultSet implements ResultSet
{
    protected final ProxyConnection connection;
    protected final ProxyStatement statement;
    final ResultSet delegate;
    
    protected ProxyResultSet(final ProxyConnection connection, final ProxyStatement statement, final ResultSet resultSet) {
        this.connection = connection;
        this.statement = statement;
        this.delegate = resultSet;
    }
    
    final SQLException checkException(final SQLException e) {
        return this.connection.checkException(e);
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "@" + System.identityHashCode((Object)this) + " wrapping " + this.delegate;
    }
    
    public final Statement getStatement() throws SQLException {
        return (Statement)this.statement;
    }
    
    public void updateRow() throws SQLException {
        this.connection.markCommitStateDirty();
        this.delegate.updateRow();
    }
    
    public void insertRow() throws SQLException {
        this.connection.markCommitStateDirty();
        this.delegate.insertRow();
    }
    
    public void deleteRow() throws SQLException {
        this.connection.markCommitStateDirty();
        this.delegate.deleteRow();
    }
    
    public final <T> T unwrap(final Class<T> iface) throws SQLException {
        if (iface.isInstance(this.delegate)) {
            return (T)this.delegate;
        }
        if (this.delegate != null) {
            return (T)this.delegate.unwrap((Class)iface);
        }
        throw new SQLException("Wrapped ResultSet is not an instance of " + iface);
    }
}
