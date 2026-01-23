package cc.dreamcode.antylogout.libs.com.mongodb.internal.operation;

import java.util.List;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.SingleResultCallback;

class MapReduceInlineResultsAsyncCursor<T> implements MapReduceAsyncBatchCursor<T>
{
    private final AsyncSingleBatchCursor<T> delegate;
    private final MapReduceStatistics statistics;
    
    MapReduceInlineResultsAsyncCursor(final AsyncSingleBatchCursor<T> delegate, final MapReduceStatistics statistics) {
        this.delegate = delegate;
        this.statistics = statistics;
    }
    
    @Override
    public MapReduceStatistics getStatistics() {
        return this.statistics;
    }
    
    @Override
    public void next(final SingleResultCallback<List<T>> callback) {
        this.delegate.next(callback);
    }
    
    @Override
    public void setBatchSize(final int batchSize) {
        this.delegate.setBatchSize(batchSize);
    }
    
    @Override
    public int getBatchSize() {
        return this.delegate.getBatchSize();
    }
    
    @Override
    public boolean isClosed() {
        return this.delegate.isClosed();
    }
    
    @Override
    public void close() {
        this.delegate.close();
    }
}
