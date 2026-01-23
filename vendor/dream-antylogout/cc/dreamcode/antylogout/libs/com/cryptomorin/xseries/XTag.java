package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries;

import java.util.EnumSet;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import org.bukkit.Material;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.Iterator;
import java.util.regex.Pattern;
import java.util.Locale;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.Nullable;
import java.util.Collection;
import java.util.Set;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.base.XBase;

public final class XTag<T extends XBase<?, ?>>
{
    @NotNull
    public static final XTag<XMaterial> AIR;
    @NotNull
    public static final XTag<XMaterial> INVENTORY_NOT_DISPLAYABLE;
    @NotNull
    public static final XTag<XMaterial> ACACIA_LOGS;
    @NotNull
    public static final XTag<XMaterial> CORAL_FANS;
    @NotNull
    public static final XTag<XMaterial> ALIVE_CORAL_BLOCKS;
    @NotNull
    public static final XTag<XMaterial> ALIVE_CORAL_FANS;
    @NotNull
    public static final XTag<XMaterial> ALIVE_CORAL_PLANTS;
    @NotNull
    public static final XTag<XMaterial> ALIVE_CORAL_WALL_FANS;
    @NotNull
    public static final XTag<XMaterial> SPAWN_EGGS;
    @NotNull
    public static final XTag<XMaterial> ANIMALS_SPAWNABLE_ON;
    @NotNull
    public static final XTag<XMaterial> ANVIL;
    @NotNull
    public static final XTag<XMaterial> AXOLOTL_TEMPT_ITEMS;
    @NotNull
    public static final XTag<XMaterial> AXOLOTLS_SPAWNABLE_ON;
    @NotNull
    public static final XTag<XMaterial> AZALEA_GROWS_ON;
    @NotNull
    public static final XTag<XMaterial> AZALEA_ROOT_REPLACEABLE;
    @NotNull
    public static final XTag<XMaterial> BAMBOO_LOGS;
    @NotNull
    public static final XTag<XMaterial> BAMBOO_PLANTABLE_ON;
    @NotNull
    public static final XTag<XMaterial> BANNERS;
    @NotNull
    public static final XTag<XMaterial> BASE_STONE_NETHER;
    @NotNull
    public static final XTag<XMaterial> BASE_STONE_OVERWORLD;
    @NotNull
    public static final XTag<XMaterial> BEACON_BASE_BLOCKS;
    @NotNull
    public static final XTag<XMaterial> BEDS;
    @NotNull
    public static final XTag<XMaterial> BEE_GROWABLES;
    @NotNull
    public static final XTag<XMaterial> BIG_DRIPLEAF_PLACEABLE;
    @NotNull
    public static final XTag<XMaterial> BIRCH_LOGS;
    @NotNull
    public static final XTag<XMaterial> BUTTONS;
    @NotNull
    public static final XTag<XMaterial> CAMPFIRES;
    @NotNull
    public static final XTag<XMaterial> CANDLE_CAKES;
    @NotNull
    public static final XTag<XMaterial> CANDLES;
    @NotNull
    public static final XTag<XMaterial> CARPETS;
    @NotNull
    public static final XTag<XMaterial> CAULDRONS;
    @NotNull
    public static final XTag<XMaterial> CAVE_VINES;
    @NotNull
    public static final XTag<XMaterial> CHERRY_LOGS;
    @NotNull
    public static final XTag<XMaterial> CLIMBABLE;
    @NotNull
    public static final XTag<XMaterial> CLUSTER_MAX_HARVESTABLES;
    @NotNull
    public static final XTag<XMaterial> COAL_ORES;
    @NotNull
    public static final XTag<XMaterial> CONCRETE;
    @NotNull
    public static final XTag<XMaterial> CONCRETE_POWDER;
    @NotNull
    public static final XTag<XMaterial> COPPER_ORES;
    @NotNull
    public static final XTag<XMaterial> CORALS;
    @NotNull
    public static final XTag<XMaterial> CRIMSON_STEMS;
    @NotNull
    public static final XTag<XMaterial> CROPS;
    @NotNull
    public static final XTag<XMaterial> CRYSTAL_SOUND_BLOCKS;
    @NotNull
    public static final XTag<XMaterial> DARK_OAK_LOGS;
    @NotNull
    public static final XTag<XMaterial> DEAD_CORAL_BLOCKS;
    @NotNull
    public static final XTag<XMaterial> DEAD_CORAL_FANS;
    @NotNull
    public static final XTag<XMaterial> DEAD_CORAL_PLANTS;
    @NotNull
    public static final XTag<XMaterial> DEAD_CORAL_WALL_FANS;
    @NotNull
    public static final XTag<XMaterial> DEEPSLATE_ORE_REPLACEABLES;
    @NotNull
    public static final XTag<XMaterial> DIAMOND_ORES;
    @NotNull
    public static final XTag<XMaterial> DIRT;
    @NotNull
    public static final XTag<XMaterial> DOORS;
    @NotNull
    public static final XTag<XMaterial> DRAGON_IMMUNE;
    @NotNull
    public static final XTag<XMaterial> DRIPSTONE_REPLACEABLE;
    @NotNull
    public static final XTag<XMaterial> WALL_HEADS;
    @NotNull
    public static final XTag<XMaterial> EMERALD_ORES;
    @NotNull
    public static final XTag<XMaterial> ENDERMAN_HOLDABLE;
    @NotNull
    public static final XTag<XMaterial> FEATURES_CANNOT_REPLACE;
    @NotNull
    public static final XTag<XMaterial> FENCE_GATES;
    @NotNull
    public static final XTag<XMaterial> FENCES;
    @NotNull
    public static final XTag<XMaterial> FILLED_CAULDRONS;
    @NotNull
    public static final XTag<XMaterial> FIRE;
    @NotNull
    public static final XTag<XMaterial> FLOWER_POTS;
    @NotNull
    public static final XTag<XMaterial> FLOWERS;
    @NotNull
    public static final XTag<XMaterial> FOX_FOOD;
    @NotNull
    public static final XTag<XMaterial> FOXES_SPAWNABLE_ON;
    @NotNull
    public static final XTag<XMaterial> FREEZE_IMMUNE_WEARABLES;
    @NotNull
    public static final XTag<XMaterial> GEODE_INVALID_BLOCKS;
    @NotNull
    public static final XTag<XMaterial> GLASS;
    @NotNull
    public static final XTag<XMaterial> GLAZED_TERRACOTTA;
    @NotNull
    public static final XTag<XMaterial> GOATS_SPAWNABLE_ON;
    @NotNull
    public static final XTag<XMaterial> GOLD_ORES;
    @NotNull
    public static final XTag<XMaterial> GUARDED_BY_PIGLINS;
    @NotNull
    public static final XTag<XMaterial> HANGING_SIGNS;
    @NotNull
    public static final XTag<XMaterial> HOGLIN_REPELLENTS;
    @NotNull
    public static final XTag<XMaterial> ICE;
    @NotNull
    public static final XTag<XMaterial> IGNORED_BY_PIGLIN_BABIES;
    @NotNull
    public static final XTag<XMaterial> IMPERMEABLE;
    @NotNull
    public static final XTag<XMaterial> INFINIBURN_END;
    @NotNull
    public static final XTag<XMaterial> INFINIBURN_NETHER;
    @NotNull
    public static final XTag<XMaterial> INFINIBURN_OVERWORLD;
    @NotNull
    public static final XTag<XMaterial> INSIDE_STEP_SOUND_BLOCKS;
    @NotNull
    public static final XTag<XMaterial> IRON_ORES;
    @NotNull
    public static final XTag<XMaterial> ITEMS_ARROWS;
    @NotNull
    public static final XTag<XMaterial> ITEMS_BANNERS;
    @NotNull
    public static final XTag<XMaterial> ITEMS_BEACON_PAYMENT_ITEMS;
    @NotNull
    public static final XTag<XMaterial> ITEMS_BOATS;
    @NotNull
    public static final XTag<XMaterial> ITEMS_COALS;
    @NotNull
    public static final XTag<XMaterial> ITEMS_CREEPER_DROP_MUSIC_DISCS;
    @NotNull
    public static final XTag<XMaterial> ITEMS_FISHES;
    @NotNull
    public static final XTag<XMaterial> ITEMS_FURNACE_MATERIALS;
    @NotNull
    public static final XTag<XMaterial> ITEMS_LECTERN_BOOKS;
    @NotNull
    public static final XTag<XMaterial> ITEMS_MUSIC_DISCS;
    @NotNull
    public static final XTag<XMaterial> ITEMS_PIGLIN_LOVED;
    @NotNull
    public static final XTag<XMaterial> ITEMS_STONE_TOOL_MATERIALS;
    @NotNull
    public static final XTag<XMaterial> WALL_BANNERS;
    @NotNull
    public static final XTag<XMaterial> JUNGLE_LOGS;
    @NotNull
    public static final XTag<XMaterial> LAPIS_ORES;
    @NotNull
    public static final XTag<XMaterial> LAVA_POOL_STONE_CANNOT_REPLACE;
    @NotNull
    public static final XTag<XMaterial> LEAVES;
    @NotNull
    public static final XTag<XMaterial> LOGS;
    @NotNull
    public static final XTag<XMaterial> LOGS_THAT_BURN;
    @NotNull
    public static final XTag<XMaterial> LUSH_GROUND_REPLACEABLE;
    @NotNull
    public static final XTag<XMaterial> MANGROVE_LOGS;
    @NotNull
    public static final XTag<XMaterial> MINEABLE_AXE;
    @NotNull
    public static final XTag<XMaterial> MINEABLE_HOE;
    @NotNull
    public static final XTag<XMaterial> MINEABLE_PICKAXE;
    @NotNull
    public static final XTag<XMaterial> MINEABLE_SHOVEL;
    @NotNull
    public static final XTag<XMaterial> MOOSHROOMS_SPAWNABLE_ON;
    @NotNull
    public static final XTag<XMaterial> MOSS_REPLACEABLE;
    @NotNull
    public static final XTag<XMaterial> MUSHROOM_GROW_BLOCK;
    @NotNull
    public static final XTag<XMaterial> NEEDS_DIAMOND_TOOL;
    @NotNull
    public static final XTag<XMaterial> NEEDS_IRON_TOOL;
    @NotNull
    public static final XTag<XMaterial> NEEDS_STONE_TOOL;
    @NotNull
    public static final XTag<XMaterial> NON_FLAMMABLE_WOOD;
    @NotNull
    public static final XTag<XMaterial> NON_WOODEN_STAIRS;
    @NotNull
    public static final XTag<XMaterial> NON_WOODEN_SLABS;
    @NotNull
    public static final XTag<XMaterial> NYLIUM;
    @NotNull
    public static final XTag<XMaterial> OAK_LOGS;
    @NotNull
    public static final XTag<XMaterial> OCCLUDES_VIBRATION_SIGNALS;
    @NotNull
    public static final XTag<XMaterial> ORES;
    @NotNull
    public static final XTag<XMaterial> PALE_OAK_LOGS;
    @NotNull
    public static final XTag<XMaterial> PARROTS_SPAWNABLE_ON;
    @NotNull
    public static final XTag<XMaterial> PIGLIN_FOOD;
    @NotNull
    public static final XTag<XMaterial> PIGLIN_REPELLENTS;
    @NotNull
    public static final XTag<XMaterial> PLANKS;
    @NotNull
    public static final XTag<XMaterial> POLAR_BEARS_SPAWNABLE_ON_IN_FROZEN_OCEAN;
    @NotNull
    public static final XTag<XMaterial> PORTALS;
    @NotNull
    public static final XTag<XMaterial> POTTERY_SHERDS;
    @NotNull
    public static final XTag<XMaterial> PRESSURE_PLATES;
    @NotNull
    public static final XTag<XMaterial> PREVENT_MOB_SPAWNING_INSIDE;
    @NotNull
    public static final XTag<XMaterial> RABBITS_SPAWNABLE_ON;
    @NotNull
    public static final XTag<XMaterial> RAILS;
    @NotNull
    public static final XTag<XMaterial> REDSTONE_ORES;
    @NotNull
    public static final XTag<XMaterial> REPLACEABLE_PLANTS;
    @NotNull
    public static final XTag<XMaterial> SAND;
    @NotNull
    public static final XTag<XMaterial> SAPLINGS;
    @NotNull
    public static final XTag<XMaterial> SHULKER_BOXES;
    @NotNull
    public static final XTag<XMaterial> SIGNS;
    @NotNull
    public static final XTag<XMaterial> SMALL_DRIPLEAF_PLACEABLE;
    @NotNull
    public static final XTag<XMaterial> SMALL_FLOWERS;
    @NotNull
    public static final XTag<XMaterial> SMITHING_TEMPLATES;
    @NotNull
    public static final XTag<XMaterial> SNOW;
    @NotNull
    public static final XTag<XMaterial> SOUL_FIRE_BASE_BLOCKS;
    @NotNull
    public static final XTag<XMaterial> SOUL_SPEED_BLOCKS;
    @NotNull
    public static final XTag<XMaterial> SPRUCE_LOGS;
    @NotNull
    public static final XTag<XMaterial> STAIRS;
    @NotNull
    public static final XTag<XMaterial> STANDING_SIGNS;
    @NotNull
    public static final XTag<XMaterial> STONE_BRICKS;
    @NotNull
    public static final XTag<XMaterial> STONE_ORE_REPLACEABLES;
    @NotNull
    public static final XTag<XMaterial> STONE_PRESSURE_PLATES;
    @NotNull
    public static final XTag<XMaterial> STRIDER_WARM_BLOCKS;
    @NotNull
    public static final XTag<XMaterial> TALL_FLOWERS;
    @NotNull
    public static final XTag<XMaterial> TERRACOTTA;
    @NotNull
    public static final XTag<XMaterial> TRAPDOORS;
    @NotNull
    public static final XTag<XMaterial> UNDERWATER_BONEMEALS;
    @NotNull
    public static final XTag<XMaterial> UNSTABLE_BOTTOM_CENTER;
    @NotNull
    public static final XTag<XMaterial> VALID_SPAWN;
    @NotNull
    public static final XTag<XMaterial> WALL_HANGING_SIGNS;
    @NotNull
    public static final XTag<XMaterial> WALL_POST_OVERRIDE;
    @NotNull
    public static final XTag<XMaterial> WALL_SIGNS;
    @NotNull
    public static final XTag<XMaterial> WALL_TORCHES;
    @NotNull
    public static final XTag<XMaterial> WALLS;
    @NotNull
    public static final XTag<XMaterial> WARPED_STEMS;
    @NotNull
    public static final XTag<XMaterial> WITHER_IMMUNE;
    @NotNull
    public static final XTag<XMaterial> WITHER_SUMMON_BASE_BLOCKS;
    @NotNull
    public static final XTag<XMaterial> WOLVES_SPAWNABLE_ON;
    @NotNull
    public static final XTag<XMaterial> WOODEN_BUTTONS;
    @NotNull
    public static final XTag<XMaterial> WOODEN_DOORS;
    @NotNull
    public static final XTag<XMaterial> WOODEN_FENCE_GATES;
    @NotNull
    public static final XTag<XMaterial> WOODEN_FENCES;
    @NotNull
    public static final XTag<XMaterial> WOODEN_PRESSURE_PLATES;
    @NotNull
    public static final XTag<XMaterial> WOODEN_SLABS;
    @NotNull
    public static final XTag<XMaterial> WOODEN_STAIRS;
    @NotNull
    public static final XTag<XMaterial> WOODEN_TRAPDOORS;
    @NotNull
    public static final XTag<XMaterial> WOOL;
    @NotNull
    public static final XTag<XMaterial> LEATHER_ARMOR_PIECES;
    @NotNull
    public static final XTag<XMaterial> IRON_ARMOR_PIECES;
    @NotNull
    public static final XTag<XMaterial> CHAINMAIL_ARMOR_PIECES;
    @NotNull
    public static final XTag<XMaterial> GOLDEN_ARMOR_PIECES;
    @NotNull
    public static final XTag<XMaterial> DIAMOND_ARMOR_PIECES;
    @NotNull
    public static final XTag<XMaterial> NETHERITE_ARMOR_PIECES;
    @NotNull
    public static final XTag<XMaterial> ARMOR_PIECES;
    @NotNull
    public static final XTag<XMaterial> WOODEN_TOOLS;
    @NotNull
    public static final XTag<XMaterial> FLUID;
    @NotNull
    public static final XTag<XMaterial> STONE_TOOLS;
    @NotNull
    public static final XTag<XMaterial> IRON_TOOLS;
    @NotNull
    public static final XTag<XMaterial> DIAMOND_TOOLS;
    @NotNull
    public static final XTag<XMaterial> NETHERITE_TOOLS;
    @NotNull
    public static final XTag<XMaterial> DANGEROUS_BLOCKS;
    @NotNull
    public static final XTag<XEnchantment> ARMOR_ENCHANTS;
    @NotNull
    public static final XTag<XEnchantment> HELEMT_ENCHANTS;
    @NotNull
    public static final XTag<XEnchantment> CHESTPLATE_ENCHANTS;
    @NotNull
    public static final XTag<XEnchantment> LEGGINGS_ENCHANTS;
    @NotNull
    public static final XTag<XEnchantment> BOOTS_ENCHANTS;
    @NotNull
    public static final XTag<XEnchantment> ELYTRA_ENCHANTS;
    @NotNull
    public static final XTag<XEnchantment> SWORD_ENCHANTS;
    @NotNull
    public static final XTag<XEnchantment> AXE_ENCHANTS;
    @NotNull
    public static final XTag<XEnchantment> HOE_ENCHANTS;
    @NotNull
    public static final XTag<XEnchantment> PICKAXE_ENCHANTS;
    @NotNull
    public static final XTag<XEnchantment> SHOVEL_ENCHANTS;
    @NotNull
    public static final XTag<XEnchantment> SHEARS_ENCHANTS;
    @NotNull
    public static final XTag<XEnchantment> BOW_ENCHANTS;
    @NotNull
    public static final XTag<XEnchantment> CROSSBOW_ENCHANTS;
    @NotNull
    public static final XTag<XEntityType> EFFECTIVE_SMITE_ENTITIES;
    @NotNull
    public static final XTag<XEntityType> EFFECTIVE_BANE_OF_ARTHROPODS_ENTITIES;
    @NotNull
    public static final XTag<XPotion> DEBUFFS;
    public static final Map<XMaterial, XEntityType> MATERIAL_TO_ENTITY;
    @NotNull
    private final Set<T> values;
    private static final Map<String, XTag<?>> TAGS;
    
