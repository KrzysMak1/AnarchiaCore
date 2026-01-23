package cc.dreamcode.antylogout.libs.com.zaxxer.hikari.pool;

import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DatabaseMetaData;

public abstract class ProxyDatabaseMetaData implements DatabaseMetaData
{
    protected final ProxyConnection connection;
    protected final DatabaseMetaData delegate;
    
    ProxyDatabaseMetaData(final ProxyConnection connection, final DatabaseMetaData metaData) {
        this.connection = connection;
        this.delegate = metaData;
    }
    
    final SQLException checkException(final SQLException e) {
        return this.connection.checkException(e);
    }
    
    @Override
    public final String toString() {
        final String delegateToString = this.delegate.toString();
        return this.getClass().getSimpleName() + "@" + System.identityHashCode((Object)this) + " wrapping " + delegateToString;
    }
    
    public final Connection getConnection() {
        return (Connection)this.connection;
    }
    
    public ResultSet getProcedures(final String catalog, final String schemaPattern, final String procedureNamePattern) throws SQLException {
        final ResultSet resultSet = this.delegate.getProcedures(catalog, schemaPattern, procedureNamePattern);
        Statement statement = resultSet.getStatement();
        if (statement != null) {
            statement = ProxyFactory.getProxyStatement(this.connection, statement);
        }
        return ProxyFactory.getProxyResultSet(this.connection, (ProxyStatement)statement, resultSet);
    }
    
    public ResultSet getProcedureColumns(final String catalog, final String schemaPattern, final String procedureNamePattern, final String columnNamePattern) throws SQLException {
        final ResultSet resultSet = this.delegate.getProcedureColumns(catalog, schemaPattern, procedureNamePattern, columnNamePattern);
        Statement statement = resultSet.getStatement();
        if (statement != null) {
            statement = ProxyFactory.getProxyStatement(this.connection, statement);
        }
        return ProxyFactory.getProxyResultSet(this.connection, (ProxyStatement)statement, resultSet);
    }
    
    public ResultSet getTables(final String catalog, final String schemaPattern, final String tableNamePattern, final String[] types) throws SQLException {
        final ResultSet resultSet = this.delegate.getTables(catalog, schemaPattern, tableNamePattern, types);
        Statement statement = resultSet.getStatement();
        if (statement != null) {
            statement = ProxyFactory.getProxyStatement(this.connection, statement);
        }
        return ProxyFactory.getProxyResultSet(this.connection, (ProxyStatement)statement, resultSet);
    }
    
    public ResultSet getSchemas() throws SQLException {
        final ResultSet resultSet = this.delegate.getSchemas();
        Statement statement = resultSet.getStatement();
        if (statement != null) {
            statement = ProxyFactory.getProxyStatement(this.connection, statement);
        }
        return ProxyFactory.getProxyResultSet(this.connection, (ProxyStatement)statement, resultSet);
    }
    
    public ResultSet getCatalogs() throws SQLException {
        final ResultSet resultSet = this.delegate.getCatalogs();
        Statement statement = resultSet.getStatement();
        if (statement != null) {
            statement = ProxyFactory.getProxyStatement(this.connection, statement);
        }
        return ProxyFactory.getProxyResultSet(this.connection, (ProxyStatement)statement, resultSet);
    }
    
    public ResultSet getTableTypes() throws SQLException {
        final ResultSet resultSet = this.delegate.getTableTypes();
        Statement statement = resultSet.getStatement();
        if (statement != null) {
            statement = ProxyFactory.getProxyStatement(this.connection, statement);
        }
        return ProxyFactory.getProxyResultSet(this.connection, (ProxyStatement)statement, resultSet);
    }
    
    public ResultSet getColumns(final String catalog, final String schemaPattern, final String tableNamePattern, final String columnNamePattern) throws SQLException {
        final ResultSet resultSet = this.delegate.getColumns(catalog, schemaPattern, tableNamePattern, columnNamePattern);
        Statement statement = resultSet.getStatement();
        if (statement != null) {
            statement = ProxyFactory.getProxyStatement(this.connection, statement);
        }
        return ProxyFactory.getProxyResultSet(this.connection, (ProxyStatement)statement, resultSet);
    }
    
    public ResultSet getColumnPrivileges(final String catalog, final String schema, final String table, final String columnNamePattern) throws SQLException {
        final ResultSet resultSet = this.delegate.getColumnPrivileges(catalog, schema, table, columnNamePattern);
        Statement statement = resultSet.getStatement();
        if (statement != null) {
            statement = ProxyFactory.getProxyStatement(this.connection, statement);
        }
        return ProxyFactory.getProxyResultSet(this.connection, (ProxyStatement)statement, resultSet);
    }
    
