package cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.tlschannel.impl;

import java.nio.Buffer;
import java.nio.ByteBuffer;

public class ByteBufferUtil
{
    public static void copy(final ByteBuffer src, final ByteBuffer dst, final int length) {
        if (length < 0) {
            throw new IllegalArgumentException("negative length");
        }
        if (src.remaining() < length) {
            throw new IllegalArgumentException(String.format("source buffer does not have enough remaining capacity (%d < %d)", new Object[] { src.remaining(), length }));
        }
        if (dst.remaining() < length) {
            throw new IllegalArgumentException(String.format("destination buffer does not have enough remaining capacity (%d < %d)", new Object[] { dst.remaining(), length }));
        }
        if (length == 0) {
            return;
        }
        final ByteBuffer tmp = src.duplicate();
        ((Buffer)tmp).limit(src.position() + length);
        dst.put(tmp);
        ((Buffer)src).position(src.position() + length);
    }
}
