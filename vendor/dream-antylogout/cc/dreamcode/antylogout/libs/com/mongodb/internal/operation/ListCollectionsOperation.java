package cc.dreamcode.antylogout.libs.com.mongodb.internal.operation;

import cc.dreamcode.antylogout.libs.com.mongodb.MongoCommandException;
import java.util.function.BiFunction;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.AsyncConnection;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.Connection;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.ConnectionSource;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.ConnectionDescription;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.ServerDescription;
import cc.dreamcode.antylogout.libs.org.bson.codecs.Codec;
import cc.dreamcode.antylogout.libs.org.bson.BsonInt64;
import cc.dreamcode.antylogout.libs.org.bson.BsonBoolean;
import cc.dreamcode.antylogout.libs.org.bson.BsonInt32;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoNamespace;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.ErrorHandlingResultCallback;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.AsyncConnectionSource;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.function.AsyncCallbackSupplier;
import java.util.Objects;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.SingleResultCallback;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.AsyncReadBinding;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.function.RetryState;
import java.util.function.Supplier;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.ReadBinding;
import java.util.concurrent.TimeUnit;
import cc.dreamcode.antylogout.libs.com.mongodb.lang.Nullable;
import cc.dreamcode.antylogout.libs.com.mongodb.assertions.Assertions;
import cc.dreamcode.antylogout.libs.org.bson.BsonValue;
import cc.dreamcode.antylogout.libs.org.bson.BsonDocument;
import cc.dreamcode.antylogout.libs.org.bson.codecs.Decoder;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.AsyncBatchCursor;

public class ListCollectionsOperation<T> implements AsyncReadOperation<AsyncBatchCursor<T>>, ReadOperation<BatchCursor<T>>
{
    private final String databaseName;
    private final Decoder<T> decoder;
    private boolean retryReads;
    private BsonDocument filter;
    private int batchSize;
    private long maxTimeMS;
    private boolean nameOnly;
    private boolean authorizedCollections;
    private BsonValue comment;
    
    public ListCollectionsOperation(final String databaseName, final Decoder<T> decoder) {
        this.databaseName = Assertions.notNull("databaseName", databaseName);
        this.decoder = Assertions.notNull("decoder", decoder);
    }
    
    public BsonDocument getFilter() {
        return this.filter;
    }
    
    public boolean isNameOnly() {
        return this.nameOnly;
    }
    
    public ListCollectionsOperation<T> filter(@Nullable final BsonDocument filter) {
        this.filter = filter;
        return this;
    }
    
    public ListCollectionsOperation<T> nameOnly(final boolean nameOnly) {
        this.nameOnly = nameOnly;
        return this;
    }
    
    public Integer getBatchSize() {
        return this.batchSize;
    }
    
    public ListCollectionsOperation<T> batchSize(final int batchSize) {
        this.batchSize = batchSize;
        return this;
    }
    
    public long getMaxTime(final TimeUnit timeUnit) {
        Assertions.notNull("timeUnit", timeUnit);
        return timeUnit.convert(this.maxTimeMS, TimeUnit.MILLISECONDS);
    }
    
    public ListCollectionsOperation<T> maxTime(final long maxTime, final TimeUnit timeUnit) {
        Assertions.notNull("timeUnit", timeUnit);
        this.maxTimeMS = TimeUnit.MILLISECONDS.convert(maxTime, timeUnit);
        return this;
    }
    
    public ListCollectionsOperation<T> retryReads(final boolean retryReads) {
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
    
    public ListCollectionsOperation<T> comment(@Nullable final BsonValue comment) {
        this.comment = comment;
        return this;
    }
    
    public ListCollectionsOperation<T> authorizedCollections(final boolean authorizedCollections) {
        this.authorizedCollections = authorizedCollections;
        return this;
    }
    
    public boolean isAuthorizedCollections() {
        return this.authorizedCollections;
    }
    
    @Override
    public BatchCursor<T> execute(final ReadBinding binding) {
        final RetryState retryState = CommandOperationHelper.initialRetryState(this.retryReads);
        final Supplier<BatchCursor<T>> read = SyncOperationHelper.decorateReadWithRetries(retryState, binding.getOperationContext(), (java.util.function.Supplier<BatchCursor<T>>)(() -> {
            Objects.requireNonNull((Object)binding);
            return SyncOperationHelper.withSourceAndConnection((Supplier<ConnectionSource>)binding::getReadConnectionSource, false, (java.util.function.BiFunction<ConnectionSource, Connection, BatchCursor>)((source, connection) -> {
                retryState.breakAndThrowIfRetryAnd((Supplier<Boolean>)(() -> !OperationHelper.canRetryRead(source.getServerDescription(), binding.getSessionContext())));
                try {
                    return SyncOperationHelper.createReadCommandAndExecute(retryState, binding, source, this.databaseName, this.getCommandCreator(), this.createCommandDecoder(), this.commandTransformer(), connection);
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
                    AsyncOperationHelper.createReadCommandAndExecuteAsync(retryState, binding, source, this.databaseName, this.getCommandCreator(), this.createCommandDecoder(), this.asyncTransformer(), connection, (result, t) -> {
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
    
    private MongoNamespace createNamespace() {
        return new MongoNamespace(this.databaseName, "$cmd.listCollections");
    }
    
    private AsyncOperationHelper.CommandReadTransformerAsync<BsonDocument, AsyncBatchCursor<T>> asyncTransformer() {
        return (AsyncOperationHelper.CommandReadTransformerAsync<BsonDocument, AsyncBatchCursor<T>>)((result, source, connection) -> AsyncOperationHelper.cursorDocumentToAsyncBatchCursor(result, this.decoder, this.comment, source, connection, this.batchSize));
    }
    
    private SyncOperationHelper.CommandReadTransformer<BsonDocument, BatchCursor<T>> commandTransformer() {
        return (SyncOperationHelper.CommandReadTransformer<BsonDocument, BatchCursor<T>>)((result, source, connection) -> SyncOperationHelper.cursorDocumentToBatchCursor(result, this.decoder, this.comment, source, connection, this.batchSize));
    }
    
    private CommandOperationHelper.CommandCreator getCommandCreator() {
        return (serverDescription, connectionDescription) -> this.getCommand();
    }
    
    private BsonDocument getCommand() {
        final BsonDocument command = new BsonDocument("listCollections", new BsonInt32(1)).append("cursor", CursorHelper.getCursorDocumentFromBatchSize((this.batchSize == 0) ? null : Integer.valueOf(this.batchSize)));
        if (this.filter != null) {
            command.append("filter", this.filter);
        }
        if (this.nameOnly) {
            command.append("nameOnly", BsonBoolean.TRUE);
        }
        DocumentHelper.putIfTrue(command, "authorizedCollections", this.authorizedCollections);
        if (this.maxTimeMS > 0L) {
            command.put("maxTimeMS", new BsonInt64(this.maxTimeMS));
        }
        DocumentHelper.putIfNotNull(command, "comment", this.comment);
        return command;
    }
    
    private Codec<BsonDocument> createCommandDecoder() {
        return CommandResultDocumentCodec.create(this.decoder, "firstBatch");
    }
}
