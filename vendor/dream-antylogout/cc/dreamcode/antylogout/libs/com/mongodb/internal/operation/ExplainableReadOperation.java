package cc.dreamcode.antylogout.libs.com.mongodb.internal.operation;

import cc.dreamcode.antylogout.libs.org.bson.codecs.Decoder;
import cc.dreamcode.antylogout.libs.com.mongodb.lang.Nullable;
import cc.dreamcode.antylogout.libs.com.mongodb.ExplainVerbosity;

public interface ExplainableReadOperation<T> extends ReadOperation<T>
{
     <R> ReadOperation<R> asExplainableOperation(@Nullable final ExplainVerbosity p0, final Decoder<R> p1);
}
