package cc.dreamcode.antylogout.libs.com.mongodb.internal.operation;

import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.Connection;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.AsyncConnection;
import cc.dreamcode.antylogout.libs.com.mongodb.DuplicateKeyException;
import cc.dreamcode.antylogout.libs.com.mongodb.WriteConcernResult;
import cc.dreamcode.antylogout.libs.com.mongodb.ErrorCategory;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoException;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoClientException;
import cc.dreamcode.antylogout.libs.org.bson.BsonArray;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.ConnectionDescription;
import cc.dreamcode.antylogout.libs.org.bson.BsonDouble;
import cc.dreamcode.antylogout.libs.org.bson.BsonInt32;
import cc.dreamcode.antylogout.libs.org.bson.BsonInt64;
import cc.dreamcode.antylogout.libs.org.bson.BsonBoolean;
import cc.dreamcode.antylogout.libs.org.bson.BsonString;
import cc.dreamcode.antylogout.libs.org.bson.BsonValue;
import cc.dreamcode.antylogout.libs.org.bson.BsonDocument;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.ErrorHandlingResultCallback;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.SingleResultCallback;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.AsyncWriteBinding;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoCommandException;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.WriteBinding;
import java.util.concurrent.TimeUnit;
import java.util.Iterator;
import java.util.ArrayList;
import cc.dreamcode.antylogout.libs.com.mongodb.assertions.Assertions;
import cc.dreamcode.antylogout.libs.com.mongodb.lang.Nullable;
import cc.dreamcode.antylogout.libs.com.mongodb.CreateIndexCommitQuorum;
import cc.dreamcode.antylogout.libs.com.mongodb.WriteConcern;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.bulk.IndexRequest;
import java.util.List;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoNamespace;

public class CreateIndexesOperation implements AsyncWriteOperation<Void>, WriteOperation<Void>
{
    private final MongoNamespace namespace;
    private final List<IndexRequest> requests;
    private final WriteConcern writeConcern;
    private long maxTimeMS;
    private CreateIndexCommitQuorum commitQuorum;
    
    public CreateIndexesOperation(final MongoNamespace namespace, final List<IndexRequest> requests) {
        this(namespace, requests, null);
    }
    
    public CreateIndexesOperation(final MongoNamespace namespace, final List<IndexRequest> requests, @Nullable final WriteConcern writeConcern) {
        this.namespace = Assertions.notNull("namespace", namespace);
        this.requests = Assertions.notNull("indexRequests", requests);
        this.writeConcern = writeConcern;
    }
    
    public WriteConcern getWriteConcern() {
        return this.writeConcern;
    }
    
    public List<IndexRequest> getRequests() {
        return this.requests;
    }
    
    public List<String> getIndexNames() {
        final List<String> indexNames = (List<String>)new ArrayList(this.requests.size());
        for (final IndexRequest request : this.requests) {
            if (request.getName() != null) {
                indexNames.add((Object)request.getName());
            }
            else {
                indexNames.add((Object)IndexHelper.generateIndexName(request.getKeys()));
            }
        }
        return indexNames;
    }
    
    public long getMaxTime(final TimeUnit timeUnit) {
        Assertions.notNull("timeUnit", timeUnit);
        return timeUnit.convert(this.maxTimeMS, TimeUnit.MILLISECONDS);
    }
    
    public CreateIndexesOperation maxTime(final long maxTime, final TimeUnit timeUnit) {
        Assertions.notNull("timeUnit", timeUnit);
        Assertions.isTrueArgument("maxTime >= 0", maxTime >= 0L);
        this.maxTimeMS = TimeUnit.MILLISECONDS.convert(maxTime, timeUnit);
        return this;
    }
    
    public CreateIndexCommitQuorum getCommitQuorum() {
        return this.commitQuorum;
    }
    
    public CreateIndexesOperation commitQuorum(@Nullable final CreateIndexCommitQuorum commitQuorum) {
        this.commitQuorum = commitQuorum;
        return this;
    }
    
    @Override
    public Void execute(final WriteBinding binding) {
        return SyncOperationHelper.withConnection(binding, connection -> {
            try {
                SyncOperationHelper.executeCommand(binding, this.namespace.getDatabaseName(), this.getCommand(connection.getDescription()), connection, SyncOperationHelper.writeConcernErrorTransformer());
            }
            catch (final MongoCommandException e) {
                throw this.checkForDuplicateKeyError(e);
            }
            return null;
        });
    }
    
    @Override
    public void executeAsync(final AsyncWriteBinding binding, final SingleResultCallback<Void> callback) {
        AsyncOperationHelper.withAsyncConnection(binding, (connection, t) -> {
            final SingleResultCallback<Void> errHandlingCallback = ErrorHandlingResultCallback.errorHandlingCallback((SingleResultCallback<Void>)callback, OperationHelper.LOGGER);
            if (t != null) {
                errHandlingCallback.onResult(null, t);
            }
            else {
                final SingleResultCallback<Void> wrappedCallback = AsyncOperationHelper.releasingCallback(errHandlingCallback, connection);
                try {
                    AsyncOperationHelper.executeCommandAsync(binding, this.namespace.getDatabaseName(), this.getCommand(connection.getDescription()), connection, AsyncOperationHelper.writeConcernErrorTransformerAsync(), (result, t12) -> wrappedCallback.onResult(null, (Throwable)this.translateException(t12)));
                }
                catch (final Throwable t2) {
                    wrappedCallback.onResult(null, t2);
                }
            }
        });
    }
    
