package cc.dreamcode.antylogout.libs.eu.okaeri.configs.migrate.builtin.action;

import lombok.Generated;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.migrate.view.RawConfigView;
import lombok.NonNull;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.OkaeriConfig;
import java.util.function.Supplier;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.migrate.ConfigMigration;

public class SimpleSupplyMigration implements ConfigMigration
{
    private final String key;
    private final Supplier supplier;
    
    @Override
    public boolean migrate(@NonNull final OkaeriConfig config, @NonNull final RawConfigView view) {
        if (config == null) {
            throw new NullPointerException("config is marked non-null but is null");
        }
        if (view == null) {
            throw new NullPointerException("view is marked non-null but is null");
        }
        if (view.exists(this.key)) {
            return false;
        }
        view.set(this.key, this.supplier.get());
        return true;
    }
    
    @Generated
    @Override
    public String toString() {
        return "SimpleSupplyMigration(key=" + this.key + ", supplier=" + (Object)this.supplier + ")";
    }
    
    @Generated
    public SimpleSupplyMigration(final String key, final Supplier supplier) {
        this.key = key;
        this.supplier = supplier;
    }
}
