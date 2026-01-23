package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.profiles.objects;

import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;
import org.bukkit.block.Block;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.profiles.PlayerProfiles;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.profiles.ProfilesCore;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.profiles.exceptions.InvalidProfileContainerException;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.Objects;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.mojang.authlib.GameProfile;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public abstract class ProfileContainer<T> implements Profileable
{
    @NotNull
    public abstract void setProfile(@Nullable final GameProfile p0);
    
    @NotNull
    public abstract T getObject();
    
    @Override
    public boolean isReady() {
        return true;
    }
    
    @Override
    public final String toString() {
        return this.getClass().getSimpleName() + '[' + this.getObject() + ']';
    }
    
    public static final class ItemStackProfileContainer extends ProfileContainer<ItemStack> implements DelegateProfileable
    {
        private final ItemStack itemStack;
        
        public ItemStackProfileContainer(final ItemStack itemStack) {
            this.itemStack = (ItemStack)Objects.requireNonNull((Object)itemStack, "ItemStack is null");
        }
        
        private ItemMetaProfileContainer getMetaContainer(final ItemMeta meta) {
            if (!(meta instanceof SkullMeta)) {
                throw new InvalidProfileContainerException(this.itemStack, "Item can't contain texture: " + (Object)this.itemStack);
            }
            return new ItemMetaProfileContainer((SkullMeta)meta);
        }
        
        @Override
        public void setProfile(final GameProfile profile) {
            final ItemMeta meta = this.itemStack.getItemMeta();
            this.getMetaContainer(meta).setProfile(profile);
            this.itemStack.setItemMeta(meta);
        }
        
        @Override
        public ItemStack getObject() {
            return this.itemStack;
        }
        
        @Override
        public Profileable getDelegateProfile() {
            return this.getMetaContainer(this.itemStack.getItemMeta());
        }
    }
    
    public static final class ItemMetaProfileContainer extends ProfileContainer<ItemMeta>
    {
        private final ItemMeta meta;
        
        public ItemMetaProfileContainer(final SkullMeta meta) {
            this.meta = (ItemMeta)Objects.requireNonNull((Object)meta, "ItemMeta is null");
        }
        
        @Override
        public void setProfile(final GameProfile profile) {
            try {
                ProfilesCore.CraftMetaSkull_profile$setter.invoke(this.meta, PlayerProfiles.wrapProfile(profile));
            }
            catch (final Throwable throwable) {
                throw new IllegalStateException("Unable to set profile " + (Object)profile + " to " + (Object)this.meta, throwable);
            }
        }
        
        @Override
        public ItemMeta getObject() {
            return this.meta;
        }
        
        @Override
        public GameProfile getProfile() {
            try {
                return PlayerProfiles.unwrapProfile(ProfilesCore.CraftMetaSkull_profile$getter.invoke((SkullMeta)this.meta));
            }
            catch (final Throwable throwable) {
                throw new IllegalStateException("Failed to get profile from item meta: " + (Object)this.meta, throwable);
            }
        }
    }
    
    public static final class BlockProfileContainer extends ProfileContainer<Block> implements DelegateProfileable
    {
        private final Block block;
        
        public BlockProfileContainer(final Block block) {
            this.block = (Block)Objects.requireNonNull((Object)block, "Block is null");
        }
        
        private Skull getBlockState() {
            final BlockState state = this.block.getState();
            if (!(state instanceof Skull)) {
                throw new InvalidProfileContainerException(this.block, "Block can't contain texture: " + (Object)this.block);
            }
            return (Skull)state;
        }
        
        @Override
        public void setProfile(final GameProfile profile) {
            final Skull state = this.getBlockState();
            new BlockStateProfileContainer(state).setProfile(profile);
            state.update(true);
        }
        
        @Override
        public Block getObject() {
            return this.block;
        }
        
        @Override
        public Profileable getDelegateProfile() {
            return new BlockStateProfileContainer(this.getBlockState());
        }
    }
    
    public static final class BlockStateProfileContainer extends ProfileContainer<Skull>
    {
        private final Skull state;
        
        public BlockStateProfileContainer(final Skull state) {
            this.state = (Skull)Objects.requireNonNull((Object)state, "Skull BlockState is null");
        }
        
        @Override
        public void setProfile(final GameProfile profile) {
            try {
                ProfilesCore.CraftSkull_profile$setter.invoke(this.state, PlayerProfiles.wrapProfile(profile));
            }
            catch (final Throwable throwable) {
                throw new IllegalStateException("Unable to set profile " + (Object)profile + " to " + (Object)this.state, throwable);
            }
        }
        
        @Override
        public Skull getObject() {
            return this.state;
        }
        
        @Override
        public GameProfile getProfile() {
            try {
                return PlayerProfiles.unwrapProfile(ProfilesCore.CraftSkull_profile$getter.invoke(this.state));
            }
            catch (final Throwable throwable) {
                throw new IllegalStateException("Unable to get profile fr om blockstate: " + (Object)this.state, throwable);
            }
        }
    }
}
