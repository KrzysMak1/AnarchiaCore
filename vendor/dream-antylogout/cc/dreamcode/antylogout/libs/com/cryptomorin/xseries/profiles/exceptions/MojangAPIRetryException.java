package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.profiles.exceptions;

public final class MojangAPIRetryException extends MojangAPIException
{
    private final Reason reason;
    
    public MojangAPIRetryException(final Reason reason, final String message) {
        super(message);
        this.reason = reason;
    }
    
    public MojangAPIRetryException(final Reason reason, final String message, final Throwable cause) {
        super(message, cause);
        this.reason = reason;
    }
    
    public Reason getReason() {
        return this.reason;
    }
    
    public enum Reason
    {
        CONNECTION_RESET, 
        CONNECTION_TIMEOUT, 
        RATELIMITED;
    }
}
