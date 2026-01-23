package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.profiles.builder;

import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.profiles.exceptions.ProfileChangeException;

public final class ProfileFallback<T>
{
    private final ProfileInstruction<T> instruction;
    private T object;
    private final ProfileChangeException error;
    
    public ProfileFallback(final ProfileInstruction<T> instruction, final T object, final ProfileChangeException error) {
        this.instruction = instruction;
        this.object = object;
        this.error = error;
    }
    
    public T getObject() {
        return this.object;
    }
    
    public ProfileInstruction<T> getInstruction() {
        return this.instruction;
    }
    
    public void setObject(final T object) {
        this.object = object;
    }
    
    public ProfileChangeException getError() {
        return this.error;
    }
}
