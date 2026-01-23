package cc.dreamcode.antylogout.libs.com.mongodb.internal.operation;

import cc.dreamcode.antylogout.libs.org.bson.codecs.BsonDocumentCodec;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.Connection;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.ConnectionSource;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.AsyncConnection;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.AsyncConnectionSource;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.ServerDescription;
import cc.dreamcode.antylogout.libs.org.bson.BsonString;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.session.SessionContext;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.ConnectionDescription;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.SingleResultCallback;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.AsyncReadBinding;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoCommandException;
import java.util.List;
import java.util.Collections;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.ReadBinding;
import cc.dreamcode.antylogout.libs.com.mongodb.lang.Nullable;
import java.util.concurrent.TimeUnit;
import cc.dreamcode.antylogout.libs.com.mongodb.assertions.Assertions;
import cc.dreamcode.antylogout.libs.org.bson.BsonValue;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoNamespace;
import cc.dreamcode.antylogout.libs.org.bson.BsonDocument;
import cc.dreamcode.antylogout.libs.org.bson.codecs.Decoder;

public class EstimatedDocumentCountOperation implements AsyncReadOperation<Long>, ReadOperation<Long>
{
    private static final Decoder<BsonDocument> DECODER;
    private final MongoNamespace namespace;
    private boolean retryReads;
    private long maxTimeMS;
    private BsonValue comment;
    
    public EstimatedDocumentCountOperation(final MongoNamespace namespace) {
        this.namespace = Assertions.notNull("namespace", namespace);
    }
    
    public EstimatedDocumentCountOperation retryReads(final boolean retryReads) {
        this.retryReads = retryReads;
        return this;
    }
    
    public EstimatedDocumentCountOperation maxTime(final long maxTime, final TimeUnit timeUnit) {
        Assertions.notNull("timeUnit", timeUnit);
        this.maxTimeMS = TimeUnit.MILLISECONDS.convert(maxTime, timeUnit);
        return this;
    }
    
    @Nullable
    public BsonValue getComment() {
        return this.comment;
    }
    
    public EstimatedDocumentCountOperation comment(@Nullable final BsonValue comment) {
        this.comment = comment;
        return this;
    }
    
    @Override
    public Long execute(final ReadBinding binding) {
        try {
            return SyncOperationHelper.executeRetryableRead(binding, this.namespace.getDatabaseName(), this.getCommandCreator(binding.getSessionContext()), CommandResultDocumentCodec.create(EstimatedDocumentCountOperation.DECODER, (List<String>)Collections.singletonList((Object)"firstBatch")), this.transformer(), this.retryReads);
        }
        catch (final MongoCommandException e) {
            return Assertions.assertNotNull((Long)CommandOperationHelper.rethrowIfNotNamespaceError(e, (T)0L));
        }
    }
    
    @Override
    public void executeAsync(final AsyncReadBinding binding, final SingleResultCallback<Long> callback) {
        AsyncOperationHelper.executeRetryableReadAsync(binding, this.namespace.getDatabaseName(), this.getCommandCreator(binding.getSessionContext()), CommandResultDocumentCodec.create(EstimatedDocumentCountOperation.DECODER, (List<String>)Collections.singletonList((Object)"firstBatch")), this.asyncTransformer(), this.retryReads, (result, t) -> {
            if (CommandOperationHelper.isNamespaceError(t)) {
                callback.onResult(0L, null);
            }
            else {
                callback.onResult(result, t);
            }
        });
    }
    
    private SyncOperationHelper.CommandReadTransformer<BsonDocument, Long> transformer() {
        return (SyncOperationHelper.CommandReadTransformer<BsonDocument, Long>)((result, source, connection) -> this.transformResult(result, connection.getDescription()));
    }
    
    private AsyncOperationHelper.CommandReadTransformerAsync<BsonDocument, Long> asyncTransformer() {
        return (AsyncOperationHelper.CommandReadTransformerAsync<BsonDocument, Long>)((result, source, connection) -> this.transformResult(result, connection.getDescription()));
    }
    
    private long transformResult(final BsonDocument result, final ConnectionDescription connectionDescription) {
        return result.getNumber("n").longValue();
    }
    
    private CommandOperationHelper.CommandCreator getCommandCreator(final SessionContext sessionContext) {
        return (serverDescription, connectionDescription) -> {
            new BsonDocument("count", new BsonString(this.namespace.getCollectionName()));
            final BsonDocument bsonDocument;
            final BsonDocument document = bsonDocument;
            OperationReadConcernHelper.appendReadConcernToCommand(sessionContext, connectionDescription.getMaxWireVersion(), document);
            DocumentHelper.putIfNotZero(document, "maxTimeMS", this.maxTimeMS);
            if (this.comment != null) {
                document.put("comment", this.comment);
            }
            return document;
        };
    }
    
    static {
        DECODER = new BsonDocumentCodec();
    }
}
