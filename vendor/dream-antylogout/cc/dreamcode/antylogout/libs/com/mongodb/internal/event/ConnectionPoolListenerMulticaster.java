package cc.dreamcode.antylogout.libs.com.mongodb.internal.event;

import cc.dreamcode.antylogout.libs.com.mongodb.internal.diagnostics.logging.Loggers;
import cc.dreamcode.antylogout.libs.com.mongodb.event.ConnectionClosedEvent;
import cc.dreamcode.antylogout.libs.com.mongodb.event.ConnectionReadyEvent;
import cc.dreamcode.antylogout.libs.com.mongodb.event.ConnectionCreatedEvent;
import cc.dreamcode.antylogout.libs.com.mongodb.event.ConnectionCheckedInEvent;
import cc.dreamcode.antylogout.libs.com.mongodb.event.ConnectionCheckOutFailedEvent;
import cc.dreamcode.antylogout.libs.com.mongodb.event.ConnectionCheckedOutEvent;
import cc.dreamcode.antylogout.libs.com.mongodb.event.ConnectionCheckOutStartedEvent;
import cc.dreamcode.antylogout.libs.com.mongodb.event.ConnectionPoolClosedEvent;
import cc.dreamcode.antylogout.libs.com.mongodb.event.ConnectionPoolReadyEvent;
import cc.dreamcode.antylogout.libs.com.mongodb.event.ConnectionPoolClearedEvent;
import java.util.Iterator;
import cc.dreamcode.antylogout.libs.com.mongodb.event.ConnectionPoolCreatedEvent;
import java.util.Collection;
import java.util.ArrayList;
import cc.dreamcode.antylogout.libs.com.mongodb.assertions.Assertions;
import java.util.List;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.diagnostics.logging.Logger;
import cc.dreamcode.antylogout.libs.com.mongodb.event.ConnectionPoolListener;

final class ConnectionPoolListenerMulticaster implements ConnectionPoolListener
{
    private static final Logger LOGGER;
    private final List<ConnectionPoolListener> connectionPoolListeners;
    
    ConnectionPoolListenerMulticaster(final List<ConnectionPoolListener> connectionPoolListeners) {
        Assertions.isTrue("All ConnectionPoolListener instances are non-null", !connectionPoolListeners.contains((Object)null));
        this.connectionPoolListeners = (List<ConnectionPoolListener>)new ArrayList((Collection)connectionPoolListeners);
    }
    
    @Override
    public void connectionPoolCreated(final ConnectionPoolCreatedEvent event) {
        for (final ConnectionPoolListener cur : this.connectionPoolListeners) {
            try {
                cur.connectionPoolCreated(event);
            }
            catch (final Exception e) {
                if (!ConnectionPoolListenerMulticaster.LOGGER.isWarnEnabled()) {
                    continue;
                }
                ConnectionPoolListenerMulticaster.LOGGER.warn(String.format("Exception thrown raising connection pool created event to listener %s", new Object[] { cur }), (Throwable)e);
            }
        }
    }
    
    @Override
    public void connectionPoolCleared(final ConnectionPoolClearedEvent event) {
        for (final ConnectionPoolListener cur : this.connectionPoolListeners) {
            try {
                cur.connectionPoolCleared(event);
            }
            catch (final Exception e) {
                if (!ConnectionPoolListenerMulticaster.LOGGER.isWarnEnabled()) {
                    continue;
                }
                ConnectionPoolListenerMulticaster.LOGGER.warn(String.format("Exception thrown raising connection pool cleared event to listener %s", new Object[] { cur }), (Throwable)e);
            }
        }
    }
    
    @Override
    public void connectionPoolReady(final ConnectionPoolReadyEvent event) {
        for (final ConnectionPoolListener cur : this.connectionPoolListeners) {
            try {
                cur.connectionPoolReady(event);
            }
            catch (final Exception e) {
                if (!ConnectionPoolListenerMulticaster.LOGGER.isWarnEnabled()) {
                    continue;
                }
                ConnectionPoolListenerMulticaster.LOGGER.warn(String.format("Exception thrown raising connection pool ready event to listener %s", new Object[] { cur }), (Throwable)e);
            }
        }
    }
    
    @Override
    public void connectionPoolClosed(final ConnectionPoolClosedEvent event) {
        for (final ConnectionPoolListener cur : this.connectionPoolListeners) {
            try {
                cur.connectionPoolClosed(event);
            }
            catch (final Exception e) {
                if (!ConnectionPoolListenerMulticaster.LOGGER.isWarnEnabled()) {
                    continue;
                }
                ConnectionPoolListenerMulticaster.LOGGER.warn(String.format("Exception thrown raising connection pool closed event to listener %s", new Object[] { cur }), (Throwable)e);
            }
        }
    }
    
