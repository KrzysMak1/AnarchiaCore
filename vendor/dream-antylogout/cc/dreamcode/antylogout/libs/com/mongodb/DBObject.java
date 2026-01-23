package cc.dreamcode.antylogout.libs.com.mongodb;

import cc.dreamcode.antylogout.libs.org.bson.BSONObject;

public interface DBObject extends BSONObject
{
    void markAsPartialObject();
    
    boolean isPartialObject();
}
