package cc.dreamcode.antylogout.libs.com.google.gson.internal;

import java.lang.reflect.Type;

public final class Primitives
{
    private Primitives() {
    }
    
    public static boolean isPrimitive(final Type type) {
        return type instanceof Class && ((Class)type).isPrimitive();
    }
    
    public static boolean isWrapperType(final Type type) {
        return type == Integer.class || type == Float.class || type == Byte.class || type == Double.class || type == Long.class || type == Character.class || type == Boolean.class || type == Short.class || type == Void.class;
    }
    
    public static <T> Class<T> wrap(final Class<T> type) {
        if (type == Integer.TYPE) {
            return (Class<T>)Integer.class;
        }
        if (type == Float.TYPE) {
            return (Class<T>)Float.class;
        }
        if (type == Byte.TYPE) {
            return (Class<T>)Byte.class;
        }
        if (type == Double.TYPE) {
            return (Class<T>)Double.class;
        }
        if (type == Long.TYPE) {
            return (Class<T>)Long.class;
        }
        if (type == Character.TYPE) {
            return (Class<T>)Character.class;
        }
        if (type == Boolean.TYPE) {
            return (Class<T>)Boolean.class;
        }
        if (type == Short.TYPE) {
            return (Class<T>)Short.class;
        }
        if (type == Void.TYPE) {
            return (Class<T>)Void.class;
        }
        return type;
    }
    
    public static <T> Class<T> unwrap(final Class<T> type) {
        if (type == Integer.class) {
            return Integer.TYPE;
        }
        if (type == Float.class) {
            return Float.TYPE;
        }
        if (type == Byte.class) {
            return Byte.TYPE;
        }
        if (type == Double.class) {
            return Double.TYPE;
        }
        if (type == Long.class) {
            return Long.TYPE;
        }
        if (type == Character.class) {
            return Character.TYPE;
        }
        if (type == Boolean.class) {
            return Boolean.TYPE;
        }
        if (type == Short.class) {
            return Short.TYPE;
        }
        if (type == Void.class) {
            return Void.TYPE;
        }
        return type;
    }
}
