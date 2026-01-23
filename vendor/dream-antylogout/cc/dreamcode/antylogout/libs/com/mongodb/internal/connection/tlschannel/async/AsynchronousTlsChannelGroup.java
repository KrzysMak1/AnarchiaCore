package cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.tlschannel.async;

import java.util.concurrent.Future;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.CountDownLatch;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.diagnostics.logging.Loggers;
import java.nio.channels.InterruptedByTimeoutException;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.tlschannel.NeedsTaskException;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.tlschannel.NeedsWriteException;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.tlschannel.NeedsReadException;
import java.util.Iterator;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.SelectionKey;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.thread.InterruptionUtil;
import java.nio.channels.WritePendingException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.ReadPendingException;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.tlschannel.util.Util;
import java.util.function.Consumer;
import java.util.function.LongConsumer;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.tlschannel.impl.ByteBufferSet;
import java.nio.channels.ShutdownChannelGroupException;
import java.nio.channels.SocketChannel;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.tlschannel.TlsChannel;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ExecutorService;
import java.nio.channels.Selector;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.diagnostics.logging.Logger;

public class AsynchronousTlsChannelGroup
{
    private static final Logger LOGGER;
    private static final int queueLengthMultiplier = 32;
    private static final AtomicInteger globalGroupCount;
    private final int id;
    private final AtomicBoolean loggedTaskWarning;
    private final Selector selector;
    final ExecutorService executor;
    private final ScheduledThreadPoolExecutor timeoutExecutor;
    private final Thread selectorThread;
    private final ConcurrentLinkedQueue<RegisteredSocket> pendingRegistrations;
    private volatile Shutdown shutdown;
    private final LongAdder selectionCount;
    private final LongAdder startedReads;
    private final LongAdder startedWrites;
    private final LongAdder successfulReads;
    private final LongAdder successfulWrites;
    private final LongAdder failedReads;
    private final LongAdder failedWrites;
    private final LongAdder cancelledReads;
    private final LongAdder cancelledWrites;
    private final ConcurrentHashMap<RegisteredSocket, Boolean> registrations;
    private final LongAdder currentReads;
    private final LongAdder currentWrites;
    
    public AsynchronousTlsChannelGroup(final int nThreads) {
        this.id = AsynchronousTlsChannelGroup.globalGroupCount.getAndIncrement();
        this.loggedTaskWarning = new AtomicBoolean();
        this.timeoutExecutor = new ScheduledThreadPoolExecutor(1, runnable -> new Thread(runnable, String.format("async-channel-group-%d-timeout-thread", new Object[] { this.id })));
        this.selectorThread = new Thread(this::loop, String.format("async-channel-group-%d-selector", new Object[] { this.id }));
        this.pendingRegistrations = (ConcurrentLinkedQueue<RegisteredSocket>)new ConcurrentLinkedQueue();
        this.shutdown = Shutdown.No;
        this.selectionCount = new LongAdder();
        this.startedReads = new LongAdder();
        this.startedWrites = new LongAdder();
        this.successfulReads = new LongAdder();
        this.successfulWrites = new LongAdder();
        this.failedReads = new LongAdder();
        this.failedWrites = new LongAdder();
        this.cancelledReads = new LongAdder();
        this.cancelledWrites = new LongAdder();
        this.registrations = (ConcurrentHashMap<RegisteredSocket, Boolean>)new ConcurrentHashMap();
        this.currentReads = new LongAdder();
        this.currentWrites = new LongAdder();
        try {
            this.selector = Selector.open();
        }
        catch (final IOException e) {
            throw new RuntimeException((Throwable)e);
        }
        this.timeoutExecutor.setRemoveOnCancelPolicy(true);
        this.executor = (ExecutorService)new ThreadPoolExecutor(nThreads, nThreads, 0L, TimeUnit.MILLISECONDS, (BlockingQueue)new LinkedBlockingQueue(nThreads * 32), runnable -> new Thread(runnable, String.format("async-channel-group-%d-handler-executor", new Object[] { this.id })), (RejectedExecutionHandler)new ThreadPoolExecutor.CallerRunsPolicy());
        this.selectorThread.start();
    }
    
