package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection;

import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.minecraft.MinecraftClassHandle;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.parser.ReflectionParser;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm.classes.DynamicClassHandle;
import org.intellij.lang.annotations.Language;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm.classes.StaticClassHandle;
import org.jetbrains.annotations.ApiStatus;
import java.util.Iterator;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.HashMap;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm.classes.ClassHandle;
import java.util.Set;
import java.lang.invoke.MethodHandles;
import java.util.Map;

public class ReflectiveNamespace
{
    private final Map<String, Class<?>> imports;
    private final MethodHandles.Lookup lookup;
    private final Set<ClassHandle> handles;
    
    protected ReflectiveNamespace() {
        this.imports = (Map<String, Class<?>>)new HashMap();
        this.lookup = MethodHandles.lookup();
        this.handles = (Set<ClassHandle>)Collections.newSetFromMap((Map)new IdentityHashMap());
    }
    
    public ReflectiveNamespace imports(@NotNull final Class<?>... classes) {
        for (final Class<?> clazz : classes) {
            this.imports(clazz.getSimpleName(), clazz);
        }
        return this;
    }
    
    public ReflectiveNamespace imports(@NotNull final String name, @NotNull final Class<?> clazz) {
        Objects.requireNonNull((Object)name);
        Objects.requireNonNull((Object)clazz);
        this.imports.put((Object)name, (Object)clazz);
        return this;
    }
    
    @NotNull
    @ApiStatus.Internal
    public Map<String, Class<?>> getImports() {
        for (final ClassHandle handle : this.handles) {
            final Class<?> clazz = handle.reflectOrNull();
            if (clazz == null) {
                continue;
            }
            for (final String className : handle.getPossibleNames()) {
                this.imports.put((Object)className, (Object)clazz);
            }
        }
        return this.imports;
    }
    
    @ApiStatus.Internal
    public void link(final ClassHandle handle) {
        if (handle.getNamespace() != this) {
            throw new IllegalArgumentException("Not the same namespace");
        }
        this.handles.add((Object)handle);
    }
    
    @NotNull
    @ApiStatus.Internal
    public MethodHandles.Lookup getLookup() {
        return this.lookup;
    }
    
    public StaticClassHandle of(final Class<?> clazz) {
        this.imports(clazz);
        return new StaticClassHandle(this, clazz);
    }
    
    public DynamicClassHandle classHandle(@Language(value = "Java", suffix = "{}") final String declaration) {
        final DynamicClassHandle classHandle = new DynamicClassHandle(this);
        return new ReflectionParser(declaration).imports(this).parseClass(classHandle);
    }
    
    public DynamicClassHandle classHandle() {
        return new DynamicClassHandle(this);
    }
    
    public MinecraftClassHandle ofMinecraft(@Language(value = "Java", suffix = "{}") final String declaration) {
        final MinecraftClassHandle classHandle = new MinecraftClassHandle(this);
        return new ReflectionParser(declaration).imports(this).parseClass(classHandle);
    }
    
    public MinecraftClassHandle ofMinecraft() {
        return new MinecraftClassHandle(this);
    }
}
