package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.base;

import org.jetbrains.annotations.Nullable;
import java.util.Arrays;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.ApiStatus;

public abstract class XModule<XForm extends XModule<XForm, BukkitForm>, BukkitForm> implements XBase<XForm, BukkitForm>
{
    private final BukkitForm bukkitForm;
    private final String[] names;
    
    @ApiStatus.Internal
    protected XModule(final BukkitForm bukkitForm, final String[] names) {
        this.bukkitForm = bukkitForm;
        this.names = names;
    }
    
    @NotNull
    @Override
    public final String name() {
        return this.names[0];
    }
    
    @ApiStatus.Experimental
    protected void setEnumName(final XRegistry<XForm, BukkitForm> registry, final String enumName) {
        if (this.names[0] != null) {
            throw new IllegalStateException("Enum name already set " + enumName + " -> " + Arrays.toString((Object[])this.names));
        }
        this.names[0] = enumName;
        final BukkitForm newForm = registry.getBukkit(this.names);
        if (this.bukkitForm != newForm) {
            registry.std((XForm)this);
        }
    }
    
    @ApiStatus.Internal
    @Override
    public String[] getNames() {
        return this.names;
    }
    
    @Nullable
    @Override
    public final BukkitForm get() {
        return this.bukkitForm;
    }
    
    @Override
    public final String toString() {
        return (this.isSupported() ? "" : "!") + this.getClass().getSimpleName() + '(' + this.name() + ')';
    }
    
    @Override
    public final int hashCode() {
        return super.hashCode();
    }
    
    @Deprecated
    @Override
    public final boolean equals(final Object obj) {
        return super.equals(obj);
    }
}
