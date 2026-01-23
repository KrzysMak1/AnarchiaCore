package cc.dreamcode.antylogout.libs.com.mongodb.internal.operation;

import cc.dreamcode.antylogout.libs.org.bson.BsonDocumentWrapper;
import java.util.List;
import cc.dreamcode.antylogout.libs.org.bson.BsonDocument;

final class BsonDocumentWrapperHelper
{
    static <T> List<T> toList(final BsonDocument result, final String fieldContainingWrappedArray) {
        return ((BsonArrayWrapper)result.getArray(fieldContainingWrappedArray)).getWrappedArray();
    }
    
    static <T> T toDocument(final BsonDocument document) {
        if (document == null) {
            return null;
        }
        return ((BsonDocumentWrapper)document).getWrappedDocument();
    }
    
    private BsonDocumentWrapperHelper() {
    }
}
