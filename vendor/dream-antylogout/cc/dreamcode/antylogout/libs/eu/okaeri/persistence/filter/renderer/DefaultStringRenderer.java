package cc.dreamcode.antylogout.libs.eu.okaeri.persistence.filter.renderer;

import lombok.NonNull;

public class DefaultStringRenderer implements StringRenderer
{
    @Override
    public String render(@NonNull final String text) {
        if (text == null) {
            throw new NullPointerException("text is marked non-null but is null");
        }
        return "\"" + this.escape(text) + "\"";
    }
    
    protected String escape(@NonNull final String value) {
        if (value == null) {
            throw new NullPointerException("value is marked non-null but is null");
        }
        final StringBuilder builder = new StringBuilder();
        for (final char c : value.toCharArray()) {
            if (c == '\'') {
                builder.append("\\'");
            }
            else if (c == '\"') {
                builder.append("\\\"");
            }
            else if (c == '\r') {
                builder.append("\\r");
            }
            else if (c == '\n') {
                builder.append("\\n");
            }
            else if (c == '\t') {
                builder.append("\\t");
            }
            else if (c < ' ' || c >= '\u007f') {
                builder.append(String.format("\\u%04x", new Object[] { (int)c }));
            }
            else {
                builder.append(c);
            }
        }
        return builder.toString();
    }
}
