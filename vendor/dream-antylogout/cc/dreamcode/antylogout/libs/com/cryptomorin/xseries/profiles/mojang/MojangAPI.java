package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.profiles.mojang;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import com.mojang.authlib.properties.PropertyMap;
import com.mojang.authlib.properties.Property;
import java.util.ArrayList;
import java.util.function.Supplier;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.profiles.exceptions.UnknownPlayerException;
import cc.dreamcode.antylogout.libs.com.google.gson.JsonArray;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.profiles.lock.KeyedLockMap;
import java.util.Iterator;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.profiles.lock.KeyedLock;
import java.util.List;
import com.google.common.collect.Iterables;
import java.util.function.Function;
import java.util.Objects;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.profiles.lock.MojangRequestQueue;
import java.util.HashMap;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.profiles.objects.ProfileInputType;
import java.util.Map;
import java.util.Collection;
import java.util.Locale;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.XReflection;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.profiles.ProfileLogger;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.profiles.PlayerProfiles;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.profiles.ProfilesCore;
import org.jetbrains.annotations.Nullable;
import java.io.IOException;
import cc.dreamcode.antylogout.libs.com.google.gson.JsonObject;
import cc.dreamcode.antylogout.libs.com.google.gson.JsonElement;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.profiles.PlayerUUIDs;
import org.jetbrains.annotations.NotNull;
import com.mojang.authlib.GameProfile;
import java.util.Optional;
import java.util.UUID;
import com.google.common.cache.Cache;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public final class MojangAPI
{
    private static final MojangProfileCache MOJANG_PROFILE_CACHE;
    private static final Cache<UUID, Optional<GameProfile>> INSECURE_PROFILES;
    private static final boolean REQUIRE_SECURE_PROFILES = false;
    private static final MinecraftClient USERNAME_TO_UUID;
    private static final MinecraftClient USERNAMES_TO_UUIDS;
    private static final MinecraftClient UUID_TO_PROFILE;
    
    @Nullable
    public static UUID requestUsernameToUUID(@NotNull final String username) throws IOException {
        final JsonElement requestElement = MojangAPI.USERNAME_TO_UUID.session(null).append(username).request();
        if (requestElement == null) {
            return null;
        }
        final JsonObject userJson = requestElement.getAsJsonObject();
        final JsonElement idElement = userJson.get("id");
        if (idElement == null) {
            throw new IllegalArgumentException("No 'id' field for UUID request for '" + username + "': " + (Object)userJson);
        }
        return PlayerUUIDs.UUIDFromDashlessString(idElement.getAsString());
    }
    
    @ApiStatus.Obsolete
    private static GameProfile getCachedProfileByUsername(final String username) {
        try {
            Object profile = ProfilesCore.GameProfileCache_get$profileByName$.invoke(ProfilesCore.USER_CACHE, username);
            if (profile instanceof Optional) {
                profile = ((Optional)profile).orElse((Object)null);
            }
            final GameProfile gameProfile = (profile == null) ? PlayerProfiles.createGameProfile(PlayerUUIDs.IDENTITY_UUID, username) : PlayerProfiles.sanitizeProfile((GameProfile)profile);
            ProfileLogger.debug("The cached profile for {} -> {}", username, profile);
            return gameProfile;
        }
        catch (final Throwable throwable) {
            ProfileLogger.LOGGER.error("Unable to get cached profile by username: {}", (Object)username, (Object)throwable);
            return null;
        }
    }
    
    public static Optional<GameProfile> getMojangCachedProfileFromUsername(final String username) {
        try {
            return getMojangCachedProfileFromUsername0(username);
        }
        catch (final Throwable e) {
            throw XReflection.throwCheckedException(e);
        }
    }
    
    private static Optional<GameProfile> getMojangCachedProfileFromUsername0(final String username) throws Throwable {
        final String normalized = username.toLowerCase(Locale.ENGLISH);
        final Object userCacheEntry = ProfilesCore.UserCache_profilesByName.get((Object)normalized);
        Optional<GameProfile> optional;
        if (userCacheEntry != null) {
            if (ProfilesCore.UserCacheEntry_setLastAccess != null && ProfilesCore.UserCache_getNextOperation != null) {
                final long nextOperation = ProfilesCore.UserCache_getNextOperation.invoke(ProfilesCore.USER_CACHE);
                ProfilesCore.UserCacheEntry_setLastAccess.invoke(userCacheEntry, nextOperation);
            }
            optional = (Optional<GameProfile>)Optional.of((Object)ProfilesCore.UserCacheEntry_getProfile.invoke(userCacheEntry));
        }
        else {
            final UUID realUUID = PlayerUUIDs.getRealUUIDOfPlayer(username);
            if (realUUID == null) {
                return (Optional<GameProfile>)Optional.empty();
            }
            final GameProfile profile = PlayerProfiles.createGameProfile(PlayerUUIDs.isOnlineMode() ? realUUID : PlayerUUIDs.getOfflineUUID(username), username);
            optional = (Optional<GameProfile>)Optional.of((Object)profile);
            cacheProfile(profile);
        }
        return optional;
    }
    
    public static Map<UUID, String> usernamesToUUIDs(@NotNull final Collection<String> usernames, @Nullable final ProfileRequestConfiguration config) {
        if (usernames == null || usernames.isEmpty()) {
            throw new IllegalArgumentException("Usernames are null or empty");
        }
        for (final String username : usernames) {
            if (username == null || !ProfileInputType.USERNAME.pattern.matcher((CharSequence)username).matches()) {
                throw new IllegalArgumentException("One of the requested usernames is invalid: " + username + " in " + (Object)usernames);
            }
        }
        final Map<UUID, String> mapped = (Map<UUID, String>)new HashMap(usernames.size());
        final Map<String, KeyedLock<String, UUID>> pendingUsernames = (Map<String, KeyedLock<String, UUID>>)new HashMap(usernames.size());
        try {
            for (final String username2 : usernames) {
                if (pendingUsernames.containsKey((Object)username2)) {
                    continue;
                }
                final KeyedLockMap<String> username_REQUESTS = MojangRequestQueue.USERNAME_REQUESTS;
                final String key = username2;
                final Map<String, UUID> username_TO_ONLINE = PlayerUUIDs.USERNAME_TO_ONLINE;
                Objects.requireNonNull((Object)username_TO_ONLINE);
                final KeyedLock<String, UUID> lock = username_REQUESTS.lock(key, (java.util.function.Function<String, UUID>)username_TO_ONLINE::get);
                final UUID cached = lock.getOrRetryValue();
                if (cached != null) {
                    mapped.put((Object)cached, (Object)username2);
                    lock.unlock();
                }
                else {
                    pendingUsernames.put((Object)username2, (Object)lock);
                }
            }
            if (pendingUsernames.isEmpty()) {
                return mapped;
            }
            final boolean onlineMode = PlayerUUIDs.isOnlineMode();
            final Iterable<List<String>> partition = (Iterable<List<String>>)Iterables.partition((Iterable)pendingUsernames.keySet(), 10);
            for (final List<String> batch : partition) {
                JsonArray response;
                try {
                    response = MojangAPI.USERNAMES_TO_UUIDS.session(config).body(batch).request().getAsJsonArray();
                }
                catch (final IOException ex) {
                    throw new IllegalStateException("Failed to request UUIDs for username batch: " + (Object)batch, (Throwable)ex);
                }
                for (final JsonElement element : response) {
                    final JsonObject obj = element.getAsJsonObject();
                    final String name = obj.get("name").getAsString();
                    final UUID realId = PlayerUUIDs.UUIDFromDashlessString(obj.get("id").getAsString());
                    final UUID offlineId = PlayerUUIDs.getOfflineUUID(name);
                    PlayerUUIDs.USERNAME_TO_ONLINE.put((Object)name, (Object)realId);
                    PlayerUUIDs.ONLINE_TO_OFFLINE.put((Object)realId, (Object)offlineId);
                    PlayerUUIDs.OFFLINE_TO_ONLINE.put((Object)offlineId, (Object)realId);
                    if (!ProfilesCore.UserCache_profilesByName.containsKey((Object)name)) {
                        cacheProfile(PlayerProfiles.createGameProfile(onlineMode ? realId : offlineId, name));
                    }
                    final String prev = (String)mapped.put((Object)realId, (Object)name);
                    if (prev != null) {
                        throw new IllegalArgumentException("Got duplicate usernames for UUID: " + (Object)realId + " (" + prev + " -> " + name + ')');
                    }
                }
            }
        }
        finally {
            for (final KeyedLock<String, UUID> lock2 : pendingUsernames.values()) {
                lock2.unlock();
            }
        }
        return mapped;
    }
    
    @NotNull
    public static GameProfile getCachedProfileByUUID(UUID uuid) {
        uuid = (UUID)(PlayerUUIDs.isOnlineMode() ? uuid : PlayerUUIDs.ONLINE_TO_OFFLINE.getOrDefault((Object)uuid, (Object)uuid));
        try {
            Object profile = ProfilesCore.GameProfileCache_get$profileByUUID$.invoke(ProfilesCore.USER_CACHE, uuid);
            if (profile instanceof Optional) {
                profile = ((Optional)profile).orElse((Object)null);
            }
            ProfileLogger.debug("The cached profile for {} -> {}", uuid, profile);
            return (profile == null) ? PlayerProfiles.createNamelessGameProfile(uuid) : PlayerProfiles.sanitizeProfile((GameProfile)profile);
        }
        catch (final Throwable throwable) {
            ProfileLogger.LOGGER.error("Unable to get cached profile by UUID: {}", (Object)uuid, (Object)throwable);
            return PlayerProfiles.createNamelessGameProfile(uuid);
        }
    }
    
    private static void cacheProfile(final GameProfile profile) {
        try {
            ProfilesCore.CACHE_PROFILE.invoke(ProfilesCore.USER_CACHE, profile);
            ProfileLogger.debug("Profile is now cached: {}", profile);
        }
        catch (final Throwable throwable) {
            ProfileLogger.LOGGER.error("Unable to cache profile {}", (Object)profile);
            throwable.printStackTrace();
        }
    }
    
    @NotNull
    public static GameProfile getOrFetchProfile(@NotNull final GameProfile profile) throws UnknownPlayerException {
        UUID realUUID;
        if (profile.getName().equals((Object)"XSeries")) {
            realUUID = profile.getId();
        }
        else {
            realUUID = PlayerUUIDs.getRealUUIDOfPlayer(profile.getName(), profile.getId());
            if (realUUID == null) {
                throw new UnknownPlayerException((Object)profile.getName(), "Player with the given properties not found: " + (Object)profile);
            }
        }
        try (final KeyedLock<UUID, GameProfile> lock = MojangRequestQueue.UUID_REQUESTS.lock(realUUID, (java.util.function.Supplier<GameProfile>)(() -> handleCache(profile, realUUID)))) {
            final GameProfile cached = lock.getOrRetryValue();
            if (cached != null) {
                final GameProfile gameProfile = cached;
                if (lock != null) {
                    lock.close();
                }
                return gameProfile;
            }
            final JsonElement request = requestProfile(profile, realUUID);
            final JsonObject profileData = request.getAsJsonObject();
            final List<String> profileActions = (List<String>)new ArrayList();
            GameProfile fetchedProfile = createGameProfile(profileData, profileActions);
            fetchedProfile = PlayerProfiles.sanitizeProfile(fetchedProfile);
            cacheProfile(fetchedProfile);
            MojangAPI.INSECURE_PROFILES.put((Object)realUUID, (Object)Optional.of((Object)fetchedProfile));
            MojangAPI.MOJANG_PROFILE_CACHE.cache(new PlayerProfile(realUUID, profile, fetchedProfile, profileActions));
            return fetchedProfile;
        }
        catch (final Throwable ex) {
            throw new IllegalStateException("Failed to fetch profile for " + (Object)profile, ex);
        }
    }
    
    @Nullable
    private static GameProfile handleCache(@NotNull final GameProfile profile, final UUID realUUID) {
        final Optional<GameProfile> cached = (Optional<GameProfile>)MojangAPI.INSECURE_PROFILES.getIfPresent((Object)realUUID);
        if (cached != null) {
            ProfileLogger.debug("Found cached profile from UUID ({}): {} -> {}", realUUID, profile, cached);
            if (cached.isPresent()) {
                return (GameProfile)cached.get();
            }
            throw new UnknownPlayerException(realUUID, "Player with the given properties not found: " + (Object)profile);
        }
        else {
            final Optional<GameProfile> mojangCache = MojangAPI.MOJANG_PROFILE_CACHE.get(realUUID, profile);
            if (mojangCache == null) {
                return null;
            }
            MojangAPI.INSECURE_PROFILES.put((Object)realUUID, (Object)mojangCache);
            if (mojangCache.isPresent()) {
                return (GameProfile)mojangCache.get();
            }
            throw new UnknownPlayerException(realUUID, "Player with the given properties not found: " + (Object)profile);
        }
    }
    
    @NotNull
    private static JsonElement requestProfile(@NotNull final GameProfile profile, final UUID realUUID) {
        JsonElement request;
        try {
            request = MojangAPI.UUID_TO_PROFILE.session(null).append(PlayerUUIDs.toUndashedUUID(realUUID) + "?unsigned=" + true).request();
        }
        catch (final IOException e) {
            throw new IllegalStateException("Failed to request profile: " + (Object)profile + " with real UUID: " + (Object)realUUID, (Throwable)e);
        }
        if (request == null) {
            MojangAPI.INSECURE_PROFILES.put((Object)realUUID, (Object)Optional.empty());
            MojangAPI.MOJANG_PROFILE_CACHE.cache(new PlayerProfile(realUUID, profile, null, null));
            throw new UnknownPlayerException(realUUID, "Player with the given properties not found: " + (Object)profile);
        }
        return request;
    }
    
    @NotNull
    private static GameProfile createGameProfile(final JsonObject profileData, final List<String> profileActions) {
        final UUID id = PlayerUUIDs.UUIDFromDashlessString(profileData.get("id").getAsString());
        final String name = profileData.get("name").getAsString();
        final GameProfile fetchedProfile = PlayerProfiles.createGameProfile(id, name);
        final JsonElement propertiesEle = profileData.get("properties");
        if (propertiesEle != null) {
            final JsonArray props = propertiesEle.getAsJsonArray();
            final PropertyMap properties = fetchedProfile.getProperties();
            for (final JsonElement prop : props) {
                final JsonObject obj = prop.getAsJsonObject();
                final String propName = obj.get("name").getAsString();
                final String propValue = obj.get("value").getAsString();
                final JsonElement sig = obj.get("signature");
                Property property;
                if (sig != null) {
                    property = new Property(propName, propValue, sig.getAsString());
                }
                else {
                    property = new Property(propName, propValue);
                }
                properties.put((Object)propName, (Object)property);
            }
        }
        final JsonElement profileActionsElement = profileData.get("profileActions");
        if (profileActionsElement != null) {
            for (final JsonElement action : profileActionsElement.getAsJsonArray()) {
                profileActions.add((Object)action.getAsString());
            }
        }
        return fetchedProfile;
    }
    
    static {
        MOJANG_PROFILE_CACHE = (ProfilesCore.NULLABILITY_RECORD_UPDATE ? new MojangProfileCache.ProfileResultCache(ProfilesCore.YggdrasilMinecraftSessionService_insecureProfiles) : new MojangProfileCache.GameProfileCache(ProfilesCore.YggdrasilMinecraftSessionService_insecureProfiles));
        INSECURE_PROFILES = CacheBuilder.newBuilder().expireAfterWrite(6L, TimeUnit.HOURS).build();
        USERNAME_TO_UUID = new MinecraftClient("GET", "https://api.mojang.com/users/profiles/minecraft/", new RateLimiter(600, Duration.ofMinutes(10L)));
        USERNAMES_TO_UUIDS = new MinecraftClient("POST", "https://api.minecraftservices.com/minecraft/profile/lookup/bulk/byname", new RateLimiter(600, Duration.ofMinutes(10L)));
        UUID_TO_PROFILE = new MinecraftClient("GET", "https://sessionserver.mojang.com/session/minecraft/profile/", new RateLimiter(200, Duration.ofMinutes(1L)));
    }
}
