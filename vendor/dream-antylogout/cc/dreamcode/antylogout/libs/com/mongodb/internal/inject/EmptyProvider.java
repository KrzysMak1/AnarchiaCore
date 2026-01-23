package cc.dreamcode.antylogout.libs.com.mongodb.internal.inject;

import java.util.Optional;
import cc.dreamcode.antylogout.libs.com.mongodb.annotations.Immutable;

@Immutable
public final class EmptyProvider<T> implements OptionalProvider<T>
{
    private static final EmptyProvider<?> INSTANCE;
    
    private EmptyProvider() {
    }
    
    @Override
    public Optional<T> optional() {
        return (Optional<T>)Optional.empty();
    }
    
    public static <T> EmptyProvider<T> instance() {
        return (EmptyProvider<T>)EmptyProvider.INSTANCE;
    }
    
    static {
        INSTANCE = new EmptyProvider<Object>();
    }
}
