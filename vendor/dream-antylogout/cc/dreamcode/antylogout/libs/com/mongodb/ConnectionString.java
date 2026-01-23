package cc.dreamcode.antylogout.libs.com.mongodb;

import java.util.LinkedHashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.diagnostics.logging.Loggers;
import java.util.HashSet;
import java.util.Objects;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.OidcAuthenticator;
import java.util.ArrayList;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.ServerMonitoringModeUtil;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;
import java.util.Collection;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.dns.DefaultDnsResolver;
import java.util.Collections;
import java.util.Arrays;
import cc.dreamcode.antylogout.libs.com.mongodb.lang.Nullable;
import cc.dreamcode.antylogout.libs.com.mongodb.spi.dns.DnsClient;
import cc.dreamcode.antylogout.libs.org.bson.UuidRepresentation;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.ServerMonitoringMode;
import java.util.List;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.diagnostics.logging.Logger;
import java.util.Set;

public class ConnectionString
{
    private static final String MONGODB_PREFIX = "mongodb://";
    private static final String MONGODB_SRV_PREFIX = "mongodb+srv://";
    private static final Set<String> ALLOWED_OPTIONS_IN_TXT_RECORD;
    private static final Logger LOGGER;
    private static final List<String> MECHANISM_KEYS_DISALLOWED_IN_CONNECTION_STRING;
    private final MongoCredential credential;
    private final boolean isSrvProtocol;
    private final List<String> hosts;
    private final String database;
    private final String collection;
    private final String connectionString;
    private Integer srvMaxHosts;
    private String srvServiceName;
    private Boolean directConnection;
    private Boolean loadBalanced;
    private ReadPreference readPreference;
    private WriteConcern writeConcern;
    private Boolean retryWrites;
    private Boolean retryReads;
    private ReadConcern readConcern;
    private Integer minConnectionPoolSize;
    private Integer maxConnectionPoolSize;
    private Integer maxWaitTime;
    private Integer maxConnectionIdleTime;
    private Integer maxConnectionLifeTime;
    private Integer maxConnecting;
    private Integer connectTimeout;
    private Integer socketTimeout;
    private Boolean sslEnabled;
    private Boolean sslInvalidHostnameAllowed;
    private String proxyHost;
    private Integer proxyPort;
    private String proxyUsername;
    private String proxyPassword;
    private String requiredReplicaSetName;
    private Integer serverSelectionTimeout;
    private Integer localThreshold;
    private Integer heartbeatFrequency;
    private ServerMonitoringMode serverMonitoringMode;
    private String applicationName;
    private List<MongoCompressor> compressorList;
    private UuidRepresentation uuidRepresentation;
    private static final Set<String> GENERAL_OPTIONS_KEYS;
    private static final Set<String> AUTH_KEYS;
    private static final Set<String> READ_PREFERENCE_KEYS;
    private static final Set<String> WRITE_CONCERN_KEYS;
    private static final Set<String> COMPRESSOR_KEYS;
    private static final Set<String> ALL_KEYS;
    private static final Set<String> TRUE_VALUES;
    private static final Set<String> FALSE_VALUES;
    
    public ConnectionString(final String connectionString) {
        this(connectionString, null);
    }
    
    public ConnectionString(final String connectionString, @Nullable final DnsClient dnsClient) {
        this.connectionString = connectionString;
        final boolean isMongoDBProtocol = connectionString.startsWith("mongodb://");
        this.isSrvProtocol = connectionString.startsWith("mongodb+srv://");
        if (!isMongoDBProtocol && !this.isSrvProtocol) {
            throw new IllegalArgumentException(String.format("The connection string is invalid. Connection strings must start with either '%s' or '%s", new Object[] { "mongodb://", "mongodb+srv://" }));
        }
        String unprocessedConnectionString;
        if (isMongoDBProtocol) {
            unprocessedConnectionString = connectionString.substring("mongodb://".length());
        }
        else {
            unprocessedConnectionString = connectionString.substring("mongodb+srv://".length());
        }
        int idx = unprocessedConnectionString.indexOf("/");
        String userAndHostInformation;
        if (idx == -1) {
            if (unprocessedConnectionString.contains((CharSequence)"?")) {
                throw new IllegalArgumentException("The connection string contains options without trailing slash");
            }
            userAndHostInformation = unprocessedConnectionString;
            unprocessedConnectionString = "";
        }
        else {
            userAndHostInformation = unprocessedConnectionString.substring(0, idx);
            unprocessedConnectionString = unprocessedConnectionString.substring(idx + 1);
        }
        String userName = null;
        char[] password = null;
        idx = userAndHostInformation.lastIndexOf("@");
        String hostIdentifier;
        if (idx > 0) {
            final String userInfo = userAndHostInformation.substring(0, idx).replace((CharSequence)"+", (CharSequence)"%2B");
            hostIdentifier = userAndHostInformation.substring(idx + 1);
            final int colonCount = this.countOccurrences(userInfo, ":");
            if (userInfo.contains((CharSequence)"@") || colonCount > 1) {
                throw new IllegalArgumentException("The connection string contains invalid user information. If the username or password contains a colon (:) or an at-sign (@) then it must be urlencoded");
            }
            if (colonCount == 0) {
                userName = this.urldecode(userInfo);
            }
            else {
                idx = userInfo.indexOf(":");
                if (idx == 0) {
                    throw new IllegalArgumentException("No username is provided in the connection string");
                }
                userName = this.urldecode(userInfo.substring(0, idx));
                password = this.urldecode(userInfo.substring(idx + 1), true).toCharArray();
            }
        }
        else {
            if (idx == 0) {
                throw new IllegalArgumentException("The connection string contains an at-sign (@) without a user name");
            }
            hostIdentifier = userAndHostInformation;
        }
        final List<String> unresolvedHosts = (List<String>)Collections.unmodifiableList((List)this.parseHosts((List<String>)Arrays.asList((Object[])hostIdentifier.split(","))));
        if (this.isSrvProtocol) {
            if (unresolvedHosts.size() > 1) {
                throw new IllegalArgumentException("Only one host allowed when using mongodb+srv protocol");
            }
            if (((String)unresolvedHosts.get(0)).contains((CharSequence)":")) {
                throw new IllegalArgumentException("Host for when using mongodb+srv protocol can not contain a port");
            }
        }
        this.hosts = unresolvedHosts;
        idx = unprocessedConnectionString.indexOf("?");
        String nsPart;
        if (idx == -1) {
            nsPart = unprocessedConnectionString;
            unprocessedConnectionString = "";
        }
        else {
            nsPart = unprocessedConnectionString.substring(0, idx);
            unprocessedConnectionString = unprocessedConnectionString.substring(idx + 1);
        }
        if (nsPart.length() > 0) {
            nsPart = this.urldecode(nsPart);
            idx = nsPart.indexOf(".");
            if (idx < 0) {
                this.database = nsPart;
                this.collection = null;
            }
            else {
                this.database = nsPart.substring(0, idx);
                this.collection = nsPart.substring(idx + 1);
            }
            MongoNamespace.checkDatabaseNameValidity(this.database);
        }
        else {
            this.database = null;
            this.collection = null;
        }
        final String txtRecordsQueryParameters = this.isSrvProtocol ? new DefaultDnsResolver(dnsClient).resolveAdditionalQueryParametersFromTxtRecords((String)unresolvedHosts.get(0)) : "";
        final String connectionStringQueryParameters = unprocessedConnectionString;
        final Map<String, List<String>> connectionStringOptionsMap = this.parseOptions(connectionStringQueryParameters);
        final Map<String, List<String>> txtRecordsOptionsMap = this.parseOptions(txtRecordsQueryParameters);
        if (!ConnectionString.ALLOWED_OPTIONS_IN_TXT_RECORD.containsAll((Collection)txtRecordsOptionsMap.keySet())) {
            throw new MongoConfigurationException(String.format("A TXT record is only permitted to contain the keys %s, but the TXT record for '%s' contains the keys %s", new Object[] { ConnectionString.ALLOWED_OPTIONS_IN_TXT_RECORD, unresolvedHosts.get(0), txtRecordsOptionsMap.keySet() }));
        }
        final Map<String, List<String>> combinedOptionsMaps = this.combineOptionsMaps(txtRecordsOptionsMap, connectionStringOptionsMap);
        if (this.isSrvProtocol && !combinedOptionsMaps.containsKey((Object)"tls") && !combinedOptionsMaps.containsKey((Object)"ssl")) {
            combinedOptionsMaps.put((Object)"tls", (Object)Collections.singletonList((Object)"true"));
        }
        this.translateOptions(combinedOptionsMaps);
        if (!this.isSrvProtocol && this.srvMaxHosts != null) {
            throw new IllegalArgumentException("srvMaxHosts can only be specified with mongodb+srv protocol");
        }
        if (!this.isSrvProtocol && this.srvServiceName != null) {
            throw new IllegalArgumentException("srvServiceName can only be specified with mongodb+srv protocol");
        }
        if (this.directConnection != null && this.directConnection) {
            if (this.isSrvProtocol) {
                throw new IllegalArgumentException("Direct connections are not supported when using mongodb+srv protocol");
            }
            if (this.hosts.size() > 1) {
                throw new IllegalArgumentException("Direct connections are not supported when using multiple hosts");
            }
        }
        if (this.loadBalanced != null && this.loadBalanced) {
            if (this.directConnection != null && this.directConnection) {
                throw new IllegalArgumentException("directConnection=true can not be specified with loadBalanced=true");
            }
            if (this.requiredReplicaSetName != null) {
                throw new IllegalArgumentException("replicaSet can not be specified with loadBalanced=true");
            }
            if (this.hosts.size() > 1) {
                throw new IllegalArgumentException("Only one host can be specified with loadBalanced=true");
            }
            if (this.srvMaxHosts != null && this.srvMaxHosts > 0) {
                throw new IllegalArgumentException("srvMaxHosts can not be specified with loadBalanced=true");
            }
        }
        if (this.requiredReplicaSetName != null && this.srvMaxHosts != null && this.srvMaxHosts > 0) {
            throw new IllegalArgumentException("srvMaxHosts can not be specified with replica set name");
        }
        this.validateProxyParameters();
        this.credential = this.createCredentials(combinedOptionsMaps, userName, password);
        this.warnOnUnsupportedOptions(combinedOptionsMaps);
    }
    
