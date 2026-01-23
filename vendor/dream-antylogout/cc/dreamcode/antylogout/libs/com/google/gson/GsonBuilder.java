package cc.dreamcode.antylogout.libs.com.google.gson;

import cc.dreamcode.antylogout.libs.com.google.gson.internal.bind.DefaultDateTypeAdapter;
import cc.dreamcode.antylogout.libs.com.google.gson.internal.sql.SqlTypesSupport;
import java.util.Collections;
import cc.dreamcode.antylogout.libs.com.google.gson.internal.bind.TypeAdapters;
import cc.dreamcode.antylogout.libs.com.google.gson.internal.bind.TreeTypeAdapter;
import cc.dreamcode.antylogout.libs.com.google.gson.reflect.TypeToken;
import cc.dreamcode.antylogout.libs.com.google.gson.internal.$Gson$Preconditions;
import java.util.Collection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.lang.reflect.Type;
import java.util.Map;
import cc.dreamcode.antylogout.libs.com.google.gson.internal.Excluder;

public final class GsonBuilder
{
    private Excluder excluder;
    private LongSerializationPolicy longSerializationPolicy;
    private FieldNamingStrategy fieldNamingPolicy;
    private final Map<Type, InstanceCreator<?>> instanceCreators;
    private final List<TypeAdapterFactory> factories;
    private final List<TypeAdapterFactory> hierarchyFactories;
    private boolean serializeNulls;
    private String datePattern;
    private int dateStyle;
    private int timeStyle;
    private boolean complexMapKeySerialization;
    private boolean serializeSpecialFloatingPointValues;
    private boolean escapeHtmlChars;
    private boolean prettyPrinting;
    private boolean generateNonExecutableJson;
    private boolean lenient;
    private boolean useJdkUnsafe;
    private ToNumberStrategy objectToNumberStrategy;
    private ToNumberStrategy numberToNumberStrategy;
    private final LinkedList<ReflectionAccessFilter> reflectionFilters;
    
    public GsonBuilder() {
        this.excluder = Excluder.DEFAULT;
        this.longSerializationPolicy = LongSerializationPolicy.DEFAULT;
        this.fieldNamingPolicy = FieldNamingPolicy.IDENTITY;
        this.instanceCreators = (Map<Type, InstanceCreator<?>>)new HashMap();
        this.factories = (List<TypeAdapterFactory>)new ArrayList();
        this.hierarchyFactories = (List<TypeAdapterFactory>)new ArrayList();
        this.serializeNulls = false;
        this.datePattern = Gson.DEFAULT_DATE_PATTERN;
        this.dateStyle = 2;
        this.timeStyle = 2;
        this.complexMapKeySerialization = false;
        this.serializeSpecialFloatingPointValues = false;
        this.escapeHtmlChars = true;
        this.prettyPrinting = false;
        this.generateNonExecutableJson = false;
        this.lenient = false;
        this.useJdkUnsafe = true;
        this.objectToNumberStrategy = Gson.DEFAULT_OBJECT_TO_NUMBER_STRATEGY;
        this.numberToNumberStrategy = Gson.DEFAULT_NUMBER_TO_NUMBER_STRATEGY;
        this.reflectionFilters = (LinkedList<ReflectionAccessFilter>)new LinkedList();
    }
    
    GsonBuilder(final Gson gson) {
        this.excluder = Excluder.DEFAULT;
        this.longSerializationPolicy = LongSerializationPolicy.DEFAULT;
        this.fieldNamingPolicy = FieldNamingPolicy.IDENTITY;
        this.instanceCreators = (Map<Type, InstanceCreator<?>>)new HashMap();
        this.factories = (List<TypeAdapterFactory>)new ArrayList();
        this.hierarchyFactories = (List<TypeAdapterFactory>)new ArrayList();
        this.serializeNulls = false;
        this.datePattern = Gson.DEFAULT_DATE_PATTERN;
        this.dateStyle = 2;
        this.timeStyle = 2;
        this.complexMapKeySerialization = false;
        this.serializeSpecialFloatingPointValues = false;
        this.escapeHtmlChars = true;
        this.prettyPrinting = false;
        this.generateNonExecutableJson = false;
        this.lenient = false;
        this.useJdkUnsafe = true;
        this.objectToNumberStrategy = Gson.DEFAULT_OBJECT_TO_NUMBER_STRATEGY;
        this.numberToNumberStrategy = Gson.DEFAULT_NUMBER_TO_NUMBER_STRATEGY;
        this.reflectionFilters = (LinkedList<ReflectionAccessFilter>)new LinkedList();
        this.excluder = gson.excluder;
        this.fieldNamingPolicy = gson.fieldNamingStrategy;
        this.instanceCreators.putAll((Map)gson.instanceCreators);
        this.serializeNulls = gson.serializeNulls;
        this.complexMapKeySerialization = gson.complexMapKeySerialization;
        this.generateNonExecutableJson = gson.generateNonExecutableJson;
        this.escapeHtmlChars = gson.htmlSafe;
        this.prettyPrinting = gson.prettyPrinting;
        this.lenient = gson.lenient;
        this.serializeSpecialFloatingPointValues = gson.serializeSpecialFloatingPointValues;
        this.longSerializationPolicy = gson.longSerializationPolicy;
        this.datePattern = gson.datePattern;
        this.dateStyle = gson.dateStyle;
        this.timeStyle = gson.timeStyle;
        this.factories.addAll((Collection)gson.builderFactories);
        this.hierarchyFactories.addAll((Collection)gson.builderHierarchyFactories);
        this.useJdkUnsafe = gson.useJdkUnsafe;
        this.objectToNumberStrategy = gson.objectToNumberStrategy;
        this.numberToNumberStrategy = gson.numberToNumberStrategy;
        this.reflectionFilters.addAll((Collection)gson.reflectionFilters);
    }
    
