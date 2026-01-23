package cc.dreamcode.antylogout.libs.com.mongodb.internal.operation;

import java.util.Iterator;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoException;
import java.util.List;
import java.util.Collections;
import cc.dreamcode.antylogout.libs.org.bson.BsonValue;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.AsyncBatchCursor;
import java.util.function.BiPredicate;
import java.util.function.BinaryOperator;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.function.RetryingAsyncCallbackSupplier;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.OperationContext;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.function.LoopState;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.operation.retry.AttachmentKeys;
import cc.dreamcode.antylogout.libs.com.mongodb.Function;
import cc.dreamcode.antylogout.libs.com.mongodb.lang.Nullable;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.BindingContext;
import cc.dreamcode.antylogout.libs.org.bson.FieldNameValidator;
import cc.dreamcode.antylogout.libs.org.bson.codecs.BsonDocumentCodec;
import cc.dreamcode.antylogout.libs.com.mongodb.ReadPreference;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.validator.NoOpFieldNameValidator;
import cc.dreamcode.antylogout.libs.org.bson.BsonDocument;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.function.RetryState;
import java.util.function.Supplier;
import cc.dreamcode.antylogout.libs.org.bson.codecs.Decoder;
import cc.dreamcode.antylogout.libs.com.mongodb.assertions.Assertions;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.ReferenceCounted;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.function.AsyncCallbackFunction;
import java.util.Objects;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.AsyncConnection;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.function.AsyncCallbackBiFunction;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.function.AsyncCallbackSupplier;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.AsyncWriteBinding;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.AsyncConnectionSource;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.SingleResultCallback;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.ErrorHandlingResultCallback;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.AsyncReadBinding;

final class AsyncOperationHelper
{
    static void withAsyncReadConnectionSource(final AsyncReadBinding binding, final AsyncCallableWithSource callable) {
        binding.getReadConnectionSource(ErrorHandlingResultCallback.errorHandlingCallback((SingleResultCallback<AsyncConnectionSource>)new AsyncCallableWithSourceCallback(callable), OperationHelper.LOGGER));
    }
    
    static void withAsyncConnection(final AsyncWriteBinding binding, final AsyncCallableWithConnection callable) {
        binding.getWriteConnectionSource(ErrorHandlingResultCallback.errorHandlingCallback((SingleResultCallback<AsyncConnectionSource>)new AsyncCallableWithConnectionCallback(callable), OperationHelper.LOGGER));
    }
    
    static <R> void withAsyncSourceAndConnection(final AsyncCallbackSupplier<AsyncConnectionSource> sourceSupplier, final boolean wrapConnectionSourceException, final SingleResultCallback<R> callback, final AsyncCallbackBiFunction<AsyncConnectionSource, AsyncConnection, R> asyncFunction) throws OperationHelper.ResourceSupplierInternalException {
        final SingleResultCallback<R> errorHandlingCallback = ErrorHandlingResultCallback.errorHandlingCallback(callback, OperationHelper.LOGGER);
        withAsyncSuppliedResource(sourceSupplier, wrapConnectionSourceException, errorHandlingCallback, (source, sourceReleasingCallback) -> {
            Objects.requireNonNull((Object)source);
            withAsyncSuppliedResource(source::getConnection, wrapConnectionSourceException, sourceReleasingCallback, (connection, connectionAndSourceReleasingCallback) -> asyncFunction.apply(source, connection, connectionAndSourceReleasingCallback));
        });
    }
    
    static <R, T extends ReferenceCounted> void withAsyncSuppliedResource(final AsyncCallbackSupplier<T> resourceSupplier, final boolean wrapSourceConnectionException, final SingleResultCallback<R> callback, final AsyncCallbackFunction<T, R> function) throws OperationHelper.ResourceSupplierInternalException {
        final SingleResultCallback<R> errorHandlingCallback = ErrorHandlingResultCallback.errorHandlingCallback(callback, OperationHelper.LOGGER);
        resourceSupplier.get((resource, supplierException) -> {
            if (supplierException != null) {
                if (wrapSourceConnectionException) {
                    supplierException = (Throwable)new OperationHelper.ResourceSupplierInternalException(supplierException);
                }
                errorHandlingCallback.onResult(null, supplierException);
            }
            else {
                Assertions.assertNotNull(resource);
                final AsyncCallbackSupplier asyncCallbackSupplier;
                final AsyncCallbackSupplier<Object> curriedFunction = asyncCallbackSupplier = (c -> function.apply(resource, c));
                Objects.requireNonNull((Object)resource);
                asyncCallbackSupplier.whenComplete(resource::release).get(errorHandlingCallback);
            }
        });
    }
    
