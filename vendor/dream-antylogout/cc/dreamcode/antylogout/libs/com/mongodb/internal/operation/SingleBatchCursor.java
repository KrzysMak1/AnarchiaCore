package cc.dreamcode.antylogout.libs.com.mongodb.internal.operation;

import cc.dreamcode.antylogout.libs.com.mongodb.ServerCursor;
import java.util.NoSuchElementException;
import java.util.Collections;
import cc.dreamcode.antylogout.libs.com.mongodb.ServerAddress;
import java.util.List;

class SingleBatchCursor<T> implements BatchCursor<T>
{
    private final List<T> batch;
    private final ServerAddress serverAddress;
    private final int batchSize;
    private boolean hasNext;
    
    static <R> SingleBatchCursor<R> createEmptySingleBatchCursor(final ServerAddress serverAddress, final int batchSize) {
        return new SingleBatchCursor<R>((java.util.List<R>)Collections.emptyList(), batchSize, serverAddress);
    }
    
    SingleBatchCursor(final List<T> batch, final int batchSize, final ServerAddress serverAddress) {
        this.batch = batch;
        this.serverAddress = serverAddress;
        this.batchSize = batchSize;
        this.hasNext = !batch.isEmpty();
    }
    
    @Override
    public boolean hasNext() {
        return this.hasNext;
    }
    
    @Override
    public List<T> next() {
        if (this.hasNext) {
            this.hasNext = false;
            return this.batch;
        }
        throw new NoSuchElementException();
    }
    
    @Override
    public int available() {
        return this.hasNext ? 1 : 0;
    }
    
    @Override
    public void setBatchSize(final int batchSize) {
    }
    
    @Override
    public int getBatchSize() {
        return this.batchSize;
    }
    
    @Override
    public List<T> tryNext() {
        return this.hasNext ? this.next() : null;
    }
    
    @Override
    public ServerCursor getServerCursor() {
        return null;
    }
    
    @Override
    public ServerAddress getServerAddress() {
        return this.serverAddress;
    }
    
    @Override
    public void close() {
    }
}
