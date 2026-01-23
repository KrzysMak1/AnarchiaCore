package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection;

import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm.objects.ReflectedObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.ApiStatus;

public interface ReflectiveHandle<T>
{
    @ApiStatus.Experimental
    @Contract(value = "-> new", pure = true)
    ReflectiveHandle<T> copy();
    
    @Contract(pure = true)
    default boolean exists() {
        try {
            this.reflect();
            return true;
        }
        catch (final ReflectiveOperationException ignored) {
            return false;
        }
    }
    
    @Deprecated
    @Nullable
    @ApiStatus.Obsolete
    default ReflectiveOperationException catchError() {
        try {
            this.reflect();
            return null;
        }
        catch (final ReflectiveOperationException ex) {
            return ex;
        }
    }
    
    @NotNull
    @Contract(pure = true)
    default T unreflect() {
        try {
            return this.reflect();
        }
        catch (final ReflectiveOperationException e) {
            throw XReflection.throwCheckedException((Throwable)e);
        }
    }
    
    @Nullable
    @Contract(pure = true)
    default T reflectOrNull() {
        try {
            return this.reflect();
        }
        catch (final ReflectiveOperationException ignored) {
            return null;
        }
    }
    
    @NotNull
    @Contract(pure = true)
    T reflect() throws ReflectiveOperationException;
    
    @NotNull
    @ApiStatus.Experimental
    @Contract(pure = true)
    ReflectiveHandle<ReflectedObject> jvm();
    
    @NotNull
    @ApiStatus.Experimental
    @Contract(value = "-> new", pure = true)
    default ReflectiveHandle<T> cached() {
        return new CachedReflectiveHandle<T>(this.copy());
    }
    
    @NotNull
    @ApiStatus.Experimental
    @Contract(pure = true)
    default ReflectiveHandle<T> unwrap() {
        if (this instanceof CachedReflectiveHandle) {
            return ((CachedReflectiveHandle)this).getDelegate();
        }
        return this;
    }
}
