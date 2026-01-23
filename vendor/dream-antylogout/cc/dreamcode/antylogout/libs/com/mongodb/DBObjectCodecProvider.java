package cc.dreamcode.antylogout.libs.com.mongodb;

import cc.dreamcode.antylogout.libs.org.bson.codecs.DateCodec;
import java.util.Date;
import java.util.List;
import cc.dreamcode.antylogout.libs.org.bson.types.BSONTimestamp;
import cc.dreamcode.antylogout.libs.org.bson.codecs.Codec;
import cc.dreamcode.antylogout.libs.org.bson.codecs.configuration.CodecRegistry;
import cc.dreamcode.antylogout.libs.com.mongodb.assertions.Assertions;
import cc.dreamcode.antylogout.libs.org.bson.codecs.BsonTypeClassMap;
import cc.dreamcode.antylogout.libs.org.bson.codecs.configuration.CodecProvider;

public class DBObjectCodecProvider implements CodecProvider
{
    private final BsonTypeClassMap bsonTypeClassMap;
    
    public DBObjectCodecProvider() {
        this(DBObjectCodec.getDefaultBsonTypeClassMap());
    }
    
    public DBObjectCodecProvider(final BsonTypeClassMap bsonTypeClassMap) {
        this.bsonTypeClassMap = Assertions.notNull("bsonTypeClassMap", bsonTypeClassMap);
    }
    
    @Override
    public <T> Codec<T> get(final Class<T> clazz, final CodecRegistry registry) {
        if (clazz == BSONTimestamp.class) {
            return (Codec<T>)new BSONTimestampCodec();
        }
        if (DBObject.class.isAssignableFrom(clazz) && !List.class.isAssignableFrom(clazz)) {
            return (Codec<T>)new DBObjectCodec(registry, this.bsonTypeClassMap);
        }
        if (Date.class.isAssignableFrom(clazz)) {
            return (Codec<T>)new DateCodec();
        }
        return null;
    }
    
    @Override
    public boolean equals(final Object o) {
        return this == o || (o != null && this.getClass() == o.getClass());
    }
    
    @Override
    public int hashCode() {
        return this.getClass().hashCode();
    }
    
    @Override
    public String toString() {
        return "DBObjectCodecProvider{}";
    }
}
