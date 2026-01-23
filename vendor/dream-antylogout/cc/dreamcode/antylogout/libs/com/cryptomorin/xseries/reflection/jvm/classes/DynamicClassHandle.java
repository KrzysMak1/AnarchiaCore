package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm.classes;

import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.ReflectiveHandle;
import com.google.common.base.Strings;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.XReflection;
import java.util.Iterator;
import java.util.Collection;
import java.util.Arrays;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.minecraft.MinecraftMapping;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.intellij.lang.annotations.Pattern;
import java.util.HashSet;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.ReflectiveNamespace;
import java.util.Set;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm.NameableReflectiveHandle;

public class DynamicClassHandle extends ClassHandle implements NameableReflectiveHandle
{
    protected ClassHandle parent;
    protected String packageName;
    protected final Set<String> classNames;
    protected int array;
    
    public DynamicClassHandle(final ReflectiveNamespace namespace) {
        super(namespace);
        this.classNames = (Set<String>)new HashSet(5);
    }
    
    public DynamicClassHandle inPackage(@Pattern("(\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*\\.)*\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*") @NotNull final String packageName) {
        Objects.requireNonNull((Object)packageName, "Null package name");
        this.packageName = packageName;
        return this;
    }
    
    public DynamicClassHandle inPackage(@NotNull final PackageHandle packageHandle) {
        return this.inPackage(packageHandle, "");
    }
    
    public DynamicClassHandle inPackage(@NotNull final PackageHandle packageHandle, @Pattern("(\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*\\.)*\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*") @NotNull final String packageName) {
        Objects.requireNonNull((Object)packageHandle, "Null package handle type");
        Objects.requireNonNull((Object)packageName, "Null package handle name");
        if (this.parent != null) {
            throw new IllegalStateException("Cannot change package of an inner class: " + (Object)packageHandle + " -> " + packageName);
        }
        this.packageName = packageHandle.getPackage(packageName);
        return this;
    }
    
    @Override
    public DynamicClassHandle map(final MinecraftMapping mapping, final String name) {
        return this.named(name);
    }
    
    @Override
    public DynamicClassHandle named(@Pattern("\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*") @NotNull final String... classNames) {
        Objects.requireNonNull((Object)classNames);
        for (final String className : this.classNames) {
            Objects.requireNonNull((Object)className, () -> "Cannot add null class name from: " + Arrays.toString((Object[])classNames) + " to " + (Object)this);
        }
        this.classNames.addAll((Collection)Arrays.asList((Object[])classNames));
        return this;
    }
    
    public String[] reflectClassNames() {
        if (this.parent == null) {
            Objects.requireNonNull((Object)this.packageName, "Package name is null");
        }
        final String[] classNames = new String[this.classNames.size()];
        final Class<?> parent = (this.parent == null) ? null : XReflection.of(this.parent.unreflect()).asArray(0).unreflect();
        int i = 0;
        for (final String className : this.classNames) {
            String clazz;
            if (parent == null) {
                clazz = this.packageName + '.' + className;
            }
            else {
                clazz = parent.getName() + '$' + className;
            }
            if (this.array != 0) {
                clazz = Strings.repeat("[", this.array) + 'L' + clazz + ';';
            }
            classNames[i++] = clazz;
        }
        return classNames;
    }
    
    @Override
    public DynamicClassHandle copy() {
        final DynamicClassHandle handle = new DynamicClassHandle(this.namespace);
        handle.array = this.array;
        handle.parent = this.parent;
        handle.packageName = this.packageName;
        handle.classNames.addAll((Collection)this.classNames);
        return handle;
    }
    
    @Override
    public Class<?> reflect() throws ClassNotFoundException {
        final String[] classNames = this.reflectClassNames();
        if (classNames.length == 0) {
            throw new IllegalStateException("No class name specified for " + (Object)this);
        }
        ClassNotFoundException errors = null;
        final String[] array = classNames;
        final int length = array.length;
        int i = 0;
        while (i < length) {
            final String className = array[i];
            try {
                return this.checkConstraints(Class.forName(className));
            }
            catch (final ClassNotFoundException ex) {
                if (errors == null) {
                    errors = new ClassNotFoundException("None of the classes were found");
                }
                errors.addSuppressed((Throwable)ex);
                ++i;
                continue;
            }
            break;
        }
        throw XReflection.relativizeSuppressedExceptions(errors);
    }
    
    @Override
    public DynamicClassHandle asArray(final int dimension) {
        if (dimension < 0) {
            throw new IllegalArgumentException("Array dimension cannot be negative: " + dimension);
        }
        this.array = dimension;
        return this;
    }
    
    @Override
    public boolean isArray() {
        return this.array > 0;
    }
    
    @Override
    public Set<String> getPossibleNames() {
        return this.classNames;
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + '{' + ((this.parent == null) ? "" : ((Object)this.parent + " -> ")) + ((this.parent == null) ? this.packageName : ((this.packageName == null) ? "" : this.packageName)) + '(' + String.join((CharSequence)"|", (Iterable)this.classNames) + ')' + ((this.array == 0) ? "" : ("[" + this.array + ']')) + " }";
    }
}
