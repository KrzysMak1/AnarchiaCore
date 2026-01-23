package cc.dreamcode.antylogout.libs.com.mongodb.internal.operation;

import java.util.Collection;
import java.util.ArrayDeque;
import java.util.Deque;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoCommandException;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.Connection;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.AsyncConnection;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.AsyncBatchCursor;
import cc.dreamcode.antylogout.libs.org.bson.codecs.Decoder;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.AsyncReadBinding;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.ReadBinding;
import cc.dreamcode.antylogout.libs.org.bson.BsonValue;
import cc.dreamcode.antylogout.libs.org.bson.BsonString;
import java.util.Arrays;
import java.util.Collections;
import java.util.function.Supplier;
import java.util.List;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.AsyncReadWriteBinding;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.ErrorHandlingResultCallback;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.SingleResultCallback;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.AsyncWriteBinding;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.ReadWriteBinding;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.WriteBinding;
import cc.dreamcode.antylogout.libs.com.mongodb.assertions.Assertions;
import cc.dreamcode.antylogout.libs.com.mongodb.lang.Nullable;
import cc.dreamcode.antylogout.libs.org.bson.BsonDocument;
import cc.dreamcode.antylogout.libs.com.mongodb.WriteConcern;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoNamespace;
import cc.dreamcode.antylogout.libs.org.bson.codecs.BsonValueCodec;

public class DropCollectionOperation implements AsyncWriteOperation<Void>, WriteOperation<Void>
{
    private static final String ENCRYPT_PREFIX = "enxcol_.";
    private static final BsonValueCodec BSON_VALUE_CODEC;
    private final MongoNamespace namespace;
    private final WriteConcern writeConcern;
    private BsonDocument encryptedFields;
    private boolean autoEncryptedFields;
    
    public DropCollectionOperation(final MongoNamespace namespace) {
        this(namespace, null);
    }
    
    public DropCollectionOperation(final MongoNamespace namespace, @Nullable final WriteConcern writeConcern) {
        this.namespace = Assertions.notNull("namespace", namespace);
        this.writeConcern = writeConcern;
    }
    
    public WriteConcern getWriteConcern() {
        return this.writeConcern;
    }
    
    public DropCollectionOperation encryptedFields(final BsonDocument encryptedFields) {
        this.encryptedFields = encryptedFields;
        return this;
    }
    
    public DropCollectionOperation autoEncryptedFields(final boolean autoEncryptedFields) {
        this.autoEncryptedFields = autoEncryptedFields;
        return this;
    }
    
    @Override
    public Void execute(final WriteBinding binding) {
        final BsonDocument localEncryptedFields = this.getEncryptedFields((ReadWriteBinding)binding);
        return SyncOperationHelper.withConnection(binding, connection -> {
            this.getCommands(localEncryptedFields).forEach(command -> {
                try {
                    SyncOperationHelper.executeCommand(binding, this.namespace.getDatabaseName(), (BsonDocument)command.get(), connection, SyncOperationHelper.writeConcernErrorTransformer());
                }
                catch (final MongoCommandException e) {
                    CommandOperationHelper.rethrowIfNotNamespaceError(e);
                }
            });
            return null;
        });
    }
    
    @Override
    public void executeAsync(final AsyncWriteBinding binding, final SingleResultCallback<Void> callback) {
        final SingleResultCallback<Void> errHandlingCallback = ErrorHandlingResultCallback.errorHandlingCallback(callback, OperationHelper.LOGGER);
        this.getEncryptedFields((AsyncReadWriteBinding)binding, (result, t) -> {
            if (t != null) {
                errHandlingCallback.onResult(null, t);
            }
            else {
                AsyncOperationHelper.withAsyncConnection(binding, (connection, t1) -> {
                    if (t1 != null) {
                        errHandlingCallback.onResult(null, t1);
                    }
                    else {
                        new ProcessCommandsCallback(binding, connection, this.getCommands(result), AsyncOperationHelper.releasingCallback((SingleResultCallback<Void>)errHandlingCallback, connection)).onResult((Void)null, (Throwable)null);
                    }
                });
            }
        });
    }
    
    private List<Supplier<BsonDocument>> getCommands(final BsonDocument encryptedFields) {
        if (encryptedFields == null) {
            return (List<Supplier<BsonDocument>>)Collections.singletonList((Object)this::dropCollectionCommand);
        }
        return (List<Supplier<BsonDocument>>)Arrays.asList((Object[])new Supplier[] { () -> this.getDropEncryptedFieldsCollectionCommand(encryptedFields, "esc"), () -> this.getDropEncryptedFieldsCollectionCommand(encryptedFields, "ecoc"), this::dropCollectionCommand });
    }
    
