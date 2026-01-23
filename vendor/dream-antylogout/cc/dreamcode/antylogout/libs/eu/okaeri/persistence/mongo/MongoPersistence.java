package cc.dreamcode.antylogout.libs.eu.okaeri.persistence.mongo;

import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.mongo.filter.MongoFilterRenderer;
import cc.dreamcode.antylogout.libs.com.mongodb.client.model.Indexes;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.document.index.IndexProperty;
import cc.dreamcode.antylogout.libs.com.mongodb.client.model.ReplaceOneModel;
import cc.dreamcode.antylogout.libs.com.mongodb.client.MongoCollection;
import java.io.IOException;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.filter.DeleteFilter;
import cc.dreamcode.antylogout.libs.com.mongodb.client.model.WriteModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Collection;
import java.util.Optional;
import cc.dreamcode.antylogout.libs.com.mongodb.BasicDBObject;
import cc.dreamcode.antylogout.libs.com.mongodb.client.FindIterable;
import java.util.Spliterator;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.filter.FindFilter;
import java.util.stream.StreamSupport;
import cc.dreamcode.antylogout.libs.com.mongodb.client.model.Filters;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.PersistenceEntity;
import java.util.stream.Stream;
import cc.dreamcode.antylogout.libs.com.mongodb.client.model.IndexModel;
import java.util.stream.Collectors;
import java.util.List;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.PersistenceCollection;
import cc.dreamcode.antylogout.libs.org.bson.conversions.Bson;
import cc.dreamcode.antylogout.libs.org.bson.Document;
import lombok.NonNull;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.PersistencePath;
import cc.dreamcode.antylogout.libs.com.mongodb.client.MongoDatabase;
import cc.dreamcode.antylogout.libs.com.mongodb.client.MongoClient;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.filter.renderer.FilterRenderer;
import cc.dreamcode.antylogout.libs.com.mongodb.client.model.ReplaceOptions;
import java.util.logging.Logger;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.raw.NativeRawPersistence;

public class MongoPersistence extends NativeRawPersistence
{
    private static final Logger LOGGER;
    private static final ReplaceOptions REPLACE_OPTIONS;
    private static final FilterRenderer FILTER_RENDERER;
    private MongoClient client;
    private MongoDatabase database;
    
    public MongoPersistence(@NonNull final PersistencePath basePath, @NonNull final MongoClient client, @NonNull final String databaseName) {
        super(basePath);
        if (basePath == null) {
            throw new NullPointerException("basePath is marked non-null but is null");
        }
        if (client == null) {
            throw new NullPointerException("client is marked non-null but is null");
        }
        if (databaseName == null) {
            throw new NullPointerException("databaseName is marked non-null but is null");
        }
        this.connect(client, databaseName);
    }
    
    private void connect(@NonNull final MongoClient client, @NonNull final String databaseName) {
        try {
            if (client == null) {
                throw new NullPointerException("client is marked non-null but is null");
            }
            if (databaseName == null) {
                throw new NullPointerException("databaseName is marked non-null but is null");
            }
            do {
                try {
                    this.client = client;
                    final MongoDatabase database = client.getDatabase(databaseName);
                    database.runCommand(new Document("ping", 1));
                    this.database = database;
                }
                catch (final Exception exception) {
                    if (exception.getCause() != null) {
                        MongoPersistence.LOGGER.severe("[" + this.getBasePath().getValue() + "] Cannot connect with database (waiting 30s): " + exception.getMessage() + " caused by " + exception.getCause().getMessage());
                    }
                    else {
                        MongoPersistence.LOGGER.severe("[" + this.getBasePath().getValue() + "] Cannot connect with database (waiting 30s): " + exception.getMessage());
                    }
                    Thread.sleep(30000L);
                }
            } while (this.database == null);
        }
        catch (final Throwable $ex) {
            throw $ex;
        }
    }
    
