package cc.dreamcode.antylogout.libs.com.mongodb.internal.operation;

import cc.dreamcode.antylogout.libs.com.mongodb.ServerCursor;
import cc.dreamcode.antylogout.libs.com.mongodb.assertions.Assertions;
import cc.dreamcode.antylogout.libs.org.bson.BsonDocument;
import cc.dreamcode.antylogout.libs.com.mongodb.lang.Nullable;
import cc.dreamcode.antylogout.libs.org.bson.BsonTimestamp;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoNamespace;
import java.util.List;
import cc.dreamcode.antylogout.libs.com.mongodb.ServerAddress;

public class CommandCursorResult<T>
{
    private static final String CURSOR = "cursor";
    private static final String POST_BATCH_RESUME_TOKEN = "postBatchResumeToken";
    private static final String OPERATION_TIME = "operationTime";
    private final ServerAddress serverAddress;
    private final List<T> results;
    private final MongoNamespace namespace;
    private final long cursorId;
    @Nullable
    private final BsonTimestamp operationTime;
    @Nullable
    private final BsonDocument postBatchResumeToken;
    
    public CommandCursorResult(final ServerAddress serverAddress, final String fieldNameContainingBatch, final BsonDocument commandCursorDocument) {
        Assertions.isTrue("Contains cursor", commandCursorDocument.isDocument("cursor"));
        this.serverAddress = serverAddress;
        final BsonDocument cursorDocument = commandCursorDocument.getDocument("cursor");
        this.results = BsonDocumentWrapperHelper.toList(cursorDocument, fieldNameContainingBatch);
        this.namespace = new MongoNamespace(cursorDocument.getString("ns").getValue());
        this.cursorId = cursorDocument.getNumber("id").longValue();
        this.operationTime = cursorDocument.getTimestamp("operationTime", null);
        this.postBatchResumeToken = cursorDocument.getDocument("postBatchResumeToken", null);
    }
    
    public MongoNamespace getNamespace() {
        return this.namespace;
    }
    
    @Nullable
    public ServerCursor getServerCursor() {
        return (this.cursorId == 0L) ? null : new ServerCursor(this.cursorId, this.serverAddress);
    }
    
    public List<T> getResults() {
        return this.results;
    }
    
    public ServerAddress getServerAddress() {
        return this.serverAddress;
    }
    
    public long getCursorId() {
        return this.cursorId;
    }
    
    @Nullable
    public BsonDocument getPostBatchResumeToken() {
        return this.postBatchResumeToken;
    }
    
    @Nullable
    public BsonTimestamp getOperationTime() {
        return this.operationTime;
    }
}
