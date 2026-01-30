/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.io.File
 *  java.io.IOException
 *  java.lang.CharSequence
 *  java.lang.Exception
 *  java.lang.IllegalArgumentException
 *  java.lang.Integer
 *  java.lang.NumberFormatException
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.Iterator
 *  java.util.List
 *  java.util.Random
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.configuration.ConfigurationSection
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.inventory.EquipmentSlot
 *  org.bukkit.inventory.ItemFlag
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.plugin.Plugin
 */
package me.anarchiacore.customitems.stormitemy.items;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import me.anarchiacore.customitems.stormitemy.Main;
import me.anarchiacore.customitems.stormitemy.utils.color.A;

public class G
implements Listener {
    private final Plugin C;
    private ConfigurationSection A;
    private final Random B = new Random();

    public G(Plugin plugin) {
        YamlConfiguration yamlConfiguration;
        File file;
        this.C = plugin;
        File file2 = new File(plugin.getDataFolder(), "items");
        if (!file2.exists()) {
            file2.mkdirs();
        }
        if (!(file = new File(file2, "watacukrowa.yml")).exists()) {
            try {
                file.createNewFile();
                yamlConfiguration = new YamlConfiguration();
                String string = "=== Konfiguracja przedmiotu 'Wata cukrowa' ===\nOpis: Plik konfiguracyjny okre\u015bla w\u0142a\u015bciwo\u015bci przedmiotu eventowego 'Wata cukrowa'.\n";
                yamlConfiguration.options().header(string);
                yamlConfiguration.options().copyHeader(true);
                yamlConfiguration.set("watacukrowa.material", (Object)"PINK_DYE");
                yamlConfiguration.set("watacukrowa.name", (Object)"&b&lWata cukrowa");
                yamlConfiguration.set("watacukrowa.lore", (Object)List.of((Object)"&7", (Object)"&8 \u00bb &7Jest to przedmiot zdobyty podczas", (Object)"&8 \u00bb &fwydarzenia cyrkowego 2025&7!", (Object)"&7", (Object)"&8 \u00bb &7Po u\u017cyciu rozpoczyna &dnapraw\u0119 &7jednego", (Object)"&8 \u00bb &7z element\u00f3w twojej zbroi do &ape\u0142nej trwa\u0142o\u015bci&7!", (Object)"&7"));
                yamlConfiguration.set("watacukrowa.customModelData", (Object)1);
                yamlConfiguration.set("watacukrowa.cooldown", (Object)20);
                yamlConfiguration.set("watacukrowa.enchantments", (Object)List.of((Object)"DURABILITY:1"));
                yamlConfiguration.set("watacukrowa.flags", (Object)List.of((Object)"HIDE_ENCHANTS", (Object)"HIDE_UNBREAKABLE", (Object)"HIDE_ATTRIBUTES"));
                yamlConfiguration.set("watacukrowa.unbreakable", (Object)true);
                yamlConfiguration.set("watacukrowa.messages.success.title", (Object)"&b&lWATA CUKROWA");
                yamlConfiguration.set("watacukrowa.messages.success.subtitle", (Object)"&7Naprawiono przedmiot &f{ITEM}&7!");
                yamlConfiguration.set("watacukrowa.messages.success.chatMessage", (Object)"");
                yamlConfiguration.set("watacukrowa.messages.fullarmor.title", (Object)"&b&lWATA CUKROWA");
                yamlConfiguration.set("watacukrowa.messages.fullarmor.subtitle", (Object)"&7Twoja zbroja jest &fju\u017c naprawiona&7!");
                yamlConfiguration.set("watacukrowa.messages.fullarmor.chatMessage", (Object)"");
                yamlConfiguration.set("watacukrowa.messages.noarmor.title", (Object)"&b&lWATA CUKROWA");
                yamlConfiguration.set("watacukrowa.messages.noarmor.subtitle", (Object)"&7Nie masz na sobie &f\u017cadnej&7 zbroi!");
                yamlConfiguration.set("watacukrowa.messages.noarmor.chatMessage", (Object)"");
                yamlConfiguration.set("watacukrowa.messages.cooldown", (Object)"");
                yamlConfiguration.set("watacukrowa.sounds.success.enabled", (Object)true);
                yamlConfiguration.set("watacukrowa.sounds.success.sound", (Object)"ENTITY_PLAYER_LEVELUP");
                yamlConfiguration.set("watacukrowa.sounds.success.volume", (Object)1.0);
                yamlConfiguration.set("watacukrowa.sounds.success.pitch", (Object)1.2);
                yamlConfiguration.set("watacukrowa.sounds.fullarmor.enabled", (Object)true);
                yamlConfiguration.set("watacukrowa.sounds.fullarmor.sound", (Object)"ENTITY_VILLAGER_NO");
                yamlConfiguration.set("watacukrowa.sounds.fullarmor.volume", (Object)0.8);
                yamlConfiguration.set("watacukrowa.sounds.fullarmor.pitch", (Object)1.0);
                yamlConfiguration.set("watacukrowa.sounds.noarmor.enabled", (Object)true);
                yamlConfiguration.set("watacukrowa.sounds.noarmor.sound", (Object)"ENTITY_VILLAGER_NO");
                yamlConfiguration.set("watacukrowa.sounds.noarmor.volume", (Object)0.8);
                yamlConfiguration.set("watacukrowa.sounds.noarmor.pitch", (Object)0.8);
                yamlConfiguration.set("watacukrowa.sounds.use.enabled", (Object)true);
                yamlConfiguration.set("watacukrowa.sounds.use.sound", (Object)"ENTITY_GENERIC_EAT");
                yamlConfiguration.set("watacukrowa.sounds.use.volume", (Object)1.0);
                yamlConfiguration.set("watacukrowa.sounds.use.pitch", (Object)1.5);
                yamlConfiguration.save(file);
            }
            catch (IOException iOException) {
                iOException.printStackTrace();
            }
        }
        yamlConfiguration = YamlConfiguration.loadConfiguration((File)file);
        this.A = yamlConfiguration.getConfigurationSection("watacukrowa");
        Bukkit.getPluginManager().registerEvents((Listener)this, plugin);
    }

    @EventHandler(priority=EventPriority.HIGH)
    public void onPlayerInteract(PlayerInteractEvent playerInteractEvent) {
        if (this.C instanceof Main && ((Main)this.C).isItemDisabledByKey("watacukrowa")) {
            return;
        }
        if (playerInteractEvent.getHand() != EquipmentSlot.HAND) {
            return;
        }
        if (playerInteractEvent.getAction() != Action.RIGHT_CLICK_AIR && playerInteractEvent.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        Player player = playerInteractEvent.getPlayer();
        ItemStack itemStack = playerInteractEvent.getItem();
        if (itemStack == null || !itemStack.hasItemMeta() || !itemStack.getItemMeta().hasDisplayName()) {
            return;
        }
        String string = ChatColor.stripColor((String)itemStack.getItemMeta().getDisplayName());
        int n2 = itemStack.getItemMeta().hasCustomModelData() ? itemStack.getItemMeta().getCustomModelData() : -1;
        int n3 = this.A.getInt("customModelData", 1);
        String string2 = ChatColor.stripColor((String)me.anarchiacore.customitems.stormitemy.utils.color.A.C(this.A.getString("name", "&b&lWata cukrowa")));
        if (string.equals((Object)string2) && n2 == n3) {
            Material material = itemStack.getType();
            this.B(player, "use");
            if (player.hasCooldown(material)) {
                long l2 = player.getCooldown(material) / 20;
                String string3 = this.A.getString("messages.cooldown", "");
                if (!string3.isEmpty()) {
                    string3 = me.anarchiacore.customitems.stormitemy.utils.color.A.C(string3.replace((CharSequence)"{TIME}", (CharSequence)String.valueOf((long)l2)));
                    player.sendMessage(string3);
                }
                playerInteractEvent.setCancelled(true);
                return;
            }
            if (!this.B(player)) {
                this.D(player);
                playerInteractEvent.setCancelled(true);
                return;
            }
            List<ItemStack> list = this.A(player);
            if (list.isEmpty()) {
                this.C(player);
                playerInteractEvent.setCancelled(true);
                return;
            }
            ItemStack itemStack2 = this.A(list);
            this.A(itemStack2);
            this.A(player, this.B(itemStack2));
            int n4 = this.A.getInt("cooldown", 20) * 20;
            me.anarchiacore.customitems.stormitemy.utils.cooldown.A.A(this.C, player, itemStack, this.A.getInt("cooldown", 20), "watacukrowa");
            if (itemStack.getAmount() > 1) {
                itemStack.setAmount(itemStack.getAmount() - 1);
            } else {
                player.getInventory().setItemInMainHand(null);
            }
            playerInteractEvent.setCancelled(true);
        }
    }

    private List<ItemStack> A(Player player) {
        ItemStack[] itemStackArray;
        ArrayList arrayList = new ArrayList();
        for (ItemStack itemStack : itemStackArray = player.getInventory().getArmorContents()) {
            if (itemStack == null || itemStack.getType().getMaxDurability() <= 0 || itemStack.getDurability() <= 0) continue;
            arrayList.add((Object)itemStack);
        }
        return arrayList;
    }

    private ItemStack A(List<ItemStack> list) {
        if (list.isEmpty()) {
            return null;
        }
        ItemStack itemStack = (ItemStack)list.get(0);
        for (ItemStack itemStack2 : list) {
            if (itemStack2.getDurability() <= itemStack.getDurability()) continue;
            itemStack = itemStack2;
        }
        return itemStack;
    }

    private void A(ItemStack itemStack) {
        itemStack.setDurability((short)0);
    }

    private String B(ItemStack itemStack) {
        switch (itemStack.getType()) {
            case LEATHER_HELMET: 
            case CHAINMAIL_HELMET: 
            case IRON_HELMET: 
            case GOLDEN_HELMET: 
            case DIAMOND_HELMET: 
            case NETHERITE_HELMET: {
                return "he\u0142m";
            }
            case LEATHER_CHESTPLATE: 
            case CHAINMAIL_CHESTPLATE: 
            case IRON_CHESTPLATE: 
            case GOLDEN_CHESTPLATE: 
            case DIAMOND_CHESTPLATE: 
            case NETHERITE_CHESTPLATE: {
                return "napier\u015bnik";
            }
            case LEATHER_LEGGINGS: 
            case CHAINMAIL_LEGGINGS: 
            case IRON_LEGGINGS: 
            case GOLDEN_LEGGINGS: 
            case DIAMOND_LEGGINGS: 
            case NETHERITE_LEGGINGS: {
                return "spodnie";
            }
            case LEATHER_BOOTS: 
            case CHAINMAIL_BOOTS: 
            case IRON_BOOTS: 
            case GOLDEN_BOOTS: 
            case DIAMOND_BOOTS: 
            case NETHERITE_BOOTS: {
                return "buty";
            }
        }
        return "zbroja";
    }

    private void A(Player player, String string) {
        ConfigurationSection configurationSection = this.A.getConfigurationSection("messages.success");
        if (configurationSection == null) {
            return;
        }
        String string2 = configurationSection.getString("title", "");
        String string3 = configurationSection.getString("subtitle", "");
        String string4 = configurationSection.getString("chatMessage", "");
        string3 = string3.replace((CharSequence)"{ITEM}", (CharSequence)string);
        string4 = string4.replace((CharSequence)"{ITEM}", (CharSequence)string);
        if (!string2.isEmpty() || !string3.isEmpty()) {
            player.sendTitle(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string2), me.anarchiacore.customitems.stormitemy.utils.color.A.C(string3), 10, 60, 20);
        }
        if (!string4.isEmpty()) {
            player.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string4));
        }
        this.B(player, "success");
    }

    private void C(Player player) {
        ConfigurationSection configurationSection = this.A.getConfigurationSection("messages.fullarmor");
        if (configurationSection == null) {
            return;
        }
        String string = configurationSection.getString("title", "");
        String string2 = configurationSection.getString("subtitle", "");
        String string3 = configurationSection.getString("chatMessage", "");
        if (!string.isEmpty() || !string2.isEmpty()) {
            player.sendTitle(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string), me.anarchiacore.customitems.stormitemy.utils.color.A.C(string2), 10, 60, 20);
        }
        if (!string3.isEmpty()) {
            player.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string3));
        }
        this.B(player, "fullarmor");
    }

    public ItemStack getItem() {
        Material material = Material.valueOf((String)this.A.getString("material", "PINK_DYE"));
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta != null) {
            Enchantment enchantment;
            Object object;
            String string3;
            String string22;
            itemMeta.setDisplayName(me.anarchiacore.customitems.stormitemy.utils.color.A.C(this.A.getString("name", "&b&lWata cukrowa")));
            ArrayList arrayList = new ArrayList();
            for (String string22 : this.A.getStringList("lore")) {
                arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C(string22));
            }
            itemMeta.setLore((List)arrayList);
            if (this.A.contains("customModelData")) {
                itemMeta.setCustomModelData(Integer.valueOf((int)this.A.getInt("customModelData")));
            }
            if (this.A.getBoolean("unbreakable", false)) {
                itemMeta.setUnbreakable(true);
            }
            Iterator iterator = this.A.getStringList("enchantments");
            for (String string3 : iterator) {
                object = string3.split(":");
                if (((String[])object).length != 2) continue;
                try {
                    enchantment = Enchantment.getByName((String)object[0]);
                    int n2 = Integer.parseInt((String)object[1]);
                    if (enchantment == null) continue;
                    itemMeta.addEnchant(enchantment, n2, true);
                }
                catch (NumberFormatException numberFormatException) {}
            }
            string22 = this.A.getStringList("flags");
            string3 = string22.iterator();
            while (string3.hasNext()) {
                object = (String)string3.next();
                try {
                    enchantment = ItemFlag.valueOf((String)object);
                    itemMeta.addItemFlags(new ItemFlag[]{enchantment});
                }
                catch (IllegalArgumentException illegalArgumentException) {}
            }
            itemStack.setItemMeta(itemMeta);
        }
        return itemStack;
    }

    private boolean B(Player player) {
        ItemStack[] itemStackArray;
        for (ItemStack itemStack : itemStackArray = player.getInventory().getArmorContents()) {
            if (itemStack == null || itemStack.getType() == Material.AIR) continue;
            return true;
        }
        return false;
    }

    private void D(Player player) {
        ConfigurationSection configurationSection = this.A.getConfigurationSection("messages.noarmor");
        if (configurationSection == null) {
            return;
        }
        String string = configurationSection.getString("title", "");
        String string2 = configurationSection.getString("subtitle", "");
        String string3 = configurationSection.getString("chatMessage", "");
        if (!string.isEmpty() || !string2.isEmpty()) {
            player.sendTitle(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string), me.anarchiacore.customitems.stormitemy.utils.color.A.C(string2), 10, 60, 20);
        }
        if (!string3.isEmpty()) {
            player.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string3));
        }
        this.B(player, "noarmor");
    }

    private void B(Player player, String string) {
        try {
            boolean bl = this.A.getBoolean("sounds." + string + ".enabled", true);
            if (!bl) {
                return;
            }
            String string2 = this.A.getString("sounds." + string + ".sound");
            if (string2 != null && !string2.trim().isEmpty()) {
                Sound sound = Sound.valueOf((String)string2);
                float f2 = (float)this.A.getDouble("sounds." + string + ".volume", 1.0);
                float f3 = (float)this.A.getDouble("sounds." + string + ".pitch", 1.0);
                player.playSound(player.getLocation(), sound, f2, f3);
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public void reload() {
        try {
            File file = new File(this.C.getDataFolder(), "items/watacukrowa.yml");
            if (file.exists()) {
                YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration((File)file);
                this.A = yamlConfiguration.getConfigurationSection("watacukrowa");
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }
}

