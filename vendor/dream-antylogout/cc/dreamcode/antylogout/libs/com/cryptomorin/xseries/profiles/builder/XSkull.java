package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.profiles.builder;

import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.profiles.objects.ProfileInputType;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.profiles.PlayerProfiles;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.profiles.objects.Profileable;
import org.bukkit.block.Skull;
import org.bukkit.block.BlockState;
import org.bukkit.block.Block;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.inventory.meta.ItemMeta;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.profiles.objects.ProfileContainer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.XMaterial;
import org.bukkit.inventory.ItemStack;
import com.mojang.authlib.GameProfile;

public final class XSkull
{
    private static final GameProfile DEFAULT_PROFILE;
    
    @NotNull
    @Contract(value = "-> new", pure = true)
    public static ProfileInstruction<ItemStack> createItem() {
        return of(XMaterial.PLAYER_HEAD.parseItem());
    }
    
    @NotNull
    @Contract(value = "_ -> new", pure = true)
    public static ProfileInstruction<ItemStack> of(@NotNull final ItemStack stack) {
        return new ProfileInstruction<ItemStack>(new ProfileContainer.ItemStackProfileContainer(stack));
    }
    
    @NotNull
    @Contract(value = "_ -> new", pure = true)
    public static ProfileInstruction<ItemMeta> of(@NotNull final ItemMeta meta) {
        return new ProfileInstruction<ItemMeta>(new ProfileContainer.ItemMetaProfileContainer((SkullMeta)meta));
    }
    
    @NotNull
    @Contract(value = "_ -> new", pure = true)
    public static ProfileInstruction<Block> of(@NotNull final Block block) {
        return new ProfileInstruction<Block>(new ProfileContainer.BlockProfileContainer(block));
    }
    
    @NotNull
    @Contract(value = "_ -> new", pure = true)
    public static ProfileInstruction<Skull> of(@NotNull final BlockState state) {
        return new ProfileInstruction<Skull>(new ProfileContainer.BlockStateProfileContainer((Skull)state));
    }
    
    @NotNull
    @Contract(value = "-> new", pure = true)
    protected static Profileable getDefaultProfile() {
        return Profileable.of(PlayerProfiles.clone(XSkull.DEFAULT_PROFILE), false);
    }
    
    static {
        DEFAULT_PROFILE = PlayerProfiles.signXSeries(ProfileInputType.BASE64.getProfile("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzEwNTkxZTY5MDllNmEyODFiMzcxODM2ZTQ2MmQ2N2EyYzc4ZmEwOTUyZTkxMGYzMmI0MWEyNmM0OGMxNzU3YyJ9fX0="));
    }
}
