package cc.dreamcode.antylogout.libs.com.mongodb.internal.connection;

import cc.dreamcode.antylogout.libs.com.mongodb.internal.diagnostics.logging.Loggers;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.ServerDescription;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.ConnectionDescription;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.ErrorHandlingResultCallback;
import cc.dreamcode.antylogout.libs.com.mongodb.RequestContext;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.session.SessionContext;
import cc.dreamcode.antylogout.libs.org.bson.codecs.Decoder;
import java.util.List;
import cc.dreamcode.antylogout.libs.org.bson.ByteBuf;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.SingleResultCallback;
import cc.dreamcode.antylogout.libs.com.mongodb.assertions.Assertions;
import cc.dreamcode.antylogout.libs.org.bson.types.ObjectId;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.diagnostics.logging.Logger;

class UsageTrackingInternalConnection implements InternalConnection
{
    private static final Logger LOGGER;
    private volatile long openedAt;
    private volatile long lastUsedAt;
    private volatile boolean closeSilently;
    private final InternalConnection wrapped;
    private final DefaultConnectionPool.ServiceStateManager serviceStateManager;
    
    UsageTrackingInternalConnection(final InternalConnection wrapped, final DefaultConnectionPool.ServiceStateManager serviceStateManager) {
        this.wrapped = wrapped;
        this.serviceStateManager = serviceStateManager;
        this.openedAt = Long.MAX_VALUE;
        this.lastUsedAt = this.openedAt;
    }
    
    @Override
    public void open() {
        this.wrapped.open();
        this.openedAt = System.currentTimeMillis();
        this.lastUsedAt = this.openedAt;
        if (this.getDescription().getServiceId() != null) {
            this.serviceStateManager.addConnection(Assertions.assertNotNull(this.getDescription().getServiceId()));
        }
    }
    
    @Override
    public void openAsync(final SingleResultCallback<Void> callback) {
        this.wrapped.openAsync((result, t) -> {
            if (t != null) {
                callback.onResult(null, t);
            }
            else {
                this.openedAt = System.currentTimeMillis();
                this.lastUsedAt = this.openedAt;
                if (this.getDescription().getServiceId() != null) {
                    this.serviceStateManager.addConnection(this.getDescription().getServiceId());
                }
                callback.onResult(null, null);
            }
        });
    }
    
    @Override
    public void close() {
        try {
            this.wrapped.close();
        }
        finally {
            if (this.openedAt != Long.MAX_VALUE && this.getDescription().getServiceId() != null) {
                this.serviceStateManager.removeConnection(Assertions.assertNotNull(this.getDescription().getServiceId()));
            }
        }
    }
    
    @Override
    public boolean opened() {
        return this.wrapped.opened();
    }
    
    @Override
    public boolean isClosed() {
        return this.wrapped.isClosed();
    }
    
    @Override
    public ByteBuf getBuffer(final int size) {
        return this.wrapped.getBuffer(size);
    }
    
    @Override
    public void sendMessage(final List<ByteBuf> byteBuffers, final int lastRequestId) {
        this.wrapped.sendMessage(byteBuffers, lastRequestId);
        this.lastUsedAt = System.currentTimeMillis();
    }
    
    @Override
    public <T> T sendAndReceive(final CommandMessage message, final Decoder<T> decoder, final SessionContext sessionContext, final RequestContext requestContext, final OperationContext operationContext) {
        final T result = this.wrapped.sendAndReceive(message, decoder, sessionContext, requestContext, operationContext);
        this.lastUsedAt = System.currentTimeMillis();
        return result;
    }
    
    @Override
    public <T> void send(final CommandMessage message, final Decoder<T> decoder, final SessionContext sessionContext) {
        this.wrapped.send(message, decoder, sessionContext);
        this.lastUsedAt = System.currentTimeMillis();
    }
    
    @Override
    public <T> T receive(final Decoder<T> decoder, final SessionContext sessionContext) {
        final T result = this.wrapped.receive(decoder, sessionContext);
        this.lastUsedAt = System.currentTimeMillis();
        return result;
    }
    
    @Override
    public <T> T receive(final Decoder<T> decoder, final SessionContext sessionContext, final int additionalTimeout) {
        final T result = this.wrapped.receive(decoder, sessionContext, additionalTimeout);
        this.lastUsedAt = System.currentTimeMillis();
        return result;
    }
    
    @Override
    public boolean hasMoreToCome() {
        return this.wrapped.hasMoreToCome();
    }
    
    @Override
    public <T> void sendAndReceiveAsync(final CommandMessage message, final Decoder<T> decoder, final SessionContext sessionContext, final RequestContext requestContext, final OperationContext operationContext, final SingleResultCallback<T> callback) {
        final SingleResultCallback<T> errHandlingCallback = ErrorHandlingResultCallback.errorHandlingCallback((result, t) -> {
            this.lastUsedAt = System.currentTimeMillis();
            callback.onResult(result, t);
            return;
        }, UsageTrackingInternalConnection.LOGGER);
        this.wrapped.sendAndReceiveAsync(message, decoder, sessionContext, requestContext, operationContext, errHandlingCallback);
    }
    
    @Override
    public ResponseBuffers receiveMessage(final int responseTo) {
        final ResponseBuffers responseBuffers = this.wrapped.receiveMessage(responseTo);
        this.lastUsedAt = System.currentTimeMillis();
        return responseBuffers;
    }
    
    @Override
    public void sendMessageAsync(final List<ByteBuf> byteBuffers, final int lastRequestId, final SingleResultCallback<Void> callback) {
        final SingleResultCallback<Void> errHandlingCallback = ErrorHandlingResultCallback.errorHandlingCallback((result, t) -> {
            this.lastUsedAt = System.currentTimeMillis();
            callback.onResult(result, t);
            return;
        }, UsageTrackingInternalConnection.LOGGER);
        this.wrapped.sendMessageAsync(byteBuffers, lastRequestId, errHandlingCallback);
    }
    
    @Override
    public void receiveMessageAsync(final int responseTo, final SingleResultCallback<ResponseBuffers> callback) {
        final SingleResultCallback<ResponseBuffers> errHandlingCallback = ErrorHandlingResultCallback.errorHandlingCallback((result, t) -> {
            this.lastUsedAt = System.currentTimeMillis();
            callback.onResult(result, t);
            return;
        }, UsageTrackingInternalConnection.LOGGER);
        this.wrapped.receiveMessageAsync(responseTo, errHandlingCallback);
    }
    
    @Override
    public ConnectionDescription getDescription() {
        return this.wrapped.getDescription();
    }
    
    @Override
    public ServerDescription getInitialServerDescription() {
        return this.wrapped.getInitialServerDescription();
    }
    
    @Override
    public int getGeneration() {
        return this.wrapped.getGeneration();
    }
    
    long getOpenedAt() {
        return this.openedAt;
    }
    
    long getLastUsedAt() {
        return this.lastUsedAt;
    }
    
    void setCloseSilently() {
        this.closeSilently = true;
    }
    
    boolean isCloseSilently() {
        return this.closeSilently;
    }
    
    static {
        LOGGER = Loggers.getLogger("connection");
    }
}
