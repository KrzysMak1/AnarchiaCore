package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.proxy;

import java.util.Collections;
import java.util.Iterator;
import java.util.HashMap;
import java.util.function.Function;
import java.util.Map;
import java.util.Collection;
import java.util.function.Supplier;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public abstract class OverloadedMethod<T>
{
    public abstract T get(final Supplier<String> p0);
    
    public abstract Collection<T> getOverloads();
    
    static String getParameterDescriptor(final Class<?>[] parameters) {
        final StringBuilder stringBuilder = new StringBuilder(parameters.length * 10);
        for (Class<?> currentClass : parameters) {
            final Class<?> parameter = currentClass;
            while (currentClass.isArray()) {
                stringBuilder.append('[');
                currentClass = currentClass.getComponentType();
            }
            if (currentClass.isPrimitive()) {
                stringBuilder.append(getDescriptor(currentClass));
            }
            else if (currentClass == String.class) {
                stringBuilder.append('@');
            }
            else {
                stringBuilder.append(currentClass.getName());
            }
        }
        return stringBuilder.toString();
    }
    
    static char getDescriptor(final Class<?> currentClass) {
        if (currentClass == Integer.TYPE) {
            return 'I';
        }
        if (currentClass == Void.TYPE) {
            return 'V';
        }
        if (currentClass == Boolean.TYPE) {
            return 'Z';
        }
        if (currentClass == Byte.TYPE) {
            return 'B';
        }
        if (currentClass == Character.TYPE) {
            return 'C';
        }
        if (currentClass == Short.TYPE) {
            return 'S';
        }
        if (currentClass == Double.TYPE) {
            return 'D';
        }
        if (currentClass == Float.TYPE) {
            return 'F';
        }
        if (currentClass == Long.TYPE) {
            return 'J';
        }
        throw new AssertionError((Object)("Unknown primitive: " + (Object)currentClass));
    }
    
    public static final class Builder<T>
    {
        private final Map<String, Map<String, T>> descriptorMap;
        private final Function<T, String> descritporProcessor;
        
        public Builder(final Function<T, String> descritporProcessor) {
            this.descriptorMap = (Map<String, Map<String, T>>)new HashMap(10);
            this.descritporProcessor = descritporProcessor;
        }
        
        public void add(final T method, final String name) {
            final Map<String, T> descriptors = (Map<String, T>)this.descriptorMap.computeIfAbsent((Object)name, k -> new HashMap(3));
            final String descriptor = (String)this.descritporProcessor.apply((Object)method);
            if (descriptors.put((Object)descriptor, (Object)method) != null) {
                throw new IllegalArgumentException("Method named '" + name + "' with descriptor '" + descriptor + "' was already added: " + (Object)descriptors);
            }
        }
        
        public ClassOverloadedMethods<T> build() {
            final HashMap<String, OverloadedMethod<T>> nameMapped = (HashMap<String, OverloadedMethod<T>>)new HashMap(this.descriptorMap.size());
            final ClassOverloadedMethods<T> classOverloadedMethods = new ClassOverloadedMethods<T>((java.util.Map<String, OverloadedMethod<T>>)nameMapped);
            this.build((Map<String, OverloadedMethod<T>>)nameMapped);
            return classOverloadedMethods;
        }
        
        public void build(final Map<String, OverloadedMethod<T>> nameMapped) {
            for (final Map.Entry<String, Map<String, T>> method : this.descriptorMap.entrySet()) {
                OverloadedMethod<T> overload;
                if (((Map)method.getValue()).size() == 1) {
                    overload = new Single<T>((T)((Map)method.getValue()).values().iterator().next());
                }
                else {
                    overload = new Multi<T>((java.util.Map<String, T>)method.getValue());
                }
                nameMapped.put((Object)method.getKey(), (Object)overload);
            }
        }
    }
    
    private static final class Single<T> extends OverloadedMethod<T>
    {
        private final T object;
        
        public Single(final T object) {
            this.object = object;
        }
        
        @Override
        public T get(final Supplier<String> descriptor) {
            return this.object;
        }
        
        @Override
        public Collection<T> getOverloads() {
            return (Collection<T>)Collections.singleton((Object)this.object);
        }
        
        @Override
        public String toString() {
            return "Single";
        }
    }
    
    private static final class Multi<T> extends OverloadedMethod<T>
    {
        private final Map<String, T> descriptorMap;
        
        public Multi(final Map<String, T> descriptorMap) {
            this.descriptorMap = descriptorMap;
        }
        
        @Override
        public T get(final Supplier<String> descriptor) {
            return (T)this.descriptorMap.get(descriptor.get());
        }
        
        @Override
        public Collection<T> getOverloads() {
            return (Collection<T>)this.descriptorMap.values();
        }
        
        @Override
        public String toString() {
            return "Multi(" + (Object)this.descriptorMap.keySet() + ')';
        }
    }
}
