package cc.dreamcode.antylogout.libs.com.mongodb.internal.operation;

import cc.dreamcode.antylogout.libs.com.mongodb.internal.validator.NoOpFieldNameValidator;
import cc.dreamcode.antylogout.libs.org.bson.codecs.configuration.CodecRegistries;
import cc.dreamcode.antylogout.libs.org.bson.codecs.BsonValueCodecProvider;
import cc.dreamcode.antylogout.libs.org.bson.codecs.configuration.CodecProvider;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.bulk.DeleteRequest;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.bulk.UpdateRequest;
import cc.dreamcode.antylogout.libs.com.mongodb.bulk.WriteConcernError;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoInternalException;
import cc.dreamcode.antylogout.libs.org.bson.BsonNumber;
import cc.dreamcode.antylogout.libs.org.bson.BsonInt32;
import java.util.Iterator;
import cc.dreamcode.antylogout.libs.org.bson.BsonArray;
import java.util.stream.Collectors;
import cc.dreamcode.antylogout.libs.com.mongodb.bulk.BulkWriteError;
import java.util.Set;
import cc.dreamcode.antylogout.libs.com.mongodb.bulk.BulkWriteUpsert;
import cc.dreamcode.antylogout.libs.com.mongodb.bulk.BulkWriteInsert;
import java.util.Map;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.validator.MappedFieldNameValidator;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.validator.UpdateFieldNameValidator;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.validator.ReplacingDocumentFieldNameValidator;
import java.util.HashMap;
import cc.dreamcode.antylogout.libs.com.mongodb.bulk.BulkWriteResult;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoBulkWriteException;
import cc.dreamcode.antylogout.libs.com.mongodb.assertions.Assertions;
import cc.dreamcode.antylogout.libs.org.bson.BsonInt64;
import cc.dreamcode.antylogout.libs.org.bson.BsonBoolean;
import cc.dreamcode.antylogout.libs.org.bson.BsonString;
import java.util.Collection;
import java.util.ArrayList;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoClientException;
import cc.dreamcode.antylogout.libs.com.mongodb.lang.Nullable;
import cc.dreamcode.antylogout.libs.org.bson.BsonValue;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.session.SessionContext;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.bulk.WriteRequestWithIndex;
import java.util.List;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.SplittablePayload;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.bulk.WriteRequest;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.IndexMap;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.BulkWriteBatchCombiner;
import cc.dreamcode.antylogout.libs.com.mongodb.WriteConcern;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.ConnectionDescription;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoNamespace;
import cc.dreamcode.antylogout.libs.org.bson.FieldNameValidator;
import cc.dreamcode.antylogout.libs.org.bson.BsonDocument;
import cc.dreamcode.antylogout.libs.org.bson.codecs.Decoder;
import cc.dreamcode.antylogout.libs.org.bson.codecs.configuration.CodecRegistry;

public final class BulkWriteBatch
{
    private static final CodecRegistry REGISTRY;
    private static final Decoder<BsonDocument> DECODER;
    private static final FieldNameValidator NO_OP_FIELD_NAME_VALIDATOR;
    private final MongoNamespace namespace;
    private final ConnectionDescription connectionDescription;
    private final boolean ordered;
    private final WriteConcern writeConcern;
    private final Boolean bypassDocumentValidation;
    private final boolean retryWrites;
    private final BulkWriteBatchCombiner bulkWriteBatchCombiner;
    private final IndexMap indexMap;
    private final WriteRequest.Type batchType;
    private final BsonDocument command;
    private final SplittablePayload payload;
    private final List<WriteRequestWithIndex> unprocessed;
    private final SessionContext sessionContext;
    private final BsonValue comment;
    private final BsonDocument variables;
    
