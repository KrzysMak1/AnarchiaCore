package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm;

import java.util.Locale;
import java.lang.reflect.AccessibleObject;
import java.util.stream.Collectors;
import java.util.Collection;
import org.jetbrains.annotations.NotNull;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm.objects.ReflectedObjectHandle;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm.objects.ReflectedObject;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.ReflectiveHandle;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.parser.ReflectionParser;
import org.intellij.lang.annotations.Language;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.XAccessFlag;
import java.lang.invoke.MethodHandle;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.XReflection;
import java.util.Arrays;
import org.jetbrains.annotations.ApiStatus;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm.classes.ClassHandle;

public class ConstructorMemberHandle extends MemberHandle
{
    protected ClassHandle[] parameterTypes;
    
    public ConstructorMemberHandle(final ClassHandle clazz) {
        super(clazz);
        this.parameterTypes = new ClassHandle[0];
    }
    
    @ApiStatus.Internal
    public ClassHandle[] getParameterTypes() {
        return this.parameterTypes;
    }
    
    public ConstructorMemberHandle parameters(final Class<?>... parameterTypes) {
        this.parameterTypes = (ClassHandle[])Arrays.stream((Object[])parameterTypes).map(XReflection::of).toArray(ClassHandle[]::new);
        return this;
    }
    
    public ConstructorMemberHandle parameters(final ClassHandle... parameterTypes) {
        this.parameterTypes = parameterTypes;
        return this;
    }
    
    @Override
    public MethodHandle reflect() throws ReflectiveOperationException {
        if (this.accessFlags.contains((Object)XAccessFlag.FINAL)) {
            throw new UnsupportedOperationException("Constructor cannot be final: " + (Object)this);
        }
        if (this.accessFlags.contains((Object)XAccessFlag.PRIVATE)) {
            return this.clazz.getNamespace().getLookup().unreflectConstructor((Constructor)this.reflectJvm());
        }
        final Class<?>[] parameterTypes = FlaggedNamedMemberHandle.getParameters(this, this.parameterTypes);
        return this.clazz.getNamespace().getLookup().findConstructor((Class)((ReflectiveHandle<Class>)this.clazz).unreflect(), MethodType.methodType(Void.TYPE, (Class[])parameterTypes));
    }
    
    @Override
    public ConstructorMemberHandle signature(@Language(value = "Java", suffix = ";") final String declaration) {
        return new ReflectionParser(declaration).imports(this.clazz.getNamespace()).parseConstructor(this);
    }
    
    @NotNull
    @Override
    public ReflectiveHandle<ReflectedObject> jvm() {
        return new ReflectedObjectHandle(() -> ReflectedObject.of(this.reflectJvm()));
    }
    
    public Constructor<?> reflectJvm() throws ReflectiveOperationException {
        final Class<?>[] parameterTypes = FlaggedNamedMemberHandle.getParameters(this, this.parameterTypes);
        return this.handleAccessible(((ReflectiveHandle<Class>)this.clazz).unreflect().getDeclaredConstructor((Class[])parameterTypes));
    }
    
    @Override
    public ConstructorMemberHandle copy() {
        final ConstructorMemberHandle handle = new ConstructorMemberHandle(this.clazz);
        handle.parameterTypes = this.parameterTypes;
        handle.accessFlags.addAll((Collection)this.accessFlags);
        return handle;
    }
    
    @Override
    public String toString() {
        String str = this.getClass().getSimpleName() + '{';
        str += (String)this.accessFlags.stream().map(x -> x.name().toLowerCase(Locale.ENGLISH)).collect(Collectors.joining((CharSequence)" "));
        str = str + this.clazz.toString() + ' ';
        str = str + '(' + (String)Arrays.stream((Object[])this.parameterTypes).map(Object::toString).collect(Collectors.joining((CharSequence)", ")) + ')';
        return str + '}';
    }
}
