package cc.dreamcode.antylogout.libs.com.mongodb;

import java.util.HashMap;
import java.util.Collections;
import cc.dreamcode.antylogout.libs.com.mongodb.lang.Nullable;
import cc.dreamcode.antylogout.libs.com.mongodb.assertions.Assertions;
import java.util.Map;

public final class MongoCompressor
{
    public static final String LEVEL = "LEVEL";
    private final String name;
    private final Map<String, Object> properties;
    
    public static MongoCompressor createSnappyCompressor() {
        return new MongoCompressor("snappy");
    }
    
    public static MongoCompressor createZlibCompressor() {
        return new MongoCompressor("zlib");
    }
    
    public static MongoCompressor createZstdCompressor() {
        return new MongoCompressor("zstd");
    }
    
    public String getName() {
        return this.name;
    }
    
    @Nullable
    public <T> T getProperty(final String key, final T defaultValue) {
        Assertions.notNull("key", key);
        final T value = (T)this.properties.get((Object)key.toLowerCase());
        return (value == null && !this.properties.containsKey((Object)key)) ? defaultValue : value;
    }
    
    public <T> T getPropertyNonNull(final String key, final T defaultValue) {
        final T value = (T)this.getProperty(key, (Object)defaultValue);
        if (value == null) {
            throw new IllegalArgumentException();
        }
        return value;
    }
    
    public <T> MongoCompressor withProperty(final String key, final T value) {
        return new MongoCompressor(this, key, (T)value);
    }
    
    private MongoCompressor(final String name) {
        this.name = name;
        this.properties = (Map<String, Object>)Collections.emptyMap();
    }
    
    private <T> MongoCompressor(final MongoCompressor from, final String propertyKey, final T propertyValue) {
        Assertions.notNull("propertyKey", propertyKey);
        this.name = from.name;
        (this.properties = (Map<String, Object>)new HashMap((Map)from.properties)).put((Object)propertyKey.toLowerCase(), (Object)propertyValue);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final MongoCompressor that = (MongoCompressor)o;
        return this.name.equals((Object)that.name) && this.properties.equals((Object)that.properties);
    }
    
    @Override
    public int hashCode() {
        int result = this.name.hashCode();
        result = 31 * result + this.properties.hashCode();
        return result;
    }
    
    @Override
    public String toString() {
        return "MongoCompressor{name='" + this.name + '\'' + ", properties=" + (Object)this.properties + '}';
    }
}
