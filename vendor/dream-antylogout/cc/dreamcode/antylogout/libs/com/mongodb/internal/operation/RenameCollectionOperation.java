package cc.dreamcode.antylogout.libs.com.mongodb.internal.operation;

import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.Connection;
import cc.dreamcode.antylogout.libs.org.bson.BsonBoolean;
import cc.dreamcode.antylogout.libs.org.bson.BsonValue;
import cc.dreamcode.antylogout.libs.org.bson.BsonString;
import cc.dreamcode.antylogout.libs.org.bson.BsonDocument;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.AsyncConnection;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.ErrorHandlingResultCallback;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.SingleResultCallback;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.AsyncWriteBinding;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.WriteBinding;
import cc.dreamcode.antylogout.libs.com.mongodb.assertions.Assertions;
import cc.dreamcode.antylogout.libs.com.mongodb.lang.Nullable;
import cc.dreamcode.antylogout.libs.com.mongodb.WriteConcern;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoNamespace;

public class RenameCollectionOperation implements AsyncWriteOperation<Void>, WriteOperation<Void>
{
    private final MongoNamespace originalNamespace;
    private final MongoNamespace newNamespace;
    private final WriteConcern writeConcern;
    private boolean dropTarget;
    
    public RenameCollectionOperation(final MongoNamespace originalNamespace, final MongoNamespace newNamespace) {
        this(originalNamespace, newNamespace, null);
    }
    
    public RenameCollectionOperation(final MongoNamespace originalNamespace, final MongoNamespace newNamespace, @Nullable final WriteConcern writeConcern) {
        this.originalNamespace = Assertions.notNull("originalNamespace", originalNamespace);
        this.newNamespace = Assertions.notNull("newNamespace", newNamespace);
        this.writeConcern = writeConcern;
    }
    
    public WriteConcern getWriteConcern() {
        return this.writeConcern;
    }
    
    public boolean isDropTarget() {
        return this.dropTarget;
    }
    
    public RenameCollectionOperation dropTarget(final boolean dropTarget) {
        this.dropTarget = dropTarget;
        return this;
    }
    
    @Override
    public Void execute(final WriteBinding binding) {
        return SyncOperationHelper.withConnection(binding, connection -> SyncOperationHelper.executeCommand(binding, "admin", this.getCommand(), connection, SyncOperationHelper.writeConcernErrorTransformer()));
    }
    
    @Override
    public void executeAsync(final AsyncWriteBinding binding, final SingleResultCallback<Void> callback) {
        AsyncOperationHelper.withAsyncConnection(binding, (connection, t) -> {
            final SingleResultCallback<Void> errHandlingCallback = ErrorHandlingResultCallback.errorHandlingCallback((SingleResultCallback<Void>)callback, OperationHelper.LOGGER);
            if (t != null) {
                errHandlingCallback.onResult(null, t);
            }
            else {
                AsyncOperationHelper.executeCommandAsync(binding, "admin", this.getCommand(), Assertions.assertNotNull(connection), AsyncOperationHelper.writeConcernErrorTransformerAsync(), (SingleResultCallback<Void>)AsyncOperationHelper.releasingCallback((SingleResultCallback<T>)errHandlingCallback, connection));
            }
        });
    }
    
    private BsonDocument getCommand() {
        final BsonDocument commandDocument = new BsonDocument("renameCollection", new BsonString(this.originalNamespace.getFullName())).append("to", new BsonString(this.newNamespace.getFullName())).append("dropTarget", BsonBoolean.valueOf(this.dropTarget));
        WriteConcernHelper.appendWriteConcernToCommand(this.writeConcern, commandDocument);
        return commandDocument;
    }
}
