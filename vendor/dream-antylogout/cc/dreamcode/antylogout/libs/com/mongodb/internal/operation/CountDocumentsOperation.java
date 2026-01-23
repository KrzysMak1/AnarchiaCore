package cc.dreamcode.antylogout.libs.com.mongodb.internal.operation;

import cc.dreamcode.antylogout.libs.org.bson.codecs.BsonDocumentCodec;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.AsyncBatchCursor;
import cc.dreamcode.antylogout.libs.org.bson.BsonInt32;
import cc.dreamcode.antylogout.libs.org.bson.BsonInt64;
import java.util.ArrayList;
import java.util.List;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.SingleResultCallback;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.AsyncReadBinding;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.ReadBinding;
import java.util.concurrent.TimeUnit;
import cc.dreamcode.antylogout.libs.com.mongodb.lang.Nullable;
import cc.dreamcode.antylogout.libs.com.mongodb.assertions.Assertions;
import cc.dreamcode.antylogout.libs.com.mongodb.client.model.Collation;
import cc.dreamcode.antylogout.libs.org.bson.BsonValue;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoNamespace;
import cc.dreamcode.antylogout.libs.org.bson.BsonDocument;
import cc.dreamcode.antylogout.libs.org.bson.codecs.Decoder;

public class CountDocumentsOperation implements AsyncReadOperation<Long>, ReadOperation<Long>
{
    private static final Decoder<BsonDocument> DECODER;
    private final MongoNamespace namespace;
    private boolean retryReads;
    private BsonDocument filter;
    private BsonValue hint;
    private BsonValue comment;
    private long skip;
    private long limit;
    private long maxTimeMS;
    private Collation collation;
    
    public CountDocumentsOperation(final MongoNamespace namespace) {
        this.namespace = Assertions.notNull("namespace", namespace);
    }
    
    public BsonDocument getFilter() {
        return this.filter;
    }
    
    public CountDocumentsOperation filter(@Nullable final BsonDocument filter) {
        this.filter = filter;
        return this;
    }
    
    public CountDocumentsOperation retryReads(final boolean retryReads) {
        this.retryReads = retryReads;
        return this;
    }
    
    public boolean getRetryReads() {
        return this.retryReads;
    }
    
    public BsonValue getHint() {
        return this.hint;
    }
    
    public CountDocumentsOperation hint(@Nullable final BsonValue hint) {
        this.hint = hint;
        return this;
    }
    
    public long getLimit() {
        return this.limit;
    }
    
    public CountDocumentsOperation limit(final long limit) {
        this.limit = limit;
        return this;
    }
    
    public long getSkip() {
        return this.skip;
    }
    
    public CountDocumentsOperation skip(final long skip) {
        this.skip = skip;
        return this;
    }
    
    public long getMaxTime(final TimeUnit timeUnit) {
        Assertions.notNull("timeUnit", timeUnit);
        return timeUnit.convert(this.maxTimeMS, TimeUnit.MILLISECONDS);
    }
    
    public CountDocumentsOperation maxTime(final long maxTime, final TimeUnit timeUnit) {
        Assertions.notNull("timeUnit", timeUnit);
        this.maxTimeMS = TimeUnit.MILLISECONDS.convert(maxTime, timeUnit);
        return this;
    }
    
    public Collation getCollation() {
        return this.collation;
    }
    
    public CountDocumentsOperation collation(@Nullable final Collation collation) {
        this.collation = collation;
        return this;
    }
    
    @Nullable
    public BsonValue getComment() {
        return this.comment;
    }
    
    public CountDocumentsOperation comment(@Nullable final BsonValue comment) {
        this.comment = comment;
        return this;
    }
    
    @Override
    public Long execute(final ReadBinding binding) {
        final BatchCursor<BsonDocument> cursor = this.getAggregateOperation().execute(binding);
        return cursor.hasNext() ? this.getCountFromAggregateResults(cursor.next()) : 0L;
    }
    
    @Override
    public void executeAsync(final AsyncReadBinding binding, final SingleResultCallback<Long> callback) {
        this.getAggregateOperation().executeAsync(binding, (result, t) -> {
            if (t != null) {
                callback.onResult(null, t);
            }
            else {
                result.next((result1, t1) -> {
                    if (t1 != null) {
                        callback.onResult(null, t1);
                    }
                    else {
                        callback.onResult(this.getCountFromAggregateResults((List<BsonDocument>)result1), null);
                    }
                });
            }
        });
    }
    
    private AggregateOperation<BsonDocument> getAggregateOperation() {
        return new AggregateOperation<BsonDocument>(this.namespace, this.getPipeline(), CountDocumentsOperation.DECODER).retryReads(this.retryReads).collation(this.collation).comment(this.comment).hint(this.hint).maxTime(this.maxTimeMS, TimeUnit.MILLISECONDS);
    }
    
    private List<BsonDocument> getPipeline() {
        final ArrayList<BsonDocument> pipeline = (ArrayList<BsonDocument>)new ArrayList();
        pipeline.add((Object)new BsonDocument("$match", (this.filter != null) ? this.filter : new BsonDocument()));
        if (this.skip > 0L) {
            pipeline.add((Object)new BsonDocument("$skip", new BsonInt64(this.skip)));
        }
        if (this.limit > 0L) {
            pipeline.add((Object)new BsonDocument("$limit", new BsonInt64(this.limit)));
        }
        pipeline.add((Object)new BsonDocument("$group", new BsonDocument("_id", new BsonInt32(1)).append("n", new BsonDocument("$sum", new BsonInt32(1)))));
        return (List<BsonDocument>)pipeline;
    }
    
    private Long getCountFromAggregateResults(final List<BsonDocument> results) {
        if (results == null || results.isEmpty()) {
            return 0L;
        }
        return ((BsonDocument)results.get(0)).getNumber("n").longValue();
    }
    
    static {
        DECODER = new BsonDocumentCodec();
    }
}
