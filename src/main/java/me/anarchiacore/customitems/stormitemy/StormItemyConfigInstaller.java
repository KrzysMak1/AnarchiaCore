package me.anarchiacore.customitems.stormitemy;

import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;

public class StormItemyConfigInstaller {
    private static final String RESOURCE_ROOT = "stormitemy/";
    private static final String TARGET_ROOT = "configs/STORMITEMY";

    private final Plugin plugin;

    public StormItemyConfigInstaller(Plugin plugin) {
        this.plugin = plugin;
    }

    public int installMissing() {
        List<String> resources = listResourcePaths();
        if (resources.isEmpty()) {
            return 0;
        }
        File targetRoot = new File(plugin.getDataFolder(), TARGET_ROOT);
        int copied = 0;
        for (String relativePath : resources) {
            File target = new File(targetRoot, relativePath);
            if (target.exists()) {
                continue;
            }
            File parent = target.getParentFile();
            if (parent != null && !parent.exists() && !parent.mkdirs()) {
                plugin.getLogger().warning("Nie można utworzyć katalogu: " + parent.getAbsolutePath());
                continue;
            }
            String resourcePath = RESOURCE_ROOT + relativePath;
            try (InputStream input = plugin.getResource(resourcePath)) {
                if (input == null) {
                    plugin.getLogger().warning("Nie znaleziono zasobu: " + resourcePath);
                    continue;
                }
                Files.copy(input, target.toPath());
                copied++;
            } catch (IOException e) {
                plugin.getLogger().warning("Nie można skopiować zasobu " + resourcePath + ": " + e.getMessage());
            }
        }
        return copied;
    }

    private List<String> listResourcePaths() {
        URL codeSource = plugin.getClass().getProtectionDomain().getCodeSource().getLocation();
        if (codeSource == null) {
            return Collections.emptyList();
        }
        try {
            Path basePath = Paths.get(codeSource.toURI());
            if (Files.isDirectory(basePath)) {
                return listFromDirectory(basePath);
            }
            return listFromJar(basePath);
        } catch (URISyntaxException | IOException e) {
            plugin.getLogger().warning("Nie można odczytać zasobów StormItemy: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    private List<String> listFromDirectory(Path basePath) throws IOException {
        Path resourceRoot = basePath.resolve(RESOURCE_ROOT);
        if (!Files.exists(resourceRoot)) {
            return Collections.emptyList();
        }
        try (Stream<Path> stream = Files.walk(resourceRoot)) {
            return stream
                .filter(Files::isRegularFile)
                .map(path -> resourceRoot.relativize(path).toString().replace('\\', '/'))
                .sorted()
                .toList();
        }
    }

    private List<String> listFromJar(Path jarPath) throws IOException {
        List<String> resources = new ArrayList<>();
        try (JarFile jarFile = new JarFile(jarPath.toFile())) {
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (entry.isDirectory()) {
                    continue;
                }
                String name = entry.getName();
                if (!name.startsWith(RESOURCE_ROOT)) {
                    continue;
                }
                String relative = name.substring(RESOURCE_ROOT.length());
                if (relative.isBlank()) {
                    continue;
                }
                resources.add(relative);
            }
        }
        resources.sort(String::compareTo);
        return resources;
    }
}
