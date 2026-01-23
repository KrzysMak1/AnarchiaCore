/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Boolean
 *  java.lang.CharSequence
 *  java.lang.Class
 *  java.lang.Exception
 *  java.lang.IllegalArgumentException
 *  java.lang.Integer
 *  java.lang.NumberFormatException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.lang.reflect.Method
 *  java.util.ArrayList
 *  java.util.Collection
 *  java.util.Collections
 *  java.util.HashMap
 *  java.util.LinkedHashMap
 *  java.util.LinkedHashSet
 *  java.util.List
 *  java.util.Map
 *  java.util.UUID
 *  org.bukkit.Material
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.inventory.ClickType
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.event.player.AsyncPlayerChatEvent
 *  org.bukkit.inventory.ItemFlag
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.plugin.Plugin
 */
package me.anarchiacore.customitems.stormitemy.listeners;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import me.anarchiacore.customitems.stormitemy.Main;
import me.anarchiacore.customitems.stormitemy.core.ItemRegistry;
import me.anarchiacore.customitems.stormitemy.messages.A;
import me.anarchiacore.customitems.stormitemy.ui.gui.B;
import me.anarchiacore.customitems.stormitemy.utils.color.A;

public class C
implements Listener {
    private final Main G;
    private final B F;
    private final Map<UUID, Boolean> E = new HashMap();
    private final Map<UUID, ItemStack> M = new HashMap();
    private final Map<UUID, Integer> B = new HashMap();
    private final Map<UUID, Integer> D = new HashMap();
    private final Map<UUID, Boolean> L = new HashMap();
    private final Map<UUID, Integer> K = new HashMap();
    private final Map<UUID, Boolean> A = new HashMap();
    private final Map<UUID, Boolean> H = new HashMap();
    private final Map<UUID, Boolean> I = new HashMap();
    private final Map<UUID, Boolean> C = new HashMap();
    private final Map<UUID, Boolean> J = new HashMap();

    public C(Main main, B b2) {
        this.G = main;
        this.F = b2;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent inventoryClickEvent) {
        String string = inventoryClickEvent.getView().getTitle();
        Player player = (Player)inventoryClickEvent.getWhoClicked();
        UUID uUID = player.getUniqueId();
        if (string.contains((CharSequence)me.anarchiacore.customitems.stormitemy.utils.color.A.C("\u00a7x\u00a7F\u00a7F\u00a7F\u00a79\u00a71\u00a7A\u26a1 \u00a78\u1d07\u1d05\u028f\u1d1b\u1d0f\u0280 \u1d18\u0280\u1d22\u1d07\u1d05\u1d0d\u026a\u1d0f\u1d1b\u00f3\u1d21"))) {
            int n2;
            Object object;
            inventoryClickEvent.setCancelled(true);
            int n3 = inventoryClickEvent.getRawSlot();
            int n4 = 0;
            int n5 = 1;
            try {
                object = me.anarchiacore.customitems.stormitemy.utils.color.A.B(string);
                n2 = object.lastIndexOf("(");
                int n6 = object.lastIndexOf("/");
                int n7 = object.lastIndexOf(")");
                if (n2 > 0 && n6 > n2 && n7 > n6) {
                    n4 = Integer.parseInt((String)object.substring(n2 + 1, n6).trim()) - 1;
                    n5 = Integer.parseInt((String)object.substring(n6 + 1, n7).trim());
                }
            }
            catch (Exception exception) {
                // empty catch block
            }
            if (n3 == 48 && n4 > 0) {
                this.F.A(player, n4 - 1, n5);
                return;
            }
            if (n3 == 50 && n4 < n5 - 1) {
                this.F.A(player, n4 + 1, n5);
                return;
            }
            if (n3 == 49) {
                this.G.getPanelCommand().openPanelGUI(player);
                return;
            }
            object = new int[]{10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43};
            n2 = 0;
            for (Object object2 : object) {
                if (n3 != object2) continue;
                n2 = 1;
                break;
            }
            if (n2 != 0 && inventoryClickEvent.getCurrentItem() != null) {
                ItemStack itemStack = inventoryClickEvent.getCurrentItem().clone();
                this.M.put((Object)uUID, (Object)itemStack);
                this.B.put((Object)uUID, (Object)n4);
                this.D.put((Object)uUID, (Object)n5);
                this.F.A(player, itemStack);
            }
        } else if (string.contains((CharSequence)"\u1d07\u1d05\u028f\u1d1b\u1d0f\u0280 \u029f\u1d0f\u0280\u1d07")) {
            inventoryClickEvent.setCancelled(true);
            int n8 = inventoryClickEvent.getRawSlot();
            ItemStack itemStack = (ItemStack)this.M.get((Object)uUID);
            if (itemStack == null) {
                return;
            }
            if (n8 == 39) {
                this.F.A(player, itemStack);
                return;
            }
            if (n8 == 41 && inventoryClickEvent.getCurrentItem() != null && inventoryClickEvent.getCurrentItem().getType() == Material.LIME_DYE) {
                this.L.put((Object)uUID, (Object)true);
                this.K.put((Object)uUID, (Object)-1);
                player.closeInventory();
                me.anarchiacore.customitems.stormitemy.messages.A a2 = new A._A().D(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8\u00bb &7Wpisz &#FFD300now\u0105 &7linijk\u0119 lore &fprzedmiotu &8(&7Wpisz &#FF0000&7, aby anulowa\u0107&8)&7!")).B(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7Wpisz &#FFD300now\u0105 &7linijk\u0119 lore na &fchacie&7!")).A(10).B(60).C(15).A();
                me.anarchiacore.customitems.stormitemy.messages.C.A(player, a2);
                this.A(player, "ENTITY_EXPERIENCE_ORB_PICKUP");
                return;
            }
            int[] nArray = new int[]{10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25};
            int n9 = -1;
            for (int i2 = 0; i2 < nArray.length; ++i2) {
                if (n8 != nArray[i2]) continue;
                n9 = i2;
                break;
            }
            if (n9 != -1 && inventoryClickEvent.getCurrentItem() != null && inventoryClickEvent.getCurrentItem().getType() == Material.PAPER) {
                ArrayList arrayList;
                ArrayList arrayList2 = arrayList = itemStack.getItemMeta().hasLore() ? new ArrayList((Collection)itemStack.getItemMeta().getLore()) : new ArrayList();
                if (n9 >= arrayList.size()) {
                    return;
                }
                if (inventoryClickEvent.getClick() == ClickType.SHIFT_RIGHT) {
                    arrayList.remove(n9);
                    this.A(player, itemStack, (List<String>)arrayList);
                    this.F.A(null, itemStack, (List<String>)arrayList);
                    this.F.C(player, itemStack);
                    me.anarchiacore.customitems.stormitemy.messages.A a3 = new A._A().D(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8\u00bb &7Linijka lore zosta\u0142a &#FF0000usuni\u0119ta&7!")).B(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&fLinijka &7zosta\u0142a &#FF0000usuni\u0119ta&7!")).A(10).B(60).C(15).A();
                    me.anarchiacore.customitems.stormitemy.messages.C.A(player, a3);
                    this.A(player, "ENTITY_ENDERMAN_TELEPORT");
                    return;
                }
                if (inventoryClickEvent.getClick() == ClickType.LEFT && n9 > 0) {
                    Collections.swap((List)arrayList, (int)n9, (int)(n9 - 1));
                    this.A(player, itemStack, (List<String>)arrayList);
                    this.F.A(null, itemStack, (List<String>)arrayList);
                    this.F.C(player, itemStack);
                    this.A(player, "ENTITY_EXPERIENCE_ORB_PICKUP");
                    return;
                }
                if (inventoryClickEvent.getClick() == ClickType.RIGHT && n9 < arrayList.size() - 1) {
                    Collections.swap((List)arrayList, (int)n9, (int)(n9 + 1));
                    this.A(player, itemStack, (List<String>)arrayList);
                    this.F.A(null, itemStack, (List<String>)arrayList);
                    this.F.C(player, itemStack);
                    this.A(player, "ENTITY_EXPERIENCE_ORB_PICKUP");
                    return;
                }
            }
        } else if (string.contains((CharSequence)"\u1d07\u1d05\u028f\u1d1b\u1d0f\u0280 \ua730\u029f\u1d00\u0262")) {
            inventoryClickEvent.setCancelled(true);
            int n10 = inventoryClickEvent.getRawSlot();
            ItemStack itemStack = (ItemStack)this.M.get((Object)uUID);
            if (itemStack == null) {
                return;
            }
            if (n10 == 39) {
                this.F.A(player, itemStack);
                return;
            }
            if (n10 == 41 && inventoryClickEvent.getCurrentItem() != null && inventoryClickEvent.getCurrentItem().getType() == Material.LIME_DYE) {
                this.I.put((Object)uUID, (Object)true);
                player.closeInventory();
                me.anarchiacore.customitems.stormitemy.messages.A a4 = new A._A().D(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8\u00bb &7Wpisz &#FFD300flag\u0119 &8(&7np. &#FFD300HIDE_ATTRIBUTES&7)&8:")).B(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7Wpisz flag\u0119 na &fchacie&7!")).A(10).B(60).C(15).A();
                me.anarchiacore.customitems.stormitemy.messages.C.A(player, a4);
                this.A(player, "ENTITY_EXPERIENCE_ORB_PICKUP");
                return;
            }
            int[] nArray = new int[]{10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25};
            int n11 = -1;
            for (int i3 = 0; i3 < nArray.length; ++i3) {
                if (n10 != nArray[i3]) continue;
                n11 = i3;
                break;
            }
            if (n11 != -1 && inventoryClickEvent.getCurrentItem() != null && inventoryClickEvent.getCurrentItem().getType() == Material.PAPER && inventoryClickEvent.getClick() == ClickType.SHIFT_RIGHT) {
                LinkedHashSet linkedHashSet = new LinkedHashSet((Collection)itemStack.getItemMeta().getItemFlags());
                if (n11 < linkedHashSet.size()) {
                    ItemFlag itemFlag = (ItemFlag)linkedHashSet.toArray()[n11];
                    ItemMeta itemMeta = itemStack.getItemMeta();
                    itemMeta.removeItemFlags(new ItemFlag[]{itemFlag});
                    itemStack.setItemMeta(itemMeta);
                    this.M.put((Object)uUID, (Object)itemStack);
                    String string2 = me.anarchiacore.customitems.stormitemy.utils.color.A.B(itemStack.getItemMeta().getDisplayName());
                    String string3 = this.F.B(string2);
                    if (string3 != null) {
                        this.F.A(string3, itemStack, player);
                    }
                    me.anarchiacore.customitems.stormitemy.messages.A a5 = new A._A().D(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8\u00bb &7Flaga zosta\u0142a &#FF0000usuni\u0119ta&7!")).B(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&fFlaga &7zosta\u0142a &#FF0000usuni\u0119ta&7!")).A(10).B(60).C(15).A();
                    me.anarchiacore.customitems.stormitemy.messages.C.A(player, a5);
                    this.A(player, "ENTITY_ENDERMAN_TELEPORT");
                }
                return;
            }
        } else if (string.contains((CharSequence)"\u1d07\u1d05\u028f\u1d1b\u1d0f\u0280 \u1d07\u0274\u1d04\u029c\u1d00\u0274\u1d1b\u00f3\u1d21")) {
            inventoryClickEvent.setCancelled(true);
            int n12 = inventoryClickEvent.getRawSlot();
            ItemStack itemStack = (ItemStack)this.M.get((Object)uUID);
            if (itemStack == null) {
                return;
            }
            if (n12 == 39) {
                this.F.A(player, itemStack);
                return;
            }
            if (n12 == 41 && inventoryClickEvent.getCurrentItem() != null && inventoryClickEvent.getCurrentItem().getType() == Material.LIME_DYE) {
                this.H.put((Object)uUID, (Object)true);
                player.closeInventory();
                me.anarchiacore.customitems.stormitemy.messages.A a6 = new A._A().D(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8\u00bb &7Wpisz &#FFD300enchantment &7i &fpoziomu &8(&7np. &#FFD300SHARPNESS 5&7)&8:")).B(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7Wpisz enchantment na &fchacie&7!")).A(10).B(60).C(15).A();
                me.anarchiacore.customitems.stormitemy.messages.C.A(player, a6);
                this.A(player, "ENTITY_EXPERIENCE_ORB_PICKUP");
                return;
            }
            int[] nArray = new int[]{10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25};
            int n13 = -1;
            for (int i4 = 0; i4 < nArray.length; ++i4) {
                if (n12 != nArray[i4]) continue;
                n13 = i4;
                break;
            }
            if (n13 != -1 && inventoryClickEvent.getCurrentItem() != null && inventoryClickEvent.getCurrentItem().getType() == Material.ENCHANTED_BOOK && inventoryClickEvent.getClick() == ClickType.SHIFT_RIGHT) {
                LinkedHashMap linkedHashMap;
                LinkedHashMap linkedHashMap2 = linkedHashMap = itemStack.getItemMeta().hasEnchants() ? new LinkedHashMap(itemStack.getItemMeta().getEnchants()) : new LinkedHashMap();
                if (n13 < linkedHashMap.size()) {
                    Enchantment enchantment = (Enchantment)linkedHashMap.keySet().toArray()[n13];
                    itemStack.removeEnchantment(enchantment);
                    this.M.put((Object)uUID, (Object)itemStack);
                    String string4 = me.anarchiacore.customitems.stormitemy.utils.color.A.B(itemStack.getItemMeta().getDisplayName());
                    String string5 = this.F.B(string4);
                    if (string5 != null) {
                        this.F.B(string5, itemStack, player);
                    }
                    me.anarchiacore.customitems.stormitemy.messages.A a7 = new A._A().D(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8\u00bb &7Enchantment zosta\u0142 &#FF0000usuni\u0119ty&7!")).B(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&fEnchantment &7zosta\u0142 &#FF0000usuni\u0119ty&7!")).A(10).B(60).C(15).A();
                    me.anarchiacore.customitems.stormitemy.messages.C.A(player, a7);
                    this.A(player, "ENTITY_ENDERMAN_TELEPORT");
                }
                return;
            }
        } else if (string.contains((CharSequence)"\u1d07\u1d05\u028f\u1d1b\u1d0f\u0280") && !string.contains((CharSequence)"\u1d07\u1d05\u028f\u1d1b\u1d0f\u0280 \u029f\u1d0f\u0280\u1d07") && !string.contains((CharSequence)"\u1d07\u1d05\u028f\u1d1b\u1d0f\u0280 \u1d18\u0280\u1d22\u1d07\u1d05\u1d0d\u026a\u1d0f\u1d1b\u00f3\u1d21") && !string.contains((CharSequence)"\u1d07\u1d05\u028f\u1d1b\u1d0f\u0280 \u1d07\u0274\u1d04\u029c\u1d00\u0274\u1d1b\u00f3\u1d21")) {
            inventoryClickEvent.setCancelled(true);
            int n14 = inventoryClickEvent.getRawSlot();
            ItemStack itemStack = (ItemStack)this.M.get((Object)uUID);
            if (this.F.A(player, n14, itemStack)) {
                if (n14 == 11) {
                    player.closeInventory();
                    this.E.put((Object)uUID, (Object)true);
                    me.anarchiacore.customitems.stormitemy.messages.A a8 = new A._A().D(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8\u00bb &7Wpisz &#FFD300now\u0105 &7nazw\u0119 &fprzedmiotu &8(&7Wpisz &#FF0000anuluj&7, aby anulowa\u0107&8)&7!")).B(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7Wpisz &#FFD300now\u0105 &7nazw\u0119 przedmiotu na &fchacie&7!")).A(10).B(60).C(15).A();
                    me.anarchiacore.customitems.stormitemy.messages.C.A(player, a8);
                    this.A(player, "ENTITY_EXPERIENCE_ORB_PICKUP");
                } else if (n14 != 12) {
                    if (n14 == 13) {
                        player.closeInventory();
                        this.A.put((Object)uUID, (Object)true);
                        int n15 = itemStack.hasItemMeta() && itemStack.getItemMeta().hasCustomModelData() ? itemStack.getItemMeta().getCustomModelData() : 0;
                        me.anarchiacore.customitems.stormitemy.messages.A a9 = new A._A().D(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8\u00bb &7Wpisz &#FFD300nowy &7Custom Model Data &fprzedmiotu &8(&7Wpisz &#FF0000anuluj&7, aby anulowa\u0107&8)&7!")).B(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7Aktualnie: &f" + n15)).A(10).B(60).C(15).A();
                        me.anarchiacore.customitems.stormitemy.messages.C.A(player, a9);
                        this.A(player, "ENTITY_EXPERIENCE_ORB_PICKUP");
                    } else if (n14 == 39) {
                        Integer n16 = (Integer)this.B.getOrDefault((Object)uUID, (Object)0);
                        Integer n17 = (Integer)this.D.getOrDefault((Object)uUID, (Object)1);
                        this.F.A(player, (int)n16, (int)n17);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent asyncPlayerChatEvent) {
        Player player = asyncPlayerChatEvent.getPlayer();
        UUID uUID = player.getUniqueId();
        if (((Boolean)this.A.getOrDefault((Object)uUID, (Object)false)).booleanValue()) {
            asyncPlayerChatEvent.setCancelled(true);
            String string = asyncPlayerChatEvent.getMessage().trim();
            ItemStack itemStack = (ItemStack)this.M.get((Object)uUID);
            if (itemStack == null) {
                this.A.remove((Object)uUID);
                return;
            }
            if (string.equalsIgnoreCase("anuluj")) {
                me.anarchiacore.customitems.stormitemy.messages.A a2 = new A._A().B(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&fCustom Model Data &7zosta\u0142a &#FF0000anulowana&7!")).A(10).B(60).C(15).A();
                this.G.getServer().getScheduler().runTask((Plugin)this.G, () -> {
                    me.anarchiacore.customitems.stormitemy.messages.C.A(player, a2);
                    this.A(player, "ENTITY_ENDERMAN_TELEPORT");
                    this.F.A(player, itemStack);
                });
                this.A.remove((Object)uUID);
                return;
            }
            try {
                int n2 = Integer.parseInt((String)string);
                if (n2 < 0) {
                    me.anarchiacore.customitems.stormitemy.messages.A a3 = new A._A().D(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8\u00bb &7Custom Model Data &7nie mo\u017ce by\u0107 &#FF0000ujemny&7!")).B(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&fCustom Model Data &7musi by\u0107 &#FF0000dodatni&7!")).A(10).B(60).C(15).A();
                    me.anarchiacore.customitems.stormitemy.messages.C.A(player, a3);
                    this.A(player, "ENTITY_ENDERMAN_TELEPORT");
                    return;
                }
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.setCustomModelData(Integer.valueOf((int)n2));
                itemStack.setItemMeta(itemMeta);
                this.M.put((Object)uUID, (Object)itemStack);
                this.F.A(player, itemStack, n2);
                me.anarchiacore.customitems.stormitemy.messages.A a4 = new A._A().D(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8\u00bb &7Custom Model Data zosta\u0142o &#27FF00zmienione&7 na &f" + n2 + "&7!")).B(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&fCustom Model Data &7zmienione na &f" + n2)).A(10).B(60).C(15).A();
                this.G.getServer().getScheduler().runTask((Plugin)this.G, () -> {
                    me.anarchiacore.customitems.stormitemy.messages.C.A(player, a4);
                    this.A(player, "ENTITY_PLAYER_LEVELUP");
                });
                this.A.remove((Object)uUID);
            }
            catch (NumberFormatException numberFormatException) {
                me.anarchiacore.customitems.stormitemy.messages.A a5 = new A._A().D(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8\u00bb &7Custom Model Data musi by\u0107 &#FF0000liczb\u0105&7!")).B(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&fCustom Model Data &7musi by\u0107 &#FF0000liczb\u0105&7!")).A(10).B(60).C(15).A();
                me.anarchiacore.customitems.stormitemy.messages.C.A(player, a5);
                this.A(player, "ENTITY_ENDERMAN_TELEPORT");
            }
            return;
        }
        if (((Boolean)this.L.getOrDefault((Object)uUID, (Object)false)).booleanValue()) {
            ArrayList arrayList;
            asyncPlayerChatEvent.setCancelled(true);
            String string = asyncPlayerChatEvent.getMessage().trim();
            ItemStack itemStack = (ItemStack)this.M.get((Object)uUID);
            if (itemStack == null) {
                this.L.remove((Object)uUID);
                this.K.remove((Object)uUID);
                return;
            }
            if (string.equalsIgnoreCase("anuluj")) {
                me.anarchiacore.customitems.stormitemy.messages.A a6 = new A._A().B(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&fLinijka lore &7zosta\u0142a &#FF0000anulowana&7!")).A(10).B(60).C(15).A();
                this.G.getServer().getScheduler().runTask((Plugin)this.G, () -> {
                    me.anarchiacore.customitems.stormitemy.messages.C.A(player, a6);
                    this.A(player, "ENTITY_ENDERMAN_TELEPORT");
                    this.F.C(player, itemStack);
                });
                this.L.remove((Object)uUID);
                this.K.remove((Object)uUID);
                return;
            }
            ArrayList arrayList2 = arrayList = itemStack.getItemMeta().hasLore() ? new ArrayList((Collection)itemStack.getItemMeta().getLore()) : new ArrayList();
            if (arrayList.size() >= 14) {
                me.anarchiacore.customitems.stormitemy.messages.A a7 = new A._A().D(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8\u00bb &7Osi\u0105gni\u0119to maksymaln\u0105 liczb\u0119 &#FF0000linijek&7!")).B(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&fLinijka lore &7osi\u0105gn\u0119\u0142a &#FF0000limit&7!")).A(10).B(60).C(15).A();
                this.G.getServer().getScheduler().runTask((Plugin)this.G, () -> {
                    me.anarchiacore.customitems.stormitemy.messages.C.A(player, a7);
                    this.A(player, "ENTITY_ENDERMAN_TELEPORT");
                    this.F.C(player, itemStack);
                });
                this.L.remove((Object)uUID);
                this.K.remove((Object)uUID);
                return;
            }
            arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C(string));
            this.A(player, itemStack, (List<String>)arrayList);
            this.F.A(player, itemStack, (List<String>)arrayList);
            me.anarchiacore.customitems.stormitemy.messages.A a8 = new A._A().D(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8\u00bb &7Dodano now\u0105 &flinijk\u0119 &7lore &#27FF00pomy\u015blnie&7!")).B(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&fLinijka lore &7zosta\u0142a &#27FF00dodana&7!")).A(10).B(60).C(15).A();
            this.G.getServer().getScheduler().runTask((Plugin)this.G, () -> {
                me.anarchiacore.customitems.stormitemy.messages.C.A(player, a8);
                this.A(player, "ENTITY_PLAYER_LEVELUP");
            });
            this.L.remove((Object)uUID);
            this.K.remove((Object)uUID);
            return;
        }
        if (((Boolean)this.H.getOrDefault((Object)uUID, (Object)false)).booleanValue()) {
            asyncPlayerChatEvent.setCancelled(true);
            String string = asyncPlayerChatEvent.getMessage().trim();
            ItemStack itemStack = (ItemStack)this.M.get((Object)uUID);
            if (itemStack == null) {
                this.H.remove((Object)uUID);
                return;
            }
            if (string.equalsIgnoreCase("anuluj")) {
                me.anarchiacore.customitems.stormitemy.messages.A a9 = new A._A().B(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&fEnchantment &7zosta\u0142 &#FF0000anulowany&7!")).A(10).B(60).C(15).A();
                this.G.getServer().getScheduler().runTask((Plugin)this.G, () -> {
                    me.anarchiacore.customitems.stormitemy.messages.C.A(player, a9);
                    this.A(player, "ENTITY_ENDERMAN_TELEPORT");
                    this.F.B(player, itemStack);
                });
                this.H.remove((Object)uUID);
                return;
            }
            String[] stringArray = string.split(" ");
            if (stringArray.length < 2) {
                me.anarchiacore.customitems.stormitemy.messages.A a10 = new A._A().D(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8\u00bb &7Format: &#FFD300ENCHANTMENT_NAME LEVEL &8(&7np. &#FFD300SHARPNESS 5&7)")).B(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&fEnchantment &7format &#FF0000nieprawid\u0142owy&7!")).A(10).B(60).C(15).A();
                me.anarchiacore.customitems.stormitemy.messages.C.A(player, a10);
                this.A(player, "ENTITY_ENDERMAN_TELEPORT");
                return;
            }
            try {
                String string2 = stringArray[0].toUpperCase();
                int n3 = Integer.parseInt((String)stringArray[1]);
                Enchantment enchantment = null;
                enchantment = Enchantment.getByName((String)string2);
                if (enchantment == null) {
                    enchantment = Enchantment.getByName((String)("MINECRAFT_" + string2));
                }
                if (enchantment == null) {
                    for (Enchantment enchantment2 : Enchantment.values()) {
                        if (enchantment2 == null) continue;
                        String string3 = enchantment2.getKey().getKey();
                        if (string3.equalsIgnoreCase(string2)) {
                            enchantment = enchantment2;
                            break;
                        }
                        if (string3.replace((CharSequence)"minecraft:", (CharSequence)"").equalsIgnoreCase(string2)) {
                            enchantment = enchantment2;
                            break;
                        }
                        if (enchantment2.getName().equalsIgnoreCase(string2)) {
                            enchantment = enchantment2;
                            break;
                        }
                        if (!enchantment2.getName().replace((CharSequence)"MINECRAFT_", (CharSequence)"").equalsIgnoreCase(string2)) continue;
                        enchantment = enchantment2;
                        break;
                    }
                }
                if (enchantment == null) {
                    StringBuilder stringBuilder = new StringBuilder();
                    for (Enchantment enchantment3 : Enchantment.values()) {
                        if (enchantment3 == null) continue;
                        stringBuilder.append(enchantment3.getKey().getKey()).append(", ");
                    }
                    this.G.getLogger().warning("[ItemEditor] Dost\u0119pne enchantmenty: " + stringBuilder.toString());
                    me.anarchiacore.customitems.stormitemy.messages.A a2 = new A._A().D(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8\u00bb &7Enchantment &#FF0000nie istnieje&7!")).B(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&fEnchantment &7nie &#FF0000istnieje&7!")).A(10).B(60).C(15).A();
                    me.anarchiacore.customitems.stormitemy.messages.C.A(player, a2);
                    this.A(player, "ENTITY_ENDERMAN_TELEPORT");
                    return;
                }
                if (n3 < 1 || n3 > enchantment.getMaxLevel()) {
                    me.anarchiacore.customitems.stormitemy.messages.A a3 = new A._A().D(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8\u00bb &7Poziom musi by\u0107 od &f1 &7do &f" + enchantment.getMaxLevel() + "&7!")).B(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&fEnchantment &7poziom &#FF0000nieprawid\u0142owy&7!")).A(10).B(60).C(15).A();
                    me.anarchiacore.customitems.stormitemy.messages.C.A(player, a3);
                    this.A(player, "ENTITY_ENDERMAN_TELEPORT");
                    return;
                }
                itemStack.addUnsafeEnchantment(enchantment, n3);
                this.M.put((Object)uUID, (Object)itemStack);
                String string4 = me.anarchiacore.customitems.stormitemy.utils.color.A.B(itemStack.getItemMeta().getDisplayName());
                String string5 = this.F.B(string4);
                if (string5 != null) {
                    this.F.B(string5, itemStack, player);
                }
                me.anarchiacore.customitems.stormitemy.messages.A a4 = new A._A().D(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8\u00bb &7Enchantment &#27FF00" + string2 + " " + n3 + " &7zosta\u0142 &#27FF00dodany&7!")).B(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&fEnchantment &7zosta\u0142 &#27FF00dodany&7!")).A(10).B(60).C(15).A();
                this.G.getServer().getScheduler().runTask((Plugin)this.G, () -> {
                    me.anarchiacore.customitems.stormitemy.messages.C.A(player, a11);
                    this.A(player, "ENTITY_PLAYER_LEVELUP");
                });
            }
            catch (NumberFormatException numberFormatException) {
                me.anarchiacore.customitems.stormitemy.messages.A a12 = new A._A().D(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8\u00bb &7Poziom musi by\u0107 &#FF0000liczb\u0105&7!")).B(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&fEnchantment &7poziom &#FF0000liczba&7!")).A(10).B(60).C(15).A();
                me.anarchiacore.customitems.stormitemy.messages.C.A(player, a12);
                this.A(player, "ENTITY_ENDERMAN_TELEPORT");
            }
            this.H.remove((Object)uUID);
            return;
        }
        if (((Boolean)this.I.getOrDefault((Object)uUID, (Object)false)).booleanValue()) {
            asyncPlayerChatEvent.setCancelled(true);
            String string = asyncPlayerChatEvent.getMessage().trim().toUpperCase();
            ItemStack itemStack = (ItemStack)this.M.get((Object)uUID);
            if (itemStack == null) {
                this.I.remove((Object)uUID);
                return;
            }
            if (string.equalsIgnoreCase("anuluj")) {
                me.anarchiacore.customitems.stormitemy.messages.A a13 = new A._A().B(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&fFlaga &7zosta\u0142a &#FF0000anulowana&7!")).A(10).B(60).C(15).A();
                this.G.getServer().getScheduler().runTask((Plugin)this.G, () -> {
                    me.anarchiacore.customitems.stormitemy.messages.C.A(player, a13);
                    this.A(player, "ENTITY_ENDERMAN_TELEPORT");
                    this.F.E(player, itemStack);
                });
                this.I.remove((Object)uUID);
                return;
            }
            try {
                ItemFlag itemFlag = ItemFlag.valueOf((String)string);
                itemStack.getItemMeta().addItemFlags(new ItemFlag[]{itemFlag});
                this.M.put((Object)uUID, (Object)itemStack);
                String string5 = me.anarchiacore.customitems.stormitemy.utils.color.A.B(itemStack.getItemMeta().getDisplayName());
                String string6 = this.F.B(string5);
                if (string6 != null) {
                    this.F.A(string6, itemStack, player);
                }
                me.anarchiacore.customitems.stormitemy.messages.A a14 = new A._A().D(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8\u00bb &7Flaga &#27FF00" + itemFlag.name() + " &7zosta\u0142a &#27FF00dodana&7!")).B(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&fFlaga &7zosta\u0142a &#27FF00dodana&7!")).A(10).B(60).C(15).A();
                this.G.getServer().getScheduler().runTask((Plugin)this.G, () -> {
                    me.anarchiacore.customitems.stormitemy.messages.C.A(player, a14);
                    this.A(player, "ENTITY_PLAYER_LEVELUP");
                });
            }
            catch (IllegalArgumentException illegalArgumentException) {
                me.anarchiacore.customitems.stormitemy.messages.A a15 = new A._A().D(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8\u00bb &7Flaga &#FF0000nie istnieje&7!")).B(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&fFlaga &7nie &#FF0000istnieje&7!")).A(10).B(60).C(15).A();
                me.anarchiacore.customitems.stormitemy.messages.C.A(player, a15);
                this.A(player, "ENTITY_ENDERMAN_TELEPORT");
            }
            this.I.remove((Object)uUID);
            return;
        }
        if (((Boolean)this.C.getOrDefault((Object)uUID, (Object)false)).booleanValue()) {
            asyncPlayerChatEvent.setCancelled(true);
            String string = asyncPlayerChatEvent.getMessage().trim();
            ItemStack itemStack = (ItemStack)this.M.get((Object)uUID);
            if (itemStack == null) {
                this.C.remove((Object)uUID);
                return;
            }
            if (string.equalsIgnoreCase("anuluj")) {
                me.anarchiacore.customitems.stormitemy.messages.A a16 = new A._A().B(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&fCooldown &7zosta\u0142 &#FF0000anulowany&7!")).A(10).B(60).C(15).A();
                this.G.getServer().getScheduler().runTask((Plugin)this.G, () -> {
                    me.anarchiacore.customitems.stormitemy.messages.C.A(player, a16);
                    this.A(player, "ENTITY_ENDERMAN_TELEPORT");
                    this.F.A(player, itemStack);
                });
                this.C.remove((Object)uUID);
                return;
            }
            try {
                int n4 = Integer.parseInt((String)string);
                if (n4 < 0) {
                    me.anarchiacore.customitems.stormitemy.messages.A a17 = new A._A().D(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8\u00bb &7Cooldown &7nie mo\u017ce by\u0107 &#FF0000ujemny&7!")).B(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&fCooldown &7musi by\u0107 &#FF0000dodatni&7!")).A(10).B(60).C(15).A();
                    me.anarchiacore.customitems.stormitemy.messages.C.A(player, a17);
                    this.A(player, "ENTITY_ENDERMAN_TELEPORT");
                    return;
                }
                String string7 = me.anarchiacore.customitems.stormitemy.utils.color.A.B(itemStack.getItemMeta().getDisplayName());
                String string8 = this.A(string7);
                if (string8 != null) {
                    this.F.A(string8, n4, player);
                    me.anarchiacore.customitems.stormitemy.messages.A a18 = new A._A().D(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8\u00bb &7Cooldown zosta\u0142 &#27FF00zmieniony&7 na &f" + n4 + "s&7!")).B(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&fCooldown &7zmieniony na &f" + n4 + "s")).A(10).B(60).C(15).A();
                    this.G.getServer().getScheduler().runTask((Plugin)this.G, () -> {
                        me.anarchiacore.customitems.stormitemy.messages.C.A(player, a18);
                        this.A(player, "ENTITY_PLAYER_LEVELUP");
                    });
                    this.C.remove((Object)uUID);
                } else {
                    me.anarchiacore.customitems.stormitemy.messages.A a19 = new A._A().D(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8\u00bb &7Nie znaleziono &fprzedmiotu&7!")).B(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&fCooldown &7b\u0142\u0105d &#FF0000systemu&7!")).A(10).B(60).C(15).A();
                    me.anarchiacore.customitems.stormitemy.messages.C.A(player, a19);
                    this.A(player, "ENTITY_ENDERMAN_TELEPORT");
                }
            }
            catch (NumberFormatException numberFormatException) {
                me.anarchiacore.customitems.stormitemy.messages.A a20 = new A._A().D(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8\u00bb &7Cooldown musi by\u0107 &#FF0000liczb\u0105&7!")).B(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&fCooldown &7musi by\u0107 &#FF0000liczb\u0105&7!")).A(10).B(60).C(15).A();
                me.anarchiacore.customitems.stormitemy.messages.C.A(player, a20);
                this.A(player, "ENTITY_ENDERMAN_TELEPORT");
            }
            return;
        }
        if (((Boolean)this.J.getOrDefault((Object)uUID, (Object)false)).booleanValue()) {
            asyncPlayerChatEvent.setCancelled(true);
            String string = asyncPlayerChatEvent.getMessage().trim();
            ItemStack itemStack = (ItemStack)this.M.get((Object)uUID);
            if (itemStack == null) {
                this.J.remove((Object)uUID);
                return;
            }
            if (string.equalsIgnoreCase("anuluj")) {
                me.anarchiacore.customitems.stormitemy.messages.A a21 = new A._A().B(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&fKolor cooldownu &7zosta\u0142 &#FF0000anulowany&7!")).A(10).B(60).C(15).A();
                this.G.getServer().getScheduler().runTask((Plugin)this.G, () -> {
                    me.anarchiacore.customitems.stormitemy.messages.C.A(player, a21);
                    this.A(player, "ENTITY_ENDERMAN_TELEPORT");
                    this.F.A(player, itemStack);
                });
                this.J.remove((Object)uUID);
                return;
            }
            try {
                String string9;
                String string10;
                String string11 = string.trim();
                if (!string11.startsWith("#") && !string11.startsWith("&#")) {
                    string11 = "#" + string11;
                }
                if (!string11.matches("^&#?[0-9A-Fa-f]{6}$")) {
                    me.anarchiacore.customitems.stormitemy.messages.A a22 = new A._A().D(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8\u00bb &7Kolor musi by\u0107 w &fformacie hex &8(&7np. &#FFD300#FF5555&7 lub &#FFD300&#FF5555&8)&7!")).B(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&fKolor cooldownu &7format &#FF0000nieprawid\u0142owy&7!")).A(10).B(60).C(15).A();
                    me.anarchiacore.customitems.stormitemy.messages.C.A(player, a22);
                    this.A(player, "ENTITY_ENDERMAN_TELEPORT");
                    return;
                }
                if (string11.startsWith("#")) {
                    string11 = "&" + string11;
                }
                if ((string10 = this.A(string9 = me.anarchiacore.customitems.stormitemy.utils.color.A.B(itemStack.getItemMeta().getDisplayName()))) != null) {
                    this.F.A(string10, string11, player);
                    me.anarchiacore.customitems.stormitemy.messages.A a23 = new A._A().D(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8\u00bb &7Kolor cooldownu zosta\u0142 &#27FF00zmieniony&7 na &f" + string11 + "&7!")).B(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&fKolor cooldownu &7zmieniony na &f" + string11)).A(10).B(60).C(15).A();
                    this.G.getServer().getScheduler().runTask((Plugin)this.G, () -> {
                        me.anarchiacore.customitems.stormitemy.messages.C.A(player, a23);
                        this.A(player, "ENTITY_PLAYER_LEVELUP");
                    });
                } else {
                    me.anarchiacore.customitems.stormitemy.messages.A a24 = new A._A().D(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8\u00bb &7Nie znaleziono &fprzedmiotu&7!")).B(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&fKolor cooldownu &7b\u0142\u0105d &#FF0000systemu&7!")).A(10).B(60).C(15).A();
                    me.anarchiacore.customitems.stormitemy.messages.C.A(player, a24);
                    this.A(player, "ENTITY_ENDERMAN_TELEPORT");
                }
            }
            catch (Exception exception) {
                me.anarchiacore.customitems.stormitemy.messages.A a25 = new A._A().D(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8\u00bb &7Kolor musi by\u0107 w &fformacie hex&7!")).B(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&fKolor cooldownu &7format &#FF0000nieprawid\u0142owy&7!")).A(10).B(60).C(15).A();
                me.anarchiacore.customitems.stormitemy.messages.C.A(player, a25);
                this.A(player, "ENTITY_ENDERMAN_TELEPORT");
            }
            return;
        }
        if (!((Boolean)this.E.getOrDefault((Object)uUID, (Object)false)).booleanValue()) {
            return;
        }
        asyncPlayerChatEvent.setCancelled(true);
        String string = asyncPlayerChatEvent.getMessage().trim();
        if (string.equalsIgnoreCase("/cancel") || string.equalsIgnoreCase("anuluj")) {
            me.anarchiacore.customitems.stormitemy.messages.A a26 = new A._A().B(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&fNazwa &7zosta\u0142a &#FF0000anulowana&7!")).A(10).B(60).C(15).A();
            me.anarchiacore.customitems.stormitemy.messages.C.A(player, a26);
            this.A(player, "ENTITY_ENDERMAN_TELEPORT");
            this.E.remove((Object)uUID);
            ItemStack itemStack = (ItemStack)this.M.get((Object)uUID);
            if (itemStack != null) {
                this.F.A(player, itemStack);
            }
            return;
        }
        String string12 = me.anarchiacore.customitems.stormitemy.utils.color.A.B(string);
        if (string12.isEmpty() || string12.trim().isEmpty()) {
            me.anarchiacore.customitems.stormitemy.messages.A a27 = new A._A().D(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8\u00bb &7Nazwa &fprzedmiotu &7nie mo\u017ce by\u0107 &#FF0000pusta&7!")).B(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&fNazwa &7nie mo\u017ce by\u0107 &#FF0000pusta&7!")).A(10).B(60).C(15).A();
            me.anarchiacore.customitems.stormitemy.messages.C.A(player, a27);
            this.A(player, "ENTITY_ENDERMAN_TELEPORT");
            return;
        }
        if (string12.length() > 50) {
            me.anarchiacore.customitems.stormitemy.messages.A a28 = new A._A().D(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8\u00bb &7Nazwa przedmiotu jest &#FF0000zbyt d\u0142uga&7! Maksymalnie &f50 znak\u00f3w&7.")).B(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&fNazwa &7jest zbyt &#FF0000d\u0142uga&7!")).A(10).B(60).C(15).A();
            me.anarchiacore.customitems.stormitemy.messages.C.A(player, a28);
            this.A(player, "ENTITY_ENDERMAN_TELEPORT");
            return;
        }
        ItemStack itemStack = (ItemStack)this.M.get((Object)uUID);
        if (itemStack != null) {
            this.G.getServer().getScheduler().runTask((Plugin)this.G, () -> {
                this.F.A(player, string12, itemStack);
                this.M.put((Object)uUID, (Object)itemStack);
                this.E.remove((Object)uUID);
            });
        }
    }

    private void A(Player player, String string) {
        try {
            player.playSound(player.getLocation(), string, 1.0f, 1.0f);
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public void cacheEditorState(Player player, int n2, int n3) {
        UUID uUID = player.getUniqueId();
        this.B.put((Object)uUID, (Object)n2);
        this.D.put((Object)uUID, (Object)n3);
    }

    public void clearPlayerData(Player player) {
        UUID uUID = player.getUniqueId();
        this.E.remove((Object)uUID);
        this.M.remove((Object)uUID);
        this.B.remove((Object)uUID);
        this.D.remove((Object)uUID);
        this.L.remove((Object)uUID);
        this.K.remove((Object)uUID);
        this.A.remove((Object)uUID);
        this.H.remove((Object)uUID);
        this.I.remove((Object)uUID);
        this.C.remove((Object)uUID);
        this.J.remove((Object)uUID);
    }

    public void setWaitingForCooldown(Player player, boolean bl) {
        UUID uUID = player.getUniqueId();
        if (bl) {
            this.C.put((Object)uUID, (Object)true);
        } else {
            this.C.remove((Object)uUID);
        }
    }

    public void setWaitingForCooldownColor(Player player, boolean bl) {
        UUID uUID = player.getUniqueId();
        if (bl) {
            this.J.put((Object)uUID, (Object)true);
        } else {
            this.J.remove((Object)uUID);
        }
    }

    private void A(Player player, ItemStack itemStack, List<String> list) {
        if (itemStack == null || !itemStack.hasItemMeta()) {
            return;
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setLore(list);
        itemStack.setItemMeta(itemMeta);
        this.M.put((Object)player.getUniqueId(), (Object)itemStack);
    }

    private String A(String string) {
        Map<String, Object> map = this.G.getItems();
        for (String string2 : map.keySet()) {
            try {
                Object object;
                ItemStack itemStack = ItemRegistry.getItemStack(string2);
                if (itemStack == null && (object = map.get((Object)string2)) != null) {
                    try {
                        Method method = object.getClass().getMethod("getItem", new Class[0]);
                        itemStack = (ItemStack)method.invoke(object, new Object[0]);
                    }
                    catch (Exception exception) {
                        // empty catch block
                    }
                }
                if (itemStack == null || !itemStack.hasItemMeta() || !itemStack.getItemMeta().hasDisplayName() || !(object = me.anarchiacore.customitems.stormitemy.utils.color.A.B(itemStack.getItemMeta().getDisplayName())).equals((Object)string)) continue;
                return string2;
            }
            catch (Exception exception) {
            }
        }
        return null;
    }
}