    private BsonDocument getIndex(final IndexRequest request) {
        final BsonDocument index = new BsonDocument();
        index.append("key", request.getKeys());
        index.append("name", new BsonString((request.getName() != null) ? request.getName() : IndexHelper.generateIndexName(request.getKeys())));
        if (request.isBackground()) {
            index.append("background", BsonBoolean.TRUE);
        }
        if (request.isUnique()) {
            index.append("unique", BsonBoolean.TRUE);
        }
        if (request.isSparse()) {
            index.append("sparse", BsonBoolean.TRUE);
        }
        if (request.getExpireAfter(TimeUnit.SECONDS) != null) {
            index.append("expireAfterSeconds", new BsonInt64(Assertions.assertNotNull(request.getExpireAfter(TimeUnit.SECONDS))));
        }
        if (request.getVersion() != null) {
            index.append("v", new BsonInt32(Assertions.assertNotNull(request.getVersion())));
        }
        if (request.getWeights() != null) {
            index.append("weights", Assertions.assertNotNull(request.getWeights()));
        }
        if (request.getDefaultLanguage() != null) {
            index.append("default_language", new BsonString(Assertions.assertNotNull(request.getDefaultLanguage())));
        }
        if (request.getLanguageOverride() != null) {
            index.append("language_override", new BsonString(Assertions.assertNotNull(request.getLanguageOverride())));
        }
        if (request.getTextVersion() != null) {
            index.append("textIndexVersion", new BsonInt32(Assertions.assertNotNull(request.getTextVersion())));
        }
        if (request.getSphereVersion() != null) {
            index.append("2dsphereIndexVersion", new BsonInt32(Assertions.assertNotNull(request.getSphereVersion())));
        }
        if (request.getBits() != null) {
            index.append("bits", new BsonInt32(Assertions.assertNotNull(request.getBits())));
        }
        if (request.getMin() != null) {
            index.append("min", new BsonDouble(Assertions.assertNotNull(request.getMin())));
        }
        if (request.getMax() != null) {
            index.append("max", new BsonDouble(Assertions.assertNotNull(request.getMax())));
        }
        if (request.getDropDups()) {
            index.append("dropDups", BsonBoolean.TRUE);
        }
        if (request.getStorageEngine() != null) {
            index.append("storageEngine", Assertions.assertNotNull(request.getStorageEngine()));
        }
        if (request.getPartialFilterExpression() != null) {
            index.append("partialFilterExpression", Assertions.assertNotNull(request.getPartialFilterExpression()));
        }
        if (request.getCollation() != null) {
            index.append("collation", Assertions.assertNotNull(request.getCollation().asDocument()));
        }
        if (request.getWildcardProjection() != null) {
            index.append("wildcardProjection", Assertions.assertNotNull(request.getWildcardProjection()));
        }
        if (request.isHidden()) {
            index.append("hidden", BsonBoolean.TRUE);
        }
        return index;
    }
    
    private BsonDocument getCommand(final ConnectionDescription description) {
        final BsonDocument command = new BsonDocument("createIndexes", new BsonString(this.namespace.getCollectionName()));
        final List<BsonDocument> values = (List<BsonDocument>)new ArrayList();
        for (final IndexRequest request : this.requests) {
            values.add((Object)this.getIndex(request));
        }
        command.put("indexes", new BsonArray(values));
        DocumentHelper.putIfNotZero(command, "maxTimeMS", this.maxTimeMS);
        WriteConcernHelper.appendWriteConcernToCommand(this.writeConcern, command);
        if (this.commitQuorum != null) {
            if (!ServerVersionHelper.serverIsAtLeastVersionFourDotFour(description)) {
                throw new MongoClientException("Specifying a value for the create index commit quorum option requires a minimum MongoDB version of 4.4");
            }
            command.put("commitQuorum", this.commitQuorum.toBsonValue());
        }
        return command;
    }
    
    @Nullable
    private MongoException translateException(@Nullable final Throwable t) {
        return (t instanceof MongoCommandException) ? this.checkForDuplicateKeyError((MongoCommandException)t) : MongoException.fromThrowable(t);
    }
    
    private MongoException checkForDuplicateKeyError(final MongoCommandException e) {
        if (ErrorCategory.fromErrorCode(e.getCode()) == ErrorCategory.DUPLICATE_KEY) {
            return new DuplicateKeyException(e.getResponse(), e.getServerAddress(), WriteConcernResult.acknowledged(0, false, null));
        }
        return e;
    }
}
