package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries;

import java.util.HashMap;
import java.util.EnumMap;
import java.util.Collections;
import java.util.EnumSet;
import org.bukkit.block.data.Rotatable;
import org.bukkit.block.data.Openable;
import org.bukkit.block.data.Powerable;
import org.bukkit.block.data.type.EndPortalFrame;
import org.bukkit.block.data.type.Cake;
import java.util.Optional;
import java.util.Iterator;
import java.util.Locale;
import java.util.Collection;
import org.bukkit.block.data.Levelled;
import org.bukkit.block.data.Ageable;
import org.bukkit.SkullType;
import org.bukkit.block.Skull;
import org.bukkit.TreeSpecies;
import org.bukkit.block.Banner;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.bukkit.block.data.Directional;
import org.bukkit.block.BlockFace;
import org.bukkit.Material;
import org.bukkit.material.MaterialData;
import org.bukkit.block.BlockState;
import org.bukkit.material.Wool;
import org.bukkit.material.Colorable;
import org.bukkit.DyeColor;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.Nullable;
import org.bukkit.block.data.Lightable;
import org.bukkit.block.Block;
import java.util.Map;
import java.util.Set;

public final class XBlock
{
    @Deprecated
    public static final Set<XMaterial> CROPS;
    @Deprecated
    public static final Set<XMaterial> DANGEROUS;
    public static final byte CAKE_SLICES = 6;
    private static final boolean ISFLAT;
    private static final Map<XMaterial, XMaterial> ITEM_TO_BLOCK;
    
    private XBlock() {
    }
    
    public static boolean isLit(final Block block) {
        if (!XBlock.ISFLAT) {
            return isMaterial(block, BlockMaterial.REDSTONE_LAMP_ON, BlockMaterial.REDSTONE_TORCH_ON, BlockMaterial.BURNING_FURNACE);
        }
        if (!(block.getBlockData() instanceof Lightable)) {
            return false;
        }
        final Lightable lightable = (Lightable)block.getBlockData();
        return lightable.isLit();
    }
    
    public static boolean isContainer(@Nullable final Block block) {
        return block != null && block.getState() instanceof InventoryHolder;
    }
    
    public static void setLit(final Block block, final boolean lit) {
        if (!XBlock.ISFLAT) {
            final String name = block.getType().name();
            if (name.endsWith("FURNACE")) {
                block.setType(BlockMaterial.BURNING_FURNACE.material);
            }
            else if (name.startsWith("REDSTONE_LAMP")) {
                block.setType(BlockMaterial.REDSTONE_LAMP_ON.material);
            }
            else {
                block.setType(BlockMaterial.REDSTONE_TORCH_ON.material);
            }
            return;
        }
        if (!(block.getBlockData() instanceof Lightable)) {
            return;
        }
        final BlockData data = block.getBlockData();
        final Lightable lightable = (Lightable)data;
        lightable.setLit(lit);
        block.setBlockData(data, false);
    }
    
    @Deprecated
    public static boolean isCrop(final XMaterial material) {
        return XBlock.CROPS.contains((Object)material);
    }
    
    @Deprecated
    public static boolean isDangerous(final XMaterial material) {
        return XBlock.DANGEROUS.contains((Object)material);
    }
    
    public static DyeColor getColor(final Block block) {
        if (XBlock.ISFLAT) {
            if (!(block.getBlockData() instanceof Colorable)) {
                return null;
            }
            final Colorable colorable = (Colorable)block.getBlockData();
            return colorable.getColor();
        }
        else {
            final BlockState state = block.getState();
            final MaterialData data = state.getData();
            if (data instanceof Wool) {
                final Wool wool = (Wool)data;
                return wool.getColor();
            }
            return null;
        }
    }
    
    public static boolean isCake(@Nullable final Material material) {
        if (!XBlock.ISFLAT) {
            return material == BlockMaterial.CAKE_BLOCK.material;
        }
        return material == Material.CAKE;
    }
    
