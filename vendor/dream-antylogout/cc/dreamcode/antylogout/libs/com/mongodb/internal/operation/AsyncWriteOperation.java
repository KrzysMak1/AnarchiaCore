package cc.dreamcode.antylogout.libs.com.mongodb.internal.operation;

import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.SingleResultCallback;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.AsyncWriteBinding;

public interface AsyncWriteOperation<T>
{
    void executeAsync(final AsyncWriteBinding p0, final SingleResultCallback<T> p1);
}
