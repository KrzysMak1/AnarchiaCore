package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.asm;

import java.lang.reflect.Method;
import java.lang.invoke.MethodHandles;
import org.objectweb.asm.tree.analysis.BasicValue;
import java.util.Iterator;
import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.tree.analysis.Interpreter;
import org.objectweb.asm.tree.analysis.Analyzer;
import java.util.List;
import org.objectweb.asm.tree.analysis.SimpleVerifier;
import org.objectweb.asm.tree.MethodNode;
import java.util.ArrayList;
import org.objectweb.asm.Type;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.util.CheckClassAdapter;
import org.objectweb.asm.tree.ClassNode;
import java.io.PrintWriter;
import org.objectweb.asm.ClassReader;
import java.util.Set;
import java.util.Map;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.function.Predicate;
import java.lang.invoke.MethodHandle;

final class ASMAnalyzer
{
    private static final MethodHandle CheckClassAdapter_printAnalyzerResult;
    
    private ASMAnalyzer() {
    }
    
    protected static Throwable findCause(final Throwable exception, final Predicate<Throwable> findCause) {
        final Set<Throwable> circularCause = (Set<Throwable>)Collections.newSetFromMap((Map)new IdentityHashMap(5));
        Throwable cause = exception;
        while (circularCause.add((Object)cause)) {
            if (findCause.test((Object)cause)) {
                return cause;
            }
            cause = cause.getCause();
            if (cause == null) {
                return null;
            }
        }
        return null;
    }
    
    protected static void verify(final ClassReader classReader, final ClassLoader loader, final boolean printResults, final PrintWriter printWriter) {
        final ClassNode classNode = new ClassNode();
        classReader.accept((ClassVisitor)new CheckClassAdapter(ASMVersion.LATEST_ASM_OPCODE_VERSION, classNode, false) {}, 2);
        final Type syperType = (classNode.superName == null) ? null : Type.getObjectType(classNode.superName);
        final List<MethodNode> methods = (List<MethodNode>)classNode.methods;
        final List<Type> interfaces = (List<Type>)new ArrayList();
        for (final String interfaceName : classNode.interfaces) {
            interfaces.add((Object)Type.getObjectType(interfaceName));
        }
        for (final MethodNode method : methods) {
            final SimpleVerifier verifier = new SimpleVerifier(Type.getObjectType(classNode.name), syperType, (List)interfaces, (classNode.access & 0x200) != 0x0);
            final Analyzer<BasicValue> analyzer = (Analyzer<BasicValue>)new Analyzer((Interpreter)verifier);
            if (loader != null) {
                verifier.setClassLoader(loader);
            }
            boolean hasError;
            try {
                analyzer.analyze(classNode.name, method);
                hasError = false;
            }
            catch (final AnalyzerException e) {
                final ClassNotFoundException cls = (ClassNotFoundException)findCause((Throwable)e, (Predicate<Throwable>)(cause -> cause instanceof ClassNotFoundException));
                if (cls == null || cls.getMessage() == null || !cls.getMessage().contains((CharSequence)"XSeriesGen")) {
                    hasError = true;
                    e.printStackTrace(printWriter);
                }
                else {
                    hasError = false;
                }
            }
            if (!printResults) {
                if (!hasError) {
                    continue;
                }
            }
            try {
                ASMAnalyzer.CheckClassAdapter_printAnalyzerResult.invokeExact(method, (Analyzer)analyzer, printWriter);
            }
            catch (final Throwable e2) {
                throw new IllegalStateException("Cannot write bytecode instructions: ", e2);
            }
        }
        printWriter.flush();
    }
    
    static {
        final MethodHandles.Lookup lookup = MethodHandles.lookup();
        MethodHandle printAnalyzerResult;
        try {
            final Method printer = CheckClassAdapter.class.getDeclaredMethod("printAnalyzerResult", MethodNode.class, Analyzer.class, PrintWriter.class);
            printer.setAccessible(true);
            printAnalyzerResult = lookup.unreflect(printer);
        }
        catch (final NoSuchMethodException | IllegalAccessException e) {
            printAnalyzerResult = null;
        }
        CheckClassAdapter_printAnalyzerResult = printAnalyzerResult;
    }
}
