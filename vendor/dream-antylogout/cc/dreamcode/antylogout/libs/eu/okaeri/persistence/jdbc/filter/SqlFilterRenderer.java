package cc.dreamcode.antylogout.libs.eu.okaeri.persistence.jdbc.filter;

import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.filter.predicate.EqPredicate;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.filter.predicate.Predicate;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.filter.condition.LogicalOperator;
import lombok.NonNull;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.filter.renderer.StringRenderer;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.filter.renderer.DefaultFilterRenderer;

public class SqlFilterRenderer extends DefaultFilterRenderer
{
    public SqlFilterRenderer(@NonNull final StringRenderer stringRenderer) {
        super(stringRenderer);
        if (stringRenderer == null) {
            throw new NullPointerException("stringRenderer is marked non-null but is null");
        }
    }
    
    @Override
    public String renderOperator(@NonNull final LogicalOperator operator) {
        if (operator == null) {
            throw new NullPointerException("operator is marked non-null but is null");
        }
        if (operator == LogicalOperator.AND) {
            return " and ";
        }
        if (operator == LogicalOperator.OR) {
            return " or ";
        }
        throw new IllegalArgumentException("Unsupported operator: " + (Object)operator);
    }
    
    @Override
    public String renderOperator(@NonNull final Predicate predicate) {
        if (predicate == null) {
            throw new NullPointerException("predicate is marked non-null but is null");
        }
        if (predicate instanceof EqPredicate) {
            return "=";
        }
        return super.renderOperator(predicate);
    }
}
