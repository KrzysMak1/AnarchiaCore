package cc.dreamcode.antylogout.libs.com.mongodb.internal.operation;

import cc.dreamcode.antylogout.libs.org.bson.BsonValue;
import cc.dreamcode.antylogout.libs.org.bson.BsonString;
import cc.dreamcode.antylogout.libs.org.bson.BsonDocument;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoNamespace;

final class UpdateSearchIndexesOperation extends AbstractWriteSearchIndexOperation
{
    private static final String COMMAND_NAME = "updateSearchIndex";
    private final SearchIndexRequest request;
    
    UpdateSearchIndexesOperation(final MongoNamespace namespace, final SearchIndexRequest request) {
        super(namespace);
        this.request = request;
    }
    
    @Override
    BsonDocument buildCommand() {
        return new BsonDocument("updateSearchIndex", new BsonString(this.getNamespace().getCollectionName())).append("name", new BsonString(this.request.getIndexName())).append("definition", this.request.getDefinition());
    }
}
