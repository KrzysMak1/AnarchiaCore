package cc.dreamcode.antylogout.libs.com.mongodb.internal.session;

import cc.dreamcode.antylogout.libs.com.mongodb.lang.Nullable;
import cc.dreamcode.antylogout.libs.org.bson.BsonTimestamp;
import cc.dreamcode.antylogout.libs.org.bson.BsonDocument;
import cc.dreamcode.antylogout.libs.com.mongodb.assertions.Assertions;
import cc.dreamcode.antylogout.libs.com.mongodb.session.ClientSession;

public abstract class ClientSessionContext implements SessionContext
{
    private final ClientSession clientSession;
    
    public ClientSessionContext(final ClientSession clientSession) {
        this.clientSession = Assertions.notNull("clientSession", clientSession);
    }
    
    public ClientSession getClientSession() {
        return this.clientSession;
    }
    
    @Override
    public boolean hasSession() {
        return true;
    }
    
    @Override
    public BsonDocument getSessionId() {
        return this.clientSession.getServerSession().getIdentifier();
    }
    
    @Override
    public boolean isCausallyConsistent() {
        return this.clientSession.isCausallyConsistent();
    }
    
    @Override
    public long getTransactionNumber() {
        return this.clientSession.getServerSession().getTransactionNumber();
    }
    
    @Override
    public long advanceTransactionNumber() {
        return this.clientSession.getServerSession().advanceTransactionNumber();
    }
    
    @Override
    public BsonTimestamp getOperationTime() {
        return this.clientSession.getOperationTime();
    }
    
    @Override
    public void advanceOperationTime(@Nullable final BsonTimestamp operationTime) {
        this.clientSession.advanceOperationTime(operationTime);
    }
    
    @Override
    public BsonDocument getClusterTime() {
        return this.clientSession.getClusterTime();
    }
    
    @Override
    public void advanceClusterTime(@Nullable final BsonDocument clusterTime) {
        this.clientSession.advanceClusterTime(clusterTime);
    }
    
    @Override
    public boolean isSnapshot() {
        final Boolean snapshot = this.clientSession.getOptions().isSnapshot();
        return snapshot != null && snapshot;
    }
    
    @Override
    public void setSnapshotTimestamp(@Nullable final BsonTimestamp snapshotTimestamp) {
        this.clientSession.setSnapshotTimestamp(snapshotTimestamp);
    }
    
    @Nullable
    @Override
    public BsonTimestamp getSnapshotTimestamp() {
        return this.clientSession.getSnapshotTimestamp();
    }
    
    @Override
    public void setRecoveryToken(final BsonDocument recoveryToken) {
        this.clientSession.setRecoveryToken(recoveryToken);
    }
    
    @Override
    public void clearTransactionContext() {
        this.clientSession.clearTransactionContext();
    }
    
    @Override
    public void markSessionDirty() {
        this.clientSession.getServerSession().markDirty();
    }
    
    @Override
    public boolean isSessionMarkedDirty() {
        return this.clientSession.getServerSession().isMarkedDirty();
    }
}
