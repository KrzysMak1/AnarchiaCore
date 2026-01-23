package cc.dreamcode.antylogout.libs.com.mongodb.internal.operation;

import java.util.Optional;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.validator.NoOpFieldNameValidator;
import java.util.function.BiFunction;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.ConnectionSource;
import cc.dreamcode.antylogout.libs.org.bson.BsonArray;
import java.util.stream.Collectors;
import cc.dreamcode.antylogout.libs.org.bson.BsonString;
import java.util.Set;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.BindingContext;
import cc.dreamcode.antylogout.libs.com.mongodb.ReadPreference;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.function.AsyncCallbackRunnable;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.function.AsyncCallbackLoop;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.function.LoopState;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.AsyncConnection;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.MongoWriteConcernWithResponseException;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.ProtocolHelper;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.Connection;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.session.SessionContext;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.ConnectionDescription;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.ErrorHandlingResultCallback;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.AsyncConnectionSource;
import java.util.Objects;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.SingleResultCallback;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.AsyncWriteBinding;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoException;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.WriteBinding;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.operation.retry.AttachmentKeys;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.function.RetryingAsyncCallbackSupplier;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.function.AsyncCallbackSupplier;
import java.util.function.BiPredicate;
import java.util.function.BinaryOperator;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.function.RetryingSyncSupplier;
import java.util.function.Supplier;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.OperationContext;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.function.RetryState;
import cc.dreamcode.antylogout.libs.com.mongodb.lang.Nullable;
import cc.dreamcode.antylogout.libs.com.mongodb.assertions.Assertions;
import cc.dreamcode.antylogout.libs.org.bson.BsonDocument;
import cc.dreamcode.antylogout.libs.org.bson.BsonValue;
import cc.dreamcode.antylogout.libs.com.mongodb.WriteConcern;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.bulk.WriteRequest;
import java.util.List;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoNamespace;
import cc.dreamcode.antylogout.libs.org.bson.FieldNameValidator;
import cc.dreamcode.antylogout.libs.com.mongodb.bulk.BulkWriteResult;

public class MixedBulkWriteOperation implements AsyncWriteOperation<BulkWriteResult>, WriteOperation<BulkWriteResult>
{
    private static final FieldNameValidator NO_OP_FIELD_NAME_VALIDATOR;
    private final MongoNamespace namespace;
    private final List<? extends WriteRequest> writeRequests;
    private final boolean ordered;
    private final boolean retryWrites;
    private final WriteConcern writeConcern;
    private Boolean bypassDocumentValidation;
    private BsonValue comment;
    private BsonDocument variables;
    
    public MixedBulkWriteOperation(final MongoNamespace namespace, final List<? extends WriteRequest> writeRequests, final boolean ordered, final WriteConcern writeConcern, final boolean retryWrites) {
        this.ordered = ordered;
        this.namespace = Assertions.notNull("namespace", namespace);
        this.writeRequests = Assertions.notNull("writes", writeRequests);
        this.writeConcern = Assertions.notNull("writeConcern", writeConcern);
        this.retryWrites = retryWrites;
        Assertions.isTrueArgument("writes is not an empty list", !writeRequests.isEmpty());
    }
    
    public MongoNamespace getNamespace() {
        return this.namespace;
    }
    
    public WriteConcern getWriteConcern() {
        return this.writeConcern;
    }
    
    public boolean isOrdered() {
        return this.ordered;
    }
    
    public List<? extends WriteRequest> getWriteRequests() {
        return this.writeRequests;
    }
    
    public Boolean getBypassDocumentValidation() {
        return this.bypassDocumentValidation;
    }
    
    public MixedBulkWriteOperation bypassDocumentValidation(@Nullable final Boolean bypassDocumentValidation) {
        this.bypassDocumentValidation = bypassDocumentValidation;
        return this;
    }
    
    public BsonValue getComment() {
        return this.comment;
    }
    
    public MixedBulkWriteOperation comment(@Nullable final BsonValue comment) {
        this.comment = comment;
        return this;
    }
    
    public MixedBulkWriteOperation let(@Nullable final BsonDocument variables) {
        this.variables = variables;
        return this;
    }
    
    public Boolean getRetryWrites() {
        return this.retryWrites;
    }
    
    private <R> Supplier<R> decorateWriteWithRetries(final RetryState retryState, final OperationContext operationContext, final Supplier<R> writeFunction) {
        return (Supplier<R>)new RetryingSyncSupplier(retryState, (BinaryOperator<Throwable>)CommandOperationHelper::chooseRetryableWriteException, (BiPredicate<RetryState, Throwable>)this::shouldAttemptToRetryWrite, (java.util.function.Supplier<Object>)(() -> {
            CommandOperationHelper.logRetryExecute(retryState, operationContext);
            return writeFunction.get();
        }));
    }
    
