package cc.dreamcode.antylogout.libs.eu.okaeri.persistence.document;

import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.PersistenceEntity;
import java.util.stream.Stream;
import java.util.Optional;
import java.util.Set;
import java.util.Collection;
import java.util.Map;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.document.index.IndexProperty;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.PersistencePath;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.PersistenceCollection;
import lombok.NonNull;

public class ReadOnlyDocumentPersistence extends DocumentPersistence
{
    private final DocumentPersistence parentPersistence;
    
    public ReadOnlyDocumentPersistence(@NonNull final DocumentPersistence parentPersistence) {
        super(parentPersistence.getRead(), parentPersistence.getWrite(), parentPersistence.getConfigurerProvider(), parentPersistence.getSerdesPacks());
        if (parentPersistence == null) {
            throw new NullPointerException("parentPersistence is marked non-null but is null");
        }
        this.parentPersistence = parentPersistence;
    }
    
    @Override
    public void flush() {
    }
    
    @Override
    public long fixIndexes(@NonNull final PersistenceCollection collection) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        return 0L;
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
        return false;
    }
    
    @Override
    public boolean updateIndex(@NonNull final PersistenceCollection collection, @NonNull final PersistencePath path, @NonNull final Document document) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        if (path == null) {
            throw new NullPointerException("path is marked non-null but is null");
        }
        if (document == null) {
            throw new NullPointerException("document is marked non-null but is null");
        }
        return false;
    }
    
    @Override
    public boolean updateIndex(@NonNull final PersistenceCollection collection, @NonNull final PersistencePath path) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        if (path == null) {
            throw new NullPointerException("path is marked non-null but is null");
        }
        return false;
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
        return false;
    }
    
    @Override
    public boolean dropIndex(@NonNull final PersistenceCollection collection, @NonNull final PersistencePath path) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        if (path == null) {
            throw new NullPointerException("path is marked non-null but is null");
        }
        return false;
    }
    
    @Override
    public boolean dropIndex(@NonNull final PersistenceCollection collection, @NonNull final IndexProperty property) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        if (property == null) {
            throw new NullPointerException("property is marked non-null but is null");
        }
        return false;
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
        return false;
    }
    
    @Override
    public long write(@NonNull final PersistenceCollection collection, @NonNull final Map<PersistencePath, Document> entities) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        if (entities == null) {
            throw new NullPointerException("entities is marked non-null but is null");
        }
        return 0L;
    }
    
    @Override
    public boolean delete(@NonNull final PersistenceCollection collection, @NonNull final PersistencePath path) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        if (path == null) {
            throw new NullPointerException("path is marked non-null but is null");
        }
        return false;
    }
    
    @Override
    public long delete(@NonNull final PersistenceCollection collection, @NonNull final Collection<PersistencePath> paths) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        if (paths == null) {
            throw new NullPointerException("paths is marked non-null but is null");
        }
        return 0L;
    }
    
    @Override
    public boolean deleteAll(@NonNull final PersistenceCollection collection) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        return false;
    }
    
    @Override
    public long deleteAll() {
        return 0L;
    }
    
    @Override
    public Set<PersistencePath> findMissingIndexes(@NonNull final PersistenceCollection collection, @NonNull final Set<IndexProperty> indexProperties) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        if (indexProperties == null) {
            throw new NullPointerException("indexProperties is marked non-null but is null");
        }
        return this.parentPersistence.findMissingIndexes(collection, indexProperties);
    }
    
    @Override
    public Document readOrEmpty(@NonNull final PersistenceCollection collection, @NonNull final PersistencePath path) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        if (path == null) {
            throw new NullPointerException("path is marked non-null but is null");
        }
        return this.parentPersistence.readOrEmpty(collection, path);
    }
    
    @Override
    public Optional<Document> read(@NonNull final PersistenceCollection collection, @NonNull final PersistencePath path) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        if (path == null) {
            throw new NullPointerException("path is marked non-null but is null");
        }
        return this.parentPersistence.read(collection, path);
    }
    
    @Override
    public Map<PersistencePath, Document> readOrEmpty(@NonNull final PersistenceCollection collection, @NonNull final Collection<PersistencePath> paths) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        if (paths == null) {
            throw new NullPointerException("paths is marked non-null but is null");
        }
        return this.parentPersistence.readOrEmpty(collection, paths);
    }
    
    @Override
    public Map<PersistencePath, Document> read(@NonNull final PersistenceCollection collection, @NonNull final Collection<PersistencePath> paths) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        if (paths == null) {
            throw new NullPointerException("paths is marked non-null but is null");
        }
        return this.parentPersistence.read(collection, paths);
    }
    
    @Override
    public Map<PersistencePath, Document> readAll(@NonNull final PersistenceCollection collection) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        return this.parentPersistence.readAll(collection);
    }
    
    @Override
    public Stream<PersistenceEntity<Document>> readByProperty(@NonNull final PersistenceCollection collection, @NonNull final PersistencePath property, final Object propertyValue) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        if (property == null) {
            throw new NullPointerException("property is marked non-null but is null");
        }
        return this.parentPersistence.readByProperty(collection, property, propertyValue);
    }
    
    @Override
    public Stream<PersistenceEntity<Document>> streamAll(@NonNull final PersistenceCollection collection) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        return this.parentPersistence.streamAll(collection);
    }
    
    @Override
    public long count(@NonNull final PersistenceCollection collection) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        return this.parentPersistence.count(collection);
    }
    
    @Override
    public boolean exists(@NonNull final PersistenceCollection collection, @NonNull final PersistencePath path) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        if (path == null) {
            throw new NullPointerException("path is marked non-null but is null");
        }
        return this.parentPersistence.exists(collection, path);
    }
}
