package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm;

import java.lang.reflect.AccessibleObject;
import java.util.Iterator;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.XReflection;
import java.lang.reflect.Field;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm.objects.ReflectedObjectHandle;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm.objects.ReflectedObject;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.ReflectiveHandle;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import java.lang.invoke.MethodHandle;
import org.jetbrains.annotations.ApiStatus;
import org.intellij.lang.annotations.Language;
import org.intellij.lang.annotations.Pattern;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.minecraft.MinecraftMapping;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm.classes.ClassHandle;

public class EnumMemberHandle extends NamedMemberHandle
{
    public EnumMemberHandle(final ClassHandle clazz) {
        super(clazz);
    }
    
    @Override
    public EnumMemberHandle map(final MinecraftMapping mapping, @Pattern("\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*") final String name) {
        super.map(mapping, name);
        return this;
    }
    
    @Override
    public EnumMemberHandle named(@Pattern("\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*") final String... names) {
        super.named(names);
        return this;
    }
    
    @ApiStatus.Obsolete
    @Override
    public MemberHandle signature(@Language(value = "Java", suffix = ";") final String declaration) {
        throw new UnsupportedOperationException();
    }
    
    @NotNull
    @ApiStatus.Obsolete
    @Override
    public MethodHandle unreflect() {
        return super.unreflect();
    }
    
    @ApiStatus.Obsolete
    @Nullable
    @Override
    public MethodHandle reflectOrNull() {
        return super.reflectOrNull();
    }
    
    @ApiStatus.Obsolete
    @NotNull
    @Override
    public ReflectiveHandle<ReflectedObject> jvm() {
        return new ReflectedObjectHandle(() -> ReflectedObject.of(this.reflectJvm()));
    }
    
    @ApiStatus.Obsolete
    @Override
    public MethodHandle reflect() throws ReflectiveOperationException {
        final Field jvm = this.reflectJvm();
        return this.clazz.getNamespace().getLookup().unreflectGetter(jvm);
    }
    
    @Nullable
    public Object getEnumConstant() {
        try {
            return this.reflectJvm().get((Object)null);
        }
        catch (final ReflectiveOperationException ex) {
            throw XReflection.throwCheckedException((Throwable)ex);
        }
    }
    
    public Field reflectJvm() throws ReflectiveOperationException {
        if (this.names.isEmpty()) {
            throw new IllegalStateException("No enum names specified");
        }
        NoSuchFieldException errors = null;
        Field field = null;
        final Class<?> clazz = this.clazz.reflect();
        if (!clazz.isEnum()) {
            throw new IllegalStateException("Class is not an enum: " + (Object)this.clazz + " -> " + (Object)clazz);
        }
        for (final String name : this.names) {
            if (field != null) {
                break;
            }
            try {
                field = clazz.getDeclaredField(name);
                if (!field.isEnumConstant()) {
                    throw new NoSuchFieldException("Field named '" + name + "' was found but it's not an enum constant " + (Object)this);
                }
                continue;
            }
            catch (final NoSuchFieldException ex) {
                field = null;
                if (errors == null) {
                    errors = new NoSuchFieldException("None of the enums were found for " + (Object)this);
                }
                errors.addSuppressed((Throwable)ex);
            }
        }
        if (field == null) {
            throw XReflection.relativizeSuppressedExceptions(errors);
        }
        return this.handleAccessible(field);
    }
    
    @ApiStatus.Obsolete
    @Override
    public EnumMemberHandle copy() {
        throw new UnsupportedOperationException();
    }
}
