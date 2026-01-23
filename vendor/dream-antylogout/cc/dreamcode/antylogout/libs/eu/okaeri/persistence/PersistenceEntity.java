package cc.dreamcode.antylogout.libs.eu.okaeri.persistence;

import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.document.Document;
import lombok.NonNull;

public class PersistenceEntity<V>
{
    private PersistencePath path;
    private V value;
    
    public <T> PersistenceEntity<T> into(@NonNull final T value) {
        if (value == null) {
            throw new NullPointerException("value is marked non-null but is null");
        }
        this.value = (V)value;
        return (PersistenceEntity<T>)this;
    }
    
    public <T extends Document> PersistenceEntity<T> into(@NonNull final Class<T> configClazz) {
        if (configClazz == null) {
            throw new NullPointerException("configClazz is marked non-null but is null");
        }
        return this.into((T)((Document)this.value).into((Class<T>)configClazz));
    }
    
    public PersistencePath getPath() {
        return this.path;
    }
    
    public V getValue() {
        return this.value;
    }
    
    public void setPath(final PersistencePath path) {
        this.path = path;
    }
    
    public void setValue(final V value) {
        this.value = value;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PersistenceEntity)) {
            return false;
        }
        final PersistenceEntity<?> other = (PersistenceEntity<?>)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$path = this.getPath();
        final Object other$path = other.getPath();
        Label_0065: {
            if (this$path == null) {
                if (other$path == null) {
                    break Label_0065;
                }
            }
            else if (this$path.equals(other$path)) {
                break Label_0065;
            }
            return false;
        }
        final Object this$value = this.getValue();
        final Object other$value = other.getValue();
        if (this$value == null) {
            if (other$value == null) {
                return true;
            }
        }
        else if (this$value.equals(other$value)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof PersistenceEntity;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $path = this.getPath();
        result = result * 59 + (($path == null) ? 43 : $path.hashCode());
        final Object $value = this.getValue();
        result = result * 59 + (($value == null) ? 43 : $value.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "PersistenceEntity(path=" + (Object)this.getPath() + ", value=" + this.getValue() + ")";
    }
    
    public PersistenceEntity(final PersistencePath path, final V value) {
        this.path = path;
        this.value = value;
    }
}
