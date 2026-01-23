package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.profiles;

import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.minecraft.MinecraftClassHandle;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.ReflectiveNamespace;
import java.util.Objects;
import com.mojang.authlib.properties.Property;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.ReflectiveHandle;
import java.util.concurrent.Callable;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm.FlaggedNamedMemberHandle;
import java.util.function.Consumer;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm.FieldMemberHandle;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.minecraft.MinecraftMapping;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.GameProfile;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm.MethodMemberHandle;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.XReflection;
import java.lang.invoke.MethodHandle;
import java.util.UUID;
import java.util.Map;
import com.google.common.cache.LoadingCache;
import java.net.Proxy;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public final class ProfilesCore
{
    public static final Object USER_CACHE;
    public static final Object MINECRAFT_SESSION_SERVICE;
    public static final Proxy PROXY;
    public static final LoadingCache<Object, Object> YggdrasilMinecraftSessionService_insecureProfiles;
    public static final boolean SUPPORTS_BUKKIT_PlayerProfile;
    public static final Map<String, Object> UserCache_profilesByName;
    public static final Map<UUID, Object> UserCache_profilesByUUID;
    public static final MethodHandle MinecraftSessionService_fillProfileProperties;
    public static final MethodHandle GameProfileCache_get$profileByName$;
    public static final MethodHandle GameProfileCache_get$profileByUUID$;
    public static final MethodHandle CACHE_PROFILE;
    public static final MethodHandle CraftMetaSkull_profile$getter;
    public static final MethodHandle CraftMetaSkull_profile$setter;
    public static final MethodHandle CraftSkull_profile$setter;
    public static final MethodHandle CraftSkull_profile$getter;
    public static final MethodHandle Property_getValue;
    public static final MethodHandle UserCache_getNextOperation;
    public static final MethodHandle UserCacheEntry_getProfile;
    public static final MethodHandle UserCacheEntry_setLastAccess;
    public static final MethodHandle ResolvableProfile$constructor;
    public static final MethodHandle ResolvableProfile_gameProfile;
    public static final boolean ResolvableProfile$bukkitSupports;
    public static final boolean NULLABILITY_RECORD_UPDATE;
    
    static {
        NULLABILITY_RECORD_UPDATE = XReflection.supports(1, 20, 2);
        Object insecureProfiles = null;
        MethodHandle fillProfileProperties = null;
        MethodHandle newResolvableProfile = null;
        MethodHandle $ResolvableProfile_gameProfile = null;
        boolean bukkitUsesResolvableProfile = false;
        final ReflectiveNamespace ns = XReflection.namespaced().imports(GameProfile.class, MinecraftSessionService.class, LoadingCache.class);
        final MinecraftClassHandle GameProfileCache = ns.ofMinecraft("package nms.server.players; public class GameProfileCache").map(MinecraftMapping.SPIGOT, "UserCache");
        SUPPORTS_BUKKIT_PlayerProfile = ns.ofMinecraft("package org.bukkit; public interface OfflinePlayer").method("org.bukkit.profile.PlayerProfile getPlayerProfile()").exists();
        MethodHandle profileGetterMeta;
        MethodHandle profileSetterMeta;
        Object minecraftSessionService;
        Object userCache;
        MethodHandle getProfileByName;
        MethodHandle getProfileByUUID;
        MethodHandle cacheProfile;
        Proxy proxy;
        try {
            final MinecraftClassHandle CraftMetaSkull = ns.ofMinecraft("package cb.inventory; class CraftMetaSkull extends CraftMetaItem implements SkullMeta");
            final MinecraftClassHandle ResolvableProfile = ns.ofMinecraft("package nms.world.item.component; public class ResolvableProfile");
            if (ResolvableProfile.exists()) {
                newResolvableProfile = ResolvableProfile.constructor("public ResolvableProfile(GameProfile gameProfile)").reflect();
                $ResolvableProfile_gameProfile = ResolvableProfile.method("public GameProfile gameProfile()").map(MinecraftMapping.OBFUSCATED, XReflection.v(21, 6, "g").orElse("f")).reflect();
                bukkitUsesResolvableProfile = CraftMetaSkull.field("private ResolvableProfile profile").exists();
            }
            profileGetterMeta = XReflection.any(CraftMetaSkull.field("private ResolvableProfile profile"), CraftMetaSkull.field("private GameProfile       profile")).modify((java.util.function.Consumer<FieldMemberHandle>)FieldMemberHandle::getter).reflect();
            profileSetterMeta = XReflection.any(CraftMetaSkull.method("private void setProfile(ResolvableProfile profile)"), CraftMetaSkull.method("private void setProfile(GameProfile       profile)"), CraftMetaSkull.field("private                 GameProfile       profile ").setter()).reflect();
            final MinecraftClassHandle MinecraftServer = ns.ofMinecraft("package nms.server; public abstract class MinecraftServer");
            final Object minecraftServer = MinecraftServer.method("public static MinecraftServer getServer()").reflect().invoke();
            minecraftSessionService = MinecraftServer.method("public MinecraftSessionService getSessionService()").named("aq", "ay", "getMinecraftSessionService", "az", "ao", "am", "aD", "ar", "ap").reflect().invoke(minecraftServer);
            final FieldMemberHandle insecureProfilesFieldHandle = ns.ofMinecraft("package com.mojang.authlib.yggdrasil;public class YggdrasilMinecraftSessionService implements MinecraftSessionService").field().getter();
            if (ProfilesCore.NULLABILITY_RECORD_UPDATE) {
                insecureProfilesFieldHandle.signature("private final LoadingCache<UUID, Optional<ProfileResult>> insecureProfiles");
            }
            else {
                insecureProfilesFieldHandle.signature("private final LoadingCache<GameProfile, GameProfile> insecureProfiles");
            }
            final MethodHandle insecureProfilesField = insecureProfilesFieldHandle.reflectOrNull();
            if (insecureProfilesField != null) {
                insecureProfiles = insecureProfilesField.invoke(minecraftSessionService);
            }
            userCache = MinecraftServer.method("public GameProfileCache getProfileCache()").named("at", "ar", "ao", "ap", "au").map(MinecraftMapping.OBFUSCATED, "getUserCache").reflect().invoke(minecraftServer);
            if (!ProfilesCore.NULLABILITY_RECORD_UPDATE) {
                fillProfileProperties = ns.of(MinecraftSessionService.class).method("public GameProfile fillProfileProperties(GameProfile profile, boolean flag)").reflect();
            }
            UserCache_getNextOperation = GameProfileCache.method("private long getNextOperation()").map(MinecraftMapping.OBFUSCATED, XReflection.v(21, "e").v(16, "d").orElse("d")).reflectOrNull();
            final MethodMemberHandle profileByName = GameProfileCache.method().named("getProfile", "a");
            final MethodMemberHandle profileByUUID = GameProfileCache.method().named("getProfile", "a");
            getProfileByName = XReflection.anyOf(() -> profileByName.signature("public          GameProfile  get(String username)"), () -> profileByName.signature("public Optional<GameProfile> get(String username)")).reflect();
            getProfileByUUID = XReflection.anyOf(() -> profileByUUID.signature("public          GameProfile  get(UUID id)"), () -> profileByUUID.signature("public Optional<GameProfile> get(UUID id)")).reflect();
            cacheProfile = GameProfileCache.method("public void add(GameProfile profile)").map(MinecraftMapping.OBFUSCATED, "a").reflect();
            try {
                proxy = MinecraftServer.field("protected final java.net.Proxy proxy").getter().map(MinecraftMapping.OBFUSCATED, XReflection.v(20, 5, "h").v(20, 3, "i").v(19, "j").v(18, 2, "n").v(18, "o").v(17, "m").v(14, "proxy").v(13, "c").orElse("e")).reflect().invoke(minecraftServer);
            }
            catch (final Throwable ex) {
                ProfileLogger.LOGGER.error("Failed to initialize server proxy settings", ex);
                proxy = null;
            }
        }
        catch (final Throwable throwable) {
            throw XReflection.throwCheckedException(throwable);
        }
        final MinecraftClassHandle CraftSkull = ns.ofMinecraft("package cb.block; public class CraftSkull extends CraftBlockEntityState implements Skull");
        final FieldMemberHandle CraftSkull_profile = XReflection.any(CraftSkull.field("private ResolvableProfile profile"), CraftSkull.field("private GameProfile profile")).getHandle();
        Property_getValue = (ProfilesCore.NULLABILITY_RECORD_UPDATE ? null : ns.of(Property.class).method("public String getValue()").unreflect());
        PROXY = proxy;
        USER_CACHE = userCache;
        CACHE_PROFILE = cacheProfile;
        MINECRAFT_SESSION_SERVICE = minecraftSessionService;
        Objects.requireNonNull(insecureProfiles, () -> "Couldn't find Mojang's insecureProfiles cache " + XReflection.getVersionInformation());
        YggdrasilMinecraftSessionService_insecureProfiles = (LoadingCache)insecureProfiles;
        MinecraftSessionService_fillProfileProperties = fillProfileProperties;
        GameProfileCache_get$profileByName$ = getProfileByName;
        GameProfileCache_get$profileByUUID$ = getProfileByUUID;
        CraftMetaSkull_profile$setter = profileSetterMeta;
        CraftMetaSkull_profile$getter = profileGetterMeta;
        CraftSkull_profile$setter = CraftSkull_profile.setter().unreflect();
        CraftSkull_profile$getter = CraftSkull_profile.getter().unreflect();
        ResolvableProfile$constructor = newResolvableProfile;
        ResolvableProfile_gameProfile = $ResolvableProfile_gameProfile;
        ResolvableProfile$bukkitSupports = bukkitUsesResolvableProfile;
        final MinecraftClassHandle UserCacheEntry = GameProfileCache.inner("private static class GameProfileInfo").map(MinecraftMapping.SPIGOT, "UserCacheEntry");
        UserCacheEntry_getProfile = UserCacheEntry.method("public GameProfile getProfile()").map(MinecraftMapping.OBFUSCATED, "a").makeAccessible().unreflect();
        UserCacheEntry_setLastAccess = UserCacheEntry.method("public void setLastAccess(long i)").map(MinecraftMapping.OBFUSCATED, "a").reflectOrNull();
        try {
            UserCache_profilesByName = GameProfileCache.field("private final Map<String, UserCache.UserCacheEntry> profilesByName").getter().map(MinecraftMapping.OBFUSCATED, XReflection.v(17, "e").v(16, 2, "c").v(9, "d").orElse("c")).reflect().invoke(userCache);
            UserCache_profilesByUUID = GameProfileCache.field("private final Map<UUID, UserCache.UserCacheEntry> profilesByUUID").getter().map(MinecraftMapping.OBFUSCATED, XReflection.v(17, "f").v(16, 2, "d").v(9, "e").orElse("d")).reflect().invoke(userCache);
        }
        catch (final Throwable e) {
            throw new IllegalStateException("Failed to initialize ProfilesCore", e);
        }
    }
}
