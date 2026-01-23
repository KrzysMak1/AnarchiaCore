package cc.dreamcode.antylogout.libs.com.mongodb.session;

import cc.dreamcode.antylogout.libs.org.bson.BsonDocument;

public interface ServerSession
{
    BsonDocument getIdentifier();
    
    long getTransactionNumber();
    
    long advanceTransactionNumber();
    
    boolean isClosed();
    
    void markDirty();
    
    boolean isMarkedDirty();
}
