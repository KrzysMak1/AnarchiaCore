package cc.dreamcode.antylogout.libs.com.mongodb;

import cc.dreamcode.antylogout.libs.com.mongodb.assertions.Assertions;
import cc.dreamcode.antylogout.libs.org.bson.BsonDocument;
import cc.dreamcode.antylogout.libs.com.mongodb.annotations.Beta;

@Beta({ Beta.Reason.SERVER })
public final class MongoUpdatedEncryptedFieldsException extends MongoClientException
{
    private static final long serialVersionUID = 1L;
    private final BsonDocument encryptedFields;
    
    public MongoUpdatedEncryptedFieldsException(final BsonDocument encryptedFields, final String msg, final Throwable cause) {
        super(msg, Assertions.assertNotNull(cause));
        this.encryptedFields = Assertions.assertNotNull(encryptedFields);
    }
    
    public BsonDocument getEncryptedFields() {
        return this.encryptedFields;
    }
}
