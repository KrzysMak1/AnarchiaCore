package cc.dreamcode.antylogout.libs.com.zaxxer.hikari.util;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.Iterator;
import javassist.ClassMap;
import javassist.CtNewMethod;
import java.util.HashSet;
import javassist.Modifier;
import java.io.IOException;
import javassist.CannotCompileException;
import javassist.NotFoundException;
import javassist.CtMethod;
import javassist.CtClass;
import cc.dreamcode.antylogout.libs.com.zaxxer.hikari.pool.ProxyCallableStatement;
import java.sql.CallableStatement;
import cc.dreamcode.antylogout.libs.com.zaxxer.hikari.pool.ProxyPreparedStatement;
import java.sql.PreparedStatement;
import cc.dreamcode.antylogout.libs.com.zaxxer.hikari.pool.ProxyDatabaseMetaData;
import java.sql.DatabaseMetaData;
import cc.dreamcode.antylogout.libs.com.zaxxer.hikari.pool.ProxyResultSet;
import java.sql.ResultSet;
import cc.dreamcode.antylogout.libs.com.zaxxer.hikari.pool.ProxyStatement;
import java.sql.Statement;
import cc.dreamcode.antylogout.libs.com.zaxxer.hikari.pool.ProxyConnection;
import java.sql.Connection;
import javassist.ClassPath;
import javassist.LoaderClassPath;
import javassist.ClassPool;

public final class JavassistProxyFactory
{
    private static ClassPool classPool;
    private static String genDirectory;
    
    public static void main(final String... args) throws Exception {
        (JavassistProxyFactory.classPool = new ClassPool()).importPackage("java.sql");
        JavassistProxyFactory.classPool.appendClassPath((ClassPath)new LoaderClassPath(JavassistProxyFactory.class.getClassLoader()));
        if (args.length > 0) {
            JavassistProxyFactory.genDirectory = args[0];
        }
        String methodBody = "{ try { return delegate.method($$); } catch (SQLException e) { throw checkException(e); } }";
        generateProxyClass(Connection.class, ProxyConnection.class.getName(), methodBody);
        generateProxyClass(Statement.class, ProxyStatement.class.getName(), methodBody);
        generateProxyClass(ResultSet.class, ProxyResultSet.class.getName(), methodBody);
        generateProxyClass(DatabaseMetaData.class, ProxyDatabaseMetaData.class.getName(), methodBody);
        methodBody = "{ try { return ((cast) delegate).method($$); } catch (SQLException e) { throw checkException(e); } }";
        generateProxyClass(PreparedStatement.class, ProxyPreparedStatement.class.getName(), methodBody);
        generateProxyClass(CallableStatement.class, ProxyCallableStatement.class.getName(), methodBody);
        modifyProxyFactory();
    }
    
    private static void modifyProxyFactory() throws NotFoundException, CannotCompileException, IOException {
        System.out.println("Generating method bodies for com.zaxxer.hikari.proxy.ProxyFactory");
        final String packageName = ProxyConnection.class.getPackage().getName();
        final CtClass proxyCt = JavassistProxyFactory.classPool.getCtClass("cc.dreamcode.antylogout.libs.com.zaxxer.hikari.pool.ProxyFactory");
        final CtMethod[] methods = proxyCt.getMethods();
        for (int length = methods.length, i = 0; i < length; ++i) {
            final CtMethod method = methods[i];
            final String name = method.getName();
            int n = -1;
            switch (name.hashCode()) {
                case 2011710902: {
                    if (name.equals((Object)"getProxyConnection")) {
                        n = 0;
                        break;
                    }
                    break;
                }
                case -1995233385: {
                    if (name.equals((Object)"getProxyStatement")) {
                        n = 1;
                        break;
                    }
                    break;
                }
                case 2145615834: {
                    if (name.equals((Object)"getProxyPreparedStatement")) {
                        n = 2;
                        break;
                    }
                    break;
                }
                case -443793985: {
                    if (name.equals((Object)"getProxyCallableStatement")) {
                        n = 3;
                        break;
                    }
                    break;
                }
                case -1729648339: {
                    if (name.equals((Object)"getProxyResultSet")) {
                        n = 4;
                        break;
                    }
                    break;
                }
                case 1457258178: {
                    if (name.equals((Object)"getProxyDatabaseMetaData")) {
                        n = 5;
                        break;
                    }
                    break;
                }
            }
            switch (n) {
                case 0: {
                    method.setBody("{return new " + packageName + ".HikariProxyConnection($$);}");
                    break;
                }
                case 1: {
                    method.setBody("{return new " + packageName + ".HikariProxyStatement($$);}");
                    break;
                }
                case 2: {
                    method.setBody("{return new " + packageName + ".HikariProxyPreparedStatement($$);}");
                    break;
                }
                case 3: {
                    method.setBody("{return new " + packageName + ".HikariProxyCallableStatement($$);}");
                    break;
                }
                case 4: {
                    method.setBody("{return new " + packageName + ".HikariProxyResultSet($$);}");
                    break;
                }
                case 5: {
                    method.setBody("{return new " + packageName + ".HikariProxyDatabaseMetaData($$);}");
                    break;
                }
            }
        }
        proxyCt.writeFile(JavassistProxyFactory.genDirectory + "target/classes");
    }
    
