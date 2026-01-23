package cc.dreamcode.antylogout.libs.eu.okaeri.persistence;

import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.repository.annotation.DocumentIndex;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.repository.annotation.DocumentCollection;
import java.util.HashSet;
import lombok.NonNull;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.document.index.IndexProperty;
import java.util.Set;

public class PersistenceCollection extends PersistencePath
{
    private int keyLength;
    private boolean autofixIndexes;
    private Set<IndexProperty> indexes;
    
    private PersistenceCollection(@NonNull final String value, final int keyLength) {
        super(value);
        this.autofixIndexes = true;
        this.indexes = (Set<IndexProperty>)new HashSet();
        if (value == null) {
            throw new NullPointerException("value is marked non-null but is null");
        }
        this.keyLength = keyLength;
    }
    
    public static PersistenceCollection of(@NonNull final Class<?> clazz) {
        if (clazz == null) {
            throw new NullPointerException("clazz is marked non-null but is null");
        }
        final DocumentCollection collection = clazz.getAnnotation(DocumentCollection.class);
        if (collection == null) {
            throw new IllegalArgumentException((Object)clazz + " is not annotated with @DocumentCollection");
        }
        final PersistenceCollection out = of(collection.path(), collection.keyLength());
        for (final DocumentIndex index : collection.indexes()) {
            out.index(IndexProperty.parse(index.path()).maxLength(index.maxLength()));
        }
        return out.autofixIndexes(collection.autofixIndexes());
    }
    
    public static PersistenceCollection of(@NonNull final String path) {
        if (path == null) {
            throw new NullPointerException("path is marked non-null but is null");
        }
        return new PersistenceCollection(path, 255);
    }
    
    public static PersistenceCollection of(@NonNull final String path, final int keyLength) {
        if (path == null) {
            throw new NullPointerException("path is marked non-null but is null");
        }
        return of(path).keyLength(keyLength);
    }
    
    public PersistenceCollection keyLength(final int keyLength) {
        if (keyLength < 1 || keyLength > 255) {
            throw new IllegalArgumentException("key length should be between 1 and 255");
        }
        this.keyLength = keyLength;
        return this;
    }
    
    public PersistenceCollection index(@NonNull final IndexProperty indexProperty) {
        if (indexProperty == null) {
            throw new NullPointerException("indexProperty is marked non-null but is null");
        }
        this.indexes.add((Object)indexProperty);
        return this;
    }
    
    public PersistenceCollection autofixIndexes(final boolean autofixIndexes) {
        this.autofixIndexes = autofixIndexes;
        return this;
    }
    
    public int getMaxIndexIdentityLength() {
        return this.indexes.stream().mapToInt(IndexProperty::getMaxLength).max().orElse(255);
    }
    
    public int getMaxIndexPropertyLength() {
        return this.indexes.stream().mapToInt(index -> index.getValue().length()).max().orElse(255);
    }
    
    public int getKeyLength() {
        return this.keyLength;
    }
    
    public boolean isAutofixIndexes() {
        return this.autofixIndexes;
    }
    
    public Set<IndexProperty> getIndexes() {
        return this.indexes;
    }
    
    @Override
    public String toString() {
        return "PersistenceCollection(super=" + super.toString() + ", keyLength=" + this.getKeyLength() + ", autofixIndexes=" + this.isAutofixIndexes() + ", indexes=" + (Object)this.getIndexes() + ")";
    }
}
