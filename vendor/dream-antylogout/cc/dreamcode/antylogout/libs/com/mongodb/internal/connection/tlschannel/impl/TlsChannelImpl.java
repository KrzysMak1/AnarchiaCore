package cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.tlschannel.impl;

import java.nio.Buffer;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.diagnostics.logging.Loggers;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.tlschannel.TlsChannelCallbackException;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.tlschannel.NeedsWriteException;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.tlschannel.NeedsReadException;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.tlschannel.WouldBlockException;
import javax.net.ssl.SSLException;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.tlschannel.NeedsTaskException;
import java.io.IOException;
import javax.net.ssl.SSLEngineResult;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.tlschannel.util.Util;
import java.nio.channels.ClosedChannelException;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.tlschannel.BufferAllocator;
import java.nio.ByteBuffer;
import java.util.concurrent.locks.ReentrantLock;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.tlschannel.TrackingAllocator;
import javax.net.ssl.SSLSession;
import java.util.function.Consumer;
import javax.net.ssl.SSLEngine;
import java.nio.channels.WritableByteChannel;
import java.nio.channels.ReadableByteChannel;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.diagnostics.logging.Logger;
import java.nio.channels.ByteChannel;

public class TlsChannelImpl implements ByteChannel
{
    private static final Logger LOGGER;
    public static final int buffersInitialSize = 4096;
    public static final int maxTlsPacketSize = 17408;
    private final ReadableByteChannel readChannel;
    private final WritableByteChannel writeChannel;
    private final SSLEngine engine;
    private BufferHolder inEncrypted;
    private final Consumer<SSLSession> initSessionCallback;
    private final boolean runTasks;
    private final TrackingAllocator encryptedBufAllocator;
    private final TrackingAllocator plainBufAllocator;
    private final boolean waitForCloseConfirmation;
    private final Lock initLock;
    private final Lock readLock;
    private final Lock writeLock;
    private volatile boolean negotiated;
    private volatile boolean invalid;
    private volatile boolean shutdownSent;
    private volatile boolean shutdownReceived;
    private BufferHolder inPlain;
    private BufferHolder outEncrypted;
    private final ByteBufferSet dummyOut;
    
    public TlsChannelImpl(final ReadableByteChannel readChannel, final WritableByteChannel writeChannel, final SSLEngine engine, final Optional<BufferHolder> inEncrypted, final Consumer<SSLSession> initSessionCallback, final boolean runTasks, final TrackingAllocator plainBufAllocator, final TrackingAllocator encryptedBufAllocator, final boolean releaseBuffers, final boolean waitForCloseConfirmation) {
        this.initLock = (Lock)new ReentrantLock();
        this.readLock = (Lock)new ReentrantLock();
        this.writeLock = (Lock)new ReentrantLock();
        this.negotiated = false;
        this.invalid = false;
        this.shutdownSent = false;
        this.shutdownReceived = false;
        this.dummyOut = new ByteBufferSet(new ByteBuffer[] { ByteBuffer.allocate(0) });
        this.readChannel = readChannel;
        this.writeChannel = writeChannel;
        this.engine = engine;
        this.inEncrypted = (BufferHolder)inEncrypted.orElseGet(() -> new BufferHolder("inEncrypted", (Optional<ByteBuffer>)Optional.empty(), encryptedBufAllocator, 4096, 17408, false, releaseBuffers));
        this.initSessionCallback = initSessionCallback;
        this.runTasks = runTasks;
        this.plainBufAllocator = plainBufAllocator;
        this.encryptedBufAllocator = encryptedBufAllocator;
        this.waitForCloseConfirmation = waitForCloseConfirmation;
        this.inPlain = new BufferHolder("inPlain", (Optional<ByteBuffer>)Optional.empty(), plainBufAllocator, 4096, 17408, true, releaseBuffers);
        this.outEncrypted = new BufferHolder("outEncrypted", (Optional<ByteBuffer>)Optional.empty(), encryptedBufAllocator, 4096, 17408, false, releaseBuffers);
    }
    
    public Consumer<SSLSession> getSessionInitCallback() {
        return this.initSessionCallback;
    }
    
    public TrackingAllocator getPlainBufferAllocator() {
        return this.plainBufAllocator;
    }
    
    public TrackingAllocator getEncryptedBufferAllocator() {
        return this.encryptedBufAllocator;
    }
    
