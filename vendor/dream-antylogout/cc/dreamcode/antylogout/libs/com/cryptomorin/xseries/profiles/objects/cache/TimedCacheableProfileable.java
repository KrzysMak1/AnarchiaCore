package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.profiles.objects.cache;

import org.jetbrains.annotations.NotNull;
import java.time.Duration;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public abstract class TimedCacheableProfileable extends CacheableProfileable
{
    private long lastUpdate;
    
    @NotNull
    protected Duration expiresAfter() {
        return Duration.ofHours(6L);
    }
    
    public final boolean hasExpired(final boolean renew) {
        final Duration expiresAfter = this.expiresAfter();
        if (expiresAfter.isZero()) {
            return false;
        }
        if (super.hasExpired(renew)) {
            return true;
        }
        if (this.cache == null && this.lastError == null) {
            return true;
        }
        final long now = System.currentTimeMillis();
        if (this.lastUpdate == 0L) {
            if (renew) {
                this.lastUpdate = now;
            }
            return true;
        }
        final long diff = now - this.lastUpdate;
        if (diff >= expiresAfter.toMillis()) {
            if (renew) {
                this.lastUpdate = now;
            }
            return true;
        }
        return false;
    }
}
