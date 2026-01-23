package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.parser;

import java.util.stream.Collectors;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;
import java.time.Duration;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.HashMap;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.minecraft.MinecraftPackage;
import java.util.stream.Stream;
import java.util.Objects;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm.FlaggedNamedMemberHandle;
import java.util.Collection;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm.FieldMemberHandle;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm.MethodMemberHandle;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm.ConstructorMemberHandle;
import java.util.Arrays;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm.MemberHandle;
import java.util.Locale;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm.classes.DynamicClassHandle;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.ReflectiveHandle;
import org.jetbrains.annotations.Nullable;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm.classes.StaticClassHandle;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.XReflection;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm.classes.UnknownClassHandle;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm.classes.ClassHandle;
import org.jetbrains.annotations.NotNull;
import java.util.EnumSet;
import org.intellij.lang.annotations.Language;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm.classes.PackageHandle;
import java.util.Set;
import java.util.Map;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.ReflectiveNamespace;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public final class ReflectionParser
{
    private static final String[] DEFAULT_CHECKED_PACKAGES;
    private final String declaration;
    private Pattern pattern;
    private Matcher matcher;
    private ReflectiveNamespace namespace;
    private Map<String, Class<?>> cachedImports;
    private String[] checkedPackages;
    private final Set<Flag> flags;
    private static final PackageHandle[] PACKAGE_HANDLES;
    @Language("RegExp")
    private static final String GENERIC = "(?:\\s*<\\s*[.\\w<>\\[\\], ]+\\s*>)?";
    @Language("RegExp")
    private static final String ARRAY = "(?:(?:\\[])*)";
    @Language("RegExp")
    private static final String PACKAGE_REGEX = "(?:package\\s+(?<package>(\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*\\.)*\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*)\\s*;\\s*)?";
    @Language("RegExp")
    private static final String CLASS_TYPES = "(?<classType>class|interface|enum|record)";
    @Language("RegExp")
    private static final String PARAMETERS = "\\s*\\(\\s*(?<parameters>[\\w$_,.<?>\\[\\] ]+)?\\s*\\)";
    @Language("RegExp")
    private static final String THROWS;
    @Language("RegExp")
    private static final String END_DECL = "\\s*;?\\s*";
    private static final Pattern CLASS;
    private static final Pattern METHOD;
    private static final Pattern CONSTRUCTOR;
    private static final Pattern FIELD;
    private static final Map<String, Class<?>> PREDEFINED_TYPES;
    
    public ReflectionParser(@Language("Java") final String declaration) {
        this.checkedPackages = ReflectionParser.DEFAULT_CHECKED_PACKAGES;
        this.flags = (Set<Flag>)EnumSet.noneOf((Class)Flag.class);
        this.declaration = declaration;
    }
    
    public ReflectionParser checkedPackages(@org.intellij.lang.annotations.Pattern("(\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*\\.)*\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*") final String... checkedPackages) {
        this.checkedPackages = checkedPackages;
        return this;
    }
    
    private static IDHandler id(@NotNull @Language("RegExp") final String groupName) {
        return new IDHandler(groupName, false);
    }
    
    private static IDHandler type(@Language("RegExp") final String groupName) {
        return new IDHandler(groupName, true).generic(true).array(true);
    }
    
    private ClassHandle[] parseTypes(final String[] typeNames) {
        final ClassHandle[] classes = new ClassHandle[typeNames.length];
        for (int i = 0; i < typeNames.length; ++i) {
            String typeName = typeNames[i];
            typeName = typeName.trim().substring(0, typeName.lastIndexOf(32)).trim();
            classes[i] = this.parseType(typeName);
        }
        return classes;
    }
    
    private ClassHandle parseType(String typeName) {
        if (this.cachedImports == null && this.namespace != null) {
            this.cachedImports = this.namespace.getImports();
        }
        final String firstTypeName = typeName;
        typeName = typeName.replace((CharSequence)" ", (CharSequence)"");
        int arrayDimension = 0;
        if (typeName.endsWith("[]")) {
            final String replaced = typeName.replace((CharSequence)"[]", (CharSequence)"");
            arrayDimension = (typeName.length() - replaced.length()) / 2;
            typeName = replaced;
        }
        if (typeName.endsWith(">")) {
            typeName = typeName.substring(0, typeName.indexOf(60));
        }
        Class<?> clazz = this.stringToClass(typeName);
        if (clazz == null) {
            return new UnknownClassHandle(this.getOrCreateNamespace(), firstTypeName + " -> " + typeName);
        }
        if (arrayDimension != 0) {
            clazz = XReflection.of(clazz).asArray(arrayDimension).unreflect();
        }
        return new StaticClassHandle(this.getOrCreateNamespace(), clazz);
    }
    
    @Nullable
    private Class<?> stringToClass(final String typeName) {
        Class<?> clazz = null;
        if (!typeName.contains((CharSequence)".")) {
            if (this.cachedImports != null) {
                clazz = (Class)this.cachedImports.get((Object)typeName);
            }
            if (clazz == null) {
                clazz = (Class)ReflectionParser.PREDEFINED_TYPES.get((Object)typeName);
            }
            if (clazz == null && this.checkedPackages != null) {
                for (final String checkedPackage : this.checkedPackages) {
                    final boolean inner = checkedPackage.endsWith("$");
                    clazz = classNamed(checkedPackage + (inner ? "" : Character.valueOf('.')) + typeName);
                    if (clazz != null) {
                        break;
                    }
                }
            }
        }
        if (clazz == null) {
            clazz = classNamed(typeName);
        }
        return clazz;
    }
    
    private static Class<?> classNamed(final String name) {
        try {
            return Class.forName(name);
        }
        catch (final ClassNotFoundException ignored) {
            return null;
        }
    }
    
    private ReflectiveNamespace getOrCreateNamespace() {
        return (this.namespace == null) ? XReflection.namespaced() : this.namespace;
    }
    
    public ReflectionParser imports(final ReflectiveNamespace namespace) {
        this.namespace = namespace;
        return this;
    }
    
    private void pattern(final Pattern pattern, final ReflectiveHandle<?> handle) {
        this.pattern = pattern;
        this.matcher = pattern.matcher((CharSequence)this.declaration);
        this.start(handle);
    }
    
    public <T extends DynamicClassHandle> T parseClass(final T classHandle) {
        this.pattern(ReflectionParser.CLASS, classHandle);
        final String packageName = this.group("package");
        if (packageName != null && !packageName.isEmpty()) {
            boolean found = false;
            for (final PackageHandle pkgHandle : ReflectionParser.PACKAGE_HANDLES) {
                final String targetPackageName = pkgHandle.packageId().toLowerCase(Locale.ENGLISH);
                if (packageName.startsWith(targetPackageName)) {
                    if (packageName.indexOf(46) == -1) {
                        classHandle.inPackage(pkgHandle);
                    }
                    else {
                        classHandle.inPackage(pkgHandle, packageName.substring(targetPackageName.length() + 1));
                    }
                    found = true;
                    break;
                }
            }
            if (!found) {
                classHandle.inPackage(packageName);
            }
        }
        String className = this.group("className");
        if (className.contains((CharSequence)"<")) {
            className = className.substring(0, className.indexOf(60));
        }
        classHandle.named(className);
        return classHandle;
    }
    
    private void includeInnerClassOf(final MemberHandle handle) {
        final Class<?> clazz = handle.getClassHandle().reflectOrNull();
        if (clazz == null) {
            return;
        }
        final int len = ReflectionParser.DEFAULT_CHECKED_PACKAGES.length + 2;
        (this.checkedPackages = (String[])Arrays.copyOf((Object[])ReflectionParser.DEFAULT_CHECKED_PACKAGES, len))[len - 1] = clazz.getName() + '$';
        this.checkedPackages[len - 2] = clazz.getPackage().getName();
    }
    
    public <T extends ConstructorMemberHandle> T parseConstructor(final T ctorHandle) {
        this.includeInnerClassOf(ctorHandle);
        this.pattern(ReflectionParser.CONSTRUCTOR, ctorHandle);
        if (this.has("className") && !ctorHandle.getClassHandle().getPossibleNames().contains((Object)this.group("className"))) {
            this.error("Wrong class name associated to constructor, possible names: " + (Object)ctorHandle.getClassHandle().getPossibleNames());
        }
        if (this.has("parameters")) {
            ctorHandle.parameters(this.parseTypes(this.group("parameters").split(",")));
        }
        return ctorHandle;
    }
    
    public <T extends MethodMemberHandle> T parseMethod(final T methodHandle) {
        this.includeInnerClassOf(methodHandle);
        this.pattern(ReflectionParser.METHOD, methodHandle);
        methodHandle.named(this.group("methodName").split("\\$"));
        methodHandle.returns(this.parseType(this.group("methodReturnType")));
        if (this.has("parameters")) {
            methodHandle.parameters(this.parseTypes(this.group("parameters").split(",")));
        }
        return methodHandle;
    }
    
    public <T extends FieldMemberHandle> T parseField(final T fieldHandle) {
        this.includeInnerClassOf(fieldHandle);
        this.pattern(ReflectionParser.FIELD, fieldHandle);
        fieldHandle.named(this.group("fieldName").split("\\$"));
        fieldHandle.returns(this.parseType(this.group("fieldType")));
        return fieldHandle;
    }
    
    private String group(final String groupName) {
        return this.matcher.group(groupName);
    }
    
    private boolean has(final String groupName) {
        final String group = this.group(groupName);
        return group != null && !group.isEmpty();
    }
    
    private void start(final ReflectiveHandle<?> handle) {
        if (!this.matcher.matches()) {
            this.error("Not a " + (Object)handle + " declaration");
        }
        this.parseFlags();
        if (handle instanceof MemberHandle) {
            final MemberHandle memberHandle = (MemberHandle)handle;
            if (!hasOneOf((java.util.Collection<Flag>)this.flags, Flag.PUBLIC, Flag.PROTECTED, Flag.PRIVATE)) {
                final Class<?> clazz = memberHandle.getClassHandle().reflectOrNull();
                if (clazz != null && !clazz.isInterface()) {
                    memberHandle.makeAccessible();
                }
            }
            else if (hasOneOf((java.util.Collection<Flag>)this.flags, Flag.PRIVATE, Flag.PROTECTED)) {
                memberHandle.makeAccessible();
            }
            if (handle instanceof FieldMemberHandle && this.flags.contains((Object)Flag.FINAL)) {
                ((FieldMemberHandle)handle).asFinal();
            }
            if (handle instanceof FlaggedNamedMemberHandle && this.flags.contains((Object)Flag.STATIC)) {
                ((FlaggedNamedMemberHandle)handle).asStatic();
            }
        }
    }
    
    private void parseFlags() {
        if (!this.has("flags")) {
            return;
        }
        final String flagsStr = this.group("flags");
        for (final String flag : flagsStr.split("\\s+")) {
            if (!this.flags.add((Object)Flag.valueOf(flag.toUpperCase(Locale.ENGLISH)))) {
                this.error("Repeated flag: " + flag);
            }
        }
        if (containsDuplicates((java.util.Collection<Flag>)this.flags, Flag.PUBLIC, Flag.PROTECTED, Flag.PRIVATE)) {
            this.error("Duplicate visibility flags");
        }
    }
    
    @SafeVarargs
    private static <T> boolean containsDuplicates(final Collection<T> collection, final T... values) {
        boolean contained = false;
        for (final T value : values) {
            if (collection.contains((Object)value)) {
                if (contained) {
                    return true;
                }
                contained = true;
            }
        }
        return false;
    }
    
    @SafeVarargs
    private static <T> boolean hasOneOf(final Collection<T> collection, final T... elements) {
        final Stream stream = Arrays.stream((Object[])elements);
        Objects.requireNonNull((Object)collection);
        return stream.anyMatch(collection::contains);
    }
    
    private void error(final String message) {
        throw new ReflectionParserException(message + " in: " + this.declaration + " (RegEx: " + this.pattern.pattern() + "), (Imports: " + (Object)this.cachedImports + ')');
    }
    
    static {
        DEFAULT_CHECKED_PACKAGES = new String[] { "java.util", "java.util.function", "java.lang", "java.io" };
        PACKAGE_HANDLES = MinecraftPackage.values();
        THROWS = "(?:\\s*throws\\s+(?<throws>(?:" + (Object)type(null).array(false) + ")(?:\\s*,\\s*" + (Object)type(null).array(false) + ")*))?";
        CLASS = Pattern.compile("(?:package\\s+(?<package>(\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*\\.)*\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*)\\s*;\\s*)?" + Flag.FLAGS_REGEX + "(?<classType>class|interface|enum|record)" + "\\s+" + (Object)type("className") + "(?:\\(\\))?(?:\\s+extends\\s+" + (Object)id("superclasses") + ")?(?:\\s+implements\\s+(?<interfaces>(?:" + (Object)type(null).array(false) + ")(?:\\s*,\\s*" + (Object)type(null).array(false) + ")*))?(?:\\s*\\{\\s*})?\\s*");
        METHOD = Pattern.compile(Flag.FLAGS_REGEX + (Object)type("methodReturnType") + "\\s+" + (Object)id("methodName") + "\\s*\\(\\s*(?<parameters>[\\w$_,.<?>\\[\\] ]+)?\\s*\\)" + ReflectionParser.THROWS + "\\s*;?\\s*");
        CONSTRUCTOR = Pattern.compile(Flag.FLAGS_REGEX + "\\s+" + (Object)id("className") + "\\s*\\(\\s*(?<parameters>[\\w$_,.<?>\\[\\] ]+)?\\s*\\)" + "\\s*;?\\s*");
        FIELD = Pattern.compile(Flag.FLAGS_REGEX + (Object)type("fieldType") + "\\s+" + (Object)id("fieldName") + "\\s*;?\\s*");
        PREDEFINED_TYPES = (Map)new HashMap();
        Arrays.asList((Object[])new Class[] { Byte.TYPE, Short.TYPE, Integer.TYPE, Long.TYPE, Float.TYPE, Double.TYPE, Boolean.TYPE, Character.TYPE, Void.TYPE, Byte.class, Short.class, Integer.class, Long.class, Float.class, Double.class, Boolean.class, Character.class, Void.class, Object.class, String.class, CharSequence.class, StringBuilder.class, StringBuffer.class, UUID.class, Optional.class, Map.class, HashMap.class, Date.class, Calendar.class, Duration.class, TimeUnit.class, Path.class, Files.class, ConcurrentHashMap.class, Callable.class, Future.class, CompletableFuture.class, Throwable.class, Error.class, Exception.class, IllegalArgumentException.class, IllegalStateException.class }).forEach(x -> ReflectionParser.PREDEFINED_TYPES.put((Object)x.getSimpleName(), (Object)x));
    }
    
    private enum Flag
    {
        PUBLIC, 
        PROTECTED, 
        PRIVATE, 
        FINAL, 
        TRANSIENT, 
        ABSTRACT, 
        STATIC, 
        NATIVE, 
        SYNCHRONIZED, 
        STRICTFP, 
        VOLATILE;
        
        private static final String FLAGS_REGEX;
        
        static {
            FLAGS_REGEX = "(?<flags>(?:(?:" + (String)Arrays.stream((Object[])values()).map(Enum::name).map(x -> x.toLowerCase(Locale.ENGLISH)).collect(Collectors.joining((CharSequence)"|")) + ")\\s*)+)?";
        }
    }
    
    private static final class IDHandler
    {
        private boolean generic;
        private boolean array;
        private final String groupName;
        private final boolean isFullyQualified;
        
        private IDHandler(final String groupName, final boolean isFullyQualified) {
            this.groupName = groupName;
            this.isFullyQualified = isFullyQualified;
        }
        
        public IDHandler generic(final boolean generic) {
            this.generic = generic;
            return this;
        }
        
        public IDHandler array(final boolean array) {
            this.array = array;
            return this;
        }
        
        @Override
        public String toString() {
            final String type = (this.isFullyQualified ? "(\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*\\.)*\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*" : "\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*") + (this.generic ? "(?:\\s*<\\s*[.\\w<>\\[\\], ]+\\s*>)?" : "") + (this.array ? "(?:(?:\\[])*)" : "");
            if (this.groupName == null) {
                return "(?:" + type + ')';
            }
            return "(?<" + this.groupName + '>' + type + ')';
        }
    }
    
    public static final class ReflectionParserException extends RuntimeException
    {
        public ReflectionParserException(final String message) {
            super(message);
        }
    }
}
