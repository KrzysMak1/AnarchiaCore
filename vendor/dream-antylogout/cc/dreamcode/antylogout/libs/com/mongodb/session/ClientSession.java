package cc.dreamcode.antylogout.libs.com.mongodb.session;

import cc.dreamcode.antylogout.libs.org.bson.BsonTimestamp;
import cc.dreamcode.antylogout.libs.com.mongodb.ClientSessionOptions;
import cc.dreamcode.antylogout.libs.org.bson.BsonDocument;
import cc.dreamcode.antylogout.libs.com.mongodb.lang.Nullable;
import cc.dreamcode.antylogout.libs.com.mongodb.ServerAddress;
import cc.dreamcode.antylogout.libs.com.mongodb.annotations.NotThreadSafe;
import java.io.Closeable;

@NotThreadSafe
public interface ClientSession extends Closeable
{
    @Nullable
    ServerAddress getPinnedServerAddress();
    
    @Nullable
    Object getTransactionContext();
    
    void setTransactionContext(final ServerAddress p0, final Object p1);
    
    void clearTransactionContext();
    
    @Nullable
    BsonDocument getRecoveryToken();
    
    void setRecoveryToken(final BsonDocument p0);
    
    ClientSessionOptions getOptions();
    
    boolean isCausallyConsistent();
    
    Object getOriginator();
    
    ServerSession getServerSession();
    
    BsonTimestamp getOperationTime();
    
    void advanceOperationTime(@Nullable final BsonTimestamp p0);
    
    void advanceClusterTime(@Nullable final BsonDocument p0);
    
    void setSnapshotTimestamp(@Nullable final BsonTimestamp p0);
    
    @Nullable
    BsonTimestamp getSnapshotTimestamp();
    
    BsonDocument getClusterTime();
    
    void close();
}
