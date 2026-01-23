package cc.dreamcode.antylogout.libs.eu.okaeri.persistence.filter;

import lombok.NonNull;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.filter.condition.Condition;

public class FindFilterBuilder
{
    private Condition where;
    private int limit;
    private int skip;
    
    public FindFilterBuilder where(@NonNull final Condition where) {
        if (where == null) {
            throw new NullPointerException("where is marked non-null but is null");
        }
        this.where = where;
        return this;
    }
    
    public FindFilterBuilder limit(final int limit) {
        this.limit = limit;
        return this;
    }
    
    public FindFilterBuilder skip(final int skip) {
        this.skip = skip;
        return this;
    }
    
    public FindFilter build() {
        return new FindFilter(this.where, this.limit, this.skip);
    }
}
