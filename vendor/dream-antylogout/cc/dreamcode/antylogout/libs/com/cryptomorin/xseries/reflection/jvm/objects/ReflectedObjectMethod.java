package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm.objects;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Member;
import java.lang.reflect.Method;

final class ReflectedObjectMethod extends AbstractMemberReflectedObject
{
    private final Method delegate;
    
    ReflectedObjectMethod(final Method delegate) {
        this.delegate = delegate;
    }
    
    @Override
    public ReflectedObject.Type type() {
        return ReflectedObject.Type.METHOD;
    }
    
    @Override
    public Method unreflect() {
        return this.delegate;
    }
    
    @Override
    protected Member member() {
        return (Member)this.delegate;
    }
}
