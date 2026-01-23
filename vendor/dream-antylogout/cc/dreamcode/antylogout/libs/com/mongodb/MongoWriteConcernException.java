package cc.dreamcode.antylogout.libs.com.mongodb;

import cc.dreamcode.antylogout.libs.com.mongodb.assertions.Assertions;
import cc.dreamcode.antylogout.libs.com.mongodb.lang.Nullable;
import java.util.Collection;
import java.util.Collections;
import cc.dreamcode.antylogout.libs.com.mongodb.bulk.WriteConcernError;

public class MongoWriteConcernException extends MongoServerException
{
    private static final long serialVersionUID = 4577579466973523211L;
    private final WriteConcernError writeConcernError;
    private final WriteConcernResult writeConcernResult;
    
    @Deprecated
    public MongoWriteConcernException(final WriteConcernError writeConcernError, final ServerAddress serverAddress) {
        this(writeConcernError, null, serverAddress, (Collection<String>)Collections.emptySet());
    }
    
    @Deprecated
    public MongoWriteConcernException(final WriteConcernError writeConcernError, @Nullable final WriteConcernResult writeConcernResult, final ServerAddress serverAddress) {
        this(writeConcernError, writeConcernResult, serverAddress, (Collection<String>)Collections.emptySet());
    }
    
    public MongoWriteConcernException(final WriteConcernError writeConcernError, @Nullable final WriteConcernResult writeConcernResult, final ServerAddress serverAddress, final Collection<String> errorLabels) {
        super(writeConcernError.getCode(), writeConcernError.getMessage(), serverAddress);
        this.writeConcernResult = writeConcernResult;
        this.writeConcernError = Assertions.notNull("writeConcernError", writeConcernError);
        this.addLabels(errorLabels);
    }
    
    public WriteConcernError getWriteConcernError() {
        return this.writeConcernError;
    }
    
    public WriteConcernResult getWriteResult() {
        return this.writeConcernResult;
    }
}
