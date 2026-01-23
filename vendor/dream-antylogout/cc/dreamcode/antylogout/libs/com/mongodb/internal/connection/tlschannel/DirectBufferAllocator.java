package cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.tlschannel;

import java.nio.ByteBuffer;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.tlschannel.util.DirectBufferDeallocator;

public class DirectBufferAllocator implements BufferAllocator
{
    private final DirectBufferDeallocator deallocator;
    
    public DirectBufferAllocator() {
        this.deallocator = new DirectBufferDeallocator();
    }
    
    @Override
    public ByteBuffer allocate(final int size) {
        return ByteBuffer.allocateDirect(size);
    }
    
    @Override
    public void free(final ByteBuffer buffer) {
        this.deallocator.deallocate(buffer);
    }
}
