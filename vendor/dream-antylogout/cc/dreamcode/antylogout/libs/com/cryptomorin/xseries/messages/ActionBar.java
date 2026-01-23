package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.messages;

import java.lang.invoke.MethodType;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.minecraft.MinecraftPackage;
import java.lang.invoke.MethodHandles;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.XReflection;
import java.util.concurrent.Callable;
import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import net.md_5.bungee.api.chat.BaseComponent;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.minecraft.MinecraftConnection;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.ChatMessageType;
import java.util.Objects;
import com.google.common.base.Strings;
import org.jetbrains.annotations.Nullable;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.bukkit.plugin.Plugin;
import java.lang.invoke.MethodHandle;

public final class ActionBar
{
    private static final boolean USE_SPIGOT_API;
    private static final MethodHandle CHAT_COMPONENT_TEXT;
    private static final MethodHandle PACKET_PLAY_OUT_CHAT;
    private static final Object CHAT_MESSAGE_TYPE;
    private static final char TIME_SPECIFIER_START = '^';
    private static final char TIME_SPECIFIER_END = '|';
    
    private ActionBar() {
    }
    
    public static void sendActionBar(@NotNull final Plugin plugin, @NotNull final Player player, @Nullable final String message) {
        if (!Strings.isNullOrEmpty(message) && message.charAt(0) == '^') {
            final int end = message.indexOf(124);
            if (end != -1) {
                int time = 0;
                try {
                    time = Integer.parseInt(message.substring(1, end)) * 20;
                }
                catch (final NumberFormatException ex) {}
                if (time >= 0) {
                    sendActionBar(plugin, player, MessageComponents.fromLegacy(message.substring(end + 1)), time);
                }
            }
        }
        sendActionBar(player, message);
    }
    
