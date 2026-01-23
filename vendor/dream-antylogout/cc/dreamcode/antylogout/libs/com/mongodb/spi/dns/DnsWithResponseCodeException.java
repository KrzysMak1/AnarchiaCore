package cc.dreamcode.antylogout.libs.com.mongodb.spi.dns;

public class DnsWithResponseCodeException extends DnsException
{
    private static final long serialVersionUID = 1L;
    private final int responseCode;
    
    public DnsWithResponseCodeException(final String message, final int responseCode, final Throwable cause) {
        super(message, cause);
        this.responseCode = responseCode;
    }
    
    public int getResponseCode() {
        return this.responseCode;
    }
}
