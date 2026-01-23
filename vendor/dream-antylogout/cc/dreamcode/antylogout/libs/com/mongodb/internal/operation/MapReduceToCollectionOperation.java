package cc.dreamcode.antylogout.libs.com.mongodb.internal.operation;

import java.util.Arrays;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.Connection;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.AsyncConnection;
import cc.dreamcode.antylogout.libs.org.bson.BsonBoolean;
import cc.dreamcode.antylogout.libs.org.bson.BsonValue;
import cc.dreamcode.antylogout.libs.org.bson.BsonString;
import cc.dreamcode.antylogout.libs.org.bson.codecs.Decoder;
import cc.dreamcode.antylogout.libs.org.bson.codecs.BsonDocumentCodec;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.ConnectionDescription;
import cc.dreamcode.antylogout.libs.com.mongodb.ExplainVerbosity;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.ErrorHandlingResultCallback;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.SingleResultCallback;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.AsyncWriteBinding;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.WriteBinding;
import java.util.concurrent.TimeUnit;
import cc.dreamcode.antylogout.libs.com.mongodb.assertions.Assertions;
import cc.dreamcode.antylogout.libs.com.mongodb.lang.Nullable;
import java.util.List;
import cc.dreamcode.antylogout.libs.com.mongodb.client.model.Collation;
import cc.dreamcode.antylogout.libs.org.bson.BsonDocument;
import cc.dreamcode.antylogout.libs.com.mongodb.WriteConcern;
import cc.dreamcode.antylogout.libs.org.bson.BsonJavaScript;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoNamespace;

public class MapReduceToCollectionOperation implements AsyncWriteOperation<MapReduceStatistics>, WriteOperation<MapReduceStatistics>
{
    private final MongoNamespace namespace;
    private final BsonJavaScript mapFunction;
    private final BsonJavaScript reduceFunction;
    private final String collectionName;
    private final WriteConcern writeConcern;
    private BsonJavaScript finalizeFunction;
    private BsonDocument scope;
    private BsonDocument filter;
    private BsonDocument sort;
    private int limit;
    private boolean jsMode;
    private boolean verbose;
    private long maxTimeMS;
    private String action;
    private String databaseName;
    private Boolean bypassDocumentValidation;
    private Collation collation;
    private static final List<String> VALID_ACTIONS;
    
    public MapReduceToCollectionOperation(final MongoNamespace namespace, final BsonJavaScript mapFunction, final BsonJavaScript reduceFunction, final String collectionName) {
        this(namespace, mapFunction, reduceFunction, collectionName, null);
    }
    
    public MapReduceToCollectionOperation(final MongoNamespace namespace, final BsonJavaScript mapFunction, final BsonJavaScript reduceFunction, @Nullable final String collectionName, @Nullable final WriteConcern writeConcern) {
        this.action = "replace";
        this.namespace = Assertions.notNull("namespace", namespace);
        this.mapFunction = Assertions.notNull("mapFunction", mapFunction);
        this.reduceFunction = Assertions.notNull("reduceFunction", reduceFunction);
        this.collectionName = Assertions.notNull("collectionName", collectionName);
        this.writeConcern = writeConcern;
    }
    
    public MongoNamespace getNamespace() {
        return this.namespace;
    }
    
    public BsonJavaScript getMapFunction() {
        return this.mapFunction;
    }
    
    public BsonJavaScript getReduceFunction() {
        return this.reduceFunction;
    }
    
    public String getCollectionName() {
        return this.collectionName;
    }
    
    public WriteConcern getWriteConcern() {
        return this.writeConcern;
    }
    
    public BsonJavaScript getFinalizeFunction() {
        return this.finalizeFunction;
    }
    
    public MapReduceToCollectionOperation finalizeFunction(final BsonJavaScript finalizeFunction) {
        this.finalizeFunction = finalizeFunction;
        return this;
    }
    
