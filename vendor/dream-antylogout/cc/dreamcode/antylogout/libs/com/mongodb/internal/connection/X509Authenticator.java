package cc.dreamcode.antylogout.libs.com.mongodb.internal.connection;

import cc.dreamcode.antylogout.libs.com.mongodb.internal.diagnostics.logging.Loggers;
import cc.dreamcode.antylogout.libs.com.mongodb.AuthenticationMechanism;
import cc.dreamcode.antylogout.libs.org.bson.BsonInt32;
import cc.dreamcode.antylogout.libs.org.bson.BsonValue;
import cc.dreamcode.antylogout.libs.org.bson.BsonString;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.ErrorHandlingResultCallback;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.SingleResultCallback;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoCommandException;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoSecurityException;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.ConnectionDescription;
import cc.dreamcode.antylogout.libs.com.mongodb.lang.Nullable;
import cc.dreamcode.antylogout.libs.com.mongodb.ServerApi;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.ClusterConnectionMode;
import cc.dreamcode.antylogout.libs.org.bson.BsonDocument;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.diagnostics.logging.Logger;

class X509Authenticator extends Authenticator implements SpeculativeAuthenticator
{
    public static final Logger LOGGER;
    private BsonDocument speculativeAuthenticateResponse;
    
    X509Authenticator(final MongoCredentialWithCache credential, final ClusterConnectionMode clusterConnectionMode, @Nullable final ServerApi serverApi) {
        super(credential, clusterConnectionMode, serverApi);
    }
    
    @Override
    void authenticate(final InternalConnection connection, final ConnectionDescription connectionDescription) {
        if (this.speculativeAuthenticateResponse != null) {
            return;
        }
        try {
            final BsonDocument authCommand = this.getAuthCommand(this.getMongoCredential().getUserName());
            CommandHelper.executeCommand(this.getMongoCredential().getSource(), authCommand, this.getClusterConnectionMode(), this.getServerApi(), connection);
        }
        catch (final MongoCommandException e) {
            throw new MongoSecurityException(this.getMongoCredential(), "Exception authenticating", (Throwable)e);
        }
    }
    
    @Override
    void authenticateAsync(final InternalConnection connection, final ConnectionDescription connectionDescription, final SingleResultCallback<Void> callback) {
        if (this.speculativeAuthenticateResponse != null) {
            callback.onResult(null, null);
        }
        else {
            final SingleResultCallback<Void> errHandlingCallback = ErrorHandlingResultCallback.errorHandlingCallback(callback, X509Authenticator.LOGGER);
            try {
                final Throwable t;
                CommandHelper.executeCommandAsync(this.getMongoCredential().getSource(), this.getAuthCommand(this.getMongoCredential().getUserName()), this.getClusterConnectionMode(), this.getServerApi(), connection, (nonceResult, t) -> {
                    if (t != null) {
                        errHandlingCallback.onResult(null, this.translateThrowable(t));
                    }
                    else {
                        errHandlingCallback.onResult(null, null);
                    }
                });
            }
            catch (final Throwable t) {
                errHandlingCallback.onResult(null, t);
            }
        }
    }
    
    @Override
    public BsonDocument createSpeculativeAuthenticateCommand(final InternalConnection connection) {
        return this.getAuthCommand(this.getMongoCredential().getUserName()).append("db", new BsonString("$external"));
    }
    
    @Override
    public void setSpeculativeAuthenticateResponse(final BsonDocument response) {
        this.speculativeAuthenticateResponse = response;
    }
    
    @Override
    public BsonDocument getSpeculativeAuthenticateResponse() {
        return this.speculativeAuthenticateResponse;
    }
    
    private BsonDocument getAuthCommand(@Nullable final String userName) {
        final BsonDocument cmd = new BsonDocument();
        cmd.put("authenticate", new BsonInt32(1));
        if (userName != null) {
            cmd.put("user", new BsonString(userName));
        }
        cmd.put("mechanism", new BsonString(AuthenticationMechanism.MONGODB_X509.getMechanismName()));
        return cmd;
    }
    
    private Throwable translateThrowable(final Throwable t) {
        if (t instanceof MongoCommandException) {
            return (Throwable)new MongoSecurityException(this.getMongoCredential(), "Exception authenticating", t);
        }
        return t;
    }
    
    static {
        LOGGER = Loggers.getLogger("authenticator");
    }
}