    private Map<String, List<String>> combineOptionsMaps(final Map<String, List<String>> txtRecordsOptionsMap, final Map<String, List<String>> connectionStringOptionsMap) {
        final Map<String, List<String>> combinedOptionsMaps = (Map<String, List<String>>)new HashMap((Map)txtRecordsOptionsMap);
        combinedOptionsMaps.putAll((Map)connectionStringOptionsMap);
        return combinedOptionsMaps;
    }
    
    private void warnOnUnsupportedOptions(final Map<String, List<String>> optionsMap) {
        for (final String key : optionsMap.keySet()) {
            if (!ConnectionString.ALL_KEYS.contains((Object)key) && ConnectionString.LOGGER.isWarnEnabled()) {
                ConnectionString.LOGGER.warn(String.format("Connection string contains unsupported option '%s'.", new Object[] { key }));
            }
        }
    }
    
    private void translateOptions(final Map<String, List<String>> optionsMap) {
        boolean tlsInsecureSet = false;
        boolean tlsAllowInvalidHostnamesSet = false;
        for (final String key : ConnectionString.GENERAL_OPTIONS_KEYS) {
            final String value = this.getLastValue(optionsMap, key);
            if (value == null) {
                continue;
            }
            final String s = key;
            int n = -1;
            switch (s.hashCode()) {
                case -1691284607: {
                    if (s.equals((Object)"maxpoolsize")) {
                        n = 0;
                        break;
                    }
                    break;
                }
                case 1926250095: {
                    if (s.equals((Object)"minpoolsize")) {
                        n = 1;
                        break;
                    }
                    break;
                }
                case -571921813: {
                    if (s.equals((Object)"maxidletimems")) {
                        n = 2;
                        break;
                    }
                    break;
                }
                case 819239827: {
                    if (s.equals((Object)"maxlifetimems")) {
                        n = 3;
                        break;
                    }
                    break;
                }
                case 353482524: {
                    if (s.equals((Object)"maxconnecting")) {
                        n = 4;
                        break;
                    }
                    break;
                }
                case 1524396427: {
                    if (s.equals((Object)"waitqueuetimeoutms")) {
                        n = 5;
                        break;
                    }
                    break;
                }
                case 218461469: {
                    if (s.equals((Object)"connecttimeoutms")) {
                        n = 6;
                        break;
                    }
                    break;
                }
                case 859445428: {
                    if (s.equals((Object)"sockettimeoutms")) {
                        n = 7;
                        break;
                    }
                    break;
                }
                case -475408106: {
                    if (s.equals((Object)"proxyhost")) {
                        n = 8;
                        break;
                    }
                    break;
                }
                case -475169809: {
                    if (s.equals((Object)"proxyport")) {
                        n = 9;
                        break;
                    }
                    break;
                }
                case -595131068: {
                    if (s.equals((Object)"proxyusername")) {
                        n = 10;
                        break;
                    }
                    break;
                }
                case 887568137: {
                    if (s.equals((Object)"proxypassword")) {
                        n = 11;
                        break;
                    }
                    break;
                }
                case 410888631: {
                    if (s.equals((Object)"tlsallowinvalidhostnames")) {
                        n = 12;
                        break;
                    }
                    break;
                }
                case -827816886: {
                    if (s.equals((Object)"sslinvalidhostnameallowed")) {
                        n = 13;
                        break;
                    }
                    break;
                }
                case 1100926679: {
                    if (s.equals((Object)"tlsinsecure")) {
                        n = 14;
                        break;
                    }
                    break;
                }
                case 114188: {
                    if (s.equals((Object)"ssl")) {
                        n = 15;
                        break;
                    }
                    break;
                }
                case 114939: {
                    if (s.equals((Object)"tls")) {
                        n = 16;
                        break;
                    }
                    break;
                }
                case -1004105558: {
                    if (s.equals((Object)"replicaset")) {
                        n = 17;
                        break;
                    }
                    break;
                }
                case -908890758: {
                    if (s.equals((Object)"readconcernlevel")) {
                        n = 18;
                        break;
                    }
                    break;
                }
                case 75372350: {
                    if (s.equals((Object)"serverselectiontimeoutms")) {
                        n = 19;
                        break;
                    }
                    break;
                }
                case 1241735942: {
                    if (s.equals((Object)"localthresholdms")) {
                        n = 20;
                        break;
                    }
                    break;
                }
                case -1901656698: {
                    if (s.equals((Object)"heartbeatfrequencyms")) {
                        n = 21;
                        break;
                    }
                    break;
                }
                case -1827850258: {
                    if (s.equals((Object)"servermonitoringmode")) {
                        n = 22;
                        break;
                    }
                    break;
                }
                case -793183188: {
                    if (s.equals((Object)"appname")) {
                        n = 23;
                        break;
                    }
                    break;
                }
                case -227268324: {
                    if (s.equals((Object)"retrywrites")) {
                        n = 24;
                        break;
                    }
                    break;
                }
                case -1120722955: {
                    if (s.equals((Object)"retryreads")) {
                        n = 25;
                        break;
                    }
                    break;
                }
                case 718842184: {
                    if (s.equals((Object)"uuidrepresentation")) {
                        n = 26;
                        break;
                    }
                    break;
                }
                case -377025657: {
                    if (s.equals((Object)"directconnection")) {
                        n = 27;
                        break;
                    }
                    break;
                }
                case -705696786: {
                    if (s.equals((Object)"loadbalanced")) {
                        n = 28;
                        break;
                    }
                    break;
                }
                case 466333662: {
                    if (s.equals((Object)"srvmaxhosts")) {
                        n = 29;
                        break;
                    }
                    break;
                }
                case -973631383: {
                    if (s.equals((Object)"srvservicename")) {
                        n = 30;
                        break;
                    }
                    break;
                }
            }
            switch (n) {
                case 0: {
                    this.maxConnectionPoolSize = this.parseInteger(value, "maxpoolsize");
                    continue;
                }
                case 1: {
                    this.minConnectionPoolSize = this.parseInteger(value, "minpoolsize");
                    continue;
                }
                case 2: {
                    this.maxConnectionIdleTime = this.parseInteger(value, "maxidletimems");
                    continue;
                }
                case 3: {
                    this.maxConnectionLifeTime = this.parseInteger(value, "maxlifetimems");
                    continue;
                }
                case 4: {
                    this.maxConnecting = this.parseInteger(value, "maxConnecting");
                    continue;
                }
                case 5: {
                    this.maxWaitTime = this.parseInteger(value, "waitqueuetimeoutms");
                    continue;
                }
                case 6: {
                    this.connectTimeout = this.parseInteger(value, "connecttimeoutms");
                    continue;
                }
                case 7: {
                    this.socketTimeout = this.parseInteger(value, "sockettimeoutms");
                    continue;
                }
                case 8: {
                    this.proxyHost = value;
                    continue;
                }
                case 9: {
                    this.proxyPort = this.parseInteger(value, "proxyPort");
                    continue;
                }
                case 10: {
                    this.proxyUsername = value;
                    continue;
                }
                case 11: {
                    this.proxyPassword = value;
                    continue;
                }
                case 12: {
                    this.sslInvalidHostnameAllowed = this.parseBoolean(value, "tlsAllowInvalidHostnames");
                    tlsAllowInvalidHostnamesSet = true;
                    continue;
                }
                case 13: {
                    this.sslInvalidHostnameAllowed = this.parseBoolean(value, "sslinvalidhostnameallowed");
                    tlsAllowInvalidHostnamesSet = true;
                    continue;
                }
                case 14: {
                    this.sslInvalidHostnameAllowed = this.parseBoolean(value, "tlsinsecure");
                    tlsInsecureSet = true;
                    continue;
                }
                case 15: {
                    this.initializeSslEnabled("ssl", value);
                    continue;
                }
                case 16: {
                    this.initializeSslEnabled("tls", value);
                    continue;
                }
                case 17: {
                    this.requiredReplicaSetName = value;
                    continue;
                }
                case 18: {
                    this.readConcern = new ReadConcern(ReadConcernLevel.fromString(value));
                    continue;
                }
                case 19: {
                    this.serverSelectionTimeout = this.parseInteger(value, "serverselectiontimeoutms");
                    continue;
                }
                case 20: {
                    this.localThreshold = this.parseInteger(value, "localthresholdms");
                    continue;
                }
                case 21: {
                    this.heartbeatFrequency = this.parseInteger(value, "heartbeatfrequencyms");
                    continue;
                }
                case 22: {
                    this.serverMonitoringMode = ServerMonitoringModeUtil.fromString(value);
                    continue;
                }
                case 23: {
                    this.applicationName = value;
                    continue;
                }
                case 24: {
                    this.retryWrites = this.parseBoolean(value, "retrywrites");
                    continue;
                }
                case 25: {
                    this.retryReads = this.parseBoolean(value, "retryreads");
                    continue;
                }
                case 26: {
                    this.uuidRepresentation = this.createUuidRepresentation(value);
                    continue;
                }
                case 27: {
                    this.directConnection = this.parseBoolean(value, "directconnection");
                    continue;
                }
                case 28: {
                    this.loadBalanced = this.parseBoolean(value, "loadbalanced");
                    continue;
                }
                case 29: {
                    this.srvMaxHosts = this.parseInteger(value, "srvmaxhosts");
                    if (this.srvMaxHosts < 0) {
                        throw new IllegalArgumentException("srvMaxHosts must be >= 0");
                    }
                    continue;
                }
                case 30: {
                    this.srvServiceName = value;
                    continue;
                }
            }
        }
        if (tlsInsecureSet && tlsAllowInvalidHostnamesSet) {
            throw new IllegalArgumentException("tlsAllowInvalidHostnames or sslInvalidHostnameAllowed set along with tlsInsecure is not allowed");
        }
        this.writeConcern = this.createWriteConcern(optionsMap);
        this.readPreference = this.createReadPreference(optionsMap);
        this.compressorList = this.createCompressors(optionsMap);
    }
    
