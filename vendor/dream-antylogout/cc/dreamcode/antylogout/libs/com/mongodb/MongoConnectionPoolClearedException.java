package cc.dreamcode.antylogout.libs.com.mongodb;

import cc.dreamcode.antylogout.libs.com.mongodb.assertions.Assertions;
import cc.dreamcode.antylogout.libs.com.mongodb.lang.Nullable;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.ServerId;

public final class MongoConnectionPoolClearedException extends MongoClientException
{
    private static final long serialVersionUID = 1L;
    
    public MongoConnectionPoolClearedException(final ServerId connectionPoolServerId, @Nullable final Throwable cause) {
        super("Connection pool for " + (Object)Assertions.assertNotNull(connectionPoolServerId) + " is paused" + ((cause == null) ? "" : " because another operation failed"), cause);
    }
}
