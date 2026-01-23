package cc.dreamcode.antylogout.libs.com.mongodb.spi.dns;

public class DnsException extends RuntimeException
{
    private static final long serialVersionUID = 1L;
    
    public DnsException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
