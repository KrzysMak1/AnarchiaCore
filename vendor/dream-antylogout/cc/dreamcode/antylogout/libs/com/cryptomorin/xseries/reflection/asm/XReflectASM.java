package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.asm;

import java.util.List;
import java.util.ArrayList;
import com.google.common.collect.Streams;
import java.util.stream.Stream;
import java.util.IdentityHashMap;
import org.intellij.lang.annotations.Pattern;
import org.objectweb.asm.commons.Method;
import java.lang.invoke.MethodHandle;
import org.objectweb.asm.commons.GeneratorAdapter;
import org.objectweb.asm.Label;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.proxy.processors.MappedType;
import java.util.function.Supplier;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.TypePath;
import org.objectweb.asm.AnnotationVisitor;
import java.util.Collection;
import java.util.Iterator;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm.FieldMemberHandle;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.XAccessFlag;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm.objects.ReflectedObject;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.proxy.OverloadedMethod;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.io.OutputStream;
import java.io.PrintWriter;
import org.jetbrains.annotations.NotNull;
import java.util.Optional;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Constructor;
import java.util.Objects;
import java.util.function.Function;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.proxy.processors.ReflectiveAnnotationProcessor;
import java.util.Arrays;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.proxy.processors.ProxyMethodInfo;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.proxy.ClassOverloadedMethods;
import org.objectweb.asm.Type;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import java.util.Map;
import org.jetbrains.annotations.ApiStatus;
import org.objectweb.asm.ClassVisitor;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.proxy.ReflectiveProxyObject;

@ApiStatus.Internal
public final class XReflectASM<T extends ReflectiveProxyObject> extends ClassVisitor
{
    private static final int JAVA_VERSION;
    private static final int ASM_VERSION;
    private static final String CONSTRUCTOR_NAME = "<init>";
    private static final String STATIC_BLOCK = "<clinit>";
    private static final String INSTANCE_FIELD = "instance";
    private static final String METHOD_HANDLE_PREFIX = "H_";
    private static final String XSERIES_ANNOTATIONS;
    private static final String GENERATED_CLASS_PACKAGE_PREFIX = "generated";
    private static final String GENERATED_CLASS_SUFFIX;
    private static final String MAGIC_ACCESSOR_IMPL;
    private static final String SUPER_CLASS;
    private static final ASMClassLoader CLASS_LOADER;
    private static final Map<Class<?>, XReflectASM<?>> PROCESSED;
    private final ClassWriter classWriter;
    private final ClassReader classReader;
    private final Class<T> templateClass;
    private final Class<?> targetClass;
    private final Type templateClassType;
    private final Type targetClassType;
    private final Type generatedClassType;
    private final String generatedClassName;
    private final String generatedClassPath;
    private Class<?> loaded;
    private byte[] bytecode;
    private final ClassOverloadedMethods<ASMProxyInfo> mapped;
    
    private static String getGeneratedClassPath(final Class<?> clazz) {
        return clazz.getPackage().getName() + '.' + "generated" + '.' + clazz.getSimpleName() + XReflectASM.GENERATED_CLASS_SUFFIX;
    }
    
    private static String descriptorProcessor(final ProxyMethodInfo info) {
        final Type rType = Type.getType((Class)info.rType.synthetic);
        final Type[] pTypes = (Type[])Arrays.stream((Object[])info.pTypes).map(x -> Type.getType((Class)x.synthetic)).toArray(Type[]::new);
        return Type.getMethodDescriptor(rType, pTypes);
    }
    
    public static <T extends ReflectiveProxyObject> XReflectASM<T> proxify(final Class<T> interfaceClass) {
        final XReflectASM<?> cache = (XReflectASM<?>)XReflectASM.PROCESSED.get((Object)interfaceClass);
        if (cache != null) {
            return (XReflectASM<T>)cache;
        }
        final ReflectiveAnnotationProcessor processor = new ReflectiveAnnotationProcessor(interfaceClass);
        processor.process((Function<ProxyMethodInfo, String>)XReflectASM::descriptorProcessor);
        final XReflectASM<T> asm = new XReflectASM<T>(interfaceClass, processor.getTargetClass(), processor.getMapped());
        XReflectASM.PROCESSED.put((Object)interfaceClass, (Object)asm);
        final ReflectiveAnnotationProcessor reflectiveAnnotationProcessor = processor;
        final Map<Class<?>, XReflectASM<?>> processed = XReflectASM.PROCESSED;
        Objects.requireNonNull((Object)processed);
        reflectiveAnnotationProcessor.loadDependencies((Function<Class<?>, Boolean>)processed::containsKey);
        asm.generate();
        return asm;
    }
    
    @NotNull
    public T create() {
        final Class<?> proxified = this.loadClass();
        try {
            final Optional<Constructor<?>> ctor = (Optional<Constructor<?>>)Arrays.stream((Object[])proxified.getDeclaredConstructors()).filter(x -> XAccessFlag.PUBLIC.isSet(x.getModifiers()) && x.getParameterCount() == 1).findFirst();
            if (!ctor.isPresent()) {
                throw new IllegalStateException("Cannot find appropriate constructor for " + Arrays.toString((Object[])proxified.getDeclaredConstructors()));
            }
            return (T)((Constructor)ctor.get()).newInstance(new Object[] { null });
        }
        catch (final InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException("Couldn't initialize proxified ASM class: " + (Object)this.templateClass + " -> " + (Object)proxified, (Throwable)e);
        }
    }
    
