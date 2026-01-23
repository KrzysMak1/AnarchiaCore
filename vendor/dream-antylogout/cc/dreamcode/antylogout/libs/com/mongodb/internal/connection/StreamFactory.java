package cc.dreamcode.antylogout.libs.com.mongodb.internal.connection;

import cc.dreamcode.antylogout.libs.com.mongodb.ServerAddress;

public interface StreamFactory
{
    Stream create(final ServerAddress p0);
}