    static void withAsyncConnectionSourceCallableConnection(final AsyncConnectionSource source, final AsyncCallableWithConnection callable) {
        source.getConnection((connection, t) -> {
            source.release();
            if (t != null) {
                callable.call(null, t);
            }
            else {
                callable.call(connection, null);
            }
        });
    }
    
    static void withAsyncConnectionSource(final AsyncConnectionSource source, final AsyncCallableWithSource callable) {
        callable.call(source, null);
    }
    
    static <D, T> void executeRetryableReadAsync(final AsyncReadBinding binding, final String database, final CommandOperationHelper.CommandCreator commandCreator, final Decoder<D> decoder, final CommandReadTransformerAsync<D, T> transformer, final boolean retryReads, final SingleResultCallback<T> callback) {
        Objects.requireNonNull((Object)binding);
        executeRetryableReadAsync(binding, binding::getReadConnectionSource, database, commandCreator, decoder, transformer, retryReads, callback);
    }
    
    static <D, T> void executeRetryableReadAsync(final AsyncReadBinding binding, final AsyncCallbackSupplier<AsyncConnectionSource> sourceAsyncSupplier, final String database, final CommandOperationHelper.CommandCreator commandCreator, final Decoder<D> decoder, final CommandReadTransformerAsync<D, T> transformer, final boolean retryReads, final SingleResultCallback<T> callback) {
        final RetryState retryState = CommandOperationHelper.initialRetryState(retryReads);
        binding.retain();
        final AsyncCallbackSupplier<T> decorateReadWithRetriesAsync = decorateReadWithRetriesAsync(retryState, binding.getOperationContext(), funcCallback -> withAsyncSourceAndConnection(sourceAsyncSupplier, (boolean)(0 != 0), (SingleResultCallback<Object>)funcCallback, (source, connection, releasingCallback) -> {
            if (!retryState.breakAndCompleteIfRetryAnd((Supplier<Boolean>)(() -> !OperationHelper.canRetryRead(source.getServerDescription(), binding.getSessionContext())), releasingCallback)) {
                createReadCommandAndExecuteAsync(retryState, binding, source, database, commandCreator, decoder, (CommandReadTransformerAsync<Object, Object>)transformer, connection, releasingCallback);
            }
        }));
        Objects.requireNonNull((Object)binding);
        final AsyncCallbackSupplier<T> asyncRead = decorateReadWithRetriesAsync.whenComplete(binding::release);
        asyncRead.get(ErrorHandlingResultCallback.errorHandlingCallback(callback, OperationHelper.LOGGER));
    }
    
    static <T> void executeCommandAsync(final AsyncWriteBinding binding, final String database, final BsonDocument command, final AsyncConnection connection, final CommandWriteTransformerAsync<BsonDocument, T> transformer, final SingleResultCallback<T> callback) {
        Assertions.notNull("binding", binding);
        final SingleResultCallback<T> addingRetryableLabelCallback = addingRetryableLabelCallback(callback, connection.getDescription().getMaxWireVersion());
        connection.commandAsync(database, command, new NoOpFieldNameValidator(), ReadPreference.primary(), (Decoder<Object>)new BsonDocumentCodec(), binding, (SingleResultCallback<Object>)transformingWriteCallback((CommandWriteTransformerAsync<T, T>)transformer, connection, addingRetryableLabelCallback));
    }
    
