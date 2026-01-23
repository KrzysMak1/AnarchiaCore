package cc.dreamcode.antylogout.libs.com.mongodb.internal.session;

import cc.dreamcode.antylogout.libs.org.bson.BsonWriter;
import cc.dreamcode.antylogout.libs.org.bson.codecs.EncoderContext;
import java.util.UUID;
import cc.dreamcode.antylogout.libs.org.bson.BsonDocumentWriter;
import cc.dreamcode.antylogout.libs.org.bson.codecs.UuidCodec;
import cc.dreamcode.antylogout.libs.org.bson.UuidRepresentation;
import cc.dreamcode.antylogout.libs.org.bson.BsonBinary;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.Connection;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoException;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.BindingContext;
import cc.dreamcode.antylogout.libs.org.bson.codecs.Decoder;
import cc.dreamcode.antylogout.libs.org.bson.FieldNameValidator;
import cc.dreamcode.antylogout.libs.org.bson.codecs.BsonDocumentCodec;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.validator.NoOpFieldNameValidator;
import cc.dreamcode.antylogout.libs.org.bson.BsonDocument;
import cc.dreamcode.antylogout.libs.org.bson.BsonValue;
import cc.dreamcode.antylogout.libs.org.bson.BsonArray;
import java.util.Iterator;
import java.util.Collections;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.ServerDescription;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.ClusterDescription;
import java.util.List;
import cc.dreamcode.antylogout.libs.com.mongodb.selector.ServerSelector;
import cc.dreamcode.antylogout.libs.com.mongodb.RequestContext;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.StaticBindingContext;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.OperationContext;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.IgnorableRequestContext;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.NoOpSessionContext;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.selector.ReadPreferenceServerSelector;
import cc.dreamcode.antylogout.libs.com.mongodb.ReadPreference;
import cc.dreamcode.antylogout.libs.com.mongodb.assertions.Assertions;
import cc.dreamcode.antylogout.libs.com.mongodb.session.ServerSession;
import java.util.concurrent.atomic.LongAdder;
import cc.dreamcode.antylogout.libs.com.mongodb.lang.Nullable;
import cc.dreamcode.antylogout.libs.com.mongodb.ServerApi;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.Cluster;
import java.util.concurrent.ConcurrentLinkedDeque;

public class ServerSessionPool
{
    private final ConcurrentLinkedDeque<ServerSessionImpl> available;
    private final Cluster cluster;
    private final Clock clock;
    private volatile boolean closed;
    @Nullable
    private final ServerApi serverApi;
    private final LongAdder inUseCount;
    
    public ServerSessionPool(final Cluster cluster, @Nullable final ServerApi serverApi) {
        this(cluster, serverApi, System::currentTimeMillis);
    }
    
    public ServerSessionPool(final Cluster cluster, @Nullable final ServerApi serverApi, final Clock clock) {
        this.available = (ConcurrentLinkedDeque<ServerSessionImpl>)new ConcurrentLinkedDeque();
        this.inUseCount = new LongAdder();
        this.cluster = cluster;
        this.serverApi = serverApi;
        this.clock = clock;
    }
    
    public ServerSession get() {
        Assertions.isTrue("server session pool is open", !this.closed);
        ServerSessionImpl serverSession;
        for (serverSession = (ServerSessionImpl)this.available.pollLast(); serverSession != null && this.shouldPrune(serverSession); serverSession = (ServerSessionImpl)this.available.pollLast()) {
            serverSession.close();
        }
        if (serverSession == null) {
            serverSession = new ServerSessionImpl();
        }
        this.inUseCount.increment();
        return serverSession;
    }
    
    public void release(final ServerSession serverSession) {
        this.inUseCount.decrement();
        final ServerSessionImpl serverSessionImpl = (ServerSessionImpl)serverSession;
        if (serverSessionImpl.isMarkedDirty()) {
            serverSessionImpl.close();
        }
        else {
            this.available.addLast((Object)serverSessionImpl);
        }
    }
    
    public long getInUseCount() {
        return this.inUseCount.sum();
    }
    
    public void close() {
        this.closed = true;
        this.endClosedSessions();
    }
    
