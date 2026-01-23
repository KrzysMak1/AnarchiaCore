package cc.dreamcode.antylogout.libs.com.mongodb.internal.operation;

import cc.dreamcode.antylogout.libs.org.bson.BsonInt64;
import cc.dreamcode.antylogout.libs.org.bson.BsonInt32;
import cc.dreamcode.antylogout.libs.org.bson.BsonString;
import cc.dreamcode.antylogout.libs.com.mongodb.lang.Nullable;
import cc.dreamcode.antylogout.libs.org.bson.BsonValue;
import cc.dreamcode.antylogout.libs.org.bson.BsonBoolean;
import cc.dreamcode.antylogout.libs.org.bson.BsonDocument;

final class DocumentHelper
{
    private DocumentHelper() {
    }
    
    static void putIfTrue(final BsonDocument command, final String key, final boolean condition) {
        if (condition) {
            command.put(key, BsonBoolean.TRUE);
        }
    }
    
    static void putIfFalse(final BsonDocument command, final String key, final boolean condition) {
        if (!condition) {
            command.put(key, BsonBoolean.FALSE);
        }
    }
    
    static void putIfNotNullOrEmpty(final BsonDocument command, final String key, @Nullable final BsonDocument documentValue) {
        if (documentValue != null && !documentValue.isEmpty()) {
            command.put(key, documentValue);
        }
    }
    
    static void putIfNotNull(final BsonDocument command, final String key, @Nullable final BsonValue value) {
        if (value != null) {
            command.put(key, value);
        }
    }
    
    static void putIfNotNull(final BsonDocument command, final String key, @Nullable final String value) {
        if (value != null) {
            command.put(key, new BsonString(value));
        }
    }
    
    static void putIfNotZero(final BsonDocument command, final String key, final int value) {
        if (value != 0) {
            command.put(key, new BsonInt32(value));
        }
    }
    
    static void putIfNotZero(final BsonDocument command, final String key, final long value) {
        if (value != 0L) {
            command.put(key, new BsonInt64(value));
        }
    }
}