    public long read(final ByteBufferSet dest) throws IOException {
        checkReadBuffer(dest);
        if (!dest.hasRemaining()) {
            return 0L;
        }
        this.handshake();
        this.readLock.lock();
        try {
            if (this.invalid || this.shutdownSent) {
                throw new ClosedChannelException();
            }
            SSLEngineResult.HandshakeStatus handshakeStatus = this.engine.getHandshakeStatus();
            int bytesToReturn = this.inPlain.nullOrEmpty() ? 0 : this.inPlain.buffer.position();
            while (bytesToReturn <= 0) {
                if (this.shutdownReceived) {
                    return -1L;
                }
                Util.assertTrue(this.inPlain.nullOrEmpty());
                switch (handshakeStatus) {
                    case NEED_UNWRAP:
                    case NEED_WRAP: {
                        bytesToReturn = this.handshake((Optional<ByteBufferSet>)Optional.of((Object)dest), (Optional<SSLEngineResult.HandshakeStatus>)Optional.of((Object)handshakeStatus));
                        handshakeStatus = SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING;
                        continue;
                    }
                    case NOT_HANDSHAKING:
                    case FINISHED: {
                        final UnwrapResult res = this.readAndUnwrap((Optional<ByteBufferSet>)Optional.of((Object)dest));
                        if (res.wasClosed) {
                            return -1L;
                        }
                        bytesToReturn = res.bytesProduced;
                        handshakeStatus = res.lastHandshakeStatus;
                        continue;
                    }
                    case NEED_TASK: {
                        this.handleTask();
                        handshakeStatus = this.engine.getHandshakeStatus();
                        continue;
                    }
                    default: {
                        return -1L;
                    }
                }
            }
            if (this.inPlain.nullOrEmpty()) {
                return bytesToReturn;
            }
            return this.transferPendingPlain(dest);
        }
        catch (final EofException e) {
            return -1L;
        }
        finally {
            this.readLock.unlock();
        }
    }
    
    private void handleTask() throws NeedsTaskException {
        if (this.runTasks) {
            this.engine.getDelegatedTask().run();
            return;
        }
        throw new NeedsTaskException(this.engine.getDelegatedTask());
    }
    
    private int transferPendingPlain(final ByteBufferSet dstBuffers) {
        ((Buffer)this.inPlain.buffer).flip();
        final int bytes = dstBuffers.putRemaining(this.inPlain.buffer);
        this.inPlain.buffer.compact();
        final boolean disposed = this.inPlain.release();
        if (!disposed) {
            this.inPlain.zeroRemaining();
        }
        return bytes;
    }
    
    private UnwrapResult unwrapLoop(final Optional<ByteBufferSet> dest, final SSLEngineResult.HandshakeStatus originalStatus) throws SSLException {
        ByteBufferSet effDest = (ByteBufferSet)dest.orElseGet(() -> {
            this.inPlain.prepare();
            return new ByteBufferSet(this.inPlain.buffer);
        });
        SSLEngineResult result;
        while (true) {
            Util.assertTrue(this.inPlain.nullOrEmpty());
            result = this.callEngineUnwrap(effDest);
            if (result.bytesProduced() > 0 || result.getStatus() == SSLEngineResult.Status.BUFFER_UNDERFLOW || result.getStatus() == SSLEngineResult.Status.CLOSED || result.getHandshakeStatus() != originalStatus) {
                break;
            }
            if (result.getStatus() != SSLEngineResult.Status.BUFFER_OVERFLOW) {
                continue;
            }
            if (dest.isPresent() && effDest == dest.get()) {
                this.inPlain.prepare();
                this.ensureInPlainCapacity(Math.min((int)((ByteBufferSet)dest.get()).remaining() * 2, 17408));
            }
            else {
                this.inPlain.enlarge();
            }
            effDest = new ByteBufferSet(this.inPlain.buffer);
        }
        final boolean wasClosed = result.getStatus() == SSLEngineResult.Status.CLOSED;
        return new UnwrapResult(result.bytesProduced(), result.getHandshakeStatus(), wasClosed);
    }
    
    private SSLEngineResult callEngineUnwrap(final ByteBufferSet dest) throws SSLException {
        ((Buffer)this.inEncrypted.buffer).flip();
        try {
            final SSLEngineResult result = this.engine.unwrap(this.inEncrypted.buffer, dest.array, dest.offset, dest.length);
            if (TlsChannelImpl.LOGGER.isTraceEnabled()) {
                TlsChannelImpl.LOGGER.trace(String.format("engine.unwrap() result [%s]. Engine status: %s; inEncrypted %s; inPlain: %s", new Object[] { Util.resultToString(result), result.getHandshakeStatus(), this.inEncrypted, dest }));
            }
            return result;
        }
        catch (final SSLException e) {
            this.invalid = true;
            throw e;
        }
        finally {
            this.inEncrypted.buffer.compact();
        }
    }
    
