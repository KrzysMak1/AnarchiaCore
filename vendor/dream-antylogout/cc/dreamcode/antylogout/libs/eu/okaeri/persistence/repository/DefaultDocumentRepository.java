package cc.dreamcode.antylogout.libs.eu.okaeri.persistence.repository;

import java.util.Map;
import java.util.Set;
import java.util.Optional;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.filter.condition.Condition;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.filter.FindFilterBuilder;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.filter.FindFilter;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.PersistenceEntity;
import java.util.stream.Stream;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import java.util.Collection;
import lombok.NonNull;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.filter.DeleteFilterBuilder;
import java.util.function.Function;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.filter.DeleteFilter;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.PersistencePath;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.PersistenceCollection;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.document.DocumentPersistence;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.document.Document;

public class DefaultDocumentRepository<T extends Document> implements DocumentRepository<Object, T>
{
    private final DocumentPersistence persistence;
    private final PersistenceCollection collection;
    private final Class<T> documentType;
    
    private static PersistencePath toPath(final Object object) {
        if (object instanceof PersistencePath) {
            return (PersistencePath)object;
        }
        return PersistencePath.of(String.valueOf(object));
    }
    
    @Override
    public long count() {
        return this.persistence.count(this.collection);
    }
    
    @Override
    public boolean deleteAll() {
        return this.persistence.deleteAll(this.collection);
    }
    
    @Override
    public long delete(final DeleteFilter filter) {
        return this.persistence.deleteByFilter(this.collection, filter);
    }
    
    @Override
    public long delete(final Function<DeleteFilterBuilder, DeleteFilterBuilder> function) {
        return this.delete(((DeleteFilterBuilder)function.apply((Object)DeleteFilter.builder())).build());
    }
    
    @Override
    public long deleteAllByPath(@NonNull final Iterable<?> paths) {
        if (paths == null) {
            throw new NullPointerException("paths is marked non-null but is null");
        }
        return this.persistence.delete(this.collection, (Collection<PersistencePath>)StreamSupport.stream(paths.spliterator(), false).map(DefaultDocumentRepository::toPath).collect(Collectors.toSet()));
    }
    
    @Override
    public boolean deleteByPath(@NonNull final Object path) {
        if (path == null) {
            throw new NullPointerException("path is marked non-null but is null");
        }
        return this.persistence.delete(this.collection, toPath(path));
    }
    
    @Override
    public boolean existsByPath(@NonNull final Object path) {
        if (path == null) {
            throw new NullPointerException("path is marked non-null but is null");
        }
        return this.persistence.exists(this.collection, toPath(path));
    }
    
    @Override
    public Stream<T> streamAll() {
        return (Stream<T>)this.persistence.streamAll(this.collection).map(document -> document.into(this.documentType)).map(PersistenceEntity::getValue);
    }
    
    @Override
    public Collection<T> findAll() {
        return (Collection<T>)this.persistence.readAll(this.collection).values().stream().map(entity -> entity.into(this.documentType)).collect(Collectors.toList());
    }
    
    @Override
    public Stream<T> find(@NonNull final FindFilter filter) {
        if (filter == null) {
            throw new NullPointerException("filter is marked non-null but is null");
        }
        return (Stream<T>)this.persistence.readByFilter(this.collection, filter).map(document -> document.into(this.documentType)).map(PersistenceEntity::getValue);
    }
    
    @Override
    public Stream<T> find(@NonNull final Function<FindFilterBuilder, FindFilterBuilder> function) {
        if (function == null) {
            throw new NullPointerException("function is marked non-null but is null");
        }
        return this.find(((FindFilterBuilder)function.apply((Object)FindFilter.builder())).build());
    }
    
    @Override
    public Stream<T> find(@NonNull final Condition condition) {
        if (condition == null) {
            throw new NullPointerException("condition is marked non-null but is null");
        }
        return this.find((Function<FindFilterBuilder, FindFilterBuilder>)(q -> q.where(condition)));
    }
    
    @Override
    public Optional<T> findOne(@NonNull final Condition condition) {
        if (condition == null) {
            throw new NullPointerException("condition is marked non-null but is null");
        }
        return (Optional<T>)this.find((Function<FindFilterBuilder, FindFilterBuilder>)(q -> q.where(condition).limit(1))).findAny();
    }
    
    @Override
    public Collection<T> findAllByPath(@NonNull final Iterable<?> paths) {
        if (paths == null) {
            throw new NullPointerException("paths is marked non-null but is null");
        }
        final Set<PersistencePath> pathSet = (Set<PersistencePath>)StreamSupport.stream(paths.spliterator(), false).map(DefaultDocumentRepository::toPath).collect(Collectors.toSet());
        return (Collection<T>)this.persistence.read(this.collection, (Collection<PersistencePath>)pathSet).values().stream().map(document -> document.into(this.documentType)).collect(Collectors.toList());
    }
    
    @Override
    public Collection<T> findOrCreateAllByPath(@NonNull final Iterable<?> paths) {
        if (paths == null) {
            throw new NullPointerException("paths is marked non-null but is null");
        }
        final Set<PersistencePath> pathSet = (Set<PersistencePath>)StreamSupport.stream(paths.spliterator(), false).map(DefaultDocumentRepository::toPath).collect(Collectors.toSet());
        return (Collection<T>)this.persistence.readOrEmpty(this.collection, (Collection<PersistencePath>)pathSet).values().stream().map(document -> document.into(this.documentType)).collect(Collectors.toList());
    }
    
    @Override
    public Optional<T> findByPath(@NonNull final Object path) {
        if (path == null) {
            throw new NullPointerException("path is marked non-null but is null");
        }
        return (Optional<T>)this.persistence.read(this.collection, toPath(path)).map(document -> document.into(this.documentType));
    }
    
    @Override
    public T findOrCreateByPath(@NonNull final Object path) {
        if (path == null) {
            throw new NullPointerException("path is marked non-null but is null");
        }
        final Document document = this.persistence.readOrEmpty(this.collection, toPath(path));
        return document.into(this.documentType);
    }
    
    @Override
    public T save(@NonNull final T document) {
        if (document == null) {
            throw new NullPointerException("document is marked non-null but is null");
        }
        this.persistence.write(this.collection, document.getPath(), (Document)document);
        return document;
    }
    
    @Override
    public Iterable<T> saveAll(@NonNull final Iterable<T> documents) {
        if (documents == null) {
            throw new NullPointerException("documents is marked non-null but is null");
        }
        final Map<PersistencePath, Document> documentMap = (Map<PersistencePath, Document>)StreamSupport.stream(documents.spliterator(), false).collect(Collectors.toMap(Document::getPath, Function.identity()));
        this.persistence.write(this.collection, documentMap);
        return documents;
    }
    
    @Override
    public DocumentPersistence getPersistence() {
        return this.persistence;
    }
    
    @Override
    public PersistenceCollection getCollection() {
        return this.collection;
    }
    
    @Override
    public Class<T> getDocumentType() {
        return this.documentType;
    }
    
    public DefaultDocumentRepository(final DocumentPersistence persistence, final PersistenceCollection collection, final Class<T> documentType) {
        this.persistence = persistence;
        this.collection = collection;
        this.documentType = documentType;
    }
}
