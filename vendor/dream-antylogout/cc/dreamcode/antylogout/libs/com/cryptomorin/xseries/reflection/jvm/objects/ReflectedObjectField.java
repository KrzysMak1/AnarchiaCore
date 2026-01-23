package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm.objects;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Member;
import java.lang.reflect.Field;

final class ReflectedObjectField extends AbstractMemberReflectedObject
{
    private final Field delegate;
    
    ReflectedObjectField(final Field delegate) {
        this.delegate = delegate;
    }
    
    @Override
    public ReflectedObject.Type type() {
        return ReflectedObject.Type.FIELD;
    }
    
    @Override
    public Field unreflect() {
        return this.delegate;
    }
    
    @Override
    protected Member member() {
        return (Member)this.delegate;
    }
}
