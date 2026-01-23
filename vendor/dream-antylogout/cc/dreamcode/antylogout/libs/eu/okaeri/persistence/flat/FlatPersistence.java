package cc.dreamcode.antylogout.libs.eu.okaeri.persistence.flat;

import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.document.Document;
import java.util.HashSet;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.logging.Level;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.nio.file.FileVisitOption;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.Iterator;
import java.nio.file.LinkOption;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.ConfigManager;
import java.util.Collection;
import java.util.ArrayList;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.PersistenceEntity;
import java.util.stream.Stream;
import java.util.stream.Collectors;
import java.util.Objects;
import java.nio.file.Files;
import java.util.function.Predicate;
import java.util.Collections;
import java.util.Set;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.document.index.IndexProperty;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.PersistenceCollection;
import java.util.concurrent.ConcurrentHashMap;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.raw.PersistenceIndexMode;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.raw.PersistencePropertyMode;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.configurer.InMemoryConfigurer;
import lombok.NonNull;
import java.io.File;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.document.ConfigurerProvider;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.PersistencePath;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.document.index.InMemoryIndex;
import java.util.Map;
import java.nio.file.Path;
import java.util.function.Function;
import java.util.logging.Logger;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.raw.RawPersistence;

public class FlatPersistence extends RawPersistence
{
    private static final boolean DEBUG;
    private static final Logger LOGGER;
    private final Function<Path, String> fileToKeyMapper;
    private final Map<String, Map<String, InMemoryIndex>> indexMap;
    private final PersistencePath basePath;
    private final String fileSuffix;
    private final ConfigurerProvider indexProvider;
    private boolean saveIndex;
    
    public FlatPersistence(@NonNull final File basePath, @NonNull final String fileSuffix) {
        this(basePath, fileSuffix, InMemoryConfigurer::new, false);
        if (basePath == null) {
            throw new NullPointerException("basePath is marked non-null but is null");
        }
        if (fileSuffix == null) {
            throw new NullPointerException("fileSuffix is marked non-null but is null");
        }
    }
    
    public FlatPersistence(@NonNull final File basePath, @NonNull final String fileSuffix, @NonNull final ConfigurerProvider indexProvider) {
        this(basePath, fileSuffix, indexProvider, true);
        if (basePath == null) {
            throw new NullPointerException("basePath is marked non-null but is null");
        }
        if (fileSuffix == null) {
            throw new NullPointerException("fileSuffix is marked non-null but is null");
        }
        if (indexProvider == null) {
            throw new NullPointerException("indexProvider is marked non-null but is null");
        }
    }
    
    public FlatPersistence(@NonNull final File basePath, @NonNull final String fileSuffix, @NonNull final ConfigurerProvider indexProvider, final boolean saveIndex) {
        super(PersistencePath.of(basePath), PersistencePropertyMode.TOSTRING, PersistenceIndexMode.EMULATED);
        this.fileToKeyMapper = (Function<Path, String>)(path -> {
            final String name = path.getFileName().toString();
            return name.substring(0, name.length() - this.getFileSuffix().length());
        });
        this.indexMap = (Map<String, Map<String, InMemoryIndex>>)new ConcurrentHashMap();
        if (basePath == null) {
            throw new NullPointerException("basePath is marked non-null but is null");
        }
        if (fileSuffix == null) {
            throw new NullPointerException("fileSuffix is marked non-null but is null");
        }
        if (indexProvider == null) {
            throw new NullPointerException("indexProvider is marked non-null but is null");
        }
        this.basePath = PersistencePath.of(basePath);
        this.fileSuffix = fileSuffix;
        this.indexProvider = indexProvider;
        this.saveIndex = saveIndex;
    }
    
    @Override
    public void flush() {
        if (!this.isSaveIndex()) {
            return;
        }
        this.indexMap.forEach((collection, indexes) -> indexes.values().forEach(Document::save));
    }
    
