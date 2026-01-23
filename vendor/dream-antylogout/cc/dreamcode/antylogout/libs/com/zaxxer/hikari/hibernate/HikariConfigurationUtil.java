package cc.dreamcode.antylogout.libs.com.zaxxer.hikari.hibernate;

import java.util.Iterator;
import java.util.Properties;
import cc.dreamcode.antylogout.libs.com.zaxxer.hikari.HikariConfig;
import java.util.Map;

public class HikariConfigurationUtil
{
    public static final String CONFIG_PREFIX = "hibernate.hikari.";
    public static final String CONFIG_PREFIX_DATASOURCE = "hibernate.hikari.dataSource.";
    
    public static HikariConfig loadConfiguration(final Map props) {
        final Properties hikariProps = new Properties();
        copyProperty("hibernate.connection.isolation", props, "transactionIsolation", hikariProps);
        copyProperty("hibernate.connection.autocommit", props, "autoCommit", hikariProps);
        copyProperty("hibernate.connection.driver_class", props, "driverClassName", hikariProps);
        copyProperty("hibernate.connection.url", props, "jdbcUrl", hikariProps);
        copyProperty("hibernate.connection.username", props, "username", hikariProps);
        copyProperty("hibernate.connection.password", props, "password", hikariProps);
        for (final Object keyo : props.keySet()) {
            final String key = (String)keyo;
            if (key.startsWith("hibernate.hikari.")) {
                hikariProps.setProperty(key.substring("hibernate.hikari.".length()), (String)props.get((Object)key));
            }
        }
        return new HikariConfig(hikariProps);
    }
    
    private static void copyProperty(final String srcKey, final Map src, final String dstKey, final Properties dst) {
        if (src.containsKey((Object)srcKey)) {
            dst.setProperty(dstKey, (String)src.get((Object)srcKey));
        }
    }
}
