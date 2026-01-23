package cc.dreamcode.antylogout.libs.com.mongodb.internal.operation;

import cc.dreamcode.antylogout.libs.com.mongodb.lang.NonNull;
import cc.dreamcode.antylogout.libs.com.mongodb.assertions.Assertions;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.diagnostics.logging.Loggers;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.ServerDescription;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.ServerType;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.session.SessionContext;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.SingleResultCallback;
import cc.dreamcode.antylogout.libs.org.bson.conversions.Bson;
import cc.dreamcode.antylogout.libs.org.bson.BsonDocument;
import java.util.Iterator;
import java.util.List;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.bulk.DeleteRequest;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.bulk.UpdateRequest;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.bulk.WriteRequest;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.ConnectionDescription;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoClientException;
import cc.dreamcode.antylogout.libs.com.mongodb.WriteConcern;
import cc.dreamcode.antylogout.libs.com.mongodb.lang.Nullable;
import cc.dreamcode.antylogout.libs.com.mongodb.client.model.Collation;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.diagnostics.logging.Logger;

final class OperationHelper
{
    public static final Logger LOGGER;
    
    static void validateCollationAndWriteConcern(@Nullable final Collation collation, final WriteConcern writeConcern) {
        if (collation != null && !writeConcern.isAcknowledged()) {
            throw new MongoClientException("Specifying collation with an unacknowledged WriteConcern is not supported");
        }
    }
    
    private static void validateArrayFilters(final WriteConcern writeConcern) {
        if (!writeConcern.isAcknowledged()) {
            throw new MongoClientException("Specifying array filters with an unacknowledged WriteConcern is not supported");
        }
    }
    
    private static void validateWriteRequestHint(final ConnectionDescription connectionDescription, final WriteConcern writeConcern, final WriteRequest request) {
        if (!writeConcern.isAcknowledged()) {
            if (request instanceof UpdateRequest && ServerVersionHelper.serverIsLessThanVersionFourDotTwo(connectionDescription)) {
                throw new IllegalArgumentException(String.format("Hint not supported by wire version: %s", new Object[] { connectionDescription.getMaxWireVersion() }));
            }
            if (request instanceof DeleteRequest && ServerVersionHelper.serverIsLessThanVersionFourDotFour(connectionDescription)) {
                throw new IllegalArgumentException(String.format("Hint not supported by wire version: %s", new Object[] { connectionDescription.getMaxWireVersion() }));
            }
        }
    }
    
    static void validateHintForFindAndModify(final ConnectionDescription connectionDescription, final WriteConcern writeConcern) {
        if (ServerVersionHelper.serverIsLessThanVersionFourDotTwo(connectionDescription)) {
            throw new IllegalArgumentException(String.format("Hint not supported by wire version: %s", new Object[] { connectionDescription.getMaxWireVersion() }));
        }
        if (!writeConcern.isAcknowledged() && ServerVersionHelper.serverIsLessThanVersionFourDotFour(connectionDescription)) {
            throw new IllegalArgumentException(String.format("Hint not supported by wire version: %s", new Object[] { connectionDescription.getMaxWireVersion() }));
        }
    }
    
    private static void validateWriteRequestCollations(final List<? extends WriteRequest> requests, final WriteConcern writeConcern) {
        Collation collation = null;
        for (final WriteRequest request : requests) {
            if (request instanceof UpdateRequest) {
                collation = ((UpdateRequest)request).getCollation();
            }
            else if (request instanceof DeleteRequest) {
                collation = ((DeleteRequest)request).getCollation();
            }
            if (collation != null) {
                break;
            }
        }
        validateCollationAndWriteConcern(collation, writeConcern);
    }
    
    private static void validateUpdateRequestArrayFilters(final List<? extends WriteRequest> requests, final WriteConcern writeConcern) {
        for (final WriteRequest request : requests) {
            List<BsonDocument> arrayFilters = null;
            if (request instanceof UpdateRequest) {
                arrayFilters = ((UpdateRequest)request).getArrayFilters();
            }
            if (arrayFilters != null) {
                validateArrayFilters(writeConcern);
                break;
            }
        }
    }
    
