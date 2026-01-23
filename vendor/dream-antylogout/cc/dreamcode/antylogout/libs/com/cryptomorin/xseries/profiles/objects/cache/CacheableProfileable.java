package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.profiles.objects.cache;

import org.jetbrains.annotations.NotNull;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.XReflection;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.profiles.exceptions.MojangAPIRetryException;
import com.mojang.authlib.GameProfile;
import org.jetbrains.annotations.ApiStatus;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.profiles.objects.Profileable;

@ApiStatus.Internal
public abstract class CacheableProfileable implements Profileable
{
    protected GameProfile cache;
    protected Throwable lastError;
    
    @Override
    public final synchronized GameProfile getProfile() {
        if (this.hasExpired(true)) {
            this.lastError = null;
            this.cache = null;
        }
        if (this.lastError != null && !(this.lastError instanceof MojangAPIRetryException)) {
            throw XReflection.throwCheckedException(this.lastError);
        }
        if (this.cache == null) {
            try {
                this.cache = this.cacheProfile();
                this.lastError = null;
            }
            catch (final Throwable ex) {
                throw this.lastError = ex;
            }
        }
        return this.cache;
    }
    
    public final boolean hasExpired() {
        return this.hasExpired(false);
    }
    
    @Override
    public final boolean isReady() {
        return !this.hasExpired(false);
    }
    
    protected boolean hasExpired(final boolean renew) {
        return this.lastError instanceof MojangAPIRetryException;
    }
    
    @NotNull
    protected abstract GameProfile cacheProfile();
    
    @Override
    public final String toString() {
        return this.getClass().getSimpleName() + "[cache=" + (Object)this.cache + ", lastError=" + (Object)this.lastError + ']';
    }
}
