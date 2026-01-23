package cc.dreamcode.antylogout.libs.com.mongodb.internal.operation;

import java.util.Collection;
import java.util.ArrayDeque;
import java.util.Deque;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.Connection;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.AsyncConnection;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoException;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoClientException;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.ConnectionDescription;
import cc.dreamcode.antylogout.libs.org.bson.BsonInt64;
import java.util.concurrent.TimeUnit;
import cc.dreamcode.antylogout.libs.org.bson.BsonBoolean;
import cc.dreamcode.antylogout.libs.org.bson.BsonString;
import cc.dreamcode.antylogout.libs.org.bson.BsonValue;
import java.util.Arrays;
import java.util.Collections;
import java.util.function.Supplier;
import java.util.List;
import cc.dreamcode.antylogout.libs.com.mongodb.client.model.TimeSeriesGranularity;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.ErrorHandlingResultCallback;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.SingleResultCallback;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.AsyncWriteBinding;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.WriteBinding;
import cc.dreamcode.antylogout.libs.com.mongodb.assertions.Assertions;
import cc.dreamcode.antylogout.libs.com.mongodb.lang.Nullable;
import cc.dreamcode.antylogout.libs.com.mongodb.client.model.ChangeStreamPreAndPostImagesOptions;
import cc.dreamcode.antylogout.libs.com.mongodb.client.model.TimeSeriesOptions;
import cc.dreamcode.antylogout.libs.com.mongodb.client.model.Collation;
import cc.dreamcode.antylogout.libs.com.mongodb.client.model.ValidationAction;
import cc.dreamcode.antylogout.libs.com.mongodb.client.model.ValidationLevel;
import cc.dreamcode.antylogout.libs.com.mongodb.WriteConcern;
import cc.dreamcode.antylogout.libs.org.bson.BsonArray;
import cc.dreamcode.antylogout.libs.org.bson.BsonDocument;

public class CreateCollectionOperation implements AsyncWriteOperation<Void>, WriteOperation<Void>
{
    private static final String ENCRYPT_PREFIX = "enxcol_.";
    private static final BsonDocument ENCRYPT_CLUSTERED_INDEX;
    private static final BsonArray SAFE_CONTENT_ARRAY;
    private final String databaseName;
    private final String collectionName;
    private final WriteConcern writeConcern;
    private boolean capped;
    private long sizeInBytes;
    private boolean autoIndex;
    private long maxDocuments;
    private BsonDocument storageEngineOptions;
    private BsonDocument indexOptionDefaults;
    private BsonDocument validator;
    private ValidationLevel validationLevel;
    private ValidationAction validationAction;
    private Collation collation;
    private long expireAfterSeconds;
    private TimeSeriesOptions timeSeriesOptions;
    private ChangeStreamPreAndPostImagesOptions changeStreamPreAndPostImagesOptions;
    private BsonDocument clusteredIndexKey;
    private boolean clusteredIndexUnique;
    private String clusteredIndexName;
    private BsonDocument encryptedFields;
    
    public CreateCollectionOperation(final String databaseName, final String collectionName) {
        this(databaseName, collectionName, null);
    }
    
    public CreateCollectionOperation(final String databaseName, final String collectionName, @Nullable final WriteConcern writeConcern) {
        this.capped = false;
        this.sizeInBytes = 0L;
        this.autoIndex = true;
        this.maxDocuments = 0L;
        this.validationLevel = null;
        this.validationAction = null;
        this.collation = null;
        this.databaseName = Assertions.notNull("databaseName", databaseName);
        this.collectionName = Assertions.notNull("collectionName", collectionName);
        this.writeConcern = writeConcern;
    }
    
    public String getCollectionName() {
        return this.collectionName;
    }
    
    public WriteConcern getWriteConcern() {
        return this.writeConcern;
    }
    
    public boolean isAutoIndex() {
        return this.autoIndex;
    }
    