    @Override
    public void connectionCheckOutStarted(final ConnectionCheckOutStartedEvent event) {
        for (final ConnectionPoolListener cur : this.connectionPoolListeners) {
            try {
                cur.connectionCheckOutStarted(event);
            }
            catch (final Exception e) {
                if (!ConnectionPoolListenerMulticaster.LOGGER.isWarnEnabled()) {
                    continue;
                }
                ConnectionPoolListenerMulticaster.LOGGER.warn(String.format("Exception thrown raising connection check out started event to listener %s", new Object[] { cur }), (Throwable)e);
            }
        }
    }
    
    @Override
    public void connectionCheckedOut(final ConnectionCheckedOutEvent event) {
        for (final ConnectionPoolListener cur : this.connectionPoolListeners) {
            try {
                cur.connectionCheckedOut(event);
            }
            catch (final Exception e) {
                if (!ConnectionPoolListenerMulticaster.LOGGER.isWarnEnabled()) {
                    continue;
                }
                ConnectionPoolListenerMulticaster.LOGGER.warn(String.format("Exception thrown raising connection pool checked out event to listener %s", new Object[] { cur }), (Throwable)e);
            }
        }
    }
    
    @Override
    public void connectionCheckOutFailed(final ConnectionCheckOutFailedEvent event) {
        for (final ConnectionPoolListener cur : this.connectionPoolListeners) {
            try {
                cur.connectionCheckOutFailed(event);
            }
            catch (final Exception e) {
                if (!ConnectionPoolListenerMulticaster.LOGGER.isWarnEnabled()) {
                    continue;
                }
                ConnectionPoolListenerMulticaster.LOGGER.warn(String.format("Exception thrown raising connection pool check out failed event to listener %s", new Object[] { cur }), (Throwable)e);
            }
        }
    }
    
    @Override
    public void connectionCheckedIn(final ConnectionCheckedInEvent event) {
        for (final ConnectionPoolListener cur : this.connectionPoolListeners) {
            try {
                cur.connectionCheckedIn(event);
            }
            catch (final Exception e) {
                if (!ConnectionPoolListenerMulticaster.LOGGER.isWarnEnabled()) {
                    continue;
                }
                ConnectionPoolListenerMulticaster.LOGGER.warn(String.format("Exception thrown raising connection pool checked in event to listener %s", new Object[] { cur }), (Throwable)e);
            }
        }
    }
    
    @Override
    public void connectionCreated(final ConnectionCreatedEvent event) {
        for (final ConnectionPoolListener cur : this.connectionPoolListeners) {
            try {
                cur.connectionCreated(event);
            }
            catch (final Exception e) {
                if (!ConnectionPoolListenerMulticaster.LOGGER.isWarnEnabled()) {
                    continue;
                }
                ConnectionPoolListenerMulticaster.LOGGER.warn(String.format("Exception thrown raising connection pool connection created event to listener %s", new Object[] { cur }), (Throwable)e);
            }
        }
    }
    
    @Override
    public void connectionReady(final ConnectionReadyEvent event) {
        for (final ConnectionPoolListener cur : this.connectionPoolListeners) {
            try {
                cur.connectionReady(event);
            }
            catch (final Exception e) {
                if (!ConnectionPoolListenerMulticaster.LOGGER.isWarnEnabled()) {
                    continue;
                }
                ConnectionPoolListenerMulticaster.LOGGER.warn(String.format("Exception thrown raising connection pool connection ready event to listener %s", new Object[] { cur }), (Throwable)e);
            }
        }
    }
    
    @Override
    public void connectionClosed(final ConnectionClosedEvent event) {
        for (final ConnectionPoolListener cur : this.connectionPoolListeners) {
            try {
                cur.connectionClosed(event);
            }
            catch (final Exception e) {
                if (!ConnectionPoolListenerMulticaster.LOGGER.isWarnEnabled()) {
                    continue;
                }
                ConnectionPoolListenerMulticaster.LOGGER.warn(String.format("Exception thrown raising connection pool connection removed event to listener %s", new Object[] { cur }), (Throwable)e);
            }
        }
    }
    
    static {
        LOGGER = Loggers.getLogger("protocol.event");
    }
}
