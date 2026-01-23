package cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.tlschannel;

import java.io.IOException;
import java.nio.ByteBuffer;
import javax.net.ssl.SSLSession;
import java.util.function.Consumer;
import javax.net.ssl.SSLEngine;
import java.nio.channels.ScatteringByteChannel;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ByteChannel;

public interface TlsChannel extends ByteChannel, GatheringByteChannel, ScatteringByteChannel
{
    public static final BufferAllocator defaultPlainBufferAllocator = new HeapBufferAllocator();
    public static final BufferAllocator defaultEncryptedBufferAllocator = new DirectBufferAllocator();
    
    ByteChannel getUnderlying();
    
    SSLEngine getSslEngine();
    
    Consumer<SSLSession> getSessionInitCallback();
    
    TrackingAllocator getPlainBufferAllocator();
    
    TrackingAllocator getEncryptedBufferAllocator();
    
    boolean getRunTasks();
    
    int read(final ByteBuffer p0) throws IOException;
    
    int write(final ByteBuffer p0) throws IOException;
    
    void renegotiate() throws IOException;
    
    void handshake() throws IOException;
    
    long write(final ByteBuffer[] p0, final int p1, final int p2) throws IOException;
    
    long write(final ByteBuffer[] p0) throws IOException;
    
    long read(final ByteBuffer[] p0, final int p1, final int p2) throws IOException;
    
    long read(final ByteBuffer[] p0) throws IOException;
    
    void close() throws IOException;
    
    boolean shutdown() throws IOException;
    
    boolean shutdownReceived();
    
    boolean shutdownSent();
}
