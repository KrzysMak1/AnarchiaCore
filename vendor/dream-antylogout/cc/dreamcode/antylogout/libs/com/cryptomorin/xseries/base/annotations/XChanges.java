package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.base.annotations;

import org.jetbrains.annotations.ApiStatus;
import java.lang.annotation.Documented;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.SOURCE)
@Documented
@ApiStatus.Internal
public @interface XChanges {
    XChange[] value();
}
