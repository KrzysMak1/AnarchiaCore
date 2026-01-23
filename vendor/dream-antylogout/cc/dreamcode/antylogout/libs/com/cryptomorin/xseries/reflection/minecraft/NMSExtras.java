package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.minecraft;

import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.ReflectiveHandle;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;
import java.lang.invoke.MethodType;
import java.lang.invoke.MethodHandles;
import org.bukkit.Material;
import org.bukkit.DyeColor;
import java.lang.reflect.Array;
import java.util.Map;
import org.bukkit.Chunk;
import java.util.concurrent.Callable;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import java.lang.reflect.Field;
import org.jetbrains.annotations.Nullable;
import java.util.Objects;
import org.bukkit.entity.Entity;
import java.util.Iterator;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.XReflection;
import java.util.Collection;
import java.util.Collections;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import java.lang.invoke.MethodHandle;

public final class NMSExtras
{
    public static final Class<?> EntityLiving;
    private static final MethodHandle GET_ENTITY_HANDLE;
    public static final MethodHandle EXP_PACKET;
    public static final MethodHandle ENTITY_PACKET;
    public static final MethodHandle WORLD_HANDLE;
    public static final MethodHandle ENTITY_HANDLE;
    public static final MethodHandle LIGHTNING_ENTITY;
    public static final MethodHandle VEC3D;
    public static final MethodHandle GET_DATA_WATCHER;
    public static final MethodHandle DATA_WATCHER_GET_ITEM;
    public static final MethodHandle DATA_WATCHER_SET_ITEM;
    public static final MethodHandle PACKET_PLAY_OUT_OPEN_SIGN_EDITOR;
    public static final MethodHandle PACKET_PLAY_OUT_BLOCK_CHANGE;
    public static final MethodHandle ANIMATION_PACKET;
    public static final MethodHandle ANIMATION_TYPE;
    public static final MethodHandle ANIMATION_ENTITY_ID;
    public static final MethodHandle PLAY_OUT_MULTI_BLOCK_CHANGE_PACKET;
    public static final MethodHandle MULTI_BLOCK_CHANGE_INFO;
    public static final MethodHandle CHUNK_WRAPPER_SET;
    public static final MethodHandle CHUNK_WRAPPER;
    public static final MethodHandle SHORTS_OR_INFO;
    public static final MethodHandle SET_BlockState;
    public static final MethodHandle BLOCK_POSITION;
    public static final MethodHandle PLAY_BLOCK_ACTION;
    public static final MethodHandle GET_BUKKIT_ENTITY;
    public static final MethodHandle GET_BLOCK_TYPE;
    public static final MethodHandle GET_BLOCK;
    public static final MethodHandle GET_IBlockState;
    public static final MethodHandle SANITIZE_LINES;
    public static final MethodHandle TILE_ENTITY_SIGN;
    public static final MethodHandle TILE_ENTITY_SIGN__GET_UPDATE_PACKET;
    public static final MethodHandle TILE_ENTITY_SIGN__SET_LINE;
    public static final MethodHandle SIGN_TEXT;
    public static final Class<?> BlockState;
    public static final Class<?> MULTI_BLOCK_CHANGE_INFO_CLASS;
    
    private NMSExtras() {
    }
    
    public static void setExp(final Player player, final float bar, final int lvl, final int exp) {
        try {
            final Object packet = NMSExtras.EXP_PACKET.invoke(bar, lvl, exp);
            MinecraftConnection.sendPacket(player, packet);
        }
        catch (final Throwable ex) {
            ex.printStackTrace();
        }
    }
    
    public static void lightning(final Player player, final Location location, final boolean sound) {
        lightning((Collection<Player>)Collections.singletonList((Object)player), location, sound);
    }
    
