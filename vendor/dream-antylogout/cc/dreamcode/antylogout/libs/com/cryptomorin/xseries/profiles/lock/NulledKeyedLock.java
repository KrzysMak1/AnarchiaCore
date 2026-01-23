package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.profiles.lock;

import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Lock;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
final class NulledKeyedLock<K, V> implements KeyedLock<K, V>
{
    protected final Lock lock;
    private final KeyedLockMap<K> map;
    protected final K key;
    protected int pendingTasks;
    
    protected NulledKeyedLock(final KeyedLockMap<K> map, final K key) {
        this.lock = (Lock)new ReentrantLock();
        this.map = map;
        this.key = key;
    }
    
    @Override
    public V getOrRetryValue() {
        return null;
    }
    
    @Override
    public void lock() {
        this.lock.lock();
    }
    
    protected boolean tryLock() {
        return this.lock.tryLock();
    }
    
    @Override
    public void unlock() {
        this.map.unlock(this);
    }
    
    @Override
    public void close() {
        this.unlock();
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "(key=" + (Object)this.key + ", pendingTasks=" + this.pendingTasks + ')';
    }
}
