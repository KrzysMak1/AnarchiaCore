package cc.dreamcode.antylogout.libs.com.mongodb.internal.operation;

import cc.dreamcode.antylogout.libs.com.mongodb.internal.session.SessionContext;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.NoOpSessionContext;
import cc.dreamcode.antylogout.libs.com.mongodb.ExplainVerbosity;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.SingleResultCallback;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.AsyncReadBinding;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.ReadBinding;
import cc.dreamcode.antylogout.libs.org.bson.BsonValue;
import cc.dreamcode.antylogout.libs.com.mongodb.client.model.Collation;
import java.util.concurrent.TimeUnit;
import cc.dreamcode.antylogout.libs.com.mongodb.lang.Nullable;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.client.model.AggregationLevel;
import cc.dreamcode.antylogout.libs.org.bson.codecs.Decoder;
import cc.dreamcode.antylogout.libs.org.bson.BsonDocument;
import java.util.List;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoNamespace;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.AsyncBatchCursor;

public class AggregateOperation<T> implements AsyncExplainableReadOperation<AsyncBatchCursor<T>>, ExplainableReadOperation<BatchCursor<T>>
{
    private final AggregateOperationImpl<T> wrapped;
    
    public AggregateOperation(final MongoNamespace namespace, final List<BsonDocument> pipeline, final Decoder<T> decoder) {
        this(namespace, pipeline, decoder, AggregationLevel.COLLECTION);
    }
    
    public AggregateOperation(final MongoNamespace namespace, final List<BsonDocument> pipeline, final Decoder<T> decoder, final AggregationLevel aggregationLevel) {
        this.wrapped = new AggregateOperationImpl<T>(namespace, pipeline, decoder, aggregationLevel);
    }
    
    public List<BsonDocument> getPipeline() {
        return this.wrapped.getPipeline();
    }
    
    public Boolean getAllowDiskUse() {
        return this.wrapped.getAllowDiskUse();
    }
    
    public AggregateOperation<T> allowDiskUse(@Nullable final Boolean allowDiskUse) {
        this.wrapped.allowDiskUse(allowDiskUse);
        return this;
    }
    
    public Integer getBatchSize() {
        return this.wrapped.getBatchSize();
    }
    
    public AggregateOperation<T> batchSize(@Nullable final Integer batchSize) {
        this.wrapped.batchSize(batchSize);
        return this;
    }
    
    public long getMaxAwaitTime(final TimeUnit timeUnit) {
        return this.wrapped.getMaxAwaitTime(timeUnit);
    }
    
    public AggregateOperation<T> maxAwaitTime(final long maxAwaitTime, final TimeUnit timeUnit) {
        this.wrapped.maxAwaitTime(maxAwaitTime, timeUnit);
        return this;
    }
    
    public long getMaxTime(final TimeUnit timeUnit) {
        return this.wrapped.getMaxTime(timeUnit);
    }
    
    public AggregateOperation<T> maxTime(final long maxTime, final TimeUnit timeUnit) {
        this.wrapped.maxTime(maxTime, timeUnit);
        return this;
    }
    
    public Collation getCollation() {
        return this.wrapped.getCollation();
    }
    
    public AggregateOperation<T> collation(@Nullable final Collation collation) {
        this.wrapped.collation(collation);
        return this;
    }
    
    @Nullable
    public BsonValue getComment() {
        return this.wrapped.getComment();
    }
    
    public AggregateOperation<T> comment(@Nullable final BsonValue comment) {
        this.wrapped.comment(comment);
        return this;
    }
    
    public AggregateOperation<T> let(@Nullable final BsonDocument variables) {
        this.wrapped.let(variables);
        return this;
    }
    
    public AggregateOperation<T> retryReads(final boolean retryReads) {
        this.wrapped.retryReads(retryReads);
        return this;
    }
    
    public boolean getRetryReads() {
        return this.wrapped.getRetryReads();
    }
    
    @Nullable
    public BsonDocument getHint() {
        final BsonValue hint = this.wrapped.getHint();
        if (hint == null) {
            return null;
        }
        if (!hint.isDocument()) {
            throw new IllegalArgumentException("Hint is not a BsonDocument please use the #getHintBsonValue() method. ");
        }
        return hint.asDocument();
    }
    
    @Nullable
    public BsonValue getHintBsonValue() {
        return this.wrapped.getHint();
    }
    
    public AggregateOperation<T> hint(@Nullable final BsonValue hint) {
        this.wrapped.hint(hint);
        return this;
    }
    
    @Override
    public BatchCursor<T> execute(final ReadBinding binding) {
        return this.wrapped.execute(binding);
    }
    
    @Override
    public void executeAsync(final AsyncReadBinding binding, final SingleResultCallback<AsyncBatchCursor<T>> callback) {
        this.wrapped.executeAsync(binding, callback);
    }
    
    @Override
    public <R> ReadOperation<R> asExplainableOperation(@Nullable final ExplainVerbosity verbosity, final Decoder<R> resultDecoder) {
        return new CommandReadOperation<R>(this.getNamespace().getDatabaseName(), ExplainHelper.asExplainCommand(this.wrapped.getCommand(NoOpSessionContext.INSTANCE, 0), verbosity), resultDecoder);
    }
    
    @Override
    public <R> AsyncReadOperation<R> asAsyncExplainableOperation(@Nullable final ExplainVerbosity verbosity, final Decoder<R> resultDecoder) {
        return new CommandReadOperation<R>(this.getNamespace().getDatabaseName(), ExplainHelper.asExplainCommand(this.wrapped.getCommand(NoOpSessionContext.INSTANCE, 0), verbosity), resultDecoder);
    }
    
    MongoNamespace getNamespace() {
        return this.wrapped.getNamespace();
    }
    
    Decoder<T> getDecoder() {
        return this.wrapped.getDecoder();
    }
}
