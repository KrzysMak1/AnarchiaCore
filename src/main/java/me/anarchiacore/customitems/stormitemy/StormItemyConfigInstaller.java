package me.anarchiacore.customitems.stormitemy;

import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Base64;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;

public class StormItemyConfigInstaller {
    private static final String RESOURCE_ROOT = "stormitemy/";
    private static final String TARGET_ROOT = "configs/STORMITEMY";
    private static final String ITEM_TARGET_ROOT = "configs/customitems";
    private static final String ITEMS_PREFIX = "items/";
    private static final String TRAP_SCHEM_BASE64 = "H4sIAAAAAAAAA5yTTW/TQBCGZ73xZxQJ8QM4wDUH4ISQcgARBFKrFiERaISqtT22V3V2o/VU4cQP5kdQdmNTO41Ea2yPvfP49ey7H04g/pxVuBEkMw7Td4LEFzSN1ApgFnMIbzPwPPBXMqcKuAfBB5RlRTCzzRNUpaXBFIKzomiQrJjb+GnjxMaTBKJTJJHb4hzi1bJVfb2xxyD/die/sOlv6+n9m9Wyd+FKJ+eiRiI8FT9sHicQdoDDbCMVZkYU9FpI08of90ynjcylcIUYh0f9i0ynO0zdKDk86/GukoSXqVAKzdpostOk1eLFy++uMIdPg8ri6rJAleEaRUMLMtc4V9pQ1TYbfW2bhagbnO8Eoal1WWL+l2D3ias74fD0PgfPndB/oIG2j9bBHt9jIHiAgVdOGHK4ODRARmxzrc26EJlU5cLVnVeiLhapJtKbud6i6vrc6h2a3sFdT66DCEKI39Y6u3Ib04JfbkUZMGYDusM2PcY4s/cO2ueEMZ+x4JDwAWlhOEhbwo+I/1+a/dldtwQONdERYT2JYbYf+VKRJIlN4uZjCvxcN93/xbrwIvA+5sPd3K4WHKm9UWo+Sj0ZpfZHqYNR6nCUOvqXOoZoOP8AfwAAAP//AwCt18hRMAUAAA==";
    private static final String DOM_SCHEM_BASE64 = "H4sIAAAAAAAAA3SPT0vEMBDFXxt2u0npzU8jXhQrgi4KgnW9yNBNt6HdVNo5+OnVSSsbPTiQP+/3XoaJgX6qW3skdrVCfk1Mz3ac3OCBQitkJ4U0xapye26xSrG+te7QMpRc760/BJpj/dA0k2UJqy8pOS9k7Qw2W8u0l+YKuiqX1IskPn/pHcK7qF8XP7+5rMo4RYiYR+ots93SR5jLIPsBCsXReVuP1PA5uXGJn0U2UPf23pPvJnESZNBX/VB34dsC7pAkiWCx4jaz/425TpHINIq5denZsbOTWeDmj8Y3AAAA//8DAGIlyjt/AQAA";

    private final Plugin plugin;

    public StormItemyConfigInstaller(Plugin plugin) {
        this.plugin = plugin;
    }

    public boolean isResourceSourceDirectory() {
        URL codeSource = plugin.getClass().getProtectionDomain().getCodeSource().getLocation();
        if (codeSource == null) {
            return false;
        }
        try {
            Path basePath = Paths.get(codeSource.toURI());
            return Files.isDirectory(basePath);
        } catch (URISyntaxException e) {
            plugin.getLogger().warning("Nie można odczytać ścieżki zasobów StormItemy: " + e.getMessage());
            return false;
        }
    }

    public int getMissingEventConfigCount() {
        File itemsRoot = new File(plugin.getDataFolder(), ITEM_TARGET_ROOT);
        return validateEventConfigs(itemsRoot).size();
    }

