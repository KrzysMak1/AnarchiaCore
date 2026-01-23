package cc.dreamcode.antylogout.libs.com.mongodb.spi.dns;

import java.util.List;
import cc.dreamcode.antylogout.libs.com.mongodb.annotations.ThreadSafe;

@ThreadSafe
public interface DnsClient
{
    List<String> getResourceRecordData(final String p0, final String p1) throws DnsException;
}
