package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.profiles.builder;

import java.util.concurrent.Executor;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.profiles.mojang.PlayerProfileFetcherThread;
import java.util.concurrent.CompletableFuture;
import java.util.Iterator;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.profiles.ProfileLogger;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.profiles.exceptions.ProfileException;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.profiles.exceptions.InvalidProfileException;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.profiles.exceptions.ProfileChangeException;
import java.util.Collection;
import java.util.Arrays;
import java.util.Objects;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import com.mojang.authlib.GameProfile;
import java.util.ArrayList;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.profiles.mojang.ProfileRequestConfiguration;
import java.util.function.Consumer;
import java.util.List;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.profiles.objects.Profileable;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.profiles.objects.ProfileContainer;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.profiles.objects.DelegateProfileable;

public final class ProfileInstruction<T> implements DelegateProfileable
{
    private final ProfileContainer<T> profileContainer;
    private Profileable profileable;
    private final List<Profileable> fallbacks;
    private Consumer<ProfileFallback<T>> onFallback;
    private ProfileRequestConfiguration profileRequestConfiguration;
    private boolean lenient;
    
    protected ProfileInstruction(final ProfileContainer<T> profileContainer) {
        this.fallbacks = (List<Profileable>)new ArrayList();
        this.lenient = false;
        this.profileContainer = profileContainer;
    }
    
    @NotNull
    @Contract(mutates = "this")
    public T removeProfile() {
        this.profileContainer.setProfile(null);
        return this.profileContainer.getObject();
    }
    
    @NotNull
    @Contract(value = "_ -> this", mutates = "this")
    @ApiStatus.Experimental
    public ProfileInstruction<T> profileRequestConfiguration(final ProfileRequestConfiguration config) {
        this.profileRequestConfiguration = config;
        return this;
    }
    
    @NotNull
    @Contract(value = "-> this", mutates = "this")
    public ProfileInstruction<T> lenient() {
        this.lenient = true;
        return this;
    }
    
    @Nullable
    @ApiStatus.Internal
    @Override
    public GameProfile getProfile() {
        return this.profileContainer.getProfile();
    }
    
    @Contract(pure = true)
    @ApiStatus.Internal
    @Override
    public Profileable getDelegateProfile() {
        return this.profileContainer;
    }
    
    @NotNull
    @Contract(value = "_ -> this", mutates = "this")
    public ProfileInstruction<T> profile(@NotNull final Profileable profileable) {
        this.profileable = (Profileable)Objects.requireNonNull((Object)profileable, "Profileable is null");
        return this;
    }
    
    @NotNull
    @Contract(value = "_ -> this", mutates = "this")
    public ProfileInstruction<T> fallback(@NotNull final Profileable... fallbacks) {
        Objects.requireNonNull((Object)fallbacks, "fallbacks array is null");
        this.fallbacks.addAll((Collection)Arrays.asList((Object[])fallbacks));
        return this;
    }
    
    @NotNull
    @Contract(value = "_ -> this", mutates = "this")
    public ProfileInstruction<T> onFallback(@Nullable final Consumer<ProfileFallback<T>> onFallback) {
        this.onFallback = onFallback;
        return this;
    }
    
    @NotNull
    @Contract(value = "_ -> this", mutates = "this")
    public ProfileInstruction<T> onFallback(@NotNull final Runnable onFallback) {
        Objects.requireNonNull((Object)onFallback, "onFallback runnable is null");
        this.onFallback = (Consumer<ProfileFallback<T>>)(fallback -> onFallback.run());
        return this;
    }
    
    @NotNull
    public T apply() {
        Objects.requireNonNull((Object)this.profileable, "No profile was set");
        ProfileChangeException exception = null;
        final List<Profileable> tries = (List<Profileable>)new ArrayList(2 + this.fallbacks.size());
        tries.add((Object)this.profileable);
        tries.addAll((Collection)this.fallbacks);
        if (this.lenient) {
            tries.add((Object)XSkull.getDefaultProfile());
        }
        boolean success = false;
        boolean tryingFallbacks = false;
        for (final Profileable profileable : tries) {
            try {
                final GameProfile gameProfile = profileable.getDisposableProfile();
                if (gameProfile != null) {
                    this.profileContainer.setProfile(gameProfile);
                    success = true;
                    break;
                }
                if (exception == null) {
                    exception = new ProfileChangeException("Could not set the profile for " + (Object)this.profileContainer);
                }
                exception.addSuppressed((Throwable)new InvalidProfileException(profileable.toString(), "Profile doesn't have a value: " + (Object)profileable));
                tryingFallbacks = true;
            }
            catch (final ProfileException ex) {
                if (exception == null) {
                    exception = new ProfileChangeException("Could not set the profile for " + (Object)this.profileContainer);
                }
                exception.addSuppressed((Throwable)ex);
                tryingFallbacks = true;
            }
        }
        if (exception != null) {
            if (!success && !this.lenient) {
                throw exception;
            }
            ProfileLogger.debug("apply() silenced exception {}", exception);
        }
        T object = this.profileContainer.getObject();
        if (tryingFallbacks && this.onFallback != null) {
            final ProfileFallback<T> fallback = new ProfileFallback<T>(this, object, exception);
            this.onFallback.accept((Object)fallback);
            object = fallback.getObject();
        }
        return object;
    }
    
    @NotNull
    public CompletableFuture<T> applyAsync() {
        return (CompletableFuture<T>)CompletableFuture.supplyAsync(this::apply, (Executor)PlayerProfileFetcherThread.EXECUTOR);
    }
}