    private BsonDocument getDropEncryptedFieldsCollectionCommand(final BsonDocument encryptedFields, final String collectionSuffix) {
        final BsonString defaultCollectionName = new BsonString("enxcol_." + this.namespace.getCollectionName() + "." + collectionSuffix);
        return new BsonDocument("drop", (BsonValue)encryptedFields.getOrDefault((Object)(collectionSuffix + "Collection"), (Object)defaultCollectionName));
    }
    
    private BsonDocument dropCollectionCommand() {
        final BsonDocument commandDocument = new BsonDocument("drop", new BsonString(this.namespace.getCollectionName()));
        WriteConcernHelper.appendWriteConcernToCommand(this.writeConcern, commandDocument);
        return commandDocument;
    }
    
    @Nullable
    private BsonDocument getEncryptedFields(final ReadWriteBinding readWriteBinding) {
        if (this.encryptedFields == null && this.autoEncryptedFields) {
            try (final BatchCursor<BsonValue> cursor = this.listCollectionOperation().execute((ReadBinding)readWriteBinding)) {
                return this.getCollectionEncryptedFields(this.encryptedFields, cursor.tryNext());
            }
        }
        return this.encryptedFields;
    }
    
    private void getEncryptedFields(final AsyncReadWriteBinding asyncReadWriteBinding, final SingleResultCallback<BsonDocument> callback) {
        if (this.encryptedFields == null && this.autoEncryptedFields) {
            this.listCollectionOperation().executeAsync(asyncReadWriteBinding, (cursor, t) -> {
                if (t != null) {
                    callback.onResult(null, t);
                }
                else {
                    cursor.next((bsonValues, t1) -> {
                        if (t1 != null) {
                            callback.onResult(null, t1);
                        }
                        else {
                            callback.onResult(this.getCollectionEncryptedFields(this.encryptedFields, (List<BsonValue>)bsonValues), null);
                        }
                    });
                }
            });
        }
        else {
            callback.onResult(this.encryptedFields, null);
        }
    }
    
    private BsonDocument getCollectionEncryptedFields(final BsonDocument defaultEncryptedFields, @Nullable final List<BsonValue> bsonValues) {
        if (bsonValues != null && bsonValues.size() > 0) {
            return ((BsonValue)bsonValues.get(0)).asDocument().getDocument("options", new BsonDocument()).getDocument("encryptedFields", new BsonDocument());
        }
        return defaultEncryptedFields;
    }
    
    private ListCollectionsOperation<BsonValue> listCollectionOperation() {
        return new ListCollectionsOperation<BsonValue>(this.namespace.getDatabaseName(), DropCollectionOperation.BSON_VALUE_CODEC).filter(new BsonDocument("name", new BsonString(this.namespace.getCollectionName()))).batchSize(1);
    }
    
    static {
        BSON_VALUE_CODEC = new BsonValueCodec();
    }
    
    class ProcessCommandsCallback implements SingleResultCallback<Void>
    {
        private final AsyncWriteBinding binding;
        private final AsyncConnection connection;
        private final SingleResultCallback<Void> finalCallback;
        private final Deque<Supplier<BsonDocument>> commands;
        
        ProcessCommandsCallback(final AsyncWriteBinding binding, final AsyncConnection connection, final List<Supplier<BsonDocument>> commands, final SingleResultCallback<Void> finalCallback) {
            this.binding = binding;
            this.connection = connection;
            this.finalCallback = finalCallback;
            this.commands = (Deque<Supplier<BsonDocument>>)new ArrayDeque((Collection)commands);
        }
        
        @Override
        public void onResult(@Nullable final Void result, @Nullable final Throwable t) {
            if (t != null && !CommandOperationHelper.isNamespaceError(t)) {
                this.finalCallback.onResult(null, t);
                return;
            }
            final Supplier<BsonDocument> nextCommandFunction = (Supplier<BsonDocument>)this.commands.poll();
            if (nextCommandFunction == null) {
                this.finalCallback.onResult(null, null);
            }
            else {
                AsyncOperationHelper.executeCommandAsync(this.binding, DropCollectionOperation.this.namespace.getDatabaseName(), (BsonDocument)nextCommandFunction.get(), this.connection, AsyncOperationHelper.writeConcernErrorTransformerAsync(), this);
            }
        }
    }
}
