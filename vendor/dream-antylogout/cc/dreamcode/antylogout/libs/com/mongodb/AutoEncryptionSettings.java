package cc.dreamcode.antylogout.libs.com.mongodb;

import java.util.HashMap;
import cc.dreamcode.antylogout.libs.com.mongodb.annotations.NotThreadSafe;
import cc.dreamcode.antylogout.libs.com.mongodb.assertions.Assertions;
import java.util.Collections;
import cc.dreamcode.antylogout.libs.com.mongodb.lang.Nullable;
import cc.dreamcode.antylogout.libs.org.bson.BsonDocument;
import java.util.function.Supplier;
import javax.net.ssl.SSLContext;
import java.util.Map;

public final class AutoEncryptionSettings
{
    private final MongoClientSettings keyVaultMongoClientSettings;
    private final String keyVaultNamespace;
    private final Map<String, Map<String, Object>> kmsProviders;
    private final Map<String, SSLContext> kmsProviderSslContextMap;
    private final Map<String, Supplier<Map<String, Object>>> kmsProviderPropertySuppliers;
    private final Map<String, BsonDocument> schemaMap;
    private final Map<String, Object> extraOptions;
    private final boolean bypassAutoEncryption;
    private final Map<String, BsonDocument> encryptedFieldsMap;
    private final boolean bypassQueryAnalysis;
    
    public static Builder builder() {
        return new Builder();
    }
    
    @Nullable
    public MongoClientSettings getKeyVaultMongoClientSettings() {
        return this.keyVaultMongoClientSettings;
    }
    
    public String getKeyVaultNamespace() {
        return this.keyVaultNamespace;
    }
    
    public Map<String, Map<String, Object>> getKmsProviders() {
        return (Map<String, Map<String, Object>>)Collections.unmodifiableMap((Map)this.kmsProviders);
    }
    
    public Map<String, Supplier<Map<String, Object>>> getKmsProviderPropertySuppliers() {
        return (Map<String, Supplier<Map<String, Object>>>)Collections.unmodifiableMap((Map)this.kmsProviderPropertySuppliers);
    }
    
    public Map<String, SSLContext> getKmsProviderSslContextMap() {
        return (Map<String, SSLContext>)Collections.unmodifiableMap((Map)this.kmsProviderSslContextMap);
    }
    
    public Map<String, BsonDocument> getSchemaMap() {
        return this.schemaMap;
    }
    
    public Map<String, Object> getExtraOptions() {
        return this.extraOptions;
    }
    
    public boolean isBypassAutoEncryption() {
        return this.bypassAutoEncryption;
    }
    
    @Nullable
    public Map<String, BsonDocument> getEncryptedFieldsMap() {
        return this.encryptedFieldsMap;
    }
    
    public boolean isBypassQueryAnalysis() {
        return this.bypassQueryAnalysis;
    }
    
    private AutoEncryptionSettings(final Builder builder) {
        this.keyVaultMongoClientSettings = builder.keyVaultMongoClientSettings;
        this.keyVaultNamespace = Assertions.notNull("keyVaultNamespace", builder.keyVaultNamespace);
        this.kmsProviders = (Map<String, Map<String, Object>>)Assertions.notNull("kmsProviders", builder.kmsProviders);
        this.kmsProviderSslContextMap = (Map<String, SSLContext>)Assertions.notNull("kmsProviderSslContextMap", builder.kmsProviderSslContextMap);
        this.kmsProviderPropertySuppliers = (Map<String, Supplier<Map<String, Object>>>)Assertions.notNull("kmsProviderPropertySuppliers", builder.kmsProviderPropertySuppliers);
        this.schemaMap = (Map<String, BsonDocument>)Assertions.notNull("schemaMap", builder.schemaMap);
        this.extraOptions = (Map<String, Object>)Assertions.notNull("extraOptions", builder.extraOptions);
        this.bypassAutoEncryption = builder.bypassAutoEncryption;
        this.encryptedFieldsMap = builder.encryptedFieldsMap;
        this.bypassQueryAnalysis = builder.bypassQueryAnalysis;
    }
    
    @Override
    public String toString() {
        return "AutoEncryptionSettings{<hidden>}";
    }
    
    @NotThreadSafe
    public static final class Builder
    {
        private MongoClientSettings keyVaultMongoClientSettings;
        private String keyVaultNamespace;
        private Map<String, Map<String, Object>> kmsProviders;
        private Map<String, SSLContext> kmsProviderSslContextMap;
        private Map<String, Supplier<Map<String, Object>>> kmsProviderPropertySuppliers;
        private Map<String, BsonDocument> schemaMap;
        private Map<String, Object> extraOptions;
        private boolean bypassAutoEncryption;
        private Map<String, BsonDocument> encryptedFieldsMap;
        private boolean bypassQueryAnalysis;
        
        public Builder keyVaultMongoClientSettings(final MongoClientSettings keyVaultMongoClientSettings) {
            this.keyVaultMongoClientSettings = keyVaultMongoClientSettings;
            return this;
        }
        
        public Builder keyVaultNamespace(final String keyVaultNamespace) {
            this.keyVaultNamespace = Assertions.notNull("keyVaultNamespace", keyVaultNamespace);
            return this;
        }
        
        public Builder kmsProviders(final Map<String, Map<String, Object>> kmsProviders) {
            this.kmsProviders = Assertions.notNull("kmsProviders", kmsProviders);
            return this;
        }
        
        public Builder kmsProviderPropertySuppliers(final Map<String, Supplier<Map<String, Object>>> kmsProviderPropertySuppliers) {
            this.kmsProviderPropertySuppliers = Assertions.notNull("kmsProviderPropertySuppliers", kmsProviderPropertySuppliers);
            return this;
        }
        
        public Builder kmsProviderSslContextMap(final Map<String, SSLContext> kmsProviderSslContextMap) {
            this.kmsProviderSslContextMap = Assertions.notNull("kmsProviderSslContextMap", kmsProviderSslContextMap);
            return this;
        }
        
        public Builder schemaMap(final Map<String, BsonDocument> schemaMap) {
            this.schemaMap = Assertions.notNull("schemaMap", schemaMap);
            return this;
        }
        
        public Builder extraOptions(final Map<String, Object> extraOptions) {
            this.extraOptions = Assertions.notNull("extraOptions", extraOptions);
            return this;
        }
        
        public Builder bypassAutoEncryption(final boolean bypassAutoEncryption) {
            this.bypassAutoEncryption = bypassAutoEncryption;
            return this;
        }
        
        public Builder encryptedFieldsMap(final Map<String, BsonDocument> encryptedFieldsMap) {
            this.encryptedFieldsMap = Assertions.notNull("encryptedFieldsMap", encryptedFieldsMap);
            return this;
        }
        
        public Builder bypassQueryAnalysis(final boolean bypassQueryAnalysis) {
            this.bypassQueryAnalysis = bypassQueryAnalysis;
            return this;
        }
        
        public AutoEncryptionSettings build() {
            return new AutoEncryptionSettings(this, null);
        }
        
        private Builder() {
            this.kmsProviderSslContextMap = (Map<String, SSLContext>)new HashMap();
            this.kmsProviderPropertySuppliers = (Map<String, Supplier<Map<String, Object>>>)new HashMap();
            this.schemaMap = (Map<String, BsonDocument>)Collections.emptyMap();
            this.extraOptions = (Map<String, Object>)Collections.emptyMap();
            this.encryptedFieldsMap = (Map<String, BsonDocument>)Collections.emptyMap();
        }
    }
}
