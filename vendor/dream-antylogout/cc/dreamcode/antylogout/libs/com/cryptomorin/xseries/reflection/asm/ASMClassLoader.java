package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.asm;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

final class ASMClassLoader extends ClassLoader
{
    private static final String DEFINE_CLASS = "defineClass";
    
    protected ASMClassLoader() {
    }
    
    protected Class<?> defineClass(final String name, final byte[] bytes) {
        return super.defineClass(asmTypeToBinary(name), bytes, 0, bytes.length);
    }
    
    private static Class<?> defineClassLombockTransplanter(final String className, final byte[] bytecode, final ClassLoader classLoader) {
        try {
            final Class<?> methodHandles = Class.forName("java.lang.invoke.MethodHandles");
            final Class<?> methodHandle = Class.forName("java.lang.invoke.MethodHandle");
            final Class<?> methodType = Class.forName("java.lang.invoke.MethodType");
            final Class<?> methodHandlesLookup = Class.forName("java.lang.invoke.MethodHandles$Lookup");
            final Method lookupMethod = methodHandles.getDeclaredMethod("lookup", (Class<?>[])new Class[0]);
            final Method methodTypeMethod = methodType.getDeclaredMethod("methodType", Class.class, Class[].class);
            final Method findVirtualMethod = methodHandlesLookup.getDeclaredMethod("findVirtual", Class.class, String.class, methodType);
            final Method invokeMethod = methodHandle.getDeclaredMethod("invokeWithArguments", Object[].class);
            final Object lookup = lookupMethod.invoke((Object)null, new Object[0]);
            final Object type = methodTypeMethod.invoke((Object)null, new Object[] { Class.class, { String.class, byte[].class, Integer.TYPE, Integer.TYPE } });
            final Object method = findVirtualMethod.invoke(lookup, new Object[] { classLoader.getClass(), "defineClass", type });
            return (Class)invokeMethod.invoke(method, new Object[] { { classLoader, className, bytecode, 0, bytecode.length } });
        }
        catch (final ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException((Throwable)e);
        }
    }
    
    private static Class<?> defineClassJLA(final String generatedClassName, final byte[] bytecode) {
        try {
            final Class<?> ClassDefiner = Class.forName("jdk.internal.reflect.ClassDefiner");
            final Method meth = ClassDefiner.getDeclaredMethod("defineClass", String.class, byte[].class, Integer.TYPE, Integer.TYPE, ClassLoader.class);
            return (Class)meth.invoke((Object)null, new Object[] { generatedClassName, bytecode, 0, bytecode.length, XReflectASM.class.getClassLoader() });
        }
        catch (final ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException((Throwable)e);
        }
    }
    
    private static String asmTypeToBinary(final String dirPath) {
        return dirPath.replace('/', '.');
    }
    
    private static Class<?> methodHandleLoadClass(final byte[] bytecode) {
        final MethodHandles.Lookup lookup = MethodHandles.lookup();
        try {
            final MethodHandle defineClass = lookup.findVirtual((Class)MethodHandles.Lookup.class, "defineClass", MethodType.methodType((Class)Class.class, (Class)byte[].class));
            return defineClass.invoke(lookup, bytecode);
        }
        catch (final Throwable e) {
            throw new IllegalStateException(e);
        }
    }
    
    private static Class<?> loadClass(final String className, final byte[] bytecode) {
        Class<?> clazz;
        try {
            final ClassLoader loader = ClassLoader.getSystemClassLoader();
            final Class<?> cls = Class.forName("java.lang.ClassLoader");
            final Method method = cls.getDeclaredMethod("defineClass", String.class, byte[].class, Integer.TYPE, Integer.TYPE);
            method.setAccessible(true);
            try {
                final Object[] args = { className, bytecode, 0, bytecode.length };
                clazz = (Class)method.invoke((Object)loader, args);
            }
            finally {
                method.setAccessible(false);
            }
        }
        catch (final Exception e) {
            throw new IllegalStateException("Failed to load class " + className + " into the system class loader", (Throwable)e);
        }
        return clazz;
    }
}
