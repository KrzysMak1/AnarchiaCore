package cc.dreamcode.antylogout.libs.com.mongodb.internal.connection;

import java.io.IOException;
import com.github.luben.zstd.ZstdInputStream;
import java.io.InputStream;
import java.util.Iterator;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoInternalException;
import com.github.luben.zstd.Zstd;
import cc.dreamcode.antylogout.libs.org.bson.io.BsonOutput;
import cc.dreamcode.antylogout.libs.org.bson.ByteBuf;
import java.util.List;

class ZstdCompressor extends Compressor
{
    public String getName() {
        return "zstd";
    }
    
    public byte getId() {
        return 3;
    }
    
    public void compress(final List<ByteBuf> source, final BsonOutput target) {
        final int uncompressedSize = this.getUncompressedSize(source);
        final byte[] singleByteArraySource = new byte[uncompressedSize];
        this.copy(source, singleByteArraySource);
        try {
            final byte[] out = new byte[(int)Zstd.compressBound((long)uncompressedSize)];
            final int compressedSize = (int)Zstd.compress(out, singleByteArraySource, Zstd.defaultCompressionLevel());
            target.writeBytes(out, 0, compressedSize);
        }
        catch (final Exception e) {
            throw new MongoInternalException("Unexpected exception", (Throwable)e);
        }
    }
    
    private int getUncompressedSize(final List<ByteBuf> source) {
        int uncompressedSize = 0;
        for (final ByteBuf cur : source) {
            uncompressedSize += cur.remaining();
        }
        return uncompressedSize;
    }
    
    private void copy(final List<ByteBuf> source, final byte[] in) {
        int offset = 0;
        for (final ByteBuf cur : source) {
            final int remaining = cur.remaining();
            cur.get(in, offset, remaining);
            offset += remaining;
        }
    }
    
    @Override
    InputStream getInputStream(final InputStream source) throws IOException {
        return (InputStream)new ZstdInputStream(source);
    }
}
