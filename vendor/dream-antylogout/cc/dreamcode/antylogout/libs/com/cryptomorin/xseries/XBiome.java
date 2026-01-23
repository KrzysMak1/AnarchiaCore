package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries;

import java.util.function.BiFunction;
import java.util.function.Supplier;
import org.bukkit.Registry;
import org.bukkit.block.Block;
import java.util.function.Function;
import java.util.Collection;
import org.bukkit.Location;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import org.jetbrains.annotations.NotNull;
import org.bukkit.Chunk;
import java.util.Optional;
import org.jetbrains.annotations.Nullable;
import org.bukkit.World;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.base.XRegistry;
import org.bukkit.block.Biome;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.base.XModule;

public final class XBiome extends XModule<XBiome, Biome>
{
    public static final XRegistry<XBiome, Biome> REGISTRY;
    public static final XBiome WINDSWEPT_HILLS;
    public static final XBiome SNOWY_PLAINS;
    public static final XBiome SPARSE_JUNGLE;
    public static final XBiome STONY_SHORE;
    public static final XBiome CHERRY_GROVE;
    public static final XBiome PALE_GARDEN;
    public static final XBiome OLD_GROWTH_PINE_TAIGA;
    public static final XBiome WINDSWEPT_FOREST;
    public static final XBiome WOODED_BADLANDS;
    public static final XBiome WINDSWEPT_GRAVELLY_HILLS;
    public static final XBiome OLD_GROWTH_BIRCH_FOREST;
    public static final XBiome OLD_GROWTH_SPRUCE_TAIGA;
    public static final XBiome WINDSWEPT_SAVANNA;
    public static final XBiome MEADOW;
    public static final XBiome MANGROVE_SWAMP;
    public static final XBiome DEEP_DARK;
    public static final XBiome GROVE;
    public static final XBiome SNOWY_SLOPES;
    public static final XBiome FROZEN_PEAKS;
    public static final XBiome JAGGED_PEAKS;
    public static final XBiome STONY_PEAKS;
    public static final XBiome BADLANDS;
    public static final XBiome BADLANDS_PLATEAU;
    public static final XBiome BEACH;
    public static final XBiome BIRCH_FOREST;
    public static final XBiome BIRCH_FOREST_HILLS;
    public static final XBiome COLD_OCEAN;
    public static final XBiome DARK_FOREST;
    public static final XBiome DARK_FOREST_HILLS;
    public static final XBiome DEEP_COLD_OCEAN;
    public static final XBiome DEEP_FROZEN_OCEAN;
    public static final XBiome DEEP_LUKEWARM_OCEAN;
    public static final XBiome DEEP_OCEAN;
    public static final XBiome DEEP_WARM_OCEAN;
    public static final XBiome DESERT;
    public static final XBiome DESERT_HILLS;
    public static final XBiome DESERT_LAKES;
    public static final XBiome END_BARRENS;
    public static final XBiome END_HIGHLANDS;
    public static final XBiome END_MIDLANDS;
    public static final XBiome ERODED_BADLANDS;
    public static final XBiome FLOWER_FOREST;
    public static final XBiome FOREST;
    public static final XBiome FROZEN_OCEAN;
    public static final XBiome FROZEN_RIVER;
    public static final XBiome GIANT_SPRUCE_TAIGA;
    public static final XBiome GIANT_SPRUCE_TAIGA_HILLS;
    public static final XBiome GIANT_TREE_TAIGA;
    public static final XBiome GIANT_TREE_TAIGA_HILLS;
    public static final XBiome ICE_SPIKES;
    public static final XBiome JUNGLE;
    public static final XBiome JUNGLE_HILLS;
    public static final XBiome LUKEWARM_OCEAN;
    public static final XBiome MODIFIED_BADLANDS_PLATEAU;
    public static final XBiome MODIFIED_GRAVELLY_MOUNTAINS;
    public static final XBiome MODIFIED_JUNGLE;
    public static final XBiome MODIFIED_JUNGLE_EDGE;
    public static final XBiome MODIFIED_WOODED_BADLANDS_PLATEAU;
    public static final XBiome MOUNTAIN_EDGE;
    public static final XBiome MUSHROOM_FIELDS;
    public static final XBiome MUSHROOM_FIELD_SHORE;
    public static final XBiome SOUL_SAND_VALLEY;
    public static final XBiome CRIMSON_FOREST;
    public static final XBiome WARPED_FOREST;
    public static final XBiome BASALT_DELTAS;
    public static final XBiome NETHER_WASTES;
    public static final XBiome OCEAN;
    public static final XBiome PLAINS;
    public static final XBiome RIVER;
    public static final XBiome SAVANNA;
    public static final XBiome SAVANNA_PLATEAU;
    public static final XBiome SHATTERED_SAVANNA_PLATEAU;
    public static final XBiome SMALL_END_ISLANDS;
    public static final XBiome SNOWY_BEACH;
    public static final XBiome SNOWY_MOUNTAINS;
    public static final XBiome SNOWY_TAIGA;
    public static final XBiome SNOWY_TAIGA_HILLS;
    public static final XBiome SNOWY_TAIGA_MOUNTAINS;
    public static final XBiome SUNFLOWER_PLAINS;
    public static final XBiome SWAMP;
    public static final XBiome SWAMP_HILLS;
    public static final XBiome TAIGA;
    public static final XBiome TAIGA_HILLS;
    public static final XBiome TAIGA_MOUNTAINS;
    public static final XBiome CUSTOM;
    public static final XBiome TALL_BIRCH_FOREST;
    public static final XBiome TALL_BIRCH_HILLS;
    public static final XBiome THE_END;
    public static final XBiome THE_VOID;
    public static final XBiome WARM_OCEAN;
    public static final XBiome WOODED_BADLANDS_PLATEAU;
    public static final XBiome WOODED_HILLS;
    public static final XBiome WOODED_MOUNTAINS;
    public static final XBiome BAMBOO_JUNGLE;
    public static final XBiome BAMBOO_JUNGLE_HILLS;
    public static final XBiome DRIPSTONE_CAVES;
    public static final XBiome LUSH_CAVES;
    private static final boolean World_getMaxHeight$SUPPORTED;
    private static final boolean World_getMinHeight$SUPPORTED;
    @Nullable
    private final World.Environment environment;
    
