package cc.dreamcode.antylogout.libs.com.mongodb.internal.operation;

import cc.dreamcode.antylogout.libs.org.bson.BsonArray;
import java.util.Collection;
import cc.dreamcode.antylogout.libs.org.bson.BsonBoolean;
import java.util.ArrayList;
import cc.dreamcode.antylogout.libs.org.bson.BsonInt32;
import cc.dreamcode.antylogout.libs.org.bson.BsonString;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.SingleResultCallback;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.AsyncReadBinding;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.ReadBinding;
import cc.dreamcode.antylogout.libs.org.bson.BsonValue;
import cc.dreamcode.antylogout.libs.com.mongodb.client.model.Collation;
import java.util.concurrent.TimeUnit;
import cc.dreamcode.antylogout.libs.com.mongodb.lang.Nullable;
import cc.dreamcode.antylogout.libs.com.mongodb.assertions.Assertions;
import java.util.List;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoNamespace;
import cc.dreamcode.antylogout.libs.org.bson.BsonTimestamp;
import cc.dreamcode.antylogout.libs.org.bson.BsonDocument;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.client.model.changestream.ChangeStreamLevel;
import cc.dreamcode.antylogout.libs.org.bson.codecs.Decoder;
import cc.dreamcode.antylogout.libs.com.mongodb.client.model.changestream.FullDocumentBeforeChange;
import cc.dreamcode.antylogout.libs.com.mongodb.client.model.changestream.FullDocument;
import cc.dreamcode.antylogout.libs.org.bson.RawBsonDocument;
import cc.dreamcode.antylogout.libs.org.bson.codecs.RawBsonDocumentCodec;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.AsyncBatchCursor;

public class ChangeStreamOperation<T> implements AsyncReadOperation<AsyncBatchCursor<T>>, ReadOperation<BatchCursor<T>>
{
    private static final RawBsonDocumentCodec RAW_BSON_DOCUMENT_CODEC;
    private final AggregateOperationImpl<RawBsonDocument> wrapped;
    private final FullDocument fullDocument;
    private final FullDocumentBeforeChange fullDocumentBeforeChange;
    private final Decoder<T> decoder;
    private final ChangeStreamLevel changeStreamLevel;
    private BsonDocument resumeAfter;
    private BsonDocument startAfter;
    private BsonTimestamp startAtOperationTime;
    private boolean showExpandedEvents;
    
    public ChangeStreamOperation(final MongoNamespace namespace, final FullDocument fullDocument, final FullDocumentBeforeChange fullDocumentBeforeChange, final List<BsonDocument> pipeline, final Decoder<T> decoder) {
        this(namespace, fullDocument, fullDocumentBeforeChange, pipeline, decoder, ChangeStreamLevel.COLLECTION);
    }
    
    public ChangeStreamOperation(final MongoNamespace namespace, final FullDocument fullDocument, final FullDocumentBeforeChange fullDocumentBeforeChange, final List<BsonDocument> pipeline, final Decoder<T> decoder, final ChangeStreamLevel changeStreamLevel) {
        this.wrapped = new AggregateOperationImpl<RawBsonDocument>(namespace, pipeline, ChangeStreamOperation.RAW_BSON_DOCUMENT_CODEC, this.getAggregateTarget(), this.getPipelineCreator());
        this.fullDocument = Assertions.notNull("fullDocument", fullDocument);
        this.fullDocumentBeforeChange = Assertions.notNull("fullDocumentBeforeChange", fullDocumentBeforeChange);
        this.decoder = Assertions.notNull("decoder", decoder);
        this.changeStreamLevel = Assertions.notNull("changeStreamLevel", changeStreamLevel);
    }
    
    public MongoNamespace getNamespace() {
        return this.wrapped.getNamespace();
    }
    
    public Decoder<T> getDecoder() {
        return this.decoder;
    }
    
    public FullDocument getFullDocument() {
        return this.fullDocument;
    }
    
    public BsonDocument getResumeAfter() {
        return this.resumeAfter;
    }
    
    public ChangeStreamOperation<T> resumeAfter(final BsonDocument resumeAfter) {
        this.resumeAfter = resumeAfter;
        return this;
    }
    
    public BsonDocument getStartAfter() {
        return this.startAfter;
    }
    
    public ChangeStreamOperation<T> startAfter(final BsonDocument startAfter) {
        this.startAfter = startAfter;
        return this;
    }
    
    public List<BsonDocument> getPipeline() {
        return this.wrapped.getPipeline();
    }
    
    public Integer getBatchSize() {
        return this.wrapped.getBatchSize();
    }
    
    public ChangeStreamOperation<T> batchSize(@Nullable final Integer batchSize) {
        this.wrapped.batchSize(batchSize);
        return this;
    }
    
    public long getMaxAwaitTime(final TimeUnit timeUnit) {
        return this.wrapped.getMaxAwaitTime(timeUnit);
    }
    
    public ChangeStreamOperation<T> maxAwaitTime(final long maxAwaitTime, final TimeUnit timeUnit) {
        this.wrapped.maxAwaitTime(maxAwaitTime, timeUnit);
        return this;
    }
    
    public Collation getCollation() {
        return this.wrapped.getCollation();
    }
    
    public ChangeStreamOperation<T> collation(final Collation collation) {
        this.wrapped.collation(collation);
        return this;
    }
    
    public ChangeStreamOperation<T> startAtOperationTime(final BsonTimestamp startAtOperationTime) {
        this.startAtOperationTime = startAtOperationTime;
        return this;
    }
    
