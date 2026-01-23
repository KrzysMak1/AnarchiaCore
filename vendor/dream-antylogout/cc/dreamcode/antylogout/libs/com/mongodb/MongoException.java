package cc.dreamcode.antylogout.libs.com.mongodb;

import java.util.Iterator;
import cc.dreamcode.antylogout.libs.org.bson.BsonValue;
import java.util.Collections;
import cc.dreamcode.antylogout.libs.com.mongodb.assertions.Assertions;
import cc.dreamcode.antylogout.libs.org.bson.BsonArray;
import cc.dreamcode.antylogout.libs.org.bson.BsonDocument;
import java.util.Collection;
import java.util.HashSet;
import cc.dreamcode.antylogout.libs.com.mongodb.lang.Nullable;
import java.util.Set;

public class MongoException extends RuntimeException
{
    public static final String TRANSIENT_TRANSACTION_ERROR_LABEL = "TransientTransactionError";
    public static final String UNKNOWN_TRANSACTION_COMMIT_RESULT_LABEL = "UnknownTransactionCommitResult";
    private static final long serialVersionUID = -4415279469780082174L;
    private final int code;
    private final Set<String> errorLabels;
    
    @Nullable
    public static MongoException fromThrowable(@Nullable final Throwable t) {
        if (t == null) {
            return null;
        }
        return fromThrowableNonNull(t);
    }
    
    public static MongoException fromThrowableNonNull(final Throwable t) {
        if (t instanceof MongoException) {
            return (MongoException)t;
        }
        return new MongoException(t.getMessage(), t);
    }
    
    public MongoException(final String msg) {
        super(msg);
        this.errorLabels = (Set<String>)new HashSet();
        this.code = -3;
    }
    
    public MongoException(final int code, final String msg) {
        super(msg);
        this.errorLabels = (Set<String>)new HashSet();
        this.code = code;
    }
    
    public MongoException(@Nullable final String msg, @Nullable final Throwable t) {
        super(msg, t);
        this.errorLabels = (Set<String>)new HashSet();
        this.code = -4;
    }
    
    public MongoException(final int code, final String msg, final Throwable t) {
        super(msg, t);
        this.errorLabels = (Set<String>)new HashSet();
        this.code = code;
        if (t instanceof MongoException) {
            this.addLabels((Collection<String>)((MongoException)t).getErrorLabels());
        }
    }
    
    public MongoException(final int code, final String msg, final BsonDocument response) {
        super(msg);
        this.errorLabels = (Set<String>)new HashSet();
        this.code = code;
        this.addLabels(response.getArray("errorLabels", new BsonArray()));
    }
    
    public int getCode() {
        return this.code;
    }
    
    public void addLabel(final String errorLabel) {
        Assertions.notNull("errorLabel", errorLabel);
        this.errorLabels.add((Object)errorLabel);
    }
    
    public void removeLabel(final String errorLabel) {
        Assertions.notNull("errorLabel", errorLabel);
        this.errorLabels.remove((Object)errorLabel);
    }
    
    public Set<String> getErrorLabels() {
        return (Set<String>)Collections.unmodifiableSet((Set)this.errorLabels);
    }
    
    public boolean hasErrorLabel(final String errorLabel) {
        Assertions.notNull("errorLabel", errorLabel);
        return this.errorLabels.contains((Object)errorLabel);
    }
    
    protected void addLabels(final BsonArray labels) {
        for (final BsonValue errorLabel : labels) {
            this.addLabel(errorLabel.asString().getValue());
        }
    }
    
    protected void addLabels(final Collection<String> labels) {
        for (final String errorLabel : labels) {
            this.addLabel(errorLabel);
        }
    }
}
