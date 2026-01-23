package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm.classes;

import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm.objects.ReflectedObjectHandle;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm.objects.ReflectedObject;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.parser.ReflectionParser;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm.ConstructorMemberHandle;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm.FieldMemberHandle;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm.EnumMemberHandle;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm.MethodMemberHandle;
import java.util.Objects;
import org.intellij.lang.annotations.Language;
import java.util.Iterator;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.constraint.ReflectiveConstraintException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.ApiStatus;
import java.util.IdentityHashMap;
import org.jetbrains.annotations.NotNull;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.constraint.ReflectiveConstraint;
import java.util.Map;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.ReflectiveNamespace;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm.NamedReflectiveHandle;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.ReflectiveHandle;

public abstract class ClassHandle implements ReflectiveHandle<Class<?>>, NamedReflectiveHandle
{
    protected final ReflectiveNamespace namespace;
    private final Map<Class<ReflectiveConstraint>, ReflectiveConstraint> constraints;
    
    protected ClassHandle(@NotNull final ReflectiveNamespace namespace) {
        this.constraints = (Map<Class<ReflectiveConstraint>, ReflectiveConstraint>)new IdentityHashMap();
        (this.namespace = namespace).link(this);
    }
    
    @ApiStatus.Experimental
    @Contract(value = "_ -> this", mutates = "this")
    public ClassHandle constraint(@NotNull final ReflectiveConstraint constraint) {
        this.constraints.put((Object)constraint.getClass(), (Object)constraint);
        return this;
    }
    
    protected <T extends Class<?>> T checkConstraints(final T jvm) {
        for (final ReflectiveConstraint constraint : this.constraints.values()) {
            final ReflectiveConstraint.Result result = constraint.appliesTo(this, jvm);
            if (result != ReflectiveConstraint.Result.MATCHED) {
                throw ReflectiveConstraintException.create(constraint, result, this, jvm);
            }
        }
        return jvm;
    }
    
    @NotNull
    @Contract("_ -> new")
    public abstract ClassHandle asArray(final int p0);
    
    @NotNull
    @Contract("-> new")
    public final ClassHandle asArray() {
        return this.asArray(1);
    }
    
    @Contract(pure = true)
    public abstract boolean isArray();
    
    @NotNull
    @Contract("_ -> new")
    public DynamicClassHandle inner(@Language(value = "Java", suffix = "{}") final String declaration) {
        return this.inner(this.namespace.classHandle(declaration));
    }
    
    @NotNull
    @Contract("_ -> param1")
    public <T extends DynamicClassHandle> T inner(@NotNull final T handle) {
        Objects.requireNonNull((Object)handle, "Inner handle is null");
        if (this == handle) {
            throw new IllegalArgumentException("Same instance: " + (Object)this);
        }
        handle.parent = this;
        this.namespace.link(this);
        return handle;
    }
    
    public int getDimensionCount() {
        int count = -1;
        Class<?> clazz = this.reflectOrNull();
        if (clazz == null) {
            return count;
        }
        do {
            clazz = clazz.getComponentType();
            ++count;
        } while (clazz != null);
        return count;
    }
    
    @Contract(pure = true)
    public ReflectiveNamespace getNamespace() {
        return this.namespace;
    }
    
    @Contract(value = "-> new", pure = true)
    public MethodMemberHandle method() {
        return new MethodMemberHandle(this);
    }
    
    @Contract(value = "_ -> new", pure = true)
    public MethodMemberHandle method(@Language(value = "Java", suffix = ";") final String declaration) {
        return this.createParser(declaration).parseMethod(this.method());
    }
    
    @Contract(value = "-> new", pure = true)
    public EnumMemberHandle enums() {
        return new EnumMemberHandle(this);
    }
    
    @Contract(value = "-> new", pure = true)
    public FieldMemberHandle field() {
        return new FieldMemberHandle(this);
    }
    
    @Contract(value = "_ -> new", pure = true)
    public FieldMemberHandle field(@Language(value = "Java", suffix = ";") final String declaration) {
        return this.createParser(declaration).parseField(this.field());
    }
    
    @Contract(value = "_ -> new", pure = true)
    public ConstructorMemberHandle constructor(@Language(value = "Java", suffix = ";") final String declaration) {
        return this.createParser(declaration).parseConstructor(this.constructor());
    }
    
    @Contract(value = "-> new", pure = true)
    public ConstructorMemberHandle constructor() {
        return new ConstructorMemberHandle(this);
    }
    
    @Contract(value = "_ -> new", pure = true)
    public ConstructorMemberHandle constructor(final Class<?>... parameters) {
        return this.constructor().parameters(parameters);
    }
    
    @Contract(value = "_ -> new", pure = true)
    public ConstructorMemberHandle constructor(final ClassHandle... parameters) {
        return this.constructor().parameters(parameters);
    }
    
    @Contract(value = "_ -> new", pure = true)
    private ReflectionParser createParser(@Language("Java") final String declaration) {
        return new ReflectionParser(declaration).imports(this.namespace);
    }
    
    @Override
    public abstract ClassHandle copy();
    
    @NotNull
    @Override
    public ReflectiveHandle<ReflectedObject> jvm() {
        return new ReflectedObjectHandle(() -> ReflectedObject.of(this.reflect()));
    }
}
