package cc.dreamcode.antylogout.libs.com.mongodb.internal.event;

import cc.dreamcode.antylogout.libs.com.mongodb.internal.diagnostics.logging.Loggers;
import cc.dreamcode.antylogout.libs.com.mongodb.event.ServerHeartbeatFailedEvent;
import cc.dreamcode.antylogout.libs.com.mongodb.event.ServerHeartbeatSucceededEvent;
import java.util.Iterator;
import cc.dreamcode.antylogout.libs.com.mongodb.event.ServerHeartbeatStartedEvent;
import java.util.Collection;
import java.util.ArrayList;
import cc.dreamcode.antylogout.libs.com.mongodb.assertions.Assertions;
import java.util.List;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.diagnostics.logging.Logger;
import cc.dreamcode.antylogout.libs.com.mongodb.event.ServerMonitorListener;

final class ServerMonitorListenerMulticaster implements ServerMonitorListener
{
    private static final Logger LOGGER;
    private final List<ServerMonitorListener> serverMonitorListeners;
    
    ServerMonitorListenerMulticaster(final List<ServerMonitorListener> serverMonitorListeners) {
        Assertions.isTrue("All ServerMonitorListener instances are non-null", !serverMonitorListeners.contains((Object)null));
        this.serverMonitorListeners = (List<ServerMonitorListener>)new ArrayList((Collection)serverMonitorListeners);
    }
    
    @Override
    public void serverHearbeatStarted(final ServerHeartbeatStartedEvent event) {
        for (final ServerMonitorListener cur : this.serverMonitorListeners) {
            try {
                cur.serverHearbeatStarted(event);
            }
            catch (final Exception e) {
                if (!ServerMonitorListenerMulticaster.LOGGER.isWarnEnabled()) {
                    continue;
                }
                ServerMonitorListenerMulticaster.LOGGER.warn(String.format("Exception thrown raising server heartbeat started event to listener %s", new Object[] { cur }), (Throwable)e);
            }
        }
    }
    
    @Override
    public void serverHeartbeatSucceeded(final ServerHeartbeatSucceededEvent event) {
        for (final ServerMonitorListener cur : this.serverMonitorListeners) {
            try {
                cur.serverHeartbeatSucceeded(event);
            }
            catch (final Exception e) {
                if (!ServerMonitorListenerMulticaster.LOGGER.isWarnEnabled()) {
                    continue;
                }
                ServerMonitorListenerMulticaster.LOGGER.warn(String.format("Exception thrown raising server heartbeat succeeded event to listener %s", new Object[] { cur }), (Throwable)e);
            }
        }
    }
    
    @Override
    public void serverHeartbeatFailed(final ServerHeartbeatFailedEvent event) {
        for (final ServerMonitorListener cur : this.serverMonitorListeners) {
            try {
                cur.serverHeartbeatFailed(event);
            }
            catch (final Exception e) {
                if (!ServerMonitorListenerMulticaster.LOGGER.isWarnEnabled()) {
                    continue;
                }
                ServerMonitorListenerMulticaster.LOGGER.warn(String.format("Exception thrown raising server heartbeat failed event to listener %s", new Object[] { cur }), (Throwable)e);
            }
        }
    }
    
    static {
        LOGGER = Loggers.getLogger("cluster.event");
    }
}
