package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.proxy.processors;

import java.util.IdentityHashMap;
import java.util.Arrays;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.proxy.ReflectiveProxyObject;
import java.util.Map;

public final class MappedType
{
    public static final Map<Class<? extends ReflectiveProxyObject>, Class<?>> LOOK_AHEAD;
    public final Class<?> synthetic;
    public final Class<?> real;
    
    public MappedType(final Class<?> synthetic, final Class<?> real) {
        this.synthetic = synthetic;
        this.real = real;
    }
    
    public boolean isDifferent() {
        return this.synthetic != this.real;
    }
    
    public static Class<?>[] getRealTypes(final MappedType[] types) {
        return (Class[])Arrays.stream((Object[])types).map(x -> x.real).toArray(Class[]::new);
    }
    
    public static Class<?> getMappedTypeOrCreate(final Class<? extends ReflectiveProxyObject> synthetic) {
        Class<?> real = (Class<?>)MappedType.LOOK_AHEAD.get((Object)synthetic);
        if (real == null) {
            final ReflectiveAnnotationProcessor processor = new ReflectiveAnnotationProcessor(synthetic);
            processor.processTargetClass();
            real = processor.getTargetClass();
            if (real == null) {
                throw new IllegalStateException("Look ahead type " + (Object)synthetic + " could not be processed.");
            }
        }
        return real;
    }
    
    @Override
    public String toString() {
        return "MappedType[synthetic: " + (Object)this.synthetic + ", real: " + (Object)this.real + ']';
    }
    
    static {
        LOOK_AHEAD = (Map)new IdentityHashMap();
    }
}
