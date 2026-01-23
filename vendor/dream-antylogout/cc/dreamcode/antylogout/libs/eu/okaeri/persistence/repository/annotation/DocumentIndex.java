package cc.dreamcode.antylogout.libs.eu.okaeri.persistence.repository.annotation;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;

@Retention(RetentionPolicy.RUNTIME)
public @interface DocumentIndex {
    String path();
    
    int maxLength();
}
