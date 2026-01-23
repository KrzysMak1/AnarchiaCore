package cc.dreamcode.antylogout.libs.com.mongodb.internal.operation;

import cc.dreamcode.antylogout.libs.com.mongodb.MongoException;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.SingleResultCallback;
import java.util.Collections;
import java.util.List;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.AsyncBatchCursor;

class AsyncSingleBatchCursor<T> implements AsyncBatchCursor<T>
{
    private final List<T> batch;
    private final int batchSize;
    private volatile boolean hasNext;
    private volatile boolean closed;
    
    static <R> AsyncSingleBatchCursor<R> createEmptyAsyncSingleBatchCursor(final int batchSize) {
        return new AsyncSingleBatchCursor<R>((java.util.List<R>)Collections.emptyList(), batchSize);
    }
    
    AsyncSingleBatchCursor(final List<T> batch, final int batchSize) {
        this.hasNext = true;
        this.closed = false;
        this.batch = batch;
        this.batchSize = batchSize;
    }
    
    @Override
    public void close() {
        this.closed = true;
    }
    
    @Override
    public void next(final SingleResultCallback<List<T>> callback) {
        if (this.closed) {
            callback.onResult(null, (Throwable)new MongoException("next() called after the cursor was closed."));
        }
        else if (this.hasNext && !this.batch.isEmpty()) {
            this.hasNext = false;
            callback.onResult(this.batch, null);
        }
        else {
            this.closed = true;
            callback.onResult((List<T>)Collections.emptyList(), null);
        }
    }
    
    @Override
    public void setBatchSize(final int batchSize) {
    }
    
    @Override
    public int getBatchSize() {
        return this.batchSize;
    }
    
    @Override
    public boolean isClosed() {
        return this.closed;
    }
}
