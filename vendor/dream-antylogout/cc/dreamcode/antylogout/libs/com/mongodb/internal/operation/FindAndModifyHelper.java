package cc.dreamcode.antylogout.libs.com.mongodb.internal.operation;

import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.Connection;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.AsyncConnection;
import cc.dreamcode.antylogout.libs.org.bson.BsonValue;
import cc.dreamcode.antylogout.libs.org.bson.BsonNumber;
import cc.dreamcode.antylogout.libs.org.bson.BsonInt32;
import cc.dreamcode.antylogout.libs.org.bson.BsonBoolean;
import cc.dreamcode.antylogout.libs.com.mongodb.WriteConcernResult;
import cc.dreamcode.antylogout.libs.com.mongodb.lang.Nullable;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoWriteConcernException;
import java.util.stream.Collectors;
import cc.dreamcode.antylogout.libs.org.bson.BsonArray;
import java.util.Collection;
import cc.dreamcode.antylogout.libs.com.mongodb.ServerAddress;
import cc.dreamcode.antylogout.libs.org.bson.BsonDocument;

final class FindAndModifyHelper
{
    static <T> SyncOperationHelper.CommandWriteTransformer<BsonDocument, T> transformer() {
        return (SyncOperationHelper.CommandWriteTransformer<BsonDocument, T>)((result, connection) -> transformDocument(result, connection.getDescription().getServerAddress()));
    }
    
    static <T> AsyncOperationHelper.CommandWriteTransformerAsync<BsonDocument, T> asyncTransformer() {
        return (AsyncOperationHelper.CommandWriteTransformerAsync<BsonDocument, T>)((result, connection) -> transformDocument(result, connection.getDescription().getServerAddress()));
    }
    
    @Nullable
    private static <T> T transformDocument(final BsonDocument result, final ServerAddress serverAddress) {
        if (WriteConcernHelper.hasWriteConcernError(result)) {
            throw new MongoWriteConcernException(WriteConcernHelper.createWriteConcernError(result.getDocument("writeConcernError")), createWriteConcernResult(result.getDocument("lastErrorObject", new BsonDocument())), serverAddress, (Collection<String>)result.getArray("errorLabels", new BsonArray()).stream().map(i -> i.asString().getValue()).collect(Collectors.toSet()));
        }
        if (!result.isDocument("value")) {
            return null;
        }
        return BsonDocumentWrapperHelper.toDocument(result.getDocument("value", null));
    }
    
    private static WriteConcernResult createWriteConcernResult(final BsonDocument result) {
        final BsonBoolean updatedExisting = result.getBoolean("updatedExisting", BsonBoolean.FALSE);
        return WriteConcernResult.acknowledged(result.getNumber("n", new BsonInt32(0)).intValue(), updatedExisting.getValue(), result.get("upserted"));
    }
    
    private FindAndModifyHelper() {
    }
}
