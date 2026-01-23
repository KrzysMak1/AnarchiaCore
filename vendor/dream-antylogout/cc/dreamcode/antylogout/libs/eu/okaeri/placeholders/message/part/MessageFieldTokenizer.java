package cc.dreamcode.antylogout.libs.eu.okaeri.placeholders.message.part;

import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import lombok.NonNull;

public class MessageFieldTokenizer
{
    public List<FieldParams> tokenize(@NonNull final String field) {
        if (field == null) {
            throw new NullPointerException("field is marked non-null but is null");
        }
        final List<FieldParams> tokens = (List<FieldParams>)new ArrayList();
        final char[] charArray = field.toCharArray();
        final StringBuilder buffer = new StringBuilder();
        boolean parsingArgs = false;
        String name = "";
        for (int i = 0, charArrayLength = charArray.length; i < charArrayLength; ++i) {
            final char c = charArray[i];
            if (c == '(' && !parsingArgs) {
                name = buffer.toString();
                buffer.setLength(0);
                parsingArgs = true;
            }
            else if (c == ')' && (charArrayLength == i + 1 || charArray[i + 1] == '.') && parsingArgs) {
                tokens.add((Object)FieldParams.of(name, (String[])this.tokenizeArgs(buffer.toString()).toArray((Object[])new String[0])));
                buffer.setLength(0);
                parsingArgs = false;
                ++i;
            }
            else if (parsingArgs) {
                buffer.append(c);
            }
            else if (charArrayLength == i + 1 || charArray[i + 1] == '.') {
                buffer.append(c);
                tokens.add((Object)FieldParams.of(buffer.toString(), new String[0]));
                buffer.setLength(0);
                ++i;
            }
            else {
                buffer.append(c);
            }
        }
        return tokens;
    }
    
    public List<String> tokenizeArgs(@NonNull final String argText) {
        if (argText == null) {
            throw new NullPointerException("argText is marked non-null but is null");
        }
        if (argText.isEmpty()) {
            return (List<String>)Collections.singletonList((Object)"");
        }
        final List<String> args = (List<String>)new ArrayList();
        final char[] charArray = argText.toCharArray();
        final StringBuilder buffer = new StringBuilder();
        for (int i = 0, charArrayLength = charArray.length; i < charArrayLength; ++i) {
            final char c = charArray[i];
            if (c == ',') {
                if (i > 0 && charArray[i - 1] == '\\') {
                    buffer.setCharAt(buffer.length() - 1, c);
                }
                else {
                    args.add((Object)buffer.toString());
                    buffer.setLength(0);
                }
            }
            else if (charArrayLength == i + 1) {
                buffer.append(c);
                args.add((Object)buffer.toString());
                buffer.setLength(0);
            }
            else {
                buffer.append(c);
            }
        }
        return args;
    }
}
