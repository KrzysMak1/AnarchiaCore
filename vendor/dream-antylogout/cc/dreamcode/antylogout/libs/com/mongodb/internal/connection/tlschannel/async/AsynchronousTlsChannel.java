package cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.tlschannel.async;

import java.io.IOException;
import java.util.function.LongConsumer;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.concurrent.TimeUnit;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.tlschannel.impl.ByteBufferSet;
import java.nio.channels.CompletionHandler;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SocketChannel;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.tlschannel.TlsChannel;

public class AsynchronousTlsChannel implements ExtendedAsynchronousByteChannel
{
    private final AsynchronousTlsChannelGroup group;
    private final TlsChannel tlsChannel;
    private final AsynchronousTlsChannelGroup.RegisteredSocket registeredSocket;
    
    public AsynchronousTlsChannel(final AsynchronousTlsChannelGroup channelGroup, final TlsChannel tlsChannel, final SocketChannel socketChannel) throws ClosedChannelException, IllegalArgumentException {
        if (!tlsChannel.isOpen() || !socketChannel.isOpen()) {
            throw new ClosedChannelException();
        }
        if (socketChannel.isBlocking()) {
            throw new IllegalArgumentException("socket channel must be in non-blocking mode");
        }
        this.group = channelGroup;
        this.tlsChannel = tlsChannel;
        this.registeredSocket = channelGroup.registerSocket(tlsChannel, socketChannel);
    }
    
    public <A> void read(final ByteBuffer dst, final A attach, final CompletionHandler<Integer, ? super A> handler) {
        this.checkReadOnly(dst);
        if (!dst.hasRemaining()) {
            this.completeWithZeroInt(attach, handler);
            return;
        }
        this.group.startRead(this.registeredSocket, new ByteBufferSet(dst), 0L, TimeUnit.MILLISECONDS, c -> this.group.executor.submit(() -> handler.completed((Object)(int)c, attach)), (Consumer<Throwable>)(e -> this.group.executor.submit(() -> handler.failed(e, attach))));
    }
    
    @Override
    public <A> void read(final ByteBuffer dst, final long timeout, final TimeUnit unit, final A attach, final CompletionHandler<Integer, ? super A> handler) {
        this.checkReadOnly(dst);
        if (!dst.hasRemaining()) {
            this.completeWithZeroInt(attach, handler);
            return;
        }
        this.group.startRead(this.registeredSocket, new ByteBufferSet(dst), timeout, unit, c -> this.group.executor.submit(() -> handler.completed((Object)(int)c, attach)), (Consumer<Throwable>)(e -> this.group.executor.submit(() -> handler.failed(e, attach))));
    }
    
    @Override
    public <A> void read(final ByteBuffer[] dsts, final int offset, final int length, final long timeout, final TimeUnit unit, final A attach, final CompletionHandler<Long, ? super A> handler) {
        final ByteBufferSet bufferSet = new ByteBufferSet(dsts, offset, length);
        if (bufferSet.isReadOnly()) {
            throw new IllegalArgumentException("buffer is read-only");
        }
        if (!bufferSet.hasRemaining()) {
            this.completeWithZeroLong(attach, handler);
            return;
        }
        this.group.startRead(this.registeredSocket, bufferSet, timeout, unit, c -> this.group.executor.submit(() -> handler.completed((Object)c, attach)), (Consumer<Throwable>)(e -> this.group.executor.submit(() -> handler.failed(e, attach))));
    }
    
    public Future<Integer> read(final ByteBuffer dst) {
        this.checkReadOnly(dst);
        if (!dst.hasRemaining()) {
            return (Future<Integer>)CompletableFuture.completedFuture((Object)0);
        }
        final FutureReadResult future = new FutureReadResult();
        final AsynchronousTlsChannelGroup group = this.group;
        final AsynchronousTlsChannelGroup.RegisteredSocket registeredSocket = this.registeredSocket;
        final ByteBufferSet buffer = new ByteBufferSet(dst);
        final long timeout = 0L;
        final TimeUnit milliseconds = TimeUnit.MILLISECONDS;
        final LongConsumer onSuccess = c -> future.complete((Object)(int)c);
        final FutureReadResult futureReadResult = future;
        Objects.requireNonNull((Object)futureReadResult);
        final AsynchronousTlsChannelGroup.ReadOperation op = group.startRead(registeredSocket, buffer, timeout, milliseconds, onSuccess, (Consumer<Throwable>)futureReadResult::completeExceptionally);
        future.op = op;
        return (Future<Integer>)future;
    }
    
