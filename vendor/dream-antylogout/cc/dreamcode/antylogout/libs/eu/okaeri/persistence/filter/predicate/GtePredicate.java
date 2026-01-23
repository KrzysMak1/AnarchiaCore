package cc.dreamcode.antylogout.libs.eu.okaeri.persistence.filter.predicate;

import lombok.NonNull;

public class GtePredicate extends PredicateNumeric
{
    public GtePredicate(@NonNull final Object rightOperand) {
        super(rightOperand);
        if (rightOperand == null) {
            throw new NullPointerException("rightOperand is marked non-null but is null");
        }
    }
    
    @Override
    public boolean results(final int compareResult) {
        return compareResult >= 0;
    }
}