    public GsonBuilder setVersion(final double ignoreVersionsAfter) {
        this.excluder = this.excluder.withVersion(ignoreVersionsAfter);
        return this;
    }
    
    public GsonBuilder excludeFieldsWithModifiers(final int... modifiers) {
        this.excluder = this.excluder.withModifiers(modifiers);
        return this;
    }
    
    public GsonBuilder generateNonExecutableJson() {
        this.generateNonExecutableJson = true;
        return this;
    }
    
    public GsonBuilder excludeFieldsWithoutExposeAnnotation() {
        this.excluder = this.excluder.excludeFieldsWithoutExposeAnnotation();
        return this;
    }
    
    public GsonBuilder serializeNulls() {
        this.serializeNulls = true;
        return this;
    }
    
    public GsonBuilder enableComplexMapKeySerialization() {
        this.complexMapKeySerialization = true;
        return this;
    }
    
    public GsonBuilder disableInnerClassSerialization() {
        this.excluder = this.excluder.disableInnerClassSerialization();
        return this;
    }
    
    public GsonBuilder setLongSerializationPolicy(final LongSerializationPolicy serializationPolicy) {
        this.longSerializationPolicy = serializationPolicy;
        return this;
    }
    
    public GsonBuilder setFieldNamingPolicy(final FieldNamingPolicy namingConvention) {
        this.fieldNamingPolicy = namingConvention;
        return this;
    }
    
    public GsonBuilder setFieldNamingStrategy(final FieldNamingStrategy fieldNamingStrategy) {
        this.fieldNamingPolicy = fieldNamingStrategy;
        return this;
    }
    
    public GsonBuilder setObjectToNumberStrategy(final ToNumberStrategy objectToNumberStrategy) {
        this.objectToNumberStrategy = objectToNumberStrategy;
        return this;
    }
    
    public GsonBuilder setNumberToNumberStrategy(final ToNumberStrategy numberToNumberStrategy) {
        this.numberToNumberStrategy = numberToNumberStrategy;
        return this;
    }
    
    public GsonBuilder setExclusionStrategies(final ExclusionStrategy... strategies) {
        for (final ExclusionStrategy strategy : strategies) {
            this.excluder = this.excluder.withExclusionStrategy(strategy, true, true);
        }
        return this;
    }
    
    public GsonBuilder addSerializationExclusionStrategy(final ExclusionStrategy strategy) {
        this.excluder = this.excluder.withExclusionStrategy(strategy, true, false);
        return this;
    }
    
    public GsonBuilder addDeserializationExclusionStrategy(final ExclusionStrategy strategy) {
        this.excluder = this.excluder.withExclusionStrategy(strategy, false, true);
        return this;
    }
    
    public GsonBuilder setPrettyPrinting() {
        this.prettyPrinting = true;
        return this;
    }
    
    public GsonBuilder setLenient() {
        this.lenient = true;
        return this;
    }
    
    public GsonBuilder disableHtmlEscaping() {
        this.escapeHtmlChars = false;
        return this;
    }
    
    public GsonBuilder setDateFormat(final String pattern) {
        this.datePattern = pattern;
        return this;
    }
    
    public GsonBuilder setDateFormat(final int style) {
        this.dateStyle = style;
        this.datePattern = null;
        return this;
    }
    
    public GsonBuilder setDateFormat(final int dateStyle, final int timeStyle) {
        this.dateStyle = dateStyle;
        this.timeStyle = timeStyle;
        this.datePattern = null;
        return this;
    }
    
    public GsonBuilder registerTypeAdapter(final Type type, final Object typeAdapter) {
        $Gson$Preconditions.checkArgument(typeAdapter instanceof JsonSerializer || typeAdapter instanceof JsonDeserializer || typeAdapter instanceof InstanceCreator || typeAdapter instanceof TypeAdapter);
        if (typeAdapter instanceof InstanceCreator) {
            this.instanceCreators.put((Object)type, (Object)typeAdapter);
        }
        if (typeAdapter instanceof JsonSerializer || typeAdapter instanceof JsonDeserializer) {
            final TypeToken<?> typeToken = TypeToken.get(type);
            this.factories.add((Object)TreeTypeAdapter.newFactoryWithMatchRawType(typeToken, typeAdapter));
        }
        if (typeAdapter instanceof TypeAdapter) {
            this.factories.add((Object)TypeAdapters.newFactory(TypeToken.get(type), (TypeAdapter<?>)typeAdapter));
        }
        return this;
    }
    
