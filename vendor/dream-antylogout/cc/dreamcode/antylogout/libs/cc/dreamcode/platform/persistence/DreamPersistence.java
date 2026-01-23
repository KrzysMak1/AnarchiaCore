package cc.dreamcode.antylogout.libs.cc.dreamcode.platform.persistence;

import cc.dreamcode.antylogout.libs.eu.okaeri.configs.serdes.SerdesRegistry;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.serdes.OkaeriSerdesPack;

public interface DreamPersistence
{
    default OkaeriSerdesPack getPersistenceSerdesPack() {
        return registry -> {};
    }
}
