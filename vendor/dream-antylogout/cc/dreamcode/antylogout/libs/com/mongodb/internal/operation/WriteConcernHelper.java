package cc.dreamcode.antylogout.libs.com.mongodb.internal.operation;

import cc.dreamcode.antylogout.libs.org.bson.BsonString;
import cc.dreamcode.antylogout.libs.com.mongodb.bulk.WriteConcernError;
import java.util.stream.Collectors;
import cc.dreamcode.antylogout.libs.org.bson.BsonArray;
import java.util.Collection;
import cc.dreamcode.antylogout.libs.com.mongodb.WriteConcernResult;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoWriteConcernException;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoException;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.ProtocolHelper;
import cc.dreamcode.antylogout.libs.com.mongodb.ServerAddress;
import cc.dreamcode.antylogout.libs.org.bson.BsonValue;
import cc.dreamcode.antylogout.libs.org.bson.BsonDocument;
import cc.dreamcode.antylogout.libs.com.mongodb.WriteConcern;

public final class WriteConcernHelper
{
    public static void appendWriteConcernToCommand(final WriteConcern writeConcern, final BsonDocument commandDocument) {
        if (writeConcern != null && !writeConcern.isServerDefault()) {
            commandDocument.put("writeConcern", writeConcern.asDocument());
        }
    }
    
    public static void throwOnWriteConcernError(final BsonDocument result, final ServerAddress serverAddress, final int maxWireVersion) {
        if (hasWriteConcernError(result)) {
            MongoException exception = ProtocolHelper.createSpecialException(result, serverAddress, "errmsg");
            if (exception == null) {
                exception = createWriteConcernException(result, serverAddress);
            }
            CommandOperationHelper.addRetryableWriteErrorLabel(exception, maxWireVersion);
            throw exception;
        }
    }
    
    public static boolean hasWriteConcernError(final BsonDocument result) {
        return result.containsKey("writeConcernError");
    }
    
    public static MongoWriteConcernException createWriteConcernException(final BsonDocument result, final ServerAddress serverAddress) {
        return new MongoWriteConcernException(createWriteConcernError(result.getDocument("writeConcernError")), WriteConcernResult.acknowledged(0, false, null), serverAddress, (Collection<String>)result.getArray("errorLabels", new BsonArray()).stream().map(i -> i.asString().getValue()).collect(Collectors.toSet()));
    }
    
    public static WriteConcernError createWriteConcernError(final BsonDocument writeConcernErrorDocument) {
        return new WriteConcernError(writeConcernErrorDocument.getNumber("code").intValue(), writeConcernErrorDocument.getString("codeName", new BsonString("")).getValue(), writeConcernErrorDocument.getString("errmsg").getValue(), writeConcernErrorDocument.getDocument("errInfo", new BsonDocument()));
    }
    
    private WriteConcernHelper() {
    }
}