    private int readFromChannel() throws IOException, EofException {
        try {
            return readFromChannel(this.readChannel, this.inEncrypted.buffer);
        }
        catch (final WouldBlockException e) {
            throw e;
        }
        catch (final IOException e2) {
            this.invalid = true;
            throw e2;
        }
    }
    
    public static int readFromChannel(final ReadableByteChannel readChannel, final ByteBuffer buffer) throws IOException, EofException {
        Util.assertTrue(buffer.hasRemaining());
        TlsChannelImpl.LOGGER.trace("Reading from channel");
        final int c = readChannel.read(buffer);
        if (TlsChannelImpl.LOGGER.isTraceEnabled()) {
            TlsChannelImpl.LOGGER.trace(String.format("Read from channel; response: %s, buffer: %s", new Object[] { c, buffer }));
        }
        if (c == -1) {
            throw new EofException();
        }
        if (c == 0) {
            throw new NeedsReadException();
        }
        return c;
    }
    
    public long write(final ByteBufferSet source) throws IOException {
        this.handshake();
        this.writeLock.lock();
        try {
            if (this.invalid || this.shutdownSent) {
                throw new ClosedChannelException();
            }
            return this.wrapAndWrite(source);
        }
        finally {
            this.writeLock.unlock();
        }
    }
    
    private long wrapAndWrite(final ByteBufferSet source) throws IOException {
        final long bytesToConsume = source.remaining();
        long bytesConsumed = 0L;
        this.outEncrypted.prepare();
        try {
            while (true) {
                this.writeToChannel();
                if (bytesConsumed == bytesToConsume) {
                    break;
                }
                final WrapResult res = this.wrapLoop(source);
                bytesConsumed += res.bytesConsumed;
            }
            return bytesToConsume;
        }
        finally {
            this.outEncrypted.release();
        }
    }
    
    private WrapResult wrapLoop(final ByteBufferSet source) throws SSLException {
        while (true) {
            final SSLEngineResult result = this.callEngineWrap(source);
            switch (result.getStatus()) {
                case OK:
                case CLOSED: {
                    return new WrapResult(result.bytesConsumed(), result.getHandshakeStatus());
                }
                case BUFFER_OVERFLOW: {
                    Util.assertTrue(result.bytesConsumed() == 0);
                    this.outEncrypted.enlarge();
                    continue;
                }
                case BUFFER_UNDERFLOW: {
                    throw new IllegalStateException();
                }
            }
        }
    }
    
    private SSLEngineResult callEngineWrap(final ByteBufferSet source) throws SSLException {
        try {
            final SSLEngineResult result = this.engine.wrap(source.array, source.offset, source.length, this.outEncrypted.buffer);
            if (TlsChannelImpl.LOGGER.isTraceEnabled()) {
                TlsChannelImpl.LOGGER.trace(String.format("engine.wrap() result: [%s]; engine status: %s; srcBuffer: %s, outEncrypted: %s", new Object[] { Util.resultToString(result), result.getHandshakeStatus(), source, this.outEncrypted }));
            }
            return result;
        }
        catch (final SSLException e) {
            this.invalid = true;
            throw e;
        }
    }
    
    private void ensureInPlainCapacity(final int newCapacity) {
        if (this.inPlain.buffer.capacity() < newCapacity) {
            if (TlsChannelImpl.LOGGER.isTraceEnabled()) {
                TlsChannelImpl.LOGGER.trace(String.format("inPlain buffer too small, increasing from %s to %s", new Object[] { this.inPlain.buffer.capacity(), newCapacity }));
            }
            this.inPlain.resize(newCapacity);
        }
    }
    
    private void writeToChannel() throws IOException {
        if (this.outEncrypted.buffer.position() == 0) {
            return;
        }
        ((Buffer)this.outEncrypted.buffer).flip();
        try {
            writeToChannel(this.writeChannel, this.outEncrypted.buffer);
        }
        catch (final WouldBlockException e) {
            throw e;
        }
        catch (final IOException e2) {
            this.invalid = true;
            throw e2;
        }
        finally {
            this.outEncrypted.buffer.compact();
        }
    }
    
    private static void writeToChannel(final WritableByteChannel channel, final ByteBuffer src) throws IOException {
        while (src.hasRemaining()) {
            if (TlsChannelImpl.LOGGER.isTraceEnabled()) {
                TlsChannelImpl.LOGGER.trace("Writing to channel: " + (Object)src);
            }
            final int c = channel.write(src);
            if (c == 0) {
                throw new NeedsWriteException();
            }
        }
    }
    
