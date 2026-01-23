package cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.tlschannel.impl;

import java.util.Arrays;
import java.nio.ByteBuffer;

public class ByteBufferSet
{
    public final ByteBuffer[] array;
    public final int offset;
    public final int length;
    
    public ByteBufferSet(final ByteBuffer[] array, final int offset, final int length) {
        if (array == null) {
            throw new NullPointerException();
        }
        if (array.length < offset) {
            throw new IndexOutOfBoundsException();
        }
        if (array.length < offset + length) {
            throw new IndexOutOfBoundsException();
        }
        for (int i = offset; i < offset + length; ++i) {
            if (array[i] == null) {
                throw new NullPointerException();
            }
        }
        this.array = array;
        this.offset = offset;
        this.length = length;
    }
    
    public ByteBufferSet(final ByteBuffer[] array) {
        this(array, 0, array.length);
    }
    
    public ByteBufferSet(final ByteBuffer buffer) {
        this(new ByteBuffer[] { buffer });
    }
    
    public long remaining() {
        long ret = 0L;
        for (int i = this.offset; i < this.offset + this.length; ++i) {
            ret += this.array[i].remaining();
        }
        return ret;
    }
    
    public int putRemaining(final ByteBuffer from) {
        int totalBytes = 0;
        for (int i = this.offset; i < this.offset + this.length && from.hasRemaining(); ++i) {
            final ByteBuffer dstBuffer = this.array[i];
            final int bytes = Math.min(from.remaining(), dstBuffer.remaining());
            ByteBufferUtil.copy(from, dstBuffer, bytes);
            totalBytes += bytes;
        }
        return totalBytes;
    }
    
    public ByteBufferSet put(final ByteBuffer from, final int length) {
        if (from.remaining() < length) {
            throw new IllegalArgumentException();
        }
        if (this.remaining() < length) {
            throw new IllegalArgumentException();
        }
        int totalBytes = 0;
        for (int i = this.offset; i < this.offset + this.length; ++i) {
            final int pending = length - totalBytes;
            if (pending == 0) {
                break;
            }
            final int bytes = Math.min(pending, (int)this.remaining());
            final ByteBuffer dstBuffer = this.array[i];
            ByteBufferUtil.copy(from, dstBuffer, bytes);
            totalBytes += bytes;
        }
        return this;
    }
    
    public int getRemaining(final ByteBuffer dst) {
        int totalBytes = 0;
        for (int i = this.offset; i < this.offset + this.length && dst.hasRemaining(); ++i) {
            final ByteBuffer srcBuffer = this.array[i];
            final int bytes = Math.min(dst.remaining(), srcBuffer.remaining());
            ByteBufferUtil.copy(srcBuffer, dst, bytes);
            totalBytes += bytes;
        }
        return totalBytes;
    }
    
    public ByteBufferSet get(final ByteBuffer dst, final int length) {
        if (this.remaining() < length) {
            throw new IllegalArgumentException();
        }
        if (dst.remaining() < length) {
            throw new IllegalArgumentException();
        }
        int totalBytes = 0;
        for (int i = this.offset; i < this.offset + this.length; ++i) {
            final int pending = length - totalBytes;
            if (pending == 0) {
                break;
            }
            final ByteBuffer srcBuffer = this.array[i];
            final int bytes = Math.min(pending, srcBuffer.remaining());
            ByteBufferUtil.copy(srcBuffer, dst, bytes);
            totalBytes += bytes;
        }
        return this;
    }
    
    public boolean hasRemaining() {
        return this.remaining() > 0L;
    }
    
    public boolean isReadOnly() {
        for (int i = this.offset; i < this.offset + this.length; ++i) {
            if (this.array[i].isReadOnly()) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public String toString() {
        return "ByteBufferSet[array=" + Arrays.toString((Object[])this.array) + ", offset=" + this.offset + ", length=" + this.length + "]";
    }
}
