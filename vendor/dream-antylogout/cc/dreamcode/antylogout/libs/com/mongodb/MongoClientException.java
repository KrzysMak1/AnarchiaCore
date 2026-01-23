package cc.dreamcode.antylogout.libs.com.mongodb;

import cc.dreamcode.antylogout.libs.com.mongodb.lang.Nullable;

public class MongoClientException extends MongoException
{
    private static final long serialVersionUID = -5127414714432646066L;
    
    public MongoClientException(final String message) {
        super(message);
    }
    
    public MongoClientException(final String message, @Nullable final Throwable cause) {
        super(message, cause);
    }
}
