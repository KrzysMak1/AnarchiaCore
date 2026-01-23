package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.profiles;

import cc.dreamcode.antylogout.libs.com.google.gson.GsonBuilder;
import cc.dreamcode.antylogout.libs.com.google.gson.JsonParser;
import java.util.Base64;
import com.mojang.authlib.properties.PropertyMap;
import com.google.common.collect.Multimap;
import cc.dreamcode.antylogout.libs.com.google.gson.JsonElement;
import cc.dreamcode.antylogout.libs.com.google.gson.JsonObject;
import java.util.UUID;
import java.nio.charset.StandardCharsets;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.profiles.objects.transformer.ProfileTransformer;
import org.jetbrains.annotations.Nullable;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import com.google.common.collect.Iterables;
import java.util.Optional;
import cc.dreamcode.antylogout.libs.com.google.gson.Gson;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public final class PlayerProfiles
{
    public static final String XSERIES_SIG = "XSeries";
    private static final Property XSERIES_GAMEPROFILE_SIGNATURE;
    private static final String TEXTURES_PROPERTY = "textures";
    public static final GameProfile NIL;
    private static final Gson GSON;
    public static final String TEXTURES_NBT_PROPERTY_PREFIX = "{\"textures\":{\"SKIN\":{\"url\":\"";
    public static final String TEXTURES_BASE_URL = "http://textures.minecraft.net/texture/";
    
    public static Optional<Property> getTextureProperty(final GameProfile profile) {
        return (Optional<Property>)Optional.ofNullable((Object)Iterables.getFirst((Iterable)profile.getProperties().get((Object)"textures"), (Object)null));
    }
    
    @Nullable
    public static String getTextureValue(@NotNull final GameProfile profile) {
        Objects.requireNonNull((Object)profile, "Game profile cannot be null");
        return (String)getTextureProperty(profile).map(PlayerProfiles::getPropertyValue).orElse((Object)null);
    }
    
    @Nullable
    public static String getOriginalValue(@Nullable final GameProfile profile) {
        if (profile == null) {
            return null;
        }
        final String original = ProfileTransformer.IncludeOriginalValue.getOriginalValue(profile);
        if (original != null) {
            return original;
        }
        return getTextureValue(profile);
    }
    
    @NotNull
    public static String getPropertyValue(@NotNull final Property property) {
        if (ProfilesCore.NULLABILITY_RECORD_UPDATE) {
            return property.value();
        }
        try {
            return ProfilesCore.Property_getValue.invoke(property);
        }
        catch (final Throwable throwable) {
            throw new IllegalArgumentException("Unable to get a property value: " + (Object)property, throwable);
        }
    }
    
    public static boolean hasTextures(final GameProfile profile) {
        return getTextureProperty(profile).isPresent();
    }
    
    @NotNull
    public static GameProfile profileFromHashAndBase64(final String hash, final String base64) {
        final UUID uuid = UUID.nameUUIDFromBytes(hash.getBytes(StandardCharsets.UTF_8));
        final GameProfile profile = createNamelessGameProfile(uuid);
        setTexturesProperty(profile, base64);
        return profile;
    }
    
    public static void removeTimestamp(final GameProfile profile) {
        final JsonObject jsonObject = (JsonObject)Optional.ofNullable((Object)getTextureValue(profile)).map(PlayerProfiles::decodeBase64).map(decoded -> new JsonParser().parse(decoded).getAsJsonObject()).orElse((Object)null);
        if (jsonObject == null || !jsonObject.has("timestamp")) {
            return;
        }
        jsonObject.remove("timestamp");
        setTexturesProperty(profile, encodeBase64(PlayerProfiles.GSON.toJson(jsonObject)));
    }
    
    @Nullable
    public static GameProfile unwrapProfile(@Nullable Object profile) throws Throwable {
        if (profile == null) {
            return null;
        }
        if (!(profile instanceof GameProfile) && ProfilesCore.ResolvableProfile_gameProfile != null) {
            profile = ProfilesCore.ResolvableProfile_gameProfile.invoke(profile);
        }
        return (GameProfile)profile;
    }
    
    @Nullable
    public static Object wrapProfile(@Nullable final GameProfile profile) throws Throwable {
        if (profile == null) {
            return null;
        }
        if (ProfilesCore.ResolvableProfile$bukkitSupports) {
            return ProfilesCore.ResolvableProfile$constructor.invoke(profile);
        }
        return profile;
    }
    
    public static GameProfile sanitizeProfile(final GameProfile profile) {
        if (PlayerUUIDs.isOnlineMode()) {
            return profile;
        }
        final UUID offlineId = PlayerUUIDs.getOfflineUUID(profile.getName());
        PlayerUUIDs.ONLINE_TO_OFFLINE.put((Object)profile.getId(), (Object)offlineId);
        final GameProfile clone = createGameProfile(offlineId, profile.getName());
        clone.getProperties().putAll((Multimap)profile.getProperties());
        return clone;
    }
    
    public static GameProfile clone(final GameProfile gameProfile) {
        final GameProfile clone = new GameProfile(gameProfile.getId(), gameProfile.getName());
        clone.getProperties().putAll((Multimap)gameProfile.getProperties());
        return clone;
    }
    
    public static void setTexturesProperty(final GameProfile profile, final String texture) {
        final Property property = new Property("textures", texture);
        final PropertyMap properties = profile.getProperties();
        properties.asMap().remove((Object)"textures");
        properties.put((Object)"textures", (Object)property);
    }
    
    public static String encodeBase64(final String str) {
        return Base64.getEncoder().encodeToString(str.getBytes(StandardCharsets.UTF_8));
    }
    
    @Nullable
    public static String decodeBase64(final String base64) {
        Objects.requireNonNull((Object)base64, "Cannot decode null string");
        try {
            final byte[] bytes = Base64.getDecoder().decode(base64);
            return new String(bytes, StandardCharsets.UTF_8);
        }
        catch (final IllegalArgumentException exception) {
            return null;
        }
    }
    
    public static GameProfile createGameProfile(final UUID uuid, final String username) {
        return signXSeries(new GameProfile(uuid, username));
    }
    
    public static GameProfile signXSeries(final GameProfile profile) {
        final PropertyMap properties = profile.getProperties();
        properties.put((Object)"XSeries", (Object)PlayerProfiles.XSERIES_GAMEPROFILE_SIGNATURE);
        return profile;
    }
    
    public static GameProfile createNamelessGameProfile(final UUID id) {
        return createGameProfile(id, "XSeries");
    }
    
    static {
        XSERIES_GAMEPROFILE_SIGNATURE = new Property("XSeries", "13.3.2");
        NIL = createGameProfile(PlayerUUIDs.IDENTITY_UUID, "XSeries");
        GSON = new GsonBuilder().setPrettyPrinting().create();
    }
}
