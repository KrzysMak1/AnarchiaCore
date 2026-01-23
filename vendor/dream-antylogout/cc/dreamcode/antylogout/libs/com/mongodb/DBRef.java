package cc.dreamcode.antylogout.libs.com.mongodb;

import java.util.Objects;
import cc.dreamcode.antylogout.libs.com.mongodb.assertions.Assertions;
import cc.dreamcode.antylogout.libs.com.mongodb.lang.Nullable;
import java.io.Serializable;

public class DBRef implements Serializable
{
    private static final long serialVersionUID = -849581217713362618L;
    private final Object id;
    private final String collectionName;
    private final String databaseName;
    
    public DBRef(final String collectionName, final Object id) {
        this(null, collectionName, id);
    }
    
    public DBRef(@Nullable final String databaseName, final String collectionName, final Object id) {
        this.id = Assertions.notNull("id", id);
        this.collectionName = Assertions.notNull("collectionName", collectionName);
        this.databaseName = databaseName;
    }
    
    public Object getId() {
        return this.id;
    }
    
    public String getCollectionName() {
        return this.collectionName;
    }
    
    @Nullable
    public String getDatabaseName() {
        return this.databaseName;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final DBRef dbRef = (DBRef)o;
        return this.id.equals(dbRef.id) && this.collectionName.equals((Object)dbRef.collectionName) && Objects.equals((Object)this.databaseName, (Object)dbRef.databaseName);
    }
    
    @Override
    public int hashCode() {
        int result = this.id.hashCode();
        result = 31 * result + this.collectionName.hashCode();
        result = 31 * result + ((this.databaseName != null) ? this.databaseName.hashCode() : 0);
        return result;
    }
    
    @Override
    public String toString() {
        return "{ \"$ref\" : \"" + this.collectionName + "\", \"$id\" : \"" + this.id + "\"" + ((this.databaseName == null) ? "" : (", \"$db\" : \"" + this.databaseName + "\"")) + " }";
    }
}
