package me.anarchiacore.customitems;

import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SchematicService {
    private final Plugin plugin;
    private final Map<String, CachedSchematic> cache = new ConcurrentHashMap<>();

    public SchematicService(Plugin plugin) {
        this.plugin = plugin;
    }

    public void clear() {
        cache.clear();
    }

    public boolean paste(String path, Location origin) {
        if (origin == null || origin.getWorld() == null) {
            return false;
        }
        File schematicFile = new File(plugin.getDataFolder(), path);
        if (!schematicFile.exists()) {
            return false;
        }
        CachedSchematic cached = loadSchematic(path, schematicFile);
        if (cached == null) {
            return false;
        }
        if (!canPaste(cached, origin)) {
            return false;
        }
        try {
            Class<?> worldEdit = Class.forName("com.sk89q.worldedit.WorldEdit");
            Object instance = worldEdit.getMethod("getInstance").invoke(null);
            Object editSessionFactory = instance.getClass().getMethod("getEditSessionFactory").invoke(instance);
            Object adaptedWorld = Class.forName("com.sk89q.worldedit.bukkit.BukkitAdapter")
                    .getMethod("adapt", org.bukkit.World.class)
                    .invoke(null, origin.getWorld());
            Object editSession = editSessionFactory.getClass()
                    .getMethod("getEditSession", Class.forName("com.sk89q.worldedit.world.World"), int.class)
                    .invoke(editSessionFactory, adaptedWorld, -1);
            Object originVector = Class.forName("com.sk89q.worldedit.math.BlockVector3")
                    .getMethod("at", int.class, int.class, int.class)
                    .invoke(null, origin.getBlockX(), origin.getBlockY(), origin.getBlockZ());
            Object pasteBuilder = cached.clipboard.getClass()
                    .getMethod("createPaste", Class.forName("com.sk89q.worldedit.EditSession"))
                    .invoke(cached.clipboard, editSession);
            pasteBuilder.getClass().getMethod("to", Class.forName("com.sk89q.worldedit.math.BlockVector3"))
                    .invoke(pasteBuilder, originVector);
            pasteBuilder.getClass().getMethod("ignoreAirBlocks", boolean.class).invoke(pasteBuilder, false);
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
            CachedSchematic refreshed = new CachedSchematic(clipboard, new Bounds(minX, minY, minZ, maxX, maxY, maxZ), lastModified);
            cache.put(path, refreshed);
            return refreshed;
        } catch (Exception e) {
            plugin.getLogger().warning("Nie można wczytać schematu: " + e.getMessage());
            return null;
        }
    }

    private boolean canPaste(CachedSchematic cached, Location origin) {
        Bounds bounds = cached.bounds;
        for (int x = bounds.minX(); x <= bounds.maxX(); x++) {
            for (int y = bounds.minY(); y <= bounds.maxY(); y++) {
                for (int z = bounds.minZ(); z <= bounds.maxZ(); z++) {
                    if (!origin.getWorld().getBlockAt(origin.getBlockX() + x, origin.getBlockY() + y, origin.getBlockZ() + z).isEmpty()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private record CachedSchematic(Object clipboard, Bounds bounds, long lastModified) {
    }

    private record Bounds(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
    }
}
