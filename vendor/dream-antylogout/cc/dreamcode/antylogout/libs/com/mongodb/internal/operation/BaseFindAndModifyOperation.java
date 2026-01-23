package cc.dreamcode.antylogout.libs.com.mongodb.internal.operation;

import cc.dreamcode.antylogout.libs.com.mongodb.connection.ServerDescription;
import cc.dreamcode.antylogout.libs.org.bson.BsonInt64;
import cc.dreamcode.antylogout.libs.org.bson.BsonString;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.session.SessionContext;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.ConnectionDescription;
import cc.dreamcode.antylogout.libs.org.bson.FieldNameValidator;
import java.util.concurrent.TimeUnit;
import cc.dreamcode.antylogout.libs.com.mongodb.lang.Nullable;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.SingleResultCallback;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.AsyncWriteBinding;
import cc.dreamcode.antylogout.libs.com.mongodb.ReadPreference;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.WriteBinding;
import cc.dreamcode.antylogout.libs.com.mongodb.assertions.Assertions;
import cc.dreamcode.antylogout.libs.org.bson.BsonValue;
import cc.dreamcode.antylogout.libs.com.mongodb.client.model.Collation;
import cc.dreamcode.antylogout.libs.org.bson.BsonDocument;
import cc.dreamcode.antylogout.libs.org.bson.codecs.Decoder;
import cc.dreamcode.antylogout.libs.com.mongodb.WriteConcern;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoNamespace;

public abstract class BaseFindAndModifyOperation<T> implements AsyncWriteOperation<T>, WriteOperation<T>
{
    private final MongoNamespace namespace;
    private final WriteConcern writeConcern;
    private final boolean retryWrites;
    private final Decoder<T> decoder;
    private BsonDocument filter;
    private BsonDocument projection;
    private BsonDocument sort;
    private long maxTimeMS;
    private Collation collation;
    private BsonDocument hint;
    private String hintString;
    private BsonValue comment;
    private BsonDocument variables;
    
    protected BaseFindAndModifyOperation(final MongoNamespace namespace, final WriteConcern writeConcern, final boolean retryWrites, final Decoder<T> decoder) {
        this.namespace = Assertions.notNull("namespace", namespace);
        this.writeConcern = Assertions.notNull("writeConcern", writeConcern);
        this.retryWrites = retryWrites;
        this.decoder = Assertions.notNull("decoder", decoder);
    }
    
    @Override
    public T execute(final WriteBinding binding) {
        return SyncOperationHelper.executeRetryableWrite(binding, this.getDatabaseName(), (ReadPreference)null, this.getFieldNameValidator(), (Decoder<BsonDocument>)CommandResultDocumentCodec.create(this.getDecoder(), "value"), this.getCommandCreator(binding.getSessionContext()), FindAndModifyHelper.transformer(), cmd -> cmd);
    }
    
    @Override
    public void executeAsync(final AsyncWriteBinding binding, final SingleResultCallback<T> callback) {
        AsyncOperationHelper.executeRetryableWriteAsync(binding, this.getDatabaseName(), (ReadPreference)null, this.getFieldNameValidator(), (Decoder<BsonDocument>)CommandResultDocumentCodec.create(this.getDecoder(), "value"), this.getCommandCreator(binding.getSessionContext()), FindAndModifyHelper.asyncTransformer(), cmd -> cmd, callback);
    }
    
    public MongoNamespace getNamespace() {
        return this.namespace;
    }
    
    public WriteConcern getWriteConcern() {
        return this.writeConcern;
    }
    
    public Decoder<T> getDecoder() {
        return this.decoder;
    }
    
    public boolean isRetryWrites() {
        return this.retryWrites;
    }
    
    public BsonDocument getFilter() {
        return this.filter;
    }
    
    public BaseFindAndModifyOperation<T> filter(@Nullable final BsonDocument filter) {
        this.filter = filter;
        return this;
    }
    
    public BsonDocument getProjection() {
        return this.projection;
    }
    
    public BaseFindAndModifyOperation<T> projection(@Nullable final BsonDocument projection) {
        this.projection = projection;
        return this;
    }
    
