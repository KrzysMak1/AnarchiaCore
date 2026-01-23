package cc.dreamcode.antylogout.libs.com.mongodb.internal.operation;

import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.ReferenceCounted;
import cc.dreamcode.antylogout.libs.org.bson.codecs.BsonDocumentCodec;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoSocketException;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoException;
import cc.dreamcode.antylogout.libs.com.mongodb.annotations.ThreadSafe;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoCommandException;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.BindingContext;
import cc.dreamcode.antylogout.libs.com.mongodb.ReadPreference;
import java.util.function.Consumer;
import cc.dreamcode.antylogout.libs.org.bson.BsonTimestamp;
import cc.dreamcode.antylogout.libs.com.mongodb.ServerAddress;
import cc.dreamcode.antylogout.libs.com.mongodb.ServerCursor;
import java.util.NoSuchElementException;
import cc.dreamcode.antylogout.libs.com.mongodb.assertions.Assertions;
import java.util.function.Supplier;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.ConnectionDescription;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.ServerType;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.Connection;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.ConnectionSource;
import cc.dreamcode.antylogout.libs.org.bson.BsonDocument;
import java.util.List;
import cc.dreamcode.antylogout.libs.com.mongodb.lang.Nullable;
import cc.dreamcode.antylogout.libs.org.bson.BsonValue;
import cc.dreamcode.antylogout.libs.org.bson.codecs.Decoder;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoNamespace;

class CommandBatchCursor<T> implements AggregateResponseBatchCursor<T>
{
    private final MongoNamespace namespace;
    private final long maxTimeMS;
    private final Decoder<T> decoder;
    @Nullable
    private final BsonValue comment;
    private final int maxWireVersion;
    private final boolean firstBatchEmpty;
    private final ResourceManager resourceManager;
    private int batchSize;
    private CommandCursorResult<T> commandCursorResult;
    @Nullable
    private List<T> nextBatch;
    
    CommandBatchCursor(final BsonDocument commandCursorDocument, final int batchSize, final long maxTimeMS, final Decoder<T> decoder, @Nullable final BsonValue comment, final ConnectionSource connectionSource, final Connection connection) {
        final ConnectionDescription connectionDescription = connection.getDescription();
        this.commandCursorResult = this.toCommandCursorResult(connectionDescription.getServerAddress(), "firstBatch", commandCursorDocument);
        this.namespace = this.commandCursorResult.getNamespace();
        this.batchSize = batchSize;
        this.maxTimeMS = maxTimeMS;
        this.decoder = decoder;
        this.comment = comment;
        this.maxWireVersion = connectionDescription.getMaxWireVersion();
        this.firstBatchEmpty = this.commandCursorResult.getResults().isEmpty();
        final Connection connectionToPin = (connectionSource.getServerDescription().getType() == ServerType.LOAD_BALANCER) ? connection : null;
        this.resourceManager = new ResourceManager(this.namespace, connectionSource, connectionToPin, this.commandCursorResult.getServerCursor());
    }
    
    @Override
    public boolean hasNext() {
        return Assertions.assertNotNull((Boolean)this.resourceManager.execute("Cursor has been closed", (java.util.function.Supplier<T>)this::doHasNext));
    }
    
