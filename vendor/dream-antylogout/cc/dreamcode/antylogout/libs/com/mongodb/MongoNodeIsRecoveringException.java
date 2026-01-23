package cc.dreamcode.antylogout.libs.com.mongodb;

import cc.dreamcode.antylogout.libs.org.bson.BsonDocument;

public class MongoNodeIsRecoveringException extends MongoCommandException
{
    private static final long serialVersionUID = 6062524147327071635L;
    
    public MongoNodeIsRecoveringException(final BsonDocument response, final ServerAddress serverAddress) {
        super(response, serverAddress);
    }
}
