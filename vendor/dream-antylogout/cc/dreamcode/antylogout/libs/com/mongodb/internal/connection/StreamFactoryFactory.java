package cc.dreamcode.antylogout.libs.com.mongodb.internal.connection;

import cc.dreamcode.antylogout.libs.com.mongodb.connection.SslSettings;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.SocketSettings;

public interface StreamFactoryFactory extends AutoCloseable
{
    StreamFactory create(final SocketSettings p0, final SslSettings p1);
    
    void close();
}
