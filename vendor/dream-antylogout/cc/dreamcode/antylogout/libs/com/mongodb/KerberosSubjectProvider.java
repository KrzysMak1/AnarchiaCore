package cc.dreamcode.antylogout.libs.com.mongodb;

import cc.dreamcode.antylogout.libs.com.mongodb.internal.diagnostics.logging.Loggers;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import javax.security.auth.kerberos.KerberosTicket;
import javax.security.auth.login.LoginContext;
import cc.dreamcode.antylogout.libs.com.mongodb.lang.NonNull;
import javax.security.auth.login.LoginException;
import java.util.concurrent.locks.Lock;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.Locks;
import cc.dreamcode.antylogout.libs.com.mongodb.assertions.Assertions;
import cc.dreamcode.antylogout.libs.com.mongodb.lang.Nullable;
import javax.security.auth.Subject;
import java.util.concurrent.locks.ReentrantLock;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.diagnostics.logging.Logger;
import cc.dreamcode.antylogout.libs.com.mongodb.annotations.ThreadSafe;

@ThreadSafe
public class KerberosSubjectProvider implements SubjectProvider
{
    private static final Logger LOGGER;
    private static final String TGT_PREFIX = "krbtgt/";
    private final ReentrantLock lock;
    private String loginContextName;
    private String fallbackLoginContextName;
    private Subject subject;
    
    public KerberosSubjectProvider() {
        this("com.sun.security.jgss.krb5.initiate", "com.sun.security.jgss.initiate");
    }
    
    public KerberosSubjectProvider(final String loginContextName) {
        this(loginContextName, null);
    }
    
    private KerberosSubjectProvider(final String loginContextName, @Nullable final String fallbackLoginContextName) {
        this.lock = new ReentrantLock();
        this.loginContextName = Assertions.notNull("loginContextName", loginContextName);
        this.fallbackLoginContextName = fallbackLoginContextName;
    }
    
    @NonNull
    @Override
    public Subject getSubject() throws LoginException {
        return Locks.checkedWithInterruptibleLock((Lock)this.lock, () -> {
            if (this.subject == null || needNewSubject(this.subject)) {
                this.subject = this.createNewSubject();
            }
            return this.subject;
        });
    }
    
    private Subject createNewSubject() throws LoginException {
        LoginContext loginContext;
        try {
            KerberosSubjectProvider.LOGGER.debug(String.format("Creating LoginContext with name '%s'", new Object[] { this.loginContextName }));
            loginContext = new LoginContext(this.loginContextName);
        }
        catch (final LoginException e) {
            if (this.fallbackLoginContextName == null) {
                throw e;
            }
            KerberosSubjectProvider.LOGGER.debug(String.format("Creating LoginContext with fallback name '%s'", new Object[] { this.fallbackLoginContextName }));
            loginContext = new LoginContext(this.fallbackLoginContextName);
            this.loginContextName = this.fallbackLoginContextName;
            this.fallbackLoginContextName = null;
        }
        loginContext.login();
        KerberosSubjectProvider.LOGGER.debug("Login successful");
        return loginContext.getSubject();
    }
    
    private static boolean needNewSubject(final Subject subject) {
        for (final KerberosTicket cur : subject.getPrivateCredentials((Class)KerberosTicket.class)) {
            if (cur.getServer().getName().startsWith("krbtgt/")) {
                if (System.currentTimeMillis() > cur.getEndTime().getTime() - TimeUnit.MILLISECONDS.convert(5L, TimeUnit.MINUTES)) {
                    KerberosSubjectProvider.LOGGER.info("The TGT is close to expiring. Time to reacquire.");
                    return true;
                }
                break;
            }
        }
        return false;
    }
    
    static {
        LOGGER = Loggers.getLogger("authenticator");
    }
}
