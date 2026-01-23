package cc.dreamcode.antylogout.libs.eu.okaeri.configs.schema;

import java.util.HashSet;
import java.util.HashMap;
import lombok.Generated;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.OkaeriConfig;
import java.util.Objects;
import java.util.Arrays;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.stream.Collectors;
import lombok.NonNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Map;

public class GenericsDeclaration
{
    private static final Map<String, Class<?>> PRIMITIVES;
    private static final Map<Class<?>, Class<?>> PRIMITIVE_TO_WRAPPER;
    private static final Set<Class<?>> PRIMITIVE_WRAPPERS;
    private Class<?> type;
    private List<GenericsDeclaration> subtype;
    private boolean isEnum;
    
    private GenericsDeclaration(final Class<?> type) {
        this.subtype = (List<GenericsDeclaration>)new ArrayList();
        this.type = type;
        this.isEnum = type.isEnum();
    }
    
    public static boolean isUnboxedCompatibleWithBoxed(@NonNull final Class<?> unboxedClazz, @NonNull final Class<?> boxedClazz) {
        if (unboxedClazz == null) {
            throw new NullPointerException("unboxedClazz is marked non-null but is null");
        }
        if (boxedClazz == null) {
            throw new NullPointerException("boxedClazz is marked non-null but is null");
        }
        final Class<?> primitiveWrapper = (Class<?>)GenericsDeclaration.PRIMITIVE_TO_WRAPPER.get((Object)unboxedClazz);
        return primitiveWrapper == boxedClazz;
    }
    
    public static boolean doBoxTypesMatch(@NonNull final Class<?> clazz1, @NonNull final Class<?> clazz2) {
        if (clazz1 == null) {
            throw new NullPointerException("clazz1 is marked non-null but is null");
        }
        if (clazz2 == null) {
            throw new NullPointerException("clazz2 is marked non-null but is null");
        }
        return isUnboxedCompatibleWithBoxed(clazz1, clazz2) || isUnboxedCompatibleWithBoxed(clazz2, clazz1);
    }
    
