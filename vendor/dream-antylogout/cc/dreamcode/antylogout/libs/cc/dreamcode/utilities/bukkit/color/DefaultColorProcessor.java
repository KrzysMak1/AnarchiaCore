package cc.dreamcode.antylogout.libs.cc.dreamcode.utilities.bukkit.color;

import cc.dreamcode.antylogout.libs.cc.dreamcode.utilities.builder.MapBuilder;
import java.util.regex.Matcher;
import cc.dreamcode.antylogout.libs.cc.dreamcode.utilities.bukkit.VersionUtil;
import java.util.concurrent.atomic.AtomicReference;
import java.util.Comparator;
import cc.dreamcode.antylogout.libs.cc.dreamcode.utilities.StringUtil;
import java.util.Locale;
import lombok.NonNull;
import net.md_5.bungee.api.ChatColor;
import java.awt.Color;
import java.util.Map;
import java.util.regex.Pattern;
import cc.dreamcode.antylogout.libs.cc.dreamcode.utilities.color.ColorProcessor;

public class DefaultColorProcessor implements ColorProcessor
{
    private static final char COLOR_CHAR = '§';
    private static final char ALT_COLOR_CHAR = '&';
    private static final Pattern HEX_PATTERN;
    private static final Map<Color, ChatColor> COLORS;
    
    @Override
    public String color(@NonNull final String text) {
        if (text == null) {
            throw new NullPointerException("text is marked non-null but is null");
        }
        return ChatColor.translateAlternateColorCodes('&', this.processRgb(text));
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
        if (colorizePlaceholders) {
            return this.color(StringUtil.replace(text, locale, placeholders));
        }
        final String color = this.color(text);
        return StringUtil.replace(color, locale, placeholders);
    }
    
    @Override
    public String decolor(@NonNull final String text) {
        if (text == null) {
            throw new NullPointerException("text is marked non-null but is null");
        }
        return text.replace((CharSequence)"§", (CharSequence)"&");
    }
    
    private ChatColor getClosestColor(@NonNull final Color color) {
        if (color == null) {
            throw new NullPointerException("color is marked non-null but is null");
        }
        return (ChatColor)DefaultColorProcessor.COLORS.entrySet().stream().sorted(Comparator.comparing(entry -> Math.pow((double)(color.getRed() - ((Color)entry.getKey()).getRed()), 2.0) + Math.pow((double)(color.getRed() - ((Color)entry.getKey()).getRed()), 2.0) + Math.pow((double)(color.getRed() - ((Color)entry.getKey()).getRed()), 2.0))).map(Map.Entry::getValue).findAny().orElseThrow(() -> new RuntimeException("Cannot find closest rgb color to format chat-color. (" + (Object)color + ")"));
    }
    
    private Color hexToRgb(@NonNull final String hex) {
        if (hex == null) {
            throw new NullPointerException("hex is marked non-null but is null");
        }
        return new Color(Integer.parseInt(hex.substring(2), 16));
    }
    
    private String processRgb(@NonNull final String text) {
        if (text == null) {
            throw new NullPointerException("text is marked non-null but is null");
        }
        final Matcher matcher = DefaultColorProcessor.HEX_PATTERN.matcher((CharSequence)text);
        final AtomicReference<String> atomicText = (AtomicReference<String>)new AtomicReference((Object)text);
        while (matcher.find()) {
            final String hex = matcher.group();
            final Color color = this.hexToRgb(hex);
            if (VersionUtil.isSupported(16)) {
                atomicText.set((Object)((String)atomicText.get()).replace((CharSequence)hex, (CharSequence)((Object)ChatColor.of(color) + "")));
            }
            else {
                atomicText.set((Object)((String)atomicText.get()).replace((CharSequence)hex, (CharSequence)((Object)this.getClosestColor(color) + "")));
            }
        }
        return (String)atomicText.get();
    }
    
    static {
        HEX_PATTERN = Pattern.compile("&#([0-9A-Fa-f]{6})");
        COLORS = new MapBuilder<Color, ChatColor>().put(new Color(0), ChatColor.getByChar('0')).put(new Color(170), ChatColor.getByChar('1')).put(new Color(43520), ChatColor.getByChar('2')).put(new Color(43690), ChatColor.getByChar('3')).put(new Color(11141120), ChatColor.getByChar('4')).put(new Color(11141290), ChatColor.getByChar('5')).put(new Color(16755200), ChatColor.getByChar('6')).put(new Color(11184810), ChatColor.getByChar('7')).put(new Color(5592405), ChatColor.getByChar('8')).put(new Color(5592575), ChatColor.getByChar('9')).put(new Color(5635925), ChatColor.getByChar('a')).put(new Color(5636095), ChatColor.getByChar('b')).put(new Color(16733525), ChatColor.getByChar('c')).put(new Color(16733695), ChatColor.getByChar('d')).put(new Color(16777045), ChatColor.getByChar('e')).put(new Color(16777215), ChatColor.getByChar('f')).build();
    }
}