    public ResultSet getTablePrivileges(final String catalog, final String schemaPattern, final String tableNamePattern) throws SQLException {
        final ResultSet resultSet = this.delegate.getTablePrivileges(catalog, schemaPattern, tableNamePattern);
        Statement statement = resultSet.getStatement();
        if (statement != null) {
            statement = ProxyFactory.getProxyStatement(this.connection, statement);
        }
        return ProxyFactory.getProxyResultSet(this.connection, (ProxyStatement)statement, resultSet);
    }
    
    public ResultSet getBestRowIdentifier(final String catalog, final String schema, final String table, final int scope, final boolean nullable) throws SQLException {
        final ResultSet resultSet = this.delegate.getBestRowIdentifier(catalog, schema, table, scope, nullable);
        Statement statement = resultSet.getStatement();
        if (statement != null) {
            statement = ProxyFactory.getProxyStatement(this.connection, statement);
        }
        return ProxyFactory.getProxyResultSet(this.connection, (ProxyStatement)statement, resultSet);
    }
    
    public ResultSet getVersionColumns(final String catalog, final String schema, final String table) throws SQLException {
        final ResultSet resultSet = this.delegate.getVersionColumns(catalog, schema, table);
        Statement statement = resultSet.getStatement();
        if (statement != null) {
            statement = ProxyFactory.getProxyStatement(this.connection, statement);
        }
        return ProxyFactory.getProxyResultSet(this.connection, (ProxyStatement)statement, resultSet);
    }
    
    public ResultSet getPrimaryKeys(final String catalog, final String schema, final String table) throws SQLException {
        final ResultSet resultSet = this.delegate.getPrimaryKeys(catalog, schema, table);
        Statement statement = resultSet.getStatement();
        if (statement != null) {
            statement = ProxyFactory.getProxyStatement(this.connection, statement);
        }
        return ProxyFactory.getProxyResultSet(this.connection, (ProxyStatement)statement, resultSet);
    }
    
    public ResultSet getImportedKeys(final String catalog, final String schema, final String table) throws SQLException {
        final ResultSet resultSet = this.delegate.getImportedKeys(catalog, schema, table);
        Statement statement = resultSet.getStatement();
        if (statement != null) {
            statement = ProxyFactory.getProxyStatement(this.connection, statement);
        }
        return ProxyFactory.getProxyResultSet(this.connection, (ProxyStatement)statement, resultSet);
    }
    
    public ResultSet getExportedKeys(final String catalog, final String schema, final String table) throws SQLException {
        final ResultSet resultSet = this.delegate.getExportedKeys(catalog, schema, table);
        Statement statement = resultSet.getStatement();
        if (statement != null) {
            statement = ProxyFactory.getProxyStatement(this.connection, statement);
        }
        return ProxyFactory.getProxyResultSet(this.connection, (ProxyStatement)statement, resultSet);
    }
    
    public ResultSet getCrossReference(final String parentCatalog, final String parentSchema, final String parentTable, final String foreignCatalog, final String foreignSchema, final String foreignTable) throws SQLException {
        final ResultSet resultSet = this.delegate.getCrossReference(parentCatalog, parentSchema, parentTable, foreignCatalog, foreignSchema, foreignTable);
        Statement statement = resultSet.getStatement();
        if (statement != null) {
            statement = ProxyFactory.getProxyStatement(this.connection, statement);
        }
        return ProxyFactory.getProxyResultSet(this.connection, (ProxyStatement)statement, resultSet);
    }
    
    public ResultSet getTypeInfo() throws SQLException {
        final ResultSet resultSet = this.delegate.getTypeInfo();
        Statement statement = resultSet.getStatement();
        if (statement != null) {
            statement = ProxyFactory.getProxyStatement(this.connection, statement);
        }
        return ProxyFactory.getProxyResultSet(this.connection, (ProxyStatement)statement, resultSet);
    }
    
    public ResultSet getIndexInfo(final String catalog, final String schema, final String table, final boolean unique, final boolean approximate) throws SQLException {
        final ResultSet resultSet = this.delegate.getIndexInfo(catalog, schema, table, unique, approximate);
        Statement statement = resultSet.getStatement();
        if (statement != null) {
            statement = ProxyFactory.getProxyStatement(this.connection, statement);
        }
        return ProxyFactory.getProxyResultSet(this.connection, (ProxyStatement)statement, resultSet);
    }
    
