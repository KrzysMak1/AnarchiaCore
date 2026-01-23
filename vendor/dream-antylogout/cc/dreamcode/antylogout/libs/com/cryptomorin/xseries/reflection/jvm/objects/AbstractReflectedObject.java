package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm.objects;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

abstract class AbstractReflectedObject implements ReflectedObject
{
    @Override
    public abstract AnnotatedElement unreflect();
    
    public final <A extends Annotation> A getAnnotation(final Class<A> annotationClass) {
        return (A)this.unreflect().getAnnotation((Class)annotationClass);
    }
    
    public final boolean isAnnotationPresent(final Class<? extends Annotation> annotationClass) {
        return this.unreflect().isAnnotationPresent((Class)annotationClass);
    }
    
    public final <A extends Annotation> A[] getAnnotationsByType(final Class<A> annotationClass) {
        return (A[])this.unreflect().getAnnotationsByType((Class)annotationClass);
    }
    
    public final Annotation[] getAnnotations() {
        return this.unreflect().getAnnotations();
    }
    
    public final <A extends Annotation> A getDeclaredAnnotation(final Class<A> annotationClass) {
        return (A)this.unreflect().getDeclaredAnnotation((Class)annotationClass);
    }
    
    public final <A extends Annotation> A[] getDeclaredAnnotationsByType(final Class<A> annotationClass) {
        return (A[])this.unreflect().getDeclaredAnnotationsByType((Class)annotationClass);
    }
    
    public final Annotation[] getDeclaredAnnotations() {
        return this.unreflect().getDeclaredAnnotations();
    }
    
    @Override
    public final boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (obj instanceof ReflectedObject) {
            return this.unreflect().equals(((ReflectedObject)obj).unreflect());
        }
        return this.unreflect().equals(obj);
    }
    
    @Override
    public final int hashCode() {
        return this.unreflect().hashCode();
    }
    
    @Override
    public final String toString() {
        return this.getClass().getSimpleName() + '(' + (Object)this.unreflect() + ')';
    }
}
