package cc.dreamcode.antylogout.libs.com.mongodb.internal.operation;

import cc.dreamcode.antylogout.libs.org.bson.BsonValue;
import java.util.function.BiPredicate;
import java.util.function.BinaryOperator;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.function.RetryingSyncSupplier;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.OperationContext;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.function.LoopState;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.operation.retry.AttachmentKeys;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoException;
import cc.dreamcode.antylogout.libs.com.mongodb.lang.Nullable;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.BindingContext;
import cc.dreamcode.antylogout.libs.org.bson.FieldNameValidator;
import cc.dreamcode.antylogout.libs.org.bson.codecs.BsonDocumentCodec;
import cc.dreamcode.antylogout.libs.com.mongodb.ReadPreference;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.validator.NoOpFieldNameValidator;
import cc.dreamcode.antylogout.libs.com.mongodb.assertions.Assertions;
import cc.dreamcode.antylogout.libs.org.bson.BsonDocument;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.function.RetryState;
import java.util.Objects;
import cc.dreamcode.antylogout.libs.org.bson.codecs.Decoder;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.ReferenceCounted;
import java.util.function.Function;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.Connection;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.WriteBinding;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.ConnectionSource;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.ReadBinding;

final class SyncOperationHelper
{
    static <T> T withReadConnectionSource(final ReadBinding binding, final CallableWithSource<T> callable) {
        final ConnectionSource source = binding.getReadConnectionSource();
        try {
            return callable.call(source);
        }
        finally {
            source.release();
        }
    }
    
    static <T> T withConnection(final WriteBinding binding, final CallableWithConnection<T> callable) {
        final ConnectionSource source = binding.getWriteConnectionSource();
        try {
            return withConnectionSource(source, callable);
        }
        finally {
            source.release();
        }
    }
    
    static <R> R withSourceAndConnection(final Supplier<ConnectionSource> sourceSupplier, final boolean wrapConnectionSourceException, final BiFunction<ConnectionSource, Connection, R> function) throws OperationHelper.ResourceSupplierInternalException {
        return withSuppliedResource(sourceSupplier, wrapConnectionSourceException, (java.util.function.Function<ConnectionSource, R>)(source -> {
            Objects.requireNonNull((Object)source);
            return withSuppliedResource((java.util.function.Supplier<ReferenceCounted>)source::getConnection, wrapConnectionSourceException, (java.util.function.Function<ReferenceCounted, Object>)(connection -> function.apply((Object)source, (Object)connection)));
        }));
    }
    
    static <R, T extends ReferenceCounted> R withSuppliedResource(final Supplier<T> resourceSupplier, final boolean wrapSupplierException, final Function<T, R> function) throws OperationHelper.ResourceSupplierInternalException {
        T resource = null;
        try {
            try {
                resource = (T)resourceSupplier.get();
            }
            catch (final Exception supplierException) {
                if (wrapSupplierException) {
                    throw new OperationHelper.ResourceSupplierInternalException((Throwable)supplierException);
                }
                throw supplierException;
            }
            return (R)function.apply((Object)resource);
        }
        finally {
            if (resource != null) {
                resource.release();
            }
        }
    }
    
    private static <T> T withConnectionSource(final ConnectionSource source, final CallableWithConnection<T> callable) {
        final Connection connection = source.getConnection();
        try {
            return callable.call(connection);
        }
        finally {
            connection.release();
        }
    }
    
    static <D, T> T executeRetryableRead(final ReadBinding binding, final String database, final CommandOperationHelper.CommandCreator commandCreator, final Decoder<D> decoder, final CommandReadTransformer<D, T> transformer, final boolean retryReads) {
        Objects.requireNonNull((Object)binding);
        return executeRetryableRead(binding, (Supplier<ConnectionSource>)binding::getReadConnectionSource, database, commandCreator, decoder, transformer, retryReads);
    }
    
