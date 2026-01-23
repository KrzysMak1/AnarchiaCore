package cc.dreamcode.antylogout.libs.com.mongodb.internal.operation;

import cc.dreamcode.antylogout.libs.com.mongodb.connection.ConnectionDescription;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.ServerDescription;
import java.util.Arrays;
import java.util.Iterator;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoClientException;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.OperationContext;
import cc.dreamcode.antylogout.libs.org.bson.BsonDocument;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.operation.retry.AttachmentKeys;
import java.util.function.Supplier;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoSecurityException;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoCommandException;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoConnectionPoolClearedException;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoNodeIsRecoveringException;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoNotPrimaryException;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.function.RetryState;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoException;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoServerException;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoSocketException;
import cc.dreamcode.antylogout.libs.com.mongodb.assertions.Assertions;
import cc.dreamcode.antylogout.libs.com.mongodb.lang.Nullable;
import java.util.List;

final class CommandOperationHelper
{
    private static final List<Integer> RETRYABLE_ERROR_CODES;
    static final String RETRYABLE_WRITE_ERROR_LABEL = "RetryableWriteError";
    private static final String NO_WRITES_PERFORMED_ERROR_LABEL = "NoWritesPerformed";
    
    static Throwable chooseRetryableReadException(@Nullable final Throwable previouslyChosenException, final Throwable mostRecentAttemptException) {
        Assertions.assertFalse(mostRecentAttemptException instanceof OperationHelper.ResourceSupplierInternalException);
        if (previouslyChosenException == null || mostRecentAttemptException instanceof MongoSocketException || mostRecentAttemptException instanceof MongoServerException) {
            return mostRecentAttemptException;
        }
        return previouslyChosenException;
    }
    
    static Throwable chooseRetryableWriteException(@Nullable final Throwable previouslyChosenException, final Throwable mostRecentAttemptException) {
        if (previouslyChosenException == null) {
            if (mostRecentAttemptException instanceof OperationHelper.ResourceSupplierInternalException) {
                return mostRecentAttemptException.getCause();
            }
            return mostRecentAttemptException;
        }
        else {
            if (mostRecentAttemptException instanceof OperationHelper.ResourceSupplierInternalException || (mostRecentAttemptException instanceof MongoException && ((MongoException)mostRecentAttemptException).hasErrorLabel("NoWritesPerformed"))) {
                return previouslyChosenException;
            }
            return mostRecentAttemptException;
        }
    }
    
    static RetryState initialRetryState(final boolean retry) {
        return new RetryState(retry ? 1 : 0);
    }
    
    static boolean isRetryableException(final Throwable t) {
        return t instanceof MongoException && (t instanceof MongoSocketException || t instanceof MongoNotPrimaryException || t instanceof MongoNodeIsRecoveringException || t instanceof MongoConnectionPoolClearedException || CommandOperationHelper.RETRYABLE_ERROR_CODES.contains((Object)((MongoException)t).getCode()));
    }
    
    static void rethrowIfNotNamespaceError(final MongoCommandException e) {
        rethrowIfNotNamespaceError(e, (Object)null);
    }
    
    @Nullable
    static <T> T rethrowIfNotNamespaceError(final MongoCommandException e, @Nullable final T defaultValue) {
        if (!isNamespaceError((Throwable)e)) {
            throw e;
        }
        return defaultValue;
    }
    
    static boolean isNamespaceError(final Throwable t) {
        if (t instanceof MongoCommandException) {
            final MongoCommandException e = (MongoCommandException)t;
            return e.getErrorMessage().contains((CharSequence)"ns not found") || e.getErrorCode() == 26;
        }
        return false;
    }
    
    static boolean shouldAttemptToRetryRead(final RetryState retryState, final Throwable attemptFailure) {
        Assertions.assertFalse(attemptFailure instanceof OperationHelper.ResourceSupplierInternalException);
        final boolean decision = isRetryableException(attemptFailure) || (attemptFailure instanceof MongoSecurityException && attemptFailure.getCause() != null && isRetryableException(attemptFailure.getCause()));
        if (!decision) {
            logUnableToRetry((Supplier<String>)retryState.attachment(AttachmentKeys.commandDescriptionSupplier()).orElse((Object)null), attemptFailure);
        }
        return decision;
    }
    
