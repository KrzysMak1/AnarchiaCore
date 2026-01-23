package cc.dreamcode.antylogout.libs.eu.okaeri.persistence.repository;

import java.util.Optional;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.filter.condition.Condition;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.filter.FindFilterBuilder;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.filter.FindFilter;
import java.util.stream.Stream;
import java.util.Collection;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.filter.DeleteFilterBuilder;
import java.util.function.Function;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.filter.DeleteFilter;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.PersistenceCollection;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.document.DocumentPersistence;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.document.Document;

public interface DocumentRepository<PATH, T extends Document>
{
    DocumentPersistence getPersistence();
    
    PersistenceCollection getCollection();
    
    Class<? extends Document> getDocumentType();
    
    long count();
    
    boolean deleteAll();
    
    long delete(final DeleteFilter filter);
    
    long delete(final Function<DeleteFilterBuilder, DeleteFilterBuilder> function);
    
    long deleteAllByPath(final Iterable<? extends PATH> paths);
    
    boolean deleteByPath(final PATH path);
    
    boolean existsByPath(final PATH path);
    
    Collection<T> findAll();
    
    Stream<T> streamAll();
    
    Stream<T> find(final FindFilter filter);
    
    Stream<T> find(final Function<FindFilterBuilder, FindFilterBuilder> function);
    
    Stream<T> find(final Condition condition);
    
    Optional<T> findOne(final Condition condition);
    
    Collection<T> findAllByPath(final Iterable<? extends PATH> paths);
    
    Collection<T> findOrCreateAllByPath(final Iterable<? extends PATH> paths);
    
    Optional<T> findByPath(final PATH path);
    
    T findOrCreateByPath(final PATH path);
    
    T save(final T document);
    
    Iterable<T> saveAll(final Iterable<T> documents);
}
