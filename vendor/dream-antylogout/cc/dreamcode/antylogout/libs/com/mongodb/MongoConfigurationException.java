package cc.dreamcode.antylogout.libs.com.mongodb;

public class MongoConfigurationException extends MongoClientException
{
    private static final long serialVersionUID = -2343119787572079323L;
    
    public MongoConfigurationException(final String message) {
        super(message);
    }
    
    public MongoConfigurationException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