    public XBiome(final World.Environment environment, final Biome biome, final String[] names) {
        super(biome, names);
        this.environment = environment;
    }
    
    private XBiome(final Biome biome, final String[] names) {
        this(null, biome, names);
    }
    
    public Optional<World.Environment> getEnvironment() {
        return (Optional<World.Environment>)Optional.ofNullable((Object)this.environment);
    }
    
    @Deprecated
    @Nullable
    public Biome getBiome() {
        return ((XModule<XForm, Biome>)this).get();
    }
    
    @NotNull
    public CompletableFuture<Void> setBiome(@NotNull final Chunk chunk) {
        final Biome biome = ((XModule<XForm, Biome>)this).get();
        Objects.requireNonNull((Object)biome, () -> "Unsupported biome: " + this.name());
        Objects.requireNonNull((Object)chunk, "Cannot set biome of null chunk");
        if (!chunk.isLoaded() && !chunk.load(true)) {
            throw new IllegalStateException("Could not load chunk at " + chunk.getX() + ", " + chunk.getZ());
        }
        final int heightMax = XBiome.World_getMaxHeight$SUPPORTED ? chunk.getWorld().getMaxHeight() : 1;
        final int heightMin = XBiome.World_getMinHeight$SUPPORTED ? chunk.getWorld().getMinHeight() : 0;
        return (CompletableFuture<Void>)CompletableFuture.runAsync(() -> {
            for (int x = 0; x < 16; ++x) {
                for (int y = heightMin; y < heightMax; y += 4) {
                    for (int z = 0; z < 16; ++z) {
                        final Block block = chunk.getBlock(x, y, z);
                        if (block.getBiome() != biome) {
                            block.setBiome(biome);
                        }
                    }
                }
            }
        }).exceptionally(result -> {
            result.printStackTrace();
            return null;
        });
    }
    
