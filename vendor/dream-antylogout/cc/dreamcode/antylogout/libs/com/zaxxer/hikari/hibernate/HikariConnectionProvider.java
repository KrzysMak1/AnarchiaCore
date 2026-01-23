package cc.dreamcode.antylogout.libs.com.zaxxer.hikari.hibernate;

import cc.dreamcode.antylogout.libs.org.slf4j.LoggerFactory;
import org.hibernate.service.UnknownUnwrapTypeException;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.Connection;
import org.hibernate.HibernateException;
import java.util.Map;
import org.hibernate.Version;
import cc.dreamcode.antylogout.libs.com.zaxxer.hikari.HikariDataSource;
import cc.dreamcode.antylogout.libs.com.zaxxer.hikari.HikariConfig;
import cc.dreamcode.antylogout.libs.org.slf4j.Logger;
import org.hibernate.service.spi.Stoppable;
import org.hibernate.service.spi.Configurable;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;

public class HikariConnectionProvider implements ConnectionProvider, Configurable, Stoppable
{
    private static final long serialVersionUID = -9131625057941275711L;
    private static final Logger LOGGER;
    private HikariConfig hcfg;
    private HikariDataSource hds;
    
    public HikariConnectionProvider() {
        this.hcfg = null;
        this.hds = null;
        if (Version.getVersionString().substring(0, 5).compareTo("4.3.6") >= 1) {
            HikariConnectionProvider.LOGGER.warn("cc.dreamcode.antylogout.libs.com.zaxxer.hikari.hibernate.HikariConnectionProvider has been deprecated for versions of Hibernate 4.3.6 and newer.  Please switch to org.hibernate.hikaricp.internal.HikariCPConnectionProvider.");
        }
    }
    
    public void configure(final Map props) throws HibernateException {
        try {
            HikariConnectionProvider.LOGGER.debug("Configuring HikariCP");
            this.hcfg = HikariConfigurationUtil.loadConfiguration(props);
            this.hds = new HikariDataSource(this.hcfg);
        }
        catch (final Exception e) {
            throw new HibernateException((Throwable)e);
        }
        HikariConnectionProvider.LOGGER.debug("HikariCP Configured");
    }
    
    public Connection getConnection() throws SQLException {
        Connection conn = null;
        if (this.hds != null) {
            conn = this.hds.getConnection();
        }
        return conn;
    }
    
    public void closeConnection(final Connection conn) throws SQLException {
        conn.close();
    }
    
    public boolean supportsAggressiveRelease() {
        return false;
    }
    
    public boolean isUnwrappableAs(final Class unwrapType) {
        return ConnectionProvider.class.equals(unwrapType) || HikariConnectionProvider.class.isAssignableFrom(unwrapType);
    }
    
    public <T> T unwrap(final Class<T> unwrapType) {
        if (ConnectionProvider.class.equals(unwrapType) || HikariConnectionProvider.class.isAssignableFrom(unwrapType)) {
            return (T)this;
        }
        if (DataSource.class.isAssignableFrom(unwrapType)) {
            return (T)this.hds;
        }
        throw new UnknownUnwrapTypeException((Class)unwrapType);
    }
    
    public void stop() {
        this.hds.close();
    }
    
    static {
        LOGGER = LoggerFactory.getLogger(HikariConnectionProvider.class);
    }
}
