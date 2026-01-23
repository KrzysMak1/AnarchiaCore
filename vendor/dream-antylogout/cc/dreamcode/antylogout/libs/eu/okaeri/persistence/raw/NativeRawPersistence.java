package cc.dreamcode.antylogout.libs.eu.okaeri.persistence.raw;

import java.util.Collections;
import java.util.Set;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.document.index.IndexProperty;
import lombok.NonNull;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.PersistenceCollection;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.PersistencePath;

public abstract class NativeRawPersistence extends RawPersistence
{
    public NativeRawPersistence(final PersistencePath basePath) {
        super(basePath, PersistencePropertyMode.NATIVE, PersistenceIndexMode.NATIVE);
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
    public Set<PersistencePath> findMissingIndexes(@NonNull final PersistenceCollection collection, @NonNull final Set<IndexProperty> indexProperties) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        if (indexProperties == null) {
            throw new NullPointerException("indexProperties is marked non-null but is null");
        }
        return (Set<PersistencePath>)Collections.emptySet();
    }
}
