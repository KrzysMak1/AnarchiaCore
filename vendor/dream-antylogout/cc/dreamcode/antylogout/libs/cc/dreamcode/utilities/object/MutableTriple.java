package cc.dreamcode.antylogout.libs.cc.dreamcode.utilities.object;

import lombok.Generated;
import java.util.function.Consumer;

public class MutableTriple<A, B, C>
{
    private A first;
    private B second;
    private C third;
    
    public MutableTriple<A, B, C> then(final Consumer<MutableTriple<A, B, C>> after) {
        after.accept((Object)this);
        return this;
    }
    
    public Triple<A, B, C> immutable() {
        return Triple.of(this.first, this.second, this.third);
    }
    
    @Generated
    public A getFirst() {
        return this.first;
    }
    
    @Generated
    public B getSecond() {
        return this.second;
    }
    
    @Generated
    public C getThird() {
        return this.third;
    }
    
    @Generated
    public void setFirst(final A first) {
        this.first = first;
    }
    
    @Generated
    public void setSecond(final B second) {
        this.second = second;
    }
    
    @Generated
    public void setThird(final C third) {
        this.third = third;
    }
    
    @Generated
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof MutableTriple)) {
            return false;
        }
        final MutableTriple<?, ?, ?> other = (MutableTriple<?, ?, ?>)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$first = this.getFirst();
        final Object other$first = other.getFirst();
        Label_0065: {
            if (this$first == null) {
                if (other$first == null) {
                    break Label_0065;
                }
            }
            else if (this$first.equals(other$first)) {
                break Label_0065;
            }
            return false;
        }
        final Object this$second = this.getSecond();
        final Object other$second = other.getSecond();
        Label_0102: {
            if (this$second == null) {
                if (other$second == null) {
                    break Label_0102;
                }
            }
            else if (this$second.equals(other$second)) {
                break Label_0102;
            }
            return false;
        }
        final Object this$third = this.getThird();
        final Object other$third = other.getThird();
        if (this$third == null) {
            if (other$third == null) {
                return true;
            }
        }
        else if (this$third.equals(other$third)) {
            return true;
        }
        return false;
    }
    
    @Generated
    protected boolean canEqual(final Object other) {
        return other instanceof MutableTriple;
    }
    
    @Generated
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $first = this.getFirst();
        result = result * 59 + (($first == null) ? 43 : $first.hashCode());
        final Object $second = this.getSecond();
        result = result * 59 + (($second == null) ? 43 : $second.hashCode());
        final Object $third = this.getThird();
        result = result * 59 + (($third == null) ? 43 : $third.hashCode());
        return result;
    }
    
    @Generated
    @Override
    public String toString() {
        return "MutableTriple(first=" + this.getFirst() + ", second=" + this.getSecond() + ", third=" + this.getThird() + ")";
    }
    
    @Generated
    private MutableTriple(final A first, final B second, final C third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }
    
    @Generated
    public static <A, B, C> MutableTriple<A, B, C> of(final A first, final B second, final C third) {
        return new MutableTriple<A, B, C>(first, second, third);
    }
}
