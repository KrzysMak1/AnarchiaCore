package cc.dreamcode.antylogout.libs.eu.okaeri.persistence.document;

import cc.dreamcode.antylogout.libs.eu.okaeri.configs.ConfigManager;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Function;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;
import java.util.Objects;
import java.nio.file.Files;
import java.util.function.Predicate;
import java.util.Collections;
import java.util.Set;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.document.index.IndexProperty;
import java.util.concurrent.ConcurrentHashMap;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.configurer.InMemoryConfigurer;
import java.io.IOException;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.PersistenceEntity;
import java.util.stream.Stream;
import java.util.Collection;
import java.util.Optional;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.PersistenceCollection;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.raw.RawPersistence;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.raw.PersistenceIndexMode;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.raw.PersistencePropertyMode;
import lombok.NonNull;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.serdes.OkaeriSerdesPack;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.document.index.InMemoryIndex;
import java.util.Map;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.PersistencePath;

public class InMemoryDocumentPersistence extends DocumentPersistence
{
    private final PersistencePath basePath;
    private final Map<String, Map<String, InMemoryIndex>> indexMap;
    private final Map<String, Map<PersistencePath, Document>> documents;
    
    public InMemoryDocumentPersistence(@NonNull final OkaeriSerdesPack... serdesPacks) {
        super(new RawPersistence(PersistencePath.of("memory"), PersistencePropertyMode.NATIVE, PersistenceIndexMode.EMULATED) {
            @Override
            public long count(final PersistenceCollection collection) {
                return 0L;
            }
            
            @Override
            public boolean exists(final PersistenceCollection collection, final PersistencePath path) {
                return false;
            }
            
            @Override
            public Optional<String> read(final PersistenceCollection collection, final PersistencePath path) {
                return (Optional<String>)Optional.empty();
            }
            
            @Override
            public Map<PersistencePath, String> read(final PersistenceCollection collection, final Collection<PersistencePath> paths) {
                return null;
            }
            
            @Override
            public Map<PersistencePath, String> readAll(final PersistenceCollection collection) {
                return null;
            }
            
            @Override
            public Stream<PersistenceEntity<String>> streamAll(final PersistenceCollection collection) {
                return null;
            }
            
            @Override
            public boolean write(final PersistenceCollection collection, final PersistencePath path, final String entity) {
                return false;
            }
            
            @Override
            public boolean delete(final PersistenceCollection collection, final PersistencePath path) {
                return false;
            }
            
            @Override
            public long delete(final PersistenceCollection collection, final Collection<PersistencePath> paths) {
                return 0L;
            }
            
            @Override
            public boolean deleteAll(final PersistenceCollection collection) {
                return false;
            }
            
            @Override
            public long deleteAll() {
                return 0L;
            }
            
            public void close() throws IOException {
            }
        }, InMemoryConfigurer::new, serdesPacks);
        this.basePath = PersistencePath.of("memory");
        this.indexMap = (Map<String, Map<String, InMemoryIndex>>)new ConcurrentHashMap();
        this.documents = (Map<String, Map<PersistencePath, Document>>)new ConcurrentHashMap();
        if (serdesPacks == null) {
            throw new NullPointerException("serdesPacks is marked non-null but is null");
        }
    }
    
    @Override
    public void setFlushOnWrite(final boolean state) {
    }
    
    @Override
    public void flush() {
    }
    
