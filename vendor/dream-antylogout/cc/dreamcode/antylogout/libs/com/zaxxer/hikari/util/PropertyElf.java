package cc.dreamcode.antylogout.libs.com.zaxxer.hikari.util;

import cc.dreamcode.antylogout.libs.com.zaxxer.hikari.HikariConfig;
import cc.dreamcode.antylogout.libs.org.slf4j.Logger;
import cc.dreamcode.antylogout.libs.org.slf4j.LoggerFactory;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.HashSet;
import java.util.Set;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Arrays;
import java.util.Properties;
import java.util.regex.Pattern;

public final class PropertyElf
{
    private static final Pattern GETTER_PATTERN;
    
    private PropertyElf() {
    }
    
    public static void setTargetFromProperties(final Object target, final Properties properties) {
        if (target == null || properties == null) {
            return;
        }
        final List<Method> methods = (List<Method>)Arrays.asList((Object[])target.getClass().getMethods());
        properties.forEach((key, value) -> {
            if (target instanceof HikariConfig && key.toString().startsWith("dataSource.")) {
                ((HikariConfig)target).addDataSourceProperty(key.toString().substring("dataSource.".length()), value);
            }
            else {
                setProperty(target, key.toString(), value, methods);
            }
        });
    }
    
    public static Set<String> getPropertyNames(final Class<?> targetClass) {
        final HashSet<String> set = (HashSet<String>)new HashSet();
        final Matcher matcher = PropertyElf.GETTER_PATTERN.matcher((CharSequence)"");
        final Method[] methods = targetClass.getMethods();
        for (int length = methods.length, i = 0; i < length; ++i) {
            final Method method = methods[i];
            String name = method.getName();
            if (method.getParameterTypes().length == 0 && matcher.reset((CharSequence)name).matches()) {
                name = name.replaceFirst("(get|is)", "");
                try {
                    if (targetClass.getMethod("set" + name, method.getReturnType()) != null) {
                        name = Character.toLowerCase(name.charAt(0)) + name.substring(1);
                        set.add((Object)name);
                    }
                }
                catch (final Exception ex) {}
            }
        }
        return (Set<String>)set;
    }
    
    public static Object getProperty(final String propName, final Object target) {
        try {
            final String capitalized = "get" + propName.substring(0, 1).toUpperCase(Locale.ENGLISH) + propName.substring(1);
            final Method method = target.getClass().getMethod(capitalized, (Class<?>[])new Class[0]);
            return method.invoke(target, new Object[0]);
        }
        catch (final Exception e) {
            try {
                final String capitalized2 = "is" + propName.substring(0, 1).toUpperCase(Locale.ENGLISH) + propName.substring(1);
                final Method method2 = target.getClass().getMethod(capitalized2, (Class<?>[])new Class[0]);
                return method2.invoke(target, new Object[0]);
            }
            catch (final Exception e2) {
                return null;
            }
        }
    }
    
    public static Properties copyProperties(final Properties props) {
        final Properties copy = new Properties();
        props.forEach((key, value) -> copy.setProperty(key.toString(), value.toString()));
        return copy;
    }
    
    private static void setProperty(final Object target, final String propName, final Object propValue, final List<Method> methods) {
        final Logger logger = LoggerFactory.getLogger(PropertyElf.class);
        final String methodName = "set" + propName.substring(0, 1).toUpperCase(Locale.ENGLISH) + propName.substring(1);
        Method writeMethod = (Method)methods.stream().filter(m -> m.getName().equals((Object)methodName) && m.getParameterCount() == 1).findFirst().orElse((Object)null);
        if (writeMethod == null) {
            final String methodName2 = "set" + propName.toUpperCase(Locale.ENGLISH);
            writeMethod = (Method)methods.stream().filter(m -> m.getName().equals((Object)methodName2) && m.getParameterCount() == 1).findFirst().orElse((Object)null);
        }
        if (writeMethod == null) {
            logger.error("Property {} does not exist on target {}", propName, target.getClass());
            throw new RuntimeException(String.format("Property %s does not exist on target %s", new Object[] { propName, target.getClass() }));
        }
        try {
            final Class<?> paramClass = writeMethod.getParameterTypes()[0];
            if (paramClass == Integer.TYPE) {
                writeMethod.invoke(target, new Object[] { Integer.parseInt(propValue.toString()) });
            }
            else if (paramClass == Long.TYPE) {
                writeMethod.invoke(target, new Object[] { Long.parseLong(propValue.toString()) });
            }
            else if (paramClass == Short.TYPE) {
                writeMethod.invoke(target, new Object[] { Short.parseShort(propValue.toString()) });
            }
            else if (paramClass == Boolean.TYPE || paramClass == Boolean.class) {
                writeMethod.invoke(target, new Object[] { Boolean.parseBoolean(propValue.toString()) });
            }
            else if (paramClass.isArray() && Character.TYPE.isAssignableFrom(paramClass.getComponentType())) {
                writeMethod.invoke(target, new Object[] { propValue.toString().toCharArray() });
            }
            else if (paramClass == String.class) {
                writeMethod.invoke(target, new Object[] { propValue.toString() });
            }
            else {
                try {
                    logger.debug("Try to create a new instance of \"{}\"", propValue);
                    writeMethod.invoke(target, new Object[] { Class.forName(propValue.toString()).getDeclaredConstructor((Class<?>[])new Class[0]).newInstance(new Object[0]) });
                }
                catch (final InstantiationException | ClassNotFoundException e) {
                    logger.debug("Class \"{}\" not found or could not instantiate it (Default constructor)", propValue);
                    writeMethod.invoke(target, new Object[] { propValue });
                }
            }
        }
        catch (final Exception e2) {
            logger.error("Failed to set property {} on target {}", propName, target.getClass(), e2);
            throw new RuntimeException((Throwable)e2);
        }
    }
    
    static {
        GETTER_PATTERN = Pattern.compile("(get|is)[A-Z].+");
    }
}
