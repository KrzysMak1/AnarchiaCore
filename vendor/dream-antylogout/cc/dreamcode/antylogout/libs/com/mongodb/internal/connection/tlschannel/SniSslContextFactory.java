package cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.tlschannel;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SNIServerName;
import java.util.Optional;

@FunctionalInterface
public interface SniSslContextFactory
{
    Optional<SSLContext> getSslContext(final Optional<SNIServerName> p0);
}
