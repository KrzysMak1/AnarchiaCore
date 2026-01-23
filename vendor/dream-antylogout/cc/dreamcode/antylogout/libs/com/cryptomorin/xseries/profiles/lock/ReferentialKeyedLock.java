package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.profiles.lock;

import java.util.function.Function;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
final class ReferentialKeyedLock<K, V> implements KeyedLock<K, V>
{
    protected final Function<K, V> fetcher;
    protected final NulledKeyedLock<K, V> lock;
    private V value;
    
    protected ReferentialKeyedLock(final NulledKeyedLock<K, V> lock, final Function<K, V> fetcher) {
        this.lock = lock;
        this.fetcher = fetcher;
    }
    
    @Override
    public V getOrRetryValue() {
        if (this.value == null) {
            this.value = (V)this.fetcher.apply((Object)this.lock.key);
        }
        return this.value;
    }
    
    @Override
    public void lock() {
        this.lock.lock();
    }
    
    @Override
    public void unlock() {
        this.lock.unlock();
    }
    
    @Override
    public void close() {
        this.unlock();
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "(lock=" + (Object)this.lock + ", value=" + (Object)this.value + ", fetcher=" + ((this.fetcher == null) ? "null" : this.fetcher.getClass().getSimpleName()) + ')';
    }
}
