package cc.dreamcode.antylogout.libs.com.google.gson.internal.reflect;

import java.lang.reflect.Constructor;
import cc.dreamcode.antylogout.libs.com.google.gson.JsonIOException;
import java.lang.reflect.Field;

public class ReflectionHelper
{
    private ReflectionHelper() {
    }
    
    public static void makeAccessible(final Field field) throws JsonIOException {
        try {
            field.setAccessible(true);
        }
        catch (final Exception exception) {
            throw new JsonIOException("Failed making field '" + field.getDeclaringClass().getName() + "#" + field.getName() + "' accessible; either change its visibility or write a custom TypeAdapter for its declaring type", (Throwable)exception);
        }
    }
    
    private static String constructorToString(final Constructor<?> constructor) {
        final StringBuilder stringBuilder = new StringBuilder(constructor.getDeclaringClass().getName()).append('#').append(constructor.getDeclaringClass().getSimpleName()).append('(');
        final Class<?>[] parameters = constructor.getParameterTypes();
        for (int i = 0; i < parameters.length; ++i) {
            if (i > 0) {
                stringBuilder.append(", ");
            }
            stringBuilder.append(parameters[i].getSimpleName());
        }
        return stringBuilder.append(')').toString();
    }
    
    public static String tryMakeAccessible(final Constructor<?> constructor) {
        try {
            constructor.setAccessible(true);
            return null;
        }
        catch (final Exception exception) {
            return "Failed making constructor '" + constructorToString(constructor) + "' accessible; either change its visibility or write a custom InstanceCreator or TypeAdapter for its declaring type: " + exception.getMessage();
        }
    }
    
    public static RuntimeException createExceptionForUnexpectedIllegalAccess(final IllegalAccessException exception) {
        throw new RuntimeException("Unexpected IllegalAccessException occurred (Gson 2.9.1). Certain ReflectionAccessFilter features require Java >= 9 to work correctly. If you are not using ReflectionAccessFilter, report this to the Gson maintainers.", (Throwable)exception);
    }
}
