package cc.dreamcode.antylogout.libs.com.zaxxer.hikari.pool;

import java.sql.RowIdLifetime;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.DatabaseMetaData;
import java.sql.Wrapper;

public final class HikariProxyDatabaseMetaData extends ProxyDatabaseMetaData implements Wrapper, DatabaseMetaData
{
    public boolean isWrapperFor(final Class clazz) throws SQLException {
        try {
            return ((Wrapper)super.delegate).isWrapperFor(clazz);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean allProceduresAreCallable() throws SQLException {
        try {
            return super.delegate.allProceduresAreCallable();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean allTablesAreSelectable() throws SQLException {
        try {
            return super.delegate.allTablesAreSelectable();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public String getURL() throws SQLException {
        try {
            return super.delegate.getURL();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public String getUserName() throws SQLException {
        try {
            return super.delegate.getUserName();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean isReadOnly() throws SQLException {
        try {
            return super.delegate.isReadOnly();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean nullsAreSortedHigh() throws SQLException {
        try {
            return super.delegate.nullsAreSortedHigh();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean nullsAreSortedLow() throws SQLException {
        try {
            return super.delegate.nullsAreSortedLow();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean nullsAreSortedAtStart() throws SQLException {
        try {
            return super.delegate.nullsAreSortedAtStart();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean nullsAreSortedAtEnd() throws SQLException {
        try {
            return super.delegate.nullsAreSortedAtEnd();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public String getDatabaseProductName() throws SQLException {
        try {
            return super.delegate.getDatabaseProductName();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public String getDatabaseProductVersion() throws SQLException {
        try {
            return super.delegate.getDatabaseProductVersion();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public String getDriverName() throws SQLException {
        try {
            return super.delegate.getDriverName();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public String getDriverVersion() throws SQLException {
        try {
            return super.delegate.getDriverVersion();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public int getDriverMajorVersion() {
        return super.delegate.getDriverMajorVersion();
    }
    
    public int getDriverMinorVersion() {
        return super.delegate.getDriverMinorVersion();
    }
    
    public boolean usesLocalFiles() throws SQLException {
        try {
            return super.delegate.usesLocalFiles();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean usesLocalFilePerTable() throws SQLException {
        try {
            return super.delegate.usesLocalFilePerTable();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean supportsMixedCaseIdentifiers() throws SQLException {
        try {
            return super.delegate.supportsMixedCaseIdentifiers();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean storesUpperCaseIdentifiers() throws SQLException {
        try {
            return super.delegate.storesUpperCaseIdentifiers();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean storesLowerCaseIdentifiers() throws SQLException {
        try {
            return super.delegate.storesLowerCaseIdentifiers();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean storesMixedCaseIdentifiers() throws SQLException {
        try {
            return super.delegate.storesMixedCaseIdentifiers();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean supportsMixedCaseQuotedIdentifiers() throws SQLException {
        try {
            return super.delegate.supportsMixedCaseQuotedIdentifiers();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean storesUpperCaseQuotedIdentifiers() throws SQLException {
        try {
            return super.delegate.storesUpperCaseQuotedIdentifiers();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean storesLowerCaseQuotedIdentifiers() throws SQLException {
        try {
            return super.delegate.storesLowerCaseQuotedIdentifiers();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean storesMixedCaseQuotedIdentifiers() throws SQLException {
        try {
            return super.delegate.storesMixedCaseQuotedIdentifiers();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public String getIdentifierQuoteString() throws SQLException {
        try {
            return super.delegate.getIdentifierQuoteString();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public String getSQLKeywords() throws SQLException {
        try {
            return super.delegate.getSQLKeywords();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public String getNumericFunctions() throws SQLException {
        try {
            return super.delegate.getNumericFunctions();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public String getStringFunctions() throws SQLException {
        try {
            return super.delegate.getStringFunctions();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public String getSystemFunctions() throws SQLException {
        try {
            return super.delegate.getSystemFunctions();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public String getTimeDateFunctions() throws SQLException {
        try {
            return super.delegate.getTimeDateFunctions();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public String getSearchStringEscape() throws SQLException {
        try {
            return super.delegate.getSearchStringEscape();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public String getExtraNameCharacters() throws SQLException {
        try {
            return super.delegate.getExtraNameCharacters();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean supportsAlterTableWithAddColumn() throws SQLException {
        try {
            return super.delegate.supportsAlterTableWithAddColumn();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean supportsAlterTableWithDropColumn() throws SQLException {
        try {
            return super.delegate.supportsAlterTableWithDropColumn();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean supportsColumnAliasing() throws SQLException {
        try {
            return super.delegate.supportsColumnAliasing();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean nullPlusNonNullIsNull() throws SQLException {
        try {
            return super.delegate.nullPlusNonNullIsNull();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean supportsConvert() throws SQLException {
        try {
            return super.delegate.supportsConvert();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean supportsConvert(final int n, final int n2) throws SQLException {
        try {
            return super.delegate.supportsConvert(n, n2);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean supportsTableCorrelationNames() throws SQLException {
        try {
            return super.delegate.supportsTableCorrelationNames();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean supportsDifferentTableCorrelationNames() throws SQLException {
        try {
            return super.delegate.supportsDifferentTableCorrelationNames();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean supportsExpressionsInOrderBy() throws SQLException {
        try {
            return super.delegate.supportsExpressionsInOrderBy();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean supportsOrderByUnrelated() throws SQLException {
        try {
            return super.delegate.supportsOrderByUnrelated();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean supportsGroupBy() throws SQLException {
        try {
            return super.delegate.supportsGroupBy();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean supportsGroupByUnrelated() throws SQLException {
        try {
            return super.delegate.supportsGroupByUnrelated();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean supportsGroupByBeyondSelect() throws SQLException {
        try {
            return super.delegate.supportsGroupByBeyondSelect();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean supportsLikeEscapeClause() throws SQLException {
        try {
            return super.delegate.supportsLikeEscapeClause();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean supportsMultipleResultSets() throws SQLException {
        try {
            return super.delegate.supportsMultipleResultSets();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean supportsMultipleTransactions() throws SQLException {
        try {
            return super.delegate.supportsMultipleTransactions();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean supportsNonNullableColumns() throws SQLException {
        try {
            return super.delegate.supportsNonNullableColumns();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean supportsMinimumSQLGrammar() throws SQLException {
        try {
            return super.delegate.supportsMinimumSQLGrammar();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean supportsCoreSQLGrammar() throws SQLException {
        try {
            return super.delegate.supportsCoreSQLGrammar();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean supportsExtendedSQLGrammar() throws SQLException {
        try {
            return super.delegate.supportsExtendedSQLGrammar();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean supportsANSI92EntryLevelSQL() throws SQLException {
        try {
            return super.delegate.supportsANSI92EntryLevelSQL();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean supportsANSI92IntermediateSQL() throws SQLException {
        try {
            return super.delegate.supportsANSI92IntermediateSQL();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean supportsANSI92FullSQL() throws SQLException {
        try {
            return super.delegate.supportsANSI92FullSQL();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean supportsIntegrityEnhancementFacility() throws SQLException {
        try {
            return super.delegate.supportsIntegrityEnhancementFacility();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean supportsOuterJoins() throws SQLException {
        try {
            return super.delegate.supportsOuterJoins();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean supportsFullOuterJoins() throws SQLException {
        try {
            return super.delegate.supportsFullOuterJoins();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean supportsLimitedOuterJoins() throws SQLException {
        try {
            return super.delegate.supportsLimitedOuterJoins();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public String getSchemaTerm() throws SQLException {
        try {
            return super.delegate.getSchemaTerm();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public String getProcedureTerm() throws SQLException {
        try {
            return super.delegate.getProcedureTerm();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public String getCatalogTerm() throws SQLException {
        try {
            return super.delegate.getCatalogTerm();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean isCatalogAtStart() throws SQLException {
        try {
            return super.delegate.isCatalogAtStart();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public String getCatalogSeparator() throws SQLException {
        try {
            return super.delegate.getCatalogSeparator();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean supportsSchemasInDataManipulation() throws SQLException {
        try {
            return super.delegate.supportsSchemasInDataManipulation();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean supportsSchemasInProcedureCalls() throws SQLException {
        try {
            return super.delegate.supportsSchemasInProcedureCalls();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean supportsSchemasInTableDefinitions() throws SQLException {
        try {
            return super.delegate.supportsSchemasInTableDefinitions();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean supportsSchemasInIndexDefinitions() throws SQLException {
        try {
            return super.delegate.supportsSchemasInIndexDefinitions();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean supportsSchemasInPrivilegeDefinitions() throws SQLException {
        try {
            return super.delegate.supportsSchemasInPrivilegeDefinitions();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean supportsCatalogsInDataManipulation() throws SQLException {
        try {
            return super.delegate.supportsCatalogsInDataManipulation();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean supportsCatalogsInProcedureCalls() throws SQLException {
        try {
            return super.delegate.supportsCatalogsInProcedureCalls();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean supportsCatalogsInTableDefinitions() throws SQLException {
        try {
            return super.delegate.supportsCatalogsInTableDefinitions();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean supportsCatalogsInIndexDefinitions() throws SQLException {
        try {
            return super.delegate.supportsCatalogsInIndexDefinitions();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean supportsCatalogsInPrivilegeDefinitions() throws SQLException {
        try {
            return super.delegate.supportsCatalogsInPrivilegeDefinitions();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean supportsPositionedDelete() throws SQLException {
        try {
            return super.delegate.supportsPositionedDelete();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean supportsPositionedUpdate() throws SQLException {
        try {
            return super.delegate.supportsPositionedUpdate();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean supportsSelectForUpdate() throws SQLException {
        try {
            return super.delegate.supportsSelectForUpdate();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean supportsStoredProcedures() throws SQLException {
        try {
            return super.delegate.supportsStoredProcedures();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean supportsSubqueriesInComparisons() throws SQLException {
        try {
            return super.delegate.supportsSubqueriesInComparisons();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean supportsSubqueriesInExists() throws SQLException {
        try {
            return super.delegate.supportsSubqueriesInExists();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean supportsSubqueriesInIns() throws SQLException {
        try {
            return super.delegate.supportsSubqueriesInIns();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean supportsSubqueriesInQuantifieds() throws SQLException {
        try {
            return super.delegate.supportsSubqueriesInQuantifieds();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean supportsCorrelatedSubqueries() throws SQLException {
        try {
            return super.delegate.supportsCorrelatedSubqueries();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean supportsUnion() throws SQLException {
        try {
            return super.delegate.supportsUnion();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean supportsUnionAll() throws SQLException {
        try {
            return super.delegate.supportsUnionAll();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean supportsOpenCursorsAcrossCommit() throws SQLException {
        try {
            return super.delegate.supportsOpenCursorsAcrossCommit();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean supportsOpenCursorsAcrossRollback() throws SQLException {
        try {
            return super.delegate.supportsOpenCursorsAcrossRollback();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean supportsOpenStatementsAcrossCommit() throws SQLException {
        try {
            return super.delegate.supportsOpenStatementsAcrossCommit();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean supportsOpenStatementsAcrossRollback() throws SQLException {
        try {
            return super.delegate.supportsOpenStatementsAcrossRollback();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public int getMaxBinaryLiteralLength() throws SQLException {
        try {
            return super.delegate.getMaxBinaryLiteralLength();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public int getMaxCharLiteralLength() throws SQLException {
        try {
            return super.delegate.getMaxCharLiteralLength();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public int getMaxColumnNameLength() throws SQLException {
        try {
            return super.delegate.getMaxColumnNameLength();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public int getMaxColumnsInGroupBy() throws SQLException {
        try {
            return super.delegate.getMaxColumnsInGroupBy();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public int getMaxColumnsInIndex() throws SQLException {
        try {
            return super.delegate.getMaxColumnsInIndex();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public int getMaxColumnsInOrderBy() throws SQLException {
        try {
            return super.delegate.getMaxColumnsInOrderBy();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public int getMaxColumnsInSelect() throws SQLException {
        try {
            return super.delegate.getMaxColumnsInSelect();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public int getMaxColumnsInTable() throws SQLException {
        try {
            return super.delegate.getMaxColumnsInTable();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public int getMaxConnections() throws SQLException {
        try {
            return super.delegate.getMaxConnections();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public int getMaxCursorNameLength() throws SQLException {
        try {
            return super.delegate.getMaxCursorNameLength();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public int getMaxIndexLength() throws SQLException {
        try {
            return super.delegate.getMaxIndexLength();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public int getMaxSchemaNameLength() throws SQLException {
        try {
            return super.delegate.getMaxSchemaNameLength();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public int getMaxProcedureNameLength() throws SQLException {
        try {
            return super.delegate.getMaxProcedureNameLength();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public int getMaxCatalogNameLength() throws SQLException {
        try {
            return super.delegate.getMaxCatalogNameLength();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public int getMaxRowSize() throws SQLException {
        try {
            return super.delegate.getMaxRowSize();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean doesMaxRowSizeIncludeBlobs() throws SQLException {
        try {
            return super.delegate.doesMaxRowSizeIncludeBlobs();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public int getMaxStatementLength() throws SQLException {
        try {
            return super.delegate.getMaxStatementLength();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public int getMaxStatements() throws SQLException {
        try {
            return super.delegate.getMaxStatements();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public int getMaxTableNameLength() throws SQLException {
        try {
            return super.delegate.getMaxTableNameLength();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public int getMaxTablesInSelect() throws SQLException {
        try {
            return super.delegate.getMaxTablesInSelect();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public int getMaxUserNameLength() throws SQLException {
        try {
            return super.delegate.getMaxUserNameLength();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public int getDefaultTransactionIsolation() throws SQLException {
        try {
            return super.delegate.getDefaultTransactionIsolation();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean supportsTransactions() throws SQLException {
        try {
            return super.delegate.supportsTransactions();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean supportsTransactionIsolationLevel(final int n) throws SQLException {
        try {
            return super.delegate.supportsTransactionIsolationLevel(n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean supportsDataDefinitionAndDataManipulationTransactions() throws SQLException {
        try {
            return super.delegate.supportsDataDefinitionAndDataManipulationTransactions();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean supportsDataManipulationTransactionsOnly() throws SQLException {
        try {
            return super.delegate.supportsDataManipulationTransactionsOnly();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean dataDefinitionCausesTransactionCommit() throws SQLException {
        try {
            return super.delegate.dataDefinitionCausesTransactionCommit();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean dataDefinitionIgnoredInTransactions() throws SQLException {
        try {
            return super.delegate.dataDefinitionIgnoredInTransactions();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public ResultSet getProcedures(final String catalog, final String schemaPattern, final String procedureNamePattern) throws SQLException {
        try {
            return super.getProcedures(catalog, schemaPattern, procedureNamePattern);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public ResultSet getProcedureColumns(final String catalog, final String schemaPattern, final String procedureNamePattern, final String columnNamePattern) throws SQLException {
        try {
            return super.getProcedureColumns(catalog, schemaPattern, procedureNamePattern, columnNamePattern);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public ResultSet getTables(final String catalog, final String schemaPattern, final String tableNamePattern, final String[] types) throws SQLException {
        try {
            return super.getTables(catalog, schemaPattern, tableNamePattern, types);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public ResultSet getSchemas() throws SQLException {
        try {
            return super.getSchemas();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public ResultSet getCatalogs() throws SQLException {
        try {
            return super.getCatalogs();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public ResultSet getTableTypes() throws SQLException {
        try {
            return super.getTableTypes();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public ResultSet getColumns(final String catalog, final String schemaPattern, final String tableNamePattern, final String columnNamePattern) throws SQLException {
        try {
            return super.getColumns(catalog, schemaPattern, tableNamePattern, columnNamePattern);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public ResultSet getColumnPrivileges(final String catalog, final String schema, final String table, final String columnNamePattern) throws SQLException {
        try {
            return super.getColumnPrivileges(catalog, schema, table, columnNamePattern);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public ResultSet getTablePrivileges(final String catalog, final String schemaPattern, final String tableNamePattern) throws SQLException {
        try {
            return super.getTablePrivileges(catalog, schemaPattern, tableNamePattern);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public ResultSet getBestRowIdentifier(final String catalog, final String schema, final String table, final int scope, final boolean nullable) throws SQLException {
        try {
            return super.getBestRowIdentifier(catalog, schema, table, scope, nullable);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public ResultSet getVersionColumns(final String catalog, final String schema, final String table) throws SQLException {
        try {
            return super.getVersionColumns(catalog, schema, table);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public ResultSet getPrimaryKeys(final String catalog, final String schema, final String table) throws SQLException {
        try {
            return super.getPrimaryKeys(catalog, schema, table);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public ResultSet getImportedKeys(final String catalog, final String schema, final String table) throws SQLException {
        try {
            return super.getImportedKeys(catalog, schema, table);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public ResultSet getExportedKeys(final String catalog, final String schema, final String table) throws SQLException {
        try {
            return super.getExportedKeys(catalog, schema, table);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public ResultSet getCrossReference(final String parentCatalog, final String parentSchema, final String parentTable, final String foreignCatalog, final String foreignSchema, final String foreignTable) throws SQLException {
        try {
            return super.getCrossReference(parentCatalog, parentSchema, parentTable, foreignCatalog, foreignSchema, foreignTable);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public ResultSet getTypeInfo() throws SQLException {
        try {
            return super.getTypeInfo();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public ResultSet getIndexInfo(final String catalog, final String schema, final String table, final boolean unique, final boolean approximate) throws SQLException {
        try {
            return super.getIndexInfo(catalog, schema, table, unique, approximate);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean supportsResultSetType(final int n) throws SQLException {
        try {
            return super.delegate.supportsResultSetType(n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean supportsResultSetConcurrency(final int n, final int n2) throws SQLException {
        try {
            return super.delegate.supportsResultSetConcurrency(n, n2);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean ownUpdatesAreVisible(final int n) throws SQLException {
        try {
            return super.delegate.ownUpdatesAreVisible(n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean ownDeletesAreVisible(final int n) throws SQLException {
        try {
            return super.delegate.ownDeletesAreVisible(n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean ownInsertsAreVisible(final int n) throws SQLException {
        try {
            return super.delegate.ownInsertsAreVisible(n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean othersUpdatesAreVisible(final int n) throws SQLException {
        try {
            return super.delegate.othersUpdatesAreVisible(n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean othersDeletesAreVisible(final int n) throws SQLException {
        try {
            return super.delegate.othersDeletesAreVisible(n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean othersInsertsAreVisible(final int n) throws SQLException {
        try {
            return super.delegate.othersInsertsAreVisible(n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean updatesAreDetected(final int n) throws SQLException {
        try {
            return super.delegate.updatesAreDetected(n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean deletesAreDetected(final int n) throws SQLException {
        try {
            return super.delegate.deletesAreDetected(n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean insertsAreDetected(final int n) throws SQLException {
        try {
            return super.delegate.insertsAreDetected(n);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean supportsBatchUpdates() throws SQLException {
        try {
            return super.delegate.supportsBatchUpdates();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public ResultSet getUDTs(final String catalog, final String schemaPattern, final String typeNamePattern, final int[] types) throws SQLException {
        try {
            return super.getUDTs(catalog, schemaPattern, typeNamePattern, types);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean supportsSavepoints() throws SQLException {
        try {
            return super.delegate.supportsSavepoints();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean supportsNamedParameters() throws SQLException {
        try {
            return super.delegate.supportsNamedParameters();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean supportsMultipleOpenResults() throws SQLException {
        try {
            return super.delegate.supportsMultipleOpenResults();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean supportsGetGeneratedKeys() throws SQLException {
        try {
            return super.delegate.supportsGetGeneratedKeys();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public ResultSet getSuperTypes(final String catalog, final String schemaPattern, final String typeNamePattern) throws SQLException {
        try {
            return super.getSuperTypes(catalog, schemaPattern, typeNamePattern);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public ResultSet getSuperTables(final String catalog, final String schemaPattern, final String tableNamePattern) throws SQLException {
        try {
            return super.getSuperTables(catalog, schemaPattern, tableNamePattern);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public ResultSet getAttributes(final String catalog, final String schemaPattern, final String typeNamePattern, final String attributeNamePattern) throws SQLException {
        try {
            return super.getAttributes(catalog, schemaPattern, typeNamePattern, attributeNamePattern);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean supportsResultSetHoldability(final int n) throws SQLException {
        try {
            return super.delegate.supportsResultSetHoldability(n);
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
    
    public int getDatabaseMajorVersion() throws SQLException {
        try {
            return super.delegate.getDatabaseMajorVersion();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public int getDatabaseMinorVersion() throws SQLException {
        try {
            return super.delegate.getDatabaseMinorVersion();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public int getJDBCMajorVersion() throws SQLException {
        try {
            return super.delegate.getJDBCMajorVersion();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public int getJDBCMinorVersion() throws SQLException {
        try {
            return super.delegate.getJDBCMinorVersion();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public int getSQLStateType() throws SQLException {
        try {
            return super.delegate.getSQLStateType();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean locatorsUpdateCopy() throws SQLException {
        try {
            return super.delegate.locatorsUpdateCopy();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean supportsStatementPooling() throws SQLException {
        try {
            return super.delegate.supportsStatementPooling();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public RowIdLifetime getRowIdLifetime() throws SQLException {
        try {
            return super.delegate.getRowIdLifetime();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public ResultSet getSchemas(final String catalog, final String schemaPattern) throws SQLException {
        try {
            return super.getSchemas(catalog, schemaPattern);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean supportsStoredFunctionsUsingCallSyntax() throws SQLException {
        try {
            return super.delegate.supportsStoredFunctionsUsingCallSyntax();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean autoCommitFailureClosesAllResultSets() throws SQLException {
        try {
            return super.delegate.autoCommitFailureClosesAllResultSets();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public ResultSet getClientInfoProperties() throws SQLException {
        try {
            return super.getClientInfoProperties();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public ResultSet getFunctions(final String catalog, final String schemaPattern, final String functionNamePattern) throws SQLException {
        try {
            return super.getFunctions(catalog, schemaPattern, functionNamePattern);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public ResultSet getFunctionColumns(final String catalog, final String schemaPattern, final String functionNamePattern, final String columnNamePattern) throws SQLException {
        try {
            return super.getFunctionColumns(catalog, schemaPattern, functionNamePattern, columnNamePattern);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    @Override
    public ResultSet getPseudoColumns(final String catalog, final String schemaPattern, final String tableNamePattern, final String columnNamePattern) throws SQLException {
        try {
            return super.getPseudoColumns(catalog, schemaPattern, tableNamePattern, columnNamePattern);
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean generatedKeyAlwaysReturned() throws SQLException {
        try {
            return super.delegate.generatedKeyAlwaysReturned();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public long getMaxLogicalLobSize() throws SQLException {
        try {
            return super.delegate.getMaxLogicalLobSize();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean supportsRefCursors() throws SQLException {
        try {
            return super.delegate.supportsRefCursors();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    public boolean supportsSharding() throws SQLException {
        try {
            return super.delegate.supportsSharding();
        }
        catch (final SQLException e) {
            throw this.checkException(e);
        }
    }
    
    HikariProxyDatabaseMetaData(final ProxyConnection connection, final DatabaseMetaData metaData) {
        super(connection, metaData);
    }
}
