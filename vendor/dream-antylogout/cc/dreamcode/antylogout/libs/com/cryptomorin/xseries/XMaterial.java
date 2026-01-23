package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bukkit.Bukkit;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;
import com.google.common.cache.CacheBuilder;
import java.util.HashMap;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.Arrays;
import org.jetbrains.annotations.ApiStatus;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SpawnEggMeta;
import org.bukkit.inventory.ItemStack;
import java.util.Objects;
import java.util.Optional;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import java.util.Set;
import com.google.common.cache.Cache;
import java.util.Map;
import org.bukkit.Material;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.base.XBase;

public enum XMaterial implements XBase<XMaterial, Material>
{
    ACACIA_BOAT(new String[] { "BOAT_ACACIA" }), 
    ACACIA_BUTTON(new String[0]), 
    ACACIA_CHEST_BOAT(new String[0]), 
    ACACIA_DOOR(new String[] { "ACACIA_DOOR", "ACACIA_DOOR_ITEM" }), 
    ACACIA_FENCE(new String[0]), 
    ACACIA_FENCE_GATE(new String[0]), 
    ACACIA_HANGING_SIGN(new String[0]), 
    ACACIA_LEAVES(0, new String[] { "LEAVES_2" }), 
    ACACIA_LOG(0, new String[] { "LOG_2" }), 
    ACACIA_PLANKS(4, new String[] { "WOOD" }), 
    ACACIA_PRESSURE_PLATE(new String[0]), 
    ACACIA_SAPLING(4, new String[] { "SAPLING" }), 
    ACACIA_SIGN(new String[0]), 
    ACACIA_SLAB(4, new String[] { "WOOD_DOUBLE_STEP", "WOOD_STEP", "WOODEN_SLAB" }), 
    ACACIA_STAIRS(new String[0]), 
    ACACIA_TRAPDOOR(new String[0]), 
    ACACIA_WALL_HANGING_SIGN(new String[0]), 
    ACACIA_WALL_SIGN(new String[] { "WALL_SIGN" }), 
    ACACIA_WOOD(0, new String[] { "LOG_2" }), 
    ACTIVATOR_RAIL(new String[0]), 
    AIR(new String[0]), 
    ALLAY_SPAWN_EGG(new String[0]), 
    ALLIUM(2, new String[] { "RED_ROSE" }), 
    AMETHYST_BLOCK(new String[0]), 
    AMETHYST_CLUSTER(new String[0]), 
    AMETHYST_SHARD(new String[0]), 
    ANCIENT_DEBRIS(new String[0]), 
    ANDESITE(5, new String[] { "STONE" }), 
    ANDESITE_SLAB(new String[0]), 
    ANDESITE_STAIRS(new String[0]), 
    ANDESITE_WALL(new String[0]), 
    ANGLER_POTTERY_SHERD(new String[0]), 
    ANVIL(new String[0]), 
    APPLE(new String[0]), 
    ARCHER_POTTERY_SHERD(new String[0]), 
    ARMADILLO_SCUTE(new String[0]), 
    ARMADILLO_SPAWN_EGG(new String[0]), 
    ARMOR_STAND(new String[0]), 
    ARMS_UP_POTTERY_SHERD(new String[0]), 
    ARROW(new String[0]), 
    ATTACHED_MELON_STEM(7, new String[] { "MELON_STEM" }), 
    ATTACHED_PUMPKIN_STEM(7, new String[] { "PUMPKIN_STEM" }), 
    AXOLOTL_BUCKET(new String[0]), 
    AXOLOTL_SPAWN_EGG(new String[0]), 
    AZALEA(new String[0]), 
    AZALEA_LEAVES(new String[0]), 
    AZURE_BLUET(3, new String[] { "RED_ROSE" }), 
    BAKED_POTATO(new String[0]), 
    BAMBOO(new String[0]), 
    BAMBOO_BLOCK(new String[0]), 
    BAMBOO_BUTTON(new String[0]), 
    BAMBOO_CHEST_RAFT(new String[0]), 
    BAMBOO_DOOR(new String[0]), 
    BAMBOO_FENCE(new String[0]), 
    BAMBOO_FENCE_GATE(new String[0]), 
    BAMBOO_HANGING_SIGN(new String[0]), 
    BAMBOO_MOSAIC(new String[0]), 
    BAMBOO_MOSAIC_SLAB(new String[0]), 
    BAMBOO_MOSAIC_STAIRS(new String[0]), 
    BAMBOO_PLANKS(new String[0]), 
    BAMBOO_PRESSURE_PLATE(new String[0]), 
    BAMBOO_RAFT(new String[0]), 
    BAMBOO_SAPLING(new String[0]), 
    BAMBOO_SIGN(new String[0]), 
    BAMBOO_SLAB(new String[0]), 
    BAMBOO_STAIRS(new String[0]), 
    BAMBOO_TRAPDOOR(new String[0]), 
    BAMBOO_WALL_HANGING_SIGN(new String[0]), 
    BAMBOO_WALL_SIGN(new String[0]), 
    BARREL(new String[0]), 
    BARRIER(new String[0]), 
    BASALT(new String[0]), 
    BAT_SPAWN_EGG(65, new String[] { "MONSTER_EGG" }), 
    BEACON(new String[0]), 
    BEDROCK(new String[0]), 
    BEEF(new String[] { "RAW_BEEF" }), 
    BEEHIVE(new String[0]), 
    BEETROOT(new String[] { "BEETROOT_BLOCK" }), 
    BEETROOTS(new String[] { "BEETROOT" }), 
    BEETROOT_SEEDS(new String[0]), 
    BEETROOT_SOUP(new String[0]), 
    BEE_NEST(new String[0]), 
    BEE_SPAWN_EGG(new String[0]), 
    BELL(new String[0]), 
    BIG_DRIPLEAF(new String[0]), 
    BIG_DRIPLEAF_STEM(new String[0]), 
    BIRCH_BOAT(new String[] { "BOAT_BIRCH" }), 
    BIRCH_BUTTON(new String[0]), 
    BIRCH_CHEST_BOAT(new String[0]), 
    BIRCH_DOOR(new String[] { "BIRCH_DOOR", "BIRCH_DOOR_ITEM" }), 
    BIRCH_FENCE(new String[0]), 
    BIRCH_FENCE_GATE(new String[0]), 
    BIRCH_HANGING_SIGN(new String[0]), 
    BIRCH_LEAVES(2, new String[] { "LEAVES" }), 
    BIRCH_LOG(2, new String[] { "LOG" }), 
    BIRCH_PLANKS(2, new String[] { "WOOD" }), 
    BIRCH_PRESSURE_PLATE(new String[0]), 
    BIRCH_SAPLING(2, new String[] { "SAPLING" }), 
    BIRCH_SIGN(new String[0]), 
    BIRCH_SLAB(2, new String[] { "WOOD_DOUBLE_STEP", "WOOD_STEP", "WOODEN_SLAB" }), 
    BIRCH_STAIRS(new String[] { "BIRCH_WOOD_STAIRS" }), 
    BIRCH_TRAPDOOR(new String[0]), 
    BIRCH_WALL_HANGING_SIGN(new String[0]), 
    BIRCH_WALL_SIGN(new String[] { "WALL_SIGN" }), 
    BIRCH_WOOD(2, new String[] { "LOG" }), 
    BLACKSTONE(new String[0]), 
    BLACKSTONE_SLAB(new String[0]), 
    BLACKSTONE_STAIRS(new String[0]), 
    BLACKSTONE_WALL(new String[0]), 
    BLACK_BANNER(new String[] { "STANDING_BANNER", "BANNER" }), 
    BLACK_BED(supports(12) ? 15 : 0, new String[] { "BED_BLOCK", "BED" }), 
    BLACK_BUNDLE(new String[0]), 
    BLACK_CANDLE(new String[0]), 
    BLACK_CANDLE_CAKE(new String[0]), 
    BLACK_CARPET(15, new String[] { "CARPET" }), 
    BLACK_CONCRETE(15, new String[] { "CONCRETE" }), 
    BLACK_CONCRETE_POWDER(15, new String[] { "CONCRETE_POWDER" }), 
    BLACK_DYE(0, new String[] { "INK_SACK", "INK_SAC" }), 
    BLACK_GLAZED_TERRACOTTA(new String[0]), 
    BLACK_HARNESS(new String[0]), 
    BLACK_SHULKER_BOX(new String[0]), 
    BLACK_STAINED_GLASS(15, new String[] { "STAINED_GLASS" }), 
    BLACK_STAINED_GLASS_PANE(15, new String[] { "STAINED_GLASS_PANE" }), 
    BLACK_TERRACOTTA(15, new String[] { "STAINED_CLAY" }), 
    BLACK_WALL_BANNER(new String[] { "WALL_BANNER" }), 
    BLACK_WOOL(15, new String[] { "WOOL" }), 
    BLADE_POTTERY_SHERD(new String[0]), 
    BLAST_FURNACE(new String[0]), 
    BLAZE_POWDER(new String[0]), 
    BLAZE_ROD(new String[0]), 
    BLAZE_SPAWN_EGG(61, new String[] { "MONSTER_EGG" }), 
    BLUE_BANNER(4, new String[] { "STANDING_BANNER", "BANNER" }), 
    BLUE_BED(supports(12) ? 11 : 0, new String[] { "BED_BLOCK", "BED" }), 
    BLUE_BUNDLE(new String[0]), 
    BLUE_CANDLE(new String[0]), 
    BLUE_CANDLE_CAKE(new String[0]), 
    BLUE_CARPET(11, new String[] { "CARPET" }), 
    BLUE_CONCRETE(11, new String[] { "CONCRETE" }), 
    BLUE_CONCRETE_POWDER(11, new String[] { "CONCRETE_POWDER" }), 
    BLUE_DYE(new String[0]), 
    BLUE_EGG(new String[0]), 
    BLUE_GLAZED_TERRACOTTA(new String[0]), 
    BLUE_HARNESS(new String[0]), 
    BLUE_ICE(new String[0]), 
    BLUE_ORCHID(1, new String[] { "RED_ROSE" }), 
    BLUE_SHULKER_BOX(new String[0]), 
    BLUE_STAINED_GLASS(11, new String[] { "STAINED_GLASS" }), 
    BLUE_STAINED_GLASS_PANE(11, new String[] { "THIN_GLASS", "STAINED_GLASS_PANE" }), 
    BLUE_TERRACOTTA(11, new String[] { "STAINED_CLAY" }), 
    BLUE_WALL_BANNER(4, new String[] { "WALL_BANNER" }), 
    BLUE_WOOL(11, new String[] { "WOOL" }), 
    BOGGED_SPAWN_EGG(new String[0]), 
    BOLT_ARMOR_TRIM_SMITHING_TEMPLATE(new String[0]), 
    BONE(new String[0]), 
    BONE_BLOCK(new String[0]), 
    BONE_MEAL(15, new String[] { "INK_SACK" }), 
    BOOK(new String[0]), 
    BOOKSHELF(new String[0]), 
    BORDURE_INDENTED_BANNER_PATTERN(new String[0]), 
    BOW(new String[0]), 
    BOWL(new String[0]), 
    BRAIN_CORAL(new String[0]), 
    BRAIN_CORAL_BLOCK(new String[0]), 
    BRAIN_CORAL_FAN(new String[0]), 
    BRAIN_CORAL_WALL_FAN(new String[0]), 
    BREAD(new String[0]), 
    BREEZE_ROD(new String[0]), 
    BREEZE_SPAWN_EGG(new String[0]), 
    BREWER_POTTERY_SHERD(new String[0]), 
    BREWING_STAND(new String[] { "BREWING_STAND", "BREWING_STAND_ITEM" }), 
    BRICK(new String[] { "CLAY_BRICK" }), 
    BRICKS(new String[] { "BRICK" }), 
    BRICK_SLAB(4, new String[] { "STEP" }), 
    BRICK_STAIRS(new String[0]), 
    BRICK_WALL(new String[0]), 
    BROWN_BANNER(3, new String[] { "STANDING_BANNER", "BANNER" }), 
    BROWN_BED(supports(12) ? 12 : 0, new String[] { "BED_BLOCK", "BED" }), 
    BROWN_BUNDLE(new String[0]), 
    BROWN_CANDLE(new String[0]), 
    BROWN_CANDLE_CAKE(new String[0]), 
    BROWN_CARPET(12, new String[] { "CARPET" }), 
    BROWN_CONCRETE(12, new String[] { "CONCRETE" }), 
    BROWN_CONCRETE_POWDER(12, new String[] { "CONCRETE_POWDER" }), 
    BROWN_DYE(new String[0]), 
    BROWN_EGG(new String[0]), 
    BROWN_GLAZED_TERRACOTTA(new String[0]), 
    BROWN_HARNESS(new String[0]), 
    BROWN_MUSHROOM(new String[0]), 
    BROWN_MUSHROOM_BLOCK(new String[] { "BROWN_MUSHROOM", "HUGE_MUSHROOM_1" }), 
    BROWN_SHULKER_BOX(new String[0]), 
    BROWN_STAINED_GLASS(12, new String[] { "STAINED_GLASS" }), 
    BROWN_STAINED_GLASS_PANE(12, new String[] { "THIN_GLASS", "STAINED_GLASS_PANE" }), 
    BROWN_TERRACOTTA(12, new String[] { "STAINED_CLAY" }), 
    BROWN_WALL_BANNER(3, new String[] { "WALL_BANNER" }), 
    BROWN_WOOL(12, new String[] { "WOOL" }), 
    BRUSH(new String[0]), 
    BUBBLE_COLUMN(new String[0]), 
    BUBBLE_CORAL(new String[0]), 
    BUBBLE_CORAL_BLOCK(new String[0]), 
    BUBBLE_CORAL_FAN(new String[0]), 
    BUBBLE_CORAL_WALL_FAN(new String[0]), 
    BUCKET(new String[0]), 
    BUDDING_AMETHYST(new String[0]), 
    BUNDLE(new String[0]), 
    BURN_POTTERY_SHERD(new String[0]), 
    BUSH(new String[0]), 
    CACTUS(new String[0]), 
    CACTUS_FLOWER(new String[0]), 
    CAKE(new String[] { "CAKE_BLOCK" }), 
    CALCITE(new String[0]), 
    CALIBRATED_SCULK_SENSOR(new String[0]), 
    CAMEL_SPAWN_EGG(new String[0]), 
    CAMPFIRE(new String[0]), 
    CANDLE(new String[0]), 
    CANDLE_CAKE(new String[0]), 
    CARROT(new String[] { "CARROT_ITEM" }), 
    CARROTS(new String[] { "CARROT" }), 
    CARROT_ON_A_STICK(new String[] { "CARROT_STICK" }), 
    CARTOGRAPHY_TABLE(new String[0]), 
    CARVED_PUMPKIN(new String[0]), 
    CAT_SPAWN_EGG(new String[0]), 
    CAULDRON(new String[] { "CAULDRON", "CAULDRON_ITEM" }), 
    CAVE_AIR(new String[] { "AIR" }), 
    CAVE_SPIDER_SPAWN_EGG(59, new String[] { "MONSTER_EGG" }), 
    CAVE_VINES(new String[0]), 
    CAVE_VINES_PLANT(new String[0]), 
    CHAIN(new String[0]), 
    CHAINMAIL_BOOTS(new String[0]), 
    CHAINMAIL_CHESTPLATE(new String[0]), 
    CHAINMAIL_HELMET(new String[0]), 
    CHAINMAIL_LEGGINGS(new String[0]), 
    CHAIN_COMMAND_BLOCK(new String[] { "COMMAND_CHAIN" }), 
    CHARCOAL(1, new String[] { "COAL" }), 
    CHERRY_BOAT(new String[0]), 
    CHERRY_BUTTON(new String[0]), 
    CHERRY_CHEST_BOAT(new String[0]), 
    CHERRY_DOOR(new String[0]), 
    CHERRY_FENCE(new String[0]), 
    CHERRY_FENCE_GATE(new String[0]), 
    CHERRY_HANGING_SIGN(new String[0]), 
    CHERRY_LEAVES(new String[0]), 
    CHERRY_LOG(new String[0]), 
    CHERRY_PLANKS(new String[0]), 
    CHERRY_PRESSURE_PLATE(new String[0]), 
    CHERRY_SAPLING(new String[0]), 
    CHERRY_SIGN(new String[0]), 
    CHERRY_SLAB(new String[0]), 
    CHERRY_STAIRS(new String[0]), 
    CHERRY_TRAPDOOR(new String[0]), 
    CHERRY_WALL_HANGING_SIGN(new String[0]), 
    CHERRY_WALL_SIGN(new String[0]), 
    CHERRY_WOOD(new String[0]), 
    CHEST(new String[] { "LOCKED_CHEST" }), 
    CHEST_MINECART(new String[] { "STORAGE_MINECART" }), 
    CHICKEN(new String[] { "RAW_CHICKEN" }), 
    CHICKEN_SPAWN_EGG(93, new String[] { "MONSTER_EGG" }), 
    CHIPPED_ANVIL(1, new String[] { "ANVIL" }), 
    CHISELED_BOOKSHELF(new String[0]), 
    CHISELED_COPPER(new String[0]), 
    CHISELED_DEEPSLATE(new String[0]), 
    CHISELED_NETHER_BRICKS(1, new String[] { "NETHER_BRICKS" }), 
    CHISELED_POLISHED_BLACKSTONE(new String[] { "POLISHED_BLACKSTONE" }), 
    CHISELED_QUARTZ_BLOCK(1, new String[] { "QUARTZ_BLOCK" }), 
    CHISELED_RED_SANDSTONE(1, new String[] { "RED_SANDSTONE" }), 
    CHISELED_RESIN_BRICKS(new String[0]), 
    CHISELED_SANDSTONE(1, new String[] { "SANDSTONE" }), 
    CHISELED_STONE_BRICKS(3, new String[] { "SMOOTH_BRICK" }), 
    CHISELED_TUFF(new String[0]), 
    CHISELED_TUFF_BRICKS(new String[0]), 
    CHORUS_FLOWER(new String[0]), 
    CHORUS_FRUIT(new String[0]), 
    CHORUS_PLANT(new String[0]), 
    CLAY(new String[0]), 
    CLAY_BALL(new String[0]), 
    CLOCK(new String[] { "WATCH" }), 
    CLOSED_EYEBLOSSOM(new String[0]), 
    COAL(new String[0]), 
    COAL_BLOCK(new String[0]), 
    COAL_ORE(new String[0]), 
    COARSE_DIRT(1, new String[] { "DIRT" }), 
    COAST_ARMOR_TRIM_SMITHING_TEMPLATE(new String[0]), 
    COBBLED_DEEPSLATE(new String[0]), 
    COBBLED_DEEPSLATE_SLAB(new String[0]), 
    COBBLED_DEEPSLATE_STAIRS(new String[0]), 
    COBBLED_DEEPSLATE_WALL(new String[0]), 
    COBBLESTONE(new String[0]), 
    COBBLESTONE_SLAB(3, new String[] { "STEP" }), 
    COBBLESTONE_STAIRS(new String[0]), 
    COBBLESTONE_WALL(new String[] { "COBBLE_WALL" }), 
    COBWEB(new String[] { "WEB" }), 
    COCOA(new String[0]), 
    COCOA_BEANS(3, new String[] { "INK_SACK" }), 
    COD(new String[] { "RAW_FISH" }), 
    COD_BUCKET(new String[0]), 
    COD_SPAWN_EGG(new String[0]), 
    COMMAND_BLOCK(new String[] { "COMMAND" }), 
    COMMAND_BLOCK_MINECART(new String[] { "COMMAND_MINECART" }), 
    COMPARATOR(new String[] { "REDSTONE_COMPARATOR_OFF", "REDSTONE_COMPARATOR_ON", "REDSTONE_COMPARATOR" }), 
    COMPASS(new String[0]), 
    COMPOSTER(new String[0]), 
    CONDUIT(new String[0]), 
    COOKED_BEEF(new String[0]), 
    COOKED_CHICKEN(new String[0]), 
    COOKED_COD(new String[] { "COOKED_FISH" }), 
    COOKED_MUTTON(new String[0]), 
    COOKED_PORKCHOP(new String[] { "GRILLED_PORK" }), 
    COOKED_RABBIT(new String[0]), 
    COOKED_SALMON(1, new String[] { "COOKED_FISH" }), 
    COOKIE(new String[0]), 
    COPPER_BLOCK(new String[0]), 
    COPPER_BULB(new String[0]), 
    COPPER_DOOR(new String[0]), 
    COPPER_GRATE(new String[0]), 
    COPPER_INGOT(new String[0]), 
    COPPER_ORE(new String[0]), 
    COPPER_TRAPDOOR(new String[0]), 
    CORNFLOWER(new String[0]), 
    COW_SPAWN_EGG(92, new String[] { "MONSTER_EGG" }), 
    CRACKED_DEEPSLATE_BRICKS(new String[0]), 
    CRACKED_DEEPSLATE_TILES(new String[0]), 
    CRACKED_NETHER_BRICKS(2, new String[] { "NETHER_BRICKS" }), 
    CRACKED_POLISHED_BLACKSTONE_BRICKS(new String[] { "POLISHED_BLACKSTONE_BRICKS" }), 
    CRACKED_STONE_BRICKS(2, new String[] { "SMOOTH_BRICK" }), 
    CRAFTER(new String[0]), 
    CRAFTING_TABLE(new String[] { "WORKBENCH" }), 
    CREAKING_HEART(new String[0]), 
    CREAKING_SPAWN_EGG(new String[0]), 
    CREEPER_BANNER_PATTERN(new String[0]), 
    CREEPER_HEAD(4, new String[] { "SKULL", "SKULL_ITEM" }), 
    CREEPER_SPAWN_EGG(50, new String[] { "MONSTER_EGG" }), 
    CREEPER_WALL_HEAD(4, new String[] { "SKULL", "SKULL_ITEM" }), 
    CRIMSON_BUTTON(new String[0]), 
    CRIMSON_DOOR(new String[0]), 
    CRIMSON_FENCE(new String[0]), 
    CRIMSON_FENCE_GATE(new String[0]), 
    CRIMSON_FUNGUS(new String[0]), 
    CRIMSON_HANGING_SIGN(new String[0]), 
    CRIMSON_HYPHAE(new String[0]), 
    CRIMSON_NYLIUM(new String[0]), 
    CRIMSON_PLANKS(new String[0]), 
    CRIMSON_PRESSURE_PLATE(new String[0]), 
    CRIMSON_ROOTS(new String[0]), 
    CRIMSON_SIGN(new String[] { "SIGN_POST" }), 
    CRIMSON_SLAB(new String[0]), 
    CRIMSON_STAIRS(new String[0]), 
    CRIMSON_STEM(new String[0]), 
    CRIMSON_TRAPDOOR(new String[0]), 
    CRIMSON_WALL_HANGING_SIGN(new String[0]), 
    CRIMSON_WALL_SIGN(new String[] { "WALL_SIGN" }), 
    CROSSBOW(new String[0]), 
    CRYING_OBSIDIAN(new String[0]), 
    CUT_COPPER(new String[0]), 
    CUT_COPPER_SLAB(new String[0]), 
    CUT_COPPER_STAIRS(new String[0]), 
    CUT_RED_SANDSTONE(new String[0]), 
    CUT_RED_SANDSTONE_SLAB(new String[0]), 
    CUT_SANDSTONE(new String[0]), 
    CUT_SANDSTONE_SLAB(new String[0]), 
    CYAN_BANNER(6, new String[] { "STANDING_BANNER", "BANNER" }), 
    CYAN_BED(supports(12) ? 9 : 0, new String[] { "BED_BLOCK", "BED" }), 
    CYAN_BUNDLE(new String[0]), 
    CYAN_CANDLE(new String[0]), 
    CYAN_CANDLE_CAKE(new String[0]), 
    CYAN_CARPET(9, new String[] { "CARPET" }), 
    CYAN_CONCRETE(9, new String[] { "CONCRETE" }), 
    CYAN_CONCRETE_POWDER(9, new String[] { "CONCRETE_POWDER" }), 
    CYAN_DYE(6, new String[] { "INK_SACK" }), 
    CYAN_GLAZED_TERRACOTTA(new String[0]), 
    CYAN_HARNESS(new String[0]), 
    CYAN_SHULKER_BOX(new String[0]), 
    CYAN_STAINED_GLASS(9, new String[] { "STAINED_GLASS" }), 
    CYAN_STAINED_GLASS_PANE(9, new String[] { "STAINED_GLASS_PANE" }), 
    CYAN_TERRACOTTA(9, new String[] { "STAINED_CLAY" }), 
    CYAN_WALL_BANNER(6, new String[] { "WALL_BANNER" }), 
    CYAN_WOOL(9, new String[] { "WOOL" }), 
    DAMAGED_ANVIL(2, new String[] { "ANVIL" }), 
    DANDELION(new String[] { "YELLOW_FLOWER" }), 
    DANGER_POTTERY_SHERD(new String[0]), 
    DARK_OAK_BOAT(new String[] { "BOAT_DARK_OAK" }), 
    DARK_OAK_BUTTON(new String[0]), 
    DARK_OAK_CHEST_BOAT(new String[0]), 
    DARK_OAK_DOOR(new String[] { "DARK_OAK_DOOR", "DARK_OAK_DOOR_ITEM" }), 
    DARK_OAK_FENCE(new String[0]), 
    DARK_OAK_FENCE_GATE(new String[0]), 
    DARK_OAK_HANGING_SIGN(new String[0]), 
    DARK_OAK_LEAVES(1, new String[] { "LEAVES_2" }), 
    DARK_OAK_LOG(1, new String[] { "LOG_2" }), 
    DARK_OAK_PLANKS(5, new String[] { "WOOD" }), 
    DARK_OAK_PRESSURE_PLATE(new String[0]), 
    DARK_OAK_SAPLING(5, new String[] { "SAPLING" }), 
    DARK_OAK_SIGN(new String[0]), 
    DARK_OAK_SLAB(5, new String[] { "WOOD_DOUBLE_STEP", "WOOD_STEP", "WOODEN_SLAB" }), 
    DARK_OAK_STAIRS(new String[0]), 
    DARK_OAK_TRAPDOOR(new String[0]), 
    DARK_OAK_WALL_HANGING_SIGN(new String[0]), 
    DARK_OAK_WALL_SIGN(new String[] { "WALL_SIGN" }), 
    DARK_OAK_WOOD(1, new String[] { "LOG_2" }), 
    DARK_PRISMARINE(2, new String[] { "PRISMARINE" }), 
    DARK_PRISMARINE_SLAB(new String[0]), 
    DARK_PRISMARINE_STAIRS(new String[0]), 
    DAYLIGHT_DETECTOR(new String[] { "DAYLIGHT_DETECTOR_INVERTED" }), 
    DEAD_BRAIN_CORAL(new String[0]), 
    DEAD_BRAIN_CORAL_BLOCK(new String[0]), 
    DEAD_BRAIN_CORAL_FAN(new String[0]), 
    DEAD_BRAIN_CORAL_WALL_FAN(new String[0]), 
    DEAD_BUBBLE_CORAL(new String[0]), 
    DEAD_BUBBLE_CORAL_BLOCK(new String[0]), 
    DEAD_BUBBLE_CORAL_FAN(new String[0]), 
    DEAD_BUBBLE_CORAL_WALL_FAN(new String[0]), 
    DEAD_BUSH(new String[] { "LONG_GRASS" }), 
    DEAD_FIRE_CORAL(new String[0]), 
    DEAD_FIRE_CORAL_BLOCK(new String[0]), 
    DEAD_FIRE_CORAL_FAN(new String[0]), 
    DEAD_FIRE_CORAL_WALL_FAN(new String[0]), 
    DEAD_HORN_CORAL(new String[0]), 
    DEAD_HORN_CORAL_BLOCK(new String[0]), 
    DEAD_HORN_CORAL_FAN(new String[0]), 
    DEAD_HORN_CORAL_WALL_FAN(new String[0]), 
    DEAD_TUBE_CORAL(new String[0]), 
    DEAD_TUBE_CORAL_BLOCK(new String[0]), 
    DEAD_TUBE_CORAL_FAN(new String[0]), 
    DEAD_TUBE_CORAL_WALL_FAN(new String[0]), 
    DEBUG_STICK(new String[0]), 
    DECORATED_POT(new String[0]), 
    DEEPSLATE(new String[0]), 
    DEEPSLATE_BRICKS(new String[0]), 
    DEEPSLATE_BRICK_SLAB(new String[0]), 
    DEEPSLATE_BRICK_STAIRS(new String[0]), 
    DEEPSLATE_BRICK_WALL(new String[0]), 
    DEEPSLATE_COAL_ORE(new String[0]), 
    DEEPSLATE_COPPER_ORE(new String[0]), 
    DEEPSLATE_DIAMOND_ORE(new String[0]), 
    DEEPSLATE_EMERALD_ORE(new String[0]), 
    DEEPSLATE_GOLD_ORE(new String[0]), 
    DEEPSLATE_IRON_ORE(new String[0]), 
    DEEPSLATE_LAPIS_ORE(new String[0]), 
    DEEPSLATE_REDSTONE_ORE(new String[0]), 
    DEEPSLATE_TILES(new String[0]), 
    DEEPSLATE_TILE_SLAB(new String[0]), 
    DEEPSLATE_TILE_STAIRS(new String[0]), 
    DEEPSLATE_TILE_WALL(new String[0]), 
    DETECTOR_RAIL(new String[0]), 
    DIAMOND(new String[0]), 
    DIAMOND_AXE(new String[0]), 
    DIAMOND_BLOCK(new String[0]), 
    DIAMOND_BOOTS(new String[0]), 
    DIAMOND_CHESTPLATE(new String[0]), 
    DIAMOND_HELMET(new String[0]), 
    DIAMOND_HOE(new String[0]), 
    DIAMOND_HORSE_ARMOR(new String[] { "DIAMOND_BARDING" }), 
    DIAMOND_LEGGINGS(new String[0]), 
    DIAMOND_ORE(new String[0]), 
    DIAMOND_PICKAXE(new String[0]), 
    DIAMOND_SHOVEL(new String[] { "DIAMOND_SPADE" }), 
    DIAMOND_SWORD(new String[0]), 
    DIORITE(3, new String[] { "STONE" }), 
    DIORITE_SLAB(new String[0]), 
    DIORITE_STAIRS(new String[0]), 
    DIORITE_WALL(new String[0]), 
    DIRT(new String[0]), 
    DIRT_PATH(new String[] { "GRASS_PATH" }), 
    DISC_FRAGMENT_5(new String[0]), 
    DISPENSER(new String[0]), 
    DOLPHIN_SPAWN_EGG(new String[0]), 
    DONKEY_SPAWN_EGG(32, new String[] { "MONSTER_EGG" }), 
    DRAGON_BREATH(new String[] { "DRAGONS_BREATH" }), 
    DRAGON_EGG(new String[0]), 
    DRAGON_HEAD(5, new String[] { "SKULL", "SKULL_ITEM" }), 
    DRAGON_WALL_HEAD(5, new String[] { "SKULL", "SKULL_ITEM" }), 
    DRIED_GHAST(new String[0]), 
    DRIED_KELP(new String[0]), 
    DRIED_KELP_BLOCK(new String[0]), 
    DRIPSTONE_BLOCK(new String[0]), 
    DROPPER(new String[0]), 
    DROWNED_SPAWN_EGG(new String[0]), 
    DUNE_ARMOR_TRIM_SMITHING_TEMPLATE(new String[0]), 
    ECHO_SHARD(new String[0]), 
    EGG(new String[0]), 
    ELDER_GUARDIAN_SPAWN_EGG(4, new String[] { "MONSTER_EGG" }), 
    ELYTRA(new String[0]), 
    EMERALD(new String[0]), 
    EMERALD_BLOCK(new String[0]), 
    EMERALD_ORE(new String[0]), 
    ENCHANTED_BOOK(new String[0]), 
    ENCHANTED_GOLDEN_APPLE(1, new String[] { "GOLDEN_APPLE" }), 
    ENCHANTING_TABLE(new String[] { "ENCHANTMENT_TABLE" }), 
    ENDERMAN_SPAWN_EGG(58, new String[] { "MONSTER_EGG" }), 
    ENDERMITE_SPAWN_EGG(67, new String[] { "MONSTER_EGG" }), 
    ENDER_CHEST(new String[0]), 
    ENDER_DRAGON_SPAWN_EGG(new String[0]), 
    ENDER_EYE(new String[] { "EYE_OF_ENDER" }), 
    ENDER_PEARL(new String[0]), 
    END_CRYSTAL(new String[0]), 
    END_GATEWAY(new String[0]), 
    END_PORTAL(new String[] { "ENDER_PORTAL" }), 
    END_PORTAL_FRAME(new String[] { "ENDER_PORTAL_FRAME" }), 
    END_ROD(new String[0]), 
    END_STONE(new String[] { "ENDER_STONE" }), 
    END_STONE_BRICKS(new String[] { "END_BRICKS" }), 
    END_STONE_BRICK_SLAB(new String[0]), 
    END_STONE_BRICK_STAIRS(new String[0]), 
    END_STONE_BRICK_WALL(new String[0]), 
    EVOKER_SPAWN_EGG(34, new String[] { "MONSTER_EGG" }), 
    EXPERIENCE_BOTTLE(new String[] { "EXP_BOTTLE" }), 
    EXPLORER_POTTERY_SHERD(new String[0]), 
    EXPOSED_CHISELED_COPPER(new String[0]), 
    EXPOSED_COPPER(new String[0]), 
    EXPOSED_COPPER_BULB(new String[0]), 
    EXPOSED_COPPER_DOOR(new String[0]), 
    EXPOSED_COPPER_GRATE(new String[0]), 
    EXPOSED_COPPER_TRAPDOOR(new String[0]), 
    EXPOSED_CUT_COPPER(new String[0]), 
    EXPOSED_CUT_COPPER_SLAB(new String[0]), 
    EXPOSED_CUT_COPPER_STAIRS(new String[0]), 
    EYE_ARMOR_TRIM_SMITHING_TEMPLATE(new String[0]), 
    FARMLAND(new String[] { "SOIL" }), 
    FEATHER(new String[0]), 
    FERMENTED_SPIDER_EYE(new String[0]), 
    FERN(2, new String[] { "LONG_GRASS" }), 
    FIELD_MASONED_BANNER_PATTERN(new String[0]), 
    FILLED_MAP(new String[] { "MAP" }), 
    FIRE(new String[0]), 
    FIREFLY_BUSH(new String[0]), 
    FIREWORK_ROCKET(new String[] { "FIREWORK" }), 
    FIREWORK_STAR(new String[] { "FIREWORK_CHARGE" }), 
    FIRE_CHARGE(new String[] { "FIREBALL" }), 
    FIRE_CORAL(new String[0]), 
    FIRE_CORAL_BLOCK(new String[0]), 
    FIRE_CORAL_FAN(new String[0]), 
    FIRE_CORAL_WALL_FAN(new String[0]), 
    FISHING_ROD(new String[0]), 
    FLETCHING_TABLE(new String[0]), 
    FLINT(new String[0]), 
    FLINT_AND_STEEL(new String[0]), 
    FLOWERING_AZALEA(new String[0]), 
    FLOWERING_AZALEA_LEAVES(new String[0]), 
    FLOWER_BANNER_PATTERN(new String[0]), 
    FLOWER_POT(new String[] { "FLOWER_POT", "FLOWER_POT_ITEM" }), 
    FLOW_ARMOR_TRIM_SMITHING_TEMPLATE(new String[0]), 
    FLOW_BANNER_PATTERN(new String[0]), 
    FLOW_POTTERY_SHERD(new String[0]), 
    FOX_SPAWN_EGG(new String[0]), 
    FRIEND_POTTERY_SHERD(new String[0]), 
    FROGSPAWN(new String[0]), 
    FROG_SPAWN_EGG(new String[0]), 
    FROSTED_ICE(new String[0]), 
    FURNACE(new String[] { "BURNING_FURNACE" }), 
    FURNACE_MINECART(new String[] { "POWERED_MINECART" }), 
    GHAST_SPAWN_EGG(56, new String[] { "MONSTER_EGG" }), 
    GHAST_TEAR(new String[0]), 
    GILDED_BLACKSTONE(new String[0]), 
    GLASS(new String[0]), 
    GLASS_BOTTLE(new String[0]), 
    GLASS_PANE(new String[] { "THIN_GLASS" }), 
    GLISTERING_MELON_SLICE(new String[] { "SPECKLED_MELON" }), 
    GLOBE_BANNER_PATTERN(new String[0]), 
    GLOWSTONE(new String[0]), 
    GLOWSTONE_DUST(new String[0]), 
    GLOW_BERRIES(new String[0]), 
    GLOW_INK_SAC(new String[0]), 
    GLOW_ITEM_FRAME(new String[0]), 
    GLOW_LICHEN(new String[0]), 
    GLOW_SQUID_SPAWN_EGG(new String[0]), 
    GOAT_HORN(new String[0]), 
    GOAT_SPAWN_EGG(new String[0]), 
    GOLDEN_APPLE(new String[0]), 
    GOLDEN_AXE(new String[] { "GOLD_AXE" }), 
    GOLDEN_BOOTS(new String[] { "GOLD_BOOTS" }), 
    GOLDEN_CARROT(new String[0]), 
    GOLDEN_CHESTPLATE(new String[] { "GOLD_CHESTPLATE" }), 
    GOLDEN_HELMET(new String[] { "GOLD_HELMET" }), 
    GOLDEN_HOE(new String[] { "GOLD_HOE" }), 
    GOLDEN_HORSE_ARMOR(new String[] { "GOLD_BARDING" }), 
    GOLDEN_LEGGINGS(new String[] { "GOLD_LEGGINGS" }), 
    GOLDEN_PICKAXE(new String[] { "GOLD_PICKAXE" }), 
    GOLDEN_SHOVEL(new String[] { "GOLD_SPADE" }), 
    GOLDEN_SWORD(new String[] { "GOLD_SWORD" }), 
    GOLD_BLOCK(new String[0]), 
    GOLD_INGOT(new String[0]), 
    GOLD_NUGGET(new String[0]), 
    GOLD_ORE(new String[0]), 
    GRANITE(1, new String[] { "STONE" }), 
    GRANITE_SLAB(new String[0]), 
    GRANITE_STAIRS(new String[0]), 
    GRANITE_WALL(new String[0]), 
    GRASS_BLOCK(new String[] { "GRASS" }), 
    GRAVEL(new String[0]), 
    GRAY_BANNER(8, new String[] { "STANDING_BANNER", "BANNER" }), 
    GRAY_BED(supports(12) ? 7 : 0, new String[] { "BED_BLOCK", "BED" }), 
    GRAY_BUNDLE(new String[0]), 
    GRAY_CANDLE(new String[0]), 
    GRAY_CANDLE_CAKE(new String[0]), 
    GRAY_CARPET(7, new String[] { "CARPET" }), 
    GRAY_CONCRETE(7, new String[] { "CONCRETE" }), 
    GRAY_CONCRETE_POWDER(7, new String[] { "CONCRETE_POWDER" }), 
    GRAY_DYE(8, new String[] { "INK_SACK" }), 
    GRAY_GLAZED_TERRACOTTA(new String[0]), 
    GRAY_HARNESS(new String[0]), 
    GRAY_SHULKER_BOX(new String[0]), 
    GRAY_STAINED_GLASS(7, new String[] { "STAINED_GLASS" }), 
    GRAY_STAINED_GLASS_PANE(7, new String[] { "THIN_GLASS", "STAINED_GLASS_PANE" }), 
    GRAY_TERRACOTTA(7, new String[] { "STAINED_CLAY" }), 
    GRAY_WALL_BANNER(8, new String[] { "WALL_BANNER" }), 
    GRAY_WOOL(7, new String[] { "WOOL" }), 
    GREEN_BANNER(2, new String[] { "STANDING_BANNER", "BANNER" }), 
    GREEN_BED(supports(12) ? 13 : 0, new String[] { "BED_BLOCK", "BED" }), 
    GREEN_BUNDLE(new String[0]), 
    GREEN_CANDLE(new String[0]), 
    GREEN_CANDLE_CAKE(new String[0]), 
    GREEN_CARPET(13, new String[] { "CARPET" }), 
    GREEN_CONCRETE(13, new String[] { "CONCRETE" }), 
    GREEN_CONCRETE_POWDER(13, new String[] { "CONCRETE_POWDER" }), 
    GREEN_DYE(2, new String[] { "INK_SACK", "CACTUS_GREEN" }), 
    GREEN_GLAZED_TERRACOTTA(new String[0]), 
    GREEN_HARNESS(new String[0]), 
    GREEN_SHULKER_BOX(new String[0]), 
    GREEN_STAINED_GLASS(13, new String[] { "STAINED_GLASS" }), 
    GREEN_STAINED_GLASS_PANE(13, new String[] { "THIN_GLASS", "STAINED_GLASS_PANE" }), 
    GREEN_TERRACOTTA(13, new String[] { "STAINED_CLAY" }), 
    GREEN_WALL_BANNER(2, new String[] { "WALL_BANNER" }), 
    GREEN_WOOL(13, new String[] { "WOOL" }), 
    GRINDSTONE(new String[0]), 
    GUARDIAN_SPAWN_EGG(68, new String[] { "MONSTER_EGG" }), 
    GUNPOWDER(new String[] { "SULPHUR" }), 
    GUSTER_BANNER_PATTERN(new String[0]), 
    GUSTER_POTTERY_SHERD(new String[0]), 
    HANGING_ROOTS(new String[0]), 
    HAPPY_GHAST_SPAWN_EGG(new String[0]), 
    HAY_BLOCK(new String[0]), 
    HEARTBREAK_POTTERY_SHERD(new String[0]), 
    HEART_OF_THE_SEA(new String[0]), 
    HEART_POTTERY_SHERD(new String[0]), 
    HEAVY_CORE(new String[0]), 
    HEAVY_WEIGHTED_PRESSURE_PLATE(new String[] { "IRON_PLATE" }), 
    HOGLIN_SPAWN_EGG(new String[] { "MONSTER_EGG" }), 
    HONEYCOMB(new String[0]), 
    HONEYCOMB_BLOCK(new String[0]), 
    HONEY_BLOCK(new String[0]), 
    HONEY_BOTTLE(new String[0]), 
    HOPPER(new String[0]), 
    HOPPER_MINECART(new String[0]), 
    HORN_CORAL(new String[0]), 
    HORN_CORAL_BLOCK(new String[0]), 
    HORN_CORAL_FAN(new String[0]), 
    HORN_CORAL_WALL_FAN(new String[0]), 
    HORSE_SPAWN_EGG(100, new String[] { "MONSTER_EGG" }), 
    HOST_ARMOR_TRIM_SMITHING_TEMPLATE(new String[0]), 
    HOWL_POTTERY_SHERD(new String[0]), 
    HUSK_SPAWN_EGG(23, new String[] { "MONSTER_EGG" }), 
    ICE(new String[0]), 
    INFESTED_CHISELED_STONE_BRICKS(5, new String[] { "MONSTER_EGGS" }), 
    INFESTED_COBBLESTONE(1, new String[] { "MONSTER_EGGS" }), 
    INFESTED_CRACKED_STONE_BRICKS(4, new String[] { "MONSTER_EGGS" }), 
    INFESTED_DEEPSLATE(new String[0]), 
    INFESTED_MOSSY_STONE_BRICKS(3, new String[] { "MONSTER_EGGS" }), 
    INFESTED_STONE(new String[] { "MONSTER_EGGS" }), 
    INFESTED_STONE_BRICKS(2, new String[] { "MONSTER_EGGS" }), 
    INK_SAC(new String[] { "INK_SACK" }), 
    IRON_AXE(new String[0]), 
    IRON_BARS(new String[] { "IRON_FENCE" }), 
    IRON_BLOCK(new String[0]), 
    IRON_BOOTS(new String[0]), 
    IRON_CHESTPLATE(new String[0]), 
    IRON_DOOR(new String[] { "IRON_DOOR_BLOCK" }), 
    IRON_GOLEM_SPAWN_EGG(new String[0]), 
    IRON_HELMET(new String[0]), 
    IRON_HOE(new String[0]), 
    IRON_HORSE_ARMOR(new String[] { "IRON_BARDING" }), 
    IRON_INGOT(new String[0]), 
    IRON_LEGGINGS(new String[0]), 
    IRON_NUGGET(new String[0]), 
    IRON_ORE(new String[0]), 
    IRON_PICKAXE(new String[0]), 
    IRON_SHOVEL(new String[] { "IRON_SPADE" }), 
    IRON_SWORD(new String[0]), 
    IRON_TRAPDOOR(new String[0]), 
    ITEM_FRAME(new String[0]), 
    JACK_O_LANTERN(new String[0]), 
    JIGSAW(new String[0]), 
    JUKEBOX(new String[0]), 
    JUNGLE_BOAT(new String[] { "BOAT_JUNGLE" }), 
    JUNGLE_BUTTON(new String[0]), 
    JUNGLE_CHEST_BOAT(new String[0]), 
    JUNGLE_DOOR(new String[] { "JUNGLE_DOOR", "JUNGLE_DOOR_ITEM" }), 
    JUNGLE_FENCE(new String[0]), 
    JUNGLE_FENCE_GATE(new String[0]), 
    JUNGLE_HANGING_SIGN(new String[0]), 
    JUNGLE_LEAVES(3, new String[] { "LEAVES" }), 
    JUNGLE_LOG(3, new String[] { "LOG" }), 
    JUNGLE_PLANKS(3, new String[] { "WOOD" }), 
    JUNGLE_PRESSURE_PLATE(new String[0]), 
    JUNGLE_SAPLING(3, new String[] { "SAPLING" }), 
    JUNGLE_SIGN(new String[0]), 
    JUNGLE_SLAB(3, new String[] { "WOOD_DOUBLE_STEP", "WOOD_STEP", "WOODEN_SLAB" }), 
    JUNGLE_STAIRS(new String[] { "JUNGLE_WOOD_STAIRS" }), 
    JUNGLE_TRAPDOOR(new String[0]), 
    JUNGLE_WALL_HANGING_SIGN(new String[0]), 
    JUNGLE_WALL_SIGN(new String[] { "WALL_SIGN" }), 
    JUNGLE_WOOD(3, new String[] { "LOG" }), 
    KELP(new String[0]), 
    KELP_PLANT(new String[0]), 
    KNOWLEDGE_BOOK(new String[] { "BOOK" }), 
    LADDER(new String[0]), 
    LANTERN(new String[0]), 
    LAPIS_BLOCK(new String[0]), 
    LAPIS_LAZULI(4, new String[] { "INK_SACK" }), 
    LAPIS_ORE(new String[0]), 
    LARGE_AMETHYST_BUD(new String[0]), 
    LARGE_FERN(3, new String[] { "DOUBLE_PLANT" }), 
    LAVA(new String[] { "STATIONARY_LAVA" }), 
    LAVA_BUCKET(new String[0]), 
    LAVA_CAULDRON(new String[0]), 
    LEAD(new String[] { "LEASH" }), 
    LEAF_LITTER(new String[0]), 
    LEATHER(new String[0]), 
    LEATHER_BOOTS(new String[0]), 
    LEATHER_CHESTPLATE(new String[0]), 
    LEATHER_HELMET(new String[0]), 
    LEATHER_HORSE_ARMOR(new String[] { "IRON_HORSE_ARMOR" }), 
    LEATHER_LEGGINGS(new String[0]), 
    LECTERN(new String[0]), 
    LEVER(new String[0]), 
    LIGHT(new String[0]), 
    LIGHTNING_ROD(new String[0]), 
    LIGHT_BLUE_BANNER(12, new String[] { "STANDING_BANNER", "BANNER" }), 
    LIGHT_BLUE_BED(supports(12) ? 3 : 0, new String[] { "BED_BLOCK", "BED" }), 
    LIGHT_BLUE_BUNDLE(new String[0]), 
    LIGHT_BLUE_CANDLE(new String[0]), 
    LIGHT_BLUE_CANDLE_CAKE(new String[0]), 
    LIGHT_BLUE_CARPET(3, new String[] { "CARPET" }), 
    LIGHT_BLUE_CONCRETE(3, new String[] { "CONCRETE" }), 
    LIGHT_BLUE_CONCRETE_POWDER(3, new String[] { "CONCRETE_POWDER" }), 
    LIGHT_BLUE_DYE(12, new String[] { "INK_SACK" }), 
    LIGHT_BLUE_GLAZED_TERRACOTTA(new String[0]), 
    LIGHT_BLUE_HARNESS(new String[0]), 
    LIGHT_BLUE_SHULKER_BOX(new String[0]), 
    LIGHT_BLUE_STAINED_GLASS(3, new String[] { "STAINED_GLASS" }), 
    LIGHT_BLUE_STAINED_GLASS_PANE(3, new String[] { "THIN_GLASS", "STAINED_GLASS_PANE" }), 
    LIGHT_BLUE_TERRACOTTA(3, new String[] { "STAINED_CLAY" }), 
    LIGHT_BLUE_WALL_BANNER(12, new String[] { "WALL_BANNER", "STANDING_BANNER", "BANNER" }), 
    LIGHT_BLUE_WOOL(3, new String[] { "WOOL" }), 
    LIGHT_GRAY_BANNER(7, new String[] { "STANDING_BANNER", "BANNER" }), 
    LIGHT_GRAY_BED(supports(12) ? 8 : 0, new String[] { "BED_BLOCK", "BED" }), 
    LIGHT_GRAY_BUNDLE(new String[0]), 
    LIGHT_GRAY_CANDLE(new String[0]), 
    LIGHT_GRAY_CANDLE_CAKE(new String[0]), 
    LIGHT_GRAY_CARPET(8, new String[] { "CARPET" }), 
    LIGHT_GRAY_CONCRETE(8, new String[] { "CONCRETE" }), 
    LIGHT_GRAY_CONCRETE_POWDER(8, new String[] { "CONCRETE_POWDER" }), 
    LIGHT_GRAY_DYE(7, new String[] { "INK_SACK" }), 
    LIGHT_GRAY_GLAZED_TERRACOTTA(new String[] { "SILVER_GLAZED_TERRACOTTA" }), 
    LIGHT_GRAY_HARNESS(new String[0]), 
    LIGHT_GRAY_SHULKER_BOX(new String[] { "SILVER_SHULKER_BOX" }), 
    LIGHT_GRAY_STAINED_GLASS(8, new String[] { "STAINED_GLASS" }), 
    LIGHT_GRAY_STAINED_GLASS_PANE(8, new String[] { "THIN_GLASS", "STAINED_GLASS_PANE" }), 
    LIGHT_GRAY_TERRACOTTA(8, new String[] { "STAINED_CLAY" }), 
    LIGHT_GRAY_WALL_BANNER(7, new String[] { "WALL_BANNER" }), 
    LIGHT_GRAY_WOOL(8, new String[] { "WOOL" }), 
    LIGHT_WEIGHTED_PRESSURE_PLATE(new String[] { "GOLD_PLATE" }), 
    LILAC(1, new String[] { "DOUBLE_PLANT" }), 
    LILY_OF_THE_VALLEY(new String[0]), 
    LILY_PAD(new String[] { "WATER_LILY" }), 
    LIME_BANNER(10, new String[] { "STANDING_BANNER", "BANNER" }), 
    LIME_BED(supports(12) ? 5 : 0, new String[] { "BED_BLOCK", "BED" }), 
    LIME_BUNDLE(new String[0]), 
    LIME_CANDLE(new String[0]), 
    LIME_CANDLE_CAKE(new String[0]), 
    LIME_CARPET(5, new String[] { "CARPET" }), 
    LIME_CONCRETE(5, new String[] { "CONCRETE" }), 
    LIME_CONCRETE_POWDER(5, new String[] { "CONCRETE_POWDER" }), 
    LIME_DYE(10, new String[] { "INK_SACK" }), 
    LIME_GLAZED_TERRACOTTA(new String[0]), 
    LIME_HARNESS(new String[0]), 
    LIME_SHULKER_BOX(new String[0]), 
    LIME_STAINED_GLASS(5, new String[] { "STAINED_GLASS" }), 
    LIME_STAINED_GLASS_PANE(5, new String[] { "STAINED_GLASS_PANE" }), 
    LIME_TERRACOTTA(5, new String[] { "STAINED_CLAY" }), 
    LIME_WALL_BANNER(10, new String[] { "WALL_BANNER" }), 
    LIME_WOOL(5, new String[] { "WOOL" }), 
    LINGERING_POTION(new String[0]), 
    LLAMA_SPAWN_EGG(103, new String[] { "MONSTER_EGG" }), 
    LODESTONE(new String[0]), 
    LOOM(new String[0]), 
    MACE(new String[0]), 
    MAGENTA_BANNER(13, new String[] { "STANDING_BANNER", "BANNER" }), 
    MAGENTA_BED(supports(12) ? 2 : 0, new String[] { "BED_BLOCK", "BED" }), 
    MAGENTA_BUNDLE(new String[0]), 
    MAGENTA_CANDLE(new String[0]), 
    MAGENTA_CANDLE_CAKE(new String[0]), 
    MAGENTA_CARPET(2, new String[] { "CARPET" }), 
    MAGENTA_CONCRETE(2, new String[] { "CONCRETE" }), 
    MAGENTA_CONCRETE_POWDER(2, new String[] { "CONCRETE_POWDER" }), 
    MAGENTA_DYE(13, new String[] { "INK_SACK" }), 
    MAGENTA_GLAZED_TERRACOTTA(new String[0]), 
    MAGENTA_HARNESS(new String[0]), 
    MAGENTA_SHULKER_BOX(new String[0]), 
    MAGENTA_STAINED_GLASS(2, new String[] { "STAINED_GLASS" }), 
    MAGENTA_STAINED_GLASS_PANE(2, new String[] { "THIN_GLASS", "STAINED_GLASS_PANE" }), 
    MAGENTA_TERRACOTTA(2, new String[] { "STAINED_CLAY" }), 
    MAGENTA_WALL_BANNER(13, new String[] { "WALL_BANNER" }), 
    MAGENTA_WOOL(2, new String[] { "WOOL" }), 
    MAGMA_BLOCK(new String[] { "MAGMA" }), 
    MAGMA_CREAM(new String[0]), 
    MAGMA_CUBE_SPAWN_EGG(62, new String[] { "MONSTER_EGG" }), 
    MANGROVE_BOAT(new String[0]), 
    MANGROVE_BUTTON(new String[0]), 
    MANGROVE_CHEST_BOAT(new String[0]), 
    MANGROVE_DOOR(new String[0]), 
    MANGROVE_FENCE(new String[0]), 
    MANGROVE_FENCE_GATE(new String[0]), 
    MANGROVE_HANGING_SIGN(new String[0]), 
    MANGROVE_LEAVES(new String[0]), 
    MANGROVE_LOG(new String[0]), 
    MANGROVE_PLANKS(new String[0]), 
    MANGROVE_PRESSURE_PLATE(new String[0]), 
    MANGROVE_PROPAGULE(new String[0]), 
    MANGROVE_ROOTS(new String[0]), 
    MANGROVE_SIGN(new String[0]), 
    MANGROVE_SLAB(new String[0]), 
    MANGROVE_STAIRS(new String[0]), 
    MANGROVE_TRAPDOOR(new String[0]), 
    MANGROVE_WALL_HANGING_SIGN(new String[0]), 
    MANGROVE_WALL_SIGN(new String[0]), 
    MANGROVE_WOOD(new String[0]), 
    MAP(new String[] { "EMPTY_MAP" }), 
    MEDIUM_AMETHYST_BUD(new String[0]), 
    MELON(new String[] { "MELON_BLOCK" }), 
    MELON_SEEDS(new String[0]), 
    MELON_SLICE(new String[] { "MELON" }), 
    MELON_STEM(new String[0]), 
    MILK_BUCKET(new String[0]), 
    MINECART(new String[0]), 
    MINER_POTTERY_SHERD(new String[0]), 
    MOJANG_BANNER_PATTERN(new String[0]), 
    MOOSHROOM_SPAWN_EGG(96, new String[] { "MONSTER_EGG" }), 
    MOSSY_COBBLESTONE(new String[0]), 
    MOSSY_COBBLESTONE_SLAB(new String[0]), 
    MOSSY_COBBLESTONE_STAIRS(new String[0]), 
    MOSSY_COBBLESTONE_WALL(1, new String[] { "COBBLE_WALL", "COBBLESTONE_WALL" }), 
    MOSSY_STONE_BRICKS(1, new String[] { "SMOOTH_BRICK" }), 
    MOSSY_STONE_BRICK_SLAB(new String[0]), 
    MOSSY_STONE_BRICK_STAIRS(new String[0]), 
    MOSSY_STONE_BRICK_WALL(new String[0]), 
    MOSS_BLOCK(new String[0]), 
    MOSS_CARPET(new String[0]), 
    MOURNER_POTTERY_SHERD(new String[0]), 
    MOVING_PISTON(new String[] { "PISTON_MOVING_PIECE" }), 
    MUD(new String[0]), 
    MUDDY_MANGROVE_ROOTS(new String[0]), 
    MUD_BRICKS(new String[0]), 
    MUD_BRICK_SLAB(new String[0]), 
    MUD_BRICK_STAIRS(new String[0]), 
    MUD_BRICK_WALL(new String[0]), 
    MULE_SPAWN_EGG(32, new String[] { "MONSTER_EGG" }), 
    MUSHROOM_STEM(new String[] { "BROWN_MUSHROOM" }), 
    MUSHROOM_STEW(new String[] { "MUSHROOM_SOUP" }), 
    MUSIC_DISC_11(new String[] { "RECORD_11" }), 
    MUSIC_DISC_13(new String[] { "GOLD_RECORD" }), 
    MUSIC_DISC_5(new String[0]), 
    MUSIC_DISC_BLOCKS(new String[] { "RECORD_3" }), 
    MUSIC_DISC_CAT(new String[] { "GREEN_RECORD" }), 
    MUSIC_DISC_CHIRP(new String[] { "RECORD_4" }), 
    MUSIC_DISC_CREATOR(new String[0]), 
    MUSIC_DISC_CREATOR_MUSIC_BOX(new String[0]), 
    MUSIC_DISC_FAR(new String[] { "RECORD_5" }), 
    MUSIC_DISC_MALL(new String[] { "RECORD_6" }), 
    MUSIC_DISC_MELLOHI(new String[] { "RECORD_7" }), 
    MUSIC_DISC_OTHERSIDE(new String[0]), 
    MUSIC_DISC_PIGSTEP(new String[0]), 
    MUSIC_DISC_PRECIPICE(new String[0]), 
    MUSIC_DISC_RELIC(new String[0]), 
    MUSIC_DISC_STAL(new String[] { "RECORD_8" }), 
    MUSIC_DISC_STRAD(new String[] { "RECORD_9" }), 
    MUSIC_DISC_TEARS(new String[0]), 
    MUSIC_DISC_WAIT(new String[] { "RECORD_12" }), 
    MUSIC_DISC_WARD(new String[] { "RECORD_10" }), 
    MUTTON(new String[0]), 
    MYCELIUM(new String[] { "MYCEL" }), 
    NAME_TAG(new String[0]), 
    NAUTILUS_SHELL(new String[0]), 
    NETHERITE_AXE(new String[0]), 
    NETHERITE_BLOCK(new String[0]), 
    NETHERITE_BOOTS(new String[0]), 
    NETHERITE_CHESTPLATE(new String[0]), 
    NETHERITE_HELMET(new String[0]), 
    NETHERITE_HOE(new String[0]), 
    NETHERITE_INGOT(new String[0]), 
    NETHERITE_LEGGINGS(new String[0]), 
    NETHERITE_PICKAXE(new String[0]), 
    NETHERITE_SCRAP(new String[0]), 
    NETHERITE_SHOVEL(new String[0]), 
    NETHERITE_SWORD(new String[0]), 
    NETHERITE_UPGRADE_SMITHING_TEMPLATE(new String[0]), 
    NETHERRACK(new String[0]), 
    NETHER_BRICK(new String[] { "NETHER_BRICK_ITEM" }), 
    NETHER_BRICKS(new String[] { "NETHER_BRICK" }), 
    NETHER_BRICK_FENCE(new String[] { "NETHER_FENCE" }), 
    NETHER_BRICK_SLAB(6, new String[] { "STEP" }), 
    NETHER_BRICK_STAIRS(new String[0]), 
    NETHER_BRICK_WALL(new String[0]), 
    NETHER_GOLD_ORE(new String[0]), 
    NETHER_PORTAL(new String[] { "PORTAL" }), 
    NETHER_QUARTZ_ORE(new String[] { "QUARTZ_ORE" }), 
    NETHER_SPROUTS(new String[0]), 
    NETHER_STAR(new String[0]), 
    NETHER_WART(new String[] { "NETHER_WARTS", "NETHER_STALK" }), 
    NETHER_WART_BLOCK(new String[0]), 
    NOTE_BLOCK(new String[0]), 
    OAK_BOAT(new String[] { "BOAT" }), 
    OAK_BUTTON(new String[] { "WOOD_BUTTON" }), 
    OAK_CHEST_BOAT(new String[0]), 
    OAK_DOOR(new String[] { "WOODEN_DOOR", "WOOD_DOOR" }), 
    OAK_FENCE(new String[] { "FENCE" }), 
    OAK_FENCE_GATE(new String[] { "FENCE_GATE" }), 
    OAK_HANGING_SIGN(new String[0]), 
    OAK_LEAVES(new String[] { "LEAVES" }), 
    OAK_LOG(new String[] { "LOG" }), 
    OAK_PLANKS(new String[] { "WOOD" }), 
    OAK_PRESSURE_PLATE(new String[] { "WOOD_PLATE" }), 
    OAK_SAPLING(new String[] { "SAPLING" }), 
    OAK_SIGN(new String[] { "SIGN_POST", "SIGN" }), 
    OAK_SLAB(new String[] { "WOOD_DOUBLE_STEP", "WOOD_STEP", "WOODEN_SLAB" }), 
    OAK_STAIRS(new String[] { "WOOD_STAIRS" }), 
    OAK_TRAPDOOR(new String[] { "TRAP_DOOR" }), 
    OAK_WALL_HANGING_SIGN(new String[0]), 
    OAK_WALL_SIGN(new String[] { "WALL_SIGN" }), 
    OAK_WOOD(new String[] { "LOG" }), 
    OBSERVER(new String[0]), 
    OBSIDIAN(new String[0]), 
    OCELOT_SPAWN_EGG(98, new String[] { "MONSTER_EGG" }), 
    OCHRE_FROGLIGHT(new String[0]), 
    OMINOUS_BOTTLE(new String[0]), 
    OMINOUS_TRIAL_KEY(new String[0]), 
    OPEN_EYEBLOSSOM(new String[0]), 
    ORANGE_BANNER(14, new String[] { "STANDING_BANNER", "BANNER" }), 
    ORANGE_BED((int)(supports(12) ? 1 : 0), new String[] { "BED_BLOCK", "BED" }), 
    ORANGE_BUNDLE(new String[0]), 
    ORANGE_CANDLE(new String[0]), 
    ORANGE_CANDLE_CAKE(new String[0]), 
    ORANGE_CARPET(1, new String[] { "CARPET" }), 
    ORANGE_CONCRETE(1, new String[] { "CONCRETE" }), 
    ORANGE_CONCRETE_POWDER(1, new String[] { "CONCRETE_POWDER" }), 
    ORANGE_DYE(14, new String[] { "INK_SACK" }), 
    ORANGE_GLAZED_TERRACOTTA(new String[0]), 
    ORANGE_HARNESS(new String[0]), 
    ORANGE_SHULKER_BOX(new String[0]), 
    ORANGE_STAINED_GLASS(1, new String[] { "STAINED_GLASS" }), 
    ORANGE_STAINED_GLASS_PANE(1, new String[] { "STAINED_GLASS_PANE" }), 
    ORANGE_TERRACOTTA(1, new String[] { "STAINED_CLAY" }), 
    ORANGE_TULIP(5, new String[] { "RED_ROSE" }), 
    ORANGE_WALL_BANNER(14, new String[] { "WALL_BANNER" }), 
    ORANGE_WOOL(1, new String[] { "WOOL" }), 
    OXEYE_DAISY(8, new String[] { "RED_ROSE" }), 
    OXIDIZED_CHISELED_COPPER(new String[0]), 
    OXIDIZED_COPPER(new String[0]), 
    OXIDIZED_COPPER_BULB(new String[0]), 
    OXIDIZED_COPPER_DOOR(new String[0]), 
    OXIDIZED_COPPER_GRATE(new String[0]), 
    OXIDIZED_COPPER_TRAPDOOR(new String[0]), 
    OXIDIZED_CUT_COPPER(new String[0]), 
    OXIDIZED_CUT_COPPER_SLAB(new String[0]), 
    OXIDIZED_CUT_COPPER_STAIRS(new String[0]), 
    PACKED_ICE(new String[0]), 
    PACKED_MUD(new String[0]), 
    PAINTING(new String[0]), 
    PALE_HANGING_MOSS(new String[0]), 
    PALE_MOSS_BLOCK(new String[0]), 
    PALE_MOSS_CARPET(new String[0]), 
    PALE_OAK_BOAT(new String[0]), 
    PALE_OAK_BUTTON(new String[0]), 
    PALE_OAK_CHEST_BOAT(new String[0]), 
    PALE_OAK_DOOR(new String[0]), 
    PALE_OAK_FENCE(new String[0]), 
    PALE_OAK_FENCE_GATE(new String[0]), 
    PALE_OAK_HANGING_SIGN(new String[0]), 
    PALE_OAK_LEAVES(new String[0]), 
    PALE_OAK_LOG(new String[0]), 
    PALE_OAK_PLANKS(new String[0]), 
    PALE_OAK_PRESSURE_PLATE(new String[0]), 
    PALE_OAK_SAPLING(new String[0]), 
    PALE_OAK_SIGN(new String[0]), 
    PALE_OAK_SLAB(new String[0]), 
    PALE_OAK_STAIRS(new String[0]), 
    PALE_OAK_TRAPDOOR(new String[0]), 
    PALE_OAK_WALL_HANGING_SIGN(new String[0]), 
    PALE_OAK_WALL_SIGN(new String[0]), 
    PALE_OAK_WOOD(new String[0]), 
    PANDA_SPAWN_EGG(new String[0]), 
    PAPER(new String[0]), 
    PARROT_SPAWN_EGG(105, new String[] { "MONSTER_EGG" }), 
    PEARLESCENT_FROGLIGHT(new String[0]), 
    PEONY(5, new String[] { "DOUBLE_PLANT" }), 
    PETRIFIED_OAK_SLAB(new String[] { "WOOD_STEP" }), 
    PHANTOM_MEMBRANE(new String[0]), 
    PHANTOM_SPAWN_EGG(new String[0]), 
    PIGLIN_BANNER_PATTERN(new String[0]), 
    PIGLIN_BRUTE_SPAWN_EGG(new String[0]), 
    PIGLIN_HEAD(new String[0]), 
    PIGLIN_SPAWN_EGG(57, new String[] { "MONSTER_EGG" }), 
    PIGLIN_WALL_HEAD(new String[0]), 
    PIG_SPAWN_EGG(90, new String[] { "MONSTER_EGG" }), 
    PILLAGER_SPAWN_EGG(new String[0]), 
    PINK_BANNER(9, new String[] { "STANDING_BANNER", "BANNER" }), 
    PINK_BED(supports(12) ? 6 : 0, new String[] { "BED_BLOCK", "BED" }), 
    PINK_BUNDLE(new String[0]), 
    PINK_CANDLE(new String[0]), 
    PINK_CANDLE_CAKE(new String[0]), 
    PINK_CARPET(6, new String[] { "CARPET" }), 
    PINK_CONCRETE(6, new String[] { "CONCRETE" }), 
    PINK_CONCRETE_POWDER(6, new String[] { "CONCRETE_POWDER" }), 
    PINK_DYE(9, new String[] { "INK_SACK" }), 
    PINK_GLAZED_TERRACOTTA(new String[0]), 
    PINK_HARNESS(new String[0]), 
    PINK_PETALS(new String[0]), 
    PINK_SHULKER_BOX(new String[0]), 
    PINK_STAINED_GLASS(6, new String[] { "STAINED_GLASS" }), 
    PINK_STAINED_GLASS_PANE(6, new String[] { "THIN_GLASS", "STAINED_GLASS_PANE" }), 
    PINK_TERRACOTTA(6, new String[] { "STAINED_CLAY" }), 
    PINK_TULIP(7, new String[] { "RED_ROSE" }), 
    PINK_WALL_BANNER(9, new String[] { "WALL_BANNER" }), 
    PINK_WOOL(6, new String[] { "WOOL" }), 
    PISTON(new String[] { "PISTON_BASE" }), 
    PISTON_HEAD(new String[] { "PISTON_EXTENSION" }), 
    PITCHER_CROP(new String[0]), 
    PITCHER_PLANT(new String[0]), 
    PITCHER_POD(new String[0]), 
    PLAYER_HEAD(3, new String[] { "SKULL", "SKULL_ITEM" }), 
    PLAYER_WALL_HEAD(3, new String[] { "SKULL", "SKULL_ITEM" }), 
    PLENTY_POTTERY_SHERD(new String[0]), 
    PODZOL(2, new String[] { "DIRT" }), 
    POINTED_DRIPSTONE(new String[0]), 
    POISONOUS_POTATO(new String[0]), 
    POLAR_BEAR_SPAWN_EGG(102, new String[] { "MONSTER_EGG" }), 
    POLISHED_ANDESITE(6, new String[] { "STONE" }), 
    POLISHED_ANDESITE_SLAB(new String[0]), 
    POLISHED_ANDESITE_STAIRS(new String[0]), 
    POLISHED_BASALT(new String[0]), 
    POLISHED_BLACKSTONE(new String[0]), 
    POLISHED_BLACKSTONE_BRICKS(new String[0]), 
    POLISHED_BLACKSTONE_BRICK_SLAB(new String[0]), 
    POLISHED_BLACKSTONE_BRICK_STAIRS(new String[0]), 
    POLISHED_BLACKSTONE_BRICK_WALL(new String[0]), 
    POLISHED_BLACKSTONE_BUTTON(new String[0]), 
    POLISHED_BLACKSTONE_PRESSURE_PLATE(new String[0]), 
    POLISHED_BLACKSTONE_SLAB(new String[0]), 
    POLISHED_BLACKSTONE_STAIRS(new String[0]), 
    POLISHED_BLACKSTONE_WALL(new String[0]), 
    POLISHED_DEEPSLATE(new String[0]), 
    POLISHED_DEEPSLATE_SLAB(new String[0]), 
    POLISHED_DEEPSLATE_STAIRS(new String[0]), 
    POLISHED_DEEPSLATE_WALL(new String[0]), 
    POLISHED_DIORITE(4, new String[] { "STONE" }), 
    POLISHED_DIORITE_SLAB(new String[0]), 
    POLISHED_DIORITE_STAIRS(new String[0]), 
    POLISHED_GRANITE(2, new String[] { "STONE" }), 
    POLISHED_GRANITE_SLAB(new String[0]), 
    POLISHED_GRANITE_STAIRS(new String[0]), 
    POLISHED_TUFF(new String[0]), 
    POLISHED_TUFF_SLAB(new String[0]), 
    POLISHED_TUFF_STAIRS(new String[0]), 
    POLISHED_TUFF_WALL(new String[0]), 
    POPPED_CHORUS_FRUIT(new String[] { "CHORUS_FRUIT_POPPED" }), 
    POPPY(new String[] { "RED_ROSE" }), 
    PORKCHOP(new String[] { "PORK" }), 
    POTATO(new String[] { "POTATO_ITEM" }), 
    POTATOES(new String[] { "POTATO" }), 
    POTION(new String[0]), 
    POTTED_ACACIA_SAPLING(4, new String[] { "FLOWER_POT" }), 
    POTTED_ALLIUM(2, new String[] { "FLOWER_POT" }), 
    POTTED_AZALEA_BUSH(new String[0]), 
    POTTED_AZURE_BLUET(3, new String[] { "FLOWER_POT" }), 
    POTTED_BAMBOO(new String[0]), 
    POTTED_BIRCH_SAPLING(2, new String[] { "FLOWER_POT" }), 
    POTTED_BLUE_ORCHID(1, new String[] { "FLOWER_POT" }), 
    POTTED_BROWN_MUSHROOM(new String[] { "FLOWER_POT" }), 
    POTTED_CACTUS(new String[] { "FLOWER_POT" }), 
    POTTED_CHERRY_SAPLING(new String[0]), 
    POTTED_CLOSED_EYEBLOSSOM(new String[0]), 
    POTTED_CORNFLOWER(new String[0]), 
    POTTED_CRIMSON_FUNGUS(new String[0]), 
    POTTED_CRIMSON_ROOTS(new String[0]), 
    POTTED_DANDELION(new String[] { "FLOWER_POT" }), 
    POTTED_DARK_OAK_SAPLING(5, new String[] { "FLOWER_POT" }), 
    POTTED_DEAD_BUSH(new String[] { "FLOWER_POT" }), 
    POTTED_FERN(2, new String[] { "FLOWER_POT" }), 
    POTTED_FLOWERING_AZALEA_BUSH(new String[0]), 
    POTTED_JUNGLE_SAPLING(3, new String[] { "FLOWER_POT" }), 
    POTTED_LILY_OF_THE_VALLEY(new String[0]), 
    POTTED_MANGROVE_PROPAGULE(new String[0]), 
    POTTED_OAK_SAPLING(new String[] { "FLOWER_POT" }), 
    POTTED_OPEN_EYEBLOSSOM(new String[0]), 
    POTTED_ORANGE_TULIP(5, new String[] { "FLOWER_POT" }), 
    POTTED_OXEYE_DAISY(8, new String[] { "FLOWER_POT" }), 
    POTTED_PALE_OAK_SAPLING(new String[0]), 
    POTTED_PINK_TULIP(7, new String[] { "FLOWER_POT" }), 
    POTTED_POPPY(new String[] { "FLOWER_POT" }), 
    POTTED_RED_MUSHROOM(new String[] { "FLOWER_POT" }), 
    POTTED_RED_TULIP(4, new String[] { "FLOWER_POT" }), 
    POTTED_SPRUCE_SAPLING(1, new String[] { "FLOWER_POT" }), 
    POTTED_TORCHFLOWER(new String[0]), 
    POTTED_WARPED_FUNGUS(new String[0]), 
    POTTED_WARPED_ROOTS(new String[0]), 
    POTTED_WHITE_TULIP(6, new String[] { "FLOWER_POT" }), 
    POTTED_WITHER_ROSE(new String[0]), 
    @Deprecated
    POTTERY_SHARD_ARCHER(new String[0]), 
    @Deprecated
    POTTERY_SHARD_ARMS_UP(new String[0]), 
    @Deprecated
    POTTERY_SHARD_PRIZE(new String[0]), 
    @Deprecated
    POTTERY_SHARD_SKULL(new String[0]), 
    POWDER_SNOW(new String[0]), 
    POWDER_SNOW_BUCKET(new String[0]), 
    POWDER_SNOW_CAULDRON(new String[0]), 
    POWERED_RAIL(new String[0]), 
    PRISMARINE(new String[0]), 
    PRISMARINE_BRICKS(1, new String[] { "PRISMARINE" }), 
    PRISMARINE_BRICK_SLAB(new String[0]), 
    PRISMARINE_BRICK_STAIRS(new String[0]), 
    PRISMARINE_CRYSTALS(new String[0]), 
    PRISMARINE_SHARD(new String[0]), 
    PRISMARINE_SLAB(new String[0]), 
    PRISMARINE_STAIRS(new String[0]), 
    PRISMARINE_WALL(new String[0]), 
    PRIZE_POTTERY_SHERD(new String[0]), 
    PUFFERFISH(3, new String[] { "RAW_FISH" }), 
    PUFFERFISH_BUCKET(new String[0]), 
    PUFFERFISH_SPAWN_EGG(new String[0]), 
    PUMPKIN(new String[0]), 
    PUMPKIN_PIE(new String[0]), 
    PUMPKIN_SEEDS(new String[0]), 
    PUMPKIN_STEM(new String[0]), 
    PURPLE_BANNER(5, new String[] { "STANDING_BANNER", "BANNER" }), 
    PURPLE_BED(supports(12) ? 10 : 0, new String[] { "BED_BLOCK", "BED" }), 
    PURPLE_BUNDLE(new String[0]), 
    PURPLE_CANDLE(new String[0]), 
    PURPLE_CANDLE_CAKE(new String[0]), 
    PURPLE_CARPET(10, new String[] { "CARPET" }), 
    PURPLE_CONCRETE(10, new String[] { "CONCRETE" }), 
    PURPLE_CONCRETE_POWDER(10, new String[] { "CONCRETE_POWDER" }), 
    PURPLE_DYE(5, new String[] { "INK_SACK" }), 
    PURPLE_GLAZED_TERRACOTTA(new String[0]), 
    PURPLE_HARNESS(new String[0]), 
    PURPLE_SHULKER_BOX(new String[0]), 
    PURPLE_STAINED_GLASS(10, new String[] { "STAINED_GLASS" }), 
    PURPLE_STAINED_GLASS_PANE(10, new String[] { "THIN_GLASS", "STAINED_GLASS_PANE" }), 
    PURPLE_TERRACOTTA(10, new String[] { "STAINED_CLAY" }), 
    PURPLE_WALL_BANNER(5, new String[] { "WALL_BANNER" }), 
    PURPLE_WOOL(10, new String[] { "WOOL" }), 
    PURPUR_BLOCK(new String[0]), 
    PURPUR_PILLAR(new String[0]), 
    PURPUR_SLAB(new String[] { "PURPUR_DOUBLE_SLAB" }), 
    PURPUR_STAIRS(new String[0]), 
    QUARTZ(new String[0]), 
    QUARTZ_BLOCK(new String[0]), 
    QUARTZ_BRICKS(new String[0]), 
    QUARTZ_PILLAR(2, new String[] { "QUARTZ_BLOCK" }), 
    QUARTZ_SLAB(7, new String[] { "STEP" }), 
    QUARTZ_STAIRS(new String[0]), 
    RABBIT(new String[0]), 
    RABBIT_FOOT(new String[0]), 
    RABBIT_HIDE(new String[0]), 
    RABBIT_SPAWN_EGG(101, new String[] { "MONSTER_EGG" }), 
    RABBIT_STEW(new String[0]), 
    RAIL(new String[] { "RAILS" }), 
    RAISER_ARMOR_TRIM_SMITHING_TEMPLATE(new String[0]), 
    RAVAGER_SPAWN_EGG(new String[0]), 
    RAW_COPPER(new String[0]), 
    RAW_COPPER_BLOCK(new String[0]), 
    RAW_GOLD(new String[0]), 
    RAW_GOLD_BLOCK(new String[0]), 
    RAW_IRON(new String[0]), 
    RAW_IRON_BLOCK(new String[0]), 
    RECOVERY_COMPASS(new String[0]), 
    REDSTONE(new String[0]), 
    REDSTONE_BLOCK(new String[0]), 
    REDSTONE_LAMP(new String[] { "REDSTONE_LAMP_ON", "REDSTONE_LAMP_OFF" }), 
    REDSTONE_ORE(new String[] { "GLOWING_REDSTONE_ORE" }), 
    REDSTONE_TORCH(new String[] { "REDSTONE_TORCH_OFF", "REDSTONE_TORCH_ON" }), 
    REDSTONE_WALL_TORCH(new String[0]), 
    REDSTONE_WIRE(new String[0]), 
    RED_BANNER(1, new String[] { "STANDING_BANNER", "BANNER" }), 
    RED_BED(supports(12) ? 14 : 0, new String[] { "BED_BLOCK", "BED" }), 
    RED_BUNDLE(new String[0]), 
    RED_CANDLE(new String[0]), 
    RED_CANDLE_CAKE(new String[0]), 
    RED_CARPET(14, new String[] { "CARPET" }), 
    RED_CONCRETE(14, new String[] { "CONCRETE" }), 
    RED_CONCRETE_POWDER(14, new String[] { "CONCRETE_POWDER" }), 
    RED_DYE(1, new String[] { "INK_SACK", "ROSE_RED" }), 
    RED_GLAZED_TERRACOTTA(new String[0]), 
    RED_HARNESS(new String[0]), 
    RED_MUSHROOM(new String[0]), 
    RED_MUSHROOM_BLOCK(new String[] { "RED_MUSHROOM", "HUGE_MUSHROOM_2" }), 
    RED_NETHER_BRICKS(new String[] { "RED_NETHER_BRICK" }), 
    RED_NETHER_BRICK_SLAB(new String[0]), 
    RED_NETHER_BRICK_STAIRS(new String[0]), 
    RED_NETHER_BRICK_WALL(new String[0]), 
    RED_SAND(1, new String[] { "SAND" }), 
    RED_SANDSTONE(new String[0]), 
    RED_SANDSTONE_SLAB(new String[] { "DOUBLE_STONE_SLAB2", "STONE_SLAB2" }), 
    RED_SANDSTONE_STAIRS(new String[0]), 
    RED_SANDSTONE_WALL(new String[0]), 
    RED_SHULKER_BOX(new String[0]), 
    RED_STAINED_GLASS(14, new String[] { "STAINED_GLASS" }), 
    RED_STAINED_GLASS_PANE(14, new String[] { "THIN_GLASS", "STAINED_GLASS_PANE" }), 
    RED_TERRACOTTA(14, new String[] { "STAINED_CLAY" }), 
    RED_TULIP(4, new String[] { "RED_ROSE" }), 
    RED_WALL_BANNER(1, new String[] { "WALL_BANNER" }), 
    RED_WOOL(14, new String[] { "WOOL" }), 
    REINFORCED_DEEPSLATE(new String[0]), 
    REPEATER(new String[] { "DIODE_BLOCK_ON", "DIODE_BLOCK_OFF", "DIODE" }), 
    REPEATING_COMMAND_BLOCK(new String[] { "COMMAND", "COMMAND_REPEATING" }), 
    RESIN_BLOCK(new String[0]), 
    RESIN_BRICK(new String[0]), 
    RESIN_BRICKS(new String[0]), 
    RESIN_BRICK_SLAB(new String[0]), 
    RESIN_BRICK_STAIRS(new String[0]), 
    RESIN_BRICK_WALL(new String[0]), 
    RESIN_CLUMP(new String[0]), 
    RESPAWN_ANCHOR(new String[0]), 
    RIB_ARMOR_TRIM_SMITHING_TEMPLATE(new String[0]), 
    ROOTED_DIRT(new String[0]), 
    ROSE_BUSH(4, new String[] { "DOUBLE_PLANT" }), 
    ROTTEN_FLESH(new String[0]), 
    SADDLE(new String[0]), 
    SALMON(1, new String[] { "RAW_FISH" }), 
    SALMON_BUCKET(new String[0]), 
    SALMON_SPAWN_EGG(new String[0]), 
    SAND(new String[0]), 
    SANDSTONE(new String[0]), 
    SANDSTONE_SLAB(1, new String[] { "DOUBLE_STEP", "STEP", "STONE_SLAB" }), 
    SANDSTONE_STAIRS(new String[0]), 
    SANDSTONE_WALL(new String[0]), 
    SCAFFOLDING(new String[0]), 
    SCRAPE_POTTERY_SHERD(new String[0]), 
    SCULK(new String[0]), 
    SCULK_CATALYST(new String[0]), 
    SCULK_SENSOR(new String[0]), 
    SCULK_SHRIEKER(new String[0]), 
    SCULK_VEIN(new String[0]), 
    @Deprecated
    SCUTE(new String[0]), 
    SEAGRASS(new String[0]), 
    SEA_LANTERN(new String[0]), 
    SEA_PICKLE(new String[0]), 
    SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE(new String[0]), 
    SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE(new String[0]), 
    SHEAF_POTTERY_SHERD(new String[0]), 
    SHEARS(new String[0]), 
    SHEEP_SPAWN_EGG(91, new String[] { "MONSTER_EGG" }), 
    SHELTER_POTTERY_SHERD(new String[0]), 
    SHIELD(new String[0]), 
    SHORT_DRY_GRASS(new String[0]), 
    SHORT_GRASS(1, new String[] { "GRASS", "LONG_GRASS" }), 
    SHROOMLIGHT(new String[0]), 
    SHULKER_BOX(new String[] { "PURPLE_SHULKER_BOX" }), 
    SHULKER_SHELL(new String[0]), 
    SHULKER_SPAWN_EGG(69, new String[] { "MONSTER_EGG" }), 
    SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE(new String[0]), 
    SILVERFISH_SPAWN_EGG(60, new String[] { "MONSTER_EGG" }), 
    SKELETON_HORSE_SPAWN_EGG(28, new String[] { "MONSTER_EGG" }), 
    SKELETON_SKULL(new String[] { "SKULL", "SKULL_ITEM" }), 
    SKELETON_SPAWN_EGG(51, new String[] { "MONSTER_EGG" }), 
    SKELETON_WALL_SKULL(new String[] { "SKULL", "SKULL_ITEM" }), 
    SKULL_BANNER_PATTERN(new String[0]), 
    SKULL_POTTERY_SHERD(new String[0]), 
    SLIME_BALL(new String[0]), 
    SLIME_BLOCK(new String[0]), 
    SLIME_SPAWN_EGG(55, new String[] { "MONSTER_EGG" }), 
    SMALL_AMETHYST_BUD(new String[0]), 
    SMALL_DRIPLEAF(new String[0]), 
    SMITHING_TABLE(new String[0]), 
    SMOKER(new String[0]), 
    SMOOTH_BASALT(new String[0]), 
    SMOOTH_QUARTZ(new String[0]), 
    SMOOTH_QUARTZ_SLAB(new String[0]), 
    SMOOTH_QUARTZ_STAIRS(new String[0]), 
    SMOOTH_RED_SANDSTONE(2, new String[] { "RED_SANDSTONE" }), 
    SMOOTH_RED_SANDSTONE_SLAB(new String[0]), 
    SMOOTH_RED_SANDSTONE_STAIRS(new String[0]), 
    SMOOTH_SANDSTONE(2, new String[] { "SANDSTONE" }), 
    SMOOTH_SANDSTONE_SLAB(new String[0]), 
    SMOOTH_SANDSTONE_STAIRS(new String[0]), 
    SMOOTH_STONE(new String[0]), 
    SMOOTH_STONE_SLAB(new String[0]), 
    SNIFFER_EGG(new String[0]), 
    SNIFFER_SPAWN_EGG(new String[0]), 
    SNORT_POTTERY_SHERD(new String[0]), 
    SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE(new String[0]), 
    SNOW(new String[0]), 
    SNOWBALL(new String[] { "SNOW_BALL" }), 
    SNOW_BLOCK(new String[0]), 
    SNOW_GOLEM_SPAWN_EGG(new String[0]), 
    SOUL_CAMPFIRE(new String[0]), 
    SOUL_FIRE(new String[0]), 
    SOUL_LANTERN(new String[0]), 
    SOUL_SAND(new String[0]), 
    SOUL_SOIL(new String[0]), 
    SOUL_TORCH(new String[0]), 
    SOUL_WALL_TORCH(new String[0]), 
    SPAWNER(new String[] { "MOB_SPAWNER" }), 
    SPECTRAL_ARROW(new String[0]), 
    SPIDER_EYE(new String[0]), 
    SPIDER_SPAWN_EGG(52, new String[] { "MONSTER_EGG" }), 
    SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE(new String[0]), 
    SPLASH_POTION(new String[] { "POTION" }), 
    SPONGE(new String[0]), 
    SPORE_BLOSSOM(new String[0]), 
    SPRUCE_BOAT(new String[] { "BOAT_SPRUCE" }), 
    SPRUCE_BUTTON(new String[0]), 
    SPRUCE_CHEST_BOAT(new String[0]), 
    SPRUCE_DOOR(new String[] { "SPRUCE_DOOR", "SPRUCE_DOOR_ITEM" }), 
    SPRUCE_FENCE(new String[0]), 
    SPRUCE_FENCE_GATE(new String[0]), 
    SPRUCE_HANGING_SIGN(new String[0]), 
    SPRUCE_LEAVES(1, new String[] { "LEAVES" }), 
    SPRUCE_LOG(1, new String[] { "LOG" }), 
    SPRUCE_PLANKS(1, new String[] { "WOOD" }), 
    SPRUCE_PRESSURE_PLATE(new String[0]), 
    SPRUCE_SAPLING(1, new String[] { "SAPLING" }), 
    SPRUCE_SIGN(new String[0]), 
    SPRUCE_SLAB(1, new String[] { "WOOD_DOUBLE_STEP", "WOOD_STEP", "WOODEN_SLAB" }), 
    SPRUCE_STAIRS(new String[] { "SPRUCE_WOOD_STAIRS" }), 
    SPRUCE_TRAPDOOR(new String[0]), 
    SPRUCE_WALL_HANGING_SIGN(new String[0]), 
    SPRUCE_WALL_SIGN(new String[] { "WALL_SIGN" }), 
    SPRUCE_WOOD(1, new String[] { "LOG" }), 
    SPYGLASS(new String[0]), 
    SQUID_SPAWN_EGG(94, new String[] { "MONSTER_EGG" }), 
    STICK(new String[0]), 
    STICKY_PISTON(new String[] { "PISTON_BASE", "PISTON_STICKY_BASE" }), 
    STONE(new String[0]), 
    STONECUTTER(new String[0]), 
    STONE_AXE(new String[0]), 
    STONE_BRICKS(new String[] { "SMOOTH_BRICK" }), 
    STONE_BRICK_SLAB(5, new String[] { "DOUBLE_STEP", "STEP", "STONE_SLAB" }), 
    STONE_BRICK_STAIRS(new String[] { "SMOOTH_STAIRS" }), 
    STONE_BRICK_WALL(new String[0]), 
    STONE_BUTTON(new String[0]), 
    STONE_HOE(new String[0]), 
    STONE_PICKAXE(new String[0]), 
    STONE_PRESSURE_PLATE(new String[] { "STONE_PLATE" }), 
    STONE_SHOVEL(new String[] { "STONE_SPADE" }), 
    STONE_SLAB(new String[] { "DOUBLE_STEP", "STEP" }), 
    STONE_STAIRS(new String[0]), 
    STONE_SWORD(new String[0]), 
    STRAY_SPAWN_EGG(6, new String[] { "MONSTER_EGG" }), 
    STRIDER_SPAWN_EGG(new String[0]), 
    STRING(new String[0]), 
    STRIPPED_ACACIA_LOG(new String[0]), 
    STRIPPED_ACACIA_WOOD(new String[0]), 
    STRIPPED_BAMBOO_BLOCK(new String[0]), 
    STRIPPED_BIRCH_LOG(new String[0]), 
    STRIPPED_BIRCH_WOOD(new String[0]), 
    STRIPPED_CHERRY_LOG(new String[0]), 
    STRIPPED_CHERRY_WOOD(new String[0]), 
    STRIPPED_CRIMSON_HYPHAE(new String[0]), 
    STRIPPED_CRIMSON_STEM(new String[0]), 
    STRIPPED_DARK_OAK_LOG(new String[0]), 
    STRIPPED_DARK_OAK_WOOD(new String[0]), 
    STRIPPED_JUNGLE_LOG(new String[0]), 
    STRIPPED_JUNGLE_WOOD(new String[0]), 
    STRIPPED_MANGROVE_LOG(new String[0]), 
    STRIPPED_MANGROVE_WOOD(new String[0]), 
    STRIPPED_OAK_LOG(new String[0]), 
    STRIPPED_OAK_WOOD(new String[0]), 
    STRIPPED_PALE_OAK_LOG(new String[0]), 
    STRIPPED_PALE_OAK_WOOD(new String[0]), 
    STRIPPED_SPRUCE_LOG(new String[0]), 
    STRIPPED_SPRUCE_WOOD(new String[0]), 
    STRIPPED_WARPED_HYPHAE(new String[0]), 
    STRIPPED_WARPED_STEM(new String[0]), 
    STRUCTURE_BLOCK(new String[0]), 
    STRUCTURE_VOID(10, new String[] { "BARRIER" }), 
    SUGAR(new String[0]), 
    SUGAR_CANE(new String[] { "SUGAR_CANE_BLOCK" }), 
    SUNFLOWER(new String[] { "DOUBLE_PLANT" }), 
    SUSPICIOUS_GRAVEL(new String[0]), 
    SUSPICIOUS_SAND(new String[0]), 
    SUSPICIOUS_STEW(new String[0]), 
    SWEET_BERRIES(new String[0]), 
    SWEET_BERRY_BUSH(new String[0]), 
    TADPOLE_BUCKET(new String[0]), 
    TADPOLE_SPAWN_EGG(new String[0]), 
    TALL_DRY_GRASS(new String[0]), 
    TALL_GRASS(2, new String[] { "DOUBLE_PLANT" }), 
    TALL_SEAGRASS(new String[0]), 
    TARGET(new String[0]), 
    TERRACOTTA(new String[] { "HARD_CLAY" }), 
    TEST_BLOCK(new String[0]), 
    TEST_INSTANCE_BLOCK(new String[0]), 
    TIDE_ARMOR_TRIM_SMITHING_TEMPLATE(new String[0]), 
    TINTED_GLASS(new String[0]), 
    TIPPED_ARROW(new String[0]), 
    TNT(new String[0]), 
    TNT_MINECART(new String[] { "EXPLOSIVE_MINECART" }), 
    TORCH(new String[0]), 
    TORCHFLOWER(new String[0]), 
    TORCHFLOWER_CROP(new String[0]), 
    TORCHFLOWER_SEEDS(new String[0]), 
    TOTEM_OF_UNDYING(new String[] { "TOTEM" }), 
    TRADER_LLAMA_SPAWN_EGG(new String[0]), 
    TRAPPED_CHEST(new String[0]), 
    TRIAL_KEY(new String[0]), 
    TRIAL_SPAWNER(new String[0]), 
    TRIDENT(new String[0]), 
    TRIPWIRE(new String[0]), 
    TRIPWIRE_HOOK(new String[0]), 
    TROPICAL_FISH(2, new String[] { "RAW_FISH" }), 
    TROPICAL_FISH_BUCKET(new String[] { "BUCKET", "WATER_BUCKET" }), 
    TROPICAL_FISH_SPAWN_EGG(new String[] { "MONSTER_EGG" }), 
    TUBE_CORAL(new String[0]), 
    TUBE_CORAL_BLOCK(new String[0]), 
    TUBE_CORAL_FAN(new String[0]), 
    TUBE_CORAL_WALL_FAN(new String[0]), 
    TUFF(new String[0]), 
    TUFF_BRICKS(new String[0]), 
    TUFF_BRICK_SLAB(new String[0]), 
    TUFF_BRICK_STAIRS(new String[0]), 
    TUFF_BRICK_WALL(new String[0]), 
    TUFF_SLAB(new String[0]), 
    TUFF_STAIRS(new String[0]), 
    TUFF_WALL(new String[0]), 
    TURTLE_EGG(new String[0]), 
    TURTLE_HELMET(new String[0]), 
    TURTLE_SCUTE(new String[0]), 
    TURTLE_SPAWN_EGG(new String[0]), 
    TWISTING_VINES(new String[0]), 
    TWISTING_VINES_PLANT(new String[0]), 
    VAULT(new String[0]), 
    VERDANT_FROGLIGHT(new String[0]), 
    VEX_ARMOR_TRIM_SMITHING_TEMPLATE(new String[0]), 
    VEX_SPAWN_EGG(35, new String[] { "MONSTER_EGG" }), 
    VILLAGER_SPAWN_EGG(120, new String[] { "MONSTER_EGG" }), 
    VINDICATOR_SPAWN_EGG(36, new String[] { "MONSTER_EGG" }), 
    VINE(new String[0]), 
    VOID_AIR(new String[] { "AIR" }), 
    WALL_TORCH(new String[] { "TORCH" }), 
    WANDERING_TRADER_SPAWN_EGG(new String[0]), 
    WARDEN_SPAWN_EGG(new String[0]), 
    WARD_ARMOR_TRIM_SMITHING_TEMPLATE(new String[0]), 
    WARPED_BUTTON(new String[0]), 
    WARPED_DOOR(new String[0]), 
    WARPED_FENCE(new String[0]), 
    WARPED_FENCE_GATE(new String[0]), 
    WARPED_FUNGUS(new String[0]), 
    WARPED_FUNGUS_ON_A_STICK(new String[0]), 
    WARPED_HANGING_SIGN(new String[0]), 
    WARPED_HYPHAE(new String[0]), 
    WARPED_NYLIUM(new String[0]), 
    WARPED_PLANKS(new String[0]), 
    WARPED_PRESSURE_PLATE(new String[0]), 
    WARPED_ROOTS(new String[0]), 
    WARPED_SIGN(new String[] { "SIGN_POST" }), 
    WARPED_SLAB(new String[0]), 
    WARPED_STAIRS(new String[0]), 
    WARPED_STEM(new String[0]), 
    WARPED_TRAPDOOR(new String[0]), 
    WARPED_WALL_HANGING_SIGN(new String[0]), 
    WARPED_WALL_SIGN(new String[] { "WALL_SIGN" }), 
    WARPED_WART_BLOCK(new String[0]), 
    WATER(new String[] { "STATIONARY_WATER" }), 
    WATER_BUCKET(new String[0]), 
    WATER_CAULDRON(new String[0]), 
    WAXED_CHISELED_COPPER(new String[0]), 
    WAXED_COPPER_BLOCK(new String[0]), 
    WAXED_COPPER_BULB(new String[0]), 
    WAXED_COPPER_DOOR(new String[0]), 
    WAXED_COPPER_GRATE(new String[0]), 
    WAXED_COPPER_TRAPDOOR(new String[0]), 
    WAXED_CUT_COPPER(new String[0]), 
    WAXED_CUT_COPPER_SLAB(new String[0]), 
    WAXED_CUT_COPPER_STAIRS(new String[0]), 
    WAXED_EXPOSED_CHISELED_COPPER(new String[0]), 
    WAXED_EXPOSED_COPPER(new String[0]), 
    WAXED_EXPOSED_COPPER_BULB(new String[0]), 
    WAXED_EXPOSED_COPPER_DOOR(new String[0]), 
    WAXED_EXPOSED_COPPER_GRATE(new String[0]), 
    WAXED_EXPOSED_COPPER_TRAPDOOR(new String[0]), 
    WAXED_EXPOSED_CUT_COPPER(new String[0]), 
    WAXED_EXPOSED_CUT_COPPER_SLAB(new String[0]), 
    WAXED_EXPOSED_CUT_COPPER_STAIRS(new String[0]), 
    WAXED_OXIDIZED_CHISELED_COPPER(new String[0]), 
    WAXED_OXIDIZED_COPPER(new String[0]), 
    WAXED_OXIDIZED_COPPER_BULB(new String[0]), 
    WAXED_OXIDIZED_COPPER_DOOR(new String[0]), 
    WAXED_OXIDIZED_COPPER_GRATE(new String[0]), 
    WAXED_OXIDIZED_COPPER_TRAPDOOR(new String[0]), 
    WAXED_OXIDIZED_CUT_COPPER(new String[0]), 
    WAXED_OXIDIZED_CUT_COPPER_SLAB(new String[0]), 
    WAXED_OXIDIZED_CUT_COPPER_STAIRS(new String[0]), 
    WAXED_WEATHERED_CHISELED_COPPER(new String[0]), 
    WAXED_WEATHERED_COPPER(new String[0]), 
    WAXED_WEATHERED_COPPER_BULB(new String[0]), 
    WAXED_WEATHERED_COPPER_DOOR(new String[0]), 
    WAXED_WEATHERED_COPPER_GRATE(new String[0]), 
    WAXED_WEATHERED_COPPER_TRAPDOOR(new String[0]), 
    WAXED_WEATHERED_CUT_COPPER(new String[0]), 
    WAXED_WEATHERED_CUT_COPPER_SLAB(new String[0]), 
    WAXED_WEATHERED_CUT_COPPER_STAIRS(new String[0]), 
    WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE(new String[0]), 
    WEATHERED_CHISELED_COPPER(new String[0]), 
    WEATHERED_COPPER(new String[0]), 
    WEATHERED_COPPER_BULB(new String[0]), 
    WEATHERED_COPPER_DOOR(new String[0]), 
    WEATHERED_COPPER_GRATE(new String[0]), 
    WEATHERED_COPPER_TRAPDOOR(new String[0]), 
    WEATHERED_CUT_COPPER(new String[0]), 
    WEATHERED_CUT_COPPER_SLAB(new String[0]), 
    WEATHERED_CUT_COPPER_STAIRS(new String[0]), 
    WEEPING_VINES(new String[0]), 
    WEEPING_VINES_PLANT(new String[0]), 
    WET_SPONGE(1, new String[] { "SPONGE" }), 
    WHEAT(new String[] { "CROPS" }), 
    WHEAT_SEEDS(new String[] { "SEEDS" }), 
    WHITE_BANNER(15, new String[] { "STANDING_BANNER", "BANNER" }), 
    WHITE_BED(new String[] { "BED_BLOCK", "BED" }), 
    WHITE_BUNDLE(new String[0]), 
    WHITE_CANDLE(new String[0]), 
    WHITE_CANDLE_CAKE(new String[0]), 
    WHITE_CARPET(new String[] { "CARPET" }), 
    WHITE_CONCRETE(new String[] { "CONCRETE" }), 
    WHITE_CONCRETE_POWDER(new String[] { "CONCRETE_POWDER" }), 
    WHITE_DYE(new String[0]), 
    WHITE_GLAZED_TERRACOTTA(new String[0]), 
    WHITE_HARNESS(new String[0]), 
    WHITE_SHULKER_BOX(new String[0]), 
    WHITE_STAINED_GLASS(new String[] { "STAINED_GLASS" }), 
    WHITE_STAINED_GLASS_PANE(new String[] { "THIN_GLASS", "STAINED_GLASS_PANE" }), 
    WHITE_TERRACOTTA(new String[] { "STAINED_CLAY" }), 
    WHITE_TULIP(6, new String[] { "RED_ROSE" }), 
    WHITE_WALL_BANNER(15, new String[] { "WALL_BANNER" }), 
    WHITE_WOOL(new String[] { "WOOL" }), 
    WILDFLOWERS(new String[0]), 
    WILD_ARMOR_TRIM_SMITHING_TEMPLATE(new String[0]), 
    WIND_CHARGE(new String[0]), 
    WITCH_SPAWN_EGG(66, new String[] { "MONSTER_EGG" }), 
    WITHER_ROSE(new String[0]), 
    WITHER_SKELETON_SKULL(1, new String[] { "SKULL", "SKULL_ITEM" }), 
    WITHER_SKELETON_SPAWN_EGG(5, new String[] { "MONSTER_EGG" }), 
    WITHER_SKELETON_WALL_SKULL(1, new String[] { "SKULL", "SKULL_ITEM" }), 
    WITHER_SPAWN_EGG(new String[0]), 
    WOLF_ARMOR(new String[0]), 
    WOLF_SPAWN_EGG(95, new String[] { "MONSTER_EGG" }), 
    WOODEN_AXE(new String[] { "WOOD_AXE" }), 
    WOODEN_HOE(new String[] { "WOOD_HOE" }), 
    WOODEN_PICKAXE(new String[] { "WOOD_PICKAXE" }), 
    WOODEN_SHOVEL(new String[] { "WOOD_SPADE" }), 
    WOODEN_SWORD(new String[] { "WOOD_SWORD" }), 
    WRITABLE_BOOK(new String[] { "BOOK_AND_QUILL" }), 
    WRITTEN_BOOK(new String[0]), 
    YELLOW_BANNER(11, new String[] { "STANDING_BANNER", "BANNER" }), 
    YELLOW_BED(supports(12) ? 4 : 0, new String[] { "BED_BLOCK", "BED" }), 
    YELLOW_BUNDLE(new String[0]), 
    YELLOW_CANDLE(new String[0]), 
    YELLOW_CANDLE_CAKE(new String[0]), 
    YELLOW_CARPET(4, new String[] { "CARPET" }), 
    YELLOW_CONCRETE(4, new String[] { "CONCRETE" }), 
    YELLOW_CONCRETE_POWDER(4, new String[] { "CONCRETE_POWDER" }), 
    YELLOW_DYE(11, new String[] { "INK_SACK", "DANDELION_YELLOW" }), 
    YELLOW_GLAZED_TERRACOTTA(new String[0]), 
    YELLOW_HARNESS(new String[0]), 
    YELLOW_SHULKER_BOX(new String[0]), 
    YELLOW_STAINED_GLASS(4, new String[] { "STAINED_GLASS" }), 
    YELLOW_STAINED_GLASS_PANE(4, new String[] { "THIN_GLASS", "STAINED_GLASS_PANE" }), 
    YELLOW_TERRACOTTA(4, new String[] { "STAINED_CLAY" }), 
    YELLOW_WALL_BANNER(11, new String[] { "WALL_BANNER" }), 
    YELLOW_WOOL(4, new String[] { "WOOL" }), 
    ZOGLIN_SPAWN_EGG(new String[0]), 
    ZOMBIE_HEAD(2, new String[] { "SKULL", "SKULL_ITEM" }), 
    ZOMBIE_HORSE_SPAWN_EGG(29, new String[] { "MONSTER_EGG" }), 
    ZOMBIE_SPAWN_EGG(54, new String[] { "MONSTER_EGG" }), 
    ZOMBIE_VILLAGER_SPAWN_EGG(27, new String[] { "MONSTER_EGG" }), 
    ZOMBIE_WALL_HEAD(2, new String[] { "SKULL", "SKULL_ITEM" }), 
    ZOMBIFIED_PIGLIN_SPAWN_EGG(57, new String[] { "MONSTER_EGG", "ZOMBIE_PIGMAN_SPAWN_EGG" });
    
