package cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.netty;

import cc.dreamcode.antylogout.libs.com.mongodb.annotations.ThreadSafe;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoSocketOpenException;
import io.netty.channel.ChannelFutureListener;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.thread.InterruptionUtil;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoInternalException;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoException;
import java.util.concurrent.CountDownLatch;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoSocketReadTimeoutException;
import io.netty.handler.timeout.ReadTimeoutException;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLEngine;
import io.netty.handler.ssl.SslHandler;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.SslHelper;
import java.security.NoSuchAlgorithmException;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoClientException;
import java.util.Optional;
import javax.net.ssl.SSLContext;
import java.util.function.Supplier;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.Locks;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.Iterator;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import cc.dreamcode.antylogout.libs.com.mongodb.assertions.Assertions;
import java.util.List;
import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import java.util.concurrent.TimeUnit;
import io.netty.channel.ChannelOption;
import io.netty.bootstrap.Bootstrap;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoSocketException;
import java.net.SocketAddress;
import java.util.Queue;
import java.util.Collection;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.ServerAddressHelper;
import java.io.IOException;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.AsyncCompletionHandler;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Lock;
import io.netty.buffer.ByteBuf;
import java.util.LinkedList;
import io.netty.channel.Channel;
import cc.dreamcode.antylogout.libs.com.mongodb.lang.Nullable;
import io.netty.handler.ssl.SslContext;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.EventLoopGroup;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.SslSettings;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.SocketSettings;
import cc.dreamcode.antylogout.libs.com.mongodb.spi.dns.InetAddressResolver;
import cc.dreamcode.antylogout.libs.com.mongodb.ServerAddress;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.Stream;

final class NettyStream implements Stream
{
    private static final byte NO_SCHEDULE_TIME = 0;
    private final ServerAddress address;
    private final InetAddressResolver inetAddressResolver;
    private final SocketSettings settings;
    private final SslSettings sslSettings;
    private final EventLoopGroup workerGroup;
    private final Class<? extends SocketChannel> socketChannelClass;
    private final ByteBufAllocator allocator;
    @Nullable
    private final SslContext sslContext;
    private boolean isClosed;
    private volatile Channel channel;
    private final LinkedList<ByteBuf> pendingInboundBuffers;
    private final Lock lock;
    private PendingReader pendingReader;
    private Throwable pendingException;
    @Nullable
    private ReadTimeoutTask readTimeoutTask;
    private long readTimeoutMillis;
    
    NettyStream(final ServerAddress address, final InetAddressResolver inetAddressResolver, final SocketSettings settings, final SslSettings sslSettings, final EventLoopGroup workerGroup, final Class<? extends SocketChannel> socketChannelClass, final ByteBufAllocator allocator, @Nullable final SslContext sslContext) {
        this.pendingInboundBuffers = (LinkedList<ByteBuf>)new LinkedList();
        this.lock = (Lock)new ReentrantLock();
        this.readTimeoutMillis = 0L;
        this.address = address;
        this.inetAddressResolver = inetAddressResolver;
        this.settings = settings;
        this.sslSettings = sslSettings;
        this.workerGroup = workerGroup;
        this.socketChannelClass = socketChannelClass;
        this.allocator = allocator;
        this.sslContext = sslContext;
    }
    
    @Override
    public cc.dreamcode.antylogout.libs.org.bson.ByteBuf getBuffer(final int size) {
        return new NettyByteBuf(this.allocator.buffer(size, size));
    }
    
    @Override
    public void open() throws IOException {
        final FutureAsyncCompletionHandler<Void> handler = new FutureAsyncCompletionHandler<Void>();
        this.openAsync(handler);
        handler.get();
    }
    
