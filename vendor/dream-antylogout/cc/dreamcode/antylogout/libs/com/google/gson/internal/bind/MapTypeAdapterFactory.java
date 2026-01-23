package cc.dreamcode.antylogout.libs.com.google.gson.internal.bind;

import cc.dreamcode.antylogout.libs.com.google.gson.JsonPrimitive;
import java.util.List;
import java.util.Iterator;
import cc.dreamcode.antylogout.libs.com.google.gson.internal.Streams;
import cc.dreamcode.antylogout.libs.com.google.gson.JsonElement;
import java.util.ArrayList;
import cc.dreamcode.antylogout.libs.com.google.gson.stream.JsonWriter;
import java.io.IOException;
import cc.dreamcode.antylogout.libs.com.google.gson.internal.JsonReaderInternalAccess;
import cc.dreamcode.antylogout.libs.com.google.gson.JsonSyntaxException;
import cc.dreamcode.antylogout.libs.com.google.gson.stream.JsonToken;
import cc.dreamcode.antylogout.libs.com.google.gson.stream.JsonReader;
import java.lang.reflect.Type;
import cc.dreamcode.antylogout.libs.com.google.gson.internal.ObjectConstructor;
import cc.dreamcode.antylogout.libs.com.google.gson.internal.$Gson$Types;
import java.util.Map;
import cc.dreamcode.antylogout.libs.com.google.gson.TypeAdapter;
import cc.dreamcode.antylogout.libs.com.google.gson.reflect.TypeToken;
import cc.dreamcode.antylogout.libs.com.google.gson.Gson;
import cc.dreamcode.antylogout.libs.com.google.gson.internal.ConstructorConstructor;
import cc.dreamcode.antylogout.libs.com.google.gson.TypeAdapterFactory;

public final class MapTypeAdapterFactory implements TypeAdapterFactory
{
    private final ConstructorConstructor constructorConstructor;
    final boolean complexMapKeySerialization;
    
    public MapTypeAdapterFactory(final ConstructorConstructor constructorConstructor, final boolean complexMapKeySerialization) {
        this.constructorConstructor = constructorConstructor;
        this.complexMapKeySerialization = complexMapKeySerialization;
    }
    
