package cc.dreamcode.antylogout.libs.com.mongodb;

import cc.dreamcode.antylogout.libs.org.bson.codecs.configuration.CodecRegistries;
import cc.dreamcode.antylogout.libs.org.bson.UuidRepresentation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeSet;
import java.util.LinkedHashMap;
import java.util.List;
import cc.dreamcode.antylogout.libs.org.bson.types.BasicBSONList;
import cc.dreamcode.antylogout.libs.org.bson.BsonDocumentWrapper;
import cc.dreamcode.antylogout.libs.org.bson.BsonDocument;
import cc.dreamcode.antylogout.libs.org.bson.codecs.configuration.CodecRegistry;
import java.util.Iterator;
import cc.dreamcode.antylogout.libs.org.bson.io.OutputBuffer;
import cc.dreamcode.antylogout.libs.org.bson.io.BsonOutput;
import cc.dreamcode.antylogout.libs.org.bson.BsonBinaryWriter;
import cc.dreamcode.antylogout.libs.org.bson.io.BasicOutputBuffer;
import java.util.Arrays;
import cc.dreamcode.antylogout.libs.org.bson.BSONObject;
import cc.dreamcode.antylogout.libs.org.bson.BsonWriter;
import cc.dreamcode.antylogout.libs.org.bson.codecs.EncoderContext;
import java.io.Writer;
import cc.dreamcode.antylogout.libs.org.bson.json.JsonWriter;
import java.io.StringWriter;
import cc.dreamcode.antylogout.libs.org.bson.codecs.Encoder;
import cc.dreamcode.antylogout.libs.org.bson.json.JsonMode;
import cc.dreamcode.antylogout.libs.org.bson.json.JsonWriterSettings;
import java.util.Map;
import cc.dreamcode.antylogout.libs.org.bson.BsonReader;
import cc.dreamcode.antylogout.libs.org.bson.codecs.DecoderContext;
import cc.dreamcode.antylogout.libs.org.bson.json.JsonReader;
import cc.dreamcode.antylogout.libs.org.bson.codecs.Decoder;
import cc.dreamcode.antylogout.libs.org.bson.codecs.Codec;
import cc.dreamcode.antylogout.libs.org.bson.conversions.Bson;
import cc.dreamcode.antylogout.libs.org.bson.BasicBSONObject;

public class BasicDBObject extends BasicBSONObject implements DBObject, Bson
{
    private static final long serialVersionUID = -4415279469780082174L;
    private static final Codec<BasicDBObject> DEFAULT_CODEC;
    private boolean isPartialObject;
    
    public static BasicDBObject parse(final String json) {
        return parse(json, BasicDBObject.DEFAULT_CODEC);
    }
    
    public static BasicDBObject parse(final String json, final Decoder<BasicDBObject> decoder) {
        return decoder.decode(new JsonReader(json), DecoderContext.builder().build());
    }
    
    public BasicDBObject() {
    }
    
    public BasicDBObject(final int size) {
        super(size);
    }
    
    public BasicDBObject(final String key, final Object value) {
        super(key, value);
    }
    
    public BasicDBObject(final Map map) {
        super(map);
    }
    
    @Override
    public BasicDBObject append(final String key, final Object val) {
        this.put((Object)key, val);
        return this;
    }
    
    @Override
    public boolean isPartialObject() {
        return this.isPartialObject;
    }
    
    public String toJson() {
        return this.toJson(JsonWriterSettings.builder().outputMode(JsonMode.RELAXED).build());
    }
    
    public String toJson(final JsonWriterSettings writerSettings) {
        return this.toJson(writerSettings, BasicDBObject.DEFAULT_CODEC);
    }
    
    public String toJson(final Encoder<BasicDBObject> encoder) {
        return this.toJson(JsonWriterSettings.builder().outputMode(JsonMode.RELAXED).build(), encoder);
    }
    
    public String toJson(final JsonWriterSettings writerSettings, final Encoder<BasicDBObject> encoder) {
        final JsonWriter writer = new JsonWriter((Writer)new StringWriter(), writerSettings);
        encoder.encode(writer, this, EncoderContext.builder().build());
        return writer.getWriter().toString();
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof BSONObject)) {
            return false;
        }
        final BSONObject other = (BSONObject)o;
        return this.keySet().equals((Object)other.keySet()) && Arrays.equals(toBson(canonicalizeBSONObject(this)), toBson(canonicalizeBSONObject(other)));
    }
    
    @Override
    public int hashCode() {
        return Arrays.hashCode(toBson(canonicalizeBSONObject(this)));
    }
    
    private static byte[] toBson(final BasicDBObject dbObject) {
        final OutputBuffer outputBuffer = new BasicOutputBuffer();
        BasicDBObject.DEFAULT_CODEC.encode(new BsonBinaryWriter(outputBuffer), dbObject, EncoderContext.builder().build());
        return outputBuffer.toByteArray();
    }
    
    public String toString() {
        return this.toJson();
    }
    
    @Override
    public void markAsPartialObject() {
        this.isPartialObject = true;
    }
    
    public Object copy() {
        final BasicDBObject newCopy = new BasicDBObject(this.toMap());
        for (final String field : this.keySet()) {
            final Object val = this.get(field);
            if (val instanceof BasicDBObject) {
                newCopy.put((Object)field, ((BasicDBObject)val).copy());
            }
            else {
                if (!(val instanceof BasicDBList)) {
                    continue;
                }
                newCopy.put((Object)field, ((BasicDBList)val).copy());
            }
        }
        return newCopy;
    }
    
    @Override
    public <TDocument> BsonDocument toBsonDocument(final Class<TDocument> documentClass, final CodecRegistry codecRegistry) {
        return new BsonDocumentWrapper<Object>(this, codecRegistry.get(BasicDBObject.class));
    }
    
    private static Object canonicalize(final Object from) {
        if (from instanceof BSONObject && !(from instanceof BasicBSONList)) {
            return canonicalizeBSONObject((BSONObject)from);
        }
        if (from instanceof List) {
            return canonicalizeList((List<Object>)from);
        }
        if (from instanceof Map) {
            return canonicalizeMap((Map<String, Object>)from);
        }
        return from;
    }
    
    private static Map<String, Object> canonicalizeMap(final Map<String, Object> from) {
        final Map<String, Object> canonicalized = (Map<String, Object>)new LinkedHashMap(from.size());
        final TreeSet<String> keysInOrder = (TreeSet<String>)new TreeSet((Collection)from.keySet());
        for (final String key : keysInOrder) {
            final Object val = from.get((Object)key);
            canonicalized.put((Object)key, canonicalize(val));
        }
        return canonicalized;
    }
    
    private static BasicDBObject canonicalizeBSONObject(final BSONObject from) {
        final BasicDBObject canonicalized = new BasicDBObject();
        final TreeSet<String> keysInOrder = (TreeSet<String>)new TreeSet((Collection)from.keySet());
        for (final String key : keysInOrder) {
            final Object val = from.get(key);
            canonicalized.put((Object)key, canonicalize(val));
        }
        return canonicalized;
    }
    
    private static List canonicalizeList(final List<Object> list) {
        final List<Object> canonicalized = (List<Object>)new ArrayList(list.size());
        for (final Object cur : list) {
            canonicalized.add(canonicalize(cur));
        }
        return canonicalized;
    }
    
    static {
        DEFAULT_CODEC = CodecRegistries.withUuidRepresentation(DBObjectCodec.getDefaultRegistry(), UuidRepresentation.STANDARD).get(BasicDBObject.class);
    }
}
