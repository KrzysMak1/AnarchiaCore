package cc.dreamcode.antylogout.libs.com.mongodb.internal.operation;

import cc.dreamcode.antylogout.libs.org.bson.codecs.BsonJavaScriptWithScopeCodec;
import cc.dreamcode.antylogout.libs.org.bson.codecs.BsonDocumentCodec;
import cc.dreamcode.antylogout.libs.org.bson.codecs.BsonUndefinedCodec;
import cc.dreamcode.antylogout.libs.org.bson.codecs.BsonTimestampCodec;
import cc.dreamcode.antylogout.libs.org.bson.codecs.BsonSymbolCodec;
import cc.dreamcode.antylogout.libs.org.bson.codecs.BsonStringCodec;
import cc.dreamcode.antylogout.libs.org.bson.codecs.BsonRegularExpressionCodec;
import cc.dreamcode.antylogout.libs.org.bson.codecs.BsonObjectIdCodec;
import cc.dreamcode.antylogout.libs.org.bson.codecs.BsonJavaScriptCodec;
import cc.dreamcode.antylogout.libs.org.bson.codecs.BsonMaxKeyCodec;
import cc.dreamcode.antylogout.libs.org.bson.codecs.BsonMinKeyCodec;
import cc.dreamcode.antylogout.libs.org.bson.codecs.BsonDecimal128Codec;
import cc.dreamcode.antylogout.libs.org.bson.codecs.BsonInt64Codec;
import cc.dreamcode.antylogout.libs.org.bson.codecs.BsonInt32Codec;
import cc.dreamcode.antylogout.libs.org.bson.codecs.BsonDoubleCodec;
import cc.dreamcode.antylogout.libs.org.bson.codecs.BsonDBPointerCodec;
import cc.dreamcode.antylogout.libs.org.bson.codecs.BsonDateTimeCodec;
import cc.dreamcode.antylogout.libs.org.bson.codecs.BsonBooleanCodec;
import cc.dreamcode.antylogout.libs.org.bson.codecs.BsonBinaryCodec;
import cc.dreamcode.antylogout.libs.org.bson.BsonValue;
import cc.dreamcode.antylogout.libs.org.bson.codecs.BsonNullCodec;
import cc.dreamcode.antylogout.libs.org.bson.BsonDocument;
import cc.dreamcode.antylogout.libs.org.bson.codecs.BsonArrayCodec;
import cc.dreamcode.antylogout.libs.org.bson.BsonArray;
import cc.dreamcode.antylogout.libs.org.bson.codecs.configuration.CodecRegistry;
import java.util.HashMap;
import java.util.List;
import cc.dreamcode.antylogout.libs.org.bson.codecs.Decoder;
import cc.dreamcode.antylogout.libs.org.bson.codecs.Codec;
import java.util.Map;
import cc.dreamcode.antylogout.libs.org.bson.codecs.configuration.CodecProvider;

class CommandResultCodecProvider<P> implements CodecProvider
{
    private final Map<Class<?>, Codec<?>> codecs;
    private final Decoder<P> payloadDecoder;
    private final List<String> fieldsContainingPayload;
    
    CommandResultCodecProvider(final Decoder<P> payloadDecoder, final List<String> fieldContainingPayload) {
        this.codecs = (Map<Class<?>, Codec<?>>)new HashMap();
        this.payloadDecoder = payloadDecoder;
        this.fieldsContainingPayload = fieldContainingPayload;
        this.addCodecs();
    }
    
    @Override
    public <T> Codec<T> get(final Class<T> clazz, final CodecRegistry registry) {
        if (this.codecs.containsKey((Object)clazz)) {
            return (Codec)this.codecs.get((Object)clazz);
        }
        if (clazz == BsonArray.class) {
            return (Codec<T>)new BsonArrayCodec(registry);
        }
        if (clazz == BsonDocument.class) {
            return (Codec<T>)new CommandResultDocumentCodec(registry, (Decoder<Object>)this.payloadDecoder, this.fieldsContainingPayload);
        }
        return null;
    }
    
    private void addCodecs() {
        this.addCodec((Codec<BsonValue>)new BsonNullCodec());
        this.addCodec((Codec<BsonValue>)new BsonBinaryCodec());
        this.addCodec((Codec<BsonValue>)new BsonBooleanCodec());
        this.addCodec((Codec<BsonValue>)new BsonDateTimeCodec());
        this.addCodec((Codec<BsonValue>)new BsonDBPointerCodec());
        this.addCodec((Codec<BsonValue>)new BsonDoubleCodec());
        this.addCodec((Codec<BsonValue>)new BsonInt32Codec());
        this.addCodec((Codec<BsonValue>)new BsonInt64Codec());
        this.addCodec((Codec<BsonValue>)new BsonDecimal128Codec());
        this.addCodec((Codec<BsonValue>)new BsonMinKeyCodec());
        this.addCodec((Codec<BsonValue>)new BsonMaxKeyCodec());
        this.addCodec((Codec<BsonValue>)new BsonJavaScriptCodec());
        this.addCodec((Codec<BsonValue>)new BsonObjectIdCodec());
        this.addCodec((Codec<BsonValue>)new BsonRegularExpressionCodec());
        this.addCodec((Codec<BsonValue>)new BsonStringCodec());
        this.addCodec((Codec<BsonValue>)new BsonSymbolCodec());
        this.addCodec((Codec<BsonValue>)new BsonTimestampCodec());
        this.addCodec((Codec<BsonValue>)new BsonUndefinedCodec());
        this.addCodec((Codec<BsonValue>)new BsonJavaScriptWithScopeCodec(new BsonDocumentCodec()));
    }
    
    private <T extends BsonValue> void addCodec(final Codec<T> codec) {
        this.codecs.put((Object)codec.getEncoderClass(), (Object)codec);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final CommandResultCodecProvider<?> that = (CommandResultCodecProvider<?>)o;
        return this.fieldsContainingPayload.equals((Object)that.fieldsContainingPayload) && this.payloadDecoder.getClass().equals(that.payloadDecoder.getClass());
    }
    
    @Override
    public int hashCode() {
        int result = this.payloadDecoder.getClass().hashCode();
        result = 31 * result + this.fieldsContainingPayload.hashCode();
        return result;
    }
}
