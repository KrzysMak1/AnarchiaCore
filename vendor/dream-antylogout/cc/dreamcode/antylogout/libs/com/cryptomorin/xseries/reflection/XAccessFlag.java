package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection;

import java.util.Locale;
import java.util.StringJoiner;
import java.lang.reflect.Member;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Contract;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.Collection;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public enum XAccessFlag
{
    PUBLIC(1, true, new JVMLocation[] { JVMLocation.CLASS, JVMLocation.FIELD, JVMLocation.METHOD, JVMLocation.INNER_CLASS }), 
    PRIVATE(2, true, new JVMLocation[] { JVMLocation.FIELD, JVMLocation.METHOD, JVMLocation.INNER_CLASS }), 
    PROTECTED(4, true, new JVMLocation[] { JVMLocation.FIELD, JVMLocation.METHOD, JVMLocation.INNER_CLASS }), 
    STATIC(8, true, new JVMLocation[] { JVMLocation.FIELD, JVMLocation.METHOD, JVMLocation.INNER_CLASS }), 
    FINAL(16, true, new JVMLocation[] { JVMLocation.CLASS, JVMLocation.FIELD, JVMLocation.METHOD, JVMLocation.INNER_CLASS, JVMLocation.METHOD_PARAMETER }), 
    SUPER(32, false, new JVMLocation[] { JVMLocation.CLASS }), 
    OPEN(32, false, new JVMLocation[] { JVMLocation.MODULE }), 
    TRANSITIVE(32, false, new JVMLocation[] { JVMLocation.MODULE_REQUIRES }), 
    SYNCHRONIZED(32, true, new JVMLocation[] { JVMLocation.METHOD }), 
    STATIC_PHASE(64, false, new JVMLocation[] { JVMLocation.MODULE_REQUIRES }), 
    VOLATILE(64, true, new JVMLocation[] { JVMLocation.FIELD }), 
    BRIDGE(getPrivateMod("BRIDGE"), false, new JVMLocation[] { JVMLocation.METHOD }), 
    TRANSIENT(128, true, new JVMLocation[] { JVMLocation.FIELD }), 
    VARARGS(getPrivateMod("VARARGS"), false, new JVMLocation[] { JVMLocation.METHOD }), 
    NATIVE(256, true, new JVMLocation[] { JVMLocation.METHOD }), 
    INTERFACE(512, false, new JVMLocation[] { JVMLocation.CLASS, JVMLocation.INNER_CLASS }), 
    ABSTRACT(1024, true, new JVMLocation[] { JVMLocation.CLASS, JVMLocation.METHOD, JVMLocation.INNER_CLASS }), 
    STRICT(2048, true, new JVMLocation[0]), 
    SYNTHETIC(getPrivateMod("SYNTHETIC"), false, new JVMLocation[] { JVMLocation.CLASS, JVMLocation.FIELD, JVMLocation.METHOD, JVMLocation.INNER_CLASS, JVMLocation.METHOD_PARAMETER, JVMLocation.MODULE, JVMLocation.MODULE_REQUIRES, JVMLocation.MODULE_EXPORTS, JVMLocation.MODULE_OPENS }), 
    ANNOTATION(getPrivateMod("ANNOTATION"), false, new JVMLocation[] { JVMLocation.CLASS, JVMLocation.INNER_CLASS }), 
    ENUM(getPrivateMod("ENUM"), false, new JVMLocation[] { JVMLocation.CLASS, JVMLocation.FIELD, JVMLocation.INNER_CLASS }), 
    MANDATED(getPrivateMod("MANDATED"), false, new JVMLocation[] { JVMLocation.METHOD_PARAMETER, JVMLocation.MODULE, JVMLocation.MODULE_REQUIRES, JVMLocation.MODULE_EXPORTS, JVMLocation.MODULE_OPENS }), 
    MODULE(32768, false, new JVMLocation[] { JVMLocation.CLASS });
    
    private static final XAccessFlag[] VALUES;
    private final int mask;
    private final boolean sourceModifier;
    private final Set<JVMLocation> locations;
    
    private XAccessFlag(final int mask, final boolean sourceModifier, final JVMLocation[] locations) {
        this.mask = mask;
        this.sourceModifier = sourceModifier;
        this.locations = (Set<JVMLocation>)Collections.unmodifiableSet((Set)((locations.length == 0) ? EnumSet.noneOf((Class)JVMLocation.class) : EnumSet.copyOf((Collection)Arrays.asList((Object[])locations))));
    }
    
    private static int getPrivateMod(final String name) {
        try {
            final Class<?> AccessFlag = Class.forName("java.lang.reflect.AccessFlag");
            final Field modField = AccessFlag.getDeclaredField(name);
            final Object mod = modField.get((Object)null);
            final Method mask = AccessFlag.getDeclaredMethod("mask", (Class<?>[])new Class[0]);
            return (int)mask.invoke(mod, new Object[0]);
        }
        catch (final Throwable ex) {
            try {
                return (int)Modifier.class.getDeclaredField(name).get((Object)null);
            }
            catch (final NoSuchFieldException | IllegalAccessException ex2) {
                return 0;
            }
        }
    }
    
    @Contract(pure = true)
    public int mask() {
        return this.mask;
    }
    
    @Contract(pure = true)
    public boolean sourceModifier() {
        return this.sourceModifier;
    }
    
    @NotNull
    @Contract(pure = true)
    public Set<JVMLocation> locations() {
        return this.locations;
    }
    
    @Contract(pure = true)
    public static Set<XAccessFlag> of(final int mod) {
        final Set<XAccessFlag> accessFlags = (Set<XAccessFlag>)EnumSet.noneOf((Class)XAccessFlag.class);
        for (final XAccessFlag flag : XAccessFlag.VALUES) {
            if (flag.isSet(mod)) {
                accessFlags.add((Object)flag);
            }
        }
        return accessFlags;
    }
    
    @Contract(pure = true)
    public int add(final int mod) {
        return mod | this.mask;
    }
    
    @Contract(pure = true)
    public int remove(final int mod) {
        return mod & ~this.mask;
    }
    
    @Contract(pure = true)
    public boolean isSet(final int mod) {
        return (mod & this.mask) != 0x0;
    }
    
    @Contract(pure = true)
    public static boolean isSet(final int mod, final XAccessFlag... flags) {
        for (final XAccessFlag modifier : flags) {
            if (!modifier.isSet(mod)) {
                return false;
            }
        }
        return true;
    }
    
    @Contract(pure = true)
    public static Optional<Integer> getModifiers(final Object jvm) {
        if (jvm instanceof Class) {
            return (Optional<Integer>)Optional.of((Object)((Class)jvm).getModifiers());
        }
        if (jvm instanceof Member) {
            return (Optional<Integer>)Optional.of((Object)((Member)jvm).getModifiers());
        }
        return (Optional<Integer>)Optional.empty();
    }
    
    @Contract(pure = true)
    public static String toString(final int mod) {
        final StringJoiner flags = new StringJoiner((CharSequence)" ", (CharSequence)("Flags::" + mod + '('), (CharSequence)")");
        for (final XAccessFlag accessFlag : XAccessFlag.VALUES) {
            if (accessFlag.isSet(mod)) {
                flags.add((CharSequence)accessFlag.name().toLowerCase(Locale.ENGLISH));
            }
        }
        return flags.toString();
    }
    
    static {
        VALUES = values();
    }
    
    public enum JVMLocation
    {
        CLASS, 
        FIELD, 
        METHOD, 
        INNER_CLASS, 
        METHOD_PARAMETER, 
        MODULE, 
        MODULE_REQUIRES, 
        MODULE_EXPORTS, 
        MODULE_OPENS;
    }
}
