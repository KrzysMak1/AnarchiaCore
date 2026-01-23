package cc.dreamcode.antylogout.libs.com.google.gson.internal;

import java.lang.reflect.Field;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public abstract class UnsafeAllocator
{
    public abstract <T> T newInstance(final Class<T> p0) throws Exception;
    
    static String checkInstantiable(final Class<?> c) {
        final int modifiers = c.getModifiers();
        if (Modifier.isInterface(modifiers)) {
            return "Interfaces can't be instantiated! Register an InstanceCreator or a TypeAdapter for this type. Interface name: " + c.getName();
        }
        if (Modifier.isAbstract(modifiers)) {
            return "Abstract classes can't be instantiated! Register an InstanceCreator or a TypeAdapter for this type. Class name: " + c.getName();
        }
        return null;
    }
    
    private static void assertInstantiable(final Class<?> c) {
        final String exceptionMessage = checkInstantiable(c);
        if (exceptionMessage != null) {
            throw new AssertionError((Object)("UnsafeAllocator is used for non-instantiable type: " + exceptionMessage));
        }
    }
    
    public static UnsafeAllocator create() {
        try {
            final Class<?> unsafeClass = Class.forName("sun.misc.Unsafe");
            final Field f = unsafeClass.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            final Object unsafe = f.get((Object)null);
            final Method allocateInstance = unsafeClass.getMethod("allocateInstance", Class.class);
            return new UnsafeAllocator() {
                @Override
                public <T> T newInstance(final Class<T> c) throws Exception {
                    assertInstantiable(c);
                    return (T)allocateInstance.invoke(unsafe, new Object[] { c });
                }
            };
        }
        catch (final Exception ex) {
            try {
                final Method getConstructorId = ObjectStreamClass.class.getDeclaredMethod("getConstructorId", Class.class);
                getConstructorId.setAccessible(true);
                final int constructorId = (int)getConstructorId.invoke((Object)null, new Object[] { Object.class });
                final Method newInstance = ObjectStreamClass.class.getDeclaredMethod("newInstance", Class.class, Integer.TYPE);
                newInstance.setAccessible(true);
                return new UnsafeAllocator() {
                    @Override
                    public <T> T newInstance(final Class<T> c) throws Exception {
                        assertInstantiable(c);
                        return (T)newInstance.invoke((Object)null, new Object[] { c, constructorId });
                    }
                };
            }
            catch (final Exception ex2) {
                try {
                    final Method newInstance2 = ObjectInputStream.class.getDeclaredMethod("newInstance", Class.class, Class.class);
                    newInstance2.setAccessible(true);
                    return new UnsafeAllocator() {
                        @Override
                        public <T> T newInstance(final Class<T> c) throws Exception {
                            assertInstantiable(c);
                            return (T)newInstance2.invoke((Object)null, new Object[] { c, Object.class });
                        }
                    };
                }
                catch (final Exception ex3) {
                    return new UnsafeAllocator() {
                        @Override
                        public <T> T newInstance(final Class<T> c) {
                            throw new UnsupportedOperationException("Cannot allocate " + (Object)c + ". Usage of JDK sun.misc.Unsafe is enabled, but it could not be used. Make sure your runtime is configured correctly.");
                        }
                    };
                }
            }
        }
    }
}