    public static final XMaterial[] VALUES;
    private static final Map<String, XMaterial> NAMES;
    private static final Cache<String, XMaterial> NAME_CACHE;
    private static final byte MAX_DATA_VALUE = 120;
    private static final byte UNKNOWN_DATA_VALUE = -1;
    private static final short MAX_ID = 2267;
    private static final Set<String> DUPLICATED;
    private final byte data;
    @NotNull
    private final String[] legacy;
    @Nullable
    private final Material material;
    
    private XMaterial(final int data, final String[] legacy) {
        this.data = (byte)data;
        this.legacy = legacy;
        Material mat = null;
        if ((!Data.ISFLAT && this.isDuplicated()) || (mat = getExactMaterial(this.name())) == null) {
            for (int i = legacy.length - 1; i >= 0; --i) {
                mat = getExactMaterial(legacy[i]);
                if (mat != null) {
                    break;
                }
            }
        }
        this.material = mat;
    }
    
    private XMaterial(final String[] legacy) {
        this(0, legacy);
    }
    
    @NotNull
    private static Optional<XMaterial> getIfPresent(@NotNull final String name) {
        return (Optional<XMaterial>)Optional.ofNullable((Object)XMaterial.NAMES.get((Object)name));
    }
    
    public static int getVersion() {
        return Data.VERSION;
    }
    
