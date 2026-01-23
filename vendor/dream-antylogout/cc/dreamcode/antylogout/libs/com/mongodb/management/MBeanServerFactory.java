package cc.dreamcode.antylogout.libs.com.mongodb.management;

final class MBeanServerFactory
{
    private static final MBeanServer M_BEAN_SERVER;
    
    private MBeanServerFactory() {
    }
    
    static MBeanServer getMBeanServer() {
        return MBeanServerFactory.M_BEAN_SERVER;
    }
    
    static {
        MBeanServer tmp;
        try {
            tmp = new JMXMBeanServer();
        }
        catch (final Throwable e) {
            tmp = new NullMBeanServer();
        }
        M_BEAN_SERVER = tmp;
    }
}
