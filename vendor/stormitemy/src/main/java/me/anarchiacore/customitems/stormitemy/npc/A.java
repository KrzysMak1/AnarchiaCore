/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  com.mojang.authlib.properties.Property
 *  java.lang.Boolean
 *  java.lang.Byte
 *  java.lang.Class
 *  java.lang.Double
 *  java.lang.Enum
 *  java.lang.Exception
 *  java.lang.IllegalArgumentException
 *  java.lang.Integer
 *  java.lang.NoSuchFieldException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.reflect.Constructor
 *  java.lang.reflect.Field
 *  java.lang.reflect.Method
 *  java.util.ArrayList
 *  java.util.Collection
 *  java.util.EnumSet
 *  java.util.HashMap
 *  java.util.Map
 *  java.util.UUID
 *  java.util.WeakHashMap
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.Particle
 *  org.bukkit.Sound
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.util.Vector
 */
package me.anarchiacore.customitems.stormitemy.npc;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class A {
    private static Plugin D;
    private static final Map<UUID, _A> F;
    private static int E;
    private static Method B;
    private static Map<Class<?>, Object> C;
    private static final int G = 25;
    private static final int A = 2;

    public static void A(Plugin plugin) {
        D = plugin;
        B = null;
        C.clear();
    }

    public static UUID A(Player player, Location location, int n2, double d2) {
        if (D == null) {
            Bukkit.getLogger().warning("[PacketPlayerBot] Plugin nie zosta\u0142 zainicjalizowany!");
            return null;
        }
        try {
            Object object;
            Method method;
            UUID uUID = UUID.randomUUID();
            String string = player.getName() + "_clone";
            int n3 = E++;
            GameProfile gameProfile = new GameProfile(uUID, string);
            try {
                method = player.getClass().getMethod("getProfile", new Class[0]);
                object = (GameProfile)method.invoke((Object)player, new Object[0]);
                if (object.getProperties().containsKey((Object)"textures")) {
                    Property property = (Property)object.getProperties().get((Object)"textures").iterator().next();
                    gameProfile.getProperties().put((Object)"textures", (Object)property);
                    D.getLogger().info("[DEBUG] Skopiowano tekstury skina");
                }
            }
            catch (Exception exception) {
                D.getLogger().warning("Nie mo\u017cna skopiowa\u0107 skina: " + exception.getMessage());
            }
            method = location.getDirection().normalize();
            method.setY(0);
            method.normalize();
            object = new _A(n3, uUID, gameProfile, location.clone(), (Vector)method, d2, n2);
            F.put((Object)uUID, object);
            D.getLogger().info("[DEBUG] ========================================");
            D.getLogger().info("[DEBUG] BOT SPAWN LOCATION:");
            D.getLogger().info("[DEBUG] X: " + location.getX());
            D.getLogger().info("[DEBUG] Y: " + location.getY());
            D.getLogger().info("[DEBUG] Z: " + location.getZ());
            D.getLogger().info("[DEBUG] Yaw: " + location.getYaw());
            D.getLogger().info("[DEBUG] Pitch: " + location.getPitch());
            D.getLogger().info("[DEBUG] World: " + location.getWorld().getName());
            D.getLogger().info("[DEBUG] Entity ID: " + n3);
            D.getLogger().info("[DEBUG] UUID: " + String.valueOf((Object)uUID));
            D.getLogger().info("[DEBUG] ========================================");
            for (Player player2 : Bukkit.getOnlinePlayers()) {
                me.anarchiacore.customitems.stormitemy.npc.A.A(player2, (_A)object);
            }
            me.anarchiacore.customitems.stormitemy.npc.A.B(uUID);
            location.getWorld().spawnParticle(Particle.CLOUD, location, 20, 0.3, 0.5, 0.3, 0.1);
            location.getWorld().playSound(location, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.2f);
            D.getLogger().info("Stworzono packet bota: " + string + " (ID: " + n3 + ")");
            return uUID;
        }
        catch (Exception exception) {
            D.getLogger().severe("B\u0142\u0105d podczas tworzenia bota: " + exception.getMessage());
            exception.printStackTrace();
            return null;
        }
    }

    private static void A(Player player, _A _A2) {
        try {
            String[] stringArray;
            Method method = player.getClass().getMethod("getHandle", new Class[0]);
            Object object = method.invoke((Object)player, new Object[0]);
            Object object2 = null;
            for (String string : stringArray = new String[]{"connection", "b", "c"}) {
                try {
                    Field field = object.getClass().getField(string);
                    object2 = field.get(object);
                    break;
                }
                catch (NoSuchFieldException noSuchFieldException) {
                }
            }
            if (object2 == null) {
                D.getLogger().warning("Nie mo\u017cna znale\u017a\u0107 connection dla gracza: " + player.getName());
                return;
            }
            me.anarchiacore.customitems.stormitemy.npc.A.A(object2, _A2, true);
            me.anarchiacore.customitems.stormitemy.npc.A.A(object2, _A2);
            me.anarchiacore.customitems.stormitemy.npc.A.B(object2, _A2);
            D.getLogger().info("[DEBUG] Bot pokazany graczowi: " + player.getName());
        }
        catch (Exception exception) {
            D.getLogger().warning("B\u0142\u0105d podczas pokazywania bota: " + exception.getMessage());
            exception.printStackTrace();
        }
    }

    private static void A(Object object, _A _A2, boolean bl) {
        try {
            Class clazz = Class.forName((String)"net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket");
            Class clazz2 = null;
            for (Class clazz3 : clazz.getDeclaredClasses()) {
                if (!clazz3.isEnum() || !clazz3.getSimpleName().equals((Object)"a") && !clazz3.getSimpleName().equals((Object)"Action")) continue;
                clazz2 = clazz3;
                break;
            }
            if (clazz2 == null) {
                D.getLogger().warning("[DEBUG] Nie znaleziono klasy Action w pakiecie PlayerInfo");
                return;
            }
            Object object2 = null;
            for (Object object3 : clazz2.getEnumConstants()) {
                if (!((Enum)object3).name().equals((Object)"ADD_PLAYER")) continue;
                object2 = object3;
                break;
            }
            if (object2 == null) {
                D.getLogger().warning("[DEBUG] Nie znaleziono akcji ADD_PLAYER");
                return;
            }
            EnumSet enumSet = EnumSet.of((Enum)((Enum)object2));
            Class clazz4 = null;
            for (Class clazz5 : clazz.getDeclaredClasses()) {
                if (!clazz5.getSimpleName().equals((Object)"b") && !clazz5.getSimpleName().equals((Object)"Entry")) continue;
                clazz4 = clazz5;
                break;
            }
            if (clazz4 == null) {
                D.getLogger().warning("[DEBUG] Nie znaleziono klasy Entry");
                return;
            }
            Constructor constructor = null;
            for (Constructor constructor2 : clazz4.getDeclaredConstructors()) {
                if (constructor2.getParameterCount() < 4) continue;
                constructor = constructor2;
                break;
            }
            if (constructor == null) {
                D.getLogger().warning("[DEBUG] Nie znaleziono konstruktora Entry");
                return;
            }
            constructor.setAccessible(true);
            Object[] objectArray = new Object[constructor.getParameterCount()];
            objectArray[0] = _A2.D;
            objectArray[1] = _A2.C;
            objectArray[2] = Boolean.TRUE;
            objectArray[3] = Integer.valueOf((int)0);
            for (int i2 = 4; i2 < objectArray.length; ++i2) {
                objectArray[i2] = null;
            }
            Object object4 = constructor.newInstance(objectArray);
            ArrayList arrayList = new ArrayList();
            arrayList.add(object4);
            Constructor constructor3 = clazz.getConstructor(new Class[]{EnumSet.class, Collection.class});
            Object object5 = constructor3.newInstance(new Object[]{enumSet, arrayList});
            me.anarchiacore.customitems.stormitemy.npc.A.A(object, object5);
            D.getLogger().info("[DEBUG] \u2713 Wys\u0142ano pakiet PlayerInfo (ADD_PLAYER)");
        }
        catch (Exception exception) {
            D.getLogger().warning("[DEBUG] B\u0142\u0105d pakietu PlayerInfo: " + exception.getMessage());
            exception.printStackTrace();
        }
    }

    private static void A(Object object, _A _A2) {
        try {
            Class clazz = Class.forName((String)"net.minecraft.network.protocol.game.PacketPlayOutNamedEntitySpawn");
            Object object2 = null;
            try {
                Constructor constructor = clazz.getDeclaredConstructor(new Class[0]);
                constructor.setAccessible(true);
                object2 = constructor.newInstance(new Object[0]);
                me.anarchiacore.customitems.stormitemy.npc.A.A(object2, "a", _A2.B);
                me.anarchiacore.customitems.stormitemy.npc.A.A(object2, "b", _A2.D);
                me.anarchiacore.customitems.stormitemy.npc.A.A(object2, "c", _A2.A.getX());
                me.anarchiacore.customitems.stormitemy.npc.A.A(object2, "d", _A2.A.getY());
                me.anarchiacore.customitems.stormitemy.npc.A.A(object2, "e", _A2.A.getZ());
                me.anarchiacore.customitems.stormitemy.npc.A.A(object2, "f", (byte)(_A2.A.getYaw() * 256.0f / 360.0f));
                me.anarchiacore.customitems.stormitemy.npc.A.A(object2, "g", (byte)(_A2.A.getPitch() * 256.0f / 360.0f));
                me.anarchiacore.customitems.stormitemy.npc.A.A(object, object2);
                D.getLogger().info("[DEBUG] \u2713 Wys\u0142ano pakiet Spawn Player (przez pola)");
                return;
            }
            catch (Exception exception) {
                D.getLogger().info("[DEBUG] Nie mo\u017cna utworzy\u0107 pakietu przez pola: " + exception.getMessage());
                try {
                    Class clazz2 = Class.forName((String)"net.minecraft.network.PacketDataSerializer");
                    Class clazz3 = Class.forName((String)"io.netty.buffer.ByteBuf");
                    Object object3 = Class.forName((String)"io.netty.buffer.Unpooled").getMethod("buffer", new Class[0]).invoke(null, new Object[0]);
                    Constructor constructor = clazz2.getConstructor(new Class[]{clazz3});
                    Object object4 = constructor.newInstance(new Object[]{object3});
                    Method method = clazz2.getMethod("d", new Class[]{Integer.TYPE});
                    Method method2 = clazz2.getMethod("a", new Class[]{UUID.class});
                    Method method3 = clazz2.getMethod("writeDouble", new Class[]{Double.TYPE});
                    Method method4 = clazz2.getMethod("writeByte", new Class[]{Integer.TYPE});
                    byte by = (byte)(_A2.A.getYaw() * 256.0f / 360.0f);
                    byte by2 = (byte)(_A2.A.getPitch() * 256.0f / 360.0f);
                    D.getLogger().info("[DEBUG] Pakiet Spawn - zapisuj\u0119 dane:");
                    D.getLogger().info("[DEBUG]   Entity ID: " + _A2.B);
                    D.getLogger().info("[DEBUG]   UUID: " + String.valueOf((Object)_A2.D));
                    D.getLogger().info("[DEBUG]   X: " + _A2.A.getX());
                    D.getLogger().info("[DEBUG]   Y: " + _A2.A.getY());
                    D.getLogger().info("[DEBUG]   Z: " + _A2.A.getZ());
                    D.getLogger().info("[DEBUG]   Yaw (byte): " + by);
                    D.getLogger().info("[DEBUG]   Pitch (byte): " + by2);
                    method.invoke(object4, new Object[]{_A2.B});
                    method2.invoke(object4, new Object[]{_A2.D});
                    method3.invoke(object4, new Object[]{_A2.A.getX()});
                    method3.invoke(object4, new Object[]{_A2.A.getY()});
                    method3.invoke(object4, new Object[]{_A2.A.getZ()});
                    method4.invoke(object4, new Object[]{by});
                    method4.invoke(object4, new Object[]{by2});
                    Constructor constructor2 = clazz.getConstructor(new Class[]{clazz2});
                    object2 = constructor2.newInstance(new Object[]{object4});
                    me.anarchiacore.customitems.stormitemy.npc.A.A(object, object2);
                    D.getLogger().info("[DEBUG] \u2713 Wys\u0142ano pakiet Spawn Player (przez serializer)");
                }
                catch (Exception exception2) {
                    D.getLogger().warning("[DEBUG] Nie mo\u017cna utworzy\u0107 pakietu Spawn: " + exception2.getMessage());
                }
            }
        }
        catch (Exception exception) {
            D.getLogger().warning("[DEBUG] B\u0142\u0105d pakietu Spawn: " + exception.getMessage());
        }
    }

    private static void A(Object object, String string, Object object2) {
        try {
            Field field = object.getClass().getDeclaredField(string);
            field.setAccessible(true);
            field.set(object, object2);
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    private static void B(Object object, _A _A2) {
        try {
            Class clazz = Class.forName((String)"net.minecraft.network.protocol.game.PacketPlayOutEntityHeadRotation");
            Constructor constructor = null;
            for (Constructor constructor2 : clazz.getConstructors()) {
                Class[] classArray = constructor2.getParameterTypes();
                if (classArray.length != 2 || classArray[1] != Byte.TYPE) continue;
                constructor = constructor2;
                break;
            }
            if (constructor == null) {
                D.getLogger().warning("[DEBUG] Nie znaleziono konstruktora dla PacketPlayOutEntityHeadRotation");
                return;
            }
            byte by = (byte)(_A2.A.getYaw() * 256.0f / 360.0f);
            try {
                Object object2 = constructor.newInstance(new Object[]{_A2.B, by});
                me.anarchiacore.customitems.stormitemy.npc.A.A(object, object2);
                D.getLogger().info("[DEBUG] \u2713 Wys\u0142ano pakiet Head Rotation");
            }
            catch (IllegalArgumentException illegalArgumentException) {
                D.getLogger().info("[DEBUG] Pomijam pakiet Head Rotation (wymaga Entity)");
            }
        }
        catch (Exception exception) {
            D.getLogger().warning("[DEBUG] B\u0142\u0105d pakietu Head Rotation: " + exception.getMessage());
        }
    }

    private static void A(Object object, Object object2) {
        try {
            for (Method method : object.getClass().getMethods()) {
                Class clazz;
                if (!method.getName().equals((Object)"send") && !method.getName().equals((Object)"a") || method.getParameterCount() != 1 || !(clazz = method.getParameterTypes()[0]).isAssignableFrom(object2.getClass())) continue;
                method.invoke(object, new Object[]{object2});
                return;
            }
        }
        catch (Exception exception) {
            D.getLogger().warning("B\u0142\u0105d wysy\u0142ania pakietu: " + exception.getMessage());
        }
    }

    private static void B(final UUID uUID) {
        final _A _A2 = (_A)F.get((Object)uUID);
        if (_A2 == null) {
            return;
        }
        if (F.size() > 25) {
            D.getLogger().warning("[PacketPlayerBot] Osi\u0105gni\u0119to limit bot\u00f3w (25), usuwam najstarszego");
            UUID uUID2 = (UUID)F.keySet().iterator().next();
            if (!uUID2.equals((Object)uUID)) {
                me.anarchiacore.customitems.stormitemy.npc.A.A(uUID2);
            }
        }
        new BukkitRunnable(){
            int ticks = 0;
            final int maxTicks;
            {
                this.maxTicks = _A2.F * 20;
            }

            public void run() {
                _A _A22 = (_A)F.get((Object)uUID);
                if (_A22 == null || this.ticks >= this.maxTicks) {
                    if (_A22 != null) {
                        me.anarchiacore.customitems.stormitemy.npc.A.A(uUID);
                    }
                    this.cancel();
                    return;
                }
                _A22.A.add(_A22.G.clone().multiply(_A22.E * 2.0));
                Location location = _A22.A;
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (!player.getWorld().equals((Object)location.getWorld()) || !(player.getLocation().distanceSquared(location) < 10000.0)) continue;
                    me.anarchiacore.customitems.stormitemy.npc.A.C(player, _A22);
                }
                if (this.ticks % 20 == 0) {
                    _A22.A.getWorld().spawnParticle(Particle.CLOUD, _A22.A.clone().add(0.0, 1.0, 0.0), 2, 0.2, 0.3, 0.2, 0.01);
                }
                this.ticks += 2;
            }
        }.runTaskTimer(D, 0L, 2L);
    }

    private static void C(Player player, _A _A2) {
        try {
            Object object;
            Class clazz;
            if (B == null) {
                B = player.getClass().getMethod("getHandle", new Class[0]);
            }
            Object object2 = B.invoke((Object)player, new Object[0]);
            Object object3 = null;
            for (String string : clazz = new Class[]{"connection", "b", "c"}) {
                try {
                    object = object2.getClass().getField(string);
                    object3 = object.get(object2);
                    break;
                }
                catch (NoSuchFieldException noSuchFieldException) {
                }
            }
            if (object3 != null) {
                Class clazz2 = Class.forName((String)"net.minecraft.network.protocol.game.PacketPlayOutEntityTeleport");
                Constructor constructor = clazz2.getConstructor(new Class[]{Integer.TYPE, Double.TYPE, Double.TYPE, Double.TYPE, Byte.TYPE, Byte.TYPE, Boolean.TYPE});
                int n2 = (byte)(_A2.A.getYaw() * 256.0f / 360.0f);
                byte by = (byte)(_A2.A.getPitch() * 256.0f / 360.0f);
                object = constructor.newInstance(new Object[]{_A2.B, _A2.A.getX(), _A2.A.getY(), _A2.A.getZ(), (byte)n2, by, false});
                me.anarchiacore.customitems.stormitemy.npc.A.A(object3, object);
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public static void A(UUID uUID) {
        _A _A2 = (_A)F.remove((Object)uUID);
        if (_A2 == null) {
            return;
        }
        try {
            _A2.A.getWorld().spawnParticle(Particle.CLOUD, _A2.A.clone().add(0.0, 1.0, 0.0), 30, 0.3, 0.5, 0.3, 0.1);
            _A2.A.getWorld().playSound(_A2.A, Sound.ENTITY_ENDERMAN_TELEPORT, 0.8f, 0.8f);
            for (Player player : Bukkit.getOnlinePlayers()) {
                me.anarchiacore.customitems.stormitemy.npc.A.B(player, _A2);
            }
            D.getLogger().info("Usuni\u0119to bota: " + _A2.C.getName());
        }
        catch (Exception exception) {
            D.getLogger().warning("B\u0142\u0105d podczas usuwania bota: " + exception.getMessage());
        }
    }

    private static void B(Player player, _A _A2) {
        try {
            Class clazz;
            Method method = player.getClass().getMethod("getHandle", new Class[0]);
            Object object = method.invoke((Object)player, new Object[0]);
            Object object2 = null;
            for (String string : clazz = new Class[]{"connection", "b", "c"}) {
                try {
                    Field field = object.getClass().getField(string);
                    object2 = field.get(object);
                    break;
                }
                catch (NoSuchFieldException noSuchFieldException) {
                }
            }
            if (object2 != null) {
                Class clazz2 = Class.forName((String)"net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy");
                Constructor constructor = clazz2.getConstructor(new Class[]{int[].class});
                Object object3 = constructor.newInstance(new Object[]{new int[]{_A2.B}});
                me.anarchiacore.customitems.stormitemy.npc.A.A(object2, object3);
                me.anarchiacore.customitems.stormitemy.npc.A.A(object2, _A2, false);
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public static void A() {
        ArrayList arrayList = new ArrayList((Collection)F.keySet());
        for (UUID uUID : arrayList) {
            me.anarchiacore.customitems.stormitemy.npc.A.A(uUID);
        }
    }

    static {
        F = new HashMap();
        E = 90000;
        B = null;
        C = new WeakHashMap();
    }

    private static class _A {
        int B;
        UUID D;
        GameProfile C;
        Location A;
        Vector G;
        double E;
        int F;

        _A(int n2, UUID uUID, GameProfile gameProfile, Location location, Vector vector, double d2, int n3) {
            this.B = n2;
            this.D = uUID;
            this.C = gameProfile;
            this.A = location;
            this.G = vector;
            this.E = d2;
            this.F = n3;
        }
    }
}
