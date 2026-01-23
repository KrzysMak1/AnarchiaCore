package cc.dreamcode.antylogout.libs.com.mongodb;

import java.util.Collection;
import java.util.HashSet;
import java.util.Arrays;
import cc.dreamcode.antylogout.libs.org.bson.codecs.pojo.annotations.BsonCreator;
import cc.dreamcode.antylogout.libs.org.bson.codecs.pojo.annotations.BsonProperty;
import cc.dreamcode.antylogout.libs.com.mongodb.assertions.Assertions;
import cc.dreamcode.antylogout.libs.org.bson.codecs.pojo.annotations.BsonIgnore;
import java.util.Set;
import cc.dreamcode.antylogout.libs.com.mongodb.annotations.Immutable;

@Immutable
public final class MongoNamespace
{
    public static final String COMMAND_COLLECTION_NAME = "$cmd";
    private static final Set<Character> PROHIBITED_CHARACTERS_IN_DATABASE_NAME;
    private final String databaseName;
    private final String collectionName;
    @BsonIgnore
    private final String fullName;
    
    public static void checkDatabaseNameValidity(final String databaseName) {
        Assertions.notNull("databaseName", databaseName);
        Assertions.isTrueArgument("databaseName is not empty", !databaseName.isEmpty());
        for (int i = 0; i < databaseName.length(); ++i) {
            if (MongoNamespace.PROHIBITED_CHARACTERS_IN_DATABASE_NAME.contains((Object)databaseName.charAt(i))) {
                throw new IllegalArgumentException("state should be: databaseName does not contain '" + databaseName.charAt(i) + "'");
            }
        }
    }
    
    public static void checkCollectionNameValidity(final String collectionName) {
        Assertions.notNull("collectionName", collectionName);
        Assertions.isTrueArgument("collectionName is not empty", !collectionName.isEmpty());
    }
    
    public MongoNamespace(final String fullName) {
        Assertions.notNull("fullName", fullName);
        this.fullName = fullName;
        this.databaseName = getDatatabaseNameFromFullName(fullName);
        this.collectionName = getCollectionNameFullName(fullName);
        checkDatabaseNameValidity(this.databaseName);
        checkCollectionNameValidity(this.collectionName);
    }
    
    @BsonCreator
    public MongoNamespace(@BsonProperty("db") final String databaseName, @BsonProperty("coll") final String collectionName) {
        checkDatabaseNameValidity(databaseName);
        checkCollectionNameValidity(collectionName);
        this.databaseName = databaseName;
        this.collectionName = collectionName;
        this.fullName = databaseName + '.' + collectionName;
    }
    
    @BsonProperty("db")
    public String getDatabaseName() {
        return this.databaseName;
    }
    
    @BsonProperty("coll")
    public String getCollectionName() {
        return this.collectionName;
    }
    
    public String getFullName() {
        return this.fullName;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final MongoNamespace that = (MongoNamespace)o;
        return this.collectionName.equals((Object)that.collectionName) && this.databaseName.equals((Object)that.databaseName);
    }
    
    @Override
    public String toString() {
        return this.fullName;
    }
    
    @Override
    public int hashCode() {
        int result = this.databaseName.hashCode();
        result = 31 * result + this.collectionName.hashCode();
        return result;
    }
    
    private static String getCollectionNameFullName(final String namespace) {
        final int firstDot = namespace.indexOf(46);
        if (firstDot == -1) {
            return namespace;
        }
        return namespace.substring(firstDot + 1);
    }
    
    private static String getDatatabaseNameFromFullName(final String namespace) {
        final int firstDot = namespace.indexOf(46);
        if (firstDot == -1) {
            return "";
        }
        return namespace.substring(0, firstDot);
    }
    
    static {
        PROHIBITED_CHARACTERS_IN_DATABASE_NAME = (Set)new HashSet((Collection)Arrays.asList((Object[])new Character[] { '\0', '/', '\\', ' ', '\"', '.' }));
    }
}
