package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.messages;

import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.ReflectiveHandle;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.minecraft.MinecraftClassHandle;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm.classes.ClassHandle;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.minecraft.MinecraftPackage;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.XReflection;
import java.util.function.Function;
import org.bukkit.configuration.ConfigurationSection;
import java.util.List;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.minecraft.MinecraftConnection;
import java.util.ArrayList;
import java.util.Objects;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import org.bukkit.entity.Player;
import net.md_5.bungee.api.chat.BaseComponent;
import java.lang.invoke.MethodHandle;

public final class Titles
{
    private static final Object TITLE_ACTION_TITLE;
    private static final Object TITLE_ACTION_SUBTITLE;
    private static final Object TITLE_ACTION_TIMES;
    private static final Object TITLE_ACTION_CLEAR;
    private static final MethodHandle PACKET_PLAY_OUT_TITLE;
    private static final MethodHandle CHAT_COMPONENT_TEXT;
    private static final MethodHandle ClientboundSetTitlesAnimationPacket;
    private static final MethodHandle ClientboundSetTitleTextPacket;
    private static final MethodHandle ClientboundSetSubtitleTextPacket;
    private MessageComponents.MessageText title;
    private MessageComponents.MessageText subtitle;
    private final int fadeIn;
    private final int stay;
    private final int fadeOut;
    private static final boolean SUPPORTS_TITLES;
    private static final boolean USE_TEXT_COMPONENTS;
    
    public Titles(final MessageComponents.MessageText title, final MessageComponents.MessageText subtitle, final int fadeIn, final int stay, final int fadeOut) {
        this.title = title;
        this.subtitle = subtitle;
        this.fadeIn = fadeIn;
        this.stay = stay;
        this.fadeOut = fadeOut;
    }
    
    public Titles(final BaseComponent title, final BaseComponent subtitle, final int fadeIn, final int stay, final int fadeOut) {
        this(new MessageComponents.MessageTextComponent(title), new MessageComponents.MessageTextComponent(subtitle), fadeIn, stay, fadeOut);
    }
    
    public Titles(final String title, final String subtitle, final int fadeIn, final int stay, final int fadeOut) {
        this(new MessageComponents.MessageTextString(title), new MessageComponents.MessageTextString(subtitle), fadeIn, stay, fadeOut);
    }
    
    public Titles copy() {
        return new Titles(this.title, this.subtitle, this.fadeIn, this.stay, this.fadeOut);
    }
    
    public void send(final Player player) {
        sendTitle(player, this.fadeIn, this.stay, this.fadeOut, this.title, this.subtitle);
    }
    
    public static void sendTitle(@NotNull final Player player, final int fadeIn, final int stay, final int fadeOut, @Nullable final String title, @Nullable final String subtitle) {
        sendTitle(player, fadeIn, stay, fadeOut, new MessageComponents.MessageTextString(title), new MessageComponents.MessageTextString(subtitle));
    }
    
    public static void sendTitle(@NotNull final Player player, final int fadeIn, final int stay, final int fadeOut, @Nullable final MessageComponents.MessageText title, @Nullable final MessageComponents.MessageText subtitle) {
        Objects.requireNonNull((Object)player, "Cannot send title to null player");
        if (title == null && subtitle == null) {
            return;
        }
        if (Titles.USE_TEXT_COMPONENTS) {
            final List<Object> packets = (List<Object>)new ArrayList(3);
            try {
                packets.add(Titles.ClientboundSetTitlesAnimationPacket.invoke(fadeIn, stay, fadeOut));
                if (title != null) {
                    packets.add(Titles.ClientboundSetTitleTextPacket.invoke(MessageComponents.bungeeToVanilla(title.asComponent())));
                }
                if (subtitle != null) {
                    packets.add(Titles.ClientboundSetSubtitleTextPacket.invoke(MessageComponents.bungeeToVanilla(subtitle.asComponent())));
                }
            }
            catch (final Throwable ex) {
                throw new IllegalStateException("Failed to create packets with title: " + (Object)title + " and subtitle: " + (Object)subtitle, ex);
            }
            MinecraftConnection.sendPacket(player, packets.toArray(new Object[0]));
            return;
        }
        if (Titles.SUPPORTS_TITLES) {
            player.sendTitle(title.asString(), subtitle.asString(), fadeIn, stay, fadeOut);
            return;
        }
        try {
            final List<Object> packets = (List<Object>)new ArrayList(3);
            final Object titleComponent = Titles.CHAT_COMPONENT_TEXT.invoke((Object)((title == null) ? null : title.asString()));
            packets.add(Titles.PACKET_PLAY_OUT_TITLE.invoke(Titles.TITLE_ACTION_TIMES, titleComponent, fadeIn, stay, fadeOut));
            if (title != null) {
                packets.add(Titles.PACKET_PLAY_OUT_TITLE.invoke(Titles.TITLE_ACTION_TITLE, titleComponent, fadeIn, stay, fadeOut));
            }
            if (subtitle != null) {
                final Object subtitleComponent = Titles.CHAT_COMPONENT_TEXT.invoke(subtitle.asString());
                packets.add(Titles.PACKET_PLAY_OUT_TITLE.invoke(Titles.TITLE_ACTION_SUBTITLE, subtitleComponent, fadeIn, stay, fadeOut));
            }
            MinecraftConnection.sendPacket(player, packets.toArray(new Object[0]));
        }
        catch (final Throwable ex2) {
            throw new IllegalStateException("Failed to send packets for title: " + (Object)title + " and subtitle: " + (Object)subtitle, ex2);
        }
    }
    
