package cc.dreamcode.antylogout.libs.net.kyori.adventure.text;

import cc.dreamcode.antylogout.libs.net.kyori.adventure.text.format.StyleSetter;
import cc.dreamcode.antylogout.libs.net.kyori.adventure.key.Key;
import cc.dreamcode.antylogout.libs.net.kyori.adventure.text.event.HoverEventSource;
import cc.dreamcode.antylogout.libs.net.kyori.adventure.text.event.ClickEvent;
import java.util.Map;
import cc.dreamcode.antylogout.libs.net.kyori.adventure.text.format.TextDecoration;
import cc.dreamcode.antylogout.libs.net.kyori.adventure.util.ARGBLike;
import org.jetbrains.annotations.Nullable;
import cc.dreamcode.antylogout.libs.net.kyori.adventure.text.format.TextColor;
import java.util.Set;
import cc.dreamcode.antylogout.libs.net.kyori.adventure.text.format.StyleBuilderApplicable;
import java.util.function.Consumer;
import cc.dreamcode.antylogout.libs.net.kyori.adventure.text.format.Style;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public interface ScopedComponent<C extends Component> extends Component
{
    @NotNull
    default C asComponent() {
        return (C)super.asComponent();
    }
    
    @NotNull
    C children(@NotNull final List<? extends ComponentLike> children);
    
    @NotNull
    C style(@NotNull final Style style);
    
    @NotNull
    default C style(@NotNull final Consumer<Style.Builder> style) {
        return (C)super.style(style);
    }
    
    @NotNull
    default C style(final Style.Builder style) {
        return (C)super.style(style);
    }
    
    @NotNull
    default C style(@NotNull final Consumer<Style.Builder> consumer, final Style.Merge.Strategy strategy) {
        return (C)super.style(consumer, strategy);
    }
    
    @NotNull
    default C mergeStyle(@NotNull final Component that) {
        return (C)super.mergeStyle(that);
    }
    
    @NotNull
    default C mergeStyle(@NotNull final Component that, final Style.Merge... merges) {
        return (C)super.mergeStyle(that, merges);
    }
    
    @NotNull
    default C append(@NotNull final Component component) {
        return (C)super.append(component);
    }
    
    @NotNull
    default C append(@NotNull final ComponentLike like) {
        return (C)super.append(like);
    }
    
    @NotNull
    default C append(@NotNull final ComponentBuilder<?, ?> builder) {
        return (C)super.append(builder);
    }
    
    @NotNull
    default C append(@NotNull final List<? extends ComponentLike> components) {
        return (C)super.append(components);
    }
    
    @NotNull
    default C append(@NotNull final ComponentLike... components) {
        return (C)super.append(components);
    }
    
    @NotNull
    default C appendNewline() {
        return (C)super.appendNewline();
    }
    
    @NotNull
    default C appendSpace() {
        return (C)super.appendSpace();
    }
    
    @NotNull
    default C applyFallbackStyle(@NotNull final StyleBuilderApplicable... style) {
        return (C)super.applyFallbackStyle(style);
    }
    
    @NotNull
    default C applyFallbackStyle(@NotNull final Style style) {
        return (C)super.applyFallbackStyle(style);
    }
    
    @NotNull
    default C mergeStyle(@NotNull final Component that, @NotNull final Set<Style.Merge> merges) {
        return (C)super.mergeStyle(that, merges);
    }
    
    @NotNull
    default C color(@Nullable final TextColor color) {
        return (C)super.color(color);
    }
    
    @NotNull
    default C colorIfAbsent(@Nullable final TextColor color) {
        return (C)super.colorIfAbsent(color);
    }
    
    @NotNull
    default C shadowColor(@Nullable final ARGBLike argb) {
        return (C)super.shadowColor(argb);
    }
    
    @NotNull
    default C shadowColorIfAbsent(@Nullable final ARGBLike argb) {
        return (C)super.shadowColorIfAbsent(argb);
    }
    
    @NotNull
    default C decorate(@NotNull final TextDecoration decoration) {
        return (C)super.decorate(decoration);
    }
    
    @NotNull
    default C decoration(@NotNull final TextDecoration decoration, final boolean flag) {
        return (C)super.decoration(decoration, flag);
    }
    
    @NotNull
    default C decoration(@NotNull final TextDecoration decoration, final TextDecoration.State state) {
        return (C)super.decoration(decoration, state);
    }
    
    @NotNull
    default C decorationIfAbsent(@NotNull final TextDecoration decoration, final TextDecoration.State state) {
        return (C)super.decorationIfAbsent(decoration, state);
    }
    
    @NotNull
    default C decorations(@NotNull final Map<TextDecoration, TextDecoration.State> decorations) {
        return (C)super.decorations(decorations);
    }
    
    @NotNull
    default C clickEvent(@Nullable final ClickEvent event) {
        return (C)super.clickEvent(event);
    }
    
    @NotNull
    default C hoverEvent(@Nullable final HoverEventSource<?> event) {
        return (C)super.hoverEvent(event);
    }
    
    @NotNull
    default C insertion(@Nullable final String insertion) {
        return (C)super.insertion(insertion);
    }
    
    @NotNull
    default C font(@Nullable final Key key) {
        return (C)super.font(key);
    }
}