    @Override
    public boolean updateIndex(@NonNull final PersistenceCollection collection, @NonNull final PersistencePath path, @NonNull final IndexProperty property, final String identity) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        if (path == null) {
            throw new NullPointerException("path is marked non-null but is null");
        }
        if (property == null) {
            throw new NullPointerException("property is marked non-null but is null");
        }
        final InMemoryIndex flatIndex = (InMemoryIndex)((Map)this.indexMap.get((Object)collection.getValue())).get((Object)property.getValue());
        if (flatIndex == null) {
            throw new IllegalArgumentException("non-indexed property used: " + (Object)property);
        }
        final String currentValue = (String)flatIndex.getKeyToValue().remove((Object)path.getValue());
        if (currentValue != null) {
            ((Set)flatIndex.getValueToKeys().get((Object)currentValue)).remove((Object)path.getValue());
        }
        final Set<String> keys = (Set<String>)flatIndex.getValueToKeys().computeIfAbsent((Object)identity, s -> new HashSet());
        boolean changed = keys.add((Object)path.getValue());
        changed = (flatIndex.getKeyToValue().put((Object)path.getValue(), (Object)identity) != null || changed);
        if (this.isSaveIndex() && this.isFlushOnWrite()) {
            flatIndex.save();
        }
        return changed;
    }
    
    @Override
    public boolean dropIndex(@NonNull final PersistenceCollection collection, @NonNull final PersistencePath path, @NonNull final IndexProperty property) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        if (path == null) {
            throw new NullPointerException("path is marked non-null but is null");
        }
        if (property == null) {
            throw new NullPointerException("property is marked non-null but is null");
        }
        final InMemoryIndex flatIndex = (InMemoryIndex)((Map)this.indexMap.get((Object)collection.getValue())).get((Object)property.getValue());
        if (flatIndex == null) {
            throw new IllegalArgumentException("non-indexed property used: " + (Object)property);
        }
        final String currentValue = (String)flatIndex.getKeyToValue().remove((Object)path.getValue());
        final boolean changed = currentValue != null && ((Set)flatIndex.getValueToKeys().get((Object)currentValue)).remove((Object)path.getValue());
        if (this.isSaveIndex() && this.isFlushOnWrite()) {
            flatIndex.save();
        }
        return changed;
    }
    
    @Override
    public boolean dropIndex(@NonNull final PersistenceCollection collection, @NonNull final PersistencePath path) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        if (path == null) {
            throw new NullPointerException("path is marked non-null but is null");
        }
        return ((Set)this.getKnownIndexes().getOrDefault((Object)collection.getValue(), (Object)Collections.emptySet())).stream().map(index -> this.dropIndex(collection, path, index)).anyMatch(Predicate.isEqual((Object)true));
    }
    
    @Override
    public boolean dropIndex(@NonNull final PersistenceCollection collection, @NonNull final IndexProperty property) {
        try {
            if (collection == null) {
                throw new NullPointerException("collection is marked non-null but is null");
            }
            if (property == null) {
                throw new NullPointerException("property is marked non-null but is null");
            }
            final InMemoryIndex flatIndex = (InMemoryIndex)((Map)this.indexMap.get((Object)collection.getValue())).remove((Object)property.getValue());
            return flatIndex != null && Files.deleteIfExists(flatIndex.getBindFile());
        }
        catch (final Throwable $ex) {
            throw $ex;
        }
    }
    
    @Override
    public Set<PersistencePath> findMissingIndexes(@NonNull final PersistenceCollection collection, @NonNull final Set<IndexProperty> indexProperties) {
        try {
            if (collection == null) {
                throw new NullPointerException("collection is marked non-null but is null");
            }
            if (indexProperties == null) {
                throw new NullPointerException("indexProperties is marked non-null but is null");
            }
            final Map<String, InMemoryIndex> collectionIndexes = (Map<String, InMemoryIndex>)this.indexMap.get((Object)collection.getValue());
            if (collectionIndexes.isEmpty()) {
                return (Set<PersistencePath>)Collections.emptySet();
            }
            final Path collectionFile = this.getBasePath().sub(collection).toPath();
            return (Set<PersistencePath>)this.scanCollection(collectionFile).map((Function)this.fileToKeyMapper).map(key -> collectionIndexes.values().stream().allMatch(flatIndex -> flatIndex.getKeyToValue().containsKey((Object)key)) ? null : PersistencePath.of(key)).filter(Objects::nonNull).collect(Collectors.toSet());
        }
        catch (final Throwable $ex) {
            throw $ex;
        }
    }
    
    @Override
    public Stream<PersistenceEntity<String>> readByProperty(@NonNull final PersistenceCollection collection, @NonNull final PersistencePath property, @NonNull final Object propertyValue) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        if (property == null) {
            throw new NullPointerException("property is marked non-null but is null");
        }
        if (propertyValue == null) {
            throw new NullPointerException("propertyValue is marked non-null but is null");
        }
        if (!this.canUseToString(propertyValue)) {
            return this.streamAll(collection);
        }
        final InMemoryIndex flatIndex = (InMemoryIndex)((Map)this.indexMap.get((Object)collection.getValue())).get((Object)property.getValue());
        if (flatIndex == null) {
            return this.streamAll(collection);
        }
        final Set<String> keys = (Set<String>)flatIndex.getValueToKeys().get((Object)String.valueOf(propertyValue));
        if (keys == null || keys.isEmpty()) {
            return (Stream<PersistenceEntity<String>>)Stream.of((Object[])new PersistenceEntity[0]);
        }
        return (Stream<PersistenceEntity<String>>)new ArrayList((Collection)keys).stream().map(key -> {
            final PersistencePath path = PersistencePath.of(key);
            return (PersistenceEntity)this.read(collection, path).map(data -> new PersistenceEntity(path, (V)data)).orElseGet(() -> {
                this.dropIndex(collection, path);
                return null;
            });
        }).filter(Objects::nonNull);
    }
    
    @Override
    public void registerCollection(@NonNull final PersistenceCollection collection) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        final PersistencePath collectionPath = this.getBasePath().sub(collection);
        final File collectionFile = collectionPath.toFile();
        collectionFile.mkdirs();
        final Map<String, InMemoryIndex> indexes = (Map<String, InMemoryIndex>)this.indexMap.computeIfAbsent((Object)collection.getValue(), col -> new ConcurrentHashMap());
        for (final IndexProperty index : collection.getIndexes()) {
            final InMemoryIndex flatIndex = ConfigManager.create(InMemoryIndex.class);
            flatIndex.setConfigurer(this.indexProvider.get());
            final Path path = collectionPath.append("_").append(index.toSafeFileName()).append(".index").toPath();
            flatIndex.withBindFile(path);
            if (this.isSaveIndex() && Files.exists(path, new LinkOption[0])) {
                flatIndex.load(path);
            }
            indexes.put((Object)index.getValue(), (Object)flatIndex);
        }
        super.registerCollection(collection);
    }
    
    @Override
    public Optional<String> read(@NonNull final PersistenceCollection collection, @NonNull final PersistencePath path) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        if (path == null) {
            throw new NullPointerException("path is marked non-null but is null");
        }
        this.checkCollectionRegistered(collection);
        final File file = this.toFullPath(collection, path).toFile();
        return (Optional<String>)(file.exists() ? Optional.ofNullable((Object)this.fileToString(file)) : Optional.empty());
    }
    
    @Override
    public Map<PersistencePath, String> read(@NonNull final PersistenceCollection collection, @NonNull final Collection<PersistencePath> paths) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        if (paths == null) {
            throw new NullPointerException("paths is marked non-null but is null");
        }
        return (Map<PersistencePath, String>)paths.stream().distinct().map(path -> new PersistenceEntity(path, (V)this.read(collection, path).orElse((Object)null))).filter(entity -> entity.getValue() != null).collect(Collectors.toMap(PersistenceEntity::getPath, PersistenceEntity::getValue));
    }
    
    @Override
    public Map<PersistencePath, String> readOrEmpty(@NonNull final PersistenceCollection collection, @NonNull final Collection<PersistencePath> paths) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        if (paths == null) {
            throw new NullPointerException("paths is marked non-null but is null");
        }
        return (Map<PersistencePath, String>)paths.stream().distinct().collect(Collectors.toMap(path -> path, path -> this.readOrEmpty(collection, path)));
    }
    
    @Override
    public Map<PersistencePath, String> readAll(@NonNull final PersistenceCollection collection) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        return (Map<PersistencePath, String>)this.streamAll(collection).collect(Collectors.toMap(PersistenceEntity::getPath, PersistenceEntity::getValue));
    }
    
    @Override
    public Stream<PersistenceEntity<String>> streamAll(@NonNull final PersistenceCollection collection) {
        try {
            if (collection == null) {
                throw new NullPointerException("collection is marked non-null but is null");
            }
            this.checkCollectionRegistered(collection);
            final Path collectionFile = this.getBasePath().sub(collection).toPath();
            return (Stream<PersistenceEntity<String>>)this.scanCollection(collectionFile).map(path -> {
                final PersistencePath persistencePath = PersistencePath.of((String)this.fileToKeyMapper.apply((Object)path));
                return new PersistenceEntity(persistencePath, (V)this.fileToString(path.toFile()));
            });
        }
        catch (final Throwable $ex) {
            throw $ex;
        }
    }
    
    @Override
    public long count(@NonNull final PersistenceCollection collection) {
        try {
            if (collection == null) {
                throw new NullPointerException("collection is marked non-null but is null");
            }
            this.checkCollectionRegistered(collection);
            final Path collectionFile = this.getBasePath().sub(collection).toPath();
            return this.scanCollection(collectionFile).count();
        }
        catch (final Throwable $ex) {
            throw $ex;
        }
    }
    
    @Override
    public boolean exists(@NonNull final PersistenceCollection collection, @NonNull final PersistencePath path) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        if (path == null) {
            throw new NullPointerException("path is marked non-null but is null");
        }
        this.checkCollectionRegistered(collection);
        return this.toFullPath(collection, path).toFile().exists();
    }
    
    @Override
    public boolean write(@NonNull final PersistenceCollection collection, @NonNull final PersistencePath path, @NonNull final String raw) {
        try {
            if (collection == null) {
                throw new NullPointerException("collection is marked non-null but is null");
            }
            if (path == null) {
                throw new NullPointerException("path is marked non-null but is null");
            }
            if (raw == null) {
                throw new NullPointerException("raw is marked non-null but is null");
            }
            this.checkCollectionRegistered(collection);
            final File file = this.toFullPath(collection, path).toFile();
            final File parentFile = file.getParentFile();
            if (parentFile != null) {
                parentFile.mkdirs();
            }
            this.writeToFile(file, raw);
            return true;
        }
        catch (final Throwable $ex) {
            throw $ex;
        }
    }
    
    @Override
    public PersistencePath convertPath(@NonNull final PersistencePath path) {
        if (path == null) {
            throw new NullPointerException("path is marked non-null but is null");
        }
        return path.append(this.fileSuffix);
    }
    
    @Override
    public boolean delete(@NonNull final PersistenceCollection collection, @NonNull final PersistencePath path) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        if (path == null) {
            throw new NullPointerException("path is marked non-null but is null");
        }
        this.checkCollectionRegistered(collection);
        final Set<IndexProperty> collectionIndexes = (Set<IndexProperty>)this.getKnownIndexes().get((Object)collection.getValue());
        if (collectionIndexes != null) {
            collectionIndexes.forEach(index -> this.dropIndex(collection, path));
        }
        return this.toFullPath(collection, path).toFile().delete();
    }
    
    @Override
    public long delete(@NonNull final PersistenceCollection collection, @NonNull final Collection<PersistencePath> paths) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        if (paths == null) {
            throw new NullPointerException("paths is marked non-null but is null");
        }
        return paths.stream().map(path -> this.delete(collection, path)).filter(Predicate.isEqual((Object)true)).count();
    }
    
    @Override
    public boolean deleteAll(@NonNull final PersistenceCollection collection) {
        try {
            if (collection == null) {
                throw new NullPointerException("collection is marked non-null but is null");
            }
            this.checkCollectionRegistered(collection);
            final File collectionFile = this.getBasePath().sub(collection).toFile();
            this.checkCollectionRegistered(collection);
            final Set<IndexProperty> collectionIndexes = (Set<IndexProperty>)this.getKnownIndexes().get((Object)collection.getValue());
            if (collectionIndexes != null) {
                collectionIndexes.forEach(index -> this.dropIndex(collection, index));
            }
            return collectionFile.exists() && this.delete(collectionFile) > 0L;
        }
        catch (final Throwable $ex) {
            throw $ex;
        }
    }
    
    @Override
    public long deleteAll() {
        final File[] files = this.getBasePath().toFile().listFiles();
        if (files == null) {
            return 0L;
        }
        return Arrays.stream((Object[])files).filter(file -> this.getKnownCollections().keySet().contains((Object)file.getName())).map(this::delete).filter(deleted -> deleted > 0L).count();
    }
    
    public void close() throws IOException {
    }
    
    private long delete(@NonNull final File file) {
        try {
            if (file == null) {
                throw new NullPointerException("file is marked non-null but is null");
            }
            final Stream<Path> walk = (Stream<Path>)Files.walk(file.toPath(), new FileVisitOption[0]);
            try {
                return walk.sorted(Comparator.reverseOrder()).map(Path::toFile).map(File::delete).filter(Predicate.isEqual((Object)true)).count();
            }
            finally {
                if (Collections.singletonList((Object)walk).get(0) != null) {
                    walk.close();
                }
            }
        }
        catch (final Throwable $ex) {
            throw $ex;
        }
    }
    
    private Stream<Path> scanCollection(@NonNull final Path collectionFile) {
        try {
            if (collectionFile == null) {
                throw new NullPointerException("collectionFile is marked non-null but is null");
            }
            return (Stream<Path>)Files.list(collectionFile).filter(path -> {
                final boolean endsWithSuffix = path.toString().endsWith(this.getFileSuffix());
                if (FlatPersistence.DEBUG && !endsWithSuffix) {
                    FlatPersistence.LOGGER.log(Level.WARNING, "Possibly bogus file found in " + (Object)collectionFile + ": " + (Object)path + " (not ending with '" + this.getFileSuffix() + "')");
                }
                return endsWithSuffix;
            });
        }
        catch (final Throwable $ex) {
            throw $ex;
        }
    }
    
    private String fileToString(final File file) {
        try {
            return new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
        }
        catch (final IOException exception) {
            if (FlatPersistence.DEBUG) {
                FlatPersistence.LOGGER.log(Level.WARNING, "Returning empty data for " + (Object)file, (Throwable)exception);
            }
            return null;
        }
    }
    
    private void writeToFile(final File file, final String text) {
        try {
            final BufferedWriter writer = new BufferedWriter((Writer)new FileWriter(file));
            try {
                writer.write(text);
            }
            finally {
                if (Collections.singletonList((Object)writer).get(0) != null) {
                    writer.close();
                }
            }
        }
        catch (final Throwable $ex) {
            throw $ex;
        }
    }
    
    @Override
    public PersistencePath getBasePath() {
        return this.basePath;
    }
    
    public String getFileSuffix() {
        return this.fileSuffix;
    }
    
    public ConfigurerProvider getIndexProvider() {
        return this.indexProvider;
    }
    
    public boolean isSaveIndex() {
        return this.saveIndex;
    }
    
    public void setSaveIndex(final boolean saveIndex) {
        this.saveIndex = saveIndex;
    }
    
    static {
        DEBUG = Boolean.parseBoolean(System.getProperty("okaeri.platform.debug", "false"));
        LOGGER = Logger.getLogger(FlatPersistence.class.getSimpleName());
    }
    
    private class Pair<L, R>
    {
        private L left;
        private R right;
        
        public L getLeft() {
            return this.left;
        }
        
        public R getRight() {
            return this.right;
        }
        
        public void setLeft(final L left) {
            this.left = left;
        }
        
        public void setRight(final R right) {
            this.right = right;
        }
        
        @Override
        public boolean equals(final Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof Pair)) {
                return false;
            }
            final Pair<?, ?> other = (Pair<?, ?>)o;
            if (!other.canEqual(this)) {
                return false;
            }
            final Object this$left = this.getLeft();
            final Object other$left = other.getLeft();
            Label_0065: {
                if (this$left == null) {
                    if (other$left == null) {
                        break Label_0065;
                    }
                }
                else if (this$left.equals(other$left)) {
                    break Label_0065;
                }
                return false;
            }
            final Object this$right = this.getRight();
            final Object other$right = other.getRight();
            if (this$right == null) {
                if (other$right == null) {
                    return true;
                }
            }
            else if (this$right.equals(other$right)) {
                return true;
            }
            return false;
        }
        
        protected boolean canEqual(final Object other) {
            return other instanceof Pair;
        }
        
        @Override
        public int hashCode() {
            final int PRIME = 59;
            int result = 1;
            final Object $left = this.getLeft();
            result = result * 59 + (($left == null) ? 43 : $left.hashCode());
            final Object $right = this.getRight();
            result = result * 59 + (($right == null) ? 43 : $right.hashCode());
            return result;
        }
        
        @Override
        public String toString() {
            return "FlatPersistence.Pair(left=" + this.getLeft() + ", right=" + this.getRight() + ")";
        }
        
        public Pair(final L left, final R right) {
            this.left = left;
            this.right = right;
        }
    }
}
