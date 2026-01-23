package cc.dreamcode.antylogout.libs.com.mongodb.internal.operation;

import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.Connection;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.AsyncConnection;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.AsyncConnectionSource;
import cc.dreamcode.antylogout.libs.org.bson.BsonDocument;
import cc.dreamcode.antylogout.libs.com.mongodb.lang.Nullable;
import java.util.Objects;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.SingleResultCallback;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.AsyncWriteBinding;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoCommandException;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.WriteBinding;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoNamespace;

abstract class AbstractWriteSearchIndexOperation implements AsyncWriteOperation<Void>, WriteOperation<Void>
{
    private final MongoNamespace namespace;
    
    AbstractWriteSearchIndexOperation(final MongoNamespace mongoNamespace) {
        this.namespace = mongoNamespace;
    }
    
    @Override
    public Void execute(final WriteBinding binding) {
        return SyncOperationHelper.withConnection(binding, connection -> {
            try {
                SyncOperationHelper.executeCommand(binding, this.namespace.getDatabaseName(), this.buildCommand(), connection, SyncOperationHelper.writeConcernErrorTransformer());
            }
            catch (final MongoCommandException mongoCommandException) {
                this.swallowOrThrow(mongoCommandException);
            }
            return null;
        });
    }
    
    @Override
    public void executeAsync(final AsyncWriteBinding binding, final SingleResultCallback<Void> callback) {
        Objects.requireNonNull((Object)binding);
        AsyncOperationHelper.withAsyncSourceAndConnection(binding::getWriteConnectionSource, false, callback, (connectionSource, connection, cb) -> AsyncOperationHelper.executeCommandAsync(binding, this.namespace.getDatabaseName(), this.buildCommand(), connection, AsyncOperationHelper.writeConcernErrorTransformerAsync(), (result, commandExecutionError) -> {
            try {
                this.swallowOrThrow(commandExecutionError);
                callback.onResult(result, null);
            }
            catch (final Throwable mongoCommandException) {
                callback.onResult(null, mongoCommandException);
            }
        }));
    }
    
     <E extends Throwable> void swallowOrThrow(@Nullable final E mongoExecutionException) throws E, Throwable {
        if (mongoExecutionException != null) {
            throw mongoExecutionException;
        }
    }
    
    abstract BsonDocument buildCommand();
    
    MongoNamespace getNamespace() {
        return this.namespace;
    }
}
