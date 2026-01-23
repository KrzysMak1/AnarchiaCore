package cc.dreamcode.antylogout.libs.com.mongodb;

import cc.dreamcode.antylogout.libs.org.bson.codecs.DecoderContext;
import cc.dreamcode.antylogout.libs.org.bson.BsonReader;
import cc.dreamcode.antylogout.libs.org.bson.BsonTimestamp;
import cc.dreamcode.antylogout.libs.org.bson.codecs.EncoderContext;
import cc.dreamcode.antylogout.libs.org.bson.BsonWriter;
import cc.dreamcode.antylogout.libs.org.bson.types.BSONTimestamp;
import cc.dreamcode.antylogout.libs.org.bson.codecs.Codec;

public class BSONTimestampCodec implements Codec<BSONTimestamp>
{
    @Override
    public void encode(final BsonWriter writer, final BSONTimestamp value, final EncoderContext encoderContext) {
        writer.writeTimestamp(new BsonTimestamp(value.getTime(), value.getInc()));
    }
    
    @Override
    public BSONTimestamp decode(final BsonReader reader, final DecoderContext decoderContext) {
        final BsonTimestamp timestamp = reader.readTimestamp();
        return new BSONTimestamp(timestamp.getTime(), timestamp.getInc());
    }
    
    @Override
    public Class<BSONTimestamp> getEncoderClass() {
        return BSONTimestamp.class;
    }
}
