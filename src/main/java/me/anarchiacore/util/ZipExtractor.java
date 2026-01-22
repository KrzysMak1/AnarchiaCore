package me.anarchiacore.util;

import org.bukkit.plugin.Plugin;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipExtractor {
    private final Plugin plugin;

    public ZipExtractor(Plugin plugin) {
        this.plugin = plugin;
    }

    public void extractAlways(String resourcePath, File outputDir) {
        if (!outputDir.exists() && !outputDir.mkdirs()) {
            plugin.getLogger().warning("Nie można utworzyć katalogu: " + outputDir.getAbsolutePath());
            return;
        }
        try (InputStream input = plugin.getResource(resourcePath)) {
            if (input == null) {
                plugin.getLogger().warning("Nie znaleziono zasobu zip: " + resourcePath);
                return;
            }
            try (ZipInputStream zipInput = new ZipInputStream(new BufferedInputStream(input))) {
                ZipEntry entry;
                while ((entry = zipInput.getNextEntry()) != null) {
                    File target = new File(outputDir, entry.getName());
                    if (entry.isDirectory()) {
                        if (!target.exists() && !target.mkdirs()) {
                            plugin.getLogger().warning("Nie można utworzyć katalogu: " + target.getAbsolutePath());
                        }
                        continue;
                    }
                    File parent = target.getParentFile();
                    if (parent != null && !parent.exists() && !parent.mkdirs()) {
                        plugin.getLogger().warning("Nie można utworzyć katalogu: " + parent.getAbsolutePath());
                        continue;
                    }
                    try (FileOutputStream outputStream = new FileOutputStream(target)) {
                        zipInput.transferTo(outputStream);
                    }
                }
            }
        } catch (IOException e) {
            plugin.getLogger().warning("Nie można wypakować zipa " + resourcePath + ": " + e.getMessage());
        }
    }
}
