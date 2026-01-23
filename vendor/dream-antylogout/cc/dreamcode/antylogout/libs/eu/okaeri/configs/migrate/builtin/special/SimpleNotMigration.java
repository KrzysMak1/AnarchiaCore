package cc.dreamcode.antylogout.libs.eu.okaeri.configs.migrate.builtin.special;

import lombok.Generated;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.migrate.view.RawConfigView;
import lombok.NonNull;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.OkaeriConfig;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.migrate.ConfigMigration;

public class SimpleNotMigration implements ConfigMigration
{
    private final ConfigMigration migration;
    
    @Override
    public boolean migrate(@NonNull final OkaeriConfig config, @NonNull final RawConfigView view) {
        if (config == null) {
            throw new NullPointerException("config is marked non-null but is null");
        }
        if (view == null) {
            throw new NullPointerException("view is marked non-null but is null");
        }
        return !this.migration.migrate(config, view);
    }
    
    @Generated
    @Override
    public String toString() {
        return "SimpleNotMigration(migration=" + (Object)this.migration + ")";
    }
    
    @Generated
    public SimpleNotMigration(final ConfigMigration migration) {
        this.migration = migration;
    }
}
