package cc.dreamcode.antylogout.libs.com.mongodb.internal.operation;

import cc.dreamcode.antylogout.libs.com.mongodb.connection.ConnectionDescription;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.ServerDescription;
import cc.dreamcode.antylogout.libs.com.mongodb.Function;
import cc.dreamcode.antylogout.libs.org.bson.BsonValue;
import cc.dreamcode.antylogout.libs.org.bson.BsonInt32;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.ErrorHandlingResultCallback;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.SingleResultCallback;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.AsyncWriteBinding;
import cc.dreamcode.antylogout.libs.org.bson.BsonDocument;
import cc.dreamcode.antylogout.libs.org.bson.codecs.Decoder;
import cc.dreamcode.antylogout.libs.org.bson.FieldNameValidator;
import cc.dreamcode.antylogout.libs.com.mongodb.ReadPreference;
import cc.dreamcode.antylogout.libs.org.bson.codecs.BsonDocumentCodec;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.validator.NoOpFieldNameValidator;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.WriteBinding;
import cc.dreamcode.antylogout.libs.com.mongodb.assertions.Assertions;
import cc.dreamcode.antylogout.libs.com.mongodb.WriteConcern;

public abstract class TransactionOperation implements WriteOperation<Void>, AsyncWriteOperation<Void>
{
    private final WriteConcern writeConcern;
    
    TransactionOperation(final WriteConcern writeConcern) {
        this.writeConcern = Assertions.notNull("writeConcern", writeConcern);
    }
    
    public WriteConcern getWriteConcern() {
        return this.writeConcern;
    }
    
    @Override
    public Void execute(final WriteBinding binding) {
        Assertions.isTrue("in transaction", binding.getSessionContext().hasActiveTransaction());
        return SyncOperationHelper.executeRetryableWrite(binding, "admin", null, new NoOpFieldNameValidator(), new BsonDocumentCodec(), this.getCommandCreator(), SyncOperationHelper.writeConcernErrorTransformer(), this.getRetryCommandModifier());
    }
    
    @Override
    public void executeAsync(final AsyncWriteBinding binding, final SingleResultCallback<Void> callback) {
        Assertions.isTrue("in transaction", binding.getSessionContext().hasActiveTransaction());
        AsyncOperationHelper.executeRetryableWriteAsync(binding, "admin", null, new NoOpFieldNameValidator(), new BsonDocumentCodec(), this.getCommandCreator(), AsyncOperationHelper.writeConcernErrorTransformerAsync(), this.getRetryCommandModifier(), (SingleResultCallback<Void>)ErrorHandlingResultCallback.errorHandlingCallback((SingleResultCallback<R>)callback, OperationHelper.LOGGER));
    }
    
    CommandOperationHelper.CommandCreator getCommandCreator() {
        return (serverDescription, connectionDescription) -> {
            new BsonDocument(this.getCommandName(), new BsonInt32(1));
            final BsonDocument bsonDocument;
            final BsonDocument command = bsonDocument;
            if (!this.writeConcern.isServerDefault()) {
                command.put("writeConcern", this.writeConcern.asDocument());
            }
            return command;
        };
    }
    
    protected abstract String getCommandName();
    
    protected abstract Function<BsonDocument, BsonDocument> getRetryCommandModifier();
}
