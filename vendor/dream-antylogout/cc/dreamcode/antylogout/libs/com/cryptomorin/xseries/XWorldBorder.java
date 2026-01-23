package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries;

import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.ReflectiveHandle;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.Listener;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.lang.invoke.MethodType;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.minecraft.MinecraftMapping;
import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm.classes.ClassHandle;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.minecraft.MinecraftPackage;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.minecraft.MinecraftClassHandle;
import java.util.Iterator;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.minecraft.MinecraftConnection;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;
import java.util.UUID;
import java.util.Map;
import java.lang.invoke.MethodHandle;
import org.bukkit.World;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.XReflection;
import org.jetbrains.annotations.Nullable;
import org.bukkit.WorldBorder;
import java.util.Collections;
import org.bukkit.Bukkit;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.bukkit.entity.Player;
import java.util.Collection;
import java.time.Duration;
import org.bukkit.Location;

public abstract class XWorldBorder
{
    public static final int ABSOLUTE_MAX_SIZE = 29999984;
    public static final double MAX_SIZE = 5.9999968E7;
    public static final double MAX_CENTER_COORDINATE = 2.9999984E7;
    private static final boolean SUPPORTS_NATIVE_WORLDBORDERS;
    protected BorderBounds borderBounds;
    
    public abstract double getDamageBuffer();
    
    public abstract double getSizeLerpTarget();
    
    public abstract double getSize();
    
    public abstract boolean isWithinBorder(final Location p0);
    
    public abstract int getWarningDistance();
    
    public abstract Duration getWarningTime();
    
    public abstract Location getCenter();
    
    public abstract void setFor(final Collection<Player> p0, final boolean p1);
    
    public final BorderBounds getBorderBounds() {
        return this.borderBounds;
    }
    
    public abstract XWorldBorder copy();
    
    public abstract XWorldBorder setDamageBuffer(final double p0);
    
    public abstract XWorldBorder setWarningTime(final Duration p0);
    
    public abstract XWorldBorder setWarningDistance(final int p0);
    
    public XWorldBorder setSize(final double newSize) {
        return this.setSize(newSize, Duration.ZERO);
    }
    
    public XWorldBorder setSize(final double newSize, @NotNull final Duration duration) {
        Objects.requireNonNull((Object)duration, "Size change duration cannot be null");
        if (newSize > 5.9999968E7) {
            throw new IllegalArgumentException("Border size is bigger than max border size: " + newSize + " > " + 5.9999968E7);
        }
        return this;
    }
    
    public XWorldBorder setSizeLerpTarget(final double sizeLerpTarget) {
        if (sizeLerpTarget > 5.9999968E7) {
            throw new IllegalArgumentException("Size lerp target size is bigger than max border size: " + sizeLerpTarget + " > " + 5.9999968E7);
        }
        return this;
    }
    
    public abstract XWorldBorder setCenter(final Location p0);
    
    public XWorldBorder setCenter(final double x, final double z) {
        if (Double.isNaN(x) || Double.isNaN(z)) {
            throw new IllegalArgumentException("Invalid coordinates: " + x + ", " + z);
        }
        return this;
    }
    
    public XWorldBorder update(final Player... players) {
        if (players == null) {
            throw new IllegalArgumentException("Player array is null");
        }
        return this;
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "(size: " + this.getSize() + ", warningDistance: " + this.getWarningDistance() + ", warningTime: " + (Object)this.getWarningTime() + ", center: " + (Object)this.getCenter() + ", damageBuffer: " + this.getDamageBuffer() + ')';
    }
    
    public final double getDistanceToBorder(final Location location) {
        if (this.borderBounds == null) {
            return this.getCenter().distanceSquared(location);
        }
        final double x = location.getX();
        final double z = location.getZ();
        final double d2 = z - this.borderBounds.minZ;
        final double d3 = this.borderBounds.maxZ - z;
        final double d4 = x - this.borderBounds.minX;
        final double d5 = this.borderBounds.maxX - x;
        double d6 = Math.min(d4, d5);
        d6 = Math.min(d6, d2);
        return Math.min(d6, d3);
    }
    