    private boolean doHasNext() {
        if (this.nextBatch != null) {
            return true;
        }
        while (this.resourceManager.getServerCursor() != null) {
            this.getMore();
            if (!this.resourceManager.operable()) {
                throw new IllegalStateException("Cursor has been closed");
            }
            if (this.nextBatch != null) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public List<T> next() {
        return (List<T>)Assertions.assertNotNull((List)this.resourceManager.execute("Iterator has been closed", (java.util.function.Supplier<T>)this::doNext));
    }
    
    @Override
    public int available() {
        return (!this.resourceManager.operable() || this.nextBatch == null) ? 0 : this.nextBatch.size();
    }
    
    @Nullable
    private List<T> doNext() {
        if (!this.doHasNext()) {
            throw new NoSuchElementException();
        }
        final List<T> retVal = this.nextBatch;
        this.nextBatch = null;
        return retVal;
    }
    
    boolean isClosed() {
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
    
    public void remove() {
        throw new UnsupportedOperationException("Not implemented yet!");
    }
    
    @Override
    public void close() {
        this.resourceManager.close();
    }
    
    @Nullable
    @Override
    public List<T> tryNext() {
        return (List<T>)this.resourceManager.execute("Cursor has been closed", (java.util.function.Supplier<List>)(() -> {
            if (!this.tryHasNext()) {
                return null;
            }
            return this.doNext();
        }));
    }
    
    private boolean tryHasNext() {
        if (this.nextBatch != null) {
            return true;
        }
        if (this.resourceManager.getServerCursor() != null) {
            this.getMore();
        }
        return this.nextBatch != null;
    }
    
    @Nullable
    @Override
    public ServerCursor getServerCursor() {
        if (!this.resourceManager.operable()) {
            throw new IllegalStateException("Iterator has been closed");
        }
        return this.resourceManager.getServerCursor();
    }
    
    @Override
    public ServerAddress getServerAddress() {
        if (!this.resourceManager.operable()) {
            throw new IllegalStateException("Iterator has been closed");
        }
        return this.commandCursorResult.getServerAddress();
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
    
    private void getMore() {
        final ServerCursor serverCursor = Assertions.assertNotNull(this.resourceManager.getServerCursor());
        this.resourceManager.executeWithConnection((Consumer<Connection>)(connection -> {
            ServerCursor nextServerCursor;
            try {
                this.commandCursorResult = this.toCommandCursorResult(connection.getDescription().getServerAddress(), "nextBatch", Assertions.assertNotNull((BsonDocument)connection.command(this.namespace.getDatabaseName(), CommandBatchCursorHelper.getMoreCommandDocument(serverCursor.getId(), connection.getDescription(), this.namespace, this.batchSize, this.maxTimeMS, this.comment), CommandBatchCursorHelper.NO_OP_FIELD_NAME_VALIDATOR, ReadPreference.primary(), (Decoder<T>)CommandResultDocumentCodec.create(this.decoder, "nextBatch"), Assertions.assertNotNull(((CursorResourceManager<ConnectionSource, C>)this.resourceManager).getConnectionSource()))));
                nextServerCursor = this.commandCursorResult.getServerCursor();
            }
            catch (final MongoCommandException e) {
                throw CommandBatchCursorHelper.translateCommandException(e, serverCursor);
            }
            this.resourceManager.setServerCursor(nextServerCursor);
        }));
    }
    
    private CommandCursorResult<T> toCommandCursorResult(final ServerAddress serverAddress, final String fieldNameContainingBatch, final BsonDocument commandCursorDocument) {
        final CommandCursorResult<T> commandCursorResult = new CommandCursorResult<T>(serverAddress, fieldNameContainingBatch, commandCursorDocument);
        CommandBatchCursorHelper.logCommandCursorResult(commandCursorResult);
        this.nextBatch = (commandCursorResult.getResults().isEmpty() ? null : commandCursorResult.getResults());
        return commandCursorResult;
    }
    
    @ThreadSafe
    private static final class ResourceManager extends CursorResourceManager<ConnectionSource, Connection>
    {
        ResourceManager(final MongoNamespace namespace, final ConnectionSource connectionSource, @Nullable final Connection connectionToPin, @Nullable final ServerCursor serverCursor) {
            super(namespace, connectionSource, connectionToPin, serverCursor);
        }
        
        @Nullable
         <R> R execute(final String exceptionMessageIfClosed, final Supplier<R> operation) throws IllegalStateException {
            if (!this.tryStartOperation()) {
                throw new IllegalStateException(exceptionMessageIfClosed);
            }
            try {
                return (R)operation.get();
            }
            finally {
                this.endOperation();
            }
        }
        
        @Override
        void markAsPinned(final Connection connectionToPin, final Connection.PinningMode pinningMode) {
            connectionToPin.markAsPinned(pinningMode);
        }
        
        @Override
        void doClose() {
            if (this.isSkipReleasingServerResourcesOnClose()) {
                this.unsetServerCursor();
            }
            try {
                if (this.getServerCursor() != null) {
                    final Connection connection = this.getConnection();
                    try {
                        this.releaseServerResources(connection);
                    }
                    finally {
                        connection.release();
                    }
                }
            }
            catch (final MongoException ex) {}
            finally {
                this.unsetServerCursor();
                this.releaseClientResources();
            }
        }
        
        void executeWithConnection(final Consumer<Connection> action) {
            final Connection connection = this.getConnection();
            try {
                action.accept((Object)connection);
            }
            catch (final MongoSocketException e) {
                ((CursorResourceManager<CS, Connection>)this).onCorruptedConnection(connection, e);
                throw e;
            }
            finally {
                connection.release();
            }
        }
        
        private Connection getConnection() {
            Assertions.assertTrue(this.getState() != State.IDLE);
            final Connection pinnedConnection = ((CursorResourceManager<CS, Connection>)this).getPinnedConnection();
            if (pinnedConnection == null) {
                return Assertions.assertNotNull(((CursorResourceManager<ConnectionSource, C>)this).getConnectionSource()).getConnection();
            }
            return pinnedConnection.retain();
        }
        
        private void releaseServerResources(final Connection connection) {
            try {
                final ServerCursor localServerCursor = this.getServerCursor();
                if (localServerCursor != null) {
                    this.killServerCursor(this.getNamespace(), localServerCursor, connection);
                }
            }
            finally {
                this.unsetServerCursor();
            }
        }
        
        private void killServerCursor(final MongoNamespace namespace, final ServerCursor localServerCursor, final Connection localConnection) {
            localConnection.command(namespace.getDatabaseName(), CommandBatchCursorHelper.getKillCursorsCommand(namespace, localServerCursor), CommandBatchCursorHelper.NO_OP_FIELD_NAME_VALIDATOR, ReadPreference.primary(), (Decoder<Object>)new BsonDocumentCodec(), Assertions.assertNotNull(((CursorResourceManager<ConnectionSource, C>)this).getConnectionSource()));
        }
    }
}
