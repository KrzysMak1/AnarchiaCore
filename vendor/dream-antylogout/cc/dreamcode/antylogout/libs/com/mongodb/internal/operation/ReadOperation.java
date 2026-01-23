package cc.dreamcode.antylogout.libs.com.mongodb.internal.operation;

import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.ReadBinding;

public interface ReadOperation<T>
{
    T execute(final ReadBinding p0);
}
