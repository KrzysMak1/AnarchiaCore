package cc.dreamcode.antylogout.libs.eu.okaeri.configs.serdes.serializable;

import cc.dreamcode.antylogout.libs.eu.okaeri.configs.schema.GenericsDeclaration;
import lombok.NonNull;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.serdes.SerializationData;

public interface ConfigSerializable
{
    void serialize(@NonNull final SerializationData data, @NonNull final GenericsDeclaration generics);
}
