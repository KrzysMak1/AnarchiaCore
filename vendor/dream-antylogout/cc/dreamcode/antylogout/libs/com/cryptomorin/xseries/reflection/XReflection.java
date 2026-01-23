package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection;

import java.util.regex.Matcher;
import java.util.IdentityHashMap;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.minecraft.MinecraftPackage;
import java.util.regex.Pattern;
import java.util.EnumSet;
import java.util.stream.Stream;
import java.util.Iterator;
import java.util.Collections;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.asm.XReflectASM;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.proxy.ReflectiveProxy;
import java.lang.reflect.Array;
import java.util.concurrent.CompletableFuture;
import java.util.List;
import java.util.ArrayList;
import java.util.function.Supplier;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.aggregate.AggregateReflectiveSupplier;
import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.Collection;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.aggregate.AggregateReflectiveHandle;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm.classes.StaticClassHandle;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm.classes.DynamicClassHandle;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.minecraft.MinecraftClassHandle;
import java.util.Objects;
import java.util.concurrent.Callable;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.aggregate.VersionHandle;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.bukkit.Bukkit;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.proxy.ReflectiveProxyObject;
import java.util.Map;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.minecraft.MinecraftMapping;
import java.util.Set;
import org.jetbrains.annotations.TestOnly;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

public final class XReflection
{
    @Nullable
    @ApiStatus.Internal
    public static final String NMS_VERSION;
    @ApiStatus.Internal
    public static final String XSERIES_VERSION = "13.3.2";
    @TestOnly
    public static final String DISABLE_MINECRAFT_CAPABILITIES_PROPERTY = "xseries.xreflection.disable.minecraft";
    @ApiStatus.Internal
    public static final boolean SUPPORTS_ASM;
    public static final int MAJOR_NUMBER;
    public static final int MINOR_NUMBER;
    public static final int PATCH_NUMBER;
    @ApiStatus.Internal
    public static final String CRAFTBUKKIT_PACKAGE;
    @ApiStatus.Internal
    public static final String NMS_PACKAGE;
    @ApiStatus.Internal
    @ApiStatus.Experimental
    public static final Set<MinecraftMapping> SUPPORTED_MAPPINGS;
    private static final Map<Class<?>, ReflectiveProxyObject> PROXIFIED_CLASSES;
    
    @Nullable
    @ApiStatus.Internal
    public static String findNMSVersionString() {
        String found = null;
        for (final Package pack : Package.getPackages()) {
            final String name = pack.getName();
            if (name.startsWith("org.bukkit.craftbukkit.v")) {
                found = pack.getName().split("\\.")[3];
                try {
                    Class.forName("org.bukkit.craftbukkit." + found + ".entity.CraftPlayer");
                    break;
                }
                catch (final ClassNotFoundException e) {
                    found = null;
                }
            }
        }
        return found;
    }
    
    private static String isMinecraftDisabled() {
        try {
            return System.getProperty("xseries.xreflection.disable.minecraft");
        }
        catch (final SecurityException ignored) {
            return null;
        }
    }
    
    @NotNull
    @Contract(pure = true)
    public static String getVersionInformation() {
        return "(NMS: " + ((XReflection.NMS_VERSION == null) ? "Unknown NMS" : XReflection.NMS_VERSION) + " | Parsed: " + XReflection.MAJOR_NUMBER + '.' + XReflection.MINOR_NUMBER + '.' + XReflection.PATCH_NUMBER + " | Minecraft: " + Bukkit.getVersion() + " | Bukkit: " + Bukkit.getBukkitVersion() + ')';
    }
    
    @Nullable
    @Contract(pure = true)
    public static Integer getLatestPatchNumberOf(final int minorVersion) {
        if (minorVersion <= 0) {
            throw new IllegalArgumentException("Minor version must be positive: " + minorVersion);
        }
        final int[] patches = { 1, 5, 2, 7, 2, 4, 10, 8, 4, 2, 2, 2, 2, 4, 2, 5, 1, 2, 4, 6, 5 };
        if (minorVersion > patches.length) {
            return null;
        }
        return patches[minorVersion - 1];
    }
    
