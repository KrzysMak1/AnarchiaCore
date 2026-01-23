package cc.dreamcode.antylogout.libs.com.mongodb;

import cc.dreamcode.antylogout.libs.org.bson.BsonDocument;

public class MongoNotPrimaryException extends MongoCommandException
{
    private static final long serialVersionUID = 694876345217027108L;
    
    public MongoNotPrimaryException(final BsonDocument response, final ServerAddress serverAddress) {
        super(response, serverAddress);
    }
}
