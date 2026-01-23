package cc.dreamcode.antylogout.libs.com.mongodb.internal.operation;

import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.AsyncConnectionSource;
import java.util.function.Consumer;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoException;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.ErrorHandlingResultCallback;
import cc.dreamcode.antylogout.libs.org.bson.BsonTimestamp;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.AsyncBatchCursor;
import java.util.List;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.SingleResultCallback;
import cc.dreamcode.antylogout.libs.com.mongodb.lang.NonNull;
import cc.dreamcode.antylogout.libs.com.mongodb.assertions.Assertions;
import cc.dreamcode.antylogout.libs.com.mongodb.lang.Nullable;
import java.util.concurrent.atomic.AtomicBoolean;
import cc.dreamcode.antylogout.libs.org.bson.RawBsonDocument;
import java.util.concurrent.atomic.AtomicReference;
import cc.dreamcode.antylogout.libs.org.bson.BsonDocument;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.AsyncReadBinding;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.AsyncAggregateResponseBatchCursor;

final class AsyncChangeStreamBatchCursor<T> implements AsyncAggregateResponseBatchCursor<T>
{
    private final AsyncReadBinding binding;
    private final ChangeStreamOperation<T> changeStreamOperation;
    private final int maxWireVersion;
    private volatile BsonDocument resumeToken;
    private final AtomicReference<AsyncCommandBatchCursor<RawBsonDocument>> wrapped;
    private final AtomicBoolean isClosed;
    
    AsyncChangeStreamBatchCursor(final ChangeStreamOperation<T> changeStreamOperation, final AsyncCommandBatchCursor<RawBsonDocument> wrapped, final AsyncReadBinding binding, @Nullable final BsonDocument resumeToken, final int maxWireVersion) {
        this.changeStreamOperation = changeStreamOperation;
        this.wrapped = (AtomicReference<AsyncCommandBatchCursor<RawBsonDocument>>)new AtomicReference((Object)Assertions.assertNotNull(wrapped));
        (this.binding = binding).retain();
        this.resumeToken = resumeToken;
        this.maxWireVersion = maxWireVersion;
        this.isClosed = new AtomicBoolean();
    }
    
    @NonNull
    AsyncCommandBatchCursor<RawBsonDocument> getWrapped() {
        return Assertions.assertNotNull(this.wrapped.get());
    }
    
    @Override
    public void next(final SingleResultCallback<List<T>> callback) {
        this.resumeableOperation(AsyncBatchCursor::next, callback, false);
    }
    
    @Override
    public void close() {
        if (this.isClosed.compareAndSet(false, true)) {
            try {
                this.nullifyAndCloseWrapped();
            }
            finally {
                this.binding.release();
            }
        }
    }
    
    @Override
    public void setBatchSize(final int batchSize) {
        this.getWrapped().setBatchSize(batchSize);
    }
    
    @Override
    public int getBatchSize() {
        return this.getWrapped().getBatchSize();
    }
    
    @Override
    public boolean isClosed() {
        if (this.isClosed.get()) {
            return true;
        }
        if (this.wrappedClosedItself()) {
            this.close();
            return true;
        }
        return false;
    }
    
    private boolean wrappedClosedItself() {
        final AsyncAggregateResponseBatchCursor<RawBsonDocument> observedWrapped = (AsyncAggregateResponseBatchCursor<RawBsonDocument>)this.wrapped.get();
        return observedWrapped != null && observedWrapped.isClosed();
    }
    
    private void nullifyAndCloseWrapped() {
        try (final AsyncAggregateResponseBatchCursor<RawBsonDocument> observedWrapped = (AsyncAggregateResponseBatchCursor<RawBsonDocument>)this.wrapped.getAndSet((Object)null)) {}
    }
    
    private void setWrappedOrCloseIt(final AsyncCommandBatchCursor<RawBsonDocument> newValue) {
        if (this.isClosed()) {
            Assertions.assertNull(this.wrapped.get());
            newValue.close();
        }
        else {
            Assertions.assertNull(this.wrapped.getAndSet((Object)newValue));
            if (this.isClosed()) {
                this.nullifyAndCloseWrapped();
            }
        }
    }
    