    protected void updateBorderBounds(final Location center) {
        this.borderBounds = new BorderBounds(center.getWorld(), center.getX(), center.getZ(), this.getSize());
    }
    
    public static XWorldBorder create() {
        if (XWorldBorder.SUPPORTS_NATIVE_WORLDBORDERS) {
            return new BukkitWorldBorder(Bukkit.createWorldBorder());
        }
        return new NMSWorldBorder();
    }
    
    public static XWorldBorder getOrCreate(final Player player) {
        XWorldBorder wb = get(player);
        if (wb != null) {
            return wb;
        }
        wb = create();
        wb.setFor((Collection<Player>)Collections.singleton((Object)player), true);
        if (!XWorldBorder.SUPPORTS_NATIVE_WORLDBORDERS) {
            NMSWorldBorder.WORLD_BORDERS.put((Object)player.getUniqueId(), (Object)wb);
        }
        return wb;
    }
    
    @Nullable
    public static XWorldBorder get(final Player player) {
        if (XWorldBorder.SUPPORTS_NATIVE_WORLDBORDERS) {
            final WorldBorder worldBorder = player.getWorldBorder();
            return (worldBorder == null) ? null : new BukkitWorldBorder(worldBorder);
        }
        return (XWorldBorder)NMSWorldBorder.WORLD_BORDERS.get((Object)player.getUniqueId());
    }
    
    @Nullable
    public static XWorldBorder remove(final Player player) {
        if (!XWorldBorder.SUPPORTS_NATIVE_WORLDBORDERS) {
            return (XWorldBorder)NMSWorldBorder.WORLD_BORDERS.remove((Object)player.getUniqueId());
        }
        final WorldBorder worldBorder = player.getWorldBorder();
        if (worldBorder == null) {
            return null;
        }
        player.setWorldBorder(player.getWorld().getWorldBorder());
        return new BukkitWorldBorder(worldBorder);
    }
    
    public static XWorldBorder from(final WorldBorder bukkitWb) {
        if (XWorldBorder.SUPPORTS_NATIVE_WORLDBORDERS) {
            return new BukkitWorldBorder(bukkitWb).copy();
        }
        final NMSWorldBorder wb = new NMSWorldBorder();
        wb.world = bukkitWb.getCenter().getWorld();
        wb.centerX = bukkitWb.getCenter().getX();
        wb.centerZ = bukkitWb.getCenter().getZ();
        wb.size = bukkitWb.getSize();
        wb.sizeLerpTime = Duration.ZERO;
        wb.damagePerBlock = bukkitWb.getDamageAmount();
        wb.damageSafeZone = bukkitWb.getDamageBuffer();
        wb.warningTime = Duration.ofSeconds((long)bukkitWb.getWarningTime());
        wb.warningBlocks = bukkitWb.getWarningDistance();
        wb.handle = wb.createHandle();
        return wb;
    }
    
    static {
        SUPPORTS_NATIVE_WORLDBORDERS = XReflection.of(Player.class).method().named("setWorldBorder").returns(Void.TYPE).parameters(WorldBorder.class).exists();
    }
    
    public static final class BorderBounds
    {
        protected final World lastCenterWorld;
        protected final double lastCenterX;
        protected final double lastCenterZ;
        public final double minX;
        public final double minZ;
        public final double maxX;
        public final double maxZ;
        
        private static double clamp(final double var0, final double var2, final double var4) {
            return (var0 < var2) ? var2 : Math.min(var0, var4);
        }
        
        public boolean isCenterSame(final World world, final double centerX, final double centerZ) {
            return this.lastCenterWorld == world && this.lastCenterX == centerX && this.lastCenterZ == centerZ;
        }
        
