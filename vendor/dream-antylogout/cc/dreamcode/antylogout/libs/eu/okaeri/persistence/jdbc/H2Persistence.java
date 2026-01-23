package cc.dreamcode.antylogout.libs.eu.okaeri.persistence.jdbc;

import java.util.Iterator;
import java.util.Map;
import java.util.List;
import java.sql.ResultSet;
import java.util.ArrayList;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.PersistenceEntity;
import java.util.stream.Stream;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.document.index.IndexProperty;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.PersistenceCollection;
import cc.dreamcode.antylogout.libs.com.zaxxer.hikari.HikariDataSource;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.raw.PersistenceIndexMode;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.raw.PersistencePropertyMode;
import cc.dreamcode.antylogout.libs.com.zaxxer.hikari.HikariConfig;
import lombok.NonNull;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.PersistencePath;

public class H2Persistence extends JdbcPersistence
{
    public H2Persistence(@NonNull final PersistencePath basePath, @NonNull final HikariConfig hikariConfig) {
        super(basePath, hikariConfig, PersistencePropertyMode.NATIVE, PersistenceIndexMode.EMULATED);
        if (basePath == null) {
            throw new NullPointerException("basePath is marked non-null but is null");
        }
        if (hikariConfig == null) {
            throw new NullPointerException("hikariConfig is marked non-null but is null");
        }
    }
    
    public H2Persistence(@NonNull final PersistencePath basePath, @NonNull final HikariDataSource dataSource) {
        super(basePath, dataSource, PersistencePropertyMode.NATIVE, PersistenceIndexMode.EMULATED);
        if (basePath == null) {
            throw new NullPointerException("basePath is marked non-null but is null");
        }
        if (dataSource == null) {
            throw new NullPointerException("dataSource is marked non-null but is null");
        }
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
        final String sql = "insert into `" + indexTable + "` (`key`, `property`, `identity`) values (?, ?, ?) on duplicate key update `identity` = ?";
        final String key = path.getValue();
        try (final Connection connection = this.getDataSource().getConnection()) {
            final PreparedStatement prepared = connection.prepareStatement(sql);
            prepared.setString(1, key);
            prepared.setString(2, property.getValue());
            prepared.setString(3, identity);
            prepared.setString(4, identity);
            return prepared.executeUpdate() > 0;
        }
        catch (final SQLException exception) {
            throw new RuntimeException("cannot update index " + indexTable + "[" + property.getValue() + "] -> " + key + " = " + identity, (Throwable)exception);
        }
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
        return this.isIndexed(collection, property) ? this.readByPropertyIndexed(collection, IndexProperty.of(property.getValue()), propertyValue) : this.readByPropertyInstr(collection, property, propertyValue);
    }
    
    private Stream<PersistenceEntity<String>> readByPropertyInstr(@NonNull final PersistenceCollection collection, @NonNull final PersistencePath property, @NonNull final Object propertyValue) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        if (property == null) {
            throw new NullPointerException("property is marked non-null but is null");
        }
        if (propertyValue == null) {
            throw new NullPointerException("propertyValue is marked non-null but is null");
        }
        if (!this.canUseToString(propertyValue)) {
            return this.streamAll(collection);
        }
        this.checkCollectionRegistered(collection);
        final String sql = "select `key`, `value` from `" + this.table(collection) + "` where instr(`value`, ?)";
        try (final Connection connection = this.getDataSource().getConnection()) {
            final PreparedStatement prepared = connection.prepareStatement(sql);
            prepared.setObject(1, (Object)String.valueOf(propertyValue));
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
        final String sql = "insert into `" + this.table(collection) + "` (`key`, `value`) values (?, ?) on duplicate key update `value` = ?";
        try (final Connection connection = this.getDataSource().getConnection()) {
            final PreparedStatement prepared = connection.prepareStatement(sql);
            prepared.setString(1, path.getValue());
            prepared.setString(2, raw);
            prepared.setString(3, raw);
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
        this.checkCollectionRegistered(collection);
        final String sql = "insert into `" + this.table(collection) + "` (`key`, `value`) values (?, ?) on duplicate key update `value` = ?";
        try (final Connection connection = this.getDataSource().getConnection()) {
            final PreparedStatement prepared = connection.prepareStatement(sql);
            connection.setAutoCommit(false);
            for (final Map.Entry<PersistencePath, String> entry : entities.entrySet()) {
                prepared.setString(1, ((PersistencePath)entry.getKey()).getValue());
                prepared.setString(2, (String)entry.getValue());
                prepared.setString(3, (String)entry.getValue());
                prepared.addBatch();
            }
            final int changes = prepared.executeUpdate();
            connection.commit();
            return changes;
        }
        catch (final SQLException exception) {
            throw new RuntimeException("cannot write " + (Object)entities + " to " + (Object)collection, (Throwable)exception);
        }
    }
}
