package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.profiles;

import java.util.HashMap;
import java.io.IOException;
import java.util.function.Supplier;
import org.jetbrains.annotations.Nullable;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.profiles.lock.KeyedLock;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.profiles.lock.KeyedLockMap;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.profiles.mojang.MojangAPI;
import java.util.function.Function;
import java.util.Objects;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.profiles.lock.MojangRequestQueue;
import com.google.common.base.Strings;
import org.bukkit.Bukkit;
import java.nio.charset.StandardCharsets;
import org.jetbrains.annotations.NotNull;
import java.util.regex.Matcher;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.UUID;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public final class PlayerUUIDs
{
    public static final UUID IDENTITY_UUID;
    private static final Pattern UUID_NO_DASHES;
    public static final Map<UUID, UUID> OFFLINE_TO_ONLINE;
    public static final Map<UUID, UUID> ONLINE_TO_OFFLINE;
    public static final Map<String, UUID> USERNAME_TO_ONLINE;
    
    public static UUID UUIDFromDashlessString(final String dashlessUUIDString) {
        final Matcher matcher = PlayerUUIDs.UUID_NO_DASHES.matcher((CharSequence)dashlessUUIDString);
        try {
            return UUID.fromString(matcher.replaceFirst("$1-$2-$3-$4-$5"));
        }
        catch (final IllegalArgumentException ex) {
            throw new IllegalArgumentException("Cannot convert from dashless UUID: " + dashlessUUIDString, (Throwable)ex);
        }
    }
    
    public static String toUndashedUUID(final UUID id) {
        return id.toString().replace((CharSequence)"-", (CharSequence)"");
    }
    
    @NotNull
    public static UUID getOfflineUUID(@NotNull final String username) {
        return UUID.nameUUIDFromBytes(("OfflinePlayer:" + username).getBytes(StandardCharsets.UTF_8));
    }
    
    public static boolean isOnlineMode() {
        return Bukkit.getOnlineMode();
    }
    
    @Nullable
    public static UUID getRealUUIDOfPlayer(@NotNull final String username) {
        if (Strings.isNullOrEmpty(username)) {
            throw new IllegalArgumentException("Username is null or empty: " + username);
        }
        final UUID offlineUUID = getOfflineUUID(username);
        UUID realUUID;
        boolean cached;
        try {
            final KeyedLockMap<String> username_REQUESTS = MojangRequestQueue.USERNAME_REQUESTS;
            final Map<String, UUID> username_TO_ONLINE = PlayerUUIDs.USERNAME_TO_ONLINE;
            Objects.requireNonNull((Object)username_TO_ONLINE);
            try (final KeyedLock<String, UUID> lock = username_REQUESTS.lock(username, (java.util.function.Function<String, UUID>)username_TO_ONLINE::get)) {
                realUUID = lock.getOrRetryValue();
                cached = (realUUID != null);
                if (realUUID == null) {
                    realUUID = MojangAPI.requestUsernameToUUID(username);
                    if (realUUID == null) {
                        ProfileLogger.debug("Caching null for {} ({}) because it doesn't exist.", username, offlineUUID);
                        realUUID = PlayerUUIDs.IDENTITY_UUID;
                    }
                    else {
                        PlayerUUIDs.ONLINE_TO_OFFLINE.put((Object)realUUID, (Object)offlineUUID);
                    }
                    PlayerUUIDs.OFFLINE_TO_ONLINE.put((Object)offlineUUID, (Object)realUUID);
                    PlayerUUIDs.USERNAME_TO_ONLINE.put((Object)username, (Object)realUUID);
                }
            }
        }
        catch (final Exception e) {
            throw new IllegalStateException("Error while getting real UUID of player: " + username, (Throwable)e);
        }
        if (realUUID == PlayerUUIDs.IDENTITY_UUID) {
            ProfileLogger.debug("Providing null UUID for {} because it doesn't exist.", username);
            realUUID = null;
        }
        else {
            ProfileLogger.debug((cached ? "Cached " : "") + "Real UUID for {} ({}) is {}", username, offlineUUID, realUUID);
        }
        return realUUID;
    }
    
    @Nullable
    public static UUID getRealUUIDOfPlayer(@NotNull final String username, @NotNull final UUID uuid) {
        Objects.requireNonNull((Object)uuid);
        if (Strings.isNullOrEmpty(username)) {
            throw new IllegalArgumentException("Username is null or empty: " + username);
        }
        if (isOnlineMode()) {
            return uuid;
        }
        UUID realUUID;
        boolean cached;
        try (final KeyedLock<String, UUID> lock = MojangRequestQueue.USERNAME_REQUESTS.lock(username, (java.util.function.Supplier<UUID>)(() -> (UUID)PlayerUUIDs.OFFLINE_TO_ONLINE.get((Object)uuid)))) {
            realUUID = lock.getOrRetryValue();
            cached = (realUUID != null);
            if (realUUID == null) {
                realUUID = MojangAPI.requestUsernameToUUID(username);
                if (realUUID == null) {
                    ProfileLogger.debug("Caching null for {} ({}) because it doesn't exist.", username, uuid);
                    realUUID = PlayerUUIDs.IDENTITY_UUID;
                }
                else {
                    PlayerUUIDs.ONLINE_TO_OFFLINE.put((Object)realUUID, (Object)uuid);
                }
                PlayerUUIDs.OFFLINE_TO_ONLINE.put((Object)uuid, (Object)realUUID);
                PlayerUUIDs.USERNAME_TO_ONLINE.put((Object)username, (Object)realUUID);
            }
        }
        catch (final IOException e) {
            throw new IllegalStateException("Error while getting real UUID of player: " + username + " (" + (Object)uuid + ')', (Throwable)e);
        }
        if (realUUID == PlayerUUIDs.IDENTITY_UUID) {
            ProfileLogger.debug("Providing null UUID for {} ({}) because it doesn't exist.", username, uuid);
            realUUID = null;
        }
        else {
            ProfileLogger.debug((cached ? "Cached " : "") + "Real UUID for {} ({}) is {}", username, uuid, realUUID);
        }
        final UUID offlineUUID = getOfflineUUID(username);
        if (!uuid.equals((Object)offlineUUID) && !uuid.equals((Object)realUUID)) {
            throw new IllegalArgumentException("The provided UUID (" + (Object)uuid + ") for '" + username + "' doesn't match the offline UUID (" + (Object)offlineUUID + ") or the real UUID (" + (Object)realUUID + ')');
        }
        return realUUID;
    }
    
    static {
        IDENTITY_UUID = new UUID(0L, 0L);
        UUID_NO_DASHES = Pattern.compile("([0-9a-fA-F]{8})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]{12})");
        OFFLINE_TO_ONLINE = (Map)new HashMap();
        ONLINE_TO_OFFLINE = (Map)new HashMap();
        USERNAME_TO_ONLINE = (Map)new HashMap();
    }
}