    private XTag(@NotNull final Set<T> values) {
        this.values = values;
    }
    
    public static <E> List<Matcher<E>> stringMatcher(@Nullable final Collection<String> elements) {
        return stringMatcher(elements, null);
    }
    
    public static <E> List<Matcher<E>> stringMatcher(@Nullable final Collection<String> elements, @Nullable final Collection<Matcher.Error> errors) {
        if (elements == null || elements.isEmpty()) {
            return (List<Matcher<E>>)new ArrayList();
        }
        final List<Matcher<E>> matchers = (List<Matcher<E>>)new ArrayList(elements.size());
        for (String comp : elements) {
            final String checker = comp.toUpperCase(Locale.ENGLISH);
            if (checker.startsWith("CONTAINS:")) {
                comp = XMaterial.format(checker.substring(9));
                matchers.add((Object)new Matcher.TextMatcher(comp, true));
            }
            else if (checker.startsWith("REGEX:")) {
                comp = comp.substring(6);
                try {
                    matchers.add((Object)new Matcher.RegexMatcher(Pattern.compile(comp)));
                }
                catch (final Throwable e) {
                    if (errors == null) {
                        continue;
                    }
                    errors.add((Object)new Matcher.Error(comp, "REGEX", e));
                }
            }
            else {
                if (checker.startsWith("TAG:")) {
                    comp = XMaterial.format(comp.substring(4));
                    final Optional<XTag<?>> tag = getTag(comp);
                    if (tag.isPresent()) {
                        matchers.add((Object)new Matcher.XTagMatcher((XTag<XBase>)tag.get()));
                    }
                    else {
                        errors.add((Object)new Matcher.Error("Cannot find tag: " + comp, "TAG"));
                    }
                }
                matchers.add((Object)new Matcher.TextMatcher(comp, false));
            }
        }
        return matchers;
    }
    
    public static <T> boolean anyMatchString(final T target, final Collection<String> matchers) {
        return anyMatch(target, (java.util.Collection<Matcher<Object>>)stringMatcher(matchers));
    }
    
    public static <T> boolean anyMatch(final T target, final Collection<Matcher<T>> matchers) {
        return matchers.stream().anyMatch(x -> x.matches(target));
    }
    
    private static XMaterial[] findAllColors(final String material) {
        final String[] colorPrefixes = { "ORANGE", "LIGHT_BLUE", "GRAY", "BLACK", "MAGENTA", "PINK", "BLUE", "GREEN", "CYAN", "PURPLE", "YELLOW", "LIME", "LIGHT_GRAY", "WHITE", "BROWN", "RED" };
        final List<XMaterial> list = (List<XMaterial>)new ArrayList();
        final Optional<XMaterial> matchXMaterial = XMaterial.matchXMaterial(material);
        final List<XMaterial> list2 = list;
        Objects.requireNonNull((Object)list2);
        matchXMaterial.ifPresent(list2::add);
        for (final String color : colorPrefixes) {
            final Optional<XMaterial> matchXMaterial2 = XMaterial.matchXMaterial(color + '_' + material);
            final List<XMaterial> list3 = list;
            Objects.requireNonNull((Object)list3);
            matchXMaterial2.ifPresent(list3::add);
        }
        return (XMaterial[])list.toArray((Object[])new XMaterial[0]);
    }
    
    private static XMaterial[] findAllWoodTypes(final String material) {
        final String[] woodPrefixes = { "ACACIA", "DARK_OAK", "PALE_OAK", "JUNGLE", "BIRCH", "WARPED", "OAK", "SPRUCE", "CRIMSON", "MANGROVE", "CHERRY", "BAMBOO" };
        final List<XMaterial> list = (List<XMaterial>)new ArrayList();
        for (final String wood : woodPrefixes) {
            final Optional<XMaterial> matchXMaterial = XMaterial.matchXMaterial(wood + '_' + material);
            final List<XMaterial> list2 = list;
            Objects.requireNonNull((Object)list2);
            matchXMaterial.ifPresent(list2::add);
        }
        return (XMaterial[])list.toArray((Object[])new XMaterial[0]);
    }
    
    private static XMaterial[] findMaterialsEndingWith(final String material) {
        return (XMaterial[])Arrays.stream((Object[])XMaterial.VALUES).filter(x -> x.name().endsWith(material)).toArray(XMaterial[]::new);
    }
    
    private static XMaterial[] findMaterialsStartingWith(final String material) {
        return (XMaterial[])Arrays.stream((Object[])XMaterial.VALUES).filter(x -> x.name().startsWith(material)).toArray(XMaterial[]::new);
    }
    
    private static XMaterial[] findAllCorals(final boolean alive, final boolean block, final boolean fan, final boolean wall) {
        final String[] materials = { "FIRE", "TUBE", "BRAIN", "HORN", "BUBBLE" };
        final List<XMaterial> list = (List<XMaterial>)new ArrayList();
        for (final String material : materials) {
            final StringBuilder builder = new StringBuilder();
            if (!alive) {
                builder.append("DEAD_");
            }
            builder.append(material).append("_CORAL");
            if (block) {
                builder.append("_BLOCK");
            }
            if (fan) {
                if (wall) {
                    builder.append("_WALL");
                }
                builder.append("_FAN");
            }
            final Optional<XMaterial> matchXMaterial = XMaterial.matchXMaterial(builder.toString());
            final List<XMaterial> list2 = list;
            Objects.requireNonNull((Object)list2);
            matchXMaterial.ifPresent(list2::add);
        }
        return (XMaterial[])list.toArray((Object[])new XMaterial[0]);
    }
    
