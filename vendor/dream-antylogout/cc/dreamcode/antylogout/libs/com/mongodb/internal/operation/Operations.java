package cc.dreamcode.antylogout.libs.com.mongodb.internal.operation;

import cc.dreamcode.antylogout.libs.org.bson.codecs.Encoder;
import cc.dreamcode.antylogout.libs.org.bson.BsonDocumentWrapper;
import cc.dreamcode.antylogout.libs.org.bson.codecs.Codec;
import cc.dreamcode.antylogout.libs.org.bson.BsonTimestamp;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.client.model.changestream.ChangeStreamLevel;
import cc.dreamcode.antylogout.libs.com.mongodb.client.model.changestream.FullDocumentBeforeChange;
import cc.dreamcode.antylogout.libs.com.mongodb.client.model.changestream.FullDocument;
import cc.dreamcode.antylogout.libs.com.mongodb.client.model.DropIndexOptions;
import java.util.stream.Collectors;
import cc.dreamcode.antylogout.libs.com.mongodb.client.model.SearchIndexModel;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.bulk.IndexRequest;
import cc.dreamcode.antylogout.libs.com.mongodb.client.model.CreateIndexOptions;
import cc.dreamcode.antylogout.libs.com.mongodb.client.model.IndexModel;
import cc.dreamcode.antylogout.libs.com.mongodb.client.model.CreateViewOptions;
import cc.dreamcode.antylogout.libs.com.mongodb.client.model.RenameCollectionOptions;
import cc.dreamcode.antylogout.libs.com.mongodb.client.model.DropCollectionOptions;
import cc.dreamcode.antylogout.libs.com.mongodb.client.model.ValidationOptions;
import cc.dreamcode.antylogout.libs.com.mongodb.client.model.IndexOptionDefaults;
import java.util.Map;
import cc.dreamcode.antylogout.libs.com.mongodb.client.model.ClusteredIndexOptions;
import cc.dreamcode.antylogout.libs.com.mongodb.AutoEncryptionSettings;
import cc.dreamcode.antylogout.libs.com.mongodb.client.model.CreateCollectionOptions;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.bulk.DeleteRequest;
import cc.dreamcode.antylogout.libs.org.bson.BsonArray;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.bulk.UpdateRequest;
import java.util.Iterator;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.bulk.WriteRequest;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.bulk.InsertRequest;
import cc.dreamcode.antylogout.libs.org.bson.codecs.CollectibleCodec;
import java.util.ArrayList;
import cc.dreamcode.antylogout.libs.com.mongodb.client.model.InsertManyOptions;
import cc.dreamcode.antylogout.libs.com.mongodb.client.model.UpdateManyModel;
import cc.dreamcode.antylogout.libs.com.mongodb.client.model.UpdateOneModel;
import cc.dreamcode.antylogout.libs.com.mongodb.client.model.UpdateOptions;
import cc.dreamcode.antylogout.libs.com.mongodb.client.model.DeleteManyModel;
import cc.dreamcode.antylogout.libs.com.mongodb.client.model.DeleteOneModel;
import cc.dreamcode.antylogout.libs.com.mongodb.client.model.DeleteOptions;
import cc.dreamcode.antylogout.libs.com.mongodb.client.model.ReplaceOneModel;
import cc.dreamcode.antylogout.libs.com.mongodb.client.model.ReplaceOptions;
import cc.dreamcode.antylogout.libs.com.mongodb.client.model.WriteModel;
import cc.dreamcode.antylogout.libs.com.mongodb.client.model.BulkWriteOptions;
import java.util.Collections;
import cc.dreamcode.antylogout.libs.com.mongodb.client.model.InsertOneModel;
import cc.dreamcode.antylogout.libs.com.mongodb.client.model.InsertOneOptions;
import cc.dreamcode.antylogout.libs.com.mongodb.client.model.FindOneAndUpdateOptions;
import cc.dreamcode.antylogout.libs.com.mongodb.client.model.ReturnDocument;
import cc.dreamcode.antylogout.libs.com.mongodb.client.model.FindOneAndReplaceOptions;
import cc.dreamcode.antylogout.libs.com.mongodb.client.model.FindOneAndDeleteOptions;
import cc.dreamcode.antylogout.libs.org.bson.BsonJavaScript;
import cc.dreamcode.antylogout.libs.com.mongodb.client.model.MapReduceAction;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.client.model.AggregationLevel;
import java.util.List;
import cc.dreamcode.antylogout.libs.com.mongodb.client.model.Collation;
import cc.dreamcode.antylogout.libs.org.bson.BsonDocument;
import cc.dreamcode.antylogout.libs.org.bson.codecs.Decoder;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.client.model.FindOptions;
import cc.dreamcode.antylogout.libs.com.mongodb.client.model.EstimatedDocumentCountOptions;
import cc.dreamcode.antylogout.libs.org.bson.BsonString;
import cc.dreamcode.antylogout.libs.org.bson.BsonValue;
import java.util.concurrent.TimeUnit;
import cc.dreamcode.antylogout.libs.com.mongodb.assertions.Assertions;
import cc.dreamcode.antylogout.libs.com.mongodb.client.model.CountOptions;
import cc.dreamcode.antylogout.libs.org.bson.conversions.Bson;
import cc.dreamcode.antylogout.libs.com.mongodb.lang.Nullable;
import cc.dreamcode.antylogout.libs.com.mongodb.WriteConcern;
import cc.dreamcode.antylogout.libs.com.mongodb.ReadConcern;
import cc.dreamcode.antylogout.libs.org.bson.codecs.configuration.CodecRegistry;
import cc.dreamcode.antylogout.libs.com.mongodb.ReadPreference;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoNamespace;

