package cc.dreamcode.antylogout.libs.com.mongodb.internal.operation;

import cc.dreamcode.antylogout.libs.com.mongodb.MongoCommandException;
import java.util.function.BiFunction;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.ConnectionDescription;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.ServerDescription;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.Connection;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.ConnectionSource;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.AsyncConnection;
import cc.dreamcode.antylogout.libs.org.bson.codecs.Codec;
import cc.dreamcode.antylogout.libs.org.bson.BsonInt64;
import cc.dreamcode.antylogout.libs.org.bson.BsonString;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.ErrorHandlingResultCallback;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.AsyncConnectionSource;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.function.AsyncCallbackSupplier;
import cc.dreamcode.antylogout.libs.org.bson.BsonDocument;
import java.util.Objects;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.SingleResultCallback;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.AsyncReadBinding;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.function.RetryState;
import java.util.function.Supplier;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.ReadBinding;
import cc.dreamcode.antylogout.libs.com.mongodb.lang.Nullable;
import java.util.concurrent.TimeUnit;
import cc.dreamcode.antylogout.libs.com.mongodb.assertions.Assertions;
import cc.dreamcode.antylogout.libs.org.bson.BsonValue;
import cc.dreamcode.antylogout.libs.org.bson.codecs.Decoder;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoNamespace;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.AsyncBatchCursor;

public class ListIndexesOperation<T> implements AsyncReadOperation<AsyncBatchCursor<T>>, ReadOperation<BatchCursor<T>>
{
    private final MongoNamespace namespace;
    private final Decoder<T> decoder;
    private boolean retryReads;
    private int batchSize;
    private long maxTimeMS;
    private BsonValue comment;
    
    public ListIndexesOperation(final MongoNamespace namespace, final Decoder<T> decoder) {
        this.namespace = Assertions.notNull("namespace", namespace);
        this.decoder = Assertions.notNull("decoder", decoder);
    }
    
    public Integer getBatchSize() {
        return this.batchSize;
    }
    
    public ListIndexesOperation<T> batchSize(final int batchSize) {
        this.batchSize = batchSize;
        return this;
    }
    
    public long getMaxTime(final TimeUnit timeUnit) {
        Assertions.notNull("timeUnit", timeUnit);
        return timeUnit.convert(this.maxTimeMS, TimeUnit.MILLISECONDS);
    }
    
    public ListIndexesOperation<T> maxTime(final long maxTime, final TimeUnit timeUnit) {
        Assertions.notNull("timeUnit", timeUnit);
        this.maxTimeMS = TimeUnit.MILLISECONDS.convert(maxTime, timeUnit);
        return this;
    }
    
    public ListIndexesOperation<T> retryReads(final boolean retryReads) {
        this.retryReads = retryReads;
        return this;
    }
    
    public boolean getRetryReads() {
        return this.retryReads;
    }
    
    @Nullable
    public BsonValue getComment() {
        return this.comment;
    }
    
    public ListIndexesOperation<T> comment(@Nullable final BsonValue comment) {
        this.comment = comment;
        return this;
    }
    
    @Override
    public BatchCursor<T> execute(final ReadBinding binding) {
        final RetryState retryState = CommandOperationHelper.initialRetryState(this.retryReads);
        final Supplier<BatchCursor<T>> read = SyncOperationHelper.decorateReadWithRetries(retryState, binding.getOperationContext(), (java.util.function.Supplier<BatchCursor<T>>)(() -> {
            Objects.requireNonNull((Object)binding);
            return SyncOperationHelper.withSourceAndConnection((Supplier<ConnectionSource>)binding::getReadConnectionSource, false, (java.util.function.BiFunction<ConnectionSource, Connection, BatchCursor>)((source, connection) -> {
                retryState.breakAndThrowIfRetryAnd((Supplier<Boolean>)(() -> !OperationHelper.canRetryRead(source.getServerDescription(), binding.getSessionContext())));
                try {
                    return SyncOperationHelper.createReadCommandAndExecute(retryState, binding, source, this.namespace.getDatabaseName(), this.getCommandCreator(), this.createCommandDecoder(), this.transformer(), connection);
                }
                catch (final MongoCommandException e) {
                    return CommandOperationHelper.rethrowIfNotNamespaceError(e, SingleBatchCursor.createEmptySingleBatchCursor(source.getServerDescription().getAddress(), this.batchSize));
                }
            }));
        }));
        return (BatchCursor)read.get();
    }
    
