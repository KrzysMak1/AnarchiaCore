package cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.netty;

import io.netty.handler.ssl.ReferenceCountedOpenSslClientContext;
import cc.dreamcode.antylogout.libs.com.mongodb.assertions.Assertions;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.NettyTransportSettings;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.util.Objects;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.StreamFactory;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.SslSettings;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.SocketSettings;
import cc.dreamcode.antylogout.libs.com.mongodb.spi.dns.InetAddressResolver;
import cc.dreamcode.antylogout.libs.com.mongodb.lang.Nullable;
import io.netty.handler.ssl.SslContext;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.EventLoopGroup;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.StreamFactoryFactory;

public final class NettyStreamFactoryFactory implements StreamFactoryFactory
{
    private final EventLoopGroup eventLoopGroup;
    private final boolean ownsEventLoopGroup;
    private final Class<? extends SocketChannel> socketChannelClass;
    private final ByteBufAllocator allocator;
    @Nullable
    private final SslContext sslContext;
    private final InetAddressResolver inetAddressResolver;
    
    public static Builder builder() {
        return new Builder();
    }
    
    EventLoopGroup getEventLoopGroup() {
        return this.eventLoopGroup;
    }
    
    Class<? extends SocketChannel> getSocketChannelClass() {
        return this.socketChannelClass;
    }
    
    ByteBufAllocator getAllocator() {
        return this.allocator;
    }
    
    @Nullable
    SslContext getSslContext() {
        return this.sslContext;
    }
    
    @Override
    public StreamFactory create(final SocketSettings socketSettings, final SslSettings sslSettings) {
        return new NettyStreamFactory(this.inetAddressResolver, socketSettings, sslSettings, this.eventLoopGroup, this.socketChannelClass, this.allocator, this.sslContext);
    }
    
    @Override
    public void close() {
        if (this.ownsEventLoopGroup) {
            this.eventLoopGroup.shutdownGracefully();
        }
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final NettyStreamFactoryFactory that = (NettyStreamFactoryFactory)o;
        return Objects.equals((Object)this.eventLoopGroup, (Object)that.eventLoopGroup) && Objects.equals((Object)this.socketChannelClass, (Object)that.socketChannelClass) && Objects.equals((Object)this.allocator, (Object)that.allocator) && Objects.equals((Object)this.sslContext, (Object)that.sslContext) && Objects.equals((Object)this.inetAddressResolver, (Object)that.inetAddressResolver);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(new Object[] { this.eventLoopGroup, this.socketChannelClass, this.allocator, this.sslContext, this.inetAddressResolver });
    }
    
    private NettyStreamFactoryFactory(final Builder builder) {
        this.allocator = ((builder.allocator == null) ? ByteBufAllocator.DEFAULT : builder.allocator);
        this.socketChannelClass = (Class<? extends SocketChannel>)((builder.socketChannelClass == null) ? NioSocketChannel.class : builder.socketChannelClass);
        this.eventLoopGroup = (EventLoopGroup)((builder.eventLoopGroup == null) ? new NioEventLoopGroup() : builder.eventLoopGroup);
        this.ownsEventLoopGroup = (builder.eventLoopGroup == null);
        this.sslContext = builder.sslContext;
        this.inetAddressResolver = builder.inetAddressResolver;
    }
    
    public static final class Builder
    {
        private ByteBufAllocator allocator;
        private Class<? extends SocketChannel> socketChannelClass;
        private EventLoopGroup eventLoopGroup;
        @Nullable
        private SslContext sslContext;
        private InetAddressResolver inetAddressResolver;
        
        private Builder() {
        }
        
        public Builder applySettings(final NettyTransportSettings settings) {
            this.allocator = settings.getAllocator();
            this.eventLoopGroup = settings.getEventLoopGroup();
            this.sslContext = settings.getSslContext();
            this.socketChannelClass = settings.getSocketChannelClass();
            return this;
        }
        
        public Builder allocator(final ByteBufAllocator allocator) {
            this.allocator = Assertions.notNull("allocator", allocator);
            return this;
        }
        
        public Builder socketChannelClass(final Class<? extends SocketChannel> socketChannelClass) {
            this.socketChannelClass = Assertions.notNull("socketChannelClass", socketChannelClass);
            return this;
        }
        
        public Builder eventLoopGroup(final EventLoopGroup eventLoopGroup) {
            this.eventLoopGroup = Assertions.notNull("eventLoopGroup", eventLoopGroup);
            return this;
        }
        
        public Builder sslContext(final SslContext sslContext) {
            this.sslContext = Assertions.notNull("sslContext", sslContext);
            Assertions.isTrueArgument("sslContext must be client-side", sslContext.isClient());
            Assertions.isTrueArgument("sslContext must use either SslProvider.JDK or SslProvider.OPENSSL TLS/SSL protocol provider", !(sslContext instanceof ReferenceCountedOpenSslClientContext));
            return this;
        }
        
        public Builder inetAddressResolver(final InetAddressResolver inetAddressResolver) {
            this.inetAddressResolver = inetAddressResolver;
            return this;
        }
        
        public NettyStreamFactoryFactory build() {
            return new NettyStreamFactoryFactory(this, null);
        }
    }
}
