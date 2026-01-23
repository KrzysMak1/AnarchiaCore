package cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.tlschannel;

import java.nio.ByteBuffer;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

public class TrackingAllocator implements BufferAllocator
{
    private final BufferAllocator impl;
    private final LongAdder bytesAllocatedAdder;
    private final LongAdder bytesDeallocatedAdder;
    private final AtomicLong currentAllocationSize;
    private final LongAccumulator maxAllocationSizeAcc;
    private final LongAdder buffersAllocatedAdder;
    private final LongAdder buffersDeallocatedAdder;
    
    public TrackingAllocator(final BufferAllocator impl) {
        this.bytesAllocatedAdder = new LongAdder();
        this.bytesDeallocatedAdder = new LongAdder();
        this.currentAllocationSize = new AtomicLong();
        this.maxAllocationSizeAcc = new LongAccumulator(Math::max, 0L);
        this.buffersAllocatedAdder = new LongAdder();
        this.buffersDeallocatedAdder = new LongAdder();
        this.impl = impl;
    }
    
    @Override
    public ByteBuffer allocate(final int size) {
        this.bytesAllocatedAdder.add((long)size);
        this.currentAllocationSize.addAndGet((long)size);
        this.buffersAllocatedAdder.increment();
        return this.impl.allocate(size);
    }
    
    @Override
    public void free(final ByteBuffer buffer) {
        final int size = buffer.capacity();
        this.bytesDeallocatedAdder.add((long)size);
        this.maxAllocationSizeAcc.accumulate(this.currentAllocationSize.longValue());
        this.currentAllocationSize.addAndGet((long)(-size));
        this.buffersDeallocatedAdder.increment();
        this.impl.free(buffer);
    }
    
    public long bytesAllocated() {
        return this.bytesAllocatedAdder.longValue();
    }
    
    public long bytesDeallocated() {
        return this.bytesDeallocatedAdder.longValue();
    }
    
    public long currentAllocation() {
        return this.currentAllocationSize.longValue();
    }
    
    public long maxAllocation() {
        return this.maxAllocationSizeAcc.longValue();
    }
    
    public long buffersAllocated() {
        return this.buffersAllocatedAdder.longValue();
    }
    
    public long buffersDeallocated() {
        return this.buffersDeallocatedAdder.longValue();
    }
}