    static <D, T> T executeRetryableRead(final ReadBinding binding, final Supplier<ConnectionSource> readConnectionSourceSupplier, final String database, final CommandOperationHelper.CommandCreator commandCreator, final Decoder<D> decoder, final CommandReadTransformer<D, T> transformer, final boolean retryReads) {
        final RetryState retryState = CommandOperationHelper.initialRetryState(retryReads);
        final Supplier<T> read = decorateReadWithRetries(retryState, binding.getOperationContext(), (java.util.function.Supplier<T>)(() -> withSourceAndConnection(readConnectionSourceSupplier, false, (java.util.function.BiFunction<ConnectionSource, Connection, Object>)((source, connection) -> {
            retryState.breakAndThrowIfRetryAnd((Supplier<Boolean>)(() -> !OperationHelper.canRetryRead(source.getServerDescription(), binding.getSessionContext())));
            return createReadCommandAndExecute(retryState, binding, source, database, commandCreator, decoder, (CommandReadTransformer<Object, Object>)transformer, connection);
        }))));
        return (T)read.get();
    }
    
    static <D, T> T executeCommand(final WriteBinding binding, final String database, final BsonDocument command, final Decoder<D> decoder, final CommandWriteTransformer<D, T> transformer) {
        Objects.requireNonNull((Object)binding);
        return withSourceAndConnection((Supplier<ConnectionSource>)binding::getWriteConnectionSource, false, (java.util.function.BiFunction<ConnectionSource, Connection, T>)((source, connection) -> transformer.apply(Assertions.assertNotNull((Object)connection.command(database, command, new NoOpFieldNameValidator(), ReadPreference.primary(), (Decoder<T>)decoder, binding)), connection)));
    }
    
    @Nullable
    static <T> T executeCommand(final WriteBinding binding, final String database, final BsonDocument command, final Connection connection, final CommandWriteTransformer<BsonDocument, T> transformer) {
        Assertions.notNull("binding", binding);
        return transformer.apply(Assertions.assertNotNull((BsonDocument)connection.command(database, command, new NoOpFieldNameValidator(), ReadPreference.primary(), (Decoder<T>)new BsonDocumentCodec(), binding)), connection);
    }
    
    static <T, R> R executeRetryableWrite(final WriteBinding binding, final String database, @Nullable final ReadPreference readPreference, final FieldNameValidator fieldNameValidator, final Decoder<T> commandResultDecoder, final CommandOperationHelper.CommandCreator commandCreator, final CommandWriteTransformer<T, R> transformer, final cc.dreamcode.antylogout.libs.com.mongodb.Function<BsonDocument, BsonDocument> retryCommandModifier) {
        final RetryState retryState = CommandOperationHelper.initialRetryState(true);
        final Supplier<R> retryingWrite = decorateWriteWithRetries(retryState, binding.getOperationContext(), (java.util.function.Supplier<R>)(() -> {
            final boolean firstAttempt = retryState.isFirstAttempt();
            if (!firstAttempt && binding.getSessionContext().hasActiveTransaction()) {
                binding.getSessionContext().clearTransactionContext();
            }
            Objects.requireNonNull((Object)binding);
            return withSourceAndConnection((Supplier<ConnectionSource>)binding::getWriteConnectionSource, true, (java.util.function.BiFunction<ConnectionSource, Connection, Object>)((source, connection) -> {
                final int maxWireVersion = connection.getDescription().getMaxWireVersion();
                try {
                    retryState.breakAndThrowIfRetryAnd((Supplier<Boolean>)(() -> !OperationHelper.canRetryWrite(connection.getDescription(), binding.getSessionContext())));
                    final BsonDocument command = (BsonDocument)retryState.attachment(AttachmentKeys.command()).map(previousAttemptCommand -> {
                        Assertions.assertFalse(firstAttempt);
                        return retryCommandModifier.apply(previousAttemptCommand);
                    }).orElseGet(() -> commandCreator.create(source.getServerDescription(), connection.getDescription()));
                    final RetryState attach = retryState.attach(AttachmentKeys.maxWireVersion(), maxWireVersion, true).attach(AttachmentKeys.retryableCommandFlag(), CommandOperationHelper.isRetryWritesEnabled(command), true);
                    final LoopState.AttachmentKey<Supplier<String>> commandDescriptionSupplier = AttachmentKeys.commandDescriptionSupplier();
                    final BsonDocument bsonDocument = command;
                    Objects.requireNonNull((Object)bsonDocument);
                    attach.attach((LoopState.AttachmentKey<Object>)commandDescriptionSupplier, bsonDocument::getFirstKey, false).attach(AttachmentKeys.command(), command, false);
                    return transformer.apply(Assertions.assertNotNull((Object)connection.command(database, command, fieldNameValidator, readPreference, (Decoder<T>)commandResultDecoder, binding)), connection);
                }
                catch (final MongoException e) {
                    if (!firstAttempt) {
                        CommandOperationHelper.addRetryableWriteErrorLabel(e, maxWireVersion);
                    }
                    throw e;
                }
            }));
        }));
        try {
            return (R)retryingWrite.get();
        }
        catch (final MongoException e) {
            throw CommandOperationHelper.transformWriteException(e);
        }
    }
    
