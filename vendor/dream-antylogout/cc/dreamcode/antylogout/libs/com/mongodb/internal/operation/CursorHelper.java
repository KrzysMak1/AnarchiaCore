package cc.dreamcode.antylogout.libs.com.mongodb.internal.operation;

import cc.dreamcode.antylogout.libs.org.bson.BsonValue;
import cc.dreamcode.antylogout.libs.org.bson.BsonInt32;
import cc.dreamcode.antylogout.libs.org.bson.BsonDocument;
import cc.dreamcode.antylogout.libs.com.mongodb.lang.Nullable;

final class CursorHelper
{
    static BsonDocument getCursorDocumentFromBatchSize(@Nullable final Integer batchSize) {
        return (batchSize == null) ? new BsonDocument() : new BsonDocument("batchSize", new BsonInt32(batchSize));
    }
    
    private CursorHelper() {
    }
}
