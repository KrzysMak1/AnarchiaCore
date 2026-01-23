package cc.dreamcode.antylogout.libs.com.mongodb;

import java.util.HashMap;
import cc.dreamcode.antylogout.libs.com.mongodb.annotations.NotThreadSafe;
import cc.dreamcode.antylogout.libs.com.mongodb.assertions.Assertions;
import java.util.Collections;
import javax.net.ssl.SSLContext;
import java.util.function.Supplier;
import java.util.Map;

public final class ClientEncryptionSettings
{
    private final MongoClientSettings keyVaultMongoClientSettings;
    private final String keyVaultNamespace;
    private final Map<String, Map<String, Object>> kmsProviders;
    private final Map<String, Supplier<Map<String, Object>>> kmsProviderPropertySuppliers;
    private final Map<String, SSLContext> kmsProviderSslContextMap;
    
    public static Builder builder() {
        return new Builder();
    }
    
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
    
    private ClientEncryptionSettings(final Builder builder) {
        this.keyVaultMongoClientSettings = Assertions.notNull("keyVaultMongoClientSettings", builder.keyVaultMongoClientSettings);
        this.keyVaultNamespace = Assertions.notNull("keyVaultNamespace", builder.keyVaultNamespace);
        this.kmsProviders = (Map<String, Map<String, Object>>)Assertions.notNull("kmsProviders", builder.kmsProviders);
        this.kmsProviderPropertySuppliers = (Map<String, Supplier<Map<String, Object>>>)Assertions.notNull("kmsProviderPropertySuppliers", builder.kmsProviderPropertySuppliers);
        this.kmsProviderSslContextMap = (Map<String, SSLContext>)Assertions.notNull("kmsProviderSslContextMap", builder.kmsProviderSslContextMap);
    }
    
    @NotThreadSafe
    public static final class Builder
    {
        private MongoClientSettings keyVaultMongoClientSettings;
        private String keyVaultNamespace;
        private Map<String, Map<String, Object>> kmsProviders;
        private Map<String, Supplier<Map<String, Object>>> kmsProviderPropertySuppliers;
        private Map<String, SSLContext> kmsProviderSslContextMap;
        
        public Builder keyVaultMongoClientSettings(final MongoClientSettings keyVaultMongoClientSettings) {
            this.keyVaultMongoClientSettings = Assertions.notNull("keyVaultMongoClientSettings", keyVaultMongoClientSettings);
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
        
        public ClientEncryptionSettings build() {
            return new ClientEncryptionSettings(this, null);
        }
        
        private Builder() {
            this.kmsProviderPropertySuppliers = (Map<String, Supplier<Map<String, Object>>>)new HashMap();
            this.kmsProviderSslContextMap = (Map<String, SSLContext>)new HashMap();
        }
    }
}
