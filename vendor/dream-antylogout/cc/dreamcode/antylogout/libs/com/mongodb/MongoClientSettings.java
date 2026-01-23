package cc.dreamcode.antylogout.libs.com.mongodb;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import cc.dreamcode.antylogout.libs.com.mongodb.assertions.Assertions;
import java.util.ArrayList;
import cc.dreamcode.antylogout.libs.com.mongodb.annotations.NotThreadSafe;
import cc.dreamcode.antylogout.libs.org.bson.codecs.configuration.CodecRegistries;
import java.util.Arrays;
import cc.dreamcode.antylogout.libs.com.mongodb.client.model.mql.ExpressionCodecProvider;
import cc.dreamcode.antylogout.libs.org.bson.codecs.EnumCodecProvider;
import cc.dreamcode.antylogout.libs.org.bson.codecs.BsonCodecProvider;
import cc.dreamcode.antylogout.libs.org.bson.codecs.JsonObjectCodecProvider;
import cc.dreamcode.antylogout.libs.org.bson.codecs.jsr310.Jsr310CodecProvider;
import cc.dreamcode.antylogout.libs.com.mongodb.client.gridfs.codecs.GridFSFileCodecProvider;
import cc.dreamcode.antylogout.libs.com.mongodb.client.model.geojson.codecs.GeoJsonCodecProvider;
import cc.dreamcode.antylogout.libs.org.bson.codecs.MapCodecProvider;
import cc.dreamcode.antylogout.libs.org.bson.codecs.IterableCodecProvider;
import cc.dreamcode.antylogout.libs.org.bson.codecs.CollectionCodecProvider;
import cc.dreamcode.antylogout.libs.org.bson.Transformer;
import cc.dreamcode.antylogout.libs.org.bson.codecs.DocumentCodecProvider;
import cc.dreamcode.antylogout.libs.org.bson.codecs.BsonValueCodecProvider;
import cc.dreamcode.antylogout.libs.org.bson.codecs.ValueCodecProvider;
import cc.dreamcode.antylogout.libs.org.bson.codecs.configuration.CodecProvider;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.ProxySettings;
import java.util.concurrent.TimeUnit;
import java.util.Objects;
import java.util.Collections;
import cc.dreamcode.antylogout.libs.com.mongodb.lang.Nullable;
import cc.dreamcode.antylogout.libs.com.mongodb.spi.dns.InetAddressResolver;
import cc.dreamcode.antylogout.libs.com.mongodb.spi.dns.DnsClient;
import cc.dreamcode.antylogout.libs.org.bson.UuidRepresentation;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.SslSettings;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.ServerSettings;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.ConnectionPoolSettings;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.SocketSettings;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.ClusterSettings;
import cc.dreamcode.antylogout.libs.com.mongodb.event.CommandListener;
import java.util.List;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.TransportSettings;
import cc.dreamcode.antylogout.libs.org.bson.codecs.configuration.CodecRegistry;
import cc.dreamcode.antylogout.libs.com.mongodb.annotations.Immutable;

@Immutable
public final class MongoClientSettings
{
    private static final CodecRegistry DEFAULT_CODEC_REGISTRY;
    private final ReadPreference readPreference;
    private final WriteConcern writeConcern;
    private final boolean retryWrites;
    private final boolean retryReads;
    private final ReadConcern readConcern;
    private final MongoCredential credential;
    private final TransportSettings transportSettings;
    private final List<CommandListener> commandListeners;
    private final CodecRegistry codecRegistry;
    private final LoggerSettings loggerSettings;
    private final ClusterSettings clusterSettings;
    private final SocketSettings socketSettings;
    private final SocketSettings heartbeatSocketSettings;
    private final ConnectionPoolSettings connectionPoolSettings;
    private final ServerSettings serverSettings;
    private final SslSettings sslSettings;
    private final String applicationName;
    private final List<MongoCompressor> compressorList;
    private final UuidRepresentation uuidRepresentation;
    private final ServerApi serverApi;
    private final AutoEncryptionSettings autoEncryptionSettings;
    private final boolean heartbeatSocketTimeoutSetExplicitly;
    private final boolean heartbeatConnectTimeoutSetExplicitly;
    private final ContextProvider contextProvider;
    private final DnsClient dnsClient;
    private final InetAddressResolver inetAddressResolver;
    