    static BulkWriteBatch createBulkWriteBatch(final MongoNamespace namespace, final ConnectionDescription connectionDescription, final boolean ordered, final WriteConcern writeConcern, final Boolean bypassDocumentValidation, final boolean retryWrites, final List<? extends WriteRequest> writeRequests, final SessionContext sessionContext, @Nullable final BsonValue comment, @Nullable final BsonDocument variables) {
        if (sessionContext.hasSession() && !sessionContext.isImplicitSession() && !sessionContext.hasActiveTransaction() && !writeConcern.isAcknowledged()) {
            throw new MongoClientException("Unacknowledged writes are not supported when using an explicit session");
        }
        boolean canRetryWrites = OperationHelper.isRetryableWrite(retryWrites, writeConcern, connectionDescription, sessionContext);
        final List<WriteRequestWithIndex> writeRequestsWithIndex = (List<WriteRequestWithIndex>)new ArrayList();
        boolean writeRequestsAreRetryable = true;
        for (int i = 0; i < writeRequests.size(); ++i) {
            final WriteRequest writeRequest = (WriteRequest)writeRequests.get(i);
            writeRequestsAreRetryable = (writeRequestsAreRetryable && isRetryable(writeRequest));
            writeRequestsWithIndex.add((Object)new WriteRequestWithIndex(writeRequest, i));
        }
        if (canRetryWrites && !writeRequestsAreRetryable) {
            canRetryWrites = false;
            OperationHelper.LOGGER.debug("retryWrites set but one or more writeRequests do not support retryable writes");
        }
        return new BulkWriteBatch(namespace, connectionDescription, ordered, writeConcern, bypassDocumentValidation, canRetryWrites, new BulkWriteBatchCombiner(connectionDescription.getServerAddress(), ordered, writeConcern), writeRequestsWithIndex, sessionContext, comment, variables);
    }
    
    private BulkWriteBatch(final MongoNamespace namespace, final ConnectionDescription connectionDescription, final boolean ordered, final WriteConcern writeConcern, @Nullable final Boolean bypassDocumentValidation, final boolean retryWrites, final BulkWriteBatchCombiner bulkWriteBatchCombiner, final List<WriteRequestWithIndex> writeRequestsWithIndices, final SessionContext sessionContext, @Nullable final BsonValue comment, @Nullable final BsonDocument variables) {
        this.namespace = namespace;
        this.connectionDescription = connectionDescription;
        this.ordered = ordered;
        this.writeConcern = writeConcern;
        this.bypassDocumentValidation = bypassDocumentValidation;
        this.bulkWriteBatchCombiner = bulkWriteBatchCombiner;
        this.batchType = (writeRequestsWithIndices.isEmpty() ? WriteRequest.Type.INSERT : ((WriteRequestWithIndex)writeRequestsWithIndices.get(0)).getType());
        this.retryWrites = retryWrites;
        final List<WriteRequestWithIndex> payloadItems = (List<WriteRequestWithIndex>)new ArrayList();
        final List<WriteRequestWithIndex> unprocessedItems = (List<WriteRequestWithIndex>)new ArrayList();
        IndexMap indexMap = IndexMap.create();
        for (int i = 0; i < writeRequestsWithIndices.size(); ++i) {
            final WriteRequestWithIndex writeRequestWithIndex = (WriteRequestWithIndex)writeRequestsWithIndices.get(i);
            if (writeRequestWithIndex.getType() != this.batchType) {
                if (ordered) {
                    unprocessedItems.addAll((Collection)writeRequestsWithIndices.subList(i, writeRequestsWithIndices.size()));
                    break;
                }
                unprocessedItems.add((Object)writeRequestWithIndex);
            }
            else {
                indexMap = indexMap.add(payloadItems.size(), writeRequestWithIndex.getIndex());
                payloadItems.add((Object)writeRequestWithIndex);
            }
        }
        this.indexMap = indexMap;
        this.unprocessed = unprocessedItems;
        this.payload = new SplittablePayload(this.getPayloadType(this.batchType), payloadItems);
        this.sessionContext = sessionContext;
        this.comment = comment;
        this.variables = variables;
        this.command = new BsonDocument();
        if (!payloadItems.isEmpty()) {
            this.command.put(this.getCommandName(this.batchType), new BsonString(namespace.getCollectionName()));
            this.command.put("ordered", new BsonBoolean(ordered));
            if (!writeConcern.isServerDefault() && !sessionContext.hasActiveTransaction()) {
                this.command.put("writeConcern", writeConcern.asDocument());
            }
            if (bypassDocumentValidation != null) {
                this.command.put("bypassDocumentValidation", new BsonBoolean(bypassDocumentValidation));
            }
            DocumentHelper.putIfNotNull(this.command, "comment", comment);
            DocumentHelper.putIfNotNull(this.command, "let", variables);
            if (retryWrites) {
                this.command.put("txnNumber", new BsonInt64(sessionContext.advanceTransactionNumber()));
            }
        }
    }
    