    public static boolean isWheat(@Nullable final Material material) {
        if (!XBlock.ISFLAT) {
            return material == BlockMaterial.CROPS.material;
        }
        return material == Material.WHEAT;
    }
    
    public static boolean isSugarCane(@Nullable final Material material) {
        if (!XBlock.ISFLAT) {
            return material == BlockMaterial.SUGAR_CANE_BLOCK.material;
        }
        return material == Material.SUGAR_CANE;
    }
    
    public static boolean isBeetroot(@Nullable final Material material) {
        if (!XBlock.ISFLAT) {
            return material != null && material == BlockMaterial.BEETROOT_BLOCK.material;
        }
        return material == Material.BEETROOTS;
    }
    
    public static boolean isNetherWart(@Nullable final Material material) {
        if (!XBlock.ISFLAT) {
            return material == BlockMaterial.NETHER_WARTS.material;
        }
        return material == Material.NETHER_WART;
    }
    
    public static boolean isCarrot(@Nullable final Material material) {
        if (!XBlock.ISFLAT) {
            return material == Material.CARROT;
        }
        return material == Material.CARROTS;
    }
    
    public static boolean isMelon(@Nullable final Material material) {
        if (!XBlock.ISFLAT) {
            return material == BlockMaterial.MELON_BLOCK.material;
        }
        return material == Material.MELON;
    }
    
    public static boolean isPotato(@Nullable final Material material) {
        if (!XBlock.ISFLAT) {
            return material == Material.POTATO;
        }
        return material == Material.POTATOES;
    }
    
    public static BlockFace getDirection(final Block block) {
        if (XBlock.ISFLAT) {
            if (!(block.getBlockData() instanceof Directional)) {
                return BlockFace.SELF;
            }
            final Directional direction = (Directional)block.getBlockData();
            return direction.getFacing();
        }
        else {
            final BlockState state = block.getState();
            final MaterialData data = state.getData();
            if (data instanceof org.bukkit.material.Directional) {
                return ((org.bukkit.material.Directional)data).getFacing();
            }
            return BlockFace.SELF;
        }
    }
    
    public static boolean setDirection(final Block block, BlockFace facing) {
        if (XBlock.ISFLAT) {
            if (!(block.getBlockData() instanceof Directional)) {
                return false;
            }
            final BlockData data = block.getBlockData();
            final Directional direction = (Directional)data;
            direction.setFacing(facing);
            block.setBlockData(data, false);
            return true;
        }
        else {
            final BlockState state = block.getState();
            final MaterialData data2 = state.getData();
            if (data2 instanceof org.bukkit.material.Directional) {
                if (XMaterial.matchXMaterial(block.getType()) == XMaterial.LADDER) {
                    facing = facing.getOppositeFace();
                }
                ((org.bukkit.material.Directional)data2).setFacingDirection(facing);
                state.update(true);
                return true;
            }
            return false;
        }
    }
    
