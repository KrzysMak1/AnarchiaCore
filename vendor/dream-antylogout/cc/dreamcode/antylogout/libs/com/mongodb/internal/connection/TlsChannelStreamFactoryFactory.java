package cc.dreamcode.antylogout.libs.com.mongodb.internal.connection;

import java.util.concurrent.Future;
import cc.dreamcode.antylogout.libs.com.mongodb.lang.Nullable;
import java.util.concurrent.TimeUnit;
import java.nio.channels.CompletionHandler;
import java.nio.ByteBuffer;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.tlschannel.TlsChannel;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.tlschannel.BufferAllocator;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLEngine;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.tlschannel.async.AsynchronousTlsChannel;
import java.nio.channels.ByteChannel;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.tlschannel.ClientTlsChannel;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import javax.net.ssl.SSLContext;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoSocketOpenException;
import java.net.SocketAddress;
import java.net.StandardSocketOptions;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.AsyncCompletionHandler;
import java.util.Iterator;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.io.IOException;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoClientException;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.nio.channels.Selector;
import java.io.Closeable;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.diagnostics.logging.Loggers;
import cc.dreamcode.antylogout.libs.com.mongodb.ServerAddress;
import cc.dreamcode.antylogout.libs.com.mongodb.assertions.Assertions;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.SslSettings;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.SocketSettings;
import cc.dreamcode.antylogout.libs.com.mongodb.spi.dns.InetAddressResolver;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.tlschannel.async.AsynchronousTlsChannelGroup;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.diagnostics.logging.Logger;

public class TlsChannelStreamFactoryFactory implements StreamFactoryFactory
{
    private static final Logger LOGGER;
    private final SelectorMonitor selectorMonitor;
    private final AsynchronousTlsChannelGroup group;
    private final PowerOfTwoBufferPool bufferPool;
    private final InetAddressResolver inetAddressResolver;
    
    public TlsChannelStreamFactoryFactory(final InetAddressResolver inetAddressResolver) {
        this.bufferPool = PowerOfTwoBufferPool.DEFAULT;
        this.inetAddressResolver = inetAddressResolver;
        this.group = new AsynchronousTlsChannelGroup();
        (this.selectorMonitor = new SelectorMonitor()).start();
    }
    
    @Override
    public StreamFactory create(final SocketSettings socketSettings, final SslSettings sslSettings) {
        Assertions.assertTrue(sslSettings.isEnabled());
        return serverAddress -> new TlsChannelStream(serverAddress, this.inetAddressResolver, socketSettings, sslSettings, this.bufferPool, this.group, this.selectorMonitor);
    }
    
    @Override
    public void close() {
        this.selectorMonitor.close();
        this.group.shutdown();
    }
    
    static {
        LOGGER = Loggers.getLogger("connection.tls");
    }
    
    private static class SelectorMonitor implements Closeable
    {
        private final Selector selector;
        private volatile boolean isClosed;
        private final ConcurrentLinkedDeque<Pair> pendingRegistrations;
        
        SelectorMonitor() {
            this.pendingRegistrations = (ConcurrentLinkedDeque<Pair>)new ConcurrentLinkedDeque();
            try {
                this.selector = Selector.open();
            }
            catch (final IOException e) {
                throw new MongoClientException("Exception opening Selector", (Throwable)e);
            }
        }
        
        void start() {
            final Thread selectorThread = new Thread(() -> {
                try {
                    while (!this.isClosed) {
                        try {
                            this.selector.select();
                            for (final SelectionKey selectionKey : this.selector.selectedKeys()) {
                                selectionKey.cancel();
                                final Runnable runnable = (Runnable)selectionKey.attachment();
                                runnable.run();
                            }
                            final Iterator<Pair> iter = (Iterator<Pair>)this.pendingRegistrations.iterator();
                            while (iter.hasNext()) {
                                final Pair pendingRegistration = (Pair)iter.next();
                                pendingRegistration.socketChannel.register(this.selector, 8, (Object)pendingRegistration.attachment);
                                iter.remove();
                            }
                        }
                        catch (final Exception e) {
                            TlsChannelStreamFactoryFactory.LOGGER.warn("Exception in selector loop", (Throwable)e);
                        }
                    }
                }
                finally {
                    try {
                        this.selector.close();
                    }
                    catch (final IOException ex) {}
                }
            });
            selectorThread.setDaemon(true);
            selectorThread.start();
        }
        
        void register(final SocketChannel channel, final Runnable attachment) {
            this.pendingRegistrations.add((Object)new Pair(channel, attachment));
            this.selector.wakeup();
        }
        
        public void close() {
            this.isClosed = true;
            this.selector.wakeup();
        }
        
        private static final class Pair
        {
            private final SocketChannel socketChannel;
            private final Runnable attachment;
            
            private Pair(final SocketChannel socketChannel, final Runnable attachment) {
                this.socketChannel = socketChannel;
                this.attachment = attachment;
            }
        }
    }
    
    private static class TlsChannelStream extends AsynchronousChannelStream
    {
        private final AsynchronousTlsChannelGroup group;
        private final SelectorMonitor selectorMonitor;
        private final InetAddressResolver inetAddressResolver;
        private final SslSettings sslSettings;
        
        TlsChannelStream(final ServerAddress serverAddress, final InetAddressResolver inetAddressResolver, final SocketSettings settings, final SslSettings sslSettings, final PowerOfTwoBufferPool bufferProvider, final AsynchronousTlsChannelGroup group, final SelectorMonitor selectorMonitor) {
            super(serverAddress, settings, bufferProvider);
            this.inetAddressResolver = inetAddressResolver;
            this.sslSettings = sslSettings;
            this.group = group;
            this.selectorMonitor = selectorMonitor;
        }
        
