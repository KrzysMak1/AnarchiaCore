package cc.dreamcode.antylogout.libs.com.mongodb;

import cc.dreamcode.antylogout.libs.org.bson.codecs.configuration.CodecRegistries;
import java.util.Arrays;
import cc.dreamcode.antylogout.libs.org.bson.codecs.BsonValueCodecProvider;
import cc.dreamcode.antylogout.libs.org.bson.codecs.ValueCodecProvider;
import cc.dreamcode.antylogout.libs.org.bson.codecs.configuration.CodecProvider;
import java.util.UUID;
import cc.dreamcode.antylogout.libs.org.bson.types.Binary;
import cc.dreamcode.antylogout.libs.org.bson.BsonBinarySubType;
import cc.dreamcode.antylogout.libs.org.bson.BsonDbPointer;
import java.lang.reflect.Array;
import cc.dreamcode.antylogout.libs.org.bson.BsonBinary;
import cc.dreamcode.antylogout.libs.org.bson.codecs.Encoder;
import cc.dreamcode.antylogout.libs.org.bson.types.Symbol;
import cc.dreamcode.antylogout.libs.org.bson.types.CodeWScope;
import cc.dreamcode.antylogout.libs.org.bson.BSONObject;
import cc.dreamcode.antylogout.libs.com.mongodb.lang.Nullable;
import cc.dreamcode.antylogout.libs.org.bson.codecs.Codec;
import cc.dreamcode.antylogout.libs.org.bson.BsonDocumentWriter;
import cc.dreamcode.antylogout.libs.org.bson.BsonDocument;
import cc.dreamcode.antylogout.libs.org.bson.BsonValue;
import java.util.List;
import java.util.ArrayList;
import cc.dreamcode.antylogout.libs.org.bson.codecs.DecoderContext;
import cc.dreamcode.antylogout.libs.org.bson.BsonReader;
import java.util.Iterator;
import cc.dreamcode.antylogout.libs.org.bson.codecs.EncoderContext;
import cc.dreamcode.antylogout.libs.org.bson.BsonWriter;
import cc.dreamcode.antylogout.libs.org.bson.codecs.ObjectIdGenerator;
import cc.dreamcode.antylogout.libs.com.mongodb.assertions.Assertions;
import java.util.Map;
import cc.dreamcode.antylogout.libs.org.bson.types.BSONTimestamp;
import java.util.regex.Pattern;
import cc.dreamcode.antylogout.libs.org.bson.BsonType;
import java.util.HashMap;
import cc.dreamcode.antylogout.libs.org.bson.UuidRepresentation;
import cc.dreamcode.antylogout.libs.org.bson.codecs.IdGenerator;
import cc.dreamcode.antylogout.libs.org.bson.codecs.BsonTypeCodecMap;
import cc.dreamcode.antylogout.libs.org.bson.codecs.configuration.CodecRegistry;
import cc.dreamcode.antylogout.libs.org.bson.codecs.BsonTypeClassMap;
import cc.dreamcode.antylogout.libs.org.bson.codecs.OverridableUuidRepresentationCodec;
import cc.dreamcode.antylogout.libs.org.bson.codecs.CollectibleCodec;

public class DBObjectCodec implements CollectibleCodec<DBObject>, OverridableUuidRepresentationCodec<DBObject>
{
    private static final BsonTypeClassMap DEFAULT_BSON_TYPE_CLASS_MAP;
    private static final CodecRegistry DEFAULT_REGISTRY;
    private static final String ID_FIELD_NAME = "_id";
    private final CodecRegistry codecRegistry;
    private final BsonTypeCodecMap bsonTypeCodecMap;
    private final DBObjectFactory objectFactory;
    private final IdGenerator idGenerator;
    private final UuidRepresentation uuidRepresentation;
    
    private static BsonTypeClassMap createDefaultBsonTypeClassMap() {
        final Map<BsonType, Class<?>> replacements = (Map<BsonType, Class<?>>)new HashMap();
        replacements.put((Object)BsonType.REGULAR_EXPRESSION, (Object)Pattern.class);
        replacements.put((Object)BsonType.SYMBOL, (Object)String.class);
        replacements.put((Object)BsonType.TIMESTAMP, (Object)BSONTimestamp.class);
        replacements.put((Object)BsonType.JAVASCRIPT_WITH_SCOPE, (Object)null);
        replacements.put((Object)BsonType.DOCUMENT, (Object)null);
        return new BsonTypeClassMap(replacements);
    }
    
