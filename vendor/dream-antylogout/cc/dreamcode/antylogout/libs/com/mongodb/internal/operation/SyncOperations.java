package cc.dreamcode.antylogout.libs.com.mongodb.internal.operation;

import cc.dreamcode.antylogout.libs.org.bson.BsonTimestamp;
import cc.dreamcode.antylogout.libs.org.bson.BsonDocument;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.client.model.changestream.ChangeStreamLevel;
import cc.dreamcode.antylogout.libs.org.bson.codecs.Decoder;
import cc.dreamcode.antylogout.libs.com.mongodb.client.model.changestream.FullDocumentBeforeChange;
import cc.dreamcode.antylogout.libs.com.mongodb.client.model.changestream.FullDocument;
import cc.dreamcode.antylogout.libs.com.mongodb.client.model.DropIndexOptions;
import cc.dreamcode.antylogout.libs.com.mongodb.client.model.SearchIndexModel;
import cc.dreamcode.antylogout.libs.com.mongodb.client.model.CreateIndexOptions;
import cc.dreamcode.antylogout.libs.com.mongodb.client.model.IndexModel;
import cc.dreamcode.antylogout.libs.com.mongodb.client.model.CreateViewOptions;
import cc.dreamcode.antylogout.libs.com.mongodb.client.model.RenameCollectionOptions;
import cc.dreamcode.antylogout.libs.com.mongodb.client.model.DropCollectionOptions;
import cc.dreamcode.antylogout.libs.com.mongodb.AutoEncryptionSettings;
import cc.dreamcode.antylogout.libs.com.mongodb.client.model.CreateCollectionOptions;
import cc.dreamcode.antylogout.libs.com.mongodb.client.model.BulkWriteOptions;
import cc.dreamcode.antylogout.libs.com.mongodb.client.model.WriteModel;
import cc.dreamcode.antylogout.libs.com.mongodb.client.model.InsertManyOptions;
import cc.dreamcode.antylogout.libs.com.mongodb.client.model.UpdateOptions;
import cc.dreamcode.antylogout.libs.com.mongodb.client.model.DeleteOptions;
import cc.dreamcode.antylogout.libs.com.mongodb.client.model.ReplaceOptions;
import cc.dreamcode.antylogout.libs.com.mongodb.bulk.BulkWriteResult;
import cc.dreamcode.antylogout.libs.com.mongodb.client.model.InsertOneOptions;
import cc.dreamcode.antylogout.libs.com.mongodb.client.model.FindOneAndUpdateOptions;
import cc.dreamcode.antylogout.libs.com.mongodb.client.model.FindOneAndReplaceOptions;
import cc.dreamcode.antylogout.libs.com.mongodb.client.model.FindOneAndDeleteOptions;
import cc.dreamcode.antylogout.libs.com.mongodb.client.model.MapReduceAction;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.client.model.AggregationLevel;
import java.util.List;
import cc.dreamcode.antylogout.libs.org.bson.BsonValue;
import cc.dreamcode.antylogout.libs.com.mongodb.client.model.Collation;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.client.model.FindOptions;
import cc.dreamcode.antylogout.libs.com.mongodb.client.model.EstimatedDocumentCountOptions;
import cc.dreamcode.antylogout.libs.com.mongodb.client.model.CountOptions;
import cc.dreamcode.antylogout.libs.org.bson.conversions.Bson;
import cc.dreamcode.antylogout.libs.com.mongodb.lang.Nullable;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoNamespace;
import cc.dreamcode.antylogout.libs.com.mongodb.WriteConcern;
import cc.dreamcode.antylogout.libs.com.mongodb.ReadConcern;
import cc.dreamcode.antylogout.libs.org.bson.codecs.configuration.CodecRegistry;
import cc.dreamcode.antylogout.libs.com.mongodb.ReadPreference;

public final class SyncOperations<TDocument>
{
    private final Operations<TDocument> operations;
    