    public static void sendActionBar(@NotNull final Player player, @Nullable final String message) {
        Objects.requireNonNull((Object)player, "Cannot send action bar to null player");
        Objects.requireNonNull((Object)message, "Cannot send null actionbar message");
        if (ActionBar.USE_SPIGOT_API) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
            return;
        }
        try {
            final Object component = ActionBar.CHAT_COMPONENT_TEXT.invoke("{\"text\":\"" + message.replace((CharSequence)"\\", (CharSequence)"\\\\").replace((CharSequence)"\"", (CharSequence)"\\\"") + "\"}");
            final Object packet = ActionBar.PACKET_PLAY_OUT_CHAT.invoke(component, ActionBar.CHAT_MESSAGE_TYPE);
            MinecraftConnection.sendPacket(player, packet);
        }
        catch (final Throwable throwable) {
            throwable.printStackTrace();
        }
    }
    
    public static void sendActionBar(@NotNull final Player player, @Nullable final BaseComponent message) {
        Objects.requireNonNull((Object)player, "Cannot send action bar to null player");
        Objects.requireNonNull((Object)message, "Cannot send null actionbar message");
        if (ActionBar.USE_SPIGOT_API) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, message);
            return;
        }
        try {
            final Object component = ActionBar.CHAT_COMPONENT_TEXT.invoke("{\"text\":\"" + message.toPlainText().replace((CharSequence)"\\", (CharSequence)"\\\\").replace((CharSequence)"\"", (CharSequence)"\\\"") + "\"}");
            final Object packet = ActionBar.PACKET_PLAY_OUT_CHAT.invoke(component, ActionBar.CHAT_MESSAGE_TYPE);
            MinecraftConnection.sendPacket(player, packet);
        }
        catch (final Throwable throwable) {
            throwable.printStackTrace();
        }
    }
    
    public static void sendActionBar(@NotNull final Plugin plugin, @NotNull final Player player, @Nullable final BaseComponent message, final long duration) {
        if (duration < 1L) {
            return;
        }
        Objects.requireNonNull((Object)plugin, "Cannot send consistent actionbar with null plugin");
        Objects.requireNonNull((Object)player, "Cannot send actionbar to null player");
        Objects.requireNonNull((Object)message, "Cannot send null actionbar message");
        new BukkitRunnable() {
            long repeater = duration;
            
            public void run() {
                ActionBar.sendActionBar(player, message);
                this.repeater -= 40L;
                if (this.repeater - 40L < -20L) {
                    this.cancel();
                }
            }
        }.runTaskTimerAsynchronously(plugin, 0L, 40L);
    }
    
    @Deprecated
    public static void sendPlayersActionBar(@Nullable final String message) {
        for (final Player player : Bukkit.getOnlinePlayers()) {
            sendActionBar(player, message);
        }
    }
    
    public static void clearActionBar(@NotNull final Player player) {
        sendActionBar(player, " ");
    }
    
    @Deprecated
    public static void clearPlayersActionBar() {
        for (final Player player : Bukkit.getOnlinePlayers()) {
            clearActionBar(player);
        }
    }
    
    public static void sendActionBarWhile(@NotNull final Plugin plugin, @NotNull final Player player, @Nullable final BaseComponent message, @NotNull final Callable<Boolean> callable) {
        new BukkitRunnable() {
            public void run() {
                try {
                    if (!(boolean)callable.call()) {
                        this.cancel();
                        return;
                    }
                }
                catch (final Exception ex) {
                    ex.printStackTrace();
                }
                ActionBar.sendActionBar(player, message);
            }
        }.runTaskTimerAsynchronously(plugin, 0L, 40L);
    }
    
    public static void sendActionBarWhile(@NotNull final Plugin plugin, @NotNull final Player player, @Nullable final Callable<BaseComponent> message, @NotNull final Callable<Boolean> callable) {
        new BukkitRunnable() {
            public void run() {
                try {
                    if (!(boolean)callable.call()) {
                        this.cancel();
                        return;
                    }
                    ActionBar.sendActionBar(player, (BaseComponent)message.call());
                }
                catch (final Exception ex) {
                    ex.printStackTrace();
                }
            }
        }.runTaskTimerAsynchronously(plugin, 0L, 40L);
    }
    
    static {
        USE_SPIGOT_API = XReflection.of(Player.class).inner("public static class Spigot").method("public void sendMessage(net.md_5.bungee.api.ChatMessageType position,net.md_5.bungee.api.chat.BaseComponent component)").exists();
        MethodHandle packet = null;
        MethodHandle chatComp = null;
        Object chatMsgType = null;
        if (!ActionBar.USE_SPIGOT_API) {
            final MethodHandles.Lookup lookup = MethodHandles.lookup();
            final Class<?> packetPlayOutChatClass = XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "network.protocol.game").named("PacketPlayOutChat").unreflect();
            final Class<?> iChatBaseComponentClass = XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "network.chat").named("IChatBaseComponent").unreflect();
            final Class<?> ChatSerializerClass = XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "network.chat").named("IChatBaseComponent$ChatSerializer").unreflect();
            try {
                chatComp = lookup.findStatic((Class)ChatSerializerClass, "a", MethodType.methodType((Class)iChatBaseComponentClass, (Class)String.class));
                final Class<?> chatMessageTypeClass = Class.forName(XReflection.NMS_PACKAGE + (String)XReflection.v(17, "network.chat").orElse("") + "ChatMessageType");
                final MethodType type = MethodType.methodType(Void.TYPE, (Class)iChatBaseComponentClass, new Class[] { chatMessageTypeClass });
                for (final Object obj : chatMessageTypeClass.getEnumConstants()) {
                    final String name = obj.toString();
                    if (name.equals((Object)"GAME_INFO") || name.equalsIgnoreCase("ACTION_BAR")) {
                        chatMsgType = obj;
                        break;
                    }
                }
                packet = lookup.findConstructor((Class)packetPlayOutChatClass, type);
            }
            catch (final NoSuchMethodException | IllegalAccessException | ClassNotFoundException ignored) {
                try {
                    chatMsgType = 2;
                    packet = lookup.findConstructor((Class)packetPlayOutChatClass, MethodType.methodType(Void.TYPE, (Class)iChatBaseComponentClass, new Class[] { Byte.TYPE }));
                }
                catch (final NoSuchMethodException | IllegalAccessException ex) {
                    ex.printStackTrace();
                }
            }
        }
        CHAT_MESSAGE_TYPE = chatMsgType;
        CHAT_COMPONENT_TEXT = chatComp;
        PACKET_PLAY_OUT_CHAT = packet;
    }
}