    private XReflection() {
    }
    
    @NotNull
    @Contract(value = "_, _ -> new", pure = true)
    public static <T> VersionHandle<T> v(final int version, final T handle) {
        return new VersionHandle<T>(version, handle);
    }
    
    @NotNull
    @Contract(value = "_, _, _ -> new", pure = true)
    public static <T> VersionHandle<T> v(final int version, final int patch, final T handle) {
        return new VersionHandle<T>(version, patch, handle);
    }
    
    @NotNull
    @Contract(value = "_, _ -> new", pure = true)
    public static <T> VersionHandle<T> v(final int version, final Callable<T> handle) {
        return new VersionHandle<T>(version, handle);
    }
    
    @NotNull
    @Contract(value = "_, _, _ -> new", pure = true)
    public static <T> VersionHandle<T> v(final int version, final int patch, final Callable<T> handle) {
        return new VersionHandle<T>(version, patch, handle);
    }
    
    @Contract(pure = true)
    public static boolean supports(final int minorNumber) {
        return XReflection.MINOR_NUMBER >= minorNumber;
    }
    
    @Contract(pure = true)
    public static boolean supports(final int majorNumber, final int minorNumber, final int patchNumber) {
        if (majorNumber != 1) {
            throw new IllegalArgumentException("Invalid major number: " + majorNumber);
        }
        return supports(minorNumber, patchNumber);
    }
    
    @Contract(pure = true)
    public static boolean supports(final int minorNumber, final int patchNumber) {
        return (XReflection.MINOR_NUMBER == minorNumber) ? (XReflection.PATCH_NUMBER >= patchNumber) : supports(minorNumber);
    }
    
    @Deprecated
    @Contract(pure = true)
    public static boolean supportsPatch(final int patchNumber) {
        return XReflection.PATCH_NUMBER >= patchNumber;
    }
    
    @Deprecated
    @NotNull
    public static Class<?> getNMSClass(@Nullable final String packageName, @NotNull String name) {
        if (packageName != null && supports(17)) {
            name = packageName + '.' + name;
        }
        try {
            return Class.forName(XReflection.NMS_PACKAGE + '.' + name);
        }
        catch (final ClassNotFoundException ex) {
            throw new IllegalArgumentException((Throwable)ex);
        }
    }
    
    @Deprecated
    @NotNull
    public static Class<?> getNMSClass(@NotNull final String name) {
        return getNMSClass(null, name);
    }
    
    @Deprecated
    @NotNull
    public static Class<?> getCraftClass(@NotNull final String name) {
        try {
            return Class.forName(XReflection.CRAFTBUKKIT_PACKAGE + '.' + name);
        }
        catch (final ClassNotFoundException ex) {
            throw new IllegalArgumentException((Throwable)ex);
        }
    }
    
    @NotNull
    @Contract(pure = true)
    public static Class<?> toArrayClass(@NotNull final Class<?> clazz) {
        Objects.requireNonNull((Object)clazz, "Class is null");
        try {
            return Class.forName("[L" + clazz.getName() + ';');
        }
        catch (final ClassNotFoundException ex) {
            throw new IllegalArgumentException("Cannot find array class for class: " + (Object)clazz, (Throwable)ex);
        }
    }
    
    @NotNull
    @Contract(value = "-> new", pure = true)
    public static MinecraftClassHandle ofMinecraft() {
        return new MinecraftClassHandle(new ReflectiveNamespace());
    }
    
    @NotNull
    @Contract(value = "-> new", pure = true)
    public static DynamicClassHandle classHandle() {
        return new DynamicClassHandle(new ReflectiveNamespace());
    }
    