    @Nullable
    private static XMaterial requestOldXMaterial(@NotNull final String name, final byte data) {
        final String holder = name + (int)data;
        final XMaterial cache = (XMaterial)XMaterial.NAME_CACHE.getIfPresent((Object)holder);
        if (cache != null) {
            return cache;
        }
        for (final XMaterial material : XMaterial.VALUES) {
            if ((data == -1 || data == material.data) && material.anyMatchLegacy(name)) {
                XMaterial.NAME_CACHE.put((Object)holder, (Object)material);
                return material;
            }
        }
        return null;
    }
    
    @Nullable
    private static Optional<XMaterial> matchXMaterialWithData(@NotNull final String name) {
        final int index = name.indexOf(58);
        if (index != -1) {
            final String mat = format(name.substring(0, index));
            try {
                final byte data = (byte)Integer.parseInt(name.substring(index + 1).replace((CharSequence)" ", (CharSequence)""));
                return (data >= 0 && data < 120) ? matchDefinedXMaterial(mat, data) : matchDefinedXMaterial(mat, (byte)(-1));
            }
            catch (final NumberFormatException ignored) {
                return (Optional<XMaterial>)Optional.empty();
            }
        }
        return null;
    }
    
    @NotNull
    public static Optional<XMaterial> matchXMaterial(@NotNull final String name) {
        if (name == null) {
            throw new IllegalArgumentException("Cannot match a material with null string");
        }
        final Optional<XMaterial> oldMatch = matchXMaterialWithData(name);
        return (oldMatch != null) ? oldMatch : matchDefinedXMaterial(format(name), (byte)(-1));
    }
    
