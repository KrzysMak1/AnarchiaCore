package cc.dreamcode.antylogout.libs.com.zaxxer.hikari.util;

import cc.dreamcode.antylogout.libs.org.slf4j.LoggerFactory;
import java.sql.SQLFeatureNotSupportedException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.Enumeration;
import java.util.Iterator;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.util.Map;
import java.sql.Driver;
import java.util.Properties;
import cc.dreamcode.antylogout.libs.org.slf4j.Logger;
import javax.sql.DataSource;

public final class DriverDataSource implements DataSource
{
    private static final Logger LOGGER;
    private static final String PASSWORD = "password";
    private static final String USER = "user";
    private final String jdbcUrl;
    private final Properties driverProperties;
    private Driver driver;
    
    public DriverDataSource(final String jdbcUrl, final String driverClassName, final Properties properties, final String username, final String password) {
        this.jdbcUrl = jdbcUrl;
        this.driverProperties = new Properties();
        for (final Map.Entry<Object, Object> entry : properties.entrySet()) {
            this.driverProperties.setProperty(entry.getKey().toString(), entry.getValue().toString());
        }
        if (username != null) {
            this.driverProperties.put((Object)"user", (Object)this.driverProperties.getProperty("user", username));
        }
        if (password != null) {
            this.driverProperties.put((Object)"password", (Object)this.driverProperties.getProperty("password", password));
        }
        if (driverClassName != null) {
            final Enumeration<Driver> drivers = (Enumeration<Driver>)DriverManager.getDrivers();
            while (drivers.hasMoreElements()) {
                final Driver d = (Driver)drivers.nextElement();
                if (d.getClass().getName().equals((Object)driverClassName)) {
                    this.driver = d;
                    break;
                }
            }
            if (this.driver == null) {
                DriverDataSource.LOGGER.warn("Registered driver with driverClassName={} was not found, trying direct instantiation.", driverClassName);
                Class<?> driverClass = null;
                final ClassLoader threadContextClassLoader = Thread.currentThread().getContextClassLoader();
                try {
                    if (threadContextClassLoader != null) {
                        try {
                            driverClass = threadContextClassLoader.loadClass(driverClassName);
                            DriverDataSource.LOGGER.debug("Driver class {} found in Thread context class loader {}", driverClassName, threadContextClassLoader);
                        }
                        catch (final ClassNotFoundException e) {
                            DriverDataSource.LOGGER.debug("Driver class {} not found in Thread context class loader {}, trying classloader {}", driverClassName, threadContextClassLoader, this.getClass().getClassLoader());
                        }
                    }
                    if (driverClass == null) {
                        driverClass = this.getClass().getClassLoader().loadClass(driverClassName);
                        DriverDataSource.LOGGER.debug("Driver class {} found in the HikariConfig class classloader {}", driverClassName, this.getClass().getClassLoader());
                    }
                }
                catch (final ClassNotFoundException e) {
                    DriverDataSource.LOGGER.debug("Failed to load driver class {} from HikariConfig class classloader {}", driverClassName, this.getClass().getClassLoader());
                }
                if (driverClass != null) {
                    try {
                        this.driver = (Driver)driverClass.getDeclaredConstructor((Class<?>[])new Class[0]).newInstance(new Object[0]);
                    }
                    catch (final Exception e2) {
                        DriverDataSource.LOGGER.warn("Failed to create instance of driver class {}, trying jdbcUrl resolution", driverClassName, e2);
                    }
                }
            }
        }
        final String sanitizedUrl = jdbcUrl.replaceAll("([?&;][^&#;=]*[pP]assword=)[^&#;]*", "$1<masked>");
        try {
            if (this.driver == null) {
                this.driver = DriverManager.getDriver(jdbcUrl);
                DriverDataSource.LOGGER.debug("Loaded driver with class name {} for jdbcUrl={}", this.driver.getClass().getName(), sanitizedUrl);
            }
            else if (!this.driver.acceptsURL(jdbcUrl)) {
                throw new RuntimeException("Driver " + driverClassName + " claims to not accept jdbcUrl, " + sanitizedUrl);
            }
        }
        catch (final SQLException e3) {
            throw new RuntimeException("Failed to get driver instance for jdbcUrl=" + sanitizedUrl, (Throwable)e3);
        }
    }
    
    public Connection getConnection() throws SQLException {
        return this.driver.connect(this.jdbcUrl, this.driverProperties);
    }
    
    public Connection getConnection(final String username, final String password) throws SQLException {
        final Properties cloned = (Properties)this.driverProperties.clone();
        if (username != null) {
            cloned.put((Object)"user", (Object)username);
            if (cloned.containsKey((Object)"username")) {
                cloned.put((Object)"username", (Object)username);
            }
        }
        if (password != null) {
            cloned.put((Object)"password", (Object)password);
        }
        return this.driver.connect(this.jdbcUrl, cloned);
    }
    
    public PrintWriter getLogWriter() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }
    
    public void setLogWriter(final PrintWriter logWriter) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }
    
    public void setLoginTimeout(final int seconds) throws SQLException {
        DriverManager.setLoginTimeout(seconds);
    }
    
    public int getLoginTimeout() throws SQLException {
        return DriverManager.getLoginTimeout();
    }
    
    public java.util.logging.Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return this.driver.getParentLogger();
    }
    
    public <T> T unwrap(final Class<T> iface) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }
    
    public boolean isWrapperFor(final Class<?> iface) throws SQLException {
        return false;
    }
    
    static {
        LOGGER = LoggerFactory.getLogger(DriverDataSource.class);
    }
}
