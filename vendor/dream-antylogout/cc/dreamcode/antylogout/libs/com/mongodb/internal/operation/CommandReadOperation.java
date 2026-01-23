package cc.dreamcode.antylogout.libs.com.mongodb.internal.operation;

import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.Connection;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.ConnectionSource;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.AsyncConnection;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.AsyncConnectionSource;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.ConnectionDescription;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.ServerDescription;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.SingleResultCallback;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.AsyncReadBinding;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.binding.ReadBinding;
import cc.dreamcode.antylogout.libs.com.mongodb.assertions.Assertions;
import cc.dreamcode.antylogout.libs.org.bson.codecs.Decoder;
import cc.dreamcode.antylogout.libs.org.bson.BsonDocument;

public class CommandReadOperation<T> implements AsyncReadOperation<T>, ReadOperation<T>
{
    private final String databaseName;
    private final BsonDocument command;
    private final Decoder<T> decoder;
    
    public CommandReadOperation(final String databaseName, final BsonDocument command, final Decoder<T> decoder) {
        this.databaseName = Assertions.notNull("databaseName", databaseName);
        this.command = Assertions.notNull("command", command);
        this.decoder = Assertions.notNull("decoder", decoder);
    }
    
    @Override
    public T execute(final ReadBinding binding) {
        return SyncOperationHelper.executeRetryableRead(binding, this.databaseName, this.getCommandCreator(), this.decoder, (result, source, connection) -> result, false);
    }
    
    @Override
    public void executeAsync(final AsyncReadBinding binding, final SingleResultCallback<T> callback) {
        AsyncOperationHelper.executeRetryableReadAsync(binding, this.databaseName, this.getCommandCreator(), this.decoder, (result, source, connection) -> result, false, callback);
    }
    
    private CommandOperationHelper.CommandCreator getCommandCreator() {
        return (serverDescription, connectionDescription) -> this.command;
    }
}
