package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.profiles.exceptions;

import org.jetbrains.annotations.NotNull;

public final class InvalidProfileContainerException extends ProfileException
{
    private final Object container;
    
    public InvalidProfileContainerException(final Object container, final String message) {
        super(message);
        this.container = container;
    }
    
    @NotNull
    public Object getContainer() {
        return this.container;
    }
}
