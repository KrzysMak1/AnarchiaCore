package cc.dreamcode.antylogout.libs.cc.dreamcode.utilities.builder;

import lombok.Generated;
import java.util.HashSet;
import lombok.NonNull;
import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

public class SetBuilder<T>
{
    private final Set<T> set;
    
    public static <T> SetBuilder<T> builder() {
        return new SetBuilder<T>();
    }
    
    @SafeVarargs
    public static <T> Set<T> of(final T... values) {
        return new SetBuilder<T>().addAll((java.util.Collection<? extends T>)Arrays.stream((Object[])values).collect(Collectors.toSet())).build();
    }
    
    public SetBuilder<T> add(final T t) {
        this.set.add((Object)t);
        return this;
    }
    
    public SetBuilder<T> addAll(@NonNull final Collection<? extends T> set) {
        if (set == null) {
            throw new NullPointerException("set is marked non-null but is null");
        }
        this.set.addAll((Collection)set);
        return this;
    }
    
    public Set<T> build() {
        return this.set;
    }
    
    @Generated
    public SetBuilder() {
        this.set = (Set<T>)new HashSet();
    }
}
