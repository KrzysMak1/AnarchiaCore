package cc.dreamcode.antylogout.libs.com.mongodb.internal.event;

import cc.dreamcode.antylogout.libs.com.mongodb.internal.diagnostics.logging.Loggers;
import cc.dreamcode.antylogout.libs.com.mongodb.event.ServerDescriptionChangedEvent;
import cc.dreamcode.antylogout.libs.com.mongodb.event.ServerClosedEvent;
import java.util.Iterator;
import cc.dreamcode.antylogout.libs.com.mongodb.event.ServerOpeningEvent;
import java.util.Collection;
import java.util.ArrayList;
import cc.dreamcode.antylogout.libs.com.mongodb.assertions.Assertions;
import java.util.List;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.diagnostics.logging.Logger;
import cc.dreamcode.antylogout.libs.com.mongodb.event.ServerListener;

final class ServerListenerMulticaster implements ServerListener
{
    private static final Logger LOGGER;
    private final List<ServerListener> serverListeners;
    
    ServerListenerMulticaster(final List<ServerListener> serverListeners) {
        Assertions.isTrue("All ServerListener instances are non-null", !serverListeners.contains((Object)null));
        this.serverListeners = (List<ServerListener>)new ArrayList((Collection)serverListeners);
    }
    
    @Override
    public void serverOpening(final ServerOpeningEvent event) {
        for (final ServerListener cur : this.serverListeners) {
            try {
                cur.serverOpening(event);
            }
            catch (final Exception e) {
                if (!ServerListenerMulticaster.LOGGER.isWarnEnabled()) {
                    continue;
                }
                ServerListenerMulticaster.LOGGER.warn(String.format("Exception thrown raising server opening event to listener %s", new Object[] { cur }), (Throwable)e);
            }
        }
    }
    
    @Override
    public void serverClosed(final ServerClosedEvent event) {
        for (final ServerListener cur : this.serverListeners) {
            try {
                cur.serverClosed(event);
            }
            catch (final Exception e) {
                if (!ServerListenerMulticaster.LOGGER.isWarnEnabled()) {
                    continue;
                }
                ServerListenerMulticaster.LOGGER.warn(String.format("Exception thrown raising server opening event to listener %s", new Object[] { cur }), (Throwable)e);
            }
        }
    }
    
    @Override
    public void serverDescriptionChanged(final ServerDescriptionChangedEvent event) {
        for (final ServerListener cur : this.serverListeners) {
            try {
                cur.serverDescriptionChanged(event);
            }
            catch (final Exception e) {
                if (!ServerListenerMulticaster.LOGGER.isWarnEnabled()) {
                    continue;
                }
                ServerListenerMulticaster.LOGGER.warn(String.format("Exception thrown raising server description changed event to listener %s", new Object[] { cur }), (Throwable)e);
            }
        }
    }
    
    static {
        LOGGER = Loggers.getLogger("cluster.event");
    }
}
