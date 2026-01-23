package cc.dreamcode.antylogout.libs.com.mongodb;

import cc.dreamcode.antylogout.libs.org.bson.BsonString;
import cc.dreamcode.antylogout.libs.org.bson.BsonWriter;
import cc.dreamcode.antylogout.libs.org.bson.codecs.EncoderContext;
import cc.dreamcode.antylogout.libs.org.bson.codecs.BsonDocumentCodec;
import java.io.Writer;
import cc.dreamcode.antylogout.libs.org.bson.json.JsonWriter;
import java.io.StringWriter;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.ExceptionUtils;
import cc.dreamcode.antylogout.libs.org.bson.BsonDocument;

public class MongoCommandException extends MongoServerException
{
    private static final long serialVersionUID = 8160676451944215078L;
    private final BsonDocument response;
    
    public MongoCommandException(final BsonDocument response, final ServerAddress address) {
        super(ExceptionUtils.MongoCommandExceptionUtils.extractErrorCode(response), ExceptionUtils.MongoCommandExceptionUtils.extractErrorCodeName(response), String.format("Command failed with error %s: '%s' on server %s. The full response is %s", new Object[] { extractErrorCodeAndName(response), extractErrorMessage(response), address, getResponseAsJson(response) }), address);
        this.response = response;
        this.addLabels(ExceptionUtils.MongoCommandExceptionUtils.extractErrorLabelsAsBson(response));
    }
    
    public int getErrorCode() {
        return this.getCode();
    }
    
    @Override
    public String getErrorCodeName() {
        return super.getErrorCodeName();
    }
    
    public String getErrorMessage() {
        return extractErrorMessage(this.response);
    }
    
    public BsonDocument getResponse() {
        return this.response;
    }
    
    private static String getResponseAsJson(final BsonDocument commandResponse) {
        final StringWriter writer = new StringWriter();
        final JsonWriter jsonWriter = new JsonWriter((Writer)writer);
        new BsonDocumentCodec().encode((BsonWriter)jsonWriter, commandResponse, EncoderContext.builder().build());
        return writer.toString();
    }
    
    private static String extractErrorCodeAndName(final BsonDocument response) {
        final int errorCode = ExceptionUtils.MongoCommandExceptionUtils.extractErrorCode(response);
        final String errorCodeName = ExceptionUtils.MongoCommandExceptionUtils.extractErrorCodeName(response);
        if (errorCodeName.isEmpty()) {
            return Integer.toString(errorCode);
        }
        return String.format("%d (%s)", new Object[] { errorCode, errorCodeName });
    }
    
    private static String extractErrorMessage(final BsonDocument response) {
        final String errorMessage = response.getString("errmsg", new BsonString("")).getValue();
        if (errorMessage == null) {
            throw new MongoInternalException("This value should not be null");
        }
        return errorMessage;
    }
}