    private void initializeSslEnabled(final String key, final String value) {
        final Boolean booleanValue = this.parseBoolean(value, key);
        if (this.sslEnabled != null && !this.sslEnabled.equals((Object)booleanValue)) {
            throw new IllegalArgumentException("Conflicting tls and ssl parameter values are not allowed");
        }
        this.sslEnabled = booleanValue;
    }
    
    private List<MongoCompressor> createCompressors(final Map<String, List<String>> optionsMap) {
        String compressors = "";
        Integer zlibCompressionLevel = null;
        for (final String key : ConnectionString.COMPRESSOR_KEYS) {
            final String value = this.getLastValue(optionsMap, key);
            if (value == null) {
                continue;
            }
            if (key.equals((Object)"compressors")) {
                compressors = value;
            }
            else {
                if (!key.equals((Object)"zlibcompressionlevel")) {
                    continue;
                }
                zlibCompressionLevel = Integer.parseInt(value);
            }
        }
        return this.buildCompressors(compressors, zlibCompressionLevel);
    }
    
    private List<MongoCompressor> buildCompressors(final String compressors, @Nullable final Integer zlibCompressionLevel) {
        final List<MongoCompressor> compressorsList = (List<MongoCompressor>)new ArrayList();
        for (final String cur : compressors.split(",")) {
            if (cur.equals((Object)"zlib")) {
                MongoCompressor zlibCompressor = MongoCompressor.createZlibCompressor();
                if (zlibCompressionLevel != null) {
                    zlibCompressor = zlibCompressor.withProperty("LEVEL", zlibCompressionLevel);
                }
                compressorsList.add((Object)zlibCompressor);
            }
            else if (cur.equals((Object)"snappy")) {
                compressorsList.add((Object)MongoCompressor.createSnappyCompressor());
            }
            else if (cur.equals((Object)"zstd")) {
                compressorsList.add((Object)MongoCompressor.createZstdCompressor());
            }
            else if (!cur.isEmpty()) {
                throw new IllegalArgumentException("Unsupported compressor '" + cur + "'");
            }
        }
        return (List<MongoCompressor>)Collections.unmodifiableList((List)compressorsList);
    }
    
