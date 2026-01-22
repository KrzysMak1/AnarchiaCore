package me.anarchiacore.util;

import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class ResourceExporter {
    private final Plugin plugin;

    public ResourceExporter(Plugin plugin) {
        this.plugin = plugin;
    }

    public void exportAlways(String resourcePath, String outputName) {
        File dataFolder = plugin.getDataFolder();
        if (!dataFolder.exists() && !dataFolder.mkdirs()) {
            plugin.getLogger().warning("Nie można utworzyć folderu danych pluginu.");
            return;
        }
        File outputFile = new File(dataFolder, outputName);
        try (InputStream input = plugin.getResource(resourcePath)) {
            if (input == null) {
                plugin.getLogger().warning("Nie znaleziono zasobu: " + resourcePath);
                return;
            }
            Files.copy(input, outputFile.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            plugin.getLogger().warning("Nie można zapisać zasobu " + resourcePath + ": " + e.getMessage());
        }
    }
}