    public CreateCollectionOperation autoIndex(final boolean autoIndex) {
        this.autoIndex = autoIndex;
        return this;
    }
    
    public long getMaxDocuments() {
        return this.maxDocuments;
    }
    
    public CreateCollectionOperation maxDocuments(final long maxDocuments) {
        this.maxDocuments = maxDocuments;
        return this;
    }
    
    public boolean isCapped() {
        return this.capped;
    }
    
    public CreateCollectionOperation capped(final boolean capped) {
        this.capped = capped;
        return this;
    }
    
    public long getSizeInBytes() {
        return this.sizeInBytes;
    }
    
    public CreateCollectionOperation sizeInBytes(final long sizeInBytes) {
        this.sizeInBytes = sizeInBytes;
        return this;
    }
    
    public BsonDocument getStorageEngineOptions() {
        return this.storageEngineOptions;
    }
    
    public CreateCollectionOperation storageEngineOptions(@Nullable final BsonDocument storageEngineOptions) {
        this.storageEngineOptions = storageEngineOptions;
        return this;
    }
    
    public BsonDocument getIndexOptionDefaults() {
        return this.indexOptionDefaults;
    }
    
    public CreateCollectionOperation indexOptionDefaults(@Nullable final BsonDocument indexOptionDefaults) {
        this.indexOptionDefaults = indexOptionDefaults;
        return this;
    }
    
    public BsonDocument getValidator() {
        return this.validator;
    }
    
    public CreateCollectionOperation validator(@Nullable final BsonDocument validator) {
        this.validator = validator;
        return this;
    }
    
    public ValidationLevel getValidationLevel() {
        return this.validationLevel;
    }
    
    public CreateCollectionOperation validationLevel(@Nullable final ValidationLevel validationLevel) {
        this.validationLevel = validationLevel;
        return this;
    }
    
    public ValidationAction getValidationAction() {
        return this.validationAction;
    }
    
    public CreateCollectionOperation validationAction(@Nullable final ValidationAction validationAction) {
        this.validationAction = validationAction;
        return this;
    }
    
    public Collation getCollation() {
        return this.collation;
    }
    
    public CreateCollectionOperation collation(@Nullable final Collation collation) {
        this.collation = collation;
        return this;
    }
    
    public CreateCollectionOperation expireAfter(final long expireAfterSeconds) {
        this.expireAfterSeconds = expireAfterSeconds;
        return this;
    }
    
    public CreateCollectionOperation timeSeriesOptions(@Nullable final TimeSeriesOptions timeSeriesOptions) {
        this.timeSeriesOptions = timeSeriesOptions;
        return this;
    }
    
    public CreateCollectionOperation changeStreamPreAndPostImagesOptions(@Nullable final ChangeStreamPreAndPostImagesOptions changeStreamPreAndPostImagesOptions) {
        this.changeStreamPreAndPostImagesOptions = changeStreamPreAndPostImagesOptions;
        return this;
    }
    
    public CreateCollectionOperation clusteredIndexKey(@Nullable final BsonDocument clusteredIndexKey) {
        this.clusteredIndexKey = clusteredIndexKey;
        return this;
    }
    
    public CreateCollectionOperation clusteredIndexUnique(final boolean clusteredIndexUnique) {
        this.clusteredIndexUnique = clusteredIndexUnique;
        return this;
    }
    
    public CreateCollectionOperation clusteredIndexName(@Nullable final String clusteredIndexName) {
        this.clusteredIndexName = clusteredIndexName;
        return this;
    }
    
    public CreateCollectionOperation encryptedFields(@Nullable final BsonDocument encryptedFields) {
        this.encryptedFields = encryptedFields;
        return this;
    }
    