    @Override
    public void registerCollection(@NonNull final PersistenceCollection collection) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        if (!collection.getIndexes().isEmpty()) {
            this.mongo(collection).createIndexes((List<IndexModel>)collection.getIndexes().stream().map(index -> new IndexModel(Indexes.ascending(index.getValue()))).collect(Collectors.toList()));
        }
        super.registerCollection(collection);
    }
    
    @Override
    public Stream<PersistenceEntity<String>> readByProperty(@NonNull final PersistenceCollection collection, @NonNull final PersistencePath property, final Object propertyValue) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        if (property == null) {
            throw new NullPointerException("property is marked non-null but is null");
        }
        return (Stream<PersistenceEntity<String>>)StreamSupport.stream(this.mongo(collection).find().filter(Filters.in(property.toMongoPath(), propertyValue)).map(object -> this.transformMongoObject(collection, object)).spliterator(), false);
    }
    
    @Override
    public Stream<PersistenceEntity<String>> readByFilter(@NonNull final PersistenceCollection collection, @NonNull final FindFilter filter) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        if (filter == null) {
            throw new NullPointerException("filter is marked non-null but is null");
        }
        FindIterable<BasicDBObject> findIterable = this.mongo(collection).find().filter(Document.parse(this.debugQuery(MongoPersistence.FILTER_RENDERER.renderCondition(filter.getWhere()))));
        if (filter.hasSkip()) {
            findIterable = findIterable.skip(filter.getSkip());
        }
        if (filter.hasLimit()) {
            findIterable = findIterable.limit(filter.getLimit());
        }
        final Spliterator<PersistenceEntity<String>> iterator = (Spliterator<PersistenceEntity<String>>)findIterable.map(object -> this.transformMongoObject(collection, object)).spliterator();
        return (Stream<PersistenceEntity<String>>)StreamSupport.stream((Spliterator)iterator, false);
    }
    
    @Override
    public Optional<String> read(@NonNull final PersistenceCollection collection, @NonNull final PersistencePath path) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        if (path == null) {
            throw new NullPointerException("path is marked non-null but is null");
        }
        return (Optional<String>)Optional.ofNullable((Object)this.mongo(collection).find().filter(Filters.eq("_id", path.getValue())).map(object -> this.transformMongoObject(collection, object).getValue()).first());
    }
    
    @Override
    public Map<PersistencePath, String> read(@NonNull final PersistenceCollection collection, @NonNull final Collection<PersistencePath> paths) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        if (paths == null) {
            throw new NullPointerException("paths is marked non-null but is null");
        }
        if (paths.isEmpty()) {
            return (Map<PersistencePath, String>)Collections.emptyMap();
        }
        final List<String> keys = (List<String>)paths.stream().map(PersistencePath::getValue).collect(Collectors.toList());
        return (Map<PersistencePath, String>)this.mongo(collection).find().filter(Filters.in("_id", (java.lang.Iterable<Object>)keys)).map(object -> this.transformMongoObject(collection, object)).into(new ArrayList()).stream().collect(Collectors.toMap(PersistenceEntity::getPath, PersistenceEntity::getValue));
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
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        return (Stream<PersistenceEntity<String>>)StreamSupport.stream(this.mongo(collection).find().map(object -> this.transformMongoObject(collection, object)).spliterator(), false);
    }
    
    @Override
    public long count(@NonNull final PersistenceCollection collection) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        return this.mongo(collection).countDocuments();
    }
    
    @Override
    public boolean exists(@NonNull final PersistenceCollection collection, @NonNull final PersistencePath path) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        if (path == null) {
            throw new NullPointerException("path is marked non-null but is null");
        }
        return this.read(collection, path).isPresent();
    }
    
    @Override
    public boolean write(@NonNull final PersistenceCollection collection, @NonNull final PersistencePath path, @NonNull final String document) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        if (path == null) {
            throw new NullPointerException("path is marked non-null but is null");
        }
        if (document == null) {
            throw new NullPointerException("document is marked non-null but is null");
        }
        final BasicDBObject data = BasicDBObject.parse(document);
        data.put((Object)"_id", (Object)path.getValue());
        final Bson filters = Filters.in("_id", path.getValue());
        return this.mongo(collection).replaceOne(filters, data, MongoPersistence.REPLACE_OPTIONS).getModifiedCount() > 0L;
    }
    
    @Override
    public long write(@NonNull final PersistenceCollection collection, @NonNull final Map<PersistencePath, String> entities) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        if (entities == null) {
            throw new NullPointerException("entities is marked non-null but is null");
        }
        if (entities.isEmpty()) {
            return 0L;
        }
        return this.mongo(collection).bulkWrite((java.util.List<? extends WriteModel<? extends BasicDBObject>>)entities.entrySet().stream().map(entry -> {
            final BasicDBObject data = BasicDBObject.parse((String)entry.getValue());
            data.put((Object)"_id", (Object)((PersistencePath)entry.getKey()).getValue());
            return data;
        }).map(document -> new ReplaceOneModel(Filters.in("_id", document.get("_id")), (T)document, MongoPersistence.REPLACE_OPTIONS)).collect(Collectors.toList())).getModifiedCount();
    }
    
    @Override
    public boolean delete(@NonNull final PersistenceCollection collection, @NonNull final PersistencePath path) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        if (path == null) {
            throw new NullPointerException("path is marked non-null but is null");
        }
        return this.mongo(collection).deleteOne(Filters.eq("_id", path.getValue())).getDeletedCount() > 0L;
    }
    
    @Override
    public long delete(@NonNull final PersistenceCollection collection, @NonNull final Collection<PersistencePath> paths) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        if (paths == null) {
            throw new NullPointerException("paths is marked non-null but is null");
        }
        final List<String> keys = (List<String>)paths.stream().map(PersistencePath::getValue).collect(Collectors.toList());
        return this.mongo(collection).deleteMany(Filters.in("_id", (java.lang.Iterable<Object>)keys)).getDeletedCount();
    }
    
    @Override
    public boolean deleteAll(@NonNull final PersistenceCollection collection) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        this.mongo(collection).drop();
        return true;
    }
    
    @Override
    public long deleteAll() {
        throw new RuntimeException("Not implemented yet");
    }
    
    @Override
    public long deleteByFilter(@NonNull final PersistenceCollection collection, @NonNull final DeleteFilter filter) {
        if (collection == null) {
            throw new NullPointerException("collection is marked non-null but is null");
        }
        if (filter == null) {
            throw new NullPointerException("filter is marked non-null but is null");
        }
        return this.mongo(collection).deleteMany(Document.parse(this.debugQuery(MongoPersistence.FILTER_RENDERER.renderCondition(filter.getWhere())))).getDeletedCount();
    }
    
    public void close() throws IOException {
        this.getClient().close();
    }
    
    protected MongoCollection<BasicDBObject> mongo(final PersistenceCollection collection) {
        final String identifier = this.getBasePath().sub(collection).toSqlIdentifier();
        return this.getDatabase().getCollection(identifier, BasicDBObject.class);
    }
    
    protected PersistenceEntity<String> transformMongoObject(final PersistenceCollection collection, final BasicDBObject object) {
        final PersistencePath path = PersistencePath.of(object.getString("_id"));
        object.remove((Object)"_id");
        return new PersistenceEntity<String>(path, object.toJson());
    }
    
    public MongoClient getClient() {
        return this.client;
    }
    
    public MongoDatabase getDatabase() {
        return this.database;
    }
    
    static {
        LOGGER = Logger.getLogger(MongoPersistence.class.getSimpleName());
        REPLACE_OPTIONS = new ReplaceOptions().upsert(true);
        FILTER_RENDERER = new MongoFilterRenderer();
    }
}