    @NotNull
    public static XMaterial matchXMaterial(@NotNull final Material material) {
        Objects.requireNonNull((Object)material, "Cannot match null material");
        return (XMaterial)matchDefinedXMaterial(material.name(), (byte)(-1)).orElseThrow(() -> new IllegalArgumentException("Unsupported material with no data value: " + material.name()));
    }
    
    @NotNull
    public static XMaterial matchXMaterial(@NotNull final ItemStack item) {
        Objects.requireNonNull((Object)item, "Cannot match null ItemStack");
        String material = item.getType().name();
        final byte data = (byte)((Data.ISFLAT || material.equals((Object)"MAP") || item.getType().getMaxDurability() > 0) ? 0 : item.getDurability());
        if (supports(9) && !supports(13) && item.hasItemMeta() && material.equals((Object)"MONSTER_EGG")) {
            final ItemMeta meta = item.getItemMeta();
            if (meta instanceof SpawnEggMeta) {
                final SpawnEggMeta egg = (SpawnEggMeta)meta;
                final EntityType type = egg.getSpawnedType();
                if (type == null) {
                    return XMaterial.ZOMBIE_SPAWN_EGG;
                }
                material = egg.getSpawnedType().name() + "_SPAWN_EGG";
            }
        }
        if (!supports(9) && material.equals((Object)"POTION")) {
            final int damage = item.getDurability();
            return ((damage & 0x4000) > 0) ? XMaterial.SPLASH_POTION : XMaterial.POTION;
        }
        if (supports(13) && !supports(14)) {
            final String s = material;
            int n = -1;
            switch (s.hashCode()) {
                case -1311700911: {
                    if (s.equals((Object)"CACTUS_GREEN")) {
                        n = 0;
                        break;
                    }
                    break;
                }
                case 717727105: {
                    if (s.equals((Object)"ROSE_RED")) {
                        n = 1;
                        break;
                    }
                    break;
                }
                case 2089237253: {
                    if (s.equals((Object)"DANDELION_YELLOW")) {
                        n = 2;
                        break;
                    }
                    break;
                }
            }
            switch (n) {
                case 0: {
                    return XMaterial.GREEN_DYE;
                }
                case 1: {
                    return XMaterial.RED_DYE;
                }
                case 2: {
                    return XMaterial.YELLOW_DYE;
                }
            }
        }
        final Optional<XMaterial> result = matchDefinedXMaterial(material, data);
        if (result.isPresent()) {
            return (XMaterial)result.get();
        }
        throw new IllegalArgumentException("Unsupported material from item: " + material + " (" + (int)data + ')');
    }
    
