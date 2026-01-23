package cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.tlschannel;

import java.nio.Buffer;
import javax.net.ssl.SSLHandshakeException;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.diagnostics.logging.Loggers;
import java.util.Map;
import javax.net.ssl.SNIHostName;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.tlschannel.impl.TlsExplorer;
import javax.net.ssl.SNIServerName;
import java.nio.channels.WritableByteChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.ClosedChannelException;
import java.io.IOException;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.tlschannel.impl.ByteBufferSet;
import java.nio.ByteBuffer;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.tlschannel.impl.TlsChannelImpl;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.tlschannel.impl.BufferHolder;
import java.util.concurrent.locks.Lock;
import javax.net.ssl.SSLSession;
import java.util.function.Consumer;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLContext;
import java.util.function.Function;
import java.nio.channels.ByteChannel;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.diagnostics.logging.Logger;

public class ServerTlsChannel implements TlsChannel
{
    private static final Logger LOGGER;
    private final ByteChannel underlying;
    private final SslContextStrategy sslContextStrategy;
    private final Function<SSLContext, SSLEngine> engineFactory;
    private final Consumer<SSLSession> sessionInitCallback;
    private final boolean runTasks;
    private final TrackingAllocator plainBufAllocator;
    private final TrackingAllocator encryptedBufAllocator;
    private final boolean releaseBuffers;
    private final boolean waitForCloseConfirmation;
    private final Lock initLock;
    private BufferHolder inEncrypted;
    private volatile boolean sniRead;
    private SSLContext sslContext;
    private TlsChannelImpl impl;
    
    private static SSLEngine defaultSSLEngineFactory(final SSLContext sslContext) {
        final SSLEngine engine = sslContext.createSSLEngine();
        engine.setUseClientMode(false);
        return engine;
    }
    
    public static Builder newBuilder(final ByteChannel underlying, final SSLContext sslContext) {
        return new Builder(underlying, sslContext);
    }
    
    public static Builder newBuilder(final ByteChannel underlying, final SniSslContextFactory sslContextFactory) {
        return new Builder(underlying, sslContextFactory);
    }
    
    private ServerTlsChannel(final ByteChannel underlying, final SslContextStrategy internalSslContextFactory, final Function<SSLContext, SSLEngine> engineFactory, final Consumer<SSLSession> sessionInitCallback, final boolean runTasks, final BufferAllocator plainBufAllocator, final BufferAllocator encryptedBufAllocator, final boolean releaseBuffers, final boolean waitForCloseConfirmation) {
        this.initLock = (Lock)new ReentrantLock();
        this.sniRead = false;
        this.sslContext = null;
        this.impl = null;
        this.underlying = underlying;
        this.sslContextStrategy = internalSslContextFactory;
        this.engineFactory = engineFactory;
        this.sessionInitCallback = sessionInitCallback;
        this.runTasks = runTasks;
        this.plainBufAllocator = new TrackingAllocator(plainBufAllocator);
        this.encryptedBufAllocator = new TrackingAllocator(encryptedBufAllocator);
        this.releaseBuffers = releaseBuffers;
        this.waitForCloseConfirmation = waitForCloseConfirmation;
        this.inEncrypted = new BufferHolder("inEncrypted", (Optional<ByteBuffer>)Optional.empty(), encryptedBufAllocator, 4096, 17408, false, releaseBuffers);
    }
    
    @Override
    public ByteChannel getUnderlying() {
        return this.underlying;
    }
    
    public SSLContext getSslContext() {
        return this.sslContext;
    }
    
    @Override
    public SSLEngine getSslEngine() {
        return (this.impl == null) ? null : this.impl.engine();
    }
    
    @Override
    public Consumer<SSLSession> getSessionInitCallback() {
        return this.sessionInitCallback;
    }
    
    @Override
    public boolean getRunTasks() {
        return this.impl.getRunTasks();
    }
    
    @Override
    public TrackingAllocator getPlainBufferAllocator() {
        return this.plainBufAllocator;
    }
    
    @Override
    public TrackingAllocator getEncryptedBufferAllocator() {
        return this.encryptedBufAllocator;
    }
    