    public AsynchronousTlsChannelGroup() {
        this(Runtime.getRuntime().availableProcessors());
    }
    
    RegisteredSocket registerSocket(final TlsChannel reader, final SocketChannel socketChannel) {
        if (this.shutdown != Shutdown.No) {
            throw new ShutdownChannelGroupException();
        }
        final RegisteredSocket socket = new RegisteredSocket(reader, socketChannel);
        this.pendingRegistrations.add((Object)socket);
        this.selector.wakeup();
        return socket;
    }
    
    boolean doCancelRead(final RegisteredSocket socket, final ReadOperation op) {
        socket.readLock.lock();
        try {
            if (op != socket.readOperation) {
                return false;
            }
            socket.readOperation = null;
            this.cancelledReads.increment();
            this.currentReads.decrement();
            return true;
        }
        finally {
            socket.readLock.unlock();
        }
    }
    
    boolean doCancelWrite(final RegisteredSocket socket, final WriteOperation op) {
        socket.writeLock.lock();
        try {
            if (op != socket.writeOperation) {
                return false;
            }
            socket.writeOperation = null;
            this.cancelledWrites.increment();
            this.currentWrites.decrement();
            return true;
        }
        finally {
            socket.writeLock.unlock();
        }
    }
    
    ReadOperation startRead(final RegisteredSocket socket, final ByteBufferSet buffer, final long timeout, final TimeUnit unit, final LongConsumer onSuccess, final Consumer<Throwable> onFailure) throws ReadPendingException {
        this.checkTerminated();
        Util.assertTrue(buffer.hasRemaining());
        this.waitForSocketRegistration(socket);
        socket.readLock.lock();
        try {
            if (socket.readOperation != null) {
                throw new ReadPendingException();
            }
            final ReadOperation op = new ReadOperation(buffer, onSuccess, onFailure);
            this.startedReads.increment();
            this.currentReads.increment();
            if (!this.registrations.containsKey((Object)socket)) {
                op.onFailure.accept((Object)new ClosedChannelException());
                this.failedReads.increment();
                this.currentReads.decrement();
                return op;
            }
            socket.pendingOps.set(5);
            if (timeout != 0L) {
                op.timeoutFuture = (Future<?>)this.timeoutExecutor.schedule(() -> {
                    final boolean success = this.doCancelRead(socket, op);
                    if (success) {
                        op.onFailure.accept((Object)new InterruptedByTimeoutException());
                    }
                }, timeout, unit);
            }
            socket.readOperation = op;
        }
        finally {
            socket.readLock.unlock();
        }
        this.selector.wakeup();
        return socket.readOperation;
    }
    
    WriteOperation startWrite(final RegisteredSocket socket, final ByteBufferSet buffer, final long timeout, final TimeUnit unit, final LongConsumer onSuccess, final Consumer<Throwable> onFailure) throws WritePendingException {
        this.checkTerminated();
        Util.assertTrue(buffer.hasRemaining());
        this.waitForSocketRegistration(socket);
        socket.writeLock.lock();
        try {
            if (socket.writeOperation != null) {
                throw new WritePendingException();
            }
            final WriteOperation op = new WriteOperation(buffer, onSuccess, onFailure);
            this.startedWrites.increment();
            this.currentWrites.increment();
            if (!this.registrations.containsKey((Object)socket)) {
                op.onFailure.accept((Object)new ClosedChannelException());
                this.failedWrites.increment();
                this.currentWrites.decrement();
                return op;
            }
            socket.pendingOps.set(5);
            if (timeout != 0L) {
                op.timeoutFuture = (Future<?>)this.timeoutExecutor.schedule(() -> {
                    final boolean success = this.doCancelWrite(socket, op);
                    if (success) {
                        op.onFailure.accept((Object)new InterruptedByTimeoutException());
                    }
                }, timeout, unit);
            }
            socket.writeOperation = op;
        }
        finally {
            socket.writeLock.unlock();
        }
        this.selector.wakeup();
        return socket.writeOperation;
    }
    
    private void checkTerminated() {
        if (this.isTerminated()) {
            throw new ShutdownChannelGroupException();
        }
    }
    
