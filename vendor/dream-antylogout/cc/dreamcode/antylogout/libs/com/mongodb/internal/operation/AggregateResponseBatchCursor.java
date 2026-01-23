package cc.dreamcode.antylogout.libs.com.mongodb.internal.operation;

import cc.dreamcode.antylogout.libs.org.bson.BsonTimestamp;
import cc.dreamcode.antylogout.libs.com.mongodb.lang.Nullable;
import cc.dreamcode.antylogout.libs.org.bson.BsonDocument;
import cc.dreamcode.antylogout.libs.com.mongodb.annotations.NotThreadSafe;

@NotThreadSafe
public interface AggregateResponseBatchCursor<T> extends BatchCursor<T>
{
    @Nullable
    BsonDocument getPostBatchResumeToken();
    
    @Nullable
    BsonTimestamp getOperationTime();
    
    boolean isFirstBatchEmpty();
    
    int getMaxWireVersion();
}
