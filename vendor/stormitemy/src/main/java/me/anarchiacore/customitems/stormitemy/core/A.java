/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.io.File
 *  java.lang.Class
 *  java.lang.Exception
 *  java.lang.NoSuchFieldException
 *  java.lang.NoSuchMethodException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.reflect.Field
 *  java.lang.reflect.Method
 *  java.util.Map
 *  java.util.Map$Entry
 *  org.bukkit.Bukkit
 *  org.bukkit.configuration.ConfigurationSection
 *  org.bukkit.configuration.file.YamlConfiguration
 */
package me.anarchiacore.customitems.stormitemy.core;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import me.anarchiacore.customitems.stormitemy.Main;
import me.anarchiacore.customitems.stormitemy.items.N;

public class A {
    private final Main C;
    private final Map<String, Object> B;
    private final Object A;

    public A(Main main, Map<String, Object> map, Object object) {
        this.C = main;
        this.B = map;
        this.A = object;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void A() {
        try {
            Object object = this.A;
            synchronized (object) {
                this.C.setItemsInitialized(false);
            }
            this.A("Rozpoczynam prze\u0142adowywanie przedmiot\u00f3w...");
            for (Map.Entry entry : this.B.entrySet()) {
                Object object2 = entry.getValue();
                if (object2 == null) continue;
                this.A(object2, (String)entry.getKey());
                if (!(object2 instanceof N)) continue;
                try {
                    Method method = object2.getClass().getMethod("refreshPouchesAfterReload", new Class[0]);
                    method.invoke(object2, new Object[0]);
                }
                catch (Exception exception) {}
            }
            this.B();
            this.C.setItemsInitialized(true);
            this.A("Wszystkie przedmioty zosta\u0142y prze\u0142adowane!");
        }
        catch (Exception exception) {
            this.C.getLogger().severe("B\u0142\u0105d podczas prze\u0142adowywania przedmiot\u00f3w: " + exception.getMessage());
            exception.printStackTrace();
            this.C.setItemsInitialized(true);
        }
    }

    private void A(Object object, String string) {
        block8: {
            try {
                File file = new File(this.C.getDataFolder(), "items/" + string + ".yml");
                if (!file.exists()) break block8;
                Class clazz = object.getClass();
                try {
                    Method method = clazz.getMethod("reload", new Class[0]);
                    method.invoke(object, new Object[0]);
                    return;
                }
                catch (NoSuchMethodException noSuchMethodException) {
                    try {
                        Field field = this.A(clazz);
                        if (field != null) {
                            field.setAccessible(true);
                            YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration((File)file);
                            ConfigurationSection configurationSection = yamlConfiguration.getConfigurationSection(string);
                            if (configurationSection == null) {
                                configurationSection = yamlConfiguration;
                            }
                            field.set(object, (Object)configurationSection);
                        }
                    }
                    catch (Exception exception) {}
                }
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
    }

    private Field A(Class<?> clazz) {
        String[] stringArray;
        for (String string : stringArray = new String[]{"config", "defaultConfig", "itemConfig"}) {
            try {
                return clazz.getDeclaredField(string);
            }
            catch (NoSuchFieldException noSuchFieldException) {
            }
        }
        for (String string : clazz.getDeclaredFields()) {
            if (!ConfigurationSection.class.isAssignableFrom(string.getType())) continue;
            return string;
        }
        return null;
    }

    private void A(String string) {
        Bukkit.getConsoleSender().sendMessage("\u00a78[\u00a72StormItemy\u00a78] \u00a77" + string);
    }

    private void B() {
        try {
            Field field = this.C.getClass().getDeclaredField("initializer");
            field.setAccessible(true);
            Object object = field.get((Object)this.C);
            if (object != null) {
                Field field2 = object.getClass().getDeclaredField("customItemsManager");
                field2.setAccessible(true);
                Object object2 = field2.get(object);
                if (object2 != null) {
                    Method method = object2.getClass().getMethod("reload", new Class[0]);
                    method.invoke(object2, new Object[0]);
                    this.A("W\u0142asne przedmioty z kreatora zosta\u0142y prze\u0142adowane!");
                }
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }
}