    static <T, R> void executeRetryableWriteAsync(final AsyncWriteBinding binding, final String database, @Nullable final ReadPreference readPreference, final FieldNameValidator fieldNameValidator, final Decoder<T> commandResultDecoder, final CommandOperationHelper.CommandCreator commandCreator, final CommandWriteTransformerAsync<T, R> transformer, final Function<BsonDocument, BsonDocument> retryCommandModifier, final SingleResultCallback<R> callback) {
        final RetryState retryState = CommandOperationHelper.initialRetryState(true);
        binding.retain();
        final AsyncCallbackSupplier<R> decorateWriteWithRetriesAsync = decorateWriteWithRetriesAsync(retryState, binding.getOperationContext(), funcCallback -> {
            final boolean firstAttempt = retryState.isFirstAttempt();
            if (!firstAttempt && binding.getSessionContext().hasActiveTransaction()) {
                binding.getSessionContext().clearTransactionContext();
            }
            Objects.requireNonNull((Object)binding);
            withAsyncSourceAndConnection(binding::getWriteConnectionSource, (boolean)(1 != 0), (SingleResultCallback<Object>)funcCallback, (source, connection, releasingCallback) -> {
                final int maxWireVersion = connection.getDescription().getMaxWireVersion();
                final SingleResultCallback<Object> addingRetryableLabelCallback = firstAttempt ? releasingCallback : addingRetryableLabelCallback((SingleResultCallback<Object>)releasingCallback, maxWireVersion);
                if (!retryState.breakAndCompleteIfRetryAnd((Supplier<Boolean>)(() -> !OperationHelper.canRetryWrite(connection.getDescription(), binding.getSessionContext())), addingRetryableLabelCallback)) {
                    BsonDocument command;
                    try {
                        command = (BsonDocument)retryState.attachment(AttachmentKeys.command()).map(previousAttemptCommand -> {
                            Assertions.assertFalse(firstAttempt);
                            return retryCommandModifier.apply(previousAttemptCommand);
                        }).orElseGet(() -> commandCreator.create(source.getServerDescription(), connection.getDescription()));
                        retryState.attach(AttachmentKeys.maxWireVersion(), maxWireVersion, (boolean)(1 != 0)).attach(AttachmentKeys.retryableCommandFlag(), CommandOperationHelper.isRetryWritesEnabled(command), (boolean)(1 != 0));
                        AttachmentKeys.commandDescriptionSupplier();
                        final Object o;
                        Objects.requireNonNull(o);
                        final RetryState retryState2;
                        final LoopState.AttachmentKey<Object> key;
                        retryState2.attach(key, o::getFirstKey, (boolean)(0 != 0)).attach(AttachmentKeys.command(), command, (boolean)(0 != 0));
                    }
                    catch (final Throwable t) {
                        addingRetryableLabelCallback.onResult(null, t);
                        return;
                    }
                    connection.commandAsync(database, command, fieldNameValidator, readPreference, commandResultDecoder, binding, (SingleResultCallback<Object>)transformingWriteCallback((CommandWriteTransformerAsync<T, Object>)transformer, connection, addingRetryableLabelCallback));
                }
            });
            return;
        });
        Objects.requireNonNull((Object)binding);
        final AsyncCallbackSupplier<R> asyncWrite = decorateWriteWithRetriesAsync.whenComplete(binding::release);
        asyncWrite.get(exceptionTransformingCallback((SingleResultCallback<R>)ErrorHandlingResultCallback.errorHandlingCallback((SingleResultCallback<R>)callback, OperationHelper.LOGGER)));
    }
    
    static <D, T> void createReadCommandAndExecuteAsync(final RetryState retryState, final AsyncReadBinding binding, final AsyncConnectionSource source, final String database, final CommandOperationHelper.CommandCreator commandCreator, final Decoder<D> decoder, final CommandReadTransformerAsync<D, T> transformer, final AsyncConnection connection, final SingleResultCallback<T> callback) {
        BsonDocument command;
        try {
            command = commandCreator.create(source.getServerDescription(), connection.getDescription());
            final LoopState.AttachmentKey<Supplier<String>> commandDescriptionSupplier = AttachmentKeys.commandDescriptionSupplier();
            final BsonDocument bsonDocument = command;
            Objects.requireNonNull((Object)bsonDocument);
            retryState.attach((LoopState.AttachmentKey<Object>)commandDescriptionSupplier, bsonDocument::getFirstKey, false);
        }
        catch (final IllegalArgumentException e) {
            callback.onResult(null, (Throwable)e);
            return;
        }
        connection.commandAsync(database, command, new NoOpFieldNameValidator(), source.getReadPreference(), decoder, binding, (SingleResultCallback<D>)transformingReadCallback((CommandReadTransformerAsync<T, T>)transformer, source, connection, callback));
    }
    
