package cc.dreamcode.antylogout.libs.com.mongodb;

import cc.dreamcode.antylogout.libs.org.bson.BsonDocument;

public class MongoQueryException extends MongoCommandException
{
    private static final long serialVersionUID = -5113350133297015801L;
    
    public MongoQueryException(final BsonDocument response, final ServerAddress serverAddress) {
        super(response, serverAddress);
    }
}
