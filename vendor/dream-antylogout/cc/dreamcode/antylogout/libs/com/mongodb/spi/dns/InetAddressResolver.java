package cc.dreamcode.antylogout.libs.com.mongodb.spi.dns;

import java.net.UnknownHostException;
import java.net.InetAddress;
import java.util.List;
import cc.dreamcode.antylogout.libs.com.mongodb.annotations.ThreadSafe;

@ThreadSafe
public interface InetAddressResolver
{
    List<InetAddress> lookupByName(final String p0) throws UnknownHostException;
}
