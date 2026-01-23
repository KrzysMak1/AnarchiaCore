package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm.objects;

import java.lang.reflect.Member;
import java.lang.reflect.AnnotatedElement;

abstract class AbstractMemberReflectedObject extends AbstractReflectedObject
{
    @Override
    public abstract AnnotatedElement unreflect();
    
    protected abstract Member member();
    
    @Override
    public String name() {
        return this.member().getName();
    }
    
    @Override
    public final Class<?> getDeclaringClass() {
        return this.member().getDeclaringClass();
    }
    
    @Override
    public final int getModifiers() {
        return this.member().getModifiers();
    }
}
