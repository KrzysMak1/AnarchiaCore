package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm;

import java.lang.invoke.MethodHandles;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.Iterator;
import org.jetbrains.annotations.NotNull;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm.objects.ReflectedObjectHandle;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm.objects.ReflectedObject;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.ReflectiveHandle;
import org.jetbrains.annotations.Nullable;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.XReflection;
import java.lang.reflect.Member;
import java.lang.reflect.AccessibleObject;
import org.intellij.lang.annotations.Pattern;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.minecraft.MinecraftMapping;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.parser.ReflectionParser;
import org.intellij.lang.annotations.Language;
import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Collection;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.XAccessFlag;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm.classes.ClassHandle;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm.classes.DynamicClassHandle;
import java.lang.invoke.MethodHandle;

public class FieldMemberHandle extends FlaggedNamedMemberHandle
{
    public static final MethodHandle MODIFIERS_FIELD;
    public static final DynamicClassHandle VarHandle;
    public static final MethodHandle VAR_HANDLE_SET;
    private static final Object MODIFIERS_VAR_HANDLE;
    protected Boolean getter;
    
    public FieldMemberHandle(final ClassHandle clazz) {
        super(clazz);
    }
    
    public boolean isGetter() {
        if (this.getter == null) {
            throw new IllegalStateException("Not specified whether the field handle is a getter or setter");
        }
        return this.getter;
    }
    
    public FieldMemberHandle getter() {
        this.getter = true;
        return this;
    }
    
    @Override
    public FieldMemberHandle asStatic() {
        super.asStatic();
        return this;
    }
    
    public FieldMemberHandle asFinal() {
        this.accessFlags.add((Object)XAccessFlag.FINAL);
        return this;
    }
    
    @Override
    public FieldMemberHandle makeAccessible() {
        super.makeAccessible();
        return this;
    }
    
    public FieldMemberHandle setter() {
        this.getter = false;
        return this;
    }
    
    @Override
    public FieldMemberHandle returns(final Class<?> clazz) {
        super.returns(clazz);
        return this;
    }
    
    @Override
    public FieldMemberHandle returns(final ClassHandle clazz) {
        super.returns(clazz);
        return this;
    }
    
    @Override
    public FieldMemberHandle copy() {
        final FieldMemberHandle handle = new FieldMemberHandle(this.clazz);
        handle.returnType = this.returnType;
        handle.getter = this.getter;
        handle.accessFlags.addAll((Collection)this.accessFlags);
        handle.names.addAll((Collection)this.names);
        return handle;
    }
    
    @Override
    public MethodHandle reflect() throws ReflectiveOperationException {
        Objects.requireNonNull((Object)this.getter, "Not specified whether the field handle is a getter or setter");
        final Field jvm = this.reflectJvm();
        if (this.getter) {
            return this.clazz.getNamespace().getLookup().unreflectGetter(jvm);
        }
        return this.clazz.getNamespace().getLookup().unreflectSetter(jvm);
    }
    
    @Override
    public boolean exists() {
        try {
            this.reflectJvm();
            return true;
        }
        catch (final ReflectiveOperationException ignored) {
            return false;
        }
    }
    
    @Override
    public FieldMemberHandle signature(@Language(value = "Java", suffix = ";") final String declaration) {
        return new ReflectionParser(declaration).imports(this.clazz.getNamespace()).parseField(this);
    }
    
    @Override
    public FieldMemberHandle map(final MinecraftMapping mapping, @Pattern("\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*") final String name) {
        super.map(mapping, name);
        return this;
    }
    
    @Override
    public FieldMemberHandle named(@Pattern("\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*") final String... names) {
        super.named(names);
        return this;
    }
    
    @Override
    protected <T extends AccessibleObject & Member> T handleAccessible(T field) throws ReflectiveOperationException {
        field = super.handleAccessible(field);
        if (field == null) {
            return null;
        }
        if (this.getter != null && !this.getter && this.accessFlags.contains((Object)XAccessFlag.FINAL) && this.accessFlags.contains((Object)XAccessFlag.STATIC)) {
            try {
                final int unfinalModifiers = XAccessFlag.FINAL.remove(field.getModifiers());
                if (FieldMemberHandle.MODIFIERS_VAR_HANDLE != null) {
                    FieldMemberHandle.VAR_HANDLE_SET.invoke(FieldMemberHandle.MODIFIERS_VAR_HANDLE, (AccessibleObject)field, unfinalModifiers);
                }
                else {
                    if (FieldMemberHandle.MODIFIERS_FIELD == null) {
                        throw new IllegalAccessException("Current Java version doesn't support modifying final fields. " + (Object)this);
                    }
                    FieldMemberHandle.MODIFIERS_FIELD.invoke((AccessibleObject)field, unfinalModifiers);
                }
            }
            catch (final Throwable e) {
                throw new ReflectiveOperationException("Cannot unfinal field " + (Object)this, e);
            }
        }
        return field;
    }
    
