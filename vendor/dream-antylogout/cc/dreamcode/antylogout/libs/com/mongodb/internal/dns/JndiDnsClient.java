package cc.dreamcode.antylogout.libs.com.mongodb.internal.dns;

import cc.dreamcode.antylogout.libs.com.mongodb.MongoClientException;
import java.util.Hashtable;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.InitialDirContext;
import javax.naming.NamingException;
import cc.dreamcode.antylogout.libs.com.mongodb.spi.dns.DnsException;
import javax.naming.NameNotFoundException;
import cc.dreamcode.antylogout.libs.com.mongodb.spi.dns.DnsWithResponseCodeException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import cc.dreamcode.antylogout.libs.com.mongodb.spi.dns.DnsClient;

final class JndiDnsClient implements DnsClient
{
    @Override
    public List<String> getResourceRecordData(final String name, final String type) throws DnsException {
        final InitialDirContext dirContext = createDnsDirContext();
        try {
            final Attribute attribute = dirContext.getAttributes(name, new String[] { type }).get(type);
            if (attribute == null) {
                return (List<String>)Collections.emptyList();
            }
            final List<String> attributeValues = (List<String>)new ArrayList();
            final NamingEnumeration<?> namingEnumeration = (NamingEnumeration<?>)attribute.getAll();
            while (namingEnumeration.hasMore()) {
                attributeValues.add((Object)namingEnumeration.next());
            }
            return attributeValues;
        }
        catch (final NameNotFoundException e) {
            throw new DnsWithResponseCodeException(e.getMessage(), 3, (Throwable)e);
        }
        catch (final NamingException e2) {
            throw new DnsException(e2.getMessage(), (Throwable)e2);
        }
        finally {
            try {
                dirContext.close();
            }
            catch (final NamingException ex) {}
        }
    }
    
    private static InitialDirContext createDnsDirContext() {
        final Hashtable<String, String> envProps = (Hashtable<String, String>)new Hashtable();
        envProps.put((Object)"java.naming.factory.initial", (Object)"com.sun.jndi.dns.DnsContextFactory");
        try {
            return new InitialDirContext((Hashtable)envProps);
        }
        catch (final NamingException e) {
            envProps.put((Object)"java.naming.provider.url", (Object)"dns:");
            try {
                return new InitialDirContext((Hashtable)envProps);
            }
            catch (final NamingException ex) {
                throw new MongoClientException("Unable to support mongodb+srv// style connections as the 'com.sun.jndi.dns.DnsContextFactory' class is not available in this JRE. A JNDI context is required for resolving SRV records.", (Throwable)e);
            }
        }
    }
}