    @Override
    public Void execute(final WriteBinding binding) {
        return SyncOperationHelper.withConnection(binding, connection -> {
            this.checkEncryptedFieldsSupported(connection.getDescription());
            this.getCommandFunctions().forEach(commandCreator -> SyncOperationHelper.executeCommand(binding, this.databaseName, (BsonDocument)commandCreator.get(), connection, SyncOperationHelper.writeConcernErrorTransformer()));
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
                final SingleResultCallback<Void> releasingCallback = AsyncOperationHelper.releasingCallback(errHandlingCallback, connection);
                if (!(!this.checkEncryptedFieldsSupported(connection.getDescription(), releasingCallback))) {
                    new ProcessCommandsCallback(binding, connection, releasingCallback).onResult((Void)null, (Throwable)null);
                }
            }
        });
    }
    
    private String getGranularityAsString(final TimeSeriesGranularity granularity) {
        switch (granularity) {
            case SECONDS: {
                return "seconds";
            }
            case MINUTES: {
                return "minutes";
            }
            case HOURS: {
                return "hours";
            }
            default: {
                throw new AssertionError((Object)("Unexpected granularity " + (Object)granularity));
            }
        }
    }
    
    private List<Supplier<BsonDocument>> getCommandFunctions() {
        if (this.encryptedFields == null) {
            return (List<Supplier<BsonDocument>>)Collections.singletonList((Object)this::getCreateCollectionCommand);
        }
        return (List<Supplier<BsonDocument>>)Arrays.asList((Object[])new Supplier[] { () -> this.getCreateEncryptedFieldsCollectionCommand("esc"), () -> this.getCreateEncryptedFieldsCollectionCommand("ecoc"), this::getCreateCollectionCommand, () -> new BsonDocument("createIndexes", new BsonString(this.collectionName)).append("indexes", CreateCollectionOperation.SAFE_CONTENT_ARRAY) });
    }
    
    private BsonDocument getCreateEncryptedFieldsCollectionCommand(final String collectionSuffix) {
        return new BsonDocument().append("create", (BsonValue)this.encryptedFields.getOrDefault((Object)(collectionSuffix + "Collection"), (Object)new BsonString("enxcol_." + this.collectionName + "." + collectionSuffix))).append("clusteredIndex", CreateCollectionOperation.ENCRYPT_CLUSTERED_INDEX);
    }
    
    private BsonDocument getCreateCollectionCommand() {
        final BsonDocument document = new BsonDocument("create", new BsonString(this.collectionName));
        DocumentHelper.putIfFalse(document, "autoIndexId", this.autoIndex);
        document.put("capped", BsonBoolean.valueOf(this.capped));
        if (this.capped) {
            DocumentHelper.putIfNotZero(document, "size", this.sizeInBytes);
            DocumentHelper.putIfNotZero(document, "max", this.maxDocuments);
        }
        DocumentHelper.putIfNotNull(document, "storageEngine", this.storageEngineOptions);
        DocumentHelper.putIfNotNull(document, "indexOptionDefaults", this.indexOptionDefaults);
        DocumentHelper.putIfNotNull(document, "validator", this.validator);
        if (this.validationLevel != null) {
            document.put("validationLevel", new BsonString(this.validationLevel.getValue()));
        }
        if (this.validationAction != null) {
            document.put("validationAction", new BsonString(this.validationAction.getValue()));
        }
        WriteConcernHelper.appendWriteConcernToCommand(this.writeConcern, document);
        if (this.collation != null) {
            document.put("collation", this.collation.asDocument());
        }
        DocumentHelper.putIfNotZero(document, "expireAfterSeconds", this.expireAfterSeconds);
        if (this.timeSeriesOptions != null) {
            final BsonDocument timeSeriesDocument = new BsonDocument("timeField", new BsonString(this.timeSeriesOptions.getTimeField()));
            final String metaField = this.timeSeriesOptions.getMetaField();
            if (metaField != null) {
                timeSeriesDocument.put("metaField", new BsonString(metaField));
            }
            final TimeSeriesGranularity granularity = this.timeSeriesOptions.getGranularity();
            if (granularity != null) {
                timeSeriesDocument.put("granularity", new BsonString(this.getGranularityAsString(granularity)));
            }
            final Long bucketMaxSpan = this.timeSeriesOptions.getBucketMaxSpan(TimeUnit.SECONDS);
            if (bucketMaxSpan != null) {
                timeSeriesDocument.put("bucketMaxSpanSeconds", new BsonInt64(bucketMaxSpan));
            }
            final Long bucketRounding = this.timeSeriesOptions.getBucketRounding(TimeUnit.SECONDS);
            if (bucketRounding != null) {
                timeSeriesDocument.put("bucketRoundingSeconds", new BsonInt64(bucketRounding));
            }
            document.put("timeseries", timeSeriesDocument);
        }
        if (this.changeStreamPreAndPostImagesOptions != null) {
            document.put("changeStreamPreAndPostImages", new BsonDocument("enabled", BsonBoolean.valueOf(this.changeStreamPreAndPostImagesOptions.isEnabled())));
        }
        if (this.clusteredIndexKey != null) {
            final BsonDocument clusteredIndexDocument = new BsonDocument().append("key", this.clusteredIndexKey).append("unique", BsonBoolean.valueOf(this.clusteredIndexUnique));
            if (this.clusteredIndexName != null) {
                clusteredIndexDocument.put("name", new BsonString(this.clusteredIndexName));
            }
            document.put("clusteredIndex", clusteredIndexDocument);
        }
        DocumentHelper.putIfNotNull(document, "encryptedFields", this.encryptedFields);
        return document;
    }
    
    private void checkEncryptedFieldsSupported(final ConnectionDescription connectionDescription) throws MongoException {
        if (this.encryptedFields != null && ServerVersionHelper.serverIsLessThanVersionSevenDotZero(connectionDescription)) {
            throw new MongoClientException("Driver support of Queryable Encryption is incompatible with server. Upgrade server to use Queryable Encryption.");
        }
    }
    
    private boolean checkEncryptedFieldsSupported(final ConnectionDescription connectionDescription, final SingleResultCallback<Void> callback) {
        try {
            this.checkEncryptedFieldsSupported(connectionDescription);
            return true;
        }
        catch (final Exception e) {
            callback.onResult(null, (Throwable)e);
            return false;
        }
    }
    
    static {
        ENCRYPT_CLUSTERED_INDEX = BsonDocument.parse("{key: {_id: 1}, unique: true}");
        SAFE_CONTENT_ARRAY = new BsonArray((List<? extends BsonValue>)Collections.singletonList((Object)BsonDocument.parse("{key: {__safeContent__: 1}, name: '__safeContent___1'}")));
    }
    
    class ProcessCommandsCallback implements SingleResultCallback<Void>
    {
        private final AsyncWriteBinding binding;
        private final AsyncConnection connection;
        private final SingleResultCallback<Void> finalCallback;
        private final Deque<Supplier<BsonDocument>> commands;
        
        ProcessCommandsCallback(final AsyncWriteBinding binding, final AsyncConnection connection, final SingleResultCallback<Void> finalCallback) {
            this.binding = binding;
            this.connection = connection;
            this.finalCallback = finalCallback;
            this.commands = (Deque<Supplier<BsonDocument>>)new ArrayDeque((Collection)CreateCollectionOperation.this.getCommandFunctions());
        }
        
        @Override
        public void onResult(@Nullable final Void result, @Nullable final Throwable t) {
            if (t != null) {
                this.finalCallback.onResult(null, t);
                return;
            }
            final Supplier<BsonDocument> nextCommandFunction = (Supplier<BsonDocument>)this.commands.poll();
            if (nextCommandFunction == null) {
                this.finalCallback.onResult(null, null);
            }
            else {
                AsyncOperationHelper.executeCommandAsync(this.binding, CreateCollectionOperation.this.databaseName, (BsonDocument)nextCommandFunction.get(), this.connection, AsyncOperationHelper.writeConcernErrorTransformerAsync(), this);
            }
        }
    }
}
