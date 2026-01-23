package cc.dreamcode.antylogout.libs.com.mongodb.internal.inject;

import java.util.Optional;
import cc.dreamcode.antylogout.libs.com.mongodb.annotations.ThreadSafe;

@ThreadSafe
public interface OptionalProvider<T>
{
    Optional<T> optional();
}
