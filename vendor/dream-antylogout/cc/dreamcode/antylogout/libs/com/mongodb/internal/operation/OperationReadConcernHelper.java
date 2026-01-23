package cc.dreamcode.antylogout.libs.com.mongodb.internal.operation;

import cc.dreamcode.antylogout.libs.org.bson.BsonValue;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.ReadConcernHelper;
import cc.dreamcode.antylogout.libs.com.mongodb.assertions.Assertions;
import cc.dreamcode.antylogout.libs.org.bson.BsonDocument;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.session.SessionContext;

final class OperationReadConcernHelper
{
    static void appendReadConcernToCommand(final SessionContext sessionContext, final int maxWireVersion, final BsonDocument commandDocument) {
        Assertions.notNull("commandDocument", commandDocument);
        Assertions.notNull("sessionContext", sessionContext);
        if (sessionContext.hasActiveTransaction()) {
            return;
        }
        if (sessionContext.isSnapshot()) {
            return;
        }
        final BsonDocument readConcernDocument = ReadConcernHelper.getReadConcernDocument(sessionContext, maxWireVersion);
        if (!readConcernDocument.isEmpty()) {
            commandDocument.append("readConcern", readConcernDocument);
        }
    }
    
    private OperationReadConcernHelper() {
    }
}
