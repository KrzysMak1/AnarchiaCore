package cc.dreamcode.antylogout.libs.eu.okaeri.configs.migrate.builtin.action;

import lombok.Generated;
import java.util.Objects;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.migrate.view.RawConfigView;
import lombok.NonNull;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.OkaeriConfig;
import java.util.function.Function;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.migrate.ConfigMigration;

public class SimpleUpdateMigration implements ConfigMigration
{
    private final String key;
    private final Function<Object, Object> function;
    
    @Override
    public boolean migrate(@NonNull final OkaeriConfig config, @NonNull final RawConfigView view) {
        if (config == null) {
            throw new NullPointerException("config is marked non-null but is null");
        }
        if (view == null) {
            throw new NullPointerException("view is marked non-null but is null");
        }
        final Object oldValue = view.get(this.key);
        final Object newValue = this.function.apply(oldValue);
        view.set(this.key, newValue);
        return !Objects.equals(oldValue, newValue);
    }
    
    @Generated
    @Override
    public String toString() {
        return "SimpleUpdateMigration(key=" + this.key + ", function=" + (Object)this.function + ")";
    }
    
    @Generated
    public SimpleUpdateMigration(final String key, final Function<Object, Object> function) {
        this.key = key;
        this.function = function;
    }
}
