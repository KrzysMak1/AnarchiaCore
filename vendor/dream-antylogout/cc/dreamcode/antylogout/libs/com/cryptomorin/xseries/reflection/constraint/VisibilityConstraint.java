package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.constraint;

import java.lang.reflect.Member;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.ReflectiveHandle;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.XAccessFlag;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public enum VisibilityConstraint implements ReflectiveConstraint
{
    PUBLIC(XAccessFlag.PUBLIC), 
    PRIVATE(XAccessFlag.PRIVATE), 
    PROTECTED(XAccessFlag.PROTECTED);
    
    private final XAccessFlag accessFlag;
    
    private VisibilityConstraint(final XAccessFlag accessFlag) {
        this.accessFlag = accessFlag;
    }
    
    @ApiStatus.Internal
    public XAccessFlag getAccessFlag() {
        return this.accessFlag;
    }
    
    public Result appliesTo(final ReflectiveHandle<?> handle, final Object jvm) {
        int mods;
        if (jvm instanceof Class) {
            mods = ((Class)jvm).getModifiers();
            if (this == VisibilityConstraint.PRIVATE) {
                return Result.INCOMPATIBLE;
            }
            if (this == VisibilityConstraint.PROTECTED) {
                return Result.of(!XAccessFlag.PUBLIC.isSet(mods));
            }
        }
        else {
            if (!(jvm instanceof Member)) {
                return Result.INCOMPATIBLE;
            }
            mods = ((Member)jvm).getModifiers();
        }
        return Result.of(this.accessFlag.isSet(mods));
    }
    
    public String category() {
        return "Visibility";
    }
    
    public String toString() {
        return this.getClass().getSimpleName() + "::" + this.name();
    }
}
