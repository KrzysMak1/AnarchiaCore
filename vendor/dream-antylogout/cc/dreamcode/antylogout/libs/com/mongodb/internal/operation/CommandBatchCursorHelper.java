package cc.dreamcode.antylogout.libs.com.mongodb.internal.operation;

import cc.dreamcode.antylogout.libs.com.mongodb.internal.validator.NoOpFieldNameValidator;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoCursorNotFoundException;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoQueryException;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoCommandException;
import java.util.List;
import cc.dreamcode.antylogout.libs.org.bson.BsonArray;
import java.util.Collections;
import cc.dreamcode.antylogout.libs.com.mongodb.ServerCursor;
import cc.dreamcode.antylogout.libs.org.bson.BsonInt32;
import cc.dreamcode.antylogout.libs.org.bson.BsonString;
import cc.dreamcode.antylogout.libs.org.bson.BsonInt64;
import cc.dreamcode.antylogout.libs.org.bson.BsonDocument;
import cc.dreamcode.antylogout.libs.com.mongodb.lang.Nullable;
import cc.dreamcode.antylogout.libs.org.bson.BsonValue;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoNamespace;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.ConnectionDescription;
import cc.dreamcode.antylogout.libs.org.bson.FieldNameValidator;

final class CommandBatchCursorHelper
{
    static final String FIRST_BATCH = "firstBatch";
    static final String NEXT_BATCH = "nextBatch";
    static final FieldNameValidator NO_OP_FIELD_NAME_VALIDATOR;
    static final String MESSAGE_IF_CLOSED_AS_CURSOR = "Cursor has been closed";
    static final String MESSAGE_IF_CLOSED_AS_ITERATOR = "Iterator has been closed";
    static final String MESSAGE_IF_CONCURRENT_OPERATION = "Another operation is currently in progress, concurrent operations are not supported";
    
    static BsonDocument getMoreCommandDocument(final long cursorId, final ConnectionDescription connectionDescription, final MongoNamespace namespace, final int batchSize, final long maxTimeMS, @Nullable final BsonValue comment) {
        final BsonDocument document = new BsonDocument("getMore", new BsonInt64(cursorId)).append("collection", new BsonString(namespace.getCollectionName()));
        if (batchSize != 0) {
            document.append("batchSize", new BsonInt32(batchSize));
        }
        if (maxTimeMS != 0L) {
            document.append("maxTimeMS", new BsonInt64(maxTimeMS));
        }
        if (ServerVersionHelper.serverIsAtLeastVersionFourDotFour(connectionDescription)) {
            DocumentHelper.putIfNotNull(document, "comment", comment);
        }
        return document;
    }
    
    static <T> CommandCursorResult<T> logCommandCursorResult(final CommandCursorResult<T> commandCursorResult) {
        if (OperationHelper.LOGGER.isDebugEnabled()) {
            OperationHelper.LOGGER.debug(String.format("Received batch of %d documents with cursorId %d from server %s", new Object[] { commandCursorResult.getResults().size(), commandCursorResult.getCursorId(), commandCursorResult.getServerAddress() }));
        }
        return commandCursorResult;
    }
    
    static BsonDocument getKillCursorsCommand(final MongoNamespace namespace, final ServerCursor serverCursor) {
        return new BsonDocument("killCursors", new BsonString(namespace.getCollectionName())).append("cursors", new BsonArray((List<? extends BsonValue>)Collections.singletonList((Object)new BsonInt64(serverCursor.getId()))));
    }
    
    static MongoQueryException translateCommandException(final MongoCommandException commandException, final ServerCursor cursor) {
        if (commandException.getErrorCode() == 43) {
            return new MongoCursorNotFoundException(cursor.getId(), commandException.getResponse(), cursor.getAddress());
        }
        return new MongoQueryException(commandException.getResponse(), commandException.getServerAddress());
    }
    
    private CommandBatchCursorHelper() {
    }
    
    static {
        NO_OP_FIELD_NAME_VALIDATOR = new NoOpFieldNameValidator();
    }
}
