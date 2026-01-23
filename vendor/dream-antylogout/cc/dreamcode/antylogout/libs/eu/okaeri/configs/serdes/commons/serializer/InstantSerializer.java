package cc.dreamcode.antylogout.libs.eu.okaeri.configs.serdes.commons.serializer;

import lombok.Generated;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.serdes.DeserializationData;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.schema.GenericsDeclaration;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.serdes.SerializationData;
import lombok.NonNull;
import java.time.Instant;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.serdes.ObjectSerializer;

public class InstantSerializer implements ObjectSerializer<Instant>
{
    private final boolean numeric;
    
    @Override
    public boolean supports(@NonNull final Class<? super Instant> type) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        return Instant.class.isAssignableFrom(type);
    }
    
    @Override
    public void serialize(@NonNull final Instant object, @NonNull final SerializationData data, @NonNull final GenericsDeclaration generics) {
        if (object == null) {
            throw new NullPointerException("object is marked non-null but is null");
        }
        if (data == null) {
            throw new NullPointerException("data is marked non-null but is null");
        }
        if (generics == null) {
            throw new NullPointerException("generics is marked non-null but is null");
        }
        if (this.numeric) {
            data.setValue(object.toEpochMilli(), Long.class);
        }
        else {
            data.setValue(object.toString());
        }
    }
    
    @Override
    public Instant deserialize(@NonNull final DeserializationData data, @NonNull final GenericsDeclaration generics) {
        if (data == null) {
            throw new NullPointerException("data is marked non-null but is null");
        }
        if (generics == null) {
            throw new NullPointerException("generics is marked non-null but is null");
        }
        if (data.getValueRaw() instanceof String) {
            return Instant.parse((CharSequence)data.getValueRaw());
        }
        return Instant.ofEpochMilli((long)data.getValue((Class<Long>)Long.TYPE));
    }
    
    @Generated
    public InstantSerializer(final boolean numeric) {
        this.numeric = numeric;
    }
}
