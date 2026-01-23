package cc.dreamcode.antylogout.libs.com.mongodb.internal.session;

import cc.dreamcode.antylogout.libs.com.mongodb.MongoClientException;
import cc.dreamcode.antylogout.libs.com.mongodb.assertions.Assertions;
import cc.dreamcode.antylogout.libs.com.mongodb.lang.Nullable;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.ReferenceCounted;
import cc.dreamcode.antylogout.libs.com.mongodb.ServerAddress;
import cc.dreamcode.antylogout.libs.org.bson.BsonTimestamp;
import cc.dreamcode.antylogout.libs.org.bson.BsonDocument;
import java.util.concurrent.atomic.AtomicBoolean;
import cc.dreamcode.antylogout.libs.com.mongodb.ClientSessionOptions;
import cc.dreamcode.antylogout.libs.com.mongodb.session.ServerSession;
import cc.dreamcode.antylogout.libs.com.mongodb.session.ClientSession;

public class BaseClientSessionImpl implements ClientSession
{
    private static final String CLUSTER_TIME_KEY = "clusterTime";
    private final ServerSessionPool serverSessionPool;
    private ServerSession serverSession;
    private final Object originator;
    private final ClientSessionOptions options;
    private final AtomicBoolean closed;
    private BsonDocument clusterTime;
    private BsonTimestamp operationTime;
    private BsonTimestamp snapshotTimestamp;
    private ServerAddress pinnedServerAddress;
    private BsonDocument recoveryToken;
    private ReferenceCounted transactionContext;
    
    public BaseClientSessionImpl(final ServerSessionPool serverSessionPool, final Object originator, final ClientSessionOptions options) {
        this.closed = new AtomicBoolean(false);
        this.serverSessionPool = serverSessionPool;
        this.originator = originator;
        this.options = options;
        this.pinnedServerAddress = null;
    }
    
    @Nullable
    @Override
    public ServerAddress getPinnedServerAddress() {
        return this.pinnedServerAddress;
    }
    
    @Override
    public Object getTransactionContext() {
        return this.transactionContext;
    }
    
    @Override
    public void setTransactionContext(final ServerAddress address, final Object transactionContext) {
        Assertions.assertTrue(transactionContext instanceof ReferenceCounted);
        this.pinnedServerAddress = address;
        (this.transactionContext = (ReferenceCounted)transactionContext).retain();
    }
    
    @Override
    public void clearTransactionContext() {
        this.pinnedServerAddress = null;
        if (this.transactionContext != null) {
            this.transactionContext.release();
            this.transactionContext = null;
        }
    }
    
    @Override
    public BsonDocument getRecoveryToken() {
        return this.recoveryToken;
    }
    
    @Override
    public void setRecoveryToken(final BsonDocument recoveryToken) {
        this.recoveryToken = recoveryToken;
    }
    
    @Override
    public ClientSessionOptions getOptions() {
        return this.options;
    }
    
    @Override
    public boolean isCausallyConsistent() {
        final Boolean causallyConsistent = this.options.isCausallyConsistent();
        return causallyConsistent == null || causallyConsistent;
    }
    
    @Override
    public Object getOriginator() {
        return this.originator;
    }
    
    @Override
    public BsonDocument getClusterTime() {
        return this.clusterTime;
    }
    
    @Override
    public BsonTimestamp getOperationTime() {
        return this.operationTime;
    }
    
    @Override
    public ServerSession getServerSession() {
        Assertions.isTrue("open", !this.closed.get());
        if (this.serverSession == null) {
            this.serverSession = this.serverSessionPool.get();
        }
        return this.serverSession;
    }
    
    @Override
    public void advanceOperationTime(@Nullable final BsonTimestamp newOperationTime) {
        Assertions.isTrue("open", !this.closed.get());
        this.operationTime = this.greaterOf(newOperationTime);
    }
    
    @Override
    public void advanceClusterTime(@Nullable final BsonDocument newClusterTime) {
        Assertions.isTrue("open", !this.closed.get());
        this.clusterTime = this.greaterOf(newClusterTime);
    }
    
    @Override
    public void setSnapshotTimestamp(@Nullable final BsonTimestamp snapshotTimestamp) {
        Assertions.isTrue("open", !this.closed.get());
        if (snapshotTimestamp != null) {
            if (this.snapshotTimestamp != null && !snapshotTimestamp.equals(this.snapshotTimestamp)) {
                throw new MongoClientException("Snapshot timestamps should not change during the lifetime of the session.  Current timestamp is " + (Object)this.snapshotTimestamp + ", and attempting to set it to " + (Object)snapshotTimestamp);
            }
            this.snapshotTimestamp = snapshotTimestamp;
        }
    }
    
    @Nullable
    @Override
    public BsonTimestamp getSnapshotTimestamp() {
        Assertions.isTrue("open", !this.closed.get());
        return this.snapshotTimestamp;
    }
    
    private BsonDocument greaterOf(@Nullable final BsonDocument newClusterTime) {
        if (newClusterTime == null) {
            return this.clusterTime;
        }
        if (this.clusterTime == null) {
            return newClusterTime;
        }
        return (newClusterTime.getTimestamp("clusterTime").compareTo(this.clusterTime.getTimestamp("clusterTime")) > 0) ? newClusterTime : this.clusterTime;
    }
    
    private BsonTimestamp greaterOf(@Nullable final BsonTimestamp newOperationTime) {
        if (newOperationTime == null) {
            return this.operationTime;
        }
        if (this.operationTime == null) {
            return newOperationTime;
        }
        return (newOperationTime.compareTo(this.operationTime) > 0) ? newOperationTime : this.operationTime;
    }
    
    @Override
    public void close() {
        if (this.closed.compareAndSet(false, true)) {
            if (this.serverSession != null) {
                this.serverSessionPool.release(this.serverSession);
            }
            this.clearTransactionContext();
        }
    }
}