    @Nullable
    private WriteConcern createWriteConcern(final Map<String, List<String>> optionsMap) {
        String w = null;
        Integer wTimeout = null;
        Boolean safe = null;
        Boolean journal = null;
        for (final String key : ConnectionString.WRITE_CONCERN_KEYS) {
            final String value = this.getLastValue(optionsMap, key);
            if (value == null) {
                continue;
            }
            final String s = key;
            int n = -1;
            switch (s.hashCode()) {
                case 3522445: {
                    if (s.equals((Object)"safe")) {
                        n = 0;
                        break;
                    }
                    break;
                }
                case 119: {
                    if (s.equals((Object)"w")) {
                        n = 1;
                        break;
                    }
                    break;
                }
                case -1858790352: {
                    if (s.equals((Object)"wtimeoutms")) {
                        n = 2;
                        break;
                    }
                    break;
                }
                case -1419464905: {
                    if (s.equals((Object)"journal")) {
                        n = 3;
                        break;
                    }
                    break;
                }
            }
            switch (n) {
                case 0: {
                    safe = this.parseBoolean(value, "safe");
                    continue;
                }
                case 1: {
                    w = value;
                    continue;
                }
                case 2: {
                    wTimeout = Integer.parseInt(value);
                    continue;
                }
                case 3: {
                    journal = this.parseBoolean(value, "journal");
                    continue;
                }
            }
        }
        return this.buildWriteConcern(safe, w, wTimeout, journal);
    }
    
    @Nullable
    private ReadPreference createReadPreference(final Map<String, List<String>> optionsMap) {
        String readPreferenceType = null;
        final List<TagSet> tagSetList = (List<TagSet>)new ArrayList();
        long maxStalenessSeconds = -1L;
        for (final String key : ConnectionString.READ_PREFERENCE_KEYS) {
            final String value = this.getLastValue(optionsMap, key);
            if (value == null) {
                continue;
            }
            final String s = key;
            int n = -1;
            switch (s.hashCode()) {
                case 1796707057: {
                    if (s.equals((Object)"readpreference")) {
                        n = 0;
                        break;
                    }
                    break;
                }
                case 995905651: {
                    if (s.equals((Object)"maxstalenessseconds")) {
                        n = 1;
                        break;
                    }
                    break;
                }
                case 511239818: {
                    if (s.equals((Object)"readpreferencetags")) {
                        n = 2;
                        break;
                    }
                    break;
                }
            }
            switch (n) {
                case 0: {
                    readPreferenceType = value;
                    continue;
                }
                case 1: {
                    maxStalenessSeconds = this.parseInteger(value, "maxstalenessseconds");
                    continue;
                }
                case 2: {
                    for (final String cur : (List)optionsMap.get((Object)key)) {
                        final TagSet tagSet = this.getTags(cur.trim());
                        tagSetList.add((Object)tagSet);
                    }
                    continue;
                }
            }
        }
        return this.buildReadPreference(readPreferenceType, tagSetList, maxStalenessSeconds);
    }
    
    private UuidRepresentation createUuidRepresentation(final String value) {
        if (value.equalsIgnoreCase("unspecified")) {
            return UuidRepresentation.UNSPECIFIED;
        }
        if (value.equalsIgnoreCase("javaLegacy")) {
            return UuidRepresentation.JAVA_LEGACY;
        }
        if (value.equalsIgnoreCase("csharpLegacy")) {
            return UuidRepresentation.C_SHARP_LEGACY;
        }
        if (value.equalsIgnoreCase("pythonLegacy")) {
            return UuidRepresentation.PYTHON_LEGACY;
        }
        if (value.equalsIgnoreCase("standard")) {
            return UuidRepresentation.STANDARD;
        }
        throw new IllegalArgumentException("Unknown uuid representation: " + value);
    }
    
    @Nullable
    private MongoCredential createCredentials(final Map<String, List<String>> optionsMap, @Nullable final String userName, @Nullable final char[] password) {
        AuthenticationMechanism mechanism = null;
        String authSource = null;
        String gssapiServiceName = null;
        String authMechanismProperties = null;
        for (final String key : ConnectionString.AUTH_KEYS) {
            final String value = this.getLastValue(optionsMap, key);
            if (value == null) {
                continue;
            }
            final String s = key;
            int n = -1;
            switch (s.hashCode()) {
                case -497917263: {
                    if (s.equals((Object)"authmechanism")) {
                        n = 0;
                        break;
                    }
                    break;
                }
                case -1388615741: {
                    if (s.equals((Object)"authsource")) {
                        n = 1;
                        break;
                    }
                    break;
                }
                case -909924051: {
                    if (s.equals((Object)"gssapiservicename")) {
                        n = 2;
                        break;
                    }
                    break;
                }
                case -349370716: {
                    if (s.equals((Object)"authmechanismproperties")) {
                        n = 3;
                        break;
                    }
                    break;
                }
            }
            switch (n) {
                case 0: {
                    if (!value.equals((Object)"MONGODB-CR")) {
                        mechanism = AuthenticationMechanism.fromMechanismName(value);
                        continue;
                    }
                    if (userName == null) {
                        throw new IllegalArgumentException("username can not be null");
                    }
                    ConnectionString.LOGGER.warn("Deprecated MONGDOB-CR authentication mechanism used in connection string");
                    continue;
                }
                case 1: {
                    if (value.equals((Object)"")) {
                        throw new IllegalArgumentException("authSource can not be an empty string");
                    }
                    authSource = value;
                    continue;
                }
                case 2: {
                    gssapiServiceName = value;
                    continue;
                }
                case 3: {
                    authMechanismProperties = value;
                    continue;
                }
            }
        }
        MongoCredential credential = null;
        if (mechanism != null) {
            credential = this.createMongoCredentialWithMechanism(mechanism, userName, password, authSource, gssapiServiceName);
        }
        else if (userName != null) {
            credential = MongoCredential.createCredential(userName, this.getAuthSourceOrDefault(authSource, (this.database != null) ? this.database : "admin"), password);
        }
        if (credential != null && authMechanismProperties != null) {
            for (final String part : authMechanismProperties.split(",")) {
                final String[] mechanismPropertyKeyValue = part.split(":", 2);
                if (mechanismPropertyKeyValue.length != 2) {
                    throw new IllegalArgumentException(String.format("The connection string contains invalid authentication properties. '%s' is not a key value pair", new Object[] { part }));
                }
                final String key2 = mechanismPropertyKeyValue[0].trim().toLowerCase();
                final String value2 = mechanismPropertyKeyValue[1].trim();
                if (ConnectionString.MECHANISM_KEYS_DISALLOWED_IN_CONNECTION_STRING.contains((Object)key2)) {
                    throw new IllegalArgumentException(String.format("The connection string contains disallowed mechanism properties. '%s' must be set on the credential programmatically.", new Object[] { key2 }));
                }
                if (key2.equals((Object)"canonicalize_host_name")) {
                    credential = credential.withMechanismProperty(key2, Boolean.valueOf(value2));
                }
                else {
                    credential = credential.withMechanismProperty(key2, value2);
                }
            }
        }
        return credential;
    }
    
