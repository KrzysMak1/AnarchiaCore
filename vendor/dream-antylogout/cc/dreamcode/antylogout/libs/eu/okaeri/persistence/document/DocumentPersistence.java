package cc.dreamcode.antylogout.libs.eu.okaeri.persistence.document;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;
import java.math.BigDecimal;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.ConfigManager;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.filter.DeleteFilter;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.filter.FindFilter;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Function;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.raw.PersistencePropertyMode;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.PersistenceEntity;
import java.util.stream.Collectors;
import java.util.LinkedHashMap;
import java.util.Collections;
import java.util.Collection;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.OkaeriConfig;
import java.util.Optional;
import java.util.Map;
import java.util.Iterator;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.document.index.IndexProperty;
import java.util.HashSet;
import java.util.Set;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.raw.PersistenceIndexMode;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.PersistenceCollection;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.PersistencePath;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.document.ref.EagerRefSerializer;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.document.ref.LazyRefSerializer;
import java.util.stream.Stream;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.serdes.ObjectSerializer;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.serdes.commons.serializer.InstantSerializer;
import java.time.Instant;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.serdes.commons.SerdesCommons;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.serdes.standard.StandardSerdes;
import lombok.NonNull;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.configurer.Configurer;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.serdes.SerdesRegistry;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.raw.RawPersistence;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.serdes.OkaeriSerdesPack;
import java.util.logging.Logger;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.Persistence;

public class DocumentPersistence implements Persistence<Document>
{
    private static final Logger LOGGER;
    protected final ConfigurerProvider configurerProvider;
    protected final OkaeriSerdesPack[] serdesPacks;
    protected RawPersistence read;
    protected RawPersistence write;
    protected SerdesRegistry serdesRegistry;
    protected Configurer simplifier;
    
    public DocumentPersistence(@NonNull final ConfigurerProvider configurerProvider, @NonNull final OkaeriSerdesPack... serdesPacks) {
        if (configurerProvider == null) {
            throw new NullPointerException("configurerProvider is marked non-null but is null");
        }
        if (serdesPacks == null) {
            throw new NullPointerException("serdesPacks is marked non-null but is null");
        }
        this.configurerProvider = configurerProvider;
        this.serdesPacks = serdesPacks;
        this.serdesRegistry = new SerdesRegistry();
        Stream.concat(Stream.of((Object[])new OkaeriSerdesPack[] { new StandardSerdes(), new SerdesCommons(), registry -> registry.registerExclusive(Instant.class, new InstantSerializer(true)) }), Stream.of((Object[])this.serdesPacks)).forEach(pack -> pack.register(this.serdesRegistry));
        this.serdesRegistry.register(new LazyRefSerializer(this));
        this.serdesRegistry.register(new EagerRefSerializer(this));
        (this.simplifier = configurerProvider.get()).setRegistry(this.serdesRegistry);
    }
    
    public DocumentPersistence(@NonNull final RawPersistence readPersistence, @NonNull final RawPersistence writePersistence, @NonNull final ConfigurerProvider configurerProvider, @NonNull final OkaeriSerdesPack... serdesPacks) {
        this(configurerProvider, serdesPacks);
        if (readPersistence == null) {
            throw new NullPointerException("readPersistence is marked non-null but is null");
        }
        if (writePersistence == null) {
            throw new NullPointerException("writePersistence is marked non-null but is null");
        }
        if (configurerProvider == null) {
            throw new NullPointerException("configurerProvider is marked non-null but is null");
        }
        if (serdesPacks == null) {
            throw new NullPointerException("serdesPacks is marked non-null but is null");
        }
        this.read = readPersistence;
        this.write = writePersistence;
    }
    
    public DocumentPersistence(@NonNull final RawPersistence rawPersistence, @NonNull final ConfigurerProvider configurerProvider, @NonNull final OkaeriSerdesPack... serdesPacks) {
        this(rawPersistence, rawPersistence, configurerProvider, serdesPacks);
        if (rawPersistence == null) {
            throw new NullPointerException("rawPersistence is marked non-null but is null");
        }
        if (configurerProvider == null) {
            throw new NullPointerException("configurerProvider is marked non-null but is null");
        }
        if (serdesPacks == null) {
            throw new NullPointerException("serdesPacks is marked non-null but is null");
        }
    }
    
