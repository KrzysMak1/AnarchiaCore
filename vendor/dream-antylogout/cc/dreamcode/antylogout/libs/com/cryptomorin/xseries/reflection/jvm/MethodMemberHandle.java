package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm;

import java.util.Locale;
import java.lang.reflect.AccessibleObject;
import java.util.stream.Collectors;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.lang.reflect.Method;
import org.jetbrains.annotations.NotNull;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm.objects.ReflectedObjectHandle;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm.objects.ReflectedObject;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.ReflectiveHandle;
import java.lang.invoke.MethodHandle;
import org.jetbrains.annotations.ApiStatus;
import java.lang.invoke.LambdaConversionException;
import java.lang.invoke.LambdaMetafactory;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.XAccessFlag;
import java.lang.invoke.CallSite;
import java.lang.invoke.MethodType;
import org.intellij.lang.annotations.Pattern;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.minecraft.MinecraftMapping;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.parser.ReflectionParser;
import org.intellij.lang.annotations.Language;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.XReflection;
import java.util.Arrays;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm.classes.ClassHandle;

public class MethodMemberHandle extends FlaggedNamedMemberHandle
{
    protected ClassHandle[] parameterTypes;
    
    public MethodMemberHandle(final ClassHandle clazz) {
        super(clazz);
        this.parameterTypes = new ClassHandle[0];
    }
    
    public MethodMemberHandle parameters(final ClassHandle... parameterTypes) {
        this.parameterTypes = parameterTypes;
        return this;
    }
    
    @Override
    public MethodMemberHandle returns(final Class<?> clazz) {
        super.returns(clazz);
        return this;
    }
    
    @Override
    public MethodMemberHandle returns(final ClassHandle clazz) {
        super.returns(clazz);
        return this;
    }
    
    @Override
    public MethodMemberHandle asStatic() {
        super.asStatic();
        return this;
    }
    
    public MethodMemberHandle parameters(final Class<?>... parameterTypes) {
        this.parameterTypes = (ClassHandle[])Arrays.stream((Object[])parameterTypes).map(XReflection::of).toArray(ClassHandle[]::new);
        return this;
    }
    
    @Override
    public MethodMemberHandle signature(@Language(value = "Java", suffix = ";") final String declaration) {
        return new ReflectionParser(declaration).imports(this.clazz.getNamespace()).parseMethod(this);
    }
    
    @Override
    public MethodMemberHandle map(final MinecraftMapping mapping, @Pattern("\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*") final String name) {
        super.map(mapping, name);
        return this;
    }
    
    @Override
    public MethodMemberHandle named(@Pattern("\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*") final String... names) {
        super.named(names);
        return this;
    }
    
    public MethodType getMethodType() {
        return MethodType.methodType((Class)this.getReturnType(), (Class[])FlaggedNamedMemberHandle.getParameters(this, this.parameterTypes));
    }
    
    @ApiStatus.Experimental
    public CallSite toLambda(final Class<?> proxy, final String interfaceMethod) throws ReflectiveOperationException, LambdaConversionException {
        final MethodType factoryType = this.getMethodType();
        MethodType implType;
        if (this.accessFlags.contains((Object)XAccessFlag.STATIC)) {
            implType = MethodType.methodType((Class)proxy);
        }
        else {
            implType = MethodType.methodType((Class)proxy, (Class)((ReflectiveHandle<Class>)this.clazz).reflect());
        }
        return LambdaMetafactory.metafactory(this.clazz.getNamespace().getLookup(), interfaceMethod, implType, factoryType, this.reflect(), factoryType);
    }
    
    @Override
    public MethodHandle reflect() throws ReflectiveOperationException {
        return this.clazz.getNamespace().getLookup().unreflect(this.reflectJvm());
    }
    
    @NotNull
    @Override
    public ReflectiveHandle<ReflectedObject> jvm() {
        return new ReflectedObjectHandle(() -> ReflectedObject.of(this.reflectJvm()));
    }
    
    public Method reflectJvm() throws ReflectiveOperationException {
        Objects.requireNonNull((Object)this.returnType, "Return type not specified");
        if (this.names.isEmpty()) {
            throw new IllegalStateException("No names specified");
        }
        NoSuchMethodException errors = null;
        Method method = null;
        final Class<?> clazz = this.clazz.reflect();
        final Class<?>[] parameterTypes = FlaggedNamedMemberHandle.getParameters(this, this.parameterTypes);
        final Class<?> returnType = this.getReturnType();
        for (final String name : this.names) {
            if (method != null) {
                break;
            }
            try {
                method = clazz.getDeclaredMethod(name, parameterTypes);
                if (method.getReturnType() != returnType) {
                    throw new NoSuchMethodException("Method named '" + name + "' was found but the return types don't match: " + (Object)this.returnType + " != " + (Object)method.getReturnType());
                }
                continue;
            }
            catch (final NoSuchMethodException ignored) {
                try {
                    method = clazz.getMethod(name, parameterTypes);
                    if (method.getReturnType() != returnType) {
                        throw new NoSuchMethodException("Method named '" + name + "' was found but the return types don't match: " + (Object)this.returnType + " != " + (Object)method.getReturnType());
                    }
                    continue;
                }
                catch (final NoSuchMethodException ex2) {
                    final NoSuchMethodException realEx = ex2;
                    method = null;
                    if (errors == null) {
                        errors = new NoSuchMethodException("None of the methods were found for " + (Object)this);
                    }
                    errors.addSuppressed((Throwable)realEx);
                }
            }
        }
        if (method == null) {
            throw XReflection.relativizeSuppressedExceptions(errors);
        }
        return this.handleAccessible(method);
    }
    
    @Override
    public MethodMemberHandle copy() {
        final MethodMemberHandle handle = new MethodMemberHandle(this.clazz);
        handle.returnType = this.returnType;
        handle.parameterTypes = this.parameterTypes;
        handle.accessFlags.addAll((Collection)this.accessFlags);
        handle.names.addAll((Collection)this.names);
        return handle;
    }
    
    @Override
    public String toString() {
        String str = this.getClass().getSimpleName() + '{';
        str += (String)this.accessFlags.stream().map(x -> x.name().toLowerCase(Locale.ENGLISH)).collect(Collectors.joining((CharSequence)" "));
        if (this.returnType != null) {
            str = str + (Object)this.returnType + " ";
        }
        str += String.join((CharSequence)"/", (Iterable)this.names);
        str = str + '(' + (String)Arrays.stream((Object[])this.parameterTypes).map(Object::toString).collect(Collectors.joining((CharSequence)", ")) + ')';
        return str + '}';
    }
}
