package cc.dreamcode.antylogout.libs.com.mongodb.internal.operation;

import cc.dreamcode.antylogout.libs.com.mongodb.ServerAddress;
import cc.dreamcode.antylogout.libs.com.mongodb.ServerCursor;
import java.util.List;

class MapReduceInlineResultsCursor<T> implements MapReduceBatchCursor<T>
{
    private final BatchCursor<T> delegate;
    private final MapReduceStatistics statistics;
    
    MapReduceInlineResultsCursor(final BatchCursor<T> delegate, final MapReduceStatistics statistics) {
        this.delegate = delegate;
        this.statistics = statistics;
    }
    
    @Override
    public MapReduceStatistics getStatistics() {
        return this.statistics;
    }
    
    @Override
    public boolean hasNext() {
        return this.delegate.hasNext();
    }
    
    @Override
    public List<T> next() {
        return this.delegate.next();
    }
    
    @Override
    public int available() {
        return this.delegate.available();
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
    public List<T> tryNext() {
        return this.delegate.tryNext();
    }
    
    @Override
    public ServerCursor getServerCursor() {
        return this.delegate.getServerCursor();
    }
    
    @Override
    public ServerAddress getServerAddress() {
        return this.delegate.getServerAddress();
    }
    
    @Override
    public void close() {
        this.delegate.close();
    }
}