    @Override
    public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> typeToken) {
        final Type type = typeToken.getType();
        final Class<? super T> rawType = typeToken.getRawType();
        if (!Map.class.isAssignableFrom(rawType)) {
            return null;
        }
        final Type[] keyAndValueTypes = $Gson$Types.getMapKeyAndValueTypes(type, rawType);
        final TypeAdapter<?> keyAdapter = this.getKeyAdapter(gson, keyAndValueTypes[0]);
        final TypeAdapter<?> valueAdapter = gson.getAdapter(TypeToken.get(keyAndValueTypes[1]));
        final ObjectConstructor<T> constructor = this.constructorConstructor.get(typeToken);
        final TypeAdapter<T> result = (TypeAdapter<T>)new Adapter(gson, keyAndValueTypes[0], (TypeAdapter<Object>)keyAdapter, keyAndValueTypes[1], (TypeAdapter<Object>)valueAdapter, (ObjectConstructor<? extends java.util.Map<Object, Object>>)constructor);
        return result;
    }
    
    private TypeAdapter<?> getKeyAdapter(final Gson context, final Type keyType) {
        return (keyType == Boolean.TYPE || keyType == Boolean.class) ? TypeAdapters.BOOLEAN_AS_STRING : context.getAdapter(TypeToken.get(keyType));
    }
    
    private final class Adapter<K, V> extends TypeAdapter<Map<K, V>>
    {
        private final TypeAdapter<K> keyTypeAdapter;
        private final TypeAdapter<V> valueTypeAdapter;
        private final ObjectConstructor<? extends Map<K, V>> constructor;
        
        public Adapter(final Gson context, final Type keyType, final TypeAdapter<K> keyTypeAdapter, final Type valueType, final TypeAdapter<V> valueTypeAdapter, final ObjectConstructor<? extends Map<K, V>> constructor) {
            this.keyTypeAdapter = new TypeAdapterRuntimeTypeWrapper<K>(context, keyTypeAdapter, keyType);
            this.valueTypeAdapter = new TypeAdapterRuntimeTypeWrapper<V>(context, valueTypeAdapter, valueType);
            this.constructor = constructor;
        }
        
        @Override
        public Map<K, V> read(final JsonReader in) throws IOException {
            final JsonToken peek = in.peek();
            if (peek == JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            final Map<K, V> map = (Map<K, V>)this.constructor.construct();
            if (peek == JsonToken.BEGIN_ARRAY) {
                in.beginArray();
                while (in.hasNext()) {
                    in.beginArray();
                    final K key = this.keyTypeAdapter.read(in);
                    final V value = this.valueTypeAdapter.read(in);
                    final V replaced = (V)map.put((Object)key, (Object)value);
                    if (replaced != null) {
                        throw new JsonSyntaxException("duplicate key: " + (Object)key);
                    }
                    in.endArray();
                }
                in.endArray();
            }
            else {
                in.beginObject();
                while (in.hasNext()) {
                    JsonReaderInternalAccess.INSTANCE.promoteNameToValue(in);
                    final K key = this.keyTypeAdapter.read(in);
                    final V value = this.valueTypeAdapter.read(in);
                    final V replaced = (V)map.put((Object)key, (Object)value);
                    if (replaced != null) {
                        throw new JsonSyntaxException("duplicate key: " + (Object)key);
                    }
                }
                in.endObject();
            }
            return map;
        }
        
        @Override
        public void write(final JsonWriter out, final Map<K, V> map) throws IOException {
            if (map == null) {
                out.nullValue();
                return;
            }
            if (!MapTypeAdapterFactory.this.complexMapKeySerialization) {
                out.beginObject();
                for (final Map.Entry<K, V> entry : map.entrySet()) {
                    out.name(String.valueOf(entry.getKey()));
                    this.valueTypeAdapter.write(out, (V)entry.getValue());
                }
                out.endObject();
                return;
            }
            boolean hasComplexKeys = false;
            final List<JsonElement> keys = (List<JsonElement>)new ArrayList(map.size());
            final List<V> values = (List<V>)new ArrayList(map.size());
            for (final Map.Entry<K, V> entry2 : map.entrySet()) {
                final JsonElement keyElement = this.keyTypeAdapter.toJsonTree((K)entry2.getKey());
                keys.add((Object)keyElement);
                values.add(entry2.getValue());
                hasComplexKeys |= (keyElement.isJsonArray() || keyElement.isJsonObject());
            }
            if (hasComplexKeys) {
                out.beginArray();
                for (int i = 0, size = keys.size(); i < size; ++i) {
                    out.beginArray();
                    Streams.write((JsonElement)keys.get(i), out);
                    this.valueTypeAdapter.write(out, (V)values.get(i));
                    out.endArray();
                }
                out.endArray();
            }
            else {
                out.beginObject();
                for (int i = 0, size = keys.size(); i < size; ++i) {
                    final JsonElement keyElement = (JsonElement)keys.get(i);
                    out.name(this.keyToString(keyElement));
                    this.valueTypeAdapter.write(out, (V)values.get(i));
                }
                out.endObject();
            }
        }
        
        private String keyToString(final JsonElement keyElement) {
            if (keyElement.isJsonPrimitive()) {
                final JsonPrimitive primitive = keyElement.getAsJsonPrimitive();
                if (primitive.isNumber()) {
                    return String.valueOf((Object)primitive.getAsNumber());
                }
                if (primitive.isBoolean()) {
                    return Boolean.toString(primitive.getAsBoolean());
                }
                if (primitive.isString()) {
                    return primitive.getAsString();
                }
                throw new AssertionError();
            }
            else {
                if (keyElement.isJsonNull()) {
                    return "null";
                }
                throw new AssertionError();
            }
        }
    }
}
