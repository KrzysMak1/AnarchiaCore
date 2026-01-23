package cc.dreamcode.antylogout.libs.com.mongodb.internal.operation;

import java.util.function.BiFunction;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.ConnectionDescription;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.ServerDescription;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.Connection;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.ConnectionSource;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.AsyncConnection;
import cc.dreamcode.antylogout.libs.org.bson.BsonInt64;
import cc.dreamcode.antylogout.libs.org.bson.BsonBoolean;
import cc.dreamcode.antylogout.libs.org.bson.BsonInt32;
import cc.dreamcode.antylogout.libs.org.bson.BsonString;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.session.SessionContext;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.NoOpSessionContext;
import cc.dreamcode.antylogout.libs.com.mongodb.ExplainVerbosity;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoQueryException;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoCommandException;
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
import cc.dreamcode.antylogout.libs.com.mongodb.client.model.Collation;
import cc.dreamcode.antylogout.libs.com.mongodb.CursorType;
import cc.dreamcode.antylogout.libs.org.bson.BsonDocument;
import cc.dreamcode.antylogout.libs.org.bson.codecs.Decoder;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoNamespace;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.AsyncBatchCursor;

public class FindOperation<T> implements AsyncExplainableReadOperation<AsyncBatchCursor<T>>, ExplainableReadOperation<BatchCursor<T>>
{
    private static final String FIRST_BATCH = "firstBatch";
    private final MongoNamespace namespace;
    private final Decoder<T> decoder;
    private boolean retryReads;
    private BsonDocument filter;
    private int batchSize;
    private int limit;
    private BsonDocument projection;
    private long maxTimeMS;
    private long maxAwaitTimeMS;
    private int skip;
    private BsonDocument sort;
    private CursorType cursorType;
    private boolean noCursorTimeout;
    private boolean partial;
    private Collation collation;
    private BsonValue comment;
    private BsonValue hint;
    private BsonDocument variables;
    private BsonDocument max;
    private BsonDocument min;
    private boolean returnKey;
    private boolean showRecordId;
    private Boolean allowDiskUse;
    
    public FindOperation(final MongoNamespace namespace, final Decoder<T> decoder) {
        this.cursorType = CursorType.NonTailable;
        this.namespace = Assertions.notNull("namespace", namespace);
        this.decoder = Assertions.notNull("decoder", decoder);
    }
    
    public MongoNamespace getNamespace() {
        return this.namespace;
    }
    
    public Decoder<T> getDecoder() {
        return this.decoder;
    }
    
    public BsonDocument getFilter() {
        return this.filter;
    }
    
    public FindOperation<T> filter(@Nullable final BsonDocument filter) {
        this.filter = filter;
        return this;
    }
    
    public int getBatchSize() {
        return this.batchSize;
    }
    
    public FindOperation<T> batchSize(final int batchSize) {
        this.batchSize = batchSize;
        return this;
    }
    
    public int getLimit() {
        return this.limit;
    }
    
    public FindOperation<T> limit(final int limit) {
        this.limit = limit;
        return this;
    }
    
    public BsonDocument getProjection() {
        return this.projection;
    }
    
    public FindOperation<T> projection(@Nullable final BsonDocument projection) {
        this.projection = projection;
        return this;
    }
    
    public long getMaxTime(final TimeUnit timeUnit) {
        Assertions.notNull("timeUnit", timeUnit);
        return timeUnit.convert(this.maxTimeMS, TimeUnit.MILLISECONDS);
    }
    
    public FindOperation<T> maxTime(final long maxTime, final TimeUnit timeUnit) {
        Assertions.notNull("timeUnit", timeUnit);
        Assertions.isTrueArgument("maxTime >= 0", maxTime >= 0L);
        this.maxTimeMS = TimeUnit.MILLISECONDS.convert(maxTime, timeUnit);
        return this;
    }
    
    public long getMaxAwaitTime(final TimeUnit timeUnit) {
        Assertions.notNull("timeUnit", timeUnit);
        return timeUnit.convert(this.maxAwaitTimeMS, TimeUnit.MILLISECONDS);
    }
    
    public FindOperation<T> maxAwaitTime(final long maxAwaitTime, final TimeUnit timeUnit) {
        Assertions.notNull("timeUnit", timeUnit);
        Assertions.isTrueArgument("maxAwaitTime >= 0", maxAwaitTime >= 0L);
        this.maxAwaitTimeMS = TimeUnit.MILLISECONDS.convert(maxAwaitTime, timeUnit);
        return this;
    }
    
    public int getSkip() {
        return this.skip;
    }
    
    public FindOperation<T> skip(final int skip) {
        this.skip = skip;
        return this;
    }
    
    public BsonDocument getSort() {
        return this.sort;
    }
    
    public FindOperation<T> sort(@Nullable final BsonDocument sort) {
        this.sort = sort;
        return this;
    }
    
