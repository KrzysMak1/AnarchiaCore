package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.proxy;

import java.util.function.Supplier;
import java.util.Map;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public final class ClassOverloadedMethods<T>
{
    private final Map<String, OverloadedMethod<T>> mapped;
    
    public ClassOverloadedMethods(final Map<String, OverloadedMethod<T>> mapped) {
        this.mapped = mapped;
    }
    
    public Map<String, OverloadedMethod<T>> mappings() {
        return this.mapped;
    }
    
    public T get(final String name, final Supplier<String> descriptor) {
        return this.get(name, descriptor, false);
    }
    
    public T get(final String name, final Supplier<String> descriptor, final boolean ignoreIfNull) {
        final OverloadedMethod<T> overloads = (OverloadedMethod<T>)this.mapped.get((Object)name);
        if (overloads == null) {
            if (ignoreIfNull) {
                return null;
            }
            throw new IllegalArgumentException("Failed to find any method named '" + name + "' with descriptor '" + (String)descriptor.get() + "' " + (Object)this);
        }
        else {
            final T overload = overloads.get(descriptor);
            if (overload != null) {
                return overload;
            }
            if (ignoreIfNull) {
                return null;
            }
            throw new IllegalArgumentException("Failed to find overloaded method named '" + name + " with descriptor: '" + (String)descriptor.get() + "' " + (Object)this);
        }
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + '(' + (Object)this.mapped + ')';
    }
}
