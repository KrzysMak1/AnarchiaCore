package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.profiles.exceptions;

public class MojangAPIException extends ProfileException
{
    public MojangAPIException(final String message) {
        super(message);
    }
    
    public MojangAPIException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