    @NotNull
    protected static Optional<XMaterial> matchDefinedXMaterial(@NotNull final String name, final byte data) {
        Boolean duplicated = null;
        final boolean isAMap = name.equalsIgnoreCase("MAP");
        if (Data.ISFLAT || (!isAMap && data <= 0 && !(duplicated = isDuplicated(name)))) {
            final Optional<XMaterial> xMaterial = getIfPresent(name);
            if (xMaterial.isPresent()) {
                return xMaterial;
            }
        }
        final XMaterial oldXMaterial = requestOldXMaterial(name, data);
        if (oldXMaterial == null) {
            return (Optional<XMaterial>)((data >= 0 && isAMap) ? Optional.of((Object)XMaterial.FILLED_MAP) : Optional.empty());
        }
        final boolean isPlural = oldXMaterial == XMaterial.CARROTS || oldXMaterial == XMaterial.POTATOES || oldXMaterial == XMaterial.BRICKS;
        if (!Data.ISFLAT && isPlural) {
            if (duplicated == null) {
                if (!isDuplicated(name)) {
                    return (Optional<XMaterial>)Optional.of((Object)oldXMaterial);
                }
            }
            else if (!duplicated) {
                return (Optional<XMaterial>)Optional.of((Object)oldXMaterial);
            }
            return getIfPresent(name);
        }
        return (Optional<XMaterial>)Optional.of((Object)oldXMaterial);
    }
    
