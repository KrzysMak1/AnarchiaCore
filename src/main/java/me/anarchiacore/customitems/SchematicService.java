package me.anarchiacore.customitems;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

public class SchematicService {
    private final Plugin plugin;
    private final Map<String, CachedSchematic> cache = new ConcurrentHashMap<>();
    private final Random random = new Random();

    public SchematicService(Plugin plugin) {
        this.plugin = plugin;
    }

    public void clear() {
        cache.clear();
    }

    public boolean paste(String path, Location origin) {
        return pasteInternal(path, origin, false, true, null);
    }

    public boolean pasteIgnoringAir(String path, Location origin, Predicate<Block> blockedPredicate) {
        return pasteInternal(path, origin, true, false, blockedPredicate);
    }

    public boolean pasteAnimated(String path, Location origin, AnimationOptions options, Predicate<Block> blockedPredicate) {
        if (origin == null || origin.getWorld() == null) {
            return false;
        }
        SchematicData data = getSchematicData(path);
        if (data == null) {
            return false;
        }
        if (blockedPredicate != null && hasBlockedBlocks(origin, data, blockedPredicate)) {
            return false;
        }
        int minDelay = Math.max(0, options.minDelayMs());
        int maxDelay = Math.max(minDelay, options.maxDelayMs());
        int delayTicks = Math.max(1, (minDelay + random.nextInt(maxDelay - minDelay + 1)) / 50);
        new BukkitRunnable() {
            private int layer = 0;

            @Override
            public void run() {
                if (layer >= data.dimY()) {
                    cancel();
                    return;
                }
                try {
                    Object worldEdit = Class.forName("com.sk89q.worldedit.WorldEdit").getMethod("getInstance").invoke(null);
                    Object editSessionFactory = worldEdit.getClass().getMethod("getEditSessionFactory").invoke(worldEdit);
                    Object adaptedWorld = Class.forName("com.sk89q.worldedit.bukkit.BukkitAdapter")
                        .getMethod("adapt", org.bukkit.World.class)
                        .invoke(null, origin.getWorld());
                    Object editSession = editSessionFactory.getClass()
                        .getMethod("getEditSession", Class.forName("com.sk89q.worldedit.world.World"), int.class)
                        .invoke(editSessionFactory, adaptedWorld, -1);
                    Object minVector = Class.forName("com.sk89q.worldedit.math.BlockVector3")
                        .getMethod("at", int.class, int.class, int.class)
                        .invoke(null, data.minX(), data.minY(), data.minZ());
                    int offsetX = data.originX() - data.minX();
                    int offsetY = data.originY() - data.minY();
                    int offsetZ = data.originZ() - data.minZ();
                    for (int x = 0; x < data.dimX(); x++) {
                        for (int z = 0; z < data.dimZ(); z++) {
                            Object blockVector = Class.forName("com.sk89q.worldedit.math.BlockVector3")
                                .getMethod("at", int.class, int.class, int.class)
                                .invoke(null, x, layer, z);
                            Object clipboardVector = minVector.getClass()
                                .getMethod("add", blockVector.getClass())
                                .invoke(minVector, blockVector);
                            Object baseBlock = data.clipboard().getClass()
                                .getMethod("getFullBlock", clipboardVector.getClass())
                                .invoke(data.clipboard(), clipboardVector);
                            Object blockType = baseBlock.getClass().getMethod("getBlockType").invoke(baseBlock);
                            Object material = blockType.getClass().getMethod("getMaterial").invoke(blockType);
                            boolean isAir = (boolean) material.getClass().getMethod("isAir").invoke(material);
                            if (isAir) {
                                continue;
                            }
                            int targetX = origin.getBlockX() + x - offsetX;
                            int targetY = origin.getBlockY() + layer - offsetY;
                            int targetZ = origin.getBlockZ() + z - offsetZ;
                            Block targetBlock = origin.getWorld().getBlockAt(targetX, targetY, targetZ);
                            if (blockedPredicate != null && blockedPredicate.test(targetBlock)) {
                                continue;
                            }
                            Object targetVector = Class.forName("com.sk89q.worldedit.math.BlockVector3")
                                .getMethod("at", int.class, int.class, int.class)
                                .invoke(null, targetX, targetY, targetZ);
                            editSession.getClass()
                                .getMethod("setBlock", targetVector.getClass(), Class.forName("com.sk89q.worldedit.world.block.BlockStateHolder"))
                                .invoke(editSession, targetVector, baseBlock);
                        }
                    }
                    editSession.getClass().getMethod("flushSession").invoke(editSession);
                    editSession.getClass().getMethod("close").invoke(editSession);
                    if (options.particles()) {
                        Location particleLocation = new Location(origin.getWorld(),
                            origin.getBlockX() + data.dimX() / 2.0,
                            origin.getBlockY() + layer + 0.5,
                            origin.getBlockZ() + data.dimZ() / 2.0);
                        origin.getWorld().spawnParticle(Particle.CLOUD, particleLocation, 20, data.dimX() / 2.0, 0.5, data.dimZ() / 2.0, 0.01);
                    }
                    if (options.soundEnabled() && options.sound() != null && !options.sound().isBlank()) {
                        try {
                            Sound sound = Sound.valueOf(options.sound());
                            Location soundLocation = new Location(origin.getWorld(),
                                origin.getBlockX() + data.dimX() / 2.0,
                                origin.getBlockY() + layer,
                                origin.getBlockZ() + data.dimZ() / 2.0);
                            origin.getWorld().playSound(soundLocation, sound, options.volume(), options.pitch() + (random.nextFloat() * 0.4f - 0.2f));
                        } catch (IllegalArgumentException ignored) {
                        }
                    }
                } catch (Exception e) {
                    plugin.getLogger().warning("Nie można postawić struktury: " + e.getMessage());
                    cancel();
                }
                layer++;
            }
        }.runTaskTimer(plugin, 0L, delayTicks);
        return true;
    }