    public BsonDocument getScope() {
        return this.scope;
    }
    
    public MapReduceToCollectionOperation scope(@Nullable final BsonDocument scope) {
        this.scope = scope;
        return this;
    }
    
    public BsonDocument getFilter() {
        return this.filter;
    }
    
    public MapReduceToCollectionOperation filter(@Nullable final BsonDocument filter) {
        this.filter = filter;
        return this;
    }
    
    public BsonDocument getSort() {
        return this.sort;
    }
    
    public MapReduceToCollectionOperation sort(@Nullable final BsonDocument sort) {
        this.sort = sort;
        return this;
    }
    
    public int getLimit() {
        return this.limit;
    }
    
    public MapReduceToCollectionOperation limit(final int limit) {
        this.limit = limit;
        return this;
    }
    
    public boolean isJsMode() {
        return this.jsMode;
    }
    
    public MapReduceToCollectionOperation jsMode(final boolean jsMode) {
        this.jsMode = jsMode;
        return this;
    }
    
    public boolean isVerbose() {
        return this.verbose;
    }
    
    public MapReduceToCollectionOperation verbose(final boolean verbose) {
        this.verbose = verbose;
        return this;
    }
    
    public long getMaxTime(final TimeUnit timeUnit) {
        Assertions.notNull("timeUnit", timeUnit);
        return timeUnit.convert(this.maxTimeMS, TimeUnit.MILLISECONDS);
    }
    
    public MapReduceToCollectionOperation maxTime(final long maxTime, final TimeUnit timeUnit) {
        Assertions.notNull("timeUnit", timeUnit);
        this.maxTimeMS = TimeUnit.MILLISECONDS.convert(maxTime, timeUnit);
        return this;
    }
    
    public String getAction() {
        return this.action;
    }
    
    public MapReduceToCollectionOperation action(final String action) {
        Assertions.notNull("action", action);
        Assertions.isTrue("action must be one of: \"replace\", \"merge\", \"reduce\"", MapReduceToCollectionOperation.VALID_ACTIONS.contains((Object)action));
        this.action = action;
        return this;
    }
    
    @Nullable
    public String getDatabaseName() {
        return this.databaseName;
    }
    
    public MapReduceToCollectionOperation databaseName(@Nullable final String databaseName) {
        this.databaseName = databaseName;
        return this;
    }
    
    public Boolean getBypassDocumentValidation() {
        return this.bypassDocumentValidation;
    }
    
    public MapReduceToCollectionOperation bypassDocumentValidation(@Nullable final Boolean bypassDocumentValidation) {
        this.bypassDocumentValidation = bypassDocumentValidation;
        return this;
    }
    
    public Collation getCollation() {
        return this.collation;
    }
    
    public MapReduceToCollectionOperation collation(@Nullable final Collation collation) {
        this.collation = collation;
        return this;
    }
    
    @Override
    public MapReduceStatistics execute(final WriteBinding binding) {
        return SyncOperationHelper.withConnection(binding, connection -> Assertions.assertNotNull((MapReduceStatistics)SyncOperationHelper.executeCommand(binding, this.namespace.getDatabaseName(), this.getCommand(connection.getDescription()), connection, (SyncOperationHelper.CommandWriteTransformer<BsonDocument, T>)this.transformer())));
    }
    
    @Override
    public void executeAsync(final AsyncWriteBinding binding, final SingleResultCallback<MapReduceStatistics> callback) {
        AsyncOperationHelper.withAsyncConnection(binding, (connection, t) -> {
            final SingleResultCallback<MapReduceStatistics> errHandlingCallback = ErrorHandlingResultCallback.errorHandlingCallback((SingleResultCallback<MapReduceStatistics>)callback, OperationHelper.LOGGER);
            if (t != null) {
                errHandlingCallback.onResult(null, t);
            }
            else {
                AsyncOperationHelper.executeCommandAsync(binding, this.namespace.getDatabaseName(), this.getCommand(connection.getDescription()), connection, this.transformerAsync(), (SingleResultCallback<MapReduceStatistics>)AsyncOperationHelper.releasingCallback((SingleResultCallback<T>)errHandlingCallback, connection));
            }
        });
    }
    
