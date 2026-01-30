/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.util.Arrays
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.ItemFlag
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 */
package me.anarchiacore.customitems.stormitemy.commands;

import java.util.Arrays;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import me.anarchiacore.customitems.stormitemy.Main;
import me.anarchiacore.customitems.stormitemy.ui.gui.A;
import me.anarchiacore.customitems.stormitemy.ui.gui.C;

public class B
implements CommandExecutor {
    private final Main C;
    private C A;
    private static final String B = " &#B400FF&l\u1d18\u0280\u1d07\u1d0d\u026a\u1d1c\u1d0d";

    public B(Main main, A a2) {
        this.C = main;
    }

    public void setCustomItemsListGUI(C c2) {
        this.A = c2;
    }

    public void openCustomItemsListGUI(Player player) {
        if (this.A != null) {
            this.A.A(player);
        }
    }

    public boolean onCommand(CommandSender commandSender, Command command, String string, String[] stringArray) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&cTa komenda mo\u017ce by\u0107 u\u017cywana tylko przez graczy!"));
            return true;
        }
        Player player = (Player)commandSender;
        this.openPanelGUI(player);
        return true;
    }

    public void openPanelGUI(Player player) {
        boolean bl = this.C.getInitializer() != null && this.C.getInitializer().W != null && this.C.getInitializer().W.G();
        int n2 = bl ? 45 : 27;
        String string = me.anarchiacore.customitems.stormitemy.utils.color.A.C("\u00a7x\u00a7F\u00a7F\u00a7F\u00a79\u00a71\u00a7A\u26a1 \u00a78\ua731\u1d1b\u1d0f\u0280\u1d0d\u026a\u1d1b\u1d07\u1d0d\u028f v1.15");
        if (bl) {
            string = me.anarchiacore.customitems.stormitemy.utils.color.A.C("\u00a7x\u00a7F\u00a7F\u00a7F\u00a79\u00a71\u00a7A\u26a1 \u00a78\ua731\u1d1b\u1d0f\u0280\u1d0d\u026a\u1d1b\u1d07\u1d0d\u028f v1.15 &#B400FF&l\u1d18\u0280\u1d07\u1d0d\u026a\u1d1c\u1d0d");
        }
        Inventory inventory = Bukkit.createInventory(null, (int)n2, (String)string);
        ItemStack itemStack = new ItemStack(Material.GOLDEN_AXE);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setCustomModelData(Integer.valueOf((int)1));
        itemMeta.setDisplayName(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#DD00FFPrzedmioty"));
        itemMeta.setLore(Arrays.asList(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"), me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7W tym miejscu mo\u017cesz"), me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7zobaczy\u0107 i wzi\u0105\u015b\u0107 do EQ!"), me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7wszystkie &fcustom przedmioty&7!"), me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"), me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#1DFF1A\u1d0b\u029f\u026a\u1d0b\u0274\u026a\u1d0a, \u1d00\u0299\u028f \u1d0f\u1d1b\u1d21\u1d0f\u0280\u1d22\u028f\u0107!")));
        itemMeta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE});
        itemStack.setItemMeta(itemMeta);
        inventory.setItem(11, itemStack);
        ItemStack itemStack2 = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta itemMeta2 = itemStack2.getItemMeta();
        itemMeta2.setCustomModelData(Integer.valueOf((int)24));
        itemMeta2.setDisplayName(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#DD00FFKsi\u0119gi"));
        itemMeta2.setLore(Arrays.asList(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"), me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7W tym miejscu mo\u017cesz"), me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7zobaczy\u0107 i wzi\u0105\u015b\u0107 do EQ!"), me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7wszystkie &fcustom ksi\u0119gi&7!"), me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"), me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#1DFF1A\u1d0b\u029f\u026a\u1d0b\u0274\u026a\u1d0a, \u1d00\u0299\u028f \u1d0f\u1d1b\u1d21\u1d0f\u0280\u1d22\u028f\u0107!")));
        itemMeta2.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE});
        itemStack2.setItemMeta(itemMeta2);
        inventory.setItem(12, itemStack2);
        ItemStack itemStack3 = new ItemStack(Material.PURPLE_BANNER);
        ItemMeta itemMeta3 = itemStack3.getItemMeta();
        itemMeta3.setDisplayName(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#DD00FFRegiony"));
        itemMeta3.setLore(Arrays.asList(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"), me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7W tym miejscu mo\u017cesz"), me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7edytowa\u0107 &fregiony &7w pluginie!"), me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"), me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#1DFF1A\u1d0b\u029f\u026a\u1d0b\u0274\u026a\u1d0a, \u1d00\u0299\u028f \u1d0f\u1d1b\u1d21\u1d0f\u0280\u1d22\u028f\u0107!")));
        itemMeta3.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE});
        itemStack3.setItemMeta(itemMeta3);
        inventory.setItem(13, itemStack3);
        if (bl) {
            ItemStack itemStack4 = new ItemStack(Material.CRAFTING_TABLE);
            ItemMeta itemMeta4 = itemStack4.getItemMeta();
            itemMeta4.setDisplayName(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#DD00FFKreator przedmiot\u00f3w"));
            itemMeta4.setLore(Arrays.asList(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"), me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7W tym miejscu b\u0119dziesz m\u00f3g\u0142"), me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7tworzy\u0107 w\u0142asne &fcustom przedmioty&7!"), me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"), me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#1DFF1A\u1d0b\u029f\u026a\u1d0b\u0274\u026a\u1d0a, \u1d00\u0299\u028f \u1d0f\u1d1b\u1d21\u1d0f\u0280\u1d22\u028f\u0107!")));
            itemMeta4.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE});
            itemStack4.setItemMeta(itemMeta4);
            inventory.setItem(15, itemStack4);
            ItemStack itemStack5 = new ItemStack(Material.OAK_SIGN);
            ItemMeta itemMeta5 = itemStack5.getItemMeta();
            itemMeta5.setDisplayName(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#DD00FFEdytor przedmiot\u00f3w"));
            itemMeta5.setLore(Arrays.asList(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"), me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7W tym miejscu mo\u017cesz"), me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7edytowa\u0107 przedmioty z"), me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7poziomu &fGUI&7!"), me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"), me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#1DFF1A\u1d0b\u029f\u026a\u1d0b\u0274\u026a\u1d0a, \u1d00\u0299\u028f \u1d0f\u1d1b\u1d21\u1d0f\u0280\u1d22\u028f\u0107!")));
            itemMeta5.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE});
            itemStack5.setItemMeta(itemMeta5);
            inventory.setItem(31, itemStack5);
        } else {
            ItemStack itemStack6 = new ItemStack(Material.OAK_SIGN);
            ItemMeta itemMeta6 = itemStack6.getItemMeta();
            itemMeta6.setDisplayName(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#DD00FFEdytor przedmiot\u00f3w"));
            itemMeta6.setLore(Arrays.asList(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"), me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7W tym miejscu mo\u017cesz"), me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7edytowa\u0107 przedmioty z"), me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7poziomu &fGUI&7!"), me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"), me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#1DFF1A\u1d0b\u029f\u026a\u1d0b\u0274\u026a\u1d0a, \u1d00\u0299\u028f \u1d0f\u1d1b\u1d21\u1d0f\u0280\u1d22\u028f\u0107!")));
            itemMeta6.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE});
            itemStack6.setItemMeta(itemMeta6);
            inventory.setItem(15, itemStack6);
        }
        player.openInventory(inventory);
    }
}
