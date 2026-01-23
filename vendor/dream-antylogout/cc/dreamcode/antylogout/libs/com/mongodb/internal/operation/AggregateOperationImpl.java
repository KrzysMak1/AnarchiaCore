package cc.dreamcode.antylogout.libs.com.mongodb.internal.operation;

import java.util.Arrays;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.ConnectionDescription;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.ServerDescription;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.Connection;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.ConnectionSource;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.AsyncConnection;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.AsyncConnectionSource;
import cc.dreamcode.antylogout.libs.org.bson.BsonArray;
import cc.dreamcode.antylogout.libs.org.bson.BsonString;
import cc.dreamcode.antylogout.libs.org.bson.BsonBoolean;
import cc.dreamcode.antylogout.libs.org.bson.BsonInt32;
import cc.dreamcode.antylogout.libs.org.bson.BsonInt64;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.session.SessionContext;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.ErrorHandlingResultCallback;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.SingleResultCallback;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.AsyncReadBinding;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.ReadBinding;
import java.util.concurrent.TimeUnit;
import cc.dreamcode.antylogout.libs.com.mongodb.lang.Nullable;
import cc.dreamcode.antylogout.libs.com.mongodb.assertions.Assertions;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.client.model.AggregationLevel;
import cc.dreamcode.antylogout.libs.org.bson.BsonValue;
import cc.dreamcode.antylogout.libs.com.mongodb.client.model.Collation;
import cc.dreamcode.antylogout.libs.org.bson.codecs.Decoder;
import cc.dreamcode.antylogout.libs.org.bson.BsonDocument;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoNamespace;
import java.util.List;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.AsyncBatchCursor;

class AggregateOperationImpl<T> implements AsyncReadOperation<AsyncBatchCursor<T>>, ReadOperation<BatchCursor<T>>
{
    private static final String RESULT = "result";
    private static final String CURSOR = "cursor";
    private static final String FIRST_BATCH = "firstBatch";
    private static final List<String> FIELD_NAMES_WITH_RESULT;
    private final MongoNamespace namespace;
    private final List<BsonDocument> pipeline;
    private final Decoder<T> decoder;
    private final AggregateTarget aggregateTarget;
    private final PipelineCreator pipelineCreator;
    private boolean retryReads;
    private Boolean allowDiskUse;
    private Integer batchSize;
    private Collation collation;
    private BsonValue comment;
    private BsonValue hint;
    private long maxAwaitTimeMS;
    private long maxTimeMS;
    private BsonDocument variables;
    
    AggregateOperationImpl(final MongoNamespace namespace, final List<BsonDocument> pipeline, final Decoder<T> decoder, final AggregationLevel aggregationLevel) {
        this(namespace, pipeline, decoder, defaultAggregateTarget(Assertions.notNull("aggregationLevel", aggregationLevel), Assertions.notNull("namespace", namespace).getCollectionName()), defaultPipelineCreator(pipeline));
    }
    
    AggregateOperationImpl(final MongoNamespace namespace, final List<BsonDocument> pipeline, final Decoder<T> decoder, final AggregateTarget aggregateTarget, final PipelineCreator pipelineCreator) {
        this.namespace = Assertions.notNull("namespace", namespace);
        this.pipeline = Assertions.notNull("pipeline", pipeline);
        this.decoder = Assertions.notNull("decoder", decoder);
        this.aggregateTarget = Assertions.notNull("aggregateTarget", aggregateTarget);
        this.pipelineCreator = Assertions.notNull("pipelineCreator", pipelineCreator);
    }
    
    MongoNamespace getNamespace() {
        return this.namespace;
    }
    
    List<BsonDocument> getPipeline() {
        return this.pipeline;
    }
    
    Decoder<T> getDecoder() {
        return this.decoder;
    }
    
    Boolean getAllowDiskUse() {
        return this.allowDiskUse;
    }
    
    AggregateOperationImpl<T> allowDiskUse(@Nullable final Boolean allowDiskUse) {
        this.allowDiskUse = allowDiskUse;
        return this;
    }
    
    Integer getBatchSize() {
        return this.batchSize;
    }
    
    AggregateOperationImpl<T> batchSize(@Nullable final Integer batchSize) {
        this.batchSize = batchSize;
        return this;
    }
    
    long getMaxAwaitTime(final TimeUnit timeUnit) {
        Assertions.notNull("timeUnit", timeUnit);
        return timeUnit.convert(this.maxAwaitTimeMS, TimeUnit.MILLISECONDS);
    }
    
    AggregateOperationImpl<T> maxAwaitTime(final long maxAwaitTime, final TimeUnit timeUnit) {
        Assertions.notNull("timeUnit", timeUnit);
        Assertions.isTrueArgument("maxAwaitTime >= 0", maxAwaitTime >= 0L);
        this.maxAwaitTimeMS = TimeUnit.MILLISECONDS.convert(maxAwaitTime, timeUnit);
        return this;
    }
    
    long getMaxTime(final TimeUnit timeUnit) {
        Assertions.notNull("timeUnit", timeUnit);
        return timeUnit.convert(this.maxTimeMS, TimeUnit.MILLISECONDS);
    }
    
    AggregateOperationImpl<T> maxTime(final long maxTime, final TimeUnit timeUnit) {
        Assertions.notNull("timeUnit", timeUnit);
        Assertions.isTrueArgument("maxTime >= 0", maxTime >= 0L);
        this.maxTimeMS = TimeUnit.MILLISECONDS.convert(maxTime, timeUnit);
        return this;
    }
    
