package cc.dreamcode.antylogout.libs.cc.dreamcode.utilities.color;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import lombok.NonNull;

public interface ColorProcessor
{
    String color(@NonNull final String text);
    
    default String color(@NonNull final String text, @NonNull final Map<String, Object> placeholders) {
        if (text == null) {
            throw new NullPointerException("text is marked non-null but is null");
        }
        if (placeholders == null) {
            throw new NullPointerException("placeholders is marked non-null but is null");
        }
        return this.color(text, Locale.forLanguageTag("pl"), placeholders, true);
    }
    
    default String color(@NonNull final String text, @NonNull final Map<String, Object> placeholders, final boolean colorizePlaceholders) {
        if (text == null) {
            throw new NullPointerException("text is marked non-null but is null");
        }
        if (placeholders == null) {
            throw new NullPointerException("placeholders is marked non-null but is null");
        }
        return this.color(text, Locale.forLanguageTag("pl"), placeholders, colorizePlaceholders);
    }
    
    default String color(@NonNull final String text, @NonNull final Locale locale, @NonNull final Map<String, Object> placeholders) {
        if (text == null) {
            throw new NullPointerException("text is marked non-null but is null");
        }
        if (locale == null) {
            throw new NullPointerException("locale is marked non-null but is null");
        }
        if (placeholders == null) {
            throw new NullPointerException("placeholders is marked non-null but is null");
        }
        return this.color(text, locale, placeholders, true);
    }
    
    String color(@NonNull final String text, @NonNull final Locale locale, @NonNull final Map<String, Object> placeholders, final boolean colorizePlaceholders);
    
    default List<String> color(@NonNull final List<String> stringList) {
        if (stringList == null) {
            throw new NullPointerException("stringList is marked non-null but is null");
        }
        return (List<String>)stringList.stream().map(this::color).collect(Collectors.toList());
    }
    
    default List<String> color(@NonNull final List<String> stringList, @NonNull final Map<String, Object> placeholders) {
        if (stringList == null) {
            throw new NullPointerException("stringList is marked non-null but is null");
        }
        if (placeholders == null) {
            throw new NullPointerException("placeholders is marked non-null but is null");
        }
        return (List<String>)stringList.stream().map(text -> this.color(text, placeholders)).collect(Collectors.toList());
    }
    
    default List<String> color(@NonNull final List<String> stringList, @NonNull final Map<String, Object> placeholders, final boolean colorizePlaceholders) {
        if (stringList == null) {
            throw new NullPointerException("stringList is marked non-null but is null");
        }
        if (placeholders == null) {
            throw new NullPointerException("placeholders is marked non-null but is null");
        }
        return (List<String>)stringList.stream().map(text -> this.color(text, placeholders, colorizePlaceholders)).collect(Collectors.toList());
    }
    
    default List<String> color(@NonNull final List<String> stringList, @NonNull final Locale locale, @NonNull final Map<String, Object> placeholders) {
        if (stringList == null) {
            throw new NullPointerException("stringList is marked non-null but is null");
        }
        if (locale == null) {
            throw new NullPointerException("locale is marked non-null but is null");
        }
        if (placeholders == null) {
            throw new NullPointerException("placeholders is marked non-null but is null");
        }
        return (List<String>)stringList.stream().map(text -> this.color(text, locale, placeholders)).collect(Collectors.toList());
    }
    
    default List<String> color(@NonNull final List<String> stringList, @NonNull final Locale locale, @NonNull final Map<String, Object> placeholders, final boolean colorizePlaceholders) {
        if (stringList == null) {
            throw new NullPointerException("stringList is marked non-null but is null");
        }
        if (locale == null) {
            throw new NullPointerException("locale is marked non-null but is null");
        }
        if (placeholders == null) {
            throw new NullPointerException("placeholders is marked non-null but is null");
        }
        return (List<String>)stringList.stream().map(text -> this.color(text, locale, placeholders, colorizePlaceholders)).collect(Collectors.toList());
    }
    
    default List<String> color(@NonNull final String... strings) {
        if (strings == null) {
            throw new NullPointerException("strings is marked non-null but is null");
        }
        return (List<String>)Arrays.stream((Object[])strings).map(this::color).collect(Collectors.toList());
    }
    
    String decolor(@NonNull final String text);
    
    default List<String> decolor(@NonNull final List<String> stringList) {
        if (stringList == null) {
            throw new NullPointerException("stringList is marked non-null but is null");
        }
        return (List<String>)stringList.stream().map(this::decolor).collect(Collectors.toList());
    }
    
    default List<String> decolor(@NonNull final String... strings) {
        if (strings == null) {
            throw new NullPointerException("strings is marked non-null but is null");
        }
        return (List<String>)Arrays.stream((Object[])strings).map(this::decolor).collect(Collectors.toList());
    }
}