    public static CodecRegistry getDefaultCodecRegistry() {
        return MongoClientSettings.DEFAULT_CODEC_REGISTRY;
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static Builder builder(final MongoClientSettings settings) {
        return new Builder(settings);
    }
    
    @Nullable
    public DnsClient getDnsClient() {
        return this.dnsClient;
    }
    
    @Nullable
    public InetAddressResolver getInetAddressResolver() {
        return this.inetAddressResolver;
    }
    
    public ReadPreference getReadPreference() {
        return this.readPreference;
    }
    
    @Nullable
    public MongoCredential getCredential() {
        return this.credential;
    }
    
    public WriteConcern getWriteConcern() {
        return this.writeConcern;
    }
    
    public boolean getRetryWrites() {
        return this.retryWrites;
    }
    
    public boolean getRetryReads() {
        return this.retryReads;
    }
    
    public ReadConcern getReadConcern() {
        return this.readConcern;
    }
    
    public CodecRegistry getCodecRegistry() {
        return this.codecRegistry;
    }
    
    @Nullable
    public TransportSettings getTransportSettings() {
        return this.transportSettings;
    }
    
    public List<CommandListener> getCommandListeners() {
        return (List<CommandListener>)Collections.unmodifiableList((List)this.commandListeners);
    }
    
    @Nullable
    public String getApplicationName() {
        return this.applicationName;
    }
    
    public List<MongoCompressor> getCompressorList() {
        return (List<MongoCompressor>)Collections.unmodifiableList((List)this.compressorList);
    }
    
    public UuidRepresentation getUuidRepresentation() {
        return this.uuidRepresentation;
    }
    
    @Nullable
    public ServerApi getServerApi() {
        return this.serverApi;
    }
    
    @Nullable
    public AutoEncryptionSettings getAutoEncryptionSettings() {
        return this.autoEncryptionSettings;
    }
    
    public LoggerSettings getLoggerSettings() {
        return this.loggerSettings;
    }
    
    public ClusterSettings getClusterSettings() {
        return this.clusterSettings;
    }
    
    public SslSettings getSslSettings() {
        return this.sslSettings;
    }
    
    public SocketSettings getSocketSettings() {
        return this.socketSettings;
    }
    
    public SocketSettings getHeartbeatSocketSettings() {
        return this.heartbeatSocketSettings;
    }
    
    public ConnectionPoolSettings getConnectionPoolSettings() {
        return this.connectionPoolSettings;
    }
    
    public ServerSettings getServerSettings() {
        return this.serverSettings;
    }
    
    @Nullable
    public ContextProvider getContextProvider() {
        return this.contextProvider;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final MongoClientSettings that = (MongoClientSettings)o;
        return this.retryWrites == that.retryWrites && this.retryReads == that.retryReads && this.heartbeatSocketTimeoutSetExplicitly == that.heartbeatSocketTimeoutSetExplicitly && this.heartbeatConnectTimeoutSetExplicitly == that.heartbeatConnectTimeoutSetExplicitly && Objects.equals((Object)this.readPreference, (Object)that.readPreference) && Objects.equals((Object)this.writeConcern, (Object)that.writeConcern) && Objects.equals((Object)this.readConcern, (Object)that.readConcern) && Objects.equals((Object)this.credential, (Object)that.credential) && Objects.equals((Object)this.transportSettings, (Object)that.transportSettings) && Objects.equals((Object)this.commandListeners, (Object)that.commandListeners) && Objects.equals((Object)this.codecRegistry, (Object)that.codecRegistry) && Objects.equals((Object)this.loggerSettings, (Object)that.loggerSettings) && Objects.equals((Object)this.clusterSettings, (Object)that.clusterSettings) && Objects.equals((Object)this.socketSettings, (Object)that.socketSettings) && Objects.equals((Object)this.heartbeatSocketSettings, (Object)that.heartbeatSocketSettings) && Objects.equals((Object)this.connectionPoolSettings, (Object)that.connectionPoolSettings) && Objects.equals((Object)this.serverSettings, (Object)that.serverSettings) && Objects.equals((Object)this.sslSettings, (Object)that.sslSettings) && Objects.equals((Object)this.applicationName, (Object)that.applicationName) && Objects.equals((Object)this.compressorList, (Object)that.compressorList) && this.uuidRepresentation == that.uuidRepresentation && Objects.equals((Object)this.serverApi, (Object)that.serverApi) && Objects.equals((Object)this.autoEncryptionSettings, (Object)that.autoEncryptionSettings) && Objects.equals((Object)this.dnsClient, (Object)that.dnsClient) && Objects.equals((Object)this.inetAddressResolver, (Object)that.inetAddressResolver) && Objects.equals((Object)this.contextProvider, (Object)that.contextProvider);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(new Object[] { this.readPreference, this.writeConcern, this.retryWrites, this.retryReads, this.readConcern, this.credential, this.transportSettings, this.commandListeners, this.codecRegistry, this.loggerSettings, this.clusterSettings, this.socketSettings, this.heartbeatSocketSettings, this.connectionPoolSettings, this.serverSettings, this.sslSettings, this.applicationName, this.compressorList, this.uuidRepresentation, this.serverApi, this.autoEncryptionSettings, this.heartbeatSocketTimeoutSetExplicitly, this.heartbeatConnectTimeoutSetExplicitly, this.dnsClient, this.inetAddressResolver, this.contextProvider });
    }
    
    @Override
    public String toString() {
        return "MongoClientSettings{readPreference=" + (Object)this.readPreference + ", writeConcern=" + (Object)this.writeConcern + ", retryWrites=" + this.retryWrites + ", retryReads=" + this.retryReads + ", readConcern=" + (Object)this.readConcern + ", credential=" + (Object)this.credential + ", transportSettings=" + (Object)this.transportSettings + ", commandListeners=" + (Object)this.commandListeners + ", codecRegistry=" + (Object)this.codecRegistry + ", loggerSettings=" + (Object)this.loggerSettings + ", clusterSettings=" + (Object)this.clusterSettings + ", socketSettings=" + (Object)this.socketSettings + ", heartbeatSocketSettings=" + (Object)this.heartbeatSocketSettings + ", connectionPoolSettings=" + (Object)this.connectionPoolSettings + ", serverSettings=" + (Object)this.serverSettings + ", sslSettings=" + (Object)this.sslSettings + ", applicationName='" + this.applicationName + '\'' + ", compressorList=" + (Object)this.compressorList + ", uuidRepresentation=" + (Object)this.uuidRepresentation + ", serverApi=" + (Object)this.serverApi + ", autoEncryptionSettings=" + (Object)this.autoEncryptionSettings + ", dnsClient=" + (Object)this.dnsClient + ", inetAddressResolver=" + (Object)this.inetAddressResolver + ", contextProvider=" + (Object)this.contextProvider + '}';
    }
    
    private MongoClientSettings(final Builder builder) {
        this.readPreference = builder.readPreference;
        this.writeConcern = builder.writeConcern;
        this.retryWrites = builder.retryWrites;
        this.retryReads = builder.retryReads;
        this.readConcern = builder.readConcern;
        this.credential = builder.credential;
        this.transportSettings = builder.transportSettings;
        this.codecRegistry = builder.codecRegistry;
        this.commandListeners = builder.commandListeners;
        this.applicationName = builder.applicationName;
        this.loggerSettings = builder.loggerSettingsBuilder.build();
        this.clusterSettings = builder.clusterSettingsBuilder.build();
        this.serverSettings = builder.serverSettingsBuilder.build();
        this.socketSettings = builder.socketSettingsBuilder.build();
        this.connectionPoolSettings = builder.connectionPoolSettingsBuilder.build();
        this.sslSettings = builder.sslSettingsBuilder.build();
        this.compressorList = builder.compressorList;
        this.uuidRepresentation = builder.uuidRepresentation;
        this.serverApi = builder.serverApi;
        this.dnsClient = builder.dnsClient;
        this.inetAddressResolver = builder.inetAddressResolver;
        this.autoEncryptionSettings = builder.autoEncryptionSettings;
        this.heartbeatSocketSettings = SocketSettings.builder().readTimeout((builder.heartbeatSocketTimeoutMS == 0) ? this.socketSettings.getConnectTimeout(TimeUnit.MILLISECONDS) : ((long)builder.heartbeatSocketTimeoutMS), TimeUnit.MILLISECONDS).connectTimeout((builder.heartbeatConnectTimeoutMS == 0) ? this.socketSettings.getConnectTimeout(TimeUnit.MILLISECONDS) : ((long)builder.heartbeatConnectTimeoutMS), TimeUnit.MILLISECONDS).applyToProxySettings(proxyBuilder -> proxyBuilder.applySettings(this.socketSettings.getProxySettings())).build();
        this.heartbeatSocketTimeoutSetExplicitly = (builder.heartbeatSocketTimeoutMS != 0);
        this.heartbeatConnectTimeoutSetExplicitly = (builder.heartbeatConnectTimeoutMS != 0);
        this.contextProvider = builder.contextProvider;
    }
    
    static {
        DEFAULT_CODEC_REGISTRY = CodecRegistries.fromProviders((List<? extends CodecProvider>)Arrays.asList((Object[])new CodecProvider[] { new ValueCodecProvider(), new BsonValueCodecProvider(), new DBRefCodecProvider(), new DBObjectCodecProvider(), new DocumentCodecProvider(new DocumentToDBRefTransformer()), new CollectionCodecProvider(new DocumentToDBRefTransformer()), new IterableCodecProvider(new DocumentToDBRefTransformer()), new MapCodecProvider(new DocumentToDBRefTransformer()), new GeoJsonCodecProvider(), new GridFSFileCodecProvider(), new Jsr310CodecProvider(), new JsonObjectCodecProvider(), new BsonCodecProvider(), new EnumCodecProvider(), new ExpressionCodecProvider(), new Jep395RecordCodecProvider(), new KotlinCodecProvider() }));
    }
    
    @NotThreadSafe
    public static final class Builder
    {
        private ReadPreference readPreference;
        private WriteConcern writeConcern;
        private boolean retryWrites;
        private boolean retryReads;
        private ReadConcern readConcern;
        private CodecRegistry codecRegistry;
        private TransportSettings transportSettings;
        private List<CommandListener> commandListeners;
        private final LoggerSettings.Builder loggerSettingsBuilder;
        private final ClusterSettings.Builder clusterSettingsBuilder;
        private final SocketSettings.Builder socketSettingsBuilder;
        private final ConnectionPoolSettings.Builder connectionPoolSettingsBuilder;
        private final ServerSettings.Builder serverSettingsBuilder;
        private final SslSettings.Builder sslSettingsBuilder;
        private MongoCredential credential;
        private String applicationName;
        private List<MongoCompressor> compressorList;
        private UuidRepresentation uuidRepresentation;
        private ServerApi serverApi;
        private AutoEncryptionSettings autoEncryptionSettings;
        private int heartbeatConnectTimeoutMS;
        private int heartbeatSocketTimeoutMS;
        private ContextProvider contextProvider;
        private DnsClient dnsClient;
        private InetAddressResolver inetAddressResolver;
        
        private Builder() {
            this.readPreference = ReadPreference.primary();
            this.writeConcern = WriteConcern.ACKNOWLEDGED;
            this.retryWrites = true;
            this.retryReads = true;
            this.readConcern = ReadConcern.DEFAULT;
            this.codecRegistry = MongoClientSettings.getDefaultCodecRegistry();
            this.commandListeners = (List<CommandListener>)new ArrayList();
            this.loggerSettingsBuilder = LoggerSettings.builder();
            this.clusterSettingsBuilder = ClusterSettings.builder();
            this.socketSettingsBuilder = SocketSettings.builder();
            this.connectionPoolSettingsBuilder = ConnectionPoolSettings.builder();
            this.serverSettingsBuilder = ServerSettings.builder();
            this.sslSettingsBuilder = SslSettings.builder();
            this.compressorList = (List<MongoCompressor>)Collections.emptyList();
            this.uuidRepresentation = UuidRepresentation.UNSPECIFIED;
        }
        
        private Builder(final MongoClientSettings settings) {
            this.readPreference = ReadPreference.primary();
            this.writeConcern = WriteConcern.ACKNOWLEDGED;
            this.retryWrites = true;
            this.retryReads = true;
            this.readConcern = ReadConcern.DEFAULT;
            this.codecRegistry = MongoClientSettings.getDefaultCodecRegistry();
            this.commandListeners = (List<CommandListener>)new ArrayList();
            this.loggerSettingsBuilder = LoggerSettings.builder();
            this.clusterSettingsBuilder = ClusterSettings.builder();
            this.socketSettingsBuilder = SocketSettings.builder();
            this.connectionPoolSettingsBuilder = ConnectionPoolSettings.builder();
            this.serverSettingsBuilder = ServerSettings.builder();
            this.sslSettingsBuilder = SslSettings.builder();
            this.compressorList = (List<MongoCompressor>)Collections.emptyList();
            this.uuidRepresentation = UuidRepresentation.UNSPECIFIED;
            Assertions.notNull("settings", settings);
            this.applicationName = settings.getApplicationName();
            this.commandListeners = (List<CommandListener>)new ArrayList((Collection)settings.getCommandListeners());
            this.compressorList = (List<MongoCompressor>)new ArrayList((Collection)settings.getCompressorList());
            this.codecRegistry = settings.getCodecRegistry();
            this.readPreference = settings.getReadPreference();
            this.writeConcern = settings.getWriteConcern();
            this.retryWrites = settings.getRetryWrites();
            this.retryReads = settings.getRetryReads();
            this.readConcern = settings.getReadConcern();
            this.credential = settings.getCredential();
            this.uuidRepresentation = settings.getUuidRepresentation();
            this.serverApi = settings.getServerApi();
            this.dnsClient = settings.getDnsClient();
            this.inetAddressResolver = settings.getInetAddressResolver();
            this.transportSettings = settings.getTransportSettings();
            this.autoEncryptionSettings = settings.getAutoEncryptionSettings();
            this.contextProvider = settings.getContextProvider();
            this.loggerSettingsBuilder.applySettings(settings.getLoggerSettings());
            this.clusterSettingsBuilder.applySettings(settings.getClusterSettings());
            this.serverSettingsBuilder.applySettings(settings.getServerSettings());
            this.socketSettingsBuilder.applySettings(settings.getSocketSettings());
            this.connectionPoolSettingsBuilder.applySettings(settings.getConnectionPoolSettings());
            this.sslSettingsBuilder.applySettings(settings.getSslSettings());
            if (settings.heartbeatConnectTimeoutSetExplicitly) {
                this.heartbeatConnectTimeoutMS = settings.heartbeatSocketSettings.getConnectTimeout(TimeUnit.MILLISECONDS);
            }
            if (settings.heartbeatSocketTimeoutSetExplicitly) {
                this.heartbeatSocketTimeoutMS = settings.heartbeatSocketSettings.getReadTimeout(TimeUnit.MILLISECONDS);
            }
        }
        
        public Builder applyConnectionString(final ConnectionString connectionString) {
            if (connectionString.getApplicationName() != null) {
                this.applicationName = connectionString.getApplicationName();
            }
            this.clusterSettingsBuilder.applyConnectionString(connectionString);
            if (!connectionString.getCompressorList().isEmpty()) {
                this.compressorList = connectionString.getCompressorList();
            }
            this.connectionPoolSettingsBuilder.applyConnectionString(connectionString);
            if (connectionString.getCredential() != null) {
                this.credential = connectionString.getCredential();
            }
            if (connectionString.getReadConcern() != null) {
                this.readConcern = connectionString.getReadConcern();
            }
            if (connectionString.getReadPreference() != null) {
                this.readPreference = connectionString.getReadPreference();
            }
            final Boolean retryWritesValue = connectionString.getRetryWritesValue();
            if (retryWritesValue != null) {
                this.retryWrites = retryWritesValue;
            }
            final Boolean retryReadsValue = connectionString.getRetryReads();
            if (retryReadsValue != null) {
                this.retryReads = retryReadsValue;
            }
            if (connectionString.getUuidRepresentation() != null) {
                this.uuidRepresentation = connectionString.getUuidRepresentation();
            }
            this.serverSettingsBuilder.applyConnectionString(connectionString);
            this.socketSettingsBuilder.applyConnectionString(connectionString);
            this.sslSettingsBuilder.applyConnectionString(connectionString);
            if (connectionString.getWriteConcern() != null) {
                this.writeConcern = connectionString.getWriteConcern();
            }
            return this;
        }
        
        public Builder applyToLoggerSettings(final Block<LoggerSettings.Builder> block) {
            Assertions.notNull("block", block).apply(this.loggerSettingsBuilder);
            return this;
        }
        
        public Builder applyToClusterSettings(final Block<ClusterSettings.Builder> block) {
            Assertions.notNull("block", block).apply(this.clusterSettingsBuilder);
            return this;
        }
        
        public Builder applyToSocketSettings(final Block<SocketSettings.Builder> block) {
            Assertions.notNull("block", block).apply(this.socketSettingsBuilder);
            return this;
        }
        
        public Builder applyToConnectionPoolSettings(final Block<ConnectionPoolSettings.Builder> block) {
            Assertions.notNull("block", block).apply(this.connectionPoolSettingsBuilder);
            return this;
        }
        
        public Builder applyToServerSettings(final Block<ServerSettings.Builder> block) {
            Assertions.notNull("block", block).apply(this.serverSettingsBuilder);
            return this;
        }
        
        public Builder applyToSslSettings(final Block<SslSettings.Builder> block) {
            Assertions.notNull("block", block).apply(this.sslSettingsBuilder);
            return this;
        }
        
        public Builder readPreference(final ReadPreference readPreference) {
            this.readPreference = Assertions.notNull("readPreference", readPreference);
            return this;
        }
        
        public Builder writeConcern(final WriteConcern writeConcern) {
            this.writeConcern = Assertions.notNull("writeConcern", writeConcern);
            return this;
        }
        
        public Builder retryWrites(final boolean retryWrites) {
            this.retryWrites = retryWrites;
            return this;
        }
        
        public Builder retryReads(final boolean retryReads) {
            this.retryReads = retryReads;
            return this;
        }
        
        public Builder readConcern(final ReadConcern readConcern) {
            this.readConcern = Assertions.notNull("readConcern", readConcern);
            return this;
        }
        
        public Builder credential(final MongoCredential credential) {
            this.credential = Assertions.notNull("credential", credential);
            return this;
        }
        
        public Builder codecRegistry(final CodecRegistry codecRegistry) {
            this.codecRegistry = Assertions.notNull("codecRegistry", codecRegistry);
            return this;
        }
        
        public Builder transportSettings(final TransportSettings transportSettings) {
            this.transportSettings = Assertions.notNull("transportSettings", transportSettings);
            return this;
        }
        
        public Builder addCommandListener(final CommandListener commandListener) {
            Assertions.notNull("commandListener", commandListener);
            this.commandListeners.add((Object)commandListener);
            return this;
        }
        
        public Builder commandListenerList(final List<CommandListener> commandListeners) {
            Assertions.notNull("commandListeners", commandListeners);
            this.commandListeners = (List<CommandListener>)new ArrayList((Collection)commandListeners);
            return this;
        }
        
        public Builder applicationName(@Nullable final String applicationName) {
            if (applicationName != null) {
                Assertions.isTrueArgument("applicationName UTF-8 encoding length <= 128", applicationName.getBytes(StandardCharsets.UTF_8).length <= 128);
            }
            this.applicationName = applicationName;
            return this;
        }
        
        public Builder compressorList(final List<MongoCompressor> compressorList) {
            Assertions.notNull("compressorList", compressorList);
            this.compressorList = (List<MongoCompressor>)new ArrayList((Collection)compressorList);
            return this;
        }
        
        public Builder uuidRepresentation(final UuidRepresentation uuidRepresentation) {
            this.uuidRepresentation = Assertions.notNull("uuidRepresentation", uuidRepresentation);
            return this;
        }
        
        public Builder serverApi(final ServerApi serverApi) {
            this.serverApi = Assertions.notNull("serverApi", serverApi);
            return this;
        }
        
        public Builder autoEncryptionSettings(@Nullable final AutoEncryptionSettings autoEncryptionSettings) {
            this.autoEncryptionSettings = autoEncryptionSettings;
            return this;
        }
        
        public Builder contextProvider(@Nullable final ContextProvider contextProvider) {
            this.contextProvider = contextProvider;
            return this;
        }
        
        public Builder dnsClient(@Nullable final DnsClient dnsClient) {
            this.dnsClient = dnsClient;
            return this;
        }
        
        public Builder inetAddressResolver(@Nullable final InetAddressResolver inetAddressResolver) {
            this.inetAddressResolver = inetAddressResolver;
            return this;
        }
        
        Builder heartbeatConnectTimeoutMS(final int heartbeatConnectTimeoutMS) {
            this.heartbeatConnectTimeoutMS = heartbeatConnectTimeoutMS;
            return this;
        }
        
        Builder heartbeatSocketTimeoutMS(final int heartbeatSocketTimeoutMS) {
            this.heartbeatSocketTimeoutMS = heartbeatSocketTimeoutMS;
            return this;
        }
        
        public MongoClientSettings build() {
            return new MongoClientSettings(this, null);
        }
    }
}
