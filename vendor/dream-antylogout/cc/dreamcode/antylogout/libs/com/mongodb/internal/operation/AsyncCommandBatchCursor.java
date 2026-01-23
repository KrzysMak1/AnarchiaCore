package cc.dreamcode.antylogout.libs.com.mongodb.internal.operation;

import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.ReferenceCounted;
import cc.dreamcode.antylogout.libs.org.bson.codecs.BsonDocumentCodec;
import java.util.Objects;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoSocketException;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.Connection;
import java.util.function.Supplier;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.function.AsyncCallbackSupplier;
import cc.dreamcode.antylogout.libs.com.mongodb.annotations.ThreadSafe;
import cc.dreamcode.antylogout.libs.com.mongodb.ServerAddress;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoCommandException;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.BindingContext;
import cc.dreamcode.antylogout.libs.com.mongodb.ReadPreference;
import cc.dreamcode.antylogout.libs.com.mongodb.assertions.Assertions;
import cc.dreamcode.antylogout.libs.org.bson.BsonTimestamp;
import cc.dreamcode.antylogout.libs.com.mongodb.ServerCursor;
import java.util.Collections;
import java.util.List;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.SingleResultCallback;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.ConnectionDescription;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.ServerType;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.AsyncConnection;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.AsyncConnectionSource;
import cc.dreamcode.antylogout.libs.org.bson.BsonDocument;
import java.util.concurrent.atomic.AtomicBoolean;
import cc.dreamcode.antylogout.libs.com.mongodb.lang.Nullable;
import cc.dreamcode.antylogout.libs.org.bson.BsonValue;
import cc.dreamcode.antylogout.libs.org.bson.codecs.Decoder;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoNamespace;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.AsyncAggregateResponseBatchCursor;

class AsyncCommandBatchCursor<T> implements AsyncAggregateResponseBatchCursor<T>
{
    private final MongoNamespace namespace;
    private final long maxTimeMS;
    private final Decoder<T> decoder;
    @Nullable
    private final BsonValue comment;
    private final int maxWireVersion;
    private final boolean firstBatchEmpty;
    private final ResourceManager resourceManager;
    private final AtomicBoolean processedInitial;
    private int batchSize;
    private volatile CommandCursorResult<T> commandCursorResult;
    
    AsyncCommandBatchCursor(final BsonDocument commandCursorDocument, final int batchSize, final long maxTimeMS, final Decoder<T> decoder, @Nullable final BsonValue comment, final AsyncConnectionSource connectionSource, final AsyncConnection connection) {
        this.processedInitial = new AtomicBoolean();
        final ConnectionDescription connectionDescription = connection.getDescription();
        this.commandCursorResult = this.toCommandCursorResult(connectionDescription.getServerAddress(), "firstBatch", commandCursorDocument);
        this.namespace = this.commandCursorResult.getNamespace();
        this.batchSize = batchSize;
        this.maxTimeMS = maxTimeMS;
        this.decoder = decoder;
        this.comment = comment;
        this.maxWireVersion = connectionDescription.getMaxWireVersion();
        this.firstBatchEmpty = this.commandCursorResult.getResults().isEmpty();
        final AsyncConnection connectionToPin = (connectionSource.getServerDescription().getType() == ServerType.LOAD_BALANCER) ? connection : null;
        this.resourceManager = new ResourceManager(this.namespace, connectionSource, connectionToPin, this.commandCursorResult.getServerCursor());
    }
    
    @Override
    public void next(final SingleResultCallback<List<T>> callback) {
        this.resourceManager.execute(funcCallback -> {
            final ServerCursor localServerCursor = this.resourceManager.getServerCursor();
            final boolean serverCursorIsNull = localServerCursor == null;
            List<T> batchResults = (List<T>)Collections.emptyList();
            if (!this.processedInitial.getAndSet(true) && !this.firstBatchEmpty) {
                batchResults = this.commandCursorResult.getResults();
            }
            if (serverCursorIsNull || !batchResults.isEmpty()) {
                funcCallback.onResult(batchResults, null);
            }
            else {
                this.getMore(localServerCursor, funcCallback);
            }
        }, callback);
    }
    
