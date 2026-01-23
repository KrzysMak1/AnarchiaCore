package cc.dreamcode.antylogout.libs.eu.okaeri.persistence.filter;

import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.filter.condition.Condition;

public class FindFilter
{
    private final Condition where;
    private final int limit;
    private final int skip;
    
    public static FindFilterBuilder builder() {
        return new FindFilterBuilder();
    }
    
    public boolean hasSkip() {
        return this.skip > 0;
    }
    
    public boolean hasLimit() {
        return this.limit > 0;
    }
    
    public FindFilter(final Condition where, final int limit, final int skip) {
        this.where = where;
        this.limit = limit;
        this.skip = skip;
    }
    
    public Condition getWhere() {
        return this.where;
    }
    
    public int getLimit() {
        return this.limit;
    }
    
    public int getSkip() {
        return this.skip;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof FindFilter)) {
            return false;
        }
        final FindFilter other = (FindFilter)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getLimit() != other.getLimit()) {
            return false;
        }
        if (this.getSkip() != other.getSkip()) {
            return false;
        }
        final Object this$where = this.getWhere();
        final Object other$where = other.getWhere();
        if (this$where == null) {
            if (other$where == null) {
                return true;
            }
        }
        else if (this$where.equals(other$where)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof FindFilter;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getLimit();
        result = result * 59 + this.getSkip();
        final Object $where = this.getWhere();
        result = result * 59 + (($where == null) ? 43 : $where.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "FindFilter(where=" + (Object)this.getWhere() + ", limit=" + this.getLimit() + ", skip=" + this.getSkip() + ")";
    }
}
