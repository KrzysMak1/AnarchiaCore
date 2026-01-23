package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.profiles.objects;

import java.util.UUID;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.profiles.exceptions.InvalidProfileException;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.profiles.PlayerProfiles;
import java.util.regex.Matcher;
import org.jetbrains.annotations.Nullable;
import java.util.Arrays;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import com.mojang.authlib.GameProfile;
import org.jetbrains.annotations.ApiStatus;
import java.util.regex.Pattern;

public enum ProfileInputType
{
    TEXTURE_HASH(Pattern.compile("[0-9a-z]{55,70}")) {
        @Override
        public GameProfile getProfile(final String textureHash) {
            final String base64 = PlayerProfiles.encodeBase64("{\"textures\":{\"SKIN\":{\"url\":\"http://textures.minecraft.net/texture/" + textureHash + "\"}}}");
            return PlayerProfiles.profileFromHashAndBase64(textureHash, base64);
        }
    }, 
    TEXTURE_URL(Pattern.compile("(?:https?://)?(?:textures\\.)?minecraft\\.net/texture/(?<hash>" + (Object)ProfileInputType.TEXTURE_HASH.pattern + ')', 2)) {
        @Override
        public GameProfile getProfile(final String textureUrl) {
            final String hash = extractTextureHash(textureUrl);
            return ProfileInputType$2.TEXTURE_HASH.getProfile(hash);
        }
    }, 
    BASE64(Pattern.compile("[-A-Za-z0-9+/]{100,}={0,3}")) {
        @Override
        public GameProfile getProfile(final String base64) {
            final String decodedBase64 = PlayerProfiles.decodeBase64(base64);
            if (decodedBase64 == null) {
                throw new InvalidProfileException(base64, "Not a base64 string: " + base64);
            }
            final String textureHash = extractTextureHash(decodedBase64);
            if (textureHash == null) {
                throw new InvalidProfileException(decodedBase64, "Can't extract texture hash from base64: " + decodedBase64);
            }
            return PlayerProfiles.profileFromHashAndBase64(textureHash, base64);
        }
    }, 
    UUID(Pattern.compile("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}")) {
        @Override
        public GameProfile getProfile(final String uuidString) {
            UUID uuid;
            try {
                uuid = java.util.UUID.fromString(uuidString);
            }
            catch (final IllegalArgumentException ex) {
                throw new InvalidProfileException(uuidString, "Invalid UUID string: " + uuidString, (Throwable)ex);
            }
            return Profileable.of(uuid).getProfile();
        }
    }, 
    USERNAME(Pattern.compile("[A-Za-z0-9_]{1,16}")) {
        @Override
        public GameProfile getProfile(final String username) {
            return Profileable.username(username).getProfile();
        }
    };
    
    @ApiStatus.Internal
    public final Pattern pattern;
    private static final ProfileInputType[] VALUES;
    
    private ProfileInputType(final Pattern pattern) {
        this.pattern = pattern;
    }
    
    public abstract GameProfile getProfile(final String p0);
    
    @Nullable
    public static ProfileInputType typeOf(@NotNull final String identifier) {
        Objects.requireNonNull((Object)identifier, "Identifier cannot be null");
        return (ProfileInputType)Arrays.stream((Object[])ProfileInputType.VALUES).filter(value -> value.pattern.matcher((CharSequence)identifier).matches()).findFirst().orElse((Object)null);
    }
    
    @Nullable
    private static String extractTextureHash(final String input) {
        final Matcher matcher = ProfileInputType.TEXTURE_HASH.pattern.matcher((CharSequence)input);
        return matcher.find() ? matcher.group() : null;
    }
    
    static {
        VALUES = values();
    }
}
