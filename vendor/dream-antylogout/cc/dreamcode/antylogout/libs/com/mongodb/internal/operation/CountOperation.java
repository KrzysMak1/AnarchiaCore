package cc.dreamcode.antylogout.libs.com.mongodb.internal.operation;

import cc.dreamcode.antylogout.libs.org.bson.codecs.BsonDocumentCodec;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.Connection;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.ConnectionSource;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.AsyncConnection;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.AsyncConnectionSource;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.ServerDescription;
import cc.dreamcode.antylogout.libs.org.bson.BsonString;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.ConnectionDescription;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.session.SessionContext;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.SingleResultCallback;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.AsyncReadBinding;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.ReadBinding;
import cc.dreamcode.antylogout.libs.com.mongodb.lang.Nullable;
import java.util.concurrent.TimeUnit;
import cc.dreamcode.antylogout.libs.com.mongodb.assertions.Assertions;
import cc.dreamcode.antylogout.libs.com.mongodb.client.model.Collation;
import cc.dreamcode.antylogout.libs.org.bson.BsonValue;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoNamespace;
import cc.dreamcode.antylogout.libs.org.bson.BsonDocument;
import cc.dreamcode.antylogout.libs.org.bson.codecs.Decoder;

public class CountOperation implements AsyncReadOperation<Long>, ReadOperation<Long>
{
    private static final Decoder<BsonDocument> DECODER;
    private final MongoNamespace namespace;
    private boolean retryReads;
    private BsonDocument filter;
    private BsonValue hint;
    private long skip;
    private long limit;
    private long maxTimeMS;
    private Collation collation;
    
    public CountOperation(final MongoNamespace namespace) {
        this.namespace = Assertions.notNull("namespace", namespace);
    }
    
    public BsonDocument getFilter() {
        return this.filter;
    }
    
    public CountOperation filter(final BsonDocument filter) {
        this.filter = filter;
        return this;
    }
    
    public CountOperation retryReads(final boolean retryReads) {
        this.retryReads = retryReads;
        return this;
    }
    
    public boolean getRetryReads() {
        return this.retryReads;
    }
    
    public BsonValue getHint() {
        return this.hint;
    }
    
    public CountOperation hint(final BsonValue hint) {
        this.hint = hint;
        return this;
    }
    
    public long getLimit() {
        return this.limit;
    }
    
    public CountOperation limit(final long limit) {
        this.limit = limit;
        return this;
    }
    
    public long getSkip() {
        return this.skip;
    }
    
    public CountOperation skip(final long skip) {
        this.skip = skip;
        return this;
    }
    
    public long getMaxTime(final TimeUnit timeUnit) {
        Assertions.notNull("timeUnit", timeUnit);
        return timeUnit.convert(this.maxTimeMS, TimeUnit.MILLISECONDS);
    }
    
    public CountOperation maxTime(final long maxTime, final TimeUnit timeUnit) {
        Assertions.notNull("timeUnit", timeUnit);
        this.maxTimeMS = TimeUnit.MILLISECONDS.convert(maxTime, timeUnit);
        return this;
    }
    
    public Collation getCollation() {
        return this.collation;
    }
    
    public CountOperation collation(@Nullable final Collation collation) {
        this.collation = collation;
        return this;
    }
    
    @Override
    public Long execute(final ReadBinding binding) {
        return SyncOperationHelper.executeRetryableRead(binding, this.namespace.getDatabaseName(), this.getCommandCreator(binding.getSessionContext()), CountOperation.DECODER, this.transformer(), this.retryReads);
    }
    
    @Override
    public void executeAsync(final AsyncReadBinding binding, final SingleResultCallback<Long> callback) {
        AsyncOperationHelper.executeRetryableReadAsync(binding, this.namespace.getDatabaseName(), this.getCommandCreator(binding.getSessionContext()), CountOperation.DECODER, this.asyncTransformer(), this.retryReads, callback);
    }
    
    private SyncOperationHelper.CommandReadTransformer<BsonDocument, Long> transformer() {
        return (SyncOperationHelper.CommandReadTransformer<BsonDocument, Long>)((result, source, connection) -> result.getNumber("n").longValue());
    }
    
    private AsyncOperationHelper.CommandReadTransformerAsync<BsonDocument, Long> asyncTransformer() {
        return (AsyncOperationHelper.CommandReadTransformerAsync<BsonDocument, Long>)((result, source, connection) -> result.getNumber("n").longValue());
    }
    
    private CommandOperationHelper.CommandCreator getCommandCreator(final SessionContext sessionContext) {
        return (serverDescription, connectionDescription) -> this.getCommand(sessionContext, connectionDescription);
    }
    
    private BsonDocument getCommand(final SessionContext sessionContext, final ConnectionDescription connectionDescription) {
        final BsonDocument document = new BsonDocument("count", new BsonString(this.namespace.getCollectionName()));
        OperationReadConcernHelper.appendReadConcernToCommand(sessionContext, connectionDescription.getMaxWireVersion(), document);
        DocumentHelper.putIfNotNull(document, "query", this.filter);
        DocumentHelper.putIfNotZero(document, "limit", this.limit);
        DocumentHelper.putIfNotZero(document, "skip", this.skip);
        DocumentHelper.putIfNotNull(document, "hint", this.hint);
        DocumentHelper.putIfNotZero(document, "maxTimeMS", this.maxTimeMS);
        if (this.collation != null) {
            document.put("collation", this.collation.asDocument());
        }
        return document;
    }
    
    static {
        DECODER = new BsonDocumentCodec();
    }
}