    static boolean shouldAttemptToRetryWrite(final RetryState retryState, final Throwable attemptFailure) {
        final Throwable failure = (attemptFailure instanceof OperationHelper.ResourceSupplierInternalException) ? attemptFailure.getCause() : attemptFailure;
        boolean decision = false;
        MongoException exceptionRetryableRegardlessOfCommand = null;
        if (failure instanceof MongoConnectionPoolClearedException || (failure instanceof MongoSecurityException && failure.getCause() != null && isRetryableException(failure.getCause()))) {
            decision = true;
            exceptionRetryableRegardlessOfCommand = (MongoException)failure;
        }
        if (retryState.attachment(AttachmentKeys.retryableCommandFlag()).orElse((Object)false)) {
            if (exceptionRetryableRegardlessOfCommand != null) {
                exceptionRetryableRegardlessOfCommand.addLabel("RetryableWriteError");
            }
            else if (decideRetryableAndAddRetryableWriteErrorLabel(failure, (Integer)retryState.attachment(AttachmentKeys.maxWireVersion()).orElse((Object)null))) {
                decision = true;
            }
            else {
                logUnableToRetry((Supplier<String>)retryState.attachment(AttachmentKeys.commandDescriptionSupplier()).orElse((Object)null), failure);
            }
        }
        return decision;
    }
    
    static boolean isRetryWritesEnabled(@Nullable final BsonDocument command) {
        return command != null && (command.containsKey("txnNumber") || command.getFirstKey().equals((Object)"commitTransaction") || command.getFirstKey().equals((Object)"abortTransaction"));
    }
    
    private static boolean decideRetryableAndAddRetryableWriteErrorLabel(final Throwable t, @Nullable final Integer maxWireVersion) {
        if (!(t instanceof MongoException)) {
            return false;
        }
        final MongoException exception = (MongoException)t;
        if (maxWireVersion != null) {
            addRetryableWriteErrorLabel(exception, maxWireVersion);
        }
        return exception.hasErrorLabel("RetryableWriteError");
    }
    
    static void addRetryableWriteErrorLabel(final MongoException exception, final int maxWireVersion) {
        if (maxWireVersion >= 9 && exception instanceof MongoSocketException) {
            exception.addLabel("RetryableWriteError");
        }
        else if (maxWireVersion < 9 && isRetryableException((Throwable)exception)) {
            exception.addLabel("RetryableWriteError");
        }
    }
    
    static void logRetryExecute(final RetryState retryState, final OperationContext operationContext) {
        if (OperationHelper.LOGGER.isDebugEnabled() && !retryState.isFirstAttempt()) {
            final String commandDescription = (String)retryState.attachment(AttachmentKeys.commandDescriptionSupplier()).map(Supplier::get).orElse((Object)null);
            final Throwable exception = (Throwable)retryState.exception().orElseThrow(Assertions::fail);
            final int oneBasedAttempt = retryState.attempt() + 1;
            final long operationId = operationContext.getId();
            OperationHelper.LOGGER.debug((commandDescription == null) ? String.format("Retrying the operation with operation ID %s due to the error \"%s\". Attempt number: #%d", new Object[] { operationId, exception, oneBasedAttempt }) : String.format("Retrying the operation '%s' with operation ID %s due to the error \"%s\". Attempt number: #%d", new Object[] { commandDescription, operationId, exception, oneBasedAttempt }));
        }
    }
    
    private static void logUnableToRetry(@Nullable final Supplier<String> commandDescriptionSupplier, final Throwable originalError) {
        if (OperationHelper.LOGGER.isDebugEnabled()) {
            final String commandDescription = (commandDescriptionSupplier == null) ? null : ((String)commandDescriptionSupplier.get());
            OperationHelper.LOGGER.debug((commandDescription == null) ? String.format("Unable to retry an operation due to the error \"%s\"", new Object[] { originalError }) : String.format("Unable to retry the operation %s due to the error \"%s\"", new Object[] { commandDescription, originalError }));
        }
    }
    
    static MongoException transformWriteException(final MongoException exception) {
        if (exception.getCode() == 20 && exception.getMessage().contains((CharSequence)"Transaction numbers")) {
            final MongoException clientException = new MongoClientException("This MongoDB deployment does not support retryable writes. Please add retryWrites=false to your connection string.", (Throwable)exception);
            for (final String errorLabel : exception.getErrorLabels()) {
                clientException.addLabel(errorLabel);
            }
            return clientException;
        }
        return exception;
    }
    
    private CommandOperationHelper() {
    }
    
    static {
        RETRYABLE_ERROR_CODES = Arrays.asList((Object[])new Integer[] { 6, 7, 89, 91, 189, 262, 9001, 13436, 13435, 11602, 11600, 10107 });
    }
    
    interface CommandCreator
    {
        BsonDocument create(final ServerDescription p0, final ConnectionDescription p1);
    }
}
