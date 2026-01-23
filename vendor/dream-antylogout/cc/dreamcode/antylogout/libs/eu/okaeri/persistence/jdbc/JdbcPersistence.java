package cc.dreamcode.antylogout.libs.eu.okaeri.persistence.jdbc;

import java.io.IOException;
import java.util.function.Predicate;
import java.util.List;
import java.util.ArrayList;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.PersistenceEntity;
import java.util.stream.Stream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Collection;
import java.util.Optional;
import java.sql.ResultSet;
import java.util.Iterator;
import java.sql.PreparedStatement;
import java.util.stream.Collectors;
import java.util.HashSet;
import java.util.Collections;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.document.index.IndexProperty;
import java.util.Set;
import java.sql.Connection;
import java.sql.SQLException;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.PersistenceCollection;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.raw.PersistenceIndexMode;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.raw.PersistencePropertyMode;
import cc.dreamcode.antylogout.libs.com.zaxxer.hikari.HikariConfig;
import lombok.NonNull;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.PersistencePath;
import cc.dreamcode.antylogout.libs.com.zaxxer.hikari.HikariDataSource;
import java.util.logging.Logger;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.raw.RawPersistence;

public class JdbcPersistence extends RawPersistence
{
    private static final Logger LOGGER;
    protected HikariDataSource dataSource;
    
    protected JdbcPersistence(@NonNull final PersistencePath basePath, @NonNull final HikariConfig hikariConfig, @NonNull final PersistencePropertyMode propertyMode, @NonNull final PersistenceIndexMode indexMode) {
        super(basePath, propertyMode, indexMode);
        if (basePath == null) {
            throw new NullPointerException("basePath is marked non-null but is null");
        }
        if (hikariConfig == null) {
            throw new NullPointerException("hikariConfig is marked non-null but is null");
        }
        if (propertyMode == null) {
            throw new NullPointerException("propertyMode is marked non-null but is null");
        }
        if (indexMode == null) {
            throw new NullPointerException("indexMode is marked non-null but is null");
        }
        this.connect(hikariConfig);
    }
    
    protected JdbcPersistence(@NonNull final PersistencePath basePath, @NonNull final HikariDataSource dataSource, @NonNull final PersistencePropertyMode propertyMode, @NonNull final PersistenceIndexMode indexMode) {
        super(basePath, propertyMode, indexMode);
        if (basePath == null) {
            throw new NullPointerException("basePath is marked non-null but is null");
        }
        if (dataSource == null) {
            throw new NullPointerException("dataSource is marked non-null but is null");
        }
        if (propertyMode == null) {
            throw new NullPointerException("propertyMode is marked non-null but is null");
        }
        if (indexMode == null) {
            throw new NullPointerException("indexMode is marked non-null but is null");
        }
        this.dataSource = dataSource;
    }
    
    public JdbcPersistence(@NonNull final PersistencePath basePath, @NonNull final HikariConfig hikariConfig) {
        this(basePath, hikariConfig, PersistencePropertyMode.TOSTRING, PersistenceIndexMode.EMULATED);
        if (basePath == null) {
            throw new NullPointerException("basePath is marked non-null but is null");
        }
        if (hikariConfig == null) {
            throw new NullPointerException("hikariConfig is marked non-null but is null");
        }
    }
    
    public JdbcPersistence(@NonNull final PersistencePath basePath, @NonNull final HikariDataSource dataSource) {
        this(basePath, dataSource, PersistencePropertyMode.TOSTRING, PersistenceIndexMode.EMULATED);
        if (basePath == null) {
            throw new NullPointerException("basePath is marked non-null but is null");
        }
        if (dataSource == null) {
            throw new NullPointerException("dataSource is marked non-null but is null");
        }
    }
    