    @Override
    public long read(final ByteBuffer[] dstBuffers, final int offset, final int length) throws IOException {
        final ByteBufferSet dest = new ByteBufferSet(dstBuffers, offset, length);
        TlsChannelImpl.checkReadBuffer(dest);
        if (!this.sniRead) {
            try {
                this.initEngine();
            }
            catch (final TlsChannelImpl.EofException e) {
                return -1L;
            }
        }
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
    public long write(final ByteBuffer[] srcs, final int offset, final int length) throws IOException {
        final ByteBufferSet source = new ByteBufferSet(srcs, offset, length);
        if (!this.sniRead) {
            try {
                this.initEngine();
            }
            catch (final TlsChannelImpl.EofException e) {
                throw new ClosedChannelException();
            }
        }
        return this.impl.write(source);
    }
    
    @Override
    public long write(final ByteBuffer[] srcs) throws IOException {
        return this.write(srcs, 0, srcs.length);
    }
    
    @Override
    public int write(final ByteBuffer srcBuffer) throws IOException {
        return (int)this.write(new ByteBuffer[] { srcBuffer });
    }
    
    @Override
    public void renegotiate() throws IOException {
        if (!this.sniRead) {
            try {
                this.initEngine();
            }
            catch (final TlsChannelImpl.EofException e) {
                throw new ClosedChannelException();
            }
        }
        this.impl.renegotiate();
    }
    
    @Override
    public void handshake() throws IOException {
        if (!this.sniRead) {
            try {
                this.initEngine();
            }
            catch (final TlsChannelImpl.EofException e) {
                throw new ClosedChannelException();
            }
        }
        this.impl.handshake();
    }
    
    @Override
    public void close() throws IOException {
        if (this.impl != null) {
            this.impl.close();
        }
        if (this.inEncrypted != null) {
            this.inEncrypted.dispose();
        }
        this.underlying.close();
    }
    
    public boolean isOpen() {
        return this.underlying.isOpen();
    }
    
    private void initEngine() throws IOException, TlsChannelImpl.EofException {
        this.initLock.lock();
        try {
            if (!this.sniRead) {
                this.sslContext = this.sslContextStrategy.getSslContext(this::getServerNameIndication);
                SSLEngine engine;
                try {
                    engine = (SSLEngine)this.engineFactory.apply((Object)this.sslContext);
                }
                catch (final Exception e) {
                    ServerTlsChannel.LOGGER.trace("client threw exception in SSLEngine factory", (Throwable)e);
                    throw new TlsChannelCallbackException("SSLEngine creation callback failed", (Throwable)e);
                }
                this.impl = new TlsChannelImpl((ReadableByteChannel)this.underlying, (WritableByteChannel)this.underlying, engine, (Optional<BufferHolder>)Optional.of((Object)this.inEncrypted), this.sessionInitCallback, this.runTasks, this.plainBufAllocator, this.encryptedBufAllocator, this.releaseBuffers, this.waitForCloseConfirmation);
                this.inEncrypted = null;
                this.sniRead = true;
            }
        }
        finally {
            this.initLock.unlock();
        }
    }
    
    private Optional<SNIServerName> getServerNameIndication() throws IOException, TlsChannelImpl.EofException {
        this.inEncrypted.prepare();
        try {
            final int recordHeaderSize = this.readRecordHeaderSize();
            while (this.inEncrypted.buffer.position() < recordHeaderSize) {
                if (!this.inEncrypted.buffer.hasRemaining()) {
                    this.inEncrypted.enlarge();
                }
                TlsChannelImpl.readFromChannel((ReadableByteChannel)this.underlying, this.inEncrypted.buffer);
            }
            ((Buffer)this.inEncrypted.buffer).flip();
            final Map<Integer, SNIServerName> serverNames = TlsExplorer.explore(this.inEncrypted.buffer);
            this.inEncrypted.buffer.compact();
            final SNIServerName hostName = (SNIServerName)serverNames.get((Object)0);
            if (hostName != null && hostName instanceof SNIHostName) {
                final SNIHostName sniHostName = (SNIHostName)hostName;
                return (Optional<SNIServerName>)Optional.of((Object)sniHostName);
            }
            return (Optional<SNIServerName>)Optional.empty();
        }
        finally {
            this.inEncrypted.release();
        }
    }
    
    private int readRecordHeaderSize() throws IOException, TlsChannelImpl.EofException {
        while (this.inEncrypted.buffer.position() < 5) {
            if (!this.inEncrypted.buffer.hasRemaining()) {
                throw new IllegalStateException("inEncrypted too small");
            }
            TlsChannelImpl.readFromChannel((ReadableByteChannel)this.underlying, this.inEncrypted.buffer);
        }
        ((Buffer)this.inEncrypted.buffer).flip();
        final int recordHeaderSize = TlsExplorer.getRequiredSize(this.inEncrypted.buffer);
        this.inEncrypted.buffer.compact();
        return recordHeaderSize;
    }
    
    @Override
    public boolean shutdown() throws IOException {
        return this.impl != null && this.impl.shutdown();
    }
    
    @Override
    public boolean shutdownReceived() {
        return this.impl != null && this.impl.shutdownReceived();
    }
    
    @Override
    public boolean shutdownSent() {
        return this.impl != null && this.impl.shutdownSent();
    }
    
    static {
        LOGGER = Loggers.getLogger("connection.tls");
    }
    
    private static class SniSslContextStrategy implements SslContextStrategy
    {
        private final SniSslContextFactory sniSslContextFactory;
        
        public SniSslContextStrategy(final SniSslContextFactory sniSslContextFactory) {
            this.sniSslContextFactory = sniSslContextFactory;
        }
        
        @Override
        public SSLContext getSslContext(final SniReader sniReader) throws IOException, TlsChannelImpl.EofException {
            final Optional<SNIServerName> nameOpt = sniReader.readSni();
            Optional<SSLContext> chosenContext;
            try {
                chosenContext = this.sniSslContextFactory.getSslContext(nameOpt);
            }
            catch (final Exception e) {
                ServerTlsChannel.LOGGER.trace("client code threw exception during evaluation of server name indication", (Throwable)e);
                throw new TlsChannelCallbackException("SNI callback failed", (Throwable)e);
            }
            return (SSLContext)chosenContext.orElseThrow(() -> new SSLHandshakeException("No ssl context available for received SNI: " + (Object)nameOpt));
        }
    }
    
    private static class FixedSslContextStrategy implements SslContextStrategy
    {
        private final SSLContext sslContext;
        
        public FixedSslContextStrategy(final SSLContext sslContext) {
            this.sslContext = sslContext;
        }
        
        @Override
        public SSLContext getSslContext(final SniReader sniReader) {
            return this.sslContext;
        }
    }
    
    public static class Builder extends TlsChannelBuilder<Builder>
    {
        private final SslContextStrategy internalSslContextFactory;
        private Function<SSLContext, SSLEngine> sslEngineFactory;
        
        private Builder(final ByteChannel underlying, final SSLContext sslContext) {
            super(underlying);
            this.sslEngineFactory = (Function<SSLContext, SSLEngine>)(x$0 -> defaultSSLEngineFactory(x$0));
            this.internalSslContextFactory = new FixedSslContextStrategy(sslContext);
        }
        
        private Builder(final ByteChannel wrapped, final SniSslContextFactory sslContextFactory) {
            super(wrapped);
            this.sslEngineFactory = (Function<SSLContext, SSLEngine>)(x$0 -> defaultSSLEngineFactory(x$0));
            this.internalSslContextFactory = new SniSslContextStrategy(sslContextFactory);
        }
        
        @Override
        Builder getThis() {
            return this;
        }
        
        public Builder withEngineFactory(final Function<SSLContext, SSLEngine> sslEngineFactory) {
            this.sslEngineFactory = sslEngineFactory;
            return this;
        }
        
        public ServerTlsChannel build() {
            return new ServerTlsChannel(this.underlying, this.internalSslContextFactory, this.sslEngineFactory, this.sessionInitCallback, this.runTasks, this.plainBufferAllocator, this.encryptedBufferAllocator, this.releaseBuffers, this.waitForCloseConfirmation, null);
        }
    }
    
    private interface SslContextStrategy
    {
        SSLContext getSslContext(final SniReader p0) throws IOException, TlsChannelImpl.EofException;
        
        @FunctionalInterface
        public interface SniReader
        {
            Optional<SNIServerName> readSni() throws IOException, TlsChannelImpl.EofException;
        }
    }
}
