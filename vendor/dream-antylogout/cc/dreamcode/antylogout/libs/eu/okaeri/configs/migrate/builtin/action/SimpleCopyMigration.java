package cc.dreamcode.antylogout.libs.eu.okaeri.configs.migrate.builtin.action;

import lombok.Generated;
import java.util.Objects;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.migrate.view.RawConfigView;
import lombok.NonNull;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.OkaeriConfig;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.migrate.ConfigMigration;

public class SimpleCopyMigration implements ConfigMigration
{
    private final String fromKey;
    private final String toKey;
    
    @Override
    public boolean migrate(@NonNull final OkaeriConfig config, @NonNull final RawConfigView view) {
        if (config == null) {
            throw new NullPointerException("config is marked non-null but is null");
        }
        if (view == null) {
            throw new NullPointerException("view is marked non-null but is null");
        }
        if (!view.exists(this.fromKey)) {
            return false;
        }
        final Object targetValue = view.get(this.fromKey);
        final Object oldValue = view.set(this.toKey, targetValue);
        return !Objects.equals(targetValue, oldValue);
    }
    
    @Generated
    @Override
    public String toString() {
        return "SimpleCopyMigration(fromKey=" + this.fromKey + ", toKey=" + this.toKey + ")";
    }
    
    @Generated
    public SimpleCopyMigration(final String fromKey, final String toKey) {
        this.fromKey = fromKey;
        this.toKey = toKey;
    }
}
