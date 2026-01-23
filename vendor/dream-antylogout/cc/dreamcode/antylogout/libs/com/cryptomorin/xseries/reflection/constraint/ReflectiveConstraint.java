package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.constraint;

import org.jetbrains.annotations.NotNull;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.ReflectiveHandle;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public interface ReflectiveConstraint
{
    @Contract(pure = true)
    String category();
    
    @Contract(pure = true)
    String name();
    
    @NotNull
    @Contract(pure = true)
    Result appliesTo(@NotNull final ReflectiveHandle<?> p0, @NotNull final Object p1);
    
    public enum Result
    {
        INCOMPATIBLE, 
        NOT_MATCHED, 
        MATCHED;
        
        @ApiStatus.Internal
        public static Result of(final boolean test) {
            return test ? Result.MATCHED : Result.NOT_MATCHED;
        }
    }
}
