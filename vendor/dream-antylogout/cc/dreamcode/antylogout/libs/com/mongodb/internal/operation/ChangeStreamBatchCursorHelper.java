package cc.dreamcode.antylogout.libs.com.mongodb.internal.operation;

import java.util.Arrays;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoClientException;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoSocketException;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoCursorNotFoundException;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoNotPrimaryException;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoInterruptedException;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoChangeStreamException;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoException;
import java.util.List;

final class ChangeStreamBatchCursorHelper
{
    static final List<Integer> RETRYABLE_SERVER_ERROR_CODES;
    static final String RESUMABLE_CHANGE_STREAM_ERROR_LABEL = "ResumableChangeStreamError";
    
    static boolean isResumableError(final Throwable t, final int maxWireVersion) {
        if (!(t instanceof MongoException) || t instanceof MongoChangeStreamException || t instanceof MongoInterruptedException) {
            return false;
        }
        if (t instanceof MongoNotPrimaryException || t instanceof MongoCursorNotFoundException || (t instanceof MongoSocketException | t instanceof MongoClientException)) {
            return true;
        }
        if (maxWireVersion >= 9) {
            return ((MongoException)t).getErrorLabels().contains((Object)"ResumableChangeStreamError");
        }
        return ChangeStreamBatchCursorHelper.RETRYABLE_SERVER_ERROR_CODES.contains((Object)((MongoException)t).getCode());
    }
    
    private ChangeStreamBatchCursorHelper() {
    }
    
    static {
        RETRYABLE_SERVER_ERROR_CODES = Arrays.asList((Object[])new Integer[] { 6, 7, 63, 89, 91, 133, 150, 189, 234, 262, 9001, 10107, 11600, 11602, 13388, 13435, 13436 });
    }
}