    @Override
    public BsonDocument getPostBatchResumeToken() {
        return this.getWrapped().getPostBatchResumeToken();
    }
    
    @Override
    public BsonTimestamp getOperationTime() {
        return this.changeStreamOperation.getStartAtOperationTime();
    }
    
    @Override
    public boolean isFirstBatchEmpty() {
        return this.getWrapped().isFirstBatchEmpty();
    }
    
    @Override
    public int getMaxWireVersion() {
        return this.maxWireVersion;
    }
    
    private void cachePostBatchResumeToken(final AsyncCommandBatchCursor<RawBsonDocument> cursor) {
        final BsonDocument resumeToken = cursor.getPostBatchResumeToken();
        if (resumeToken != null) {
            this.resumeToken = resumeToken;
        }
    }
    
    private void resumeableOperation(final AsyncBlock asyncBlock, final SingleResultCallback<List<T>> callback, final boolean tryNext) {
        final SingleResultCallback<List<T>> errHandlingCallback = ErrorHandlingResultCallback.errorHandlingCallback(callback, OperationHelper.LOGGER);
        if (this.isClosed()) {
            errHandlingCallback.onResult(null, (Throwable)new MongoException(String.format("%s called after the cursor was closed.", new Object[] { tryNext ? "tryNext()" : "next()" })));
            return;
        }
        final AsyncCommandBatchCursor<RawBsonDocument> wrappedCursor = this.getWrapped();
        asyncBlock.apply(wrappedCursor, (result, t) -> {
            if (t == null) {
                try {
                    List<T> convertedResults;
                    try {
                        convertedResults = ChangeStreamBatchCursor.convertAndProduceLastId((List<RawBsonDocument>)Assertions.assertNotNull(result), this.changeStreamOperation.getDecoder(), (Consumer<BsonDocument>)(lastId -> this.resumeToken = lastId));
                    }
                    finally {
                        this.cachePostBatchResumeToken(wrappedCursor);
                    }
                    errHandlingCallback.onResult(convertedResults, null);
                }
                catch (final Exception e) {
                    errHandlingCallback.onResult(null, (Throwable)e);
                }
            }
            else {
                this.cachePostBatchResumeToken(wrappedCursor);
                if (ChangeStreamBatchCursorHelper.isResumableError(t, this.maxWireVersion)) {
                    this.nullifyAndCloseWrapped();
                    this.retryOperation(asyncBlock, errHandlingCallback, tryNext);
                }
                else {
                    errHandlingCallback.onResult(null, t);
                }
            }
        });
    }
    
    private void retryOperation(final AsyncBlock asyncBlock, final SingleResultCallback<List<T>> callback, final boolean tryNext) {
        AsyncOperationHelper.withAsyncReadConnectionSource(this.binding, (source, t) -> {
            if (t != null) {
                callback.onResult(null, t);
            }
            else {
                this.changeStreamOperation.setChangeStreamOptionsForResume(this.resumeToken, Assertions.assertNotNull(source).getServerDescription().getMaxWireVersion());
                source.release();
                this.changeStreamOperation.executeAsync(this.binding, (result, t1) -> {
                    if (t1 != null) {
                        callback.onResult(null, t1);
                    }
                    else {
                        try {
                            this.setWrappedOrCloseIt(Assertions.assertNotNull(result).getWrapped());
                        }
                        finally {
                            try {
                                this.binding.release();
                            }
                            finally {
                                this.resumeableOperation(asyncBlock, callback, tryNext);
                            }
                        }
                    }
                });
            }
        });
    }
    
    private interface AsyncBlock
    {
        void apply(final AsyncAggregateResponseBatchCursor<RawBsonDocument> p0, final SingleResultCallback<List<RawBsonDocument>> p1);
    }
}
