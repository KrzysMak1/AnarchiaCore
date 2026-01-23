package cc.dreamcode.antylogout.libs.com.zaxxer.hikari.pool;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;

public abstract class ProxyPreparedStatement extends ProxyStatement implements PreparedStatement
{
    ProxyPreparedStatement(final ProxyConnection connection, final PreparedStatement statement) {
        super(connection, (Statement)statement);
    }
    
    public boolean execute() throws SQLException {
        this.connection.markCommitStateDirty();
        return ((PreparedStatement)this.delegate).execute();
    }
    
    public ResultSet executeQuery() throws SQLException {
        this.connection.markCommitStateDirty();
        final ResultSet resultSet = ((PreparedStatement)this.delegate).executeQuery();
        return ProxyFactory.getProxyResultSet(this.connection, this, resultSet);
    }
    
    public int executeUpdate() throws SQLException {
        this.connection.markCommitStateDirty();
        return ((PreparedStatement)this.delegate).executeUpdate();
    }
    
    public long executeLargeUpdate() throws SQLException {
        this.connection.markCommitStateDirty();
        return ((PreparedStatement)this.delegate).executeLargeUpdate();
    }
}
