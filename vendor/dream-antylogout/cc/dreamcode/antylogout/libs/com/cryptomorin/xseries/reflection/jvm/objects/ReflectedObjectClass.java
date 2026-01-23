package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm.objects;

import java.lang.reflect.AnnotatedElement;

final class ReflectedObjectClass extends AbstractReflectedObject
{
    private final Class<?> delegate;
    
    ReflectedObjectClass(final Class<?> delegate) {
        this.delegate = delegate;
    }
    
    @Override
    public ReflectedObject.Type type() {
        return ReflectedObject.Type.CLASS;
    }
    
    @Override
    public Class<?> unreflect() {
        return this.delegate;
    }
    
    @Override
    public String name() {
        return this.delegate.getSimpleName();
    }
    
    @Override
    public Class<?> getDeclaringClass() {
        return this.delegate.getDeclaringClass();
    }
    
    @Override
    public int getModifiers() {
        return this.delegate.getModifiers();
    }
}
