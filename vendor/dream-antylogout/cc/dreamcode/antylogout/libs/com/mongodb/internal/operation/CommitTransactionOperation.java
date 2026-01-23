package cc.dreamcode.antylogout.libs.com.mongodb.internal.operation;

import java.util.Arrays;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.ConnectionDescription;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.ServerDescription;
import cc.dreamcode.antylogout.libs.com.mongodb.Function;
import cc.dreamcode.antylogout.libs.org.bson.BsonValue;
import cc.dreamcode.antylogout.libs.org.bson.BsonInt32;
import cc.dreamcode.antylogout.libs.org.bson.BsonInt64;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoWriteConcernException;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoExecutionTimeoutException;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoNodeIsRecoveringException;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoNotPrimaryException;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoTimeoutException;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoSocketException;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.SingleResultCallback;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.AsyncWriteBinding;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoException;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.WriteBinding;
import cc.dreamcode.antylogout.libs.com.mongodb.assertions.Assertions;
import java.util.concurrent.TimeUnit;
import cc.dreamcode.antylogout.libs.com.mongodb.lang.Nullable;
import cc.dreamcode.antylogout.libs.com.mongodb.WriteConcern;
import java.util.List;
import cc.dreamcode.antylogout.libs.org.bson.BsonDocument;

public class CommitTransactionOperation extends TransactionOperation
{
    private final boolean alreadyCommitted;
    private BsonDocument recoveryToken;
    private Long maxCommitTimeMS;
    private static final List<Integer> NON_RETRYABLE_WRITE_CONCERN_ERROR_CODES;
    
    public CommitTransactionOperation(final WriteConcern writeConcern) {
        this(writeConcern, false);
    }
    
    public CommitTransactionOperation(final WriteConcern writeConcern, final boolean alreadyCommitted) {
        super(writeConcern);
        this.alreadyCommitted = alreadyCommitted;
    }
    
    public CommitTransactionOperation recoveryToken(@Nullable final BsonDocument recoveryToken) {
        this.recoveryToken = recoveryToken;
        return this;
    }
    
    public CommitTransactionOperation maxCommitTime(@Nullable final Long maxCommitTime, final TimeUnit timeUnit) {
        if (maxCommitTime == null) {
            this.maxCommitTimeMS = null;
        }
        else {
            Assertions.notNull("timeUnit", timeUnit);
            Assertions.isTrueArgument("maxCommitTime > 0", maxCommitTime > 0L);
            this.maxCommitTimeMS = TimeUnit.MILLISECONDS.convert((long)maxCommitTime, timeUnit);
        }
        return this;
    }
    
    @Nullable
    public Long getMaxCommitTime(final TimeUnit timeUnit) {
        Assertions.notNull("timeUnit", timeUnit);
        if (this.maxCommitTimeMS == null) {
            return null;
        }
        return timeUnit.convert((long)this.maxCommitTimeMS, TimeUnit.MILLISECONDS);
    }
    
    @Override
    public Void execute(final WriteBinding binding) {
        try {
            return super.execute(binding);
        }
        catch (final MongoException e) {
            this.addErrorLabels(e);
            throw e;
        }
    }
    
    @Override
    public void executeAsync(final AsyncWriteBinding binding, final SingleResultCallback<Void> callback) {
        super.executeAsync(binding, (result, t) -> {
            if (t instanceof MongoException) {
                this.addErrorLabels((MongoException)t);
            }
            callback.onResult(result, t);
        });
    }
    
    private void addErrorLabels(final MongoException e) {
        if (shouldAddUnknownTransactionCommitResultLabel(e)) {
            e.addLabel("UnknownTransactionCommitResult");
        }
    }
    
    private static boolean shouldAddUnknownTransactionCommitResultLabel(final MongoException e) {
        return e instanceof MongoSocketException || e instanceof MongoTimeoutException || e instanceof MongoNotPrimaryException || e instanceof MongoNodeIsRecoveringException || e instanceof MongoExecutionTimeoutException || e.hasErrorLabel("RetryableWriteError") || (e instanceof MongoWriteConcernException && !CommitTransactionOperation.NON_RETRYABLE_WRITE_CONCERN_ERROR_CODES.contains((Object)e.getCode()));
    }
    
    @Override
    protected String getCommandName() {
        return "commitTransaction";
    }
    
    @Override
    CommandOperationHelper.CommandCreator getCommandCreator() {
        final CommandOperationHelper.CommandCreator creator = (serverDescription, connectionDescription) -> {
            final BsonDocument command = TransactionOperation.this.getCommandCreator().create(serverDescription, connectionDescription);
            if (this.maxCommitTimeMS != null) {
                if (this.maxCommitTimeMS > 2147483647L) {
                    new(cc.dreamcode.antylogout.libs.org.bson.BsonInt64.class)();
                    new BsonInt64(this.maxCommitTimeMS);
                }
                else {
                    new(cc.dreamcode.antylogout.libs.org.bson.BsonInt32.class)();
                    new BsonInt32(this.maxCommitTimeMS.intValue());
                }
                final BsonDocument bsonDocument;
                final String key;
                final BsonValue value;
                bsonDocument.append(key, value);
            }
            return command;
        };
        if (this.alreadyCommitted) {
            return (serverDescription, connectionDescription) -> this.getRetryCommandModifier().apply(creator.create(serverDescription, connectionDescription));
        }
        if (this.recoveryToken != null) {
            return (serverDescription, connectionDescription) -> creator.create(serverDescription, connectionDescription).append("recoveryToken", this.recoveryToken);
        }
        return creator;
    }
    
    @Override
    protected Function<BsonDocument, BsonDocument> getRetryCommandModifier() {
        return (Function<BsonDocument, BsonDocument>)(command -> {
            WriteConcern retryWriteConcern = this.getWriteConcern().withW("majority");
            if (retryWriteConcern.getWTimeout(TimeUnit.MILLISECONDS) == null) {
                retryWriteConcern = retryWriteConcern.withWTimeout(10000L, TimeUnit.MILLISECONDS);
            }
            command.put("writeConcern", retryWriteConcern.asDocument());
            if (this.recoveryToken != null) {
                command.put("recoveryToken", this.recoveryToken);
            }
            return command;
        });
    }
    
    static {
        NON_RETRYABLE_WRITE_CONCERN_ERROR_CODES = Arrays.asList((Object[])new Integer[] { 79, 100 });
    }
}
