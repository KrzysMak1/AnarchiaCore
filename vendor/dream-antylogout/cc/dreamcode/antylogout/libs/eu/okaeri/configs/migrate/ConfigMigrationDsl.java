package cc.dreamcode.antylogout.libs.eu.okaeri.configs.migrate;

import cc.dreamcode.antylogout.libs.eu.okaeri.configs.migrate.builtin.special.SimplePredicateMigration;
import java.util.function.Predicate;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.migrate.builtin.special.SimpleNotMigration;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.migrate.builtin.special.SimpleNoopMigration;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.migrate.builtin.special.SimpleMultiMigration;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.migrate.builtin.special.SimpleExistsMigration;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.migrate.builtin.special.SimpleConditionalMigration;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.migrate.builtin.action.SimpleUpdateMigration;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.migrate.builtin.action.SimpleSupplyMigration;
import java.util.function.Supplier;
import java.util.function.Function;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.migrate.builtin.action.SimpleMoveMigration;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.migrate.builtin.action.SimpleDeleteMigration;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.migrate.builtin.action.SimpleCopyMigration;
import lombok.NonNull;

public interface ConfigMigrationDsl
{
    default ConfigMigration copy(@NonNull final String fromKey, @NonNull final String toKey) {
        if (fromKey == null) {
            throw new NullPointerException("fromKey is marked non-null but is null");
        }
        if (toKey == null) {
            throw new NullPointerException("toKey is marked non-null but is null");
        }
        return new SimpleCopyMigration(fromKey, toKey);
    }
    
    default ConfigMigration delete(@NonNull final String key) {
        if (key == null) {
            throw new NullPointerException("key is marked non-null but is null");
        }
        return new SimpleDeleteMigration(key);
    }
    
    default ConfigMigration move(@NonNull final String fromKey, @NonNull final String toKey) {
        if (fromKey == null) {
            throw new NullPointerException("fromKey is marked non-null but is null");
        }
        if (toKey == null) {
            throw new NullPointerException("toKey is marked non-null but is null");
        }
        return new SimpleMoveMigration(fromKey, toKey);
    }
    
    default ConfigMigration move(@NonNull final String fromKey, @NonNull final String toKey, @NonNull final Function<Object, Object> updateFunction) {
        if (fromKey == null) {
            throw new NullPointerException("fromKey is marked non-null but is null");
        }
        if (toKey == null) {
            throw new NullPointerException("toKey is marked non-null but is null");
        }
        if (updateFunction == null) {
            throw new NullPointerException("updateFunction is marked non-null but is null");
        }
        return new SimpleMoveMigration(fromKey, toKey, updateFunction);
    }
    
    default ConfigMigration supply(@NonNull final String key, @NonNull final Supplier supplier) {
        if (key == null) {
            throw new NullPointerException("key is marked non-null but is null");
        }
        if (supplier == null) {
            throw new NullPointerException("supplier is marked non-null but is null");
        }
        return new SimpleSupplyMigration(key, supplier);
    }
    
    default ConfigMigration update(@NonNull final String key, @NonNull final Function<Object, Object> function) {
        if (key == null) {
            throw new NullPointerException("key is marked non-null but is null");
        }
        if (function == null) {
            throw new NullPointerException("function is marked non-null but is null");
        }
        return new SimpleUpdateMigration(key, function);
    }
    
    default ConfigMigration when(@NonNull final ConfigMigration when, @NonNull final ConfigMigration migrationTrue, @NonNull final ConfigMigration migrationFalse) {
        if (when == null) {
            throw new NullPointerException("when is marked non-null but is null");
        }
        if (migrationTrue == null) {
            throw new NullPointerException("migrationTrue is marked non-null but is null");
        }
        if (migrationFalse == null) {
            throw new NullPointerException("migrationFalse is marked non-null but is null");
        }
        return new SimpleConditionalMigration(when, migrationTrue, migrationFalse);
    }
    
    default ConfigMigration when(@NonNull final ConfigMigration when, @NonNull final ConfigMigration migrationTrue) {
        if (when == null) {
            throw new NullPointerException("when is marked non-null but is null");
        }
        if (migrationTrue == null) {
            throw new NullPointerException("migrationTrue is marked non-null but is null");
        }
        return new SimpleConditionalMigration(when, migrationTrue, noop(false));
    }
    
    default ConfigMigration exists(@NonNull final String key) {
        if (key == null) {
            throw new NullPointerException("key is marked non-null but is null");
        }
        return new SimpleExistsMigration(key);
    }
    
    default ConfigMigration multi(@NonNull final ConfigMigration... migrations) {
        if (migrations == null) {
            throw new NullPointerException("migrations is marked non-null but is null");
        }
        return new SimpleMultiMigration(migrations);
    }
    
    default ConfigMigration any(@NonNull final ConfigMigration... migrations) {
        if (migrations == null) {
            throw new NullPointerException("migrations is marked non-null but is null");
        }
        return new SimpleMultiMigration(migrations);
    }
    
    default ConfigMigration all(@NonNull final ConfigMigration... migrations) {
        if (migrations == null) {
            throw new NullPointerException("migrations is marked non-null but is null");
        }
        return new SimpleMultiMigration(migrations, true);
    }
    
    default ConfigMigration noop(final boolean result) {
        return new SimpleNoopMigration(result);
    }
    
    default ConfigMigration not(@NonNull final ConfigMigration migration) {
        if (migration == null) {
            throw new NullPointerException("migration is marked non-null but is null");
        }
        return new SimpleNotMigration(migration);
    }
    
    default <T> ConfigMigration match(@NonNull final String key, @NonNull final Predicate<T> predicate) {
        if (key == null) {
            throw new NullPointerException("key is marked non-null but is null");
        }
        if (predicate == null) {
            throw new NullPointerException("predicate is marked non-null but is null");
        }
        return new SimplePredicateMigration<Object>(key, predicate);
    }
}