    public static boolean setType(@NotNull final Block block, @Nullable XMaterial material, final boolean applyPhysics) {
        Objects.requireNonNull((Object)block, "Cannot set type of null block");
        if (material == null) {
            material = XMaterial.AIR;
        }
        final XMaterial smartConversion = (XMaterial)XBlock.ITEM_TO_BLOCK.get((Object)material);
        if (smartConversion != null) {
            material = smartConversion;
        }
        Material parsedMat = material.get();
        if (parsedMat == null) {
            return false;
        }
        final String parsedName = parsedMat.name();
        final SkullType skullType = getSkullType(material);
        if (!XBlock.ISFLAT && (parsedName.equals((Object)"SKULL_ITEM") || skullType != null)) {
            parsedMat = Material.valueOf("SKULL");
        }
        block.setType(parsedMat, applyPhysics);
        if (XBlock.ISFLAT) {
            return false;
        }
        if (parsedName.endsWith("_ITEM")) {
            final String blockName = parsedName.substring(0, parsedName.length() - "_ITEM".length());
            final Material blockMaterial = (Material)Objects.requireNonNull((Object)Material.getMaterial(blockName), () -> "Could not find block material for item '" + parsedName + "' as '" + blockName + '\'');
            block.setType(blockMaterial, applyPhysics);
        }
        else if (parsedName.contains((CharSequence)"CAKE")) {
            final Material blockMaterial2 = Material.getMaterial("CAKE_BLOCK");
            block.setType(blockMaterial2, applyPhysics);
        }
        final LegacyMaterial legacyMaterial = getMaterial(parsedName);
        if (legacyMaterial == LegacyMaterial.BANNER) {
            block.setType(LegacyMaterial.STANDING_BANNER.material, applyPhysics);
        }
        final LegacyMaterial.Handling handling = (legacyMaterial == null) ? null : legacyMaterial.handling;
        final BlockState state = block.getState();
        boolean update = false;
        if (handling == LegacyMaterial.Handling.COLORABLE) {
            if (state instanceof Banner) {
                final Banner banner = (Banner)state;
                final String xName = material.name();
                final int colorIndex = xName.indexOf(95);
                String color = xName.substring(0, colorIndex);
                if (color.equals((Object)"LIGHT")) {
                    color = xName.substring(0, "LIGHT_".length() + 4);
                }
                banner.setBaseColor(DyeColor.valueOf(color));
            }
            else {
                state.setRawData(material.getData());
            }
            update = true;
        }
        else if (handling == LegacyMaterial.Handling.WOOD_SPECIES) {
            final String name = material.name();
            final int firstIndicator = name.indexOf(95);
            if (firstIndicator < 0) {
                return false;
            }
            final String substring;
            final String woodType = substring = name.substring(0, firstIndicator);
            int n = -1;
            switch (substring.hashCode()) {
                case 78009: {
                    if (substring.equals((Object)"OAK")) {
                        n = 0;
                        break;
                    }
                    break;
                }
                case 2090870: {
                    if (substring.equals((Object)"DARK")) {
                        n = 1;
                        break;
                    }
                    break;
                }
                case -1842339390: {
                    if (substring.equals((Object)"SPRUCE")) {
                        n = 2;
                        break;
                    }
                    break;
                }
            }
            TreeSpecies species = null;
            switch (n) {
                case 0: {
                    species = TreeSpecies.GENERIC;
                    break;
                }
                case 1: {
                    species = TreeSpecies.DARK_OAK;
                    break;
                }
                case 2: {
                    species = TreeSpecies.REDWOOD;
                    break;
                }
                default: {
                    try {
                        species = TreeSpecies.valueOf(woodType);
                    }
                    catch (final IllegalArgumentException ex) {
                        throw new AssertionError((Object)("Unknown material " + (Object)legacyMaterial + " for wood species"));
                    }
                    break;
                }
            }
            boolean firstType = false;
            switch (legacyMaterial.ordinal()) {
                case 9:
                case 11: {
                    state.setRawData(species.getData());
                    update = true;
                    break;
                }
                case 12:
                case 14: {
                    firstType = true;
                }
                case 13:
                case 15: {
                    switch (species) {
                        case GENERIC:
                        case REDWOOD:
                        case BIRCH:
                        case JUNGLE: {
                            if (!firstType) {
                                throw new AssertionError((Object)("Invalid tree species " + (Object)species + " for block type" + (Object)legacyMaterial + ", use block type 2 instead"));
                            }
                            break;
                        }
                        case ACACIA:
                        case DARK_OAK: {
                            if (firstType) {
                                throw new AssertionError((Object)("Invalid tree species " + (Object)species + " for block type 2 " + (Object)legacyMaterial + ", use block type instead"));
                            }
                            break;
                        }
                    }
                    state.setRawData((byte)((state.getRawData() & 0xC) | (species.getData() & 0x3)));
                    update = true;
                    break;
                }
                case 10:
                case 16: {
                    state.setRawData((byte)((state.getRawData() & 0x8) | species.getData()));
                    update = true;
                    break;
                }
                default: {
                    throw new AssertionError((Object)("Unknown block type " + (Object)legacyMaterial + " for tree species: " + (Object)species));
                }
            }
        }
        else if (material.getData() != 0) {
            if (skullType != null) {
                final boolean isWallSkull = material.name().contains((CharSequence)"WALL");
                state.setRawData((byte)(byte)(isWallSkull ? 0 : 1));
            }
            else {
                state.setRawData(material.getData());
            }
            update = true;
        }
        if (skullType != null) {
            final Skull skull = (Skull)state;
            skull.setSkullType(skullType);
            update = true;
        }
        if (update) {
            state.update(true, applyPhysics);
        }
        return update;
    }
    