    public CursorType getCursorType() {
        return this.cursorType;
    }
    
    public FindOperation<T> cursorType(final CursorType cursorType) {
        this.cursorType = Assertions.notNull("cursorType", cursorType);
        return this;
    }
    
    public boolean isNoCursorTimeout() {
        return this.noCursorTimeout;
    }
    
    public FindOperation<T> noCursorTimeout(final boolean noCursorTimeout) {
        this.noCursorTimeout = noCursorTimeout;
        return this;
    }
    
    public boolean isPartial() {
        return this.partial;
    }
    
    public FindOperation<T> partial(final boolean partial) {
        this.partial = partial;
        return this;
    }
    
    public Collation getCollation() {
        return this.collation;
    }
    
    public FindOperation<T> collation(@Nullable final Collation collation) {
        this.collation = collation;
        return this;
    }
    
    public BsonValue getComment() {
        return this.comment;
    }
    
    public FindOperation<T> comment(@Nullable final BsonValue comment) {
        this.comment = comment;
        return this;
    }
    
    public BsonValue getHint() {
        return this.hint;
    }
    
    public FindOperation<T> hint(@Nullable final BsonValue hint) {
        this.hint = hint;
        return this;
    }
    
    public BsonDocument getLet() {
        return this.variables;
    }
    
    public FindOperation<T> let(@Nullable final BsonDocument variables) {
        this.variables = variables;
        return this;
    }
    
    public BsonDocument getMax() {
        return this.max;
    }
    
    public FindOperation<T> max(@Nullable final BsonDocument max) {
        this.max = max;
        return this;
    }
    
    public BsonDocument getMin() {
        return this.min;
    }
    
    public FindOperation<T> min(@Nullable final BsonDocument min) {
        this.min = min;
        return this;
    }
    
    public boolean isReturnKey() {
        return this.returnKey;
    }
    
    public FindOperation<T> returnKey(final boolean returnKey) {
        this.returnKey = returnKey;
        return this;
    }
    
    public boolean isShowRecordId() {
        return this.showRecordId;
    }
    
    public FindOperation<T> showRecordId(final boolean showRecordId) {
        this.showRecordId = showRecordId;
        return this;
    }
    
    public FindOperation<T> retryReads(final boolean retryReads) {
        this.retryReads = retryReads;
        return this;
    }
    
    public boolean getRetryReads() {
        return this.retryReads;
    }
    
    public Boolean isAllowDiskUse() {
        return this.allowDiskUse;
    }
    
