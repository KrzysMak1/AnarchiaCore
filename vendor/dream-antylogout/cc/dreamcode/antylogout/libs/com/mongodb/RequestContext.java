package cc.dreamcode.antylogout.libs.com.mongodb;

import java.util.Map;
import java.util.stream.Stream;
import java.util.Optional;
import cc.dreamcode.antylogout.libs.com.mongodb.lang.Nullable;
import java.util.NoSuchElementException;

public interface RequestContext
{
     <T> T get(final Object p0);
    
    default <T> T get(final Class<T> key) {
        final T v = this.get((Object)key);
        if (key.isInstance(v)) {
            return v;
        }
        throw new NoSuchElementException("Context does not contain a value of type " + key.getName());
    }
    
    @Nullable
    default <T> T getOrDefault(final Object key, @Nullable final T defaultValue) {
        if (!this.hasKey(key)) {
            return defaultValue;
        }
        return this.get(key);
    }
    
    default <T> Optional<T> getOrEmpty(final Object key) {
        if (this.hasKey(key)) {
            return (Optional<T>)Optional.of(this.get(key));
        }
        return (Optional<T>)Optional.empty();
    }
    
    boolean hasKey(final Object p0);
    
    boolean isEmpty();
    
    void put(final Object p0, final Object p1);
    
    default void putNonNull(final Object key, @Nullable final Object valueOrNull) {
        if (valueOrNull != null) {
            this.put(key, valueOrNull);
        }
    }
    
    void delete(final Object p0);
    
    int size();
    
    Stream<Map.Entry<Object, Object>> stream();
}
