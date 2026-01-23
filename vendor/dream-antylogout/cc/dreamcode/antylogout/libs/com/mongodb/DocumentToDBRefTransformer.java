package cc.dreamcode.antylogout.libs.com.mongodb;

import cc.dreamcode.antylogout.libs.org.bson.Document;
import cc.dreamcode.antylogout.libs.org.bson.Transformer;

public final class DocumentToDBRefTransformer implements Transformer
{
    @Override
    public Object transform(final Object value) {
        if (value instanceof Document) {
            final Document document = (Document)value;
            if (document.containsKey("$id") && document.containsKey("$ref")) {
                return new DBRef((String)document.get("$db"), (String)document.get("$ref"), document.get("$id"));
            }
        }
        return value;
    }
    
    @Override
    public boolean equals(final Object o) {
        return this == o || (o != null && this.getClass() == o.getClass());
    }
    
    @Override
    public int hashCode() {
        return 0;
    }
}
