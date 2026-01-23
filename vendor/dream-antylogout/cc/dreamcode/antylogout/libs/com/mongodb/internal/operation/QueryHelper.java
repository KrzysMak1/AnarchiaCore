package cc.dreamcode.antylogout.libs.com.mongodb.internal.operation;

import cc.dreamcode.antylogout.libs.com.mongodb.MongoCursorNotFoundException;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoQueryException;
import cc.dreamcode.antylogout.libs.com.mongodb.ServerCursor;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoCommandException;

final class QueryHelper
{
    static MongoQueryException translateCommandException(final MongoCommandException commandException, final ServerCursor cursor) {
        if (commandException.getErrorCode() == 43) {
            return new MongoCursorNotFoundException(cursor.getId(), commandException.getResponse(), cursor.getAddress());
        }
        return new MongoQueryException(commandException.getResponse(), commandException.getServerAddress());
    }
    
    private QueryHelper() {
    }
}
