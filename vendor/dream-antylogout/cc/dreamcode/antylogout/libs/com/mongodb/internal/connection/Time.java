package cc.dreamcode.antylogout.libs.com.mongodb.internal.connection;

public final class Time
{
    static final long CONSTANT_TIME = 42L;
    private static boolean isConstant;
    
    public static void makeTimeConstant() {
        Time.isConstant = true;
    }
    
    public static void makeTimeMove() {
        Time.isConstant = false;
    }
    
    public static long nanoTime() {
        return Time.isConstant ? 42L : System.nanoTime();
    }
    
    private Time() {
    }
}