    public long getMaxTime(final TimeUnit timeUnit) {
        Assertions.notNull("timeUnit", timeUnit);
        return timeUnit.convert(this.maxTimeMS, TimeUnit.MILLISECONDS);
    }
    
    public BaseFindAndModifyOperation<T> maxTime(final long maxTime, final TimeUnit timeUnit) {
        Assertions.notNull("timeUnit", timeUnit);
        this.maxTimeMS = TimeUnit.MILLISECONDS.convert(maxTime, timeUnit);
        return this;
    }
    
    public BsonDocument getSort() {
        return this.sort;
    }
    
    public BaseFindAndModifyOperation<T> sort(@Nullable final BsonDocument sort) {
        this.sort = sort;
        return this;
    }
    
    @Nullable
    public Collation getCollation() {
        return this.collation;
    }
    
    @Nullable
    public BsonDocument getHint() {
        return this.hint;
    }
    
    public BaseFindAndModifyOperation<T> hint(@Nullable final BsonDocument hint) {
        this.hint = hint;
        return this;
    }
    
    @Nullable
    public String getHintString() {
        return this.hintString;
    }
    
    public BaseFindAndModifyOperation<T> hintString(@Nullable final String hint) {
        this.hintString = hint;
        return this;
    }
    
    public BaseFindAndModifyOperation<T> collation(@Nullable final Collation collation) {
        this.collation = collation;
        return this;
    }
    
    public BsonValue getComment() {
        return this.comment;
    }
    
    public BaseFindAndModifyOperation<T> comment(@Nullable final BsonValue comment) {
        this.comment = comment;
        return this;
    }
    
    public BsonDocument getLet() {
        return this.variables;
    }
    
    public BaseFindAndModifyOperation<T> let(@Nullable final BsonDocument variables) {
        this.variables = variables;
        return this;
    }
    
    protected abstract FieldNameValidator getFieldNameValidator();
    
    protected abstract void specializeCommand(final BsonDocument p0, final ConnectionDescription p1);
    
    private CommandOperationHelper.CommandCreator getCommandCreator(final SessionContext sessionContext) {
        return (serverDescription, connectionDescription) -> {
            new BsonDocument("findAndModify", new BsonString(this.getNamespace().getCollectionName()));
            final BsonDocument bsonDocument;
            final BsonDocument commandDocument = bsonDocument;
            DocumentHelper.putIfNotNull(commandDocument, "query", this.getFilter());
            DocumentHelper.putIfNotNull(commandDocument, "fields", this.getProjection());
            DocumentHelper.putIfNotNull(commandDocument, "sort", this.getSort());
            this.specializeCommand(commandDocument, connectionDescription);
            DocumentHelper.putIfNotZero(commandDocument, "maxTimeMS", this.getMaxTime(TimeUnit.MILLISECONDS));
            if (this.getWriteConcern().isAcknowledged() && !this.getWriteConcern().isServerDefault() && !sessionContext.hasActiveTransaction()) {
                commandDocument.put("writeConcern", this.getWriteConcern().asDocument());
            }
            if (this.getCollation() != null) {
                commandDocument.put("collation", this.getCollation().asDocument());
            }
            if (this.getHint() != null || this.getHintString() != null) {
                OperationHelper.validateHintForFindAndModify(connectionDescription, this.getWriteConcern());
                if (this.getHint() != null) {
                    commandDocument.put("hint", this.getHint());
                }
                else {
                    commandDocument.put("hint", new BsonString(this.getHintString()));
                }
            }
            DocumentHelper.putIfNotNull(commandDocument, "comment", this.getComment());
            DocumentHelper.putIfNotNull(commandDocument, "let", this.getLet());
            if (OperationHelper.isRetryableWrite(this.isRetryWrites(), this.getWriteConcern(), connectionDescription, sessionContext)) {
                commandDocument.put("txnNumber", new BsonInt64(sessionContext.advanceTransactionNumber()));
            }
            return commandDocument;
        };
    }
    
    private String getDatabaseName() {
        return this.getNamespace().getDatabaseName();
    }
}