    private static void validateWriteRequestHints(final ConnectionDescription connectionDescription, final List<? extends WriteRequest> requests, final WriteConcern writeConcern) {
        for (final WriteRequest request : requests) {
            Bson hint = null;
            String hintString = null;
            if (request instanceof UpdateRequest) {
                hint = ((UpdateRequest)request).getHint();
                hintString = ((UpdateRequest)request).getHintString();
            }
            else if (request instanceof DeleteRequest) {
                hint = ((DeleteRequest)request).getHint();
                hintString = ((DeleteRequest)request).getHintString();
            }
            if (hint != null || hintString != null) {
                validateWriteRequestHint(connectionDescription, writeConcern, request);
                break;
            }
        }
    }
    
    static void validateWriteRequests(final ConnectionDescription connectionDescription, final Boolean bypassDocumentValidation, final List<? extends WriteRequest> requests, final WriteConcern writeConcern) {
        checkBypassDocumentValidationIsSupported(bypassDocumentValidation, writeConcern);
        validateWriteRequestCollations(requests, writeConcern);
        validateUpdateRequestArrayFilters(requests, writeConcern);
        validateWriteRequestHints(connectionDescription, requests, writeConcern);
    }
    
    static <R> boolean validateWriteRequestsAndCompleteIfInvalid(final ConnectionDescription connectionDescription, final Boolean bypassDocumentValidation, final List<? extends WriteRequest> requests, final WriteConcern writeConcern, final SingleResultCallback<R> callback) {
        try {
            validateWriteRequests(connectionDescription, bypassDocumentValidation, requests, writeConcern);
            return false;
        }
        catch (final Throwable validationT) {
            callback.onResult(null, validationT);
            return true;
        }
    }
    
    private static void checkBypassDocumentValidationIsSupported(@Nullable final Boolean bypassDocumentValidation, final WriteConcern writeConcern) {
        if (bypassDocumentValidation != null && !writeConcern.isAcknowledged()) {
            throw new MongoClientException("Specifying bypassDocumentValidation with an unacknowledged WriteConcern is not supported");
        }
    }
    
    static boolean isRetryableWrite(final boolean retryWrites, final WriteConcern writeConcern, final ConnectionDescription connectionDescription, final SessionContext sessionContext) {
        if (!retryWrites) {
            return false;
        }
        if (!writeConcern.isAcknowledged()) {
            OperationHelper.LOGGER.debug("retryWrites set to true but the writeConcern is unacknowledged.");
            return false;
        }
        if (sessionContext.hasActiveTransaction()) {
            OperationHelper.LOGGER.debug("retryWrites set to true but in an active transaction.");
            return false;
        }
        return canRetryWrite(connectionDescription, sessionContext);
    }
    
    static boolean canRetryWrite(final ConnectionDescription connectionDescription, final SessionContext sessionContext) {
        if (connectionDescription.getLogicalSessionTimeoutMinutes() == null) {
            OperationHelper.LOGGER.debug("retryWrites set to true but the server does not support sessions.");
            return false;
        }
        if (connectionDescription.getServerType().equals((Object)ServerType.STANDALONE)) {
            OperationHelper.LOGGER.debug("retryWrites set to true but the server is a standalone server.");
            return false;
        }
        return true;
    }
    
    static boolean canRetryRead(final ServerDescription serverDescription, final SessionContext sessionContext) {
        if (sessionContext.hasActiveTransaction()) {
            OperationHelper.LOGGER.debug("retryReads set to true but in an active transaction.");
            return false;
        }
        return true;
    }
    
    private OperationHelper() {
    }
    
    static {
        LOGGER = Loggers.getLogger("operation");
    }
    
    public static final class ResourceSupplierInternalException extends RuntimeException
    {
        private static final long serialVersionUID = 0L;
        
        ResourceSupplierInternalException(final Throwable cause) {
            super((Throwable)Assertions.assertNotNull(cause));
        }
        
        @NonNull
        public Throwable getCause() {
            return Assertions.assertNotNull(super.getCause());
        }
    }
}
