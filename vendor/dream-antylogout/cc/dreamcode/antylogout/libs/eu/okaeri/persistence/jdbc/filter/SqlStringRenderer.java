package cc.dreamcode.antylogout.libs.eu.okaeri.persistence.jdbc.filter;

import lombok.NonNull;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.filter.renderer.DefaultStringRenderer;

public class SqlStringRenderer extends DefaultStringRenderer
{
    @Override
    public String render(@NonNull final String text) {
        if (text == null) {
            throw new NullPointerException("text is marked non-null but is null");
        }
        return "'" + this.escape(text) + "'";
    }
}