    public static GenericsDeclaration of(@NonNull final Object type, @NonNull final List<Object> subtypes) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        if (subtypes == null) {
            throw new NullPointerException("subtypes is marked non-null but is null");
        }
        final Class<?> finalType = (type instanceof Class) ? ((Class)type) : type.getClass();
        final GenericsDeclaration declaration = new GenericsDeclaration(finalType);
        declaration.setSubtype((List<GenericsDeclaration>)subtypes.stream().map(GenericsDeclaration::of).collect(Collectors.toList()));
        return declaration;
    }
    
    public static GenericsDeclaration of(final Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof GenericsDeclaration) {
            return (GenericsDeclaration)object;
        }
        if (object instanceof Class) {
            return new GenericsDeclaration((Class<?>)object);
        }
        if (object instanceof Type) {
            return from((Type)object);
        }
        return new GenericsDeclaration(object.getClass());
    }
    
    private static GenericsDeclaration from(final Type type) {
        if (type instanceof ParameterizedType) {
            final ParameterizedType parameterizedType = (ParameterizedType)type;
            final Type rawType = parameterizedType.getRawType();
            final Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            if (rawType instanceof Class) {
                final GenericsDeclaration declaration = new GenericsDeclaration((Class<?>)rawType);
                declaration.setSubtype((List<GenericsDeclaration>)Arrays.stream((Object[])actualTypeArguments).map(GenericsDeclaration::of).filter(Objects::nonNull).collect(Collectors.toList()));
                return declaration;
            }
        }
        throw new IllegalArgumentException("cannot process type: " + (Object)type + " [" + (Object)type.getClass() + "]");
    }
    
    public GenericsDeclaration getSubtypeAtOrNull(final int index) {
        return (this.subtype == null) ? null : ((index >= this.subtype.size()) ? null : ((GenericsDeclaration)this.subtype.get(index)));
    }
    
    public GenericsDeclaration getSubtypeAtOrThrow(final int index) {
        final GenericsDeclaration subtype = this.getSubtypeAtOrNull(index);
        if (subtype == null) {
            throw new IllegalArgumentException("Cannot resolve subtype with index " + index + " for " + (Object)this);
        }
        return subtype;
    }
    
    public Class<?> wrap() {
        return (Class)GenericsDeclaration.PRIMITIVE_TO_WRAPPER.get((Object)this.type);
    }
    
    public Object unwrapValue(final Object object) {
        if (object instanceof Boolean) {
            return object;
        }
        if (object instanceof Byte) {
            return object;
        }
        if (object instanceof Character) {
            return object;
        }
        if (object instanceof Double) {
            return object;
        }
        if (object instanceof Float) {
            return object;
        }
        if (object instanceof Integer) {
            return object;
        }
        if (object instanceof Long) {
            return object;
        }
        if (object instanceof Short) {
            return object;
        }
        return object;
    }
    
    public boolean isPrimitive() {
        return this.type.isPrimitive();
    }
    
    public boolean isPrimitiveWrapper() {
        return GenericsDeclaration.PRIMITIVE_WRAPPERS.contains((Object)this.type);
    }
    
    public boolean isConfig() {
        return OkaeriConfig.class.isAssignableFrom(this.type);
    }
    
    public boolean hasSubtypes() {
        return this.subtype != null && !this.subtype.isEmpty();
    }
    
    @Generated
    public Class<?> getType() {
        return this.type;
    }
    
    @Generated
    public List<GenericsDeclaration> getSubtype() {
        return this.subtype;
    }
    
    @Generated
    public boolean isEnum() {
        return this.isEnum;
    }
    
    @Generated
    public void setType(final Class<?> type) {
        this.type = type;
    }
    
    @Generated
    public void setSubtype(final List<GenericsDeclaration> subtype) {
        this.subtype = subtype;
    }
    
    @Generated
    public void setEnum(final boolean isEnum) {
        this.isEnum = isEnum;
    }
    
    @Generated
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof GenericsDeclaration)) {
            return false;
        }
        final GenericsDeclaration other = (GenericsDeclaration)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.isEnum() != other.isEnum()) {
            return false;
        }
        final Object this$type = this.getType();
        final Object other$type = other.getType();
        Label_0078: {
            if (this$type == null) {
                if (other$type == null) {
                    break Label_0078;
                }
            }
            else if (this$type.equals(other$type)) {
                break Label_0078;
            }
            return false;
        }
        final Object this$subtype = this.getSubtype();
        final Object other$subtype = other.getSubtype();
        if (this$subtype == null) {
            if (other$subtype == null) {
                return true;
            }
        }
        else if (this$subtype.equals(other$subtype)) {
            return true;
        }
        return false;
    }
    
    @Generated
    protected boolean canEqual(final Object other) {
        return other instanceof GenericsDeclaration;
    }
    
    @Generated
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + (this.isEnum() ? 79 : 97);
        final Object $type = this.getType();
        result = result * 59 + (($type == null) ? 43 : $type.hashCode());
        final Object $subtype = this.getSubtype();
        result = result * 59 + (($subtype == null) ? 43 : $subtype.hashCode());
        return result;
    }
    
    @Generated
    @Override
    public String toString() {
        return "GenericsDeclaration(type=" + (Object)this.getType() + ", subtype=" + (Object)this.getSubtype() + ", isEnum=" + this.isEnum() + ")";
    }
    
    @Generated
    public GenericsDeclaration(final Class<?> type, final List<GenericsDeclaration> subtype, final boolean isEnum) {
        this.subtype = (List<GenericsDeclaration>)new ArrayList();
        this.type = type;
        this.subtype = subtype;
        this.isEnum = isEnum;
    }
    
    static {
        PRIMITIVES = (Map)new HashMap();
        PRIMITIVE_TO_WRAPPER = (Map)new HashMap();
        PRIMITIVE_WRAPPERS = (Set)new HashSet();
        GenericsDeclaration.PRIMITIVES.put((Object)Boolean.TYPE.getName(), (Object)Boolean.TYPE);
        GenericsDeclaration.PRIMITIVES.put((Object)Byte.TYPE.getName(), (Object)Byte.TYPE);
        GenericsDeclaration.PRIMITIVES.put((Object)Character.TYPE.getName(), (Object)Character.TYPE);
        GenericsDeclaration.PRIMITIVES.put((Object)Double.TYPE.getName(), (Object)Double.TYPE);
        GenericsDeclaration.PRIMITIVES.put((Object)Float.TYPE.getName(), (Object)Float.TYPE);
        GenericsDeclaration.PRIMITIVES.put((Object)Integer.TYPE.getName(), (Object)Integer.TYPE);
        GenericsDeclaration.PRIMITIVES.put((Object)Long.TYPE.getName(), (Object)Long.TYPE);
        GenericsDeclaration.PRIMITIVES.put((Object)Short.TYPE.getName(), (Object)Short.TYPE);
        GenericsDeclaration.PRIMITIVE_TO_WRAPPER.put((Object)Boolean.TYPE, (Object)Boolean.class);
        GenericsDeclaration.PRIMITIVE_TO_WRAPPER.put((Object)Byte.TYPE, (Object)Byte.class);
        GenericsDeclaration.PRIMITIVE_TO_WRAPPER.put((Object)Character.TYPE, (Object)Character.class);
        GenericsDeclaration.PRIMITIVE_TO_WRAPPER.put((Object)Double.TYPE, (Object)Double.class);
        GenericsDeclaration.PRIMITIVE_TO_WRAPPER.put((Object)Float.TYPE, (Object)Float.class);
        GenericsDeclaration.PRIMITIVE_TO_WRAPPER.put((Object)Integer.TYPE, (Object)Integer.class);
        GenericsDeclaration.PRIMITIVE_TO_WRAPPER.put((Object)Long.TYPE, (Object)Long.class);
        GenericsDeclaration.PRIMITIVE_TO_WRAPPER.put((Object)Short.TYPE, (Object)Short.class);
        GenericsDeclaration.PRIMITIVE_WRAPPERS.add((Object)Boolean.class);
        GenericsDeclaration.PRIMITIVE_WRAPPERS.add((Object)Byte.class);
        GenericsDeclaration.PRIMITIVE_WRAPPERS.add((Object)Character.class);
        GenericsDeclaration.PRIMITIVE_WRAPPERS.add((Object)Double.class);
        GenericsDeclaration.PRIMITIVE_WRAPPERS.add((Object)Float.class);
        GenericsDeclaration.PRIMITIVE_WRAPPERS.add((Object)Integer.class);
        GenericsDeclaration.PRIMITIVE_WRAPPERS.add((Object)Long.class);
        GenericsDeclaration.PRIMITIVE_WRAPPERS.add((Object)Short.class);
    }
}
