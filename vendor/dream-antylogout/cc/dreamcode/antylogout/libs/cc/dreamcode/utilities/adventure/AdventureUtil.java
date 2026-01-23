package cc.dreamcode.antylogout.libs.cc.dreamcode.utilities.adventure;

import cc.dreamcode.antylogout.libs.net.kyori.adventure.util.Buildable;
import cc.dreamcode.antylogout.libs.net.kyori.adventure.text.ComponentBuilder;
import cc.dreamcode.antylogout.libs.net.kyori.adventure.text.serializer.ComponentSerializer;
import cc.dreamcode.antylogout.libs.net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import cc.dreamcode.antylogout.libs.net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import java.util.function.UnaryOperator;
import java.util.function.Function;
import cc.dreamcode.antylogout.libs.net.kyori.adventure.text.event.ClickEvent;
import cc.dreamcode.antylogout.libs.eu.okaeri.placeholders.message.part.MessageField;
import cc.dreamcode.antylogout.libs.net.kyori.adventure.text.ComponentLike;
import cc.dreamcode.antylogout.libs.net.kyori.adventure.text.TextComponent;
import java.util.regex.MatchResult;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import cc.dreamcode.antylogout.libs.eu.okaeri.placeholders.context.PlaceholderContext;
import cc.dreamcode.antylogout.libs.cc.dreamcode.utilities.StringUtil;
import cc.dreamcode.antylogout.libs.eu.okaeri.placeholders.message.CompiledMessage;
import java.util.Locale;
import java.util.Map;
import cc.dreamcode.antylogout.libs.net.kyori.adventure.text.Component;
import lombok.NonNull;
import cc.dreamcode.antylogout.libs.net.kyori.adventure.text.minimessage.MiniMessage;
import cc.dreamcode.antylogout.libs.net.kyori.adventure.text.TextReplacementConfig;
import cc.dreamcode.antylogout.libs.net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import java.util.regex.Pattern;

public final class AdventureUtil
{
    private static final Pattern ALL_TEXT_PATTERN;
    private static final Pattern FIELD_PATTERN;
    private static final Pattern SECTION_COLOR_PATTERN;
    private static final Pattern LEGACY_RGB_PATTERN;
    private static final Pattern URL_PATTERN;
    private static final LegacyComponentSerializer SECTION_SERIALIZER;
    private static final LegacyComponentSerializer LEGACY_SECTION_SERIALIZER;
    private static final LegacyComponentSerializer AMPERSAND_SERIALIZER;
    private static final LegacyComponentSerializer LEGACY_AMPERSAND_SERIALIZER;
    private static final TextReplacementConfig AMPERSAND_REPLACEMENTS;
    private static final TextReplacementConfig CLICKABLE_URL_REPLACEMENT;
    private static final MiniMessage MINI_MESSAGE;
    private static final MiniMessage PLACEHOLDER_MINI_MESSAGE;
    private static final MiniMessage COLORIZED_PLACEHOLDER_MINI_MESSAGE;
    public static boolean rgbSupport;
    
    public static Component component(@NonNull final String text) {
        if (text == null) {
            throw new NullPointerException("text is marked non-null but is null");
        }
        return ((ComponentSerializer<I, Component, String>)AdventureUtil.MINI_MESSAGE).deserialize(text);
    }
    
    public static Component component(@NonNull final String text, @NonNull final Map<String, Object> placeholders) {
        if (text == null) {
            throw new NullPointerException("text is marked non-null but is null");
        }
        if (placeholders == null) {
            throw new NullPointerException("placeholders is marked non-null but is null");
        }
        return component(text, Locale.forLanguageTag("pl"), placeholders);
    }
    
    public static Component component(@NonNull final String text, @NonNull final Map<String, Object> placeholders, final boolean colorizePlaceholders) {
        if (text == null) {
            throw new NullPointerException("text is marked non-null but is null");
        }
        if (placeholders == null) {
            throw new NullPointerException("placeholders is marked non-null but is null");
        }
        return component(text, Locale.forLanguageTag("pl"), placeholders, colorizePlaceholders);
    }
    