    @NotNull
    @Contract(value = "_ -> new", pure = true)
    public static StaticClassHandle of(final Class<?> clazz) {
        return new StaticClassHandle(new ReflectiveNamespace(), clazz);
    }
    
    @NotNull
    @Contract(value = "-> new", pure = true)
    public static ReflectiveNamespace namespaced() {
        return new ReflectiveNamespace();
    }
    
    @SafeVarargs
    @NotNull
    @Contract(value = "_ -> new", pure = true)
    public static <T, H extends ReflectiveHandle<T>> AggregateReflectiveHandle<T, H> any(final H... handles) {
        return new AggregateReflectiveHandle<T, H>((java.util.Collection<java.util.concurrent.Callable<H>>)Arrays.stream((Object[])handles).map(x -> () -> x).collect(Collectors.toList()));
    }
    
    @SafeVarargs
    @NotNull
    @Contract(value = "_ -> new", pure = true)
    public static <T, H extends ReflectiveHandle<T>> AggregateReflectiveHandle<T, H> anyOf(final Callable<H>... handles) {
        return new AggregateReflectiveHandle<T, H>((java.util.Collection<java.util.concurrent.Callable<H>>)Arrays.asList((Object[])handles));
    }
    
    @ApiStatus.Experimental
    @NotNull
    @Contract(value = "_, _ -> new", pure = true)
    public static <H extends ReflectiveHandle<?>, O> AggregateReflectiveSupplier<H, O> supply(final H handle, final O object) {
        final AggregateReflectiveSupplier<H, O> supplier = new AggregateReflectiveSupplier<H, O>();
        supplier.or(handle, object);
        return supplier;
    }
    
    @ApiStatus.Experimental
    @NotNull
    @Contract(value = "_, _ -> new", pure = true)
    public static <H extends ReflectiveHandle<?>, O> AggregateReflectiveSupplier<H, O> supply(final H handle, final Supplier<O> object) {
        final AggregateReflectiveSupplier<H, O> supplier = new AggregateReflectiveSupplier<H, O>();
        supplier.or(handle, object);
        return supplier;
    }
    
    @ApiStatus.Experimental
    @NotNull
    @Contract(value = "_ -> param1", mutates = "param1")
    public static <T extends Throwable> T relativizeSuppressedExceptions(@NotNull final T ex) {
        Objects.requireNonNull((Object)ex, "Cannot relativize null exception");
        final StackTraceElement[] EMPTY_STACK_TRACE_ARRAY = new StackTraceElement[0];
        final StackTraceElement[] mainStackTrace = ex.getStackTrace();
        for (final Throwable suppressed : ex.getSuppressed()) {
            final StackTraceElement[] suppressedStackTrace = suppressed.getStackTrace();
            List<StackTraceElement> relativized = (List<StackTraceElement>)new ArrayList(10);
            for (int i = 0; i < suppressedStackTrace.length; ++i) {
                if (mainStackTrace.length <= i) {
                    relativized = null;
                    break;
                }
                final StackTraceElement mainTrace = mainStackTrace[i];
                final StackTraceElement suppTrace = suppressedStackTrace[i];
                if (mainTrace.equals((Object)suppTrace)) {
                    break;
                }
                relativized.add((Object)suppTrace);
            }
            if (relativized != null) {
                suppressed.setStackTrace((StackTraceElement[])relativized.toArray((Object[])EMPTY_STACK_TRACE_ARRAY));
            }
        }
        return ex;
    }
    
    @Contract(value = "_ -> fail", pure = true)
    private static <T extends Throwable> void throwException(final Throwable exception) throws T, Throwable {
        throw exception;
    }
    
    @Contract(value = "_ -> fail", pure = true)
    public static RuntimeException throwCheckedException(@NotNull final Throwable exception) {
        Objects.requireNonNull((Object)exception, "Cannot throw null exception");
        throwException(exception);
        return null;
    }
    
