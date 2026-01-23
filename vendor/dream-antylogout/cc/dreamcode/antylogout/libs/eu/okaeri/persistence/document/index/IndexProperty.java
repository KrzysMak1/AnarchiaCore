package cc.dreamcode.antylogout.libs.eu.okaeri.persistence.document.index;

import lombok.NonNull;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.PersistencePath;

public class IndexProperty extends PersistencePath
{
    private int maxLength;
    
    private IndexProperty(@NonNull final String value, final int maxLength) {
        super(value);
        if (value == null) {
            throw new NullPointerException("value is marked non-null but is null");
        }
        this.maxLength = maxLength;
    }
    
    public static IndexProperty of(@NonNull final String path) {
        if (path == null) {
            throw new NullPointerException("path is marked non-null but is null");
        }
        return new IndexProperty(path, 255);
    }
    
    public static IndexProperty of(@NonNull final String path, final int maxLength) {
        if (path == null) {
            throw new NullPointerException("path is marked non-null but is null");
        }
        return of(path).maxLength(maxLength);
    }
    
    public static IndexProperty parse(@NonNull final String source) {
        if (source == null) {
            throw new NullPointerException("source is marked non-null but is null");
        }
        return of(source.replace((CharSequence)".", (CharSequence)":"));
    }
    
    @Override
    public IndexProperty sub(@NonNull final String sub) {
        if (sub == null) {
            throw new NullPointerException("sub is marked non-null but is null");
        }
        return of(super.sub(sub).getValue(), this.maxLength);
    }
    
    public IndexProperty maxLength(final int maxLength) {
        if (maxLength < 1 || maxLength > 255) {
            throw new IllegalArgumentException("max length should be between 1 and 255");
        }
        this.maxLength = maxLength;
        return this;
    }
    
    public int getMaxLength() {
        return this.maxLength;
    }
    
    @Override
    public String toString() {
        return "IndexProperty(super=" + super.toString() + ", maxLength=" + this.getMaxLength() + ")";
    }
}
