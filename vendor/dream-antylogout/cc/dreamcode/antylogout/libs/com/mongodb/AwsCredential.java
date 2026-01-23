package cc.dreamcode.antylogout.libs.com.mongodb;

import cc.dreamcode.antylogout.libs.com.mongodb.assertions.Assertions;
import cc.dreamcode.antylogout.libs.com.mongodb.lang.Nullable;
import cc.dreamcode.antylogout.libs.com.mongodb.annotations.Beta;

@Beta({ Beta.Reason.CLIENT })
public final class AwsCredential
{
    private final String accessKeyId;
    private final String secretAccessKey;
    private final String sessionToken;
    
    public AwsCredential(final String accessKeyId, final String secretAccessKey, @Nullable final String sessionToken) {
        this.accessKeyId = Assertions.notNull("accessKeyId", accessKeyId);
        this.secretAccessKey = Assertions.notNull("secretAccessKey", secretAccessKey);
        this.sessionToken = sessionToken;
    }
    
    public String getAccessKeyId() {
        return this.accessKeyId;
    }
    
    public String getSecretAccessKey() {
        return this.secretAccessKey;
    }
    
    @Nullable
    public String getSessionToken() {
        return this.sessionToken;
    }
}
