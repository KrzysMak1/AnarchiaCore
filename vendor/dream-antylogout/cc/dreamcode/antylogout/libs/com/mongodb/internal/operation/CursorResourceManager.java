package cc.dreamcode.antylogout.libs.com.mongodb.internal.operation;

import cc.dreamcode.antylogout.libs.com.mongodb.assertions.Assertions;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoSocketException;
import java.util.function.Supplier;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.Locks;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.Connection;
import java.util.concurrent.locks.ReentrantLock;
import cc.dreamcode.antylogout.libs.com.mongodb.ServerCursor;
import cc.dreamcode.antylogout.libs.com.mongodb.lang.Nullable;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoNamespace;
import java.util.concurrent.locks.Lock;
import cc.dreamcode.antylogout.libs.com.mongodb.annotations.ThreadSafe;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.ReferenceCounted;

@ThreadSafe
abstract class CursorResourceManager<CS extends ReferenceCounted, C extends ReferenceCounted>
{
    private final Lock lock;
    private final MongoNamespace namespace;
    private volatile State state;
    @Nullable
    private volatile CS connectionSource;
    @Nullable
    private volatile C pinnedConnection;
    @Nullable
    private volatile ServerCursor serverCursor;
    private volatile boolean skipReleasingServerResourcesOnClose;
    
    CursorResourceManager(final MongoNamespace namespace, final CS connectionSource, @Nullable final C connectionToPin, @Nullable final ServerCursor serverCursor) {
        this.lock = (Lock)new ReentrantLock();
        this.namespace = namespace;
        this.state = State.IDLE;
        if (serverCursor != null) {
            connectionSource.retain();
            this.connectionSource = connectionSource;
            if (connectionToPin != null) {
                connectionToPin.retain();
                this.markAsPinned(connectionToPin, Connection.PinningMode.CURSOR);
                this.pinnedConnection = connectionToPin;
            }
        }
        this.skipReleasingServerResourcesOnClose = false;
        this.serverCursor = serverCursor;
    }
    
    MongoNamespace getNamespace() {
        return this.namespace;
    }
    
    State getState() {
        return this.state;
    }
    
    @Nullable
    CS getConnectionSource() {
        return this.connectionSource;
    }
    
    @Nullable
    C getPinnedConnection() {
        return this.pinnedConnection;
    }
    
    boolean isSkipReleasingServerResourcesOnClose() {
        return this.skipReleasingServerResourcesOnClose;
    }
    
    abstract void markAsPinned(final C p0, final Connection.PinningMode p1);
    
    boolean operable() {
        return this.state.operable();
    }
    
    boolean tryStartOperation() throws IllegalStateException {
        return Locks.withLock(this.lock, (java.util.function.Supplier<Boolean>)(() -> {
            final State localState = this.state;
            if (!localState.operable()) {
                return false;
            }
            if (localState == State.IDLE) {
                this.state = State.OPERATION_IN_PROGRESS;
                return true;
            }
            if (localState == State.OPERATION_IN_PROGRESS) {
                throw new IllegalStateException("Another operation is currently in progress, concurrent operations are not supported");
            }
            throw Assertions.fail(this.state.toString());
        }));
    }
    
    void endOperation() {
        final boolean doClose = Locks.withLock(this.lock, (java.util.function.Supplier<Boolean>)(() -> {
            final State localState = this.state;
            if (localState == State.OPERATION_IN_PROGRESS) {
                this.state = State.IDLE;
            }
            else {
                if (localState == State.CLOSE_PENDING) {
                    this.state = State.CLOSED;
                    return true;
                }
                if (localState != State.CLOSED) {
                    throw Assertions.fail(localState.toString());
                }
            }
            return false;
        }));
        if (doClose) {
            this.doClose();
        }
    }
    
    void close() {
        final boolean doClose = Locks.withLock(this.lock, (java.util.function.Supplier<Boolean>)(() -> {
            final State localState = this.state;
            if (localState == State.OPERATION_IN_PROGRESS) {
                this.state = State.CLOSE_PENDING;
            }
            else if (localState != State.CLOSED) {
                this.state = State.CLOSED;
                return true;
            }
            return false;
        }));
        if (doClose) {
            this.doClose();
        }
    }
    
    abstract void doClose();
    
    void onCorruptedConnection(@Nullable final C corruptedConnection, final MongoSocketException e) {
        final C localPinnedConnection = this.pinnedConnection;
        if (localPinnedConnection != null) {
            if (corruptedConnection != localPinnedConnection) {
                e.addSuppressed((Throwable)new AssertionError((Object)"Corrupted connection does not equal the pinned connection."));
            }
            this.skipReleasingServerResourcesOnClose = true;
        }
    }
    
    @Nullable
    ServerCursor getServerCursor() {
        return this.serverCursor;
    }
    
    void setServerCursor(@Nullable final ServerCursor serverCursor) {
        Assertions.assertTrue(this.state.inProgress());
        Assertions.assertNotNull(this.serverCursor);
        Assertions.assertNotNull(this.connectionSource);
        this.serverCursor = serverCursor;
        if (serverCursor == null) {
            this.releaseClientResources();
        }
    }
    
    void unsetServerCursor() {
        this.serverCursor = null;
    }
    
    void releaseClientResources() {
        Assertions.assertNull(this.serverCursor);
        final CS localConnectionSource = this.connectionSource;
        if (localConnectionSource != null) {
            localConnectionSource.release();
            this.connectionSource = null;
        }
        final C localPinnedConnection = this.pinnedConnection;
        if (localPinnedConnection != null) {
            localPinnedConnection.release();
            this.pinnedConnection = null;
        }
    }
    
    enum State
    {
        IDLE(true, false), 
        OPERATION_IN_PROGRESS(true, true), 
        CLOSE_PENDING(false, true), 
        CLOSED(false, false);
        
        private final boolean operable;
        private final boolean inProgress;
        
        private State(final boolean operable, final boolean inProgress) {
            this.operable = operable;
            this.inProgress = inProgress;
        }
        
        boolean operable() {
            return this.operable;
        }
        
        boolean inProgress() {
            return this.inProgress;
        }
    }
}
