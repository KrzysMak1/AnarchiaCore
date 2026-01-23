package cc.dreamcode.antylogout.libs.com.mongodb;

import cc.dreamcode.antylogout.libs.org.bson.codecs.DecoderContext;
import cc.dreamcode.antylogout.libs.org.bson.BsonReader;
import cc.dreamcode.antylogout.libs.org.bson.codecs.EncoderContext;
import cc.dreamcode.antylogout.libs.org.bson.BsonWriter;
import cc.dreamcode.antylogout.libs.com.mongodb.assertions.Assertions;
import cc.dreamcode.antylogout.libs.org.bson.codecs.configuration.CodecRegistry;
import cc.dreamcode.antylogout.libs.org.bson.codecs.Codec;

public class DBRefCodec implements Codec<DBRef>
{
    private final CodecRegistry registry;
    
    public DBRefCodec(final CodecRegistry registry) {
        this.registry = Assertions.notNull("registry", registry);
    }
    
    @Override
    public void encode(final BsonWriter writer, final DBRef value, final EncoderContext encoderContext) {
        writer.writeStartDocument();
        writer.writeString("$ref", value.getCollectionName());
        writer.writeName("$id");
        final Codec codec = this.registry.get(value.getId().getClass());
        codec.encode(writer, value.getId(), encoderContext);
        if (value.getDatabaseName() != null) {
            writer.writeString("$db", value.getDatabaseName());
        }
        writer.writeEndDocument();
    }
    
    @Override
    public Class<DBRef> getEncoderClass() {
        return DBRef.class;
    }
    
    @Override
    public DBRef decode(final BsonReader reader, final DecoderContext decoderContext) {
        throw new UnsupportedOperationException("DBRefCodec does not support decoding");
    }
}
