package cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.tlschannel;

import java.io.IOException;

public abstract class TlsChannelFlowControlException extends IOException
{
    private static final long serialVersionUID = -2394919487958591959L;
    
    public Throwable fillInStackTrace() {
        return (Throwable)this;
    }
}