    public static SkullType getSkullType(final XMaterial material) {
        switch (material) {
            case PLAYER_HEAD:
            case PLAYER_WALL_HEAD: {
                return SkullType.PLAYER;
            }
            case DRAGON_HEAD:
            case DRAGON_WALL_HEAD: {
                return SkullType.DRAGON;
            }
            case ZOMBIE_HEAD:
            case ZOMBIE_WALL_HEAD: {
                return SkullType.ZOMBIE;
            }
            case CREEPER_HEAD:
            case CREEPER_WALL_HEAD: {
                return SkullType.CREEPER;
            }
            case SKELETON_SKULL:
            case SKELETON_WALL_SKULL: {
                return SkullType.SKELETON;
            }
            case WITHER_SKELETON_SKULL:
            case WITHER_SKELETON_WALL_SKULL: {
                return SkullType.WITHER;
            }
            case PIGLIN_HEAD:
            case PIGLIN_WALL_HEAD: {
                return SkullType.PIGLIN;
            }
            default: {
                return null;
            }
        }
    }
    
    public static boolean setType(@NotNull final Block block, @Nullable final XMaterial material) {
        return setType(block, material, true);
    }
    
    public static int getAge(final Block block) {
        if (!XBlock.ISFLAT) {
            final BlockState state = block.getState();
            final MaterialData data = state.getData();
            return data.getData();
        }
        if (!(block.getBlockData() instanceof Ageable)) {
            return 0;
        }
        final Ageable ageable = (Ageable)block.getBlockData();
        return ageable.getAge();
    }
    
    public static void setAge(final Block block, final int age) {
        if (XBlock.ISFLAT) {
            if (!(block.getBlockData() instanceof Ageable)) {
                return;
            }
            final BlockData data = block.getBlockData();
            final Ageable ageable = (Ageable)data;
            ageable.setAge(age);
            block.setBlockData(data, false);
        }
        final BlockState state = block.getState();
        final MaterialData data2 = state.getData();
        data2.setData((byte)age);
        state.update(true);
    }
    
    public static boolean setColor(final Block block, final DyeColor color) {
        if (!XBlock.ISFLAT) {
            final BlockState state = block.getState();
            state.setRawData(color.getWoolData());
            state.update(true);
            return false;
        }
        final String type = block.getType().name();
        final int index = type.indexOf(95);
        if (index == -1) {
            return false;
        }
        final String realType = type.substring(index + 1);
        final Material material = Material.getMaterial(color.name() + '_' + realType);
        if (material == null) {
            return false;
        }
        block.setType(material);
        return true;
    }
    
    public static boolean setFluidLevel(final Block block, final int level) {
        if (!XBlock.ISFLAT) {
            final BlockState state = block.getState();
            final MaterialData data = state.getData();
            data.setData((byte)level);
            state.update(true);
            return false;
        }
        if (!(block.getBlockData() instanceof Levelled)) {
            return false;
        }
        final BlockData data2 = block.getBlockData();
        final Levelled levelled = (Levelled)data2;
        levelled.setLevel(level);
        block.setBlockData(data2, false);
        return true;
    }
    
