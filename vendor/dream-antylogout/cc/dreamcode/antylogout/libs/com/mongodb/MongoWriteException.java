package cc.dreamcode.antylogout.libs.com.mongodb;

import java.util.Collection;
import java.util.Collections;

public class MongoWriteException extends MongoServerException
{
    private static final long serialVersionUID = -1906795074458258147L;
    private final WriteError error;
    
    @Deprecated
    public MongoWriteException(final WriteError error, final ServerAddress serverAddress) {
        this(error, serverAddress, (Collection<String>)Collections.emptySet());
    }
    
    public MongoWriteException(final WriteError error, final ServerAddress serverAddress, final Collection<String> errorLabels) {
        super(error.getCode(), "Write operation error on server " + (Object)serverAddress + ". Write error: " + (Object)error + ".", serverAddress);
        this.error = error;
        this.addLabels(errorLabels);
    }
    
    public WriteError getError() {
        return this.error;
    }
}