    @NotNull
    protected static String format(@NotNull final String name) {
        final int len = name.length();
        final char[] chs = new char[len];
        int count = 0;
        boolean appendUnderline = false;
        for (int i = 0; i < len; ++i) {
            final char ch = name.charAt(i);
            if (!appendUnderline && count != 0 && (ch == '-' || ch == ' ' || ch == '_') && chs[count] != '_') {
                appendUnderline = true;
            }
            else {
                boolean number = false;
                if ((ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z') || (number = (ch >= '0' && ch <= '9'))) {
                    if (appendUnderline) {
                        chs[count++] = '_';
                        appendUnderline = false;
                    }
                    if (number) {
                        chs[count++] = ch;
                    }
                    else {
                        chs[count++] = (char)(ch & '_');
                    }
                }
            }
        }
        return new String(chs, 0, count);
    }
    
    @ApiStatus.Internal
    public static boolean supports(final int version) {
        return Data.VERSION >= version;
    }
    
    public String[] getLegacy() {
        return this.legacy;
    }
    
    @NotNull
    public ItemStack setType(@NotNull final ItemStack item) {
        Objects.requireNonNull((Object)item, "Cannot set material for null ItemStack");
        final Material material = this.get();
        Objects.requireNonNull((Object)material, () -> "Unsupported material: " + this.name());
        item.setType(material);
        if (!Data.ISFLAT && material.getMaxDurability() <= 0) {
            item.setDurability((short)this.data);
        }
        if (!Data.ISFLAT && this == XMaterial.SPLASH_POTION) {
            item.setDurability((short)16384);
        }
        return item;
    }
    
    private boolean anyMatchLegacy(@NotNull final String name) {
        for (int i = this.legacy.length - 1; i >= 0; --i) {
            if (name.equals((Object)this.legacy[i])) {
                return true;
            }
        }
        return false;
    }
    
    @NotNull
    public String toString() {
        return (String)Arrays.stream((Object[])this.name().split("_")).map(t -> t.charAt(0) + t.substring(1).toLowerCase(Locale.ENGLISH)).collect(Collectors.joining((CharSequence)" "));
    }
    
    public int getId() {
        final Material material = this.get();
        if (material == null) {
            return -1;
        }
        try {
            return material.getId();
        }
        catch (final IllegalArgumentException ignored) {
            return -1;
        }
    }
    
    public byte getData() {
        return this.data;
    }
    
    @Nullable
    public ItemStack parseItem() {
        final Material material = this.get();
        if (material == null) {
            return null;
        }
        final ItemStack base = Data.ISFLAT ? new ItemStack(material) : new ItemStack(material, 1, (short)this.data);
        if (!Data.ISFLAT && this == XMaterial.SPLASH_POTION) {
            base.setDurability((short)16384);
        }
        if (supports(9) && !supports(13) && base.hasItemMeta() && this.name().endsWith("_SPAWN_EGG")) {
            final ItemMeta meta = base.getItemMeta();
            if (meta instanceof SpawnEggMeta) {
                final SpawnEggMeta egg = (SpawnEggMeta)meta;
                final String entityName = this.name();
                egg.setSpawnedType(((XEntityType)XEntityType.of(entityName.substring(0, entityName.length() - "_SPAWN_EGG".length())).orElse((Object)XEntityType.ZOMBIE)).get());
            }
        }
        return base;
    }
    
    @Deprecated
    @Nullable
    public Material parseMaterial() {
        return this.get();
    }
    
    public boolean isSimilar(@NotNull final ItemStack item) {
        Objects.requireNonNull((Object)item, "Cannot compare with null ItemStack");
        if (item.getType() != this.get()) {
            return false;
        }
        if (this == XMaterial.SPLASH_POTION) {
            return Data.ISFLAT || item.getDurability() == 16384;
        }
        return Data.ISFLAT || item.getDurability() == this.data || item.getType().getMaxDurability() > 0;
    }
    
    public String[] getNames() {
        return this.legacy;
    }
    
    @Nullable
    public Material get() {
        return this.material;
    }
    
    private static boolean isDuplicated(@NotNull final String name) {
        return XMaterial.DUPLICATED.contains((Object)name);
    }
    
    private boolean isDuplicated() {
        final String name = this.name();
        int n = -1;
        switch (name.hashCode()) {
            case 73242259: {
                if (name.equals((Object)"MELON")) {
                    n = 0;
                    break;
                }
                break;
            }
            case 1980706179: {
                if (name.equals((Object)"CARROT")) {
                    n = 1;
                    break;
                }
                break;
            }
            case -1929109465: {
                if (name.equals((Object)"POTATO")) {
                    n = 2;
                    break;
                }
                break;
            }
            case 68077974: {
                if (name.equals((Object)"GRASS")) {
                    n = 3;
                    break;
                }
                break;
            }
            case 63467553: {
                if (name.equals((Object)"BRICK")) {
                    n = 4;
                    break;
                }
                break;
            }
            case -328086150: {
                if (name.equals((Object)"NETHER_BRICK")) {
                    n = 5;
                    break;
                }
                break;
            }
            case -1722057187: {
                if (name.equals((Object)"DARK_OAK_DOOR")) {
                    n = 6;
                    break;
                }
                break;
            }
            case 478520881: {
                if (name.equals((Object)"ACACIA_DOOR")) {
                    n = 7;
                    break;
                }
                break;
            }
            case -519277571: {
                if (name.equals((Object)"BIRCH_DOOR")) {
                    n = 8;
                    break;
                }
                break;
            }
            case 1379814896: {
                if (name.equals((Object)"JUNGLE_DOOR")) {
                    n = 9;
                    break;
                }
                break;
            }
            case -333218805: {
                if (name.equals((Object)"SPRUCE_DOOR")) {
                    n = 10;
                    break;
                }
                break;
            }
            case 76092: {
                if (name.equals((Object)"MAP")) {
                    n = 11;
                    break;
                }
                break;
            }
            case 868145122: {
                if (name.equals((Object)"CAULDRON")) {
                    n = 12;
                    break;
                }
                break;
            }
            case 1545025079: {
                if (name.equals((Object)"BREWING_STAND")) {
                    n = 13;
                    break;
                }
                break;
            }
            case 1401892433: {
                if (name.equals((Object)"FLOWER_POT")) {
                    n = 14;
                    break;
                }
                break;
            }
        }
        switch (n) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14: {
                return true;
            }
            default: {
                return false;
            }
        }
    }
    
