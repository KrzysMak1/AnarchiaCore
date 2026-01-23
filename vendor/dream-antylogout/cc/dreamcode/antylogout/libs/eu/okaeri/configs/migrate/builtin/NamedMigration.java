package cc.dreamcode.antylogout.libs.eu.okaeri.configs.migrate.builtin;

import lombok.Generated;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.migrate.ConfigMigration;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.migrate.builtin.special.SimpleMultiMigration;

public class NamedMigration extends SimpleMultiMigration
{
    private final String description;
    
    public NamedMigration(final String description, final ConfigMigration... migrations) {
        super(migrations);
        this.description = description;
    }
    
    @Generated
    @Override
    public String toString() {
        return "NamedMigration(description=" + this.getDescription() + ")";
    }
    
    @Generated
    public String getDescription() {
        return this.description;
    }
}
