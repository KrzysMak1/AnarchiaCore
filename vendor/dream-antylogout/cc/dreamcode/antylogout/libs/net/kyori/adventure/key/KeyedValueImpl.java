package cc.dreamcode.antylogout.libs.net.kyori.adventure.key;

import cc.dreamcode.antylogout.libs.net.kyori.examination.Examiner;
import cc.dreamcode.antylogout.libs.net.kyori.examination.string.StringExaminer;
import cc.dreamcode.antylogout.libs.net.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import cc.dreamcode.antylogout.libs.net.kyori.examination.Examinable;

final class KeyedValueImpl<T> implements Examinable, KeyedValue<T>
{
    private final Key key;
    private final T value;
    
    KeyedValueImpl(final Key key, final T value) {
        this.key = key;
        this.value = value;
    }
    
    @NotNull
    @Override
    public Key key() {
        return this.key;
    }
    
    @NotNull
    @Override
    public T value() {
        return this.value;
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || this.getClass() != other.getClass()) {
            return false;
        }
        final KeyedValueImpl<?> that = (KeyedValueImpl<?>)other;
        return this.key.equals(that.key) && this.value.equals(that.value);
    }
    
    @Override
    public int hashCode() {
        int result = this.key.hashCode();
        result = 31 * result + this.value.hashCode();
        return result;
    }
    
    @NotNull
    @Override
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return (Stream<? extends ExaminableProperty>)Stream.of((Object[])new ExaminableProperty[] { ExaminableProperty.of("key", this.key), ExaminableProperty.of("value", this.value) });
    }
    
    @Override
    public String toString() {
        return this.examine((Examiner<String>)StringExaminer.simpleEscaping());
    }
}
