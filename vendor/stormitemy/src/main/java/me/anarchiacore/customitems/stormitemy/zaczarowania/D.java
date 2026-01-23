/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.CharSequence
 *  java.lang.IllegalArgumentException
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.HashMap
 *  java.util.Iterator
 *  java.util.List
 *  java.util.Map
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.event.inventory.InventoryCloseEvent
 *  org.bukkit.event.inventory.InventoryDragEvent
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.plugin.Plugin
 */
package me.anarchiacore.customitems.stormitemy.zaczarowania;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import me.anarchiacore.customitems.stormitemy.Main;
import me.anarchiacore.customitems.stormitemy.utils.color.A;
import me.anarchiacore.customitems.stormitemy.zaczarowania.C;

public class D
implements Listener {
    private final Main E;
    private final C D;
    private final Map<Player, ItemStack> C = new HashMap();
    private final Map<Player, Integer> B = new HashMap();
    private String A;

    public D(Main main, C c2) {
        this.E = main;
        this.D = c2;
        this.A = me.anarchiacore.customitems.stormitemy.utils.color.A.C(main.getConfig().getString("zaczarowania.menu.title", "&3&lZaczarowania przedmiot\u00f3w"));
        main.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)main);
    }

    public void openMenu(Player player, ItemStack itemStack) {
        Material material;
        Material material2;
        Inventory inventory = Bukkit.createInventory(null, (int)27, (String)this.A);
        this.C.put((Object)player, (Object)itemStack);
        ItemStack itemStack2 = this.A();
        String string = this.E.getConfig().getString("zaczarowania.menu.select_button.material", "PAPER");
        String string2 = this.E.getConfig().getString("zaczarowania.menu.select_button.name", "&6WYBIERZ PRZEDMIOT");
        List list = this.E.getConfig().getStringList("zaczarowania.menu.select_button.lore");
        int n2 = this.E.getConfig().getInt("zaczarowania.menu.select_button.customModelData", 0);
        if (list.isEmpty()) {
            list = List.of((Object)"&7Kliknij na miecz w ekwipunku,", (Object)"&7kt\u00f3ry chcia\u0142by\u015b zaczarowa\u0107.");
        }
        try {
            material2 = Material.valueOf((String)string);
        }
        catch (IllegalArgumentException illegalArgumentException) {
            material2 = Material.PAPER;
        }
        ItemStack itemStack3 = this.A(material2, string2, (List<String>)list, n2);
        String string3 = this.E.getConfig().getString("zaczarowania.menu.close_button.material", "BARRIER");
        String string4 = this.E.getConfig().getString("zaczarowania.menu.close_button.name", "&cZamknij");
        List list2 = this.E.getConfig().getStringList("zaczarowania.menu.close_button.lore");
        int n3 = this.E.getConfig().getInt("zaczarowania.menu.close_button.customModelData", 0);
        if (list2.isEmpty()) {
            list2 = List.of((Object)"&7Zamyka menu zaczarowa\u0144.");
        }
        try {
            material = Material.valueOf((String)string3);
        }
        catch (IllegalArgumentException illegalArgumentException) {
            material = Material.BARRIER;
        }
        ItemStack itemStack4 = this.A(material, string4, (List<String>)list2, n3);
        inventory.setItem(11, itemStack2);
        inventory.setItem(13, itemStack3);
        inventory.setItem(15, itemStack4);
        player.openInventory(inventory);
    }

    private ItemStack A() {
        Material material;
        String string = this.E.getConfig().getString("zaczarowania.menu.przeboj_button.material", "BLUE_DYE");
        String string2 = this.E.getConfig().getString("zaczarowania.menu.przeboj_button.name", "&3&lPrzebouj przedmiot");
        List list = this.E.getConfig().getStringList("zaczarowania.menu.przeboj_button.lore");
        int n2 = this.E.getConfig().getInt("zaczarowania.menu.przeboj_button.customModelData", 0);
        if (list.isEmpty()) {
            list = List.of((Object)"&7", (Object)"&4 UWAGA! &cKlikaj\u0105c ten przedmiot", (Object)"&c zmienisz bonusowe obra\u017cenia", (Object)"&c bezpowrotnie!", (Object)"&7", (Object)"&8 \u00bb &7Koszt: &bZaczarowany przedmiot x1", (Object)"&7", (Object)"&aKliknij aby to zrobi\u0107!");
        }
        try {
            material = Material.valueOf((String)string);
        }
        catch (IllegalArgumentException illegalArgumentException) {
            material = Material.BLUE_DYE;
        }
        return this.A(material, string2, (List<String>)list, n2);
    }

    private ItemStack A(ItemStack itemStack) {
        Iterator iterator;
        int n2 = this.D.C(itemStack);
        int n3 = this.E.getConfig().getInt("zaczarowania.effects.negative_threshold", 0);
        int n4 = this.E.getConfig().getInt("zaczarowania.effects.high_threshold", 40);
        String string = this.E.getConfig().getString("zaczarowania.effects.format_negative", "&cDodatkowe obra\u017cenia {percent}%");
        String string2 = this.E.getConfig().getString("zaczarowania.effects.format_normal", "&fDodatkowe obra\u017cenia {percent}%");
        String string3 = this.E.getConfig().getString("zaczarowania.effects.format_high", "&6Dodatkowe obra\u017cenia {percent}%");
        String string4 = n2 < n3 ? string.replace((CharSequence)"{percent}", (CharSequence)String.valueOf((int)n2)) : (n2 >= n4 ? string3.replace((CharSequence)"{percent}", (CharSequence)String.valueOf((int)n2)) : string2.replace((CharSequence)"{percent}", (CharSequence)String.valueOf((int)n2)));
        String string5 = this.E.getConfig().getString("zaczarowania.menu.przeboj_button.material", "BLUE_DYE");
        String string6 = this.E.getConfig().getString("zaczarowania.menu.przeboj_button.name", "&3&lPrzebouj przedmiot");
        List list = this.E.getConfig().getStringList("zaczarowania.menu.przeboj_button.lore_selected");
        int n5 = this.E.getConfig().getInt("zaczarowania.menu.przeboj_button.customModelData", 0);
        if (list.isEmpty()) {
            list = List.of((Object[])new String[]{"&7", "&8 \u00bb &7Ostatnie wylosowane obra\u017cenia: ", "&8 \u00bb {bonus_text}", "", "&4 UWAGA! &cKlikaj\u0105c ten przedmiot", "&c zmienisz bonusowe obra\u017cenia", "&c bezpowrotnie!", "&7", "&8 \u00bb &7Koszt: &bZaczarowany przedmiot x1", "&7", "&aKliknij aby to zrobi\u0107!"});
        }
        ArrayList arrayList = new ArrayList();
        for (String string7 : list) {
            arrayList.add((Object)string7.replace((CharSequence)"{bonus_text}", (CharSequence)string4));
        }
        try {
            iterator = Material.valueOf((String)string5);
        }
        catch (IllegalArgumentException illegalArgumentException) {
            iterator = Material.BLUE_DYE;
        }
        return this.A((Material)iterator, string6, (List<String>)arrayList, n5);
    }

    private ItemStack A(Material material, String string, List<String> list) {
        return this.A(material, string, list, 0);
    }

    private ItemStack A(Material material, String string, List<String> list, int n2) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return itemStack;
        }
        itemMeta.setDisplayName(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string));
        ArrayList arrayList = new ArrayList();
        for (String string2 : list) {
            arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C(string2));
        }
        itemMeta.setLore((List)arrayList);
        if (n2 > 0) {
            itemMeta.setCustomModelData(Integer.valueOf((int)n2));
        }
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private boolean B(Player player) {
        for (ItemStack itemStack : player.getInventory().getContents()) {
            if (itemStack == null || !this.D.A(itemStack)) continue;
            return true;
        }
        return false;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent inventoryClickEvent) {
        if (!(inventoryClickEvent.getWhoClicked() instanceof Player)) {
            return;
        }
        Player player = (Player)inventoryClickEvent.getWhoClicked();
        String string = inventoryClickEvent.getView().getTitle();
        if (!string.equals((Object)this.A)) {
            return;
        }
        int n2 = inventoryClickEvent.getRawSlot();
        inventoryClickEvent.setCancelled(true);
        if (n2 == 11) {
            if (!this.B(player)) {
                me.anarchiacore.customitems.stormitemy.utils.language.A.A(player, "zaczarowania_no_item");
                return;
            }
            ItemStack itemStack = inventoryClickEvent.getInventory().getItem(13);
            if (itemStack != null && itemStack.getType() != Material.PAPER) {
                ItemStack itemStack2;
                Integer n3 = (Integer)this.B.get((Object)player);
                if (n3 != null && (itemStack2 = player.getInventory().getItem(n3.intValue())) != null && this.D.B(itemStack2)) {
                    int n4 = this.D.D();
                    ItemStack itemStack3 = this.D.A(itemStack2, n4);
                    player.getInventory().setItem(n3.intValue(), itemStack3);
                    inventoryClickEvent.getInventory().setItem(13, itemStack3.clone());
                    inventoryClickEvent.getInventory().setItem(11, this.A(itemStack3));
                    int n5 = this.E.getConfig().getInt("zaczarowania.effects.negative_threshold", 0);
                    int n6 = this.E.getConfig().getInt("zaczarowania.effects.high_threshold", 40);
                    String string2 = n4 < n5 ? "&c" : (n4 >= n6 ? "&6" : "&f");
                    HashMap hashMap = new HashMap();
                    hashMap.put((Object)"percent", (Object)String.valueOf((int)n4));
                    hashMap.put((Object)"color", (Object)(n4 < 0 ? "&c" : "&a"));
                    me.anarchiacore.customitems.stormitemy.utils.language.A.A(player, "zaczarowania_bonus_generated", (Map<String, String>)hashMap);
                    this.A(player);
                }
            } else {
                me.anarchiacore.customitems.stormitemy.utils.language.A.A(player, "zaczarowania_select_first");
            }
        } else if (n2 == 15) {
            player.closeInventory();
        } else if (n2 >= 27) {
            ItemStack itemStack = inventoryClickEvent.getCurrentItem();
            if (itemStack != null && this.D.A(itemStack)) {
                me.anarchiacore.customitems.stormitemy.utils.language.A.A(player, "zaczarowania_cant_enchant_enchanter");
                return;
            }
            if (itemStack != null && this.D.B(itemStack)) {
                this.B.put((Object)player, (Object)inventoryClickEvent.getSlot());
                inventoryClickEvent.getInventory().setItem(13, itemStack.clone());
                inventoryClickEvent.getInventory().setItem(11, this.A(itemStack));
                me.anarchiacore.customitems.stormitemy.utils.language.A.A(player, "zaczarowania_item_selected");
            }
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent inventoryDragEvent) {
        if (!(inventoryDragEvent.getWhoClicked() instanceof Player)) {
            return;
        }
        if (inventoryDragEvent.getView().getTitle().equals((Object)this.A)) {
            inventoryDragEvent.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent inventoryCloseEvent) {
        if (!(inventoryCloseEvent.getPlayer() instanceof Player)) {
            return;
        }
        if (!inventoryCloseEvent.getView().getTitle().equals((Object)this.A)) {
            return;
        }
        Player player = (Player)inventoryCloseEvent.getPlayer();
        this.C.remove((Object)player);
        this.B.remove((Object)player);
    }

    private void A(Player player) {
        for (int i2 = 0; i2 < player.getInventory().getSize(); ++i2) {
            ItemStack itemStack = player.getInventory().getItem(i2);
            if (itemStack == null || !this.D.A(itemStack)) continue;
            if (itemStack.getAmount() > 1) {
                itemStack.setAmount(itemStack.getAmount() - 1);
            } else {
                player.getInventory().setItem(i2, null);
            }
            me.anarchiacore.customitems.stormitemy.utils.language.A.A(player, "zaczarowania_item_consumed");
            return;
        }
    }
}

