package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.profiles.exceptions;

import org.jetbrains.annotations.NotNull;

public final class UnknownPlayerException extends InvalidProfileException
{
    private final Object unknownObject;
    
    public UnknownPlayerException(final Object unknownObject, final String message) {
        super(unknownObject.toString(), message);
        this.unknownObject = unknownObject;
    }
    
    @NotNull
    public Object getUnknownObject() {
        return this.unknownObject;
    }
}
