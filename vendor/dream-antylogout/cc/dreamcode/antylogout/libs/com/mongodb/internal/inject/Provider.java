package cc.dreamcode.antylogout.libs.com.mongodb.internal.inject;

import java.util.Optional;
import cc.dreamcode.antylogout.libs.com.mongodb.annotations.ThreadSafe;
import java.util.function.Supplier;

@ThreadSafe
public interface Provider<T> extends OptionalProvider<T>, Supplier<T>
{
    T get();
    
    Optional<T> optional();
}