    static BsonTypeClassMap getDefaultBsonTypeClassMap() {
        return DBObjectCodec.DEFAULT_BSON_TYPE_CLASS_MAP;
    }
    
    static CodecRegistry getDefaultRegistry() {
        return DBObjectCodec.DEFAULT_REGISTRY;
    }
    
    public DBObjectCodec() {
        this(DBObjectCodec.DEFAULT_REGISTRY);
    }
    
    public DBObjectCodec(final CodecRegistry codecRegistry) {
        this(codecRegistry, DBObjectCodec.DEFAULT_BSON_TYPE_CLASS_MAP);
    }
    
    public DBObjectCodec(final CodecRegistry codecRegistry, final BsonTypeClassMap bsonTypeClassMap) {
        this(codecRegistry, bsonTypeClassMap, new BasicDBObjectFactory());
    }
    
    public DBObjectCodec(final CodecRegistry codecRegistry, final BsonTypeClassMap bsonTypeClassMap, final DBObjectFactory objectFactory) {
        this(codecRegistry, new BsonTypeCodecMap(Assertions.notNull("bsonTypeClassMap", bsonTypeClassMap), codecRegistry), objectFactory, UuidRepresentation.UNSPECIFIED);
    }
    
    private DBObjectCodec(final CodecRegistry codecRegistry, final BsonTypeCodecMap bsonTypeCodecMap, final DBObjectFactory objectFactory, final UuidRepresentation uuidRepresentation) {
        this.idGenerator = new ObjectIdGenerator();
        this.objectFactory = Assertions.notNull("objectFactory", objectFactory);
        this.codecRegistry = Assertions.notNull("codecRegistry", codecRegistry);
        this.uuidRepresentation = Assertions.notNull("uuidRepresentation", uuidRepresentation);
        this.bsonTypeCodecMap = bsonTypeCodecMap;
    }
    
    @Override
    public void encode(final BsonWriter writer, final DBObject document, final EncoderContext encoderContext) {
        writer.writeStartDocument();
        this.beforeFields(writer, encoderContext, document);
        for (final String key : document.keySet()) {
            if (this.skipField(encoderContext, key)) {
                continue;
            }
            writer.writeName(key);
            this.writeValue(writer, encoderContext, document.get(key));
        }
        writer.writeEndDocument();
    }
    
    @Override
    public DBObject decode(final BsonReader reader, final DecoderContext decoderContext) {
        final List<String> path = (List<String>)new ArrayList(10);
        return this.readDocument(reader, decoderContext, path);
    }
    
    @Override
    public Class<DBObject> getEncoderClass() {
        return DBObject.class;
    }
    
    @Override
    public boolean documentHasId(final DBObject document) {
        return document.containsField("_id");
    }
    
    @Override
    public BsonValue getDocumentId(final DBObject document) {
        if (!this.documentHasId(document)) {
            throw new IllegalStateException("The document does not contain an _id");
        }
        final Object id = document.get("_id");
        if (id instanceof BsonValue) {
            return (BsonValue)id;
        }
        final BsonDocument idHoldingDocument = new BsonDocument();
        final BsonWriter writer = new BsonDocumentWriter(idHoldingDocument);
        writer.writeStartDocument();
        writer.writeName("_id");
        this.writeValue(writer, EncoderContext.builder().build(), id);
        writer.writeEndDocument();
        return idHoldingDocument.get("_id");
    }
    
    @Override
    public DBObject generateIdIfAbsentFromDocument(final DBObject document) {
        if (!this.documentHasId(document)) {
            document.put("_id", this.idGenerator.generate());
        }
        return document;
    }
    
    @Override
    public Codec<DBObject> withUuidRepresentation(final UuidRepresentation uuidRepresentation) {
        if (this.uuidRepresentation.equals((Object)uuidRepresentation)) {
            return this;
        }
        return new DBObjectCodec(this.codecRegistry, this.bsonTypeCodecMap, this.objectFactory, uuidRepresentation);
    }
    
    private void beforeFields(final BsonWriter bsonWriter, final EncoderContext encoderContext, final DBObject document) {
        if (encoderContext.isEncodingCollectibleDocument() && document.containsField("_id")) {
            bsonWriter.writeName("_id");
            this.writeValue(bsonWriter, encoderContext, document.get("_id"));
        }
    }
    