    @NotNull
    public CompletableFuture<Void> setBiome(@NotNull final Location start, @NotNull final Location end) {
        final Biome biome = ((XModule<XForm, Biome>)this).get();
        Objects.requireNonNull((Object)start, "Start location cannot be null");
        Objects.requireNonNull((Object)end, "End location cannot be null");
        Objects.requireNonNull((Object)biome, () -> "Unsupported biome: " + this.name());
        final World world = start.getWorld();
        if (!world.getUID().equals((Object)end.getWorld().getUID())) {
            throw new IllegalArgumentException("Location worlds mismatch");
        }
        final int heightMax = XBiome.World_getMaxHeight$SUPPORTED ? world.getMaxHeight() : 1;
        final int heightMin = XBiome.World_getMinHeight$SUPPORTED ? world.getMinHeight() : 0;
        return (CompletableFuture<Void>)CompletableFuture.runAsync(() -> {
            for (int x = start.getBlockX(); x < end.getBlockX(); ++x) {
                for (int y = heightMin; y < heightMax; y += 4) {
                    for (int z = start.getBlockZ(); z < end.getBlockZ(); ++z) {
                        final Block block = new Location(world, (double)x, (double)y, (double)z).getBlock();
                        if (block.getBiome() != biome) {
                            block.setBiome(biome);
                        }
                    }
                }
            }
        }).exceptionally(result -> {
            result.printStackTrace();
            return null;
        });
    }
    
    @NotNull
    public static XBiome of(@NotNull final Biome biome) {
        return XBiome.REGISTRY.getByBukkitForm(biome);
    }
    
    public static Optional<XBiome> of(@NotNull final String biome) {
        return XBiome.REGISTRY.getByName(biome);
    }
    
    @Deprecated
    public static XBiome[] values() {
        return XBiome.REGISTRY.values();
    }
    
    @NotNull
    public static Collection<XBiome> getValues() {
        return XBiome.REGISTRY.getValues();
    }
    
    @NotNull
    private static XBiome std(@NotNull final World.Environment environment, @NotNull final String... names) {
        return XBiome.REGISTRY.std((java.util.function.Function<Biome, XBiome>)(bukkit -> new XBiome(environment, bukkit, names)), names);
    }
    
    @NotNull
    private static XBiome std(@Nullable final XBiome newVersion, @NotNull final String... names) {
        return XBiome.REGISTRY.std((java.util.function.Function<Biome, XBiome>)(bukkit -> new XBiome(null, bukkit, names)), newVersion, names);
    }
    
    @NotNull
    private static XBiome std(@NotNull final String... names) {
        return XBiome.REGISTRY.std((java.util.function.Function<Biome, XBiome>)(bukkit -> new XBiome(World.Environment.NORMAL, bukkit, names)), names);
    }
    
