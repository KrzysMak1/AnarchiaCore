package cc.dreamcode.antylogout.libs.com.mongodb.internal.operation;

import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.Connection;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.ConnectionDescription;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.ServerDescription;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.AsyncConnection;
import cc.dreamcode.antylogout.libs.org.bson.BsonBoolean;
import cc.dreamcode.antylogout.libs.org.bson.BsonInt64;
import cc.dreamcode.antylogout.libs.org.bson.BsonArray;
import cc.dreamcode.antylogout.libs.org.bson.BsonString;
import cc.dreamcode.antylogout.libs.org.bson.BsonInt32;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.AsyncConnectionSource;
import cc.dreamcode.antylogout.libs.com.mongodb.ReadPreference;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.SingleResultCallback;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.AsyncReadBinding;
import cc.dreamcode.antylogout.libs.org.bson.codecs.Decoder;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.ConnectionSource;
import java.util.function.Supplier;
import cc.dreamcode.antylogout.libs.org.bson.codecs.BsonDocumentCodec;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.ReadBinding;
import java.util.concurrent.TimeUnit;
import cc.dreamcode.antylogout.libs.com.mongodb.assertions.Assertions;
import cc.dreamcode.antylogout.libs.com.mongodb.lang.Nullable;
import cc.dreamcode.antylogout.libs.org.bson.BsonValue;
import cc.dreamcode.antylogout.libs.com.mongodb.client.model.Collation;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.client.model.AggregationLevel;
import cc.dreamcode.antylogout.libs.com.mongodb.ReadConcern;
import cc.dreamcode.antylogout.libs.com.mongodb.WriteConcern;
import cc.dreamcode.antylogout.libs.org.bson.BsonDocument;
import java.util.List;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoNamespace;

public class AggregateToCollectionOperation implements AsyncReadOperation<Void>, ReadOperation<Void>
{
    private final MongoNamespace namespace;
    private final List<BsonDocument> pipeline;
    private final WriteConcern writeConcern;
    private final ReadConcern readConcern;
    private final AggregationLevel aggregationLevel;
    private Boolean allowDiskUse;
    private long maxTimeMS;
    private Boolean bypassDocumentValidation;
    private Collation collation;
    private BsonValue comment;
    private BsonValue hint;
    private BsonDocument variables;
    
    public AggregateToCollectionOperation(final MongoNamespace namespace, final List<BsonDocument> pipeline) {
        this(namespace, pipeline, null, null, AggregationLevel.COLLECTION);
    }
    
    public AggregateToCollectionOperation(final MongoNamespace namespace, final List<BsonDocument> pipeline, final WriteConcern writeConcern) {
        this(namespace, pipeline, null, writeConcern, AggregationLevel.COLLECTION);
    }
    
    public AggregateToCollectionOperation(final MongoNamespace namespace, final List<BsonDocument> pipeline, final ReadConcern readConcern) {
        this(namespace, pipeline, readConcern, null, AggregationLevel.COLLECTION);
    }
    
    public AggregateToCollectionOperation(final MongoNamespace namespace, final List<BsonDocument> pipeline, final ReadConcern readConcern, final WriteConcern writeConcern) {
        this(namespace, pipeline, readConcern, writeConcern, AggregationLevel.COLLECTION);
    }
    
    public AggregateToCollectionOperation(final MongoNamespace namespace, final List<BsonDocument> pipeline, @Nullable final ReadConcern readConcern, @Nullable final WriteConcern writeConcern, final AggregationLevel aggregationLevel) {
        this.namespace = Assertions.notNull("namespace", namespace);
        this.pipeline = Assertions.notNull("pipeline", pipeline);
        this.writeConcern = writeConcern;
        this.readConcern = readConcern;
        this.aggregationLevel = Assertions.notNull("aggregationLevel", aggregationLevel);
        Assertions.isTrueArgument("pipeline is not empty", !pipeline.isEmpty());
    }
    
    public List<BsonDocument> getPipeline() {
        return this.pipeline;
    }
    
    public ReadConcern getReadConcern() {
        return this.readConcern;
    }
    
    public WriteConcern getWriteConcern() {
        return this.writeConcern;
    }
    
    public Boolean getAllowDiskUse() {
        return this.allowDiskUse;
    }
    
    public AggregateToCollectionOperation allowDiskUse(@Nullable final Boolean allowDiskUse) {
        this.allowDiskUse = allowDiskUse;
        return this;
    }
    
