package cc.dreamcode.antylogout.libs.eu.okaeri.persistence.repository;

import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.PersistenceCollection;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.document.DocumentPersistence;

public interface RepositoryMethodCaller
{
    Object call(final DocumentPersistence persistence, final PersistenceCollection collection, final Object[] args);
}
