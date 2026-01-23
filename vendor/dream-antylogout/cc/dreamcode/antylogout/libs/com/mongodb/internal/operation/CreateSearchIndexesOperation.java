package cc.dreamcode.antylogout.libs.com.mongodb.internal.operation;

import cc.dreamcode.antylogout.libs.org.bson.BsonValue;
import cc.dreamcode.antylogout.libs.org.bson.BsonString;
import cc.dreamcode.antylogout.libs.org.bson.BsonDocument;
import java.util.stream.Collectors;
import cc.dreamcode.antylogout.libs.org.bson.BsonArray;
import cc.dreamcode.antylogout.libs.com.mongodb.assertions.Assertions;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoNamespace;
import java.util.List;

final class CreateSearchIndexesOperation extends AbstractWriteSearchIndexOperation
{
    private static final String COMMAND_NAME = "createSearchIndexes";
    private final List<SearchIndexRequest> indexRequests;
    
    CreateSearchIndexesOperation(final MongoNamespace namespace, final List<SearchIndexRequest> indexRequests) {
        super(namespace);
        this.indexRequests = Assertions.assertNotNull(indexRequests);
    }
    
    private static BsonArray convert(final List<SearchIndexRequest> requests) {
        return (BsonArray)requests.stream().map(CreateSearchIndexesOperation::convert).collect(Collectors.toCollection(BsonArray::new));
    }
    
    private static BsonDocument convert(final SearchIndexRequest request) {
        final BsonDocument bsonIndexRequest = new BsonDocument();
        final String searchIndexName = request.getIndexName();
        if (searchIndexName != null) {
            bsonIndexRequest.append("name", new BsonString(searchIndexName));
        }
        bsonIndexRequest.append("definition", request.getDefinition());
        return bsonIndexRequest;
    }
    
    @Override
    BsonDocument buildCommand() {
        return new BsonDocument("createSearchIndexes", new BsonString(this.getNamespace().getCollectionName())).append("indexes", convert(this.indexRequests));
    }
}
