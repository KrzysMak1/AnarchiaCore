package cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.tlschannel;

import java.nio.ByteBuffer;

public interface BufferAllocator
{
    ByteBuffer allocate(final int p0);
    
    void free(final ByteBuffer p0);
}
