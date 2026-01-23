package cc.dreamcode.antylogout.libs.eu.okaeri.persistence.document.ref;

import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.PersistencePath;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.serdes.DeserializationData;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.schema.GenericsDeclaration;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.serdes.SerializationData;
import lombok.NonNull;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.document.DocumentPersistence;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.document.Document;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.serdes.ObjectSerializer;

public class LazyRefSerializer implements ObjectSerializer<Ref<? extends Document>>
{
    private final DocumentPersistence persistence;
    
    @Override
    public boolean supports(@NonNull final Class clazz) {
        if (clazz == null) {
            throw new NullPointerException("clazz is marked non-null but is null");
        }
        return LazyRef.class.isAssignableFrom(clazz);
    }
    
    @Override
    public void serialize(@NonNull final Ref lazyRef, @NonNull final SerializationData serializationData, @NonNull final GenericsDeclaration genericsDeclaration) {
        if (lazyRef == null) {
            throw new NullPointerException("lazyRef is marked non-null but is null");
        }
        if (serializationData == null) {
            throw new NullPointerException("serializationData is marked non-null but is null");
        }
        if (genericsDeclaration == null) {
            throw new NullPointerException("genericsDeclaration is marked non-null but is null");
        }
        serializationData.add("_id", lazyRef.getId().getValue());
        serializationData.add("_collection", lazyRef.getCollection().getValue());
    }
    
    @Override
    public Ref<? extends Document> deserialize(@NonNull final DeserializationData deserializationData, @NonNull final GenericsDeclaration genericsDeclaration) {
        if (deserializationData == null) {
            throw new NullPointerException("deserializationData is marked non-null but is null");
        }
        if (genericsDeclaration == null) {
            throw new NullPointerException("genericsDeclaration is marked non-null but is null");
        }
        final PersistencePath id = PersistencePath.of(deserializationData.get("_id", String.class));
        final PersistencePath collection = PersistencePath.of(deserializationData.get("_collection", String.class));
        final GenericsDeclaration subtype = genericsDeclaration.getSubtypeAtOrNull(0);
        if (subtype == null) {
            throw new IllegalArgumentException("cannot create LazyRef from " + (Object)genericsDeclaration);
        }
        final Class<? extends Document> type = (Class<? extends Document>)subtype.getType();
        return new LazyRef<Document>(id, collection, type, null, false, this.persistence);
    }
    
    public LazyRefSerializer(final DocumentPersistence persistence) {
        this.persistence = persistence;
    }
}