    private <R> AsyncCallbackSupplier<R> decorateWriteWithRetries(final RetryState retryState, final OperationContext operationContext, final AsyncCallbackSupplier<R> writeFunction) {
        return new RetryingAsyncCallbackSupplier<R>(retryState, (BinaryOperator<Throwable>)CommandOperationHelper::chooseRetryableWriteException, (BiPredicate<RetryState, Throwable>)this::shouldAttemptToRetryWrite, callback -> {
            CommandOperationHelper.logRetryExecute(retryState, operationContext);
            writeFunction.get(callback);
        });
    }
    
    private boolean shouldAttemptToRetryWrite(final RetryState retryState, final Throwable attemptFailure) {
        final BulkWriteTracker bulkWriteTracker = (BulkWriteTracker)retryState.attachment(AttachmentKeys.bulkWriteTracker()).orElseThrow(Assertions::fail);
        if (bulkWriteTracker.lastAttempt()) {
            return false;
        }
        final boolean decision = CommandOperationHelper.shouldAttemptToRetryWrite(retryState, attemptFailure);
        if (decision) {
            bulkWriteTracker.advance();
        }
        return decision;
    }
    
    @Override
    public BulkWriteResult execute(final WriteBinding binding) {
        final RetryState retryState = new RetryState();
        BulkWriteTracker.attachNew(retryState, this.retryWrites);
        final Supplier<BulkWriteResult> retryingBulkWrite = this.decorateWriteWithRetries(retryState, binding.getOperationContext(), (java.util.function.Supplier<BulkWriteResult>)(() -> {
            Objects.requireNonNull((Object)binding);
            return SyncOperationHelper.withSourceAndConnection((Supplier<ConnectionSource>)binding::getWriteConnectionSource, true, (java.util.function.BiFunction<ConnectionSource, Connection, BulkWriteResult>)((source, connection) -> {
                final ConnectionDescription connectionDescription = connection.getDescription();
                retryState.attach(AttachmentKeys.maxWireVersion(), connectionDescription.getMaxWireVersion(), true);
                final SessionContext sessionContext = binding.getSessionContext();
                final WriteConcern writeConcern = this.getAppliedWriteConcern(sessionContext);
                if (!OperationHelper.isRetryableWrite(this.retryWrites, this.getAppliedWriteConcern(sessionContext), connectionDescription, sessionContext)) {
                    this.handleMongoWriteConcernWithResponseException(retryState, true);
                }
                OperationHelper.validateWriteRequests(connectionDescription, this.bypassDocumentValidation, this.writeRequests, writeConcern);
                if (!((BulkWriteTracker)retryState.attachment(AttachmentKeys.bulkWriteTracker()).orElseThrow(Assertions::fail)).batch().isPresent()) {
                    BulkWriteTracker.attachNew(retryState, BulkWriteBatch.createBulkWriteBatch(this.namespace, connectionDescription, this.ordered, writeConcern, this.bypassDocumentValidation, this.retryWrites, this.writeRequests, sessionContext, this.comment, this.variables));
                }
                return this.executeBulkWriteBatch(retryState, binding, connection);
            }));
        }));
        try {
            return (BulkWriteResult)retryingBulkWrite.get();
        }
        catch (final MongoException e) {
            throw CommandOperationHelper.transformWriteException(e);
        }
    }
    
