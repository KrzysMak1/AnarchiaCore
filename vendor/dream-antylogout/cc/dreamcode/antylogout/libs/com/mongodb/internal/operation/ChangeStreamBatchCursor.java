package cc.dreamcode.antylogout.libs.com.mongodb.internal.operation;

import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.ConnectionSource;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoException;
import java.util.Iterator;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoChangeStreamException;
import cc.dreamcode.antylogout.libs.com.mongodb.assertions.Assertions;
import java.util.ArrayList;
import java.util.function.Consumer;
import cc.dreamcode.antylogout.libs.org.bson.codecs.Decoder;
import cc.dreamcode.antylogout.libs.org.bson.BsonTimestamp;
import cc.dreamcode.antylogout.libs.com.mongodb.ServerAddress;
import cc.dreamcode.antylogout.libs.com.mongodb.ServerCursor;
import java.util.List;
import java.util.function.Function;
import cc.dreamcode.antylogout.libs.com.mongodb.lang.Nullable;
import java.util.concurrent.atomic.AtomicBoolean;
import cc.dreamcode.antylogout.libs.org.bson.BsonDocument;
import cc.dreamcode.antylogout.libs.org.bson.RawBsonDocument;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.ReadBinding;

final class ChangeStreamBatchCursor<T> implements AggregateResponseBatchCursor<T>
{
    private final ReadBinding binding;
    private final ChangeStreamOperation<T> changeStreamOperation;
    private final int maxWireVersion;
    private CommandBatchCursor<RawBsonDocument> wrapped;
    private BsonDocument resumeToken;
    private final AtomicBoolean closed;
    
    ChangeStreamBatchCursor(final ChangeStreamOperation<T> changeStreamOperation, final CommandBatchCursor<RawBsonDocument> wrapped, final ReadBinding binding, @Nullable final BsonDocument resumeToken, final int maxWireVersion) {
        this.changeStreamOperation = changeStreamOperation;
        this.binding = binding.retain();
        this.wrapped = wrapped;
        this.resumeToken = resumeToken;
        this.maxWireVersion = maxWireVersion;
        this.closed = new AtomicBoolean();
    }
    
    CommandBatchCursor<RawBsonDocument> getWrapped() {
        return this.wrapped;
    }
    
    @Override
    public boolean hasNext() {
        return this.resumeableOperation((java.util.function.Function<AggregateResponseBatchCursor<RawBsonDocument>, Boolean>)(commandBatchCursor -> {
            try {
                return commandBatchCursor.hasNext();
            }
            finally {
                this.cachePostBatchResumeToken(commandBatchCursor);
            }
        }));
    }
    
    @Override
    public List<T> next() {
        return (List<T>)this.resumeableOperation((java.util.function.Function<AggregateResponseBatchCursor<RawBsonDocument>, List>)(commandBatchCursor -> {
            try {
                return convertAndProduceLastId(commandBatchCursor.next(), this.changeStreamOperation.getDecoder(), (Consumer<BsonDocument>)(lastId -> this.resumeToken = lastId));
            }
            finally {
                this.cachePostBatchResumeToken(commandBatchCursor);
            }
        }));
    }
    
    @Override
    public int available() {
        return this.wrapped.available();
    }
    
    @Override
    public List<T> tryNext() {
        return (List<T>)this.resumeableOperation((java.util.function.Function<AggregateResponseBatchCursor<RawBsonDocument>, List>)(commandBatchCursor -> {
            try {
                final List<RawBsonDocument> tryNext = commandBatchCursor.tryNext();
                return (tryNext == null) ? null : convertAndProduceLastId(tryNext, this.changeStreamOperation.getDecoder(), (Consumer<BsonDocument>)(lastId -> this.resumeToken = lastId));
            }
            finally {
                this.cachePostBatchResumeToken(commandBatchCursor);
            }
        }));
    }
    
    @Override
    public void close() {
        if (!this.closed.getAndSet(true)) {
            this.wrapped.close();
            this.binding.release();
        }
    }
    
    @Override
    public void setBatchSize(final int batchSize) {
        this.wrapped.setBatchSize(batchSize);
    }
    
    @Override
    public int getBatchSize() {
        return this.wrapped.getBatchSize();
    }
    
    @Override
    public ServerCursor getServerCursor() {
        return this.wrapped.getServerCursor();
    }
    
    @Override
    public ServerAddress getServerAddress() {
        return this.wrapped.getServerAddress();
    }
    
    public void remove() {
        throw new UnsupportedOperationException("Not implemented!");
    }
    
    @Override
    public BsonDocument getPostBatchResumeToken() {
        return this.wrapped.getPostBatchResumeToken();
    }
    
    @Override
    public BsonTimestamp getOperationTime() {
        return this.changeStreamOperation.getStartAtOperationTime();
    }
    
    @Override
    public boolean isFirstBatchEmpty() {
        return this.wrapped.isFirstBatchEmpty();
    }
    
    @Override
    public int getMaxWireVersion() {
        return this.maxWireVersion;
    }
    
    private void cachePostBatchResumeToken(final AggregateResponseBatchCursor<RawBsonDocument> commandBatchCursor) {
        if (commandBatchCursor.getPostBatchResumeToken() != null) {
            this.resumeToken = commandBatchCursor.getPostBatchResumeToken();
        }
    }
    
    static <T> List<T> convertAndProduceLastId(final List<RawBsonDocument> rawDocuments, final Decoder<T> decoder, final Consumer<BsonDocument> lastIdConsumer) {
        final List<T> results = (List<T>)new ArrayList();
        for (final RawBsonDocument rawDocument : Assertions.assertNotNull(rawDocuments)) {
            if (!rawDocument.containsKey("_id")) {
                throw new MongoChangeStreamException("Cannot provide resume functionality when the resume token is missing.");
            }
            results.add((Object)rawDocument.decode(decoder));
        }
        if (!rawDocuments.isEmpty()) {
            lastIdConsumer.accept((Object)((RawBsonDocument)rawDocuments.get(rawDocuments.size() - 1)).getDocument("_id"));
        }
        return results;
    }
    
     <R> R resumeableOperation(final Function<AggregateResponseBatchCursor<RawBsonDocument>, R> function) {
        try {
            return (R)function.apply((Object)this.wrapped);
        }
        catch (final Throwable t) {
            if (!ChangeStreamBatchCursorHelper.isResumableError(t, this.maxWireVersion)) {
                throw MongoException.fromThrowableNonNull(t);
            }
            this.wrapped.close();
            SyncOperationHelper.withReadConnectionSource(this.binding, source -> {
                this.changeStreamOperation.setChangeStreamOptionsForResume(this.resumeToken, source.getServerDescription().getMaxWireVersion());
                return null;
            });
            this.wrapped = ((ChangeStreamBatchCursor)this.changeStreamOperation.execute(this.binding)).getWrapped();
            this.binding.release();
            return (R)function.apply((Object)this.wrapped);
        }
    }
}
