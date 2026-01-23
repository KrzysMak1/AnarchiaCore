package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.profiles.exceptions;

import org.jetbrains.annotations.NotNull;

public class InvalidProfileException extends ProfileException
{
    private final String value;
    
    public InvalidProfileException(final String value, final String message) {
        super(message);
        this.value = value;
    }
    
    public InvalidProfileException(final String value, final String message, final Throwable cause) {
        super(message, cause);
        this.value = value;
    }
    
    @NotNull
    public String getValue() {
        return this.value;
    }
}
