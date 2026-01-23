/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Boolean
 *  java.lang.CharSequence
 *  java.lang.Double
 *  java.lang.Exception
 *  java.lang.IllegalArgumentException
 *  java.lang.Integer
 *  java.lang.Math
 *  java.lang.NumberFormatException
 *  java.lang.Object
 *  java.lang.Runnable
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.Collection
 *  java.util.Collections
 *  java.util.HashMap
 *  java.util.LinkedHashMap
 *  java.util.LinkedHashSet
 *  java.util.List
 *  java.util.Map
 *  java.util.Set
 *  java.util.UUID
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.inventory.ClickType
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.event.player.AsyncPlayerChatEvent
 *  org.bukkit.inventory.ItemFlag
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 */
package me.anarchiacore.customitems.stormitemy.listeners;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import me.anarchiacore.customitems.stormitemy.Main;
import me.anarchiacore.customitems.stormitemy.messages.A;
import me.anarchiacore.customitems.stormitemy.messages.C;
import me.anarchiacore.customitems.stormitemy.ui.gui.D;
import me.anarchiacore.customitems.stormitemy.utils.color.A;

public class E
implements Listener {
    private final Main E;
    private final me.anarchiacore.customitems.stormitemy.ui.gui.E S;
    private final me.anarchiacore.customitems.stormitemy.ui.gui.C L;
    private final D D;
    private final Map<UUID, String> R = new HashMap();
    private final Map<UUID, Boolean> P = new HashMap();
    private final Map<UUID, Boolean> U = new HashMap();
    private final Map<UUID, Boolean> C = new HashMap();
    private final Map<UUID, Boolean> K = new HashMap();
    private final Map<UUID, Boolean> H = new HashMap();
    private final Map<UUID, Boolean> V = new HashMap();
    private final Map<UUID, Boolean> N = new HashMap();
    private final Map<UUID, Boolean> O = new HashMap();
    private final Map<UUID, Boolean> M = new HashMap();
    private final Map<UUID, Boolean> J = new HashMap();
    private final Map<UUID, Integer> I = new HashMap();
    private final Map<UUID, String> F = new HashMap();
    private final Map<UUID, Boolean> Q = new HashMap();
    private final Map<UUID, Integer> B = new HashMap();
    private final Map<UUID, Boolean> G = new HashMap();
    private final Map<UUID, String> A = new HashMap();
    private final Map<UUID, Boolean> T = new HashMap();

    public E(Main main, me.anarchiacore.customitems.stormitemy.ui.gui.E e2, me.anarchiacore.customitems.stormitemy.ui.gui.C c2, D d2) {
        this.E = main;
        this.S = e2;
        this.L = c2;
        this.D = d2;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent inventoryClickEvent) {
        String string = inventoryClickEvent.getView().getTitle();
        Player player = (Player)inventoryClickEvent.getWhoClicked();
        UUID uUID = player.getUniqueId();
        if (string.contains((CharSequence)me.anarchiacore.customitems.stormitemy.utils.color.A.C("\u00a7x\u00a7F\u00a7F\u00a7F\u00a79\u00a71\u00a7A\u26a1 \u00a78\u1d0b\u0280\u1d07\u1d00\u1d1b\u1d0f\u0280 \u1d18\u0280\u1d22\u1d07\u1d05\u1d0d\u026a\u1d0f\u1d1b\u00f3\u1d21"))) {
            this.A(inventoryClickEvent, player, uUID, string);
        } else if (string.contains((CharSequence)"\u1d0b\u0280\u1d07\u1d00\u1d1b\u1d0f\u0280") && !string.contains((CharSequence)"\u1d0b\u0280\u1d07\u1d00\u1d1b\u1d0f\u0280 \u1d18\u0280\u1d22\u1d07\u1d05\u1d0d\u026a\u1d0f\u1d1b\u00f3\u1d21")) {
            this.D(inventoryClickEvent, player, uUID);
        } else if (string.contains((CharSequence)"\u1d07\u1d05\u028f\u1d1b\u1d0f\u0280 \u029f\u1d0f\u0280\u1d07") && this.R.containsKey((Object)uUID)) {
            this.B(inventoryClickEvent, player, uUID);
        } else if (string.contains((CharSequence)"\u1d07\u1d05\u028f\u1d1b\u1d0f\u0280 \u1d07\u0274\u1d04\u029c\u1d00\u0274\u1d1b\u00f3\u1d21") && this.R.containsKey((Object)uUID)) {
            this.A(inventoryClickEvent, player, uUID);
        } else if (string.contains((CharSequence)"\u1d07\u1d05\u028f\u1d1b\u1d0f\u0280 \ua730\u029f\u1d00\u0262") && this.R.containsKey((Object)uUID)) {
            this.G(inventoryClickEvent, player, uUID);
        } else if (string.contains((CharSequence)"\u1d1c\u1d0d\u026a\u1d07\u1d0a\u1d07\u1d1b\u0274\u1d0f\u015b\u1d04\u026a") && this.R.containsKey((Object)uUID)) {
            this.E(inventoryClickEvent, player, uUID);
        } else if (string.contains((CharSequence)"\u1d21\u028f\u0299\u026a\u1d07\u0280\u1d22 \u1d1c\u1d0d\u026a\u1d07\u1d0a\u1d07\u1d1b\u0274\u1d0f\u015b\u0107") && this.R.containsKey((Object)uUID)) {
            this.C(inventoryClickEvent, player, uUID);
        } else if (string.contains((CharSequence)"\u1d21\u028f\u0299\u026a\u1d07\u0280\u1d22 \u1d07\ua730\u1d07\u1d0b\u1d1b") && this.R.containsKey((Object)uUID)) {
            this.I(inventoryClickEvent, player, uUID);
        } else if (string.contains((CharSequence)"\u1d21\u026a\u1d00\u1d05\u1d0f\u1d0d\u1d0f\u015b\u1d04\u026a") && this.R.containsKey((Object)uUID)) {
            this.F(inventoryClickEvent, player, uUID);
        } else if (string.contains((CharSequence)"\u1d07\u1d05\u028f\u1d1b\u1d0f\u0280") && this.B.containsKey((Object)uUID)) {
            this.H(inventoryClickEvent, player, uUID);
        }
    }

    private void A(InventoryClickEvent inventoryClickEvent, Player player, UUID uUID, String string) {
        inventoryClickEvent.setCancelled(true);
        int n2 = inventoryClickEvent.getRawSlot();
        int n3 = this.B(string);
        int n4 = this.C(string);
        this.I.put((Object)uUID, (Object)n3);
        if (n2 == 48 && n3 > 0) {
            this.L.A(player, n3 - 1);
        } else if (n2 == 50 && n3 < n4 - 1) {
            this.L.A(player, n3 + 1);
        } else if (n2 == 49) {
            this.E.getPanelCommand().openPanelGUI(player);
        } else if (n2 == 52) {
            player.closeInventory();
            this.P.put((Object)uUID, (Object)true);
            this.B(player, "&8\u00bb &7Wpisz &#FFD300ID &7nowego przedmiotu &8(&7Wpisz &#FF0000anuluj&7, aby anulowa\u0107&8)&7!", "&7Wpisz &#FFD300ID &7przedmiotu na &fchacie&7!");
        } else {
            this.A(inventoryClickEvent, player, uUID, n2, n3);
        }
    }

    private void A(InventoryClickEvent inventoryClickEvent, Player player, UUID uUID, int n2, int n3) {
        int[] nArray;
        for (int n4 : nArray = new int[]{10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43}) {
            ItemStack itemStack;
            if (n2 != n4 || inventoryClickEvent.getCurrentItem() == null || inventoryClickEvent.getCurrentItem().getType() == Material.AIR) continue;
            String string = this.A(inventoryClickEvent.getCurrentItem());
            if (string == null) break;
            if (inventoryClickEvent.getClick() == ClickType.SHIFT_RIGHT) {
                if (!this.S.K(string)) break;
                this.A(player, "&8\u00bb &7Przedmiot &f" + string + " &7zosta\u0142 &#FF0000usuni\u0119ty&7!", "&fPrzedmiot &7zosta\u0142 &#FF0000usuni\u0119ty&7!", "ENTITY_ENDERMAN_TELEPORT");
                this.L.A(player, n3);
                break;
            }
            if (inventoryClickEvent.getClick() == ClickType.RIGHT) {
                this.R.put((Object)uUID, (Object)string);
                this.D.E(player, string);
                break;
            }
            if (inventoryClickEvent.getClick() != ClickType.LEFT || (itemStack = this.S.G(string)) == null) break;
            ItemStack itemStack2 = itemStack.clone();
            player.getInventory().addItem(new ItemStack[]{itemStack2});
            this.A(player, "&8\u00bb &7Przedmiot &f" + string + " &7zosta\u0142 &#1DFF1Adodany &7do ekwipunku!", "&fPrzedmiot &7dodany do &#1DFF1AEQ&7!", "ENTITY_ITEM_PICKUP");
            break;
        }
    }

    private void D(InventoryClickEvent inventoryClickEvent, Player player, UUID uUID) {
        inventoryClickEvent.setCancelled(true);
        String string = (String)this.R.get((Object)uUID);
        if (string == null) {
            return;
        }
        ItemStack itemStack = this.S.G(string);
        if (itemStack == null) {
            return;
        }
        int n2 = inventoryClickEvent.getRawSlot();
        switch (n2) {
            case 11: {
                this.B(player, uUID, itemStack);
                break;
            }
            case 12: {
                this.D.F(player, string);
                break;
            }
            case 13: {
                this.A(player, uUID, itemStack);
                break;
            }
            case 14: {
                this.D.G(player, string);
                break;
            }
            case 15: {
                this.D.A(player, string);
                break;
            }
            case 20: {
                this.D(player, uUID, string);
                break;
            }
            case 21: {
                this.C(player, uUID, string);
                break;
            }
            case 22: {
                this.A(player, string, itemStack);
                break;
            }
            case 23: {
                this.A(player, uUID, string);
                break;
            }
            case 24: {
                this.D.D(player, string);
                break;
            }
            case 30: {
                this.E(player, uUID, string);
                break;
            }
            case 31: {
                this.B(player, uUID, string);
                break;
            }
            case 32: {
                this.D.C(player, string);
                break;
            }
            case 48: {
                this.L.A(player, (Integer)this.I.getOrDefault((Object)uUID, (Object)0));
                break;
            }
            case 50: {
                this.F(player, string);
            }
        }
    }

    private void B(Player player, UUID uUID, ItemStack itemStack) {
        player.closeInventory();
        this.U.put((Object)uUID, (Object)true);
        String string = itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName() ? itemStack.getItemMeta().getDisplayName() : "";
        this.B(player, "&8\u00bb &7Wpisz &#FFD300now\u0105 &7nazw\u0119 &fprzedmiotu &8(&7Wpisz &#FF0000anuluj&7, aby anulowa\u0107&8)&7!", "&7Aktualna: " + string);
    }

    private void A(Player player, UUID uUID, ItemStack itemStack) {
        player.closeInventory();
        this.K.put((Object)uUID, (Object)true);
        int n2 = itemStack.hasItemMeta() && itemStack.getItemMeta().hasCustomModelData() ? itemStack.getItemMeta().getCustomModelData() : 0;
        this.B(player, "&8\u00bb &7Wpisz &#FFD300nowy &7Custom Model Data &8(&7Wpisz &#FF0000anuluj&7, aby anulowa\u0107&8)&7!", "&7Aktualnie: &f" + n2);
    }

    private void D(Player player, UUID uUID, String string) {
        player.closeInventory();
        this.M.put((Object)uUID, (Object)true);
        Material material = this.S.A(string);
        this.B(player, "&8\u00bb &7Wpisz &#FFD300nowy &7rodzaj przedmiotu &8(&7np. &#FFD300DIAMOND_SWORD&7)&8!", "&7Aktualnie: &f" + material.name());
    }

    private void C(Player player, UUID uUID, String string) {
        player.closeInventory();
        this.N.put((Object)uUID, (Object)true);
        int n2 = this.S.J(string);
        this.B(player, "&8\u00bb &7Wpisz &#FFD300nowy cooldown &7w &fsekundach &8(&7Wpisz &#FF0000anuluj&7, aby anulowa\u0107&8)&7!", "&7Aktualnie: &f" + n2 + "s");
    }

    private void A(Player player, UUID uUID, String string) {
        player.closeInventory();
        this.O.put((Object)uUID, (Object)true);
        String string2 = this.S.D(string);
        this.B(player, "&8\u00bb &7Wpisz &#FFD300nowy kolor &7cooldownu w &fformacie hex &8(&7np. &#FFD300#FF5555&8)&7!", "&7Aktualnie: " + string2);
    }

    private void E(Player player, UUID uUID, String string) {
        player.closeInventory();
        this.J.put((Object)uUID, (Object)true);
        int n2 = this.S.C(string);
        this.B(player, "&8\u00bb &7Wpisz &#FFD300now\u0105 szans\u0119 &7na zadzia\u0142anie &8(&f0-100&8)&7!", "&7Aktualnie: &f" + n2 + "%");
    }

    private void A(Player player, String string, ItemStack itemStack) {
        boolean bl = itemStack.hasItemMeta() && itemStack.getItemMeta().isUnbreakable();
        this.S.A(string, !bl);
        this.A(player, "&8\u00bb &7Unbreakable zosta\u0142 " + (!bl ? "&#1DFF1Aw\u0142\u0105czony" : "&#FF0000wy\u0142\u0105czony") + "&7!", "&fUnbreakable &7" + (!bl ? "&#1DFF1Aw\u0142\u0105czony" : "&#FF0000wy\u0142\u0105czony"), "ENTITY_PLAYER_LEVELUP");
        this.D.E(player, string);
    }

    private void F(Player player, String string) {
        boolean bl = this.S.B(string);
        this.S.B(string, !bl);
        this.A(player, "&8\u00bb &7Przedmiot zosta\u0142 " + (bl ? "&#1DFF1Aw\u0142\u0105czony" : "&#FF0000wy\u0142\u0105czony") + "&7!", "&fPrzedmiot &7" + (bl ? "&#1DFF1Aw\u0142\u0105czony" : "&#FF0000wy\u0142\u0105czony"), "ENTITY_PLAYER_LEVELUP");
        this.D.E(player, string);
    }

    private void B(InventoryClickEvent inventoryClickEvent, Player player, UUID uUID) {
        inventoryClickEvent.setCancelled(true);
        String string = (String)this.R.get((Object)uUID);
        if (string == null) {
            return;
        }
        int n2 = inventoryClickEvent.getRawSlot();
        if (n2 == 39) {
            this.D.E(player, string);
            return;
        }
        if (n2 == 41 && inventoryClickEvent.getCurrentItem() != null && inventoryClickEvent.getCurrentItem().getType() == Material.LIME_DYE) {
            this.C.put((Object)uUID, (Object)true);
            player.closeInventory();
            this.B(player, "&8\u00bb &7Wpisz &#FFD300now\u0105 &7linijk\u0119 lore &8(&7Wpisz &#FF0000anuluj&7, aby anulowa\u0107&8)&7!", "&7Wpisz &#FFD300now\u0105 &7linijk\u0119 lore na &fchacie&7!");
            return;
        }
        this.A(inventoryClickEvent, player, string);
    }

    private void A(InventoryClickEvent inventoryClickEvent, Player player, String string) {
        int[] nArray = new int[]{10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25};
        int n2 = inventoryClickEvent.getRawSlot();
        for (int i2 = 0; i2 < nArray.length; ++i2) {
            ArrayList arrayList;
            if (n2 != nArray[i2] || inventoryClickEvent.getCurrentItem() == null || inventoryClickEvent.getCurrentItem().getType() != Material.PAPER) continue;
            ItemStack itemStack = this.S.G(string);
            ArrayList arrayList2 = arrayList = itemStack != null && itemStack.hasItemMeta() && itemStack.getItemMeta().hasLore() ? new ArrayList((Collection)itemStack.getItemMeta().getLore()) : new ArrayList();
            if (i2 >= arrayList.size()) {
                return;
            }
            if (inventoryClickEvent.getClick() == ClickType.SHIFT_RIGHT) {
                arrayList.remove(i2);
                this.S.C(string, (List<String>)arrayList);
                this.A(player, "&8\u00bb &7Linijka lore zosta\u0142a &#FF0000usuni\u0119ta&7!", "&fLinijka &7zosta\u0142a &#FF0000usuni\u0119ta&7!", "ENTITY_ENDERMAN_TELEPORT");
            } else if (inventoryClickEvent.getClick() == ClickType.LEFT && i2 > 0) {
                Collections.swap((List)arrayList, (int)i2, (int)(i2 - 1));
                this.S.C(string, (List<String>)arrayList);
                this.K(player, "ENTITY_EXPERIENCE_ORB_PICKUP");
            } else if (inventoryClickEvent.getClick() == ClickType.RIGHT && i2 < arrayList.size() - 1) {
                Collections.swap((List)arrayList, (int)i2, (int)(i2 + 1));
                this.S.C(string, (List<String>)arrayList);
                this.K(player, "ENTITY_EXPERIENCE_ORB_PICKUP");
            }
            this.D.F(player, string);
            break;
        }
    }

    private void A(InventoryClickEvent inventoryClickEvent, Player player, UUID uUID) {
        inventoryClickEvent.setCancelled(true);
        String string = (String)this.R.get((Object)uUID);
        if (string == null) {
            return;
        }
        int n2 = inventoryClickEvent.getRawSlot();
        if (n2 == 39) {
            this.D.E(player, string);
            return;
        }
        if (n2 == 41 && inventoryClickEvent.getCurrentItem() != null && inventoryClickEvent.getCurrentItem().getType() == Material.LIME_DYE) {
            this.H.put((Object)uUID, (Object)true);
            player.closeInventory();
            this.B(player, "&8\u00bb &7Wpisz &#FFD300enchantment &7i &fpoziomu &8(&7np. &#FFD300SHARPNESS 5&7)&8:", "&7Wpisz enchantment na &fchacie&7!");
            return;
        }
        this.B(inventoryClickEvent, player, string);
    }

    private void B(InventoryClickEvent inventoryClickEvent, Player player, String string) {
        int[] nArray = new int[]{10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25};
        int n2 = inventoryClickEvent.getRawSlot();
        for (int i2 = 0; i2 < nArray.length; ++i2) {
            LinkedHashMap linkedHashMap;
            if (n2 != nArray[i2] || inventoryClickEvent.getCurrentItem() == null || inventoryClickEvent.getCurrentItem().getType() != Material.ENCHANTED_BOOK) continue;
            if (inventoryClickEvent.getClick() != ClickType.SHIFT_RIGHT) break;
            ItemStack itemStack = this.S.G(string);
            LinkedHashMap linkedHashMap2 = linkedHashMap = itemStack != null && itemStack.hasItemMeta() && itemStack.getItemMeta().hasEnchants() ? new LinkedHashMap(itemStack.getItemMeta().getEnchants()) : new LinkedHashMap();
            if (i2 >= linkedHashMap.size()) break;
            Enchantment enchantment = (Enchantment)linkedHashMap.keySet().toArray()[i2];
            linkedHashMap.remove((Object)enchantment);
            this.S.C(string, (Map<Enchantment, Integer>)linkedHashMap);
            this.A(player, "&8\u00bb &7Enchantment zosta\u0142 &#FF0000usuni\u0119ty&7!", "&fEnchantment &7zosta\u0142 &#FF0000usuni\u0119ty&7!", "ENTITY_ENDERMAN_TELEPORT");
            this.D.G(player, string);
            break;
        }
    }

    private void G(InventoryClickEvent inventoryClickEvent, Player player, UUID uUID) {
        inventoryClickEvent.setCancelled(true);
        String string = (String)this.R.get((Object)uUID);
        if (string == null) {
            return;
        }
        int n2 = inventoryClickEvent.getRawSlot();
        if (n2 == 39) {
            this.D.E(player, string);
            return;
        }
        if (n2 == 41 && inventoryClickEvent.getCurrentItem() != null && inventoryClickEvent.getCurrentItem().getType() == Material.LIME_DYE) {
            this.V.put((Object)uUID, (Object)true);
            player.closeInventory();
            this.B(player, "&8\u00bb &7Wpisz &#FFD300flag\u0119 &8(&7np. &#FFD300HIDE_ATTRIBUTES&7)&8:", "&7Wpisz flag\u0119 na &fchacie&7!");
            return;
        }
        this.C(inventoryClickEvent, player, string);
    }

    private void C(InventoryClickEvent inventoryClickEvent, Player player, String string) {
        int[] nArray = new int[]{10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25};
        int n2 = inventoryClickEvent.getRawSlot();
        for (int i2 = 0; i2 < nArray.length; ++i2) {
            LinkedHashSet linkedHashSet;
            if (n2 != nArray[i2] || inventoryClickEvent.getCurrentItem() == null || inventoryClickEvent.getCurrentItem().getType() != Material.PAPER) continue;
            if (inventoryClickEvent.getClick() != ClickType.SHIFT_RIGHT) break;
            ItemStack itemStack = this.S.G(string);
            LinkedHashSet linkedHashSet2 = linkedHashSet = itemStack != null && itemStack.hasItemMeta() ? new LinkedHashSet((Collection)itemStack.getItemMeta().getItemFlags()) : new LinkedHashSet();
            if (i2 >= linkedHashSet.size()) break;
            ItemFlag itemFlag = (ItemFlag)linkedHashSet.toArray()[i2];
            linkedHashSet.remove((Object)itemFlag);
            this.S.A(string, (Set<ItemFlag>)linkedHashSet);
            this.A(player, "&8\u00bb &7Flaga zosta\u0142a &#FF0000usuni\u0119ta&7!", "&fFlaga &7zosta\u0142a &#FF0000usuni\u0119ta&7!", "ENTITY_ENDERMAN_TELEPORT");
            this.D.A(player, string);
            break;
        }
    }

    /*
     * Exception decompiling
     */
    private void B(Player var1_1, UUID var2_2, String var3_3) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * ob.f0$e
         *     at ob.f0.e(SourceFile:35)
         *     at ob.f0.a(SourceFile:1)
         *     at ob.f0$d.a(SourceFile:68)
         *     at qb.n.i(SourceFile:13)
         *     at qb.e.i(SourceFile:9)
         *     at qb.l.i(SourceFile:14)
         *     at qb.n.i(SourceFile:3)
         *     at ob.f0.g(SourceFile:649)
         *     at ob.f0.d(SourceFile:37)
         *     at ib.f.d(SourceFile:235)
         *     at ib.f.e(SourceFile:7)
         *     at ib.f.c(SourceFile:95)
         *     at rc.f.n(SourceFile:11)
         *     at pc.i.m(SourceFile:5)
         *     at pc.d.K(SourceFile:92)
         *     at pc.d.g0(SourceFile:1)
         *     at fb.b.d(SourceFile:191)
         *     at fb.b.c(SourceFile:145)
         *     at fb.a.a(SourceFile:108)
         *     at com.thesourceofcode.jadec.decompilers.JavaExtractionWorker.decompileWithCFR(SourceFile:76)
         *     at com.thesourceofcode.jadec.decompilers.JavaExtractionWorker.doWork(SourceFile:110)
         *     at com.thesourceofcode.jadec.decompilers.BaseDecompiler.withAttempt(SourceFile:3)
         *     at com.thesourceofcode.jadec.workers.DecompilerWorker.d(SourceFile:53)
         *     at com.thesourceofcode.jadec.workers.DecompilerWorker.b(SourceFile:1)
         *     at e7.a.run(SourceFile:1)
         *     at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1154)
         *     at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:652)
         *     at java.lang.Thread.run(Thread.java:1563)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    private void F(InventoryClickEvent inventoryClickEvent, Player player, UUID uUID) {
        inventoryClickEvent.setCancelled(true);
        String string = (String)this.R.get((Object)uUID);
        if (string == null) {
            return;
        }
        int n2 = inventoryClickEvent.getRawSlot();
        if (n2 == 39) {
            this.D.E(player, string);
            return;
        }
        String string2 = null;
        String string3 = null;
        switch (n2) {
            case 10: {
                string2 = "attacker_title";
                string3 = "Title (u\u017cywaj\u0105cy)";
                break;
            }
            case 11: {
                string2 = "attacker_subtitle";
                string3 = "Subtitle (u\u017cywaj\u0105cy)";
                break;
            }
            case 12: {
                string2 = "attacker_chat";
                string3 = "Chat (u\u017cywaj\u0105cy)";
                break;
            }
            case 14: {
                string2 = "victim_title";
                string3 = "Title (przeciwnik)";
                break;
            }
            case 15: {
                string2 = "victim_subtitle";
                string3 = "Subtitle (przeciwnik)";
                break;
            }
            case 16: {
                string2 = "victim_chat";
                string3 = "Chat (przeciwnik)";
            }
        }
        if (string2 != null) {
            if (inventoryClickEvent.getClick() == ClickType.SHIFT_RIGHT) {
                String[] stringArray = string2.split("_");
                String string4 = stringArray[0];
                String string5 = stringArray[1];
                if (string4.equals((Object)"attacker")) {
                    this.S.B(string, string5, "");
                } else {
                    this.S.A(string, string5, "");
                }
                this.A(player, "&8\u00bb &7Wiadomo\u015b\u0107 &f" + string3 + " &7zosta\u0142a &#FF0000usuni\u0119ta&7!", "&fWiadomo\u015b\u0107 &7zosta\u0142a &#FF0000usuni\u0119ta&7!", "ENTITY_ENDERMAN_TELEPORT");
                this.D.C(player, string);
            } else if (inventoryClickEvent.getClick() == ClickType.LEFT) {
                this.F.put((Object)uUID, (Object)string2);
                this.Q.put((Object)uUID, (Object)true);
                player.closeInventory();
                this.B(player, "&8\u00bb &7Wpisz &#FFD300now\u0105 wiadomo\u015b\u0107 &7dla &f" + string3 + " &8(&7Wpisz &#FF0000anuluj&7, aby anulowa\u0107&8)&7:", "&7Wpisz wiadomo\u015b\u0107 na &fchacie&7!");
            }
        }
    }

    private void B(AsyncPlayerChatEvent asyncPlayerChatEvent, Player player, UUID uUID) {
        asyncPlayerChatEvent.setCancelled(true);
        String string = asyncPlayerChatEvent.getMessage();
        String string2 = (String)this.R.get((Object)uUID);
        String string3 = (String)this.F.get((Object)uUID);
        if (string2 == null || string3 == null) {
            this.Q.remove((Object)uUID);
            this.F.remove((Object)uUID);
            return;
        }
        if (string.equalsIgnoreCase("anuluj")) {
            this.Q.remove((Object)uUID);
            this.F.remove((Object)uUID);
            this.E.getServer().getScheduler().runTask((Plugin)this.E, () -> {
                this.A(player, "&8\u00bb &7Edycja wiadomo\u015bci zosta\u0142a &#FF0000anulowana&7!", "&fEdycja &7zosta\u0142a &#FF0000anulowana&7!", "ENTITY_ENDERMAN_TELEPORT");
                this.D.C(player, string2);
            });
            return;
        }
        String[] stringArray = string3.split("_");
        String string4 = stringArray[0];
        String string5 = stringArray[1];
        if (string4.equals((Object)"attacker")) {
            this.S.B(string2, string5, string);
        } else {
            this.S.A(string2, string5, string);
        }
        this.Q.remove((Object)uUID);
        this.F.remove((Object)uUID);
        this.E.getServer().getScheduler().runTask((Plugin)this.E, () -> {
            this.A(player, "&8\u00bb &7Wiadomo\u015b\u0107 zosta\u0142a &#1DFF1Azaktualizowana&7!", "&fWiadomo\u015b\u0107 &7zosta\u0142a &#1DFF1Azaktualizowana&7!", "ENTITY_PLAYER_LEVELUP");
            this.D.C(player, string2);
        });
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent asyncPlayerChatEvent) {
        Player player = asyncPlayerChatEvent.getPlayer();
        UUID uUID = player.getUniqueId();
        if (((Boolean)this.P.getOrDefault((Object)uUID, (Object)false)).booleanValue()) {
            this.A(asyncPlayerChatEvent, player, uUID);
        } else if (((Boolean)this.U.getOrDefault((Object)uUID, (Object)false)).booleanValue()) {
            this.I(asyncPlayerChatEvent, player, uUID);
        } else if (((Boolean)this.C.getOrDefault((Object)uUID, (Object)false)).booleanValue()) {
            this.H(asyncPlayerChatEvent, player, uUID);
        } else if (((Boolean)this.K.getOrDefault((Object)uUID, (Object)false)).booleanValue()) {
            this.G(asyncPlayerChatEvent, player, uUID);
        } else if (((Boolean)this.H.getOrDefault((Object)uUID, (Object)false)).booleanValue()) {
            this.F(asyncPlayerChatEvent, player, uUID);
        } else if (((Boolean)this.V.getOrDefault((Object)uUID, (Object)false)).booleanValue()) {
            this.J(asyncPlayerChatEvent, player, uUID);
        } else if (((Boolean)this.N.getOrDefault((Object)uUID, (Object)false)).booleanValue()) {
            this.E(asyncPlayerChatEvent, player, uUID);
        } else if (((Boolean)this.O.getOrDefault((Object)uUID, (Object)false)).booleanValue()) {
            this.D(asyncPlayerChatEvent, player, uUID);
        } else if (((Boolean)this.M.getOrDefault((Object)uUID, (Object)false)).booleanValue()) {
            this.C(asyncPlayerChatEvent, player, uUID);
        } else if (((Boolean)this.J.getOrDefault((Object)uUID, (Object)false)).booleanValue()) {
            this.K(asyncPlayerChatEvent, player, uUID);
        } else if (((Boolean)this.T.getOrDefault((Object)uUID, (Object)false)).booleanValue()) {
            this.L(asyncPlayerChatEvent, player, uUID);
        } else if (((Boolean)this.Q.getOrDefault((Object)uUID, (Object)false)).booleanValue()) {
            this.B(asyncPlayerChatEvent, player, uUID);
        }
    }

    private void A(AsyncPlayerChatEvent asyncPlayerChatEvent, Player player, UUID uUID) {
        asyncPlayerChatEvent.setCancelled(true);
        String string = asyncPlayerChatEvent.getMessage().trim();
        if (string.equalsIgnoreCase("anuluj")) {
            this.A(player, uUID, this.P, "&fTworzenie przedmiotu &7zosta\u0142o &#FF0000anulowane&7!", () -> this.L.A(player));
            return;
        }
        if (!string.matches("^[a-zA-Z0-9_]+$")) {
            this.A(player, "&8\u00bb &7ID mo\u017ce zawiera\u0107 tylko &flitery&7, &fcyfry &7i &fpodkre\u015blniki&7!", "&fNieprawid\u0142owe &7ID!");
            return;
        }
        String string2 = string.toLowerCase();
        if (this.S.I(string2)) {
            this.A(player, "&8\u00bb &7Przedmiot o ID &f" + string2 + " &7ju\u017c &#FF0000istnieje&7!", "&fPrzedmiot &7ju\u017c &#FF0000istnieje&7!");
            return;
        }
        if (this.S.L(string2)) {
            this.E.getServer().getScheduler().runTask((Plugin)this.E, () -> {
                this.A(player, "&8\u00bb &7Przedmiot &f" + string2 + " &7zosta\u0142 &#27FF00stworzony&7!", "&fPrzedmiot &7zosta\u0142 &#27FF00stworzony&7!", "ENTITY_PLAYER_LEVELUP");
                this.R.put((Object)uUID, (Object)string2);
                this.D.E(player, string2);
            });
        }
        this.P.remove((Object)uUID);
    }

    private void I(AsyncPlayerChatEvent asyncPlayerChatEvent, Player player, UUID uUID) {
        asyncPlayerChatEvent.setCancelled(true);
        String string = asyncPlayerChatEvent.getMessage().trim();
        String string2 = (String)this.R.get((Object)uUID);
        if (string2 == null) {
            this.U.remove((Object)uUID);
            return;
        }
        if (string.equalsIgnoreCase("anuluj")) {
            this.A(player, uUID, this.U, "&fZmiana nazwy &7zosta\u0142a &#FF0000anulowana&7!", () -> this.D.E(player, string2));
            return;
        }
        this.S.A(string2, string);
        this.E.getServer().getScheduler().runTask((Plugin)this.E, () -> {
            this.A(player, "&8\u00bb &7Nazwa zosta\u0142a &#27FF00zmieniona&7!", "&fNazwa &7zosta\u0142a &#27FF00zmieniona&7!", "ENTITY_PLAYER_LEVELUP");
            this.D.E(player, string2);
        });
        this.U.remove((Object)uUID);
    }

    private void H(AsyncPlayerChatEvent asyncPlayerChatEvent, Player player, UUID uUID) {
        ArrayList arrayList;
        asyncPlayerChatEvent.setCancelled(true);
        String string = asyncPlayerChatEvent.getMessage().trim();
        String string2 = (String)this.R.get((Object)uUID);
        if (string2 == null) {
            this.C.remove((Object)uUID);
            return;
        }
        if (string.equalsIgnoreCase("anuluj")) {
            this.A(player, uUID, this.C, "&fDodawanie lore &7zosta\u0142o &#FF0000anulowane&7!", () -> this.D.F(player, string2));
            return;
        }
        ItemStack itemStack = this.S.G(string2);
        ArrayList arrayList2 = arrayList = itemStack != null && itemStack.hasItemMeta() && itemStack.getItemMeta().hasLore() ? new ArrayList((Collection)itemStack.getItemMeta().getLore()) : new ArrayList();
        if (arrayList.size() >= 14) {
            this.E.getServer().getScheduler().runTask((Plugin)this.E, () -> {
                this.A(player, "&8\u00bb &7Osi\u0105gni\u0119to maksymaln\u0105 liczb\u0119 &#FF0000linijek&7!", "&fLore &7osi\u0105gn\u0119\u0142o &#FF0000limit&7!");
                this.D.F(player, string2);
            });
            this.C.remove((Object)uUID);
            return;
        }
        arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C(string));
        this.S.C(string2, (List<String>)arrayList);
        this.E.getServer().getScheduler().runTask((Plugin)this.E, () -> {
            this.A(player, "&8\u00bb &7Dodano now\u0105 &flinijk\u0119 &7lore &#27FF00pomy\u015blnie&7!", "&fLinijka lore &7zosta\u0142a &#27FF00dodana&7!", "ENTITY_PLAYER_LEVELUP");
            this.D.F(player, string2);
        });
        this.C.remove((Object)uUID);
    }

    private void G(AsyncPlayerChatEvent asyncPlayerChatEvent, Player player, UUID uUID) {
        asyncPlayerChatEvent.setCancelled(true);
        String string = asyncPlayerChatEvent.getMessage().trim();
        String string2 = (String)this.R.get((Object)uUID);
        if (string2 == null) {
            this.K.remove((Object)uUID);
            return;
        }
        if (string.equalsIgnoreCase("anuluj")) {
            this.A(player, uUID, this.K, "&fZmiana CMD &7zosta\u0142a &#FF0000anulowana&7!", () -> this.D.E(player, string2));
            return;
        }
        try {
            int n2 = Integer.parseInt((String)string);
            if (n2 < 0) {
                this.A(player, "&8\u00bb &7CMD &7nie mo\u017ce by\u0107 &#FF0000ujemny&7!", "&fCMD &7musi by\u0107 &#FF0000dodatni&7!");
                return;
            }
            this.S.C(string2, n2);
            this.E.getServer().getScheduler().runTask((Plugin)this.E, () -> {
                this.A(player, "&8\u00bb &7CMD zosta\u0142o &#27FF00zmienione&7 na &f" + n2 + "&7!", "&fCMD &7zmienione na &f" + n2, "ENTITY_PLAYER_LEVELUP");
                this.D.E(player, string2);
            });
            this.K.remove((Object)uUID);
        }
        catch (NumberFormatException numberFormatException) {
            this.A(player, "&8\u00bb &7CMD musi by\u0107 &#FF0000liczb\u0105&7!", "&fCMD &7musi by\u0107 &#FF0000liczb\u0105&7!");
        }
    }

    private void F(AsyncPlayerChatEvent asyncPlayerChatEvent, Player player, UUID uUID) {
        asyncPlayerChatEvent.setCancelled(true);
        String string = asyncPlayerChatEvent.getMessage().trim();
        String string2 = (String)this.R.get((Object)uUID);
        if (string2 == null) {
            this.H.remove((Object)uUID);
            return;
        }
        if (string.equalsIgnoreCase("anuluj")) {
            this.A(player, uUID, this.H, "&fDodawanie enchantmentu &7zosta\u0142o &#FF0000anulowane&7!", () -> this.D.G(player, string2));
            return;
        }
        String[] stringArray = string.split(" ");
        if (stringArray.length < 2) {
            this.A(player, "&8\u00bb &7Format: &#FFD300ENCHANTMENT_NAME LEVEL", "&fFormat &7&#FF0000nieprawid\u0142owy&7!");
            return;
        }
        try {
            Enchantment enchantment = this.A(stringArray[0].toUpperCase());
            int n2 = Integer.parseInt((String)stringArray[1]);
            if (enchantment == null) {
                this.A(player, "&8\u00bb &7Enchantment &#FF0000nie istnieje&7!", "&fEnchantment &7nie &#FF0000istnieje&7!");
                return;
            }
            ItemStack itemStack = this.S.G(string2);
            LinkedHashMap linkedHashMap = itemStack != null && itemStack.hasItemMeta() && itemStack.getItemMeta().hasEnchants() ? new LinkedHashMap(itemStack.getItemMeta().getEnchants()) : new LinkedHashMap();
            linkedHashMap.put((Object)enchantment, (Object)n2);
            this.S.C(string2, (Map<Enchantment, Integer>)linkedHashMap);
            this.E.getServer().getScheduler().runTask((Plugin)this.E, () -> {
                this.A(player, "&8\u00bb &7Enchantment &f" + enchantment.getName() + " " + n2 + " &7zosta\u0142 &#27FF00dodany&7!", "&fEnchantment &7zosta\u0142 &#27FF00dodany&7!", "ENTITY_PLAYER_LEVELUP");
                this.D.G(player, string2);
            });
            this.H.remove((Object)uUID);
        }
        catch (NumberFormatException numberFormatException) {
            this.A(player, "&8\u00bb &7Poziom musi by\u0107 &#FF0000liczb\u0105&7!", "&fPoziom &7musi by\u0107 &#FF0000liczb\u0105&7!");
        }
    }

    private void J(AsyncPlayerChatEvent asyncPlayerChatEvent, Player player, UUID uUID) {
        asyncPlayerChatEvent.setCancelled(true);
        String string = asyncPlayerChatEvent.getMessage().trim();
        String string2 = (String)this.R.get((Object)uUID);
        if (string2 == null) {
            this.V.remove((Object)uUID);
            return;
        }
        if (string.equalsIgnoreCase("anuluj")) {
            this.A(player, uUID, this.V, "&fDodawanie flagi &7zosta\u0142o &#FF0000anulowane&7!", () -> this.D.A(player, string2));
            return;
        }
        try {
            ItemFlag itemFlag = ItemFlag.valueOf((String)string.toUpperCase());
            ItemStack itemStack = this.S.G(string2);
            LinkedHashSet linkedHashSet = itemStack != null && itemStack.hasItemMeta() ? new LinkedHashSet((Collection)itemStack.getItemMeta().getItemFlags()) : new LinkedHashSet();
            linkedHashSet.add((Object)itemFlag);
            this.S.A(string2, (Set<ItemFlag>)linkedHashSet);
            this.E.getServer().getScheduler().runTask((Plugin)this.E, () -> {
                this.A(player, "&8\u00bb &7Flaga &f" + itemFlag.name() + " &7zosta\u0142a &#27FF00dodana&7!", "&fFlaga &7zosta\u0142a &#27FF00dodana&7!", "ENTITY_PLAYER_LEVELUP");
                this.D.A(player, string2);
            });
            this.V.remove((Object)uUID);
        }
        catch (IllegalArgumentException illegalArgumentException) {
            this.A(player, "&8\u00bb &7Flaga &#FF0000nie istnieje&7!", "&fFlaga &7nie &#FF0000istnieje&7!");
        }
    }

    private void E(AsyncPlayerChatEvent asyncPlayerChatEvent, Player player, UUID uUID) {
        asyncPlayerChatEvent.setCancelled(true);
        String string = asyncPlayerChatEvent.getMessage().trim();
        String string2 = (String)this.R.get((Object)uUID);
        if (string2 == null) {
            this.N.remove((Object)uUID);
            return;
        }
        if (string.equalsIgnoreCase("anuluj")) {
            this.A(player, uUID, this.N, "&fZmiana cooldownu &7zosta\u0142a &#FF0000anulowana&7!", () -> this.D.E(player, string2));
            return;
        }
        try {
            int n2 = Integer.parseInt((String)string);
            if (n2 < 0) {
                this.A(player, "&8\u00bb &7Cooldown &7nie mo\u017ce by\u0107 &#FF0000ujemny&7!", "&fCooldown &7musi by\u0107 &#FF0000dodatni&7!");
                return;
            }
            this.S.B(string2, n2);
            this.E.getServer().getScheduler().runTask((Plugin)this.E, () -> {
                this.A(player, "&8\u00bb &7Cooldown zosta\u0142 &#27FF00zmieniony&7 na &f" + n2 + "s&7!", "&fCooldown &7zmieniony na &f" + n2 + "s", "ENTITY_PLAYER_LEVELUP");
                this.D.E(player, string2);
            });
            this.N.remove((Object)uUID);
        }
        catch (NumberFormatException numberFormatException) {
            this.A(player, "&8\u00bb &7Cooldown musi by\u0107 &#FF0000liczb\u0105&7!", "&fCooldown &7musi by\u0107 &#FF0000liczb\u0105&7!");
        }
    }

    private void D(AsyncPlayerChatEvent asyncPlayerChatEvent, Player player, UUID uUID) {
        asyncPlayerChatEvent.setCancelled(true);
        String string = asyncPlayerChatEvent.getMessage().trim();
        String string2 = (String)this.R.get((Object)uUID);
        if (string2 == null) {
            this.O.remove((Object)uUID);
            return;
        }
        if (string.equalsIgnoreCase("anuluj")) {
            this.A(player, uUID, this.O, "&fZmiana koloru &7zosta\u0142a &#FF0000anulowana&7!", () -> this.D.E(player, string2));
            return;
        }
        String string3 = string;
        if (!string3.startsWith("&#") && !string3.startsWith("#")) {
            string3 = "&#" + string3;
        } else if (string3.startsWith("#") && !string3.startsWith("&#")) {
            string3 = "&" + string3;
        }
        String string4 = string3.replace((CharSequence)"&#", (CharSequence)"").replace((CharSequence)"#", (CharSequence)"");
        if (!string4.matches("^[0-9A-Fa-f]{6}$")) {
            this.A(player, "&8\u00bb &7Nieprawid\u0142owy format koloru! U\u017cyj formatu &#FF0000#RRGGBB&7!", "&fFormat &7&#FF0000nieprawid\u0142owy&7!");
            return;
        }
        String string5 = string3;
        this.S.D(string2, string5);
        this.E.getServer().getScheduler().runTask((Plugin)this.E, () -> {
            this.A(player, "&8\u00bb &7Kolor cooldownu zosta\u0142 &#27FF00zmieniony&7!", "&fKolor &7zosta\u0142 &#27FF00zmieniony&7!", "ENTITY_PLAYER_LEVELUP");
            this.D.E(player, string2);
        });
        this.O.remove((Object)uUID);
    }

    private void C(AsyncPlayerChatEvent asyncPlayerChatEvent, Player player, UUID uUID) {
        asyncPlayerChatEvent.setCancelled(true);
        String string = asyncPlayerChatEvent.getMessage().trim();
        String string2 = (String)this.R.get((Object)uUID);
        if (string2 == null) {
            this.M.remove((Object)uUID);
            return;
        }
        if (string.equalsIgnoreCase("anuluj")) {
            this.A(player, uUID, this.M, "&fZmiana materia\u0142u &7zosta\u0142a &#FF0000anulowana&7!", () -> this.D.E(player, string2));
            return;
        }
        Material material = Material.matchMaterial((String)string.toUpperCase());
        if (material == null) {
            this.A(player, "&8\u00bb &7Materia\u0142 &#FF0000nie istnieje&7!", "&fMateria\u0142 &7nie &#FF0000istnieje&7!");
            return;
        }
        this.S.A(string2, material);
        this.E.getServer().getScheduler().runTask((Plugin)this.E, () -> {
            this.A(player, "&8\u00bb &7Materia\u0142 zosta\u0142 &#27FF00zmieniony&7 na &f" + material.name() + "&7!", "&fMateria\u0142 &7zosta\u0142 &#27FF00zmieniony&7!", "ENTITY_PLAYER_LEVELUP");
            this.D.E(player, string2);
        });
        this.M.remove((Object)uUID);
    }

    private void K(AsyncPlayerChatEvent asyncPlayerChatEvent, Player player, UUID uUID) {
        int n2;
        asyncPlayerChatEvent.setCancelled(true);
        String string = asyncPlayerChatEvent.getMessage().trim();
        String string2 = (String)this.R.get((Object)uUID);
        if (string2 == null) {
            this.J.remove((Object)uUID);
            return;
        }
        if (string.equalsIgnoreCase("anuluj")) {
            this.A(player, uUID, this.J, "&fZmiana szansy &7zosta\u0142a &#FF0000anulowana&7!", () -> this.D.E(player, string2));
            return;
        }
        try {
            n2 = Integer.parseInt((String)string);
        }
        catch (NumberFormatException numberFormatException) {
            this.A(player, "&8\u00bb &7Wpisz &fliczb\u0119 &7od &f0 &7do &f100&7!", "&fNieprawid\u0142owa &7warto\u015b\u0107!");
            return;
        }
        if (n2 < 0 || n2 > 100) {
            this.A(player, "&8\u00bb &7Szansa musi by\u0107 w zakresie &f0-100&7!", "&fNieprawid\u0142owy &7zakres!");
            return;
        }
        this.S.A(string2, n2);
        this.E.getServer().getScheduler().runTask((Plugin)this.E, () -> {
            this.A(player, "&8\u00bb &7Szansa zosta\u0142a &#27FF00zmieniona&7 na &f" + n2 + "%&7!", "&fSzansa &7zosta\u0142a &#27FF00zmieniona&7!", "ENTITY_PLAYER_LEVELUP");
            this.D.E(player, string2);
        });
        this.J.remove((Object)uUID);
    }

    private void L(AsyncPlayerChatEvent asyncPlayerChatEvent, Player player, UUID uUID) {
        int n2;
        Object object;
        asyncPlayerChatEvent.setCancelled(true);
        String string = asyncPlayerChatEvent.getMessage().trim();
        String string2 = (String)this.R.get((Object)uUID);
        Integer n3 = (Integer)this.B.get((Object)uUID);
        Boolean bl = (Boolean)this.G.get((Object)uUID);
        String string3 = (String)this.A.get((Object)uUID);
        if (string2 == null || n3 == null || bl == null || string3 == null) {
            this.T.remove((Object)uUID);
            return;
        }
        if (string.equalsIgnoreCase("anuluj")) {
            this.T.remove((Object)uUID);
            this.A.remove((Object)uUID);
            this.E.getServer().getScheduler().runTask((Plugin)this.E, () -> {
                this.A(player, "&8\u00bb &7Edycja parametru zosta\u0142a &#FF0000anulowana&7!", "&fEdycja &7zosta\u0142a &#FF0000anulowana&7!", "ENTITY_ENDERMAN_TELEPORT");
                this.D.A(player, string2, n3, bl);
            });
            return;
        }
        try {
            object = string.equalsIgnoreCase("true") || string.equalsIgnoreCase("false") ? Boolean.valueOf((boolean)Boolean.parseBoolean((String)string)) : (string.contains((CharSequence)".") ? (Object)Double.parseDouble((String)string) : (Object)Integer.parseInt((String)string));
        }
        catch (NumberFormatException numberFormatException) {
            this.A(player, "&8\u00bb &7Nieprawid\u0142owa warto\u015b\u0107! Wpisz &fliczb\u0119&7.", "&fNieprawid\u0142owa &7warto\u015b\u0107!");
            return;
        }
        if (string3.equals((Object)"chance") && object instanceof Integer && ((n2 = ((Integer)object).intValue()) < 0 || n2 > 100)) {
            this.A(player, "&8\u00bb &7Szansa musi by\u0107 w zakresie &f0-100&7!", "&fNieprawid\u0142owy &7zakres!");
            return;
        }
        if (bl.booleanValue()) {
            this.S.A(string2, n3, string3, object);
        } else {
            this.S.B(string2, n3, string3, object);
        }
        this.T.remove((Object)uUID);
        this.A.remove((Object)uUID);
        String string4 = this.D(string3);
        this.E.getServer().getScheduler().runTask((Plugin)this.E, () -> {
            this.A(player, "&8\u00bb &7Parametr &f" + string4 + " &7zosta\u0142 &#1DFF1Azmieniony&7 na &f" + String.valueOf((Object)object) + "&7!", "&fParametr &7zosta\u0142 &#1DFF1Azmieniony&7!", "ENTITY_PLAYER_LEVELUP");
            this.D.A(player, string2, n3, bl);
        });
    }

    private void H(InventoryClickEvent inventoryClickEvent, Player player, UUID uUID) {
        int[] nArray;
        inventoryClickEvent.setCancelled(true);
        String string = (String)this.R.get((Object)uUID);
        if (string == null) {
            return;
        }
        int n2 = inventoryClickEvent.getRawSlot();
        Integer n3 = (Integer)this.B.get((Object)uUID);
        Boolean bl = (Boolean)this.G.get((Object)uUID);
        if (n3 == null || bl == null) {
            return;
        }
        if (n2 == 39) {
            this.B.remove((Object)uUID);
            this.G.remove((Object)uUID);
            this.D.D(player, string);
            return;
        }
        for (int n4 : nArray = new int[]{10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25}) {
            if (n2 != n4 || inventoryClickEvent.getCurrentItem() == null || inventoryClickEvent.getCurrentItem().getType() == Material.AIR) continue;
            String string2 = this.B(inventoryClickEvent.getCurrentItem());
            if (string2 != null) {
                player.closeInventory();
                this.A.put((Object)uUID, (Object)string2);
                this.T.put((Object)uUID, (Object)true);
                String string3 = this.D(string2);
                this.B(player, "&8\u00bb &7Wpisz now\u0105 warto\u015b\u0107 dla &f" + string3 + " &8(&7Wpisz &#FF0000anuluj&7, aby anulowa\u0107&8)&7!", "&7Edycja: &f" + string3);
            }
            return;
        }
    }

    private String B(ItemStack itemStack) {
        if (itemStack == null || !itemStack.hasItemMeta() || !itemStack.getItemMeta().hasLore()) {
            return null;
        }
        for (String string : itemStack.getItemMeta().getLore()) {
            String string2 = me.anarchiacore.customitems.stormitemy.utils.color.A.B(string);
            if (!string2.startsWith("ID: ")) continue;
            return string2.substring(4).trim();
        }
        return null;
    }

    /*
     * Exception decompiling
     */
    private String D(String var1_1) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * ob.f0$e
         *     at ob.f0.e(SourceFile:35)
         *     at ob.f0.a(SourceFile:1)
         *     at ob.f0$d.a(SourceFile:68)
         *     at qb.n.i(SourceFile:13)
         *     at qb.e.i(SourceFile:9)
         *     at qb.l.i(SourceFile:14)
         *     at qb.n.i(SourceFile:3)
         *     at ob.f0.g(SourceFile:649)
         *     at ob.f0.d(SourceFile:37)
         *     at ib.f.d(SourceFile:235)
         *     at ib.f.e(SourceFile:7)
         *     at ib.f.c(SourceFile:95)
         *     at rc.f.n(SourceFile:11)
         *     at pc.i.m(SourceFile:5)
         *     at pc.d.K(SourceFile:92)
         *     at pc.d.g0(SourceFile:1)
         *     at fb.b.d(SourceFile:191)
         *     at fb.b.c(SourceFile:145)
         *     at fb.a.a(SourceFile:108)
         *     at com.thesourceofcode.jadec.decompilers.JavaExtractionWorker.decompileWithCFR(SourceFile:76)
         *     at com.thesourceofcode.jadec.decompilers.JavaExtractionWorker.doWork(SourceFile:110)
         *     at com.thesourceofcode.jadec.decompilers.BaseDecompiler.withAttempt(SourceFile:3)
         *     at com.thesourceofcode.jadec.workers.DecompilerWorker.d(SourceFile:53)
         *     at com.thesourceofcode.jadec.workers.DecompilerWorker.b(SourceFile:1)
         *     at e7.a.run(SourceFile:1)
         *     at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1154)
         *     at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:652)
         *     at java.lang.Thread.run(Thread.java:1563)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    private void E(InventoryClickEvent inventoryClickEvent, Player player, UUID uUID) {
        inventoryClickEvent.setCancelled(true);
        String string = (String)this.R.get((Object)uUID);
        if (string == null) {
            return;
        }
        int n2 = inventoryClickEvent.getRawSlot();
        if (n2 == 47) {
            this.D.B(player, string);
            return;
        }
        if (n2 == 49) {
            this.D.E(player, string);
            return;
        }
        if (n2 == 51) {
            this.D.H(player, string);
            return;
        }
        int[] nArray = new int[]{10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25};
        List<Map<String, Object>> list = this.S.M(string);
        for (int i2 = 0; i2 < nArray.length; ++i2) {
            if (n2 != nArray[i2] || inventoryClickEvent.getCurrentItem() == null || inventoryClickEvent.getCurrentItem().getType() == Material.AIR) continue;
            if (i2 >= list.size()) {
                return;
            }
            if (inventoryClickEvent.getClick() == ClickType.SHIFT_RIGHT) {
                if (this.S.E(string, i2)) {
                    this.A(player, "&8\u00bb &7Umiej\u0119tno\u015b\u0107 zosta\u0142a &#FF0000usuni\u0119ta&7!", "&fUmiej\u0119tno\u015b\u0107 &7zosta\u0142a &#FF0000usuni\u0119ta&7!", "ENTITY_ENDERMAN_TELEPORT");
                    this.D.D(player, string);
                }
            } else if (inventoryClickEvent.getClick() == ClickType.LEFT) {
                this.B.put((Object)uUID, (Object)i2);
                this.G.put((Object)uUID, (Object)false);
                this.D.A(player, string, i2, false);
            }
            return;
        }
        int[] nArray2 = new int[]{28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43};
        List<Map<String, Object>> list2 = this.S.F(string);
        for (int i3 = 0; i3 < nArray2.length; ++i3) {
            if (n2 != nArray2[i3] || inventoryClickEvent.getCurrentItem() == null || inventoryClickEvent.getCurrentItem().getType() == Material.AIR) continue;
            if (i3 >= list2.size()) {
                return;
            }
            if (inventoryClickEvent.getClick() == ClickType.SHIFT_RIGHT) {
                if (this.S.D(string, i3)) {
                    this.A(player, "&8\u00bb &7Efekt wizualny zosta\u0142 &#FF0000usuni\u0119ty&7!", "&fEfekt &7zosta\u0142 &#FF0000usuni\u0119ty&7!", "ENTITY_ENDERMAN_TELEPORT");
                    this.D.D(player, string);
                }
            } else if (inventoryClickEvent.getClick() == ClickType.LEFT) {
                this.B.put((Object)uUID, (Object)i3);
                this.G.put((Object)uUID, (Object)true);
                this.D.A(player, string, i3, true);
            }
            return;
        }
    }

    private void C(InventoryClickEvent inventoryClickEvent, Player player, UUID uUID) {
        inventoryClickEvent.setCancelled(true);
        String string = (String)this.R.get((Object)uUID);
        if (string == null) {
            return;
        }
        int n2 = inventoryClickEvent.getRawSlot();
        String string2 = inventoryClickEvent.getView().getTitle();
        int n3 = this.B(string2);
        if (n2 == 49) {
            this.D.D(player, string);
            return;
        }
        if (n2 == 45 && n3 > 0) {
            this.D.A(player, string, n3 - 1);
            return;
        }
        String[][] stringArray = me.anarchiacore.customitems.stormitemy.ui.gui.D.D();
        int n4 = (int)Math.ceil((double)((double)stringArray.length / 28.0));
        if (n2 == 53 && n3 < n4 - 1) {
            this.D.A(player, string, n3 + 1);
            return;
        }
        int[] nArray = new int[]{10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43};
        int n5 = n3 * 28;
        for (int i2 = 0; i2 < nArray.length; ++i2) {
            if (n2 != nArray[i2] || inventoryClickEvent.getCurrentItem() == null || inventoryClickEvent.getCurrentItem().getType() == Material.AIR) continue;
            int n6 = n5 + i2;
            if (n6 >= stringArray.length) {
                return;
            }
            if (this.S.M(string).size() >= 14) {
                this.A(player, "&8\u00bb &7Osi\u0105gni\u0119to maksymaln\u0105 liczb\u0119 umiej\u0119tno\u015bci (&f14&7)!", "&fLimit &7umiej\u0119tno\u015bci!");
                return;
            }
            String string3 = stringArray[n6][0];
            HashMap hashMap = new HashMap();
            hashMap.put((Object)"type", (Object)string3);
            this.A((Map<String, Object>)hashMap, string3);
            if (this.S.B(string, (Map<String, Object>)hashMap)) {
                this.A(player, "&8\u00bb &7Umiej\u0119tno\u015b\u0107 zosta\u0142a &#1DFF1Adodana&7!", "&fUmiej\u0119tno\u015b\u0107 &7zosta\u0142a &#1DFF1Adodana&7!", "ENTITY_PLAYER_LEVELUP");
                this.D.D(player, string);
            }
            return;
        }
    }

    /*
     * Exception decompiling
     */
    private void A(Map<String, Object> var1_1, String var2_2) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * ob.f0$e
         *     at ob.f0.e(SourceFile:35)
         *     at ob.f0.a(SourceFile:1)
         *     at ob.f0$d.a(SourceFile:68)
         *     at qb.n.i(SourceFile:13)
         *     at qb.e.i(SourceFile:9)
         *     at qb.l.i(SourceFile:14)
         *     at qb.n.i(SourceFile:3)
         *     at ob.f0.g(SourceFile:649)
         *     at ob.f0.d(SourceFile:37)
         *     at ib.f.d(SourceFile:235)
         *     at ib.f.e(SourceFile:7)
         *     at ib.f.c(SourceFile:95)
         *     at rc.f.n(SourceFile:11)
         *     at pc.i.m(SourceFile:5)
         *     at pc.d.K(SourceFile:92)
         *     at pc.d.g0(SourceFile:1)
         *     at fb.b.d(SourceFile:191)
         *     at fb.b.c(SourceFile:145)
         *     at fb.a.a(SourceFile:108)
         *     at com.thesourceofcode.jadec.decompilers.JavaExtractionWorker.decompileWithCFR(SourceFile:76)
         *     at com.thesourceofcode.jadec.decompilers.JavaExtractionWorker.doWork(SourceFile:110)
         *     at com.thesourceofcode.jadec.decompilers.BaseDecompiler.withAttempt(SourceFile:3)
         *     at com.thesourceofcode.jadec.workers.DecompilerWorker.d(SourceFile:53)
         *     at com.thesourceofcode.jadec.workers.DecompilerWorker.b(SourceFile:1)
         *     at e7.a.run(SourceFile:1)
         *     at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1154)
         *     at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:652)
         *     at java.lang.Thread.run(Thread.java:1563)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    private void I(InventoryClickEvent inventoryClickEvent, Player player, UUID uUID) {
        inventoryClickEvent.setCancelled(true);
        String string = (String)this.R.get((Object)uUID);
        if (string == null) {
            return;
        }
        int n2 = inventoryClickEvent.getRawSlot();
        String string2 = inventoryClickEvent.getView().getTitle();
        int n3 = this.B(string2);
        if (n2 == 49) {
            this.D.D(player, string);
            return;
        }
        if (n2 == 45 && n3 > 0) {
            this.D.B(player, string, n3 - 1);
            return;
        }
        String[][] stringArray = me.anarchiacore.customitems.stormitemy.ui.gui.D.C();
        int n4 = (int)Math.ceil((double)((double)stringArray.length / 28.0));
        if (n2 == 53 && n3 < n4 - 1) {
            this.D.B(player, string, n3 + 1);
            return;
        }
        int[] nArray = new int[]{10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43};
        int n5 = n3 * 28;
        for (int i2 = 0; i2 < nArray.length; ++i2) {
            if (n2 != nArray[i2] || inventoryClickEvent.getCurrentItem() == null || inventoryClickEvent.getCurrentItem().getType() == Material.AIR) continue;
            int n6 = n5 + i2;
            if (n6 >= stringArray.length) {
                return;
            }
            if (this.S.F(string).size() >= 14) {
                this.A(player, "&8\u00bb &7Osi\u0105gni\u0119to maksymaln\u0105 liczb\u0119 efekt\u00f3w (&f14&7)!", "&fLimit &7efekt\u00f3w!");
                return;
            }
            String string3 = stringArray[n6][0];
            HashMap hashMap = new HashMap();
            hashMap.put((Object)"type", (Object)string3);
            if (string3.startsWith("PARTICLE_")) {
                hashMap.put((Object)"count", (Object)20);
                hashMap.put((Object)"radius", (Object)1.0);
                if (string3.equals((Object)"PARTICLE_REDSTONE")) {
                    hashMap.put((Object)"r", (Object)255);
                    hashMap.put((Object)"g", (Object)0);
                    hashMap.put((Object)"b", (Object)0);
                }
            } else if (string3.startsWith("SOUND_")) {
                hashMap.put((Object)"volume", (Object)1.0);
                hashMap.put((Object)"pitch", (Object)1.0);
            }
            if (this.S.A(string, (Map<String, Object>)hashMap)) {
                this.A(player, "&8\u00bb &7Efekt wizualny zosta\u0142 &#1DFF1Adodany&7!", "&fEfekt &7zosta\u0142 &#1DFF1Adodany&7!", "ENTITY_PLAYER_LEVELUP");
                this.D.D(player, string);
            }
            return;
        }
    }

    private int B(String string) {
        try {
            String string2 = me.anarchiacore.customitems.stormitemy.utils.color.A.B(string);
            int n2 = string2.lastIndexOf("(");
            int n3 = string2.lastIndexOf("/");
            if (n2 > 0 && n3 > n2) {
                return Integer.parseInt((String)string2.substring(n2 + 1, n3).trim()) - 1;
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        return 0;
    }

    private int C(String string) {
        try {
            String string2 = me.anarchiacore.customitems.stormitemy.utils.color.A.B(string);
            int n2 = string2.lastIndexOf("/");
            int n3 = string2.lastIndexOf(")");
            if (n2 > 0 && n3 > n2) {
                return Integer.parseInt((String)string2.substring(n2 + 1, n3).trim());
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        return 1;
    }

    private String A(ItemStack itemStack) {
        if (itemStack == null || !itemStack.hasItemMeta() || !itemStack.getItemMeta().hasLore()) {
            return null;
        }
        for (String string : itemStack.getItemMeta().getLore()) {
            String string2 = me.anarchiacore.customitems.stormitemy.utils.color.A.B(string);
            if (!string2.contains((CharSequence)"ID: ")) continue;
            int n2 = string2.indexOf("ID: ");
            return string2.substring(n2 + 4).trim();
        }
        return null;
    }

    private Enchantment A(String string) {
        Enchantment enchantment = Enchantment.getByName((String)string);
        if (enchantment != null) {
            return enchantment;
        }
        for (Enchantment enchantment2 : Enchantment.values()) {
            if (enchantment2 == null || !enchantment2.getKey().getKey().equalsIgnoreCase(string) && !enchantment2.getName().equalsIgnoreCase(string)) continue;
            return enchantment2;
        }
        return null;
    }

    private void B(Player player, String string, String string2) {
        me.anarchiacore.customitems.stormitemy.messages.A a2 = new A._A().D(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string)).B(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string2)).A(10).B(60).C(15).A();
        me.anarchiacore.customitems.stormitemy.messages.C.A(player, a2);
        this.K(player, "ENTITY_EXPERIENCE_ORB_PICKUP");
    }

    private void A(Player player, String string, String string2, String string3) {
        me.anarchiacore.customitems.stormitemy.messages.A a2 = new A._A().D(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string)).B(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string2)).A(10).B(60).C(15).A();
        me.anarchiacore.customitems.stormitemy.messages.C.A(player, a2);
        this.K(player, string3);
    }

    private void A(Player player, String string, String string2) {
        me.anarchiacore.customitems.stormitemy.messages.A a2 = new A._A().D(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string)).B(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string2)).A(10).B(60).C(15).A();
        me.anarchiacore.customitems.stormitemy.messages.C.A(player, a2);
        this.K(player, "ENTITY_ENDERMAN_TELEPORT");
    }

    private void A(Player player, UUID uUID, Map<UUID, Boolean> map, String string, Runnable runnable) {
        me.anarchiacore.customitems.stormitemy.messages.A a2 = new A._A().B(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string)).A(10).B(60).C(15).A();
        this.E.getServer().getScheduler().runTask((Plugin)this.E, () -> {
            me.anarchiacore.customitems.stormitemy.messages.C.A(player, a2);
            this.K(player, "ENTITY_ENDERMAN_TELEPORT");
            runnable.run();
        });
        map.remove((Object)uUID);
    }

    private void K(Player player, String string) {
        try {
            player.playSound(player.getLocation(), Sound.valueOf((String)string), 1.0f, 1.0f);
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public me.anarchiacore.customitems.stormitemy.ui.gui.E getCustomItemsManager() {
        return this.S;
    }
}