    private BulkWriteBatch(final MongoNamespace namespace, final ConnectionDescription connectionDescription, final boolean ordered, final WriteConcern writeConcern, final Boolean bypassDocumentValidation, final boolean retryWrites, final BulkWriteBatchCombiner bulkWriteBatchCombiner, final IndexMap indexMap, final WriteRequest.Type batchType, final BsonDocument command, final SplittablePayload payload, final List<WriteRequestWithIndex> unprocessed, final SessionContext sessionContext, @Nullable final BsonValue comment, @Nullable final BsonDocument variables) {
        this.namespace = namespace;
        this.connectionDescription = connectionDescription;
        this.ordered = ordered;
        this.writeConcern = writeConcern;
        this.bypassDocumentValidation = bypassDocumentValidation;
        this.bulkWriteBatchCombiner = bulkWriteBatchCombiner;
        this.indexMap = indexMap;
        this.batchType = batchType;
        this.payload = payload;
        this.unprocessed = unprocessed;
        this.retryWrites = retryWrites;
        this.sessionContext = sessionContext;
        this.comment = comment;
        this.variables = variables;
        if (retryWrites) {
            command.put("txnNumber", new BsonInt64(sessionContext.advanceTransactionNumber()));
        }
        this.command = command;
    }
    
    void addResult(@Nullable final BsonDocument result) {
        if (this.writeConcern.isAcknowledged()) {
            if (this.hasError(Assertions.assertNotNull(result))) {
                final MongoBulkWriteException bulkWriteException = this.getBulkWriteException(result);
                this.bulkWriteBatchCombiner.addErrorResult(bulkWriteException, this.indexMap);
            }
            else {
                this.bulkWriteBatchCombiner.addResult(this.getBulkWriteResult(result));
            }
        }
    }
    
    boolean getRetryWrites() {
        return this.retryWrites;
    }
    
    BsonDocument getCommand() {
        return this.command;
    }
    
    SplittablePayload getPayload() {
        return this.payload;
    }
    
    Decoder<BsonDocument> getDecoder() {
        return BulkWriteBatch.DECODER;
    }
    
    BulkWriteResult getResult() {
        return this.bulkWriteBatchCombiner.getResult();
    }
    
    boolean hasErrors() {
        return this.bulkWriteBatchCombiner.hasErrors();
    }
    
    @Nullable
    MongoBulkWriteException getError() {
        return this.bulkWriteBatchCombiner.getError();
    }
    
    boolean shouldProcessBatch() {
        return !this.bulkWriteBatchCombiner.shouldStopSendingMoreBatches() && !this.payload.isEmpty();
    }
    
    boolean hasAnotherBatch() {
        return !this.unprocessed.isEmpty();
    }
    
    BulkWriteBatch getNextBatch() {
        if (this.payload.hasAnotherSplit()) {
            IndexMap nextIndexMap = IndexMap.create();
            int newIndex = 0;
            for (int i = this.payload.getPosition(); i < this.payload.size(); ++i) {
                nextIndexMap = nextIndexMap.add(newIndex, this.indexMap.map(i));
                ++newIndex;
            }
            return new BulkWriteBatch(this.namespace, this.connectionDescription, this.ordered, this.writeConcern, this.bypassDocumentValidation, this.retryWrites, this.bulkWriteBatchCombiner, nextIndexMap, this.batchType, this.command, this.payload.getNextSplit(), this.unprocessed, this.sessionContext, this.comment, this.variables);
        }
        return new BulkWriteBatch(this.namespace, this.connectionDescription, this.ordered, this.writeConcern, this.bypassDocumentValidation, this.retryWrites, this.bulkWriteBatchCombiner, this.unprocessed, this.sessionContext, this.comment, this.variables);
    }
    
    FieldNameValidator getFieldNameValidator() {
        if (this.batchType == WriteRequest.Type.UPDATE || this.batchType == WriteRequest.Type.REPLACE) {
            final Map<String, FieldNameValidator> rootMap = (Map<String, FieldNameValidator>)new HashMap();
            if (this.batchType == WriteRequest.Type.REPLACE) {
                rootMap.put((Object)"u", (Object)new ReplacingDocumentFieldNameValidator());
            }
            else {
                rootMap.put((Object)"u", (Object)new UpdateFieldNameValidator());
            }
            return new MappedFieldNameValidator(BulkWriteBatch.NO_OP_FIELD_NAME_VALIDATOR, rootMap);
        }
        return BulkWriteBatch.NO_OP_FIELD_NAME_VALIDATOR;
    }
    
    private BulkWriteResult getBulkWriteResult(final BsonDocument result) {
        final int count = result.getNumber("n").intValue();
        final List<BulkWriteInsert> insertedItems = this.getInsertedItems(result);
        final List<BulkWriteUpsert> upsertedItems = this.getUpsertedItems(result);
        return BulkWriteResult.acknowledged(this.batchType, count - upsertedItems.size(), this.getModifiedCount(result), upsertedItems, insertedItems);
    }
    
