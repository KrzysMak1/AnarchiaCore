package cc.dreamcode.antylogout.libs.com.mongodb.internal.dns;

import java.util.stream.StreamSupport;
import java.util.ServiceLoader;
import cc.dreamcode.antylogout.libs.com.mongodb.spi.dns.DnsClientProvider;
import cc.dreamcode.antylogout.libs.com.mongodb.spi.dns.DnsWithResponseCodeException;
import java.util.Iterator;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoConfigurationException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import cc.dreamcode.antylogout.libs.com.mongodb.lang.Nullable;
import cc.dreamcode.antylogout.libs.com.mongodb.spi.dns.DnsClient;

public final class DefaultDnsResolver implements DnsResolver
{
    private static final DnsClient DEFAULT_DNS_CLIENT;
    private final DnsClient dnsClient;
    
    public DefaultDnsResolver() {
        this(DefaultDnsResolver.DEFAULT_DNS_CLIENT);
    }
    
    public DefaultDnsResolver(@Nullable final DnsClient dnsClient) {
        this.dnsClient = ((dnsClient == null) ? DefaultDnsResolver.DEFAULT_DNS_CLIENT : dnsClient);
    }
    
    @Override
    public List<String> resolveHostFromSrvRecords(final String srvHost, final String srvServiceName) {
        final String srvHostDomain = srvHost.substring(srvHost.indexOf(46) + 1);
        final List<String> srvHostDomainParts = (List<String>)Arrays.asList((Object[])srvHostDomain.split("\\."));
        final List<String> hosts = (List<String>)new ArrayList();
        final String resourceName = "_" + srvServiceName + "._tcp." + srvHost;
        try {
            final List<String> srvAttributeValues = this.dnsClient.getResourceRecordData(resourceName, "SRV");
            if (srvAttributeValues == null || srvAttributeValues.isEmpty()) {
                throw new MongoConfigurationException(String.format("No SRV records available for '%s'.", new Object[] { resourceName }));
            }
            for (final String srvRecord : srvAttributeValues) {
                final String[] split = srvRecord.split(" ");
                final String resolvedHost = split[3].endsWith(".") ? split[3].substring(0, split[3].length() - 1) : split[3];
                final String resolvedHostDomain = resolvedHost.substring(resolvedHost.indexOf(46) + 1);
                if (!sameParentDomain(srvHostDomainParts, resolvedHostDomain)) {
                    throw new MongoConfigurationException(String.format("The SRV host name '%s' resolved to a host '%s 'that is not in a sub-domain of the SRV host.", new Object[] { srvHost, resolvedHost }));
                }
                hosts.add((Object)(resolvedHost + ":" + split[2]));
            }
        }
        catch (final Exception e) {
            throw new MongoConfigurationException(String.format("Failed looking up SRV record for '%s'.", new Object[] { resourceName }), (Throwable)e);
        }
        return hosts;
    }
    
    private static boolean sameParentDomain(final List<String> srvHostDomainParts, final String resolvedHostDomain) {
        final List<String> resolvedHostDomainParts = (List<String>)Arrays.asList((Object[])resolvedHostDomain.split("\\."));
        return srvHostDomainParts.size() <= resolvedHostDomainParts.size() && resolvedHostDomainParts.subList(resolvedHostDomainParts.size() - srvHostDomainParts.size(), resolvedHostDomainParts.size()).equals((Object)srvHostDomainParts);
    }
    
    @Override
    public String resolveAdditionalQueryParametersFromTxtRecords(final String host) {
        try {
            final List<String> attributeValues = this.dnsClient.getResourceRecordData(host, "TXT");
            if (attributeValues == null || attributeValues.isEmpty()) {
                return "";
            }
            if (attributeValues.size() > 1) {
                throw new MongoConfigurationException(String.format("Multiple TXT records found for host '%s'.  Only one is permitted", new Object[] { host }));
            }
            return ((String)attributeValues.get(0)).replaceAll("\\s", "");
        }
        catch (final DnsWithResponseCodeException e) {
            if (e.getResponseCode() != 3) {
                throw new MongoConfigurationException("Failed looking up TXT record for host " + host, (Throwable)e);
            }
            return "";
        }
        catch (final Exception e2) {
            throw new MongoConfigurationException("Failed looking up TXT record for host " + host, (Throwable)e2);
        }
    }
    
    static {
        DEFAULT_DNS_CLIENT = (DnsClient)StreamSupport.stream(ServiceLoader.load((Class)DnsClientProvider.class).spliterator(), false).findFirst().map(DnsClientProvider::create).orElse((Object)new JndiDnsClient());
    }
}
