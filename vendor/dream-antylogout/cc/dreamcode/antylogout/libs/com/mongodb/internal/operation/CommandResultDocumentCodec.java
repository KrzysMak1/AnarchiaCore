package cc.dreamcode.antylogout.libs.com.mongodb.internal.operation;

import cc.dreamcode.antylogout.libs.org.bson.codecs.Encoder;
import cc.dreamcode.antylogout.libs.org.bson.BsonDocumentWrapper;
import cc.dreamcode.antylogout.libs.org.bson.BsonType;
import cc.dreamcode.antylogout.libs.org.bson.BsonValue;
import cc.dreamcode.antylogout.libs.org.bson.codecs.DecoderContext;
import cc.dreamcode.antylogout.libs.org.bson.BsonReader;
import cc.dreamcode.antylogout.libs.org.bson.codecs.configuration.CodecRegistries;
import cc.dreamcode.antylogout.libs.org.bson.codecs.configuration.CodecProvider;
import java.util.Collections;
import cc.dreamcode.antylogout.libs.org.bson.BsonDocument;
import cc.dreamcode.antylogout.libs.org.bson.codecs.Codec;
import cc.dreamcode.antylogout.libs.org.bson.codecs.configuration.CodecRegistry;
import java.util.List;
import cc.dreamcode.antylogout.libs.org.bson.codecs.Decoder;
import cc.dreamcode.antylogout.libs.org.bson.codecs.BsonDocumentCodec;

class CommandResultDocumentCodec<T> extends BsonDocumentCodec
{
    private final Decoder<T> payloadDecoder;
    private final List<String> fieldsContainingPayload;
    
    CommandResultDocumentCodec(final CodecRegistry registry, final Decoder<T> payloadDecoder, final List<String> fieldsContainingPayload) {
        super(registry);
        this.payloadDecoder = payloadDecoder;
        this.fieldsContainingPayload = fieldsContainingPayload;
    }
    
    static <P> Codec<BsonDocument> create(final Decoder<P> decoder, final String fieldContainingPayload) {
        return create(decoder, (List<String>)Collections.singletonList((Object)fieldContainingPayload));
    }
    
    static <P> Codec<BsonDocument> create(final Decoder<P> decoder, final List<String> fieldsContainingPayload) {
        final CodecRegistry registry = CodecRegistries.fromProviders(new CommandResultCodecProvider<Object>(decoder, fieldsContainingPayload));
        return registry.get(BsonDocument.class);
    }
    
    @Override
    protected BsonValue readValue(final BsonReader reader, final DecoderContext decoderContext) {
        if (this.fieldsContainingPayload.contains((Object)reader.getCurrentName())) {
            if (reader.getCurrentBsonType() == BsonType.DOCUMENT) {
                return new BsonDocumentWrapper<Object>(this.payloadDecoder.decode(reader, decoderContext), null);
            }
            if (reader.getCurrentBsonType() == BsonType.ARRAY) {
                return new CommandResultArrayCodec(this.getCodecRegistry(), this.payloadDecoder).decode(reader, decoderContext);
            }
        }
        return super.readValue(reader, decoderContext);
    }
}
