package cc.dreamcode.antylogout.libs.cc.dreamcode.menu.serializer;

import cc.dreamcode.antylogout.libs.eu.okaeri.configs.serdes.ObjectSerializer;
import lombok.NonNull;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.serdes.SerdesRegistry;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.serdes.OkaeriSerdesPack;

public class SerdesMenu implements OkaeriSerdesPack
{
    @Override
    public void register(@NonNull final SerdesRegistry registry) {
        if (registry == null) {
            throw new NullPointerException("registry is marked non-null but is null");
        }
        registry.register(new MenuBuilderSerializer());
    }
}