    public static void sendTitle(@NotNull final Player player, @NotNull final String title, @NotNull final String subtitle) {
        sendTitle(player, 10, 20, 10, title, subtitle);
    }
    
    public static Titles sendTitle(@NotNull final Player player, @NotNull final ConfigurationSection config) {
        final Titles titles = parseTitle(config, null);
        titles.send(player);
        return titles;
    }
    
    public static Titles parseTitle(@NotNull final ConfigurationSection config) {
        return parseTitle(config, null);
    }
    
    public static Titles parseTitle(@NotNull final ConfigurationSection config, @Nullable final Function<String, String> transformers) {
        String title = config.getString("title");
        String subtitle = config.getString("subtitle");
        if (transformers != null) {
            title = (String)transformers.apply((Object)title);
            subtitle = (String)transformers.apply((Object)subtitle);
        }
        int fadeIn = config.getInt("fade-in");
        int stay = config.getInt("stay");
        int fadeOut = config.getInt("fade-out");
        if (fadeIn < 1) {
            fadeIn = 10;
        }
        if (stay < 1) {
            stay = 20;
        }
        if (fadeOut < 1) {
            fadeOut = 10;
        }
        return new Titles(title, subtitle, fadeIn, stay, fadeOut);
    }
    
    public MessageComponents.MessageText getTitle() {
        return this.title;
    }
    
    public MessageComponents.MessageText getSubtitle() {
        return this.subtitle;
    }
    
    public void setTitle(final String title) {
        this.title = new MessageComponents.MessageTextString(title);
    }
    
    public void setSubtitle(final String subtitle) {
        this.subtitle = new MessageComponents.MessageTextString(subtitle);
    }
    
    public void setTitle(final BaseComponent title) {
        this.title = new MessageComponents.MessageTextComponent(title);
    }
    
    public void setSubtitle(final BaseComponent subtitle) {
        this.subtitle = new MessageComponents.MessageTextComponent(subtitle);
    }
    
    public static void clearTitle(@NotNull final Player player) {
        Objects.requireNonNull((Object)player, "Cannot clear title from null player");
        if (XReflection.supports(11)) {
            player.resetTitle();
            return;
        }
        Object clearPacket;
        try {
            clearPacket = Titles.PACKET_PLAY_OUT_TITLE.invoke(Titles.TITLE_ACTION_CLEAR, (Void)null, -1, -1, -1);
        }
        catch (final Throwable throwable) {
            throwable.printStackTrace();
            return;
        }
        MinecraftConnection.sendPacket(player, clearPacket);
    }
    
