package cc.dreamcode.antylogout.libs.com.mongodb;

import cc.dreamcode.antylogout.libs.org.bson.BsonValue;
import cc.dreamcode.antylogout.libs.org.bson.BsonString;
import cc.dreamcode.antylogout.libs.org.bson.BsonDocument;
import cc.dreamcode.antylogout.libs.com.mongodb.lang.Nullable;
import cc.dreamcode.antylogout.libs.com.mongodb.assertions.Assertions;

public final class ReadConcern
{
    private final ReadConcernLevel level;
    public static final ReadConcern DEFAULT;
    public static final ReadConcern LOCAL;
    public static final ReadConcern MAJORITY;
    public static final ReadConcern LINEARIZABLE;
    public static final ReadConcern SNAPSHOT;
    public static final ReadConcern AVAILABLE;
    
    public ReadConcern(final ReadConcernLevel level) {
        this.level = Assertions.notNull("level", level);
    }
    
    @Nullable
    public ReadConcernLevel getLevel() {
        return this.level;
    }
    
    public boolean isServerDefault() {
        return this.level == null;
    }
    
    public BsonDocument asDocument() {
        final BsonDocument readConcern = new BsonDocument();
        if (this.level != null) {
            readConcern.put("level", new BsonString(this.level.getValue()));
        }
        return readConcern;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final ReadConcern that = (ReadConcern)o;
        return this.level == that.level;
    }
    
    @Override
    public int hashCode() {
        return (this.level != null) ? this.level.hashCode() : 0;
    }
    
    @Override
    public String toString() {
        return "ReadConcern{level=" + (Object)this.level + '}';
    }
    
    private ReadConcern() {
        this.level = null;
    }
    
    static {
        DEFAULT = new ReadConcern();
        LOCAL = new ReadConcern(ReadConcernLevel.LOCAL);
        MAJORITY = new ReadConcern(ReadConcernLevel.MAJORITY);
        LINEARIZABLE = new ReadConcern(ReadConcernLevel.LINEARIZABLE);
        SNAPSHOT = new ReadConcern(ReadConcernLevel.SNAPSHOT);
        AVAILABLE = new ReadConcern(ReadConcernLevel.AVAILABLE);
    }
}