    public int installMissing() {
        File targetRoot = new File(plugin.getDataFolder(), TARGET_ROOT);
        File itemsRoot = new File(plugin.getDataFolder(), ITEM_TARGET_ROOT);
        List<String> resources = listResourcePaths();
        int copied = 0;
        for (String relativePath : resources) {
            File target = resolveTarget(targetRoot, itemsRoot, relativePath);
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
        ensureDatabaseFile(targetRoot);
        ensureSchematicFiles(itemsRoot);
        validateInstallation(targetRoot, itemsRoot, resources);
        return copied;
    }

    private void ensureDatabaseFile(File targetRoot) {
        File databaseFile = new File(targetRoot, "data.db");
        if (databaseFile.exists()) {
            return;
        }
        File parent = databaseFile.getParentFile();
        if (parent != null && !parent.exists() && !parent.mkdirs()) {
            plugin.getLogger().warning("Nie można utworzyć katalogu: " + parent.getAbsolutePath());
            return;
        }
        try {
            Files.createFile(databaseFile.toPath());
        } catch (IOException e) {
            plugin.getLogger().warning("Nie można utworzyć pliku bazy danych: " + e.getMessage());
        }
    }

    private void ensureSchematicFiles(File targetRoot) {
        Map<String, String> schematics = new LinkedHashMap<>();
        schematics.put("trapanarchia.schem", TRAP_SCHEM_BASE64);
        schematics.put("turbodomek.schem", DOM_SCHEM_BASE64);
        for (Map.Entry<String, String> entry : schematics.entrySet()) {
            File target = new File(targetRoot, entry.getKey());
            if (target.exists()) {
                continue;
            }
            File parent = target.getParentFile();
            if (parent != null && !parent.exists() && !parent.mkdirs()) {
                plugin.getLogger().warning("Nie można utworzyć katalogu: " + parent.getAbsolutePath());
                continue;
            }
            try {
                byte[] data = Base64.getDecoder().decode(entry.getValue());
                Files.write(target.toPath(), data);
            } catch (IllegalArgumentException | IOException e) {
                plugin.getLogger().warning("Nie można utworzyć schematu " + entry.getKey() + ": " + e.getMessage());
            }
        }
    }

    private void validateInstallation(File targetRoot, File itemsRoot, List<String> resources) {
        List<String> missingFiles = new ArrayList<>();
        for (String resource : resources) {
            File target = resolveTarget(targetRoot, itemsRoot, resource);
            if (!target.exists()) {
                missingFiles.add(resource);
            }
        }
        int total = resources.size();
        int done = total - missingFiles.size();
        plugin.getLogger().info("StormItemy configs installed: " + done + "/" + total
            + " (configs: " + targetRoot.getAbsolutePath() + ", items: " + itemsRoot.getAbsolutePath() + ")");
        if (done < total) {
            plugin.getLogger().severe("Brakuje plików StormItemy: " + String.join(", ", missingFiles));
        }
        if (!hasAnyFile(targetRoot)) {
            plugin.getLogger().severe("Folder StormItemy jest pusty: " + targetRoot.getAbsolutePath());
        }
        if (!hasAnyFile(itemsRoot)) {
            plugin.getLogger().severe("Folder eventowych itemów StormItemy jest pusty: " + itemsRoot.getAbsolutePath());
        }
        List<String> missingEvents = validateEventConfigs(itemsRoot);
        if (!missingEvents.isEmpty()) {
            plugin.getLogger().severe("Brakuje konfiguracji eventów StormItemy w "
                + itemsRoot.getAbsolutePath() + ": " + String.join(", ", missingEvents));
        }
    }

    private boolean hasAnyFile(File targetRoot) {
        if (!targetRoot.exists()) {
            return false;
        }
        try (Stream<Path> paths = Files.walk(targetRoot.toPath())) {
            return paths.anyMatch(Files::isRegularFile);
        } catch (IOException e) {
            plugin.getLogger().warning("Nie można sprawdzić plików StormItemy: " + e.getMessage());
            return false;
        }
    }

    private List<String> validateEventConfigs(File targetRoot) {
        Map<String, EventCheck> checks = new LinkedHashMap<>();
        checks.put("Bombarda Maxima", new EventCheck("bombardamaxima.yml", List.of("bombarda maxima", "bombarda")));
        checks.put("Turbo Trap", new EventCheck("turbotrap.yml", List.of("turbo-trap", "turbotrap", "turbo trap")));
        checks.put("Dynamit", new EventCheck("dynamit.yml", List.of("dynamit")));
        checks.put("Totem Ułaskawienia", new EventCheck("totemulaskawienia.yml", List.of("totem ułaskawienia", "totem ulaskawienia")));

        List<Path> ymlFiles = new ArrayList<>();
        if (targetRoot.exists()) {
            try (Stream<Path> paths = Files.walk(targetRoot.toPath())) {
                paths.filter(path -> path.toString().endsWith(".yml")).forEach(ymlFiles::add);
            } catch (IOException e) {
                plugin.getLogger().warning("Nie można odczytać plików yml StormItemy: " + e.getMessage());
            }
        }

        List<String> missing = new ArrayList<>();
        for (Map.Entry<String, EventCheck> entry : checks.entrySet()) {
            String eventName = entry.getKey();
            EventCheck check = entry.getValue();
            File expectedFile = new File(targetRoot, check.relativePath());
            if (expectedFile.exists()) {
                continue;
            }
            if (!hasEventInContents(ymlFiles, check.tokens())) {
                missing.add(eventName);
            }
        }
        return missing;
    }

    private boolean hasEventInContents(List<Path> ymlFiles, List<String> tokens) {
        for (Path path : ymlFiles) {
            String content;
            try {
                content = Files.readString(path, StandardCharsets.UTF_8).toLowerCase();
            } catch (IOException e) {
                plugin.getLogger().warning("Nie można odczytać pliku StormItemy: " + path + ": " + e.getMessage());
                continue;
            }
            for (String token : tokens) {
                if (content.contains(token)) {
                    return true;
                }
            }
        }
        return false;
    }

    private record EventCheck(String relativePath, List<String> tokens) {}

    private List<String> listResourcePaths() {
        URL codeSource = plugin.getClass().getProtectionDomain().getCodeSource().getLocation();
        if (codeSource == null) {
            return Collections.emptyList();
        }
        try {
            Path basePath = Paths.get(codeSource.toURI());
            if (Files.isDirectory(basePath)) {
            return filterBinaryResources(listFromDirectory(basePath));
        }
        return filterBinaryResources(listFromJar(basePath));
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

    private List<String> filterBinaryResources(List<String> resources) {
        return resources.stream()
            .filter(path -> !isBinaryResource(path))
            .sorted()
            .toList();
    }

    private boolean isBinaryResource(String relativePath) {
        return relativePath.endsWith("data.db") || relativePath.endsWith(".schem");
    }

    private File resolveTarget(File configRoot, File itemsRoot, String relativePath) {
        if (relativePath.startsWith(ITEMS_PREFIX)) {
            String trimmed = relativePath.substring(ITEMS_PREFIX.length());
            return new File(itemsRoot, trimmed);
        }
        return new File(configRoot, relativePath);
    }
}
