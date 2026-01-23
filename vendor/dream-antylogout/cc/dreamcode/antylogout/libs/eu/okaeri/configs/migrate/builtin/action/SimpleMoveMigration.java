package cc.dreamcode.antylogout.libs.eu.okaeri.configs.migrate.builtin.action;

import lombok.Generated;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.migrate.view.RawConfigView;
import lombok.NonNull;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.OkaeriConfig;
import java.util.function.Function;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.migrate.ConfigMigration;

public class SimpleMoveMigration implements ConfigMigration
{
    private final String fromKey;
    private final String toKey;
    private Function<Object, Object> updateFunction;
    
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
        final Object targetValue = view.remove(this.fromKey);
        if (this.updateFunction == null) {
            view.set(this.toKey, targetValue);
            return true;
        }
        final Object updatedValue = this.updateFunction.apply(targetValue);
        view.set(this.toKey, updatedValue);
        return true;
    }
    
    @Generated
    @Override
    public String toString() {
        return "SimpleMoveMigration(fromKey=" + this.fromKey + ", toKey=" + this.toKey + ", updateFunction=" + (Object)this.updateFunction + ")";
    }
    
    @Generated
    public SimpleMoveMigration(final String fromKey, final String toKey, final Function<Object, Object> updateFunction) {
        this.fromKey = fromKey;
        this.toKey = toKey;
        this.updateFunction = updateFunction;
    }
    
    @Generated
    public SimpleMoveMigration(final String fromKey, final String toKey) {
        this.fromKey = fromKey;
        this.toKey = toKey;
    }
}