    @Override
    public void openAsync(final AsyncCompletionHandler<Void> handler) {
        Queue<SocketAddress> socketAddressQueue;
        try {
            socketAddressQueue = (Queue<SocketAddress>)new LinkedList((Collection)ServerAddressHelper.getSocketAddresses(this.address, this.inetAddressResolver));
        }
        catch (final Throwable t) {
            handler.failed(t);
            return;
        }
        this.initializeChannel(handler, socketAddressQueue);
    }
    
    private void initializeChannel(final AsyncCompletionHandler<Void> handler, final Queue<SocketAddress> socketAddressQueue) {
        if (socketAddressQueue.isEmpty()) {
            handler.failed((Throwable)new MongoSocketException("Exception opening socket", this.getAddress()));
        }
        else {
            final SocketAddress nextAddress = (SocketAddress)socketAddressQueue.poll();
            final Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(this.workerGroup);
            bootstrap.channel((Class)this.socketChannelClass);
            bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, (Object)this.settings.getConnectTimeout(TimeUnit.MILLISECONDS));
            bootstrap.option(ChannelOption.TCP_NODELAY, (Object)true);
            bootstrap.option(ChannelOption.SO_KEEPALIVE, (Object)true);
            if (this.settings.getReceiveBufferSize() > 0) {
                bootstrap.option(ChannelOption.SO_RCVBUF, (Object)this.settings.getReceiveBufferSize());
            }
            if (this.settings.getSendBufferSize() > 0) {
                bootstrap.option(ChannelOption.SO_SNDBUF, (Object)this.settings.getSendBufferSize());
            }
            bootstrap.option(ChannelOption.ALLOCATOR, (Object)this.allocator);
            bootstrap.handler((ChannelHandler)new ChannelInitializer<SocketChannel>() {
                public void initChannel(final SocketChannel ch) {
                    final ChannelPipeline pipeline = ch.pipeline();
                    if (NettyStream.this.sslSettings.isEnabled()) {
                        NettyStream.this.addSslHandler(ch);
                    }
                    final int readTimeout = NettyStream.this.settings.getReadTimeout(TimeUnit.MILLISECONDS);
                    if (readTimeout > 0) {
                        NettyStream.this.readTimeoutMillis = readTimeout;
                        pipeline.addLast(new ChannelHandler[] { (ChannelHandler)new ChannelInboundHandlerAdapter() });
                        NettyStream.this.readTimeoutTask = new ReadTimeoutTask(pipeline.lastContext());
                    }
                    pipeline.addLast(new ChannelHandler[] { (ChannelHandler)new InboundBufferHandler() });
                }
            });
            final ChannelFuture channelFuture = bootstrap.connect(nextAddress);
            channelFuture.addListener((GenericFutureListener)new OpenChannelFutureListener(socketAddressQueue, channelFuture, handler));
        }
    }
    
    @Override
    public void write(final List<cc.dreamcode.antylogout.libs.org.bson.ByteBuf> buffers) throws IOException {
        final FutureAsyncCompletionHandler<Void> future = new FutureAsyncCompletionHandler<Void>();
        this.writeAsync(buffers, future);
        future.get();
    }
    
    @Override
    public cc.dreamcode.antylogout.libs.org.bson.ByteBuf read(final int numBytes) throws IOException {
        return this.read(numBytes, 0);
    }
    
    @Override
    public cc.dreamcode.antylogout.libs.org.bson.ByteBuf read(final int numBytes, final int additionalTimeoutMillis) throws IOException {
        Assertions.isTrueArgument("additionalTimeoutMillis must not be negative", additionalTimeoutMillis >= 0);
        final FutureAsyncCompletionHandler<cc.dreamcode.antylogout.libs.org.bson.ByteBuf> future = new FutureAsyncCompletionHandler<cc.dreamcode.antylogout.libs.org.bson.ByteBuf>();
        this.readAsync(numBytes, future, combinedTimeout(this.readTimeoutMillis, additionalTimeoutMillis));
        return future.get();
    }
    
    @Override
    public void writeAsync(final List<cc.dreamcode.antylogout.libs.org.bson.ByteBuf> buffers, final AsyncCompletionHandler<Void> handler) {
        final CompositeByteBuf composite = PooledByteBufAllocator.DEFAULT.compositeBuffer();
        for (final cc.dreamcode.antylogout.libs.org.bson.ByteBuf cur : buffers) {
            composite.addComponent(true, ((NettyByteBuf)cur).asByteBuf().retain());
        }
        this.channel.writeAndFlush((Object)composite).addListener((GenericFutureListener)(future -> {
            if (!future.isSuccess()) {
                handler.failed(future.cause());
            }
            else {
                handler.completed(null);
            }
        }));
    }
    
    @Override
    public void readAsync(final int numBytes, final AsyncCompletionHandler<cc.dreamcode.antylogout.libs.org.bson.ByteBuf> handler) {
        this.readAsync(numBytes, handler, this.readTimeoutMillis);
    }
    
    private void readAsync(final int numBytes, final AsyncCompletionHandler<cc.dreamcode.antylogout.libs.org.bson.ByteBuf> handler, final long readTimeoutMillis) {
        cc.dreamcode.antylogout.libs.org.bson.ByteBuf buffer = null;
        Throwable exceptionResult = null;
        this.lock.lock();
        try {
            exceptionResult = this.pendingException;
            if (exceptionResult == null) {
                if (!this.hasBytesAvailable(numBytes)) {
                    if (this.pendingReader == null) {
                        this.pendingReader = new PendingReader(numBytes, (AsyncCompletionHandler)handler, (ScheduledFuture)scheduleReadTimeout(this.readTimeoutTask, readTimeoutMillis));
                    }
                }
                else {
                    final CompositeByteBuf composite = this.allocator.compositeBuffer(this.pendingInboundBuffers.size());
                    int bytesNeeded = numBytes;
                    final Iterator<ByteBuf> iter = (Iterator<ByteBuf>)this.pendingInboundBuffers.iterator();
                    while (iter.hasNext()) {
                        final ByteBuf next = (ByteBuf)iter.next();
                        final int bytesNeededFromCurrentBuffer = Math.min(next.readableBytes(), bytesNeeded);
                        if (bytesNeededFromCurrentBuffer == next.readableBytes()) {
                            composite.addComponent(next);
                            iter.remove();
                        }
                        else {
                            next.retain();
                            composite.addComponent(next.readSlice(bytesNeededFromCurrentBuffer));
                        }
                        composite.writerIndex(composite.writerIndex() + bytesNeededFromCurrentBuffer);
                        bytesNeeded -= bytesNeededFromCurrentBuffer;
                        if (bytesNeeded == 0) {
                            break;
                        }
                    }
                    buffer = new NettyByteBuf((ByteBuf)composite).flip();
                }
            }
            if ((exceptionResult != null || buffer != null) && this.pendingReader != null) {
                cancel((Future<?>)this.pendingReader.timeout);
                this.pendingReader = null;
            }
        }
        finally {
            this.lock.unlock();
        }
        if (exceptionResult != null) {
            handler.failed(exceptionResult);
        }
        if (buffer != null) {
            handler.completed(buffer);
        }
    }
    
    private boolean hasBytesAvailable(final int numBytes) {
        int bytesAvailable = 0;
        for (final ByteBuf cur : this.pendingInboundBuffers) {
            bytesAvailable += cur.readableBytes();
            if (bytesAvailable >= numBytes) {
                return true;
            }
        }
        return false;
    }
    
    private void handleReadResponse(@Nullable final ByteBuf buffer, @Nullable final Throwable t) {
        final PendingReader localPendingReader = Locks.withLock(this.lock, (java.util.function.Supplier<PendingReader>)(() -> {
            if (buffer != null) {
                this.pendingInboundBuffers.add((Object)buffer.retain());
            }
            else {
                this.pendingException = t;
            }
            return this.pendingReader;
        }));
        if (localPendingReader != null) {
            this.readAsync(localPendingReader.numBytes, localPendingReader.handler, 0L);
        }
    }
    
    @Override
    public ServerAddress getAddress() {
        return this.address;
    }
    
    @Override
    public void close() {
        Locks.withLock(this.lock, () -> {
            this.isClosed = true;
            if (this.channel != null) {
                this.channel.close();
                this.channel = null;
            }
            final Iterator<ByteBuf> iterator = (Iterator<ByteBuf>)this.pendingInboundBuffers.iterator();
            while (iterator.hasNext()) {
                final ByteBuf nextByteBuf = (ByteBuf)iterator.next();
                iterator.remove();
                nextByteBuf.release();
            }
        });
    }
    
    @Override
    public boolean isClosed() {
        return this.isClosed;
    }
    
    public SocketSettings getSettings() {
        return this.settings;
    }
    
    public SslSettings getSslSettings() {
        return this.sslSettings;
    }
    
    public EventLoopGroup getWorkerGroup() {
        return this.workerGroup;
    }
    
    public Class<? extends SocketChannel> getSocketChannelClass() {
        return this.socketChannelClass;
    }
    
    public ByteBufAllocator getAllocator() {
        return this.allocator;
    }
    
    private void addSslHandler(final SocketChannel channel) {
        SSLEngine engine;
        if (this.sslContext == null) {
            SSLContext sslContext;
            try {
                sslContext = (SSLContext)Optional.ofNullable((Object)this.sslSettings.getContext()).orElse((Object)SSLContext.getDefault());
            }
            catch (final NoSuchAlgorithmException e) {
                throw new MongoClientException("Unable to create standard SSLContext", (Throwable)e);
            }
            engine = sslContext.createSSLEngine(this.address.getHost(), this.address.getPort());
        }
        else {
            engine = this.sslContext.newEngine(channel.alloc(), this.address.getHost(), this.address.getPort());
        }
        engine.setUseClientMode(true);
        final SSLParameters sslParameters = engine.getSSLParameters();
        SslHelper.enableSni(this.address.getHost(), sslParameters);
        if (!this.sslSettings.isInvalidHostNameAllowed()) {
            SslHelper.enableHostNameVerification(sslParameters);
        }
        engine.setSSLParameters(sslParameters);
        channel.pipeline().addFirst("ssl", (ChannelHandler)new SslHandler(engine, false));
    }
    
    private static void cancel(@Nullable final Future<?> f) {
        if (f != null) {
            f.cancel(false);
        }
    }
    
    private static long combinedTimeout(final long timeout, final int additionalTimeout) {
        if (timeout == 0L) {
            return 0L;
        }
        return Math.addExact(timeout, (long)additionalTimeout);
    }
    
    @Nullable
    private static ScheduledFuture<?> scheduleReadTimeout(@Nullable final ReadTimeoutTask readTimeoutTask, final long timeoutMillis) {
        if (timeoutMillis == 0L) {
            return null;
        }
        return Assertions.assertNotNull(readTimeoutTask).schedule(timeoutMillis);
    }
    
    private class InboundBufferHandler extends SimpleChannelInboundHandler<ByteBuf>
    {
        protected void channelRead0(final ChannelHandlerContext ctx, final ByteBuf buffer) {
            NettyStream.this.handleReadResponse(buffer, null);
        }
        
        public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable t) {
            if (t instanceof ReadTimeoutException) {
                NettyStream.this.handleReadResponse(null, (Throwable)new MongoSocketReadTimeoutException("Timeout while receiving message", NettyStream.this.address, t));
            }
            else {
                NettyStream.this.handleReadResponse(null, t);
            }
            ctx.close();
        }
    }
    
    private static final class PendingReader
    {
        private final int numBytes;
        private final AsyncCompletionHandler<cc.dreamcode.antylogout.libs.org.bson.ByteBuf> handler;
        @Nullable
        private final ScheduledFuture<?> timeout;
        
        private PendingReader(final int numBytes, final AsyncCompletionHandler<cc.dreamcode.antylogout.libs.org.bson.ByteBuf> handler, @Nullable final ScheduledFuture<?> timeout) {
            this.numBytes = numBytes;
            this.handler = handler;
            this.timeout = timeout;
        }
    }
    
    private static final class FutureAsyncCompletionHandler<T> implements AsyncCompletionHandler<T>
    {
        private final CountDownLatch latch;
        private volatile T t;
        private volatile Throwable throwable;
        
        FutureAsyncCompletionHandler() {
            this.latch = new CountDownLatch(1);
        }
        
        @Override
        public void completed(@Nullable final T t) {
            this.t = t;
            this.latch.countDown();
        }
        
        @Override
        public void failed(final Throwable t) {
            this.throwable = t;
            this.latch.countDown();
        }
        
        public T get() throws IOException {
            try {
                this.latch.await();
                if (this.throwable == null) {
                    return this.t;
                }
                if (this.throwable instanceof IOException) {
                    throw (IOException)this.throwable;
                }
                if (this.throwable instanceof MongoException) {
                    throw (MongoException)this.throwable;
                }
                throw new MongoInternalException("Exception thrown from Netty Stream", this.throwable);
            }
            catch (final InterruptedException e) {
                throw InterruptionUtil.interruptAndCreateMongoInterruptedException("Interrupted", e);
            }
        }
    }
    
    private class OpenChannelFutureListener implements ChannelFutureListener
    {
        private final Queue<SocketAddress> socketAddressQueue;
        private final ChannelFuture channelFuture;
        private final AsyncCompletionHandler<Void> handler;
        
        OpenChannelFutureListener(final Queue<SocketAddress> socketAddressQueue, final ChannelFuture channelFuture, final AsyncCompletionHandler<Void> handler) {
            this.socketAddressQueue = socketAddressQueue;
            this.channelFuture = channelFuture;
            this.handler = handler;
        }
        
        public void operationComplete(final ChannelFuture future) {
            Locks.withLock(NettyStream.this.lock, () -> {
                if (future.isSuccess()) {
                    if (NettyStream.this.isClosed) {
                        this.channelFuture.channel().close();
                    }
                    else {
                        NettyStream.this.channel = this.channelFuture.channel();
                        NettyStream.this.channel.closeFuture().addListener((GenericFutureListener)(future1 -> NettyStream.this.handleReadResponse(null, (Throwable)new IOException("The connection to the server was closed"))));
                    }
                    this.handler.completed(null);
                }
                else if (NettyStream.this.isClosed) {
                    this.handler.completed(null);
                }
                else if (this.socketAddressQueue.isEmpty()) {
                    this.handler.failed((Throwable)new MongoSocketOpenException("Exception opening socket", NettyStream.this.getAddress(), future.cause()));
                }
                else {
                    NettyStream.this.initializeChannel(this.handler, this.socketAddressQueue);
                }
            });
        }
    }
    
    @ThreadSafe
    private static final class ReadTimeoutTask implements Runnable
    {
        private final ChannelHandlerContext ctx;
        
        private ReadTimeoutTask(final ChannelHandlerContext timeoutChannelHandlerContext) {
            this.ctx = timeoutChannelHandlerContext;
        }
        
        public void run() {
            try {
                if (this.ctx.channel().isOpen()) {
                    this.ctx.fireExceptionCaught((Throwable)ReadTimeoutException.INSTANCE);
                    this.ctx.close();
                }
            }
            catch (final Throwable t) {
                this.ctx.fireExceptionCaught(t);
            }
        }
        
        private ScheduledFuture<?> schedule(final long timeoutMillis) {
            return (ScheduledFuture<?>)this.ctx.executor().schedule((Runnable)this, timeoutMillis, TimeUnit.MILLISECONDS);
        }
    }
}
