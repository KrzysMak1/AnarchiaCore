package cc.dreamcode.antylogout.libs.com.mongodb.management;

import cc.dreamcode.antylogout.libs.com.mongodb.event.ConnectionClosedEvent;
import cc.dreamcode.antylogout.libs.com.mongodb.event.ConnectionCreatedEvent;
import cc.dreamcode.antylogout.libs.com.mongodb.event.ConnectionCheckedInEvent;
import cc.dreamcode.antylogout.libs.com.mongodb.event.ConnectionCheckedOutEvent;
import cc.dreamcode.antylogout.libs.com.mongodb.event.ConnectionPoolCreatedEvent;
import java.util.concurrent.atomic.AtomicInteger;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.ConnectionPoolSettings;
import cc.dreamcode.antylogout.libs.com.mongodb.ServerAddress;
import cc.dreamcode.antylogout.libs.com.mongodb.event.ConnectionPoolListener;

final class ConnectionPoolStatistics implements ConnectionPoolListener, ConnectionPoolStatisticsMBean
{
    private final ServerAddress serverAddress;
    private final ConnectionPoolSettings settings;
    private final AtomicInteger size;
    private final AtomicInteger checkedOutCount;
    
    ConnectionPoolStatistics(final ConnectionPoolCreatedEvent event) {
        this.size = new AtomicInteger();
        this.checkedOutCount = new AtomicInteger();
        this.serverAddress = event.getServerId().getAddress();
        this.settings = event.getSettings();
    }
    
    @Override
    public String getHost() {
        return this.serverAddress.getHost();
    }
    
    @Override
    public int getPort() {
        return this.serverAddress.getPort();
    }
    
    @Override
    public int getMinSize() {
        return this.settings.getMinSize();
    }
    
    @Override
    public int getMaxSize() {
        return this.settings.getMaxSize();
    }
    
    @Override
    public int getSize() {
        return this.size.get();
    }
    
    @Override
    public int getCheckedOutCount() {
        return this.checkedOutCount.get();
    }
    
    @Override
    public void connectionCheckedOut(final ConnectionCheckedOutEvent event) {
        this.checkedOutCount.incrementAndGet();
    }
    
    @Override
    public void connectionCheckedIn(final ConnectionCheckedInEvent event) {
        this.checkedOutCount.decrementAndGet();
    }
    
    @Override
    public void connectionCreated(final ConnectionCreatedEvent event) {
        this.size.incrementAndGet();
    }
    
    @Override
    public void connectionClosed(final ConnectionClosedEvent event) {
        this.size.decrementAndGet();
    }
}