    private MongoCredential createMongoCredentialWithMechanism(final AuthenticationMechanism mechanism, final String userName, @Nullable final char[] password, @Nullable final String authSource, @Nullable final String gssapiServiceName) {
        String mechanismAuthSource = null;
        switch (mechanism) {
            case PLAIN: {
                mechanismAuthSource = this.getAuthSourceOrDefault(authSource, (this.database != null) ? this.database : "$external");
                break;
            }
            case GSSAPI:
            case MONGODB_X509: {
                mechanismAuthSource = this.getAuthSourceOrDefault(authSource, "$external");
                if (!mechanismAuthSource.equals((Object)"$external")) {
                    throw new IllegalArgumentException(String.format("Invalid authSource for %s, it must be '$external'", new Object[] { mechanism }));
                }
                break;
            }
            default: {
                mechanismAuthSource = this.getAuthSourceOrDefault(authSource, (this.database != null) ? this.database : "admin");
                break;
            }
        }
        MongoCredential credential = null;
        switch (mechanism) {
            case GSSAPI: {
                credential = MongoCredential.createGSSAPICredential(userName);
                if (gssapiServiceName != null) {
                    credential = credential.withMechanismProperty("SERVICE_NAME", gssapiServiceName);
                }
                if (password != null && ConnectionString.LOGGER.isWarnEnabled()) {
                    ConnectionString.LOGGER.warn("Password in connection string not used with MONGODB_X509 authentication mechanism.");
                    break;
                }
                break;
            }
            case PLAIN: {
                credential = MongoCredential.createPlainCredential(userName, mechanismAuthSource, password);
                break;
            }
            case MONGODB_X509: {
                if (password != null) {
                    throw new IllegalArgumentException("Invalid mechanism, MONGODB_x509 does not support passwords");
                }
                credential = MongoCredential.createMongoX509Credential(userName);
                break;
            }
            case SCRAM_SHA_1: {
                credential = MongoCredential.createScramSha1Credential(userName, mechanismAuthSource, password);
                break;
            }
            case SCRAM_SHA_256: {
                credential = MongoCredential.createScramSha256Credential(userName, mechanismAuthSource, password);
                break;
            }
            case MONGODB_AWS: {
                credential = MongoCredential.createAwsCredential(userName, password);
                break;
            }
            case MONGODB_OIDC: {
                OidcAuthenticator.OidcValidator.validateCreateOidcCredential(password);
                credential = MongoCredential.createOidcCredential(userName);
                break;
            }
            default: {
                throw new UnsupportedOperationException(String.format("The connection string contains an invalid authentication mechanism'. '%s' is not a supported authentication mechanism", new Object[] { mechanism }));
            }
        }
        return credential;
    }
    
    private String getAuthSourceOrDefault(@Nullable final String authSource, final String defaultAuthSource) {
        if (authSource != null) {
            return authSource;
        }
        return defaultAuthSource;
    }
    
    @Nullable
    private String getLastValue(final Map<String, List<String>> optionsMap, final String key) {
        final List<String> valueList = (List<String>)optionsMap.get((Object)key);
        if (valueList == null) {
            return null;
        }
        return (String)valueList.get(valueList.size() - 1);
    }
    
    private Map<String, List<String>> parseOptions(final String optionsPart) {
        final Map<String, List<String>> optionsMap = (Map<String, List<String>>)new HashMap();
        if (optionsPart.isEmpty()) {
            return optionsMap;
        }
        for (final String part : optionsPart.split("&|;")) {
            if (!part.isEmpty()) {
                final int idx = part.indexOf("=");
                if (idx < 0) {
                    throw new IllegalArgumentException(String.format("The connection string contains an invalid option '%s'. '%s' is missing the value delimiter eg '%s=value'", new Object[] { optionsPart, part, part }));
                }
                final String key = part.substring(0, idx).toLowerCase();
                final String value = part.substring(idx + 1);
                List<String> valueList = (List<String>)optionsMap.get((Object)key);
                if (valueList == null) {
                    valueList = (List<String>)new ArrayList(1);
                }
                valueList.add((Object)this.urldecode(value));
                optionsMap.put((Object)key, (Object)valueList);
            }
        }
        if (optionsMap.containsKey((Object)"wtimeout") && !optionsMap.containsKey((Object)"wtimeoutms")) {
            optionsMap.put((Object)"wtimeoutms", (Object)optionsMap.remove((Object)"wtimeout"));
            if (ConnectionString.LOGGER.isWarnEnabled()) {
                ConnectionString.LOGGER.warn("Uri option 'wtimeout' has been deprecated, use 'wtimeoutms' instead.");
            }
        }
        if (optionsMap.containsKey((Object)"j") && !optionsMap.containsKey((Object)"journal")) {
            optionsMap.put((Object)"journal", (Object)optionsMap.remove((Object)"j"));
            if (ConnectionString.LOGGER.isWarnEnabled()) {
                ConnectionString.LOGGER.warn("Uri option 'j' has been deprecated, use 'journal' instead.");
            }
        }
        return optionsMap;
    }
    
    @Nullable
    private ReadPreference buildReadPreference(@Nullable final String readPreferenceType, final List<TagSet> tagSetList, final long maxStalenessSeconds) {
        if (readPreferenceType != null) {
            if (tagSetList.isEmpty() && maxStalenessSeconds == -1L) {
                return ReadPreference.valueOf(readPreferenceType);
            }
            if (maxStalenessSeconds == -1L) {
                return ReadPreference.valueOf(readPreferenceType, tagSetList);
            }
            return ReadPreference.valueOf(readPreferenceType, tagSetList, maxStalenessSeconds, TimeUnit.SECONDS);
        }
        else {
            if (!tagSetList.isEmpty() || maxStalenessSeconds != -1L) {
                throw new IllegalArgumentException("Read preference mode must be specified if either read preference tags or max staleness is specified");
            }
            return null;
        }
    }
    
