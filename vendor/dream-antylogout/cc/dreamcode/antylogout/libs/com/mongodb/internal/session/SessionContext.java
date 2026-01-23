package cc.dreamcode.antylogout.libs.com.mongodb.internal.session;

import cc.dreamcode.antylogout.libs.com.mongodb.ReadConcern;
import cc.dreamcode.antylogout.libs.com.mongodb.lang.Nullable;
import cc.dreamcode.antylogout.libs.org.bson.BsonTimestamp;
import cc.dreamcode.antylogout.libs.org.bson.BsonDocument;

public interface SessionContext
{
    boolean hasSession();
    
    boolean isImplicitSession();
    
    BsonDocument getSessionId();
    
    boolean isCausallyConsistent();
    
    long getTransactionNumber();
    
    long advanceTransactionNumber();
    
    boolean notifyMessageSent();
    
    @Nullable
    BsonTimestamp getOperationTime();
    
    void advanceOperationTime(@Nullable final BsonTimestamp p0);
    
    @Nullable
    BsonDocument getClusterTime();
    
    void advanceClusterTime(@Nullable final BsonDocument p0);
    
    boolean isSnapshot();
    
    void setSnapshotTimestamp(@Nullable final BsonTimestamp p0);
    
    @Nullable
    BsonTimestamp getSnapshotTimestamp();
    
    boolean hasActiveTransaction();
    
    ReadConcern getReadConcern();
    
    void setRecoveryToken(final BsonDocument p0);
    
    void clearTransactionContext();
    
    void markSessionDirty();
    
    boolean isSessionMarkedDirty();
}