    public static int getFluidLevel(final Block block) {
        if (!XBlock.ISFLAT) {
            final BlockState state = block.getState();
            final MaterialData data = state.getData();
            return data.getData();
        }
        if (!(block.getBlockData() instanceof Levelled)) {
            return -1;
        }
        final Levelled levelled = (Levelled)block.getBlockData();
        return levelled.getLevel();
    }
    
    public static boolean isWaterStationary(final Block block) {
        return XBlock.ISFLAT ? (getFluidLevel(block) < 7) : (block.getType() == BlockMaterial.STATIONARY_WATER.material);
    }
    
    public static boolean isWater(final Material material) {
        return material == Material.WATER || material == BlockMaterial.STATIONARY_WATER.material;
    }
    
    public static boolean isLava(final Material material) {
        return material == Material.LAVA || material == BlockMaterial.STATIONARY_LAVA.material;
    }
    
    @Deprecated
    public static boolean isOneOf(final Block block, final Collection<String> blocks) {
        if (blocks == null || blocks.isEmpty()) {
            return false;
        }
        final String name = block.getType().name();
        final XMaterial matched = XMaterial.matchXMaterial(block.getType());
        for (String comp : blocks) {
            final String checker = comp.toUpperCase(Locale.ENGLISH);
            if (checker.startsWith("CONTAINS:")) {
                comp = XMaterial.format(checker.substring(9));
                if (name.contains((CharSequence)comp)) {
                    return true;
                }
                continue;
            }
            else if (checker.startsWith("REGEX:")) {
                comp = comp.substring(6);
                if (name.matches(comp)) {
                    return true;
                }
                continue;
            }
            else {
                final Optional<XMaterial> xMat = XMaterial.matchXMaterial(comp);
                if (xMat.isPresent() && isSimilar(block, (XMaterial)xMat.get())) {
                    return true;
                }
                continue;
            }
        }
        return false;
    }
    
    public static void setCakeSlices(final Block block, final int amount) {
        if (!isCake(block.getType())) {
            throw new IllegalArgumentException("Block is not a cake: " + (Object)block.getType());
        }
        if (XBlock.ISFLAT) {
            final BlockData data = block.getBlockData();
            final Cake cake = (Cake)data;
            final int remaining = cake.getMaximumBites() - (cake.getBites() + amount);
            if (remaining > 0) {
                cake.setBites(remaining);
                block.setBlockData(data);
            }
            else {
                block.breakNaturally();
            }
            return;
        }
        final BlockState state = block.getState();
        final org.bukkit.material.Cake cake2 = (org.bukkit.material.Cake)state.getData();
        if (amount > 0) {
            cake2.setSlicesRemaining(amount);
            state.update(true);
        }
        else {
            block.breakNaturally();
        }
    }
    
    public static int addCakeSlices(final Block block, final int slices) {
        if (!isCake(block.getType())) {
            throw new IllegalArgumentException("Block is not a cake: " + (Object)block.getType());
        }
        if (XBlock.ISFLAT) {
            final BlockData data = block.getBlockData();
            final Cake cake = (Cake)data;
            final int bites = cake.getBites() - slices;
            final int remaining = cake.getMaximumBites() - bites;
            if (remaining > 0) {
                cake.setBites(bites);
                block.setBlockData(data);
                return remaining;
            }
            block.breakNaturally();
            return 0;
        }
        else {
            final BlockState state = block.getState();
            final org.bukkit.material.Cake cake2 = (org.bukkit.material.Cake)state.getData();
            final int remaining2 = cake2.getSlicesRemaining() + slices;
            if (remaining2 > 0) {
                cake2.setSlicesRemaining(remaining2);
                state.update(true);
                return remaining2;
            }
            block.breakNaturally();
            return 0;
        }
    }
    