    @Nullable
    private WriteConcern buildWriteConcern(@Nullable final Boolean safe, @Nullable final String w, @Nullable final Integer wTimeout, @Nullable final Boolean journal) {
        WriteConcern retVal = null;
        if (w != null || wTimeout != null || journal != null) {
            if (w == null) {
                retVal = WriteConcern.ACKNOWLEDGED;
            }
            else {
                try {
                    retVal = new WriteConcern(Integer.parseInt(w));
                }
                catch (final NumberFormatException e) {
                    retVal = new WriteConcern(w);
                }
            }
            if (wTimeout != null) {
                retVal = retVal.withWTimeout(wTimeout, TimeUnit.MILLISECONDS);
            }
            if (journal != null) {
                retVal = retVal.withJournal(journal);
            }
            return retVal;
        }
        if (safe != null) {
            if (safe) {
                retVal = WriteConcern.ACKNOWLEDGED;
            }
            else {
                retVal = WriteConcern.UNACKNOWLEDGED;
            }
        }
        return retVal;
    }
    
    private TagSet getTags(final String tagSetString) {
        final List<Tag> tagList = (List<Tag>)new ArrayList();
        if (tagSetString.length() > 0) {
            for (final String tag : tagSetString.split(",")) {
                final String[] tagKeyValuePair = tag.split(":");
                if (tagKeyValuePair.length != 2) {
                    throw new IllegalArgumentException(String.format("The connection string contains an invalid read preference tag. '%s' is not a key value pair", new Object[] { tagSetString }));
                }
                tagList.add((Object)new Tag(tagKeyValuePair[0].trim(), tagKeyValuePair[1].trim()));
            }
        }
        return new TagSet(tagList);
    }
    
    @Nullable
    private Boolean parseBoolean(final String input, final String key) {
        final String trimmedInput = input.trim().toLowerCase();
        if (ConnectionString.TRUE_VALUES.contains((Object)trimmedInput)) {
            if (!trimmedInput.equals((Object)"true")) {
                ConnectionString.LOGGER.warn(String.format("Deprecated boolean value '%s' in the connection string for '%s'. Replace with 'true'", new Object[] { trimmedInput, key }));
            }
            return true;
        }
        if (ConnectionString.FALSE_VALUES.contains((Object)trimmedInput)) {
            if (!trimmedInput.equals((Object)"false")) {
                ConnectionString.LOGGER.warn(String.format("Deprecated boolean value '%s' in the connection string for '%s'. Replace with'false'", new Object[] { trimmedInput, key }));
            }
            return false;
        }
        ConnectionString.LOGGER.warn(String.format("Ignoring unrecognized boolean value '%s' in the connection string for '%s'. Replace with either 'true' or 'false'", new Object[] { trimmedInput, key }));
        return null;
    }
    
    private int parseInteger(final String input, final String key) {
        try {
            return Integer.parseInt(input);
        }
        catch (final NumberFormatException e) {
            throw new IllegalArgumentException(String.format("The connection string contains an invalid value for '%s'. '%s' is not a valid integer", new Object[] { key, input }));
        }
    }
    
    private List<String> parseHosts(final List<String> rawHosts) {
        if (rawHosts.size() == 0) {
            throw new IllegalArgumentException("The connection string must contain at least one host");
        }
        final List<String> hosts = (List<String>)new ArrayList();
        for (String host : rawHosts) {
            if (host.length() == 0) {
                throw new IllegalArgumentException(String.format("The connection string contains an empty host '%s'. ", new Object[] { rawHosts }));
            }
            if (host.endsWith(".sock")) {
                host = this.urldecode(host);
            }
            else if (host.startsWith("[")) {
                if (!host.contains((CharSequence)"]")) {
                    throw new IllegalArgumentException(String.format("The connection string contains an invalid host '%s'. IPv6 address literals must be enclosed in '[' and ']' according to RFC 2732", new Object[] { host }));
                }
                final int idx = host.indexOf("]:");
                if (idx != -1) {
                    this.validatePort(host, host.substring(idx + 2));
                }
            }
            else {
                final int colonCount = this.countOccurrences(host, ":");
                if (colonCount > 1) {
                    throw new IllegalArgumentException(String.format("The connection string contains an invalid host '%s'. Reserved characters such as ':' must be escaped according RFC 2396. Any IPv6 address literal must be enclosed in '[' and ']' according to RFC 2732.", new Object[] { host }));
                }
                if (colonCount == 1) {
                    this.validatePort(host, host.substring(host.indexOf(":") + 1));
                }
            }
            hosts.add((Object)host);
        }
        Collections.sort((List)hosts);
        return hosts;
    }
    
    private void validatePort(final String host, final String port) {
        boolean invalidPort = false;
        try {
            final int portInt = Integer.parseInt(port);
            if (portInt <= 0 || portInt > 65535) {
                invalidPort = true;
            }
        }
        catch (final NumberFormatException e) {
            invalidPort = true;
        }
        if (invalidPort) {
            throw new IllegalArgumentException(String.format("The connection string contains an invalid host '%s'. The port '%s' is not a valid, it must be an integer between 0 and 65535", new Object[] { host, port }));
        }
    }
    
    private void validateProxyParameters() {
        if (this.proxyHost == null) {
            if (this.proxyPort != null) {
                throw new IllegalArgumentException("proxyPort can only be specified with proxyHost");
            }
            if (this.proxyUsername != null) {
                throw new IllegalArgumentException("proxyUsername can only be specified with proxyHost");
            }
            if (this.proxyPassword != null) {
                throw new IllegalArgumentException("proxyPassword can only be specified with proxyHost");
            }
        }
        if (this.proxyPort != null && (this.proxyPort < 0 || this.proxyPort > 65535)) {
            throw new IllegalArgumentException("proxyPort should be within the valid range (0 to 65535)");
        }
        if (this.proxyUsername != null) {
            if (this.proxyUsername.isEmpty()) {
                throw new IllegalArgumentException("proxyUsername cannot be empty");
            }
            if (this.proxyUsername.getBytes(StandardCharsets.UTF_8).length >= 255) {
                throw new IllegalArgumentException("username's length in bytes cannot be greater than 255");
            }
        }
        if (this.proxyPassword != null) {
            if (this.proxyPassword.isEmpty()) {
                throw new IllegalArgumentException("proxyPassword cannot be empty");
            }
            if (this.proxyPassword.getBytes(StandardCharsets.UTF_8).length >= 255) {
                throw new IllegalArgumentException("password's length in bytes cannot be greater than 255");
            }
        }
        if (this.proxyUsername == null ^ this.proxyPassword == null) {
            throw new IllegalArgumentException("Both proxyUsername and proxyPassword must be set together. They cannot be set individually");
        }
    }
    
    private int countOccurrences(final String haystack, final String needle) {
        return haystack.length() - haystack.replace((CharSequence)needle, (CharSequence)"").length();
    }
    
    private String urldecode(final String input) {
        return this.urldecode(input, false);
    }
    