    public SyncOperations(final Class<TDocument> documentClass, final ReadPreference readPreference, final CodecRegistry codecRegistry, final boolean retryReads) {
        this(null, documentClass, readPreference, codecRegistry, ReadConcern.DEFAULT, WriteConcern.ACKNOWLEDGED, true, retryReads);
    }
    
    public SyncOperations(final MongoNamespace namespace, final Class<TDocument> documentClass, final ReadPreference readPreference, final CodecRegistry codecRegistry, final boolean retryReads) {
        this(namespace, documentClass, readPreference, codecRegistry, ReadConcern.DEFAULT, WriteConcern.ACKNOWLEDGED, true, retryReads);
    }
    
    public SyncOperations(@Nullable final MongoNamespace namespace, final Class<TDocument> documentClass, final ReadPreference readPreference, final CodecRegistry codecRegistry, final ReadConcern readConcern, final WriteConcern writeConcern, final boolean retryWrites, final boolean retryReads) {
        this.operations = new Operations<TDocument>(namespace, documentClass, readPreference, codecRegistry, readConcern, writeConcern, retryWrites, retryReads);
    }
    
    public ReadOperation<Long> countDocuments(final Bson filter, final CountOptions options) {
        return this.operations.countDocuments(filter, options);
    }
    
    public ReadOperation<Long> estimatedDocumentCount(final EstimatedDocumentCountOptions options) {
        return this.operations.estimatedDocumentCount(options);
    }
    
    public <TResult> ReadOperation<BatchCursor<TResult>> findFirst(final Bson filter, final Class<TResult> resultClass, final FindOptions options) {
        return this.operations.findFirst(filter, resultClass, options);
    }
    
    public <TResult> ExplainableReadOperation<BatchCursor<TResult>> find(final Bson filter, final Class<TResult> resultClass, final FindOptions options) {
        return this.operations.find(filter, resultClass, options);
    }
    
    public <TResult> ReadOperation<BatchCursor<TResult>> find(final MongoNamespace findNamespace, final Bson filter, final Class<TResult> resultClass, final FindOptions options) {
        return this.operations.find(findNamespace, filter, resultClass, options);
    }
    
    public <TResult> ReadOperation<BatchCursor<TResult>> distinct(final String fieldName, final Bson filter, final Class<TResult> resultClass, final long maxTimeMS, final Collation collation, final BsonValue comment) {
        return this.operations.distinct(fieldName, filter, resultClass, maxTimeMS, collation, comment);
    }
    
    public <TResult> ExplainableReadOperation<BatchCursor<TResult>> aggregate(final List<? extends Bson> pipeline, final Class<TResult> resultClass, final long maxTimeMS, final long maxAwaitTimeMS, @Nullable final Integer batchSize, final Collation collation, final Bson hint, final String hintString, final BsonValue comment, final Bson variables, final Boolean allowDiskUse, final AggregationLevel aggregationLevel) {
        return this.operations.aggregate(pipeline, resultClass, maxTimeMS, maxAwaitTimeMS, batchSize, collation, hint, hintString, comment, variables, allowDiskUse, aggregationLevel);
    }
    
    public ReadOperation<Void> aggregateToCollection(final List<? extends Bson> pipeline, final long maxTimeMS, final Boolean allowDiskUse, final Boolean bypassDocumentValidation, final Collation collation, final Bson hint, final String hintString, final BsonValue comment, final Bson variables, final AggregationLevel aggregationLevel) {
        return this.operations.aggregateToCollection(pipeline, maxTimeMS, allowDiskUse, bypassDocumentValidation, collation, hint, hintString, comment, variables, aggregationLevel);
    }
    
    public WriteOperation<MapReduceStatistics> mapReduceToCollection(final String databaseName, final String collectionName, final String mapFunction, final String reduceFunction, final String finalizeFunction, final Bson filter, final int limit, final long maxTimeMS, final boolean jsMode, final Bson scope, final Bson sort, final boolean verbose, final MapReduceAction action, final Boolean bypassDocumentValidation, final Collation collation) {
        return this.operations.mapReduceToCollection(databaseName, collectionName, mapFunction, reduceFunction, finalizeFunction, filter, limit, maxTimeMS, jsMode, scope, sort, verbose, action, bypassDocumentValidation, collation);
    }
    
