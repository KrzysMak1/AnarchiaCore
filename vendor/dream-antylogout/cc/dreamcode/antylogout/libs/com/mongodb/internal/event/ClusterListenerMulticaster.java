package cc.dreamcode.antylogout.libs.com.mongodb.internal.event;

import cc.dreamcode.antylogout.libs.com.mongodb.internal.diagnostics.logging.Loggers;
import cc.dreamcode.antylogout.libs.com.mongodb.event.ClusterDescriptionChangedEvent;
import cc.dreamcode.antylogout.libs.com.mongodb.event.ClusterClosedEvent;
import java.util.Iterator;
import cc.dreamcode.antylogout.libs.com.mongodb.event.ClusterOpeningEvent;
import java.util.Collection;
import java.util.ArrayList;
import cc.dreamcode.antylogout.libs.com.mongodb.assertions.Assertions;
import java.util.List;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.diagnostics.logging.Logger;
import cc.dreamcode.antylogout.libs.com.mongodb.event.ClusterListener;

final class ClusterListenerMulticaster implements ClusterListener
{
    private static final Logger LOGGER;
    private final List<ClusterListener> clusterListeners;
    
    ClusterListenerMulticaster(final List<ClusterListener> clusterListeners) {
        Assertions.isTrue("All ClusterListener instances are non-null", !clusterListeners.contains((Object)null));
        this.clusterListeners = (List<ClusterListener>)new ArrayList((Collection)clusterListeners);
    }
    
    @Override
    public void clusterOpening(final ClusterOpeningEvent event) {
        for (final ClusterListener cur : this.clusterListeners) {
            try {
                cur.clusterOpening(event);
            }
            catch (final Exception e) {
                if (!ClusterListenerMulticaster.LOGGER.isWarnEnabled()) {
                    continue;
                }
                ClusterListenerMulticaster.LOGGER.warn(String.format("Exception thrown raising cluster opening event to listener %s", new Object[] { cur }), (Throwable)e);
            }
        }
    }
    
    @Override
    public void clusterClosed(final ClusterClosedEvent event) {
        for (final ClusterListener cur : this.clusterListeners) {
            try {
                cur.clusterClosed(event);
            }
            catch (final Exception e) {
                if (!ClusterListenerMulticaster.LOGGER.isWarnEnabled()) {
                    continue;
                }
                ClusterListenerMulticaster.LOGGER.warn(String.format("Exception thrown raising cluster closed event to listener %s", new Object[] { cur }), (Throwable)e);
            }
        }
    }
    
    @Override
    public void clusterDescriptionChanged(final ClusterDescriptionChangedEvent event) {
        for (final ClusterListener cur : this.clusterListeners) {
            try {
                cur.clusterDescriptionChanged(event);
            }
            catch (final Exception e) {
                if (!ClusterListenerMulticaster.LOGGER.isWarnEnabled()) {
                    continue;
                }
                ClusterListenerMulticaster.LOGGER.warn(String.format("Exception thrown raising cluster description changed event to listener %s", new Object[] { cur }), (Throwable)e);
            }
        }
    }
    
    static {
        LOGGER = Loggers.getLogger("cluster.event");
    }
}
