package cc.dreamcode.antylogout.libs.com.mongodb.management;

import cc.dreamcode.antylogout.libs.com.mongodb.internal.diagnostics.logging.Loggers;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.diagnostics.logging.Logger;

class JMXMBeanServer implements MBeanServer
{
    private static final Logger LOGGER;
    private final javax.management.MBeanServer server;
    
    JMXMBeanServer() {
        this.server = ManagementFactory.getPlatformMBeanServer();
    }
    
    @Override
    public void registerMBean(final Object mBean, final String mBeanName) {
        try {
            this.server.registerMBean(mBean, new ObjectName(mBeanName));
        }
        catch (final Exception e) {
            JMXMBeanServer.LOGGER.warn("Unable to register MBean " + mBeanName, (Throwable)e);
        }
    }
    
    @Override
    public void unregisterMBean(final String mBeanName) {
        try {
            final ObjectName objectName = new ObjectName(mBeanName);
            if (this.server.isRegistered(objectName)) {
                this.server.unregisterMBean(objectName);
            }
        }
        catch (final Exception e) {
            JMXMBeanServer.LOGGER.warn("Unable to unregister MBean " + mBeanName, (Throwable)e);
        }
    }
    
    static {
        LOGGER = Loggers.getLogger("management");
    }
}