    private void waitForSocketRegistration(final RegisteredSocket socket) {
        try {
            socket.registered.await();
        }
        catch (final InterruptedException e) {
            throw InterruptionUtil.interruptAndCreateMongoInterruptedException(null, e);
        }
    }
    
    private void loop() {
        try {
            while (this.shutdown == Shutdown.No || (this.shutdown == Shutdown.Wait && (!this.pendingRegistrations.isEmpty() || !this.registrations.isEmpty()))) {
                final int c = this.selector.select(100L);
                this.selectionCount.increment();
                if (c > 0) {
                    final Iterator<SelectionKey> it = (Iterator<SelectionKey>)this.selector.selectedKeys().iterator();
                    while (it.hasNext()) {
                        final SelectionKey key = (SelectionKey)it.next();
                        it.remove();
                        try {
                            key.interestOps(0);
                        }
                        catch (final CancelledKeyException e) {
                            continue;
                        }
                        final RegisteredSocket socket = (RegisteredSocket)key.attachment();
                        this.processRead(socket);
                        this.processWrite(socket);
                    }
                }
                this.registerPendingSockets();
                this.processPendingInterests();
                this.checkClosings();
            }
        }
        catch (final Throwable e2) {
            AsynchronousTlsChannelGroup.LOGGER.error("error in selector loop", e2);
        }
        finally {
            this.executor.shutdown();
            this.timeoutExecutor.shutdownNow();
            try {
                this.selector.close();
            }
            catch (final IOException e3) {
                AsynchronousTlsChannelGroup.LOGGER.warn("error closing selector: " + e3.getMessage());
            }
            this.checkClosings();
        }
    }
    
    private void processPendingInterests() {
        for (final SelectionKey key : this.selector.keys()) {
            final RegisteredSocket socket = (RegisteredSocket)key.attachment();
            final int pending = socket.pendingOps.getAndSet(0);
            if (pending != 0) {
                try {
                    key.interestOps(key.interestOps() | pending);
                }
                catch (final CancelledKeyException ex) {}
            }
        }
    }
    
    private void processWrite(final RegisteredSocket socket) {
        socket.writeLock.lock();
        try {
            final WriteOperation op = socket.writeOperation;
            if (op != null) {
                this.executor.execute(() -> {
                    try {
                        this.doWrite(socket, op);
                    }
                    catch (final Throwable e) {
                        AsynchronousTlsChannelGroup.LOGGER.error("error in operation", e);
                    }
                });
            }
        }
        finally {
            socket.writeLock.unlock();
        }
    }
    
    private void processRead(final RegisteredSocket socket) {
        socket.readLock.lock();
        try {
            final ReadOperation op = socket.readOperation;
            if (op != null) {
                this.executor.execute(() -> {
                    try {
                        this.doRead(socket, op);
                    }
                    catch (final Throwable e) {
                        AsynchronousTlsChannelGroup.LOGGER.error("error in operation", e);
                    }
                });
            }
        }
        finally {
            socket.readLock.unlock();
        }
    }
    
    private void doWrite(final RegisteredSocket socket, final WriteOperation op) {
        socket.writeLock.lock();
        try {
            if (socket.writeOperation != op) {
                return;
            }
            try {
                final long before = op.bufferSet.remaining();
                try {
                    this.writeHandlingTasks(socket, op);
                }
                finally {
                    final long c = before - op.bufferSet.remaining();
                    Util.assertTrue(c >= 0L);
                    op.consumesBytes += c;
                }
                socket.writeOperation = null;
                if (op.timeoutFuture != null) {
                    op.timeoutFuture.cancel(false);
                }
                op.onSuccess.accept(op.consumesBytes);
                this.successfulWrites.increment();
                this.currentWrites.decrement();
            }
            catch (final NeedsReadException e) {
                socket.pendingOps.accumulateAndGet(1, (a, b) -> a | b);
                this.selector.wakeup();
            }
            catch (final NeedsWriteException e2) {
                socket.pendingOps.accumulateAndGet(4, (a, b) -> a | b);
                this.selector.wakeup();
            }
            catch (final IOException e3) {
                if (socket.writeOperation == op) {
                    socket.writeOperation = null;
                }
                if (op.timeoutFuture != null) {
                    op.timeoutFuture.cancel(false);
                }
                op.onFailure.accept((Object)e3);
                this.failedWrites.increment();
                this.currentWrites.decrement();
            }
        }
        finally {
            socket.writeLock.unlock();
        }
    }
    
