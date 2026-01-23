package cc.dreamcode.antylogout.libs.com.mongodb.management;

class NullMBeanServer implements MBeanServer
{
    @Override
    public void unregisterMBean(final String mBeanName) {
    }
    
    @Override
    public void registerMBean(final Object mBean, final String mBeanName) {
    }
}
