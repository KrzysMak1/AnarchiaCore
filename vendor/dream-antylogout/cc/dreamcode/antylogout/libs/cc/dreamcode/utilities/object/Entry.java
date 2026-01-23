package cc.dreamcode.antylogout.libs.cc.dreamcode.utilities.object;

import java.util.function.Consumer;
import java.util.Map;

public class Entry<K, V> implements Map.Entry<K, V>
{
    private final K key;
    private V value;
    
    public Entry(final K key) {
        this.key = key;
    }
    
    public Entry(final K key, final V value) {
        this.key = key;
        this.value = value;
    }
    
    public Entry<K, V> then(final Consumer<Entry<K, V>> after) {
        after.accept((Object)this);
        return this;
    }
    
    public K getKey() {
        return this.key;
    }
    
    public V getValue() {
        return this.value;
    }
    
    public V setValue(final V value) {
        return this.value = value;
    }
    
    public static <K, V> Entry<K, V> of(final K key, final V value) {
        return new Entry<K, V>(key, value);
    }
}