    private static <T> void generateProxyClass(final Class<T> primaryInterface, final String superClassName, final String methodBody) throws Exception {
        final String newClassName = superClassName.replaceAll("(.+)\\.(\\w+)", "$1.Hikari$2");
        final CtClass superCt = JavassistProxyFactory.classPool.getCtClass(superClassName);
        final CtClass targetCt = JavassistProxyFactory.classPool.makeClass(newClassName, superCt);
        targetCt.setModifiers(Modifier.setPublic(16));
        System.out.println("Generating " + newClassName);
        final HashSet<String> superSigs = (HashSet<String>)new HashSet();
        final CtMethod[] methods2 = superCt.getMethods();
        for (int length = methods2.length, i = 0; i < length; ++i) {
            final CtMethod method = methods2[i];
            if ((method.getModifiers() & 0x10) == 0x10) {
                superSigs.add(method.getName() + method.getSignature());
            }
        }
        final HashSet<String> methods = (HashSet<String>)new HashSet();
        for (Class<?> intf : getAllInterfaces(primaryInterface)) {
            final CtClass intfCt = JavassistProxyFactory.classPool.getCtClass(intf.getName());
            targetCt.addInterface(intfCt);
            final CtMethod[] declaredMethods = intfCt.getDeclaredMethods();
            for (int length2 = declaredMethods.length, j = 0; j < length2; ++j) {
                final CtMethod intfMethod = declaredMethods[j];
                final String signature = intfMethod.getName() + intfMethod.getSignature();
                if (!superSigs.contains((Object)signature)) {
                    if (!methods.contains((Object)signature)) {
                        methods.add((Object)signature);
                        final CtMethod method2 = CtNewMethod.copy(intfMethod, targetCt, (ClassMap)null);
                        String modifiedBody = methodBody;
                        final CtMethod superMethod = superCt.getMethod(intfMethod.getName(), intfMethod.getSignature());
                        if ((superMethod.getModifiers() & 0x400) != 0x400 && !isDefaultMethod(intf, intfMethod)) {
                            modifiedBody = modifiedBody.replace((CharSequence)"((cast) ", (CharSequence)"");
                            modifiedBody = modifiedBody.replace((CharSequence)"delegate", (CharSequence)"super");
                            modifiedBody = modifiedBody.replace((CharSequence)"super)", (CharSequence)"super");
                        }
                        modifiedBody = modifiedBody.replace((CharSequence)"cast", (CharSequence)primaryInterface.getName());
                        if (isThrowsSqlException(intfMethod)) {
                            modifiedBody = modifiedBody.replace((CharSequence)"method", (CharSequence)method2.getName());
                        }
                        else {
                            modifiedBody = "{ return ((cast) delegate).method($$); }".replace((CharSequence)"method", (CharSequence)method2.getName()).replace((CharSequence)"cast", (CharSequence)primaryInterface.getName());
                        }
                        if (method2.getReturnType() == CtClass.voidType) {
                            modifiedBody = modifiedBody.replace((CharSequence)"return", (CharSequence)"");
                        }
                        method2.setBody(modifiedBody);
                        targetCt.addMethod(method2);
                    }
                }
            }
        }
        targetCt.getClassFile().setMajorVersion(52);
        targetCt.writeFile(JavassistProxyFactory.genDirectory + "target/classes");
    }
    
