package cc.dreamcode.antylogout.libs.com.mongodb.internal.operation;

import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.Connection;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.AsyncConnection;
import cc.dreamcode.antylogout.libs.org.bson.BsonValue;
import cc.dreamcode.antylogout.libs.org.bson.BsonInt32;
import cc.dreamcode.antylogout.libs.org.bson.BsonDocument;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.ErrorHandlingResultCallback;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.SingleResultCallback;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.AsyncWriteBinding;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.WriteBinding;
import cc.dreamcode.antylogout.libs.com.mongodb.assertions.Assertions;
import cc.dreamcode.antylogout.libs.com.mongodb.lang.Nullable;
import cc.dreamcode.antylogout.libs.com.mongodb.WriteConcern;

public class DropDatabaseOperation implements AsyncWriteOperation<Void>, WriteOperation<Void>
{
    private final String databaseName;
    private final WriteConcern writeConcern;
    
    public DropDatabaseOperation(final String databaseName) {
        this(databaseName, null);
    }
    
    public DropDatabaseOperation(final String databaseName, @Nullable final WriteConcern writeConcern) {
        this.databaseName = Assertions.notNull("databaseName", databaseName);
        this.writeConcern = writeConcern;
    }
    
    public WriteConcern getWriteConcern() {
        return this.writeConcern;
    }
    
    @Override
    public Void execute(final WriteBinding binding) {
        return SyncOperationHelper.withConnection(binding, connection -> {
            SyncOperationHelper.executeCommand(binding, this.databaseName, this.getCommand(), connection, SyncOperationHelper.writeConcernErrorTransformer());
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
                AsyncOperationHelper.executeCommandAsync(binding, this.databaseName, this.getCommand(), connection, AsyncOperationHelper.writeConcernErrorTransformerAsync(), (SingleResultCallback<Void>)AsyncOperationHelper.releasingCallback((SingleResultCallback<T>)errHandlingCallback, connection));
            }
        });
    }
    
    private BsonDocument getCommand() {
        final BsonDocument commandDocument = new BsonDocument("dropDatabase", new BsonInt32(1));
        WriteConcernHelper.appendWriteConcernToCommand(this.writeConcern, commandDocument);
        return commandDocument;
    }
}