    @Override
    public void executeAsync(final AsyncWriteBinding binding, final SingleResultCallback<BulkWriteResult> callback) {
        final RetryState retryState = new RetryState();
        BulkWriteTracker.attachNew(retryState, this.retryWrites);
        binding.retain();
        final AsyncCallbackSupplier<BulkWriteResult> decorateWriteWithRetries = this.decorateWriteWithRetries(retryState, binding.getOperationContext(), funcCallback -> {
            Objects.requireNonNull((Object)binding);
            AsyncOperationHelper.withAsyncSourceAndConnection(binding::getWriteConnectionSource, (boolean)(1 != 0), (SingleResultCallback<Object>)funcCallback, (source, connection, releasingCallback) -> {
                final ConnectionDescription connectionDescription = connection.getDescription();
                retryState.attach(AttachmentKeys.maxWireVersion(), connectionDescription.getMaxWireVersion(), (boolean)(1 != 0));
                final SessionContext sessionContext = binding.getSessionContext();
                final WriteConcern writeConcern = this.getAppliedWriteConcern(sessionContext);
                if (OperationHelper.isRetryableWrite(this.retryWrites, this.getAppliedWriteConcern(sessionContext), connectionDescription, sessionContext) || !this.handleMongoWriteConcernWithResponseExceptionAsync(retryState, releasingCallback)) {
                    if (!OperationHelper.validateWriteRequestsAndCompleteIfInvalid(connectionDescription, this.bypassDocumentValidation, this.writeRequests, writeConcern, (SingleResultCallback<Object>)releasingCallback)) {
                        try {
                            if (!((BulkWriteTracker)retryState.attachment(AttachmentKeys.bulkWriteTracker()).orElseThrow(Assertions::fail)).batch().isPresent()) {
                                BulkWriteTracker.attachNew(retryState, BulkWriteBatch.createBulkWriteBatch(this.namespace, connectionDescription, this.ordered, writeConcern, this.bypassDocumentValidation, this.retryWrites, this.writeRequests, sessionContext, this.comment, this.variables));
                            }
                        }
                        catch (final Throwable t) {
                            releasingCallback.onResult(null, t);
                            return;
                        }
                        this.executeBulkWriteBatchAsync(retryState, binding, connection, releasingCallback);
                    }
                }
            });
            return;
        });
        Objects.requireNonNull((Object)binding);
        final AsyncCallbackSupplier<BulkWriteResult> retryingBulkWrite = decorateWriteWithRetries.whenComplete(binding::release);
        retryingBulkWrite.get(AsyncOperationHelper.exceptionTransformingCallback((SingleResultCallback<BulkWriteResult>)ErrorHandlingResultCallback.errorHandlingCallback((SingleResultCallback<R>)callback, OperationHelper.LOGGER)));
    }
    
    private BulkWriteResult executeBulkWriteBatch(final RetryState retryState, final WriteBinding binding, final Connection connection) {
        BulkWriteTracker currentBulkWriteTracker = (BulkWriteTracker)retryState.attachment(AttachmentKeys.bulkWriteTracker()).orElseThrow(Assertions::fail);
        BulkWriteBatch currentBatch = (BulkWriteBatch)currentBulkWriteTracker.batch().orElseThrow(Assertions::fail);
        final int maxWireVersion = connection.getDescription().getMaxWireVersion();
        while (currentBatch.shouldProcessBatch()) {
            try {
                final BsonDocument result = this.executeCommand(connection, currentBatch, binding);
                if (currentBatch.getRetryWrites() && !binding.getSessionContext().hasActiveTransaction()) {
                    final MongoException writeConcernBasedError = ProtocolHelper.createSpecialException(result, connection.getDescription().getServerAddress(), "errMsg");
                    if (writeConcernBasedError != null) {
                        if (currentBulkWriteTracker.lastAttempt()) {
                            CommandOperationHelper.addRetryableWriteErrorLabel(writeConcernBasedError, maxWireVersion);
                            this.addErrorLabelsToWriteConcern(result.getDocument("writeConcernError"), writeConcernBasedError.getErrorLabels());
                        }
                        else if (CommandOperationHelper.shouldAttemptToRetryWrite(retryState, (Throwable)writeConcernBasedError)) {
                            throw new MongoWriteConcernWithResponseException(writeConcernBasedError, result);
                        }
                    }
                }
                currentBatch.addResult(result);
                currentBulkWriteTracker = BulkWriteTracker.attachNext(retryState, currentBatch);
                currentBatch = (BulkWriteBatch)currentBulkWriteTracker.batch().orElseThrow(Assertions::fail);
                continue;
            }
            catch (final MongoException exception) {
                if (!retryState.isFirstAttempt() && !(exception instanceof MongoWriteConcernWithResponseException)) {
                    CommandOperationHelper.addRetryableWriteErrorLabel(exception, maxWireVersion);
                }
                this.handleMongoWriteConcernWithResponseException(retryState, false);
                throw exception;
            }
            break;
        }
        try {
            return currentBatch.getResult();
        }
        catch (final MongoException e) {
            retryState.markAsLastAttempt();
            throw e;
        }
    }
    
