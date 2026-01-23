/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  com.mojang.authlib.properties.Property
 *  java.lang.CharSequence
 *  java.lang.Class
 *  java.lang.ClassNotFoundException
 *  java.lang.Exception
 *  java.lang.NoSuchFieldException
 *  java.lang.NoSuchMethodException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.reflect.Constructor
 *  java.lang.reflect.Field
 *  java.lang.reflect.Method
 *  java.lang.reflect.Modifier
 *  java.net.URL
 *  java.util.Base64
 *  java.util.UUID
 *  org.bukkit.Bukkit
 *  org.bukkit.inventory.meta.SkullMeta
 *  org.bukkit.profile.PlayerProfile
 *  org.bukkit.profile.PlayerTextures
 */
package me.anarchiacore.customitems.stormitemy.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.Base64;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

public class A {
    public static void D(SkullMeta skullMeta, String string) {
        if (skullMeta == null || string == null || string.isEmpty()) {
            return;
        }
        if (A.C(skullMeta, string)) {
            return;
        }
        if (A.A(skullMeta, string)) {
            return;
        }
        A.B(skullMeta, string);
    }

    private static boolean C(SkullMeta skullMeta, String string) {
        try {
            Method method = SkullMeta.class.getMethod("setOwnerProfile", new Class[]{PlayerProfile.class});
            PlayerProfile playerProfile = Bukkit.createPlayerProfile((UUID)UUID.randomUUID(), (String)"");
            PlayerTextures playerTextures = playerProfile.getTextures();
            String string2 = A.A(string);
            if (string2 != null) {
                playerTextures.setSkin(new URL(string2));
                playerProfile.setTextures(playerTextures);
                method.invoke((Object)skullMeta, new Object[]{playerProfile});
                return true;
            }
        }
        catch (NoSuchMethodException noSuchMethodException) {
        }
        catch (Exception exception) {
            // empty catch block
        }
        return false;
    }

    private static boolean A(SkullMeta skullMeta, String string) {
        block5: {
            try {
                GameProfile gameProfile = new GameProfile(UUID.randomUUID(), "");
                gameProfile.getProperties().put((Object)"textures", (Object)new Property("textures", string));
                Field field = A.A(skullMeta.getClass());
                if (field == null) break block5;
                field.setAccessible(true);
                try {
                    Class clazz = Class.forName((String)"net.minecraft.world.item.component.ResolvableProfile");
                    Object object = A.A(clazz, gameProfile);
                    if (object != null) {
                        field.set((Object)skullMeta, object);
                        return true;
                    }
                }
                catch (ClassNotFoundException classNotFoundException) {
                    // empty catch block
                }
                field.set((Object)skullMeta, (Object)gameProfile);
                return true;
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        return false;
    }

    private static boolean B(SkullMeta skullMeta, String string) {
        try {
            GameProfile gameProfile = new GameProfile(UUID.randomUUID(), "");
            gameProfile.getProperties().put((Object)"textures", (Object)new Property("textures", string));
            Field field = skullMeta.getClass().getDeclaredField("profile");
            field.setAccessible(true);
            field.set((Object)skullMeta, (Object)gameProfile);
            return true;
        }
        catch (Exception exception) {
            return false;
        }
    }

    private static Field A(Class<?> clazz) {
        for (Class clazz2 = clazz; clazz2 != null && clazz2 != Object.class; clazz2 = clazz2.getSuperclass()) {
            try {
                return clazz2.getDeclaredField("profile");
            }
            catch (NoSuchFieldException noSuchFieldException) {
                continue;
            }
        }
        return null;
    }

    private static Object A(Class<?> clazz, GameProfile gameProfile) {
        try {
            for (Constructor constructor : clazz.getDeclaredConstructors()) {
                Class[] classArray = constructor.getParameterTypes();
                if (classArray.length != 1 || !classArray[0].isAssignableFrom(GameProfile.class)) continue;
                constructor.setAccessible(true);
                return constructor.newInstance(new Object[]{gameProfile});
            }
            for (Constructor constructor : clazz.getDeclaredMethods()) {
                if (!Modifier.isStatic((int)constructor.getModifiers()) || !clazz.isAssignableFrom(constructor.getReturnType()) || constructor.getParameterCount() != 1 || !constructor.getParameterTypes()[0].isAssignableFrom(GameProfile.class)) continue;
                constructor.setAccessible(true);
                return constructor.invoke(null, new Object[]{gameProfile});
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        return null;
    }

    private static String A(String string) {
        try {
            String[] stringArray;
            int n2;
            int n3;
            int n4;
            String string2 = new String(Base64.getDecoder().decode(string));
            if (string2.contains((CharSequence)"\"url\"") && (n4 = string2.indexOf("\"url\"")) != -1 && (n3 = string2.indexOf("\"", n4 + 5)) != -1 && (n2 = string2.indexOf("\"", ++n3)) != -1) {
                return string2.substring(n3, n2);
            }
            if (string2.contains((CharSequence)"url") && (stringArray = string2.split("\"url\"\\s*:\\s*\"")).length > 1) {
                return stringArray[1].split("\"")[0];
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        return null;
    }
}

