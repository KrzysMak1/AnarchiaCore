package cc.dreamcode.antylogout.libs.eu.okaeri.persistence.jdbc.filter;

import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.filter.predicate.SimplePredicate;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.filter.predicate.Predicate;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.PersistencePath;
import lombok.NonNull;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.filter.renderer.StringRenderer;

public class PostgresFilterRenderer extends SqlFilterRenderer
{
    public PostgresFilterRenderer(@NonNull final StringRenderer stringRenderer) {
        super(stringRenderer);
        if (stringRenderer == null) {
            throw new NullPointerException("stringRenderer is marked non-null but is null");
        }
    }
    
    @Override
    public String renderPredicate(@NonNull PersistencePath path, @NonNull final Predicate predicate) {
        if (path == null) {
            throw new NullPointerException("path is marked non-null but is null");
        }
        if (predicate == null) {
            throw new NullPointerException("predicate is marked non-null but is null");
        }
        path = PersistencePath.of("value").sub(path);
        if (predicate instanceof SimplePredicate && ((SimplePredicate)predicate).getRightOperand() instanceof Number) {
            return "((" + path.toPostgresJsonPath() + ")::numeric " + this.renderOperator(predicate) + " " + this.renderOperand(predicate) + ")";
        }
        return "(" + path.toPostgresJsonPath(true) + this.renderOperator(predicate) + " " + this.renderOperand(predicate) + ")";
    }
}
