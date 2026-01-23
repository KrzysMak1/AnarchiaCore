package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.profiles.lock;

import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
final class FinalizedKeyedLock<K, V> implements KeyedLock<K, V>
{
    private final V value;
    
    protected FinalizedKeyedLock(final V value) {
        this.value = value;
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + '(' + (Object)this.value + ')';
    }
    
    @Override
    public V getOrRetryValue() {
        return this.value;
    }
    
    @Override
    public void lock() {
    }
    
    @Override
    public void unlock() {
    }
    
    @Override
    public void close() {
    }
}
