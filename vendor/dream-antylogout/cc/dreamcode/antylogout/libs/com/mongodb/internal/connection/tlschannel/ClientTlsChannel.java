package cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.tlschannel;

import java.util.function.Supplier;
import java.io.IOException;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.tlschannel.impl.ByteBufferSet;
import java.nio.ByteBuffer;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.tlschannel.impl.BufferHolder;
import java.nio.channels.WritableByteChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.Optional;
import javax.net.ssl.SSLSession;
import java.util.function.Consumer;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLContext;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.tlschannel.impl.TlsChannelImpl;
import java.nio.channels.ByteChannel;

public class ClientTlsChannel implements TlsChannel
{
    private final ByteChannel underlying;
    private final TlsChannelImpl impl;
    
    private static SSLEngine defaultSSLEngineFactory(final SSLContext sslContext) {
        final SSLEngine engine = sslContext.createSSLEngine();
        engine.setUseClientMode(true);
        return engine;
    }
    
    public static Builder newBuilder(final ByteChannel underlying, final SSLEngine sslEngine) {
        return new Builder(underlying, sslEngine);
    }
    
    public static Builder newBuilder(final ByteChannel underlying, final SSLContext sslContext) {
        return new Builder(underlying, sslContext);
    }
    
    private ClientTlsChannel(final ByteChannel underlying, final SSLEngine engine, final Consumer<SSLSession> sessionInitCallback, final boolean runTasks, final BufferAllocator plainBufAllocator, final BufferAllocator encryptedBufAllocator, final boolean releaseBuffers, final boolean waitForCloseNotifyOnClose) {
        if (!engine.getUseClientMode()) {
            throw new IllegalArgumentException("SSLEngine must be in client mode");
        }
        this.underlying = underlying;
        final TrackingAllocator trackingPlainBufAllocator = new TrackingAllocator(plainBufAllocator);
        final TrackingAllocator trackingEncryptedAllocator = new TrackingAllocator(encryptedBufAllocator);
        this.impl = new TlsChannelImpl((ReadableByteChannel)underlying, (WritableByteChannel)underlying, engine, (Optional<BufferHolder>)Optional.empty(), sessionInitCallback, runTasks, trackingPlainBufAllocator, trackingEncryptedAllocator, releaseBuffers, waitForCloseNotifyOnClose);
    }
    
    @Override
    public ByteChannel getUnderlying() {
        return this.underlying;
    }
    
    @Override
    public SSLEngine getSslEngine() {
        return this.impl.engine();
    }
    
    @Override
    public Consumer<SSLSession> getSessionInitCallback() {
        return this.impl.getSessionInitCallback();
    }
    
    @Override
    public TrackingAllocator getPlainBufferAllocator() {
        return this.impl.getPlainBufferAllocator();
    }
    
    @Override
    public TrackingAllocator getEncryptedBufferAllocator() {
        return this.impl.getEncryptedBufferAllocator();
    }
    
    @Override
    public boolean getRunTasks() {
        return this.impl.getRunTasks();
    }
    
    @Override
    public long read(final ByteBuffer[] dstBuffers, final int offset, final int length) throws IOException {
        final ByteBufferSet dest = new ByteBufferSet(dstBuffers, offset, length);
        TlsChannelImpl.checkReadBuffer(dest);
        return this.impl.read(dest);
    }
    
    @Override
    public long read(final ByteBuffer[] dstBuffers) throws IOException {
        return this.read(dstBuffers, 0, dstBuffers.length);
    }
    
    @Override
    public int read(final ByteBuffer dstBuffer) throws IOException {
        return (int)this.read(new ByteBuffer[] { dstBuffer });
    }
    
    @Override
    public long write(final ByteBuffer[] srcBuffers, final int offset, final int length) throws IOException {
        final ByteBufferSet source = new ByteBufferSet(srcBuffers, offset, length);
        return this.impl.write(source);
    }
    
    @Override
    public long write(final ByteBuffer[] outs) throws IOException {
        return this.write(outs, 0, outs.length);
    }
    
    @Override
    public int write(final ByteBuffer srcBuffer) throws IOException {
        return (int)this.write(new ByteBuffer[] { srcBuffer });
    }
    
    @Override
    public void renegotiate() throws IOException {
        this.impl.renegotiate();
    }
    
    @Override
    public void handshake() throws IOException {
        this.impl.handshake();
    }
    
    @Override
    public void close() throws IOException {
        this.impl.close();
    }
    
    public boolean isOpen() {
        return this.impl.isOpen();
    }
    
    @Override
    public boolean shutdown() throws IOException {
        return this.impl.shutdown();
    }
    
    @Override
    public boolean shutdownReceived() {
        return this.impl.shutdownReceived();
    }
    
    @Override
    public boolean shutdownSent() {
        return this.impl.shutdownSent();
    }
    
    public static class Builder extends TlsChannelBuilder<Builder>
    {
        private final Supplier<SSLEngine> sslEngineFactory;
        
        private Builder(final ByteChannel underlying, final SSLEngine sslEngine) {
            super(underlying);
            this.sslEngineFactory = (Supplier<SSLEngine>)(() -> sslEngine);
        }
        
        private Builder(final ByteChannel underlying, final SSLContext sslContext) {
            super(underlying);
            this.sslEngineFactory = (Supplier<SSLEngine>)(() -> defaultSSLEngineFactory(sslContext));
        }
        
        @Override
        Builder getThis() {
            return this;
        }
        
        public ClientTlsChannel build() {
            return new ClientTlsChannel(this.underlying, (SSLEngine)this.sslEngineFactory.get(), this.sessionInitCallback, this.runTasks, this.plainBufferAllocator, this.encryptedBufferAllocator, this.releaseBuffers, this.waitForCloseConfirmation, null);
        }
    }
}