    public static void setEnderPearlOnFrame(final Block endPortalFrame, final boolean eye) {
        final BlockState state = endPortalFrame.getState();
        if (XBlock.ISFLAT) {
            final BlockData data = state.getBlockData();
            final EndPortalFrame frame = (EndPortalFrame)data;
            frame.setEye(eye);
            state.setBlockData(data);
        }
        else {
            state.setRawData((byte)(eye ? 4 : 0));
        }
        state.update(true);
    }
    
    public static boolean isSimilar(final Block block, final XMaterial material) {
        final Material mat = block.getType();
        if (material == XMaterial.matchXMaterial(mat)) {
            return true;
        }
        switch (material) {
            case CAKE: {
                return isCake(mat);
            }
            case NETHER_WART: {
                return isNetherWart(mat);
            }
            case MELON:
            case MELON_SLICE: {
                return isMelon(mat);
            }
            case CARROT:
            case CARROTS: {
                return isCarrot(mat);
            }
            case POTATO:
            case POTATOES: {
                return isPotato(mat);
            }
            case WHEAT:
            case WHEAT_SEEDS: {
                return isWheat(mat);
            }
            case BEETROOT:
            case BEETROOT_SEEDS:
            case BEETROOTS: {
                return isBeetroot(mat);
            }
            case SUGAR_CANE: {
                return isSugarCane(mat);
            }
            case WATER: {
                return isWater(mat);
            }
            case LAVA: {
                return isLava(mat);
            }
            case AIR:
            case CAVE_AIR:
            case VOID_AIR: {
                return isAir(mat);
            }
            default: {
                return false;
            }
        }
    }
    
    public static boolean isAir(@Nullable final Material material) {
        if (!XBlock.ISFLAT) {
            return material == Material.AIR;
        }
        switch (material) {
            case AIR:
            case CAVE_AIR:
            case VOID_AIR: {
                return true;
            }
            default: {
                return false;
            }
        }
    }
    
    public static boolean isPowered(final Block block) {
        if (!XBlock.ISFLAT) {
            final String name = block.getType().name();
            return name.startsWith("REDSTONE_COMPARATOR") && block.getType() == BlockMaterial.REDSTONE_COMPARATOR_ON.material;
        }
        if (!(block.getBlockData() instanceof Powerable)) {
            return false;
        }
        final Powerable powerable = (Powerable)block.getBlockData();
        return powerable.isPowered();
    }
    
    public static void setPowered(final Block block, final boolean powered) {
        if (!XBlock.ISFLAT) {
            final String name = block.getType().name();
            if (name.startsWith("REDSTONE_COMPARATOR")) {
                block.setType(BlockMaterial.REDSTONE_COMPARATOR_ON.material);
            }
            return;
        }
        if (!(block.getBlockData() instanceof Powerable)) {
            return;
        }
        final BlockData data = block.getBlockData();
        final Powerable powerable = (Powerable)data;
        powerable.setPowered(powered);
        block.setBlockData(data, false);
    }
    
    public static boolean isOpen(final Block block) {
        if (XBlock.ISFLAT) {
            if (!(block.getBlockData() instanceof Openable)) {
                return false;
            }
            final Openable openable = (Openable)block.getBlockData();
            return openable.isOpen();
        }
        else {
            final BlockState state = block.getState();
            if (!(state instanceof org.bukkit.material.Openable)) {
                return false;
            }
            final org.bukkit.material.Openable openable2 = (org.bukkit.material.Openable)state.getData();
            return openable2.isOpen();
        }
    }
    