    static {
        REGISTRY = new XRegistry<XBiome, Biome>(Biome.class, XBiome.class, (Supplier<Object>)(() -> Registry.BIOME), (java.util.function.BiFunction<Biome, String[], XBiome>)XBiome::new, (java.util.function.Function<Integer, XBiome[]>)(x$0 -> new XBiome[x$0]));
        WINDSWEPT_HILLS = std("WINDSWEPT_HILLS", "MOUNTAINS", "EXTREME_HILLS");
        SNOWY_PLAINS = std("SNOWY_PLAINS", "SNOWY_TUNDRA", "ICE_FLATS", "ICE_PLAINS");
        SPARSE_JUNGLE = std("SPARSE_JUNGLE", "JUNGLE_EDGE", "JUNGLE_EDGE");
        STONY_SHORE = std("STONY_SHORE", "STONE_SHORE", "STONE_BEACH");
        CHERRY_GROVE = std("CHERRY_GROVE");
        PALE_GARDEN = std("PALE_GARDEN");
        OLD_GROWTH_PINE_TAIGA = std("OLD_GROWTH_PINE_TAIGA", "GIANT_TREE_TAIGA", "REDWOOD_TAIGA", "MEGA_TAIGA");
        WINDSWEPT_FOREST = std("WINDSWEPT_FOREST", "WOODED_MOUNTAINS", "EXTREME_HILLS_WITH_TREES", "EXTREME_HILLS_PLUS");
        WOODED_BADLANDS = std("WOODED_BADLANDS", "WOODED_BADLANDS_PLATEAU", "MESA_ROCK", "MESA_PLATEAU_FOREST");
        WINDSWEPT_GRAVELLY_HILLS = std("WINDSWEPT_GRAVELLY_HILLS", "GRAVELLY_MOUNTAINS", "MUTATED_EXTREME_HILLS", "EXTREME_HILLS_MOUNTAINS");
        OLD_GROWTH_BIRCH_FOREST = std("OLD_GROWTH_BIRCH_FOREST", "TALL_BIRCH_FOREST", "MUTATED_BIRCH_FOREST", "BIRCH_FOREST_MOUNTAINS");
        OLD_GROWTH_SPRUCE_TAIGA = std("OLD_GROWTH_SPRUCE_TAIGA", "GIANT_SPRUCE_TAIGA", "MUTATED_REDWOOD_TAIGA", "MEGA_SPRUCE_TAIGA");
        WINDSWEPT_SAVANNA = std("WINDSWEPT_SAVANNA", "SHATTERED_SAVANNA", "MUTATED_SAVANNA", "SAVANNA_MOUNTAINS");
        MEADOW = std("MEADOW");
        MANGROVE_SWAMP = std("MANGROVE_SWAMP");
        DEEP_DARK = std("DEEP_DARK");
        GROVE = std("GROVE");
        SNOWY_SLOPES = std("SNOWY_SLOPES");
        FROZEN_PEAKS = std("FROZEN_PEAKS");
        JAGGED_PEAKS = std("JAGGED_PEAKS");
        STONY_PEAKS = std("STONY_PEAKS");
        BADLANDS = std("BADLANDS", "MESA");
        BADLANDS_PLATEAU = std(XBiome.WOODED_BADLANDS, "BADLANDS_PLATEAU", "MESA_CLEAR_ROCK", "MESA_PLATEAU");
        BEACH = std("BEACH", "BEACHES");
        BIRCH_FOREST = std(XBiome.OLD_GROWTH_BIRCH_FOREST, "BIRCH_FOREST");
        BIRCH_FOREST_HILLS = std(XBiome.OLD_GROWTH_BIRCH_FOREST, "BIRCH_FOREST_HILLS");
        COLD_OCEAN = std("COLD_OCEAN");
        DARK_FOREST = std("DARK_FOREST", "ROOFED_FOREST");
        DARK_FOREST_HILLS = std("DARK_FOREST_HILLS", "MUTATED_ROOFED_FOREST", "ROOFED_FOREST_MOUNTAINS");
        DEEP_COLD_OCEAN = std("DEEP_COLD_OCEAN", "COLD_DEEP_OCEAN");
        DEEP_FROZEN_OCEAN = std("DEEP_FROZEN_OCEAN", "FROZEN_DEEP_OCEAN");
        DEEP_LUKEWARM_OCEAN = std("DEEP_LUKEWARM_OCEAN", "LUKEWARM_DEEP_OCEAN");
        DEEP_OCEAN = std("DEEP_OCEAN");
        DEEP_WARM_OCEAN = std("DEEP_WARM_OCEAN", "WARM_DEEP_OCEAN");
        DESERT = std("DESERT");
        DESERT_HILLS = std("DESERT_HILLS");
        DESERT_LAKES = std("DESERT_LAKES", "MUTATED_DESERT", "DESERT_MOUNTAINS");
        END_BARRENS = std(World.Environment.THE_END, "END_BARRENS", "SKY_ISLAND_BARREN");
        END_HIGHLANDS = std(World.Environment.THE_END, "END_HIGHLANDS", "SKY_ISLAND_HIGH");
        END_MIDLANDS = std(World.Environment.THE_END, "END_MIDLANDS", "SKY_ISLAND_MEDIUM");
        ERODED_BADLANDS = std("ERODED_BADLANDS", "MUTATED_MESA", "MESA_BRYCE");
        FLOWER_FOREST = std("FLOWER_FOREST", "MUTATED_FOREST");
        FOREST = std("FOREST");
        FROZEN_OCEAN = std("FROZEN_OCEAN");
        FROZEN_RIVER = std("FROZEN_RIVER");
        GIANT_SPRUCE_TAIGA = std(XBiome.OLD_GROWTH_SPRUCE_TAIGA, "GIANT_SPRUCE_TAIGA", "MUTATED_REDWOOD_TAIGA", "MEGA_SPRUCE_TAIGA");
        GIANT_SPRUCE_TAIGA_HILLS = std(XBiome.OLD_GROWTH_SPRUCE_TAIGA, "GIANT_SPRUCE_TAIGA_HILLS", "MUTATED_REDWOOD_TAIGA_HILLS", "MEGA_SPRUCE_TAIGA_HILLS");
        GIANT_TREE_TAIGA = std(XBiome.OLD_GROWTH_PINE_TAIGA, "GIANT_TREE_TAIGA", "REDWOOD_TAIGA", "MEGA_TAIGA");
        GIANT_TREE_TAIGA_HILLS = std(XBiome.OLD_GROWTH_PINE_TAIGA, "GIANT_TREE_TAIGA_HILLS", "REDWOOD_TAIGA_HILLS", "MEGA_TAIGA_HILLS");
        ICE_SPIKES = std("ICE_SPIKES", "MUTATED_ICE_FLATS", "ICE_PLAINS_SPIKES");
        JUNGLE = std("JUNGLE");
        JUNGLE_HILLS = std("JUNGLE_HILLS");
        LUKEWARM_OCEAN = std("LUKEWARM_OCEAN");
        MODIFIED_BADLANDS_PLATEAU = std(XBiome.WOODED_BADLANDS, "MODIFIED_BADLANDS_PLATEAU", "MUTATED_MESA_CLEAR_ROCK", "MESA_PLATEAU");
        MODIFIED_GRAVELLY_MOUNTAINS = std(XBiome.WINDSWEPT_GRAVELLY_HILLS, "MODIFIED_GRAVELLY_MOUNTAINS", "MUTATED_EXTREME_HILLS_WITH_TREES", "EXTREME_HILLS_MOUNTAINS");
        MODIFIED_JUNGLE = std("MODIFIED_JUNGLE", "MUTATED_JUNGLE", "JUNGLE_MOUNTAINS");
        MODIFIED_JUNGLE_EDGE = std(XBiome.SPARSE_JUNGLE, "MODIFIED_JUNGLE_EDGE", "MUTATED_JUNGLE_EDGE", "JUNGLE_EDGE_MOUNTAINS");
        MODIFIED_WOODED_BADLANDS_PLATEAU = std(XBiome.WOODED_BADLANDS, "MODIFIED_WOODED_BADLANDS_PLATEAU", "MUTATED_MESA_ROCK", "MESA_PLATEAU_FOREST_MOUNTAINS");
        MOUNTAIN_EDGE = std(XBiome.SPARSE_JUNGLE, "MOUNTAIN_EDGE", "SMALLER_EXTREME_HILLS");
        MUSHROOM_FIELDS = std("MUSHROOM_FIELDS", "MUSHROOM_ISLAND");
        MUSHROOM_FIELD_SHORE = std(XBiome.STONY_SHORE, "MUSHROOM_FIELD_SHORE", "MUSHROOM_ISLAND_SHORE", "MUSHROOM_SHORE");
        SOUL_SAND_VALLEY = std(World.Environment.NETHER, "SOUL_SAND_VALLEY");
        CRIMSON_FOREST = std(World.Environment.NETHER, "CRIMSON_FOREST");
        WARPED_FOREST = std(World.Environment.NETHER, "WARPED_FOREST");
        BASALT_DELTAS = std(World.Environment.NETHER, "BASALT_DELTAS");
        NETHER_WASTES = std(World.Environment.NETHER, "NETHER_WASTES", "NETHER", "HELL");
        OCEAN = std("OCEAN");
        PLAINS = std("PLAINS");
        RIVER = std("RIVER");
        SAVANNA = std("SAVANNA");
        SAVANNA_PLATEAU = std(XBiome.WINDSWEPT_SAVANNA, "SAVANNA_ROCK", "SAVANNA_PLATEAU");
        SHATTERED_SAVANNA_PLATEAU = std(XBiome.WINDSWEPT_SAVANNA, "SHATTERED_SAVANNA_PLATEAU", "MUTATED_SAVANNA_ROCK", "SAVANNA_PLATEAU_MOUNTAINS");
        SMALL_END_ISLANDS = std(World.Environment.THE_END, "SMALL_END_ISLANDS", "SKY_ISLAND_LOW");
        SNOWY_BEACH = std("SNOWY_BEACH", "COLD_BEACH");
        SNOWY_MOUNTAINS = std(XBiome.WINDSWEPT_HILLS, "SNOWY_MOUNTAINS", "ICE_MOUNTAINS");
        SNOWY_TAIGA = std("SNOWY_TAIGA", "TAIGA_COLD", "COLD_TAIGA");
        SNOWY_TAIGA_HILLS = std("SNOWY_TAIGA_HILLS", "TAIGA_COLD_HILLS", "COLD_TAIGA_HILLS");
        SNOWY_TAIGA_MOUNTAINS = std(XBiome.WINDSWEPT_FOREST, "SNOWY_TAIGA_MOUNTAINS", "MUTATED_TAIGA_COLD", "COLD_TAIGA_MOUNTAINS");
        SUNFLOWER_PLAINS = std("SUNFLOWER_PLAINS", "MUTATED_PLAINS");
        SWAMP = std("SWAMP", "SWAMPLAND");
        SWAMP_HILLS = std("SWAMP_HILLS", "MUTATED_SWAMPLAND", "SWAMPLAND_MOUNTAINS");
        TAIGA = std("TAIGA");
        TAIGA_HILLS = std("TAIGA_HILLS");
        TAIGA_MOUNTAINS = std(XBiome.WINDSWEPT_FOREST, "TAIGA_MOUNTAINS", "MUTATED_TAIGA");
        CUSTOM = std("CUSTOM");
        TALL_BIRCH_FOREST = std(XBiome.OLD_GROWTH_BIRCH_FOREST, "TALL_BIRCH_FOREST", "MUTATED_BIRCH_FOREST", "BIRCH_FOREST_MOUNTAINS");
        TALL_BIRCH_HILLS = std(XBiome.OLD_GROWTH_BIRCH_FOREST, "TALL_BIRCH_HILLS", "MUTATED_BIRCH_FOREST_HILLS", "MESA_PLATEAU_FOREST_MOUNTAINS");
        THE_END = std(World.Environment.THE_END, "THE_END", "SKY");
        THE_VOID = std("THE_VOID", "VOID");
        WARM_OCEAN = std("WARM_OCEAN");
        WOODED_BADLANDS_PLATEAU = std("WOODED_BADLANDS_PLATEAU", "MESA_ROCK", "MESA_PLATEAU_FOREST");
        WOODED_HILLS = std("WOODED_HILLS", "FOREST_HILLS");
        WOODED_MOUNTAINS = std("WOODED_MOUNTAINS", "EXTREME_HILLS_WITH_TREES", "EXTREME_HILLS_PLUS");
        BAMBOO_JUNGLE = std("BAMBOO_JUNGLE");
        BAMBOO_JUNGLE_HILLS = std("BAMBOO_JUNGLE_HILLS");
        DRIPSTONE_CAVES = std("DRIPSTONE_CAVES");
        LUSH_CAVES = std("LUSH_CAVES");
        boolean maxHeight = false;
        boolean minHeight = false;
        try {
            World.class.getMethod("getMaxHeight", (Class<?>[])new Class[0]);
            maxHeight = true;
        }
        catch (final Exception ex) {}
        try {
            World.class.getMethod("getMinHeight", (Class<?>[])new Class[0]);
            minHeight = true;
        }
        catch (final Exception ex2) {}
        World_getMaxHeight$SUPPORTED = maxHeight;
        World_getMinHeight$SUPPORTED = minHeight;
        XBiome.REGISTRY.discardMetadata();
    }
}
