package cc.dreamcode.antylogout.libs.cc.dreamcode.platform.persistence.component;

import cc.dreamcode.antylogout.libs.com.mongodb.client.MongoClient;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.mongo.MongoPersistence;
import cc.dreamcode.antylogout.libs.com.mongodb.client.MongoClients;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.jdbc.H2Persistence;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.json.simple.JsonSimpleConfigurer;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.jdbc.MariaDbPersistence;
import cc.dreamcode.antylogout.libs.com.zaxxer.hikari.HikariConfig;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.raw.RawPersistence;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.serdes.commons.SerdesCommons;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.serdes.OkaeriSerdesPack;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.json.gson.JsonGsonConfigurer;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.flat.FlatPersistence;
import cc.dreamcode.antylogout.libs.cc.dreamcode.platform.exception.PlatformException;
import cc.dreamcode.antylogout.libs.cc.dreamcode.platform.persistence.DreamPersistence;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.PersistencePath;
import cc.dreamcode.antylogout.libs.eu.okaeri.injector.Injector;
import cc.dreamcode.antylogout.libs.cc.dreamcode.utilities.builder.MapBuilder;
import java.util.Map;
import lombok.NonNull;
import cc.dreamcode.antylogout.libs.eu.okaeri.injector.annotation.Inject;
import cc.dreamcode.antylogout.libs.cc.dreamcode.platform.persistence.StorageConfig;
import cc.dreamcode.antylogout.libs.cc.dreamcode.platform.DreamPlatform;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.document.DocumentPersistence;
import cc.dreamcode.antylogout.libs.cc.dreamcode.platform.component.ComponentClassResolver;

public class DocumentPersistenceResolver implements ComponentClassResolver<DocumentPersistence>
{
    private final DreamPlatform dreamPlatform;
    private final StorageConfig storageConfig;
    
    @Inject
    public DocumentPersistenceResolver(final DreamPlatform dreamPlatform, final StorageConfig storageConfig) {
        this.dreamPlatform = dreamPlatform;
        this.storageConfig = storageConfig;
    }
    
    @Override
    public boolean isAssignableFrom(@NonNull final Class<DocumentPersistence> documentPersistenceClass) {
        if (documentPersistenceClass == null) {
            throw new NullPointerException("documentPersistenceClass is marked non-null but is null");
        }
        return DocumentPersistence.class.isAssignableFrom(documentPersistenceClass);
    }
    
    @Override
    public String getComponentName() {
        return "persistence";
    }
    
    @Override
    public Map<String, Object> getMetas(@NonNull final DocumentPersistence documentPersistence) {
        if (documentPersistence == null) {
            throw new NullPointerException("documentPersistence is marked non-null but is null");
        }
        return (Map<String, Object>)new MapBuilder<String, String>().put("type", this.storageConfig.storageType.getName()).put("prefix", this.storageConfig.prefix).build();
    }
    
    @Override
    public DocumentPersistence resolve(@NonNull final Injector injector, @NonNull final Class<DocumentPersistence> documentPersistenceClass) {
        if (injector == null) {
            throw new NullPointerException("injector is marked non-null but is null");
        }
        if (documentPersistenceClass == null) {
            throw new NullPointerException("documentPersistenceClass is marked non-null but is null");
        }
        final PersistencePath persistencePath = PersistencePath.of(this.storageConfig.prefix);
        try {
            Class.forName("org.mariadb.jdbc.Driver");
        }
        catch (final ClassNotFoundException ex) {}
        try {
            Class.forName("org.h2.Driver");
        }
        catch (final ClassNotFoundException ex2) {}
        if (!(this.dreamPlatform instanceof DreamPersistence)) {
            throw new PlatformException(this.dreamPlatform.getClass().getSimpleName() + " class must implement DreamPersistence.");
        }
        final DreamPersistence dreamPersistence = (DreamPersistence)this.dreamPlatform;
        switch (this.storageConfig.storageType) {
            case FLAT: {
                return new DocumentPersistence(new FlatPersistence(this.dreamPlatform.getDataFolder(), ".json"), JsonGsonConfigurer::new, new OkaeriSerdesPack[] { new SerdesCommons(), dreamPersistence.getPersistenceSerdesPack() });
            }
            case MYSQL: {
                final HikariConfig mariadbHikari = new HikariConfig();
                mariadbHikari.setJdbcUrl(this.storageConfig.uri);
                return new DocumentPersistence(new MariaDbPersistence(persistencePath, mariadbHikari), JsonSimpleConfigurer::new, new OkaeriSerdesPack[] { new SerdesCommons(), dreamPersistence.getPersistenceSerdesPack() });
            }
            case H2: {
                final HikariConfig jdbcHikari = new HikariConfig();
                jdbcHikari.setJdbcUrl(this.storageConfig.uri);
                return new DocumentPersistence(new H2Persistence(persistencePath, jdbcHikari), JsonSimpleConfigurer::new, new OkaeriSerdesPack[] { new SerdesCommons(), dreamPersistence.getPersistenceSerdesPack() });
            }
            case MONGO: {
                final MongoClient mongoClient = MongoClients.create(this.storageConfig.uri);
                return new DocumentPersistence(new MongoPersistence(persistencePath, mongoClient, this.storageConfig.prefix), JsonSimpleConfigurer::new, new OkaeriSerdesPack[] { new SerdesCommons(), dreamPersistence.getPersistenceSerdesPack() });
            }
            default: {
                throw new PlatformException("Unknown data type: " + (Object)this.storageConfig.storageType);
            }
        }
    }
}