    private void writeHandlingTasks(final RegisteredSocket socket, final WriteOperation op) throws IOException {
        while (true) {
            try {
                socket.tlsChannel.write(op.bufferSet.array, op.bufferSet.offset, op.bufferSet.length);
            }
            catch (final NeedsTaskException e) {
                this.warnAboutNeedTask();
                e.getTask().run();
                continue;
            }
            break;
        }
    }
    
    private void warnAboutNeedTask() {
        if (!this.loggedTaskWarning.getAndSet(true)) {
            AsynchronousTlsChannelGroup.LOGGER.warn(String.format("caught %s; channels used in asynchronous groups should run tasks themselves; although task is being dealt with anyway, consider configuring channels properly", new Object[] { NeedsTaskException.class.getName() }));
        }
    }
    
    private void doRead(final RegisteredSocket socket, final ReadOperation op) {
        socket.readLock.lock();
        try {
            if (socket.readOperation != op) {
                return;
            }
            try {
                Util.assertTrue(op.bufferSet.hasRemaining());
                final long c = this.readHandlingTasks(socket, op);
                Util.assertTrue(c > 0L || c == -1L);
                socket.readOperation = null;
                if (op.timeoutFuture != null) {
                    op.timeoutFuture.cancel(false);
                }
                op.onSuccess.accept(c);
                this.successfulReads.increment();
                this.currentReads.decrement();
            }
            catch (final NeedsReadException e) {
                socket.pendingOps.accumulateAndGet(1, (a, b) -> a | b);
                this.selector.wakeup();
            }
            catch (final NeedsWriteException e2) {
                socket.pendingOps.accumulateAndGet(4, (a, b) -> a | b);
                this.selector.wakeup();
            }
            catch (final IOException e3) {
                if (socket.readOperation == op) {
                    socket.readOperation = null;
                }
                if (op.timeoutFuture != null) {
                    op.timeoutFuture.cancel(false);
                }
                op.onFailure.accept((Object)e3);
                this.failedReads.increment();
                this.currentReads.decrement();
            }
        }
        finally {
            socket.readLock.unlock();
        }
    }
    
    private long readHandlingTasks(final RegisteredSocket socket, final ReadOperation op) throws IOException {
        try {
            return socket.tlsChannel.read(op.bufferSet.array, op.bufferSet.offset, op.bufferSet.length);
        }
        catch (final NeedsTaskException e) {
            this.warnAboutNeedTask();
            e.getTask().run();
            return socket.tlsChannel.read(op.bufferSet.array, op.bufferSet.offset, op.bufferSet.length);
        }
    }
    
    private void registerPendingSockets() {
        RegisteredSocket socket;
        while ((socket = (RegisteredSocket)this.pendingRegistrations.poll()) != null) {
            try {
                socket.key = socket.socketChannel.register(this.selector, 0, (Object)socket);
                this.registrations.put((Object)socket, (Object)true);
            }
            catch (final ClosedChannelException ex) {}
            finally {
                socket.registered.countDown();
            }
        }
    }
    
    private void checkClosings() {
        for (final RegisteredSocket socket : this.registrations.keySet()) {
            if (!socket.key.isValid() || this.shutdown == Shutdown.Immediate) {
                this.registrations.remove((Object)socket);
                this.failCurrentRead(socket);
                this.failCurrentWrite(socket);
            }
        }
    }
    
    private void failCurrentRead(final RegisteredSocket socket) {
        socket.readLock.lock();
        try {
            if (socket.readOperation != null) {
                socket.readOperation.onFailure.accept((Object)new ClosedChannelException());
                if (socket.readOperation.timeoutFuture != null) {
                    socket.readOperation.timeoutFuture.cancel(false);
                }
                socket.readOperation = null;
                this.failedReads.increment();
                this.currentReads.decrement();
            }
        }
        finally {
            socket.readLock.unlock();
        }
    }
    
