package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.proxy.processors;

import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.aggregate.VersionHandle;
import java.util.Arrays;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.StaticReflectiveHandle;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm.objects.ReflectedObject;
import java.lang.invoke.MethodType;
import java.lang.invoke.MethodHandles;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.proxy.annotations.ReflectName;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.proxy.annotations.MappedMinecraftName;
import org.jetbrains.annotations.NotNull;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm.classes.DynamicClassHandle;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm.classes.PackageHandle;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.proxy.annotations.ReflectMinecraftPackage;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.proxy.annotations.Proxify;
import java.lang.invoke.MethodHandle;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm.MethodMemberHandle;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm.FieldMemberHandle;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm.MemberHandle;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm.classes.ClassHandle;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.ReflectiveHandle;
import java.lang.reflect.AnnotatedElement;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm.NameableReflectiveHandle;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.proxy.annotations.Protected;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.XAccessFlag;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.proxy.annotations.Private;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.proxy.annotations.Field;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.proxy.annotations.Constructor;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.proxy.annotations.Final;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.proxy.annotations.Static;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.proxy.annotations.Ignore;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.XReflection;
import java.util.Iterator;
import java.util.function.Function;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.proxy.ReflectiveProxy;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.proxy.OverloadedMethod;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.proxy.ClassOverloadedMethods;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.proxy.ReflectiveProxyObject;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public final class ReflectiveAnnotationProcessor
{
    private final Class<? extends ReflectiveProxyObject> interfaceClass;
    private ClassOverloadedMethods<ProxyMethodInfo> mapped;
    private OverloadedMethod.Builder<ProxyMethodInfo> mappedHandles;
    private Class<?> targetClass;
    
    public ReflectiveAnnotationProcessor(final Class<? extends ReflectiveProxyObject> interfaceClass) {
        ReflectiveProxy.checkInterfaceClass(interfaceClass);
        this.interfaceClass = interfaceClass;
    }
    
    private void error(final String msg) {
        this.error(msg, null);
    }
    
    private void error(final String msg, final Throwable ex) {
        throw new IllegalStateException(msg + " (Proxified Interface: " + (Object)this.interfaceClass + ')', ex);
    }
    
    protected static boolean isAnnotationInherited(final Class<?> clazz, final Method method, final Class<? extends Annotation> annotation) {
        try {
            final Method superMethod = clazz.getDeclaredMethod(method.getName(), (Class<?>[])method.getParameterTypes());
            if (superMethod.isAnnotationPresent((Class)annotation)) {
                return true;
            }
        }
        catch (final NoSuchMethodException ex) {}
        for (final Class<?> superInterface : clazz.getInterfaces()) {
            if (isAnnotationInherited(superInterface, method, annotation)) {
                return true;
            }
        }
        return false;
    }
    
    public void loadDependencies(final Function<Class<?>, Boolean> isLoaded) {
        for (final OverloadedMethod<ProxyMethodInfo> overloads : this.mapped.mappings().values()) {
            for (final ProxyMethodInfo overload : overloads.getOverloads()) {
                this.loadDependency(overload.rType, isLoaded);
                for (final MappedType pType : overload.pTypes) {
                    this.loadDependency(pType, isLoaded);
                }
            }
        }
    }
    
    private void loadDependency(final MappedType type, final Function<Class<?>, Boolean> isLoaded) {
        if (ReflectiveProxyObject.class.isAssignableFrom(type.synthetic) && type.synthetic != this.interfaceClass && !(boolean)isLoaded.apply((Object)type.synthetic)) {
            XReflection.proxify(type.synthetic);
        }
    }
    
    public void process(final Function<ProxyMethodInfo, String> descriptorProcessor) {
        final ClassHandle classHandle = this.processTargetClass();
        final Method[] interfaceMethods = this.interfaceClass.getMethods();
        this.mappedHandles = new OverloadedMethod.Builder<ProxyMethodInfo>(descriptorProcessor);
        for (final Method method : interfaceMethods) {
            if (!isAnnotationInherited(this.interfaceClass, method, (Class<? extends Annotation>)Ignore.class)) {
                final boolean asStatic = method.isAnnotationPresent((Class)Static.class);
                final boolean asFinal = method.isAnnotationPresent((Class)Final.class);
                MappedType[] pTypes = new MappedType[0];
                MappedType rType;
                MemberHandle handle;
                if (method.isAnnotationPresent((Class)Constructor.class)) {
                    final Class<?> returnType = method.getReturnType();
                    if (returnType != this.targetClass && returnType != this.interfaceClass && returnType != Object.class) {
                        this.error("Method marked with @Constructor must return Object.class, " + (Object)this.targetClass + " or " + (Object)this.interfaceClass);
                    }
                    rType = this.unwrap(returnType);
                    pTypes = this.unwrap(method.getParameterTypes());
                    handle = classHandle.constructor(MappedType.getRealTypes(pTypes));
                    if (asStatic) {
                        this.error("Constructor cannot be static: " + (Object)method);
                    }
                    if (asFinal) {
                        this.error("Constructor cannot be final: " + (Object)method);
                    }
                }
                else if (method.isAnnotationPresent((Class)Field.class)) {
                    final FieldMemberHandle field = classHandle.field();
                    if (method.getReturnType() == Void.TYPE) {
                        field.setter();
                        if (method.getParameterCount() != 1) {
                            this.error("Field setter method must have only one parameter: " + (Object)method);
                        }
                        final MappedType fieldType = this.unwrap(method.getParameterTypes()[0]);
                        rType = new MappedType(Void.TYPE, Void.TYPE);
                        pTypes = new MappedType[] { fieldType };
                        field.returns(fieldType.real);
                    }
                    else {
                        field.getter();
                        if (method.getParameterCount() != 0) {
                            this.error("Field getter method must not have any parameters: " + (Object)method);
                        }
                        rType = this.unwrap(method.getReturnType());
                        field.returns(rType.real);
                    }
                    if (asStatic) {
                        field.asStatic();
                    }
                    if (asFinal) {
                        field.asFinal();
                    }
                    handle = field;
                }
                else {
                    rType = this.unwrap(method.getReturnType());
                    pTypes = this.unwrap(method.getParameterTypes());
                    MethodMemberHandle methHandle = classHandle.method().returns(rType.real).parameters(MappedType.getRealTypes(pTypes));
                    if (asStatic) {
                        methHandle = methHandle.asStatic();
                    }
                    if (asFinal) {
                        this.error("Declaring method as final has no effect: " + (Object)method);
                    }
                    handle = methHandle;
                }
                boolean visibilitySet = false;
                if (method.isAnnotationPresent((Class)Private.class)) {
                    visibilitySet = true;
                    handle.getAccessFlags().add((Object)XAccessFlag.PRIVATE);
                }
                if (method.isAnnotationPresent((Class)Protected.class)) {
                    if (visibilitySet) {
                        this.error("Cannot have two visibility modifier private and protected for " + (Object)method);
                    }
                    handle.getAccessFlags().add((Object)XAccessFlag.PRIVATE);
                }
                if (handle instanceof NameableReflectiveHandle) {
                    ((NameableReflectiveHandle)handle).named(method.getName());
                    this.reflectNames((NameableReflectiveHandle)handle, (AnnotatedElement)method);
                }
                final ReflectiveHandle<MethodHandle> cached = handle.cached();
                try {
                    cached.reflect();
                }
                catch (final ReflectiveOperationException e) {
                    this.error("Failed to map " + (Object)method, (Throwable)e);
                }
                final ProxyMethodInfo methodInfo = new ProxyMethodInfo(cached, method, rType, pTypes);
                this.mappedHandles.add(methodInfo, method.getName());
            }
        }
        this.mapped = this.mappedHandles.build();
    }
    
    @NotNull
    public ClassHandle processTargetClass() {
        final Proxify reflectClass = this.interfaceClass.getAnnotation(Proxify.class);
        final ReflectMinecraftPackage mcClass = this.interfaceClass.getAnnotation(ReflectMinecraftPackage.class);
        if (reflectClass == null && mcClass == null) {
            this.error("Proxy interface is not annotated with @Class or @ReflectMinecraftPackage");
        }
        if (reflectClass != null && mcClass != null) {
            this.error("Proxy interface cannot contain both @Class or @ReflectMinecraftPackage");
        }
        ClassHandle classHandle;
        boolean ignoreCurrentName;
        if (reflectClass != null) {
            if (reflectClass.target() != Void.TYPE) {
                classHandle = XReflection.of(reflectClass.target());
                ignoreCurrentName = true;
            }
            else {
                final DynamicClassHandle dynClassHandle = (DynamicClassHandle)(classHandle = XReflection.classHandle());
                dynClassHandle.inPackage(reflectClass.packageName());
                ignoreCurrentName = reflectClass.ignoreCurrentName();
            }
        }
        else {
            final DynamicClassHandle dynClassHandle = (DynamicClassHandle)(classHandle = XReflection.ofMinecraft());
            dynClassHandle.inPackage(mcClass.type(), mcClass.packageName());
            ignoreCurrentName = mcClass.ignoreCurrentName();
        }
        if (classHandle instanceof DynamicClassHandle) {
            final DynamicClassHandle dynamicClassHandle = (DynamicClassHandle)classHandle;
            if (!ignoreCurrentName) {
                dynamicClassHandle.named(this.interfaceClass.getSimpleName());
            }
            this.reflectNames(dynamicClassHandle, (AnnotatedElement)this.interfaceClass);
        }
        this.targetClass = classHandle.unreflect();
        MappedType.LOOK_AHEAD.put((Object)this.interfaceClass, (Object)this.targetClass);
        return classHandle;
    }
    
    public Class<?> getTargetClass() {
        return this.targetClass;
    }
    
    public ClassOverloadedMethods<ProxyMethodInfo> getMapped() {
        return this.mapped;
    }
    
    private void reflectNames(final NameableReflectiveHandle handle, final AnnotatedElement annotated) {
        final MappedMinecraftName[] mapped = (MappedMinecraftName[])annotated.getDeclaredAnnotationsByType((Class)MappedMinecraftName.class);
        final ReflectName[] rawNames = (ReflectName[])annotated.getDeclaredAnnotationsByType((Class)ReflectName.class);
        for (final MappedMinecraftName mcMapped : mapped) {
            this.reflectNames0(handle, mcMapped.names());
        }
        this.reflectNames0(handle, rawNames);
    }
    
    private void reflectDefaults() {
        try {
            final MethodHandle privateLookupIn = XReflection.of(MethodHandles.class).method("public static Lookup privateLookupIn(Class<?> targetClass, Lookup caller) throws IllegalAccessException").reflectOrNull();
            if (privateLookupIn == null) {
                final java.lang.reflect.Constructor<MethodHandles.Lookup> constructor = MethodHandles.Lookup.class.getDeclaredConstructor(Class.class);
                constructor.setAccessible(true);
                final MethodHandles.Lookup lookup = ((MethodHandles.Lookup)constructor.newInstance(new Object[] { this.interfaceClass })).in((Class)this.interfaceClass);
                for (final Method method : this.interfaceClass.getMethods()) {
                    if (method.isDefault()) {
                        this.handleDefaultMethod(method, lookup.unreflectSpecial(method, (Class)this.interfaceClass));
                    }
                }
            }
            final MethodHandles.Lookup lookup2 = privateLookupIn.invokeExact((Class)this.interfaceClass, MethodHandles.lookup());
            for (final Method method2 : this.interfaceClass.getMethods()) {
                if (method2.isDefault()) {
                    final MethodHandle meth = lookup2.findSpecial((Class)this.interfaceClass, method2.getName(), MethodType.methodType(method2.getReturnType(), method2.getParameterTypes()), (Class)this.interfaceClass);
                    this.handleDefaultMethod(method2, meth);
                }
            }
        }
        catch (final Throwable ex) {
            ex.printStackTrace();
        }
    }
    
    private void handleDefaultMethod(final Method method, final MethodHandle defaultHandle) {
        this.mappedHandles.add(new ProxyMethodInfo(new StaticReflectiveHandle<Object>(defaultHandle, ReflectedObject.of(method)), method, this.unwrap(method.getReturnType()), this.unwrap(method.getParameterTypes())), method.getName());
    }
    
    private void reflectNames0(final NameableReflectiveHandle handle, final ReflectName[] reflectedNames) {
        if (reflectedNames.length == 0) {
            return;
        }
        VersionHandle<String[]> versionControl = null;
        String[] chosen = null;
        int index = 0;
        for (final ReflectName name : reflectedNames) {
            ++index;
            if (chosen != null) {
                this.error("Cannot contain more tha one @ReflectName if no version is specified");
            }
            if (!name.version().isEmpty()) {
                if (index == reflectedNames.length) {
                    this.error("Last @ReflectName should not contain version");
                }
                final int[] semVer = Arrays.stream((Object[])name.version().split("\\.")).mapToInt(Integer::parseInt).toArray();
                if (versionControl == null) {
                    if (semVer.length == 1) {
                        versionControl = XReflection.v(semVer[0], name.value());
                    }
                    if (semVer.length == 2) {
                        versionControl = XReflection.v(semVer[1], name.value());
                    }
                    if (semVer.length == 3) {
                        versionControl = XReflection.v(semVer[1], semVer[2], name.value());
                    }
                }
                else {
                    if (semVer.length == 1) {
                        versionControl.v(semVer[0], name.value());
                    }
                    if (semVer.length == 2) {
                        versionControl.v(semVer[1], name.value());
                    }
                    if (semVer.length == 3) {
                        versionControl.v(semVer[1], semVer[2], name.value());
                    }
                }
            }
            else if (versionControl != null) {
                if (index != reflectedNames.length) {
                    this.error("One of @ReflectName doesn't contain a version.");
                }
                else {
                    chosen = versionControl.orElse(name.value());
                }
            }
            else {
                chosen = name.value();
            }
        }
        handle.named(chosen);
    }
    
    private MappedType[] unwrap(final Class<?>[] classes) {
        final MappedType[] unwrapped = new MappedType[classes.length];
        for (int i = 0; i < classes.length; ++i) {
            final Class<?> clazz = classes[i];
            unwrapped[i] = this.unwrap(clazz);
        }
        return unwrapped;
    }
    
    private MappedType unwrap(final Class<?> clazz) {
        if (clazz == this.interfaceClass || clazz == ReflectiveProxyObject.class) {
            return new MappedType(this.interfaceClass, this.targetClass);
        }
        if (ReflectiveProxyObject.class.isAssignableFrom(clazz)) {
            final Class<?> real = MappedType.getMappedTypeOrCreate((Class<? extends ReflectiveProxyObject>)clazz);
            return new MappedType(clazz, real);
        }
        return new MappedType(clazz, clazz);
    }
}