    public void renegotiate() throws IOException {
        if (this.engine.getSession().getProtocol().compareTo("TLSv1.3") >= 0) {
            throw new SSLException("renegotiation not supported in TLS 1.3 or latter");
        }
        try {
            this.doHandshake(true);
        }
        catch (final EofException e) {
            throw new ClosedChannelException();
        }
    }
    
    public void handshake() throws IOException {
        try {
            this.doHandshake(false);
        }
        catch (final EofException e) {
            throw new ClosedChannelException();
        }
    }
    
    private void doHandshake(final boolean force) throws IOException, EofException {
        if (!force && this.negotiated) {
            return;
        }
        this.initLock.lock();
        try {
            if (this.invalid || this.shutdownSent) {
                throw new ClosedChannelException();
            }
            if (force || !this.negotiated) {
                this.engine.beginHandshake();
                TlsChannelImpl.LOGGER.trace("Called engine.beginHandshake()");
                this.handshake((Optional<ByteBufferSet>)Optional.empty(), (Optional<SSLEngineResult.HandshakeStatus>)Optional.empty());
                try {
                    this.initSessionCallback.accept((Object)this.engine.getSession());
                }
                catch (final Exception e) {
                    TlsChannelImpl.LOGGER.trace("client code threw exception in session initialization callback", (Throwable)e);
                    throw new TlsChannelCallbackException("session initialization callback failed", (Throwable)e);
                }
                this.negotiated = true;
            }
        }
        finally {
            this.initLock.unlock();
        }
    }
    
    private int handshake(final Optional<ByteBufferSet> dest, final Optional<SSLEngineResult.HandshakeStatus> handshakeStatus) throws IOException, EofException {
        this.readLock.lock();
        try {
            this.writeLock.lock();
            try {
                Util.assertTrue(this.inPlain.nullOrEmpty());
                this.outEncrypted.prepare();
                try {
                    this.writeToChannel();
                    return this.handshakeLoop(dest, handshakeStatus);
                }
                finally {
                    this.outEncrypted.release();
                }
            }
            finally {
                this.writeLock.unlock();
            }
        }
        finally {
            this.readLock.unlock();
        }
    }
    
    private int handshakeLoop(final Optional<ByteBufferSet> dest, final Optional<SSLEngineResult.HandshakeStatus> handshakeStatus) throws IOException, EofException {
        Util.assertTrue(this.inPlain.nullOrEmpty());
        SSLEngineResult.HandshakeStatus status = (SSLEngineResult.HandshakeStatus)handshakeStatus.orElseGet(() -> this.engine.getHandshakeStatus());
        while (true) {
            switch (status) {
                case NEED_WRAP: {
                    Util.assertTrue(this.outEncrypted.nullOrEmpty());
                    final WrapResult wrapResult = this.wrapLoop(this.dummyOut);
                    status = wrapResult.lastHandshakeStatus;
                    this.writeToChannel();
                    continue;
                }
                case NEED_UNWRAP: {
                    final UnwrapResult res = this.readAndUnwrap(dest);
                    status = res.lastHandshakeStatus;
                    if (res.bytesProduced > 0) {
                        return res.bytesProduced;
                    }
                    continue;
                }
                case NOT_HANDSHAKING: {
                    return 0;
                }
                case NEED_TASK: {
                    this.handleTask();
                    status = this.engine.getHandshakeStatus();
                    continue;
                }
                case FINISHED: {
                    return 0;
                }
                default: {
                    return 0;
                }
            }
        }
    }
    
    private UnwrapResult readAndUnwrap(final Optional<ByteBufferSet> dest) throws IOException, EofException {
        final SSLEngineResult.HandshakeStatus orig = this.engine.getHandshakeStatus();
        this.inEncrypted.prepare();
        try {
            UnwrapResult res;
            while (true) {
                Util.assertTrue(this.inPlain.nullOrEmpty());
                res = this.unwrapLoop(dest, orig);
                if (res.bytesProduced > 0 || res.lastHandshakeStatus != orig || res.wasClosed) {
                    break;
                }
                if (!this.inEncrypted.buffer.hasRemaining()) {
                    this.inEncrypted.enlarge();
                }
                this.readFromChannel();
            }
            if (res.wasClosed) {
                this.shutdownReceived = true;
            }
            return res;
        }
        finally {
            this.inEncrypted.release();
        }
    }
    
    public void close() throws IOException {
        this.tryShutdown();
        this.writeChannel.close();
        this.readChannel.close();
        this.readLock.lock();
        try {
            this.writeLock.lock();
            try {
                this.freeBuffers();
            }
            finally {
                this.writeLock.unlock();
            }
        }
        finally {
            this.readLock.unlock();
        }
    }
    
