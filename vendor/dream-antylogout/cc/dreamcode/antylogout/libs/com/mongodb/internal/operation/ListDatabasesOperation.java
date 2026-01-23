package cc.dreamcode.antylogout.libs.com.mongodb.internal.operation;

import cc.dreamcode.antylogout.libs.com.mongodb.connection.ConnectionDescription;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.ServerDescription;
import cc.dreamcode.antylogout.libs.org.bson.BsonBoolean;
import cc.dreamcode.antylogout.libs.org.bson.BsonInt64;
import cc.dreamcode.antylogout.libs.org.bson.BsonInt32;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.ErrorHandlingResultCallback;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.SingleResultCallback;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.AsyncReadBinding;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.ReadBinding;
import cc.dreamcode.antylogout.libs.com.mongodb.lang.Nullable;
import java.util.concurrent.TimeUnit;
import cc.dreamcode.antylogout.libs.com.mongodb.assertions.Assertions;
import cc.dreamcode.antylogout.libs.org.bson.BsonValue;
import cc.dreamcode.antylogout.libs.org.bson.BsonDocument;
import cc.dreamcode.antylogout.libs.org.bson.codecs.Decoder;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.AsyncBatchCursor;

public class ListDatabasesOperation<T> implements AsyncReadOperation<AsyncBatchCursor<T>>, ReadOperation<BatchCursor<T>>
{
    private static final String DATABASES = "databases";
    private final Decoder<T> decoder;
    private boolean retryReads;
    private long maxTimeMS;
    private BsonDocument filter;
    private Boolean nameOnly;
    private Boolean authorizedDatabasesOnly;
    private BsonValue comment;
    
    public ListDatabasesOperation(final Decoder<T> decoder) {
        this.decoder = Assertions.notNull("decoder", decoder);
    }
    
    public long getMaxTime(final TimeUnit timeUnit) {
        Assertions.notNull("timeUnit", timeUnit);
        return timeUnit.convert(this.maxTimeMS, TimeUnit.MILLISECONDS);
    }
    
    public ListDatabasesOperation<T> maxTime(final long maxTime, final TimeUnit timeUnit) {
        Assertions.notNull("timeUnit", timeUnit);
        this.maxTimeMS = TimeUnit.MILLISECONDS.convert(maxTime, timeUnit);
        return this;
    }
    
    public ListDatabasesOperation<T> filter(@Nullable final BsonDocument filter) {
        this.filter = filter;
        return this;
    }
    
    public BsonDocument getFilter() {
        return this.filter;
    }
    
    public ListDatabasesOperation<T> nameOnly(final Boolean nameOnly) {
        this.nameOnly = nameOnly;
        return this;
    }
    
    public ListDatabasesOperation<T> authorizedDatabasesOnly(final Boolean authorizedDatabasesOnly) {
        this.authorizedDatabasesOnly = authorizedDatabasesOnly;
        return this;
    }
    
    public ListDatabasesOperation<T> retryReads(final boolean retryReads) {
        this.retryReads = retryReads;
        return this;
    }
    
    public boolean getRetryReads() {
        return this.retryReads;
    }
    
    public Boolean getNameOnly() {
        return this.nameOnly;
    }
    
    public Boolean getAuthorizedDatabasesOnly() {
        return this.authorizedDatabasesOnly;
    }
    
    @Nullable
    public BsonValue getComment() {
        return this.comment;
    }
    
    public ListDatabasesOperation<T> comment(@Nullable final BsonValue comment) {
        this.comment = comment;
        return this;
    }
    
    @Override
    public BatchCursor<T> execute(final ReadBinding binding) {
        return SyncOperationHelper.executeRetryableRead(binding, "admin", this.getCommandCreator(), CommandResultDocumentCodec.create(this.decoder, "databases"), SyncOperationHelper.singleBatchCursorTransformer("databases"), this.retryReads);
    }
    
    @Override
    public void executeAsync(final AsyncReadBinding binding, final SingleResultCallback<AsyncBatchCursor<T>> callback) {
        AsyncOperationHelper.executeRetryableReadAsync(binding, "admin", this.getCommandCreator(), CommandResultDocumentCodec.create(this.decoder, "databases"), AsyncOperationHelper.asyncSingleBatchCursorTransformer("databases"), this.retryReads, (SingleResultCallback<AsyncBatchCursor<Object>>)ErrorHandlingResultCallback.errorHandlingCallback((SingleResultCallback<T>)callback, OperationHelper.LOGGER));
    }
    
    private CommandOperationHelper.CommandCreator getCommandCreator() {
        return (serverDescription, connectionDescription) -> this.getCommand();
    }
    
    private BsonDocument getCommand() {
        final BsonDocument command = new BsonDocument("listDatabases", new BsonInt32(1));
        if (this.maxTimeMS > 0L) {
            command.put("maxTimeMS", new BsonInt64(this.maxTimeMS));
        }
        if (this.filter != null) {
            command.put("filter", this.filter);
        }
        if (this.nameOnly != null) {
            command.put("nameOnly", new BsonBoolean(this.nameOnly));
        }
        if (this.authorizedDatabasesOnly != null) {
            command.put("authorizedDatabases", new BsonBoolean(this.authorizedDatabasesOnly));
        }
        DocumentHelper.putIfNotNull(command, "comment", this.comment);
        return command;
    }
}
