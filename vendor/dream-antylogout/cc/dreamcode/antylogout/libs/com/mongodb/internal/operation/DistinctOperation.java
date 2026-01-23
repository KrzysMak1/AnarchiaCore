package cc.dreamcode.antylogout.libs.com.mongodb.internal.operation;

import cc.dreamcode.antylogout.libs.com.mongodb.connection.ServerDescription;
import cc.dreamcode.antylogout.libs.org.bson.BsonString;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.ConnectionDescription;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.session.SessionContext;
import cc.dreamcode.antylogout.libs.org.bson.codecs.Codec;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.ErrorHandlingResultCallback;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.SingleResultCallback;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.AsyncReadBinding;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.ReadBinding;
import java.util.concurrent.TimeUnit;
import cc.dreamcode.antylogout.libs.com.mongodb.lang.Nullable;
import cc.dreamcode.antylogout.libs.com.mongodb.assertions.Assertions;
import cc.dreamcode.antylogout.libs.org.bson.BsonValue;
import cc.dreamcode.antylogout.libs.com.mongodb.client.model.Collation;
import cc.dreamcode.antylogout.libs.org.bson.BsonDocument;
import cc.dreamcode.antylogout.libs.org.bson.codecs.Decoder;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoNamespace;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.AsyncBatchCursor;

public class DistinctOperation<T> implements AsyncReadOperation<AsyncBatchCursor<T>>, ReadOperation<BatchCursor<T>>
{
    private static final String VALUES = "values";
    private final MongoNamespace namespace;
    private final String fieldName;
    private final Decoder<T> decoder;
    private boolean retryReads;
    private BsonDocument filter;
    private long maxTimeMS;
    private Collation collation;
    private BsonValue comment;
    
    public DistinctOperation(final MongoNamespace namespace, final String fieldName, final Decoder<T> decoder) {
        this.namespace = Assertions.notNull("namespace", namespace);
        this.fieldName = Assertions.notNull("fieldName", fieldName);
        this.decoder = Assertions.notNull("decoder", decoder);
    }
    
    public BsonDocument getFilter() {
        return this.filter;
    }
    
    public DistinctOperation<T> filter(@Nullable final BsonDocument filter) {
        this.filter = filter;
        return this;
    }
    
    public DistinctOperation<T> retryReads(final boolean retryReads) {
        this.retryReads = retryReads;
        return this;
    }
    
    public boolean getRetryReads() {
        return this.retryReads;
    }
    
    public long getMaxTime(final TimeUnit timeUnit) {
        Assertions.notNull("timeUnit", timeUnit);
        return timeUnit.convert(this.maxTimeMS, TimeUnit.MILLISECONDS);
    }
    
    public DistinctOperation<T> maxTime(final long maxTime, final TimeUnit timeUnit) {
        Assertions.notNull("timeUnit", timeUnit);
        this.maxTimeMS = TimeUnit.MILLISECONDS.convert(maxTime, timeUnit);
        return this;
    }
    
    public Collation getCollation() {
        return this.collation;
    }
    
    public DistinctOperation<T> collation(@Nullable final Collation collation) {
        this.collation = collation;
        return this;
    }
    
    public BsonValue getComment() {
        return this.comment;
    }
    
    public DistinctOperation<T> comment(final BsonValue comment) {
        this.comment = comment;
        return this;
    }
    
    @Override
    public BatchCursor<T> execute(final ReadBinding binding) {
        return SyncOperationHelper.executeRetryableRead(binding, this.namespace.getDatabaseName(), this.getCommandCreator(binding.getSessionContext()), this.createCommandDecoder(), SyncOperationHelper.singleBatchCursorTransformer("values"), this.retryReads);
    }
    
    @Override
    public void executeAsync(final AsyncReadBinding binding, final SingleResultCallback<AsyncBatchCursor<T>> callback) {
        AsyncOperationHelper.executeRetryableReadAsync(binding, this.namespace.getDatabaseName(), this.getCommandCreator(binding.getSessionContext()), this.createCommandDecoder(), AsyncOperationHelper.asyncSingleBatchCursorTransformer("values"), this.retryReads, (SingleResultCallback<AsyncBatchCursor<Object>>)ErrorHandlingResultCallback.errorHandlingCallback((SingleResultCallback<T>)callback, OperationHelper.LOGGER));
    }
    
    private Codec<BsonDocument> createCommandDecoder() {
        return CommandResultDocumentCodec.create(this.decoder, "values");
    }
    
    private CommandOperationHelper.CommandCreator getCommandCreator(final SessionContext sessionContext) {
        return (serverDescription, connectionDescription) -> this.getCommand(sessionContext, connectionDescription);
    }
    
    private BsonDocument getCommand(final SessionContext sessionContext, final ConnectionDescription connectionDescription) {
        final BsonDocument commandDocument = new BsonDocument("distinct", new BsonString(this.namespace.getCollectionName()));
        OperationReadConcernHelper.appendReadConcernToCommand(sessionContext, connectionDescription.getMaxWireVersion(), commandDocument);
        commandDocument.put("key", new BsonString(this.fieldName));
        DocumentHelper.putIfNotNull(commandDocument, "query", this.filter);
        DocumentHelper.putIfNotZero(commandDocument, "maxTimeMS", this.maxTimeMS);
        if (this.collation != null) {
            commandDocument.put("collation", this.collation.asDocument());
        }
        DocumentHelper.putIfNotNull(commandDocument, "comment", this.comment);
        return commandDocument;
    }
}
