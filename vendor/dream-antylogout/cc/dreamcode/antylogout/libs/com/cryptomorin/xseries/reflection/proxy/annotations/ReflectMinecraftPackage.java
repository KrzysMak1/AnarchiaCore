package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.proxy.annotations;

import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.minecraft.MinecraftPackage;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ReflectMinecraftPackage {
    MinecraftPackage type();
    
    String packageName() default "";
    
    boolean ignoreCurrentName() default false;
}
