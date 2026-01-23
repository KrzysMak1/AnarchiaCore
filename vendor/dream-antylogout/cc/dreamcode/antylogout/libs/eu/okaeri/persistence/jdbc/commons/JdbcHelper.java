package cc.dreamcode.antylogout.libs.eu.okaeri.persistence.jdbc.commons;

import cc.dreamcode.antylogout.libs.com.zaxxer.hikari.HikariConfig;
import lombok.NonNull;

public final class JdbcHelper
{
    public static void initDriverQuietly(@NonNull final String clazz) {
        if (clazz == null) {
            throw new NullPointerException("clazz is marked non-null but is null");
        }
        try {
            Class.forName(clazz);
        }
        catch (final ClassNotFoundException ex) {}
    }
    
    public static HikariConfig configureHikari(@NonNull final String jdbcUrl) {
        if (jdbcUrl == null) {
            throw new NullPointerException("jdbcUrl is marked non-null but is null");
        }
        final HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        return config;
    }
    
    public static HikariConfig configureHikari(@NonNull final String jdbcUrl, @NonNull final String driverClazz) {
        if (jdbcUrl == null) {
            throw new NullPointerException("jdbcUrl is marked non-null but is null");
        }
        if (driverClazz == null) {
            throw new NullPointerException("driverClazz is marked non-null but is null");
        }
        initDriverQuietly(driverClazz);
        return configureHikari(jdbcUrl);
    }
}
