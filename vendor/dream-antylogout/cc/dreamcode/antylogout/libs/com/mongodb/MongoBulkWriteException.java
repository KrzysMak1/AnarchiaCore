package cc.dreamcode.antylogout.libs.com.mongodb;

import java.util.Objects;
import java.util.Collection;
import java.util.Set;
import cc.dreamcode.antylogout.libs.com.mongodb.lang.Nullable;
import cc.dreamcode.antylogout.libs.com.mongodb.bulk.WriteConcernError;
import cc.dreamcode.antylogout.libs.com.mongodb.bulk.BulkWriteError;
import java.util.List;
import cc.dreamcode.antylogout.libs.com.mongodb.bulk.BulkWriteResult;

public class MongoBulkWriteException extends MongoServerException
{
    private static final long serialVersionUID = -4345399805987210275L;
    private final BulkWriteResult writeResult;
    private final List<BulkWriteError> errors;
    private final ServerAddress serverAddress;
    private final WriteConcernError writeConcernError;
    
    public MongoBulkWriteException(final BulkWriteResult writeResult, final List<BulkWriteError> writeErrors, @Nullable final WriteConcernError writeConcernError, final ServerAddress serverAddress, final Set<String> errorLabels) {
        super("Bulk write operation error on server " + (Object)serverAddress + ". " + (writeErrors.isEmpty() ? "" : ("Write errors: " + (Object)writeErrors + ". ")) + ((writeConcernError == null) ? "" : ("Write concern error: " + (Object)writeConcernError + ". ")), serverAddress);
        this.writeResult = writeResult;
        this.errors = writeErrors;
        this.writeConcernError = writeConcernError;
        this.serverAddress = serverAddress;
        this.addLabels((Collection<String>)errorLabels);
    }
    
    public BulkWriteResult getWriteResult() {
        return this.writeResult;
    }
    
    public List<BulkWriteError> getWriteErrors() {
        return this.errors;
    }
    
    @Nullable
    public WriteConcernError getWriteConcernError() {
        return this.writeConcernError;
    }
    
    @Override
    public ServerAddress getServerAddress() {
        return this.serverAddress;
    }
    
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final MongoBulkWriteException that = (MongoBulkWriteException)o;
        return this.errors.equals((Object)that.errors) && this.serverAddress.equals(that.serverAddress) && Objects.equals((Object)this.writeConcernError, (Object)that.writeConcernError) && this.writeResult.equals(that.writeResult);
    }
    
    public int hashCode() {
        int result = this.writeResult.hashCode();
        result = 31 * result + this.errors.hashCode();
        result = 31 * result + this.serverAddress.hashCode();
        result = 31 * result + ((this.writeConcernError != null) ? this.writeConcernError.hashCode() : 0);
        return result;
    }
}
