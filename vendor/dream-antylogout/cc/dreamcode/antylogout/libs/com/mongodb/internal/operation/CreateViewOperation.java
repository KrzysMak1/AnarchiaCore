package cc.dreamcode.antylogout.libs.com.mongodb.internal.operation;

import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.Connection;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.AsyncConnection;
import cc.dreamcode.antylogout.libs.org.bson.BsonArray;
import cc.dreamcode.antylogout.libs.org.bson.BsonValue;
import cc.dreamcode.antylogout.libs.org.bson.BsonString;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.ErrorHandlingResultCallback;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.SingleResultCallback;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.AsyncWriteBinding;
import cc.dreamcode.antylogout.libs.org.bson.codecs.Decoder;
import cc.dreamcode.antylogout.libs.org.bson.codecs.BsonDocumentCodec;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.WriteBinding;
import cc.dreamcode.antylogout.libs.com.mongodb.lang.Nullable;
import cc.dreamcode.antylogout.libs.com.mongodb.assertions.Assertions;
import cc.dreamcode.antylogout.libs.com.mongodb.client.model.Collation;
import cc.dreamcode.antylogout.libs.com.mongodb.WriteConcern;
import cc.dreamcode.antylogout.libs.org.bson.BsonDocument;
import java.util.List;

public class CreateViewOperation implements AsyncWriteOperation<Void>, WriteOperation<Void>
{
    private final String databaseName;
    private final String viewName;
    private final String viewOn;
    private final List<BsonDocument> pipeline;
    private final WriteConcern writeConcern;
    private Collation collation;
    
    public CreateViewOperation(final String databaseName, final String viewName, final String viewOn, final List<BsonDocument> pipeline, final WriteConcern writeConcern) {
        this.databaseName = Assertions.notNull("databaseName", databaseName);
        this.viewName = Assertions.notNull("viewName", viewName);
        this.viewOn = Assertions.notNull("viewOn", viewOn);
        this.pipeline = Assertions.notNull("pipeline", pipeline);
        this.writeConcern = Assertions.notNull("writeConcern", writeConcern);
    }
    
    public String getDatabaseName() {
        return this.databaseName;
    }
    
    public String getViewName() {
        return this.viewName;
    }
    
    public String getViewOn() {
        return this.viewOn;
    }
    
    public List<BsonDocument> getPipeline() {
        return this.pipeline;
    }
    
    public WriteConcern getWriteConcern() {
        return this.writeConcern;
    }
    
    public Collation getCollation() {
        return this.collation;
    }
    
    public CreateViewOperation collation(@Nullable final Collation collation) {
        this.collation = collation;
        return this;
    }
    
    @Override
    public Void execute(final WriteBinding binding) {
        return SyncOperationHelper.withConnection(binding, connection -> {
            SyncOperationHelper.executeCommand(binding, this.databaseName, this.getCommand(), new BsonDocumentCodec(), SyncOperationHelper.writeConcernErrorTransformer());
            return null;
        });
    }
    
    @Override
    public void executeAsync(final AsyncWriteBinding binding, final SingleResultCallback<Void> callback) {
        AsyncOperationHelper.withAsyncConnection(binding, (connection, t) -> {
            final SingleResultCallback<Void> errHandlingCallback = ErrorHandlingResultCallback.errorHandlingCallback((SingleResultCallback<Void>)callback, OperationHelper.LOGGER);
            if (t != null) {
                errHandlingCallback.onResult(null, t);
            }
            else {
                final SingleResultCallback<Void> wrappedCallback = AsyncOperationHelper.releasingCallback(errHandlingCallback, connection);
                AsyncOperationHelper.executeCommandAsync(binding, this.databaseName, this.getCommand(), connection, AsyncOperationHelper.writeConcernErrorTransformerAsync(), wrappedCallback);
            }
        });
    }
    
    private BsonDocument getCommand() {
        final BsonDocument commandDocument = new BsonDocument("create", new BsonString(this.viewName)).append("viewOn", new BsonString(this.viewOn)).append("pipeline", new BsonArray(this.pipeline));
        if (this.collation != null) {
            commandDocument.put("collation", this.collation.asDocument());
        }
        WriteConcernHelper.appendWriteConcernToCommand(this.writeConcern, commandDocument);
        return commandDocument;
    }
}
