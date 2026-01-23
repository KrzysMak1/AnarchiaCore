package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.profiles.lock;

import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public interface KeyedLock<K, V> extends AutoCloseable
{
    V getOrRetryValue();
    
    @ApiStatus.OverrideOnly
    void lock();
    
    void unlock();
    
    void close();
}