    private String urldecode(final String input, final boolean password) {
        try {
            return URLDecoder.decode(input, StandardCharsets.UTF_8.name());
        }
        catch (final UnsupportedEncodingException e) {
            if (password) {
                throw new IllegalArgumentException("The connection string contained unsupported characters in the password.");
            }
            throw new IllegalArgumentException(String.format("The connection string contained unsupported characters: '%s'.Decoding produced the following error: %s", new Object[] { input, e.getMessage() }));
        }
    }
    
    @Nullable
    public String getUsername() {
        return (this.credential != null) ? this.credential.getUserName() : null;
    }
    
    @Nullable
    public char[] getPassword() {
        return (char[])((this.credential != null) ? this.credential.getPassword() : null);
    }
    
    public boolean isSrvProtocol() {
        return this.isSrvProtocol;
    }
    
    @Nullable
    public Integer getSrvMaxHosts() {
        return this.srvMaxHosts;
    }
    
    @Nullable
    public String getSrvServiceName() {
        return this.srvServiceName;
    }
    
    public List<String> getHosts() {
        return this.hosts;
    }
    
    @Nullable
    public String getDatabase() {
        return this.database;
    }
    
    @Nullable
    public String getCollection() {
        return this.collection;
    }
    
    @Nullable
    public Boolean isDirectConnection() {
        return this.directConnection;
    }
    
    @Nullable
    public Boolean isLoadBalanced() {
        return this.loadBalanced;
    }
    
    public String getConnectionString() {
        return this.connectionString;
    }
    
    @Nullable
    public MongoCredential getCredential() {
        return this.credential;
    }
    
    @Nullable
    public ReadPreference getReadPreference() {
        return this.readPreference;
    }
    
    @Nullable
    public ReadConcern getReadConcern() {
        return this.readConcern;
    }
    
    @Nullable
    public WriteConcern getWriteConcern() {
        return this.writeConcern;
    }
    
    @Nullable
    public Boolean getRetryWritesValue() {
        return this.retryWrites;
    }
    
    @Nullable
    public Boolean getRetryReads() {
        return this.retryReads;
    }
    
    @Nullable
    public Integer getMinConnectionPoolSize() {
        return this.minConnectionPoolSize;
    }
    
    @Nullable
    public Integer getMaxConnectionPoolSize() {
        return this.maxConnectionPoolSize;
    }
    
    @Nullable
    public Integer getMaxWaitTime() {
        return this.maxWaitTime;
    }
    
    @Nullable
    public Integer getMaxConnectionIdleTime() {
        return this.maxConnectionIdleTime;
    }
    
    @Nullable
    public Integer getMaxConnectionLifeTime() {
        return this.maxConnectionLifeTime;
    }
    
    @Nullable
    public Integer getMaxConnecting() {
        return this.maxConnecting;
    }
    
    @Nullable
    public Integer getConnectTimeout() {
        return this.connectTimeout;
    }
    
    @Nullable
    public Integer getSocketTimeout() {
        return this.socketTimeout;
    }
    
    @Nullable
    public Boolean getSslEnabled() {
        return this.sslEnabled;
    }
    
    @Nullable
    public String getProxyHost() {
        return this.proxyHost;
    }
    
    @Nullable
    public Integer getProxyPort() {
        return this.proxyPort;
    }
    
    @Nullable
    public String getProxyUsername() {
        return this.proxyUsername;
    }
    
    @Nullable
    public String getProxyPassword() {
        return this.proxyPassword;
    }
    
    @Nullable
    public Boolean getSslInvalidHostnameAllowed() {
        return this.sslInvalidHostnameAllowed;
    }
    
    @Nullable
    public String getRequiredReplicaSetName() {
        return this.requiredReplicaSetName;
    }
    
    @Nullable
    public Integer getServerSelectionTimeout() {
        return this.serverSelectionTimeout;
    }
    
    @Nullable
    public Integer getLocalThreshold() {
        return this.localThreshold;
    }
    
    @Nullable
    public Integer getHeartbeatFrequency() {
        return this.heartbeatFrequency;
    }
    
    @Nullable
    public ServerMonitoringMode getServerMonitoringMode() {
        return this.serverMonitoringMode;
    }
    
    @Nullable
    public String getApplicationName() {
        return this.applicationName;
    }
    
    public List<MongoCompressor> getCompressorList() {
        return this.compressorList;
    }
    
    @Nullable
    public UuidRepresentation getUuidRepresentation() {
        return this.uuidRepresentation;
    }
    
    @Override
    public String toString() {
        return this.connectionString;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final ConnectionString that = (ConnectionString)o;
        return this.isSrvProtocol == that.isSrvProtocol && Objects.equals((Object)this.directConnection, (Object)that.directConnection) && Objects.equals((Object)this.credential, (Object)that.credential) && Objects.equals((Object)this.hosts, (Object)that.hosts) && Objects.equals((Object)this.database, (Object)that.database) && Objects.equals((Object)this.collection, (Object)that.collection) && Objects.equals((Object)this.readPreference, (Object)that.readPreference) && Objects.equals((Object)this.writeConcern, (Object)that.writeConcern) && Objects.equals((Object)this.retryWrites, (Object)that.retryWrites) && Objects.equals((Object)this.retryReads, (Object)that.retryReads) && Objects.equals((Object)this.readConcern, (Object)that.readConcern) && Objects.equals((Object)this.minConnectionPoolSize, (Object)that.minConnectionPoolSize) && Objects.equals((Object)this.maxConnectionPoolSize, (Object)that.maxConnectionPoolSize) && Objects.equals((Object)this.maxWaitTime, (Object)that.maxWaitTime) && Objects.equals((Object)this.maxConnectionIdleTime, (Object)that.maxConnectionIdleTime) && Objects.equals((Object)this.maxConnectionLifeTime, (Object)that.maxConnectionLifeTime) && Objects.equals((Object)this.maxConnecting, (Object)that.maxConnecting) && Objects.equals((Object)this.connectTimeout, (Object)that.connectTimeout) && Objects.equals((Object)this.socketTimeout, (Object)that.socketTimeout) && Objects.equals((Object)this.proxyHost, (Object)that.proxyHost) && Objects.equals((Object)this.proxyPort, (Object)that.proxyPort) && Objects.equals((Object)this.proxyUsername, (Object)that.proxyUsername) && Objects.equals((Object)this.proxyPassword, (Object)that.proxyPassword) && Objects.equals((Object)this.sslEnabled, (Object)that.sslEnabled) && Objects.equals((Object)this.sslInvalidHostnameAllowed, (Object)that.sslInvalidHostnameAllowed) && Objects.equals((Object)this.requiredReplicaSetName, (Object)that.requiredReplicaSetName) && Objects.equals((Object)this.serverSelectionTimeout, (Object)that.serverSelectionTimeout) && Objects.equals((Object)this.localThreshold, (Object)that.localThreshold) && Objects.equals((Object)this.heartbeatFrequency, (Object)that.heartbeatFrequency) && Objects.equals((Object)this.serverMonitoringMode, (Object)that.serverMonitoringMode) && Objects.equals((Object)this.applicationName, (Object)that.applicationName) && Objects.equals((Object)this.compressorList, (Object)that.compressorList) && Objects.equals((Object)this.uuidRepresentation, (Object)that.uuidRepresentation) && Objects.equals((Object)this.srvServiceName, (Object)that.srvServiceName) && Objects.equals((Object)this.srvMaxHosts, (Object)that.srvMaxHosts);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(new Object[] { this.credential, this.isSrvProtocol, this.hosts, this.database, this.collection, this.directConnection, this.readPreference, this.writeConcern, this.retryWrites, this.retryReads, this.readConcern, this.minConnectionPoolSize, this.maxConnectionPoolSize, this.maxWaitTime, this.maxConnectionIdleTime, this.maxConnectionLifeTime, this.maxConnecting, this.connectTimeout, this.socketTimeout, this.sslEnabled, this.sslInvalidHostnameAllowed, this.requiredReplicaSetName, this.serverSelectionTimeout, this.localThreshold, this.heartbeatFrequency, this.serverMonitoringMode, this.applicationName, this.compressorList, this.uuidRepresentation, this.srvServiceName, this.srvMaxHosts, this.proxyHost, this.proxyPort, this.proxyUsername, this.proxyPassword });
    }
    
