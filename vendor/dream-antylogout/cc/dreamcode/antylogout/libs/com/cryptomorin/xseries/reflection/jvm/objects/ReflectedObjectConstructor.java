package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm.objects;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Member;
import java.lang.reflect.Constructor;

final class ReflectedObjectConstructor extends AbstractMemberReflectedObject
{
    private final Constructor<?> delegate;
    
    ReflectedObjectConstructor(final Constructor<?> delegate) {
        this.delegate = delegate;
    }
    
    @Override
    public ReflectedObject.Type type() {
        return ReflectedObject.Type.CONSTRUCTOR;
    }
    
    @Override
    public String name() {
        return "<init>";
    }
    
    @Override
    public Constructor<?> unreflect() {
        return this.delegate;
    }
    
    @Override
    protected Member member() {
        return (Member)this.delegate;
    }
}
