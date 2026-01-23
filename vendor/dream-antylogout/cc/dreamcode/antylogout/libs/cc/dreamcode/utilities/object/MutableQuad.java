package cc.dreamcode.antylogout.libs.cc.dreamcode.utilities.object;

import lombok.Generated;
import java.util.function.Consumer;

public class MutableQuad<A, B, C, D>
{
    private A first;
    private B second;
    private C third;
    private D fourth;
    
    public MutableQuad<A, B, C, D> then(final Consumer<MutableQuad<A, B, C, D>> after) {
        after.accept((Object)this);
        return this;
    }
    
    public Quad<A, B, C, D> immutable() {
        return Quad.of(this.first, this.second, this.third, this.fourth);
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
    public D getFourth() {
        return this.fourth;
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
    public void setFourth(final D fourth) {
        this.fourth = fourth;
    }
    
    @Generated
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof MutableQuad)) {
            return false;
        }
        final MutableQuad<?, ?, ?, ?> other = (MutableQuad<?, ?, ?, ?>)o;
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
        Label_0139: {
            if (this$third == null) {
                if (other$third == null) {
                    break Label_0139;
                }
            }
            else if (this$third.equals(other$third)) {
                break Label_0139;
            }
            return false;
        }
        final Object this$fourth = this.getFourth();
        final Object other$fourth = other.getFourth();
        if (this$fourth == null) {
            if (other$fourth == null) {
                return true;
            }
        }
        else if (this$fourth.equals(other$fourth)) {
            return true;
        }
        return false;
    }
    
    @Generated
    protected boolean canEqual(final Object other) {
        return other instanceof MutableQuad;
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
        final Object $fourth = this.getFourth();
        result = result * 59 + (($fourth == null) ? 43 : $fourth.hashCode());
        return result;
    }
    
    @Generated
    @Override
    public String toString() {
        return "MutableQuad(first=" + this.getFirst() + ", second=" + this.getSecond() + ", third=" + this.getThird() + ", fourth=" + this.getFourth() + ")";
    }
    
    @Generated
    private MutableQuad(final A first, final B second, final C third, final D fourth) {
        this.first = first;
        this.second = second;
        this.third = third;
        this.fourth = fourth;
    }
    
    @Generated
    public static <A, B, C, D> MutableQuad<A, B, C, D> of(final A first, final B second, final C third, final D fourth) {
        return new MutableQuad<A, B, C, D>(first, second, third, fourth);
    }
}
