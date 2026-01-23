package cc.dreamcode.antylogout.libs.com.mongodb.management;

interface MBeanServer
{
    void unregisterMBean(final String p0);
    
    void registerMBean(final Object p0, final String p1);
}
