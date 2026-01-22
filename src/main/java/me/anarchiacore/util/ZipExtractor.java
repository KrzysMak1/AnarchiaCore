package me.anarchiacore.util;

import org.bukkit.plugin.Plugin;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipExtractor {
    private final Plugin plugin;

    public ZipExtractor(Plugin plugin) {
        this.plugin = plugin;
    }

    public void extractAlways(String resourcePath, File outputDir) {
        if (!shouldExtract(outputDir)) {
            return;
        }
        if (!outputDir.exists() && !outputDir.mkdirs()) {
            plugin.getLogger().warning("Nie można utworzyć katalogu: " + outputDir.getAbsolutePath());
            return;
        }
        plugin.getLogger().info("Rozpakowywanie zipa z: " + resourcePath);
        plugin.getLogger().info("Docelowy katalog: " + outputDir.getAbsolutePath());
        int extractedFiles = 0;
        try (InputStream input = plugin.getResource(resourcePath)) {
            if (input == null) {
                plugin.getLogger().severe("Nie znaleziono zasobu zip: " + resourcePath);
                return;
            }
            try (ZipInputStream zipInput = new ZipInputStream(new BufferedInputStream(input))) {
                ZipEntry entry;
                while ((entry = zipInput.getNextEntry()) != null) {
                    String entryName = stripLeadingFolder(entry.getName(), outputDir.getName());
                    if (entryName.isBlank()) {
                        continue;
                    }
                    File target = new File(outputDir, entryName);
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
                    extractedFiles++;
                }
            }
            writeMarker(outputDir);
            plugin.getLogger().info("Wypakowano plików: " + extractedFiles);
        } catch (IOException e) {
            plugin.getLogger().warning("Nie można wypakować zipa " + resourcePath + ": " + e.getMessage());
        }
    }

    private boolean shouldExtract(File outputDir) {
        File marker = new File(outputDir, ".extracted");
        if (!marker.exists()) {
            return true;
        }
        return !hasAnyYaml(outputDir);
    }

    private boolean hasAnyYaml(File outputDir) {
        if (!outputDir.exists()) {
            return false;
        }
        try (Stream<Path> paths = Files.walk(outputDir.toPath())) {
            return paths.anyMatch(path -> path.toString().endsWith(".yml"));
        } catch (IOException e) {
            plugin.getLogger().warning("Nie można sprawdzić plików yml w: " + outputDir.getAbsolutePath());
            return false;
        }
    }

    private void writeMarker(File outputDir) throws IOException {
        File marker = new File(outputDir, ".extracted");
        if (!marker.exists() && !marker.createNewFile()) {
            plugin.getLogger().warning("Nie można utworzyć markera: " + marker.getAbsolutePath());
        }
    }

    private String stripLeadingFolder(String entryName, String folderName) {
        String prefix = folderName + "/";
        if (entryName.startsWith(prefix)) {
            return entryName.substring(prefix.length());
        }
        return entryName;
    }
}
