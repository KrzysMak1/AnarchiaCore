package cc.dreamcode.antylogout.libs.com.mongodb.internal.connection;

import java.io.IOException;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Deflater;
import java.io.OutputStream;
import java.util.zip.InflaterInputStream;
import java.io.InputStream;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoCompressor;

class ZlibCompressor extends Compressor
{
    private final int level;
    
    ZlibCompressor(final MongoCompressor mongoCompressor) {
        this.level = mongoCompressor.getPropertyNonNull("LEVEL", -1);
    }
    
    public String getName() {
        return "zlib";
    }
    
    public byte getId() {
        return 2;
    }
    
    @Override
    InputStream getInputStream(final InputStream source) {
        return (InputStream)new InflaterInputStream(source);
    }
    
    @Override
    OutputStream getOutputStream(final OutputStream source) {
        return (OutputStream)new DeflaterOutputStream(source, new Deflater(this.level)) {
            public void close() throws IOException {
                try {
                    super.close();
                }
                finally {
                    this.def.end();
                }
            }
        };
    }
}