    private static boolean isThrowsSqlException(final CtMethod method) {
        try {
            for (final CtClass clazz : method.getExceptionTypes()) {
                if (clazz.getSimpleName().equals((Object)"SQLException")) {
                    return true;
                }
            }
        }
        catch (final NotFoundException ex) {}
        return false;
    }
    
    private static boolean isDefaultMethod(final Class<?> intf, final CtMethod intfMethod) throws Exception {
        final ArrayList<Class<?>> paramTypes = (ArrayList<Class<?>>)new ArrayList();
        for (final CtClass pt : intfMethod.getParameterTypes()) {
            paramTypes.add((Object)toJavaClass(pt));
        }
        return intf.getDeclaredMethod(intfMethod.getName(), (Class<?>[])paramTypes.toArray((Object[])new Class[0])).toString().contains((CharSequence)"default ");
    }
    
    private static Set<Class<?>> getAllInterfaces(final Class<?> clazz) {
        final LinkedHashSet<Class<?>> interfaces = (LinkedHashSet<Class<?>>)new LinkedHashSet();
        for (final Class<?> intf : clazz.getInterfaces()) {
            if (intf.getInterfaces().length > 0) {
                interfaces.addAll((Collection)getAllInterfaces(intf));
            }
            interfaces.add((Object)intf);
        }
        if (clazz.getSuperclass() != null) {
            interfaces.addAll((Collection)getAllInterfaces(clazz.getSuperclass()));
        }
        if (clazz.isInterface()) {
            interfaces.add((Object)clazz);
        }
        return (Set<Class<?>>)interfaces;
    }
    
    private static Class<?> toJavaClass(final CtClass cls) throws Exception {
        if (cls.getName().endsWith("[]")) {
            return Array.newInstance((Class)toJavaClass(cls.getName().replace((CharSequence)"[]", (CharSequence)"")), 0).getClass();
        }
        return toJavaClass(cls.getName());
    }
    
    private static Class<?> toJavaClass(final String cn) throws Exception {
        int n = -1;
        switch (cn.hashCode()) {
            case 104431: {
                if (cn.equals((Object)"int")) {
                    n = 0;
                    break;
                }
                break;
            }
            case 3327612: {
                if (cn.equals((Object)"long")) {
                    n = 1;
                    break;
                }
                break;
            }
            case 109413500: {
                if (cn.equals((Object)"short")) {
                    n = 2;
                    break;
                }
                break;
            }
            case 3039496: {
                if (cn.equals((Object)"byte")) {
                    n = 3;
                    break;
                }
                break;
            }
            case 97526364: {
                if (cn.equals((Object)"float")) {
                    n = 4;
                    break;
                }
                break;
            }
            case -1325958191: {
                if (cn.equals((Object)"double")) {
                    n = 5;
                    break;
                }
                break;
            }
            case 64711720: {
                if (cn.equals((Object)"boolean")) {
                    n = 6;
                    break;
                }
                break;
            }
            case 3052374: {
                if (cn.equals((Object)"char")) {
                    n = 7;
                    break;
                }
                break;
            }
            case 3625364: {
                if (cn.equals((Object)"void")) {
                    n = 8;
                    break;
                }
                break;
            }
        }
        switch (n) {
            case 0: {
                return Integer.TYPE;
            }
            case 1: {
                return Long.TYPE;
            }
            case 2: {
                return Short.TYPE;
            }
            case 3: {
                return Byte.TYPE;
            }
            case 4: {
                return Float.TYPE;
            }
            case 5: {
                return Double.TYPE;
            }
            case 6: {
                return Boolean.TYPE;
            }
            case 7: {
                return Character.TYPE;
            }
            case 8: {
                return Void.TYPE;
            }
            default: {
                return Class.forName(cn);
            }
        }
    }
    
    static {
        JavassistProxyFactory.genDirectory = "";
    }
}
