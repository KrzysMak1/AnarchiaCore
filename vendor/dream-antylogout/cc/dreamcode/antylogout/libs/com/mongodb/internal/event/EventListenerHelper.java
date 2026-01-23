package cc.dreamcode.antylogout.libs.com.mongodb.internal.event;

import cc.dreamcode.antylogout.libs.com.mongodb.connection.ConnectionPoolSettings;
import cc.dreamcode.antylogout.libs.com.mongodb.lang.Nullable;
import cc.dreamcode.antylogout.libs.com.mongodb.event.CommandListener;
import java.util.List;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.ServerSettings;
import cc.dreamcode.antylogout.libs.com.mongodb.assertions.Assertions;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.ClusterSettings;
import cc.dreamcode.antylogout.libs.com.mongodb.event.ConnectionPoolListener;
import cc.dreamcode.antylogout.libs.com.mongodb.event.ClusterListener;
import cc.dreamcode.antylogout.libs.com.mongodb.event.ServerMonitorListener;
import cc.dreamcode.antylogout.libs.com.mongodb.event.ServerListener;

public final class EventListenerHelper
{
    public static final ServerListener NO_OP_SERVER_LISTENER;
    public static final ServerMonitorListener NO_OP_SERVER_MONITOR_LISTENER;
    public static final ClusterListener NO_OP_CLUSTER_LISTENER;
    private static final ConnectionPoolListener NO_OP_CONNECTION_POOL_LISTENER;
    
    public static ClusterListener singleClusterListener(final ClusterSettings clusterSettings) {
        Assertions.assertTrue(clusterSettings.getClusterListeners().size() <= 1);
        return (ClusterListener)(clusterSettings.getClusterListeners().isEmpty() ? EventListenerHelper.NO_OP_CLUSTER_LISTENER : clusterSettings.getClusterListeners().get(0));
    }
    
    public static ServerListener singleServerListener(final ServerSettings serverSettings) {
        Assertions.assertTrue(serverSettings.getServerListeners().size() <= 1);
        return (ServerListener)(serverSettings.getServerListeners().isEmpty() ? EventListenerHelper.NO_OP_SERVER_LISTENER : serverSettings.getServerListeners().get(0));
    }
    
    public static ServerMonitorListener singleServerMonitorListener(final ServerSettings serverSettings) {
        Assertions.assertTrue(serverSettings.getServerMonitorListeners().size() <= 1);
        return (ServerMonitorListener)(serverSettings.getServerMonitorListeners().isEmpty() ? EventListenerHelper.NO_OP_SERVER_MONITOR_LISTENER : serverSettings.getServerMonitorListeners().get(0));
    }
    
    public static ClusterListener clusterListenerMulticaster(final List<ClusterListener> clusterListeners) {
        return new ClusterListenerMulticaster(clusterListeners);
    }
    
    public static ServerListener serverListenerMulticaster(final List<ServerListener> serverListeners) {
        return new ServerListenerMulticaster(serverListeners);
    }
    
    public static ServerMonitorListener serverMonitorListenerMulticaster(final List<ServerMonitorListener> serverMonitorListeners) {
        return new ServerMonitorListenerMulticaster(serverMonitorListeners);
    }
    
    @Nullable
    public static CommandListener getCommandListener(final List<CommandListener> commandListeners) {
        switch (commandListeners.size()) {
            case 0: {
                return null;
            }
            case 1: {
                return (CommandListener)commandListeners.get(0);
            }
            default: {
                return new CommandListenerMulticaster(commandListeners);
            }
        }
    }
    
    public static ConnectionPoolListener getConnectionPoolListener(final ConnectionPoolSettings connectionPoolSettings) {
        switch (connectionPoolSettings.getConnectionPoolListeners().size()) {
            case 0: {
                return EventListenerHelper.NO_OP_CONNECTION_POOL_LISTENER;
            }
            case 1: {
                return (ConnectionPoolListener)connectionPoolSettings.getConnectionPoolListeners().get(0);
            }
            default: {
                return new ConnectionPoolListenerMulticaster(connectionPoolSettings.getConnectionPoolListeners());
            }
        }
    }
    
    private EventListenerHelper() {
    }
    
    static {
        NO_OP_SERVER_LISTENER = new ServerListener() {};
        NO_OP_SERVER_MONITOR_LISTENER = new ServerMonitorListener() {};
        NO_OP_CLUSTER_LISTENER = new ClusterListener() {};
        NO_OP_CONNECTION_POOL_LISTENER = new ConnectionPoolListener() {};
    }
}
