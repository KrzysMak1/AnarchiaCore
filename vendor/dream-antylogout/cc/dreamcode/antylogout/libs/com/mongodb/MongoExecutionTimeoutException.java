package cc.dreamcode.antylogout.libs.com.mongodb;

import cc.dreamcode.antylogout.libs.org.bson.BsonDocument;

public class MongoExecutionTimeoutException extends MongoException
{
    private static final long serialVersionUID = 5955669123800274594L;
    
    public MongoExecutionTimeoutException(final int code, final String message) {
        super(code, message);
    }
    
    public MongoExecutionTimeoutException(final int code, final String message, final BsonDocument response) {
        super(code, message, response);
    }
}