final class Operations<TDocument>
{
    private final MongoNamespace namespace;
    private final Class<TDocument> documentClass;
    private final ReadPreference readPreference;
    private final CodecRegistry codecRegistry;
    private final ReadConcern readConcern;
    private final WriteConcern writeConcern;
    private final boolean retryWrites;
    private final boolean retryReads;
    
    Operations(@Nullable final MongoNamespace namespace, final Class<TDocument> documentClass, final ReadPreference readPreference, final CodecRegistry codecRegistry, final ReadConcern readConcern, final WriteConcern writeConcern, final boolean retryWrites, final boolean retryReads) {
        this.namespace = namespace;
        this.documentClass = documentClass;
        this.readPreference = readPreference;
        this.codecRegistry = codecRegistry;
        this.readConcern = readConcern;
        this.writeConcern = writeConcern;
        this.retryWrites = retryWrites;
        this.retryReads = retryReads;
    }
    
    @Nullable
    MongoNamespace getNamespace() {
        return this.namespace;
    }
    
    Class<TDocument> getDocumentClass() {
        return this.documentClass;
    }
    
    ReadPreference getReadPreference() {
        return this.readPreference;
    }
    
    CodecRegistry getCodecRegistry() {
        return this.codecRegistry;
    }
    
    ReadConcern getReadConcern() {
        return this.readConcern;
    }
    
    WriteConcern getWriteConcern() {
        return this.writeConcern;
    }
    
    boolean isRetryWrites() {
        return this.retryWrites;
    }
    
    boolean isRetryReads() {
        return this.retryReads;
    }
    
