package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.constraint;

import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.XAccessFlag;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.ReflectiveHandle;

public class ReflectiveConstraintException extends RuntimeException
{
    private final ReflectiveConstraint constraint;
    private final ReflectiveConstraint.Result result;
    
    private ReflectiveConstraintException(final ReflectiveConstraint constraint, final ReflectiveConstraint.Result result, final String message) {
        super(message);
        this.constraint = constraint;
        this.result = result;
    }
    
    public ReflectiveConstraint getConstraint() {
        return this.constraint;
    }
    
    public ReflectiveConstraint.Result getResult() {
        return this.result;
    }
    
    public static ReflectiveConstraintException create(final ReflectiveConstraint constraint, final ReflectiveConstraint.Result result, final ReflectiveHandle<?> handle, final Object jvm) {
        String message = null;
        switch (result) {
            case MATCHED: {
                throw new IllegalArgumentException("Cannot create an exception if results are successful: " + (Object)constraint + " -> MATCHED");
            }
            case INCOMPATIBLE: {
                message = "The constraint " + (Object)constraint + " cannot be applied to " + (Object)handle;
                break;
            }
            case NOT_MATCHED: {
                message = "Found " + (Object)handle + " with JVM " + jvm + ", however it doesn't match the constraint: " + (Object)constraint + " - " + (String)XAccessFlag.getModifiers(jvm).map(XAccessFlag::toString).orElse((Object)"[NO MODIFIER]");
                break;
            }
            default: {
                throw new AssertionError((Object)("Unknown reflective constraint result: " + (Object)result));
            }
        }
        return new ReflectiveConstraintException(constraint, result, message);
    }
}