    public static void lightning(final Collection<Player> players, final Location location, final boolean sound) {
        try {
            final Object world = NMSExtras.WORLD_HANDLE.invoke(location.getWorld());
            if (!XReflection.supports(16)) {
                final Object lightningBolt = NMSExtras.LIGHTNING_ENTITY.invoke(world, location.getX(), location.getY(), location.getZ(), false, false);
                final Object packet = NMSExtras.ENTITY_PACKET.invoke(lightningBolt);
                for (final Player player : players) {
                    MinecraftConnection.sendPacket(player, packet);
                }
            }
            else {
                final Class<?> nmsEntityType = XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "world.entity").map(MinecraftMapping.MOJANG, "EntityType").map(MinecraftMapping.SPIGOT, "EntityTypes").unreflect();
                final Object lightningType = nmsEntityType.getField(XReflection.supports(17) ? "U" : "LIGHTNING_BOLT").get((Object)nmsEntityType);
                final Object lightningBolt2 = NMSExtras.LIGHTNING_ENTITY.invoke(lightningType, world);
                final Object lightningBoltID = lightningBolt2.getClass().getMethod("getId", (Class<?>[])new Class[0]).invoke(lightningBolt2, new Object[0]);
                final Object lightningBoltUUID = lightningBolt2.getClass().getMethod("getUniqueID", (Class<?>[])new Class[0]).invoke(lightningBolt2, new Object[0]);
                final Object vec3D = NMSExtras.VEC3D.invoke(0.0, 0.0, 0.0);
                final Object packet2 = NMSExtras.ENTITY_PACKET.invoke(lightningBoltID, lightningBoltUUID, location.getX(), location.getY(), location.getZ(), 0.0f, 0.0f, lightningType, 0, vec3D);
                for (final Player player2 : players) {
                    MinecraftConnection.sendPacket(player2, packet2);
                }
            }
        }
        catch (final Throwable throwable) {
            throwable.printStackTrace();
        }
    }
    
    public static Object getData(final Object dataWatcher, final Object dataWatcherObject) {
        try {
            return NMSExtras.DATA_WATCHER_GET_ITEM.invoke(dataWatcher, dataWatcherObject);
        }
        catch (final Throwable e) {
            throw new IllegalArgumentException("Failed to create data watcher", e);
        }
    }
    
    @Nullable
    public static Object getEntityHandle(final Entity entity) {
        Objects.requireNonNull((Object)entity, "Cannot get handle of null entity");
        try {
            return NMSExtras.GET_ENTITY_HANDLE.invoke(entity);
        }
        catch (final Throwable throwable) {
            throwable.printStackTrace();
            return null;
        }
    }
    
    public static Object getDataWatcher(final Object handle) {
        try {
            return NMSExtras.GET_DATA_WATCHER.invoke(handle);
        }
        catch (final Throwable e) {
            throw new IllegalArgumentException("Failed to get data watcher", e);
        }
    }
    
    public static Object setData(final Object dataWatcher, final Object dataWatcherObject, final Object value) {
        try {
            return NMSExtras.DATA_WATCHER_SET_ITEM.invoke(dataWatcher, dataWatcherObject, value);
        }
        catch (final Throwable e) {
            throw new IllegalArgumentException("Failed to set data watcher item", e);
        }
    }
    
    public static Object getStaticFieldIgnored(final Class<?> clazz, final String name) {
        return getStaticField(clazz, name, true);
    }
    
    public static Object getStaticField(final Class<?> clazz, final String name, final boolean silent) {
        try {
            final Field field = clazz.getDeclaredField(name);
            field.setAccessible(true);
            return field.get((Object)null);
        }
        catch (final Throwable e) {
            if (!silent) {
                throw new IllegalArgumentException("Failed to get static field of " + (Object)clazz + " named " + name, e);
            }
            return null;
        }
    }
    
    public static void spinEntity(final LivingEntity entity, final boolean enabled) {
        if (!EntityPose.SPIN_ATTACK.isSupported()) {
            throw new UnsupportedOperationException("Spin attacks are not supported in " + XReflection.getVersionInformation());
        }
        setLivingEntityFlag((Entity)entity, LivingEntityFlags.SPIN_ATTACK.getBit(), enabled);
    }
    
    public static void setLivingEntityFlag(final Entity entity, final int index, final boolean flag) {
        final Object handle = getEntityHandle(entity);
        final Object dataWatcher = getDataWatcher(handle);
        final Object flagItem = DataWatcherItemType.DATA_LIVING_ENTITY_FLAGS.getId();
        final byte currentFlags = (byte)getData(dataWatcher, flagItem);
        int newFlags;
        if (flag) {
            newFlags = (currentFlags | index);
        }
        else {
            newFlags = (currentFlags & ~index);
        }
        setData(dataWatcher, flagItem, (byte)newFlags);
    }
    
    public static boolean hasLivingEntityFlag(final Entity entity, final int index) {
        final Object handle = getEntityHandle(entity);
        final Object dataWatcher = getDataWatcher(handle);
        final byte flags = (byte)getData(dataWatcher, DataWatcherItemType.DATA_LIVING_ENTITY_FLAGS.getId());
        return (flags & index) != 0x0;
    }
    
    public boolean isAutoSpinAttack(final LivingEntity entity) {
        return hasLivingEntityFlag((Entity)entity, LivingEntityFlags.SPIN_ATTACK.getBit());
    }
    
    public static void animation(final Collection<? extends Player> players, final LivingEntity entity, final Animation animation) {
        try {
            Object packet;
            if (XReflection.supports(17)) {
                packet = NMSExtras.ANIMATION_PACKET.invoke(NMSExtras.ENTITY_HANDLE.invoke(entity), animation.ordinal());
            }
            else {
                packet = NMSExtras.ANIMATION_PACKET.invoke();
                NMSExtras.ANIMATION_TYPE.invoke(packet, animation.ordinal());
                NMSExtras.ANIMATION_ENTITY_ID.invoke(packet, entity.getEntityId());
            }
            for (final Player player : players) {
                MinecraftConnection.sendPacket(player, packet);
            }
        }
        catch (final Throwable throwable) {
            throwable.printStackTrace();
        }
    }
    
    public static void chest(final Block chest, final boolean open) {
        final Location location = chest.getLocation();
        try {
            final Object world = NMSExtras.WORLD_HANDLE.invoke(location.getWorld());
            final Object position = XReflection.v(19, (java.util.concurrent.Callable<Object>)(() -> {
                try {
                    return NMSExtras.BLOCK_POSITION.invoke(location.getBlockX(), location.getBlockY(), location.getBlockZ());
                }
                catch (final Throwable e) {
                    throw new IllegalArgumentException("Failed to set block position", e);
                }
            })).orElse((java.util.concurrent.Callable<Object>)(() -> {
                try {
                    return NMSExtras.BLOCK_POSITION.invoke(location.getX(), location.getY(), location.getZ());
                }
                catch (final Throwable e) {
                    throw new IllegalArgumentException("Failed to set block position", e);
                }
            }));
            final Object block = NMSExtras.GET_BLOCK.invoke(NMSExtras.GET_BLOCK_TYPE.invoke(world, position));
            NMSExtras.PLAY_BLOCK_ACTION.invoke(world, position, block, 1, (int)(open ? 1 : 0));
        }
        catch (final Throwable throwable) {
            throwable.printStackTrace();
        }
    }
    
    @Deprecated
    protected static void sendBlockChange(final Player player, final Chunk chunk, final Map<WorldlessBlockWrapper, Object> blocks) {
        try {
            final Object packet = NMSExtras.PLAY_OUT_MULTI_BLOCK_CHANGE_PACKET.invoke();
            if (XReflection.supports(16)) {
                final Object wrapper = NMSExtras.CHUNK_WRAPPER.invoke(chunk.getX(), chunk.getZ());
                NMSExtras.CHUNK_WRAPPER_SET.invoke(wrapper);
                final Object dataArray = Array.newInstance((Class)NMSExtras.BlockState, blocks.size());
                final Object shortArray = Array.newInstance(Short.TYPE, blocks.size());
                int i = 0;
                for (final Map.Entry<WorldlessBlockWrapper, Object> entry : blocks.entrySet()) {
                    final Block loc = ((WorldlessBlockWrapper)entry.getKey()).block;
                    final int x = loc.getX() & 0xF;
                    final int y = loc.getY() & 0xF;
                    final int z = loc.getZ() & 0xF;
                    ++i;
                }
                NMSExtras.SHORTS_OR_INFO.invoke(packet, shortArray);
                NMSExtras.SET_BlockState.invoke(packet, dataArray);
            }
            else {
                final Object wrapper = NMSExtras.CHUNK_WRAPPER.invoke(chunk.getX(), chunk.getZ());
                NMSExtras.CHUNK_WRAPPER_SET.invoke(wrapper);
                final Object array = Array.newInstance((Class)NMSExtras.MULTI_BLOCK_CHANGE_INFO_CLASS, blocks.size());
                int j = 0;
                for (final Map.Entry<WorldlessBlockWrapper, Object> entry2 : blocks.entrySet()) {
                    final Block loc2 = ((WorldlessBlockWrapper)entry2.getKey()).block;
                    final int x2 = loc2.getX() & 0xF;
                    final int z2 = loc2.getZ() & 0xF;
                    ++j;
                }
                NMSExtras.SHORTS_OR_INFO.invoke(packet, array);
            }
            MinecraftConnection.sendPacket(player, packet);
        }
        catch (final Throwable throwable) {
            throwable.printStackTrace();
        }
    }
    
    public static void openSign(final Player player, final DyeColor textColor, final String[] lines, final boolean frontSide) {
        try {
            final Location loc = player.getLocation();
            final Object position = NMSExtras.BLOCK_POSITION.invoke(loc.getBlockX(), 1, loc.getBlockY());
            final Object signBlockData = NMSExtras.GET_IBlockState.invoke(Material.OAK_SIGN, (byte)0);
            final Object blockChangePacket = NMSExtras.PACKET_PLAY_OUT_BLOCK_CHANGE.invoke(position, signBlockData);
            final Object components = NMSExtras.SANITIZE_LINES.invoke((Object[])lines);
            final Object tileSign = NMSExtras.TILE_ENTITY_SIGN.invoke(position, signBlockData);
            if (XReflection.supports(20)) {
                final Class<?> EnumColor = XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "world.item").map(MinecraftMapping.MOJANG, "DyeColor").map(MinecraftMapping.SPIGOT, "EnumColor").unreflect();
                Object enumColor = null;
                for (final Field field : EnumColor.getFields()) {
                    final Object color = field.get((Object)null);
                    final String colorName = (String)EnumColor.getDeclaredMethod("b", (Class<?>[])new Class[0]).invoke(color, new Object[0]);
                    if (textColor.name().equalsIgnoreCase(colorName)) {
                        enumColor = color;
                        break;
                    }
                }
                final Object signText = NMSExtras.SIGN_TEXT.invoke(components, components, enumColor, frontSide);
                NMSExtras.TILE_ENTITY_SIGN__SET_LINE.invoke(signText, true);
            }
            else {
                for (int i = 0; i < lines.length; ++i) {
                    final Object component = Array.get(components, i);
                    NMSExtras.TILE_ENTITY_SIGN__SET_LINE.invoke(tileSign, i, component, component);
                }
            }
            final Object signLinesUpdatePacket = NMSExtras.TILE_ENTITY_SIGN__GET_UPDATE_PACKET.invoke(tileSign);
            final Object signPacket = XReflection.v(20, NMSExtras.PACKET_PLAY_OUT_OPEN_SIGN_EDITOR.invoke(position, true)).orElse(NMSExtras.PACKET_PLAY_OUT_OPEN_SIGN_EDITOR.invoke(position));
            MinecraftConnection.sendPacket(player, blockChangePacket, signLinesUpdatePacket, signPacket);
        }
        catch (final Throwable x) {
            x.printStackTrace();
        }
    }
    
    static {
        EntityLiving = XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "world.entity").map(MinecraftMapping.MOJANG, "LivingEntity").map(MinecraftMapping.SPIGOT, "EntityLiving").unreflect();
        BlockState = XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "world.level.block.state").map(MinecraftMapping.MOJANG, "BlockState").map(MinecraftMapping.SPIGOT, "IBlockData").unreflect();
        MULTI_BLOCK_CHANGE_INFO_CLASS = null;
        final MethodHandles.Lookup lookup = MethodHandles.lookup();
        MethodHandle expPacket = null;
        MethodHandle entityPacket = null;
        MethodHandle worldHandle = null;
        MethodHandle entityHandle = null;
        MethodHandle lightning = null;
        MethodHandle vec3D = null;
        MethodHandle signEditorPacket = null;
        MethodHandle packetPlayOutBlockChange = null;
        MethodHandle animationPacket = null;
        MethodHandle animationType = null;
        MethodHandle animationEntityId = null;
        MethodHandle getBukkitEntity = null;
        MethodHandle blockPosition = null;
        MethodHandle playBlockAction = null;
        MethodHandle getBlockType = null;
        MethodHandle getBlock = null;
        MethodHandle getIBlockData = null;
        MethodHandle sanitizeLines = null;
        MethodHandle tileEntitySign = null;
        MethodHandle tileEntitySign_getUpdatePacket = null;
        MethodHandle tileEntitySign_setLine = null;
        MethodHandle signText = null;
        final MethodHandle playOutMultiBlockChange = null;
        final MethodHandle multiBlockChangeInfo = null;
        MethodHandle chunkWrapper = null;
        final MethodHandle chunkWrapperSet = null;
        final MethodHandle shortsOrInfo = null;
        final MethodHandle setBlockData = null;
        MethodHandle getDataWatcher = null;
        MethodHandle dataWatcherGetItem = null;
        MethodHandle dataWatcherSetItem = null;
        MethodHandle getHandle = null;
        try {
            final Class<?> CraftEntityClass = XReflection.ofMinecraft().inPackage(MinecraftPackage.CB, "entity").named("CraftEntity").unreflect();
            final Class<?> nmsEntityType = XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "world.entity").map(MinecraftMapping.MOJANG, "EntityType").map(MinecraftMapping.SPIGOT, "EntityTypes").unreflect();
            final Class<?> nmsEntity = XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "world.entity").named("Entity").unreflect();
            final Class<?> craftEntity = XReflection.ofMinecraft().inPackage(MinecraftPackage.CB, "entity").named("CraftEntity").unreflect();
            final Class<?> nmsVec3D = XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "world.phys").map(MinecraftMapping.MOJANG, "Vec3").map(MinecraftMapping.SPIGOT, "Vec3D").unreflect();
            final Class<?> world = XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "world.level").map(MinecraftMapping.MOJANG, "Level").map(MinecraftMapping.SPIGOT, "World").unreflect();
            final Class<?> signOpenPacket = XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "network.protocol.game").map(MinecraftMapping.MOJANG, "ClientboundOpenSignEditorPacket").map(MinecraftMapping.SPIGOT, "PacketPlayOutOpenSignEditor").unreflect();
            final Class<?> packetPlayOutBlockChangeClass = XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "network.protocol.game").map(MinecraftMapping.MOJANG, "ClientboundBlockUpdatePacket").map(MinecraftMapping.SPIGOT, "PacketPlayOutBlockChange").unreflect();
            final Class<?> CraftMagicNumbers = XReflection.ofMinecraft().inPackage(MinecraftPackage.CB, "util").named("CraftMagicNumbers").unreflect();
            final Class<?> CraftSign = XReflection.ofMinecraft().inPackage(MinecraftPackage.CB, "block").named("CraftSign").unreflect();
            final Class<?> IChatBaseComponent = XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "network.chat").map(MinecraftMapping.MOJANG, "Component").map(MinecraftMapping.SPIGOT, "IChatBaseComponent").unreflect();
            final Class<?> TileEntitySign = XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "world.level.block.entity").map(MinecraftMapping.MOJANG, "SignBlockEntity").map(MinecraftMapping.SPIGOT, "TileEntitySign").unreflect();
            final Class<?> PacketPlayOutTileEntityData = XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "network.protocol.game").map(MinecraftMapping.MOJANG, "ClientboundBlockEntityDataPacket").map(MinecraftMapping.SPIGOT, "PacketPlayOutTileEntityData").unreflect();
            final Class<?> DataWatcherClass = XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "network.syncher").map(MinecraftMapping.MOJANG, "SynchedEntityData").map(MinecraftMapping.SPIGOT, "DataWatcher").unreflect();
            final Class<?> DataWatcherItemClass = XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "network.syncher").map(MinecraftMapping.MOJANG, "SynchedEntityData$DataItem").map(MinecraftMapping.SPIGOT, "DataWatcher$Item").unreflect();
            final Class<?> DataWatcherObject = XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "network.syncher").map(MinecraftMapping.MOJANG, "EntityDataAccessor").map(MinecraftMapping.SPIGOT, "DataWatcherObject").unreflect();
            getHandle = lookup.findVirtual((Class)CraftEntityClass, "getHandle", MethodType.methodType((Class)nmsEntity));
            getDataWatcher = XReflection.of(nmsEntity).method().returns(DataWatcherClass).map(MinecraftMapping.MOJANG, "getEntityData").map(MinecraftMapping.OBFUSCATED, XReflection.v(21, 6, "au").v(21, 5, "ar").v(21, 3, "au").v(21, "ar").v(20, 5, "ap").v(20, 4, "an").v(20, 2, "al").v(19, "aj").v(18, "ai").orElse("getDataWatcher")).unreflect();
            dataWatcherGetItem = XReflection.of(DataWatcherClass).method().returns(Object.class).parameters(DataWatcherObject).map(MinecraftMapping.MOJANG, "get").map(MinecraftMapping.SPIGOT, XReflection.v(20, 5, "a").v(20, "b").v(18, "a").orElse("get")).unreflect();
            dataWatcherSetItem = XReflection.of(DataWatcherClass).method().returns(Void.TYPE).parameters(DataWatcherObject, Object.class).map(MinecraftMapping.MOJANG, "set").map(MinecraftMapping.SPIGOT, XReflection.v(20, 5, "a").v(18, "b").orElse("set")).unreflect();
            getBukkitEntity = lookup.findVirtual((Class)nmsEntity, "getBukkitEntity", MethodType.methodType((Class)craftEntity));
            entityHandle = lookup.findVirtual((Class)craftEntity, "getHandle", MethodType.methodType((Class)nmsEntity));
            expPacket = lookup.findConstructor((Class)((ReflectiveHandle<Class>)XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "network.protocol.game").map(MinecraftMapping.MOJANG, "ClientboundSetExperiencePacket").map(MinecraftMapping.SPIGOT, "PacketPlayOutExperience")).unreflect(), MethodType.methodType(Void.TYPE, Float.TYPE, new Class[] { Integer.TYPE, Integer.TYPE }));
            if (!XReflection.supports(16)) {
                entityPacket = XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS).named("PacketPlayOutSpawnEntityWeather").constructor().parameters(nmsEntity).unreflect();
            }
            else {
                vec3D = lookup.findConstructor((Class)nmsVec3D, MethodType.methodType(Void.TYPE, Double.TYPE, new Class[] { Double.TYPE, Double.TYPE }));
                final List<Class<?>> spawnTypes = (List<Class<?>>)new ArrayList((Collection)Arrays.asList((Object[])new Class[] { Integer.TYPE, UUID.class, Double.TYPE, Double.TYPE, Double.TYPE, Float.TYPE, Float.TYPE, nmsEntityType, Integer.TYPE, nmsVec3D }));
                if (XReflection.supports(19)) {
                    spawnTypes.add((Object)Double.TYPE);
                }
                entityPacket = lookup.findConstructor((Class)((ReflectiveHandle<Class>)XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "network.protocol.game").map(MinecraftMapping.MOJANG, "ClientboundAddEntityPacket").map(MinecraftMapping.SPIGOT, "PacketPlayOutSpawnEntity")).unreflect(), MethodType.methodType(Void.TYPE, (List)spawnTypes));
            }
            worldHandle = lookup.findVirtual((Class)((ReflectiveHandle<Class>)XReflection.ofMinecraft().inPackage(MinecraftPackage.CB).named("CraftWorld")).unreflect(), "getHandle", MethodType.methodType((Class)((ReflectiveHandle<Class>)XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "server.level").map(MinecraftMapping.MOJANG, "ServerLevel").map(MinecraftMapping.SPIGOT, "WorldServer")).unreflect()));
            final MinecraftClassHandle entityLightning = XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "world.entity").map(MinecraftMapping.MOJANG, "LightningBolt").map(MinecraftMapping.SPIGOT, "EntityLightning");
            if (!XReflection.supports(16)) {
                lightning = lookup.findConstructor((Class)((ReflectiveHandle<Class>)entityLightning).unreflect(), MethodType.methodType(Void.TYPE, (Class)world, new Class[] { Double.TYPE, Double.TYPE, Double.TYPE, Boolean.TYPE, Boolean.TYPE }));
            }
            else {
                lightning = lookup.findConstructor((Class)((ReflectiveHandle<Class>)entityLightning).unreflect(), MethodType.methodType(Void.TYPE, (Class)nmsEntityType, new Class[] { world }));
            }
            final Class<?> playOutMultiBlockChangeClass = XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "network.protocol.game").map(MinecraftMapping.MOJANG, "ClientboundSectionBlocksUpdatePacket").map(MinecraftMapping.SPIGOT, "PacketPlayOutMultiBlockChange").unreflect();
            final Class<?> chunkCoordIntPairClass = XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "world.level").map(MinecraftMapping.MOJANG, "ChunkPos").map(MinecraftMapping.SPIGOT, "ChunkCoordIntPair").unreflect();
            try {
                if (!XReflection.supports(16)) {
                    chunkWrapper = lookup.findConstructor((Class)chunkCoordIntPairClass, MethodType.methodType(Void.TYPE, Integer.TYPE, new Class[] { Integer.TYPE }));
                }
            }
            catch (final NoSuchMethodException | IllegalAccessException e) {
                e.printStackTrace();
            }
            final Class<?> animation = XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "network.protocol.game").map(MinecraftMapping.MOJANG, "ClientboundAnimatePacket").map(MinecraftMapping.SPIGOT, "PacketPlayOutAnimation").unreflect();
            animationPacket = lookup.findConstructor((Class)animation, XReflection.supports(17) ? MethodType.methodType(Void.TYPE, (Class)nmsEntity, new Class[] { Integer.TYPE }) : MethodType.methodType(Void.TYPE));
            if (!XReflection.supports(17)) {
                Field field = animation.getDeclaredField("a");
                field.setAccessible(true);
                animationEntityId = lookup.unreflectSetter(field);
                field = animation.getDeclaredField("b");
                field.setAccessible(true);
                animationType = lookup.unreflectSetter(field);
            }
            final Class<?> blockPos = XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "core").map(MinecraftMapping.MOJANG, "BlockPos").map(MinecraftMapping.SPIGOT, "BlockPosition").unreflect();
            final Class<?> block = XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "world.level.block").named("Block").unreflect();
            blockPosition = lookup.findConstructor((Class)blockPos, (MethodType)XReflection.v(19, MethodType.methodType(Void.TYPE, Integer.TYPE, new Class[] { Integer.TYPE, Integer.TYPE })).orElse(MethodType.methodType(Void.TYPE, Double.TYPE, new Class[] { Double.TYPE, Double.TYPE })));
            getBlockType = XReflection.of(world).method().returns(NMSExtras.BlockState).parameters(blockPos).map(MinecraftMapping.MOJANG, "getBlockState").map(MinecraftMapping.SPIGOT, XReflection.v(18, "a_").orElse("getType")).unreflect();
            if (XReflection.supports(21)) {
                getBlock = XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "world.level.block.state").map(MinecraftMapping.MOJANG, "BlockBehaviour").map(MinecraftMapping.SPIGOT, "BlockBase").inner(XReflection.ofMinecraft().map(MinecraftMapping.MOJANG, "BlockStateBase").map(MinecraftMapping.SPIGOT, "BlockData")).method().returns(block).map(MinecraftMapping.MOJANG, "getBlock").map(MinecraftMapping.SPIGOT, "b").unreflect();
            }
            else {
                getBlock = XReflection.of(NMSExtras.BlockState).method().returns(block).map(MinecraftMapping.MOJANG, "getBlock").map(MinecraftMapping.SPIGOT, XReflection.v(18, "b").orElse("getBlock")).unreflect();
            }
            playBlockAction = XReflection.of(world).method().returns(Void.TYPE).parameters(blockPos, block, Integer.TYPE, Integer.TYPE).map(MinecraftMapping.MOJANG, "blockEvent").map(MinecraftMapping.SPIGOT, XReflection.v(18, "a").orElse("playBlockAction")).unreflect();
            signEditorPacket = lookup.findConstructor((Class)signOpenPacket, (MethodType)XReflection.v(20, MethodType.methodType(Void.TYPE, (Class)blockPos, new Class[] { Boolean.TYPE })).orElse(MethodType.methodType(Void.TYPE, (Class)blockPos)));
            if (XReflection.supports(17)) {
                packetPlayOutBlockChange = lookup.findConstructor((Class)packetPlayOutBlockChangeClass, MethodType.methodType(Void.TYPE, (Class)blockPos, new Class[] { NMSExtras.BlockState }));
                getIBlockData = lookup.findStatic((Class)CraftMagicNumbers, "getBlock", MethodType.methodType((Class)NMSExtras.BlockState, (Class)Material.class, new Class[] { Byte.TYPE }));
                sanitizeLines = lookup.findStatic((Class)CraftSign, (String)XReflection.v(17, "sanitizeLines").orElse("SANITIZE_LINES"), MethodType.methodType((Class)XReflection.toArrayClass(IChatBaseComponent), (Class)String[].class));
                tileEntitySign = lookup.findConstructor((Class)TileEntitySign, MethodType.methodType(Void.TYPE, (Class)blockPos, new Class[] { NMSExtras.BlockState }));
                tileEntitySign_getUpdatePacket = XReflection.of(TileEntitySign).method().returns(PacketPlayOutTileEntityData).map(MinecraftMapping.MOJANG, "getUpdatePacket").map(MinecraftMapping.SPIGOT, XReflection.v(21, 6, "u").v(21, 4, "s").v(21, 3, "t").v(20, 5, "l").v(20, 4, "m").v(20, "j").v(19, "f").v(18, "c").orElse("getUpdatePacket")).unreflect();
                if (XReflection.supports(20)) {
                    final Class<?> SignText = XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "world.level.block.entity").named("SignText").unreflect();
                    if (!XReflection.supports(20, 6)) {
                        tileEntitySign_setLine = lookup.findVirtual((Class)TileEntitySign, "a", MethodType.methodType(Boolean.TYPE, (Class)SignText, new Class[] { Boolean.TYPE }));
                    }
                    final Class<?> IChatBaseComponentArray = XReflection.of(IChatBaseComponent).asArray().unreflect();
                    final Class<?> EnumColor = XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "world.item").map(MinecraftMapping.MOJANG, "DyeColor").map(MinecraftMapping.SPIGOT, "EnumColor").unreflect();
                    signText = lookup.findConstructor((Class)SignText, MethodType.methodType(Void.TYPE, (Class)IChatBaseComponentArray, new Class[] { IChatBaseComponentArray, EnumColor, Boolean.TYPE }));
                }
                else {
                    tileEntitySign_setLine = lookup.findVirtual((Class)TileEntitySign, "a", MethodType.methodType(Void.TYPE, Integer.TYPE, new Class[] { IChatBaseComponent, IChatBaseComponent }));
                }
            }
        }
        catch (final NoSuchMethodException | IllegalAccessException | NoSuchFieldException ex) {
            ex.printStackTrace();
        }
        GET_ENTITY_HANDLE = getHandle;
        GET_DATA_WATCHER = getDataWatcher;
        DATA_WATCHER_GET_ITEM = dataWatcherGetItem;
        DATA_WATCHER_SET_ITEM = dataWatcherSetItem;
        EXP_PACKET = expPacket;
        ENTITY_PACKET = entityPacket;
        WORLD_HANDLE = worldHandle;
        ENTITY_HANDLE = entityHandle;
        LIGHTNING_ENTITY = lightning;
        VEC3D = vec3D;
        PACKET_PLAY_OUT_OPEN_SIGN_EDITOR = signEditorPacket;
        PACKET_PLAY_OUT_BLOCK_CHANGE = packetPlayOutBlockChange;
        ANIMATION_PACKET = animationPacket;
        ANIMATION_TYPE = animationType;
        ANIMATION_ENTITY_ID = animationEntityId;
        BLOCK_POSITION = blockPosition;
        PLAY_BLOCK_ACTION = playBlockAction;
        GET_BLOCK_TYPE = getBlockType;
        GET_BLOCK = getBlock;
        GET_IBlockState = getIBlockData;
        SANITIZE_LINES = sanitizeLines;
        TILE_ENTITY_SIGN = tileEntitySign;
        TILE_ENTITY_SIGN__GET_UPDATE_PACKET = tileEntitySign_getUpdatePacket;
        TILE_ENTITY_SIGN__SET_LINE = tileEntitySign_setLine;
        GET_BUKKIT_ENTITY = getBukkitEntity;
        PLAY_OUT_MULTI_BLOCK_CHANGE_PACKET = playOutMultiBlockChange;
        MULTI_BLOCK_CHANGE_INFO = multiBlockChangeInfo;
        CHUNK_WRAPPER = chunkWrapper;
        CHUNK_WRAPPER_SET = chunkWrapperSet;
        SHORTS_OR_INFO = shortsOrInfo;
        SET_BlockState = setBlockData;
        SIGN_TEXT = signText;
    }
    
    public enum EntityPose
    {
        STANDING("a"), 
        FALL_FLYING("b"), 
        SLEEPING("c"), 
        SWIMMING("d"), 
        SPIN_ATTACK("e"), 
        CROUCHING("f"), 
        LONG_JUMPING("g"), 
        DYING("h"), 
        CROAKING("i"), 
        USING_TONGUE("j"), 
        SITTING("k"), 
        ROARING("l"), 
        SNIFFING("m"), 
        EMERGING("n"), 
        DIGGING("o");
        
        public final Object enumValue;
        private final boolean supported;
        
        private EntityPose(final String fieldName) {
            boolean supported = true;
            Object enumValue = null;
            try {
                final Class<?> EntityPose = XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "world.entity").map(MinecraftMapping.MOJANG, "Pose").map(MinecraftMapping.SPIGOT, "EntityPose").reflect();
                enumValue = EntityPose.getDeclaredField(XReflection.v(17, fieldName).orElse(this.name())).get((Object)null);
            }
            catch (final Throwable e) {
                supported = false;
            }
            this.supported = supported;
            this.enumValue = enumValue;
        }
        
        public boolean isSupported() {
            return this.supported;
        }
        
        public Object getEnumValue() {
            return this.enumValue;
        }
    }
    
    public enum DataWatcherItemType
    {
        DATA_LIVING_ENTITY_FLAGS(NMSExtras.getStaticFieldIgnored(NMSExtras.EntityLiving, "t"));
        
        private final Object id;
        private final boolean supported;
        
        private DataWatcherItemType(final Object DataWatcherObject) {
            boolean supported = true;
            Object id = null;
            try {
                id = DataWatcherObject;
            }
            catch (final Throwable e) {
                supported = false;
            }
            this.supported = supported;
            this.id = id;
        }
        
        public boolean isSupported() {
            return this.supported;
        }
        
        public Object getId() {
            return this.id;
        }
    }
    
    public enum LivingEntityFlags
    {
        SPIN_ATTACK(4);
        
        private final byte bit;
        
        private LivingEntityFlags(final int bit) {
            this.bit = (byte)bit;
        }
        
        public byte getBit() {
            return this.bit;
        }
    }
    
    public enum Animation
    {
        SWING_MAIN_ARM, 
        HURT, 
        LEAVE_BED, 
        SWING_OFF_HAND, 
        CRITICAL_EFFECT, 
        MAGIC_CRITICAL_EFFECT;
    }
    
    public static class WorldlessBlockWrapper
    {
        public final Block block;
        
        public WorldlessBlockWrapper(final Block block) {
            this.block = block;
        }
        
        @Override
        public int hashCode() {
            return (this.block.getY() + this.block.getZ() * 31) * 31 + this.block.getX();
        }
        
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Block)) {
                return false;
            }
            final Block other = (Block)obj;
            return this.block.getX() == other.getX() && this.block.getY() == other.getY() && this.block.getZ() == other.getZ();
        }
    }
}
