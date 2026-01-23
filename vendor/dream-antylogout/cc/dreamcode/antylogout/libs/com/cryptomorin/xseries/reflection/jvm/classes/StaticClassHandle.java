package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm.classes;

import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.ReflectiveHandle;
import java.util.Collections;
import java.util.Set;
import java.lang.reflect.Array;
import java.util.Objects;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.ReflectiveNamespace;
import org.jetbrains.annotations.NotNull;

public class StaticClassHandle extends ClassHandle
{
    @NotNull
    protected Class<?> clazz;
    
    public StaticClassHandle(final ReflectiveNamespace namespace, @NotNull final Class<?> clazz) {
        super(namespace);
        this.clazz = (Class)Objects.requireNonNull((Object)clazz);
    }
    
    private Class<?> purifyClass() {
        Class<?> pureClazz = this.clazz;
        while (true) {
            final Class<?> component = pureClazz.getComponentType();
            if (component == null) {
                break;
            }
            pureClazz = component;
        }
        return (Class)Objects.requireNonNull((Object)pureClazz);
    }
    
    @Override
    public StaticClassHandle asArray(final int dimension) {
        Class<?> arrayClass = this.purifyClass();
        if (dimension > 0) {
            for (int i = 0; i < dimension; ++i) {
                arrayClass = Array.newInstance((Class)arrayClass, 0).getClass();
            }
        }
        this.clazz = arrayClass;
        return this;
    }
    
    @Override
    public Class<?> reflect() throws ClassNotFoundException {
        return this.checkConstraints(this.clazz);
    }
    
    @Override
    public boolean isArray() {
        return this.clazz.isArray();
    }
    
    @Override
    public Set<String> getPossibleNames() {
        return (Set<String>)Collections.singleton((Object)this.clazz.getSimpleName());
    }
    
    @Override
    public StaticClassHandle copy() {
        return new StaticClassHandle(this.namespace, this.clazz);
    }
    
    @Override
    public String toString() {
        return "StaticClassHandle(" + (Object)this.clazz + ')';
    }
}
