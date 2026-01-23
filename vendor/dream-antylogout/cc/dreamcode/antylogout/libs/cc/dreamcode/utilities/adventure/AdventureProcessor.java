package cc.dreamcode.antylogout.libs.cc.dreamcode.utilities.adventure;

import java.util.Map;
import java.util.Locale;
import lombok.NonNull;
import cc.dreamcode.antylogout.libs.cc.dreamcode.utilities.color.ColorProcessor;

public class AdventureProcessor implements ColorProcessor
{
    @Override
    public String color(@NonNull final String text) {
        if (text == null) {
            throw new NullPointerException("text is marked non-null but is null");
        }
        return AdventureUtil.process(text);
    }
    
    @Override
    public String color(@NonNull final String text, @NonNull final Locale locale, @NonNull final Map<String, Object> placeholders, final boolean colorizePlaceholders) {
        if (text == null) {
            throw new NullPointerException("text is marked non-null but is null");
        }
        if (locale == null) {
            throw new NullPointerException("locale is marked non-null but is null");
        }
        if (placeholders == null) {
            throw new NullPointerException("placeholders is marked non-null but is null");
        }
        return AdventureUtil.process(text, locale, placeholders, colorizePlaceholders);
    }
    
    @Override
    public String decolor(@NonNull final String text) {
        if (text == null) {
            throw new NullPointerException("text is marked non-null but is null");
        }
        return AdventureUtil.deprocess(text);
    }
}