    public static Component component(@NonNull final String text, @NonNull final Locale locale, @NonNull final Map<String, Object> placeholders) {
        if (text == null) {
            throw new NullPointerException("text is marked non-null but is null");
        }
        if (locale == null) {
            throw new NullPointerException("locale is marked non-null but is null");
        }
        if (placeholders == null) {
            throw new NullPointerException("placeholders is marked non-null but is null");
        }
        return component(text, locale, placeholders, true);
    }
    
    public static Component component(@NonNull final String text, @NonNull final Locale locale, @NonNull final Map<String, Object> placeholders, final boolean colorizePlaceholders) {
        if (text == null) {
            throw new NullPointerException("text is marked non-null but is null");
        }
        if (locale == null) {
            throw new NullPointerException("locale is marked non-null but is null");
        }
        if (placeholders == null) {
            throw new NullPointerException("placeholders is marked non-null but is null");
        }
        final CompiledMessage compiledMessage = CompiledMessage.of(locale, text);
        final PlaceholderContext placeholderContext = StringUtil.getPlaceholders().contextOf(compiledMessage).with(placeholders);
        return component(text, placeholderContext, colorizePlaceholders);
    }
    
    private static Component component(@NonNull final String text, @NonNull final PlaceholderContext placeholderContext, final boolean colorizePlaceholders) {
        if (text == null) {
            throw new NullPointerException("text is marked non-null but is null");
        }
        if (placeholderContext == null) {
            throw new NullPointerException("placeholderContext is marked non-null but is null");
        }
        final Component component = ((ComponentSerializer<I, Component, String>)AdventureUtil.MINI_MESSAGE).deserialize(text);
        final Map<String, String> fields = renderFields(placeholderContext);
        final TextReplacementConfig replacementConfig = replacementConfig(fields, colorizePlaceholders);
        return component.replaceText(replacementConfig);
    }
    
    public static String process(@NonNull final String text) {
        if (text == null) {
            throw new NullPointerException("text is marked non-null but is null");
        }
        final Component component = ((ComponentSerializer<I, Component, String>)AdventureUtil.MINI_MESSAGE).deserialize(text);
        if (AdventureUtil.rgbSupport) {
            return AdventureUtil.SECTION_SERIALIZER.serialize(component);
        }
        return AdventureUtil.LEGACY_SECTION_SERIALIZER.serialize(component);
    }
    
    public static String process(@NonNull final String text, @NonNull final Map<String, Object> placeholders) {
        if (text == null) {
            throw new NullPointerException("text is marked non-null but is null");
        }
        if (placeholders == null) {
            throw new NullPointerException("placeholders is marked non-null but is null");
        }
        return process(text, Locale.forLanguageTag("pl"), placeholders);
    }
    
    public static String process(@NonNull final String text, @NonNull final Map<String, Object> placeholders, final boolean colorizePlaceholders) {
        if (text == null) {
            throw new NullPointerException("text is marked non-null but is null");
        }
        if (placeholders == null) {
            throw new NullPointerException("placeholders is marked non-null but is null");
        }
        return process(text, Locale.forLanguageTag("pl"), placeholders, colorizePlaceholders);
    }
    
    public static String process(@NonNull final String text, @NonNull final Locale locale, @NonNull final Map<String, Object> placeholders) {
        if (text == null) {
            throw new NullPointerException("text is marked non-null but is null");
        }
        if (locale == null) {
            throw new NullPointerException("locale is marked non-null but is null");
        }
        if (placeholders == null) {
            throw new NullPointerException("placeholders is marked non-null but is null");
        }
        return process(text, locale, placeholders, true);
    }
    