    @Nullable
    public Object get(final Object instance) {
        try {
            return this.getter().reflectJvm().get(instance);
        }
        catch (final ReflectiveOperationException ex) {
            throw XReflection.throwCheckedException((Throwable)ex);
        }
    }
    
    @Nullable
    public Object getStatic() {
        try {
            return this.asStatic().getter().reflectJvm().get((Object)null);
        }
        catch (final ReflectiveOperationException ex) {
            throw XReflection.throwCheckedException((Throwable)ex);
        }
    }
    
    @NotNull
    @Override
    public ReflectiveHandle<ReflectedObject> jvm() {
        return new ReflectedObjectHandle(() -> ReflectedObject.of(this.reflectJvm()));
    }
    
    public Field reflectJvm() throws ReflectiveOperationException {
        Objects.requireNonNull((Object)this.returnType, "Return type not specified");
        if (this.names.isEmpty()) {
            throw new IllegalStateException("No names specified");
        }
        NoSuchFieldException errors = null;
        Field field = null;
        final Class<?> clazz = this.clazz.reflect();
        final Class<?> returnType = this.getReturnType();
        for (final String name : this.names) {
            if (field != null) {
                break;
            }
            try {
                field = clazz.getDeclaredField(name);
                if (field.getType() != returnType) {
                    throw new NoSuchFieldException("Field named '" + name + "' was found but the types don't match: " + (Object)field + " != " + (Object)this);
                }
                continue;
            }
            catch (final NoSuchFieldException ex) {
                field = null;
                if (errors == null) {
                    errors = new NoSuchFieldException("None of the fields were found for " + (Object)this);
                }
                errors.addSuppressed((Throwable)ex);
            }
        }
        if (field == null) {
            throw XReflection.relativizeSuppressedExceptions(errors);
        }
        return this.handleAccessible(field);
    }
    
    @Override
    public String toString() {
        String str = this.getClass().getSimpleName() + '{';
        str += (String)this.accessFlags.stream().map(x -> x.name().toLowerCase(Locale.ENGLISH)).collect(Collectors.joining((CharSequence)" "));
        if (this.returnType != null) {
            str = str + (Object)this.returnType + " ";
        }
        str += String.join((CharSequence)"/", (Iterable)this.names);
        return str + '}';
    }
    
    static {
        VarHandle = XReflection.classHandle().inPackage("java.lang.invoke").named("VarHandle");
        VAR_HANDLE_SET = FieldMemberHandle.VarHandle.method().named("set").returns(Void.TYPE).parameters(Object[].class).reflectOrNull();
        final Object modVarHandle = null;
        MethodHandle modifierFieldJvm = null;
        try {
            modifierFieldJvm = XReflection.of(Field.class).field().setter().named("modifiers").returns(Integer.TYPE).unreflect();
        }
        catch (final Exception ex) {}
        try {
            FieldMemberHandle.VarHandle.reflect();
            final MethodHandle PRIVATE_LOOKUP_IN = XReflection.of(MethodHandles.class).method().named("privateLookupIn").returns(MethodHandles.Lookup.class).parameters(Class.class, MethodHandles.Lookup.class).reflect();
            final MethodHandle FIND_VAR_HANDLE = FieldMemberHandle.VarHandle.method().named("findVarHandle").returns(MethodHandles.Lookup.class).parameters(Class.class, String.class, Class.class).reflect();
            final MethodHandles.Lookup lookup = PRIVATE_LOOKUP_IN.invoke((Class)Field.class, MethodHandles.lookup());
            FIND_VAR_HANDLE.invoke(lookup, (Class)Field.class, "modifiers", Integer.TYPE);
        }
        catch (final Throwable t) {}
        MODIFIERS_VAR_HANDLE = modVarHandle;
        MODIFIERS_FIELD = modifierFieldJvm;
    }
}