    @Override
    public void registerCollection(@NonNull final PersistenceCollection collection) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        this.getRead().getKnownCollections().put((Object)collection.getValue(), (Object)collection);
        this.getRead().getKnownIndexes().put((Object)collection.getValue(), (Object)collection.getIndexes());
        this.getWrite().getKnownCollections().put((Object)collection.getValue(), (Object)collection);
        this.getWrite().getKnownIndexes().put((Object)collection.getValue(), (Object)collection.getIndexes());
        this.documents.put((Object)collection.getValue(), (Object)new ConcurrentHashMap());
        final Map<String, InMemoryIndex> indexes = (Map<String, InMemoryIndex>)this.indexMap.computeIfAbsent((Object)collection.getValue(), col -> new ConcurrentHashMap());
        collection.getIndexes().forEach(index -> indexes.put((Object)index.getValue(), (Object)ConfigManager.create(InMemoryIndex.class)));
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
        this.getWrite().checkCollectionRegistered(collection);
        final InMemoryIndex flatIndex = (InMemoryIndex)((Map)this.indexMap.get((Object)collection.getValue())).get((Object)property.getValue());
        if (flatIndex == null) {
            throw new IllegalArgumentException("non-indexed property used: " + (Object)property);
        }
        final String currentValue = (String)flatIndex.getKeyToValue().remove((Object)path.getValue());
        if (currentValue != null) {
            ((Set)flatIndex.getValueToKeys().get((Object)currentValue)).remove((Object)path.getValue());
        }
        final Set<String> keys = (Set<String>)flatIndex.getValueToKeys().computeIfAbsent((Object)identity, s -> new HashSet());
        final boolean changed = keys.add((Object)path.getValue());
        return flatIndex.getKeyToValue().put((Object)path.getValue(), (Object)identity) != null || changed;
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
        this.getWrite().checkCollectionRegistered(collection);
        final InMemoryIndex flatIndex = (InMemoryIndex)((Map)this.indexMap.get((Object)collection.getValue())).get((Object)property.getValue());
        if (flatIndex == null) {
            throw new IllegalArgumentException("non-indexed property used: " + (Object)property);
        }
        final String currentValue = (String)flatIndex.getKeyToValue().remove((Object)path.getValue());
        return currentValue != null && ((Set)flatIndex.getValueToKeys().get((Object)currentValue)).remove((Object)path.getValue());
    }
    
    @Override
    public boolean dropIndex(@NonNull final PersistenceCollection collection, @NonNull final PersistencePath path) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        if (path == null) {
            throw new NullPointerException("path is marked non-null but is null");
        }
        return ((Set)this.getWrite().getKnownIndexes().getOrDefault((Object)collection.getValue(), (Object)Collections.emptySet())).stream().map(index -> this.dropIndex(collection, path, index)).anyMatch(Predicate.isEqual((Object)true));
    }
    
    @Override
    public boolean dropIndex(@NonNull final PersistenceCollection collection, @NonNull final IndexProperty property) {
        try {
            if (collection == null) {
                throw new NullPointerException("collection is marked non-null but is null");
            }
            if (property == null) {
                throw new NullPointerException("property is marked non-null but is null");
            }
            this.getWrite().checkCollectionRegistered(collection);
            final InMemoryIndex flatIndex = (InMemoryIndex)((Map)this.indexMap.get((Object)collection.getValue())).remove((Object)property.getValue());
            return flatIndex != null && Files.deleteIfExists(flatIndex.getBindFile());
        }
        catch (final Throwable $ex) {
            throw $ex;
        }
    }
    
    @Override
    public Set<PersistencePath> findMissingIndexes(@NonNull final PersistenceCollection collection, @NonNull final Set<IndexProperty> indexProperties) {
        try {
            if (collection == null) {
                throw new NullPointerException("collection is marked non-null but is null");
            }
            if (indexProperties == null) {
                throw new NullPointerException("indexProperties is marked non-null but is null");
            }
            this.getRead().checkCollectionRegistered(collection);
            final Map<String, InMemoryIndex> collectionIndexes = (Map<String, InMemoryIndex>)this.indexMap.get((Object)collection.getValue());
            if (collectionIndexes.isEmpty()) {
                return (Set<PersistencePath>)Collections.emptySet();
            }
            return (Set<PersistencePath>)this.streamAll(collection).map(PersistenceEntity::getValue).map(entity -> collectionIndexes.values().stream().allMatch(flatIndex -> flatIndex.getKeyToValue().containsKey((Object)entity.getPath().getValue())) ? null : entity.getPath()).filter(Objects::nonNull).collect(Collectors.toSet());
        }
        catch (final Throwable $ex) {
            throw $ex;
        }
    }
    
    @Override
    public Document readOrEmpty(@NonNull final PersistenceCollection collection, @NonNull final PersistencePath path) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        if (path == null) {
            throw new NullPointerException("path is marked non-null but is null");
        }
        return (Document)this.read(collection, path).orElse((Object)this.createDocument(collection, path));
    }
    
    @Override
    public Optional<Document> read(@NonNull final PersistenceCollection collection, @NonNull final PersistencePath path) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        if (path == null) {
            throw new NullPointerException("path is marked non-null but is null");
        }
        this.getRead().checkCollectionRegistered(collection);
        return (Optional<Document>)Optional.ofNullable((Object)((Map)this.documents.get((Object)collection.getValue())).get((Object)path));
    }
    
    @Override
    public Map<PersistencePath, Document> readOrEmpty(@NonNull final PersistenceCollection collection, @NonNull final Collection<PersistencePath> paths) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        if (paths == null) {
            throw new NullPointerException("paths is marked non-null but is null");
        }
        this.getRead().checkCollectionRegistered(collection);
        final Map<PersistencePath, Document> map = (Map<PersistencePath, Document>)new LinkedHashMap();
        final Map<PersistencePath, Document> data = this.read(collection, paths);
        for (final PersistencePath path : paths) {
            map.put((Object)path, (Object)data.getOrDefault((Object)path, (Object)this.createDocument(collection, path)));
        }
        return map;
    }
    
    @Override
    public Map<PersistencePath, Document> read(@NonNull final PersistenceCollection collection, @NonNull final Collection<PersistencePath> paths) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        if (paths == null) {
            throw new NullPointerException("paths is marked non-null but is null");
        }
        this.getRead().checkCollectionRegistered(collection);
        return (Map<PersistencePath, Document>)paths.stream().map(path -> (Document)((Map)this.documents.get((Object)collection.getValue())).get((Object)path)).filter(Objects::nonNull).collect(Collectors.toMap(Document::getPath, Function.identity()));
    }
    
    @Override
    public Map<PersistencePath, Document> readAll(@NonNull final PersistenceCollection collection) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        return (Map<PersistencePath, Document>)new HashMap((Map)this.documents.get((Object)collection.getValue()));
    }
    
    @Override
    public Stream<PersistenceEntity<Document>> readByProperty(@NonNull final PersistenceCollection collection, @NonNull final PersistencePath property, final Object propertyValue) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        if (property == null) {
            throw new NullPointerException("property is marked non-null but is null");
        }
        this.getRead().checkCollectionRegistered(collection);
        final InMemoryIndex flatIndex = (InMemoryIndex)((Map)this.indexMap.get((Object)collection.getValue())).get((Object)property.getValue());
        if (flatIndex == null) {
            return this.streamAll(collection);
        }
        final Set<String> keys = (Set<String>)flatIndex.getValueToKeys().get((Object)String.valueOf(propertyValue));
        if (keys == null || keys.isEmpty()) {
            return (Stream<PersistenceEntity<Document>>)Stream.of((Object[])new PersistenceEntity[0]);
        }
        return (Stream<PersistenceEntity<Document>>)new ArrayList((Collection)keys).stream().map(key -> {
            final PersistencePath path = PersistencePath.of(key);
            return (PersistenceEntity)this.read(collection, path).map(data -> new PersistenceEntity(path, (V)data)).orElseGet(() -> {
                this.dropIndex(collection, path);
                return null;
            });
        }).filter(Objects::nonNull);
    }
    
    @Override
    public Stream<PersistenceEntity<Document>> streamAll(@NonNull final PersistenceCollection collection) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        this.getRead().checkCollectionRegistered(collection);
        final Collection<Document> docList = (Collection<Document>)((Map)this.documents.get((Object)collection.getValue())).values();
        return (Stream<PersistenceEntity<Document>>)docList.stream().map(document -> new PersistenceEntity(document.getPath(), (V)document));
    }
    
    @Override
    public long count(@NonNull final PersistenceCollection collection) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        this.getRead().checkCollectionRegistered(collection);
        return ((Map)this.documents.get((Object)collection.getValue())).size();
    }
    
    @Override
    public boolean exists(@NonNull final PersistenceCollection collection, @NonNull final PersistencePath path) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        if (path == null) {
            throw new NullPointerException("path is marked non-null but is null");
        }
        this.getRead().checkCollectionRegistered(collection);
        return ((Map)this.documents.get((Object)collection.getValue())).containsKey((Object)path);
    }
    
    @Override
    public boolean write(@NonNull final PersistenceCollection collection, @NonNull final PersistencePath path, @NonNull final Document document) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        if (path == null) {
            throw new NullPointerException("path is marked non-null but is null");
        }
        if (document == null) {
            throw new NullPointerException("document is marked non-null but is null");
        }
        this.getWrite().checkCollectionRegistered(collection);
        this.updateIndex(collection, path, document);
        return ((Map)this.documents.get((Object)collection.getValue())).put((Object)path, (Object)document) != null;
    }
    
    @Override
    public long write(@NonNull final PersistenceCollection collection, @NonNull final Map<PersistencePath, Document> entities) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        if (entities == null) {
            throw new NullPointerException("entities is marked non-null but is null");
        }
        return entities.entrySet().stream().map(entity -> this.write(collection, (PersistencePath)entity.getKey(), (Document)entity.getValue())).filter(Predicate.isEqual((Object)true)).count();
    }
    
    @Override
    public boolean delete(@NonNull final PersistenceCollection collection, @NonNull final PersistencePath path) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        if (path == null) {
            throw new NullPointerException("path is marked non-null but is null");
        }
        this.getWrite().checkCollectionRegistered(collection);
        return ((Map)this.documents.get((Object)collection.getValue())).remove((Object)path) != null;
    }
    
    @Override
    public long delete(@NonNull final PersistenceCollection collection, @NonNull final Collection<PersistencePath> paths) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        if (paths == null) {
            throw new NullPointerException("paths is marked non-null but is null");
        }
        return paths.stream().map(path -> this.delete(collection, path)).filter(Predicate.isEqual((Object)true)).count();
    }
    
    @Override
    public boolean deleteAll(@NonNull final PersistenceCollection collection) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        this.getWrite().checkCollectionRegistered(collection);
        final Map<PersistencePath, Document> data = (Map<PersistencePath, Document>)this.documents.get((Object)collection.getValue());
        final boolean changed = !data.isEmpty();
        data.clear();
        return changed;
    }
    
    @Override
    public long deleteAll() {
        return this.documents.values().stream().peek(Map::clear).count();
    }
    
    @Override
    public PersistencePath getBasePath() {
        return this.basePath;
    }
}
