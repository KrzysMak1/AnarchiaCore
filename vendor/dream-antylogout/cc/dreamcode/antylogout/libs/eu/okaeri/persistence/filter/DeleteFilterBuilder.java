package cc.dreamcode.antylogout.libs.eu.okaeri.persistence.filter;

import lombok.NonNull;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.filter.condition.Condition;

public class DeleteFilterBuilder
{
    private Condition where;
    
    public DeleteFilterBuilder where(@NonNull final Condition where) {
        if (where == null) {
            throw new NullPointerException("where is marked non-null but is null");
        }
        this.where = where;
        return this;
    }
    
    public DeleteFilter build() {
        return new DeleteFilter(this.where);
    }
}