    public SchematicData getSchematicData(String path) {
        if (path == null || path.isBlank()) {
            return null;
        }
        File schematicFile = new File(plugin.getDataFolder(), path);
        if (!schematicFile.exists()) {
            return null;
        }
        CachedSchematic cached = loadSchematic(path, schematicFile);
        return cached != null ? cached.data : null;
    }

    private boolean pasteInternal(String path, Location origin, boolean ignoreAirBlocks, boolean requireEmpty, Predicate<Block> blockedPredicate) {
        if (origin == null || origin.getWorld() == null) {
            return false;
        }
        SchematicData data = getSchematicData(path);
        if (data == null) {
            return false;
        }
        if (blockedPredicate != null && hasBlockedBlocks(origin, data, blockedPredicate)) {
            return false;
        }
        if (requireEmpty && !canPaste(data, origin)) {
            return false;
        }
        try {
            Object worldEdit = Class.forName("com.sk89q.worldedit.WorldEdit").getMethod("getInstance").invoke(null);
            Object editSessionFactory = worldEdit.getClass().getMethod("getEditSessionFactory").invoke(worldEdit);
            Object adaptedWorld = Class.forName("com.sk89q.worldedit.bukkit.BukkitAdapter")
                    .getMethod("adapt", org.bukkit.World.class)
                    .invoke(null, origin.getWorld());
            Object editSession = editSessionFactory.getClass()
                    .getMethod("getEditSession", Class.forName("com.sk89q.worldedit.world.World"), int.class)
                    .invoke(editSessionFactory, adaptedWorld, -1);
            Object originVector = Class.forName("com.sk89q.worldedit.math.BlockVector3")
                    .getMethod("at", int.class, int.class, int.class)
                    .invoke(null, origin.getBlockX(), origin.getBlockY(), origin.getBlockZ());
            Object pasteBuilder = data.clipboard().getClass()
                    .getMethod("createPaste", Class.forName("com.sk89q.worldedit.EditSession"))
                    .invoke(data.clipboard(), editSession);
            pasteBuilder.getClass().getMethod("to", Class.forName("com.sk89q.worldedit.math.BlockVector3"))
                    .invoke(pasteBuilder, originVector);
            pasteBuilder.getClass().getMethod("ignoreAirBlocks", boolean.class).invoke(pasteBuilder, ignoreAirBlocks);
            Object operation = pasteBuilder.getClass().getMethod("build").invoke(pasteBuilder);
            Class.forName("com.sk89q.worldedit.function.operation.Operations")
                    .getMethod("complete", Class.forName("com.sk89q.worldedit.function.operation.Operation"))
                    .invoke(null, operation);
            editSession.getClass().getMethod("close").invoke(editSession);
            return true;
        } catch (Exception e) {
            plugin.getLogger().warning("Nie można postawić struktury: " + e.getMessage());
            return false;
        }
    }

