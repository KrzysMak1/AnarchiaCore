package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.base;

import java.util.Locale;
import org.jetbrains.annotations.Nullable;
import java.util.stream.Collectors;
import java.util.Arrays;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface XBase<XForm extends XBase<XForm, BukkitForm>, BukkitForm>
{
    @NotNull
    @Contract(pure = true)
    String name();
    
    @ApiStatus.Internal
    @Contract(pure = true)
    String[] getNames();
    
    @NotNull
    @Contract(pure = true)
    default String friendlyName() {
        return (String)Arrays.stream((Object[])this.name().split("_")).map(t -> t.charAt(0) + t.substring(1).toLowerCase(Locale.ENGLISH)).collect(Collectors.joining((CharSequence)" "));
    }
    
    @Nullable
    @Contract(pure = true)
    BukkitForm get();
    
    @Contract(pure = true)
    default boolean isSupported() {
        return this.get() != null;
    }
    
    @NotNull
    @Contract(pure = true)
    default XForm or(final XForm other) {
        return (XForm)(this.isSupported() ? this : other);
    }
    
    @ApiStatus.Internal
    default XModuleMetadata getMetadata() {
        final XRegistry<XForm, BukkitForm> registry = XRegistry.registryOf((Class<? extends XForm>)this.getClass());
        return registry.getOrRegisterMetadata((XForm)this, registry.getBackingField((XForm)this), false);
    }
}