    @Override
    public void executeAsync(final AsyncReadBinding binding, final SingleResultCallback<AsyncBatchCursor<T>> callback) {
        final RetryState retryState = CommandOperationHelper.initialRetryState(this.retryReads);
        binding.retain();
        final AsyncCallbackSupplier<AsyncBatchCursor<T>> decorateReadWithRetriesAsync = AsyncOperationHelper.decorateReadWithRetriesAsync(retryState, binding.getOperationContext(), funcCallback -> {
            Objects.requireNonNull((Object)binding);
            AsyncOperationHelper.withAsyncSourceAndConnection(binding::getReadConnectionSource, (boolean)(0 != 0), (SingleResultCallback<Object>)funcCallback, (source, connection, releasingCallback) -> {
                if (!retryState.breakAndCompleteIfRetryAnd((Supplier<Boolean>)(() -> !OperationHelper.canRetryRead(source.getServerDescription(), binding.getSessionContext())), releasingCallback)) {
                    AsyncOperationHelper.createReadCommandAndExecuteAsync(retryState, binding, source, this.namespace.getDatabaseName(), this.getCommandCreator(), this.createCommandDecoder(), this.asyncTransformer(), connection, (result, t) -> {
                        if (t != null && !CommandOperationHelper.isNamespaceError(t)) {
                            releasingCallback.onResult(null, t);
                        }
                        else {
                            releasingCallback.onResult((result != null) ? result : AsyncSingleBatchCursor.createEmptyAsyncSingleBatchCursor(this.getBatchSize()), null);
                        }
                    });
                }
            });
            return;
        });
        Objects.requireNonNull((Object)binding);
        final AsyncCallbackSupplier<AsyncBatchCursor<T>> asyncRead = decorateReadWithRetriesAsync.whenComplete(binding::release);
        asyncRead.get(ErrorHandlingResultCallback.errorHandlingCallback(callback, OperationHelper.LOGGER));
    }
    
    private CommandOperationHelper.CommandCreator getCommandCreator() {
        return (serverDescription, connectionDescription) -> this.getCommand();
    }
    
    private BsonDocument getCommand() {
        final BsonDocument command = new BsonDocument("listIndexes", new BsonString(this.namespace.getCollectionName())).append("cursor", CursorHelper.getCursorDocumentFromBatchSize((this.batchSize == 0) ? null : Integer.valueOf(this.batchSize)));
        if (this.maxTimeMS > 0L) {
            command.put("maxTimeMS", new BsonInt64(this.maxTimeMS));
        }
        DocumentHelper.putIfNotNull(command, "comment", this.comment);
        return command;
    }
    
    private SyncOperationHelper.CommandReadTransformer<BsonDocument, BatchCursor<T>> transformer() {
        return (SyncOperationHelper.CommandReadTransformer<BsonDocument, BatchCursor<T>>)((result, source, connection) -> SyncOperationHelper.cursorDocumentToBatchCursor(result, this.decoder, this.comment, source, connection, this.batchSize));
    }
    
    private AsyncOperationHelper.CommandReadTransformerAsync<BsonDocument, AsyncBatchCursor<T>> asyncTransformer() {
        return (AsyncOperationHelper.CommandReadTransformerAsync<BsonDocument, AsyncBatchCursor<T>>)((result, source, connection) -> AsyncOperationHelper.cursorDocumentToAsyncBatchCursor(result, this.decoder, this.comment, source, connection, this.batchSize));
    }
    
    private Codec<BsonDocument> createCommandDecoder() {
        return CommandResultDocumentCodec.create(this.decoder, "firstBatch");
    }
}
