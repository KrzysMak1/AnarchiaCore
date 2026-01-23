package cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.tlschannel.util;

import javax.net.ssl.SSLEngineResult;

public class Util
{
    public static void assertTrue(final boolean condition) {
        if (!condition) {
            throw new AssertionError();
        }
    }
    
    public static String resultToString(final SSLEngineResult result) {
        return String.format("status=%s,handshakeStatus=%s,bytesConsumed=%d,bytesConsumed=%d", new Object[] { result.getStatus(), result.getHandshakeStatus(), result.bytesProduced(), result.bytesConsumed() });
    }
    
    public static int getJavaMajorVersion() {
        return getJavaMajorVersion(System.getProperty("java.version"));
    }
    
    static int getJavaMajorVersion(final String javaVersion) {
        String version = javaVersion;
        if (version.startsWith("1.")) {
            version = version.substring(2);
        }
        final int dotPos = version.indexOf(46);
        final int dashPos = version.indexOf(45);
        return Integer.parseInt(version.substring(0, (dotPos > -1) ? dotPos : ((dashPos > -1) ? dashPos : version.length())));
    }
}