    private boolean skipField(final EncoderContext encoderContext, final String key) {
        return encoderContext.isEncodingCollectibleDocument() && key.equals((Object)"_id");
    }
    
    private void writeValue(final BsonWriter bsonWriter, final EncoderContext encoderContext, @Nullable final Object value) {
        if (value == null) {
            bsonWriter.writeNull();
        }
        else if (value instanceof DBRef) {
            this.encodeDBRef(bsonWriter, (DBRef)value, encoderContext);
        }
        else if (value instanceof Map) {
            this.encodeMap(bsonWriter, (Map<String, Object>)value, encoderContext);
        }
        else if (value instanceof Iterable) {
            this.encodeIterable(bsonWriter, (Iterable)value, encoderContext);
        }
        else if (value instanceof BSONObject) {
            this.encodeBsonObject(bsonWriter, (BSONObject)value, encoderContext);
        }
        else if (value instanceof CodeWScope) {
            this.encodeCodeWScope(bsonWriter, (CodeWScope)value, encoderContext);
        }
        else if (value instanceof byte[]) {
            this.encodeByteArray(bsonWriter, (byte[])value);
        }
        else if (value.getClass().isArray()) {
            this.encodeArray(bsonWriter, value, encoderContext);
        }
        else if (value instanceof Symbol) {
            bsonWriter.writeSymbol(((Symbol)value).getSymbol());
        }
        else {
            final Codec codec = this.codecRegistry.get(value.getClass());
            encoderContext.encodeWithChildContext(codec, bsonWriter, value);
        }
    }
    
    private void encodeMap(final BsonWriter bsonWriter, final Map<String, Object> document, final EncoderContext encoderContext) {
        bsonWriter.writeStartDocument();
        for (final Map.Entry<String, Object> entry : document.entrySet()) {
            bsonWriter.writeName((String)entry.getKey());
            this.writeValue(bsonWriter, encoderContext.getChildContext(), entry.getValue());
        }
        bsonWriter.writeEndDocument();
    }
    
    private void encodeBsonObject(final BsonWriter bsonWriter, final BSONObject document, final EncoderContext encoderContext) {
        bsonWriter.writeStartDocument();
        for (final String key : document.keySet()) {
            bsonWriter.writeName(key);
            this.writeValue(bsonWriter, encoderContext.getChildContext(), document.get(key));
        }
        bsonWriter.writeEndDocument();
    }
    
    private void encodeByteArray(final BsonWriter bsonWriter, final byte[] value) {
        bsonWriter.writeBinaryData(new BsonBinary(value));
    }
    
    private void encodeArray(final BsonWriter bsonWriter, final Object value, final EncoderContext encoderContext) {
        bsonWriter.writeStartArray();
        for (int size = Array.getLength(value), i = 0; i < size; ++i) {
            this.writeValue(bsonWriter, encoderContext.getChildContext(), Array.get(value, i));
        }
        bsonWriter.writeEndArray();
    }
    
    private void encodeDBRef(final BsonWriter bsonWriter, final DBRef dbRef, final EncoderContext encoderContext) {
        bsonWriter.writeStartDocument();
        bsonWriter.writeString("$ref", dbRef.getCollectionName());
        bsonWriter.writeName("$id");
        this.writeValue(bsonWriter, encoderContext.getChildContext(), dbRef.getId());
        if (dbRef.getDatabaseName() != null) {
            bsonWriter.writeString("$db", dbRef.getDatabaseName());
        }
        bsonWriter.writeEndDocument();
    }
    
    private void encodeCodeWScope(final BsonWriter bsonWriter, final CodeWScope value, final EncoderContext encoderContext) {
        bsonWriter.writeJavaScriptWithScope(value.getCode());
        this.encodeBsonObject(bsonWriter, value.getScope(), encoderContext.getChildContext());
    }
    
    private void encodeIterable(final BsonWriter bsonWriter, final Iterable iterable, final EncoderContext encoderContext) {
        bsonWriter.writeStartArray();
        for (final Object cur : iterable) {
            this.writeValue(bsonWriter, encoderContext.getChildContext(), cur);
        }
        bsonWriter.writeEndArray();
    }
    
