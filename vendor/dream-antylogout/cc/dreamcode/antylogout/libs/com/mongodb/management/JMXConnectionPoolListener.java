package cc.dreamcode.antylogout.libs.com.mongodb.management;

import java.util.Iterator;
import java.util.List;
import java.util.Arrays;
import javax.management.ObjectName;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.ConnectionId;
import cc.dreamcode.antylogout.libs.com.mongodb.lang.Nullable;
import cc.dreamcode.antylogout.libs.com.mongodb.event.ConnectionClosedEvent;
import cc.dreamcode.antylogout.libs.com.mongodb.event.ConnectionCreatedEvent;
import cc.dreamcode.antylogout.libs.com.mongodb.event.ConnectionCheckedInEvent;
import cc.dreamcode.antylogout.libs.com.mongodb.event.ConnectionCheckedOutEvent;
import cc.dreamcode.antylogout.libs.com.mongodb.event.ConnectionPoolClosedEvent;
import cc.dreamcode.antylogout.libs.com.mongodb.event.ConnectionPoolCreatedEvent;
import java.util.concurrent.ConcurrentHashMap;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.ServerId;
import java.util.concurrent.ConcurrentMap;
import cc.dreamcode.antylogout.libs.com.mongodb.event.ConnectionPoolListener;

public class JMXConnectionPoolListener implements ConnectionPoolListener
{
    private final ConcurrentMap<ServerId, ConnectionPoolStatistics> map;
    
    public JMXConnectionPoolListener() {
        this.map = (ConcurrentMap<ServerId, ConnectionPoolStatistics>)new ConcurrentHashMap();
    }
    
    @Override
    public void connectionPoolCreated(final ConnectionPoolCreatedEvent event) {
        final ConnectionPoolStatistics statistics = new ConnectionPoolStatistics(event);
        this.map.put((Object)event.getServerId(), (Object)statistics);
        MBeanServerFactory.getMBeanServer().registerMBean(statistics, this.getMBeanObjectName(event.getServerId()));
    }
    
    @Override
    public void connectionPoolClosed(final ConnectionPoolClosedEvent event) {
        this.map.remove((Object)event.getServerId());
        MBeanServerFactory.getMBeanServer().unregisterMBean(this.getMBeanObjectName(event.getServerId()));
    }
    
    @Override
    public void connectionCheckedOut(final ConnectionCheckedOutEvent event) {
        final ConnectionPoolStatistics statistics = this.getStatistics(event.getConnectionId());
        if (statistics != null) {
            statistics.connectionCheckedOut(event);
        }
    }
    
    @Override
    public void connectionCheckedIn(final ConnectionCheckedInEvent event) {
        final ConnectionPoolStatistics statistics = this.getStatistics(event.getConnectionId());
        if (statistics != null) {
            statistics.connectionCheckedIn(event);
        }
    }
    
    @Override
    public void connectionCreated(final ConnectionCreatedEvent event) {
        final ConnectionPoolStatistics statistics = this.getStatistics(event.getConnectionId());
        if (statistics != null) {
            statistics.connectionCreated(event);
        }
    }
    
    @Override
    public void connectionClosed(final ConnectionClosedEvent event) {
        final ConnectionPoolStatistics statistics = this.getStatistics(event.getConnectionId());
        if (statistics != null) {
            statistics.connectionClosed(event);
        }
    }
    
    String getMBeanObjectName(final ServerId serverId) {
        String name = String.format("org.mongodb.driver:type=ConnectionPool,clusterId=%s,host=%s,port=%s", new Object[] { this.ensureValidValue(serverId.getClusterId().getValue()), this.ensureValidValue(serverId.getAddress().getHost()), serverId.getAddress().getPort() });
        final String clusterDescription = serverId.getClusterId().getDescription();
        if (clusterDescription != null) {
            name = String.format("%s,description=%s", new Object[] { name, this.ensureValidValue(clusterDescription) });
        }
        return name;
    }
    
    @Nullable
    ConnectionPoolStatisticsMBean getMBean(final ServerId serverId) {
        return this.getStatistics(serverId);
    }
    
    @Nullable
    private ConnectionPoolStatistics getStatistics(final ConnectionId connectionId) {
        return this.getStatistics(connectionId.getServerId());
    }
    
    @Nullable
    private ConnectionPoolStatistics getStatistics(final ServerId serverId) {
        return (ConnectionPoolStatistics)this.map.get((Object)serverId);
    }
    
    private String ensureValidValue(final String value) {
        if (this.containsQuotableCharacter(value)) {
            return ObjectName.quote(value);
        }
        return value;
    }
    
    private boolean containsQuotableCharacter(@Nullable final String value) {
        if (value == null || value.length() == 0) {
            return false;
        }
        final List<String> quoteableCharacters = (List<String>)Arrays.asList((Object[])new String[] { ",", ":", "?", "*", "=", "\"", "\\", "\n" });
        for (final String quotable : quoteableCharacters) {
            if (value.contains((CharSequence)quotable)) {
                return true;
            }
        }
        return false;
    }
}