    @ApiStatus.Experimental
    @Contract(value = "_ -> new", mutates = "param1")
    public static <T> CompletableFuture<T> stacktrace(@NotNull final CompletableFuture<T> completableFuture) {
        final StackTraceElement[] currentStacktrace = new Throwable().getStackTrace();
        return (CompletableFuture<T>)completableFuture.whenComplete((value, ex) -> {
            if (ex == null) {
                completableFuture.complete(value);
                return;
            }
            try {
                StackTraceElement[] exStacktrace = ex.getStackTrace();
                if (exStacktrace.length >= 3) {
                    final List<StackTraceElement> clearStacktrace = (List<StackTraceElement>)new ArrayList((Collection)Arrays.asList((Object[])exStacktrace));
                    Collections.reverse((List)clearStacktrace);
                    final Iterator<StackTraceElement> iter = (Iterator<StackTraceElement>)clearStacktrace.iterator();
                    final List<String> watchClassNames = (List<String>)Arrays.asList((Object[])new String[] { "java.util.concurrent.CompletableFuture", "java.util.concurrent.ThreadPoolExecutor", "java.util.concurrent.ForkJoinTask", "java.util.concurrent.ForkJoinWorkerThread", "java.util.concurrent.ForkJoinPool" });
                    final List<String> watchMethodNames = (List<String>)Arrays.asList((Object[])new String[] { "postComplete", "encodeThrowable", "completeThrowable", "tryFire", "run", "runWorker", "scan", "exec", "doExec", "topLevelExec", "uniWhenComplete" });
                    while (iter.hasNext()) {
                        final StackTraceElement stackTraceElement = (StackTraceElement)iter.next();
                        final String className = stackTraceElement.getClassName();
                        final String methodName = stackTraceElement.getMethodName();
                        if (className.equals((Object)Thread.class.getName())) {
                            continue;
                        }
                        final Stream stream = watchClassNames.stream();
                        final String s = className;
                        Objects.requireNonNull((Object)s);
                        if (!stream.anyMatch(s::startsWith)) {
                            break;
                        }
                        final Stream stream2 = watchMethodNames.stream();
                        final String s2 = methodName;
                        Objects.requireNonNull((Object)s2);
                        if (!stream2.anyMatch(s2::equals)) {
                            break;
                        }
                        iter.remove();
                    }
                    Collections.reverse((List)clearStacktrace);
                    exStacktrace = (StackTraceElement[])clearStacktrace.toArray((Object[])new StackTraceElement[0]);
                }
                final StackTraceElement[] finalCurrentStackTrace = (StackTraceElement[])Arrays.stream((Object[])currentStacktrace).skip(1L).toArray(StackTraceElement[]::new);
                ex.setStackTrace((StackTraceElement[])concatenate(exStacktrace, finalCurrentStackTrace));
            }
            catch (final Throwable ex2) {
                ex.addSuppressed(ex2);
            }
            finally {
                completableFuture.completeExceptionally(ex);
            }
        });
    }
    
    @ApiStatus.Internal
    @Contract(value = "_, _ -> new", pure = true)
    public static <T> T[] concatenate(final T[] a, final T[] b) {
        final int aLen = a.length;
        final int bLen = b.length;
        final T[] c = (T[])Array.newInstance((Class)a.getClass().getComponentType(), aLen + bLen);
        System.arraycopy((Object)a, 0, (Object)c, 0, aLen);
        System.arraycopy((Object)b, 0, (Object)c, aLen, bLen);
        return c;
    }
    
