/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.FunctionalInterface
 *  java.lang.Object
 *  org.bukkit.inventory.ItemStack
 */
package me.anarchiacore.customitems.stormitemy.core;

import org.bukkit.inventory.ItemStack;

@FunctionalInterface
public interface ItemProvider {
    public ItemStack getItem();
}

