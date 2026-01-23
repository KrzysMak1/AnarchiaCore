package cc.dreamcode.antylogout.libs.eu.okaeri.persistence.filter.predicate;

import lombok.NonNull;

public abstract class SimplePredicate implements Predicate
{
    private final Object rightOperand;
    
    public static SimplePredicate eq(final double rightOperand) {
        return new EqPredicate(rightOperand);
    }
    
    public static SimplePredicate eq(@NonNull final CharSequence rightOperand) {
        if (rightOperand == null) {
            throw new NullPointerException("rightOperand is marked non-null but is null");
        }
        return new EqPredicate(rightOperand);
    }
    
    public static SimplePredicate gte(final double rightOperand) {
        return new GtePredicate(rightOperand);
    }
    
    public static SimplePredicate gt(final double rightOperand) {
        return new GtPredicate(rightOperand);
    }
    
    public static SimplePredicate lte(final double rightOperand) {
        return new LtePredicate(rightOperand);
    }
    
    public static SimplePredicate lt(final double rightOperand) {
        return new LtPredicate(rightOperand);
    }
    
    public static SimplePredicate ne(final double rightOperand) {
        return new NePredicate(rightOperand);
    }
    
    public static SimplePredicate ne(@NonNull final CharSequence rightOperand) {
        if (rightOperand == null) {
            throw new NullPointerException("rightOperand is marked non-null but is null");
        }
        return new NePredicate(rightOperand);
    }
    
    public Object getRightOperand() {
        return this.rightOperand;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SimplePredicate)) {
            return false;
        }
        final SimplePredicate other = (SimplePredicate)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$rightOperand = this.getRightOperand();
        final Object other$rightOperand = other.getRightOperand();
        if (this$rightOperand == null) {
            if (other$rightOperand == null) {
                return true;
            }
        }
        else if (this$rightOperand.equals(other$rightOperand)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SimplePredicate;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $rightOperand = this.getRightOperand();
        result = result * 59 + (($rightOperand == null) ? 43 : $rightOperand.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "SimplePredicate(rightOperand=" + this.getRightOperand() + ")";
    }
    
    protected SimplePredicate(final Object rightOperand) {
        this.rightOperand = rightOperand;
    }
}
