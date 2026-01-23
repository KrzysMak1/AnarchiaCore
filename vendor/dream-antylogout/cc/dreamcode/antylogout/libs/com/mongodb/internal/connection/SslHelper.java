package cc.dreamcode.antylogout.libs.com.mongodb.internal.connection;

import cc.dreamcode.antylogout.libs.com.mongodb.MongoInternalException;
import javax.net.ssl.SSLSocket;
import java.net.InetSocketAddress;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.SslSettings;
import java.net.Socket;
import javax.net.ssl.SNIServerName;
import java.util.Collections;
import javax.net.ssl.SNIHostName;
import javax.net.ssl.SSLParameters;

public final class SslHelper
{
    public static void enableHostNameVerification(final SSLParameters sslParameters) {
        sslParameters.setEndpointIdentificationAlgorithm("HTTPS");
    }
    
    public static void enableSni(final String host, final SSLParameters sslParameters) {
        try {
            final SNIServerName sniHostName = (SNIServerName)new SNIHostName(host);
            sslParameters.setServerNames(Collections.singletonList((Object)sniHostName));
        }
        catch (final IllegalArgumentException ex) {}
    }
    
    public static void configureSslSocket(final Socket socket, final SslSettings sslSettings, final InetSocketAddress inetSocketAddress) throws MongoInternalException {
        if (sslSettings.isEnabled() || socket instanceof SSLSocket) {
            if (!(socket instanceof SSLSocket)) {
                throw new MongoInternalException("SSL is enabled but the socket is not an instance of javax.net.ssl.SSLSocket");
            }
            final SSLSocket sslSocket = (SSLSocket)socket;
            SSLParameters sslParameters = sslSocket.getSSLParameters();
            if (sslParameters == null) {
                sslParameters = new SSLParameters();
            }
            enableSni(inetSocketAddress.getHostName(), sslParameters);
            if (!sslSettings.isInvalidHostNameAllowed()) {
                enableHostNameVerification(sslParameters);
            }
            sslSocket.setSSLParameters(sslParameters);
        }
    }
    
    private SslHelper() {
    }
}
