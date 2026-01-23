package cc.dreamcode.antylogout.libs.cc.dreamcode.utilities.bukkit;

import cc.dreamcode.antylogout.libs.cc.dreamcode.utilities.bukkit.color.DefaultColorProcessor;
import lombok.Generated;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import lombok.NonNull;
import cc.dreamcode.antylogout.libs.cc.dreamcode.utilities.color.ColorProcessor;

public final class StringColorUtil
{
    private static ColorProcessor COLOR_PROCESSOR;
    
    public static String fixColor(@NonNull final String text) {
        if (text == null) {
            throw new NullPointerException("text is marked non-null but is null");
        }
        return StringColorUtil.COLOR_PROCESSOR.color(text);
    }
    
    public static String fixColor(@NonNull final String text, @NonNull final Map<String, Object> placeholders) {
        if (text == null) {
            throw new NullPointerException("text is marked non-null but is null");
        }
        if (placeholders == null) {
            throw new NullPointerException("placeholders is marked non-null but is null");
        }
        return StringColorUtil.COLOR_PROCESSOR.color(text, placeholders);
    }
    
    public static String fixColor(@NonNull final String text, @NonNull final Map<String, Object> placeholders, final boolean colorizePlaceholders) {
        if (text == null) {
            throw new NullPointerException("text is marked non-null but is null");
        }
        if (placeholders == null) {
            throw new NullPointerException("placeholders is marked non-null but is null");
        }
        return StringColorUtil.COLOR_PROCESSOR.color(text, placeholders, colorizePlaceholders);
    }
    
    public static String fixColor(@NonNull final String text, @NonNull final Locale locale, @NonNull final Map<String, Object> placeholders) {
        if (text == null) {
            throw new NullPointerException("text is marked non-null but is null");
        }
        if (locale == null) {
            throw new NullPointerException("locale is marked non-null but is null");
        }
        if (placeholders == null) {
            throw new NullPointerException("placeholders is marked non-null but is null");
        }
        return StringColorUtil.COLOR_PROCESSOR.color(text, locale, placeholders);
    }
    
    public static String fixColor(@NonNull final String text, @NonNull final Locale locale, @NonNull final Map<String, Object> placeholders, final boolean colorizePlaceholders) {
        if (text == null) {
            throw new NullPointerException("text is marked non-null but is null");
        }
        if (locale == null) {
            throw new NullPointerException("locale is marked non-null but is null");
        }
        if (placeholders == null) {
            throw new NullPointerException("placeholders is marked non-null but is null");
        }
        return StringColorUtil.COLOR_PROCESSOR.color(text, locale, placeholders, colorizePlaceholders);
    }
    
    public static List<String> fixColor(@NonNull final List<String> stringList) {
        if (stringList == null) {
            throw new NullPointerException("stringList is marked non-null but is null");
        }
        return StringColorUtil.COLOR_PROCESSOR.color(stringList);
    }
    
    public static List<String> fixColor(@NonNull final List<String> stringList, @NonNull final Map<String, Object> placeholders) {
        if (stringList == null) {
            throw new NullPointerException("stringList is marked non-null but is null");
        }
        if (placeholders == null) {
            throw new NullPointerException("placeholders is marked non-null but is null");
        }
        return StringColorUtil.COLOR_PROCESSOR.color(stringList, placeholders);
    }
    
    public static List<String> fixColor(@NonNull final List<String> stringList, @NonNull final Map<String, Object> placeholders, final boolean colorizePlaceholders) {
        if (stringList == null) {
            throw new NullPointerException("stringList is marked non-null but is null");
        }
        if (placeholders == null) {
            throw new NullPointerException("placeholders is marked non-null but is null");
        }
        return StringColorUtil.COLOR_PROCESSOR.color(stringList, placeholders, colorizePlaceholders);
    }
    
    public static List<String> fixColor(@NonNull final List<String> stringList, @NonNull final Locale locale, @NonNull final Map<String, Object> placeholders) {
        if (stringList == null) {
            throw new NullPointerException("stringList is marked non-null but is null");
        }
        if (locale == null) {
            throw new NullPointerException("locale is marked non-null but is null");
        }
        if (placeholders == null) {
            throw new NullPointerException("placeholders is marked non-null but is null");
        }
        return StringColorUtil.COLOR_PROCESSOR.color(stringList, locale, placeholders);
    }
    
    public static List<String> fixColor(@NonNull final List<String> stringList, @NonNull final Locale locale, @NonNull final Map<String, Object> placeholders, final boolean colorizePlaceholders) {
        if (stringList == null) {
            throw new NullPointerException("stringList is marked non-null but is null");
        }
        if (locale == null) {
            throw new NullPointerException("locale is marked non-null but is null");
        }
        if (placeholders == null) {
            throw new NullPointerException("placeholders is marked non-null but is null");
        }
        return StringColorUtil.COLOR_PROCESSOR.color(stringList, locale, placeholders, colorizePlaceholders);
    }
    
    public static List<String> fixColor(@NonNull final String... strings) {
        if (strings == null) {
            throw new NullPointerException("strings is marked non-null but is null");
        }
        return StringColorUtil.COLOR_PROCESSOR.color(strings);
    }
    
    public static String breakColor(@NonNull final String text) {
        if (text == null) {
            throw new NullPointerException("text is marked non-null but is null");
        }
        return StringColorUtil.COLOR_PROCESSOR.decolor(text);
    }
    
    public static List<String> breakColor(@NonNull final List<String> stringList) {
        if (stringList == null) {
            throw new NullPointerException("stringList is marked non-null but is null");
        }
        return StringColorUtil.COLOR_PROCESSOR.decolor(stringList);
    }
    
    public static List<String> breakColor(@NonNull final String... strings) {
        if (strings == null) {
            throw new NullPointerException("strings is marked non-null but is null");
        }
        return StringColorUtil.COLOR_PROCESSOR.decolor(strings);
    }
    
    public static void setColorProcessor(@NonNull final ColorProcessor colorProcessor) {
        if (colorProcessor == null) {
            throw new NullPointerException("colorProcessor is marked non-null but is null");
        }
        StringColorUtil.COLOR_PROCESSOR = colorProcessor;
    }
    
    @Generated
    private StringColorUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    static {
        StringColorUtil.COLOR_PROCESSOR = new DefaultColorProcessor();
    }
}