    static <R> AsyncCallbackSupplier<R> decorateReadWithRetriesAsync(final RetryState retryState, final OperationContext operationContext, final AsyncCallbackSupplier<R> asyncReadFunction) {
        return new RetryingAsyncCallbackSupplier<R>(retryState, (BinaryOperator<Throwable>)CommandOperationHelper::chooseRetryableReadException, (BiPredicate<RetryState, Throwable>)CommandOperationHelper::shouldAttemptToRetryRead, callback -> {
            CommandOperationHelper.logRetryExecute(retryState, operationContext);
            asyncReadFunction.get(callback);
        });
    }
    
    static <R> AsyncCallbackSupplier<R> decorateWriteWithRetriesAsync(final RetryState retryState, final OperationContext operationContext, final AsyncCallbackSupplier<R> asyncWriteFunction) {
        return new RetryingAsyncCallbackSupplier<R>(retryState, (BinaryOperator<Throwable>)CommandOperationHelper::chooseRetryableWriteException, (BiPredicate<RetryState, Throwable>)CommandOperationHelper::shouldAttemptToRetryWrite, callback -> {
            CommandOperationHelper.logRetryExecute(retryState, operationContext);
            asyncWriteFunction.get(callback);
        });
    }
    
    static CommandWriteTransformerAsync<BsonDocument, Void> writeConcernErrorTransformerAsync() {
        return (CommandWriteTransformerAsync<BsonDocument, Void>)((result, connection) -> {
            Assertions.assertNotNull(result);
            WriteConcernHelper.throwOnWriteConcernError(result, connection.getDescription().getServerAddress(), connection.getDescription().getMaxWireVersion());
            return null;
        });
    }
    
    static <T> CommandReadTransformerAsync<BsonDocument, AsyncBatchCursor<T>> asyncSingleBatchCursorTransformer(final String fieldName) {
        return (CommandReadTransformerAsync<BsonDocument, AsyncBatchCursor<T>>)((result, source, connection) -> new AsyncSingleBatchCursor(BsonDocumentWrapperHelper.toList(result, fieldName), 0));
    }
    
    static <T> AsyncBatchCursor<T> cursorDocumentToAsyncBatchCursor(final BsonDocument cursorDocument, final Decoder<T> decoder, final BsonValue comment, final AsyncConnectionSource source, final AsyncConnection connection, final int batchSize) {
        return new AsyncCommandBatchCursor<T>(cursorDocument, batchSize, 0L, decoder, comment, source, connection);
    }
    
    static <T> SingleResultCallback<T> releasingCallback(final SingleResultCallback<T> wrapped, final AsyncConnection connection) {
        return new ReferenceCountedReleasingWrappedCallback<T>(wrapped, (List<? extends ReferenceCounted>)Collections.singletonList((Object)connection));
    }
    
    static <R> SingleResultCallback<R> exceptionTransformingCallback(final SingleResultCallback<R> callback) {
        return (result, t) -> {
            if (t != null) {
                if (t instanceof MongoException) {
                    callback.onResult(null, (Throwable)CommandOperationHelper.transformWriteException((MongoException)t));
                }
                else {
                    callback.onResult(null, t);
                }
            }
            else {
                callback.onResult(result, null);
            }
        };
    }
    
    private static <T, R> SingleResultCallback<T> transformingWriteCallback(final CommandWriteTransformerAsync<T, R> transformer, final AsyncConnection connection, final SingleResultCallback<R> callback) {
        return (result, t) -> {
            if (t != null) {
                callback.onResult(null, t);
            }
            else {
                Object transformedResult;
                try {
                    transformedResult = transformer.apply(Assertions.assertNotNull(result), connection);
                }
                catch (final Throwable e) {
                    callback.onResult(null, e);
                    return;
                }
                callback.onResult(transformedResult, null);
            }
        };
    }
    