    public RawPersistence getRead() {
        if (this.read == null) {
            throw new IllegalArgumentException("This persistence instance does not provide raw access.");
        }
        return this.read;
    }
    
    public RawPersistence getWrite() {
        if (this.write == null) {
            throw new IllegalArgumentException("This persistence instance does not provide raw access.");
        }
        return this.write;
    }
    
    @Deprecated
    public RawPersistence getRaw() {
        if (!this.getRead().equals(this.getWrite())) {
            throw new IllegalArgumentException("Cannot use #getRaw() with DocumentPersistence using separate instances for read and write");
        }
        return this.getRead();
    }
    
    @Override
    public void setFlushOnWrite(final boolean state) {
        this.getWrite().setFlushOnWrite(state);
    }
    
    @Override
    public void flush() {
        this.getWrite().flush();
    }
    
    @Override
    public PersistencePath getBasePath() {
        if (!this.getRead().getBasePath().equals(this.getWrite().getBasePath())) {
            throw new IllegalArgumentException("Cannot use #getBasePath() with DocumentPersistence using different paths for read and write");
        }
        return this.getWrite().getBasePath();
    }
    
    @Override
    public void registerCollection(@NonNull final PersistenceCollection collection) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        this.getRead().registerCollection(collection);
        if (!this.getRead().equals(this.getWrite())) {
            this.getWrite().registerCollection(collection);
        }
        if (!collection.isAutofixIndexes()) {
            return;
        }
        this.fixIndexes(collection);
    }
    
    @Override
    public long fixIndexes(@NonNull final PersistenceCollection collection) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        if (this.getWrite().getIndexMode() != PersistenceIndexMode.EMULATED) {
            return 0L;
        }
        final Set<IndexProperty> indexes = (Set<IndexProperty>)this.getRead().getKnownIndexes().getOrDefault((Object)collection.getValue(), (Object)new HashSet());
        final Set<PersistencePath> withMissingIndexes = this.findMissingIndexes(collection, indexes);
        if (withMissingIndexes.isEmpty()) {
            return 0L;
        }
        final int total = withMissingIndexes.size();
        final long start = System.currentTimeMillis();
        long lastInfo = System.currentTimeMillis();
        int updated = 0;
        DocumentPersistence.LOGGER.warning("[" + this.getBasePath().sub(collection).getValue() + "] Found " + total + " entries with missing indexes, updating..");
        this.setFlushOnWrite(false);
        for (final PersistencePath key : withMissingIndexes) {
            this.updateIndex(collection, key);
            ++updated;
            if (System.currentTimeMillis() - lastInfo <= 5000L) {
                continue;
            }
            final int percent = (int)(updated / (double)total * 100.0);
            DocumentPersistence.LOGGER.warning("[" + this.getBasePath().sub(collection).getValue() + "] " + updated + " already done (" + percent + "%)");
            lastInfo = System.currentTimeMillis();
        }
        this.setFlushOnWrite(true);
        this.flush();
        DocumentPersistence.LOGGER.warning("[" + this.getBasePath().sub(collection).getValue() + "] Finished creating indexes! (took: " + (System.currentTimeMillis() - start) + " ms)");
        return updated;
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
        return this.getWrite().getIndexMode() == PersistenceIndexMode.EMULATED && this.getWrite().updateIndex(collection, path, property, identity);
    }
    
    @Override
    public boolean updateIndex(@NonNull final PersistenceCollection collection, @NonNull final PersistencePath path, @NonNull final Document document) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        if (path == null) {
            throw new NullPointerException("path is marked non-null but is null");
        }
        if (document == null) {
            throw new NullPointerException("document is marked non-null but is null");
        }
        if (this.getWrite().getIndexMode() != PersistenceIndexMode.EMULATED) {
            return false;
        }
        final Set<IndexProperty> collectionIndexes = (Set<IndexProperty>)this.getRead().getKnownIndexes().get((Object)collection.getValue());
        if (collectionIndexes == null) {
            return false;
        }
        final Map<String, Object> documentMap = document.asMap(this.simplifier, true);
        int changes = 0;
        for (final IndexProperty index : collectionIndexes) {
            final Object value = this.extractValue(documentMap, index.toParts());
            if (value != null && !this.getWrite().canUseToString(value)) {
                throw new RuntimeException("cannot transform " + value + " to index as string");
            }
            final boolean changed = this.updateIndex(collection, path, index, (value == null) ? null : String.valueOf(value));
            if (!changed) {
                continue;
            }
            ++changes;
        }
        return changes > 0;
    }
    
    @Override
    public boolean updateIndex(@NonNull final PersistenceCollection collection, @NonNull final PersistencePath path) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        if (path == null) {
            throw new NullPointerException("path is marked non-null but is null");
        }
        if (this.getWrite().getIndexMode() != PersistenceIndexMode.EMULATED) {
            return false;
        }
        final Optional<Document> document = this.read(collection, path);
        return document.map(value -> this.updateIndex(collection, path, value)).isPresent();
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
        return this.getWrite().getIndexMode() == PersistenceIndexMode.EMULATED && this.getWrite().dropIndex(collection, path, property);
    }
    
    @Override
    public boolean dropIndex(@NonNull final PersistenceCollection collection, @NonNull final PersistencePath path) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        if (path == null) {
            throw new NullPointerException("path is marked non-null but is null");
        }
        return this.getWrite().getIndexMode() == PersistenceIndexMode.EMULATED && this.getWrite().dropIndex(collection, path);
    }
    
    @Override
    public boolean dropIndex(@NonNull final PersistenceCollection collection, @NonNull final IndexProperty property) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        if (property == null) {
            throw new NullPointerException("property is marked non-null but is null");
        }
        return this.getWrite().getIndexMode() == PersistenceIndexMode.EMULATED && this.getWrite().dropIndex(collection, property);
    }
    
    @Override
    public Set<PersistencePath> findMissingIndexes(@NonNull final PersistenceCollection collection, @NonNull final Set<IndexProperty> indexProperties) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        if (indexProperties == null) {
            throw new NullPointerException("indexProperties is marked non-null but is null");
        }
        return this.getWrite().findMissingIndexes(collection, indexProperties);
    }
    
    @Override
    public Document readOrEmpty(@NonNull final PersistenceCollection collection, @NonNull final PersistencePath path) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        if (path == null) {
            throw new NullPointerException("path is marked non-null but is null");
        }
        return (Document)this.read(collection, path).orElse((Object)this.createDocument(collection, path));
    }
    
    @Override
    public Optional<Document> read(@NonNull final PersistenceCollection collection, @NonNull final PersistencePath path) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        if (path == null) {
            throw new NullPointerException("path is marked non-null but is null");
        }
        final Optional<String> data = this.getRead().read(collection, path);
        if (!data.isPresent()) {
            return (Optional<Document>)Optional.empty();
        }
        final Document document = this.createDocument(collection, path);
        return (Optional<Document>)Optional.of((Object)document.load((String)data.get()));
    }
    
    @Override
    public Map<PersistencePath, Document> readOrEmpty(@NonNull final PersistenceCollection collection, @NonNull final Collection<PersistencePath> paths) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        if (paths == null) {
            throw new NullPointerException("paths is marked non-null but is null");
        }
        if (paths.isEmpty()) {
            return (Map<PersistencePath, Document>)Collections.emptyMap();
        }
        final Map<PersistencePath, Document> map = (Map<PersistencePath, Document>)new LinkedHashMap();
        final Map<PersistencePath, Document> data = this.read(collection, paths);
        for (final PersistencePath path : paths) {
            map.put((Object)path, (Object)data.getOrDefault((Object)path, (Object)this.createDocument(collection, path)));
        }
        return map;
    }
    
    @Override
    public Map<PersistencePath, Document> read(@NonNull final PersistenceCollection collection, @NonNull final Collection<PersistencePath> paths) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        if (paths == null) {
            throw new NullPointerException("paths is marked non-null but is null");
        }
        return (Map<PersistencePath, Document>)(paths.isEmpty() ? Collections.emptyMap() : this.getRead().read(collection, paths).entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> {
            final Document document = this.createDocument(collection, (PersistencePath)entry.getKey());
            return (Document)document.load((String)entry.getValue());
        })));
    }
    
    @Override
    public Map<PersistencePath, Document> readAll(@NonNull final PersistenceCollection collection) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        return (Map<PersistencePath, Document>)this.getRead().readAll(collection).entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> {
            final Document document = this.createDocument(collection, (PersistencePath)entry.getKey());
            return (Document)document.load((String)entry.getValue());
        }));
    }
    
    @Override
    public Stream<PersistenceEntity<Document>> readByProperty(@NonNull final PersistenceCollection collection, @NonNull final PersistencePath property, final Object propertyValue) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        if (property == null) {
            throw new NullPointerException("property is marked non-null but is null");
        }
        final List<String> pathParts = property.toParts();
        final Predicate<PersistenceEntity<Document>> documentFilter = (Predicate<PersistenceEntity<Document>>)(entity -> {
            if (pathParts.size() == 1) {
                return this.compare(propertyValue, entity.getValue().get((String)pathParts.get(0)));
            }
            final Map<String, Object> document = entity.getValue().asMap(this.simplifier, true);
            return this.compare(propertyValue, this.extractValue(document, pathParts));
        });
        if (this.getRead().getPropertyMode() == PersistencePropertyMode.NATIVE) {
            return (Stream<PersistenceEntity<Document>>)this.getRead().readByProperty(collection, property, propertyValue).map((Function)this.entityToDocumentMapper(collection)).filter(entity -> this.getRead().getIndexMode() == PersistenceIndexMode.NATIVE || documentFilter.test((Object)entity));
        }
        final boolean stringSearch = this.getRead().getPropertyMode() == PersistencePropertyMode.TOSTRING && this.getWrite().canUseToString(propertyValue);
        return (Stream<PersistenceEntity<Document>>)this.getRead().streamAll(collection).filter(entity -> !stringSearch || entity.getValue().contains((CharSequence)String.valueOf(propertyValue))).map((Function)this.entityToDocumentMapper(collection)).filter((Predicate)documentFilter);
    }
    
    @Override
    public Stream<PersistenceEntity<Document>> readByFilter(@NonNull final PersistenceCollection collection, @NonNull final FindFilter filter) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        if (filter == null) {
            throw new NullPointerException("filter is marked non-null but is null");
        }
        return (Stream<PersistenceEntity<Document>>)this.getRead().readByFilter(collection, filter).map((Function)this.entityToDocumentMapper(collection));
    }
    
    @Override
    public Stream<PersistenceEntity<Document>> streamAll(@NonNull final PersistenceCollection collection) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        return (Stream<PersistenceEntity<Document>>)this.getRead().streamAll(collection).map((Function)this.entityToDocumentMapper(collection));
    }
    
    @Override
    public long count(@NonNull final PersistenceCollection collection) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        return this.getRead().count(collection);
    }
    
    @Override
    public boolean exists(@NonNull final PersistenceCollection collection, @NonNull final PersistencePath path) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        if (path == null) {
            throw new NullPointerException("path is marked non-null but is null");
        }
        return this.getRead().exists(collection, path);
    }
    
    @Override
    public boolean write(@NonNull final PersistenceCollection collection, @NonNull final PersistencePath path, @NonNull final Document document) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        if (path == null) {
            throw new NullPointerException("path is marked non-null but is null");
        }
        if (document == null) {
            throw new NullPointerException("document is marked non-null but is null");
        }
        this.updateIndex(collection, path, document);
        return this.getWrite().write(collection, path, this.update(document, collection).saveToString());
    }
    
    @Override
    public long write(@NonNull final PersistenceCollection collection, @NonNull final Map<PersistencePath, Document> entities) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        if (entities == null) {
            throw new NullPointerException("entities is marked non-null but is null");
        }
        if (entities.isEmpty()) {
            return 0L;
        }
        final Map<PersistencePath, String> rawMap = (Map<PersistencePath, String>)new LinkedHashMap();
        for (final Map.Entry<PersistencePath, Document> entry : entities.entrySet()) {
            this.updateIndex(collection, (PersistencePath)entry.getKey(), (Document)entry.getValue());
            rawMap.put((Object)entry.getKey(), (Object)this.update((Document)entry.getValue(), collection).saveToString());
        }
        return this.getWrite().write(collection, rawMap);
    }
    
    @Override
    public boolean delete(@NonNull final PersistenceCollection collection, @NonNull final PersistencePath path) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        if (path == null) {
            throw new NullPointerException("path is marked non-null but is null");
        }
        return this.getWrite().delete(collection, path);
    }
    
    @Override
    public long delete(@NonNull final PersistenceCollection collection, @NonNull final Collection<PersistencePath> paths) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        if (paths == null) {
            throw new NullPointerException("paths is marked non-null but is null");
        }
        return this.getWrite().delete(collection, paths);
    }
    
    @Override
    public boolean deleteAll(@NonNull final PersistenceCollection collection) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        return this.getWrite().deleteAll(collection);
    }
    
    @Override
    public long deleteAll() {
        return this.getWrite().deleteAll();
    }
    
    @Override
    public long deleteByFilter(@NonNull final PersistenceCollection collection, @NonNull final DeleteFilter filter) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        if (filter == null) {
            throw new NullPointerException("filter is marked non-null but is null");
        }
        return this.getWrite().deleteByFilter(collection, filter);
    }
    
    public Document createDocument(@NonNull final PersistenceCollection collection, @NonNull final PersistencePath path) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        if (path == null) {
            throw new NullPointerException("path is marked non-null but is null");
        }
        final Document config = this.update(ConfigManager.create(Document.class), collection);
        config.setPath(path);
        return config;
    }
    
    protected Function<PersistenceEntity<String>, PersistenceEntity<Document>> entityToDocumentMapper(final PersistenceCollection collection) {
        return (Function<PersistenceEntity<String>, PersistenceEntity<Document>>)(entity -> {
            final Document document = this.createDocument(collection, entity.getPath());
            document.load(entity.getValue());
            return entity.into(document);
        });
    }
    
    protected Object extractValue(Map<?, ?> document, final List<String> pathParts) {
        for (final String part : pathParts) {
            final Object element = document.get((Object)part);
            if (!(element instanceof Map)) {
                return element;
            }
            document = (Map<?, ?>)element;
        }
        return null;
    }
    
    protected boolean compare(final Object object1, final Object object2) {
        if (object1 == null && object2 == null) {
            return true;
        }
        if (object1 == null || object2 == null) {
            return false;
        }
        if (object1 instanceof Number && object2 instanceof Number) {
            return ((Number)object1).doubleValue() == ((Number)object2).doubleValue();
        }
        if (object1.getClass() == object2.getClass()) {
            return object1.equals(object2);
        }
        Label_0140: {
            if (!(object1 instanceof String) || !(object2 instanceof Number)) {
                if (!(object1 instanceof Number) || !(object2 instanceof String)) {
                    break Label_0140;
                }
            }
            try {
                return new BigDecimal(String.valueOf(object1)).compareTo(new BigDecimal(String.valueOf(object2))) == 0;
            }
            catch (final NumberFormatException ignored) {
                return false;
            }
        }
        if ((object1 instanceof String && object2 instanceof UUID) || (object1 instanceof UUID && object2 instanceof String)) {
            return Objects.equals((Object)String.valueOf(object1), (Object)String.valueOf(object2));
        }
        throw new IllegalArgumentException("cannot compare " + object1 + " [" + (Object)object1.getClass() + "] to " + object2 + " [" + (Object)object2.getClass() + "]");
    }
    
    public Document update(final Document document, final PersistenceCollection collection) {
        if (document.getDeclaration() == null) {
            document.updateDeclaration();
        }
        if (document.getConfigurer() == null) {
            document.setConfigurer(this.configurerProvider.get());
            document.getConfigurer().setRegistry(this.serdesRegistry);
        }
        if (document.getPersistence() == null) {
            document.setPersistence(this);
        }
        if (document.getCollection() == null) {
            document.setCollection(collection);
        }
        return document;
    }
    
    public void close() throws IOException {
        this.getRead().close();
        if (this.getRead().equals(this.getWrite())) {
            return;
        }
        this.getWrite().close();
    }
    
    public ConfigurerProvider getConfigurerProvider() {
        return this.configurerProvider;
    }
    
    public OkaeriSerdesPack[] getSerdesPacks() {
        return this.serdesPacks;
    }
    
    static {
        LOGGER = Logger.getLogger(DocumentPersistence.class.getSimpleName());
    }
}