    public <TResult> ReadOperation<MapReduceBatchCursor<TResult>> mapReduce(final String mapFunction, final String reduceFunction, final String finalizeFunction, final Class<TResult> resultClass, final Bson filter, final int limit, final long maxTimeMS, final boolean jsMode, final Bson scope, final Bson sort, final boolean verbose, final Collation collation) {
        return this.operations.mapReduce(mapFunction, reduceFunction, finalizeFunction, resultClass, filter, limit, maxTimeMS, jsMode, scope, sort, verbose, collation);
    }
    
    public WriteOperation<TDocument> findOneAndDelete(final Bson filter, final FindOneAndDeleteOptions options) {
        return this.operations.findOneAndDelete(filter, options);
    }
    
    public WriteOperation<TDocument> findOneAndReplace(final Bson filter, final TDocument replacement, final FindOneAndReplaceOptions options) {
        return this.operations.findOneAndReplace(filter, replacement, options);
    }
    
    public WriteOperation<TDocument> findOneAndUpdate(final Bson filter, final Bson update, final FindOneAndUpdateOptions options) {
        return this.operations.findOneAndUpdate(filter, update, options);
    }
    
    public WriteOperation<TDocument> findOneAndUpdate(final Bson filter, final List<? extends Bson> update, final FindOneAndUpdateOptions options) {
        return this.operations.findOneAndUpdate(filter, update, options);
    }
    
    public WriteOperation<BulkWriteResult> insertOne(final TDocument document, final InsertOneOptions options) {
        return this.operations.insertOne(document, options);
    }
    
    public WriteOperation<BulkWriteResult> replaceOne(final Bson filter, final TDocument replacement, final ReplaceOptions options) {
        return this.operations.replaceOne(filter, replacement, options);
    }
    
    public WriteOperation<BulkWriteResult> deleteOne(final Bson filter, final DeleteOptions options) {
        return this.operations.deleteOne(filter, options);
    }
    
    public WriteOperation<BulkWriteResult> deleteMany(final Bson filter, final DeleteOptions options) {
        return this.operations.deleteMany(filter, options);
    }
    
    public WriteOperation<BulkWriteResult> updateOne(final Bson filter, final Bson update, final UpdateOptions updateOptions) {
        return this.operations.updateOne(filter, update, updateOptions);
    }
    
    public WriteOperation<BulkWriteResult> updateOne(final Bson filter, final List<? extends Bson> update, final UpdateOptions updateOptions) {
        return this.operations.updateOne(filter, update, updateOptions);
    }
    
    public WriteOperation<BulkWriteResult> updateMany(final Bson filter, final Bson update, final UpdateOptions updateOptions) {
        return this.operations.updateMany(filter, update, updateOptions);
    }
    
    public WriteOperation<BulkWriteResult> updateMany(final Bson filter, final List<? extends Bson> update, final UpdateOptions updateOptions) {
        return this.operations.updateMany(filter, update, updateOptions);
    }
    
    public WriteOperation<BulkWriteResult> insertMany(final List<? extends TDocument> documents, final InsertManyOptions options) {
        return this.operations.insertMany(documents, options);
    }
    
    public WriteOperation<BulkWriteResult> bulkWrite(final List<? extends WriteModel<? extends TDocument>> requests, final BulkWriteOptions options) {
        return this.operations.bulkWrite(requests, options);
    }
    
    public <TResult> ReadOperation<TResult> commandRead(final Bson command, final Class<TResult> resultClass) {
        return this.operations.commandRead(command, resultClass);
    }
    
    public WriteOperation<Void> dropDatabase() {
        return this.operations.dropDatabase();
    }
    
    public WriteOperation<Void> createCollection(final String collectionName, final CreateCollectionOptions createCollectionOptions, @Nullable final AutoEncryptionSettings autoEncryptionSettings) {
        return this.operations.createCollection(collectionName, createCollectionOptions, autoEncryptionSettings);
    }
    