    private void checkReadOnly(final ByteBuffer dst) {
        if (dst.isReadOnly()) {
            throw new IllegalArgumentException("buffer is read-only");
        }
    }
    
    public <A> void write(final ByteBuffer src, final A attach, final CompletionHandler<Integer, ? super A> handler) {
        if (!src.hasRemaining()) {
            this.completeWithZeroInt(attach, handler);
            return;
        }
        this.group.startWrite(this.registeredSocket, new ByteBufferSet(src), 0L, TimeUnit.MILLISECONDS, c -> this.group.executor.submit(() -> handler.completed((Object)(int)c, attach)), (Consumer<Throwable>)(e -> this.group.executor.submit(() -> handler.failed(e, attach))));
    }
    
    @Override
    public <A> void write(final ByteBuffer src, final long timeout, final TimeUnit unit, final A attach, final CompletionHandler<Integer, ? super A> handler) {
        if (!src.hasRemaining()) {
            this.completeWithZeroInt(attach, handler);
            return;
        }
        this.group.startWrite(this.registeredSocket, new ByteBufferSet(src), timeout, unit, c -> this.group.executor.submit(() -> handler.completed((Object)(int)c, attach)), (Consumer<Throwable>)(e -> this.group.executor.submit(() -> handler.failed(e, attach))));
    }
    
    @Override
    public <A> void write(final ByteBuffer[] srcs, final int offset, final int length, final long timeout, final TimeUnit unit, final A attach, final CompletionHandler<Long, ? super A> handler) {
        final ByteBufferSet bufferSet = new ByteBufferSet(srcs, offset, length);
        if (!bufferSet.hasRemaining()) {
            this.completeWithZeroLong(attach, handler);
            return;
        }
        this.group.startWrite(this.registeredSocket, bufferSet, timeout, unit, c -> this.group.executor.submit(() -> handler.completed((Object)c, attach)), (Consumer<Throwable>)(e -> this.group.executor.submit(() -> handler.failed(e, attach))));
    }
    
    public Future<Integer> write(final ByteBuffer src) {
        if (!src.hasRemaining()) {
            return (Future<Integer>)CompletableFuture.completedFuture((Object)0);
        }
        final FutureWriteResult future = new FutureWriteResult();
        final AsynchronousTlsChannelGroup group = this.group;
        final AsynchronousTlsChannelGroup.RegisteredSocket registeredSocket = this.registeredSocket;
        final ByteBufferSet buffer = new ByteBufferSet(src);
        final long timeout = 0L;
        final TimeUnit milliseconds = TimeUnit.MILLISECONDS;
        final LongConsumer onSuccess = c -> future.complete((Object)(int)c);
        final FutureWriteResult futureWriteResult = future;
        Objects.requireNonNull((Object)futureWriteResult);
        final AsynchronousTlsChannelGroup.WriteOperation op = group.startWrite(registeredSocket, buffer, timeout, milliseconds, onSuccess, (Consumer<Throwable>)futureWriteResult::completeExceptionally);
        future.op = op;
        return (Future<Integer>)future;
    }
    
    private <A> void completeWithZeroInt(final A attach, final CompletionHandler<Integer, ? super A> handler) {
        this.group.executor.submit(() -> handler.completed((Object)0, attach));
    }
    
    private <A> void completeWithZeroLong(final A attach, final CompletionHandler<Long, ? super A> handler) {
        this.group.executor.submit(() -> handler.completed((Object)0L, attach));
    }
    
    public boolean isOpen() {
        return this.tlsChannel.isOpen();
    }
    
    public void close() throws IOException {
        this.tlsChannel.close();
        this.registeredSocket.close();
    }
    
    private class FutureReadResult extends CompletableFuture<Integer>
    {
        AsynchronousTlsChannelGroup.ReadOperation op;
        
        public boolean cancel(final boolean mayInterruptIfRunning) {
            super.cancel(mayInterruptIfRunning);
            return AsynchronousTlsChannel.this.group.doCancelRead(AsynchronousTlsChannel.this.registeredSocket, this.op);
        }
    }
    
    private class FutureWriteResult extends CompletableFuture<Integer>
    {
        AsynchronousTlsChannelGroup.WriteOperation op;
        
        public boolean cancel(final boolean mayInterruptIfRunning) {
            super.cancel(mayInterruptIfRunning);
            return AsynchronousTlsChannel.this.group.doCancelWrite(AsynchronousTlsChannel.this.registeredSocket, this.op);
        }
    }
}
