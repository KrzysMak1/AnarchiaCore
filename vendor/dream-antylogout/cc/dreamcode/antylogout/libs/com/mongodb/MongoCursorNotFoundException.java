package cc.dreamcode.antylogout.libs.com.mongodb;

import cc.dreamcode.antylogout.libs.org.bson.BsonDocument;

public class MongoCursorNotFoundException extends MongoQueryException
{
    private static final long serialVersionUID = -4415279469780082174L;
    private final long cursorId;
    
    public MongoCursorNotFoundException(final long cursorId, final BsonDocument response, final ServerAddress serverAddress) {
        super(response, serverAddress);
        this.cursorId = cursorId;
    }
    
    public long getCursorId() {
        return this.cursorId;
    }
}
