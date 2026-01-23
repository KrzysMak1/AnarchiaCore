package cc.dreamcode.antylogout.libs.eu.okaeri.persistence.filter.renderer;

import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.PersistencePath;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.filter.condition.Condition;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.filter.predicate.Predicate;
import lombok.NonNull;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.filter.condition.LogicalOperator;

public interface FilterRenderer
{
    String renderOperator(@NonNull final LogicalOperator operator);
    
    String renderOperator(@NonNull final Predicate predicate);
    
    String renderCondition(@NonNull final Condition condition);
    
    String renderPredicate(@NonNull final PersistencePath path, @NonNull final Predicate predicate);
    
    String renderOperand(@NonNull final Object operand);
}