    private static <R> SingleResultCallback<R> addingRetryableLabelCallback(final SingleResultCallback<R> callback, final int maxWireVersion) {
        return (result, t) -> {
            if (t != null) {
                if (t instanceof MongoException) {
                    CommandOperationHelper.addRetryableWriteErrorLabel((MongoException)t, maxWireVersion);
                }
                callback.onResult(null, t);
            }
            else {
                callback.onResult(result, null);
            }
        };
    }
    
    private static <T, R> SingleResultCallback<T> transformingReadCallback(final CommandReadTransformerAsync<T, R> transformer, final AsyncConnectionSource source, final AsyncConnection connection, final SingleResultCallback<R> callback) {
        return (result, t) -> {
            if (t != null) {
                callback.onResult(null, t);
            }
            else {
                Object transformedResult;
                try {
                    transformedResult = transformer.apply(Assertions.assertNotNull(result), source, connection);
                }
                catch (final Throwable e) {
                    callback.onResult(null, e);
                    return;
                }
                callback.onResult(transformedResult, null);
            }
        };
    }
    
    private AsyncOperationHelper() {
    }
    
    private static class AsyncCallableWithConnectionCallback implements SingleResultCallback<AsyncConnectionSource>
    {
        private final AsyncCallableWithConnection callable;
        
        AsyncCallableWithConnectionCallback(final AsyncCallableWithConnection callable) {
            this.callable = callable;
        }
        
        @Override
        public void onResult(@Nullable final AsyncConnectionSource source, @Nullable final Throwable t) {
            if (t != null) {
                this.callable.call(null, t);
            }
            else {
                AsyncOperationHelper.withAsyncConnectionSourceCallableConnection(Assertions.assertNotNull(source), this.callable);
            }
        }
    }
    
    private static class AsyncCallableWithSourceCallback implements SingleResultCallback<AsyncConnectionSource>
    {
        private final AsyncCallableWithSource callable;
        
        AsyncCallableWithSourceCallback(final AsyncCallableWithSource callable) {
            this.callable = callable;
        }
        
        @Override
        public void onResult(@Nullable final AsyncConnectionSource source, @Nullable final Throwable t) {
            if (t != null) {
                this.callable.call(null, t);
            }
            else {
                AsyncOperationHelper.withAsyncConnectionSource(Assertions.assertNotNull(source), this.callable);
            }
        }
    }
    
    private static class ReferenceCountedReleasingWrappedCallback<T> implements SingleResultCallback<T>
    {
        private final SingleResultCallback<T> wrapped;
        private final List<? extends ReferenceCounted> referenceCounted;
        
        ReferenceCountedReleasingWrappedCallback(final SingleResultCallback<T> wrapped, final List<? extends ReferenceCounted> referenceCounted) {
            this.wrapped = wrapped;
            this.referenceCounted = Assertions.notNull("referenceCounted", referenceCounted);
        }
        
        @Override
        public void onResult(@Nullable final T result, @Nullable final Throwable t) {
            for (final ReferenceCounted cur : this.referenceCounted) {
                if (cur != null) {
                    cur.release();
                }
            }
            this.wrapped.onResult(result, t);
        }
    }
    
    interface AsyncCallableWithSource
    {
        void call(@Nullable final AsyncConnectionSource p0, @Nullable final Throwable p1);
    }
    
    interface AsyncCallableWithConnection
    {
        void call(@Nullable final AsyncConnection p0, @Nullable final Throwable p1);
    }
    
    interface CommandReadTransformerAsync<T, R>
    {
        @Nullable
        R apply(final T p0, final AsyncConnectionSource p1, final AsyncConnection p2);
    }
    
    interface CommandWriteTransformerAsync<T, R>
    {
        @Nullable
        R apply(final T p0, final AsyncConnection p1);
    }
    
    interface AsyncCallableConnectionWithCallback<T>
    {
        void call(final AsyncConnection p0, final SingleResultCallback<T> p1);
    }
}