    private void tryShutdown() {
        if (!this.readLock.tryLock()) {
            return;
        }
        try {
            if (!this.writeLock.tryLock()) {
                return;
            }
            try {
                if (!this.shutdownSent) {
                    try {
                        final boolean closed = this.shutdown();
                        if (!closed && this.waitForCloseConfirmation) {
                            this.shutdown();
                        }
                    }
                    catch (final Throwable e) {
                        if (TlsChannelImpl.LOGGER.isDebugEnabled()) {
                            TlsChannelImpl.LOGGER.debug("error doing TLS shutdown on close(), continuing: " + e.getMessage());
                        }
                    }
                }
            }
            finally {
                this.writeLock.unlock();
            }
        }
        finally {
            this.readLock.unlock();
        }
    }
    
    public boolean shutdown() throws IOException {
        this.readLock.lock();
        try {
            this.writeLock.lock();
            try {
                if (this.invalid) {
                    throw new ClosedChannelException();
                }
                if (!this.shutdownSent) {
                    this.shutdownSent = true;
                    this.outEncrypted.prepare();
                    try {
                        this.writeToChannel();
                        this.engine.closeOutbound();
                        this.wrapLoop(this.dummyOut);
                        this.writeToChannel();
                    }
                    finally {
                        this.outEncrypted.release();
                    }
                    if (this.shutdownReceived) {
                        this.freeBuffers();
                    }
                    return this.shutdownReceived;
                }
                if (!this.shutdownReceived) {
                    try {
                        this.readAndUnwrap((Optional<ByteBufferSet>)Optional.empty());
                        Util.assertTrue(this.shutdownReceived);
                    }
                    catch (final EofException e) {
                        throw new ClosedChannelException();
                    }
                }
                this.freeBuffers();
                return true;
            }
            finally {
                this.writeLock.unlock();
            }
        }
        finally {
            this.readLock.unlock();
        }
    }
    
    private void freeBuffers() {
        if (this.inEncrypted != null) {
            this.inEncrypted.dispose();
            this.inEncrypted = null;
        }
        if (this.inPlain != null) {
            this.inPlain.dispose();
            this.inPlain = null;
        }
        if (this.outEncrypted != null) {
            this.outEncrypted.dispose();
            this.outEncrypted = null;
        }
    }
    
    public boolean isOpen() {
        return !this.invalid && this.writeChannel.isOpen() && this.readChannel.isOpen();
    }
    
    public static void checkReadBuffer(final ByteBufferSet dest) {
        if (dest.isReadOnly()) {
            throw new IllegalArgumentException();
        }
    }
    
    public SSLEngine engine() {
        return this.engine;
    }
    
    public boolean getRunTasks() {
        return this.runTasks;
    }
    
    public int read(final ByteBuffer dst) throws IOException {
        return (int)this.read(new ByteBufferSet(dst));
    }
    
    public int write(final ByteBuffer src) throws IOException {
        return (int)this.write(new ByteBufferSet(src));
    }
    
    public boolean shutdownReceived() {
        return this.shutdownReceived;
    }
    
    public boolean shutdownSent() {
        return this.shutdownSent;
    }
    
    public ReadableByteChannel plainReadableChannel() {
        return this.readChannel;
    }
    
    public WritableByteChannel plainWritableChannel() {
        return this.writeChannel;
    }
    
    static {
        LOGGER = Loggers.getLogger("connection.tls");
    }
    
    private static class UnwrapResult
    {
        public final int bytesProduced;
        public final SSLEngineResult.HandshakeStatus lastHandshakeStatus;
        public final boolean wasClosed;
        
        public UnwrapResult(final int bytesProduced, final SSLEngineResult.HandshakeStatus lastHandshakeStatus, final boolean wasClosed) {
            this.bytesProduced = bytesProduced;
            this.lastHandshakeStatus = lastHandshakeStatus;
            this.wasClosed = wasClosed;
        }
    }
    
    private static class WrapResult
    {
        public final int bytesConsumed;
        public final SSLEngineResult.HandshakeStatus lastHandshakeStatus;
        
        public WrapResult(final int bytesConsumed, final SSLEngineResult.HandshakeStatus lastHandshakeStatus) {
            this.bytesConsumed = bytesConsumed;
            this.lastHandshakeStatus = lastHandshakeStatus;
        }
    }
    
    public static class EofException extends Exception
    {
        private static final long serialVersionUID = -3859156713994602991L;
        
        public Throwable fillInStackTrace() {
            return (Throwable)this;
        }
    }
}