    public static void setOpened(final Block block, final boolean opened) {
        if (XBlock.ISFLAT) {
            if (!(block.getBlockData() instanceof Openable)) {
                return;
            }
            final BlockData data = block.getBlockData();
            final Openable openable = (Openable)data;
            openable.setOpen(opened);
            block.setBlockData(data, false);
        }
        else {
            final BlockState state = block.getState();
            if (!(state instanceof org.bukkit.material.Openable)) {
                return;
            }
            final org.bukkit.material.Openable openable2 = (org.bukkit.material.Openable)state.getData();
            openable2.setOpen(opened);
            state.setData((MaterialData)openable2);
            state.update(true, true);
        }
    }
    
    public static BlockFace getRotation(final Block block) {
        if (XBlock.ISFLAT) {
            final BlockData blockData = block.getBlockData();
            if (blockData instanceof Rotatable) {
                return ((Rotatable)blockData).getRotation();
            }
            if (blockData instanceof Directional) {
                return ((Directional)blockData).getFacing();
            }
        }
        else {
            final BlockState state = block.getState();
            if (state instanceof Skull) {
                return ((Skull)state).getRotation();
            }
            final MaterialData data = state.getData();
            if (data instanceof org.bukkit.material.Directional) {
                return ((org.bukkit.material.Directional)data).getFacing();
            }
        }
        return null;
    }
    
    public static void setRotation(final Block block, final BlockFace facing) {
        if (XBlock.ISFLAT) {
            final BlockData blockData = block.getBlockData();
            if (blockData instanceof Rotatable) {
                ((Rotatable)blockData).setRotation(facing);
            }
            else if (blockData instanceof Directional) {
                ((Directional)blockData).setFacing(facing);
            }
            block.setBlockData(blockData, false);
        }
        else {
            final BlockState state = block.getState();
            if (state instanceof Skull) {
                ((Skull)state).setRotation(facing);
            }
            else {
                final MaterialData data = state.getData();
                if (!(data instanceof org.bukkit.material.Directional)) {
                    return;
                }
                final org.bukkit.material.Directional directional = (org.bukkit.material.Directional)data;
                directional.setFacingDirection(facing);
            }
            state.update(true, true);
        }
    }
    
    private static boolean isMaterial(final Block block, final BlockMaterial... materials) {
        final Material type = block.getType();
        for (final BlockMaterial material : materials) {
            if (type == material.material) {
                return true;
            }
        }
        return false;
    }
    
    static {
        CROPS = Collections.unmodifiableSet((Set)EnumSet.of((Enum)XMaterial.CARROT, (Enum[])new XMaterial[] { XMaterial.CARROTS, XMaterial.POTATO, XMaterial.POTATOES, XMaterial.NETHER_WART, XMaterial.PUMPKIN_SEEDS, XMaterial.WHEAT_SEEDS, XMaterial.WHEAT, XMaterial.MELON_SEEDS, XMaterial.BEETROOT_SEEDS, XMaterial.BEETROOTS, XMaterial.SUGAR_CANE, XMaterial.BAMBOO_SAPLING, XMaterial.BAMBOO, XMaterial.CHORUS_PLANT, XMaterial.KELP, XMaterial.KELP_PLANT, XMaterial.SEA_PICKLE, XMaterial.BROWN_MUSHROOM, XMaterial.RED_MUSHROOM, XMaterial.MELON_STEM, XMaterial.PUMPKIN_STEM, XMaterial.COCOA, XMaterial.COCOA_BEANS }));
        DANGEROUS = Collections.unmodifiableSet((Set)EnumSet.of((Enum)XMaterial.MAGMA_BLOCK, (Enum)XMaterial.LAVA, (Enum)XMaterial.CAMPFIRE, (Enum)XMaterial.FIRE, (Enum)XMaterial.SOUL_FIRE));
        ISFLAT = XMaterial.supports(13);
        (ITEM_TO_BLOCK = (Map)new EnumMap((Class)XMaterial.class)).put((Object)XMaterial.MELON_SLICE, (Object)XMaterial.MELON_STEM);
        XBlock.ITEM_TO_BLOCK.put((Object)XMaterial.MELON_SEEDS, (Object)XMaterial.MELON_STEM);
        XBlock.ITEM_TO_BLOCK.put((Object)XMaterial.CARROT_ON_A_STICK, (Object)XMaterial.CARROTS);
        XBlock.ITEM_TO_BLOCK.put((Object)XMaterial.GOLDEN_CARROT, (Object)XMaterial.CARROTS);
        XBlock.ITEM_TO_BLOCK.put((Object)XMaterial.CARROT, (Object)XMaterial.CARROTS);
        XBlock.ITEM_TO_BLOCK.put((Object)XMaterial.POTATO, (Object)XMaterial.POTATOES);
        XBlock.ITEM_TO_BLOCK.put((Object)XMaterial.BAKED_POTATO, (Object)XMaterial.POTATOES);
        XBlock.ITEM_TO_BLOCK.put((Object)XMaterial.POISONOUS_POTATO, (Object)XMaterial.POTATOES);
        XBlock.ITEM_TO_BLOCK.put((Object)XMaterial.PUMPKIN_SEEDS, (Object)XMaterial.PUMPKIN_STEM);
        XBlock.ITEM_TO_BLOCK.put((Object)XMaterial.PUMPKIN_PIE, (Object)XMaterial.PUMPKIN);
    }
    
