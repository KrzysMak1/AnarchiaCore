package cc.dreamcode.antylogout.libs.com.mongodb;

import java.util.HashMap;
import java.util.Map;

public enum AuthenticationMechanism
{
    GSSAPI("GSSAPI"), 
    MONGODB_AWS("MONGODB-AWS"), 
    MONGODB_OIDC("MONGODB-OIDC"), 
    MONGODB_X509("MONGODB-X509"), 
    PLAIN("PLAIN"), 
    SCRAM_SHA_1("SCRAM-SHA-1"), 
    SCRAM_SHA_256("SCRAM-SHA-256");
    
    private static final Map<String, AuthenticationMechanism> AUTH_MAP;
    private final String mechanismName;
    
    private AuthenticationMechanism(final String mechanismName) {
        this.mechanismName = mechanismName;
    }
    
    public String getMechanismName() {
        return this.mechanismName;
    }
    
    public String toString() {
        return this.mechanismName;
    }
    
    public static AuthenticationMechanism fromMechanismName(final String mechanismName) {
        final AuthenticationMechanism mechanism = (AuthenticationMechanism)AuthenticationMechanism.AUTH_MAP.get((Object)mechanismName);
        if (mechanism == null) {
            throw new IllegalArgumentException("Unsupported authMechanism: " + mechanismName);
        }
        return mechanism;
    }
    
    static {
        AUTH_MAP = (Map)new HashMap();
        for (final AuthenticationMechanism value : values()) {
            AuthenticationMechanism.AUTH_MAP.put((Object)value.getMechanismName(), (Object)value);
        }
    }
}
