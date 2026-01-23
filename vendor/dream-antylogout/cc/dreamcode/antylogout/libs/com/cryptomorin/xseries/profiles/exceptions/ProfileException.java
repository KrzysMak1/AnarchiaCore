package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.profiles.exceptions;

public class ProfileException extends RuntimeException
{
    public ProfileException() {
    }
    
    public ProfileException(final String message) {
        super(message);
    }
    
    public ProfileException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public ProfileException(final Throwable cause) {
        super(cause);
    }
    
    public ProfileException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
