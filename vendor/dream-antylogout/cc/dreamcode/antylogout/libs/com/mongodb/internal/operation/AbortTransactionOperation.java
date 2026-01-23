package cc.dreamcode.antylogout.libs.com.mongodb.internal.operation;

import cc.dreamcode.antylogout.libs.com.mongodb.connection.ConnectionDescription;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.ServerDescription;
import cc.dreamcode.antylogout.libs.com.mongodb.Function;
import cc.dreamcode.antylogout.libs.org.bson.BsonValue;
import cc.dreamcode.antylogout.libs.com.mongodb.lang.Nullable;
import cc.dreamcode.antylogout.libs.com.mongodb.WriteConcern;
import cc.dreamcode.antylogout.libs.org.bson.BsonDocument;

public class AbortTransactionOperation extends TransactionOperation
{
    private BsonDocument recoveryToken;
    
    public AbortTransactionOperation(final WriteConcern writeConcern) {
        super(writeConcern);
    }
    
    public AbortTransactionOperation recoveryToken(@Nullable final BsonDocument recoveryToken) {
        this.recoveryToken = recoveryToken;
        return this;
    }
    
    @Override
    protected String getCommandName() {
        return "abortTransaction";
    }
    
    @Override
    CommandOperationHelper.CommandCreator getCommandCreator() {
        final CommandOperationHelper.CommandCreator creator = super.getCommandCreator();
        if (this.recoveryToken != null) {
            return (serverDescription, connectionDescription) -> creator.create(serverDescription, connectionDescription).append("recoveryToken", this.recoveryToken);
        }
        return creator;
    }
    
    @Override
    protected Function<BsonDocument, BsonDocument> getRetryCommandModifier() {
        return (Function<BsonDocument, BsonDocument>)(cmd -> cmd);
    }
}
