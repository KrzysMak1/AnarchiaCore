package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.proxy.generator;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.StandardOpenOption;
import java.nio.file.OpenOption;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Comparator;
import java.util.Collection;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.proxy.annotations.Ignore;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.proxy.ReflectiveProxyObject;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.proxy.annotations.Proxify;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.ArrayList;
import java.lang.reflect.InvocationTargetException;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.proxy.annotations.Field;
import java.util.stream.Collectors;
import java.util.Arrays;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.proxy.annotations.Constructor;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.proxy.annotations.Final;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.proxy.annotations.Static;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.proxy.annotations.Protected;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.proxy.annotations.Private;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.XAccessFlag;
import java.lang.reflect.AnnotatedElement;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm.objects.ReflectedObject;
import java.util.StringJoiner;
import java.util.HashSet;
import java.util.function.Function;
import java.util.Set;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public final class XProxifier
{
    private static final String MEMBER_SPACES = "    ";
    private final StringBuilder writer;
    private final Set<String> imports;
    private final String proxifiedClassName;
    private final Class<?> clazz;
    private final boolean generateIntelliJAnnotations = true;
    private final boolean generateInaccessibleMembers = true;
    private final boolean copyAnnotations = true;
    private final boolean writeComments = true;
    private final boolean writeInfoAnnotationsAsComments = true;
    private boolean disableIDEFormatting;
    private Function<Class<?>, String> remapper;
    
    public XProxifier(final Class<?> clazz) {
        this.writer = new StringBuilder(1000);
        this.imports = (Set<String>)new HashSet(20);
        this.clazz = clazz;
        this.proxifiedClassName = clazz.getSimpleName() + "Proxified";
        this.proxify();
    }
    
    private static Class<?> unwrapArrayType(Class<?> clazz) {
        while (clazz.isArray()) {
            clazz = clazz.getComponentType();
        }
        return clazz;
    }
    
    private void imports(Class<?> clazz) {
        clazz = unwrapArrayType(clazz);
        if (!clazz.isPrimitive() && !clazz.getPackage().getName().equals((Object)"java.lang")) {
            this.imports.add((Object)clazz.getName().replace('$', '.'));
        }
    }
    
    private void writeComments(final String... comments) {
        final boolean multiLine = comments.length > 1;
        if (!multiLine) {
            this.writer.append("// ").append(comments[0]).append('\n');
        }
        this.writer.append("/**\n");
        for (final String comment : comments) {
            this.writer.append(" * ");
            this.writer.append(comment);
            this.writer.append('\n');
        }
        this.writer.append(" */\n");
    }
    
    private void writeThrownExceptions(final Class<?>[] exceptionTypes) {
        if (exceptionTypes == null || exceptionTypes.length == 0) {
            return;
        }
        this.writer.append(" throws ");
        final StringJoiner exs = new StringJoiner((CharSequence)", ");
        for (final Class<?> ex : exceptionTypes) {
            this.imports(ex);
            exs.add((CharSequence)ex.getSimpleName());
        }
        this.writer.append((Object)exs);
    }
    
    private void writeMember(final ReflectedObject jvm) {
        this.writeMember(jvm, false);
    }
    
    private void writeMember(final ReflectedObject jvm, final boolean generateGetterField) {
        this.writer.append(this.annotationsToString(true, true, (AnnotatedElement)jvm));
        final Set<XAccessFlag> accessFlags = jvm.accessFlags();
        if (accessFlags.contains((Object)XAccessFlag.PRIVATE)) {
            this.writeAnnotation(Private.class, new String[0]);
        }
        if (accessFlags.contains((Object)XAccessFlag.PROTECTED)) {
            this.writeAnnotation(Protected.class, new String[0]);
        }
        if (accessFlags.contains((Object)XAccessFlag.STATIC)) {
            this.writeAnnotation(Static.class, new String[0]);
        }
        if (accessFlags.contains((Object)XAccessFlag.FINAL)) {
            this.writeAnnotation(Final.class, new String[0]);
        }
        switch (jvm.type()) {
            case CONSTRUCTOR: {
                this.writeAnnotation(Constructor.class, new String[0]);
                this.writeAnnotation("NotNull", new String[0]);
                final java.lang.reflect.Constructor<?> ctor = (java.lang.reflect.Constructor<?>)jvm.unreflect();
                final String contractParams = (String)Arrays.stream((Object[])ctor.getParameterTypes()).map(x -> "_").collect(Collectors.joining((CharSequence)", "));
                this.writeAnnotation("Contract", "value = \"" + contractParams + " -> new\"", "pure = true");
                break;
            }
            case FIELD: {
                this.writeAnnotation(Field.class, new String[0]);
                if (generateGetterField) {
                    this.writeAnnotation("Contract", "pure = true");
                    break;
                }
                this.writeAnnotation("Contract", "mutates = \"this\"");
                break;
            }
        }
        final StringJoiner parameters = new StringJoiner((CharSequence)", ", (CharSequence)"(", (CharSequence)")");
        Class<?>[] exceptionTypes = null;
        this.writer.append("    ");
        switch (jvm.type()) {
            case CONSTRUCTOR: {
                final java.lang.reflect.Constructor<?> constructor = (java.lang.reflect.Constructor<?>)jvm.unreflect();
                exceptionTypes = constructor.getExceptionTypes();
                this.writer.append(this.proxifiedClassName).append(' ').append("construct");
                this.writeParameters(parameters, constructor.getParameters());
                break;
            }
            case FIELD: {
                final java.lang.reflect.Field field = (java.lang.reflect.Field)jvm.unreflect();
                this.imports(field.getType());
                if (generateGetterField) {
                    this.writer.append(field.getType().getSimpleName());
                }
                else {
                    this.writer.append("void");
                    parameters.add((CharSequence)(field.getType().getSimpleName() + " value"));
                }
                this.writer.append(' ');
                this.writer.append(jvm.name());
                break;
            }
            case METHOD: {
                final Method method = (Method)jvm.unreflect();
                exceptionTypes = method.getExceptionTypes();
                this.imports(method.getReturnType());
                this.writer.append(method.getReturnType().getSimpleName());
                this.writer.append(' ');
                this.writer.append(jvm.name());
                this.writeParameters(parameters, method.getParameters());
                break;
            }
        }
        this.writer.append((Object)parameters);
        this.writeThrownExceptions(exceptionTypes);
        this.writer.append(";\n\n");
    }
    
    private static Object[] getArray(final Object val) {
        if (val instanceof Object[]) {
            return (Object[])val;
        }
        final int arrlength = Array.getLength(val);
        final Object[] outputArray = new Object[arrlength];
        for (int i = 0; i < arrlength; ++i) {
            outputArray[i] = Array.get(val, i);
        }
        return outputArray;
    }
    
    private String constantToString(final Object obj) {
        if (obj instanceof String) {
            return '\"' + obj.toString() + '\"';
        }
        if (obj instanceof Class) {
            final Class<?> clazz = (Class<?>)obj;
            this.imports(clazz);
            return clazz.getSimpleName() + ".class";
        }
        if (obj instanceof Annotation) {
            final Annotation annotation = (Annotation)obj;
            return this.annotationToString(annotation);
        }
        if (obj.getClass().isEnum()) {
            this.imports(obj.getClass());
            return obj.getClass().getSimpleName() + '.' + ((Enum)obj).name();
        }
        if (!obj.getClass().isArray()) {
            return obj.toString();
        }
        final Object[] array = getArray(obj);
        if (array.length == 0) {
            return "{}";
        }
        StringJoiner builder;
        if (array.length == 1) {
            builder = new StringJoiner((CharSequence)", ");
        }
        else {
            builder = new StringJoiner((CharSequence)", ", (CharSequence)"{", (CharSequence)"}");
        }
        for (final Object element : array) {
            builder.add((CharSequence)this.constantToString(element));
        }
        return builder.toString();
    }
    
    private String annotationsToString(final boolean member, final boolean newLine, final AnnotatedElement annotatable) {
        final StringJoiner builder = new StringJoiner((CharSequence)((newLine ? Character.valueOf('\n') : "") + (member ? "    " : "")), (CharSequence)(member ? "    " : ""), (CharSequence)(newLine ? "\n" : "")).setEmptyValue((CharSequence)"");
        for (final Annotation annotation : annotatable.getAnnotations()) {
            final Annotation[] unwrapped = unwrapRepeatElement(annotation);
            if (unwrapped != null) {
                for (final Annotation inner : unwrapped) {
                    builder.add((CharSequence)this.annotationToString(inner));
                }
            }
            else {
                builder.add((CharSequence)this.annotationToString(annotation));
            }
        }
        return builder.toString();
    }
    
    private static Annotation[] unwrapRepeatElement(final Annotation annotation) {
        try {
            final Method method = annotation.annotationType().getDeclaredMethod("value", (Class[])new Class[0]);
            if (method.getReturnType().isArray()) {
                final Class<?> rawReturn = unwrapArrayType(method.getReturnType());
                if (rawReturn.isAnnotation()) {
                    final Repeatable repeatable = rawReturn.getAnnotation(Repeatable.class);
                    if (repeatable != null && repeatable.value() == annotation.annotationType()) {
                        try {
                            return (Annotation[])method.invoke((Object)annotation, new Object[0]);
                        }
                        catch (final IllegalAccessException | InvocationTargetException e) {
                            throw new IllegalArgumentException((Throwable)e);
                        }
                    }
                }
            }
        }
        catch (final NoSuchMethodException ex) {}
        return null;
    }
    
    private String annotationToString(final Annotation annotation) {
        final List<String> builder = (List<String>)new ArrayList();
        boolean visitedValue = false;
        for (final Method entry : annotation.annotationType().getDeclaredMethods()) {
            Label_0214: {
                try {
                    entry.setAccessible(true);
                    final String key = entry.getName();
                    final Object value = entry.invoke((Object)annotation, new Object[0]);
                    try {
                        final Object defaultValue = entry.getDefaultValue();
                        if (defaultValue != null) {
                            if (defaultValue.getClass().isArray()) {
                                if (Arrays.equals(getArray(defaultValue), getArray(value))) {
                                    break Label_0214;
                                }
                            }
                            else if (value.equals(defaultValue)) {
                                break Label_0214;
                            }
                        }
                    }
                    catch (final TypeNotPresentException ex) {}
                    if (key.equals((Object)"value")) {
                        visitedValue = true;
                    }
                    builder.add((Object)(key + " = " + this.constantToString(value)));
                }
                catch (final IllegalAccessException | InvocationTargetException e) {
                    throw new IllegalStateException("Failed to get annotation value " + (Object)entry, (Throwable)e);
                }
            }
        }
        this.imports(annotation.annotationType());
        String annotationValues;
        if (builder.isEmpty()) {
            annotationValues = "";
        }
        else if (builder.size() == 1 && visitedValue) {
            annotationValues = (String)builder.get(0);
            final int equalsSign = annotationValues.indexOf(61);
            annotationValues = '(' + annotationValues.substring(equalsSign + 2) + ')';
        }
        else {
            annotationValues = '(' + String.join((CharSequence)", ", (Iterable)builder) + ')';
        }
        return '@' + annotation.annotationType().getSimpleName() + annotationValues;
    }
    
    private StringJoiner writeParameters(final StringJoiner joiner, final Parameter[] parameters) {
        for (final Parameter parameter : parameters) {
            this.imports(parameter.getType());
            String type;
            if (parameter.isVarArgs()) {
                type = parameter.getType().getSimpleName() + "... ";
            }
            else {
                type = parameter.getType().getSimpleName();
            }
            final String annotations = this.annotationsToString(false, false, (AnnotatedElement)parameter);
            joiner.add((CharSequence)(annotations + (annotations.isEmpty() ? "" : " ") + type + ' ' + parameter.getName()));
        }
        return joiner;
    }
    
    private void writeAnnotation(final Class<?> annotation, final String... values) {
        this.writeAnnotation(true, annotation, values);
    }
    
    private void writeAnnotation(final boolean member, final Class<?> annotation, final String... values) {
        this.imports(annotation);
        this.writeAnnotation(member, annotation.getSimpleName(), values);
    }
    
    private void writeAnnotation(final String annotation, final String... values) {
        this.writeAnnotation(true, annotation, values);
    }
    
    private void writeAnnotation(final boolean member, final String annotation, final String... values) {
        if (member) {
            this.writer.append("    ");
        }
        this.writer.append('@').append(annotation);
        if (values.length != 0) {
            final StringJoiner valueJoiner = new StringJoiner((CharSequence)", ", (CharSequence)"(", (CharSequence)")");
            for (final String value : values) {
                valueJoiner.add((CharSequence)value);
            }
            this.writer.append((Object)valueJoiner);
        }
        this.writer.append('\n');
    }
    
    private void proxify() {
        if (this.disableIDEFormatting) {
            this.writer.append("// ").append("@formatter:").append("OFF").append('\n');
        }
        this.writeComments("This is a generated proxified class for " + this.clazz.getSimpleName() + ". However, you might", "want to review each member and correct its annotations when needed.", "<p>", "It's also recommended to use your IDE's code formatter to adjust", "imports and spaces according to your settings.", "In IntelliJ, this can be done by with Ctrl+Alt+L", "<p>", "Full Target Class Path:", this.clazz.getName());
        this.writer.append(this.annotationsToString(false, true, (AnnotatedElement)this.clazz));
        this.writeAnnotation(false, Proxify.class, "target = " + this.clazz.getSimpleName() + ".class");
        if (!XAccessFlag.PUBLIC.isSet(this.clazz.getModifiers())) {
            this.writeAnnotation(false, Private.class, new String[0]);
        }
        if (XAccessFlag.FINAL.isSet(this.clazz.getModifiers())) {
            this.writeAnnotation(false, Final.class, new String[0]);
            this.writeAnnotation(false, "ApiStatus.NonExtendable", new String[0]);
        }
        this.writer.append("public interface ").append(this.proxifiedClassName).append(" extends ").append(ReflectiveProxyObject.class.getSimpleName()).append(" {\n");
        final java.lang.reflect.Field[] declaredFields2;
        final java.lang.reflect.Field[] declaredFields = declaredFields2 = this.clazz.getDeclaredFields();
        for (final java.lang.reflect.Field field : declaredFields2) {
            if (!field.isSynthetic()) {
                if (!XAccessFlag.FINAL.isSet(field.getModifiers())) {
                    this.writeMember(ReflectedObject.of(field), false);
                }
                this.writeMember(ReflectedObject.of(field), true);
            }
        }
        if (declaredFields.length != 0) {
            this.writer.append('\n');
        }
        final java.lang.reflect.Constructor<?>[] declaredConstructors2;
        final java.lang.reflect.Constructor<?>[] declaredConstructors = declaredConstructors2 = this.clazz.getDeclaredConstructors();
        for (final java.lang.reflect.Constructor<?> constructor : declaredConstructors2) {
            if (!constructor.isSynthetic()) {
                this.writeMember(ReflectedObject.of(constructor));
            }
        }
        if (declaredConstructors.length != 0) {
            this.writer.append('\n');
        }
        for (final Method method : this.clazz.getDeclaredMethods()) {
            if (method.getDeclaringClass() != Object.class) {
                if (!method.isSynthetic()) {
                    if (!method.isBridge()) {
                        this.writeMember(ReflectedObject.of(method));
                    }
                }
            }
        }
        this.writer.append('\n');
        this.writeAnnotation(Ignore.class, new String[0]);
        this.writeAnnotation("NotNull", new String[0]);
        this.writeAnnotation("ApiStatus.OverrideOnly", new String[0]);
        this.writeAnnotation("Contract", "value = \"_ -> new\"", "pure = true");
        this.writer.append("    ").append(this.proxifiedClassName).append(" bindTo(@NotNull Object instance);\n");
        this.writer.append("}\n");
        this.finalizeString();
    }
    
    private void finalizeString() {
        final StringBuilder whole = new StringBuilder(this.writer.length() + this.imports.size() * 100);
        whole.append("import org.jetbrains.annotations.*;\n");
        final List<String> sortedImports = (List<String>)new ArrayList((Collection)this.imports);
        sortedImports.sort(Comparator.naturalOrder());
        for (final String anImport : sortedImports) {
            whole.append("import ").append(anImport).append(";\n");
        }
        whole.append('\n');
        this.writer.insert(0, (CharSequence)whole);
        this.imports(ReflectiveProxyObject.class);
    }
    
    public String getString() {
        if (this.writer.length() == 0) {
            this.proxify();
        }
        return this.writer.toString();
    }
    
    public void writeTo(Path path) {
        if (Files.isDirectory(path, new LinkOption[0])) {
            path = path.resolve(this.proxifiedClassName + ".java");
        }
        try (final BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8, new OpenOption[] { (OpenOption)StandardOpenOption.CREATE, (OpenOption)StandardOpenOption.WRITE, (OpenOption)StandardOpenOption.TRUNCATE_EXISTING })) {
            writer.write(this.getString());
        }
        catch (final IOException e) {
            throw new IllegalStateException((Throwable)e);
        }
    }
}