    CountDocumentsOperation countDocuments(final Bson filter, final CountOptions options) {
        final CountDocumentsOperation operation = new CountDocumentsOperation(Assertions.assertNotNull(this.namespace)).retryReads(this.retryReads).filter(this.toBsonDocument(filter)).skip(options.getSkip()).limit(options.getLimit()).maxTime(options.getMaxTime(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS).collation(options.getCollation()).comment(options.getComment());
        if (options.getHint() != null) {
            operation.hint(this.toBsonDocument(options.getHint()));
        }
        else if (options.getHintString() != null) {
            operation.hint(new BsonString(options.getHintString()));
        }
        return operation;
    }
    
    EstimatedDocumentCountOperation estimatedDocumentCount(final EstimatedDocumentCountOptions options) {
        return new EstimatedDocumentCountOperation(Assertions.assertNotNull(this.namespace)).retryReads(this.retryReads).maxTime(options.getMaxTime(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS).comment(options.getComment());
    }
    
     <TResult> FindOperation<TResult> findFirst(final Bson filter, final Class<TResult> resultClass, final FindOptions options) {
        return this.createFindOperation(Assertions.assertNotNull(this.namespace), filter, resultClass, options).batchSize(0).limit(-1);
    }
    
     <TResult> FindOperation<TResult> find(final Bson filter, final Class<TResult> resultClass, final FindOptions options) {
        return this.createFindOperation(Assertions.assertNotNull(this.namespace), filter, resultClass, options);
    }
    
     <TResult> FindOperation<TResult> find(final MongoNamespace findNamespace, @Nullable final Bson filter, final Class<TResult> resultClass, final FindOptions options) {
        return (FindOperation<TResult>)this.createFindOperation(findNamespace, filter, (Class<Object>)resultClass, options);
    }
    
    private <TResult> FindOperation<TResult> createFindOperation(final MongoNamespace findNamespace, @Nullable final Bson filter, final Class<TResult> resultClass, final FindOptions options) {
        final FindOperation<TResult> operation = new FindOperation<TResult>(findNamespace, this.codecRegistry.get(resultClass)).retryReads(this.retryReads).filter((filter == null) ? new BsonDocument() : filter.toBsonDocument(this.documentClass, this.codecRegistry)).batchSize(options.getBatchSize()).skip(options.getSkip()).limit(options.getLimit()).maxTime(options.getMaxTime(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS).maxAwaitTime(options.getMaxAwaitTime(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS).projection(this.toBsonDocument(options.getProjection())).sort(this.toBsonDocument(options.getSort())).cursorType(options.getCursorType()).noCursorTimeout(options.isNoCursorTimeout()).partial(options.isPartial()).collation(options.getCollation()).comment(options.getComment()).let(this.toBsonDocument(options.getLet())).min(this.toBsonDocument(options.getMin())).max(this.toBsonDocument(options.getMax())).returnKey(options.isReturnKey()).showRecordId(options.isShowRecordId()).allowDiskUse(options.isAllowDiskUse());
        if (options.getHint() != null) {
            operation.hint(this.toBsonDocument(options.getHint()));
        }
        else if (options.getHintString() != null) {
            operation.hint(new BsonString(options.getHintString()));
        }
        return operation;
    }
    
     <TResult> DistinctOperation<TResult> distinct(final String fieldName, @Nullable final Bson filter, final Class<TResult> resultClass, final long maxTimeMS, final Collation collation, final BsonValue comment) {
        return new DistinctOperation<TResult>(Assertions.assertNotNull(this.namespace), fieldName, this.codecRegistry.get(resultClass)).retryReads(this.retryReads).filter((filter == null) ? null : filter.toBsonDocument(this.documentClass, this.codecRegistry)).maxTime(maxTimeMS, TimeUnit.MILLISECONDS).collation(collation).comment(comment);
    }
    
     <TResult> AggregateOperation<TResult> aggregate(final List<? extends Bson> pipeline, final Class<TResult> resultClass, final long maxTimeMS, final long maxAwaitTimeMS, @Nullable final Integer batchSize, final Collation collation, @Nullable final Bson hint, @Nullable final String hintString, final BsonValue comment, final Bson variables, final Boolean allowDiskUse, final AggregationLevel aggregationLevel) {
        return new AggregateOperation<TResult>(Assertions.assertNotNull(this.namespace), Assertions.assertNotNull(this.toBsonDocumentList(pipeline)), this.codecRegistry.get(resultClass), aggregationLevel).retryReads(this.retryReads).maxTime(maxTimeMS, TimeUnit.MILLISECONDS).maxAwaitTime(maxAwaitTimeMS, TimeUnit.MILLISECONDS).allowDiskUse(allowDiskUse).batchSize(batchSize).collation(collation).hint((hint != null) ? this.toBsonDocument(hint) : ((hintString != null) ? new BsonString(hintString) : null)).comment(comment).let(this.toBsonDocument(variables));
    }
    
    AggregateToCollectionOperation aggregateToCollection(final List<? extends Bson> pipeline, final long maxTimeMS, final Boolean allowDiskUse, final Boolean bypassDocumentValidation, final Collation collation, @Nullable final Bson hint, @Nullable final String hintString, final BsonValue comment, final Bson variables, final AggregationLevel aggregationLevel) {
        return new AggregateToCollectionOperation(Assertions.assertNotNull(this.namespace), Assertions.assertNotNull(this.toBsonDocumentList(pipeline)), this.readConcern, this.writeConcern, aggregationLevel).maxTime(maxTimeMS, TimeUnit.MILLISECONDS).allowDiskUse(allowDiskUse).bypassDocumentValidation(bypassDocumentValidation).collation(collation).hint((hint != null) ? this.toBsonDocument(hint) : ((hintString != null) ? new BsonString(hintString) : null)).comment(comment).let(this.toBsonDocument(variables));
    }
    
    MapReduceToCollectionOperation mapReduceToCollection(final String databaseName, final String collectionName, final String mapFunction, final String reduceFunction, @Nullable final String finalizeFunction, final Bson filter, final int limit, final long maxTimeMS, final boolean jsMode, final Bson scope, final Bson sort, final boolean verbose, final MapReduceAction action, final Boolean bypassDocumentValidation, final Collation collation) {
        final MapReduceToCollectionOperation operation = new MapReduceToCollectionOperation(Assertions.assertNotNull(this.namespace), new BsonJavaScript(mapFunction), new BsonJavaScript(reduceFunction), collectionName, this.writeConcern).filter(this.toBsonDocument(filter)).limit(limit).maxTime(maxTimeMS, TimeUnit.MILLISECONDS).jsMode(jsMode).scope(this.toBsonDocument(scope)).sort(this.toBsonDocument(sort)).verbose(verbose).action(action.getValue()).databaseName(databaseName).bypassDocumentValidation(bypassDocumentValidation).collation(collation);
        if (finalizeFunction != null) {
            operation.finalizeFunction(new BsonJavaScript(finalizeFunction));
        }
        return operation;
    }
    
     <TResult> MapReduceWithInlineResultsOperation<TResult> mapReduce(final String mapFunction, final String reduceFunction, @Nullable final String finalizeFunction, final Class<TResult> resultClass, final Bson filter, final int limit, final long maxTimeMS, final boolean jsMode, final Bson scope, final Bson sort, final boolean verbose, final Collation collation) {
        final MapReduceWithInlineResultsOperation<TResult> operation = new MapReduceWithInlineResultsOperation<TResult>(Assertions.assertNotNull(this.namespace), new BsonJavaScript(mapFunction), new BsonJavaScript(reduceFunction), this.codecRegistry.get(resultClass)).filter(this.toBsonDocument(filter)).limit(limit).maxTime(maxTimeMS, TimeUnit.MILLISECONDS).jsMode(jsMode).scope(this.toBsonDocument(scope)).sort(this.toBsonDocument(sort)).verbose(verbose).collation(collation);
        if (finalizeFunction != null) {
            operation.finalizeFunction(new BsonJavaScript(finalizeFunction));
        }
        return operation;
    }
    
    FindAndDeleteOperation<TDocument> findOneAndDelete(final Bson filter, final FindOneAndDeleteOptions options) {
        return new FindAndDeleteOperation<TDocument>(Assertions.assertNotNull(this.namespace), this.writeConcern, this.retryWrites, this.getCodec()).filter(this.toBsonDocument(filter)).projection(this.toBsonDocument(options.getProjection())).sort(this.toBsonDocument(options.getSort())).maxTime(options.getMaxTime(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS).collation(options.getCollation()).hint(this.toBsonDocument(options.getHint())).hintString(options.getHintString()).comment(options.getComment()).let(this.toBsonDocument(options.getLet()));
    }
    
    FindAndReplaceOperation<TDocument> findOneAndReplace(final Bson filter, final TDocument replacement, final FindOneAndReplaceOptions options) {
        return new FindAndReplaceOperation<TDocument>(Assertions.assertNotNull(this.namespace), this.writeConcern, this.retryWrites, this.getCodec(), this.documentToBsonDocument(replacement)).filter(this.toBsonDocument(filter)).projection(this.toBsonDocument(options.getProjection())).sort(this.toBsonDocument(options.getSort())).returnOriginal(options.getReturnDocument() == ReturnDocument.BEFORE).upsert(options.isUpsert()).maxTime(options.getMaxTime(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS).bypassDocumentValidation(options.getBypassDocumentValidation()).collation(options.getCollation()).hint(this.toBsonDocument(options.getHint())).hintString(options.getHintString()).comment(options.getComment()).let(this.toBsonDocument(options.getLet()));
    }
    
    FindAndUpdateOperation<TDocument> findOneAndUpdate(final Bson filter, final Bson update, final FindOneAndUpdateOptions options) {
        return new FindAndUpdateOperation<TDocument>(Assertions.assertNotNull(this.namespace), this.writeConcern, this.retryWrites, this.getCodec(), Assertions.assertNotNull(this.toBsonDocument(update))).filter(this.toBsonDocument(filter)).projection(this.toBsonDocument(options.getProjection())).sort(this.toBsonDocument(options.getSort())).returnOriginal(options.getReturnDocument() == ReturnDocument.BEFORE).upsert(options.isUpsert()).maxTime(options.getMaxTime(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS).bypassDocumentValidation(options.getBypassDocumentValidation()).collation(options.getCollation()).arrayFilters(this.toBsonDocumentList(options.getArrayFilters())).hint(this.toBsonDocument(options.getHint())).hintString(options.getHintString()).comment(options.getComment()).let(this.toBsonDocument(options.getLet()));
    }
    
    FindAndUpdateOperation<TDocument> findOneAndUpdate(final Bson filter, final List<? extends Bson> update, final FindOneAndUpdateOptions options) {
        return new FindAndUpdateOperation<TDocument>(Assertions.assertNotNull(this.namespace), this.writeConcern, this.retryWrites, this.getCodec(), Assertions.assertNotNull(this.toBsonDocumentList(update))).filter(this.toBsonDocument(filter)).projection(this.toBsonDocument(options.getProjection())).sort(this.toBsonDocument(options.getSort())).returnOriginal(options.getReturnDocument() == ReturnDocument.BEFORE).upsert(options.isUpsert()).maxTime(options.getMaxTime(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS).bypassDocumentValidation(options.getBypassDocumentValidation()).collation(options.getCollation()).arrayFilters(this.toBsonDocumentList(options.getArrayFilters())).hint(this.toBsonDocument(options.getHint())).hintString(options.getHintString()).comment(options.getComment()).let(this.toBsonDocument(options.getLet()));
    }
    
    MixedBulkWriteOperation insertOne(final TDocument document, final InsertOneOptions options) {
        return this.bulkWrite((List<? extends WriteModel<? extends TDocument>>)Collections.singletonList((Object)new InsertOneModel(document)), new BulkWriteOptions().bypassDocumentValidation(options.getBypassDocumentValidation()).comment(options.getComment()));
    }
    
    MixedBulkWriteOperation replaceOne(final Bson filter, final TDocument replacement, final ReplaceOptions options) {
        return this.bulkWrite((List<? extends WriteModel<? extends TDocument>>)Collections.singletonList((Object)new ReplaceOneModel(filter, replacement, options)), new BulkWriteOptions().bypassDocumentValidation(options.getBypassDocumentValidation()).comment(options.getComment()).let(options.getLet()));
    }
    
    MixedBulkWriteOperation deleteOne(final Bson filter, final DeleteOptions options) {
        return this.bulkWrite((List<? extends WriteModel<? extends TDocument>>)Collections.singletonList((Object)new DeleteOneModel(filter, options)), new BulkWriteOptions().comment(options.getComment()).let(options.getLet()));
    }
    
    MixedBulkWriteOperation deleteMany(final Bson filter, final DeleteOptions options) {
        return this.bulkWrite((List<? extends WriteModel<? extends TDocument>>)Collections.singletonList((Object)new DeleteManyModel(filter, options)), new BulkWriteOptions().comment(options.getComment()).let(options.getLet()));
    }
    
    MixedBulkWriteOperation updateOne(final Bson filter, final Bson update, final UpdateOptions options) {
        return this.bulkWrite((List<? extends WriteModel<? extends TDocument>>)Collections.singletonList((Object)new UpdateOneModel(filter, update, options)), new BulkWriteOptions().bypassDocumentValidation(options.getBypassDocumentValidation()).comment(options.getComment()).let(options.getLet()));
    }
    
    MixedBulkWriteOperation updateOne(final Bson filter, final List<? extends Bson> update, final UpdateOptions options) {
        return this.bulkWrite((List<? extends WriteModel<? extends TDocument>>)Collections.singletonList((Object)new UpdateOneModel(filter, update, options)), new BulkWriteOptions().bypassDocumentValidation(options.getBypassDocumentValidation()).comment(options.getComment()).let(options.getLet()));
    }
    
    MixedBulkWriteOperation updateMany(final Bson filter, final Bson update, final UpdateOptions options) {
        return this.bulkWrite((List<? extends WriteModel<? extends TDocument>>)Collections.singletonList((Object)new UpdateManyModel(filter, update, options)), new BulkWriteOptions().bypassDocumentValidation(options.getBypassDocumentValidation()).comment(options.getComment()).let(options.getLet()));
    }
    
    MixedBulkWriteOperation updateMany(final Bson filter, final List<? extends Bson> update, final UpdateOptions options) {
        return this.bulkWrite((List<? extends WriteModel<? extends TDocument>>)Collections.singletonList((Object)new UpdateManyModel(filter, update, options)), new BulkWriteOptions().bypassDocumentValidation(options.getBypassDocumentValidation()).comment(options.getComment()).let(options.getLet()));
    }
    
    MixedBulkWriteOperation insertMany(final List<? extends TDocument> documents, final InsertManyOptions options) {
        Assertions.notNull("documents", documents);
        final List<InsertRequest> requests = (List<InsertRequest>)new ArrayList(documents.size());
        for (TDocument document : documents) {
            if (document == null) {
                throw new IllegalArgumentException("documents can not contain a null value");
            }
            if (this.getCodec() instanceof CollectibleCodec) {
                document = ((CollectibleCodec)this.getCodec()).generateIdIfAbsentFromDocument(document);
            }
            requests.add((Object)new InsertRequest(this.documentToBsonDocument(document)));
        }
        return new MixedBulkWriteOperation(Assertions.assertNotNull(this.namespace), requests, options.isOrdered(), this.writeConcern, this.retryWrites).bypassDocumentValidation(options.getBypassDocumentValidation()).comment(options.getComment());
    }
    
    MixedBulkWriteOperation bulkWrite(final List<? extends WriteModel<? extends TDocument>> requests, final BulkWriteOptions options) {
        Assertions.notNull("requests", requests);
        final List<WriteRequest> writeRequests = (List<WriteRequest>)new ArrayList(requests.size());
        for (final WriteModel<? extends TDocument> writeModel : requests) {
            if (writeModel == null) {
                throw new IllegalArgumentException("requests can not contain a null value");
            }
            WriteRequest writeRequest;
            if (writeModel instanceof InsertOneModel) {
                TDocument document = ((InsertOneModel)writeModel).getDocument();
                if (this.getCodec() instanceof CollectibleCodec) {
                    document = ((CollectibleCodec)this.getCodec()).generateIdIfAbsentFromDocument(document);
                }
                writeRequest = new InsertRequest(this.documentToBsonDocument(document));
            }
            else if (writeModel instanceof ReplaceOneModel) {
                final ReplaceOneModel<TDocument> replaceOneModel = (ReplaceOneModel)writeModel;
                writeRequest = new UpdateRequest(Assertions.assertNotNull(this.toBsonDocument(replaceOneModel.getFilter())), this.documentToBsonDocument(replaceOneModel.getReplacement()), WriteRequest.Type.REPLACE).upsert(replaceOneModel.getReplaceOptions().isUpsert()).collation(replaceOneModel.getReplaceOptions().getCollation()).hint(this.toBsonDocument(replaceOneModel.getReplaceOptions().getHint())).hintString(replaceOneModel.getReplaceOptions().getHintString());
            }
            else if (writeModel instanceof UpdateOneModel) {
                final UpdateOneModel<TDocument> updateOneModel = (UpdateOneModel)writeModel;
                final BsonValue update = (updateOneModel.getUpdate() != null) ? this.toBsonDocument(updateOneModel.getUpdate()) : new BsonArray(this.toBsonDocumentList(updateOneModel.getUpdatePipeline()));
                writeRequest = new UpdateRequest(Assertions.assertNotNull(this.toBsonDocument(updateOneModel.getFilter())), update, WriteRequest.Type.UPDATE).multi(false).upsert(updateOneModel.getOptions().isUpsert()).collation(updateOneModel.getOptions().getCollation()).arrayFilters(this.toBsonDocumentList(updateOneModel.getOptions().getArrayFilters())).hint(this.toBsonDocument(updateOneModel.getOptions().getHint())).hintString(updateOneModel.getOptions().getHintString());
            }
            else if (writeModel instanceof UpdateManyModel) {
                final UpdateManyModel<TDocument> updateManyModel = (UpdateManyModel)writeModel;
                final BsonValue update = (updateManyModel.getUpdate() != null) ? this.toBsonDocument(updateManyModel.getUpdate()) : new BsonArray(this.toBsonDocumentList(updateManyModel.getUpdatePipeline()));
                writeRequest = new UpdateRequest(Assertions.assertNotNull(this.toBsonDocument(updateManyModel.getFilter())), update, WriteRequest.Type.UPDATE).multi(true).upsert(updateManyModel.getOptions().isUpsert()).collation(updateManyModel.getOptions().getCollation()).arrayFilters(this.toBsonDocumentList(updateManyModel.getOptions().getArrayFilters())).hint(this.toBsonDocument(updateManyModel.getOptions().getHint())).hintString(updateManyModel.getOptions().getHintString());
            }
            else if (writeModel instanceof DeleteOneModel) {
                final DeleteOneModel<TDocument> deleteOneModel = (DeleteOneModel)writeModel;
                writeRequest = new DeleteRequest(Assertions.assertNotNull(this.toBsonDocument(deleteOneModel.getFilter()))).multi(false).collation(deleteOneModel.getOptions().getCollation()).hint(this.toBsonDocument(deleteOneModel.getOptions().getHint())).hintString(deleteOneModel.getOptions().getHintString());
            }
            else {
                if (!(writeModel instanceof DeleteManyModel)) {
                    throw new UnsupportedOperationException(String.format("WriteModel of type %s is not supported", new Object[] { writeModel.getClass() }));
                }
                final DeleteManyModel<TDocument> deleteManyModel = (DeleteManyModel)writeModel;
                writeRequest = new DeleteRequest(Assertions.assertNotNull(this.toBsonDocument(deleteManyModel.getFilter()))).multi(true).collation(deleteManyModel.getOptions().getCollation()).hint(this.toBsonDocument(deleteManyModel.getOptions().getHint())).hintString(deleteManyModel.getOptions().getHintString());
            }
            writeRequests.add((Object)writeRequest);
        }
        return new MixedBulkWriteOperation(Assertions.assertNotNull(this.namespace), writeRequests, options.isOrdered(), this.writeConcern, this.retryWrites).bypassDocumentValidation(options.getBypassDocumentValidation()).comment(options.getComment()).let(this.toBsonDocument(options.getLet()));
    }
    
     <TResult> CommandReadOperation<TResult> commandRead(final Bson command, final Class<TResult> resultClass) {
        Assertions.notNull("command", command);
        Assertions.notNull("resultClass", resultClass);
        return new CommandReadOperation<TResult>(Assertions.assertNotNull(this.namespace).getDatabaseName(), Assertions.assertNotNull(this.toBsonDocument(command)), this.codecRegistry.get(resultClass));
    }
    
    DropDatabaseOperation dropDatabase() {
        return new DropDatabaseOperation(Assertions.assertNotNull(this.namespace).getDatabaseName(), this.getWriteConcern());
    }
    
    CreateCollectionOperation createCollection(final String collectionName, final CreateCollectionOptions createCollectionOptions, @Nullable final AutoEncryptionSettings autoEncryptionSettings) {
        final CreateCollectionOperation operation = new CreateCollectionOperation(Assertions.assertNotNull(this.namespace).getDatabaseName(), collectionName, this.writeConcern).collation(createCollectionOptions.getCollation()).capped(createCollectionOptions.isCapped()).sizeInBytes(createCollectionOptions.getSizeInBytes()).maxDocuments(createCollectionOptions.getMaxDocuments()).storageEngineOptions(this.toBsonDocument(createCollectionOptions.getStorageEngineOptions())).expireAfter(createCollectionOptions.getExpireAfter(TimeUnit.SECONDS)).timeSeriesOptions(createCollectionOptions.getTimeSeriesOptions()).changeStreamPreAndPostImagesOptions(createCollectionOptions.getChangeStreamPreAndPostImagesOptions());
        final ClusteredIndexOptions clusteredIndexOptions = createCollectionOptions.getClusteredIndexOptions();
        if (clusteredIndexOptions != null) {
            operation.clusteredIndexKey(this.toBsonDocument(clusteredIndexOptions.getKey()));
            operation.clusteredIndexUnique(clusteredIndexOptions.isUnique());
            operation.clusteredIndexName(clusteredIndexOptions.getName());
        }
        final Bson encryptedFields = createCollectionOptions.getEncryptedFields();
        operation.encryptedFields(this.toBsonDocument(encryptedFields));
        if (encryptedFields == null && autoEncryptionSettings != null) {
            final Map<String, BsonDocument> encryptedFieldsMap = autoEncryptionSettings.getEncryptedFieldsMap();
            if (encryptedFieldsMap != null) {
                operation.encryptedFields((BsonDocument)encryptedFieldsMap.getOrDefault((Object)(this.namespace.getDatabaseName() + "." + collectionName), (Object)null));
            }
        }
        final IndexOptionDefaults indexOptionDefaults = createCollectionOptions.getIndexOptionDefaults();
        final Bson storageEngine = indexOptionDefaults.getStorageEngine();
        if (storageEngine != null) {
            operation.indexOptionDefaults(new BsonDocument("storageEngine", this.toBsonDocument(storageEngine)));
        }
        final ValidationOptions validationOptions = createCollectionOptions.getValidationOptions();
        final Bson validator = validationOptions.getValidator();
        operation.validator(this.toBsonDocument(validator));
        operation.validationLevel(validationOptions.getValidationLevel());
        operation.validationAction(validationOptions.getValidationAction());
        return operation;
    }
    
    DropCollectionOperation dropCollection(final DropCollectionOptions dropCollectionOptions, @Nullable final AutoEncryptionSettings autoEncryptionSettings) {
        final DropCollectionOperation operation = new DropCollectionOperation(Assertions.assertNotNull(this.namespace), this.writeConcern);
        final Bson encryptedFields = dropCollectionOptions.getEncryptedFields();
        if (encryptedFields != null) {
            operation.encryptedFields(Assertions.assertNotNull(this.toBsonDocument(encryptedFields)));
        }
        else if (autoEncryptionSettings != null) {
            final Map<String, BsonDocument> encryptedFieldsMap = autoEncryptionSettings.getEncryptedFieldsMap();
            if (encryptedFieldsMap != null) {
                operation.encryptedFields((BsonDocument)encryptedFieldsMap.getOrDefault((Object)this.namespace.getFullName(), (Object)null));
                operation.autoEncryptedFields(true);
            }
        }
        return operation;
    }
    
    RenameCollectionOperation renameCollection(final MongoNamespace newCollectionNamespace, final RenameCollectionOptions renameCollectionOptions) {
        return new RenameCollectionOperation(Assertions.assertNotNull(this.namespace), newCollectionNamespace, this.writeConcern).dropTarget(renameCollectionOptions.isDropTarget());
    }
    
    CreateViewOperation createView(final String viewName, final String viewOn, final List<? extends Bson> pipeline, final CreateViewOptions createViewOptions) {
        Assertions.notNull("options", createViewOptions);
        Assertions.notNull("pipeline", pipeline);
        return new CreateViewOperation(Assertions.assertNotNull(this.namespace).getDatabaseName(), viewName, viewOn, Assertions.assertNotNull(this.toBsonDocumentList(pipeline)), this.writeConcern).collation(createViewOptions.getCollation());
    }
    
    CreateIndexesOperation createIndexes(final List<IndexModel> indexes, final CreateIndexOptions createIndexOptions) {
        Assertions.notNull("indexes", indexes);
        Assertions.notNull("createIndexOptions", createIndexOptions);
        final List<IndexRequest> indexRequests = (List<IndexRequest>)new ArrayList(indexes.size());
        for (final IndexModel model : indexes) {
            if (model == null) {
                throw new IllegalArgumentException("indexes can not contain a null value");
            }
            indexRequests.add((Object)new IndexRequest(Assertions.assertNotNull(this.toBsonDocument(model.getKeys()))).name(model.getOptions().getName()).background(model.getOptions().isBackground()).unique(model.getOptions().isUnique()).sparse(model.getOptions().isSparse()).expireAfter(model.getOptions().getExpireAfter(TimeUnit.SECONDS), TimeUnit.SECONDS).version(model.getOptions().getVersion()).weights(this.toBsonDocument(model.getOptions().getWeights())).defaultLanguage(model.getOptions().getDefaultLanguage()).languageOverride(model.getOptions().getLanguageOverride()).textVersion(model.getOptions().getTextVersion()).sphereVersion(model.getOptions().getSphereVersion()).bits(model.getOptions().getBits()).min(model.getOptions().getMin()).max(model.getOptions().getMax()).storageEngine(this.toBsonDocument(model.getOptions().getStorageEngine())).partialFilterExpression(this.toBsonDocument(model.getOptions().getPartialFilterExpression())).collation(model.getOptions().getCollation()).wildcardProjection(this.toBsonDocument(model.getOptions().getWildcardProjection())).hidden(model.getOptions().isHidden()));
        }
        return new CreateIndexesOperation(Assertions.assertNotNull(this.namespace), indexRequests, this.writeConcern).maxTime(createIndexOptions.getMaxTime(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS).commitQuorum(createIndexOptions.getCommitQuorum());
    }
    
    CreateSearchIndexesOperation createSearchIndexes(final List<SearchIndexModel> indexes) {
        final List<SearchIndexRequest> indexRequests = (List<SearchIndexRequest>)indexes.stream().map(this::createSearchIndexRequest).collect(Collectors.toList());
        return new CreateSearchIndexesOperation(Assertions.assertNotNull(this.namespace), indexRequests);
    }
    
    UpdateSearchIndexesOperation updateSearchIndex(final String indexName, final Bson definition) {
        final BsonDocument definitionDocument = Assertions.assertNotNull(this.toBsonDocument(definition));
        final SearchIndexRequest searchIndexRequest = new SearchIndexRequest(definitionDocument, indexName);
        return new UpdateSearchIndexesOperation(Assertions.assertNotNull(this.namespace), searchIndexRequest);
    }
    
    DropSearchIndexOperation dropSearchIndex(final String indexName) {
        return new DropSearchIndexOperation(Assertions.assertNotNull(this.namespace), indexName);
    }
    
     <TResult> ListSearchIndexesOperation<TResult> listSearchIndexes(final Class<TResult> resultClass, final long maxTimeMS, @Nullable final String indexName, @Nullable final Integer batchSize, @Nullable final Collation collation, @Nullable final BsonValue comment, @Nullable final Boolean allowDiskUse) {
        return new ListSearchIndexesOperation<TResult>(Assertions.assertNotNull(this.namespace), this.codecRegistry.get(resultClass), maxTimeMS, indexName, batchSize, collation, comment, allowDiskUse, this.retryReads);
    }
    
    DropIndexOperation dropIndex(final String indexName, final DropIndexOptions dropIndexOptions) {
        return new DropIndexOperation(Assertions.assertNotNull(this.namespace), indexName, this.writeConcern).maxTime(dropIndexOptions.getMaxTime(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS);
    }
    
    DropIndexOperation dropIndex(final Bson keys, final DropIndexOptions dropIndexOptions) {
        return new DropIndexOperation(Assertions.assertNotNull(this.namespace), keys.toBsonDocument(BsonDocument.class, this.codecRegistry), this.writeConcern).maxTime(dropIndexOptions.getMaxTime(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS);
    }
    
     <TResult> ListCollectionsOperation<TResult> listCollections(final String databaseName, final Class<TResult> resultClass, final Bson filter, final boolean collectionNamesOnly, final boolean authorizedCollections, @Nullable final Integer batchSize, final long maxTimeMS, final BsonValue comment) {
        return new ListCollectionsOperation<TResult>(databaseName, this.codecRegistry.get(resultClass)).retryReads(this.retryReads).filter(this.toBsonDocument(filter)).nameOnly(collectionNamesOnly).authorizedCollections(authorizedCollections).batchSize((batchSize == null) ? 0 : batchSize).maxTime(maxTimeMS, TimeUnit.MILLISECONDS).comment(comment);
    }
    
     <TResult> ListDatabasesOperation<TResult> listDatabases(final Class<TResult> resultClass, final Bson filter, final Boolean nameOnly, final long maxTimeMS, final Boolean authorizedDatabasesOnly, final BsonValue comment) {
        return new ListDatabasesOperation<TResult>(this.codecRegistry.get(resultClass)).maxTime(maxTimeMS, TimeUnit.MILLISECONDS).retryReads(this.retryReads).filter(this.toBsonDocument(filter)).nameOnly(nameOnly).authorizedDatabasesOnly(authorizedDatabasesOnly).comment(comment);
    }
    
     <TResult> ListIndexesOperation<TResult> listIndexes(final Class<TResult> resultClass, @Nullable final Integer batchSize, final long maxTimeMS, final BsonValue comment) {
        return new ListIndexesOperation<TResult>(Assertions.assertNotNull(this.namespace), this.codecRegistry.get(resultClass)).retryReads(this.retryReads).batchSize((batchSize == null) ? 0 : batchSize).maxTime(maxTimeMS, TimeUnit.MILLISECONDS).comment(comment);
    }
    
     <TResult> ChangeStreamOperation<TResult> changeStream(final FullDocument fullDocument, final FullDocumentBeforeChange fullDocumentBeforeChange, final List<? extends Bson> pipeline, final Decoder<TResult> decoder, final ChangeStreamLevel changeStreamLevel, @Nullable final Integer batchSize, final Collation collation, final BsonValue comment, final long maxAwaitTimeMS, final BsonDocument resumeToken, final BsonTimestamp startAtOperationTime, final BsonDocument startAfter, final boolean showExpandedEvents) {
        return new ChangeStreamOperation<TResult>(Assertions.assertNotNull(this.namespace), fullDocument, fullDocumentBeforeChange, Assertions.assertNotNull(this.toBsonDocumentList(pipeline)), decoder, changeStreamLevel).batchSize(batchSize).collation(collation).comment(comment).maxAwaitTime(maxAwaitTimeMS, TimeUnit.MILLISECONDS).resumeAfter(resumeToken).startAtOperationTime(startAtOperationTime).startAfter(startAfter).showExpandedEvents(showExpandedEvents).retryReads(this.retryReads);
    }
    
    private Codec<TDocument> getCodec() {
        return this.codecRegistry.get(this.documentClass);
    }
    
    private BsonDocument documentToBsonDocument(final TDocument document) {
        if (document instanceof BsonDocument) {
            return (BsonDocument)document;
        }
        return new BsonDocumentWrapper<Object>(document, this.getCodec());
    }
    
    @Nullable
    private BsonDocument toBsonDocument(@Nullable final Bson bson) {
        return (bson == null) ? null : bson.toBsonDocument(this.documentClass, this.codecRegistry);
    }
    
    @Nullable
    private List<BsonDocument> toBsonDocumentList(@Nullable final List<? extends Bson> bsonList) {
        if (bsonList == null) {
            return null;
        }
        final List<BsonDocument> bsonDocumentList = (List<BsonDocument>)new ArrayList(bsonList.size());
        for (final Bson cur : bsonList) {
            if (cur == null) {
                throw new IllegalArgumentException("All documents in the list must be non-null");
            }
            bsonDocumentList.add((Object)this.toBsonDocument(cur));
        }
        return bsonDocumentList;
    }
    
    private SearchIndexRequest createSearchIndexRequest(final SearchIndexModel model) {
        final BsonDocument definition = Assertions.assertNotNull(this.toBsonDocument(model.getDefinition()));
        final String indexName = model.getName();
        final SearchIndexRequest indexRequest = new SearchIndexRequest(definition, indexName);
        return indexRequest;
    }
}
