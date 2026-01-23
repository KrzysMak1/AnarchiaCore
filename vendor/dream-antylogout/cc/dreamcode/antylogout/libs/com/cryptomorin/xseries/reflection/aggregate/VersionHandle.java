package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.aggregate;

import java.util.concurrent.Callable;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.XReflection;
import org.jetbrains.annotations.ApiStatus;

public final class VersionHandle<T>
{
    private int version;
    private int patch;
    private T handle;
    
    @ApiStatus.Internal
    public VersionHandle(final int version, final T handle) {
        this(version, 0, handle);
    }
    
    @ApiStatus.Internal
    public VersionHandle(final int version, final int patch, final T handle) {
        if (XReflection.supports(version, patch)) {
            this.version = version;
            this.patch = patch;
            this.handle = handle;
        }
    }
    
    @ApiStatus.Internal
    public VersionHandle(final int version, final int patch, final Callable<T> handle) {
        if (XReflection.supports(version, patch)) {
            this.version = version;
            this.patch = patch;
            try {
                this.handle = (T)handle.call();
            }
            catch (final Exception ex) {}
        }
    }
    
    @ApiStatus.Internal
    public VersionHandle(final int version, final Callable<T> handle) {
        this(version, 0, handle);
    }
    
    public VersionHandle<T> v(final int version, final T handle) {
        return this.v(version, 0, handle);
    }
    
    private boolean checkVersion(final int version, final int patch) {
        if (version == this.version && patch == this.patch) {
            throw new IllegalArgumentException("Cannot have duplicate version handles for version: " + version + '.' + patch);
        }
        return version > this.version && patch >= this.patch && XReflection.supports(version, patch);
    }
    
    public VersionHandle<T> v(final int version, final int patch, final Callable<T> handle) {
        if (!this.checkVersion(version, patch)) {
            return this;
        }
        try {
            this.handle = (T)handle.call();
        }
        catch (final Exception ex) {}
        this.version = version;
        this.patch = patch;
        return this;
    }
    
    public VersionHandle<T> v(final int version, final int patch, final T handle) {
        if (this.checkVersion(version, patch)) {
            this.version = version;
            this.patch = patch;
            this.handle = handle;
        }
        return this;
    }
    
    public T orElse(final T handle) {
        return (this.version == 0) ? handle : this.handle;
    }
    
    public T orElse(final Callable<T> handle) {
        if (this.version == 0) {
            try {
                return (T)handle.call();
            }
            catch (final Exception e) {
                throw new IllegalArgumentException("The last handle also failed", (Throwable)e);
            }
        }
        return this.handle;
    }
}
