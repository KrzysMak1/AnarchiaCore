package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.profiles.objects;

import java.time.Duration;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.minecraft.MinecraftPackage;
import java.lang.invoke.MethodHandle;
import org.bukkit.profile.PlayerProfile;
import com.google.common.base.Strings;
import java.util.Optional;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.profiles.exceptions.UnknownPlayerException;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.profiles.exceptions.InvalidProfileException;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.profiles.objects.cache.TimedCacheableProfileable;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.profiles.mojang.MojangAPI;
import java.util.concurrent.CompletionStage;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.ItemStack;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.block.BlockState;
import org.bukkit.OfflinePlayer;
import java.util.Iterator;
import java.util.UUID;
import java.util.Map;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.profiles.PlayerUUIDs;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import com.google.common.base.Function;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.profiles.mojang.ProfileRequestConfiguration;
import java.util.Collection;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.XReflection;
import java.util.concurrent.Executor;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.profiles.mojang.PlayerProfileFetcherThread;
import java.util.concurrent.CompletableFuture;
import java.util.List;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.profiles.objects.transformer.TransformableProfile;
import java.util.Arrays;
import org.jetbrains.annotations.NotNull;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.profiles.objects.transformer.ProfileTransformer;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.profiles.PlayerProfiles;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.profiles.exceptions.ProfileException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import com.mojang.authlib.GameProfile;

public interface Profileable
{
    @Nullable
    @ApiStatus.Internal
    GameProfile getProfile();
    
    @Contract(pure = true)
    boolean isReady();
    
    @Nullable
    @Contract("-> new")
    default ProfileException test() {
        try {
            this.getProfile();
            return null;
        }
        catch (final ProfileException ex) {
            return ex;
        }
    }
    
    @Nullable
    @ApiStatus.Internal
    @Contract("-> new")
    default GameProfile getDisposableProfile() {
        final GameProfile profile = this.getProfile();
        return (profile == null) ? null : PlayerProfiles.clone(profile);
    }
    
    @NotNull
    @Contract(value = "_ -> new", pure = true)
    default Profileable transform(@NotNull final ProfileTransformer... transformers) {
        return new TransformableProfile(this, (List<ProfileTransformer>)Arrays.asList((Object[])transformers));
    }
    
    @Nullable
    default String getProfileValue() {
        return PlayerProfiles.getOriginalValue(this.getProfile());
    }
    
    @NotNull
    @Contract("-> new")
    @ApiStatus.Experimental
    default CompletableFuture<Profileable> prepare() {
        if (this.isReady()) {
            return (CompletableFuture<Profileable>)CompletableFuture.completedFuture((Object)this);
        }
        return XReflection.stacktrace((java.util.concurrent.CompletableFuture<Profileable>)CompletableFuture.supplyAsync(this::getProfile, (Executor)PlayerProfileFetcherThread.EXECUTOR).thenApply(x -> this));
    }
    
    @NotNull
    @Contract("_ -> new")
    @ApiStatus.Experimental
    default <C extends Collection<Profileable>> CompletableFuture<C> prepare(@NotNull final C profileables) {
        return prepare(profileables, null, null);
    }
    
    @NotNull
    @Contract("_, _, _ -> new")
    @ApiStatus.Experimental
    default <C extends Collection<Profileable>> CompletableFuture<C> prepare(@NotNull final C profileables, @Nullable final ProfileRequestConfiguration config, @Nullable final Function<Throwable, Boolean> errorHandler) {
        Objects.requireNonNull((Object)profileables, "Profile list is null");
        if (profileables.isEmpty()) {
            return (CompletableFuture<C>)CompletableFuture.completedFuture((Object)profileables);
        }
        CompletableFuture<Map<UUID, String>> initial = (CompletableFuture<Map<UUID, String>>)CompletableFuture.completedFuture((Object)new HashMap());
        final List<String> usernameRequests = (List<String>)new ArrayList();
        if (!PlayerUUIDs.isOnlineMode()) {
            for (final Profileable profileable : profileables) {
                String username = null;
                if (profileable instanceof UsernameProfileable) {
                    username = ((UsernameProfileable)profileable).username;
                }
                else if (profileable instanceof PlayerProfileable) {
                    username = ((PlayerProfileable)profileable).username;
                }
                else if (profileable instanceof StringProfileable && ((StringProfileable)profileable).determineType().type == ProfileInputType.USERNAME) {
                    username = ((StringProfileable)profileable).string;
                }
                if (username != null) {
                    usernameRequests.add((Object)username);
                }
            }
            if (usernameRequests.size() > 1) {
                initial = (CompletableFuture<Map<UUID, String>>)CompletableFuture.supplyAsync(() -> MojangAPI.usernamesToUUIDs((Collection<String>)usernameRequests, config), (Executor)PlayerProfileFetcherThread.EXECUTOR);
            }
        }
        return XReflection.stacktrace((java.util.concurrent.CompletableFuture<C>)initial.thenCompose(a -> {
            final List<CompletableFuture<GameProfile>> profileTasks = (List<CompletableFuture<GameProfile>>)new ArrayList(profileables.size());
            for (final Profileable profileable : profileables) {
                CompletableFuture<GameProfile> profileTask;
                if (profileable.isReady()) {
                    profileTask = (CompletableFuture<GameProfile>)CompletableFuture.completedFuture((Object)profileable.getProfile());
                }
                else {
                    final Profileable profileable2 = profileable;
                    Objects.requireNonNull((Object)profileable2);
                    profileTask = (CompletableFuture<GameProfile>)CompletableFuture.supplyAsync(profileable2::getProfile, (Executor)PlayerProfileFetcherThread.EXECUTOR);
                    if (errorHandler != null) {
                        profileTask = (CompletableFuture<GameProfile>)XReflection.stacktrace(profileTask).exceptionally(ex -> {
                            final boolean rethrow = (boolean)errorHandler.apply((Object)ex);
                            if (rethrow) {
                                throw XReflection.throwCheckedException(ex);
                            }
                            return null;
                        });
                    }
                }
                profileTasks.add((Object)profileTask);
            }
            return (CompletionStage)CompletableFuture.allOf((CompletableFuture[])profileTasks.toArray((Object[])new CompletableFuture[0]));
        }).thenApply(a -> profileables));
    }
    
