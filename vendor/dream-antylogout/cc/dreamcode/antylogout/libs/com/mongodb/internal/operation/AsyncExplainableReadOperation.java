package cc.dreamcode.antylogout.libs.com.mongodb.internal.operation;

import cc.dreamcode.antylogout.libs.org.bson.codecs.Decoder;
import cc.dreamcode.antylogout.libs.com.mongodb.lang.Nullable;
import cc.dreamcode.antylogout.libs.com.mongodb.ExplainVerbosity;

public interface AsyncExplainableReadOperation<T> extends AsyncReadOperation<T>
{
     <R> AsyncReadOperation<R> asAsyncExplainableOperation(@Nullable final ExplainVerbosity p0, final Decoder<R> p1);
}
