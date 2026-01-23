package cc.dreamcode.antylogout.libs.eu.okaeri.persistence.document.ref;

import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.PersistenceCollection;
import java.util.Optional;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.document.DocumentPersistence;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.PersistencePath;
import java.util.logging.Logger;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.document.Document;

public abstract class Ref<T extends Document>
{
    private static final boolean DEBUG;
    private static final Logger LOGGER;
    private final PersistencePath id;
    private final PersistencePath collection;
    private Class<? extends Document> valueType;
    private T value;
    private boolean fetched;
    private DocumentPersistence persistence;
    
    public Optional<T> get() {
        return (Optional<T>)Optional.ofNullable((Object)(this.fetched ? this.value : this.fetch()));
    }
    
    public T fetch() {
        final long start = System.currentTimeMillis();
        final PersistenceCollection collection = PersistenceCollection.of(this.collection.getValue());
        this.value = (T)this.persistence.read(collection, this.id).orElse((Object)null);
        if (this.value != null) {
            this.value = this.value.into(this.valueType);
        }
        if (Ref.DEBUG) {
            final long took = System.currentTimeMillis() - start;
            Ref.LOGGER.info("Fetched document reference for " + this.collection.getValue() + " [" + this.id.getValue() + "]: " + took + " ms");
        }
        this.fetched = true;
        return this.value;
    }
    
    public PersistencePath getId() {
        return this.id;
    }
    
    public PersistencePath getCollection() {
        return this.collection;
    }
    
    public Class<? extends Document> getValueType() {
        return this.valueType;
    }
    
    public T getValue() {
        return this.value;
    }
    
    public boolean isFetched() {
        return this.fetched;
    }
    
    public DocumentPersistence getPersistence() {
        return this.persistence;
    }
    
    public void setValueType(final Class<? extends Document> valueType) {
        this.valueType = valueType;
    }
    
    public void setValue(final T value) {
        this.value = value;
    }
    
    public void setFetched(final boolean fetched) {
        this.fetched = fetched;
    }
    
    public void setPersistence(final DocumentPersistence persistence) {
        this.persistence = persistence;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Ref)) {
            return false;
        }
        final Ref<?> other = (Ref<?>)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.isFetched() != other.isFetched()) {
            return false;
        }
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        Label_0078: {
            if (this$id == null) {
                if (other$id == null) {
                    break Label_0078;
                }
            }
            else if (this$id.equals(other$id)) {
                break Label_0078;
            }
            return false;
        }
        final Object this$collection = this.getCollection();
        final Object other$collection = other.getCollection();
        Label_0115: {
            if (this$collection == null) {
                if (other$collection == null) {
                    break Label_0115;
                }
            }
            else if (this$collection.equals(other$collection)) {
                break Label_0115;
            }
            return false;
        }
        final Object this$valueType = this.getValueType();
        final Object other$valueType = other.getValueType();
        Label_0152: {
            if (this$valueType == null) {
                if (other$valueType == null) {
                    break Label_0152;
                }
            }
            else if (this$valueType.equals(other$valueType)) {
                break Label_0152;
            }
            return false;
        }
        final Object this$value = this.getValue();
        final Object other$value = other.getValue();
        Label_0189: {
            if (this$value == null) {
                if (other$value == null) {
                    break Label_0189;
                }
            }
            else if (this$value.equals(other$value)) {
                break Label_0189;
            }
            return false;
        }
        final Object this$persistence = this.getPersistence();
        final Object other$persistence = other.getPersistence();
        if (this$persistence == null) {
            if (other$persistence == null) {
                return true;
            }
        }
        else if (this$persistence.equals(other$persistence)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof Ref;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + (this.isFetched() ? 79 : 97);
        final Object $id = this.getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        final Object $collection = this.getCollection();
        result = result * 59 + (($collection == null) ? 43 : $collection.hashCode());
        final Object $valueType = this.getValueType();
        result = result * 59 + (($valueType == null) ? 43 : $valueType.hashCode());
        final Object $value = this.getValue();
        result = result * 59 + (($value == null) ? 43 : $value.hashCode());
        final Object $persistence = this.getPersistence();
        result = result * 59 + (($persistence == null) ? 43 : $persistence.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "Ref(id=" + (Object)this.getId() + ", collection=" + (Object)this.getCollection() + ", valueType=" + (Object)this.getValueType() + ", value=" + this.getValue() + ", fetched=" + this.isFetched() + ", persistence=" + (Object)this.getPersistence() + ")";
    }
    
    protected Ref(final PersistencePath id, final PersistencePath collection, final Class<? extends Document> valueType, final T value, final boolean fetched, final DocumentPersistence persistence) {
        this.id = id;
        this.collection = collection;
        this.valueType = valueType;
        this.value = value;
        this.fetched = fetched;
        this.persistence = persistence;
    }
    
    static {
        DEBUG = Boolean.parseBoolean(System.getProperty("okaeri.platform.debug", "false"));
        LOGGER = Logger.getLogger(Ref.class.getSimpleName());
    }
}
