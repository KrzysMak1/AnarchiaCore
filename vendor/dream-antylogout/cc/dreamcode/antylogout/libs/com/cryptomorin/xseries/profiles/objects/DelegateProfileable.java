package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.profiles.objects;

import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.profiles.objects.transformer.ProfileTransformer;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.profiles.exceptions.ProfileException;
import org.jetbrains.annotations.Nullable;
import com.mojang.authlib.GameProfile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public interface DelegateProfileable extends Profileable
{
    @ApiStatus.Internal
    @NotNull
    Profileable getDelegateProfile();
    
    @Nullable
    default GameProfile getProfile() {
        return this.getDelegateProfile().getProfile();
    }
    
    @Nullable
    default ProfileException test() {
        return this.getDelegateProfile().test();
    }
    
    @Nullable
    default GameProfile getDisposableProfile() {
        return this.getDelegateProfile().getDisposableProfile();
    }
    
    default boolean isReady() {
        return this.getDelegateProfile().isReady();
    }
    
    @NotNull
    default Profileable transform(@NotNull final ProfileTransformer... transformers) {
        return this.getDelegateProfile().transform(transformers);
    }
    
    @Nullable
    default String getProfileValue() {
        return this.getDelegateProfile().getProfileValue();
    }
}
