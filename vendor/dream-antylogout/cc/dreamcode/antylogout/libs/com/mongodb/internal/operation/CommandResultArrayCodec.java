package cc.dreamcode.antylogout.libs.com.mongodb.internal.operation;

import cc.dreamcode.antylogout.libs.org.bson.codecs.Encoder;
import cc.dreamcode.antylogout.libs.org.bson.BsonDocumentWrapper;
import cc.dreamcode.antylogout.libs.org.bson.BsonValue;
import java.util.List;
import cc.dreamcode.antylogout.libs.org.bson.BsonType;
import java.util.ArrayList;
import cc.dreamcode.antylogout.libs.org.bson.BsonArray;
import cc.dreamcode.antylogout.libs.org.bson.codecs.DecoderContext;
import cc.dreamcode.antylogout.libs.org.bson.BsonReader;
import cc.dreamcode.antylogout.libs.org.bson.codecs.configuration.CodecRegistry;
import cc.dreamcode.antylogout.libs.org.bson.codecs.Decoder;
import cc.dreamcode.antylogout.libs.org.bson.codecs.BsonArrayCodec;

class CommandResultArrayCodec<T> extends BsonArrayCodec
{
    private final Decoder<T> decoder;
    
    CommandResultArrayCodec(final CodecRegistry registry, final Decoder<T> decoder) {
        super(registry);
        this.decoder = decoder;
    }
    
    @Override
    public BsonArray decode(final BsonReader reader, final DecoderContext decoderContext) {
        reader.readStartArray();
        final List<T> list = (List<T>)new ArrayList();
        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            if (reader.getCurrentBsonType() == BsonType.NULL) {
                reader.readNull();
                list.add((Object)null);
            }
            else {
                list.add((Object)this.decoder.decode(reader, decoderContext));
            }
        }
        reader.readEndArray();
        return new BsonArrayWrapper<Object>(list);
    }
    
    @Override
    protected BsonValue readValue(final BsonReader reader, final DecoderContext decoderContext) {
        if (reader.getCurrentBsonType() == BsonType.DOCUMENT) {
            return new BsonDocumentWrapper<Object>(this.decoder.decode(reader, decoderContext), null);
        }
        return super.readValue(reader, decoderContext);
    }
}
