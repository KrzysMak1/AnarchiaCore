package cc.dreamcode.antylogout.libs.com.zaxxer.hikari.pool;

import java.util.concurrent.ScheduledExecutorService;

class ProxyLeakTaskFactory
{
    private ScheduledExecutorService executorService;
    private long leakDetectionThreshold;
    
    ProxyLeakTaskFactory(final long leakDetectionThreshold, final ScheduledExecutorService executorService) {
        this.executorService = executorService;
        this.leakDetectionThreshold = leakDetectionThreshold;
    }
    
    ProxyLeakTask schedule(final PoolEntry poolEntry) {
        return (this.leakDetectionThreshold == 0L) ? ProxyLeakTask.NO_LEAK : this.scheduleNewTask(poolEntry);
    }
    
    void updateLeakDetectionThreshold(final long leakDetectionThreshold) {
        this.leakDetectionThreshold = leakDetectionThreshold;
    }
    
    private ProxyLeakTask scheduleNewTask(final PoolEntry poolEntry) {
        final ProxyLeakTask task = new ProxyLeakTask(poolEntry);
        task.schedule(this.executorService, this.leakDetectionThreshold);
        return task;
    }
}
