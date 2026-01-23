package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.profiles.mojang;

import com.google.common.base.Strings;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.profiles.PlayerProfiles;
import com.mojang.authlib.yggdrasil.ProfileActionType;
import java.util.stream.Collectors;
import java.util.Objects;
import java.util.Set;
import com.mojang.authlib.yggdrasil.ProfileResult;
import com.google.common.cache.LoadingCache;
import org.jetbrains.annotations.Nullable;
import java.util.Optional;
import com.mojang.authlib.GameProfile;
import java.util.UUID;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
abstract class MojangProfileCache
{
    abstract void cache(final PlayerProfile p0);
    
    @Nullable
    abstract Optional<GameProfile> get(final UUID p0, final GameProfile p1);
    
    protected static final class ProfileResultCache extends MojangProfileCache
    {
        private final LoadingCache<UUID, Optional<ProfileResult>> insecureProfiles;
        
        ProfileResultCache(final LoadingCache<?, ?> insecureProfiles) {
            this.insecureProfiles = (LoadingCache<UUID, Optional<ProfileResult>>)insecureProfiles;
        }
        
        @Override
        void cache(final PlayerProfile playerProfile) {
            if (playerProfile.exists()) {
                final ProfileResult profileResult = new ProfileResult(playerProfile.fetchedGameProfile, (Set)playerProfile.profileActions.stream().map(x -> {
                    try {
                        return ProfileActionType.valueOf(x);
                    }
                    catch (final IllegalArgumentException ex) {
                        return null;
                    }
                }).filter(Objects::nonNull).collect(Collectors.toSet()));
                this.insecureProfiles.put((Object)playerProfile.realUUID, (Object)Optional.of((Object)profileResult));
            }
            else {
                this.insecureProfiles.put((Object)playerProfile.realUUID, (Object)Optional.empty());
            }
        }
        
        @Override
        Optional<GameProfile> get(final UUID realId, final GameProfile gameProfile) {
            final Optional<ProfileResult> cache = (Optional<ProfileResult>)this.insecureProfiles.getIfPresent((Object)realId);
            return (Optional<GameProfile>)((cache == null) ? null : cache.map(ProfileResult::profile));
        }
    }
    
    protected static final class GameProfileCache extends MojangProfileCache
    {
        private final LoadingCache<GameProfile, GameProfile> insecureProfiles;
        
        GameProfileCache(final LoadingCache<?, ?> insecureProfiles) {
            this.insecureProfiles = (LoadingCache<GameProfile, GameProfile>)insecureProfiles;
        }
        
        @Override
        void cache(final PlayerProfile playerProfile) {
            if (playerProfile.exists()) {
                this.insecureProfiles.put((Object)playerProfile.requestedGameProfile, (Object)playerProfile.fetchedGameProfile);
            }
            else {
                this.insecureProfiles.put((Object)playerProfile.requestedGameProfile, (Object)PlayerProfiles.NIL);
            }
        }
        
        @Nullable
        @Override
        Optional<GameProfile> get(final UUID realId, final GameProfile gameProfile) {
            final String profileName = gameProfile.getName();
            if (Strings.isNullOrEmpty(profileName) || profileName.equals((Object)"XSeries")) {
                return null;
            }
            final GameProfile cache = (GameProfile)this.insecureProfiles.getIfPresent((Object)new GameProfile(realId, gameProfile.getName()));
            if (cache == PlayerProfiles.NIL) {
                return (Optional<GameProfile>)Optional.empty();
            }
            return (Optional<GameProfile>)((cache == null) ? null : Optional.of((Object)cache));
        }
    }
}
