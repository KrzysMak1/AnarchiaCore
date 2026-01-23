package cc.dreamcode.antylogout.libs.eu.okaeri.configs.annotation;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Deprecated
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Names {
    NameStrategy strategy();
    
    NameModifier modifier() default NameModifier.NONE;
}
