package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.profiles.mojang;

import java.util.List;
import com.mojang.authlib.GameProfile;
import java.util.UUID;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
final class PlayerProfile
{
    public final UUID realUUID;
    public final GameProfile requestedGameProfile;
    public final GameProfile fetchedGameProfile;
    public final List<String> profileActions;
    
    PlayerProfile(final UUID realUUID, final GameProfile requestedGameProfile, final GameProfile fetchedGameProfile, final List<String> profileActions) {
        this.realUUID = realUUID;
        this.requestedGameProfile = requestedGameProfile;
        this.fetchedGameProfile = fetchedGameProfile;
        this.profileActions = profileActions;
    }
    
    boolean exists() {
        return this.fetchedGameProfile != null;
    }
}
