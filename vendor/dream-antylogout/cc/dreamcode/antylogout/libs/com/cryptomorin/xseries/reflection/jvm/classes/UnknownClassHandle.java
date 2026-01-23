package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm.classes;

import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.ReflectiveHandle;
import org.jetbrains.annotations.NotNull;
import java.util.Collections;
import java.util.Set;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.ReflectiveNamespace;

public class UnknownClassHandle extends ClassHandle
{
    private final String name;
    
    public UnknownClassHandle(final ReflectiveNamespace namespace, final String name) {
        super(namespace);
        this.name = name;
    }
    
    @Override
    public Set<String> getPossibleNames() {
        return (Set<String>)Collections.singleton((Object)this.name);
    }
    
    @Override
    public UnknownClassHandle asArray(final int dimensions) {
        return new UnknownClassHandle(this.namespace, this.name + "[]");
    }
    
    @Override
    public boolean isArray() {
        return this.name.endsWith("[]");
    }
    
    @Override
    public UnknownClassHandle copy() {
        return new UnknownClassHandle(this.namespace, this.name);
    }
    
    @NotNull
    @Override
    public Class<?> reflect() throws ReflectiveOperationException {
        throw new ReflectiveOperationException("Unknown class: " + this.name);
    }
    
    @Override
    public String toString() {
        return "UnknownClassHandle(" + this.name + ')';
    }
}
