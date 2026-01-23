package cc.dreamcode.antylogout.libs.eu.okaeri.configs.migrate.builtin.special;

import lombok.Generated;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.migrate.view.RawConfigView;
import lombok.NonNull;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.OkaeriConfig;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.migrate.ConfigMigration;

public class SimpleConditionalMigration implements ConfigMigration
{
    private final ConfigMigration when;
    private final ConfigMigration migrationTrue;
    private final ConfigMigration migrationFalse;
    
    @Override
    public boolean migrate(@NonNull final OkaeriConfig config, @NonNull final RawConfigView view) {
        if (config == null) {
            throw new NullPointerException("config is marked non-null but is null");
        }
        if (view == null) {
            throw new NullPointerException("view is marked non-null but is null");
        }
        if (this.when.migrate(config, view)) {
            return this.migrationTrue.migrate(config, view);
        }
        return this.migrationFalse.migrate(config, view);
    }
    
    @Generated
    @Override
    public String toString() {
        return "SimpleConditionalMigration(when=" + (Object)this.when + ", migrationTrue=" + (Object)this.migrationTrue + ", migrationFalse=" + (Object)this.migrationFalse + ")";
    }
    
    @Generated
    public SimpleConditionalMigration(final ConfigMigration when, final ConfigMigration migrationTrue, final ConfigMigration migrationFalse) {
        this.when = when;
        this.migrationTrue = migrationTrue;
        this.migrationFalse = migrationFalse;
    }
}