        public BorderBounds(final World centerWorld, final double centerX, final double centerZ, final double size) {
            this.lastCenterWorld = centerWorld;
            this.lastCenterX = centerX;
            this.lastCenterZ = centerZ;
            this.minX = clamp(centerX - size / 2.0, -2.9999984E7, 2.9999984E7);
            this.minZ = clamp(centerZ - size / 2.0, -2.9999984E7, 2.9999984E7);
            this.maxX = clamp(centerX + size / 2.0, -2.9999984E7, 2.9999984E7);
            this.maxZ = clamp(centerZ + size / 2.0, -2.9999984E7, 2.9999984E7);
        }
    }
    
    private static final class NMSWorldBorder extends XWorldBorder
    {
        private static final MethodHandle WORLD_HANDLE;
        private static final MethodHandle WORLDBORDER;
        private static final MethodHandle WORLDBORDER_WORLD;
        private static final MethodHandle CENTER;
        private static final MethodHandle WARNING_DISTANCE;
        private static final MethodHandle WARNING_TIME;
        private static final MethodHandle SIZE;
        private static final MethodHandle WorldBorder_lerpSizeBetween;
        private static final MethodHandle PACKET_WARNING_DISTANCE;
        private static final MethodHandle PACKET_WARNING_DELAY;
        private static final MethodHandle PACKET_LERP_SIZE;
        private static final MethodHandle PACKET_INIT;
        private static final MethodHandle PACKET_CENTER;
        private static final MethodHandle PACKET_SIZE;
        private static final Object INITIALIZE;
        private static final boolean SUPPORTS_SEPARATE_PACKETS;
        private static final Map<UUID, XWorldBorder> WORLD_BORDERS;
        private Object handle;
        private double damagePerBlock;
        private double damageSafeZone;
        private double size;
        private double sizeLerpTarget;
        private Duration warningTime;
        private Duration sizeLerpTime;
        private int warningBlocks;
        private World world;
        private double centerX;
        private double centerZ;
        private final Set<Component> updateRequired;
        private boolean init;
        
        private NMSWorldBorder() {
            this.damagePerBlock = 0.2;
            this.damageSafeZone = 5.0;
            this.size = 100.0;
            this.sizeLerpTarget = 0.0;
            this.warningTime = Duration.ofSeconds(15L);
            this.sizeLerpTime = Duration.ZERO;
            this.warningBlocks = 5;
            this.updateRequired = (Set<Component>)EnumSet.noneOf((Class)Component.class);
            this.init = true;
        }
        
        @Override
        public NMSWorldBorder copy() {
            final NMSWorldBorder wb = new NMSWorldBorder();
            wb.world = this.world;
            wb.centerX = this.centerX;
            wb.centerZ = this.centerZ;
            wb.size = this.size;
            wb.sizeLerpTime = this.sizeLerpTime;
            wb.damagePerBlock = this.damagePerBlock;
            wb.damageSafeZone = this.damageSafeZone;
            wb.warningTime = this.warningTime;
            wb.warningBlocks = this.warningBlocks;
            wb.handle = wb.createHandle();
            return wb;
        }
        
        public XWorldBorder setDamageAmount(final double damage) {
            this.damagePerBlock = damage;
            return this;
        }
        
        @Override
        public double getSize() {
            return this.size;
        }
        
        public double getDamageAmount() {
            return this.damagePerBlock;
        }
        
        @Override
        public XWorldBorder setDamageBuffer(final double blocks) {
            this.damageSafeZone = blocks;
            return this;
        }
        
        @Override
        public double getDamageBuffer() {
            return this.damageSafeZone;
        }
        
        @Override
        public XWorldBorder setWarningTime(final Duration time) {
            if (this.warningTime == time) {
                return this;
            }
            this.warningTime = time;
            this.update(Component.WARNING_DELAY);
            return this;
        }
        
        @Override
        public Duration getWarningTime() {
            return this.warningTime;
        }
        
        @Override
        public XWorldBorder setWarningDistance(final int blocks) {
            if (this.warningBlocks == blocks) {
                return this;
            }
            this.warningBlocks = blocks;
            this.update(Component.WARNING_DISTANCE);
            return this;
        }
        
        @Override
        public double getSizeLerpTarget() {
            return this.sizeLerpTarget;
        }
        