    public static void sendTabList(@NotNull final String header, @NotNull final String footer, final Player... players) {
        Objects.requireNonNull((Object)players, "Cannot send tab title to null players");
        Objects.requireNonNull((Object)header, "Tab title header cannot be null");
        Objects.requireNonNull((Object)footer, "Tab title footer cannot be null");
        if (XReflection.supports(13)) {
            for (final Player player : players) {
                player.setPlayerListHeaderFooter(header, footer);
            }
            return;
        }
        try {
            final Class<?> IChatBaseComponent = XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "network.chat").named("IChatBaseComponent").unreflect();
            final Class<?> PacketPlayOutPlayerListHeaderFooter = XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "network.protocol.game").named("PacketPlayOutPlayerListHeaderFooter").unreflect();
            final Method chatComponentBuilderMethod = IChatBaseComponent.getDeclaredClasses()[0].getMethod("a", String.class);
            final Object tabHeader = chatComponentBuilderMethod.invoke((Object)null, new Object[] { "{\"text\":\"" + header + "\"}" });
            final Object tabFooter = chatComponentBuilderMethod.invoke((Object)null, new Object[] { "{\"text\":\"" + footer + "\"}" });
            final Object packet = PacketPlayOutPlayerListHeaderFooter.getConstructor((Class<?>[])new Class[0]).newInstance(new Object[0]);
            final Field headerField = PacketPlayOutPlayerListHeaderFooter.getDeclaredField("a");
            final Field footerField = PacketPlayOutPlayerListHeaderFooter.getDeclaredField("b");
            headerField.setAccessible(true);
            headerField.set(packet, tabHeader);
            footerField.setAccessible(true);
            footerField.set(packet, tabFooter);
            for (final Player player2 : players) {
                MinecraftConnection.sendPacket(player2, packet);
            }
        }
        catch (final Exception ex) {
            throw new IllegalStateException("Failed to send tablist: " + header + " - " + footer, (Throwable)ex);
        }
    }
    
    static {
        MethodHandle packetCtor = null;
        MethodHandle chatComp = null;
        MethodHandle animationCtor = null;
        MethodHandle titleCtor = null;
        MethodHandle subtitleCtor = null;
        Object times = null;
        Object title = null;
        Object subtitle = null;
        Object clear = null;
        final MinecraftClassHandle IChatBaseComponentClass = XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "network.chat").named("IChatBaseComponent");
        boolean useTextComponents;
        try {
            animationCtor = XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "network.protocol.game").named("ClientboundSetTitlesAnimationPacket").constructor(Integer.TYPE, Integer.TYPE, Integer.TYPE).reflect();
            titleCtor = XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "network.protocol.game").named("ClientboundSetTitleTextPacket").constructor(IChatBaseComponentClass).reflect();
            subtitleCtor = XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "network.protocol.game").named("ClientboundSetSubtitleTextPacket").constructor(IChatBaseComponentClass).reflect();
            useTextComponents = true;
        }
        catch (final Throwable ex) {
            useTextComponents = false;
        }
        boolean supportsTitles;
        try {
            Player.class.getDeclaredMethod("sendTitle", String.class, String.class, Integer.TYPE, Integer.TYPE, Integer.TYPE);
            supportsTitles = true;
        }
        catch (final NoSuchMethodException e) {
            supportsTitles = false;
        }
        if (!(SUPPORTS_TITLES = supportsTitles)) {
            final MinecraftClassHandle chatComponentText = XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS).named("ChatComponentText");
            final MinecraftClassHandle packet = XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS).named("PacketPlayOutTitle");
            final Class<?> titleTypes = ((ReflectiveHandle<Class>)packet).unreflect().getDeclaredClasses()[0];
            for (final Object type : titleTypes.getEnumConstants()) {
                final String string = type.toString();
                int n = -1;
                switch (string.hashCode()) {
                    case 79826726: {
                        if (string.equals((Object)"TIMES")) {
                            n = 0;
                            break;
                        }
                        break;
                    }
                    case 79833656: {
                        if (string.equals((Object)"TITLE")) {
                            n = 1;
                            break;
                        }
                        break;
                    }
                    case -1277871080: {
                        if (string.equals((Object)"SUBTITLE")) {
                            n = 2;
                            break;
                        }
                        break;
                    }
                    case 64208429: {
                        if (string.equals((Object)"CLEAR")) {
                            n = 3;
                            break;
                        }
                        break;
                    }
                }
                switch (n) {
                    case 0: {
                        times = type;
                        break;
                    }
                    case 1: {
                        title = type;
                        break;
                    }
                    case 2: {
                        subtitle = type;
                        break;
                    }
                    case 3: {
                        clear = type;
                        break;
                    }
                }
            }
            try {
                chatComp = chatComponentText.constructor(String.class).reflect();
                packetCtor = packet.constructor(titleTypes, ((ReflectiveHandle<Class>)IChatBaseComponentClass).unreflect(), Integer.TYPE, Integer.TYPE, Integer.TYPE).reflect();
            }
            catch (final ReflectiveOperationException e2) {
                e2.printStackTrace();
            }
        }
        TITLE_ACTION_TITLE = title;
        TITLE_ACTION_SUBTITLE = subtitle;
        TITLE_ACTION_TIMES = times;
        TITLE_ACTION_CLEAR = clear;
        PACKET_PLAY_OUT_TITLE = packetCtor;
        CHAT_COMPONENT_TEXT = chatComp;
        USE_TEXT_COMPONENTS = useTextComponents;
        ClientboundSetTitlesAnimationPacket = animationCtor;
        ClientboundSetTitleTextPacket = titleCtor;
        ClientboundSetSubtitleTextPacket = subtitleCtor;
    }
}