    private void endClosedSessions() {
        final List<BsonDocument> identifiers = this.drainPool();
        if (identifiers.isEmpty()) {
            return;
        }
        final ReadPreference primaryPreferred = ReadPreference.primaryPreferred();
        final List<ServerDescription> primaryPreferredServers = new ReadPreferenceServerSelector(primaryPreferred).select(this.cluster.getCurrentDescription());
        if (primaryPreferredServers.isEmpty()) {
            return;
        }
        Connection connection = null;
        try {
            final StaticBindingContext context = new StaticBindingContext(NoOpSessionContext.INSTANCE, this.serverApi, IgnorableRequestContext.INSTANCE, new OperationContext());
            connection = this.cluster.selectServer(new ServerSelector() {
                @Override
                public List<ServerDescription> select(final ClusterDescription clusterDescription) {
                    for (final ServerDescription cur : clusterDescription.getServerDescriptions()) {
                        if (cur.getAddress().equals(((ServerDescription)primaryPreferredServers.get(0)).getAddress())) {
                            return (List<ServerDescription>)Collections.singletonList((Object)cur);
                        }
                    }
                    return (List<ServerDescription>)Collections.emptyList();
                }
                
                @Override
                public String toString() {
                    return "ReadPreferenceServerSelector{readPreference=" + (Object)primaryPreferred + '}';
                }
            }, context.getOperationContext()).getServer().getConnection(context.getOperationContext());
            connection.command("admin", new BsonDocument("endSessions", new BsonArray(identifiers)), new NoOpFieldNameValidator(), ReadPreference.primaryPreferred(), (Decoder<Object>)new BsonDocumentCodec(), context);
        }
        catch (final MongoException ex) {}
        finally {
            if (connection != null) {
                connection.release();
            }
        }
    }
    
    private List<BsonDocument> drainPool() {
        final List<BsonDocument> identifiers = (List<BsonDocument>)new ArrayList(this.available.size());
        for (ServerSessionImpl nextSession = (ServerSessionImpl)this.available.pollFirst(); nextSession != null; nextSession = (ServerSessionImpl)this.available.pollFirst()) {
            identifiers.add((Object)nextSession.getIdentifier());
        }
        return identifiers;
    }
    
    private boolean shouldPrune(final ServerSessionImpl serverSession) {
        final Integer logicalSessionTimeoutMinutes = this.cluster.getCurrentDescription().getLogicalSessionTimeoutMinutes();
        if (logicalSessionTimeoutMinutes == null) {
            return false;
        }
        final long currentTimeMillis = this.clock.millis();
        final long timeSinceLastUse = currentTimeMillis - serverSession.getLastUsedAtMillis();
        final long oneMinuteFromTimeout = TimeUnit.MINUTES.toMillis((long)(logicalSessionTimeoutMinutes - 1));
        return timeSinceLastUse > oneMinuteFromTimeout;
    }
    
    private BsonBinary createNewServerSessionIdentifier() {
        final UuidCodec uuidCodec = new UuidCodec(UuidRepresentation.STANDARD);
        final BsonDocument holder = new BsonDocument();
        final BsonDocumentWriter bsonDocumentWriter = new BsonDocumentWriter(holder);
        bsonDocumentWriter.writeStartDocument();
        bsonDocumentWriter.writeName("id");
        uuidCodec.encode((BsonWriter)bsonDocumentWriter, UUID.randomUUID(), EncoderContext.builder().build());
        bsonDocumentWriter.writeEndDocument();
        return holder.getBinary("id");
    }
    
    final class ServerSessionImpl implements ServerSession
    {
        private final BsonDocument identifier;
        private long transactionNumber;
        private volatile long lastUsedAtMillis;
        private volatile boolean closed;
        private volatile boolean dirty;
        
        ServerSessionImpl() {
            this.transactionNumber = 0L;
            this.lastUsedAtMillis = ServerSessionPool.this.clock.millis();
            this.dirty = false;
            this.identifier = new BsonDocument("id", ServerSessionPool.this.createNewServerSessionIdentifier());
        }
        
        void close() {
            this.closed = true;
        }
        
        long getLastUsedAtMillis() {
            return this.lastUsedAtMillis;
        }
        
        @Override
        public long getTransactionNumber() {
            return this.transactionNumber;
        }
        
        @Override
        public BsonDocument getIdentifier() {
            this.lastUsedAtMillis = ServerSessionPool.this.clock.millis();
            return this.identifier;
        }
        
        @Override
        public long advanceTransactionNumber() {
            return ++this.transactionNumber;
        }
        
        @Override
        public boolean isClosed() {
            return this.closed;
        }
        
        @Override
        public void markDirty() {
            this.dirty = true;
        }
        
        @Override
        public boolean isMarkedDirty() {
            return this.dirty;
        }
    }
    
    interface Clock
    {
        long millis();
    }
}
