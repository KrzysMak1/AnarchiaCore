package cc.dreamcode.antylogout.libs.eu.okaeri.persistence.filter.predicate;

import java.math.BigDecimal;
import lombok.NonNull;

public abstract class PredicateNumeric extends SimplePredicate
{
    protected PredicateNumeric(@NonNull final Object rightOperand) {
        super(rightOperand);
        if (rightOperand == null) {
            throw new NullPointerException("rightOperand is marked non-null but is null");
        }
    }
    
    @Override
    public boolean check(@NonNull final Object leftOperand) {
        if (leftOperand == null) {
            throw new NullPointerException("leftOperand is marked non-null but is null");
        }
        if (leftOperand instanceof Number && this.getRightOperand() instanceof Number) {
            final BigDecimal left = new BigDecimal(String.valueOf(leftOperand));
            final BigDecimal right = new BigDecimal(String.valueOf(this.getRightOperand()));
            return this.results(left.compareTo(right));
        }
        if (leftOperand instanceof CharSequence && this.getRightOperand() instanceof Number) {
            final BigDecimal left = new BigDecimal(String.valueOf(leftOperand).length());
            final BigDecimal right = new BigDecimal(String.valueOf(this.getRightOperand()));
            return this.results(left.compareTo(right));
        }
        if (leftOperand instanceof Number && this.getRightOperand() instanceof CharSequence) {
            final BigDecimal left = new BigDecimal(String.valueOf(leftOperand));
            final BigDecimal right = new BigDecimal(String.valueOf(this.getRightOperand()).length());
            return this.results(left.compareTo(right));
        }
        throw new IllegalArgumentException("cannot check " + this.getRightOperand() + " against " + leftOperand);
    }
    
    public abstract boolean results(final int compareResult);
}