    public static boolean isItem(final XMaterial material) {
        if (XMaterial.supports(13)) {
            final Material mat = material.get();
            return mat != null && mat.isItem();
        }
        switch (material) {
            case ATTACHED_MELON_STEM:
            case ATTACHED_PUMPKIN_STEM:
            case BEETROOTS:
            case BLACK_WALL_BANNER:
            case BLUE_WALL_BANNER:
            case BROWN_WALL_BANNER:
            case CARROTS:
            case COCOA:
            case CREEPER_WALL_HEAD:
            case CYAN_WALL_BANNER:
            case DRAGON_WALL_HEAD:
            case END_GATEWAY:
            case END_PORTAL:
            case FIRE:
            case FIRE_CORAL_WALL_FAN:
            case FROSTED_ICE:
            case GRAY_WALL_BANNER:
            case GREEN_WALL_BANNER:
            case HORN_CORAL_WALL_FAN:
            case LAVA:
            case LIGHT_BLUE_WALL_BANNER:
            case LIGHT_GRAY_WALL_BANNER:
            case LIME_WALL_BANNER:
            case MAGENTA_WALL_BANNER:
            case MELON_STEM:
            case MOVING_PISTON:
            case NETHER_PORTAL:
            case ORANGE_WALL_BANNER:
            case PINK_WALL_BANNER:
            case PISTON_HEAD:
            case PLAYER_WALL_HEAD:
            case POTATOES:
            case POTTED_ACACIA_SAPLING:
            case POTTED_ALLIUM:
            case POTTED_AZURE_BLUET:
            case POTTED_BIRCH_SAPLING:
            case POTTED_BLUE_ORCHID:
            case POTTED_BROWN_MUSHROOM:
            case POTTED_CACTUS:
            case POTTED_DANDELION:
            case POTTED_DARK_OAK_SAPLING:
            case POTTED_PALE_OAK_SAPLING:
            case POTTED_DEAD_BUSH:
            case POTTED_FERN:
            case POTTED_JUNGLE_SAPLING:
            case POTTED_OAK_SAPLING:
            case POTTED_ORANGE_TULIP:
            case POTTED_OXEYE_DAISY:
            case POTTED_PINK_TULIP:
            case POTTED_POPPY:
            case POTTED_RED_MUSHROOM:
            case POTTED_RED_TULIP:
            case POTTED_SPRUCE_SAPLING:
            case POTTED_WHITE_TULIP:
            case PUMPKIN_STEM:
            case PURPLE_WALL_BANNER:
            case REDSTONE_WALL_TORCH:
            case REDSTONE_WIRE:
            case RED_WALL_BANNER:
            case SKELETON_WALL_SKULL:
            case TRIPWIRE:
            case ACACIA_WALL_SIGN:
            case OAK_WALL_SIGN:
            case BIRCH_WALL_SIGN:
            case JUNGLE_WALL_SIGN:
            case SPRUCE_WALL_SIGN:
            case DARK_OAK_WALL_SIGN:
            case PALE_OAK_WALL_SIGN:
            case WALL_TORCH:
            case WATER:
            case WHITE_WALL_BANNER:
            case WITHER_SKELETON_WALL_SKULL:
            case YELLOW_WALL_BANNER:
            case ZOMBIE_WALL_HEAD: {
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    public static boolean isInteractable(final XMaterial material) {
        if (XMaterial.supports(13)) {
            return material.get().isInteractable();
        }
        switch (material) {
            case MOVING_PISTON:
            case POTTED_ACACIA_SAPLING:
            case POTTED_ALLIUM:
            case POTTED_AZURE_BLUET:
            case POTTED_BIRCH_SAPLING:
            case POTTED_BLUE_ORCHID:
            case POTTED_BROWN_MUSHROOM:
            case POTTED_CACTUS:
            case POTTED_DANDELION:
            case POTTED_DARK_OAK_SAPLING:
            case POTTED_PALE_OAK_SAPLING:
            case POTTED_DEAD_BUSH:
            case POTTED_FERN:
            case POTTED_JUNGLE_SAPLING:
            case POTTED_OAK_SAPLING:
            case POTTED_ORANGE_TULIP:
            case POTTED_OXEYE_DAISY:
            case POTTED_PINK_TULIP:
            case POTTED_POPPY:
            case POTTED_RED_MUSHROOM:
            case POTTED_RED_TULIP:
            case POTTED_SPRUCE_SAPLING:
            case POTTED_WHITE_TULIP:
            case ACACIA_WALL_SIGN:
            case OAK_WALL_SIGN:
            case BIRCH_WALL_SIGN:
            case JUNGLE_WALL_SIGN:
            case SPRUCE_WALL_SIGN:
            case DARK_OAK_WALL_SIGN:
            case PALE_OAK_WALL_SIGN:
            case ACACIA_BUTTON:
            case ACACIA_DOOR:
            case ACACIA_FENCE:
            case ACACIA_FENCE_GATE:
            case ACACIA_STAIRS:
            case ACACIA_TRAPDOOR:
            case ANVIL:
            case BEACON:
            case BIRCH_BUTTON:
            case BIRCH_DOOR:
            case BIRCH_FENCE:
            case PALE_OAK_BUTTON:
            case PALE_OAK_DOOR:
            case PALE_OAK_FENCE:
            case PALE_OAK_FENCE_GATE:
            case PALE_OAK_STAIRS:
            case PALE_OAK_TRAPDOOR:
            case BIRCH_FENCE_GATE:
            case BIRCH_STAIRS:
            case BIRCH_TRAPDOOR:
            case BLACK_BED:
            case BLACK_SHULKER_BOX:
            case BLUE_BED:
            case BLUE_SHULKER_BOX:
            case BREWING_STAND:
            case BRICK_STAIRS:
            case BROWN_BED:
            case BROWN_SHULKER_BOX:
            case CAKE:
            case CAULDRON:
            case CHAIN_COMMAND_BLOCK:
            case CHEST:
            case CHIPPED_ANVIL:
            case COBBLESTONE_STAIRS:
            case COMMAND_BLOCK:
            case COMPARATOR:
            case CRAFTING_TABLE:
            case CYAN_BED:
            case CYAN_SHULKER_BOX:
            case DAMAGED_ANVIL:
            case DARK_OAK_BUTTON:
            case DARK_OAK_DOOR:
            case DARK_OAK_FENCE:
            case DARK_OAK_FENCE_GATE:
            case DARK_OAK_STAIRS:
            case DARK_OAK_TRAPDOOR:
            case DARK_PRISMARINE_STAIRS:
            case DAYLIGHT_DETECTOR:
            case DISPENSER:
            case DRAGON_EGG:
            case DROPPER:
            case ENCHANTING_TABLE:
            case ENDER_CHEST:
            case FLOWER_POT:
            case FURNACE:
            case GRAY_BED:
            case GRAY_SHULKER_BOX:
            case GREEN_BED:
            case GREEN_SHULKER_BOX:
            case HOPPER:
            case IRON_DOOR:
            case IRON_TRAPDOOR:
            case JUKEBOX:
            case JUNGLE_BUTTON:
            case JUNGLE_DOOR:
            case JUNGLE_FENCE:
            case JUNGLE_FENCE_GATE:
            case JUNGLE_STAIRS:
            case JUNGLE_TRAPDOOR:
            case LEVER:
            case LIGHT_BLUE_BED:
            case LIGHT_BLUE_SHULKER_BOX:
            case LIGHT_GRAY_BED:
            case LIGHT_GRAY_SHULKER_BOX:
            case LIME_BED:
            case LIME_SHULKER_BOX:
            case MAGENTA_BED:
            case MAGENTA_SHULKER_BOX:
            case NETHER_BRICK_FENCE:
            case NETHER_BRICK_STAIRS:
            case NOTE_BLOCK:
            case OAK_BUTTON:
            case OAK_DOOR:
            case OAK_FENCE:
            case OAK_FENCE_GATE:
            case OAK_STAIRS:
            case OAK_TRAPDOOR:
            case ORANGE_BED:
            case ORANGE_SHULKER_BOX:
            case PINK_BED:
            case PINK_SHULKER_BOX:
            case PRISMARINE_BRICK_STAIRS:
            case PRISMARINE_STAIRS:
            case PUMPKIN:
            case PURPLE_BED:
            case PURPLE_SHULKER_BOX:
            case PURPUR_STAIRS:
            case QUARTZ_STAIRS:
            case REDSTONE_ORE:
            case RED_BED:
            case RED_SANDSTONE_STAIRS:
            case RED_SHULKER_BOX:
            case REPEATER:
            case REPEATING_COMMAND_BLOCK:
            case SANDSTONE_STAIRS:
            case SHULKER_BOX:
            case ACACIA_SIGN:
            case BIRCH_SIGN:
            case DARK_OAK_SIGN:
            case JUNGLE_SIGN:
            case OAK_SIGN:
            case SPRUCE_SIGN:
            case SPRUCE_BUTTON:
            case SPRUCE_DOOR:
            case SPRUCE_FENCE:
            case SPRUCE_FENCE_GATE:
            case SPRUCE_STAIRS:
            case SPRUCE_TRAPDOOR:
            case STONE_BRICK_STAIRS:
            case STONE_BUTTON:
            case STRUCTURE_BLOCK:
            case TNT:
            case TRAPPED_CHEST:
            case WHITE_BED:
            case WHITE_SHULKER_BOX:
            case YELLOW_BED:
            case YELLOW_SHULKER_BOX: {
                return true;
            }
            default: {
                return false;
            }
        }
    }
    
    @NotNull
    public Set<T> getValues() {
        return this.values;
    }
    
    public boolean isTagged(@Nullable final T value) {
        return value != null && this.values.contains((Object)value);
    }
    
    @SafeVarargs
    private final XTag<T> without(final T... without) {
        final Set<T> newSet = (Set<T>)new HashSet((Collection)this.values);
        for (final T value : without) {
            newSet.remove((Object)value);
        }
        return new XTag<T>(newSet);
    }
    
    public static Optional<XTag<?>> getTag(final String name) {
        return (Optional<XTag<?>>)Optional.ofNullable((Object)XTag.TAGS.get((Object)name));
    }
    
    static {
        AIR = simple((XBase[])new XMaterial[] { XMaterial.AIR, XMaterial.CAVE_AIR, XMaterial.VOID_AIR });
        SPAWN_EGGS = of((XBase[])Arrays.stream((Object[])XMaterial.values()).filter(x -> x.name().endsWith("_SPAWN_EGG")).toArray(XMaterial[]::new)).build();
        FIRE = simple((XBase[])new XMaterial[] { XMaterial.FIRE, XMaterial.SOUL_FIRE });
        PORTALS = simple((XBase[])new XMaterial[] { XMaterial.END_GATEWAY, XMaterial.END_PORTAL, XMaterial.NETHER_PORTAL });
        FLUID = simple((XBase[])new XMaterial[] { XMaterial.LAVA, XMaterial.WATER });
        DANGEROUS_BLOCKS = simple((XBase[])new XMaterial[] { XMaterial.MAGMA_BLOCK, XMaterial.LAVA, XMaterial.CAMPFIRE, XMaterial.FIRE, XMaterial.SOUL_FIRE });
        EFFECTIVE_SMITE_ENTITIES = simple((XBase[])new XEntityType[] { XEntityType.ZOMBIE, XEntityType.SKELETON, XEntityType.WITHER, XEntityType.BEE, XEntityType.PHANTOM, XEntityType.DROWNED, XEntityType.WITHER_SKELETON, XEntityType.SKELETON_HORSE, XEntityType.STRAY, XEntityType.HUSK });
        EFFECTIVE_BANE_OF_ARTHROPODS_ENTITIES = simple((XBase[])new XEntityType[] { XEntityType.SPIDER, XEntityType.CAVE_SPIDER, XEntityType.SILVERFISH, XEntityType.ENDERMITE });
        DEBUFFS = simple((XBase[])new XPotion[] { XPotion.BAD_OMEN, XPotion.BLINDNESS, XPotion.NAUSEA, XPotion.INSTANT_DAMAGE, XPotion.HUNGER, XPotion.LEVITATION, XPotion.POISON, XPotion.SLOWNESS, XPotion.MINING_FATIGUE, XPotion.UNLUCK, XPotion.WEAKNESS, XPotion.WITHER });
        MATERIAL_TO_ENTITY = (Map)new HashMap();
        ACACIA_LOGS = simple((XBase[])new XMaterial[] { XMaterial.STRIPPED_ACACIA_LOG, XMaterial.ACACIA_LOG, XMaterial.ACACIA_WOOD, XMaterial.STRIPPED_ACACIA_WOOD });
        BIRCH_LOGS = simple((XBase[])new XMaterial[] { XMaterial.STRIPPED_BIRCH_LOG, XMaterial.BIRCH_LOG, XMaterial.BIRCH_WOOD, XMaterial.STRIPPED_BIRCH_WOOD });
        DARK_OAK_LOGS = simple((XBase[])new XMaterial[] { XMaterial.STRIPPED_DARK_OAK_LOG, XMaterial.DARK_OAK_LOG, XMaterial.DARK_OAK_WOOD, XMaterial.STRIPPED_DARK_OAK_WOOD });
        JUNGLE_LOGS = simple((XBase[])new XMaterial[] { XMaterial.STRIPPED_JUNGLE_LOG, XMaterial.JUNGLE_LOG, XMaterial.JUNGLE_WOOD, XMaterial.STRIPPED_JUNGLE_WOOD });
        MANGROVE_LOGS = simple((XBase[])new XMaterial[] { XMaterial.STRIPPED_MANGROVE_LOG, XMaterial.MANGROVE_LOG, XMaterial.MANGROVE_WOOD, XMaterial.STRIPPED_MANGROVE_WOOD });
        OAK_LOGS = simple((XBase[])new XMaterial[] { XMaterial.STRIPPED_OAK_LOG, XMaterial.OAK_LOG, XMaterial.OAK_WOOD, XMaterial.STRIPPED_OAK_WOOD });
        PALE_OAK_LOGS = simple((XBase[])new XMaterial[] { XMaterial.STRIPPED_PALE_OAK_LOG, XMaterial.PALE_OAK_LOG, XMaterial.PALE_OAK_WOOD, XMaterial.STRIPPED_PALE_OAK_WOOD });
        SPRUCE_LOGS = simple((XBase[])new XMaterial[] { XMaterial.STRIPPED_SPRUCE_LOG, XMaterial.SPRUCE_LOG, XMaterial.SPRUCE_WOOD, XMaterial.STRIPPED_SPRUCE_WOOD });
        CHERRY_LOGS = simple((XBase[])new XMaterial[] { XMaterial.STRIPPED_CHERRY_LOG, XMaterial.CHERRY_LOG, XMaterial.CHERRY_WOOD, XMaterial.STRIPPED_CHERRY_WOOD });
        BAMBOO_LOGS = simple((XBase[])new XMaterial[] { XMaterial.STRIPPED_BAMBOO_BLOCK, XMaterial.BAMBOO_BLOCK, XMaterial.BAMBOO_MOSAIC, XMaterial.BAMBOO_PLANKS });
        CANDLE_CAKES = simple((XBase[])findAllColors("CANDLE_CAKE"));
        CANDLES = simple((XBase[])findAllColors("CANDLE"));
        TERRACOTTA = simple((XBase[])findAllColors("TERRACOTTA"));
        GLAZED_TERRACOTTA = simple((XBase[])findAllColors("GLAZED_TERRACOTTA"));
        SHULKER_BOXES = simple((XBase[])findAllColors("SHULKER_BOX"));
        CARPETS = simple((XBase[])findAllColors("CARPET"));
        WOOL = simple((XBase[])findAllColors("WOOL"));
        GLASS = of((XBase[])findAllColors("GLASS")).inheritFrom(simple((XBase[])new XMaterial[] { XMaterial.TINTED_GLASS })).build();
        ITEMS_BANNERS = simple((XBase[])findAllColors("BANNER"));
        WALL_BANNERS = simple((XBase[])findAllColors("WALL_BANNER"));
        BANNERS = simple((XTag<XBase>[])new XTag[] { XTag.ITEMS_BANNERS, XTag.WALL_BANNERS });
        BEDS = simple((XBase[])findAllColors("BED"));
        CONCRETE = simple((XBase[])findAllColors("CONCRETE"));
        CONCRETE_POWDER = simple((XBase[])findAllColors("CONCRETE_POWDER"));
        STANDING_SIGNS = simple((XBase[])findAllWoodTypes("SIGN"));
        WALL_SIGNS = simple((XBase[])findAllWoodTypes("WALL_SIGN"));
        WALL_HANGING_SIGNS = simple((XBase[])findAllWoodTypes("WALL_HANGING_SIGN"));
        HANGING_SIGNS = simple((XBase[])findAllWoodTypes("HANGING_SIGN"));
        WOODEN_PRESSURE_PLATES = simple((XBase[])findAllWoodTypes("PRESSURE_PLATE"));
        WOODEN_DOORS = simple((XBase[])findAllWoodTypes("DOOR"));
        WOODEN_FENCE_GATES = simple((XBase[])findAllWoodTypes("FENCE_GATE"));
        WOODEN_FENCES = simple((XBase[])findAllWoodTypes("FENCE"));
        WOODEN_SLABS = simple((XBase[])findAllWoodTypes("SLAB"));
        WOODEN_STAIRS = simple((XBase[])findAllWoodTypes("STAIRS"));
        WOODEN_TRAPDOORS = simple((XBase[])findAllWoodTypes("TRAPDOOR"));
        PLANKS = simple((XBase[])findAllWoodTypes("PLANKS"));
        WOODEN_BUTTONS = simple((XBase[])findAllWoodTypes("BUTTON"));
        COAL_ORES = simple((XBase[])new XMaterial[] { XMaterial.COAL_ORE, XMaterial.DEEPSLATE_COAL_ORE });
        IRON_ORES = simple((XBase[])new XMaterial[] { XMaterial.IRON_ORE, XMaterial.DEEPSLATE_IRON_ORE });
        COPPER_ORES = simple((XBase[])new XMaterial[] { XMaterial.COPPER_ORE, XMaterial.DEEPSLATE_COPPER_ORE });
        REDSTONE_ORES = simple((XBase[])new XMaterial[] { XMaterial.REDSTONE_ORE, XMaterial.DEEPSLATE_REDSTONE_ORE });
        LAPIS_ORES = simple((XBase[])new XMaterial[] { XMaterial.LAPIS_ORE, XMaterial.DEEPSLATE_LAPIS_ORE });
        GOLD_ORES = simple((XBase[])new XMaterial[] { XMaterial.GOLD_ORE, XMaterial.DEEPSLATE_GOLD_ORE, XMaterial.NETHER_GOLD_ORE });
        DIAMOND_ORES = simple((XBase[])new XMaterial[] { XMaterial.DIAMOND_ORE, XMaterial.DEEPSLATE_DIAMOND_ORE });
        EMERALD_ORES = simple((XBase[])new XMaterial[] { XMaterial.EMERALD_ORE, XMaterial.DEEPSLATE_EMERALD_ORE });
        ORES = of((XBase[])new XMaterial[] { XMaterial.ANCIENT_DEBRIS, XMaterial.NETHER_QUARTZ_ORE }).inheritFrom(XTag.COAL_ORES, XTag.IRON_ORES, XTag.COPPER_ORES, XTag.REDSTONE_ORES, XTag.LAPIS_ORES, XTag.GOLD_ORES, XTag.DIAMOND_ORES, XTag.EMERALD_ORES).build();
        ALIVE_CORAL_WALL_FANS = simple((XBase[])findAllCorals(true, false, true, true));
        ALIVE_CORAL_FANS = simple((XBase[])findAllCorals(true, false, true, false));
        ALIVE_CORAL_BLOCKS = simple((XBase[])findAllCorals(true, true, false, false));
        ALIVE_CORAL_PLANTS = simple((XBase[])findAllCorals(true, false, false, false));
        DEAD_CORAL_WALL_FANS = simple((XBase[])findAllCorals(false, false, true, true));
        DEAD_CORAL_FANS = simple((XBase[])findAllCorals(false, false, true, false));
        DEAD_CORAL_BLOCKS = simple((XBase[])findAllCorals(false, true, false, false));
        DEAD_CORAL_PLANTS = simple((XBase[])findAllCorals(false, false, false, false));
        CORAL_FANS = simple((XTag<XBase>[])new XTag[] { XTag.ALIVE_CORAL_FANS, XTag.ALIVE_CORAL_WALL_FANS, XTag.DEAD_CORAL_WALL_FANS, XTag.DEAD_CORAL_FANS });
        CORALS = simple((XTag<XBase>[])new XTag[] { XTag.ALIVE_CORAL_WALL_FANS, XTag.ALIVE_CORAL_FANS, XTag.ALIVE_CORAL_BLOCKS, XTag.ALIVE_CORAL_PLANTS, XTag.DEAD_CORAL_WALL_FANS, XTag.DEAD_CORAL_FANS, XTag.DEAD_CORAL_BLOCKS, XTag.DEAD_CORAL_PLANTS });
        WALL_HEADS = simple((XTag<XBase>[])new XTag[] { simple((XBase[])findMaterialsEndingWith("WALL_HEAD")), simple((XBase[])new XMaterial[] { XMaterial.WITHER_SKELETON_WALL_SKULL, XMaterial.SKELETON_WALL_SKULL }) });
        WALL_TORCHES = simple((XBase[])new XMaterial[] { XMaterial.WALL_TORCH, XMaterial.SOUL_WALL_TORCH, XMaterial.REDSTONE_WALL_TORCH });
        WALLS = simple((XBase[])new XMaterial[] { XMaterial.POLISHED_DEEPSLATE_WALL, XMaterial.NETHER_BRICK_WALL, XMaterial.POLISHED_BLACKSTONE_WALL, XMaterial.DEEPSLATE_BRICK_WALL, XMaterial.RED_SANDSTONE_WALL, XMaterial.BRICK_WALL, XMaterial.COBBLESTONE_WALL, XMaterial.POLISHED_BLACKSTONE_BRICK_WALL, XMaterial.PRISMARINE_WALL, XMaterial.SANDSTONE_WALL, XMaterial.GRANITE_WALL, XMaterial.DEEPSLATE_TILE_WALL, XMaterial.BLACKSTONE_WALL, XMaterial.STONE_BRICK_WALL, XMaterial.RED_NETHER_BRICK_WALL, XMaterial.DIORITE_WALL, XMaterial.MOSSY_COBBLESTONE_WALL, XMaterial.ANDESITE_WALL, XMaterial.MOSSY_STONE_BRICK_WALL, XMaterial.END_STONE_BRICK_WALL, XMaterial.COBBLED_DEEPSLATE_WALL });
        STONE_PRESSURE_PLATES = simple((XBase[])new XMaterial[] { XMaterial.STONE_PRESSURE_PLATE, XMaterial.POLISHED_BLACKSTONE_PRESSURE_PLATE });
        RAILS = simple((XBase[])new XMaterial[] { XMaterial.RAIL, XMaterial.ACTIVATOR_RAIL, XMaterial.DETECTOR_RAIL, XMaterial.POWERED_RAIL });
        ANIMALS_SPAWNABLE_ON = simple((XBase[])new XMaterial[] { XMaterial.GRASS_BLOCK });
        ANVIL = simple((XBase[])new XMaterial[] { XMaterial.ANVIL, XMaterial.CHIPPED_ANVIL, XMaterial.DAMAGED_ANVIL });
        AXOLOTL_TEMPT_ITEMS = simple((XBase[])new XMaterial[] { XMaterial.TROPICAL_FISH_BUCKET });
        AXOLOTLS_SPAWNABLE_ON = simple((XBase[])new XMaterial[] { XMaterial.CLAY });
        SNOW = simple((XBase[])new XMaterial[] { XMaterial.SNOW_BLOCK, XMaterial.SNOW, XMaterial.POWDER_SNOW });
        SAND = simple((XBase[])new XMaterial[] { XMaterial.SAND, XMaterial.RED_SAND });
        DIRT = simple((XBase[])new XMaterial[] { XMaterial.MOSS_BLOCK, XMaterial.COARSE_DIRT, XMaterial.PODZOL, XMaterial.DIRT, XMaterial.ROOTED_DIRT, XMaterial.MYCELIUM, XMaterial.GRASS_BLOCK });
        CAVE_VINES = simple((XBase[])new XMaterial[] { XMaterial.CAVE_VINES, XMaterial.CAVE_VINES_PLANT });
        BASE_STONE_NETHER = simple((XBase[])new XMaterial[] { XMaterial.NETHERRACK, XMaterial.BASALT, XMaterial.BLACKSTONE });
        BASE_STONE_OVERWORLD = simple((XBase[])new XMaterial[] { XMaterial.TUFF, XMaterial.DIORITE, XMaterial.DEEPSLATE, XMaterial.ANDESITE, XMaterial.GRANITE, XMaterial.STONE });
        BEACON_BASE_BLOCKS = simple((XBase[])new XMaterial[] { XMaterial.NETHERITE_BLOCK, XMaterial.GOLD_BLOCK, XMaterial.IRON_BLOCK, XMaterial.EMERALD_BLOCK, XMaterial.DIAMOND_BLOCK });
        CROPS = simple((XBase[])new XMaterial[] { XMaterial.CARROT, XMaterial.CARROTS, XMaterial.POTATO, XMaterial.POTATOES, XMaterial.NETHER_WART, XMaterial.PUMPKIN_SEEDS, XMaterial.WHEAT_SEEDS, XMaterial.WHEAT, XMaterial.MELON_SEEDS, XMaterial.BEETROOT_SEEDS, XMaterial.BEETROOTS, XMaterial.SUGAR_CANE, XMaterial.BAMBOO_SAPLING, XMaterial.BAMBOO, XMaterial.CHORUS_PLANT, XMaterial.KELP, XMaterial.KELP_PLANT, XMaterial.SEA_PICKLE, XMaterial.BROWN_MUSHROOM, XMaterial.RED_MUSHROOM, XMaterial.MELON_STEM, XMaterial.PUMPKIN_STEM, XMaterial.COCOA, XMaterial.COCOA_BEANS });
        CAMPFIRES = simple((XBase[])new XMaterial[] { XMaterial.CAMPFIRE, XMaterial.SOUL_CAMPFIRE });
        FILLED_CAULDRONS = simple((XBase[])new XMaterial[] { XMaterial.LAVA_CAULDRON, XMaterial.POWDER_SNOW_CAULDRON, XMaterial.WATER_CAULDRON });
        CAULDRONS = simple((XBase[])new XMaterial[] { XMaterial.CAULDRON, XMaterial.LAVA_CAULDRON, XMaterial.POWDER_SNOW_CAULDRON, XMaterial.WATER_CAULDRON });
        CLIMBABLE = of((XBase[])new XMaterial[] { XMaterial.SCAFFOLDING, XMaterial.WEEPING_VINES_PLANT, XMaterial.WEEPING_VINES, XMaterial.TWISTING_VINES, XMaterial.TWISTING_VINES_PLANT, XMaterial.VINE, XMaterial.LADDER }).inheritFrom(XTag.CAVE_VINES).build();
        CLUSTER_MAX_HARVESTABLES = simple((XBase[])new XMaterial[] { XMaterial.DIAMOND_PICKAXE, XMaterial.GOLDEN_PICKAXE, XMaterial.STONE_PICKAXE, XMaterial.NETHERITE_PICKAXE, XMaterial.WOODEN_PICKAXE, XMaterial.IRON_PICKAXE });
        CRIMSON_STEMS = simple((XBase[])new XMaterial[] { XMaterial.CRIMSON_HYPHAE, XMaterial.STRIPPED_CRIMSON_STEM, XMaterial.CRIMSON_STEM, XMaterial.STRIPPED_CRIMSON_HYPHAE });
        WARPED_STEMS = simple((XBase[])new XMaterial[] { XMaterial.WARPED_HYPHAE, XMaterial.STRIPPED_WARPED_STEM, XMaterial.WARPED_STEM, XMaterial.STRIPPED_WARPED_HYPHAE });
        CRYSTAL_SOUND_BLOCKS = simple((XBase[])new XMaterial[] { XMaterial.AMETHYST_BLOCK, XMaterial.BUDDING_AMETHYST });
        DEEPSLATE_ORE_REPLACEABLES = simple((XBase[])new XMaterial[] { XMaterial.TUFF, XMaterial.DEEPSLATE });
        DOORS = of((XBase[])new XMaterial[] { XMaterial.IRON_DOOR }).inheritFrom(XTag.WOODEN_DOORS).build();
        WITHER_IMMUNE = simple((XBase[])new XMaterial[] { XMaterial.STRUCTURE_BLOCK, XMaterial.END_GATEWAY, XMaterial.BEDROCK, XMaterial.END_PORTAL, XMaterial.COMMAND_BLOCK, XMaterial.REPEATING_COMMAND_BLOCK, XMaterial.MOVING_PISTON, XMaterial.CHAIN_COMMAND_BLOCK, XMaterial.BARRIER, XMaterial.END_PORTAL_FRAME, XMaterial.JIGSAW });
        WITHER_SUMMON_BASE_BLOCKS = simple((XBase[])new XMaterial[] { XMaterial.SOUL_SOIL, XMaterial.SOUL_SAND });
        NYLIUM = simple((XBase[])new XMaterial[] { XMaterial.CRIMSON_NYLIUM, XMaterial.WARPED_NYLIUM });
        SMALL_FLOWERS = simple((XBase[])new XMaterial[] { XMaterial.RED_TULIP, XMaterial.AZURE_BLUET, XMaterial.OXEYE_DAISY, XMaterial.BLUE_ORCHID, XMaterial.PINK_TULIP, XMaterial.POPPY, XMaterial.WHITE_TULIP, XMaterial.DANDELION, XMaterial.ALLIUM, XMaterial.CORNFLOWER, XMaterial.ORANGE_TULIP, XMaterial.LILY_OF_THE_VALLEY, XMaterial.WITHER_ROSE });
        TALL_FLOWERS = simple((XBase[])new XMaterial[] { XMaterial.PEONY, XMaterial.SUNFLOWER, XMaterial.LILAC, XMaterial.ROSE_BUSH });
        FEATURES_CANNOT_REPLACE = simple((XBase[])new XMaterial[] { XMaterial.SPAWNER, XMaterial.END_PORTAL_FRAME, XMaterial.BEDROCK, XMaterial.CHEST });
        FENCE_GATES = simple((XTag<XBase>[])new XTag[] { XTag.WOODEN_FENCE_GATES });
        FENCES = of((XBase[])new XMaterial[] { XMaterial.NETHER_BRICK_FENCE }).inheritFrom(XTag.WOODEN_FENCES).build();
        FLOWER_POTS = simple((XBase[])new XMaterial[] { XMaterial.POTTED_OAK_SAPLING, XMaterial.POTTED_WITHER_ROSE, XMaterial.POTTED_ACACIA_SAPLING, XMaterial.POTTED_LILY_OF_THE_VALLEY, XMaterial.POTTED_WARPED_FUNGUS, XMaterial.POTTED_WARPED_ROOTS, XMaterial.POTTED_ALLIUM, XMaterial.POTTED_BROWN_MUSHROOM, XMaterial.POTTED_WHITE_TULIP, XMaterial.POTTED_ORANGE_TULIP, XMaterial.POTTED_DANDELION, XMaterial.POTTED_AZURE_BLUET, XMaterial.POTTED_FLOWERING_AZALEA_BUSH, XMaterial.POTTED_PINK_TULIP, XMaterial.POTTED_CORNFLOWER, XMaterial.POTTED_CRIMSON_FUNGUS, XMaterial.POTTED_RED_MUSHROOM, XMaterial.POTTED_BLUE_ORCHID, XMaterial.POTTED_FERN, XMaterial.POTTED_POPPY, XMaterial.POTTED_CRIMSON_ROOTS, XMaterial.POTTED_RED_TULIP, XMaterial.POTTED_OXEYE_DAISY, XMaterial.POTTED_AZALEA_BUSH, XMaterial.POTTED_BAMBOO, XMaterial.POTTED_CACTUS, XMaterial.FLOWER_POT, XMaterial.POTTED_DEAD_BUSH, XMaterial.POTTED_DARK_OAK_SAPLING, XMaterial.POTTED_PALE_OAK_SAPLING, XMaterial.POTTED_SPRUCE_SAPLING, XMaterial.POTTED_JUNGLE_SAPLING, XMaterial.POTTED_BIRCH_SAPLING, XMaterial.POTTED_MANGROVE_PROPAGULE, XMaterial.POTTED_CHERRY_SAPLING, XMaterial.POTTED_TORCHFLOWER });
        FOX_FOOD = simple((XBase[])new XMaterial[] { XMaterial.GLOW_BERRIES, XMaterial.SWEET_BERRIES });
        FOXES_SPAWNABLE_ON = simple((XBase[])new XMaterial[] { XMaterial.SNOW, XMaterial.SNOW_BLOCK, XMaterial.PODZOL, XMaterial.GRASS_BLOCK, XMaterial.COARSE_DIRT });
        FREEZE_IMMUNE_WEARABLES = simple((XBase[])new XMaterial[] { XMaterial.LEATHER_BOOTS, XMaterial.LEATHER_CHESTPLATE, XMaterial.LEATHER_HELMET, XMaterial.LEATHER_LEGGINGS, XMaterial.LEATHER_HORSE_ARMOR });
        ICE = simple((XBase[])new XMaterial[] { XMaterial.ICE, XMaterial.PACKED_ICE, XMaterial.BLUE_ICE, XMaterial.FROSTED_ICE });
        GEODE_INVALID_BLOCKS = simple((XBase[])new XMaterial[] { XMaterial.BEDROCK, XMaterial.WATER, XMaterial.LAVA, XMaterial.ICE, XMaterial.PACKED_ICE, XMaterial.BLUE_ICE });
        HOGLIN_REPELLENTS = simple((XBase[])new XMaterial[] { XMaterial.WARPED_FUNGUS, XMaterial.NETHER_PORTAL, XMaterial.POTTED_WARPED_FUNGUS, XMaterial.RESPAWN_ANCHOR });
        IGNORED_BY_PIGLIN_BABIES = simple((XBase[])new XMaterial[] { XMaterial.LEATHER });
        IMPERMEABLE = simple((XTag<XBase>[])new XTag[] { XTag.GLASS });
        INFINIBURN_END = simple((XBase[])new XMaterial[] { XMaterial.BEDROCK, XMaterial.NETHERRACK, XMaterial.MAGMA_BLOCK });
        INFINIBURN_NETHER = simple((XBase[])new XMaterial[] { XMaterial.NETHERRACK, XMaterial.MAGMA_BLOCK });
        INFINIBURN_OVERWORLD = simple((XBase[])new XMaterial[] { XMaterial.NETHERRACK, XMaterial.MAGMA_BLOCK });
        INSIDE_STEP_SOUND_BLOCKS = simple((XBase[])new XMaterial[] { XMaterial.SNOW, XMaterial.POWDER_SNOW });
        ITEMS_ARROWS = simple((XBase[])new XMaterial[] { XMaterial.ARROW, XMaterial.SPECTRAL_ARROW, XMaterial.TIPPED_ARROW });
        ITEMS_BEACON_PAYMENT_ITEMS = simple((XBase[])new XMaterial[] { XMaterial.EMERALD, XMaterial.DIAMOND, XMaterial.NETHERITE_INGOT, XMaterial.IRON_INGOT, XMaterial.GOLD_INGOT });
        ITEMS_BOATS = simple((XBase[])new XMaterial[] { XMaterial.OAK_BOAT, XMaterial.ACACIA_BOAT, XMaterial.DARK_OAK_BOAT, XMaterial.PALE_OAK_BOAT, XMaterial.BIRCH_BOAT, XMaterial.SPRUCE_BOAT, XMaterial.JUNGLE_BOAT, XMaterial.MANGROVE_BOAT, XMaterial.CHERRY_BOAT, XMaterial.BAMBOO_RAFT });
        ITEMS_COALS = simple((XBase[])new XMaterial[] { XMaterial.COAL, XMaterial.CHARCOAL });
        ITEMS_CREEPER_DROP_MUSIC_DISCS = simple((XBase[])new XMaterial[] { XMaterial.MUSIC_DISC_BLOCKS, XMaterial.MUSIC_DISC_11, XMaterial.MUSIC_DISC_WAIT, XMaterial.MUSIC_DISC_MELLOHI, XMaterial.MUSIC_DISC_STAL, XMaterial.MUSIC_DISC_WARD, XMaterial.MUSIC_DISC_13, XMaterial.MUSIC_DISC_CAT, XMaterial.MUSIC_DISC_CHIRP, XMaterial.MUSIC_DISC_MALL, XMaterial.MUSIC_DISC_FAR, XMaterial.MUSIC_DISC_STRAD });
        ITEMS_FISHES = simple((XBase[])new XMaterial[] { XMaterial.TROPICAL_FISH, XMaterial.SALMON, XMaterial.PUFFERFISH, XMaterial.COOKED_COD, XMaterial.COD, XMaterial.COOKED_SALMON });
        ITEMS_LECTERN_BOOKS = simple((XBase[])new XMaterial[] { XMaterial.WRITABLE_BOOK, XMaterial.WRITTEN_BOOK });
        ITEMS_STONE_TOOL_MATERIALS = simple((XBase[])new XMaterial[] { XMaterial.COBBLED_DEEPSLATE, XMaterial.BLACKSTONE, XMaterial.COBBLESTONE });
        LEAVES = simple((XBase[])new XMaterial[] { XMaterial.SPRUCE_LEAVES, XMaterial.ACACIA_LEAVES, XMaterial.DARK_OAK_LEAVES, XMaterial.AZALEA_LEAVES, XMaterial.JUNGLE_LEAVES, XMaterial.FLOWERING_AZALEA_LEAVES, XMaterial.BIRCH_LEAVES, XMaterial.OAK_LEAVES, XMaterial.MANGROVE_LEAVES, XMaterial.CHERRY_LEAVES });
        NON_WOODEN_STAIRS = simple((XBase[])new XMaterial[] { XMaterial.STONE_BRICK_STAIRS, XMaterial.STONE_STAIRS, XMaterial.POLISHED_BLACKSTONE_BRICK_STAIRS, XMaterial.RED_SANDSTONE_STAIRS, XMaterial.PRISMARINE_STAIRS, XMaterial.GRANITE_STAIRS, XMaterial.WAXED_WEATHERED_CUT_COPPER_STAIRS, XMaterial.POLISHED_DIORITE_STAIRS, XMaterial.WEATHERED_CUT_COPPER_STAIRS, XMaterial.NETHER_BRICK_STAIRS, XMaterial.RED_NETHER_BRICK_STAIRS, XMaterial.PRISMARINE_BRICK_STAIRS, XMaterial.WAXED_CUT_COPPER_STAIRS, XMaterial.DEEPSLATE_TILE_STAIRS, XMaterial.POLISHED_ANDESITE_STAIRS, XMaterial.SMOOTH_RED_SANDSTONE_STAIRS, XMaterial.PURPUR_STAIRS, XMaterial.POLISHED_DEEPSLATE_STAIRS, XMaterial.QUARTZ_STAIRS, XMaterial.MOSSY_COBBLESTONE_STAIRS, XMaterial.BRICK_STAIRS, XMaterial.CUT_COPPER_STAIRS, XMaterial.SANDSTONE_STAIRS, XMaterial.ANDESITE_STAIRS, XMaterial.WAXED_EXPOSED_CUT_COPPER_STAIRS, XMaterial.COBBLED_DEEPSLATE_STAIRS, XMaterial.COBBLESTONE_STAIRS, XMaterial.DEEPSLATE_BRICK_STAIRS, XMaterial.DIORITE_STAIRS, XMaterial.SMOOTH_QUARTZ_STAIRS, XMaterial.EXPOSED_CUT_COPPER_STAIRS, XMaterial.DARK_PRISMARINE_STAIRS, XMaterial.OXIDIZED_CUT_COPPER_STAIRS, XMaterial.POLISHED_BLACKSTONE_STAIRS, XMaterial.POLISHED_GRANITE_STAIRS, XMaterial.MOSSY_STONE_BRICK_STAIRS, XMaterial.END_STONE_BRICK_STAIRS, XMaterial.WAXED_OXIDIZED_CUT_COPPER_STAIRS, XMaterial.SMOOTH_SANDSTONE_STAIRS, XMaterial.BLACKSTONE_STAIRS });
        STAIRS = simple((XTag<XBase>[])new XTag[] { XTag.NON_WOODEN_STAIRS, XTag.WOODEN_STAIRS });
        NON_WOODEN_SLABS = simple((XBase[])new XMaterial[] { XMaterial.MOSSY_COBBLESTONE_SLAB, XMaterial.EXPOSED_CUT_COPPER_SLAB, XMaterial.SMOOTH_QUARTZ_SLAB, XMaterial.COBBLESTONE_SLAB, XMaterial.POLISHED_BLACKSTONE_SLAB, XMaterial.OXIDIZED_CUT_COPPER_SLAB, XMaterial.POLISHED_ANDESITE_SLAB, XMaterial.RED_SANDSTONE_SLAB, XMaterial.BLACKSTONE_SLAB, XMaterial.STONE_SLAB, XMaterial.SMOOTH_SANDSTONE_SLAB, XMaterial.COBBLED_DEEPSLATE_SLAB, XMaterial.SMOOTH_RED_SANDSTONE_SLAB, XMaterial.POLISHED_DIORITE_SLAB, XMaterial.PRISMARINE_BRICK_SLAB, XMaterial.QUARTZ_SLAB, XMaterial.DIORITE_SLAB, XMaterial.NETHER_BRICK_SLAB, XMaterial.PRISMARINE_SLAB, XMaterial.WAXED_EXPOSED_CUT_COPPER_SLAB, XMaterial.RED_NETHER_BRICK_SLAB, XMaterial.POLISHED_BLACKSTONE_BRICK_SLAB, XMaterial.MOSSY_STONE_BRICK_SLAB, XMaterial.SMOOTH_STONE_SLAB, XMaterial.SANDSTONE_SLAB, XMaterial.WEATHERED_CUT_COPPER_SLAB, XMaterial.DEEPSLATE_BRICK_SLAB, XMaterial.POLISHED_DEEPSLATE_SLAB, XMaterial.GRANITE_SLAB, XMaterial.ANDESITE_SLAB, XMaterial.CUT_COPPER_SLAB, XMaterial.CUT_SANDSTONE_SLAB, XMaterial.END_STONE_BRICK_SLAB, XMaterial.WAXED_OXIDIZED_CUT_COPPER_SLAB, XMaterial.CUT_RED_SANDSTONE_SLAB, XMaterial.PURPUR_SLAB, XMaterial.STONE_BRICK_SLAB, XMaterial.WAXED_CUT_COPPER_SLAB, XMaterial.DEEPSLATE_TILE_SLAB, XMaterial.DARK_PRISMARINE_SLAB, XMaterial.PETRIFIED_OAK_SLAB, XMaterial.WAXED_WEATHERED_CUT_COPPER_SLAB, XMaterial.BRICK_SLAB, XMaterial.POLISHED_GRANITE_SLAB });
        POTTERY_SHERDS = simple((XBase[])new XMaterial[] { XMaterial.ANGLER_POTTERY_SHERD, XMaterial.ARCHER_POTTERY_SHERD, XMaterial.ARMS_UP_POTTERY_SHERD, XMaterial.BLADE_POTTERY_SHERD, XMaterial.BREWER_POTTERY_SHERD, XMaterial.BURN_POTTERY_SHERD, XMaterial.DANGER_POTTERY_SHERD, XMaterial.EXPLORER_POTTERY_SHERD, XMaterial.FRIEND_POTTERY_SHERD, XMaterial.HEART_POTTERY_SHERD, XMaterial.HEARTBREAK_POTTERY_SHERD, XMaterial.HOWL_POTTERY_SHERD, XMaterial.MINER_POTTERY_SHERD, XMaterial.MOURNER_POTTERY_SHERD, XMaterial.PLENTY_POTTERY_SHERD, XMaterial.PRIZE_POTTERY_SHERD, XMaterial.SHEAF_POTTERY_SHERD, XMaterial.SHELTER_POTTERY_SHERD, XMaterial.SKULL_POTTERY_SHERD, XMaterial.SNORT_POTTERY_SHERD });
        SOUL_FIRE_BASE_BLOCKS = simple((XBase[])new XMaterial[] { XMaterial.SOUL_SOIL, XMaterial.SOUL_SAND });
        SOUL_SPEED_BLOCKS = simple((XBase[])new XMaterial[] { XMaterial.SOUL_SOIL, XMaterial.SOUL_SAND });
        STONE_ORE_REPLACEABLES = simple((XBase[])new XMaterial[] { XMaterial.STONE, XMaterial.DIORITE, XMaterial.ANDESITE, XMaterial.GRANITE });
        STRIDER_WARM_BLOCKS = simple((XBase[])new XMaterial[] { XMaterial.LAVA });
        VALID_SPAWN = simple((XBase[])new XMaterial[] { XMaterial.PODZOL, XMaterial.GRASS_BLOCK });
        STONE_BRICKS = simple((XBase[])new XMaterial[] { XMaterial.CHISELED_STONE_BRICKS, XMaterial.CRACKED_STONE_BRICKS, XMaterial.MOSSY_STONE_BRICKS, XMaterial.STONE_BRICKS });
        SAPLINGS = simple((XBase[])new XMaterial[] { XMaterial.ACACIA_SAPLING, XMaterial.JUNGLE_SAPLING, XMaterial.SPRUCE_SAPLING, XMaterial.DARK_OAK_SAPLING, XMaterial.PALE_OAK_SAPLING, XMaterial.AZALEA, XMaterial.OAK_SAPLING, XMaterial.FLOWERING_AZALEA, XMaterial.BIRCH_SAPLING, XMaterial.MANGROVE_PROPAGULE, XMaterial.CHERRY_SAPLING });
        WOLVES_SPAWNABLE_ON = simple((XBase[])new XMaterial[] { XMaterial.GRASS_BLOCK, XMaterial.SNOW, XMaterial.SNOW_BLOCK });
        POLAR_BEARS_SPAWNABLE_ON_IN_FROZEN_OCEAN = simple((XBase[])new XMaterial[] { XMaterial.ICE });
        RABBITS_SPAWNABLE_ON = simple((XBase[])new XMaterial[] { XMaterial.GRASS_BLOCK, XMaterial.SNOW, XMaterial.SNOW_BLOCK, XMaterial.SAND });
        PIGLIN_FOOD = simple((XBase[])new XMaterial[] { XMaterial.COOKED_PORKCHOP, XMaterial.PORKCHOP });
        PIGLIN_REPELLENTS = simple((XBase[])new XMaterial[] { XMaterial.SOUL_WALL_TORCH, XMaterial.SOUL_TORCH, XMaterial.SOUL_CAMPFIRE, XMaterial.SOUL_LANTERN, XMaterial.SOUL_FIRE });
        REPLACEABLE_PLANTS = simple((XBase[])new XMaterial[] { XMaterial.FERN, XMaterial.GLOW_LICHEN, XMaterial.DEAD_BUSH, XMaterial.PEONY, XMaterial.TALL_GRASS, XMaterial.HANGING_ROOTS, XMaterial.VINE, XMaterial.SUNFLOWER, XMaterial.LARGE_FERN, XMaterial.LILAC, XMaterial.ROSE_BUSH, XMaterial.SHORT_GRASS });
        SMALL_DRIPLEAF_PLACEABLE = simple((XBase[])new XMaterial[] { XMaterial.CLAY, XMaterial.MOSS_BLOCK });
        NON_FLAMMABLE_WOOD = simple((XBase[])new XMaterial[] { XMaterial.CRIMSON_PLANKS, XMaterial.WARPED_WALL_SIGN, XMaterial.CRIMSON_FENCE_GATE, XMaterial.WARPED_HYPHAE, XMaterial.CRIMSON_HYPHAE, XMaterial.WARPED_STEM, XMaterial.WARPED_TRAPDOOR, XMaterial.STRIPPED_CRIMSON_HYPHAE, XMaterial.CRIMSON_PRESSURE_PLATE, XMaterial.WARPED_STAIRS, XMaterial.CRIMSON_SIGN, XMaterial.CRIMSON_STAIRS, XMaterial.STRIPPED_WARPED_STEM, XMaterial.CRIMSON_FENCE, XMaterial.WARPED_FENCE, XMaterial.CRIMSON_TRAPDOOR, XMaterial.STRIPPED_WARPED_HYPHAE, XMaterial.WARPED_DOOR, XMaterial.WARPED_PRESSURE_PLATE, XMaterial.WARPED_PLANKS, XMaterial.STRIPPED_CRIMSON_STEM, XMaterial.CRIMSON_STEM, XMaterial.CRIMSON_SLAB, XMaterial.CRIMSON_WALL_SIGN, XMaterial.WARPED_FENCE_GATE, XMaterial.WARPED_BUTTON, XMaterial.WARPED_SLAB, XMaterial.CRIMSON_DOOR, XMaterial.CRIMSON_BUTTON, XMaterial.WARPED_SIGN });
        MOOSHROOMS_SPAWNABLE_ON = simple((XBase[])new XMaterial[] { XMaterial.MYCELIUM });
        NEEDS_STONE_TOOL = simple((XBase[])new XMaterial[] { XMaterial.OXIDIZED_CUT_COPPER, XMaterial.DEEPSLATE_COPPER_ORE, XMaterial.EXPOSED_CUT_COPPER_SLAB, XMaterial.WAXED_OXIDIZED_CUT_COPPER_SLAB, XMaterial.WAXED_OXIDIZED_CUT_COPPER, XMaterial.OXIDIZED_CUT_COPPER_SLAB, XMaterial.WAXED_WEATHERED_CUT_COPPER, XMaterial.WAXED_WEATHERED_CUT_COPPER_STAIRS, XMaterial.WEATHERED_COPPER, XMaterial.WEATHERED_CUT_COPPER_STAIRS, XMaterial.EXPOSED_CUT_COPPER, XMaterial.DEEPSLATE_LAPIS_ORE, XMaterial.COPPER_ORE, XMaterial.WEATHERED_CUT_COPPER, XMaterial.WAXED_CUT_COPPER_STAIRS, XMaterial.WAXED_EXPOSED_CUT_COPPER, XMaterial.OXIDIZED_COPPER, XMaterial.WAXED_COPPER_BLOCK, XMaterial.RAW_IRON_BLOCK, XMaterial.LAPIS_BLOCK, XMaterial.DEEPSLATE_IRON_ORE, XMaterial.CUT_COPPER_STAIRS, XMaterial.COPPER_BLOCK, XMaterial.WAXED_WEATHERED_CUT_COPPER_SLAB, XMaterial.IRON_BLOCK, XMaterial.WAXED_EXPOSED_CUT_COPPER_STAIRS, XMaterial.RAW_COPPER_BLOCK, XMaterial.LAPIS_ORE, XMaterial.WEATHERED_CUT_COPPER_SLAB, XMaterial.CUT_COPPER_SLAB, XMaterial.IRON_ORE, XMaterial.EXPOSED_COPPER, XMaterial.WAXED_EXPOSED_COPPER, XMaterial.EXPOSED_CUT_COPPER_STAIRS, XMaterial.WAXED_CUT_COPPER_SLAB, XMaterial.WAXED_EXPOSED_CUT_COPPER_SLAB, XMaterial.OXIDIZED_CUT_COPPER_STAIRS, XMaterial.WAXED_OXIDIZED_COPPER, XMaterial.WAXED_CUT_COPPER, XMaterial.WAXED_WEATHERED_COPPER, XMaterial.LIGHTNING_ROD, XMaterial.WAXED_OXIDIZED_CUT_COPPER_STAIRS, XMaterial.CUT_COPPER });
        NEEDS_IRON_TOOL = simple((XBase[])new XMaterial[] { XMaterial.GOLD_ORE, XMaterial.GOLD_BLOCK, XMaterial.REDSTONE_ORE, XMaterial.RAW_GOLD_BLOCK, XMaterial.EMERALD_BLOCK, XMaterial.DIAMOND_BLOCK, XMaterial.DIAMOND_ORE, XMaterial.DEEPSLATE_EMERALD_ORE, XMaterial.DEEPSLATE_GOLD_ORE, XMaterial.EMERALD_ORE, XMaterial.DEEPSLATE_REDSTONE_ORE, XMaterial.DEEPSLATE_DIAMOND_ORE });
        NEEDS_DIAMOND_TOOL = simple((XBase[])new XMaterial[] { XMaterial.OBSIDIAN, XMaterial.NETHERITE_BLOCK, XMaterial.ANCIENT_DEBRIS, XMaterial.RESPAWN_ANCHOR, XMaterial.CRYING_OBSIDIAN });
        MINEABLE_PICKAXE = of((XBase[])new XMaterial[] { XMaterial.OXIDIZED_CUT_COPPER, XMaterial.GOLD_BLOCK, XMaterial.SMOOTH_SANDSTONE, XMaterial.IRON_DOOR, XMaterial.COBBLESTONE, XMaterial.DRIPSTONE_BLOCK, XMaterial.CHISELED_SANDSTONE, XMaterial.INFESTED_STONE_BRICKS, XMaterial.QUARTZ_BLOCK, XMaterial.COPPER_BLOCK, XMaterial.STONE_BRICKS, XMaterial.CHISELED_POLISHED_BLACKSTONE, XMaterial.DISPENSER, XMaterial.DEEPSLATE_BRICKS, XMaterial.HEAVY_WEIGHTED_PRESSURE_PLATE, XMaterial.OBSIDIAN, XMaterial.EXPOSED_CUT_COPPER, XMaterial.SMOOTH_QUARTZ, XMaterial.SMOOTH_RED_SANDSTONE, XMaterial.STONE, XMaterial.INFESTED_COBBLESTONE, XMaterial.WAXED_CUT_COPPER, XMaterial.PRISMARINE, XMaterial.PISTON, XMaterial.CUT_COPPER, XMaterial.CHISELED_QUARTZ_BLOCK, XMaterial.MOSSY_STONE_BRICKS, XMaterial.EMERALD_BLOCK, XMaterial.BELL, XMaterial.AMETHYST_BLOCK, XMaterial.GILDED_BLACKSTONE, XMaterial.CHISELED_NETHER_BRICKS, XMaterial.WAXED_COPPER_BLOCK, XMaterial.IRON_BLOCK, XMaterial.BUDDING_AMETHYST, XMaterial.POLISHED_DEEPSLATE, XMaterial.HOPPER, XMaterial.CUT_RED_SANDSTONE, XMaterial.QUARTZ_BRICKS, XMaterial.CHISELED_STONE_BRICKS, XMaterial.ENDER_CHEST, XMaterial.END_STONE_BRICKS, XMaterial.NETHERRACK, XMaterial.REDSTONE_BLOCK, XMaterial.WAXED_OXIDIZED_CUT_COPPER, XMaterial.LIGHT_WEIGHTED_PRESSURE_PLATE, XMaterial.WAXED_WEATHERED_CUT_COPPER, XMaterial.CHAIN, XMaterial.MAGMA_BLOCK, XMaterial.STONE_PRESSURE_PLATE, XMaterial.DARK_PRISMARINE, XMaterial.MEDIUM_AMETHYST_BUD, XMaterial.LANTERN, XMaterial.ICE, XMaterial.DIORITE, XMaterial.DROPPER, XMaterial.CRACKED_NETHER_BRICKS, XMaterial.BREWING_STAND, XMaterial.CHISELED_RED_SANDSTONE, XMaterial.CALCITE, XMaterial.CUT_SANDSTONE, XMaterial.POLISHED_BASALT, XMaterial.DEEPSLATE_TILES, XMaterial.QUARTZ_PILLAR, XMaterial.LODESTONE, XMaterial.POLISHED_GRANITE, XMaterial.POLISHED_ANDESITE, XMaterial.OBSERVER, XMaterial.CHISELED_DEEPSLATE, XMaterial.RAW_GOLD_BLOCK, XMaterial.CRACKED_POLISHED_BLACKSTONE_BRICKS, XMaterial.WAXED_EXPOSED_CUT_COPPER, XMaterial.SMALL_AMETHYST_BUD, XMaterial.OXIDIZED_COPPER, XMaterial.POLISHED_BLACKSTONE, XMaterial.RAW_IRON_BLOCK, XMaterial.POLISHED_BLACKSTONE_BRICKS, XMaterial.INFESTED_DEEPSLATE, XMaterial.RAW_COPPER_BLOCK, XMaterial.BLACKSTONE, XMaterial.AMETHYST_CLUSTER, XMaterial.GRINDSTONE, XMaterial.WAXED_EXPOSED_COPPER, XMaterial.RED_SANDSTONE, XMaterial.LIGHTNING_ROD, XMaterial.SOUL_LANTERN, XMaterial.POLISHED_BLACKSTONE_PRESSURE_PLATE, XMaterial.IRON_BARS, XMaterial.PURPUR_BLOCK, XMaterial.FURNACE, XMaterial.CONDUIT, XMaterial.SPAWNER, XMaterial.COAL_BLOCK, XMaterial.BONE_BLOCK, XMaterial.WARPED_NYLIUM, XMaterial.WEATHERED_COPPER, XMaterial.WEATHERED_CUT_COPPER, XMaterial.MOSSY_COBBLESTONE, XMaterial.SMOKER, XMaterial.COBBLED_DEEPSLATE, XMaterial.SMOOTH_BASALT, XMaterial.STONE_BUTTON, XMaterial.NETHER_BRICKS, XMaterial.BRICKS, XMaterial.RED_NETHER_BRICKS, XMaterial.SMOOTH_STONE, XMaterial.ANDESITE, XMaterial.BASALT, XMaterial.TUFF, XMaterial.END_STONE, XMaterial.WAXED_OXIDIZED_COPPER, XMaterial.INFESTED_CHISELED_STONE_BRICKS, XMaterial.PRISMARINE_BRICKS, XMaterial.CRYING_OBSIDIAN, XMaterial.CRACKED_DEEPSLATE_TILES, XMaterial.INFESTED_STONE, XMaterial.IRON_TRAPDOOR, XMaterial.INFESTED_MOSSY_STONE_BRICKS, XMaterial.RESPAWN_ANCHOR, XMaterial.BLUE_ICE, XMaterial.POLISHED_DIORITE, XMaterial.NETHER_BRICK_FENCE, XMaterial.INFESTED_CRACKED_STONE_BRICKS, XMaterial.SANDSTONE, XMaterial.EXPOSED_COPPER, XMaterial.WAXED_WEATHERED_COPPER, XMaterial.CRACKED_DEEPSLATE_BRICKS, XMaterial.LARGE_AMETHYST_BUD, XMaterial.PISTON_HEAD, XMaterial.NETHERITE_BLOCK, XMaterial.PURPUR_PILLAR, XMaterial.GRANITE, XMaterial.STONECUTTER, XMaterial.BLAST_FURNACE, XMaterial.ENCHANTING_TABLE, XMaterial.LAPIS_BLOCK, XMaterial.PACKED_ICE, XMaterial.CRACKED_STONE_BRICKS, XMaterial.DEEPSLATE, XMaterial.CRIMSON_NYLIUM, XMaterial.STICKY_PISTON, XMaterial.DIAMOND_BLOCK, XMaterial.POINTED_DRIPSTONE }).inheritFrom(XTag.TERRACOTTA, XTag.GLAZED_TERRACOTTA, XTag.WALLS, XTag.CORALS, XTag.SHULKER_BOXES, XTag.RAILS, XTag.DIAMOND_ORES, XTag.GOLD_ORES, XTag.IRON_ORES, XTag.EMERALD_ORES, XTag.COPPER_ORES, XTag.ANVIL, XTag.CONCRETE, XTag.NON_WOODEN_STAIRS, XTag.NON_WOODEN_SLABS, XTag.CAULDRONS).build();
        MINEABLE_SHOVEL = of((XBase[])new XMaterial[] { XMaterial.FARMLAND, XMaterial.DIRT_PATH, XMaterial.SNOW, XMaterial.SNOW_BLOCK, XMaterial.RED_SAND, XMaterial.COARSE_DIRT, XMaterial.SOUL_SAND, XMaterial.GRAVEL, XMaterial.SAND, XMaterial.PODZOL, XMaterial.DIRT, XMaterial.CLAY, XMaterial.ROOTED_DIRT, XMaterial.MYCELIUM, XMaterial.SOUL_SOIL, XMaterial.GRASS_BLOCK }).inheritFrom(XTag.CONCRETE_POWDER).build();
        MINEABLE_HOE = simple((XBase[])new XMaterial[] { XMaterial.FLOWERING_AZALEA_LEAVES, XMaterial.DARK_OAK_LEAVES, XMaterial.PALE_OAK_LEAVES, XMaterial.SHROOMLIGHT, XMaterial.BIRCH_LEAVES, XMaterial.DRIED_KELP_BLOCK, XMaterial.JUNGLE_LEAVES, XMaterial.OAK_LEAVES, XMaterial.MOSS_CARPET, XMaterial.WET_SPONGE, XMaterial.AZALEA_LEAVES, XMaterial.NETHER_WART_BLOCK, XMaterial.WARPED_WART_BLOCK, XMaterial.SPONGE, XMaterial.SPRUCE_LEAVES, XMaterial.SCULK_SENSOR, XMaterial.HAY_BLOCK, XMaterial.TARGET, XMaterial.ACACIA_LEAVES, XMaterial.MANGROVE_LEAVES, XMaterial.CHERRY_LEAVES, XMaterial.MOSS_BLOCK });
        LAVA_POOL_STONE_CANNOT_REPLACE = simple((XBase[])new XMaterial[] { XMaterial.DARK_OAK_LEAVES, XMaterial.STRIPPED_DARK_OAK_WOOD, XMaterial.STRIPPED_PALE_OAK_WOOD, XMaterial.OAK_WOOD, XMaterial.CRIMSON_HYPHAE, XMaterial.JUNGLE_LEAVES, XMaterial.MANGROVE_LEAVES, XMaterial.CHERRY_LEAVES, XMaterial.DARK_OAK_WOOD, XMaterial.STRIPPED_ACACIA_LOG, XMaterial.DARK_OAK_LOG, XMaterial.STRIPPED_DARK_OAK_LOG, XMaterial.AZALEA_LEAVES, XMaterial.SPAWNER, XMaterial.JUNGLE_LOG, XMaterial.SPRUCE_LOG, XMaterial.MANGROVE_LOG, XMaterial.CHERRY_LOG, XMaterial.STRIPPED_CRIMSON_HYPHAE, XMaterial.SPRUCE_LEAVES, XMaterial.STRIPPED_BIRCH_LOG, XMaterial.PALE_OAK_WOOD, XMaterial.PALE_OAK_LOG, XMaterial.STRIPPED_PALE_OAK_LOG, XMaterial.ACACIA_LOG, XMaterial.STRIPPED_ACACIA_WOOD, XMaterial.CRIMSON_STEM, XMaterial.BIRCH_WOOD, XMaterial.STRIPPED_JUNGLE_WOOD, XMaterial.STRIPPED_MANGROVE_LOG, XMaterial.STRIPPED_CHERRY_LOG, XMaterial.WARPED_HYPHAE, XMaterial.CHEST, XMaterial.FLOWERING_AZALEA_LEAVES, XMaterial.STRIPPED_OAK_LOG, XMaterial.ACACIA_WOOD, XMaterial.BEDROCK, XMaterial.BIRCH_LEAVES, XMaterial.STRIPPED_CRIMSON_STEM, XMaterial.OAK_LEAVES, XMaterial.STRIPPED_BIRCH_WOOD, XMaterial.STRIPPED_MANGROVE_WOOD, XMaterial.STRIPPED_CHERRY_WOOD, XMaterial.STRIPPED_JUNGLE_LOG, XMaterial.WARPED_STEM, XMaterial.END_PORTAL_FRAME, XMaterial.SPRUCE_WOOD, XMaterial.STRIPPED_SPRUCE_LOG, XMaterial.STRIPPED_SPRUCE_WOOD, XMaterial.JUNGLE_WOOD, XMaterial.MANGROVE_WOOD, XMaterial.CHERRY_WOOD, XMaterial.STRIPPED_OAK_WOOD, XMaterial.STRIPPED_WARPED_STEM, XMaterial.OAK_LOG, XMaterial.ACACIA_LEAVES, XMaterial.STRIPPED_WARPED_HYPHAE, XMaterial.BIRCH_LOG });
        LEATHER_ARMOR_PIECES = simple((XBase[])new XMaterial[] { XMaterial.LEATHER_HELMET, XMaterial.LEATHER_CHESTPLATE, XMaterial.LEATHER_LEGGINGS, XMaterial.LEATHER_BOOTS });
        IRON_ARMOR_PIECES = simple((XBase[])new XMaterial[] { XMaterial.IRON_HELMET, XMaterial.IRON_CHESTPLATE, XMaterial.IRON_LEGGINGS, XMaterial.IRON_BOOTS });
        CHAINMAIL_ARMOR_PIECES = simple((XBase[])new XMaterial[] { XMaterial.CHAINMAIL_HELMET, XMaterial.CHAINMAIL_CHESTPLATE, XMaterial.CHAINMAIL_LEGGINGS, XMaterial.CHAINMAIL_BOOTS });
        GOLDEN_ARMOR_PIECES = simple((XBase[])new XMaterial[] { XMaterial.GOLDEN_HELMET, XMaterial.GOLDEN_CHESTPLATE, XMaterial.GOLDEN_LEGGINGS, XMaterial.GOLDEN_BOOTS });
        DIAMOND_ARMOR_PIECES = simple((XBase[])new XMaterial[] { XMaterial.DIAMOND_HELMET, XMaterial.DIAMOND_CHESTPLATE, XMaterial.DIAMOND_LEGGINGS, XMaterial.DIAMOND_BOOTS });
        NETHERITE_ARMOR_PIECES = simple((XBase[])new XMaterial[] { XMaterial.NETHERITE_HELMET, XMaterial.NETHERITE_CHESTPLATE, XMaterial.NETHERITE_LEGGINGS, XMaterial.NETHERITE_BOOTS });
        WOODEN_TOOLS = simple((XBase[])new XMaterial[] { XMaterial.WOODEN_PICKAXE, XMaterial.WOODEN_AXE, XMaterial.WOODEN_HOE, XMaterial.WOODEN_SHOVEL, XMaterial.WOODEN_SWORD });
        STONE_TOOLS = simple((XBase[])new XMaterial[] { XMaterial.STONE_PICKAXE, XMaterial.STONE_AXE, XMaterial.STONE_HOE, XMaterial.STONE_SHOVEL, XMaterial.STONE_SWORD });
        IRON_TOOLS = simple((XBase[])new XMaterial[] { XMaterial.IRON_PICKAXE, XMaterial.IRON_AXE, XMaterial.IRON_HOE, XMaterial.IRON_SHOVEL, XMaterial.IRON_SWORD });
        DIAMOND_TOOLS = simple((XBase[])new XMaterial[] { XMaterial.DIAMOND_PICKAXE, XMaterial.DIAMOND_AXE, XMaterial.DIAMOND_HOE, XMaterial.DIAMOND_SHOVEL, XMaterial.DIAMOND_SHOVEL });
        NETHERITE_TOOLS = simple((XBase[])new XMaterial[] { XMaterial.NETHERITE_PICKAXE, XMaterial.NETHERITE_AXE, XMaterial.NETHERITE_HOE, XMaterial.NETHERITE_SHOVEL, XMaterial.NETHERITE_SHOVEL });
        ARMOR_PIECES = of((XBase[])new XMaterial[] { XMaterial.TURTLE_HELMET }).inheritFrom(XTag.LEATHER_ARMOR_PIECES, XTag.CHAINMAIL_ARMOR_PIECES, XTag.IRON_ARMOR_PIECES, XTag.GOLDEN_ARMOR_PIECES, XTag.DIAMOND_ARMOR_PIECES, XTag.NETHERITE_ARMOR_PIECES).build();
        SMITHING_TEMPLATES = simple((XBase[])new XMaterial[] { XMaterial.NETHERITE_UPGRADE_SMITHING_TEMPLATE, XMaterial.COAST_ARMOR_TRIM_SMITHING_TEMPLATE, XMaterial.DUNE_ARMOR_TRIM_SMITHING_TEMPLATE, XMaterial.EYE_ARMOR_TRIM_SMITHING_TEMPLATE, XMaterial.HOST_ARMOR_TRIM_SMITHING_TEMPLATE, XMaterial.RAISER_ARMOR_TRIM_SMITHING_TEMPLATE, XMaterial.RIB_ARMOR_TRIM_SMITHING_TEMPLATE, XMaterial.SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE, XMaterial.SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE, XMaterial.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE, XMaterial.SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE, XMaterial.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE, XMaterial.TIDE_ARMOR_TRIM_SMITHING_TEMPLATE, XMaterial.VEX_ARMOR_TRIM_SMITHING_TEMPLATE, XMaterial.WARD_ARMOR_TRIM_SMITHING_TEMPLATE, XMaterial.WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE, XMaterial.WILD_ARMOR_TRIM_SMITHING_TEMPLATE });
        AZALEA_GROWS_ON = of((XBase[])new XMaterial[] { XMaterial.SNOW_BLOCK, XMaterial.POWDER_SNOW }).inheritFrom(XTag.TERRACOTTA, XTag.SAND, XTag.DIRT).build();
        AZALEA_ROOT_REPLACEABLE = of((XBase[])new XMaterial[] { XMaterial.CLAY, XMaterial.GRAVEL }).inheritFrom(XTag.AZALEA_GROWS_ON, XTag.CAVE_VINES, XTag.BASE_STONE_OVERWORLD).build();
        BAMBOO_PLANTABLE_ON = of((XBase[])new XMaterial[] { XMaterial.GRAVEL, XMaterial.BAMBOO_SAPLING, XMaterial.BAMBOO }).inheritFrom(XTag.DIRT, XTag.SAND).build();
        BEE_GROWABLES = of((XBase[])new XMaterial[] { XMaterial.SWEET_BERRY_BUSH }).inheritFrom(XTag.CROPS, XTag.CAVE_VINES).build();
        BIG_DRIPLEAF_PLACEABLE = of((XBase[])new XMaterial[] { XMaterial.CLAY, XMaterial.FARMLAND }).inheritFrom(XTag.DIRT).build();
        BUTTONS = of((XBase[])new XMaterial[] { XMaterial.STONE_BUTTON, XMaterial.POLISHED_BLACKSTONE_BUTTON }).inheritFrom(XTag.WOODEN_BUTTONS).build();
        DRIPSTONE_REPLACEABLE = of((XBase[])new XMaterial[] { XMaterial.DIRT }).inheritFrom(XTag.BASE_STONE_OVERWORLD).build();
        ENDERMAN_HOLDABLE = of((XBase[])new XMaterial[] { XMaterial.TNT, XMaterial.PUMPKIN, XMaterial.CARVED_PUMPKIN, XMaterial.MELON, XMaterial.CRIMSON_FUNGUS, XMaterial.WARPED_FUNGUS, XMaterial.WARPED_ROOTS, XMaterial.CRIMSON_ROOTS, XMaterial.RED_MUSHROOM, XMaterial.BROWN_MUSHROOM, XMaterial.CACTUS, XMaterial.GRAVEL, XMaterial.CLAY }).inheritFrom(XTag.DIRT, XTag.NYLIUM, XTag.SAND, XTag.SMALL_FLOWERS).build();
        FLOWERS = of((XBase[])new XMaterial[] { XMaterial.FLOWERING_AZALEA, XMaterial.FLOWERING_AZALEA_LEAVES }).inheritFrom(XTag.SMALL_FLOWERS, XTag.TALL_FLOWERS).build();
        GOATS_SPAWNABLE_ON = of((XBase[])new XMaterial[] { XMaterial.GRAVEL, XMaterial.STONE, XMaterial.PACKED_ICE }).inheritFrom(XTag.SNOW).build();
        GUARDED_BY_PIGLINS = of((XBase[])new XMaterial[] { XMaterial.GOLD_BLOCK, XMaterial.ENDER_CHEST, XMaterial.RAW_GOLD_BLOCK, XMaterial.GILDED_BLACKSTONE, XMaterial.CHEST, XMaterial.BARREL, XMaterial.TRAPPED_CHEST }).inheritFrom(XTag.SHULKER_BOXES, XTag.GOLD_ORES).build();
        ITEMS_MUSIC_DISCS = of((XBase[])new XMaterial[] { XMaterial.MUSIC_DISC_OTHERSIDE, XMaterial.MUSIC_DISC_PIGSTEP }).inheritFrom(XTag.ITEMS_CREEPER_DROP_MUSIC_DISCS).build();
        ITEMS_PIGLIN_LOVED = of((XBase[])new XMaterial[] { XMaterial.GOLD_BLOCK, XMaterial.RAW_GOLD, XMaterial.GLISTERING_MELON_SLICE, XMaterial.GOLDEN_HORSE_ARMOR, XMaterial.LIGHT_WEIGHTED_PRESSURE_PLATE, XMaterial.GOLDEN_SWORD, XMaterial.GOLDEN_AXE, XMaterial.BELL, XMaterial.ENCHANTED_GOLDEN_APPLE, XMaterial.RAW_GOLD_BLOCK, XMaterial.GILDED_BLACKSTONE, XMaterial.CLOCK, XMaterial.GOLDEN_CARROT, XMaterial.GOLDEN_APPLE, XMaterial.GOLDEN_SHOVEL, XMaterial.GOLDEN_PICKAXE, XMaterial.GOLDEN_HOE, XMaterial.GOLD_INGOT }).inheritFrom(XTag.GOLD_ORES, XTag.GOLDEN_ARMOR_PIECES).build();
        SIGNS = simple((XTag<XBase>[])new XTag[] { XTag.WALL_SIGNS, XTag.STANDING_SIGNS });
        PRESSURE_PLATES = of((XBase[])new XMaterial[] { XMaterial.LIGHT_WEIGHTED_PRESSURE_PLATE, XMaterial.HEAVY_WEIGHTED_PRESSURE_PLATE }).inheritFrom(XTag.STONE_PRESSURE_PLATES, XTag.WOODEN_PRESSURE_PLATES).build();
        DRAGON_IMMUNE = of((XBase[])new XMaterial[] { XMaterial.IRON_BARS, XMaterial.OBSIDIAN, XMaterial.RESPAWN_ANCHOR, XMaterial.END_STONE, XMaterial.CRYING_OBSIDIAN }).inheritFrom(XTag.WITHER_IMMUNE).build();
        WALL_POST_OVERRIDE = of((XBase[])new XMaterial[] { XMaterial.TORCH, XMaterial.TRIPWIRE, XMaterial.REDSTONE_TORCH, XMaterial.SOUL_TORCH }).inheritFrom(XTag.SIGNS, XTag.BANNERS, XTag.PRESSURE_PLATES).build();
        UNDERWATER_BONEMEALS = of((XBase[])new XMaterial[] { XMaterial.SEAGRASS }).inheritFrom(XTag.CORALS, XTag.ALIVE_CORAL_WALL_FANS).build();
        UNSTABLE_BOTTOM_CENTER = simple((XTag<XBase>[])new XTag[] { XTag.FENCE_GATES });
        PREVENT_MOB_SPAWNING_INSIDE = simple((XTag<XBase>[])new XTag[] { XTag.RAILS });
        OCCLUDES_VIBRATION_SIGNALS = simple((XTag<XBase>[])new XTag[] { XTag.WOOL });
        LOGS_THAT_BURN = simple((XTag<XBase>[])new XTag[] { XTag.ACACIA_LOGS, XTag.OAK_LOGS, XTag.DARK_OAK_LOGS, XTag.PALE_OAK_LOGS, XTag.SPRUCE_LOGS, XTag.JUNGLE_LOGS, XTag.BIRCH_LOGS, XTag.MANGROVE_LOGS, XTag.CHERRY_LOGS });
        LOGS = simple((XTag<XBase>[])new XTag[] { XTag.LOGS_THAT_BURN, XTag.CRIMSON_STEMS, XTag.WARPED_STEMS });
        ITEMS_FURNACE_MATERIALS = of((XBase[])new XMaterial[] { XMaterial.COAL, XMaterial.CHARCOAL, XMaterial.COAL_BLOCK }).inheritFrom(XTag.LOGS, XTag.PLANKS).build();
        PARROTS_SPAWNABLE_ON = of((XBase[])new XMaterial[] { XMaterial.GRASS_BLOCK }).inheritFrom(XTag.AIR, XTag.LEAVES, XTag.LOGS).build();
        LUSH_GROUND_REPLACEABLE = of((XBase[])new XMaterial[] { XMaterial.GRAVEL, XMaterial.SAND, XMaterial.CLAY }).inheritFrom(XTag.CAVE_VINES, XTag.DIRT, XTag.BASE_STONE_OVERWORLD).build();
        TRAPDOORS = of((XBase[])new XMaterial[] { XMaterial.IRON_TRAPDOOR }).inheritFrom(XTag.WOODEN_TRAPDOORS).build();
        MUSHROOM_GROW_BLOCK = of((XBase[])new XMaterial[] { XMaterial.PODZOL, XMaterial.MYCELIUM }).inheritFrom(XTag.NYLIUM).build();
        MOSS_REPLACEABLE = simple((XTag<XBase>[])new XTag[] { XTag.CAVE_VINES, XTag.DIRT, XTag.BASE_STONE_OVERWORLD });
        ARMOR_ENCHANTS = simple((XBase[])new XEnchantment[] { XEnchantment.BLAST_PROTECTION, XEnchantment.BINDING_CURSE, XEnchantment.VANISHING_CURSE, XEnchantment.FIRE_PROTECTION, XEnchantment.MENDING, XEnchantment.PROJECTILE_PROTECTION, XEnchantment.PROTECTION, XEnchantment.THORNS, XEnchantment.UNBREAKING });
        HELEMT_ENCHANTS = of((XBase[])new XEnchantment[] { XEnchantment.AQUA_AFFINITY, XEnchantment.RESPIRATION }).inheritFrom(XTag.ARMOR_ENCHANTS).build();
        CHESTPLATE_ENCHANTS = simple((XTag<XBase>[])new XTag[] { XTag.ARMOR_ENCHANTS });
        LEGGINGS_ENCHANTS = simple((XTag<XBase>[])new XTag[] { XTag.ARMOR_ENCHANTS });
        BOOTS_ENCHANTS = of((XBase[])new XEnchantment[] { XEnchantment.DEPTH_STRIDER, XEnchantment.FEATHER_FALLING, XEnchantment.FROST_WALKER }).inheritFrom(XTag.ARMOR_ENCHANTS).build();
        ELYTRA_ENCHANTS = simple((XBase[])new XEnchantment[] { XEnchantment.BINDING_CURSE, XEnchantment.VANISHING_CURSE, XEnchantment.MENDING, XEnchantment.UNBREAKING });
        SWORD_ENCHANTS = simple((XBase[])new XEnchantment[] { XEnchantment.BANE_OF_ARTHROPODS, XEnchantment.VANISHING_CURSE, XEnchantment.FIRE_ASPECT, XEnchantment.KNOCKBACK, XEnchantment.LOOTING, XEnchantment.MENDING, XEnchantment.SHARPNESS, XEnchantment.SMITE, XEnchantment.SWEEPING_EDGE, XEnchantment.UNBREAKING });
        AXE_ENCHANTS = simple((XBase[])new XEnchantment[] { XEnchantment.BANE_OF_ARTHROPODS, XEnchantment.VANISHING_CURSE, XEnchantment.EFFICIENCY, XEnchantment.FORTUNE, XEnchantment.MENDING, XEnchantment.SHARPNESS, XEnchantment.SILK_TOUCH, XEnchantment.SMITE, XEnchantment.UNBREAKING });
        HOE_ENCHANTS = simple((XBase[])new XEnchantment[] { XEnchantment.VANISHING_CURSE, XEnchantment.EFFICIENCY, XEnchantment.FORTUNE, XEnchantment.MENDING, XEnchantment.SILK_TOUCH, XEnchantment.UNBREAKING });
        PICKAXE_ENCHANTS = simple((XBase[])new XEnchantment[] { XEnchantment.VANISHING_CURSE, XEnchantment.EFFICIENCY, XEnchantment.FORTUNE, XEnchantment.MENDING, XEnchantment.SILK_TOUCH, XEnchantment.UNBREAKING });
        SHOVEL_ENCHANTS = simple((XBase[])new XEnchantment[] { XEnchantment.VANISHING_CURSE, XEnchantment.EFFICIENCY, XEnchantment.FORTUNE, XEnchantment.MENDING, XEnchantment.SILK_TOUCH, XEnchantment.UNBREAKING });
        SHEARS_ENCHANTS = of((XBase[])new XEnchantment[] { XEnchantment.VANISHING_CURSE, XEnchantment.EFFICIENCY, XEnchantment.MENDING, XEnchantment.UNBREAKING }).build();
        BOW_ENCHANTS = simple((XBase[])new XEnchantment[] { XEnchantment.VANISHING_CURSE, XEnchantment.FLAME, XEnchantment.INFINITY, XEnchantment.MENDING, XEnchantment.PUNCH, XEnchantment.UNBREAKING });
        CROSSBOW_ENCHANTS = simple((XBase[])new XEnchantment[] { XEnchantment.VANISHING_CURSE, XEnchantment.MENDING, XEnchantment.MULTISHOT, XEnchantment.PIERCING, XEnchantment.QUICK_CHARGE, XEnchantment.UNBREAKING });
        MINEABLE_AXE = of((XBase[])new XMaterial[] { XMaterial.COMPOSTER, XMaterial.COCOA, XMaterial.RED_MUSHROOM_BLOCK, XMaterial.CRAFTING_TABLE, XMaterial.TALL_GRASS, XMaterial.BIG_DRIPLEAF_STEM, XMaterial.RED_MUSHROOM, XMaterial.JUKEBOX, XMaterial.WARPED_FUNGUS, XMaterial.DEAD_BUSH, XMaterial.NOTE_BLOCK, XMaterial.CRIMSON_FUNGUS, XMaterial.MUSHROOM_STEM, XMaterial.CHORUS_PLANT, XMaterial.BEE_NEST, XMaterial.BROWN_MUSHROOM_BLOCK, XMaterial.JACK_O_LANTERN, XMaterial.FERN, XMaterial.NETHER_WART, XMaterial.CARTOGRAPHY_TABLE, XMaterial.CHEST, XMaterial.SWEET_BERRY_BUSH, XMaterial.BROWN_MUSHROOM, XMaterial.CARVED_PUMPKIN, XMaterial.SMITHING_TABLE, XMaterial.GLOW_LICHEN, XMaterial.SMALL_DRIPLEAF, XMaterial.LOOM, XMaterial.BEEHIVE, XMaterial.SHORT_GRASS, XMaterial.HANGING_ROOTS, XMaterial.CHORUS_FLOWER, XMaterial.ATTACHED_PUMPKIN_STEM, XMaterial.BIG_DRIPLEAF, XMaterial.DAYLIGHT_DETECTOR, XMaterial.SPORE_BLOSSOM, XMaterial.LILY_PAD, XMaterial.TRAPPED_CHEST, XMaterial.BARREL, XMaterial.LARGE_FERN, XMaterial.LECTERN, XMaterial.SUGAR_CANE, XMaterial.MELON, XMaterial.ATTACHED_MELON_STEM, XMaterial.PUMPKIN, XMaterial.BAMBOO, XMaterial.FLETCHING_TABLE, XMaterial.BOOKSHELF }).inheritFrom(XTag.BANNERS, XTag.SIGNS, XTag.CAVE_VINES, XTag.CROPS, XTag.LOGS, XTag.WOODEN_STAIRS, XTag.WOODEN_SLABS, XTag.WOODEN_PRESSURE_PLATES, XTag.WOODEN_FENCES, XTag.WOODEN_FENCE_GATES, XTag.WOODEN_TRAPDOORS, XTag.WOODEN_DOORS, XTag.WOODEN_BUTTONS, XTag.PLANKS, XTag.SAPLINGS, XTag.CLIMBABLE, XTag.CAMPFIRES).build();
        INVENTORY_NOT_DISPLAYABLE = of((XBase[])new XMaterial[] { XMaterial.FROSTED_ICE, XMaterial.MOVING_PISTON, XMaterial.PISTON_HEAD, XMaterial.BUBBLE_COLUMN, XMaterial.POWDER_SNOW, XMaterial.REDSTONE_WIRE, XMaterial.TRIPWIRE, XMaterial.BIG_DRIPLEAF_STEM, XMaterial.SWEET_BERRY_BUSH, XMaterial.TORCHFLOWER_CROP, XMaterial.TWISTING_VINES_PLANT, XMaterial.WEEPING_VINES_PLANT, XMaterial.BAMBOO_SAPLING, XMaterial.CARROT, XMaterial.CARROTS, XMaterial.POTATO, XMaterial.POTATOES, XMaterial.BAMBOO_SAPLING, XMaterial.BAMBOO, XMaterial.CHORUS_PLANT, XMaterial.KELP_PLANT, XMaterial.COCOA, XMaterial.TALL_SEAGRASS, XMaterial.MELON_STEM, XMaterial.PUMPKIN_STEM, XMaterial.ATTACHED_MELON_STEM, XMaterial.ATTACHED_PUMPKIN_STEM }).inheritFrom(XTag.AIR, XTag.CAVE_VINES, XTag.FILLED_CAULDRONS, XTag.FIRE, XTag.FLUID, XTag.PORTALS, XTag.WALL_SIGNS, XTag.WALL_HANGING_SIGNS, XTag.WALL_TORCHES, XTag.ALIVE_CORAL_WALL_FANS, XTag.DEAD_CORAL_WALL_FANS, XTag.WALL_HEADS, XTag.CANDLE_CAKES, XTag.WALL_BANNERS, XTag.FLOWER_POTS.without(XMaterial.FLOWER_POT)).build();
        XTag.MATERIAL_TO_ENTITY.put((Object)XMaterial.MINECART, (Object)XEntityType.MINECART);
        XTag.MATERIAL_TO_ENTITY.put((Object)XMaterial.CHEST_MINECART, (Object)XEntityType.CHEST_MINECART);
        XTag.MATERIAL_TO_ENTITY.put((Object)XMaterial.COMMAND_BLOCK_MINECART, (Object)XEntityType.COMMAND_BLOCK_MINECART);
        XTag.MATERIAL_TO_ENTITY.put((Object)XMaterial.TNT_MINECART, (Object)XEntityType.TNT_MINECART);
        XTag.MATERIAL_TO_ENTITY.put((Object)XMaterial.FURNACE_MINECART, (Object)XEntityType.FURNACE_MINECART);
        XTag.MATERIAL_TO_ENTITY.put((Object)XMaterial.HOPPER_MINECART, (Object)XEntityType.HOPPER_MINECART);
        XTag.MATERIAL_TO_ENTITY.put((Object)XMaterial.END_CRYSTAL, (Object)XEntityType.END_CRYSTAL);
        XTag.MATERIAL_TO_ENTITY.put((Object)XMaterial.PAINTING, (Object)XEntityType.PAINTING);
        XTag.MATERIAL_TO_ENTITY.put((Object)XMaterial.ITEM_FRAME, (Object)XEntityType.ITEM_FRAME);
        XTag.MATERIAL_TO_ENTITY.put((Object)XMaterial.GLOW_ITEM_FRAME, (Object)XEntityType.GLOW_ITEM_FRAME);
        XTag.MATERIAL_TO_ENTITY.put((Object)XMaterial.WIND_CHARGE, (Object)XEntityType.WIND_CHARGE);
        XTag.MATERIAL_TO_ENTITY.put((Object)XMaterial.EGG, (Object)XEntityType.EGG);
        XTag.MATERIAL_TO_ENTITY.put((Object)XMaterial.SNOWBALL, (Object)XEntityType.SNOWBALL);
        XTag.MATERIAL_TO_ENTITY.put((Object)XMaterial.ENDER_PEARL, (Object)XEntityType.ENDER_PEARL);
        XTag.MATERIAL_TO_ENTITY.put((Object)XMaterial.ENDER_EYE, (Object)XEntityType.EYE_OF_ENDER);
        for (final XMaterial boat : XTag.ITEMS_BOATS.values) {
            final XEntityType entityType = (XEntityType)XEntityType.of(boat.name()).orElseThrow(() -> new IllegalStateException("Cannot find entity type for boat: " + (Object)boat));
            XTag.MATERIAL_TO_ENTITY.put((Object)boat, (Object)entityType);
        }
        for (final XMaterial spawnEgg : XTag.SPAWN_EGGS.values) {
            final String name = spawnEgg.name().substring(0, spawnEgg.name().length() - "_SPAWN_EGG".length());
            final XEntityType entityType2 = (XEntityType)XEntityType.of(name).orElseThrow(() -> new IllegalStateException("Cannot find entity type for spawn egg: " + (Object)spawnEgg + " named " + name));
            XTag.MATERIAL_TO_ENTITY.put((Object)spawnEgg, (Object)entityType2);
        }
        TAGS = (Map)new HashMap(30);
        for (final Field field : XTag.class.getDeclaredFields()) {
            try {
                if (field.getType() == XTag.class) {
                    XTag.TAGS.put((Object)field.getName(), (Object)field.get((Object)null));
                }
            }
            catch (final IllegalAccessException ex) {
                new IllegalStateException("Failed to get XTag " + (Object)field, (Throwable)ex).printStackTrace();
            }
        }
    }
    
    public abstract static class Matcher<T>
    {
        public abstract boolean matches(final T p0);
        
        public static final class Error extends RuntimeException
        {
            public final String matcher;
            
            public Error(final String matcher, final String message) {
                super(message);
                this.matcher = matcher;
            }
            
            public Error(final String matcher, final String message, final Throwable cause) {
                super(message, cause);
                this.matcher = matcher;
            }
        }
        
        public static final class TextMatcher<T> extends Matcher<T>
        {
            public final String text;
            public final boolean contains;
            
            public TextMatcher(final String text, final boolean contains) {
                this.text = text;
                this.contains = contains;
            }
            
            @Override
            public boolean matches(final T object) {
                final String name = (object instanceof Enum) ? ((Enum)object).name() : object.toString();
                return this.contains ? name.contains((CharSequence)this.text) : name.equals((Object)this.text);
            }
        }
        
        public static final class RegexMatcher<T> extends Matcher<T>
        {
            public final Pattern regex;
            
            public RegexMatcher(final Pattern regex) {
                this.regex = regex;
            }
            
            @Override
            public boolean matches(final T object) {
                final String name = (object instanceof Enum) ? ((Enum)object).name() : object.toString();
                return this.regex.matcher((CharSequence)name).matches();
            }
        }
        
        public static final class XTagMatcher<T extends XBase<?, ?>> extends Matcher<T>
        {
            public final XTag<T> matcher;
            
            public XTagMatcher(final XTag<T> matcher) {
                this.matcher = matcher;
            }
            
            @Override
            public boolean matches(final T object) {
                return this.matcher.isTagged(object);
            }
        }
    }
    
    private static final class TagBuilder<T extends XBase<?, ?>>
    {
        private final Set<T> values;
        
        private TagBuilder(final Collection<T> values) {
            if (values.isEmpty()) {
                this.values = (Set<T>)Collections.newSetFromMap((Map)new IdentityHashMap());
            }
            else if (values.iterator().next() instanceof Enum) {
                final Iterator<T> iter = (Iterator<T>)values.iterator();
                final T first = (T)iter.next();
                this.values = (Set<T>)EnumSet.of((Enum)first);
                while (iter.hasNext()) {
                    this.values.add((Object)iter.next());
                }
            }
            else {
                (this.values = (Set<T>)Collections.newSetFromMap((Map)new IdentityHashMap(values.size()))).addAll((Collection)values);
            }
        }
        
        @SafeVarargs
        private static <T extends XBase<?, ?>> XTag<T> simple(final T... values) {
            return of(values).build();
        }
        
        @SafeVarargs
        private static <T extends XBase<?, ?>> XTag<T> simple(final XTag<T>... values) {
            return new TagBuilder<T>((java.util.Collection<T>)Collections.singletonList((Object)((XTag<XBase>)values[0]).values.iterator().next())).inheritFrom(values).build();
        }
        
        @SafeVarargs
        private static <T extends XBase<?, ?>> TagBuilder<T> of(final T... values) {
            return new TagBuilder<T>((java.util.Collection<T>)Arrays.asList((Object[])values));
        }
        
        @SafeVarargs
        private final TagBuilder<T> inheritFrom(@NotNull final XTag<T>... values) {
            for (final XTag<T> value : values) {
                this.values.addAll((Collection)((XTag<XBase>)value).values);
            }
            return this;
        }
        
        private XTag<T> build() {
            return new XTag<T>(Collections.unmodifiableSet((Set)this.values), null);
        }
    }
}
