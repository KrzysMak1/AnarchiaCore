package cc.dreamcode.antylogout.libs.com.mongodb.internal.connection;

import cc.dreamcode.antylogout.libs.com.mongodb.MongoClientException;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.netty.NettyStreamFactoryFactory;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.NettyTransportSettings;
import cc.dreamcode.antylogout.libs.com.mongodb.spi.dns.InetAddressResolver;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.TransportSettings;

public final class StreamFactoryHelper
{
    public static StreamFactoryFactory getStreamFactoryFactoryFromSettings(final TransportSettings transportSettings, final InetAddressResolver inetAddressResolver) {
        if (transportSettings instanceof NettyTransportSettings) {
            return NettyStreamFactoryFactory.builder().applySettings((NettyTransportSettings)transportSettings).inetAddressResolver(inetAddressResolver).build();
        }
        throw new MongoClientException("Unsupported transport settings: " + transportSettings.getClass().getName());
    }
    
    private StreamFactoryHelper() {
    }
}