    public FindOperation<T> allowDiskUse(@Nullable final Boolean allowDiskUse) {
        this.allowDiskUse = allowDiskUse;
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
                    return SyncOperationHelper.createReadCommandAndExecute(retryState, binding, source, this.namespace.getDatabaseName(), this.getCommandCreator(binding.getSessionContext()), CommandResultDocumentCodec.create(this.decoder, "firstBatch"), this.transformer(), connection);
                }
                catch (final MongoCommandException e) {
                    throw new MongoQueryException(e.getResponse(), e.getServerAddress());
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
                    final SingleResultCallback<AsyncBatchCursor<T>> wrappedCallback = exceptionTransformingCallback((SingleResultCallback<AsyncBatchCursor<T>>)releasingCallback);
                    AsyncOperationHelper.createReadCommandAndExecuteAsync(retryState, binding, source, this.namespace.getDatabaseName(), this.getCommandCreator(binding.getSessionContext()), CommandResultDocumentCodec.create(this.decoder, "firstBatch"), this.asyncTransformer(), connection, wrappedCallback);
                }
            });
            return;
        });
        Objects.requireNonNull((Object)binding);
        final AsyncCallbackSupplier<AsyncBatchCursor<T>> asyncRead = decorateReadWithRetriesAsync.whenComplete(binding::release);
        asyncRead.get(ErrorHandlingResultCallback.errorHandlingCallback(callback, OperationHelper.LOGGER));
    }
    
    private static <T> SingleResultCallback<T> exceptionTransformingCallback(final SingleResultCallback<T> callback) {
        return (result, t) -> {
            if (t != null) {
                if (t instanceof MongoCommandException) {
                    final MongoCommandException commandException = (MongoCommandException)t;
                    callback.onResult(result, (Throwable)new MongoQueryException(commandException.getResponse(), commandException.getServerAddress()));
                }
                else {
                    callback.onResult(result, t);
                }
            }
            else {
                callback.onResult(result, null);
            }
        };
    }
    
    @Override
    public <R> ReadOperation<R> asExplainableOperation(@Nullable final ExplainVerbosity verbosity, final Decoder<R> resultDecoder) {
        return new CommandReadOperation<R>(this.getNamespace().getDatabaseName(), ExplainHelper.asExplainCommand(this.getCommand(NoOpSessionContext.INSTANCE, 0), verbosity), resultDecoder);
    }
    
    @Override
    public <R> AsyncReadOperation<R> asAsyncExplainableOperation(@Nullable final ExplainVerbosity verbosity, final Decoder<R> resultDecoder) {
        return new CommandReadOperation<R>(this.getNamespace().getDatabaseName(), ExplainHelper.asExplainCommand(this.getCommand(NoOpSessionContext.INSTANCE, 0), verbosity), resultDecoder);
    }
    
    private BsonDocument getCommand(final SessionContext sessionContext, final int maxWireVersion) {
        final BsonDocument commandDocument = new BsonDocument("find", new BsonString(this.namespace.getCollectionName()));
        OperationReadConcernHelper.appendReadConcernToCommand(sessionContext, maxWireVersion, commandDocument);
        DocumentHelper.putIfNotNull(commandDocument, "filter", this.filter);
        DocumentHelper.putIfNotNullOrEmpty(commandDocument, "sort", this.sort);
        DocumentHelper.putIfNotNullOrEmpty(commandDocument, "projection", this.projection);
        if (this.skip > 0) {
            commandDocument.put("skip", new BsonInt32(this.skip));
        }
        if (this.limit != 0) {
            commandDocument.put("limit", new BsonInt32(Math.abs(this.limit)));
        }
        if (this.limit >= 0) {
            if (this.batchSize < 0 && Math.abs(this.batchSize) < this.limit) {
                commandDocument.put("limit", new BsonInt32(Math.abs(this.batchSize)));
            }
            else if (this.batchSize != 0) {
                commandDocument.put("batchSize", new BsonInt32(Math.abs(this.batchSize)));
            }
        }
        if (this.limit < 0 || this.batchSize < 0) {
            commandDocument.put("singleBatch", BsonBoolean.TRUE);
        }
        if (this.maxTimeMS > 0L) {
            commandDocument.put("maxTimeMS", new BsonInt64(this.maxTimeMS));
        }
        if (this.isTailableCursor()) {
            commandDocument.put("tailable", BsonBoolean.TRUE);
        }
        if (this.isAwaitData()) {
            commandDocument.put("awaitData", BsonBoolean.TRUE);
        }
        if (this.noCursorTimeout) {
            commandDocument.put("noCursorTimeout", BsonBoolean.TRUE);
        }
        if (this.partial) {
            commandDocument.put("allowPartialResults", BsonBoolean.TRUE);
        }
        if (this.collation != null) {
            commandDocument.put("collation", this.collation.asDocument());
        }
        if (this.comment != null) {
            commandDocument.put("comment", this.comment);
        }
        if (this.hint != null) {
            commandDocument.put("hint", this.hint);
        }
        if (this.variables != null) {
            commandDocument.put("let", this.variables);
        }
        if (this.max != null) {
            commandDocument.put("max", this.max);
        }
        if (this.min != null) {
            commandDocument.put("min", this.min);
        }
        if (this.returnKey) {
            commandDocument.put("returnKey", BsonBoolean.TRUE);
        }
        if (this.showRecordId) {
            commandDocument.put("showRecordId", BsonBoolean.TRUE);
        }
        if (this.allowDiskUse != null) {
            commandDocument.put("allowDiskUse", BsonBoolean.valueOf(this.allowDiskUse));
        }
        return commandDocument;
    }
    
    private CommandOperationHelper.CommandCreator getCommandCreator(final SessionContext sessionContext) {
        return (serverDescription, connectionDescription) -> this.getCommand(sessionContext, connectionDescription.getMaxWireVersion());
    }
    
    private boolean isTailableCursor() {
        return this.cursorType.isTailable();
    }
    
    private boolean isAwaitData() {
        return this.cursorType == CursorType.TailableAwait;
    }
    
    private SyncOperationHelper.CommandReadTransformer<BsonDocument, CommandBatchCursor<T>> transformer() {
        return (SyncOperationHelper.CommandReadTransformer<BsonDocument, CommandBatchCursor<T>>)((result, source, connection) -> new CommandBatchCursor(result, this.batchSize, this.getMaxTimeForCursor(), (Decoder<Object>)this.decoder, this.comment, source, connection));
    }
    
    private long getMaxTimeForCursor() {
        return (this.cursorType == CursorType.TailableAwait) ? this.maxAwaitTimeMS : 0L;
    }
    
    private AsyncOperationHelper.CommandReadTransformerAsync<BsonDocument, AsyncBatchCursor<T>> asyncTransformer() {
        return (AsyncOperationHelper.CommandReadTransformerAsync<BsonDocument, AsyncBatchCursor<T>>)((result, source, connection) -> new AsyncCommandBatchCursor(result, this.batchSize, this.getMaxTimeForCursor(), (Decoder<Object>)this.decoder, this.comment, source, connection));
    }
}