    protected void connect(@NonNull final HikariConfig hikariConfig) {
        try {
            if (hikariConfig == null) {
                throw new NullPointerException("hikariConfig is marked non-null but is null");
            }
            do {
                try {
                    this.dataSource = new HikariDataSource(hikariConfig);
                }
                catch (final Exception exception) {
                    if (exception.getCause() != null) {
                        JdbcPersistence.LOGGER.severe("[" + this.getBasePath().getValue() + "] Cannot connect with database (waiting 30s): " + exception.getMessage() + " caused by " + exception.getCause().getMessage());
                    }
                    else {
                        JdbcPersistence.LOGGER.severe("[" + this.getBasePath().getValue() + "] Cannot connect with database (waiting 30s): " + exception.getMessage());
                    }
                    Thread.sleep(30000L);
                }
            } while (this.dataSource == null);
        }
        catch (final Throwable $ex) {
            throw $ex;
        }
    }
    
    @Override
    public void registerCollection(@NonNull final PersistenceCollection collection) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        final String collectionTable = this.table(collection);
        final int keyLength = collection.getKeyLength();
        final String sql = "create table if not exists `" + collectionTable + "` (`key` varchar(" + keyLength + ") primary key not null,`value` text not null)";
        final String alterKeySql = "alter table `" + collectionTable + "` MODIFY COLUMN `key` varchar(" + keyLength + ") not null";
        try (final Connection connection = this.getDataSource().getConnection()) {
            connection.createStatement().execute(sql);
            connection.createStatement().execute(alterKeySql);
        }
        catch (final SQLException exception) {
            throw new RuntimeException("cannot register collection", (Throwable)exception);
        }
        final Set<IndexProperty> indexes = collection.getIndexes();
        final int identityLength = collection.getMaxIndexIdentityLength();
        final int propertyLength = collection.getMaxIndexPropertyLength();
        indexes.forEach(index -> this.registerIndex(collection, index, identityLength, propertyLength));
        super.registerCollection(collection);
    }
    
    private void registerIndex(@NonNull final PersistenceCollection collection, @NonNull final IndexProperty property, final int identityLength, final int propertyLength) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        if (property == null) {
            throw new NullPointerException("property is marked non-null but is null");
        }
        final int keyLength = collection.getKeyLength();
        final String indexTable = this.indexTable(collection);
        final String tableSql = "create table if not exists `" + indexTable + "` (`key` varchar(" + keyLength + ") not null,`property` varchar(" + propertyLength + ") not null,`identity` varchar(" + identityLength + ") not null,primary key(`key`, `property`))";
        final String alterKeySql = "alter table `" + indexTable + "` MODIFY COLUMN `key` varchar(" + keyLength + ") not null";
        final String alterPropertySql = "alter table `" + indexTable + "` MODIFY COLUMN `property` varchar(" + propertyLength + ") not null";
        final String alterIdentitySql = "alter table `" + indexTable + "` MODIFY COLUMN `identity` varchar(" + identityLength + ") not null";
        final String indexSql = "create index `identity` on `" + indexTable + "`(`identity`)";
        final String index2Sql = "create index `property` on `" + indexTable + "`(`property`, identity`)";
        try (final Connection connection = this.getDataSource().getConnection()) {
            connection.createStatement().execute(tableSql);
            connection.createStatement().execute(alterKeySql);
            connection.createStatement().execute(alterPropertySql);
            connection.createStatement().execute(alterIdentitySql);
            try {
                connection.createStatement().execute(indexSql);
            }
            catch (final SQLException ex) {}
            try {
                connection.createStatement().execute(index2Sql);
            }
            catch (final SQLException ex2) {}
        }
        catch (final SQLException exception) {
            throw new RuntimeException("cannot register collection", (Throwable)exception);
        }
    }
    
    @Override
    public Set<PersistencePath> findMissingIndexes(@NonNull final PersistenceCollection collection, @NonNull final Set<IndexProperty> indexProperties) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        if (indexProperties == null) {
            throw new NullPointerException("indexProperties is marked non-null but is null");
        }
        if (indexProperties.isEmpty()) {
            return (Set<PersistencePath>)Collections.emptySet();
        }
        final String table = this.table(collection);
        final String indexTable = this.indexTable(collection);
        final Set<PersistencePath> paths = (Set<PersistencePath>)new HashSet();
        final String params = (String)indexProperties.stream().map(e -> "?").collect(Collectors.joining((CharSequence)", "));
        final String sql = "select `key` from `" + table + "` where (select count(1) from " + indexTable + " where `key` = `" + table + "`.`key` and `property` in (" + params + ")) != ?";
        try (final Connection connection = this.getDataSource().getConnection()) {
            final PreparedStatement prepared = connection.prepareStatement(sql);
            int currentPrepared = 1;
            for (final IndexProperty indexProperty : indexProperties) {
                prepared.setString(currentPrepared++, indexProperty.getValue());
            }
            prepared.setInt(currentPrepared, indexProperties.size());
            final ResultSet resultSet = prepared.executeQuery();
            while (resultSet.next()) {
                paths.add((Object)PersistencePath.of(resultSet.getString("key")));
            }
        }
        catch (final SQLException exception) {
            throw new RuntimeException("cannot search missing indexes for " + (Object)collection, (Throwable)exception);
        }
        return paths;
    }
    
    @Override
    public boolean updateIndex(@NonNull final PersistenceCollection collection, @NonNull final PersistencePath path, @NonNull final IndexProperty property, final String identity) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        if (path == null) {
            throw new NullPointerException("path is marked non-null but is null");
        }
        if (property == null) {
            throw new NullPointerException("property is marked non-null but is null");
        }
        this.checkCollectionRegistered(collection);
        final String indexTable = this.indexTable(collection);
        final String key = path.getValue();
        boolean exists;
        try (final Connection connection = this.getDataSource().getConnection()) {
            final String sql = "select count(1) from `" + indexTable + "` where `key` = ? and `property` = ?";
            final PreparedStatement prepared = connection.prepareStatement(sql);
            prepared.setString(1, key);
            prepared.setString(2, property.getValue());
            final ResultSet resultSet = prepared.executeQuery();
            exists = resultSet.next();
        }
        catch (final SQLException exception) {
            throw new RuntimeException("cannot update index " + indexTable + "[" + property.getValue() + "] -> " + key + " = " + identity, (Throwable)exception);
        }
        if (exists) {
            final String sql2 = "update `" + indexTable + "` set `identity` = ? where `key` = ? and `property` = ?";
            try (final Connection connection2 = this.getDataSource().getConnection()) {
                final PreparedStatement prepared = connection2.prepareStatement(sql2);
                prepared.setString(1, identity);
                prepared.setString(2, key);
                prepared.setString(3, property.getValue());
                return prepared.executeUpdate() > 0;
            }
            catch (final SQLException exception2) {
                throw new RuntimeException("cannot update index " + indexTable + "[" + property.getValue() + "] -> " + key + " = " + identity, (Throwable)exception2);
            }
        }
        final String sql2 = "insert into `" + indexTable + "` (`key`, `property`, `identity`) values (?, ?, ?)";
        try (final Connection connection2 = this.getDataSource().getConnection()) {
            final PreparedStatement prepared = connection2.prepareStatement(sql2);
            prepared.setString(1, key);
            prepared.setString(2, property.getValue());
            prepared.setString(3, identity);
            return prepared.executeUpdate() > 0;
        }
        catch (final SQLException exception2) {
            throw new RuntimeException("cannot update index " + indexTable + "[" + property.getValue() + "] -> " + key + " = " + identity, (Throwable)exception2);
        }
    }
    
    @Override
    public boolean dropIndex(@NonNull final PersistenceCollection collection, @NonNull final PersistencePath path, @NonNull final IndexProperty property) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        if (path == null) {
            throw new NullPointerException("path is marked non-null but is null");
        }
        if (property == null) {
            throw new NullPointerException("property is marked non-null but is null");
        }
        this.checkCollectionRegistered(collection);
        final String indexTable = this.indexTable(collection);
        final String sql = "delete from `" + indexTable + "` where `property` = ? and `key` = ?";
        final String key = path.getValue();
        try (final Connection connection = this.getDataSource().getConnection()) {
            final PreparedStatement prepared = connection.prepareStatement(sql);
            prepared.setString(1, property.getValue());
            prepared.setString(2, key);
            return prepared.executeUpdate() > 0;
        }
        catch (final SQLException exception) {
            throw new RuntimeException("cannot delete from index " + indexTable + "[" + property.getValue() + "] key = " + key, (Throwable)exception);
        }
    }
    
    @Override
    public boolean dropIndex(@NonNull final PersistenceCollection collection, @NonNull final PersistencePath path) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        if (path == null) {
            throw new NullPointerException("path is marked non-null but is null");
        }
        this.checkCollectionRegistered(collection);
        final String indexTable = this.indexTable(collection);
        final String sql = "delete from `" + indexTable + "` where `key` = ?";
        final String key = path.getValue();
        try (final Connection connection = this.getDataSource().getConnection()) {
            final PreparedStatement prepared = connection.prepareStatement(sql);
            prepared.setString(1, key);
            return prepared.executeUpdate() > 0;
        }
        catch (final SQLException exception) {
            throw new RuntimeException("cannot delete from index " + indexTable + " key = " + key, (Throwable)exception);
        }
    }
    
    @Override
    public boolean dropIndex(@NonNull final PersistenceCollection collection, @NonNull final IndexProperty property) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        if (property == null) {
            throw new NullPointerException("property is marked non-null but is null");
        }
        this.checkCollectionRegistered(collection);
        final String indexTable = this.indexTable(collection);
        final String sql = "delete from `" + indexTable + "` where `property` = ?";
        try (final Connection connection = this.getDataSource().getConnection()) {
            final PreparedStatement prepared = connection.prepareStatement(sql);
            prepared.setString(1, property.getValue());
            return prepared.executeUpdate() > 0;
        }
        catch (final SQLException exception) {
            throw new RuntimeException("cannot truncate " + indexTable, (Throwable)exception);
        }
    }
    
    @Override
    public Optional<String> read(@NonNull final PersistenceCollection collection, @NonNull final PersistencePath path) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        if (path == null) {
            throw new NullPointerException("path is marked non-null but is null");
        }
        this.checkCollectionRegistered(collection);
        final String sql = "select `value` from `" + this.table(collection) + "` where `key` = ? limit 1";
        try (final Connection connection = this.getDataSource().getConnection()) {
            final PreparedStatement prepared = connection.prepareStatement(sql);
            prepared.setString(1, path.getValue());
            final ResultSet resultSet = prepared.executeQuery();
            if (resultSet.next()) {
                final Optional ofNullable = Optional.ofNullable((Object)resultSet.getString("value"));
                if (connection != null) {
                    connection.close();
                }
                return (Optional<String>)ofNullable;
            }
        }
        catch (final SQLException exception) {
            throw new RuntimeException("cannot read " + (Object)path + " from " + (Object)collection, (Throwable)exception);
        }
        return (Optional<String>)Optional.empty();
    }
    
    @Override
    public Map<PersistencePath, String> read(@NonNull final PersistenceCollection collection, @NonNull final Collection<PersistencePath> paths) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        if (paths == null) {
            throw new NullPointerException("paths is marked non-null but is null");
        }
        this.checkCollectionRegistered(collection);
        final String keys = (String)paths.stream().map(path -> "`key` = ?").collect(Collectors.joining((CharSequence)" or "));
        final String sql = "select `key`, `value` from `" + this.table(collection) + "` where " + keys;
        final Map<PersistencePath, String> map = (Map<PersistencePath, String>)new LinkedHashMap();
        try (final Connection connection = this.getDataSource().getConnection()) {
            final PreparedStatement prepared = connection.prepareStatement(sql);
            int currentIndex = 1;
            for (final PersistencePath path : paths) {
                prepared.setString(currentIndex++, path.getValue());
            }
            final ResultSet resultSet = prepared.executeQuery();
            while (resultSet.next()) {
                final String key = resultSet.getString("key");
                final String value = resultSet.getString("value");
                map.put((Object)PersistencePath.of(key), (Object)value);
            }
        }
        catch (final SQLException exception) {
            throw new RuntimeException("cannot read " + (Object)paths + " from " + (Object)collection, (Throwable)exception);
        }
        return map;
    }
    
    @Override
    public Map<PersistencePath, String> readAll(@NonNull final PersistenceCollection collection) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        this.checkCollectionRegistered(collection);
        final String sql = "select `key`, `value` from `" + this.table(collection) + "`";
        final Map<PersistencePath, String> map = (Map<PersistencePath, String>)new LinkedHashMap();
        try (final Connection connection = this.getDataSource().getConnection()) {
            final PreparedStatement prepared = connection.prepareStatement(sql);
            final ResultSet resultSet = prepared.executeQuery();
            while (resultSet.next()) {
                final String key = resultSet.getString("key");
                final String value = resultSet.getString("value");
                map.put((Object)PersistencePath.of(key), (Object)value);
            }
        }
        catch (final SQLException exception) {
            throw new RuntimeException("cannot read all from " + (Object)collection, (Throwable)exception);
        }
        return map;
    }
    
    @Override
    public Stream<PersistenceEntity<String>> readByProperty(@NonNull final PersistenceCollection collection, @NonNull final PersistencePath property, @NonNull final Object propertyValue) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        if (property == null) {
            throw new NullPointerException("property is marked non-null but is null");
        }
        if (propertyValue == null) {
            throw new NullPointerException("propertyValue is marked non-null but is null");
        }
        return this.isIndexed(collection, property) ? this.readByPropertyIndexed(collection, IndexProperty.of(property.getValue()), propertyValue) : this.streamAll(collection);
    }
    
    protected Stream<PersistenceEntity<String>> readByPropertyIndexed(@NonNull final PersistenceCollection collection, @NonNull final IndexProperty indexProperty, @NonNull final Object propertyValue) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        if (indexProperty == null) {
            throw new NullPointerException("indexProperty is marked non-null but is null");
        }
        if (propertyValue == null) {
            throw new NullPointerException("propertyValue is marked non-null but is null");
        }
        if (!this.canUseToString(propertyValue)) {
            return this.streamAll(collection);
        }
        this.checkCollectionRegistered(collection);
        final String table = this.table(collection);
        final String indexTable = this.indexTable(collection);
        final String sql = "select indexer.`key`, `value` from `" + table + "` join `" + indexTable + "` indexer on `" + table + "`.`key` = indexer.`key` where indexer.`property` = ? and indexer.`identity` = ?";
        try (final Connection connection = this.getDataSource().getConnection()) {
            final PreparedStatement prepared = connection.prepareStatement(sql);
            prepared.setString(1, indexProperty.getValue());
            prepared.setString(2, String.valueOf(propertyValue));
            final ResultSet resultSet = prepared.executeQuery();
            final List<PersistenceEntity<String>> results = (List<PersistenceEntity<String>>)new ArrayList();
            while (resultSet.next()) {
                final String key = resultSet.getString("key");
                final String value = resultSet.getString("value");
                results.add((Object)new PersistenceEntity(PersistencePath.of(key), value));
            }
            final Stream stream = results.stream();
            return (Stream<PersistenceEntity<String>>)stream;
        }
        catch (final SQLException exception) {
            throw new RuntimeException("cannot ready by property from " + (Object)collection, (Throwable)exception);
        }
    }
    
    @Override
    public Stream<PersistenceEntity<String>> streamAll(@NonNull final PersistenceCollection collection) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        this.checkCollectionRegistered(collection);
        final String sql = "select `key`, `value` from `" + this.table(collection) + "`";
        try (final Connection connection = this.getDataSource().getConnection()) {
            final PreparedStatement prepared = connection.prepareStatement(sql);
            final ResultSet resultSet = prepared.executeQuery();
            final List<PersistenceEntity<String>> results = (List<PersistenceEntity<String>>)new ArrayList();
            while (resultSet.next()) {
                final String key = resultSet.getString("key");
                final String value = resultSet.getString("value");
                results.add((Object)new PersistenceEntity(PersistencePath.of(key), value));
            }
            final Stream stream = results.stream();
            return (Stream<PersistenceEntity<String>>)stream;
        }
        catch (final SQLException exception) {
            throw new RuntimeException("cannot stream all from " + (Object)collection, (Throwable)exception);
        }
    }
    
    @Override
    public long count(@NonNull final PersistenceCollection collection) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        this.checkCollectionRegistered(collection);
        final String sql = "select count(1) from `" + this.table(collection) + "`";
        try (final Connection connection = this.getDataSource().getConnection()) {
            final PreparedStatement prepared = connection.prepareStatement(sql);
            final ResultSet resultSet = prepared.executeQuery();
            if (resultSet.next()) {
                final long long1 = resultSet.getLong(1);
                if (connection != null) {
                    connection.close();
                }
                return long1;
            }
        }
        catch (final SQLException exception) {
            throw new RuntimeException("cannot count " + (Object)collection, (Throwable)exception);
        }
        return 0L;
    }
    
    @Override
    public boolean exists(@NonNull final PersistenceCollection collection, @NonNull final PersistencePath path) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        if (path == null) {
            throw new NullPointerException("path is marked non-null but is null");
        }
        this.checkCollectionRegistered(collection);
        final String sql = "select 1 from `" + this.table(collection) + "` where `key` = ? limit 1";
        try (final Connection connection = this.getDataSource().getConnection()) {
            final PreparedStatement prepared = connection.prepareStatement(sql);
            prepared.setString(1, path.getValue());
            final ResultSet resultSet = prepared.executeQuery();
            return resultSet.next();
        }
        catch (final SQLException exception) {
            throw new RuntimeException("cannot check if " + (Object)path + " exists in " + (Object)collection, (Throwable)exception);
        }
    }
    
    @Override
    public boolean write(@NonNull final PersistenceCollection collection, @NonNull final PersistencePath path, @NonNull final String raw) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        if (path == null) {
            throw new NullPointerException("path is marked non-null but is null");
        }
        if (raw == null) {
            throw new NullPointerException("raw is marked non-null but is null");
        }
        if (this.read(collection, path).isPresent()) {
            final String sql = "update `" + this.table(collection) + "` set `value` = ? where `key` = ?";
            try (final Connection connection = this.getDataSource().getConnection()) {
                final PreparedStatement prepared = connection.prepareStatement(sql);
                prepared.setString(1, raw);
                prepared.setString(2, path.getValue());
                return prepared.executeUpdate() > 0;
            }
            catch (final SQLException exception) {
                throw new RuntimeException("cannot write " + (Object)path + " to " + (Object)collection, (Throwable)exception);
            }
        }
        final String sql = "insert into `" + this.table(collection) + "` (`key`, `value`) values (?, ?)";
        try (final Connection connection = this.getDataSource().getConnection()) {
            final PreparedStatement prepared = connection.prepareStatement(sql);
            prepared.setString(1, path.getValue());
            prepared.setString(2, raw);
            return prepared.executeUpdate() > 0;
        }
        catch (final SQLException exception) {
            throw new RuntimeException("cannot write " + (Object)path + " to " + (Object)collection, (Throwable)exception);
        }
    }
    
    @Override
    public boolean delete(@NonNull final PersistenceCollection collection, @NonNull final PersistencePath path) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        if (path == null) {
            throw new NullPointerException("path is marked non-null but is null");
        }
        this.checkCollectionRegistered(collection);
        final String sql = "delete from `" + this.table(collection) + "` where `key` = ?";
        final String key = path.getValue();
        final Set<IndexProperty> collectionIndexes = (Set<IndexProperty>)this.getKnownIndexes().get((Object)collection.getValue());
        if (collectionIndexes != null) {
            collectionIndexes.forEach(index -> this.dropIndex(collection, path));
        }
        try (final Connection connection = this.getDataSource().getConnection()) {
            final PreparedStatement prepared = connection.prepareStatement(sql);
            prepared.setString(1, key);
            return prepared.executeUpdate() > 0;
        }
        catch (final SQLException exception) {
            throw new RuntimeException("cannot delete " + (Object)path + " from " + (Object)collection, (Throwable)exception);
        }
    }
    
    @Override
    public long delete(@NonNull final PersistenceCollection collection, @NonNull final Collection<PersistencePath> paths) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        if (paths == null) {
            throw new NullPointerException("paths is marked non-null but is null");
        }
        this.checkCollectionRegistered(collection);
        if (paths.isEmpty()) {
            return 0L;
        }
        this.checkCollectionRegistered(collection);
        final String keys = (String)paths.stream().map(path -> "`key` = ?").collect(Collectors.joining((CharSequence)" or "));
        final String deleteSql = "delete from `" + this.table(collection) + "` where " + keys;
        final String deleteIndexSql = "delete from `" + this.indexTable(collection) + "` where " + keys;
        try (final Connection connection = this.getDataSource().getConnection()) {
            final PreparedStatement prepared = connection.prepareStatement(deleteIndexSql);
            int currentIndex = 1;
            for (final PersistencePath path : paths) {
                prepared.setString(currentIndex++, path.getValue());
            }
            prepared.executeUpdate();
        }
        catch (final SQLException exception) {
            throw new RuntimeException("cannot delete " + (Object)paths + " from " + (Object)collection, (Throwable)exception);
        }
        try (final Connection connection = this.getDataSource().getConnection()) {
            final PreparedStatement prepared = connection.prepareStatement(deleteSql);
            int currentIndex = 1;
            for (final PersistencePath path : paths) {
                prepared.setString(currentIndex++, path.getValue());
            }
            return prepared.executeUpdate();
        }
        catch (final SQLException exception) {
            throw new RuntimeException("cannot delete " + (Object)paths + " from " + (Object)collection, (Throwable)exception);
        }
    }
    
    @Override
    public boolean deleteAll(@NonNull final PersistenceCollection collection) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        this.checkCollectionRegistered(collection);
        final String sql = "truncate table `" + this.table(collection) + "`";
        final String indexSql = "truncate table `" + this.indexTable(collection) + "`";
        try (final Connection connection = this.getDataSource().getConnection()) {
            final PreparedStatement prepared = connection.prepareStatement(indexSql);
            prepared.executeUpdate();
        }
        catch (final SQLException exception) {
            throw new RuntimeException("cannot truncate " + (Object)collection, (Throwable)exception);
        }
        try (final Connection connection = this.getDataSource().getConnection()) {
            final PreparedStatement prepared = connection.prepareStatement(sql);
            prepared.executeUpdate();
            return true;
        }
        catch (final SQLException exception) {
            throw new RuntimeException("cannot truncate " + (Object)collection, (Throwable)exception);
        }
    }
    
    @Override
    public long deleteAll() {
        return this.getKnownCollections().values().stream().map(this::deleteAll).filter(Predicate.isEqual((Object)true)).count();
    }
    
    public void close() throws IOException {
        this.getDataSource().close();
    }
    
    protected String table(final PersistenceCollection collection) {
        return this.getBasePath().sub(collection).toSqlIdentifier();
    }
    
    protected String indexTable(final PersistenceCollection collection) {
        return this.getBasePath().sub(collection).sub("index").toSqlIdentifier();
    }
    
    public HikariDataSource getDataSource() {
        return this.dataSource;
    }
    
    static {
        LOGGER = Logger.getLogger(JdbcPersistence.class.getSimpleName());
    }
}
