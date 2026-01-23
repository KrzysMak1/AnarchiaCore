package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm.objects;

import java.util.Collections;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.XAccessFlag;
import java.util.Set;
import org.jetbrains.annotations.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Constructor;
import org.jetbrains.annotations.Contract;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.ApiStatus;
import java.lang.reflect.AnnotatedElement;

@ApiStatus.Experimental
public interface ReflectedObject extends AnnotatedElement
{
    @NotNull
    @Contract(value = "_ -> new", pure = true)
    default ReflectedObject of(@NotNull final Class<?> clazz) {
        return new ReflectedObjectClass((Class<?>)Objects.requireNonNull((Object)clazz));
    }
    
    @NotNull
    @Contract(value = "_ -> new", pure = true)
    default ReflectedObject of(@NotNull final Constructor<?> constructor) {
        return new ReflectedObjectConstructor((Constructor<?>)Objects.requireNonNull((Object)constructor));
    }
    
    @NotNull
    @Contract(value = "_ -> new", pure = true)
    default ReflectedObject of(@NotNull final Method method) {
        return new ReflectedObjectMethod((Method)Objects.requireNonNull((Object)method));
    }
    
    @NotNull
    @Contract(value = "_ -> new", pure = true)
    default ReflectedObject of(@NotNull final Field field) {
        return new ReflectedObjectField((Field)Objects.requireNonNull((Object)field));
    }
    
    @NotNull
    @Contract(pure = true)
    Object unreflect();
    
    @NotNull
    @Contract(pure = true)
    Type type();
    
    @NotNull
    @Contract(pure = true)
    String name();
    
    @Nullable
    @Contract(pure = true)
    Class<?> getDeclaringClass();
    
    @Contract(pure = true)
    int getModifiers();
    
    @NotNull
    @Contract(value = "-> new", pure = true)
    default Set<XAccessFlag> accessFlags() {
        return (Set<XAccessFlag>)Collections.unmodifiableSet((Set)XAccessFlag.of(this.getModifiers()));
    }
    
    public enum Type
    {
        CLASS, 
        CONSTRUCTOR, 
        METHOD, 
        FIELD;
    }
}