    public ReadOperation<BsonDocument> asExplainableOperation(final ExplainVerbosity explainVerbosity) {
        return this.createExplainableOperation(explainVerbosity);
    }
    
    public AsyncReadOperation<BsonDocument> asExplainableOperationAsync(final ExplainVerbosity explainVerbosity) {
        return this.createExplainableOperation(explainVerbosity);
    }
    
    private CommandReadOperation<BsonDocument> createExplainableOperation(final ExplainVerbosity explainVerbosity) {
        return new CommandReadOperation<BsonDocument>(this.namespace.getDatabaseName(), ExplainHelper.asExplainCommand(this.getCommand(null), explainVerbosity), new BsonDocumentCodec());
    }
    
    private SyncOperationHelper.CommandWriteTransformer<BsonDocument, MapReduceStatistics> transformer() {
        return (SyncOperationHelper.CommandWriteTransformer<BsonDocument, MapReduceStatistics>)((result, connection) -> {
            WriteConcernHelper.throwOnWriteConcernError(result, connection.getDescription().getServerAddress(), connection.getDescription().getMaxWireVersion());
            return MapReduceHelper.createStatistics(result);
        });
    }
    
    private AsyncOperationHelper.CommandWriteTransformerAsync<BsonDocument, MapReduceStatistics> transformerAsync() {
        return (AsyncOperationHelper.CommandWriteTransformerAsync<BsonDocument, MapReduceStatistics>)((result, connection) -> {
            WriteConcernHelper.throwOnWriteConcernError(result, connection.getDescription().getServerAddress(), connection.getDescription().getMaxWireVersion());
            return MapReduceHelper.createStatistics(result);
        });
    }
    
    private BsonDocument getCommand(@Nullable final ConnectionDescription description) {
        final BsonDocument outputDocument = new BsonDocument(this.getAction(), new BsonString(this.getCollectionName()));
        if (this.getDatabaseName() != null) {
            outputDocument.put("db", new BsonString(this.getDatabaseName()));
        }
        final BsonDocument commandDocument = new BsonDocument("mapReduce", new BsonString(this.namespace.getCollectionName())).append("map", this.getMapFunction()).append("reduce", this.getReduceFunction()).append("out", outputDocument);
        DocumentHelper.putIfNotNull(commandDocument, "query", this.getFilter());
        DocumentHelper.putIfNotNull(commandDocument, "sort", this.getSort());
        DocumentHelper.putIfNotNull(commandDocument, "finalize", this.getFinalizeFunction());
        DocumentHelper.putIfNotNull(commandDocument, "scope", this.getScope());
        DocumentHelper.putIfTrue(commandDocument, "verbose", this.isVerbose());
        DocumentHelper.putIfNotZero(commandDocument, "limit", this.getLimit());
        DocumentHelper.putIfNotZero(commandDocument, "maxTimeMS", this.getMaxTime(TimeUnit.MILLISECONDS));
        DocumentHelper.putIfTrue(commandDocument, "jsMode", this.isJsMode());
        if (this.bypassDocumentValidation != null && description != null) {
            commandDocument.put("bypassDocumentValidation", BsonBoolean.valueOf(this.bypassDocumentValidation));
        }
        if (description != null) {
            WriteConcernHelper.appendWriteConcernToCommand(this.writeConcern, commandDocument);
        }
        if (this.collation != null) {
            commandDocument.put("collation", this.collation.asDocument());
        }
        return commandDocument;
    }
    
    static {
        VALID_ACTIONS = Arrays.asList((Object[])new String[] { "replace", "merge", "reduce" });
    }
}