        @Override
        public XWorldBorder setSizeLerpTarget(final double sizeLerpTarget) {
            super.setSizeLerpTarget(sizeLerpTarget);
            if (this.sizeLerpTarget == sizeLerpTarget) {
                return this;
            }
            this.sizeLerpTarget = sizeLerpTarget;
            this.update(Component.SIZE_LERP);
            return this;
        }
        
        @Override
        public XWorldBorder update(final Player... players) {
            this.setFor((Collection<Player>)Arrays.asList((Object[])players), false);
            return this;
        }
        
        @Override
        public int getWarningDistance() {
            return this.warningBlocks;
        }
        
        @Override
        public XWorldBorder setCenter(final Location location) {
            this.setCenter(location.getX(), location.getZ());
            this.world = location.getWorld();
            return this;
        }
        
        @Override
        public XWorldBorder setCenter(final double x, final double z) {
            super.setCenter(x, z);
            if (this.centerX == x && this.centerZ == z) {
                return this;
            }
            this.centerX = x;
            this.centerZ = z;
            this.updateBorderBounds(this.getCenter());
            this.update(Component.CENTER);
            return this;
        }
        
        @Override
        public Location getCenter() {
            return new Location(this.world, this.centerX, 0.0, this.centerZ);
        }
        
        @Override
        public XWorldBorder setSize(final double newSize, @NotNull final Duration duration) {
            super.setSize(newSize, duration);
            if (this.size == newSize && this.sizeLerpTime.equals((Object)duration)) {
                return this;
            }
            this.size = newSize;
            this.sizeLerpTime = duration;
            this.updateBorderBounds(this.getCenter());
            this.update(Component.SIZE);
            if (!duration.isZero()) {
                this.update(Component.SIZE_LERP);
            }
            return this;
        }
        
        private void update(final Component comp) {
            if (NMSWorldBorder.SUPPORTS_SEPARATE_PACKETS) {
                this.updateRequired.add((Object)comp);
            }
        }
        
        @Override
        public boolean isWithinBorder(final Location location) {
            return this.borderBounds != null && (this.world == null || this.world == location.getWorld()) && location.getX() + 1.0 > this.borderBounds.minX && location.getX() < this.borderBounds.maxX && location.getZ() + 1.0 > this.borderBounds.minZ && location.getZ() < this.borderBounds.maxZ;
        }
        
        @Override
        public void setFor(final Collection<Player> players, final boolean forceInit) {
            final boolean init = forceInit || this.init;
            this.init = false;
            try {
                for (final Component component : this.updateRequired) {
                    component.setHandle(this);
                }
                Object[] packets;
                if (NMSWorldBorder.SUPPORTS_SEPARATE_PACKETS && !init) {
                    packets = new Object[this.updateRequired.size()];
                    int i = 0;
                    for (final Component component2 : this.updateRequired) {
                        packets[i++] = component2.createPacket(this);
                    }
                }
                else {
                    final Object packet = XReflection.supports(17) ? NMSWorldBorder.PACKET_INIT.invoke(this.handle) : NMSWorldBorder.PACKET_INIT.invoke(this.handle, NMSWorldBorder.INITIALIZE);
                    packets = new Object[] { packet };
                }
                for (final Player player : players) {
                    MinecraftConnection.sendPacket(player, packets);
                }
            }
            catch (final Throwable throwable) {
                throwable.printStackTrace();
            }
            finally {
                this.updateRequired.clear();
            }
        }
        
        private Object createHandle() {
            Objects.requireNonNull((Object)this.world, "No world specified");
            try {
                final Object worldBorder = NMSWorldBorder.WORLDBORDER.invoke();
                final Object world = NMSWorldBorder.WORLD_HANDLE.invoke(this.world);
                NMSWorldBorder.WORLDBORDER_WORLD.invoke(worldBorder, world);
                return worldBorder;
            }
            catch (final Throwable throwable) {
                throwable.printStackTrace();
                return null;
            }
        }
        