    public BsonTimestamp getStartAtOperationTime() {
        return this.startAtOperationTime;
    }
    
    public ChangeStreamOperation<T> retryReads(final boolean retryReads) {
        this.wrapped.retryReads(retryReads);
        return this;
    }
    
    public boolean getRetryReads() {
        return this.wrapped.getRetryReads();
    }
    
    @Nullable
    public BsonValue getComment() {
        return this.wrapped.getComment();
    }
    
    public ChangeStreamOperation<T> comment(final BsonValue comment) {
        this.wrapped.comment(comment);
        return this;
    }
    
    public boolean getShowExpandedEvents() {
        return this.showExpandedEvents;
    }
    
    public ChangeStreamOperation<T> showExpandedEvents(final boolean showExpandedEvents) {
        this.showExpandedEvents = showExpandedEvents;
        return this;
    }
    
    @Override
    public BatchCursor<T> execute(final ReadBinding binding) {
        final CommandBatchCursor<RawBsonDocument> cursor = (CommandBatchCursor)this.wrapped.execute(binding);
        return new ChangeStreamBatchCursor<T>(this, cursor, binding, this.setChangeStreamOptions(cursor.getPostBatchResumeToken(), cursor.getOperationTime(), cursor.getMaxWireVersion(), cursor.isFirstBatchEmpty()), cursor.getMaxWireVersion());
    }
    
    @Override
    public void executeAsync(final AsyncReadBinding binding, final SingleResultCallback<AsyncBatchCursor<T>> callback) {
        this.wrapped.executeAsync(binding, (result, t) -> {
            if (t != null) {
                callback.onResult(null, t);
            }
            else {
                final AsyncCommandBatchCursor<RawBsonDocument> cursor = Assertions.assertNotNull((AsyncCommandBatchCursor<RawBsonDocument>)result);
                callback.onResult(new AsyncChangeStreamBatchCursor((ChangeStreamOperation<Object>)this, cursor, binding, this.setChangeStreamOptions(cursor.getPostBatchResumeToken(), cursor.getOperationTime(), cursor.getMaxWireVersion(), cursor.isFirstBatchEmpty()), cursor.getMaxWireVersion()), null);
            }
        });
    }
    
    @Nullable
    private BsonDocument setChangeStreamOptions(@Nullable final BsonDocument postBatchResumeToken, @Nullable final BsonTimestamp operationTime, final int maxWireVersion, final boolean firstBatchEmpty) {
        BsonDocument resumeToken = null;
        if (this.startAfter != null) {
            resumeToken = this.startAfter;
        }
        else if (this.resumeAfter != null) {
            resumeToken = this.resumeAfter;
        }
        else if (this.startAtOperationTime == null && postBatchResumeToken == null && firstBatchEmpty && maxWireVersion >= 7) {
            this.startAtOperationTime = operationTime;
        }
        return resumeToken;
    }
    
    public void setChangeStreamOptionsForResume(@Nullable final BsonDocument resumeToken, final int maxWireVersion) {
        this.startAfter = null;
        if (resumeToken != null) {
            this.startAtOperationTime = null;
            this.resumeAfter = resumeToken;
        }
        else if (this.startAtOperationTime != null && maxWireVersion >= 7) {
            this.resumeAfter = null;
        }
        else {
            this.resumeAfter = null;
            this.startAtOperationTime = null;
        }
    }
    
    private AggregateOperationImpl.AggregateTarget getAggregateTarget() {
        return () -> {
            if (this.changeStreamLevel == ChangeStreamLevel.COLLECTION) {
                new(cc.dreamcode.antylogout.libs.org.bson.BsonString.class)();
                new BsonString(this.getNamespace().getCollectionName());
            }
            else {
                new(cc.dreamcode.antylogout.libs.org.bson.BsonInt32.class)();
                new BsonInt32(1);
            }
            return;
        };
    }
    
    private AggregateOperationImpl.PipelineCreator getPipelineCreator() {
        return () -> {
            final List<BsonDocument> changeStreamPipeline = (List<BsonDocument>)new ArrayList();
            final BsonDocument changeStream = new BsonDocument();
            if (this.fullDocument != FullDocument.DEFAULT) {
                changeStream.append("fullDocument", new BsonString(this.fullDocument.getValue()));
            }
            if (this.fullDocumentBeforeChange != FullDocumentBeforeChange.DEFAULT) {
                changeStream.append("fullDocumentBeforeChange", new BsonString(this.fullDocumentBeforeChange.getValue()));
            }
            if (this.changeStreamLevel == ChangeStreamLevel.CLIENT) {
                changeStream.append("allChangesForCluster", BsonBoolean.TRUE);
            }
            if (this.showExpandedEvents) {
                changeStream.append("showExpandedEvents", BsonBoolean.TRUE);
            }
            if (this.resumeAfter != null) {
                changeStream.append("resumeAfter", this.resumeAfter);
            }
            if (this.startAfter != null) {
                changeStream.append("startAfter", this.startAfter);
            }
            if (this.startAtOperationTime != null) {
                changeStream.append("startAtOperationTime", this.startAtOperationTime);
            }
            changeStreamPipeline.add((Object)new BsonDocument("$changeStream", changeStream));
            changeStreamPipeline.addAll((Collection)this.getPipeline());
            return new BsonArray(changeStreamPipeline);
        };
    }
    
    static {
        RAW_BSON_DOCUMENT_CODEC = new RawBsonDocumentCodec();
    }
}