    public long getMaxTime(final TimeUnit timeUnit) {
        Assertions.notNull("timeUnit", timeUnit);
        return timeUnit.convert(this.maxTimeMS, TimeUnit.MILLISECONDS);
    }
    
    public AggregateToCollectionOperation maxTime(final long maxTime, final TimeUnit timeUnit) {
        Assertions.notNull("timeUnit", timeUnit);
        this.maxTimeMS = TimeUnit.MILLISECONDS.convert(maxTime, timeUnit);
        return this;
    }
    
    public Boolean getBypassDocumentValidation() {
        return this.bypassDocumentValidation;
    }
    
    public AggregateToCollectionOperation bypassDocumentValidation(@Nullable final Boolean bypassDocumentValidation) {
        this.bypassDocumentValidation = bypassDocumentValidation;
        return this;
    }
    
    public Collation getCollation() {
        return this.collation;
    }
    
    public AggregateToCollectionOperation collation(@Nullable final Collation collation) {
        this.collation = collation;
        return this;
    }
    
    public BsonValue getComment() {
        return this.comment;
    }
    
    public AggregateToCollectionOperation let(@Nullable final BsonDocument variables) {
        this.variables = variables;
        return this;
    }
    
    public AggregateToCollectionOperation comment(final BsonValue comment) {
        this.comment = comment;
        return this;
    }
    
    public BsonValue getHint() {
        return this.hint;
    }
    
    public AggregateToCollectionOperation hint(@Nullable final BsonValue hint) {
        this.hint = hint;
        return this;
    }
    
    @Override
    public Void execute(final ReadBinding binding) {
        return SyncOperationHelper.executeRetryableRead(binding, (Supplier<ConnectionSource>)(() -> binding.getReadConnectionSource(12, ReadPreference.primary())), this.namespace.getDatabaseName(), (serverDescription, connectionDescription) -> this.getCommand(), (Decoder<BsonDocument>)new BsonDocumentCodec(), (result, source, connection) -> {
            WriteConcernHelper.throwOnWriteConcernError(result, connection.getDescription().getServerAddress(), connection.getDescription().getMaxWireVersion());
            return null;
        }, false);
    }
    
    @Override
    public void executeAsync(final AsyncReadBinding binding, final SingleResultCallback<Void> callback) {
        AsyncOperationHelper.executeRetryableReadAsync(binding, connectionSourceCallback -> binding.getReadConnectionSource(12, ReadPreference.primary(), connectionSourceCallback), this.namespace.getDatabaseName(), (serverDescription, connectionDescription) -> this.getCommand(), (Decoder<BsonDocument>)new BsonDocumentCodec(), (result, source, connection) -> {
            WriteConcernHelper.throwOnWriteConcernError(result, connection.getDescription().getServerAddress(), connection.getDescription().getMaxWireVersion());
            return null;
        }, false, callback);
    }
    
    private BsonDocument getCommand() {
        final BsonValue aggregationTarget = (this.aggregationLevel == AggregationLevel.DATABASE) ? new BsonInt32(1) : new BsonString(this.namespace.getCollectionName());
        final BsonDocument commandDocument = new BsonDocument("aggregate", aggregationTarget);
        commandDocument.put("pipeline", new BsonArray(this.pipeline));
        if (this.maxTimeMS > 0L) {
            commandDocument.put("maxTimeMS", new BsonInt64(this.maxTimeMS));
        }
        if (this.allowDiskUse != null) {
            commandDocument.put("allowDiskUse", BsonBoolean.valueOf(this.allowDiskUse));
        }
        if (this.bypassDocumentValidation != null) {
            commandDocument.put("bypassDocumentValidation", BsonBoolean.valueOf(this.bypassDocumentValidation));
        }
        commandDocument.put("cursor", new BsonDocument());
        WriteConcernHelper.appendWriteConcernToCommand(this.writeConcern, commandDocument);
        if (this.readConcern != null && !this.readConcern.isServerDefault()) {
            commandDocument.put("readConcern", this.readConcern.asDocument());
        }
        if (this.collation != null) {
            commandDocument.put("collation", this.collation.asDocument());
        }
        if (this.comment != null) {
            commandDocument.put("comment", this.comment);
        }
        if (this.hint != null) {
            commandDocument.put("hint", this.hint);
        }
        if (this.variables != null) {
            commandDocument.put("let", this.variables);
        }
        return commandDocument;
    }
}