        static {
            WORLD_BORDERS = (Map)new HashMap();
            if (!XWorldBorder.SUPPORTS_NATIVE_WORLDBORDERS) {
                Object initialize = null;
                MethodHandle packetInit = null;
                MethodHandle packetWarnDist = null;
                MethodHandle packetWarnDelay = null;
                MethodHandle packetLerpSize = null;
                MethodHandle packetCenter = null;
                MethodHandle packetSize = null;
                final MethodHandles.Lookup lookup = MethodHandles.lookup();
                final MinecraftClassHandle wb = XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "world.level.border").named("WorldBorder");
                final MinecraftClassHandle worldServer = XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "server.level").map(MinecraftMapping.MOJANG, "ServerLevel").map(MinecraftMapping.SPIGOT, "WorldServer");
                final MinecraftClassHandle craftWorld = XReflection.ofMinecraft().inPackage(MinecraftPackage.CB).named("CraftWorld");
                try {
                    if (!XReflection.supports(17)) {
                        Class<?> wbType;
                        try {
                            wbType = Class.forName("EnumWorldBorderAction");
                        }
                        catch (final ClassNotFoundException e) {
                            wbType = XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS).named("PacketPlayOutWorldBorder$EnumWorldBorderAction").unreflect();
                        }
                        packetInit = lookup.findConstructor((Class)((ReflectiveHandle<Class>)XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS).named("PacketPlayOutWorldBorder")).unreflect(), MethodType.methodType(Void.TYPE, (Class)wb.reflect(), new Class[] { wbType }));
                        for (final Object type : wbType.getEnumConstants()) {
                            if (type.toString().equals((Object)"INITIALIZE")) {
                                initialize = type;
                                break;
                            }
                        }
                    }
                }
                catch (final Exception ex) {
                    ex.printStackTrace();
                }
                boolean supportsSeperatePackets;
                try {
                    final Function<String, MethodHandle> getPacket = (Function<String, MethodHandle>)(packet -> XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "network.protocol.game").named(packet).constructor(wb).unreflect());
                    packetWarnDist = (MethodHandle)getPacket.apply((Object)"ClientboundSetBorderWarningDistancePacket");
                    packetWarnDelay = (MethodHandle)getPacket.apply((Object)"ClientboundSetBorderWarningDelayPacket");
                    packetLerpSize = (MethodHandle)getPacket.apply((Object)"ClientboundSetBorderLerpSizePacket");
                    packetInit = (MethodHandle)getPacket.apply((Object)"ClientboundInitializeBorderPacket");
                    packetCenter = (MethodHandle)getPacket.apply((Object)"ClientboundSetBorderCenterPacket");
                    packetSize = (MethodHandle)getPacket.apply((Object)"ClientboundSetBorderSizePacket");
                    supportsSeperatePackets = true;
                }
                catch (final Throwable ignored) {
                    supportsSeperatePackets = false;
                }
                PACKET_INIT = packetInit;
                PACKET_SIZE = packetSize;
                PACKET_CENTER = packetCenter;
                PACKET_LERP_SIZE = packetLerpSize;
                PACKET_WARNING_DELAY = packetWarnDelay;
                PACKET_WARNING_DISTANCE = packetWarnDist;
                SUPPORTS_SEPARATE_PACKETS = supportsSeperatePackets;
                WORLD_HANDLE = craftWorld.method().named("getHandle").returns(worldServer).unreflect();
                INITIALIZE = initialize;
                WORLDBORDER = wb.constructor().unreflect();
                WORLDBORDER_WORLD = wb.field().setter().named("world").returns(worldServer).unreflect();
                CENTER = wb.method().named(XReflection.v(18, "c").orElse("setCenter")).returns(Void.TYPE).parameters(Double.TYPE, Double.TYPE).unreflect();
                SIZE = wb.method().named(XReflection.v(18, "a").orElse("setSize")).returns(Void.TYPE).parameters(Double.TYPE).unreflect();
                WARNING_TIME = wb.method().named(XReflection.v(18, "b").orElse("setWarningTime")).returns(Void.TYPE).parameters(Integer.TYPE).unreflect();
                WARNING_DISTANCE = wb.method().named(XReflection.v(20, "c").v(18, "b").orElse("setWarningDistance")).returns(Void.TYPE).parameters(Integer.TYPE).unreflect();
                WorldBorder_lerpSizeBetween = wb.method().map(MinecraftMapping.OBFUSCATED, XReflection.v(18, "a").orElse("transitionSizeBetween")).map(MinecraftMapping.MOJANG, XReflection.v(21, "lerpSizeBetween").orElse("transitionSizeBetween")).returns(Void.TYPE).parameters(Double.TYPE, Double.TYPE, Long.TYPE).unreflect();
            }
            else {
                WORLD_HANDLE = (WORLDBORDER = (WORLDBORDER_WORLD = (CENTER = (WARNING_DISTANCE = (WARNING_TIME = (SIZE = (WorldBorder_lerpSizeBetween = (PACKET_WARNING_DISTANCE = (PACKET_WARNING_DELAY = (PACKET_LERP_SIZE = (PACKET_INIT = (PACKET_CENTER = (PACKET_SIZE = null)))))))))))));
                INITIALIZE = null;
                SUPPORTS_SEPARATE_PACKETS = true;
            }
        }
        
        private enum Component
        {
            SIZE {
                @Override
                protected void setHandle(final NMSWorldBorder wb) throws Throwable {
                    NMSWorldBorder.SIZE.invoke(wb.handle, wb.size);
                }
                
                @Override
                protected Object createPacket(final NMSWorldBorder wb) throws Throwable {
                    return NMSWorldBorder.PACKET_SIZE.invoke(wb.handle);
                }
            }, 
            SIZE_LERP {
                @Override
                protected void setHandle(final NMSWorldBorder wb) throws Throwable {
                    NMSWorldBorder.WorldBorder_lerpSizeBetween.invoke(wb.handle, wb.sizeLerpTarget, wb.size, wb.sizeLerpTime.toMillis());
                }
                
                @Override
                protected Object createPacket(final NMSWorldBorder wb) throws Throwable {
                    return NMSWorldBorder.PACKET_LERP_SIZE.invoke(wb.handle);
                }
            }, 
            WARNING_DISTANCE {
                @Override
                protected void setHandle(final NMSWorldBorder wb) throws Throwable {
                    NMSWorldBorder.WARNING_DISTANCE.invoke(wb.handle, wb.warningBlocks);
                }
                
                @Override
                protected Object createPacket(final NMSWorldBorder wb) throws Throwable {
                    return NMSWorldBorder.PACKET_WARNING_DISTANCE.invoke(wb.handle);
                }
            }, 
            WARNING_DELAY {
                @Override
                protected void setHandle(final NMSWorldBorder wb) throws Throwable {
                    NMSWorldBorder.WARNING_TIME.invoke(wb.handle, wb.warningBlocks);
                }
                
                @Override
                protected Object createPacket(final NMSWorldBorder wb) throws Throwable {
                    return NMSWorldBorder.PACKET_WARNING_DELAY.invoke(wb.handle);
                }
            }, 
            CENTER {
                @Override
                protected void setHandle(final NMSWorldBorder wb) throws Throwable {
                    NMSWorldBorder.CENTER.invoke(wb.handle, wb.centerX, wb.centerZ);
                }
                
                @Override
                protected Object createPacket(final NMSWorldBorder wb) throws Throwable {
                    return NMSWorldBorder.PACKET_CENTER.invoke(wb.handle);
                }
            };
            
            protected abstract void setHandle(final NMSWorldBorder p0) throws Throwable;
            
            protected abstract Object createPacket(final NMSWorldBorder p0) throws Throwable;
        }
    }
    
    private static final class BukkitWorldBorder extends XWorldBorder
    {
        private final WorldBorder worldBorder;
        
        private BukkitWorldBorder(final WorldBorder worldBorder) {
            this.worldBorder = worldBorder;
        }
        
        @Override
        public double getDamageBuffer() {
            return this.worldBorder.getDamageBuffer();
        }
        
        @Override
        public double getSizeLerpTarget() {
            return 0.0;
        }
        
        @Override
        public double getSize() {
            return this.worldBorder.getSize();
        }
        
        @Override
        public int getWarningDistance() {
            return this.worldBorder.getWarningDistance();
        }
        
        @Override
        public Duration getWarningTime() {
            return Duration.ofSeconds((long)this.worldBorder.getWarningTime());
        }
        
        @Override
        public XWorldBorder setDamageBuffer(final double blocks) {
            this.worldBorder.setDamageBuffer(blocks);
            return this;
        }
        
        @Override
        public XWorldBorder setWarningDistance(final int blocks) {
            this.worldBorder.setWarningDistance(blocks);
            return this;
        }
        
        @Override
        public XWorldBorder setWarningTime(final Duration time) {
            this.worldBorder.setWarningTime((int)time.getSeconds());
            return this;
        }
        
        @Override
        public XWorldBorder setSize(final double newSize, @NotNull final Duration duration) {
            this.worldBorder.setSize(newSize, TimeUnit.MILLISECONDS, duration.toMillis());
            return this;
        }
        
        @Override
        public XWorldBorder setCenter(final Location location) {
            this.worldBorder.setCenter(location);
            return this;
        }
        
        @Override
        public XWorldBorder setCenter(final double x, final double z) {
            this.worldBorder.setCenter(x, z);
            return this;
        }
        
        @Override
        public XWorldBorder update(final Player... players) {
            return this;
        }
        
        @Override
        public XWorldBorder setSizeLerpTarget(final double sizeLerpTarget) {
            this.worldBorder.setSize(sizeLerpTarget);
            return this;
        }
        
        @Override
        public void setFor(final Collection<Player> players, final boolean forceInit) {
            for (final Player player : players) {
                player.setWorldBorder(this.worldBorder);
            }
        }
        
        @Override
        public Location getCenter() {
            final Location center = this.worldBorder.getCenter();
            if (this.borderBounds == null || this.borderBounds.isCenterSame(center.getWorld(), center.getX(), center.getZ())) {
                this.updateBorderBounds(center);
            }
            return center;
        }
        
        @Override
        public boolean isWithinBorder(final Location location) {
            return this.worldBorder.isInside(location);
        }
        
        @Override
        public XWorldBorder copy() {
            final WorldBorder border = Bukkit.createWorldBorder();
            border.setCenter(this.worldBorder.getCenter());
            border.setSize(this.worldBorder.getSize());
            border.setDamageBuffer(this.worldBorder.getDamageBuffer());
            border.setDamageAmount(this.worldBorder.getDamageAmount());
            border.setWarningDistance(this.worldBorder.getWarningDistance());
            border.setWarningTime(this.worldBorder.getWarningTime());
            return new BukkitWorldBorder(border);
        }
    }
    
    public static final class Events implements Listener
    {
        @EventHandler
        public void onJoin(final PlayerMoveEvent event) {
            final XWorldBorder wb = XWorldBorder.get(event.getPlayer());
            if (wb == null) {
                return;
            }
            final Player p = event.getPlayer();
            final Location loc = p.getLocation();
            if (wb.isWithinBorder(loc)) {
                return;
            }
            final double distance = wb.getDistanceToBorder(loc);
            if (distance < wb.getDamageBuffer()) {
                return;
            }
            p.damage(wb.getDamageBuffer() * distance);
        }
        
        @EventHandler
        public void onJoin(final PlayerJoinEvent event) {
            final Player player = event.getPlayer();
            final XWorldBorder wb = XWorldBorder.get(player);
            if (wb == null) {
                return;
            }
            wb.setFor((Collection<Player>)Collections.singleton((Object)player), true);
        }
        
        @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
        public void onWorldChange(final PlayerChangedWorldEvent event) {
            final Player player = event.getPlayer();
            final XWorldBorder wb = XWorldBorder.get(player);
            if (wb == null) {
                return;
            }
            wb.setFor((Collection<Player>)Collections.singleton((Object)player), true);
        }
    }
}
