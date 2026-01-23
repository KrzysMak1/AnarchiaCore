package cc.dreamcode.antylogout.libs.com.zaxxer.hikari.pool;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.CallableStatement;
import java.sql.Statement;
import cc.dreamcode.antylogout.libs.com.zaxxer.hikari.util.FastList;
import java.sql.Connection;

public final class ProxyFactory
{
    private ProxyFactory() {
    }
    
    static ProxyConnection getProxyConnection(final PoolEntry poolEntry, final Connection connection, final FastList<Statement> list, final ProxyLeakTask proxyLeakTask, final boolean b, final boolean b2) {
        return new HikariProxyConnection(poolEntry, connection, list, proxyLeakTask, b, b2);
    }
    
    static Statement getProxyStatement(final ProxyConnection proxyConnection, final Statement statement) {
        return (Statement)new HikariProxyStatement(proxyConnection, statement);
    }
    
    static CallableStatement getProxyCallableStatement(final ProxyConnection proxyConnection, final CallableStatement callableStatement) {
        return (CallableStatement)new HikariProxyCallableStatement(proxyConnection, callableStatement);
    }
    
    static PreparedStatement getProxyPreparedStatement(final ProxyConnection proxyConnection, final PreparedStatement preparedStatement) {
        return (PreparedStatement)new HikariProxyPreparedStatement(proxyConnection, preparedStatement);
    }
    
    static ResultSet getProxyResultSet(final ProxyConnection proxyConnection, final ProxyStatement proxyStatement, final ResultSet set) {
        return (ResultSet)new HikariProxyResultSet(proxyConnection, proxyStatement, set);
    }
    
    static DatabaseMetaData getProxyDatabaseMetaData(final ProxyConnection proxyConnection, final DatabaseMetaData databaseMetaData) {
        return (DatabaseMetaData)new HikariProxyDatabaseMetaData(proxyConnection, databaseMetaData);
    }
}
