package cc.dreamcode.antylogout.libs.eu.okaeri.persistence.jdbc;

import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.filter.renderer.StringRenderer;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.jdbc.filter.PostgresFilterRenderer;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.jdbc.filter.SqlStringRenderer;
import java.io.IOException;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.filter.DeleteFilter;
import java.util.function.Predicate;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.document.index.IndexProperty;
import java.util.Set;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.Collection;
import java.util.Optional;
import java.sql.Statement;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.filter.FindFilter;
import java.util.List;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.PersistenceEntity;
import java.util.stream.Stream;
import java.sql.Connection;
import java.sql.SQLException;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.PersistenceCollection;
import cc.dreamcode.antylogout.libs.com.zaxxer.hikari.HikariConfig;
import lombok.NonNull;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.PersistencePath;
import cc.dreamcode.antylogout.libs.com.zaxxer.hikari.HikariDataSource;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.filter.renderer.FilterRenderer;
import java.util.logging.Logger;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.raw.NativeRawPersistence;

public class PostgresPersistence extends NativeRawPersistence
{
    private static final Logger LOGGER;
    private static final FilterRenderer FILTER_RENDERER;
    protected HikariDataSource dataSource;
    
    public PostgresPersistence(@NonNull final PersistencePath basePath, @NonNull final HikariConfig hikariConfig) {
        super(basePath);
        if (basePath == null) {
            throw new NullPointerException("basePath is marked non-null but is null");
        }
        if (hikariConfig == null) {
            throw new NullPointerException("hikariConfig is marked non-null but is null");
        }
        this.connect(hikariConfig);
    }
    
    public PostgresPersistence(@NonNull final PersistencePath basePath, @NonNull final HikariDataSource dataSource) {
        super(basePath);
        if (basePath == null) {
            throw new NullPointerException("basePath is marked non-null but is null");
        }
        if (dataSource == null) {
            throw new NullPointerException("dataSource is marked non-null but is null");
        }
        this.dataSource = dataSource;
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
                        PostgresPersistence.LOGGER.severe("[" + this.getBasePath().getValue() + "] Cannot connect with database (waiting 30s): " + exception.getMessage() + " caused by " + exception.getCause().getMessage());
                    }
                    else {
                        PostgresPersistence.LOGGER.severe("[" + this.getBasePath().getValue() + "] Cannot connect with database (waiting 30s): " + exception.getMessage());
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
        final String sql = "create table if not exists \"" + collectionTable + "\" (key varchar(" + keyLength + ") primary key not null,value jsonb not null)";
        final String alterKeySql = "alter table " + collectionTable + " alter column key type varchar(" + keyLength + ")";
        try (final Connection connection = this.getDataSource().getConnection()) {
            connection.createStatement().execute(this.debugQuery(sql));
            connection.createStatement().execute(this.debugQuery(alterKeySql));
        }
        catch (final SQLException exception) {
            throw new RuntimeException("cannot register collection", (Throwable)exception);
        }
        collection.getIndexes().forEach(index -> {
            final String indexName = this.getBasePath().sub(collection).sub(index).sub("idx").toSqlIdentifier();
            final String jsonPath = PersistencePath.of("value").sub(index).toPostgresJsonPath();
            final String indexSql = "create index if not exists " + indexName + " on " + collectionTable + " ((" + jsonPath + "));";
            try (final Connection connection = this.getDataSource().getConnection()) {
                connection.createStatement().execute(this.debugQuery(indexSql));
            }
            catch (final SQLException exception) {
                throw new RuntimeException("cannot register collection index " + indexName, (Throwable)exception);
            }
        });
        super.registerCollection(collection);
    }
    