    public void verify(final boolean silent) {
        this.generate();
        final PrintWriter pw = new PrintWriter((OutputStream)(silent ? System.err : System.out));
        ASMAnalyzer.verify(new ClassReader(this.bytecode), XReflectASM.class.getClassLoader(), !silent, pw);
    }
    
    public void writeToFile(final Path folder) {
        this.generate();
        try {
            Files.write(folder.resolve(this.generatedClassName + ".class"), this.bytecode, new OpenOption[] { (OpenOption)StandardOpenOption.CREATE, (OpenOption)StandardOpenOption.TRUNCATE_EXISTING, (OpenOption)StandardOpenOption.WRITE });
        }
        catch (final IOException e) {
            throw new IllegalStateException("Cannot write generated file", (Throwable)e);
        }
    }
    
    public XReflectASM(final Class<T> templateClass, final Class<?> targetClass, final ClassOverloadedMethods<ProxyMethodInfo> mapped) {
        super(XReflectASM.ASM_VERSION);
        this.mapped = mapTypes(mapped);
        try {
            this.classReader = new ClassReader(templateClass.getName());
        }
        catch (final IOException e) {
            throw new IllegalStateException("Unable to read class: " + (Object)templateClass, (Throwable)e);
        }
        final ClassWriter classWriter = new ClassWriter(this.classReader, 3);
        this.classWriter = classWriter;
        this.cv = (ClassVisitor)classWriter;
        this.templateClass = templateClass;
        this.templateClassType = Type.getType((Class)templateClass);
        this.targetClass = targetClass;
        this.targetClassType = Type.getType((Class)targetClass);
        this.generatedClassName = templateClass.getSimpleName() + XReflectASM.GENERATED_CLASS_SUFFIX;
        this.generatedClassPath = getGeneratedClassPath(templateClass);
        this.generatedClassType = Type.getType('L' + this.generatedClassPath.replace('.', '/') + ';');
    }
    
    public void generate() {
        if (this.bytecode != null) {
            return;
        }
        this.classReader.accept((ClassVisitor)this, 0);
        this.bytecode = this.classWriter.toByteArray();
    }
    
    public byte[] getBytecode() {
        return this.bytecode;
    }
    
    private static boolean shouldRemoveAnnotation(final String descriptor) {
        return descriptor.startsWith(XReflectASM.XSERIES_ANNOTATIONS);
    }
    
    private static ClassOverloadedMethods<ASMProxyInfo> mapTypes(final ClassOverloadedMethods<ProxyMethodInfo> mapped) {
        final OverloadedMethod.Builder<ASMProxyInfo> asmMapped = new OverloadedMethod.Builder<ASMProxyInfo>((java.util.function.Function<ASMProxyInfo, String>)(x -> descriptorProcessor(x.info)));
        for (final Map.Entry<String, OverloadedMethod<ProxyMethodInfo>> overloads : mapped.mappings().entrySet()) {
            final Collection<ProxyMethodInfo> overloaded = ((OverloadedMethod)overloads.getValue()).getOverloads();
            int overloadIndex = 0;
            for (final ProxyMethodInfo overload : overloaded) {
                final ReflectedObject jvm = overload.handle.jvm().unreflect();
                if (!jvm.accessFlags().contains((Object)XAccessFlag.PUBLIC)) {
                    String name = null;
                    switch (jvm.type()) {
                        case CONSTRUCTOR: {
                            name = "$init$" + ((overloaded.size() == 1) ? "" : ("_" + overloadIndex++));
                            break;
                        }
                        case FIELD: {
                            final FieldMemberHandle field = (FieldMemberHandle)overload.handle.unwrap();
                            name = jvm.name() + '_' + (field.isGetter() ? "getter" : "setter");
                            break;
                        }
                        case METHOD: {
                            name = jvm.name() + ((overloaded.size() == 1) ? "" : ("_" + overloadIndex++));
                            break;
                        }
                        default: {
                            throw new IllegalStateException("Unexpected JVM type: " + (Object)jvm);
                        }
                    }
                    asmMapped.add(new ASMProxyInfo(overload, name), (String)overloads.getKey());
                }
                else {
                    asmMapped.add(new ASMProxyInfo(overload, (String)null), (String)overloads.getKey());
                }
            }
        }
        return asmMapped.build();
    }
    
    public AnnotationVisitor visitAnnotation(final String descriptor, final boolean visible) {
        if (shouldRemoveAnnotation(descriptor)) {
            return null;
        }
        return super.visitAnnotation(descriptor, visible);
    }
    
    public AnnotationVisitor visitTypeAnnotation(final int typeRef, final TypePath typePath, final String descriptor, final boolean visible) {
        if (shouldRemoveAnnotation(descriptor)) {
            return null;
        }
        return super.visitTypeAnnotation(typeRef, typePath, descriptor, visible);
    }
    
    public void visit(final int version, final int access, final String name, final String signature, final String superName, final String[] interfaces) {
        this.classWriter.visit(XReflectASM.JAVA_VERSION, 49, this.generatedClassType.getInternalName(), (String)null, XReflectASM.SUPER_CLASS, new String[] { this.templateClassType.getInternalName() });
    }
    
