package cc.dreamcode.antylogout.libs.eu.okaeri.persistence.filter.condition;

import java.util.Arrays;
import lombok.NonNull;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.PersistencePath;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.filter.predicate.Predicate;

public class Condition implements Predicate
{
    private final LogicalOperator operator;
    private final PersistencePath path;
    private final Predicate[] predicates;
    
    public static Condition and(@NonNull final Predicate... predicates) {
        if (predicates == null) {
            throw new NullPointerException("predicates is marked non-null but is null");
        }
        return new Condition(LogicalOperator.AND, null, predicates);
    }
    
    public static Condition on(@NonNull final String path, @NonNull final Predicate... predicates) {
        if (path == null) {
            throw new NullPointerException("path is marked non-null but is null");
        }
        if (predicates == null) {
            throw new NullPointerException("predicates is marked non-null but is null");
        }
        return and(path, predicates);
    }
    
    public static Condition and(@NonNull final String path, @NonNull final Predicate... predicates) {
        if (path == null) {
            throw new NullPointerException("path is marked non-null but is null");
        }
        if (predicates == null) {
            throw new NullPointerException("predicates is marked non-null but is null");
        }
        return and(PersistencePath.of(path), predicates);
    }
    
    public static Condition and(@NonNull final PersistencePath path, @NonNull final Predicate... predicates) {
        if (path == null) {
            throw new NullPointerException("path is marked non-null but is null");
        }
        if (predicates == null) {
            throw new NullPointerException("predicates is marked non-null but is null");
        }
        if (predicates.length <= 0) {
            throw new IllegalArgumentException("one or more predicate is required");
        }
        return new Condition(LogicalOperator.AND, path, predicates);
    }
    
    public static Condition or(@NonNull final Predicate... predicates) {
        if (predicates == null) {
            throw new NullPointerException("predicates is marked non-null but is null");
        }
        return new Condition(LogicalOperator.OR, null, predicates);
    }
    
    public static Condition or(@NonNull final String path, @NonNull final Predicate... predicates) {
        if (path == null) {
            throw new NullPointerException("path is marked non-null but is null");
        }
        if (predicates == null) {
            throw new NullPointerException("predicates is marked non-null but is null");
        }
        return or(PersistencePath.of(path), predicates);
    }
    
    public static Condition or(@NonNull final PersistencePath path, @NonNull final Predicate... predicates) {
        if (path == null) {
            throw new NullPointerException("path is marked non-null but is null");
        }
        if (predicates == null) {
            throw new NullPointerException("predicates is marked non-null but is null");
        }
        if (predicates.length <= 0) {
            throw new IllegalArgumentException("one or more predicate is required");
        }
        return new Condition(LogicalOperator.OR, path, predicates);
    }
    
    @Override
    public boolean check(final Object leftOperand) {
        if (this.operator == LogicalOperator.AND) {
            return Arrays.stream((Object[])this.predicates).allMatch(p -> p.check(leftOperand));
        }
        if (this.operator == LogicalOperator.OR) {
            return Arrays.stream((Object[])this.predicates).anyMatch(p -> p.check(leftOperand));
        }
        throw new IllegalArgumentException("Unsupported operator: " + (Object)this.operator);
    }
    
    public LogicalOperator getOperator() {
        return this.operator;
    }
    
    public PersistencePath getPath() {
        return this.path;
    }
    
    public Predicate[] getPredicates() {
        return this.predicates;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Condition)) {
            return false;
        }
        final Condition other = (Condition)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$operator = this.getOperator();
        final Object other$operator = other.getOperator();
        Label_0065: {
            if (this$operator == null) {
                if (other$operator == null) {
                    break Label_0065;
                }
            }
            else if (this$operator.equals(other$operator)) {
                break Label_0065;
            }
            return false;
        }
        final Object this$path = this.getPath();
        final Object other$path = other.getPath();
        if (this$path == null) {
            if (other$path == null) {
                return Arrays.deepEquals((Object[])this.getPredicates(), (Object[])other.getPredicates());
            }
        }
        else if (this$path.equals(other$path)) {
            return Arrays.deepEquals((Object[])this.getPredicates(), (Object[])other.getPredicates());
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof Condition;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $operator = this.getOperator();
        result = result * 59 + (($operator == null) ? 43 : $operator.hashCode());
        final Object $path = this.getPath();
        result = result * 59 + (($path == null) ? 43 : $path.hashCode());
        result = result * 59 + Arrays.deepHashCode((Object[])this.getPredicates());
        return result;
    }
    
    @Override
    public String toString() {
        return "Condition(operator=" + (Object)this.getOperator() + ", path=" + (Object)this.getPath() + ", predicates=" + Arrays.deepToString((Object[])this.getPredicates()) + ")";
    }
    
    private Condition(final LogicalOperator operator, final PersistencePath path, final Predicate[] predicates) {
        this.operator = operator;
        this.path = path;
        this.predicates = predicates;
    }
}