    private void executeBulkWriteBatchAsync(final RetryState retryState, final AsyncWriteBinding binding, final AsyncConnection connection, final SingleResultCallback<BulkWriteResult> callback) {
        final LoopState loopState = new LoopState();
        final AsyncCallbackRunnable loop = new AsyncCallbackLoop(loopState, iterationCallback -> {
            final BulkWriteTracker currentBulkWriteTracker = (BulkWriteTracker)retryState.attachment(AttachmentKeys.bulkWriteTracker()).orElseThrow(Assertions::fail);
            loopState.attach(AttachmentKeys.bulkWriteTracker(), currentBulkWriteTracker, true);
            final BulkWriteBatch currentBatch = (BulkWriteBatch)currentBulkWriteTracker.batch().orElseThrow(Assertions::fail);
            final int maxWireVersion = connection.getDescription().getMaxWireVersion();
            if (loopState.breakAndCompleteIf((Supplier<Boolean>)(() -> !currentBatch.shouldProcessBatch()), iterationCallback)) {
                return;
            }
            else {
                this.executeCommandAsync(binding, connection, currentBatch, (result, t) -> {
                    if (t == null) {
                        if (currentBatch.getRetryWrites() && !binding.getSessionContext().hasActiveTransaction()) {
                            final MongoException writeConcernBasedError = ProtocolHelper.createSpecialException(result, connection.getDescription().getServerAddress(), "errMsg");
                            if (writeConcernBasedError != null) {
                                if (currentBulkWriteTracker.lastAttempt()) {
                                    CommandOperationHelper.addRetryableWriteErrorLabel(writeConcernBasedError, maxWireVersion);
                                    this.addErrorLabelsToWriteConcern(result.getDocument("writeConcernError"), writeConcernBasedError.getErrorLabels());
                                }
                                else if (CommandOperationHelper.shouldAttemptToRetryWrite(retryState, (Throwable)writeConcernBasedError)) {
                                    iterationCallback.onResult(null, (Throwable)new MongoWriteConcernWithResponseException(writeConcernBasedError, result));
                                    return;
                                }
                            }
                        }
                        currentBatch.addResult(result);
                        BulkWriteTracker.attachNext(retryState, currentBatch);
                        iterationCallback.onResult(null, null);
                    }
                    else {
                        if (t instanceof MongoException) {
                            final MongoException exception = (MongoException)t;
                            if (!retryState.isFirstAttempt() && !(exception instanceof MongoWriteConcernWithResponseException)) {
                                CommandOperationHelper.addRetryableWriteErrorLabel(exception, maxWireVersion);
                            }
                            if (this.handleMongoWriteConcernWithResponseExceptionAsync(retryState, null)) {
                                return;
                            }
                        }
                        iterationCallback.onResult(null, t);
                    }
                });
                return;
            }
        });
        loop.run((voidResult, t) -> {
            if (t != null) {
                callback.onResult(null, t);
            }
            else {
                BulkWriteResult result2;
                try {
                    result2 = ((BulkWriteBatch)loopState.attachment(AttachmentKeys.bulkWriteTracker()).flatMap(BulkWriteTracker::batch).orElseThrow(Assertions::fail)).getResult();
                }
                catch (final Throwable loopResultT) {
                    if (loopResultT instanceof MongoException) {
                        retryState.markAsLastAttempt();
                    }
                    callback.onResult(null, loopResultT);
                    return;
                }
                callback.onResult(result2, null);
            }
        });
    }
    
    private void handleMongoWriteConcernWithResponseException(final RetryState retryState, final boolean breakAndThrowIfDifferent) {
        if (!retryState.isFirstAttempt()) {
            final RuntimeException prospectiveFailedResult = (RuntimeException)retryState.exception().orElse((Object)null);
            final boolean prospectiveResultIsWriteConcernException = prospectiveFailedResult instanceof MongoWriteConcernWithResponseException;
            retryState.breakAndThrowIfRetryAnd((Supplier<Boolean>)(() -> breakAndThrowIfDifferent && !prospectiveResultIsWriteConcernException));
            if (prospectiveResultIsWriteConcernException) {
                ((BulkWriteTracker)retryState.attachment(AttachmentKeys.bulkWriteTracker()).orElseThrow(Assertions::fail)).batch().ifPresent(bulkWriteBatch -> {
                    bulkWriteBatch.addResult((BsonDocument)((MongoWriteConcernWithResponseException)prospectiveFailedResult).getResponse());
                    BulkWriteTracker.attachNext(retryState, bulkWriteBatch);
                });
            }
        }
    }
    
    private boolean handleMongoWriteConcernWithResponseExceptionAsync(final RetryState retryState, @Nullable final SingleResultCallback<BulkWriteResult> callback) {
        if (!retryState.isFirstAttempt()) {
            final RuntimeException prospectiveFailedResult = (RuntimeException)retryState.exception().orElse((Object)null);
            final boolean prospectiveResultIsWriteConcernException = prospectiveFailedResult instanceof MongoWriteConcernWithResponseException;
            if (callback != null && retryState.breakAndCompleteIfRetryAnd((Supplier<Boolean>)(() -> !prospectiveResultIsWriteConcernException), callback)) {
                return true;
            }
            if (prospectiveResultIsWriteConcernException) {
                ((BulkWriteTracker)retryState.attachment(AttachmentKeys.bulkWriteTracker()).orElseThrow(Assertions::fail)).batch().ifPresent(bulkWriteBatch -> {
                    bulkWriteBatch.addResult((BsonDocument)((MongoWriteConcernWithResponseException)prospectiveFailedResult).getResponse());
                    BulkWriteTracker.attachNext(retryState, bulkWriteBatch);
                });
            }
        }
        return false;
    }
    
