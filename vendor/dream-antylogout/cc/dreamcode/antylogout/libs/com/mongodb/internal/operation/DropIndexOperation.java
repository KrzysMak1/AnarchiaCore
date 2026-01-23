package cc.dreamcode.antylogout.libs.com.mongodb.internal.operation;

import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.Connection;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.AsyncConnection;
import cc.dreamcode.antylogout.libs.org.bson.BsonValue;
import cc.dreamcode.antylogout.libs.org.bson.BsonString;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.ErrorHandlingResultCallback;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.SingleResultCallback;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.AsyncWriteBinding;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoCommandException;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.WriteBinding;
import java.util.concurrent.TimeUnit;
import cc.dreamcode.antylogout.libs.com.mongodb.assertions.Assertions;
import cc.dreamcode.antylogout.libs.com.mongodb.lang.Nullable;
import cc.dreamcode.antylogout.libs.com.mongodb.WriteConcern;
import cc.dreamcode.antylogout.libs.org.bson.BsonDocument;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoNamespace;

public class DropIndexOperation implements AsyncWriteOperation<Void>, WriteOperation<Void>
{
    private final MongoNamespace namespace;
    private final String indexName;
    private final BsonDocument indexKeys;
    private final WriteConcern writeConcern;
    private long maxTimeMS;
    
    public DropIndexOperation(final MongoNamespace namespace, final String indexName) {
        this(namespace, indexName, null);
    }
    
    public DropIndexOperation(final MongoNamespace namespace, final BsonDocument keys) {
        this(namespace, keys, null);
    }
    
    public DropIndexOperation(final MongoNamespace namespace, final String indexName, @Nullable final WriteConcern writeConcern) {
        this.namespace = Assertions.notNull("namespace", namespace);
        this.indexName = Assertions.notNull("indexName", indexName);
        this.indexKeys = null;
        this.writeConcern = writeConcern;
    }
    
    public DropIndexOperation(final MongoNamespace namespace, final BsonDocument indexKeys, @Nullable final WriteConcern writeConcern) {
        this.namespace = Assertions.notNull("namespace", namespace);
        this.indexKeys = Assertions.notNull("indexKeys", indexKeys);
        this.indexName = null;
        this.writeConcern = writeConcern;
    }
    
    public WriteConcern getWriteConcern() {
        return this.writeConcern;
    }
    
    public long getMaxTime(final TimeUnit timeUnit) {
        Assertions.notNull("timeUnit", timeUnit);
        return timeUnit.convert(this.maxTimeMS, TimeUnit.MILLISECONDS);
    }
    
    public DropIndexOperation maxTime(final long maxTime, final TimeUnit timeUnit) {
        Assertions.notNull("timeUnit", timeUnit);
        Assertions.isTrueArgument("maxTime >= 0", maxTime >= 0L);
        this.maxTimeMS = TimeUnit.MILLISECONDS.convert(maxTime, timeUnit);
        return this;
    }
    
    @Override
    public Void execute(final WriteBinding binding) {
        return SyncOperationHelper.withConnection(binding, connection -> {
            try {
                SyncOperationHelper.executeCommand(binding, this.namespace.getDatabaseName(), this.getCommand(), connection, SyncOperationHelper.writeConcernErrorTransformer());
            }
            catch (final MongoCommandException e) {
                CommandOperationHelper.rethrowIfNotNamespaceError(e);
            }
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
                final SingleResultCallback<Void> releasingCallback = AsyncOperationHelper.releasingCallback(errHandlingCallback, connection);
                AsyncOperationHelper.executeCommandAsync(binding, this.namespace.getDatabaseName(), this.getCommand(), connection, AsyncOperationHelper.writeConcernErrorTransformerAsync(), (result, t1) -> {
                    if (t1 != null && !CommandOperationHelper.isNamespaceError(t1)) {
                        releasingCallback.onResult(null, t1);
                    }
                    else {
                        releasingCallback.onResult(result, null);
                    }
                });
            }
        });
    }
    
    private BsonDocument getCommand() {
        final BsonDocument command = new BsonDocument("dropIndexes", new BsonString(this.namespace.getCollectionName()));
        if (this.indexName != null) {
            command.put("index", new BsonString(this.indexName));
        }
        else {
            command.put("index", this.indexKeys);
        }
        DocumentHelper.putIfNotZero(command, "maxTimeMS", this.maxTimeMS);
        WriteConcernHelper.appendWriteConcernToCommand(this.writeConcern, command);
        return command;
    }
}
