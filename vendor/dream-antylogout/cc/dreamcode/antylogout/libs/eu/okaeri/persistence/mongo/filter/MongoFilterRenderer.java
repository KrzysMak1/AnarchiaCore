package cc.dreamcode.antylogout.libs.eu.okaeri.persistence.mongo.filter;

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
import lombok.NonNull;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.filter.condition.LogicalOperator;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.filter.renderer.StringRenderer;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.filter.renderer.DefaultFilterRenderer;

public class MongoFilterRenderer extends DefaultFilterRenderer
{
    public MongoFilterRenderer() {
        super(new MongoStringRenderer());
    }
    
    @Override
    public String renderOperator(@NonNull final LogicalOperator operator) {
        if (operator == null) {
            throw new NullPointerException("operator is marked non-null but is null");
        }
        if (operator == LogicalOperator.AND) {
            return "$and";
        }
        if (operator == LogicalOperator.OR) {
            return "$or";
        }
        throw new IllegalArgumentException("Unsupported operator: " + (Object)operator);
    }
    
    @Override
    public String renderOperator(@NonNull final Predicate predicate) {
        if (predicate == null) {
            throw new NullPointerException("predicate is marked non-null but is null");
        }
        if (predicate instanceof EqPredicate) {
            return "$eq";
        }
        if (predicate instanceof GtePredicate) {
            return "$gte";
        }
        if (predicate instanceof GtPredicate) {
            return "$gt";
        }
        if (predicate instanceof LtePredicate) {
            return "$lte";
        }
        if (predicate instanceof LtPredicate) {
            return "$lt";
        }
        if (predicate instanceof NePredicate) {
            return "$ne";
        }
        throw new IllegalArgumentException("cannot render operator " + (Object)predicate + " [" + (Object)predicate.getClass() + "]");
    }
    
    @Override
    public String renderCondition(@NonNull final Condition condition) {
        if (condition == null) {
            throw new NullPointerException("condition is marked non-null but is null");
        }
        final String operator = this.renderOperator(condition.getOperator());
        final String conditions = (String)Arrays.stream((Object[])condition.getPredicates()).map(predicate -> {
            if (predicate instanceof Condition) {
                return this.renderCondition((Condition)predicate);
            }
            return this.renderPredicate(condition.getPath(), predicate);
        }).collect(Collectors.joining((CharSequence)", "));
        return "{\"" + operator + "\": [" + conditions + "]}";
    }
    
    @Override
    public String renderPredicate(@NonNull final PersistencePath path, @NonNull final Predicate predicate) {
        if (path == null) {
            throw new NullPointerException("path is marked non-null but is null");
        }
        if (predicate == null) {
            throw new NullPointerException("predicate is marked non-null but is null");
        }
        return "{ \"" + path.toMongoPath() + "\": { \"" + this.renderOperator(predicate) + "\": " + this.renderOperand(predicate) + " }}";
    }
}
