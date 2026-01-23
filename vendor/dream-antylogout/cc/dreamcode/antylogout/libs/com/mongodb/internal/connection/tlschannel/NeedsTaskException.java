package cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.tlschannel;

public class NeedsTaskException extends TlsChannelFlowControlException
{
    private static final long serialVersionUID = -936451835836926915L;
    private final Runnable task;
    
    public NeedsTaskException(final Runnable task) {
        this.task = task;
    }
    
    public Runnable getTask() {
        return this.task;
    }
}