    private List<BulkWriteInsert> getInsertedItems(final BsonDocument result) {
        final Set<Integer> writeErrors = (Set<Integer>)this.getWriteErrors(result).stream().map(BulkWriteError::getIndex).collect(Collectors.toSet());
        return (List<BulkWriteInsert>)this.payload.getInsertedIds().entrySet().stream().filter(entry -> !writeErrors.contains(entry.getKey())).map(entry -> new BulkWriteInsert((int)entry.getKey(), (BsonValue)entry.getValue())).collect(Collectors.toList());
    }
    
    private List<BulkWriteUpsert> getUpsertedItems(final BsonDocument result) {
        final BsonArray upsertedValue = result.getArray("upserted", new BsonArray());
        final List<BulkWriteUpsert> bulkWriteUpsertList = (List<BulkWriteUpsert>)new ArrayList();
        for (final BsonValue upsertedItem : upsertedValue) {
            final BsonDocument upsertedItemDocument = (BsonDocument)upsertedItem;
            bulkWriteUpsertList.add((Object)new BulkWriteUpsert(this.indexMap.map(upsertedItemDocument.getNumber("index").intValue()), upsertedItemDocument.get("_id")));
        }
        return bulkWriteUpsertList;
    }
    
    private int getModifiedCount(final BsonDocument result) {
        return result.getNumber("nModified", new BsonInt32(0)).intValue();
    }
    
    private boolean hasError(final BsonDocument result) {
        return result.get("writeErrors") != null || result.get("writeConcernError") != null;
    }
    
    private MongoBulkWriteException getBulkWriteException(final BsonDocument result) {
        if (!this.hasError(result)) {
            throw new MongoInternalException("This method should not have been called");
        }
        return new MongoBulkWriteException(this.getBulkWriteResult(result), this.getWriteErrors(result), this.getWriteConcernError(result), this.connectionDescription.getServerAddress(), (Set<String>)result.getArray("errorLabels", new BsonArray()).stream().map(i -> i.asString().getValue()).collect(Collectors.toSet()));
    }
    
    private List<BulkWriteError> getWriteErrors(final BsonDocument result) {
        final List<BulkWriteError> writeErrors = (List<BulkWriteError>)new ArrayList();
        final BsonArray writeErrorsDocuments = (BsonArray)result.get("writeErrors");
        if (writeErrorsDocuments != null) {
            for (final BsonValue cur : writeErrorsDocuments) {
                final BsonDocument curDocument = (BsonDocument)cur;
                writeErrors.add((Object)new BulkWriteError(curDocument.getNumber("code").intValue(), curDocument.getString("errmsg").getValue(), curDocument.getDocument("errInfo", new BsonDocument()), curDocument.getNumber("index").intValue()));
            }
        }
        return writeErrors;
    }
    
    @Nullable
    private WriteConcernError getWriteConcernError(final BsonDocument result) {
        final BsonDocument writeConcernErrorDocument = (BsonDocument)result.get("writeConcernError");
        if (writeConcernErrorDocument == null) {
            return null;
        }
        return WriteConcernHelper.createWriteConcernError(writeConcernErrorDocument);
    }
    
    private String getCommandName(final WriteRequest.Type batchType) {
        if (batchType == WriteRequest.Type.INSERT) {
            return "insert";
        }
        if (batchType == WriteRequest.Type.UPDATE || batchType == WriteRequest.Type.REPLACE) {
            return "update";
        }
        return "delete";
    }
    
    private SplittablePayload.Type getPayloadType(final WriteRequest.Type batchType) {
        if (batchType == WriteRequest.Type.INSERT) {
            return SplittablePayload.Type.INSERT;
        }
        if (batchType == WriteRequest.Type.UPDATE) {
            return SplittablePayload.Type.UPDATE;
        }
        if (batchType == WriteRequest.Type.REPLACE) {
            return SplittablePayload.Type.REPLACE;
        }
        return SplittablePayload.Type.DELETE;
    }
    
    private static boolean isRetryable(final WriteRequest writeRequest) {
        if (writeRequest.getType() == WriteRequest.Type.UPDATE || writeRequest.getType() == WriteRequest.Type.REPLACE) {
            return !((UpdateRequest)writeRequest).isMulti();
        }
        return writeRequest.getType() != WriteRequest.Type.DELETE || !((DeleteRequest)writeRequest).isMulti();
    }
    
    static {
        REGISTRY = CodecRegistries.fromProviders(new BsonValueCodecProvider());
        DECODER = BulkWriteBatch.REGISTRY.get(BsonDocument.class);
        NO_OP_FIELD_NAME_VALIDATOR = new NoOpFieldNameValidator();
    }
}
