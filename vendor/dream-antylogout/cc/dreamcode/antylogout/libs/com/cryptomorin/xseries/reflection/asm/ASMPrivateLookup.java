package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.asm;

import java.lang.reflect.Field;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.lang.reflect.Method;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public final class ASMPrivateLookup
{
    private final MethodHandles.Lookup lookup;
    private final Class<?> targetClass;
    
    public ASMPrivateLookup(final Class<?> targetClass) {
        this.lookup = MethodHandles.lookup();
        this.targetClass = targetClass;
    }
    
    public MethodHandle findMethod(final String name, final Class<?> rType, final Class<?>[] pTypes) throws IllegalAccessException {
        final Method found = (Method)new ReflectionIterator<Object>(clazz -> clazz.getDeclaredMethod(name, (Class[])pTypes)).find();
        if (found == null) {
            throw new IllegalArgumentException("Couldn't find method named '" + name + "' with type: " + (Object)rType + " (" + Arrays.toString((Object[])pTypes) + ')');
        }
        found.setAccessible(true);
        return this.lookup.unreflect(found);
    }
    
    public MethodHandle findConstructor(final Class<?>[] pTypes) throws IllegalAccessException {
        try {
            final Constructor<?> found = this.targetClass.getDeclaredConstructor(pTypes);
            found.setAccessible(true);
            return this.lookup.unreflectConstructor((Constructor)found);
        }
        catch (final NoSuchMethodException e) {
            throw new IllegalArgumentException("Couldn't find constructor with type:  (" + Arrays.toString((Object[])pTypes) + ')', (Throwable)e);
        }
    }
    
    public MethodHandle findField(final String name, final Class<?> rType, final boolean getter) throws IllegalAccessException {
        final Field found = (Field)new ReflectionIterator<Object>(clazz -> clazz.getDeclaredField(name)).find();
        if (found == null) {
            throw new IllegalArgumentException("Couldn't find field named '" + name + "' with type: " + (Object)rType);
        }
        found.setAccessible(true);
        return getter ? this.lookup.unreflectGetter(found) : this.lookup.unreflectSetter(found);
    }
    
    private final class ReflectionIterator<T>
    {
        private final UnsafeFunction<Class<?>, T> finder;
        
        private ReflectionIterator(final UnsafeFunction<Class<?>, T> finder) {
            this.finder = finder;
        }
        
        private T find() {
            try {
                return this.iterate(ASMPrivateLookup.this.targetClass);
            }
            catch (final Exception e) {
                throw new IllegalStateException((Throwable)e);
            }
        }
        
        private T iterate(final Class<?> clazz) throws Exception {
            if (clazz == Object.class) {
                return null;
            }
            try {
                return this.finder.apply(clazz);
            }
            catch (final NoSuchMethodException | NoSuchFieldException ignored) {
                return this.iterate(clazz.getSuperclass());
            }
        }
    }
    
    @FunctionalInterface
    private interface UnsafeFunction<I, O>
    {
        O apply(final I p0) throws Exception;
    }
}
