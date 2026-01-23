package cc.dreamcode.antylogout.libs.eu.okaeri.persistence.document.ref;

import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.PersistenceCollection;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.document.DocumentPersistence;
import lombok.NonNull;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.PersistencePath;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.document.Document;

public class LazyRef<T extends Document> extends Ref<T>
{
    protected LazyRef(@NonNull final PersistencePath id, @NonNull final PersistencePath collection, @NonNull final Class<? extends Document> valueType, final T value, final boolean fetched, @NonNull final DocumentPersistence persistence) {
        super(id, collection, valueType, value, fetched, persistence);
        if (id == null) {
            throw new NullPointerException("id is marked non-null but is null");
        }
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        if (valueType == null) {
            throw new NullPointerException("valueType is marked non-null but is null");
        }
        if (persistence == null) {
            throw new NullPointerException("persistence is marked non-null but is null");
        }
    }
    
    public static <A extends Document> LazyRef<A> of(@NonNull final A document) {
        if (document == null) {
            throw new NullPointerException("document is marked non-null but is null");
        }
        final PersistencePath path = document.getPath();
        final PersistenceCollection collection = document.getCollection();
        final DocumentPersistence persistence = document.getPersistence();
        return new LazyRef<A>(path, collection, document.getClass(), document, true, persistence);
    }
}
