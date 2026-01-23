package cc.dreamcode.antylogout.libs.eu.okaeri.persistence.filter.renderer;

import java.math.BigDecimal;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.filter.predicate.SimplePredicate;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.PersistencePath;
import java.util.stream.Collectors;
import java.util.Arrays;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.filter.condition.Condition;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.filter.predicate.NePredicate;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.filter.predicate.LtPredicate;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.filter.predicate.LtePredicate;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.filter.predicate.GtPredicate;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.filter.predicate.GtePredicate;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.filter.predicate.EqPredicate;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.filter.predicate.Predicate;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.filter.condition.LogicalOperator;
import lombok.NonNull;

public class DefaultFilterRenderer implements FilterRenderer
{
    @NonNull
    protected final StringRenderer stringRenderer;
    
    public DefaultFilterRenderer() {
        this.stringRenderer = new DefaultStringRenderer();
    }
    
    @Override
    public String renderOperator(@NonNull final LogicalOperator operator) {
        if (operator == null) {
            throw new NullPointerException("operator is marked non-null but is null");
        }
        if (operator == LogicalOperator.AND) {
            return " && ";
        }
        if (operator == LogicalOperator.OR) {
            return " || ";
        }
        throw new IllegalArgumentException("Unsupported operator: " + (Object)operator);
    }
    
    @Override
    public String renderOperator(@NonNull final Predicate predicate) {
        if (predicate == null) {
            throw new NullPointerException("predicate is marked non-null but is null");
        }
        if (predicate instanceof EqPredicate) {
            return "==";
        }
        if (predicate instanceof GtePredicate) {
            return ">=";
        }
        if (predicate instanceof GtPredicate) {
            return ">";
        }
        if (predicate instanceof LtePredicate) {
            return "<=";
        }
        if (predicate instanceof LtPredicate) {
            return "<";
        }
        if (predicate instanceof NePredicate) {
            return "!=";
        }
        throw new IllegalArgumentException("cannot render operator " + (Object)predicate + " [" + (Object)predicate.getClass() + "]");
    }
    
    @Override
    public String renderCondition(@NonNull final Condition condition) {
        if (condition == null) {
            throw new NullPointerException("condition is marked non-null but is null");
        }
        final String expression = (String)Arrays.stream((Object[])condition.getPredicates()).map(predicate -> {
            if (predicate instanceof Condition) {
                return this.renderCondition((Condition)predicate);
            }
            return this.renderPredicate(condition.getPath(), predicate);
        }).collect(Collectors.joining((CharSequence)this.renderOperator(condition.getOperator())));
        return (condition.getPredicates().length == 1) ? expression : ("(" + expression + ")");
    }
    
    @Override
    public String renderPredicate(@NonNull final PersistencePath path, @NonNull final Predicate predicate) {
        if (path == null) {
            throw new NullPointerException("path is marked non-null but is null");
        }
        if (predicate == null) {
            throw new NullPointerException("predicate is marked non-null but is null");
        }
        return "(" + path.toSqlIdentifier() + " " + this.renderOperator(predicate) + " " + this.renderOperand(predicate) + ")";
    }
    
    @Override
    public String renderOperand(@NonNull Object operand) {
        if (operand == null) {
            throw new NullPointerException("operand is marked non-null but is null");
        }
        if (operand instanceof SimplePredicate) {
            operand = ((SimplePredicate)operand).getRightOperand();
        }
        if (operand instanceof Condition) {
            return this.renderCondition((Condition)operand);
        }
        if (operand instanceof Double) {
            final double dOp = (double)operand;
            if (dOp == (int)dOp) {
                return String.valueOf((int)dOp);
            }
        }
        if (operand instanceof Float) {
            final float dOp2 = (float)operand;
            if (dOp2 == (int)dOp2) {
                return String.valueOf((int)dOp2);
            }
        }
        if (operand instanceof BigDecimal) {
            return ((BigDecimal)operand).toPlainString();
        }
        if (operand instanceof Double || operand instanceof Float) {
            return new BigDecimal(String.valueOf(operand)).toPlainString();
        }
        if (operand instanceof Number) {
            return String.valueOf(operand);
        }
        if (operand instanceof CharSequence) {
            return this.stringRenderer.render(String.valueOf(operand));
        }
        throw new IllegalArgumentException("cannot render operand " + operand + " [" + (Object)operand.getClass() + "]");
    }
    
    public DefaultFilterRenderer(@NonNull final StringRenderer stringRenderer) {
        if (stringRenderer == null) {
            throw new NullPointerException("stringRenderer is marked non-null but is null");
        }
        this.stringRenderer = stringRenderer;
    }
}
