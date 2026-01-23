/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.util.HashMap
 *  java.util.Map
 *  java.util.Set
 *  java.util.function.Supplier
 *  org.bukkit.inventory.ItemStack
 */
package me.anarchiacore.customitems.stormitemy.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import org.bukkit.inventory.ItemStack;

public class ItemRegistry {
    private static final Map<String, Supplier<ItemStack>> B = new HashMap();
    private static final Map<String, Object> A = new HashMap();

    public static void register(String string, Supplier<ItemStack> supplier, Object object) {
        B.put((Object)string, supplier);
        if (object != null) {
            A.put((Object)string, object);
        }
    }

    public static void register(String string, Supplier<ItemStack> supplier) {
        ItemRegistry.register(string, supplier, null);
    }

    public static ItemStack getItemStack(String string) {
        Supplier supplier = (Supplier)B.get((Object)string);
        if (supplier != null) {
            try {
                return (ItemStack)supplier.get();
            }
            catch (Exception exception) {
                return null;
            }
        }
        return null;
    }

    public static boolean isRegistered(String string) {
        return B.containsKey((Object)string);
    }

    public static Object getInstance(String string) {
        return A.get((Object)string);
    }

    public static Set<String> getRegisteredKeys() {
        return B.keySet();
    }

    public static void clear() {
        B.clear();
        A.clear();
    }

    public static int size() {
        return B.size();
    }
}