    public void visitSource(final String source, final String debug) {
        this.classWriter.visitSource(this.generatedClassName + ".java", (String)null);
        boolean needsStaticInit = false;
        for (final OverloadedMethod<ASMProxyInfo> method : this.mapped.mappings().values()) {
            for (final ASMProxyInfo overload : method.getOverloads()) {
                if (overload.isInaccessible()) {
                    needsStaticInit = true;
                    this.writeMethodHandleField(overload.methodHandleName);
                }
            }
        }
        this.writePrivateFinalField(false, "instance", this.targetClass);
        if (needsStaticInit) {
            this.initStaticFields();
        }
        this.writeConstructor();
    }
    
    public void visitEnd() {
        this.generateGetTargetClass();
        this.generateIsInstance();
        this.generateNewArraySingleDim();
        this.generateNewArrayMultiDim();
        this.generateInstance();
        this.generateBindTo();
        this.generateEquals();
        this.generateHashCode();
        this.generateToString();
        super.visitEnd();
    }
    
    public FieldVisitor visitField(final int access, final String name, final String descriptor, final String signature, final Object value) {
        throw new UnsupportedOperationException("Raw fields are not supported");
    }
    
    public MethodVisitor visitMethod(final int access, final String name, final String descriptor, final String signature, final String[] exceptions) {
        if ("<init>".equals((Object)name) || "<clinit>".equals((Object)name)) {
            return null;
        }
        final ASMProxyInfo handle = this.mapped.get(name, (Supplier<String>)(() -> descriptor), true);
        if (handle == null) {
            return null;
        }
        return new MethodRewriter(handle, access, name, descriptor, signature, exceptions);
    }
    
    private static int magicMaxs(final String descriptor, final boolean staticMethod) {
        return Type.getArgumentsAndReturnSizes(descriptor) >> 2 + (staticMethod ? -1 : 0);
    }
    
    public static Type getType(final String className) {
        return Type.getType('L' + className.replace('.', '/') + ';');
    }
    
    private static Type[] convert(final MappedType[] pTypes) {
        return (Type[])Arrays.stream((Object[])pTypes).map(x -> Type.getType((Class)x.real)).toArray(Type[]::new);
    }
    
    private void getInstance(final MethodVisitor mv) {
        mv.visitFieldInsn(180, this.generatedClassType.getInternalName(), "instance", this.targetClassType.getDescriptor());
    }
    
    private void writeConstructor() {
        final GeneratorAdapter mv = this.createMethod(1, "<init>", Type.getMethodDescriptor(Type.getType(Void.TYPE), new Type[] { this.targetClassType }));
        final Label label0 = new Label();
        mv.visitLabel(label0);
        mv.loadThis();
        mv.visitMethodInsn(183, XReflectASM.SUPER_CLASS, "<init>", "()V", false);
        mv.loadThis();
        mv.loadArg(0);
        mv.putField(this.generatedClassType, "instance", this.targetClassType);
        mv.returnValue();
        final Label label2 = new Label();
        mv.visitLabel(label2);
        this.visitThis((MethodVisitor)mv, label0, label2);
        mv.visitLocalVariable("instance", this.targetClassType.getDescriptor(), (String)null, label0, label2, 1);
        mv.visitMaxs(2, 2);
        mv.visitEnd();
    }
    
    private void writeMethodHandleField(final String name) {
        this.writePrivateFinalField(true, "H_" + name, MethodHandle.class);
    }
    
    private void writePrivateFinalField(final boolean asStatic, final String name, final Class<?> type) {
        int access = 18;
        if (asStatic) {
            access |= 0x8;
        }
        final FieldVisitor fv = this.classWriter.visitField(access, name, Type.getDescriptor((Class)type), (String)null, (Object)null);
        fv.visitEnd();
    }
    
