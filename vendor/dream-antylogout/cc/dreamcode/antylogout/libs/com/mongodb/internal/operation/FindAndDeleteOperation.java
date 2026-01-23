package cc.dreamcode.antylogout.libs.com.mongodb.internal.operation;

import cc.dreamcode.antylogout.libs.org.bson.BsonBoolean;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.ConnectionDescription;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.validator.NoOpFieldNameValidator;
import cc.dreamcode.antylogout.libs.org.bson.FieldNameValidator;
import cc.dreamcode.antylogout.libs.org.bson.BsonValue;
import cc.dreamcode.antylogout.libs.com.mongodb.client.model.Collation;
import java.util.concurrent.TimeUnit;
import cc.dreamcode.antylogout.libs.com.mongodb.lang.Nullable;
import cc.dreamcode.antylogout.libs.org.bson.BsonDocument;
import cc.dreamcode.antylogout.libs.org.bson.codecs.Decoder;
import cc.dreamcode.antylogout.libs.com.mongodb.WriteConcern;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoNamespace;

public class FindAndDeleteOperation<T> extends BaseFindAndModifyOperation<T>
{
    public FindAndDeleteOperation(final MongoNamespace namespace, final WriteConcern writeConcern, final boolean retryWrites, final Decoder<T> decoder) {
        super(namespace, writeConcern, retryWrites, decoder);
    }
    
    @Override
    public FindAndDeleteOperation<T> filter(@Nullable final BsonDocument filter) {
        super.filter(filter);
        return this;
    }
    
    @Override
    public FindAndDeleteOperation<T> projection(@Nullable final BsonDocument projection) {
        super.projection(projection);
        return this;
    }
    
    @Override
    public FindAndDeleteOperation<T> maxTime(final long maxTime, final TimeUnit timeUnit) {
        super.maxTime(maxTime, timeUnit);
        return this;
    }
    
    @Override
    public FindAndDeleteOperation<T> sort(@Nullable final BsonDocument sort) {
        super.sort(sort);
        return this;
    }
    
    @Override
    public FindAndDeleteOperation<T> hint(@Nullable final BsonDocument hint) {
        super.hint(hint);
        return this;
    }
    
    @Override
    public FindAndDeleteOperation<T> hintString(@Nullable final String hint) {
        super.hintString(hint);
        return this;
    }
    
    @Override
    public FindAndDeleteOperation<T> collation(@Nullable final Collation collation) {
        super.collation(collation);
        return this;
    }
    
    @Override
    public FindAndDeleteOperation<T> comment(@Nullable final BsonValue comment) {
        super.comment(comment);
        return this;
    }
    
    @Override
    public FindAndDeleteOperation<T> let(@Nullable final BsonDocument variables) {
        super.let(variables);
        return this;
    }
    
    @Override
    protected FieldNameValidator getFieldNameValidator() {
        return new NoOpFieldNameValidator();
    }
    
    @Override
    protected void specializeCommand(final BsonDocument commandDocument, final ConnectionDescription connectionDescription) {
        commandDocument.put("remove", BsonBoolean.TRUE);
    }
}