    private void failCurrentWrite(final RegisteredSocket socket) {
        socket.writeLock.lock();
        try {
            if (socket.writeOperation != null) {
                socket.writeOperation.onFailure.accept((Object)new ClosedChannelException());
                if (socket.writeOperation.timeoutFuture != null) {
                    socket.writeOperation.timeoutFuture.cancel(false);
                }
                socket.writeOperation = null;
                this.failedWrites.increment();
                this.currentWrites.decrement();
            }
        }
        finally {
            socket.writeLock.unlock();
        }
    }
    
    public boolean isShutdown() {
        return this.shutdown != Shutdown.No;
    }
    
    public void shutdown() {
        this.shutdown = Shutdown.Wait;
        this.selector.wakeup();
    }
    
    public void shutdownNow() {
        this.shutdown = Shutdown.Immediate;
        this.selector.wakeup();
    }
    
    public boolean isTerminated() {
        return this.executor.isTerminated();
    }
    
    public boolean awaitTermination(final long timeout, final TimeUnit unit) throws InterruptedException {
        return this.executor.awaitTermination(timeout, unit);
    }
    
    long getSelectionCount() {
        return this.selectionCount.longValue();
    }
    
    public long getStartedReadCount() {
        return this.startedReads.longValue();
    }
    
    public long getStartedWriteCount() {
        return this.startedWrites.longValue();
    }
    
    public long getSuccessfulReadCount() {
        return this.successfulReads.longValue();
    }
    
    public long getSuccessfulWriteCount() {
        return this.successfulWrites.longValue();
    }
    
    public long getFailedReadCount() {
        return this.failedReads.longValue();
    }
    
    public long getFailedWriteCount() {
        return this.failedWrites.longValue();
    }
    
    public long getCancelledReadCount() {
        return this.cancelledReads.longValue();
    }
    
    public long getCancelledWriteCount() {
        return this.cancelledWrites.longValue();
    }
    
    public long getCurrentReadCount() {
        return this.currentReads.longValue();
    }
    
    public long getCurrentWriteCount() {
        return this.currentWrites.longValue();
    }
    
    public long getCurrentRegistrationCount() {
        return this.registrations.mappingCount();
    }
    
    static {
        LOGGER = Loggers.getLogger("connection.tls");
        globalGroupCount = new AtomicInteger();
    }
    
    class RegisteredSocket
    {
        final TlsChannel tlsChannel;
        final SocketChannel socketChannel;
        final CountDownLatch registered;
        SelectionKey key;
        final Lock readLock;
        final Lock writeLock;
        ReadOperation readOperation;
        WriteOperation writeOperation;
        final AtomicInteger pendingOps;
        
        RegisteredSocket(final TlsChannel tlsChannel, final SocketChannel socketChannel) {
            this.registered = new CountDownLatch(1);
            this.readLock = (Lock)new ReentrantLock();
            this.writeLock = (Lock)new ReentrantLock();
            this.pendingOps = new AtomicInteger();
            this.tlsChannel = tlsChannel;
            this.socketChannel = socketChannel;
        }
        
        public void close() {
            if (this.key != null) {
                this.key.cancel();
            }
            AsynchronousTlsChannelGroup.this.selector.wakeup();
        }
    }
    
    private abstract static class Operation
    {
        final ByteBufferSet bufferSet;
        final LongConsumer onSuccess;
        final Consumer<Throwable> onFailure;
        Future<?> timeoutFuture;
        
        Operation(final ByteBufferSet bufferSet, final LongConsumer onSuccess, final Consumer<Throwable> onFailure) {
            this.bufferSet = bufferSet;
            this.onSuccess = onSuccess;
            this.onFailure = onFailure;
        }
    }
    
    static final class ReadOperation extends Operation
    {
        ReadOperation(final ByteBufferSet bufferSet, final LongConsumer onSuccess, final Consumer<Throwable> onFailure) {
            super(bufferSet, onSuccess, onFailure);
        }
    }
    
    static final class WriteOperation extends Operation
    {
        long consumesBytes;
        
        WriteOperation(final ByteBufferSet bufferSet, final LongConsumer onSuccess, final Consumer<Throwable> onFailure) {
            super(bufferSet, onSuccess, onFailure);
            this.consumesBytes = 0L;
        }
    }
    
    private enum Shutdown
    {
        No, 
        Wait, 
        Immediate;
    }
}