    @Override
    public boolean isClosed() {
        return !this.resourceManager.operable();
    }
    
    @Override
    public void setBatchSize(final int batchSize) {
        this.batchSize = batchSize;
    }
    
    @Override
    public int getBatchSize() {
        return this.batchSize;
    }
    
    @Override
    public void close() {
        this.resourceManager.close();
    }
    
    @Nullable
    ServerCursor getServerCursor() {
        if (!this.resourceManager.operable()) {
            return null;
        }
        return this.resourceManager.getServerCursor();
    }
    
    @Override
    public BsonDocument getPostBatchResumeToken() {
        return this.commandCursorResult.getPostBatchResumeToken();
    }
    
    @Override
    public BsonTimestamp getOperationTime() {
        return this.commandCursorResult.getOperationTime();
    }
    
    @Override
    public boolean isFirstBatchEmpty() {
        return this.firstBatchEmpty;
    }
    
    @Override
    public int getMaxWireVersion() {
        return this.maxWireVersion;
    }
    
    private void getMore(final ServerCursor cursor, final SingleResultCallback<List<T>> callback) {
        this.resourceManager.executeWithConnection((connection, wrappedCallback) -> this.getMoreLoop(Assertions.assertNotNull(connection), cursor, wrappedCallback), callback);
    }
    
    private void getMoreLoop(final AsyncConnection connection, final ServerCursor serverCursor, final SingleResultCallback<List<T>> callback) {
        connection.commandAsync(this.namespace.getDatabaseName(), CommandBatchCursorHelper.getMoreCommandDocument(serverCursor.getId(), connection.getDescription(), this.namespace, this.batchSize, this.maxTimeMS, this.comment), CommandBatchCursorHelper.NO_OP_FIELD_NAME_VALIDATOR, ReadPreference.primary(), CommandResultDocumentCodec.create(this.decoder, "nextBatch"), Assertions.assertNotNull(((CursorResourceManager<AsyncConnectionSource, C>)this.resourceManager).getConnectionSource()), (commandResult, t) -> {
            if (t != null) {
                final Throwable translatedException = (t instanceof MongoCommandException) ? CommandBatchCursorHelper.translateCommandException((MongoCommandException)t, serverCursor) : t;
                callback.onResult(null, translatedException);
            }
            else {
                this.commandCursorResult = this.toCommandCursorResult(connection.getDescription().getServerAddress(), "nextBatch", Assertions.assertNotNull(commandResult));
                final ServerCursor nextServerCursor = this.commandCursorResult.getServerCursor();
                this.resourceManager.setServerCursor(nextServerCursor);
                final List<T> nextBatch = this.commandCursorResult.getResults();
                if (nextServerCursor == null || !nextBatch.isEmpty()) {
                    callback.onResult(nextBatch, null);
                }
                else if (!this.resourceManager.operable()) {
                    callback.onResult(Collections.emptyList(), null);
                }
                else {
                    this.getMoreLoop(connection, nextServerCursor, callback);
                }
            }
        });
    }
    
    private CommandCursorResult<T> toCommandCursorResult(final ServerAddress serverAddress, final String fieldNameContainingBatch, final BsonDocument commandCursorDocument) {
        final CommandCursorResult<T> commandCursorResult = new CommandCursorResult<T>(serverAddress, fieldNameContainingBatch, commandCursorDocument);
        CommandBatchCursorHelper.logCommandCursorResult(commandCursorResult);
        return commandCursorResult;
    }
    
    @ThreadSafe
    private static final class ResourceManager extends CursorResourceManager<AsyncConnectionSource, AsyncConnection>
    {
        ResourceManager(final MongoNamespace namespace, final AsyncConnectionSource connectionSource, @Nullable final AsyncConnection connectionToPin, @Nullable final ServerCursor serverCursor) {
            super(namespace, connectionSource, connectionToPin, serverCursor);
        }
        
