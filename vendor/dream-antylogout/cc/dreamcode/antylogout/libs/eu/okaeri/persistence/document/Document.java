package cc.dreamcode.antylogout.libs.eu.okaeri.persistence.document;

import cc.dreamcode.antylogout.libs.eu.okaeri.configs.ConfigManager;
import lombok.NonNull;
import java.util.Optional;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.exception.OkaeriException;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.PersistenceCollection;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.PersistencePath;
import java.util.logging.Logger;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.annotation.Exclude;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.OkaeriConfig;

public class Document extends OkaeriConfig
{
    @Exclude
    private static final boolean DEBUG;
    @Exclude
    private static final Logger LOGGER;
    @Exclude
    private DocumentPersistence persistence;
    @Exclude
    private PersistencePath path;
    @Exclude
    private PersistenceCollection collection;
    @Exclude
    private Document cachedInto;
    
    public Document() {
        this.cachedInto = this;
    }
    
    @Override
    public Document save() throws OkaeriException {
        final long start = System.currentTimeMillis();
        final String logPath = Document.DEBUG ? ((this.collection != null && this.path != null) ? this.collection.sub(this.path).getValue() : ("unknown/" + (Object)this.persistence)) : null;
        if (this.getBindFile() == null) {
            this.getPersistence().write(this.getCollection(), this.getPath(), this);
        }
        else {
            this.save(this.getBindFile());
        }
        if (Document.DEBUG) {
            final long took = System.currentTimeMillis() - start;
            Document.LOGGER.info("[" + logPath + "] Document save took " + took + " ms");
        }
        return this;
    }
    
    @Override
    public OkaeriConfig load() throws OkaeriException {
        final long start = System.currentTimeMillis();
        final String logPath = Document.DEBUG ? ((this.collection != null && this.path != null) ? this.collection.sub(this.path).getValue() : ("unknown/" + (Object)this.persistence)) : null;
        if (this.getBindFile() == null) {
            final Optional<Document> document = this.getPersistence().read(this.getCollection(), this.getPath());
            if (!document.isPresent()) {
                throw new RuntimeException("Cannot #load, no result returned from persistence for path " + (Object)this.getPath());
            }
            this.load((OkaeriConfig)document.get());
        }
        else {
            this.load(this.getBindFile());
        }
        return this;
    }
    
    @Override
    public OkaeriConfig saveDefaults() throws OkaeriException {
        throw new RuntimeException("saveDefaults() not available for ConfigDocument");
    }
    
    public <T extends Document> T into(@NonNull final Class<T> configClazz) {
        if (configClazz == null) {
            throw new NullPointerException("configClazz is marked non-null but is null");
        }
        if (!configClazz.isInstance(this.cachedInto)) {
            final T newEntity = ConfigManager.transformCopy(this.cachedInto, configClazz);
            newEntity.setPath(this.cachedInto.getPath());
            newEntity.setCollection(this.cachedInto.getCollection());
            newEntity.setPersistence(this.cachedInto.getPersistence());
            this.cachedInto = newEntity;
        }
        return (T)this.cachedInto;
    }
    
    @Override
    public String toString() {
        return "Document(persistence=" + (Object)this.getPersistence() + ", path=" + (Object)this.getPath() + ", collection=" + (Object)this.getCollection() + ")";
    }
    
    public DocumentPersistence getPersistence() {
        return this.persistence;
    }
    
    public void setPersistence(final DocumentPersistence persistence) {
        this.persistence = persistence;
    }
    
    public PersistencePath getPath() {
        return this.path;
    }
    
    public void setPath(final PersistencePath path) {
        this.path = path;
    }
    
    public PersistenceCollection getCollection() {
        return this.collection;
    }
    
    public void setCollection(final PersistenceCollection collection) {
        this.collection = collection;
    }
    
    static {
        DEBUG = Boolean.parseBoolean(System.getProperty("okaeri.platform.debug", "false"));
        LOGGER = Logger.getLogger(Document.class.getSimpleName());
    }
}