    static {
        ALLOWED_OPTIONS_IN_TXT_RECORD = (Set)new HashSet((Collection)Arrays.asList((Object[])new String[] { "authsource", "replicaset", "loadbalanced" }));
        LOGGER = Loggers.getLogger("uri");
        MECHANISM_KEYS_DISALLOWED_IN_CONNECTION_STRING = (List)Stream.of((Object)"ALLOWED_HOSTS").map(k -> k.toLowerCase()).collect(Collectors.toList());
        GENERAL_OPTIONS_KEYS = (Set)new LinkedHashSet();
        AUTH_KEYS = (Set)new HashSet();
        READ_PREFERENCE_KEYS = (Set)new HashSet();
        WRITE_CONCERN_KEYS = (Set)new HashSet();
        COMPRESSOR_KEYS = (Set)new HashSet();
        ALL_KEYS = (Set)new HashSet();
        ConnectionString.GENERAL_OPTIONS_KEYS.add((Object)"minpoolsize");
        ConnectionString.GENERAL_OPTIONS_KEYS.add((Object)"maxpoolsize");
        ConnectionString.GENERAL_OPTIONS_KEYS.add((Object)"waitqueuetimeoutms");
        ConnectionString.GENERAL_OPTIONS_KEYS.add((Object)"connecttimeoutms");
        ConnectionString.GENERAL_OPTIONS_KEYS.add((Object)"maxidletimems");
        ConnectionString.GENERAL_OPTIONS_KEYS.add((Object)"maxlifetimems");
        ConnectionString.GENERAL_OPTIONS_KEYS.add((Object)"maxconnecting");
        ConnectionString.GENERAL_OPTIONS_KEYS.add((Object)"sockettimeoutms");
        ConnectionString.GENERAL_OPTIONS_KEYS.add((Object)"ssl");
        ConnectionString.GENERAL_OPTIONS_KEYS.add((Object)"tls");
        ConnectionString.GENERAL_OPTIONS_KEYS.add((Object)"tlsinsecure");
        ConnectionString.GENERAL_OPTIONS_KEYS.add((Object)"sslinvalidhostnameallowed");
        ConnectionString.GENERAL_OPTIONS_KEYS.add((Object)"tlsallowinvalidhostnames");
        ConnectionString.GENERAL_OPTIONS_KEYS.add((Object)"proxyhost");
        ConnectionString.GENERAL_OPTIONS_KEYS.add((Object)"proxyport");
        ConnectionString.GENERAL_OPTIONS_KEYS.add((Object)"proxyusername");
        ConnectionString.GENERAL_OPTIONS_KEYS.add((Object)"proxypassword");
        ConnectionString.GENERAL_OPTIONS_KEYS.add((Object)"replicaset");
        ConnectionString.GENERAL_OPTIONS_KEYS.add((Object)"readconcernlevel");
        ConnectionString.GENERAL_OPTIONS_KEYS.add((Object)"serverselectiontimeoutms");
        ConnectionString.GENERAL_OPTIONS_KEYS.add((Object)"localthresholdms");
        ConnectionString.GENERAL_OPTIONS_KEYS.add((Object)"heartbeatfrequencyms");
        ConnectionString.GENERAL_OPTIONS_KEYS.add((Object)"servermonitoringmode");
        ConnectionString.GENERAL_OPTIONS_KEYS.add((Object)"retrywrites");
        ConnectionString.GENERAL_OPTIONS_KEYS.add((Object)"retryreads");
        ConnectionString.GENERAL_OPTIONS_KEYS.add((Object)"appname");
        ConnectionString.GENERAL_OPTIONS_KEYS.add((Object)"uuidrepresentation");
        ConnectionString.GENERAL_OPTIONS_KEYS.add((Object)"directconnection");
        ConnectionString.GENERAL_OPTIONS_KEYS.add((Object)"loadbalanced");
        ConnectionString.GENERAL_OPTIONS_KEYS.add((Object)"srvmaxhosts");
        ConnectionString.GENERAL_OPTIONS_KEYS.add((Object)"srvservicename");
        ConnectionString.COMPRESSOR_KEYS.add((Object)"compressors");
        ConnectionString.COMPRESSOR_KEYS.add((Object)"zlibcompressionlevel");
        ConnectionString.READ_PREFERENCE_KEYS.add((Object)"readpreference");
        ConnectionString.READ_PREFERENCE_KEYS.add((Object)"readpreferencetags");
        ConnectionString.READ_PREFERENCE_KEYS.add((Object)"maxstalenessseconds");
        ConnectionString.WRITE_CONCERN_KEYS.add((Object)"safe");
        ConnectionString.WRITE_CONCERN_KEYS.add((Object)"w");
        ConnectionString.WRITE_CONCERN_KEYS.add((Object)"wtimeoutms");
        ConnectionString.WRITE_CONCERN_KEYS.add((Object)"journal");
        ConnectionString.AUTH_KEYS.add((Object)"authmechanism");
        ConnectionString.AUTH_KEYS.add((Object)"authsource");
        ConnectionString.AUTH_KEYS.add((Object)"gssapiservicename");
        ConnectionString.AUTH_KEYS.add((Object)"authmechanismproperties");
        ConnectionString.ALL_KEYS.addAll((Collection)ConnectionString.GENERAL_OPTIONS_KEYS);
        ConnectionString.ALL_KEYS.addAll((Collection)ConnectionString.AUTH_KEYS);
        ConnectionString.ALL_KEYS.addAll((Collection)ConnectionString.READ_PREFERENCE_KEYS);
        ConnectionString.ALL_KEYS.addAll((Collection)ConnectionString.WRITE_CONCERN_KEYS);
        ConnectionString.ALL_KEYS.addAll((Collection)ConnectionString.COMPRESSOR_KEYS);
        TRUE_VALUES = (Set)new HashSet((Collection)Arrays.asList((Object[])new String[] { "true", "yes", "1" }));
        FALSE_VALUES = (Set)new HashSet((Collection)Arrays.asList((Object[])new String[] { "false", "no", "0" }));
    }
}