    @Nullable
    static <D, T> T createReadCommandAndExecute(final RetryState retryState, final ReadBinding binding, final ConnectionSource source, final String database, final CommandOperationHelper.CommandCreator commandCreator, final Decoder<D> decoder, final CommandReadTransformer<D, T> transformer, final Connection connection) {
        final BsonDocument command = commandCreator.create(source.getServerDescription(), connection.getDescription());
        final LoopState.AttachmentKey<Supplier<String>> commandDescriptionSupplier = AttachmentKeys.commandDescriptionSupplier();
        final BsonDocument bsonDocument = command;
        Objects.requireNonNull((Object)bsonDocument);
        retryState.attach((LoopState.AttachmentKey<Object>)commandDescriptionSupplier, bsonDocument::getFirstKey, false);
        return transformer.apply(Assertions.assertNotNull((D)connection.command(database, command, new NoOpFieldNameValidator(), source.getReadPreference(), (Decoder<T>)decoder, binding)), source, connection);
    }
    
    static <R> Supplier<R> decorateWriteWithRetries(final RetryState retryState, final OperationContext operationContext, final Supplier<R> writeFunction) {
        return (Supplier<R>)new RetryingSyncSupplier(retryState, (BinaryOperator<Throwable>)CommandOperationHelper::chooseRetryableWriteException, (BiPredicate<RetryState, Throwable>)CommandOperationHelper::shouldAttemptToRetryWrite, (java.util.function.Supplier<Object>)(() -> {
            CommandOperationHelper.logRetryExecute(retryState, operationContext);
            return writeFunction.get();
        }));
    }
    
    static <R> Supplier<R> decorateReadWithRetries(final RetryState retryState, final OperationContext operationContext, final Supplier<R> readFunction) {
        return (Supplier<R>)new RetryingSyncSupplier(retryState, (BinaryOperator<Throwable>)CommandOperationHelper::chooseRetryableReadException, (BiPredicate<RetryState, Throwable>)CommandOperationHelper::shouldAttemptToRetryRead, (java.util.function.Supplier<Object>)(() -> {
            CommandOperationHelper.logRetryExecute(retryState, operationContext);
            return readFunction.get();
        }));
    }
    
    static CommandWriteTransformer<BsonDocument, Void> writeConcernErrorTransformer() {
        return (CommandWriteTransformer<BsonDocument, Void>)((result, connection) -> {
            Assertions.assertNotNull(result);
            WriteConcernHelper.throwOnWriteConcernError(result, connection.getDescription().getServerAddress(), connection.getDescription().getMaxWireVersion());
            return null;
        });
    }
    
    static <T> CommandReadTransformer<BsonDocument, BatchCursor<T>> singleBatchCursorTransformer(final String fieldName) {
        return (CommandReadTransformer<BsonDocument, BatchCursor<T>>)((result, source, connection) -> new SingleBatchCursor(BsonDocumentWrapperHelper.toList(result, fieldName), 0, connection.getDescription().getServerAddress()));
    }
    
    static <T> BatchCursor<T> cursorDocumentToBatchCursor(final BsonDocument cursorDocument, final Decoder<T> decoder, final BsonValue comment, final ConnectionSource source, final Connection connection, final int batchSize) {
        return new CommandBatchCursor<T>(cursorDocument, batchSize, 0L, decoder, comment, source, connection);
    }
    
    private SyncOperationHelper() {
    }
    
    interface CallableWithSource<T>
    {
        T call(final ConnectionSource p0);
    }
    
    interface CallableWithConnection<T>
    {
        T call(final Connection p0);
    }
    
    interface CommandReadTransformer<T, R>
    {
        @Nullable
        R apply(final T p0, final ConnectionSource p1, final Connection p2);
    }
    
    interface CommandWriteTransformer<T, R>
    {
        @Nullable
        R apply(final T p0, final Connection p1);
    }
}