    public ResultSet getUDTs(final String catalog, final String schemaPattern, final String typeNamePattern, final int[] types) throws SQLException {
        final ResultSet resultSet = this.delegate.getUDTs(catalog, schemaPattern, typeNamePattern, types);
        Statement statement = resultSet.getStatement();
        if (statement != null) {
            statement = ProxyFactory.getProxyStatement(this.connection, statement);
        }
        return ProxyFactory.getProxyResultSet(this.connection, (ProxyStatement)statement, resultSet);
    }
    
    public ResultSet getSuperTypes(final String catalog, final String schemaPattern, final String typeNamePattern) throws SQLException {
        final ResultSet resultSet = this.delegate.getSuperTypes(catalog, schemaPattern, typeNamePattern);
        Statement statement = resultSet.getStatement();
        if (statement != null) {
            statement = ProxyFactory.getProxyStatement(this.connection, statement);
        }
        return ProxyFactory.getProxyResultSet(this.connection, (ProxyStatement)statement, resultSet);
    }
    
    public ResultSet getSuperTables(final String catalog, final String schemaPattern, final String tableNamePattern) throws SQLException {
        final ResultSet resultSet = this.delegate.getSuperTables(catalog, schemaPattern, tableNamePattern);
        Statement statement = resultSet.getStatement();
        if (statement != null) {
            statement = ProxyFactory.getProxyStatement(this.connection, statement);
        }
        return ProxyFactory.getProxyResultSet(this.connection, (ProxyStatement)statement, resultSet);
    }
    
    public ResultSet getAttributes(final String catalog, final String schemaPattern, final String typeNamePattern, final String attributeNamePattern) throws SQLException {
        final ResultSet resultSet = this.delegate.getAttributes(catalog, schemaPattern, typeNamePattern, attributeNamePattern);
        Statement statement = resultSet.getStatement();
        if (statement != null) {
            statement = ProxyFactory.getProxyStatement(this.connection, statement);
        }
        return ProxyFactory.getProxyResultSet(this.connection, (ProxyStatement)statement, resultSet);
    }
    
    public ResultSet getSchemas(final String catalog, final String schemaPattern) throws SQLException {
        final ResultSet resultSet = this.delegate.getSchemas(catalog, schemaPattern);
        Statement statement = resultSet.getStatement();
        if (statement != null) {
            statement = ProxyFactory.getProxyStatement(this.connection, statement);
        }
        return ProxyFactory.getProxyResultSet(this.connection, (ProxyStatement)statement, resultSet);
    }
    
    public ResultSet getClientInfoProperties() throws SQLException {
        final ResultSet resultSet = this.delegate.getClientInfoProperties();
        Statement statement = resultSet.getStatement();
        if (statement != null) {
            statement = ProxyFactory.getProxyStatement(this.connection, statement);
        }
        return ProxyFactory.getProxyResultSet(this.connection, (ProxyStatement)statement, resultSet);
    }
    
    public ResultSet getFunctions(final String catalog, final String schemaPattern, final String functionNamePattern) throws SQLException {
        final ResultSet resultSet = this.delegate.getFunctions(catalog, schemaPattern, functionNamePattern);
        Statement statement = resultSet.getStatement();
        if (statement != null) {
            statement = ProxyFactory.getProxyStatement(this.connection, statement);
        }
        return ProxyFactory.getProxyResultSet(this.connection, (ProxyStatement)statement, resultSet);
    }
    
    public ResultSet getFunctionColumns(final String catalog, final String schemaPattern, final String functionNamePattern, final String columnNamePattern) throws SQLException {
        final ResultSet resultSet = this.delegate.getFunctionColumns(catalog, schemaPattern, functionNamePattern, columnNamePattern);
        Statement statement = resultSet.getStatement();
        if (statement != null) {
            statement = ProxyFactory.getProxyStatement(this.connection, statement);
        }
        return ProxyFactory.getProxyResultSet(this.connection, (ProxyStatement)statement, resultSet);
    }
    
    public ResultSet getPseudoColumns(final String catalog, final String schemaPattern, final String tableNamePattern, final String columnNamePattern) throws SQLException {
        final ResultSet resultSet = this.delegate.getPseudoColumns(catalog, schemaPattern, tableNamePattern, columnNamePattern);
        Statement statement = resultSet.getStatement();
        if (statement != null) {
            statement = ProxyFactory.getProxyStatement(this.connection, statement);
        }
        return ProxyFactory.getProxyResultSet(this.connection, (ProxyStatement)statement, resultSet);
    }
    
    public final <T> T unwrap(final Class<T> iface) throws SQLException {
        if (iface.isInstance(this.delegate)) {
            return (T)this.delegate;
        }
        if (this.delegate != null) {
            return (T)this.delegate.unwrap((Class)iface);
        }
        throw new SQLException("Wrapped DatabaseMetaData is not an instance of " + iface);
    }
}
