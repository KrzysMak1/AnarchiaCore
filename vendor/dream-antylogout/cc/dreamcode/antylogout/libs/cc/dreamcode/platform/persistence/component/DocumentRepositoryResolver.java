package cc.dreamcode.antylogout.libs.cc.dreamcode.platform.persistence.component;

import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.repository.RepositoryDeclaration;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.PersistenceCollection;
import cc.dreamcode.antylogout.libs.eu.okaeri.injector.Injector;
import java.util.HashMap;
import java.util.Map;
import lombok.NonNull;
import cc.dreamcode.antylogout.libs.eu.okaeri.injector.annotation.Inject;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.document.DocumentPersistence;
import cc.dreamcode.antylogout.libs.cc.dreamcode.platform.DreamPlatform;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.repository.DocumentRepository;
import cc.dreamcode.antylogout.libs.cc.dreamcode.platform.component.ComponentClassResolver;

public class DocumentRepositoryResolver implements ComponentClassResolver<DocumentRepository>
{
    private final DreamPlatform platform;
    private final DocumentPersistence documentPersistence;
    
    @Inject
    public DocumentRepositoryResolver(final DreamPlatform platform, final DocumentPersistence documentPersistence) {
        this.platform = platform;
        this.documentPersistence = documentPersistence;
    }
    
    @Override
    public boolean isAssignableFrom(@NonNull final Class<DocumentRepository> documentRepositoryClass) {
        if (documentRepositoryClass == null) {
            throw new NullPointerException("documentRepositoryClass is marked non-null but is null");
        }
        return DocumentRepository.class.isAssignableFrom(documentRepositoryClass);
    }
    
    @Override
    public String getComponentName() {
        return "repository";
    }
    
    @Override
    public Map<String, Object> getMetas(@NonNull final DocumentRepository documentRepository) {
        if (documentRepository == null) {
            throw new NullPointerException("documentRepository is marked non-null but is null");
        }
        return (Map<String, Object>)new HashMap();
    }
    
    @Override
    public DocumentRepository resolve(@NonNull final Injector injector, @NonNull final Class<DocumentRepository> documentRepositoryClass) {
        if (injector == null) {
            throw new NullPointerException("injector is marked non-null but is null");
        }
        if (documentRepositoryClass == null) {
            throw new NullPointerException("documentRepositoryClass is marked non-null but is null");
        }
        final PersistenceCollection persistenceCollection = PersistenceCollection.of(documentRepositoryClass);
        this.documentPersistence.registerCollection(persistenceCollection);
        return RepositoryDeclaration.of(documentRepositoryClass).newProxy(this.documentPersistence, persistenceCollection, this.platform.getClass().getClassLoader());
    }
}