        @Override
        public void openAsync(final AsyncCompletionHandler<Void> handler) {
            Assertions.isTrue("unopened", this.getChannel() == null);
            try {
                final SocketChannel socketChannel = SocketChannel.open();
                socketChannel.configureBlocking(false);
                socketChannel.setOption(StandardSocketOptions.TCP_NODELAY, (Object)true);
                socketChannel.setOption(StandardSocketOptions.SO_KEEPALIVE, (Object)true);
                if (this.getSettings().getReceiveBufferSize() > 0) {
                    socketChannel.setOption(StandardSocketOptions.SO_RCVBUF, (Object)this.getSettings().getReceiveBufferSize());
                }
                if (this.getSettings().getSendBufferSize() > 0) {
                    socketChannel.setOption(StandardSocketOptions.SO_SNDBUF, (Object)this.getSettings().getSendBufferSize());
                }
                socketChannel.connect((SocketAddress)ServerAddressHelper.getSocketAddresses(this.getServerAddress(), this.inetAddressResolver).get(0));
                this.selectorMonitor.register(socketChannel, () -> {
                    try {
                        if (!socketChannel.finishConnect()) {
                            throw new MongoSocketOpenException("Failed to finish connect", this.getServerAddress());
                        }
                        final SSLEngine sslEngine = this.getSslContext().createSSLEngine(this.getServerAddress().getHost(), this.getServerAddress().getPort());
                        sslEngine.setUseClientMode(true);
                        final SSLParameters sslParameters = sslEngine.getSSLParameters();
                        SslHelper.enableSni(this.getServerAddress().getHost(), sslParameters);
                        if (!this.sslSettings.isInvalidHostNameAllowed()) {
                            SslHelper.enableHostNameVerification(sslParameters);
                        }
                        sslEngine.setSSLParameters(sslParameters);
                        final BufferAllocator bufferAllocator = new BufferProviderAllocator();
                        final TlsChannel tlsChannel = ClientTlsChannel.newBuilder((ByteChannel)socketChannel, sslEngine).withEncryptedBufferAllocator(bufferAllocator).withPlainBufferAllocator(bufferAllocator).build();
                        this.setChannel(new AsynchronousTlsChannelAdapter(new AsynchronousTlsChannel(this.group, tlsChannel, socketChannel)));
                        handler.completed(null);
                    }
                    catch (final IOException e) {
                        handler.failed((Throwable)new MongoSocketOpenException("Exception opening socket", this.getServerAddress(), (Throwable)e));
                    }
                    catch (final Throwable t) {
                        handler.failed(t);
                    }
                });
            }
            catch (final IOException e) {
                handler.failed((Throwable)new MongoSocketOpenException("Exception opening socket", this.getServerAddress(), (Throwable)e));
            }
            catch (final Throwable t) {
                handler.failed(t);
            }
        }
        
        private SSLContext getSslContext() {
            try {
                return (SSLContext)Optional.ofNullable((Object)this.sslSettings.getContext()).orElse((Object)SSLContext.getDefault());
            }
            catch (final NoSuchAlgorithmException e) {
                throw new MongoClientException("Unable to create default SSLContext", (Throwable)e);
            }
        }
        
        private class BufferProviderAllocator implements BufferAllocator
        {
            @Override
            public ByteBuffer allocate(final int size) {
                return TlsChannelStream.this.getBufferProvider().getByteBuffer(size);
            }
            
            @Override
            public void free(final ByteBuffer buffer) {
                TlsChannelStream.this.getBufferProvider().release(buffer);
            }
        }
        
        public static class AsynchronousTlsChannelAdapter implements ExtendedAsynchronousByteChannel
        {
            private final AsynchronousTlsChannel wrapped;
            
            AsynchronousTlsChannelAdapter(final AsynchronousTlsChannel wrapped) {
                this.wrapped = wrapped;
            }
            
            public <A> void read(final ByteBuffer dst, final A attach, final CompletionHandler<Integer, ? super A> handler) {
                this.wrapped.read(dst, attach, handler);
            }
            
            @Override
            public <A> void read(final ByteBuffer dst, final long timeout, final TimeUnit unit, @Nullable final A attach, final CompletionHandler<Integer, ? super A> handler) {
                this.wrapped.read(dst, timeout, unit, attach, handler);
            }
            
            @Override
            public <A> void read(final ByteBuffer[] dsts, final int offset, final int length, final long timeout, final TimeUnit unit, @Nullable final A attach, final CompletionHandler<Long, ? super A> handler) {
                this.wrapped.read(dsts, offset, length, timeout, unit, attach, handler);
            }
            
            public Future<Integer> read(final ByteBuffer dst) {
                return this.wrapped.read(dst);
            }
            
            public <A> void write(final ByteBuffer src, final A attach, final CompletionHandler<Integer, ? super A> handler) {
                this.wrapped.write(src, attach, handler);
            }
            
            @Override
            public <A> void write(final ByteBuffer src, final long timeout, final TimeUnit unit, final A attach, final CompletionHandler<Integer, ? super A> handler) {
                this.wrapped.write(src, timeout, unit, attach, handler);
            }
            
            @Override
            public <A> void write(final ByteBuffer[] srcs, final int offset, final int length, final long timeout, final TimeUnit unit, final A attach, final CompletionHandler<Long, ? super A> handler) {
                this.wrapped.write(srcs, offset, length, timeout, unit, attach, handler);
            }
            
            public Future<Integer> write(final ByteBuffer src) {
                return this.wrapped.write(src);
            }
            
            public boolean isOpen() {
                return this.wrapped.isOpen();
            }
            
            public void close() throws IOException {
                this.wrapped.close();
            }
        }
    }
}