    @NotNull
    @Contract(pure = true)
    default Profileable username(@NotNull final String username) {
        return new UsernameProfileable(username);
    }
    
    @NotNull
    @Contract(pure = true)
    default Profileable of(@NotNull final UUID uuid) {
        return new UUIDProfileable(uuid);
    }
    
    @NotNull
    @Contract(pure = true)
    default Profileable of(@NotNull final GameProfile profile, final boolean fetchTexturesIfNeeded) {
        return fetchTexturesIfNeeded ? new RawGameProfileProfileable(profile) : new DynamicGameProfileProfileable(profile);
    }
    
    @NotNull
    @Contract(pure = true)
    default Profileable of(@NotNull final OfflinePlayer offlinePlayer) {
        return new PlayerProfileable(offlinePlayer);
    }
    
    @NotNull
    @Contract(pure = true)
    default Profileable of(@NotNull final BlockState blockState) {
        return new ProfileContainer.BlockStateProfileContainer((Skull)blockState);
    }
    
    @NotNull
    @Contract(pure = true)
    default Profileable of(@NotNull final Block block) {
        return new ProfileContainer.BlockProfileContainer(block);
    }
    
    @NotNull
    @Contract(pure = true)
    default Profileable of(@NotNull final ItemStack item) {
        return new ProfileContainer.ItemStackProfileContainer(item);
    }
    
    @NotNull
    @Contract(pure = true)
    default Profileable of(@NotNull final ItemMeta meta) {
        return new ProfileContainer.ItemMetaProfileContainer((SkullMeta)meta);
    }
    
    @NotNull
    @Contract(pure = true)
    default Profileable detect(@NotNull final String input) {
        return new StringProfileable(input, null);
    }
    
    @NotNull
    @Contract(pure = true)
    default Profileable of(@NotNull final ProfileInputType type, @NotNull final String input) {
        Objects.requireNonNull((Object)type, () -> "Cannot profile from a null input type: " + input);
        Objects.requireNonNull((Object)input, () -> "Cannot profile from a null input: " + (Object)type);
        return new StringProfileable(input, type);
    }
    
    @ApiStatus.Internal
    public static final class UsernameProfileable extends TimedCacheableProfileable
    {
        private final String username;
        private Boolean valid;
        
        public UsernameProfileable(final String username) {
            this.username = (String)Objects.requireNonNull((Object)username);
        }
        
        @Override
        public String getProfileValue() {
            return this.username;
        }
        
        @Override
        protected GameProfile cacheProfile() {
            if (this.valid == null) {
                this.valid = ProfileInputType.USERNAME.pattern.matcher((CharSequence)this.username).matches();
            }
            if (!this.valid) {
                throw new InvalidProfileException(this.username, "Invalid username: '" + this.username + '\'');
            }
            final Optional<GameProfile> profileOpt = MojangAPI.getMojangCachedProfileFromUsername(this.username);
            if (!profileOpt.isPresent()) {
                throw new UnknownPlayerException((Object)this.username, "Cannot find player named '" + this.username + '\'');
            }
            final GameProfile profile = (GameProfile)profileOpt.get();
            if (PlayerProfiles.hasTextures(profile)) {
                return profile;
            }
            return MojangAPI.getOrFetchProfile(profile);
        }
    }
    
    @ApiStatus.Internal
    public static final class UUIDProfileable extends TimedCacheableProfileable
    {
        private final UUID id;
        
        public UUIDProfileable(final UUID id) {
            this.id = (UUID)Objects.requireNonNull((Object)id, "UUID cannot be null");
        }
        
        @Override
        public String getProfileValue() {
            return this.id.toString();
        }
        
