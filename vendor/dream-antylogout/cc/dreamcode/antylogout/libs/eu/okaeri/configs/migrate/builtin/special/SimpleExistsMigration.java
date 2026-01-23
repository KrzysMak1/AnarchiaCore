package cc.dreamcode.antylogout.libs.eu.okaeri.configs.migrate.builtin.special;

import lombok.Generated;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.migrate.view.RawConfigView;
import lombok.NonNull;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.OkaeriConfig;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.migrate.ConfigMigration;

public class SimpleExistsMigration implements ConfigMigration
{
    private final String key;
    
    @Override
    public boolean migrate(@NonNull final OkaeriConfig config, @NonNull final RawConfigView view) {
        if (config == null) {
            throw new NullPointerException("config is marked non-null but is null");
        }
        if (view == null) {
            throw new NullPointerException("view is marked non-null but is null");
        }
        return view.exists(this.key);
    }
    
    @Generated
    @Override
    public String toString() {
        return "SimpleExistsMigration(key=" + this.key + ")";
    }
    
    @Generated
    public SimpleExistsMigration(final String key) {
        this.key = key;
    }
}
