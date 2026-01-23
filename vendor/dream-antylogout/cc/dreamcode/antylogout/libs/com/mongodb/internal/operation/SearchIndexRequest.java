package cc.dreamcode.antylogout.libs.com.mongodb.internal.operation;

import cc.dreamcode.antylogout.libs.com.mongodb.assertions.Assertions;
import cc.dreamcode.antylogout.libs.com.mongodb.lang.Nullable;
import cc.dreamcode.antylogout.libs.org.bson.BsonDocument;

final class SearchIndexRequest
{
    private final BsonDocument definition;
    @Nullable
    private final String indexName;
    
    SearchIndexRequest(final BsonDocument definition, @Nullable final String indexName) {
        Assertions.assertNotNull(definition);
        this.definition = definition;
        this.indexName = indexName;
    }
    
    public BsonDocument getDefinition() {
        return this.definition;
    }
    
    @Nullable
    public String getIndexName() {
        return this.indexName;
    }
}
