package cc.dreamcode.antylogout.libs.eu.okaeri.configs.migrate;

import cc.dreamcode.antylogout.libs.eu.okaeri.configs.migrate.view.RawConfigView;
import lombok.NonNull;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.OkaeriConfig;

@FunctionalInterface
public interface ConfigMigration
{
    boolean migrate(@NonNull final OkaeriConfig config, @NonNull final RawConfigView view);
}
