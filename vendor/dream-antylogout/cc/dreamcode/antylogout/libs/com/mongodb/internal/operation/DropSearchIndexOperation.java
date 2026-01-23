package cc.dreamcode.antylogout.libs.com.mongodb.internal.operation;

import cc.dreamcode.antylogout.libs.org.bson.BsonValue;
import cc.dreamcode.antylogout.libs.org.bson.BsonString;
import cc.dreamcode.antylogout.libs.org.bson.BsonDocument;
import cc.dreamcode.antylogout.libs.com.mongodb.lang.Nullable;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoNamespace;

final class DropSearchIndexOperation extends AbstractWriteSearchIndexOperation
{
    private static final String COMMAND_NAME = "dropSearchIndex";
    private final String indexName;
    
    DropSearchIndexOperation(final MongoNamespace namespace, final String indexName) {
        super(namespace);
        this.indexName = indexName;
    }
    
    @Override
     <E extends Throwable> void swallowOrThrow(@Nullable final E mongoExecutionException) throws E, Throwable {
        if (mongoExecutionException != null && !CommandOperationHelper.isNamespaceError(mongoExecutionException)) {
            throw mongoExecutionException;
        }
    }
    
    @Override
    BsonDocument buildCommand() {
        return new BsonDocument("dropSearchIndex", new BsonString(this.getNamespace().getCollectionName())).append("name", new BsonString(this.indexName));
    }
}