        @Override
        protected GameProfile cacheProfile() {
            final GameProfile profile = MojangAPI.getCachedProfileByUUID(this.id);
            if (PlayerProfiles.hasTextures(profile)) {
                return profile;
            }
            return MojangAPI.getOrFetchProfile(profile);
        }
    }
    
    @ApiStatus.Internal
    public static final class DynamicGameProfileProfileable extends TimedCacheableProfileable
    {
        private final GameProfile profile;
        
        public DynamicGameProfileProfileable(final GameProfile profile) {
            this.profile = (GameProfile)Objects.requireNonNull((Object)profile);
        }
        
        @Override
        protected GameProfile cacheProfile() {
            if (PlayerProfiles.hasTextures(this.profile)) {
                return this.profile;
            }
            return (PlayerUUIDs.isOnlineMode() ? new UUIDProfileable(this.profile.getId()) : new UsernameProfileable(this.profile.getName())).getProfile();
        }
    }
    
    @ApiStatus.Internal
    public static final class RawGameProfileProfileable implements Profileable
    {
        private final GameProfile profile;
        
        public RawGameProfileProfileable(final GameProfile profile) {
            this.profile = (GameProfile)Objects.requireNonNull((Object)profile);
        }
        
        @Override
        public boolean isReady() {
            return true;
        }
        
        @NotNull
        @Override
        public GameProfile getProfile() {
            return this.profile;
        }
    }
    
    @ApiStatus.Internal
    public static final class PlayerProfileable extends TimedCacheableProfileable
    {
        @Nullable
        private final String username;
        @NotNull
        private final UUID id;
        
        public PlayerProfileable(final OfflinePlayer player) {
            Objects.requireNonNull((Object)player);
            this.username = player.getName();
            this.id = player.getUniqueId();
        }
        
        @Override
        public String getProfileValue() {
            return Strings.isNullOrEmpty(this.username) ? this.id.toString() : this.username;
        }
        
        @Override
        protected GameProfile cacheProfile() {
            if (Strings.isNullOrEmpty(this.username)) {
                return new UUIDProfileable(this.id).getProfile();
            }
            return new UsernameProfileable(this.username).getProfile();
        }
    }
    
    @ApiStatus.Internal
    public static final class PlayerProfileProfileable extends TimedCacheableProfileable
    {
        @NotNull
        private final PlayerProfile profile;
        @Nullable
        private PlayerProfile updated;
        private static final MethodHandle CraftPlayerProfile_buildGameProfile;
        
        public PlayerProfileProfileable(final PlayerProfile profile) {
            this.profile = (PlayerProfile)Objects.requireNonNull((Object)profile);
        }
        
        @Override
        protected GameProfile cacheProfile() {
            this.updated = (PlayerProfile)this.profile.update().join();
            if (PlayerProfileProfileable.CraftPlayerProfile_buildGameProfile == null) {
                final GameProfile gameProfile = new GameProfile(this.updated.getUniqueId(), this.updated.getName());
                final String skinURL = this.updated.getTextures().getSkin().toString();
                final String base64 = PlayerProfiles.encodeBase64("{\"textures\":{\"SKIN\":{\"url\":\"" + skinURL + "\"}}}");
                PlayerProfiles.setTexturesProperty(gameProfile, base64);
                return gameProfile;
            }
            try {
                return PlayerProfileProfileable.CraftPlayerProfile_buildGameProfile.invoke(this.updated);
            }
            catch (final Throwable e) {
                throw new RuntimeException(e);
            }
        }
        
        static {
            CraftPlayerProfile_buildGameProfile = XReflection.ofMinecraft().inPackage(MinecraftPackage.CB, "profile").named("CraftPlayerProfile").method("public com.mojang.authlib.GameProfile buildGameProfile()").reflectOrNull();
        }
    }
    
    @ApiStatus.Internal
    public static final class StringProfileable extends TimedCacheableProfileable
    {
        private final String string;
        @Nullable
        private ProfileInputType type;
        
        public StringProfileable(final String string, @Nullable final ProfileInputType type) {
            this.string = (String)Objects.requireNonNull((Object)string, "Input string is null");
            this.type = type;
        }
        
        @Override
        public String getProfileValue() {
            return this.string;
        }
        
        @Override
        protected Duration expiresAfter() {
            this.determineType();
            if (this.type == null) {
                return Duration.ZERO;
            }
            switch (this.type) {
                case USERNAME:
                case UUID: {
                    return super.expiresAfter();
                }
                default: {
                    return Duration.ZERO;
                }
            }
        }
        
        private StringProfileable determineType() {
            if (this.type == null) {
                this.type = ProfileInputType.typeOf(this.string);
            }
            return this;
        }
        
        @Override
        protected GameProfile cacheProfile() {
            this.determineType();
            if (this.type == null) {
                throw new InvalidProfileException(this.string, "Unknown skull string value: " + this.string);
            }
            return this.type.getProfile(this.string);
        }
    }
}
