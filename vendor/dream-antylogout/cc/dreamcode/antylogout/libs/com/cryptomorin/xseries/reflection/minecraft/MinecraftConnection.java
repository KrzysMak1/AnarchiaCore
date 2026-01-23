package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.minecraft;

import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm.classes.ClassHandle;
import java.util.Arrays;
import org.jetbrains.annotations.Nullable;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.XReflection;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.bukkit.entity.Player;
import java.lang.invoke.MethodHandle;

public final class MinecraftConnection
{
    public static final MinecraftClassHandle ServerPlayer;
    public static final MinecraftClassHandle CraftPlayer;
    public static final MinecraftClassHandle ServerPlayerConnection;
    public static final MinecraftClassHandle ServerGamePacketListenerImpl;
    public static final MinecraftClassHandle Packet;
    private static final MethodHandle PLAYER_CONNECTION;
    private static final MethodHandle GET_HANDLE;
    private static final MethodHandle SEND_PACKET;
    
    @NotNull
    public static Object getHandle(@NotNull final Player player) {
        Objects.requireNonNull((Object)player, "Cannot get handle of null player");
        try {
            return MinecraftConnection.GET_HANDLE.invoke(player);
        }
        catch (final Throwable throwable) {
            throw XReflection.throwCheckedException(throwable);
        }
    }
    
    @Nullable
    public static Object getConnection(@NotNull final Player player) {
        Objects.requireNonNull((Object)player, "Cannot get connection of null player");
        try {
            final Object handle = MinecraftConnection.GET_HANDLE.invoke(player);
            return MinecraftConnection.PLAYER_CONNECTION.invoke(handle);
        }
        catch (final Throwable throwable) {
            throw XReflection.throwCheckedException(throwable);
        }
    }
    
    @NotNull
    public static void sendPacket(@NotNull final Player player, @NotNull final Object... packets) {
        Objects.requireNonNull((Object)player, () -> "Can't send packet to null player: " + Arrays.toString(packets));
        Objects.requireNonNull((Object)packets, () -> "Can't send null packets to player: " + (Object)player);
        try {
            final Object handle = MinecraftConnection.GET_HANDLE.invoke(player);
            final Object connection = MinecraftConnection.PLAYER_CONNECTION.invoke(handle);
            if (connection != null) {
                for (final Object packet : packets) {
                    Objects.requireNonNull(packet, "Null packet detected between packets array");
                    MinecraftConnection.SEND_PACKET.invoke(connection, packet);
                }
            }
        }
        catch (final Throwable throwable) {
            throw new IllegalStateException("Failed to send packet to " + (Object)player + ": " + Arrays.toString(packets), throwable);
        }
    }
    
    static {
        ServerPlayer = XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "server.level").map(MinecraftMapping.MOJANG, "ServerPlayer").map(MinecraftMapping.SPIGOT, "EntityPlayer");
        CraftPlayer = XReflection.ofMinecraft().inPackage(MinecraftPackage.CB, "entity").named("CraftPlayer");
        ServerPlayerConnection = XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "server.network").map(MinecraftMapping.MOJANG, "ServerPlayerConnection").map(MinecraftMapping.SPIGOT, "PlayerConnection");
        ServerGamePacketListenerImpl = XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "server.network").map(MinecraftMapping.MOJANG, "ServerGamePacketListenerImpl").map(MinecraftMapping.SPIGOT, "PlayerConnection");
        Packet = XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "network.protocol").map(MinecraftMapping.SPIGOT, "Packet");
        PLAYER_CONNECTION = MinecraftConnection.ServerPlayer.field().getter().returns(MinecraftConnection.ServerGamePacketListenerImpl).map(MinecraftMapping.MOJANG, "connection").map(MinecraftMapping.OBFUSCATED, XReflection.v(21, 2, "f").v(20, "c").v(17, "b").orElse("playerConnection")).unreflect();
        GET_HANDLE = MinecraftConnection.CraftPlayer.method().named("getHandle").returns(MinecraftConnection.ServerPlayer).unreflect();
        SEND_PACKET = MinecraftConnection.ServerPlayerConnection.method().returns(Void.TYPE).parameters(MinecraftConnection.Packet).map(MinecraftMapping.MOJANG, "send").map(MinecraftMapping.OBFUSCATED, XReflection.v(20, 2, "b").v(18, "a").orElse("sendPacket")).unreflect();
    }
}