    @Nullable
    private Object readValue(final BsonReader reader, final DecoderContext decoderContext, @Nullable final String fieldName, final List<String> path) {
        final BsonType bsonType = reader.getCurrentBsonType();
        if (bsonType.isContainer() && fieldName != null) {
            path.add((Object)fieldName);
        }
        Object initialRetVal = null;
        switch (bsonType) {
            case DOCUMENT: {
                initialRetVal = this.verifyForDBRef(this.readDocument(reader, decoderContext, path));
                break;
            }
            case ARRAY: {
                initialRetVal = this.readArray(reader, decoderContext, path);
                break;
            }
            case JAVASCRIPT_WITH_SCOPE: {
                initialRetVal = this.readCodeWScope(reader, decoderContext, path);
                break;
            }
            case DB_POINTER: {
                final BsonDbPointer dbPointer = reader.readDBPointer();
                initialRetVal = new DBRef(dbPointer.getNamespace(), dbPointer.getId());
                break;
            }
            case BINARY: {
                initialRetVal = this.readBinary(reader, decoderContext);
                break;
            }
            case NULL: {
                reader.readNull();
                initialRetVal = null;
                break;
            }
            default: {
                initialRetVal = this.bsonTypeCodecMap.get(bsonType).decode(reader, decoderContext);
                break;
            }
        }
        if (bsonType.isContainer() && fieldName != null) {
            path.remove((Object)fieldName);
        }
        return initialRetVal;
    }
    
    private Object readBinary(final BsonReader reader, final DecoderContext decoderContext) {
        final byte bsonBinarySubType = reader.peekBinarySubType();
        Codec<?> codec;
        if (BsonBinarySubType.isUuid(bsonBinarySubType) && reader.peekBinarySize() == 16) {
            codec = this.codecRegistry.get((Class<?>)Binary.class);
            switch (bsonBinarySubType) {
                case 3: {
                    if (this.uuidRepresentation == UuidRepresentation.JAVA_LEGACY || this.uuidRepresentation == UuidRepresentation.C_SHARP_LEGACY || this.uuidRepresentation == UuidRepresentation.PYTHON_LEGACY) {
                        codec = this.codecRegistry.get((Class<?>)UUID.class);
                        break;
                    }
                    break;
                }
                case 4: {
                    if (this.uuidRepresentation == UuidRepresentation.STANDARD) {
                        codec = this.codecRegistry.get((Class<?>)UUID.class);
                        break;
                    }
                    break;
                }
                default: {
                    throw new UnsupportedOperationException("Unknown UUID binary subtype " + (int)bsonBinarySubType);
                }
            }
        }
        else if (bsonBinarySubType == BsonBinarySubType.BINARY.getValue() || bsonBinarySubType == BsonBinarySubType.OLD_BINARY.getValue()) {
            codec = this.codecRegistry.get((Class<?>)byte[].class);
        }
        else {
            codec = this.codecRegistry.get((Class<?>)Binary.class);
        }
        return codec.decode(reader, decoderContext);
    }
    
    private List readArray(final BsonReader reader, final DecoderContext decoderContext, final List<String> path) {
        reader.readStartArray();
        final BasicDBList list = new BasicDBList();
        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            list.add(this.readValue(reader, decoderContext, null, path));
        }
        reader.readEndArray();
        return (List)list;
    }
    
    private DBObject readDocument(final BsonReader reader, final DecoderContext decoderContext, final List<String> path) {
        final DBObject document = this.objectFactory.getInstance(path);
        reader.readStartDocument();
        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            final String fieldName = reader.readName();
            document.put(fieldName, this.readValue(reader, decoderContext, fieldName, path));
        }
        reader.readEndDocument();
        return document;
    }
    
    private CodeWScope readCodeWScope(final BsonReader reader, final DecoderContext decoderContext, final List<String> path) {
        return new CodeWScope(reader.readJavaScriptWithScope(), this.readDocument(reader, decoderContext, path));
    }
    
    private Object verifyForDBRef(final DBObject document) {
        if (document.containsField("$ref") && document.containsField("$id")) {
            return new DBRef((String)document.get("$db"), (String)document.get("$ref"), document.get("$id"));
        }
        return document;
    }
    
    static {
        DEFAULT_BSON_TYPE_CLASS_MAP = createDefaultBsonTypeClassMap();
        DEFAULT_REGISTRY = CodecRegistries.fromProviders((List<? extends CodecProvider>)Arrays.asList((Object[])new CodecProvider[] { new ValueCodecProvider(), new BsonValueCodecProvider(), new DBObjectCodecProvider() }));
    }
}
