package cc.dreamcode.antylogout.libs.com.mongodb.internal.dns;

import java.util.List;

public interface DnsResolver
{
    List<String> resolveHostFromSrvRecords(final String p0, final String p1);
    
    String resolveAdditionalQueryParametersFromTxtRecords(final String p0);
}