    @Nullable
    private BsonDocument executeCommand(final Connection connection, final BulkWriteBatch batch, final WriteBinding binding) {
        return connection.command(this.namespace.getDatabaseName(), batch.getCommand(), MixedBulkWriteOperation.NO_OP_FIELD_NAME_VALIDATOR, null, batch.getDecoder(), binding, this.shouldAcknowledge(batch, binding.getSessionContext()), batch.getPayload(), batch.getFieldNameValidator());
    }
    
    private void executeCommandAsync(final AsyncWriteBinding binding, final AsyncConnection connection, final BulkWriteBatch batch, final SingleResultCallback<BsonDocument> callback) {
        connection.commandAsync(this.namespace.getDatabaseName(), batch.getCommand(), MixedBulkWriteOperation.NO_OP_FIELD_NAME_VALIDATOR, null, batch.getDecoder(), binding, this.shouldAcknowledge(batch, binding.getSessionContext()), batch.getPayload(), batch.getFieldNameValidator(), callback);
    }
    
    private WriteConcern getAppliedWriteConcern(final SessionContext sessionContext) {
        if (sessionContext.hasActiveTransaction()) {
            return WriteConcern.ACKNOWLEDGED;
        }
        return this.writeConcern;
    }
    
    private boolean shouldAcknowledge(final BulkWriteBatch batch, final SessionContext sessionContext) {
        return this.ordered ? (batch.hasAnotherBatch() || this.getAppliedWriteConcern(sessionContext).isAcknowledged()) : this.getAppliedWriteConcern(sessionContext).isAcknowledged();
    }
    
    private void addErrorLabelsToWriteConcern(final BsonDocument result, final Set<String> errorLabels) {
        if (!result.containsKey("errorLabels")) {
            result.put("errorLabels", new BsonArray((List<? extends BsonValue>)errorLabels.stream().map(BsonString::new).collect(Collectors.toList())));
        }
    }
    
    static {
        NO_OP_FIELD_NAME_VALIDATOR = new NoOpFieldNameValidator();
    }
    
    public static final class BulkWriteTracker
    {
        private int attempt;
        private final int attempts;
        @Nullable
        private final BulkWriteBatch batch;
        
        static void attachNew(final RetryState retryState, final boolean retry) {
            retryState.attach(AttachmentKeys.bulkWriteTracker(), new BulkWriteTracker(retry, null), false);
        }
        
        static void attachNew(final RetryState retryState, final BulkWriteBatch batch) {
            attach(retryState, new BulkWriteTracker(batch.getRetryWrites(), batch));
        }
        
        static BulkWriteTracker attachNext(final RetryState retryState, final BulkWriteBatch batch) {
            final BulkWriteBatch nextBatch = batch.getNextBatch();
            final BulkWriteTracker nextTracker = new BulkWriteTracker(nextBatch.getRetryWrites(), nextBatch);
            attach(retryState, nextTracker);
            return nextTracker;
        }
        
        private static void attach(final RetryState retryState, final BulkWriteTracker tracker) {
            retryState.attach(AttachmentKeys.bulkWriteTracker(), tracker, false);
            final BulkWriteBatch batch = tracker.batch;
            if (batch != null) {
                retryState.attach(AttachmentKeys.retryableCommandFlag(), batch.getRetryWrites(), false).attach(AttachmentKeys.commandDescriptionSupplier(), (Supplier<String>)(() -> batch.getPayload().getPayloadType().toString()), false);
            }
        }
        
        private BulkWriteTracker(final boolean retry, @Nullable final BulkWriteBatch batch) {
            this.attempt = 0;
            this.attempts = (retry ? 2 : 1);
            this.batch = batch;
        }
        
        boolean lastAttempt() {
            return this.attempt == this.attempts - 1;
        }
        
        void advance() {
            Assertions.assertTrue(!this.lastAttempt());
            ++this.attempt;
        }
        
        Optional<BulkWriteBatch> batch() {
            return (Optional<BulkWriteBatch>)Optional.ofNullable((Object)this.batch);
        }
    }
}
