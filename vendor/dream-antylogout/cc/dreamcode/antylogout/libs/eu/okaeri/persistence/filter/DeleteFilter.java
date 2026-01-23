package cc.dreamcode.antylogout.libs.eu.okaeri.persistence.filter;

import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.filter.condition.Condition;

public class DeleteFilter
{
    private final Condition where;
    
    public static DeleteFilterBuilder builder() {
        return new DeleteFilterBuilder();
    }
    
    public DeleteFilter(final Condition where) {
        this.where = where;
    }
    
    public Condition getWhere() {
        return this.where;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DeleteFilter)) {
            return false;
        }
        final DeleteFilter other = (DeleteFilter)o;
        if (!other.canEqual(this)) {
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
        return other instanceof DeleteFilter;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $where = this.getWhere();
        result = result * 59 + (($where == null) ? 43 : $where.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "DeleteFilter(where=" + (Object)this.getWhere() + ")";
    }
}
