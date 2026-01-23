package cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.tlschannel;

import javax.net.ssl.SSLSession;
import java.util.function.Consumer;
import java.nio.channels.ByteChannel;

public abstract class TlsChannelBuilder<T extends TlsChannelBuilder<T>>
{
    final ByteChannel underlying;
    Consumer<SSLSession> sessionInitCallback;
    boolean runTasks;
    BufferAllocator plainBufferAllocator;
    BufferAllocator encryptedBufferAllocator;
    boolean releaseBuffers;
    boolean waitForCloseConfirmation;
    
    TlsChannelBuilder(final ByteChannel underlying) {
        this.sessionInitCallback = (Consumer<SSLSession>)(session -> {});
        this.runTasks = true;
        this.plainBufferAllocator = TlsChannel.defaultPlainBufferAllocator;
        this.encryptedBufferAllocator = TlsChannel.defaultEncryptedBufferAllocator;
        this.releaseBuffers = true;
        this.waitForCloseConfirmation = false;
        this.underlying = underlying;
    }
    
    abstract T getThis();
    
    public T withRunTasks(final boolean runTasks) {
        this.runTasks = runTasks;
        return this.getThis();
    }
    
    public T withPlainBufferAllocator(final BufferAllocator bufferAllocator) {
        this.plainBufferAllocator = bufferAllocator;
        return this.getThis();
    }
    
    public T withEncryptedBufferAllocator(final BufferAllocator bufferAllocator) {
        this.encryptedBufferAllocator = bufferAllocator;
        return this.getThis();
    }
    
    public T withSessionInitCallback(final Consumer<SSLSession> sessionInitCallback) {
        this.sessionInitCallback = sessionInitCallback;
        return this.getThis();
    }
    
    public T withReleaseBuffers(final boolean releaseBuffers) {
        this.releaseBuffers = releaseBuffers;
        return this.getThis();
    }
    
    public T withWaitForCloseConfirmation(final boolean waitForCloseConfirmation) {
        this.waitForCloseConfirmation = waitForCloseConfirmation;
        return this.getThis();
    }
}
