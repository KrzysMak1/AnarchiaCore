package cc.dreamcode.antylogout.libs.com.mongodb.internal.operation;

import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.WriteBinding;

public interface WriteOperation<T>
{
    T execute(final WriteBinding p0);
}
