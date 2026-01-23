package cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.netty;

import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.Stream;
import cc.dreamcode.antylogout.libs.com.mongodb.ServerAddress;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.socket.nio.NioSocketChannel;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.DefaultInetAddressResolver;
import cc.dreamcode.antylogout.libs.com.mongodb.assertions.Assertions;
import cc.dreamcode.antylogout.libs.com.mongodb.lang.Nullable;
import io.netty.handler.ssl.SslContext;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.EventLoopGroup;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.SslSettings;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.SocketSettings;
import cc.dreamcode.antylogout.libs.com.mongodb.spi.dns.InetAddressResolver;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.StreamFactory;

public class NettyStreamFactory implements StreamFactory
{
    private final InetAddressResolver inetAddressResolver;
    private final SocketSettings settings;
    private final SslSettings sslSettings;
    private final EventLoopGroup eventLoopGroup;
    private final Class<? extends SocketChannel> socketChannelClass;
    private final ByteBufAllocator allocator;
    @Nullable
    private final SslContext sslContext;
    
    public NettyStreamFactory(final InetAddressResolver inetAddressResolver, final SocketSettings settings, final SslSettings sslSettings, final EventLoopGroup eventLoopGroup, final Class<? extends SocketChannel> socketChannelClass, final ByteBufAllocator allocator, @Nullable final SslContext sslContext) {
        this.inetAddressResolver = inetAddressResolver;
        this.settings = Assertions.notNull("settings", settings);
        this.sslSettings = Assertions.notNull("sslSettings", sslSettings);
        this.eventLoopGroup = Assertions.notNull("eventLoopGroup", eventLoopGroup);
        this.socketChannelClass = Assertions.notNull("socketChannelClass", socketChannelClass);
        this.allocator = Assertions.notNull("allocator", allocator);
        this.sslContext = sslContext;
    }
    
    public NettyStreamFactory(final SocketSettings settings, final SslSettings sslSettings, final EventLoopGroup eventLoopGroup, final Class<? extends SocketChannel> socketChannelClass, final ByteBufAllocator allocator) {
        this(new DefaultInetAddressResolver(), settings, sslSettings, eventLoopGroup, socketChannelClass, allocator, null);
    }
    
    public NettyStreamFactory(final SocketSettings settings, final SslSettings sslSettings, final EventLoopGroup eventLoopGroup, final ByteBufAllocator allocator) {
        this(settings, sslSettings, eventLoopGroup, (Class<? extends SocketChannel>)NioSocketChannel.class, allocator);
    }
    
    public NettyStreamFactory(final SocketSettings settings, final SslSettings sslSettings, final EventLoopGroup eventLoopGroup) {
        this(settings, sslSettings, eventLoopGroup, (ByteBufAllocator)PooledByteBufAllocator.DEFAULT);
    }
    
    public NettyStreamFactory(final SocketSettings settings, final SslSettings sslSettings) {
        this(settings, sslSettings, (EventLoopGroup)new NioEventLoopGroup());
    }
    
    @Override
    public Stream create(final ServerAddress serverAddress) {
        return new NettyStream(serverAddress, this.inetAddressResolver, this.settings, this.sslSettings, this.eventLoopGroup, this.socketChannelClass, this.allocator, this.sslContext);
    }
}
