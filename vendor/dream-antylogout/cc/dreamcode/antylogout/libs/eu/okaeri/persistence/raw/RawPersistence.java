package cc.dreamcode.antylogout.libs.eu.okaeri.persistence.raw;

import java.util.concurrent.ConcurrentHashMap;
import java.util.UUID;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.filter.DeleteFilter;
import java.util.function.Predicate;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Collection;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.filter.FindFilter;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.PersistenceEntity;
import java.util.stream.Stream;
import lombok.NonNull;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.PersistencePath;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.document.index.IndexProperty;
import java.util.Set;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.PersistenceCollection;
import java.util.Map;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.Persistence;

public abstract class RawPersistence implements Persistence<String>
{
    private static final boolean DEBUG;
    private final Map<String, PersistenceCollection> knownCollections;
    private final Map<String, Set<IndexProperty>> knownIndexes;
    private final PersistencePath basePath;
    private final PersistencePropertyMode propertyMode;
    private final PersistenceIndexMode indexMode;
    private boolean flushOnWrite;
    
    @Override
    public void registerCollection(@NonNull final PersistenceCollection collection) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        this.knownCollections.put((Object)collection.getValue(), (Object)collection);
        this.knownIndexes.put((Object)collection.getValue(), (Object)collection.getIndexes());
    }
    
    @Override
    public long fixIndexes(final PersistenceCollection collection) {
        throw new RuntimeException("not implemented yet");
    }
    
    @Override
    public void flush() {
    }
    
    @Override
    public boolean updateIndex(final PersistenceCollection collection, final PersistencePath path, final IndexProperty property, final String identity) {
        throw new RuntimeException("not implemented yet");
    }
    
    @Override
    public boolean updateIndex(final PersistenceCollection collection, final PersistencePath path, final String entity) {
        throw new RuntimeException("not implemented yet");
    }
    
    @Override
    public boolean updateIndex(final PersistenceCollection collection, final PersistencePath path) {
        throw new RuntimeException("not implemented yet");
    }
    
    @Override
    public boolean dropIndex(final PersistenceCollection collection, final PersistencePath path, final IndexProperty property) {
        throw new RuntimeException("not implemented yet");
    }
    
    @Override
    public boolean dropIndex(final PersistenceCollection collection, final PersistencePath path) {
        throw new RuntimeException("not implemented yet");
    }
    
    @Override
    public boolean dropIndex(final PersistenceCollection collection, final IndexProperty property) {
        throw new RuntimeException("not implemented yet");
    }
    
    @Override
    public Set<PersistencePath> findMissingIndexes(final PersistenceCollection collection, final Set<IndexProperty> indexProperties) {
        throw new RuntimeException("not implemented yet");
    }
    
    @Override
    public Stream<PersistenceEntity<String>> readByProperty(final PersistenceCollection collection, final PersistencePath property, final Object propertyValue) {
        throw new RuntimeException("not implemented yet");
    }
    
    @Override
    public Stream<PersistenceEntity<String>> readByFilter(@NonNull final PersistenceCollection collection, @NonNull final FindFilter filter) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        if (filter == null) {
            throw new NullPointerException("filter is marked non-null but is null");
        }
        throw new RuntimeException("not implemented yet");
    }
    
    @Override
    public String readOrEmpty(@NonNull final PersistenceCollection collection, @NonNull final PersistencePath path) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        if (path == null) {
            throw new NullPointerException("path is marked non-null but is null");
        }
        return (String)this.read(collection, path).orElse((Object)"");
    }
    
    @Override
    public Map<PersistencePath, String> readOrEmpty(@NonNull final PersistenceCollection collection, @NonNull final Collection<PersistencePath> paths) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        if (paths == null) {
            throw new NullPointerException("paths is marked non-null but is null");
        }
        this.checkCollectionRegistered(collection);
        final Map<PersistencePath, String> map = (Map<PersistencePath, String>)new LinkedHashMap();
        final Map<PersistencePath, String> data = this.read(collection, paths);
        for (final PersistencePath path : paths) {
            map.put((Object)path, (Object)data.getOrDefault((Object)path, (Object)""));
        }
        return map;
    }
    
    @Override
    public long write(@NonNull final PersistenceCollection collection, @NonNull final Map<PersistencePath, String> entities) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        if (entities == null) {
            throw new NullPointerException("entities is marked non-null but is null");
        }
        return entities.entrySet().stream().map(entry -> this.write(collection, (PersistencePath)entry.getKey(), (String)entry.getValue())).filter(Predicate.isEqual((Object)true)).count();
    }
    
    @Override
    public long deleteByFilter(final PersistenceCollection collection, final DeleteFilter filter) {
        throw new RuntimeException("not implemented yet");
    }
    
    public void checkCollectionRegistered(@NonNull final PersistenceCollection collection) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        if (this.getKnownCollections().containsKey((Object)collection.getValue())) {
            return;
        }
        throw new IllegalArgumentException("cannot use unregistered collection: " + (Object)collection);
    }
    
    public PersistencePath toFullPath(@NonNull final PersistenceCollection collection, @NonNull final PersistencePath path) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        if (path == null) {
            throw new NullPointerException("path is marked non-null but is null");
        }
        this.checkCollectionRegistered(collection);
        return this.getBasePath().sub(collection).sub(this.convertPath(path));
    }
    
    public PersistencePath convertPath(@NonNull final PersistencePath path) {
        if (path == null) {
            throw new NullPointerException("path is marked non-null but is null");
        }
        return path;
    }
    
    public boolean isIndexed(@NonNull final PersistenceCollection collection, @NonNull final PersistencePath path) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        if (path == null) {
            throw new NullPointerException("path is marked non-null but is null");
        }
        final Set<IndexProperty> collectionIndexes = (Set<IndexProperty>)this.getKnownIndexes().get((Object)collection.getValue());
        if (collectionIndexes == null) {
            return false;
        }
        final IndexProperty indexProperty = IndexProperty.of(path.getValue());
        return collectionIndexes.contains((Object)indexProperty);
    }
    
    public boolean canUseToString(final Object value) {
        return value instanceof String || value instanceof Integer || value instanceof UUID;
    }
    
    protected String debugQuery(@NonNull final String query) {
        if (query == null) {
            throw new NullPointerException("query is marked non-null but is null");
        }
        if (RawPersistence.DEBUG) {
            System.out.println(query);
        }
        return query;
    }
    
    public RawPersistence(final PersistencePath basePath, final PersistencePropertyMode propertyMode, final PersistenceIndexMode indexMode) {
        this.knownCollections = (Map<String, PersistenceCollection>)new ConcurrentHashMap();
        this.knownIndexes = (Map<String, Set<IndexProperty>>)new ConcurrentHashMap();
        this.flushOnWrite = true;
        this.basePath = basePath;
        this.propertyMode = propertyMode;
        this.indexMode = indexMode;
    }
    
    public Map<String, PersistenceCollection> getKnownCollections() {
        return this.knownCollections;
    }
    
    public Map<String, Set<IndexProperty>> getKnownIndexes() {
        return this.knownIndexes;
    }
    
    @Override
    public PersistencePath getBasePath() {
        return this.basePath;
    }
    
    public PersistencePropertyMode getPropertyMode() {
        return this.propertyMode;
    }
    
    public PersistenceIndexMode getIndexMode() {
        return this.indexMode;
    }
    
    public boolean isFlushOnWrite() {
        return this.flushOnWrite;
    }
    
    @Override
    public void setFlushOnWrite(final boolean flushOnWrite) {
        this.flushOnWrite = flushOnWrite;
    }
    
    static {
        DEBUG = Boolean.parseBoolean(System.getProperty("okaeri.platform.debug", "false"));
    }
}
