package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.proxy;

import java.lang.reflect.Array;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.proxy.annotations.Ignore;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public interface ReflectiveProxyObject
{
    @Ignore
    @NotNull
    @ApiStatus.NonExtendable
    @Contract(pure = true)
    Object instance();
    
    @Ignore
    @NotNull
    @ApiStatus.NonExtendable
    @Contract(pure = true)
    Class<?> getTargetClass();
    
    @Ignore
    @NotNull
    @ApiStatus.NonExtendable
    @Contract(pure = true)
    default boolean isInstance(@Nullable final Object object) {
        return this.getTargetClass().isInstance(object.getClass());
    }
    
    @Ignore
    @NotNull
    @ApiStatus.NonExtendable
    @Contract(pure = true)
    default Object[] newArray(final int length) {
        return (Object[])Array.newInstance((Class)this.getTargetClass(), length);
    }
    
    @Ignore
    @NotNull
    @ApiStatus.NonExtendable
    @Contract(pure = true)
    default Object[] newArray(final int... dimensions) {
        return (Object[])Array.newInstance((Class)this.getTargetClass(), dimensions);
    }
    
    @Ignore
    @NotNull
    @ApiStatus.OverrideOnly
    @Contract(value = "_ -> new", pure = true)
    ReflectiveProxyObject bindTo(@NotNull final Object p0);
}