    private enum LegacyMaterial
    {
        STANDING_BANNER(Handling.COLORABLE), 
        WALL_BANNER(Handling.COLORABLE), 
        BANNER(Handling.COLORABLE), 
        CARPET(Handling.COLORABLE), 
        WOOL(Handling.COLORABLE), 
        STAINED_CLAY(Handling.COLORABLE), 
        STAINED_GLASS(Handling.COLORABLE), 
        STAINED_GLASS_PANE(Handling.COLORABLE), 
        THIN_GLASS(Handling.COLORABLE), 
        WOOD(Handling.WOOD_SPECIES), 
        WOOD_STEP(Handling.WOOD_SPECIES), 
        WOOD_DOUBLE_STEP(Handling.WOOD_SPECIES), 
        LEAVES(Handling.WOOD_SPECIES), 
        LEAVES_2(Handling.WOOD_SPECIES), 
        LOG(Handling.WOOD_SPECIES), 
        LOG_2(Handling.WOOD_SPECIES), 
        SAPLING(Handling.WOOD_SPECIES);
        
        private static final Map<String, LegacyMaterial> LOOKUP;
        private final Material material;
        private final Handling handling;
        
        private LegacyMaterial(final Handling handling) {
            this.material = Material.getMaterial(this.name());
            this.handling = handling;
        }
        
        private static LegacyMaterial getMaterial(final String name) {
            return (LegacyMaterial)LegacyMaterial.LOOKUP.get((Object)name);
        }
        
        static {
            LOOKUP = (Map)new HashMap();
            for (final LegacyMaterial legacyMaterial : values()) {
                LegacyMaterial.LOOKUP.put((Object)legacyMaterial.name(), (Object)legacyMaterial);
            }
        }
        
        private enum Handling
        {
            COLORABLE, 
            WOOD_SPECIES;
        }
    }
    
    public enum BlockMaterial
    {
        CAKE_BLOCK, 
        CROPS, 
        SUGAR_CANE_BLOCK, 
        BEETROOT_BLOCK, 
        NETHER_WARTS, 
        MELON_BLOCK, 
        BURNING_FURNACE, 
        STATIONARY_WATER, 
        STATIONARY_LAVA, 
        REDSTONE_LAMP_ON, 
        REDSTONE_LAMP_OFF, 
        REDSTONE_TORCH_ON, 
        REDSTONE_TORCH_OFF, 
        REDSTONE_COMPARATOR_ON, 
        REDSTONE_COMPARATOR_OFF;
        
        @Nullable
        private final Material material;
        
        private BlockMaterial() {
            this.material = Material.getMaterial(this.name());
        }
    }
}
