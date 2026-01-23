package cc.dreamcode.antylogout.libs.eu.okaeri.persistence;

import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.filter.DeleteFilter;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.filter.FindFilter;
import java.util.stream.Stream;
import java.util.Map;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.document.index.IndexProperty;
import java.io.Closeable;

public interface Persistence<T> extends Closeable
{
    void registerCollection(final PersistenceCollection collection);
    
    long fixIndexes(final PersistenceCollection collection);
    
    void setFlushOnWrite(final boolean state);
    
    void flush();
    
    boolean updateIndex(final PersistenceCollection collection, final PersistencePath path, final IndexProperty property, final String identity);
    
    boolean updateIndex(final PersistenceCollection collection, final PersistencePath path, final T entity);
    
    boolean updateIndex(final PersistenceCollection collection, final PersistencePath path);
    
    boolean dropIndex(final PersistenceCollection collection, final PersistencePath path, final IndexProperty property);
    
    boolean dropIndex(final PersistenceCollection collection, final PersistencePath path);
    
    boolean dropIndex(final PersistenceCollection collection, final IndexProperty property);
    
    Set<PersistencePath> findMissingIndexes(final PersistenceCollection collection, final Set<IndexProperty> indexProperties);
    
    PersistencePath getBasePath();
    
    long count(final PersistenceCollection collection);
    
    boolean exists(final PersistenceCollection collection, final PersistencePath path);
    
    Optional<T> read(final PersistenceCollection collection, final PersistencePath path);
    
    T readOrEmpty(final PersistenceCollection collection, final PersistencePath path);
    
    Map<PersistencePath, T> read(final PersistenceCollection collection, final Collection<PersistencePath> paths);
    
    Map<PersistencePath, T> readOrEmpty(final PersistenceCollection collection, final Collection<PersistencePath> paths);
    
    Map<PersistencePath, T> readAll(final PersistenceCollection collection);
    
    Stream<PersistenceEntity<T>> readByProperty(final PersistenceCollection collection, final PersistencePath property, final Object propertyValue);
    
    Stream<PersistenceEntity<T>> readByFilter(final PersistenceCollection collection, final FindFilter filter);
    
    Stream<PersistenceEntity<T>> streamAll(final PersistenceCollection collection);
    
    boolean write(final PersistenceCollection collection, final PersistencePath path, final T entity);
    
    long write(final PersistenceCollection collection, final Map<PersistencePath, T> entities);
    
    boolean delete(final PersistenceCollection collection, final PersistencePath path);
    
    long delete(final PersistenceCollection collection, final Collection<PersistencePath> paths);
    
    boolean deleteAll(final PersistenceCollection collection);
    
    long deleteByFilter(final PersistenceCollection collection, final DeleteFilter filter);
    
    long deleteAll();
}
