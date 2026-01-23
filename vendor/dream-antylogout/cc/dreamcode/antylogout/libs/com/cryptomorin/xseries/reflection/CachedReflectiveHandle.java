package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection;

import org.jetbrains.annotations.NotNull;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm.objects.ReflectedObject;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
class CachedReflectiveHandle<T> implements ReflectiveHandle<T>
{
    private final ReflectiveHandle<T> delegate;
    private T cache;
    private CachedReflectiveHandle<ReflectedObject> jvm;
    
    CachedReflectiveHandle(final ReflectiveHandle<T> delegate) {
        this.delegate = delegate;
    }
    
    public ReflectiveHandle<T> getDelegate() {
        return this.delegate;
    }
    
    @Override
    public ReflectiveHandle<T> copy() {
        return this.delegate.copy();
    }
    
    @NotNull
    @Override
    public T reflect() throws ReflectiveOperationException {
        if (this.cache == null) {
            return this.cache = this.delegate.reflect();
        }
        return this.cache;
    }
    
    @NotNull
    @Override
    public ReflectiveHandle<ReflectedObject> jvm() {
        if (this.jvm == null) {
            return this.jvm = new CachedReflectiveHandle<ReflectedObject>(this.delegate.jvm());
        }
        return this.jvm;
    }
}