    @Override
    public Stream<PersistenceEntity<String>> readByProperty(@NonNull final PersistenceCollection collection, @NonNull final PersistencePath property, final Object propertyValue) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        if (property == null) {
            throw new NullPointerException("property is marked non-null but is null");
        }
        this.checkCollectionRegistered(collection);
        final String sql = "select key, value from \"" + this.table(collection) + "\" where ? = ?";
        try (final Connection connection = this.getDataSource().getConnection()) {
            final PreparedStatement prepared = connection.prepareStatement(this.debugQuery(sql));
            prepared.setString(1, PersistencePath.of("value").sub(property).toPostgresJsonPath(true));
            prepared.setObject(2, propertyValue);
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
    public Stream<PersistenceEntity<String>> readByFilter(@NonNull final PersistenceCollection collection, @NonNull final FindFilter filter) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        if (filter == null) {
            throw new NullPointerException("filter is marked non-null but is null");
        }
        this.checkCollectionRegistered(collection);
        String sql = "select key, value from \"" + this.table(collection) + "\" where " + PostgresPersistence.FILTER_RENDERER.renderCondition(filter.getWhere());
        if (filter.hasSkip()) {
            sql = sql + " offset " + filter.getSkip();
        }
        if (filter.hasLimit()) {
            sql = sql + " limit " + filter.getLimit();
        }
        try (final Connection connection = this.getDataSource().getConnection()) {
            final Statement statement = connection.createStatement();
            final ResultSet resultSet = statement.executeQuery(this.debugQuery(sql));
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
    public Optional<String> read(@NonNull final PersistenceCollection collection, @NonNull final PersistencePath path) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        if (path == null) {
            throw new NullPointerException("path is marked non-null but is null");
        }
        this.checkCollectionRegistered(collection);
        final String sql = "select value from \"" + this.table(collection) + "\" where key = ? limit 1";
        try (final Connection connection = this.getDataSource().getConnection()) {
            final PreparedStatement prepared = connection.prepareStatement(this.debugQuery(sql));
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
        final String keys = (String)paths.stream().map(key -> "?").collect(Collectors.joining((CharSequence)", "));
        final String sql = "select key, value from \"" + this.table(collection) + "\" where key in (" + keys + ")";
        final Map<PersistencePath, String> map = (Map<PersistencePath, String>)new LinkedHashMap();
        try (final Connection connection = this.getDataSource().getConnection()) {
            final PreparedStatement prepared = connection.prepareStatement(this.debugQuery(sql));
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
        return (Map<PersistencePath, String>)this.streamAll(collection).collect(Collectors.toMap(PersistenceEntity::getPath, PersistenceEntity::getValue));
    }
    
    @Override
    public Stream<PersistenceEntity<String>> streamAll(@NonNull final PersistenceCollection collection) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        this.checkCollectionRegistered(collection);
        final String sql = "select key, value from \"" + this.table(collection) + "\"";
        try (final Connection connection = this.getDataSource().getConnection()) {
            final PreparedStatement prepared = connection.prepareStatement(this.debugQuery(sql));
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
        final String sql = "select count(1) from \"" + this.table(collection) + "\"";
        try (final Connection connection = this.getDataSource().getConnection()) {
            final PreparedStatement prepared = connection.prepareStatement(this.debugQuery(sql));
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
        final String sql = "select 1 from \"" + this.table(collection) + "\" where key = ? limit 1";
        try (final Connection connection = this.getDataSource().getConnection()) {
            final PreparedStatement prepared = connection.prepareStatement(this.debugQuery(sql));
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
        this.checkCollectionRegistered(collection);
        final String sql = "insert into \"" + this.table(collection) + "\" (key, value) values (?, ?::json) on conflict(key) do update set value = EXCLUDED.value";
        try (final Connection connection = this.getDataSource().getConnection()) {
            final PreparedStatement prepared = connection.prepareStatement(this.debugQuery(sql));
            prepared.setString(1, path.getValue());
            prepared.setString(2, raw);
            return prepared.executeUpdate() > 0;
        }
        catch (final SQLException exception) {
            throw new RuntimeException("cannot write " + (Object)path + " to " + (Object)collection, (Throwable)exception);
        }
    }
    
    @Override
    public long write(@NonNull final PersistenceCollection collection, @NonNull final Map<PersistencePath, String> entities) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        if (entities == null) {
            throw new NullPointerException("entities is marked non-null but is null");
        }
        boolean first = true;
        this.checkCollectionRegistered(collection);
        final String sql = "insert into \"" + this.table(collection) + "\" (key, value) values (?, ?::json) on conflict(key) do update set value = EXCLUDED.value";
        try (final Connection connection = this.getDataSource().getConnection()) {
            final PreparedStatement prepared = connection.prepareStatement(this.debugQuery(sql));
            connection.setAutoCommit(false);
            for (final Map.Entry<PersistencePath, String> entry : entities.entrySet()) {
                prepared.setString(1, ((PersistencePath)entry.getKey()).getValue());
                prepared.setString(2, (String)entry.getValue());
                prepared.setString(3, (String)entry.getValue());
                prepared.addBatch();
                if (first) {
                    first = false;
                }
                else {
                    this.debugQuery(sql);
                }
            }
            final int changes = prepared.executeUpdate();
            connection.commit();
            return changes;
        }
        catch (final SQLException exception) {
            throw new RuntimeException("cannot write " + (Object)entities + " to " + (Object)collection, (Throwable)exception);
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
        final String sql = "delete from \"" + this.table(collection) + "\" where key = ?";
        final String key = path.getValue();
        final Set<IndexProperty> collectionIndexes = (Set<IndexProperty>)this.getKnownIndexes().get((Object)collection.getValue());
        if (collectionIndexes != null) {
            collectionIndexes.forEach(index -> this.dropIndex(collection, path));
        }
        try (final Connection connection = this.getDataSource().getConnection()) {
            final PreparedStatement prepared = connection.prepareStatement(this.debugQuery(sql));
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
        final String keys = (String)paths.stream().map(key -> "?").collect(Collectors.joining((CharSequence)", "));
        final String deleteSql = "delete from \"" + this.table(collection) + "\" where key in (" + keys + ")";
        try (final Connection connection = this.getDataSource().getConnection()) {
            final PreparedStatement prepared = connection.prepareStatement(this.debugQuery(deleteSql));
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
        final String sql = "truncate table \"" + this.table(collection) + "\"";
        try (final Connection connection = this.getDataSource().getConnection()) {
            final PreparedStatement prepared = connection.prepareStatement(this.debugQuery(sql));
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
    
    @Override
    public long deleteByFilter(@NonNull final PersistenceCollection collection, @NonNull final DeleteFilter filter) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        if (filter == null) {
            throw new NullPointerException("filter is marked non-null but is null");
        }
        this.checkCollectionRegistered(collection);
        final String sql = "delete from \"" + this.table(collection) + "\" where " + PostgresPersistence.FILTER_RENDERER.renderCondition(filter.getWhere());
        try (final Connection connection = this.getDataSource().getConnection()) {
            final Statement statement = connection.createStatement();
            return statement.executeUpdate(this.debugQuery(sql));
        }
        catch (final SQLException exception) {
            throw new RuntimeException("cannot delete from " + (Object)collection + " with " + (Object)filter, (Throwable)exception);
        }
    }
    
    public void close() throws IOException {
        this.getDataSource().close();
    }
    
    protected String table(final PersistenceCollection collection) {
        return this.getBasePath().sub(collection).toSqlIdentifier();
    }
    
    public HikariDataSource getDataSource() {
        return this.dataSource;
    }
    
    static {
        LOGGER = Logger.getLogger(PostgresPersistence.class.getSimpleName());
        FILTER_RENDERER = new PostgresFilterRenderer(new SqlStringRenderer());
    }
}
