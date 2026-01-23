package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.profiles.lock;

import java.util.function.Function;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public final class KeyedLockMap<K>
{
    private final Map<K, NulledKeyedLock<K, ?>> locks;
    
    public KeyedLockMap() {
        this.locks = (Map<K, NulledKeyedLock<K, ?>>)new HashMap();
    }
    
    private NulledKeyedLock<K, ?> createLock(final K key) {
        return new NulledKeyedLock<K, Object>(this, key);
    }
    
    public <V> KeyedLock<K, V> lock(final K key, final Supplier<V> fetcher) {
        Objects.requireNonNull((Object)fetcher, "Value fetcher is null");
        return this.lock(key, (java.util.function.Function<K, V>)(k -> fetcher.get()));
    }
    
    public <V> KeyedLock<K, V> lock(final K key, final Function<K, V> fetcher) {
        Objects.requireNonNull((Object)fetcher, "Value fetcher is null");
        Objects.requireNonNull((Object)key, "Key is null");
        final V value = (V)fetcher.apply((Object)key);
        if (value != null) {
            return new FinalizedKeyedLock<K, V>(value);
        }
        final NulledKeyedLock<K, V> lock;
        synchronized (this.locks) {
            lock = (NulledKeyedLock)this.locks.computeIfAbsent((Object)key, this::createLock);
            final boolean isNew = lock.pendingTasks == 0;
            final NulledKeyedLock<K, V> nulledKeyedLock = lock;
            ++nulledKeyedLock.pendingTasks;
            if (isNew) {
                if (lock.tryLock()) {
                    return lock;
                }
                throw new IllegalStateException("Expected first lock holder to be free: " + (Object)lock);
            }
        }
        lock.lock();
        return new ReferentialKeyedLock<K, V>(lock, fetcher);
    }
    
    void unlock(final NulledKeyedLock<K, ?> lock) {
        synchronized (this.locks) {
            --lock.pendingTasks;
            if (lock.pendingTasks <= 0) {
                this.locks.remove((Object)lock.key);
            }
        }
        lock.lock.unlock();
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + '(' + (Object)this.locks + ')';
    }
}