    private void initStaticFields() {
        final GeneratorAdapter mv = this.createMethod(8, "<clinit>", "()V");
        mv.visitCode();
        final Label start = new Label();
        final Label end = new Label();
        final Label catchException = new Label();
        mv.visitTryCatchBlock(start, end, catchException, "java/lang/Throwable");
        mv.visitLabel(start);
        final int targetClass = mv.newLocal(Type.getType((Class)Class.class));
        mv.visitLdcInsn((Object)this.targetClass.getName());
        mv.invokeStatic(Type.getType((Class)Class.class), Method.getMethod("Class forName(String)"));
        mv.storeLocal(targetClass);
        final Type ASMPrivateLookup = Type.getType((Class)ASMPrivateLookup.class);
        final int lookup = mv.newLocal(ASMPrivateLookup);
        mv.newInstance(ASMPrivateLookup);
        mv.dup();
        mv.loadLocal(targetClass);
        mv.invokeConstructor(ASMPrivateLookup, Method.getMethod("void <init>(Class)"));
        mv.storeLocal(lookup);
        for (final OverloadedMethod<ASMProxyInfo> method : this.mapped.mappings().values()) {
            for (final ASMProxyInfo overload : method.getOverloads()) {
                if (!overload.isInaccessible()) {
                    continue;
                }
                final ReflectedObject jvm = overload.info.handle.jvm().unreflect();
                final Label unitLabel = new Label();
                mv.visitLabel(unitLabel);
                switch (jvm.type()) {
                    case CONSTRUCTOR: {
                        mv.loadLocal(lookup);
                        final ArrayInsnGenerator pTypes = new ArrayInsnGenerator(mv, Class.class, overload.info.pTypes.length);
                        for (final MappedType pType : overload.info.pTypes) {
                            pTypes.add(() -> mv.push(Type.getType((Class)pType.real)));
                        }
                        mv.invokeVirtual(ASMPrivateLookup, Method.getMethod("java.lang.invoke.MethodHandle findConstructor(Class[])"));
                        break;
                    }
                    case FIELD: {
                        final FieldMemberHandle field = (FieldMemberHandle)overload.info.handle.unwrap();
                        mv.loadLocal(lookup);
                        mv.push(jvm.name());
                        mv.push(Type.getType((Class)overload.info.rType.real));
                        mv.push(field.isGetter());
                        mv.invokeVirtual(ASMPrivateLookup, Method.getMethod("java.lang.invoke.MethodHandle findField(String, Class, boolean)"));
                        break;
                    }
                    case METHOD: {
                        mv.loadLocal(lookup);
                        mv.push(jvm.name());
                        mv.push(Type.getType((Class)overload.info.rType.real));
                        final ArrayInsnGenerator pTypes = new ArrayInsnGenerator(mv, Class.class, overload.info.pTypes.length);
                        for (final MappedType pType : overload.info.pTypes) {
                            pTypes.add(() -> mv.push(Type.getType((Class)pType.real)));
                        }
                        mv.invokeVirtual(ASMPrivateLookup, Method.getMethod("java.lang.invoke.MethodHandle findMethod(String, Class, Class[])"));
                        break;
                    }
                    default: {
                        throw new IllegalStateException("Unknown ReflectedObject type: " + (Object)jvm);
                    }
                }
                mv.visitFieldInsn(179, this.generatedClassType.getInternalName(), "H_" + overload.methodHandleName, Type.getDescriptor((Class)MethodHandle.class));
            }
        }
        mv.visitLabel(end);
        final Label noExceptionThrown = new Label();
        mv.visitJumpInsn(167, noExceptionThrown);
        mv.visitLabel(catchException);
        final int ex = mv.newLocal(Type.getType((Class)Throwable.class));
        mv.storeLocal(ex);
        final Label label6 = new Label();
        mv.visitLabel(label6);
        mv.visitTypeInsn(187, "java/lang/RuntimeException");
        mv.visitInsn(89);
        final String StringBuilder = "java/lang/StringBuilder";
        mv.visitTypeInsn(187, StringBuilder);
        mv.visitInsn(89);
        mv.visitMethodInsn(183, StringBuilder, "<init>", "()V", false);
        mv.visitLdcInsn((Object)"Failed to get inaccessible members for ");
        mv.visitMethodInsn(182, StringBuilder, "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
        mv.visitLdcInsn((Object)this.generatedClassType);
        mv.visitMethodInsn(182, "java/lang/Class", "getName", "()Ljava/lang/String;", false);
        mv.visitMethodInsn(182, StringBuilder, "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
        mv.visitMethodInsn(182, StringBuilder, "toString", "()Ljava/lang/String;", false);
        mv.loadLocal(ex);
        mv.visitMethodInsn(183, "java/lang/RuntimeException", "<init>", "(Ljava/lang/String;Ljava/lang/Throwable;)V", false);
        mv.visitInsn(191);
        mv.visitLabel(noExceptionThrown);
        mv.visitInsn(177);
        mv.visitLocalVariable("targetClass", Type.getDescriptor((Class)Class.class), "Ljava/lang/Class<*>;", start, noExceptionThrown, targetClass);
        mv.visitLocalVariable("lookup", Type.getDescriptor((Class)ASMPrivateLookup.class), (String)null, start, noExceptionThrown, lookup);
        mv.visitLocalVariable("ex", Type.getDescriptor((Class)Throwable.class), (String)null, label6, noExceptionThrown, ex);
        mv.visitMaxs(-1, -1);
        mv.visitEnd();
    }
    
    private void generateInstance() {
        final GeneratorAdapter mv = this.createMethod(1, "instance", Type.getMethodDescriptor(Type.getType((Class)Object.class), new Type[0]));
        final Label label0 = new Label();
        mv.visitLabel(label0);
        mv.visitLineNumber(33, label0);
        mv.loadThis();
        this.getInstance((MethodVisitor)mv);
        mv.returnValue();
        final Label label2 = new Label();
        mv.visitLabel(label2);
        this.visitThis((MethodVisitor)mv, label0, label2);
        mv.visitMaxs(1, 1);
        mv.visitEnd();
    }
    
    private void generateBindTo() {
        final GeneratorAdapter mv = this.createMethod(1, "bindTo", Type.getMethodDescriptor(this.templateClassType, new Type[] { Type.getType((Class)Object.class) }));
        final Label label0 = mv.newLabel();
        mv.visitLabel(label0);
        final Label label2 = new Label();
        mv.loadThis();
        this.getInstance((MethodVisitor)mv);
        mv.visitJumpInsn(198, label2);
        mv.throwException(Type.getType((Class)UnsupportedOperationException.class), "bindTo() must be called from the factory object, not on an instance");
        mv.visitLabel(label2);
        mv.newInstance(this.generatedClassType);
        mv.dup();
        mv.loadArg(0);
        mv.checkCast(this.targetClassType);
        mv.invokeConstructor(this.generatedClassType, new Method("<init>", Type.VOID_TYPE, new Type[] { this.targetClassType }));
        mv.returnValue();
        final Label label3 = new Label();
        mv.visitLabel(label3);
        this.visitThis((MethodVisitor)mv, label0, label3);
        mv.visitLocalVariable("instance", this.targetClassType.getDescriptor(), (String)null, label0, label3, 1);
        mv.visitMaxs(3, 2);
        mv.visitEnd();
    }
    
    private void generateHashCode() {
        final GeneratorAdapter mv = this.createMethod(1, "hashCode", "()I");
        final Label label0 = mv.newLabel();
        mv.visitLabel(label0);
        mv.loadThis();
        this.getInstance((MethodVisitor)mv);
        final Label label2 = mv.newLabel();
        mv.ifNonNull(label2);
        mv.loadThis();
        mv.invokeVirtual(this.generatedClassType, Method.getMethod("int hashCode()"));
        mv.returnValue();
        mv.visitLabel(label2);
        mv.loadThis();
        this.getInstance((MethodVisitor)mv);
        mv.invokeVirtual(this.targetClassType, Method.getMethod("int hashCode()"));
        mv.returnValue();
        mv.visitMaxs(1, 1);
        mv.visitEnd();
    }
    
    private void generateEquals() {
        final GeneratorAdapter mv = this.createMethod(1, "equals", "(Ljava/lang/Object;)Z");
        final Label label0 = new Label();
        mv.visitLabel(label0);
        mv.loadThis();
        mv.loadArg(0);
        final Label label2 = new Label();
        mv.visitJumpInsn(166, label2);
        mv.visitInsn(4);
        mv.visitInsn(172);
        mv.visitLabel(label2);
        mv.loadThis();
        this.getInstance((MethodVisitor)mv);
        final Label label3 = new Label();
        mv.visitJumpInsn(199, label3);
        mv.visitInsn(3);
        mv.visitInsn(172);
        mv.visitLabel(label3);
        mv.loadArg(0);
        final Label label4 = new Label();
        mv.visitJumpInsn(199, label4);
        mv.visitInsn(3);
        mv.visitInsn(172);
        mv.visitLabel(label4);
        mv.loadArg(0);
        mv.instanceOf(this.templateClassType);
        final Label label5 = new Label();
        mv.visitJumpInsn(153, label5);
        final Label label6 = new Label();
        mv.visitLabel(label6);
        mv.loadThis();
        this.getInstance((MethodVisitor)mv);
        mv.loadArg(0);
        mv.checkCast(this.templateClassType);
        mv.invokeInterface(this.templateClassType, Method.getMethod("Object instance();"));
        mv.invokeVirtual(this.targetClassType, Method.getMethod("boolean equals(Object);"));
        mv.visitInsn(172);
        mv.visitLabel(label5);
        mv.loadArg(0);
        mv.instanceOf(this.targetClassType);
        final Label label7 = new Label();
        mv.visitJumpInsn(153, label7);
        final Label label8 = new Label();
        mv.visitLabel(label8);
        mv.loadThis();
        this.getInstance((MethodVisitor)mv);
        mv.loadArg(0);
        mv.invokeVirtual(this.targetClassType, Method.getMethod("boolean equals(Object);"));
        mv.visitInsn(172);
        mv.visitLabel(label7);
        mv.visitInsn(3);
        mv.visitInsn(172);
        final Label label9 = new Label();
        mv.visitLabel(label9);
        this.visitThis((MethodVisitor)mv, label0, label9);
        mv.visitLocalVariable("obj", "Ljava/lang/Object;", (String)null, label0, label9, 1);
        mv.visitMaxs(2, 2);
        mv.visitEnd();
    }
    
    private void generateGetTargetClass() {
        final GeneratorAdapter mv = this.createMethod(1, "Class getTargetClass()");
        mv.push(this.targetClassType);
        mv.returnValue();
        mv.visitMaxs(1, 0);
        mv.visitEnd();
    }
    
    private void generateIsInstance() {
        final GeneratorAdapter mv = this.createMethod(1, "boolean isInstance(Object)");
        mv.loadArg(0);
        mv.instanceOf(this.targetClassType);
        mv.returnValue();
        mv.visitMaxs(1, 1);
        mv.visitEnd();
    }
    
    private void generateNewArraySingleDim() {
        final GeneratorAdapter mv = this.createMethod(1, "Object[] newArray(int)");
        final Label startLabel = new Label();
        mv.visitLabel(startLabel);
        mv.loadArg(0);
        mv.newArray(this.targetClassType);
        mv.visitInsn(176);
        final Label endLabel = new Label();
        mv.visitLabel(endLabel);
        this.visitThis((MethodVisitor)mv, startLabel, endLabel);
        mv.visitLocalVariable("length", "I", (String)null, startLabel, endLabel, 1);
        mv.visitMaxs(1, 2);
        mv.visitEnd();
    }
    
    private void generateNewArrayMultiDim() {
        final GeneratorAdapter mv = this.createMethod(129, "Object[] newArray(int[])");
        final Label label0 = new Label();
        mv.visitLabel(label0);
        mv.loadArg(0);
        mv.arrayLength();
        final Label case1 = new Label();
        final Label case2 = new Label();
        final Label case3 = new Label();
        final Label defaultCase = new Label();
        mv.visitTableSwitchInsn(1, 3, defaultCase, new Label[] { case1, case2, case3 });
        mv.visitLabel(case1);
        mv.loadArg(0);
        mv.visitInsn(3);
        mv.visitInsn(46);
        mv.newArray(this.targetClassType);
        mv.visitInsn(176);
        mv.visitLabel(case2);
        mv.loadArg(0);
        mv.visitInsn(3);
        mv.visitInsn(46);
        mv.loadArg(0);
        mv.visitInsn(4);
        mv.visitInsn(46);
        mv.visitMultiANewArrayInsn("[[" + this.targetClassType.getDescriptor(), 2);
        mv.visitInsn(176);
        mv.visitLabel(case3);
        mv.loadArg(0);
        mv.visitInsn(3);
        mv.visitInsn(46);
        mv.loadArg(0);
        mv.visitInsn(4);
        mv.visitInsn(46);
        mv.loadArg(0);
        mv.visitInsn(5);
        mv.visitInsn(46);
        mv.visitMultiANewArrayInsn("[[[" + this.targetClassType.getDescriptor(), 3);
        mv.visitInsn(176);
        mv.visitLabel(defaultCase);
        mv.push(this.targetClassType);
        mv.loadArg(0);
        mv.visitMethodInsn(184, "java/lang/reflect/Array", "newInstance", "(Ljava/lang/Class;[I)Ljava/lang/Object;", false);
        mv.checkCast(Type.getType((Class)Object[].class));
        mv.visitInsn(176);
        final Label endLabel = new Label();
        mv.visitLabel(endLabel);
        this.visitThis((MethodVisitor)mv, label0, endLabel);
        mv.visitLocalVariable("dimensions", "[I", (String)null, label0, endLabel, 1);
        mv.visitMaxs(4, 2);
        mv.visitEnd();
    }
    
    private void generateToString() {
        final Type StringBuilder = Type.getType((Class)StringBuilder.class);
        final GeneratorAdapter mv = this.createMethod(1, "String toString()");
        final Label start = mv.newLabel();
        mv.visitLabel(start);
        mv.newInstance(StringBuilder);
        mv.dup();
        mv.invokeConstructor(StringBuilder, Method.getMethod("void <init>()"));
        mv.loadThis();
        mv.invokeVirtual(Type.getType((Class)Object.class), Method.getMethod("Class getClass()"));
        mv.invokeVirtual(Type.getType((Class)Class.class), Method.getMethod("String getSimpleName()"));
        mv.invokeVirtual(StringBuilder, Method.getMethod("StringBuilder append(String)"));
        mv.push("(instance=");
        mv.invokeVirtual(StringBuilder, Method.getMethod("StringBuilder append(String)"));
        mv.loadThis();
        this.getInstance((MethodVisitor)mv);
        mv.invokeVirtual(StringBuilder, Method.getMethod("StringBuilder append(Object)"));
        mv.push(41);
        mv.invokeVirtual(StringBuilder, Method.getMethod("StringBuilder append(char)"));
        mv.invokeVirtual(StringBuilder, Method.getMethod("String toString()"));
        mv.returnValue();
        final Label end = new Label();
        mv.visitLabel(end);
        this.visitThis((MethodVisitor)mv, start, end);
        mv.visitMaxs(2, 1);
        mv.visitEnd();
    }
    
    private GeneratorAdapter createMethod(final int access, final String descriptor) {
        final Method desc = Method.getMethod(descriptor);
        return this.createMethod(access, desc.getName(), desc.getDescriptor());
    }
    
    private GeneratorAdapter createMethod(final int access, @Pattern("(\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*)|(<init>)|(<clinit>)") final String name, final String descriptor) {
        final GeneratorAdapter method = new GeneratorAdapter(this.classWriter.visitMethod(access, name, descriptor, (String)null, (String[])null), access, name, descriptor);
        method.visitCode();
        return method;
    }
    
    private void visitThis(final MethodVisitor mv, final Label start, final Label end) {
        mv.visitLocalVariable("this", this.generatedClassType.getDescriptor(), (String)null, start, end, 0);
    }
    
    @NotNull
    public Class<?> loadClass() {
        if (this.loaded != null) {
            return this.loaded;
        }
        this.generate();
        this.verify(true);
        return this.loaded = XReflectASM.CLASS_LOADER.defineClass(this.generatedClassPath, this.bytecode);
    }
    
    static /* synthetic */ MethodVisitor access$401(final XReflectASM x0, final int x1, final String x2, final String x3, final String x4, final String[] x5) {
        return x0.visitMethod(x1, x2, x3, x4, x5);
    }
    
    static {
        JAVA_VERSION = ASMVersion.USED_JAVA_FILE_FORMAT;
        ASM_VERSION = ASMVersion.USED_ASM_OPCODE_VERSION;
        XSERIES_ANNOTATIONS = 'L' + "cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.proxy.annotations".replace('.', '/');
        GENERATED_CLASS_SUFFIX = "_XSeriesGen_" + XReflectASM.ASM_VERSION + '_' + XReflectASM.JAVA_VERSION;
        String magicAccessor;
        try {
            Class.forName("sun.reflect.MagicAccessorImpl");
            magicAccessor = "sun/reflect/MagicAccessorImpl";
        }
        catch (final ClassNotFoundException e) {
            try {
                Class.forName("jdk.internal.reflect.MagicAccessorImpl");
                magicAccessor = "jdk/internal/reflect/MagicAccessorImpl";
            }
            catch (final ClassNotFoundException ex) {
                final IllegalStateException state = new IllegalStateException("Cannot find MagicAccessorImpl class");
                state.addSuppressed((Throwable)e);
                state.addSuppressed((Throwable)ex);
                throw state;
            }
        }
        MAGIC_ACCESSOR_IMPL = magicAccessor;
        SUPER_CLASS = Type.getInternalName((Class)Object.class);
        CLASS_LOADER = new ASMClassLoader();
        PROCESSED = (Map)new IdentityHashMap();
    }
    
    private static final class ASMProxyInfo
    {
        private final ProxyMethodInfo info;
        private final String methodHandleName;
        
        private ASMProxyInfo(final ProxyMethodInfo info, final String methodHandleName) {
            this.info = info;
            this.methodHandleName = methodHandleName;
        }
        
        private boolean isInaccessible() {
            return this.methodHandleName != null;
        }
    }
    
    private final class MethodRewriter extends MethodVisitor
    {
        private final ASMProxyInfo handle;
        private final GeneratorAdapter adapter;
        private final String descriptor;
        
        MethodRewriter(final ASMProxyInfo handle, final int access, final String name, final String descriptor, final String signature, final String[] exceptions) {
            super(XReflectASM.ASM_VERSION, XReflectASM.access$401(XReflectASM.this, XAccessFlag.ABSTRACT.remove(access), name, descriptor, signature, exceptions));
            this.handle = handle;
            this.adapter = new GeneratorAdapter(this.mv, access, name, descriptor);
            this.descriptor = descriptor;
            this.generateCode();
        }
        
        private void generateCode() {
            this.adapter.visitCode();
            final boolean isInterface = XReflectASM.this.targetClass.isInterface();
            ReflectedObject.Type type;
            String name;
            boolean isStatic;
            try {
                final ReflectedObject obj = this.handle.info.handle.jvm().reflect();
                type = obj.type();
                name = obj.name();
                isStatic = obj.accessFlags().contains((Object)XAccessFlag.STATIC);
            }
            catch (final ReflectiveOperationException e) {
                throw new IllegalStateException((Throwable)e);
            }
            final Label label0 = new Label();
            this.adapter.visitLabel(label0);
            if (type == ReflectedObject.Type.CONSTRUCTOR) {
                this.adapter.loadThis();
                XReflectASM.this.getInstance((MethodVisitor)this.adapter);
                final Label label2 = new Label();
                this.adapter.ifNull(label2);
                this.adapter.throwException(Type.getType((Class)UnsupportedOperationException.class), "Constructor method must be called from the factory object, not on an instance");
                this.adapter.visitLabel(label2);
            }
            final MappedType rType = this.handle.info.rType;
            Type syntheticReturnType;
            Type realReturnType;
            boolean needsConversion;
            if (rType.isDifferent()) {
                if (rType.synthetic.isAssignableFrom(rType.real)) {
                    realReturnType = (syntheticReturnType = null);
                    needsConversion = false;
                }
                else {
                    if (!ReflectiveProxyObject.class.isAssignableFrom(rType.synthetic)) {
                        throw new VerifyError("Cannot convert return type " + (Object)rType.synthetic + " to " + (Object)rType.real + " in proxy method " + (Object)this.handle.info.interfaceMethod);
                    }
                    needsConversion = true;
                    realReturnType = Type.getType((Class)rType.real);
                    syntheticReturnType = XReflectASM.getType(getGeneratedClassPath(rType.synthetic));
                    this.adapter.newInstance(syntheticReturnType);
                    this.adapter.dup();
                }
            }
            else {
                realReturnType = (syntheticReturnType = null);
                needsConversion = false;
            }
            if (this.handle.isInaccessible()) {
                this.adapter.getStatic(XReflectASM.this.generatedClassType, "H_" + this.handle.methodHandleName, Type.getType((Class)MethodHandle.class));
            }
            if (type == ReflectedObject.Type.CONSTRUCTOR) {
                if (!this.handle.isInaccessible()) {
                    this.adapter.newInstance(XReflectASM.this.targetClassType);
                    this.adapter.dup();
                }
            }
            else if (!isStatic) {
                this.adapter.loadThis();
                XReflectASM.this.getInstance((MethodVisitor)this.adapter);
            }
            int operandIndex = 1;
            final Type[] argumentTypes = this.adapter.getArgumentTypes();
            for (int i = 0; i < argumentTypes.length; ++i) {
                final Type argumentType = argumentTypes[i];
                final MappedType pType = this.handle.info.pTypes[i];
                if (pType.isDifferent()) {
                    if (!ReflectiveProxyObject.class.isAssignableFrom(pType.synthetic)) {
                        throw new VerifyError("Cannot convert parameter type " + (Object)pType.synthetic + " to " + (Object)pType.real + " in proxy method " + (Object)this.handle.info.interfaceMethod);
                    }
                    this.adapter.visitVarInsn(argumentType.getOpcode(21), operandIndex);
                    this.adapter.invokeInterface(Type.getType((Class)ReflectiveProxyObject.class), Method.getMethod("Object instance()"));
                    this.adapter.checkCast(Type.getType((Class)pType.real));
                }
                else {
                    this.adapter.visitVarInsn(argumentType.getOpcode(21), operandIndex);
                }
                operandIndex += argumentType.getSize();
            }
            final String invokeExact = "invokeExact";
            switch (type) {
                case METHOD: {
                    if (this.handle.isInaccessible()) {
                        this.adapter.invokeVirtual(Type.getType((Class)MethodHandle.class), new Method(invokeExact, Type.getType((Class)this.handle.info.rType.real), (Type[])Streams.concat(new Stream[] { isStatic ? Stream.of((Object[])new Type[0]) : Stream.of((Object)XReflectASM.this.targetClassType), Arrays.stream((Object[])this.handle.info.pTypes).map(x -> Type.getType((Class)x.real)) }).toArray(Type[]::new)));
                        break;
                    }
                    this.adapter.visitMethodInsn(isStatic ? 184 : (isInterface ? 185 : 182), XReflectASM.this.targetClassType.getInternalName(), name, Type.getMethodDescriptor(Type.getType((Class)rType.real), convert(this.handle.info.pTypes)), isInterface);
                    break;
                }
                case FIELD: {
                    final boolean isSetter = argumentTypes.length != 0;
                    Type fieldDescriptor;
                    if (argumentTypes.length != 0) {
                        fieldDescriptor = this.adapter.getArgumentTypes()[0];
                    }
                    else {
                        fieldDescriptor = this.adapter.getReturnType();
                    }
                    if (this.handle.isInaccessible()) {
                        final List<Type> parameters = (List<Type>)new ArrayList(3);
                        if (!isStatic) {
                            parameters.add((Object)XReflectASM.this.targetClassType);
                        }
                        if (isSetter) {
                            parameters.add((Object)Type.getType((Class)this.handle.info.pTypes[0].real));
                        }
                        this.adapter.invokeVirtual(Type.getType((Class)MethodHandle.class), new Method(invokeExact, Type.getType((Class)this.handle.info.rType.real), (Type[])parameters.toArray((Object[])new Type[0])));
                        break;
                    }
                    int fieldCode;
                    if (isSetter) {
                        if (isStatic) {
                            fieldCode = 179;
                        }
                        else {
                            fieldCode = 181;
                        }
                    }
                    else if (isStatic) {
                        fieldCode = 178;
                    }
                    else {
                        fieldCode = 180;
                    }
                    this.adapter.visitFieldInsn(fieldCode, XReflectASM.this.targetClassType.getInternalName(), name, fieldDescriptor.getDescriptor());
                    break;
                }
                case CONSTRUCTOR: {
                    if (this.handle.isInaccessible()) {
                        this.adapter.invokeVirtual(Type.getType((Class)MethodHandle.class), new Method(invokeExact, XReflectASM.this.targetClassType, convert(this.handle.info.pTypes)));
                        break;
                    }
                    this.adapter.visitMethodInsn(183, XReflectASM.this.targetClassType.getInternalName(), "<init>", Type.getMethodDescriptor(Type.getType(Void.TYPE), argumentTypes), false);
                    break;
                }
                default: {
                    throw new IllegalStateException("Unknown ReflectedObject type: " + (Object)type);
                }
            }
            if (needsConversion) {
                this.adapter.invokeConstructor(syntheticReturnType, new Method("<init>", Type.VOID_TYPE, new Type[] { realReturnType }));
            }
            this.adapter.returnValue();
            final Label label3 = new Label();
            this.adapter.visitLabel(label3);
            if (!isStatic && type != ReflectedObject.Type.CONSTRUCTOR) {
                XReflectASM.this.visitThis((MethodVisitor)this.adapter, label0, label3);
            }
            final int magicMaxs = magicMaxs(this.descriptor, isStatic);
            this.adapter.visitMaxs(magicMaxs, magicMaxs);
            this.adapter.visitEnd();
        }
        
        public void visitCode() {
        }
        
        public AnnotationVisitor visitParameterAnnotation(final int parameter, final String descriptor, final boolean visible) {
            if (shouldRemoveAnnotation(descriptor)) {
                return null;
            }
            return super.visitParameterAnnotation(parameter, descriptor, visible);
        }
        
        public AnnotationVisitor visitAnnotation(final String descriptor, final boolean visible) {
            if (shouldRemoveAnnotation(descriptor)) {
                return null;
            }
            return super.visitAnnotation(descriptor, visible);
        }
        
        public AnnotationVisitor visitTypeAnnotation(final int typeRef, final TypePath typePath, final String descriptor, final boolean visible) {
            if (shouldRemoveAnnotation(descriptor)) {
                return null;
            }
            return super.visitTypeAnnotation(typeRef, typePath, descriptor, visible);
        }
    }
}
