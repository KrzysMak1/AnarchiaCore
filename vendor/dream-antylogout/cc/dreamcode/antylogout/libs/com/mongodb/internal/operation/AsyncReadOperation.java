package cc.dreamcode.antylogout.libs.com.mongodb.internal.operation;

import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.SingleResultCallback;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.AsyncReadBinding;

public interface AsyncReadOperation<T>
{
    void executeAsync(final AsyncReadBinding p0, final SingleResultCallback<T> p1);
}