    Collation getCollation() {
        return this.collation;
    }
    
    AggregateOperationImpl<T> collation(@Nullable final Collation collation) {
        this.collation = collation;
        return this;
    }
    
    @Nullable
    BsonValue getComment() {
        return this.comment;
    }
    
    AggregateOperationImpl<T> comment(@Nullable final BsonValue comment) {
        this.comment = comment;
        return this;
    }
    
    AggregateOperationImpl<T> let(@Nullable final BsonDocument variables) {
        this.variables = variables;
        return this;
    }
    
    AggregateOperationImpl<T> retryReads(final boolean retryReads) {
        this.retryReads = retryReads;
        return this;
    }
    
    boolean getRetryReads() {
        return this.retryReads;
    }
    
    @Nullable
    BsonValue getHint() {
        return this.hint;
    }
    
    AggregateOperationImpl<T> hint(@Nullable final BsonValue hint) {
        Assertions.isTrueArgument("BsonString or BsonDocument", hint == null || hint.isDocument() || hint.isString());
        this.hint = hint;
        return this;
    }
    
    @Override
    public BatchCursor<T> execute(final ReadBinding binding) {
        return SyncOperationHelper.executeRetryableRead(binding, this.namespace.getDatabaseName(), this.getCommandCreator(binding.getSessionContext()), CommandResultDocumentCodec.create(this.decoder, AggregateOperationImpl.FIELD_NAMES_WITH_RESULT), this.transformer(), this.retryReads);
    }
    
    @Override
    public void executeAsync(final AsyncReadBinding binding, final SingleResultCallback<AsyncBatchCursor<T>> callback) {
        final SingleResultCallback<AsyncBatchCursor<T>> errHandlingCallback = ErrorHandlingResultCallback.errorHandlingCallback(callback, OperationHelper.LOGGER);
        AsyncOperationHelper.executeRetryableReadAsync(binding, this.namespace.getDatabaseName(), this.getCommandCreator(binding.getSessionContext()), CommandResultDocumentCodec.create(this.decoder, AggregateOperationImpl.FIELD_NAMES_WITH_RESULT), this.asyncTransformer(), this.retryReads, errHandlingCallback);
    }
    
    private CommandOperationHelper.CommandCreator getCommandCreator(final SessionContext sessionContext) {
        return (serverDescription, connectionDescription) -> this.getCommand(sessionContext, connectionDescription.getMaxWireVersion());
    }
    
    BsonDocument getCommand(final SessionContext sessionContext, final int maxWireVersion) {
        final BsonDocument commandDocument = new BsonDocument("aggregate", this.aggregateTarget.create());
        OperationReadConcernHelper.appendReadConcernToCommand(sessionContext, maxWireVersion, commandDocument);
        commandDocument.put("pipeline", this.pipelineCreator.create());
        if (this.maxTimeMS > 0L) {
            commandDocument.put("maxTimeMS", (this.maxTimeMS > 2147483647L) ? new BsonInt64(this.maxTimeMS) : new BsonInt32((int)this.maxTimeMS));
        }
        final BsonDocument cursor = new BsonDocument();
        if (this.batchSize != null) {
            cursor.put("batchSize", new BsonInt32(this.batchSize));
        }
        commandDocument.put("cursor", cursor);
        if (this.allowDiskUse != null) {
            commandDocument.put("allowDiskUse", BsonBoolean.valueOf(this.allowDiskUse));
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
        return commandDocument;
    }
    
    private SyncOperationHelper.CommandReadTransformer<BsonDocument, CommandBatchCursor<T>> transformer() {
        return (SyncOperationHelper.CommandReadTransformer<BsonDocument, CommandBatchCursor<T>>)((result, source, connection) -> {
            new CommandBatchCursor(result, (this.batchSize != null) ? ((int)this.batchSize) : 0, this.maxAwaitTimeMS, this.decoder, this.comment, source, connection);
            return;
        });
    }
    
    private AsyncOperationHelper.CommandReadTransformerAsync<BsonDocument, AsyncBatchCursor<T>> asyncTransformer() {
        return (AsyncOperationHelper.CommandReadTransformerAsync<BsonDocument, AsyncBatchCursor<T>>)((result, source, connection) -> {
            new AsyncCommandBatchCursor(result, (this.batchSize != null) ? ((int)this.batchSize) : 0, this.maxAwaitTimeMS, this.decoder, this.comment, source, connection);
            return;
        });
    }
    
    private static AggregateTarget defaultAggregateTarget(final AggregationLevel aggregationLevel, final String collectionName) {
        return () -> {
            if (aggregationLevel == AggregationLevel.DATABASE) {
                return new BsonInt32(1);
            }
            else {
                return new BsonString(collectionName);
            }
        };
    }
    
    private static PipelineCreator defaultPipelineCreator(final List<BsonDocument> pipeline) {
        return () -> new BsonArray((List<? extends BsonValue>)pipeline);
    }
    
    static {
        FIELD_NAMES_WITH_RESULT = Arrays.asList((Object[])new String[] { "result", "firstBatch" });
    }
    
    interface AggregateTarget
    {
        BsonValue create();
    }
    
    interface PipelineCreator
    {
        BsonArray create();
    }
}
