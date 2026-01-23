package cc.dreamcode.antylogout.libs.com.mongodb.internal.operation;

import cc.dreamcode.antylogout.libs.org.bson.BsonArray;
import cc.dreamcode.antylogout.libs.org.bson.BsonBoolean;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.ConnectionDescription;
import java.util.Map;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.validator.MappedFieldNameValidator;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.validator.NoOpFieldNameValidator;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.validator.UpdateFieldNameValidator;
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
import java.util.List;
import cc.dreamcode.antylogout.libs.org.bson.BsonDocument;

public class FindAndUpdateOperation<T> extends BaseFindAndModifyOperation<T>
{
    private final BsonDocument update;
    private final List<BsonDocument> updatePipeline;
    private boolean returnOriginal;
    private boolean upsert;
    private Boolean bypassDocumentValidation;
    private List<BsonDocument> arrayFilters;
    
    public FindAndUpdateOperation(final MongoNamespace namespace, final WriteConcern writeConcern, final boolean retryWrites, final Decoder<T> decoder, final BsonDocument update) {
        super(namespace, writeConcern, retryWrites, decoder);
        this.returnOriginal = true;
        this.update = Assertions.notNull("update", update);
        this.updatePipeline = null;
    }
    
    public FindAndUpdateOperation(final MongoNamespace namespace, final WriteConcern writeConcern, final boolean retryWrites, final Decoder<T> decoder, final List<BsonDocument> update) {
        super(namespace, writeConcern, retryWrites, decoder);
        this.returnOriginal = true;
        this.updatePipeline = update;
        this.update = null;
    }
    
    @Nullable
    public BsonDocument getUpdate() {
        return this.update;
    }
    
    @Nullable
    public List<BsonDocument> getUpdatePipeline() {
        return this.updatePipeline;
    }
    
    public boolean isReturnOriginal() {
        return this.returnOriginal;
    }
    
    public FindAndUpdateOperation<T> returnOriginal(final boolean returnOriginal) {
        this.returnOriginal = returnOriginal;
        return this;
    }
    
    public boolean isUpsert() {
        return this.upsert;
    }
    
    public FindAndUpdateOperation<T> upsert(final boolean upsert) {
        this.upsert = upsert;
        return this;
    }
    
    public Boolean getBypassDocumentValidation() {
        return this.bypassDocumentValidation;
    }
    
    public FindAndUpdateOperation<T> bypassDocumentValidation(@Nullable final Boolean bypassDocumentValidation) {
        this.bypassDocumentValidation = bypassDocumentValidation;
        return this;
    }
    
    public FindAndUpdateOperation<T> arrayFilters(@Nullable final List<BsonDocument> arrayFilters) {
        this.arrayFilters = arrayFilters;
        return this;
    }
    
    public List<BsonDocument> getArrayFilters() {
        return this.arrayFilters;
    }
    
    @Override
    public FindAndUpdateOperation<T> filter(@Nullable final BsonDocument filter) {
        super.filter(filter);
        return this;
    }
    
    @Override
    public FindAndUpdateOperation<T> projection(@Nullable final BsonDocument projection) {
        super.projection(projection);
        return this;
    }
    
    @Override
    public FindAndUpdateOperation<T> maxTime(final long maxTime, final TimeUnit timeUnit) {
        super.maxTime(maxTime, timeUnit);
        return this;
    }
    
    @Override
    public FindAndUpdateOperation<T> sort(@Nullable final BsonDocument sort) {
        super.sort(sort);
        return this;
    }
    
    @Override
    public FindAndUpdateOperation<T> hint(@Nullable final BsonDocument hint) {
        super.hint(hint);
        return this;
    }
    
    @Override
    public FindAndUpdateOperation<T> hintString(@Nullable final String hint) {
        super.hintString(hint);
        return this;
    }
    
    @Override
    public FindAndUpdateOperation<T> collation(@Nullable final Collation collation) {
        super.collation(collation);
        return this;
    }
    
    @Override
    public FindAndUpdateOperation<T> comment(@Nullable final BsonValue comment) {
        super.comment(comment);
        return this;
    }
    
    @Override
    public FindAndUpdateOperation<T> let(@Nullable final BsonDocument variables) {
        super.let(variables);
        return this;
    }
    
    @Override
    protected FieldNameValidator getFieldNameValidator() {
        final Map<String, FieldNameValidator> map = (Map<String, FieldNameValidator>)new HashMap();
        map.put((Object)"update", (Object)new UpdateFieldNameValidator());
        return new MappedFieldNameValidator(new NoOpFieldNameValidator(), map);
    }
    
    @Override
    protected void specializeCommand(final BsonDocument commandDocument, final ConnectionDescription connectionDescription) {
        commandDocument.put("new", new BsonBoolean(!this.isReturnOriginal()));
        DocumentHelper.putIfTrue(commandDocument, "upsert", this.isUpsert());
        if (this.getUpdatePipeline() != null) {
            commandDocument.put("update", new BsonArray(this.getUpdatePipeline()));
        }
        else {
            DocumentHelper.putIfNotNull(commandDocument, "update", this.getUpdate());
        }
        if (this.bypassDocumentValidation != null) {
            commandDocument.put("bypassDocumentValidation", BsonBoolean.valueOf(this.bypassDocumentValidation));
        }
        if (this.arrayFilters != null) {
            commandDocument.put("arrayFilters", new BsonArray(this.arrayFilters));
        }
    }
}