    public static String process(@NonNull final String text, @NonNull final Locale locale, @NonNull final Map<String, Object> placeholders, final boolean colorizePlaceholders) {
        if (text == null) {
            throw new NullPointerException("text is marked non-null but is null");
        }
        if (locale == null) {
            throw new NullPointerException("locale is marked non-null but is null");
        }
        if (placeholders == null) {
            throw new NullPointerException("placeholders is marked non-null but is null");
        }
        final CompiledMessage compiledMessage = CompiledMessage.of(locale, text);
        final PlaceholderContext placeholderContext = StringUtil.getPlaceholders().contextOf(compiledMessage).with(placeholders);
        return process(text, placeholderContext, colorizePlaceholders);
    }
    
    private static String process(@NonNull final String text, @NonNull final PlaceholderContext placeholderContext, final boolean colorizePlaceholders) {
        if (text == null) {
            throw new NullPointerException("text is marked non-null but is null");
        }
        if (placeholderContext == null) {
            throw new NullPointerException("placeholderContext is marked non-null but is null");
        }
        Component component = ((ComponentSerializer<I, Component, String>)AdventureUtil.MINI_MESSAGE).deserialize(text);
        final Map<String, String> fields = renderFields(placeholderContext);
        final TextReplacementConfig replacementConfig = replacementConfig(fields, colorizePlaceholders);
        component = component.replaceText(replacementConfig);
        if (AdventureUtil.rgbSupport) {
            return AdventureUtil.SECTION_SERIALIZER.serialize(component);
        }
        return AdventureUtil.LEGACY_SECTION_SERIALIZER.serialize(component);
    }
    
    public static String deprocess(@NonNull final String text) {
        if (text == null) {
            throw new NullPointerException("text is marked non-null but is null");
        }
        Component component;
        if (AdventureUtil.rgbSupport) {
            component = AdventureUtil.SECTION_SERIALIZER.deserialize(text);
        }
        else {
            component = AdventureUtil.LEGACY_SECTION_SERIALIZER.deserialize(text);
        }
        return ((ComponentSerializer<Component, O, String>)AdventureUtil.MINI_MESSAGE).serialize(component);
    }
    
    private static Map<String, String> renderFields(@NonNull final PlaceholderContext placeholderContext) {
        if (placeholderContext == null) {
            throw new NullPointerException("placeholderContext is marked non-null but is null");
        }
        return (Map<String, String>)placeholderContext.renderFields().entrySet().stream().collect(Collectors.toMap(entry -> ((MessageField)entry.getKey()).getRaw(), Map.Entry::getValue));
    }
    
    private static TextReplacementConfig replacementConfig(@NonNull final Map<String, String> replaceMap, final boolean colorizePlaceholders) {
        if (replaceMap == null) {
            throw new NullPointerException("replaceMap is marked non-null but is null");
        }
        return TextReplacementConfig.builder().match(AdventureUtil.FIELD_PATTERN).replacement((BiFunction<MatchResult, TextComponent.Builder, ComponentLike>)((result, input) -> {
            final String value = (String)replaceMap.get((Object)result.group(1));
            if (colorizePlaceholders) {
                return ((ComponentSerializer<I, ComponentLike, String>)AdventureUtil.COLORIZED_PLACEHOLDER_MINI_MESSAGE).deserialize(value);
            }
            return ((ComponentSerializer<I, ComponentLike, String>)AdventureUtil.PLACEHOLDER_MINI_MESSAGE).deserialize(value);
        })).build();
    }
    
    public static void setRgbSupport(final boolean rgbSupport) {
        AdventureUtil.rgbSupport = rgbSupport;
    }
    
