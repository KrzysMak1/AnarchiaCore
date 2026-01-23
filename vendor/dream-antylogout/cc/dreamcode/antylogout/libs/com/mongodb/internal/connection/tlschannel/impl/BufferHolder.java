package cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.tlschannel.impl;

import java.nio.Buffer;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.diagnostics.logging.Loggers;
import java.util.Optional;
import java.nio.ByteBuffer;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.tlschannel.BufferAllocator;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.diagnostics.logging.Logger;

public class BufferHolder
{
    private static final Logger LOGGER;
    private static final byte[] zeros;
    public final String name;
    public final BufferAllocator allocator;
    public final boolean plainData;
    public final int maxSize;
    public final boolean opportunisticDispose;
    public ByteBuffer buffer;
    public int lastSize;
    
    public BufferHolder(final String name, final Optional<ByteBuffer> buffer, final BufferAllocator allocator, final int initialSize, final int maxSize, final boolean plainData, final boolean opportunisticDispose) {
        this.name = name;
        this.allocator = allocator;
        this.buffer = (ByteBuffer)buffer.orElse((Object)null);
        this.maxSize = maxSize;
        this.plainData = plainData;
        this.opportunisticDispose = opportunisticDispose;
        this.lastSize = (int)buffer.map(b -> b.capacity()).orElse((Object)initialSize);
    }
    
    public void prepare() {
        if (this.buffer == null) {
            this.buffer = this.allocator.allocate(this.lastSize);
        }
    }
    
    public boolean release() {
        return this.opportunisticDispose && this.buffer.position() == 0 && this.dispose();
    }
    
    public boolean dispose() {
        if (this.buffer != null) {
            this.allocator.free(this.buffer);
            this.buffer = null;
            return true;
        }
        return false;
    }
    
    public void resize(final int newCapacity) {
        if (newCapacity > this.maxSize) {
            throw new IllegalArgumentException(String.format("new capacity (%s) bigger than absolute max size (%s)", new Object[] { newCapacity, this.maxSize }));
        }
        if (BufferHolder.LOGGER.isTraceEnabled()) {
            BufferHolder.LOGGER.trace(String.format("resizing buffer %s, increasing from %s to %s (manual sizing)", new Object[] { this.name, this.buffer.capacity(), newCapacity }));
        }
        this.resizeImpl(newCapacity);
    }
    
    public void enlarge() {
        if (this.buffer.capacity() >= this.maxSize) {
            throw new IllegalStateException(String.format("%s buffer insufficient despite having capacity of %d", new Object[] { this.name, this.buffer.capacity() }));
        }
        final int newCapacity = Math.min(this.buffer.capacity() * 2, this.maxSize);
        if (BufferHolder.LOGGER.isTraceEnabled()) {
            BufferHolder.LOGGER.trace(String.format("enlarging buffer %s, increasing from %s to %s (automatic enlarge)", new Object[] { this.name, this.buffer.capacity(), newCapacity }));
        }
        this.resizeImpl(newCapacity);
    }
    
    private void resizeImpl(final int newCapacity) {
        final ByteBuffer newBuffer = this.allocator.allocate(newCapacity);
        ((Buffer)this.buffer).flip();
        newBuffer.put(this.buffer);
        if (this.plainData) {
            this.zero();
        }
        this.allocator.free(this.buffer);
        this.buffer = newBuffer;
        this.lastSize = newCapacity;
    }
    
    public void zeroRemaining() {
        this.zero(this.buffer.position());
    }
    
    public void zero() {
        this.zero(0);
    }
    
    private void zero(final int position) {
        ((Buffer)this.buffer).mark();
        ((Buffer)this.buffer).position(position);
        for (int size = this.buffer.remaining(), length = Math.min(size, BufferHolder.zeros.length), offset = 0; length > 0; length = Math.min(size - offset, BufferHolder.zeros.length)) {
            this.buffer.put(BufferHolder.zeros, 0, length);
            offset += length;
        }
        ((Buffer)this.buffer).reset();
    }
    
    public boolean nullOrEmpty() {
        return this.buffer == null || this.buffer.position() == 0;
    }
    
    static {
        LOGGER = Loggers.getLogger("connection.tls");
        zeros = new byte[17408];
    }
}