    public GsonBuilder registerTypeAdapterFactory(final TypeAdapterFactory factory) {
        this.factories.add((Object)factory);
        return this;
    }
    
    public GsonBuilder registerTypeHierarchyAdapter(final Class<?> baseType, final Object typeAdapter) {
        $Gson$Preconditions.checkArgument(typeAdapter instanceof JsonSerializer || typeAdapter instanceof JsonDeserializer || typeAdapter instanceof TypeAdapter);
        if (typeAdapter instanceof JsonDeserializer || typeAdapter instanceof JsonSerializer) {
            this.hierarchyFactories.add((Object)TreeTypeAdapter.newTypeHierarchyFactory(baseType, typeAdapter));
        }
        if (typeAdapter instanceof TypeAdapter) {
            this.factories.add((Object)TypeAdapters.newTypeHierarchyFactory(baseType, (TypeAdapter<?>)typeAdapter));
        }
        return this;
    }
    
    public GsonBuilder serializeSpecialFloatingPointValues() {
        this.serializeSpecialFloatingPointValues = true;
        return this;
    }
    
    public GsonBuilder disableJdkUnsafe() {
        this.useJdkUnsafe = false;
        return this;
    }
    
    public GsonBuilder addReflectionAccessFilter(final ReflectionAccessFilter filter) {
        if (filter == null) {
            throw new NullPointerException();
        }
        this.reflectionFilters.addFirst((Object)filter);
        return this;
    }
    
    public Gson create() {
        final List<TypeAdapterFactory> factories = (List<TypeAdapterFactory>)new ArrayList(this.factories.size() + this.hierarchyFactories.size() + 3);
        factories.addAll((Collection)this.factories);
        Collections.reverse((List)factories);
        final List<TypeAdapterFactory> hierarchyFactories = (List<TypeAdapterFactory>)new ArrayList((Collection)this.hierarchyFactories);
        Collections.reverse((List)hierarchyFactories);
        factories.addAll((Collection)hierarchyFactories);
        this.addTypeAdaptersForDate(this.datePattern, this.dateStyle, this.timeStyle, factories);
        return new Gson(this.excluder, this.fieldNamingPolicy, (Map<Type, InstanceCreator<?>>)new HashMap((Map)this.instanceCreators), this.serializeNulls, this.complexMapKeySerialization, this.generateNonExecutableJson, this.escapeHtmlChars, this.prettyPrinting, this.lenient, this.serializeSpecialFloatingPointValues, this.useJdkUnsafe, this.longSerializationPolicy, this.datePattern, this.dateStyle, this.timeStyle, (List<TypeAdapterFactory>)new ArrayList((Collection)this.factories), (List<TypeAdapterFactory>)new ArrayList((Collection)this.hierarchyFactories), factories, this.objectToNumberStrategy, this.numberToNumberStrategy, (List<ReflectionAccessFilter>)new ArrayList((Collection)this.reflectionFilters));
    }
    
    private void addTypeAdaptersForDate(final String datePattern, final int dateStyle, final int timeStyle, final List<TypeAdapterFactory> factories) {
        final boolean sqlTypesSupported = SqlTypesSupport.SUPPORTS_SQL_TYPES;
        TypeAdapterFactory sqlTimestampAdapterFactory = null;
        TypeAdapterFactory sqlDateAdapterFactory = null;
        TypeAdapterFactory dateAdapterFactory;
        if (datePattern != null && !datePattern.trim().isEmpty()) {
            dateAdapterFactory = DefaultDateTypeAdapter.DateType.DATE.createAdapterFactory(datePattern);
            if (sqlTypesSupported) {
                sqlTimestampAdapterFactory = SqlTypesSupport.TIMESTAMP_DATE_TYPE.createAdapterFactory(datePattern);
                sqlDateAdapterFactory = SqlTypesSupport.DATE_DATE_TYPE.createAdapterFactory(datePattern);
            }
        }
        else {
            if (dateStyle == 2 || timeStyle == 2) {
                return;
            }
            dateAdapterFactory = DefaultDateTypeAdapter.DateType.DATE.createAdapterFactory(dateStyle, timeStyle);
            if (sqlTypesSupported) {
                sqlTimestampAdapterFactory = SqlTypesSupport.TIMESTAMP_DATE_TYPE.createAdapterFactory(dateStyle, timeStyle);
                sqlDateAdapterFactory = SqlTypesSupport.DATE_DATE_TYPE.createAdapterFactory(dateStyle, timeStyle);
            }
        }
        factories.add((Object)dateAdapterFactory);
        if (sqlTypesSupported) {
            factories.add((Object)sqlTimestampAdapterFactory);
            factories.add((Object)sqlDateAdapterFactory);
        }
    }
}