    static {
        VALUES = values();
        NAMES = (Map)new HashMap();
        NAME_CACHE = CacheBuilder.newBuilder().expireAfterAccess(1L, TimeUnit.HOURS).build();
        for (final XMaterial material : XMaterial.VALUES) {
            XMaterial.NAMES.put((Object)material.name(), (Object)material);
        }
        if (Data.ISFLAT) {
            DUPLICATED = null;
        }
        else {
            (DUPLICATED = (Set)new HashSet(4)).add((Object)"GRASS");
            XMaterial.DUPLICATED.add((Object)XMaterial.MELON.name());
            XMaterial.DUPLICATED.add((Object)XMaterial.BRICK.name());
            XMaterial.DUPLICATED.add((Object)XMaterial.NETHER_BRICK.name());
        }
    }
    
    @ApiStatus.Internal
    private static final class Data
    {
        private static final int VERSION;
        private static final Map<String, Material> BUKKIT_NAME_MAPPINGS;
        private static final boolean ISFLAT;
        
        private static Material getExactMaterial(final String name) {
            if (Data.BUKKIT_NAME_MAPPINGS != null) {
                return (Material)Data.BUKKIT_NAME_MAPPINGS.get((Object)name);
            }
            return Material.getMaterial(name);
        }
        
        static {
            if (Bukkit.getServer() == null) {
                System.err.println("Bukkit.getServer() in null. This should not happen when running a plugin normally");
                VERSION = 21;
            }
            else {
                final String version = Bukkit.getVersion();
                final Matcher matcher = Pattern.compile("MC: \\d\\.(\\d+)").matcher((CharSequence)version);
                if (!matcher.find()) {
                    throw new IllegalArgumentException("Failed to parse server version from: " + version);
                }
                VERSION = Integer.parseInt(matcher.group(1));
            }
            Map<String, Material> mapping;
            try {
                final Field field = Material.class.getDeclaredField("BY_NAME");
                field.setAccessible(true);
                mapping = (Map<String, Material>)field.get((Object)null);
            }
            catch (final Throwable e) {
                new IllegalStateException("Unable to get Material.BY_NAME field", e).printStackTrace();
                mapping = null;
            }
            BUKKIT_NAME_MAPPINGS = mapping;
            ISFLAT = XMaterial.supports(13);
        }
    }
}