    static {
        ALL_TEXT_PATTERN = Pattern.compile(".*");
        FIELD_PATTERN = Pattern.compile("\\{(?<content>[^}]+)}");
        SECTION_COLOR_PATTERN = Pattern.compile("(?i)§([0-9A-FK-OR])");
        LEGACY_RGB_PATTERN = Pattern.compile("&#([a-fA-F0-9]{6})");
        URL_PATTERN = Pattern.compile("https?:\\/\\/(www\\.)?[a-zA-Z0-9\\-._~%]{1,256}\\.[a-zA-Z]{1,6}(\\/[a-zA-Z0-9\\-._~%!$&'()*+,;=:@/#?]*)?");
        SECTION_SERIALIZER = ((Buildable<R, LegacyComponentSerializer.Builder>)LegacyComponentSerializer.legacySection()).toBuilder().hexColors().useUnusualXRepeatedCharacterHexFormat().build();
        LEGACY_SECTION_SERIALIZER = LegacyComponentSerializer.legacySection();
        AMPERSAND_SERIALIZER = ((Buildable<R, LegacyComponentSerializer.Builder>)LegacyComponentSerializer.legacyAmpersand()).toBuilder().hexColors().useUnusualXRepeatedCharacterHexFormat().build();
        LEGACY_AMPERSAND_SERIALIZER = LegacyComponentSerializer.legacyAmpersand();
        AMPERSAND_REPLACEMENTS = TextReplacementConfig.builder().match(AdventureUtil.ALL_TEXT_PATTERN).replacement((BiFunction<MatchResult, TextComponent.Builder, ComponentLike>)((result, input) -> AdventureUtil.rgbSupport ? AdventureUtil.AMPERSAND_SERIALIZER.deserialize(result.group()) : AdventureUtil.LEGACY_AMPERSAND_SERIALIZER.deserialize(result.group()))).build();
        CLICKABLE_URL_REPLACEMENT = TextReplacementConfig.builder().match(AdventureUtil.URL_PATTERN).replacement((Function<TextComponent.Builder, ComponentLike>)(url -> ((ComponentBuilder<C, ComponentLike>)url).clickEvent(ClickEvent.openUrl(url.content())))).build();
        MINI_MESSAGE = MiniMessage.builder().preProcessor((UnaryOperator<String>)(text -> {
            String replaceText = text;
            replaceText = AdventureUtil.SECTION_COLOR_PATTERN.matcher((CharSequence)replaceText).replaceAll("&$1");
            replaceText = AdventureUtil.LEGACY_RGB_PATTERN.matcher((CharSequence)replaceText).replaceAll("<#$1>");
            return replaceText;
        })).postProcessor((UnaryOperator<Component>)(component -> component.replaceText(AdventureUtil.CLICKABLE_URL_REPLACEMENT).replaceText(AdventureUtil.AMPERSAND_REPLACEMENTS))).build();
        PLACEHOLDER_MINI_MESSAGE = MiniMessage.builder().preProcessor((UnaryOperator<String>)(text -> {
            String replaceText = text;
            replaceText = AdventureUtil.SECTION_COLOR_PATTERN.matcher((CharSequence)replaceText).replaceAll("&$1");
            replaceText = AdventureUtil.LEGACY_RGB_PATTERN.matcher((CharSequence)replaceText).replaceAll("<#$1>");
            return replaceText;
        })).tags(TagResolver.empty()).postProcessor((UnaryOperator<Component>)(component -> component.replaceText(AdventureUtil.CLICKABLE_URL_REPLACEMENT))).build();
        COLORIZED_PLACEHOLDER_MINI_MESSAGE = MiniMessage.builder().preProcessor((UnaryOperator<String>)(text -> {
            String replaceText = text;
            replaceText = AdventureUtil.SECTION_COLOR_PATTERN.matcher((CharSequence)replaceText).replaceAll("&$1");
            replaceText = AdventureUtil.LEGACY_RGB_PATTERN.matcher((CharSequence)replaceText).replaceAll("<#$1>");
            return replaceText;
        })).tags(TagResolver.builder().resolver(StandardTags.color()).resolver(StandardTags.decorations()).resolver(StandardTags.rainbow()).resolver(StandardTags.gradient()).resolver(StandardTags.transition()).build()).postProcessor((UnaryOperator<Component>)(component -> component.replaceText(AdventureUtil.CLICKABLE_URL_REPLACEMENT).replaceText(AdventureUtil.AMPERSAND_REPLACEMENTS))).build();
        AdventureUtil.rgbSupport = true;
    }
}
