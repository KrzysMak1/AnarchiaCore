package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.constraint;

import java.lang.invoke.MethodType;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandle;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.XAccessFlag;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.ReflectiveHandle;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public enum ClassTypeConstraint implements ReflectiveConstraint
{
    INTERFACE {
        @Override
        protected boolean test(final Class<?> clazz) {
            return clazz.isInterface();
        }
    }, 
    ABSTRACT {
        @Override
        protected boolean test(final Class<?> clazz) {
            return XAccessFlag.ABSTRACT.isSet(clazz.getModifiers());
        }
    }, 
    ENUM {
        @Override
        protected boolean test(final Class<?> clazz) {
            return clazz.isEnum();
        }
    }, 
    RECORD {
        private final MethodHandle isRecord;
        
        {
            MethodHandle isRecord0;
            try {
                isRecord0 = MethodHandles.lookup().findVirtual((Class)Class.class, "isRecord", MethodType.methodType(Boolean.TYPE));
            }
            catch (final NoSuchMethodException | IllegalAccessException ex) {
                isRecord0 = null;
            }
            this.isRecord = isRecord0;
        }
        
        @Override
        protected boolean test(final Class<?> clazz) {
            try {
                return this.isRecord.invoke((Class)clazz);
            }
            catch (final Throwable e) {
                throw new IllegalStateException("Cannot use Class#isRecord", e);
            }
        }
    }, 
    ANNOTATION {
        @Override
        protected boolean test(final Class<?> clazz) {
            return clazz.isAnnotation();
        }
    };
    
    protected abstract boolean test(final Class<?> p0);
    
    public Result appliesTo(final ReflectiveHandle<?> handle, final Object jvm) {
        if (jvm instanceof Class) {
            return Result.of(this.test((Class<?>)jvm));
        }
        return Result.INCOMPATIBLE;
    }
    
    public String category() {
        return "ClassType";
    }
    
    public String toString() {
        return this.getClass().getSimpleName() + "::" + this.name();
    }
}
