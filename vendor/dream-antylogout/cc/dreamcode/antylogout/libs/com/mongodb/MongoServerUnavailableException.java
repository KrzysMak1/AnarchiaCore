package cc.dreamcode.antylogout.libs.com.mongodb;

public final class MongoServerUnavailableException extends MongoClientException
{
    private static final long serialVersionUID = 5465094535584085700L;
    
    public MongoServerUnavailableException(final String message) {
        super(message);
    }
}
