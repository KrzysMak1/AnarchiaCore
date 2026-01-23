package cc.dreamcode.antylogout.libs.eu.okaeri.configs.migrate.builtin.special;

import lombok.Generated;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.migrate.view.RawConfigView;
import lombok.NonNull;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.OkaeriConfig;
import java.util.function.Predicate;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.migrate.ConfigMigration;

public class SimplePredicateMigration<T> implements ConfigMigration
{
    private final String key;
    private final Predicate<T> predicate;
    
    @Override
    public boolean migrate(@NonNull final OkaeriConfig config, @NonNull final RawConfigView view) {
        if (config == null) {
            throw new NullPointerException("config is marked non-null but is null");
        }
        if (view == null) {
            throw new NullPointerException("view is marked non-null but is null");
        }
        if (!view.exists(this.key)) {
            return false;
        }
        final T value = (T)view.get(this.key);
        return this.predicate.test((Object)value);
    }
    
    @Generated
    @Override
    public String toString() {
        return "SimplePredicateMigration(key=" + this.key + ", predicate=" + (Object)this.predicate + ")";
    }
    
    @Generated
    public SimplePredicateMigration(final String key, final Predicate<T> predicate) {
        this.key = key;
        this.predicate = predicate;
    }
}
