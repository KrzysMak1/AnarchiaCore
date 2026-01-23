package cc.dreamcode.antylogout.libs.com.mongodb.internal.operation;

import cc.dreamcode.antylogout.libs.com.mongodb.connection.ConnectionDescription;

public final class ServerVersionHelper
{
    public static final int MIN_WIRE_VERSION = 0;
    public static final int THREE_DOT_SIX_WIRE_VERSION = 6;
    public static final int FOUR_DOT_ZERO_WIRE_VERSION = 7;
    public static final int FOUR_DOT_TWO_WIRE_VERSION = 8;
    public static final int FOUR_DOT_FOUR_WIRE_VERSION = 9;
    public static final int FIVE_DOT_ZERO_WIRE_VERSION = 12;
    public static final int SIX_DOT_ZERO_WIRE_VERSION = 17;
    private static final int SEVEN_DOT_ZERO_WIRE_VERSION = 21;
    
    public static boolean serverIsAtLeastVersionFourDotZero(final ConnectionDescription description) {
        return description.getMaxWireVersion() >= 7;
    }
    
    public static boolean serverIsAtLeastVersionFourDotTwo(final ConnectionDescription description) {
        return description.getMaxWireVersion() >= 8;
    }
    
    public static boolean serverIsAtLeastVersionFourDotFour(final ConnectionDescription description) {
        return description.getMaxWireVersion() >= 9;
    }
    
    public static boolean serverIsAtLeastVersionFiveDotZero(final ConnectionDescription description) {
        return description.getMaxWireVersion() >= 12;
    }
    
    public static boolean serverIsLessThanVersionFourDotZero(final ConnectionDescription description) {
        return description.getMaxWireVersion() < 7;
    }
    
    public static boolean serverIsLessThanVersionFourDotTwo(final ConnectionDescription description) {
        return description.getMaxWireVersion() < 8;
    }
    
    public static boolean serverIsLessThanVersionFourDotFour(final ConnectionDescription description) {
        return description.getMaxWireVersion() < 9;
    }
    
    public static boolean serverIsLessThanVersionSevenDotZero(final ConnectionDescription description) {
        return description.getMaxWireVersion() < 21;
    }
    
    private ServerVersionHelper() {
    }
}