         <R> void execute(final AsyncCallbackSupplier<R> operation, final SingleResultCallback<R> callback) {
            final boolean canStartOperation = Assertions.doesNotThrow((java.util.function.Supplier<Boolean>)this::tryStartOperation);
            if (!canStartOperation) {
                callback.onResult(null, (Throwable)new IllegalStateException("Cursor has been closed"));
            }
            else {
                operation.whenComplete(() -> {
                    this.endOperation();
                    if (this.getServerCursor() == null) {
                        this.close();
                    }
                }).get(callback);
            }
        }
        
        @Override
        void markAsPinned(final AsyncConnection connectionToPin, final Connection.PinningMode pinningMode) {
            connectionToPin.markAsPinned(pinningMode);
        }
        
        @Override
        void doClose() {
            if (this.isSkipReleasingServerResourcesOnClose()) {
                this.unsetServerCursor();
            }
            if (this.getServerCursor() != null) {
                this.getConnection((connection, t) -> {
                    if (connection != null) {
                        this.releaseServerAndClientResources(connection);
                    }
                    else {
                        this.unsetServerCursor();
                        this.releaseClientResources();
                    }
                });
            }
            else {
                this.releaseClientResources();
            }
        }
        
         <R> void executeWithConnection(final AsyncOperationHelper.AsyncCallableConnectionWithCallback<R> callable, final SingleResultCallback<R> callback) {
            this.getConnection((connection, t) -> {
                if (t != null) {
                    callback.onResult(null, t);
                }
                else {
                    callable.call(Assertions.assertNotNull(connection), (result, t1) -> {
                        if (t1 instanceof MongoSocketException) {
                            ((CursorResourceManager<CS, AsyncConnection>)this).onCorruptedConnection(connection, (MongoSocketException)t1);
                        }
                        connection.release();
                        callback.onResult(result, t1);
                    });
                }
            });
        }
        
        private void getConnection(final SingleResultCallback<AsyncConnection> callback) {
            Assertions.assertTrue(this.getState() != State.IDLE);
            final AsyncConnection pinnedConnection = ((CursorResourceManager<CS, AsyncConnection>)this).getPinnedConnection();
            if (pinnedConnection != null) {
                callback.onResult(Assertions.assertNotNull(pinnedConnection).retain(), null);
            }
            else {
                Assertions.assertNotNull(((CursorResourceManager<AsyncConnectionSource, C>)this).getConnectionSource()).getConnection(callback);
            }
        }
        
        private void releaseServerAndClientResources(final AsyncConnection connection) {
            final AsyncCallbackSupplier<Void> callbackSupplier = funcCallback -> {
                final ServerCursor localServerCursor = this.getServerCursor();
                if (localServerCursor != null) {
                    this.killServerCursor(this.getNamespace(), localServerCursor, connection, funcCallback);
                }
                return;
            };
            final AsyncCallbackSupplier<Void> whenComplete = callbackSupplier.whenComplete(() -> {
                this.unsetServerCursor();
                this.releaseClientResources();
            });
            Objects.requireNonNull((Object)connection);
            whenComplete.whenComplete(connection::release).get((r, t) -> {});
        }
        
        private void killServerCursor(final MongoNamespace namespace, final ServerCursor localServerCursor, final AsyncConnection localConnection, final SingleResultCallback<Void> callback) {
            localConnection.commandAsync(namespace.getDatabaseName(), CommandBatchCursorHelper.getKillCursorsCommand(namespace, localServerCursor), CommandBatchCursorHelper.NO_OP_FIELD_NAME_VALIDATOR, ReadPreference.primary(), (Decoder<Object>)new BsonDocumentCodec(), Assertions.assertNotNull(((CursorResourceManager<AsyncConnectionSource, C>)this).getConnectionSource()), (r, t) -> callback.onResult(null, null));
        }
    }
}
