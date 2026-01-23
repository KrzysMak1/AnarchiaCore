package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm;

import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.XReflection;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.XAccessFlag;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm.classes.ClassHandle;

public abstract class FlaggedNamedMemberHandle extends NamedMemberHandle
{
    protected ClassHandle returnType;
    
    protected FlaggedNamedMemberHandle(final ClassHandle clazz) {
        super(clazz);
    }
    
    public FlaggedNamedMemberHandle asStatic() {
        this.accessFlags.add((Object)XAccessFlag.STATIC);
        return this;
    }
    
    public FlaggedNamedMemberHandle returns(final Class<?> clazz) {
        this.returnType = XReflection.of(clazz);
        return this;
    }
    
    public FlaggedNamedMemberHandle returns(final ClassHandle clazz) {
        this.returnType = clazz;
        return this;
    }
    
    public static Class<?>[] getParameters(final Object owner, final ClassHandle[] parameterTypes) {
        final Class<?>[] classes = new Class[parameterTypes.length];
        int i = 0;
        for (final ClassHandle parameterType : parameterTypes) {
            try {
                classes[i++] = parameterType.unreflect();
            }
            catch (final Throwable ex) {
                throw XReflection.throwCheckedException((Throwable)new ReflectiveOperationException("Unknown parameter " + (Object)parameterType + " for " + owner, ex));
            }
        }
        return classes;
    }
    
    protected Class<?> getReturnType() {
        try {
            return this.returnType.unreflect();
        }
        catch (final Throwable ex) {
            throw XReflection.throwCheckedException((Throwable)new ReflectiveOperationException("Unknown return type " + (Object)this.returnType + " for " + (Object)this));
        }
    }
}