    @ApiStatus.Experimental
    @NotNull
    public static <T extends ReflectiveProxyObject> T proxify(@NotNull final Class<T> interfaceClass) {
        ReflectiveProxy.checkInterfaceClass(interfaceClass);
        final ReflectiveProxyObject loaded = (ReflectiveProxyObject)XReflection.PROXIFIED_CLASSES.get((Object)interfaceClass);
        if (loaded != null) {
            return (T)loaded;
        }
        T proxified;
        if (XReflection.SUPPORTS_ASM) {
            proxified = XReflectASM.proxify(interfaceClass).create();
        }
        else {
            proxified = ReflectiveProxy.proxify(interfaceClass).proxy();
        }
        XReflection.PROXIFIED_CLASSES.put((Object)interfaceClass, (Object)proxified);
        return proxified;
    }
    
    static {
        NMS_VERSION = findNMSVersionString();
        boolean supportsASM;
        try {
            Class.forName("org.objectweb.asm.ClassWriter");
            Class.forName("org.objectweb.asm.MethodVisitor");
            Class.forName("org.objectweb.asm.FieldVisitor");
            Class.forName("org.objectweb.asm.Constants");
            Class.forName("org.objectweb.asm.Opcodes");
            supportsASM = true;
        }
        catch (final ClassNotFoundException e) {
            supportsASM = false;
        }
        SUPPORTS_ASM = supportsASM;
        String verProp = isMinecraftDisabled();
        if (verProp != null) {
            System.out.println("[XSeries/XReflection] Testing with hardcoded server version: " + (verProp.isEmpty() ? "Disabled Minecraft Capabilities" : verProp));
            if (verProp.isEmpty() || verProp.equals((Object)"true")) {
                verProp = "1.21.4-R0.1-SNAPSHOT";
            }
        }
        final Matcher bukkitVer = Pattern.compile("^(?<major>\\d+)\\.(?<minor>\\d+)(?:\\.(?<patch>\\d+))?").matcher((CharSequence)((verProp != null) ? verProp : Bukkit.getBukkitVersion()));
        if (bukkitVer.find()) {
            Label_0286: {
                try {
                    final String patch = bukkitVer.group("patch");
                    MAJOR_NUMBER = Integer.parseInt(bukkitVer.group("major"));
                    MINOR_NUMBER = Integer.parseInt(bukkitVer.group("minor"));
                    PATCH_NUMBER = Integer.parseInt((patch == null || patch.isEmpty()) ? "0" : patch);
                    break Label_0286;
                }
                catch (final Throwable ex) {
                    throw new IllegalStateException("Failed to parse minor number: " + (Object)bukkitVer + ' ' + getVersionInformation(), ex);
                }
                throw new IllegalStateException("Cannot parse server version: \"" + Bukkit.getBukkitVersion() + '\"');
            }
            CRAFTBUKKIT_PACKAGE = ((isMinecraftDisabled() != null) ? null : Bukkit.getServer().getClass().getPackage().getName());
            NMS_PACKAGE = v(17, "net.minecraft").orElse("net.minecraft.server." + XReflection.NMS_VERSION);
            if (isMinecraftDisabled() != null) {
                SUPPORTED_MAPPINGS = Collections.unmodifiableSet((Set)EnumSet.noneOf((Class)MinecraftMapping.class));
            }
            else {
                SUPPORTED_MAPPINGS = Collections.unmodifiableSet((Set)supply(ofMinecraft().inPackage(MinecraftPackage.NMS, "server.level").map(MinecraftMapping.MOJANG, "ServerPlayer"), (java.util.function.Supplier<Set>)(() -> EnumSet.of((Enum)MinecraftMapping.MOJANG))).or(ofMinecraft().inPackage(MinecraftPackage.NMS, "server.level").map(MinecraftMapping.MOJANG, "EntityPlayer"), (java.util.function.Supplier<Set>)(() -> EnumSet.of((Enum)MinecraftMapping.SPIGOT, (Enum)MinecraftMapping.OBFUSCATED))).get());
            }
            PROXIFIED_CLASSES = (Map)new IdentityHashMap();
            return;
        }
        throw new IllegalStateException("Cannot parse server version: \"" + Bukkit.getBukkitVersion() + '\"');
    }
}
