package cc.dreamcode.antylogout.libs.eu.okaeri.configs.schema;

import lombok.Generated;

public class GenericsPair<L, R>
{
    private GenericsDeclaration from;
    private GenericsDeclaration to;
    
    public GenericsPair<R, L> reverse() {
        return (GenericsPair<R, L>)new GenericsPair(this.to, this.from);
    }
    
    @Generated
    public GenericsDeclaration getFrom() {
        return this.from;
    }
    
    @Generated
    public GenericsDeclaration getTo() {
        return this.to;
    }
    
    @Generated
    public void setFrom(final GenericsDeclaration from) {
        this.from = from;
    }
    
    @Generated
    public void setTo(final GenericsDeclaration to) {
        this.to = to;
    }
    
    @Generated
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof GenericsPair)) {
            return false;
        }
        final GenericsPair<?, ?> other = (GenericsPair<?, ?>)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$from = this.getFrom();
        final Object other$from = other.getFrom();
        Label_0065: {
            if (this$from == null) {
                if (other$from == null) {
                    break Label_0065;
                }
            }
            else if (this$from.equals(other$from)) {
                break Label_0065;
            }
            return false;
        }
        final Object this$to = this.getTo();
        final Object other$to = other.getTo();
        if (this$to == null) {
            if (other$to == null) {
                return true;
            }
        }
        else if (this$to.equals(other$to)) {
            return true;
        }
        return false;
    }
    
    @Generated
    protected boolean canEqual(final Object other) {
        return other instanceof GenericsPair;
    }
    
    @Generated
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $from = this.getFrom();
        result = result * 59 + (($from == null) ? 43 : $from.hashCode());
        final Object $to = this.getTo();
        result = result * 59 + (($to == null) ? 43 : $to.hashCode());
        return result;
    }
    
    @Generated
    @Override
    public String toString() {
        return "GenericsPair(from=" + (Object)this.getFrom() + ", to=" + (Object)this.getTo() + ")";
    }
    
    @Generated
    public GenericsPair(final GenericsDeclaration from, final GenericsDeclaration to) {
        this.from = from;
        this.to = to;
    }
}
