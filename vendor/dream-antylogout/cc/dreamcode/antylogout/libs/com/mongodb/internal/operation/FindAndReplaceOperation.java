package cc.dreamcode.antylogout.libs.com.mongodb.internal.operation;

import cc.dreamcode.antylogout.libs.org.bson.BsonBoolean;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.ConnectionDescription;
import java.util.Map;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.validator.MappedFieldNameValidator;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.validator.NoOpFieldNameValidator;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.validator.ReplacingDocumentFieldNameValidator;
import java.util.HashMap;
import cc.dreamcode.antylogout.libs.org.bson.FieldNameValidator;
import cc.dreamcode.antylogout.libs.org.bson.BsonValue;
import cc.dreamcode.antylogout.libs.com.mongodb.client.model.Collation;
import java.util.concurrent.TimeUnit;
import cc.dreamcode.antylogout.libs.com.mongodb.lang.Nullable;
import cc.dreamcode.antylogout.libs.com.mongodb.assertions.Assertions;
import cc.dreamcode.antylogout.libs.org.bson.codecs.Decoder;
import cc.dreamcode.antylogout.libs.com.mongodb.WriteConcern;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoNamespace;
import cc.dreamcode.antylogout.libs.org.bson.BsonDocument;

public class FindAndReplaceOperation<T> extends BaseFindAndModifyOperation<T>
{
    private final BsonDocument replacement;
    private boolean returnOriginal;
    private boolean upsert;
    private Boolean bypassDocumentValidation;
    
    public FindAndReplaceOperation(final MongoNamespace namespace, final WriteConcern writeConcern, final boolean retryWrites, final Decoder<T> decoder, final BsonDocument replacement) {
        super(namespace, writeConcern, retryWrites, decoder);
        this.returnOriginal = true;
        this.replacement = Assertions.notNull("replacement", replacement);
    }
    
    public BsonDocument getReplacement() {
        return this.replacement;
    }
    
    public boolean isReturnOriginal() {
        return this.returnOriginal;
    }
    
    public FindAndReplaceOperation<T> returnOriginal(final boolean returnOriginal) {
        this.returnOriginal = returnOriginal;
        return this;
    }
    
    public boolean isUpsert() {
        return this.upsert;
    }
    
    public FindAndReplaceOperation<T> upsert(final boolean upsert) {
        this.upsert = upsert;
        return this;
    }
    
    public Boolean getBypassDocumentValidation() {
        return this.bypassDocumentValidation;
    }
    
    public FindAndReplaceOperation<T> bypassDocumentValidation(@Nullable final Boolean bypassDocumentValidation) {
        this.bypassDocumentValidation = bypassDocumentValidation;
        return this;
    }
    
    @Override
    public FindAndReplaceOperation<T> filter(@Nullable final BsonDocument filter) {
        super.filter(filter);
        return this;
    }
    
    @Override
    public FindAndReplaceOperation<T> projection(@Nullable final BsonDocument projection) {
        super.projection(projection);
        return this;
    }
    
    @Override
    public FindAndReplaceOperation<T> maxTime(final long maxTime, final TimeUnit timeUnit) {
        super.maxTime(maxTime, timeUnit);
        return this;
    }
    
    @Override
    public FindAndReplaceOperation<T> sort(@Nullable final BsonDocument sort) {
        super.sort(sort);
        return this;
    }
    
    @Override
    public FindAndReplaceOperation<T> hint(@Nullable final BsonDocument hint) {
        super.hint(hint);
        return this;
    }
    
    @Override
    public FindAndReplaceOperation<T> hintString(@Nullable final String hint) {
        super.hintString(hint);
        return this;
    }
    
    @Override
    public FindAndReplaceOperation<T> collation(@Nullable final Collation collation) {
        super.collation(collation);
        return this;
    }
    
    @Override
    public FindAndReplaceOperation<T> comment(@Nullable final BsonValue comment) {
        super.comment(comment);
        return this;
    }
    
    @Override
    public FindAndReplaceOperation<T> let(@Nullable final BsonDocument variables) {
        super.let(variables);
        return this;
    }
    
    @Override
    protected FieldNameValidator getFieldNameValidator() {
        final Map<String, FieldNameValidator> map = (Map<String, FieldNameValidator>)new HashMap();
        map.put((Object)"update", (Object)new ReplacingDocumentFieldNameValidator());
        return new MappedFieldNameValidator(new NoOpFieldNameValidator(), map);
    }
    
    @Override
    protected void specializeCommand(final BsonDocument commandDocument, final ConnectionDescription connectionDescription) {
        commandDocument.put("new", new BsonBoolean(!this.isReturnOriginal()));
        DocumentHelper.putIfTrue(commandDocument, "upsert", this.isUpsert());
        commandDocument.put("update", this.getReplacement());
        if (this.bypassDocumentValidation != null) {
            commandDocument.put("bypassDocumentValidation", BsonBoolean.valueOf(this.bypassDocumentValidation));
        }
    }
}
