package cc.dreamcode.antylogout.libs.com.mongodb.internal.operation;

import cc.dreamcode.antylogout.libs.com.mongodb.lang.NonNull;
import cc.dreamcode.antylogout.libs.org.bson.BsonString;
import java.util.concurrent.TimeUnit;
import java.util.List;
import java.util.Collections;
import cc.dreamcode.antylogout.libs.org.bson.BsonDocument;
import cc.dreamcode.antylogout.libs.com.mongodb.ExplainVerbosity;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.SingleResultCallback;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.AsyncReadBinding;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoCommandException;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.ReadBinding;
import cc.dreamcode.antylogout.libs.org.bson.BsonValue;
import cc.dreamcode.antylogout.libs.com.mongodb.client.model.Collation;
import cc.dreamcode.antylogout.libs.com.mongodb.lang.Nullable;
import cc.dreamcode.antylogout.libs.org.bson.codecs.Decoder;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoNamespace;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.AsyncBatchCursor;

final class ListSearchIndexesOperation<T> implements AsyncExplainableReadOperation<AsyncBatchCursor<T>>, ExplainableReadOperation<BatchCursor<T>>
{
    private static final String STAGE_LIST_SEARCH_INDEXES = "$listSearchIndexes";
    private final MongoNamespace namespace;
    private final Decoder<T> decoder;
    @Nullable
    private final Boolean allowDiskUse;
    @Nullable
    private final Integer batchSize;
    @Nullable
    private final Collation collation;
    @Nullable
    private final BsonValue comment;
    private final long maxTimeMS;
    @Nullable
    private final String indexName;
    private final boolean retryReads;
    
    ListSearchIndexesOperation(final MongoNamespace namespace, final Decoder<T> decoder, final long maxTimeMS, @Nullable final String indexName, @Nullable final Integer batchSize, @Nullable final Collation collation, @Nullable final BsonValue comment, @Nullable final Boolean allowDiskUse, final boolean retryReads) {
        this.namespace = namespace;
        this.decoder = decoder;
        this.allowDiskUse = allowDiskUse;
        this.batchSize = batchSize;
        this.collation = collation;
        this.maxTimeMS = maxTimeMS;
        this.comment = comment;
        this.indexName = indexName;
        this.retryReads = retryReads;
    }
    
    @Override
    public BatchCursor<T> execute(final ReadBinding binding) {
        try {
            return this.asAggregateOperation().execute(binding);
        }
        catch (final MongoCommandException exception) {
            final int cursorBatchSize = (this.batchSize == null) ? 0 : this.batchSize;
            if (!CommandOperationHelper.isNamespaceError((Throwable)exception)) {
                throw exception;
            }
            return (BatchCursor<T>)SingleBatchCursor.createEmptySingleBatchCursor(exception.getServerAddress(), cursorBatchSize);
        }
    }
    
    @Override
    public void executeAsync(final AsyncReadBinding binding, final SingleResultCallback<AsyncBatchCursor<T>> callback) {
        this.asAggregateOperation().executeAsync(binding, (cursor, exception) -> {
            if (exception != null && !CommandOperationHelper.isNamespaceError(exception)) {
                callback.onResult(null, exception);
            }
            else if (exception != null) {
                callback.onResult(AsyncSingleBatchCursor.createEmptyAsyncSingleBatchCursor((this.batchSize == null) ? 0 : this.batchSize), null);
            }
            else {
                callback.onResult(cursor, null);
            }
        });
    }
    
    @Override
    public <R> ReadOperation<R> asExplainableOperation(@Nullable final ExplainVerbosity verbosity, final Decoder<R> resultDecoder) {
        return this.asAggregateOperation().asExplainableOperation(verbosity, resultDecoder);
    }
    
    @Override
    public <R> AsyncReadOperation<R> asAsyncExplainableOperation(@Nullable final ExplainVerbosity verbosity, final Decoder<R> resultDecoder) {
        return this.asAggregateOperation().asAsyncExplainableOperation(verbosity, resultDecoder);
    }
    
    private AggregateOperation<T> asAggregateOperation() {
        final BsonDocument searchDefinition = this.getSearchDefinition();
        final BsonDocument listSearchIndexesStage = new BsonDocument("$listSearchIndexes", searchDefinition);
        return new AggregateOperation<T>(this.namespace, (List<BsonDocument>)Collections.singletonList((Object)listSearchIndexesStage), this.decoder).retryReads(this.retryReads).collation(this.collation).comment(this.comment).allowDiskUse(this.allowDiskUse).batchSize(this.batchSize).maxTime(this.maxTimeMS, TimeUnit.MILLISECONDS);
    }
    
    @NonNull
    private BsonDocument getSearchDefinition() {
        if (this.indexName == null) {
            return new BsonDocument();
        }
        return new BsonDocument("name", new BsonString(this.indexName));
    }
}
