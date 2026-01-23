package cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.tlschannel;

import java.nio.ByteBuffer;

public class HeapBufferAllocator implements BufferAllocator
{
    @Override
    public ByteBuffer allocate(final int size) {
        return ByteBuffer.allocate(size);
    }
    
    @Override
    public void free(final ByteBuffer buffer) {
    }
}
