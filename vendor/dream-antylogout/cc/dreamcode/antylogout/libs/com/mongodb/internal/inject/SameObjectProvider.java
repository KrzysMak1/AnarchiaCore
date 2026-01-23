package cc.dreamcode.antylogout.libs.com.mongodb.internal.inject;

import java.util.Optional;
import cc.dreamcode.antylogout.libs.com.mongodb.assertions.Assertions;
import cc.dreamcode.antylogout.libs.com.mongodb.lang.Nullable;
import java.util.concurrent.atomic.AtomicReference;
import cc.dreamcode.antylogout.libs.com.mongodb.annotations.ThreadSafe;

@ThreadSafe
public final class SameObjectProvider<T> implements Provider<T>
{
    private final AtomicReference<T> object;
    
    private SameObjectProvider(@Nullable final T o) {
        this.object = (AtomicReference<T>)new AtomicReference();
        if (o != null) {
            this.initialize(o);
        }
    }
    
    @Override
    public T get() {
        return Assertions.assertNotNull(this.object.get());
    }
    
    @Override
    public Optional<T> optional() {
        return (Optional<T>)Optional.of(this.get());
    }
    
    public void initialize(final T o) {
        Assertions.assertTrue(this.object.compareAndSet((Object)null, (Object)o));
    }
    
    public static <T> SameObjectProvider<T> initialized(final T o) {
        return new SameObjectProvider<T>(o);
    }
    
    public static <T> SameObjectProvider<T> uninitialized() {
        return new SameObjectProvider<T>(null);
    }
}
