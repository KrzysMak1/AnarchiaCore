package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries;

public abstract class AbstractReferencedClass<T>
{
    protected abstract T object();
    
    protected final Object toVirtual() {
        return this.object();
    }
    
    @Override
    public final boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass().isInstance(obj.getClass())) {
            return this.toVirtual().equals(((AbstractReferencedClass)obj).toVirtual());
        }
        return this.object().equals(obj);
    }
    
    @Override
    public final int hashCode() {
        return this.toVirtual().hashCode();
    }
    
    @Override
    public final String toString() {
        return this.getClass().getSimpleName() + '(' + this.toVirtual().toString() + ')';
    }
}
