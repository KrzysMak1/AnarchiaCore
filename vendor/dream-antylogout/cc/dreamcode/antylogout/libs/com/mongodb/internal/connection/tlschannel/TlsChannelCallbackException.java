package cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.tlschannel;

import javax.net.ssl.SSLException;

public class TlsChannelCallbackException extends SSLException
{
    private static final long serialVersionUID = 8491908031320425318L;
    
    public TlsChannelCallbackException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
