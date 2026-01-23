package cc.dreamcode.antylogout.libs.com.google.gson.internal.bind;

import java.util.Iterator;
import cc.dreamcode.antylogout.libs.com.google.gson.JsonSyntaxException;
import cc.dreamcode.antylogout.libs.com.google.gson.stream.JsonToken;
import cc.dreamcode.antylogout.libs.com.google.gson.internal.$Gson$Types;
import cc.dreamcode.antylogout.libs.com.google.gson.internal.reflect.ReflectionHelper;
import java.util.LinkedHashMap;
import java.util.Map;
import cc.dreamcode.antylogout.libs.com.google.gson.stream.JsonReader;
import java.io.IOException;
import cc.dreamcode.antylogout.libs.com.google.gson.stream.JsonWriter;
import cc.dreamcode.antylogout.libs.com.google.gson.annotations.JsonAdapter;
import java.lang.reflect.Type;
import cc.dreamcode.antylogout.libs.com.google.gson.internal.Primitives;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Modifier;
import cc.dreamcode.antylogout.libs.com.google.gson.internal.ObjectConstructor;
import cc.dreamcode.antylogout.libs.com.google.gson.JsonIOException;
import cc.dreamcode.antylogout.libs.com.google.gson.internal.ReflectionAccessFilterHelper;
import cc.dreamcode.antylogout.libs.com.google.gson.TypeAdapter;
import cc.dreamcode.antylogout.libs.com.google.gson.reflect.TypeToken;
import cc.dreamcode.antylogout.libs.com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Collections;
import cc.dreamcode.antylogout.libs.com.google.gson.annotations.SerializedName;
import java.lang.reflect.Field;
import cc.dreamcode.antylogout.libs.com.google.gson.ReflectionAccessFilter;
import java.util.List;
import cc.dreamcode.antylogout.libs.com.google.gson.internal.Excluder;
import cc.dreamcode.antylogout.libs.com.google.gson.FieldNamingStrategy;
import cc.dreamcode.antylogout.libs.com.google.gson.internal.ConstructorConstructor;
import cc.dreamcode.antylogout.libs.com.google.gson.TypeAdapterFactory;

public final class ReflectiveTypeAdapterFactory implements TypeAdapterFactory
{
    private final ConstructorConstructor constructorConstructor;
    private final FieldNamingStrategy fieldNamingPolicy;
    private final Excluder excluder;
    private final JsonAdapterAnnotationTypeAdapterFactory jsonAdapterFactory;
    private final List<ReflectionAccessFilter> reflectionFilters;
    
    public ReflectiveTypeAdapterFactory(final ConstructorConstructor constructorConstructor, final FieldNamingStrategy fieldNamingPolicy, final Excluder excluder, final JsonAdapterAnnotationTypeAdapterFactory jsonAdapterFactory, final List<ReflectionAccessFilter> reflectionFilters) {
        this.constructorConstructor = constructorConstructor;
        this.fieldNamingPolicy = fieldNamingPolicy;
        this.excluder = excluder;
        this.jsonAdapterFactory = jsonAdapterFactory;
        this.reflectionFilters = reflectionFilters;
    }
    
    private boolean includeField(final Field f, final boolean serialize) {
        return !this.excluder.excludeClass(f.getType(), serialize) && !this.excluder.excludeField(f, serialize);
    }
    
    private List<String> getFieldNames(final Field f) {
        final SerializedName annotation = (SerializedName)f.getAnnotation((Class)SerializedName.class);
        if (annotation == null) {
            final String name = this.fieldNamingPolicy.translateName(f);
            return (List<String>)Collections.singletonList((Object)name);
        }
        final String serializedName = annotation.value();
        final String[] alternates = annotation.alternate();
        if (alternates.length == 0) {
            return (List<String>)Collections.singletonList((Object)serializedName);
        }
        final List<String> fieldNames = (List<String>)new ArrayList(alternates.length + 1);
        fieldNames.add((Object)serializedName);
        for (final String alternate : alternates) {
            fieldNames.add((Object)alternate);
        }
        return fieldNames;
    }
    
