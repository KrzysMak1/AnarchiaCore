package cc.dreamcode.antylogout.libs.com.mongodb.internal.connection;

import java.io.IOException;
import jnr.unixsocket.UnixSocketChannel;
import jnr.unixsocket.UnixSocketAddress;
import java.net.Socket;
import cc.dreamcode.antylogout.libs.com.mongodb.spi.dns.InetAddressResolver;
import cc.dreamcode.antylogout.libs.com.mongodb.ServerAddress;
import javax.net.SocketFactory;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.SslSettings;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.SocketSettings;
import cc.dreamcode.antylogout.libs.com.mongodb.UnixServerAddress;

public class UnixSocketChannelStream extends SocketStream
{
    private final UnixServerAddress address;
    
    public UnixSocketChannelStream(final UnixServerAddress address, final SocketSettings settings, final SslSettings sslSettings, final BufferProvider bufferProvider) {
        super(address, new DefaultInetAddressResolver(), settings, sslSettings, SocketFactory.getDefault(), bufferProvider);
        this.address = address;
    }
    
    @Override
    protected Socket initializeSocket() throws IOException {
        return (Socket)UnixSocketChannel.open(new UnixSocketAddress(this.address.getHost())).socket();
    }
}