    private CachedSchematic loadSchematic(String path, File schematicFile) {
        long lastModified = schematicFile.lastModified();
        CachedSchematic cached = cache.get(path);
        if (cached != null && cached.lastModified == lastModified) {
            return cached;
        }
        try {
            Class<?> clipboardFormats = Class.forName("com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats");
            Object format = clipboardFormats.getMethod("findByFile", File.class).invoke(null, schematicFile);
            if (format == null) {
                return null;
            }
            Object clipboard;
            try (InputStream inputStream = new FileInputStream(schematicFile)) {
                Object reader = format.getClass().getMethod("getReader", InputStream.class).invoke(format, inputStream);
                clipboard = reader.getClass().getMethod("read").invoke(reader);
            }
            if (clipboard == null) {
                return null;
            }
            Object region = clipboard.getClass().getMethod("getRegion").invoke(clipboard);
            Object minimum = region.getClass().getMethod("getMinimumPoint").invoke(region);
            Object maximum = region.getClass().getMethod("getMaximumPoint").invoke(region);
            int minX = (int) minimum.getClass().getMethod("getBlockX").invoke(minimum);
            int minY = (int) minimum.getClass().getMethod("getBlockY").invoke(minimum);
            int minZ = (int) minimum.getClass().getMethod("getBlockZ").invoke(minimum);
            int maxX = (int) maximum.getClass().getMethod("getBlockX").invoke(maximum);
            int maxY = (int) maximum.getClass().getMethod("getBlockY").invoke(maximum);
            int maxZ = (int) maximum.getClass().getMethod("getBlockZ").invoke(maximum);
            Object dimensions = clipboard.getClass().getMethod("getDimensions").invoke(clipboard);
            int dimX = (int) dimensions.getClass().getMethod("getBlockX").invoke(dimensions);
            int dimY = (int) dimensions.getClass().getMethod("getBlockY").invoke(dimensions);
            int dimZ = (int) dimensions.getClass().getMethod("getBlockZ").invoke(dimensions);
            Object origin = clipboard.getClass().getMethod("getOrigin").invoke(clipboard);
            int originX = (int) origin.getClass().getMethod("getBlockX").invoke(origin);
            int originY = (int) origin.getClass().getMethod("getBlockY").invoke(origin);
            int originZ = (int) origin.getClass().getMethod("getBlockZ").invoke(origin);
            Object minimumPoint = clipboard.getClass().getMethod("getMinimumPoint").invoke(clipboard);
            int minPointX = (int) minimumPoint.getClass().getMethod("getBlockX").invoke(minimumPoint);
            int minPointY = (int) minimumPoint.getClass().getMethod("getBlockY").invoke(minimumPoint);
            int minPointZ = (int) minimumPoint.getClass().getMethod("getBlockZ").invoke(minimumPoint);
            SchematicData data = new SchematicData(clipboard, dimX, dimY, dimZ, originX, originY, originZ, minPointX, minPointY, minPointZ, minX, minY, minZ, maxX, maxY, maxZ);
            CachedSchematic refreshed = new CachedSchematic(data, lastModified);
            cache.put(path, refreshed);
            return refreshed;
        } catch (Exception e) {
            plugin.getLogger().warning("Nie można wczytać schematu: " + e.getMessage());
            return null;
        }
    }

    private boolean canPaste(SchematicData data, Location origin) {
        for (int x = data.regionMinX(); x <= data.regionMaxX(); x++) {
            for (int y = data.regionMinY(); y <= data.regionMaxY(); y++) {
                for (int z = data.regionMinZ(); z <= data.regionMaxZ(); z++) {
                    if (!origin.getWorld().getBlockAt(origin.getBlockX() + x, origin.getBlockY() + y, origin.getBlockZ() + z).isEmpty()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean hasBlockedBlocks(Location origin, SchematicData data, Predicate<Block> blockedPredicate) {
        int offsetX = data.originX() - data.minX();
        int offsetY = data.originY() - data.minY();
        int offsetZ = data.originZ() - data.minZ();
        for (int x = 0; x < data.dimX(); x++) {
            for (int y = 0; y < data.dimY(); y++) {
                for (int z = 0; z < data.dimZ(); z++) {
                    Block block = origin.getWorld().getBlockAt(
                        origin.getBlockX() + x - offsetX,
                        origin.getBlockY() + y - offsetY,
                        origin.getBlockZ() + z - offsetZ
                    );
                    if (blockedPredicate.test(block)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public record AnimationOptions(int minDelayMs, int maxDelayMs, boolean particles, boolean soundEnabled, String sound, float volume, float pitch) {
    }

    public record SchematicData(Object clipboard, int dimX, int dimY, int dimZ, int originX, int originY, int originZ,
                                int minX, int minY, int minZ, int regionMinX, int regionMinY, int regionMinZ,
                                int regionMaxX, int regionMaxY, int regionMaxZ) {
    }

    private record CachedSchematic(SchematicData data, long lastModified) {
    }
}