    @Override
    public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> type) {
        final Class<? super T> raw = type.getRawType();
        if (!Object.class.isAssignableFrom(raw)) {
            return null;
        }
        final ReflectionAccessFilter.FilterResult filterResult = ReflectionAccessFilterHelper.getFilterResult(this.reflectionFilters, raw);
        if (filterResult == ReflectionAccessFilter.FilterResult.BLOCK_ALL) {
            throw new JsonIOException("ReflectionAccessFilter does not permit using reflection for " + (Object)raw + ". Register a TypeAdapter for this type or adjust the access filter.");
        }
        final boolean blockInaccessible = filterResult == ReflectionAccessFilter.FilterResult.BLOCK_INACCESSIBLE;
        final ObjectConstructor<T> constructor = this.constructorConstructor.get(type);
        return new Adapter<T>(constructor, this.getBoundFields(gson, type, raw, blockInaccessible));
    }
    
    private static void checkAccessible(final Object object, final Field field) {
        if (!ReflectionAccessFilterHelper.canAccess((AccessibleObject)field, Modifier.isStatic(field.getModifiers()) ? null : object)) {
            throw new JsonIOException("Field '" + field.getDeclaringClass().getName() + "#" + field.getName() + "' is not accessible and ReflectionAccessFilter does not permit making it accessible. Register a TypeAdapter for the declaring type or adjust the access filter.");
        }
    }
    
    private BoundField createBoundField(final Gson context, final Field field, final String name, final TypeToken<?> fieldType, final boolean serialize, final boolean deserialize, final boolean blockInaccessible) {
        final boolean isPrimitive = Primitives.isPrimitive((Type)fieldType.getRawType());
        final JsonAdapter annotation = (JsonAdapter)field.getAnnotation((Class)JsonAdapter.class);
        TypeAdapter<?> mapped = null;
        if (annotation != null) {
            mapped = this.jsonAdapterFactory.getTypeAdapter(this.constructorConstructor, context, fieldType, annotation);
        }
        final boolean jsonAdapterPresent = mapped != null;
        if (mapped == null) {
            mapped = context.getAdapter(fieldType);
        }
        final TypeAdapter<?> typeAdapter = mapped;
        return new BoundField(name, serialize, deserialize) {
            @Override
            void write(final JsonWriter writer, final Object value) throws IOException, IllegalAccessException {
                if (!this.serialized) {
                    return;
                }
                if (blockInaccessible) {
                    checkAccessible(value, field);
                }
                final Object fieldValue = field.get(value);
                if (fieldValue == value) {
                    return;
                }
                writer.name(this.name);
                final TypeAdapter t = jsonAdapterPresent ? typeAdapter : new TypeAdapterRuntimeTypeWrapper(context, typeAdapter, fieldType.getType());
                t.write(writer, fieldValue);
            }
            
            @Override
            void read(final JsonReader reader, final Object value) throws IOException, IllegalAccessException {
                final Object fieldValue = typeAdapter.read(reader);
                if (fieldValue != null || !isPrimitive) {
                    if (blockInaccessible) {
                        checkAccessible(value, field);
                    }
                    field.set(value, fieldValue);
                }
            }
        };
    }
    
    private Map<String, BoundField> getBoundFields(final Gson context, TypeToken<?> type, Class<?> raw, boolean blockInaccessible) {
        final Map<String, BoundField> result = (Map<String, BoundField>)new LinkedHashMap();
        if (raw.isInterface()) {
            return result;
        }
        final Type declaredType = type.getType();
        final Class<?> originalRaw = raw;
        while (raw != Object.class) {
            final Field[] fields = raw.getDeclaredFields();
            if (raw != originalRaw && fields.length > 0) {
                final ReflectionAccessFilter.FilterResult filterResult = ReflectionAccessFilterHelper.getFilterResult(this.reflectionFilters, raw);
                if (filterResult == ReflectionAccessFilter.FilterResult.BLOCK_ALL) {
                    throw new JsonIOException("ReflectionAccessFilter does not permit using reflection for " + (Object)raw + " (supertype of " + (Object)originalRaw + "). Register a TypeAdapter for this type or adjust the access filter.");
                }
                blockInaccessible = (filterResult == ReflectionAccessFilter.FilterResult.BLOCK_INACCESSIBLE);
            }
            for (final Field field : fields) {
                boolean serialize = this.includeField(field, true);
                final boolean deserialize = this.includeField(field, false);
                if (serialize || deserialize) {
                    if (!blockInaccessible) {
                        ReflectionHelper.makeAccessible(field);
                    }
                    final Type fieldType = $Gson$Types.resolve(type.getType(), raw, field.getGenericType());
                    final List<String> fieldNames = this.getFieldNames(field);
                    BoundField previous = null;
                    for (int i = 0, size = fieldNames.size(); i < size; ++i) {
                        final String name = (String)fieldNames.get(i);
                        if (i != 0) {
                            serialize = false;
                        }
                        final BoundField boundField = this.createBoundField(context, field, name, TypeToken.get(fieldType), serialize, deserialize, blockInaccessible);
                        final BoundField replaced = (BoundField)result.put((Object)name, (Object)boundField);
                        if (previous == null) {
                            previous = replaced;
                        }
                    }
                    if (previous != null) {
                        throw new IllegalArgumentException((Object)declaredType + " declares multiple JSON fields named " + previous.name);
                    }
                }
            }
            type = TypeToken.get($Gson$Types.resolve(type.getType(), raw, raw.getGenericSuperclass()));
            raw = type.getRawType();
        }
        return result;
    }
    
    abstract static class BoundField
    {
        final String name;
        final boolean serialized;
        final boolean deserialized;
        
        protected BoundField(final String name, final boolean serialized, final boolean deserialized) {
            this.name = name;
            this.serialized = serialized;
            this.deserialized = deserialized;
        }
        
        abstract void write(final JsonWriter p0, final Object p1) throws IOException, IllegalAccessException;
        
        abstract void read(final JsonReader p0, final Object p1) throws IOException, IllegalAccessException;
    }
    
    public static final class Adapter<T> extends TypeAdapter<T>
    {
        private final ObjectConstructor<T> constructor;
        private final Map<String, BoundField> boundFields;
        
        Adapter(final ObjectConstructor<T> constructor, final Map<String, BoundField> boundFields) {
            this.constructor = constructor;
            this.boundFields = boundFields;
        }
        
        @Override
        public T read(final JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            final T instance = this.constructor.construct();
            try {
                in.beginObject();
                while (in.hasNext()) {
                    final String name = in.nextName();
                    final BoundField field = (BoundField)this.boundFields.get((Object)name);
                    if (field == null || !field.deserialized) {
                        in.skipValue();
                    }
                    else {
                        field.read(in, instance);
                    }
                }
            }
            catch (final IllegalStateException e) {
                throw new JsonSyntaxException((Throwable)e);
            }
            catch (final IllegalAccessException e2) {
                throw ReflectionHelper.createExceptionForUnexpectedIllegalAccess(e2);
            }
            in.endObject();
            return instance;
        }
        
        @Override
        public void write(final JsonWriter out, final T value) throws IOException {
            if (value == null) {
                out.nullValue();
                return;
            }
            out.beginObject();
            try {
                for (final BoundField boundField : this.boundFields.values()) {
                    boundField.write(out, value);
                }
            }
            catch (final IllegalAccessException e) {
                throw ReflectionHelper.createExceptionForUnexpectedIllegalAccess(e);
            }
            out.endObject();
        }
    }
}