    public WriteOperation<Void> dropCollection(final DropCollectionOptions dropCollectionOptions, @Nullable final AutoEncryptionSettings autoEncryptionSettings) {
        return this.operations.dropCollection(dropCollectionOptions, autoEncryptionSettings);
    }
    
    public WriteOperation<Void> renameCollection(final MongoNamespace newCollectionNamespace, final RenameCollectionOptions options) {
        return this.operations.renameCollection(newCollectionNamespace, options);
    }
    
    public WriteOperation<Void> createView(final String viewName, final String viewOn, final List<? extends Bson> pipeline, final CreateViewOptions createViewOptions) {
        return this.operations.createView(viewName, viewOn, pipeline, createViewOptions);
    }
    
    public WriteOperation<Void> createIndexes(final List<IndexModel> indexes, final CreateIndexOptions options) {
        return this.operations.createIndexes(indexes, options);
    }
    
    public WriteOperation<Void> createSearchIndexes(final List<SearchIndexModel> indexes) {
        return this.operations.createSearchIndexes(indexes);
    }
    
    public WriteOperation<Void> updateSearchIndex(final String indexName, final Bson definition) {
        return this.operations.updateSearchIndex(indexName, definition);
    }
    
    public WriteOperation<Void> dropSearchIndex(final String indexName) {
        return this.operations.dropSearchIndex(indexName);
    }
    
    public <TResult> ExplainableReadOperation<BatchCursor<TResult>> listSearchIndexes(final Class<TResult> resultClass, final long maxTimeMS, @Nullable final String indexName, @Nullable final Integer batchSize, @Nullable final Collation collation, @Nullable final BsonValue comment, @Nullable final Boolean allowDiskUse) {
        return this.operations.listSearchIndexes(resultClass, maxTimeMS, indexName, batchSize, collation, comment, allowDiskUse);
    }
    
    public WriteOperation<Void> dropIndex(final String indexName, final DropIndexOptions options) {
        return this.operations.dropIndex(indexName, options);
    }
    
    public WriteOperation<Void> dropIndex(final Bson keys, final DropIndexOptions options) {
        return this.operations.dropIndex(keys, options);
    }
    
    public <TResult> ReadOperation<BatchCursor<TResult>> listCollections(final String databaseName, final Class<TResult> resultClass, final Bson filter, final boolean collectionNamesOnly, final boolean authorizedCollections, @Nullable final Integer batchSize, final long maxTimeMS, final BsonValue comment) {
        return this.operations.listCollections(databaseName, resultClass, filter, collectionNamesOnly, authorizedCollections, batchSize, maxTimeMS, comment);
    }
    
    public <TResult> ReadOperation<BatchCursor<TResult>> listDatabases(final Class<TResult> resultClass, final Bson filter, final Boolean nameOnly, final long maxTimeMS, final Boolean authorizedDatabases, final BsonValue comment) {
        return this.operations.listDatabases(resultClass, filter, nameOnly, maxTimeMS, authorizedDatabases, comment);
    }
    
    public <TResult> ReadOperation<BatchCursor<TResult>> listIndexes(final Class<TResult> resultClass, @Nullable final Integer batchSize, final long maxTimeMS, final BsonValue comment) {
        return this.operations.listIndexes(resultClass, batchSize, maxTimeMS, comment);
    }
    
    public <TResult> ReadOperation<BatchCursor<TResult>> changeStream(final FullDocument fullDocument, final FullDocumentBeforeChange fullDocumentBeforeChange, final List<? extends Bson> pipeline, final Decoder<TResult> decoder, final ChangeStreamLevel changeStreamLevel, @Nullable final Integer batchSize, final Collation collation, final BsonValue comment, final long maxAwaitTimeMS, final BsonDocument resumeToken, final BsonTimestamp startAtOperationTime, final BsonDocument startAfter, final boolean showExpandedEvents) {
        return this.operations.changeStream(fullDocument, fullDocumentBeforeChange, pipeline, decoder, changeStreamLevel, batchSize, collation, comment, maxAwaitTimeMS, resumeToken, startAtOperationTime, startAfter, showExpandedEvents);
    }
}
