/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.CharSequence
 *  java.lang.Exception
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.Override
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.Arrays
 *  java.util.HashMap
 *  java.util.Iterator
 *  java.util.List
 *  java.util.Map
 *  java.util.Map$Entry
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.NamespacedKey
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.PlayerDeathEvent
 *  org.bukkit.inventory.ItemFlag
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.persistence.PersistentDataType
 *  org.bukkit.plugin.Plugin
 */
package me.anarchiacore.customitems.stormitemy.books.enchantments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import me.anarchiacore.customitems.stormitemy.Main;
import me.anarchiacore.customitems.stormitemy.books.A;
import me.anarchiacore.customitems.stormitemy.books.D;

public class B
extends D
implements Listener {
    public B(Main main) {
        super(main, "U\u0142askawienie I", "ulaskawienie", (List<String>)Arrays.asList((Object[])new String[]{"helmet", "chestplate", "leggings", "boots"}), 0.0, 0, (List<String>)Arrays.asList((Object[])new String[]{"&7U\u0142askawienie I", "&7", "&2&lZakl\u0119cie specjalne!", "&7Mo\u017ce zosta\u0107 na\u0142o\u017cony na &azbroj\u0119&7!", "&7Po \u015bmierci ratuje Twoje przedmioty.", "&7"}));
        Bukkit.getPluginManager().registerEvents((Listener)this, (Plugin)main);
    }

    @Override
    protected List<Map<String, Object>> getDefaultEffects() {
        return new ArrayList();
    }

    @Override
    protected Map<String, Object> getDefaultSoundConfig() {
        return null;
    }

    @Override
    protected FileConfiguration loadConfiguration() {
        FileConfiguration fileConfiguration = super.loadConfiguration();
        fileConfiguration.set("customModelData", (Object)24);
        boolean bl = true;
        if (!fileConfiguration.isSet("ulaskawienie")) {
            fileConfiguration.set("ulaskawienie.only_save_in_inventory", (Object)false);
            fileConfiguration.set("ulaskawienie.return_to_inventory", (Object)true);
            fileConfiguration.set("ulaskawienie.auto_equip_armor", (Object)true);
        } else if (!fileConfiguration.isSet("ulaskawienie.auto_equip_armor")) {
            fileConfiguration.set("ulaskawienie.auto_equip_armor", (Object)true);
        }
        if (!fileConfiguration.isSet("messages.title") || fileConfiguration.getString("messages.title").contains((CharSequence)"TRUCIZNY")) {
            fileConfiguration.set("messages.title", (Object)"");
            fileConfiguration.set("messages.subtitle", (Object)"");
            fileConfiguration.set("messages.chat", (Object)"");
            try {
                this.saveCustomConfig((YamlConfiguration)fileConfiguration, this.configFile);
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        return fileConfiguration;
    }

    @Override
    public void applyEffectToItem(ItemStack itemStack) {
        super.applyEffectToItem(itemStack);
    }

    @Override
    public boolean canApplyToItem(ItemStack itemStack) {
        if (itemStack == null) {
            return false;
        }
        Material material = itemStack.getType();
        return this.C(material) || this.B(material) || this.A(material);
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public void onPlayerDeath(PlayerDeathEvent playerDeathEvent) {
        boolean bl;
        Object object;
        ItemMeta itemMeta;
        ItemStack itemStack;
        Player player = playerDeathEvent.getEntity();
        List list = playerDeathEvent.getDrops();
        ArrayList arrayList = new ArrayList();
        A a2 = this.plugin.getEnchantedBooksManager();
        HashMap hashMap = new HashMap();
        ArrayList arrayList2 = new ArrayList();
        boolean bl2 = false;
        ItemStack[] itemStackArray = player.getInventory().getArmorContents();
        boolean[] blArray = new boolean[4];
        for (int i2 = 0; i2 < itemStackArray.length; ++i2) {
            ItemStack itemStack2 = itemStackArray[i2];
            if (itemStack2 == null || !itemStack2.hasItemMeta()) continue;
            NamespacedKey namespacedKey = new NamespacedKey((Plugin)this.plugin, "enchant_" + this.getIdentifier());
            if (!itemStack2.getItemMeta().getPersistentDataContainer().has(namespacedKey, PersistentDataType.DOUBLE)) continue;
            itemStack = itemStack2.clone();
            itemMeta = itemStack.getItemMeta();
            itemMeta.getPersistentDataContainer().remove(namespacedKey);
            if (itemMeta.hasLore()) {
                object = itemMeta.getLore();
                ArrayList arrayList3 = new ArrayList();
                for (String string2 : object) {
                    if (string2.toLowerCase().contains((CharSequence)"u\u0142askawienie") || string2.toLowerCase().contains((CharSequence)"ulaskawienie")) continue;
                    arrayList3.add((Object)string2);
                }
                itemMeta.setLore((List)arrayList3);
            }
            itemStack.setItemMeta(itemMeta);
            if (a2.B(itemStack) == 0 && (itemMeta = itemStack.getItemMeta()).hasEnchant(Enchantment.DURABILITY) && itemMeta.getEnchantLevel(Enchantment.DURABILITY) == 1) {
                itemMeta.removeEnchant(Enchantment.DURABILITY);
                if (itemMeta.hasItemFlag(ItemFlag.HIDE_ENCHANTS)) {
                    itemMeta.removeItemFlags(new ItemFlag[]{ItemFlag.HIDE_ENCHANTS});
                }
                itemStack.setItemMeta(itemMeta);
            }
            if (!a2.A(itemStack, this.getIdentifier())) {
                object = itemStack.getType().toString() + "_" + i2;
                arrayList2.add(object);
                hashMap.put((Object)i2, (Object)itemStack);
                blArray[i2] = true;
                bl2 = true;
                continue;
            }
            a2.B(itemStack, this.getIdentifier());
            object = itemStack.getType().toString() + "_" + i2;
            arrayList2.add(object);
            hashMap.put((Object)i2, (Object)itemStack);
            blArray[i2] = true;
            bl2 = true;
        }
        ItemStack[] itemStackArray2 = player.getInventory().getArmorContents();
        boolean bl3 = false;
        for (int i3 = 0; i3 < itemStackArray2.length; ++i3) {
            if (!blArray[i3]) continue;
            itemStackArray2[i3] = null;
            bl3 = true;
        }
        if (bl3) {
            player.getInventory().setArmorContents(itemStackArray2);
        }
        Iterator iterator = list.iterator();
        while (iterator.hasNext()) {
            String string2;
            itemStack = (ItemStack)iterator.next();
            if (itemStack == null || !itemStack.hasItemMeta()) continue;
            itemMeta = new NamespacedKey((Plugin)this.plugin, "enchant_" + this.getIdentifier());
            if (!itemStack.getItemMeta().getPersistentDataContainer().has((NamespacedKey)itemMeta, PersistentDataType.DOUBLE)) continue;
            object = itemStack.getType().toString();
            boolean bl4 = false;
            for (String string2 : arrayList2) {
                if (!string2.startsWith((String)object)) continue;
                bl4 = true;
                break;
            }
            if (bl4) {
                iterator.remove();
                continue;
            }
            Iterator iterator2 = itemStack.clone();
            string2 = iterator2.getItemMeta();
            string2.getPersistentDataContainer().remove((NamespacedKey)itemMeta);
            if (string2.hasLore()) {
                List list2 = string2.getLore();
                ArrayList arrayList4 = new ArrayList();
                for (String string3 : list2) {
                    if (string3.toLowerCase().contains((CharSequence)"u\u0142askawienie") || string3.toLowerCase().contains((CharSequence)"ulaskawienie")) continue;
                    arrayList4.add((Object)string3);
                }
                string2.setLore((List)arrayList4);
            }
            iterator2.setItemMeta((ItemMeta)string2);
            if (a2.B((ItemStack)iterator2) == 0 && (string2 = iterator2.getItemMeta()).hasEnchant(Enchantment.DURABILITY) && string2.getEnchantLevel(Enchantment.DURABILITY) == 1) {
                string2.removeEnchant(Enchantment.DURABILITY);
                if (string2.hasItemFlag(ItemFlag.HIDE_ENCHANTS)) {
                    string2.removeItemFlags(new ItemFlag[]{ItemFlag.HIDE_ENCHANTS});
                }
                iterator2.setItemMeta((ItemMeta)string2);
            }
            if (!a2.A((ItemStack)iterator2, this.getIdentifier())) {
                arrayList2.add(object);
                arrayList.add((Object)iterator2);
                iterator.remove();
                bl2 = true;
                continue;
            }
            a2.B((ItemStack)iterator2, this.getIdentifier());
            arrayList2.add(object);
            arrayList.add((Object)iterator2);
            iterator.remove();
            bl2 = true;
        }
        boolean bl5 = bl = !arrayList.isEmpty() || !hashMap.isEmpty();
        if (bl) {
            if (bl2) {
                player.setMetadata("ulaskawienie_activated", (MetadataValue)new FixedMetadataValue((Plugin)this.plugin, (Object)true));
                Bukkit.getScheduler().runTaskLater((Plugin)this.plugin, () -> {
                    if (player.isOnline()) {
                        player.removeMetadata("ulaskawienie_activated", (Plugin)this.plugin);
                    }
                }, 40L);
            }
            boolean bl6 = this.config.getBoolean("ulaskawienie.return_to_inventory", true);
            boolean bl7 = this.config.getBoolean("ulaskawienie.auto_equip_armor", true);
            if (bl6) {
                Bukkit.getScheduler().runTaskLater((Plugin)this.plugin, () -> B.A(bl7, (Map)hashMap, player, (List)arrayList), 20L);
            } else {
                for (Iterator iterator2 : hashMap.values()) {
                    player.getWorld().dropItemNaturally(player.getLocation(), (ItemStack)iterator2);
                }
                for (Iterator iterator2 : arrayList) {
                    player.getWorld().dropItemNaturally(player.getLocation(), (ItemStack)iterator2);
                }
            }
        }
    }

    private boolean C(Material material) {
        String string = material.name();
        return string.endsWith("_HELMET") || string.endsWith("_CHESTPLATE") || string.endsWith("_LEGGINGS") || string.endsWith("_BOOTS") || material == Material.ELYTRA;
    }

    private boolean B(Material material) {
        String string = material.name();
        return string.endsWith("_SWORD") || string.endsWith("_AXE") || material == Material.BOW || material == Material.CROSSBOW || material == Material.TRIDENT;
    }

    private boolean A(Material material) {
        String string = material.name();
        return string.endsWith("_PICKAXE") || string.endsWith("_AXE") || string.endsWith("_SHOVEL") || string.endsWith("_HOE") || material == Material.FISHING_ROD || material == Material.SHEARS || material == Material.FLINT_AND_STEEL;
    }

    @Override
    public void handleEffect(ItemStack itemStack, Object ... objectArray) {
    }

    private static /* synthetic */ void A(boolean bl, Map map, Player player, List list) {
        ArrayList arrayList;
        if (bl && !map.isEmpty()) {
            arrayList = player.getInventory().getArmorContents();
            boolean bl2 = false;
            for (Map.Entry entry : map.entrySet()) {
                int n2 = (Integer)entry.getKey();
                ItemStack itemStack = (ItemStack)entry.getValue();
                if (arrayList[n2] == null || arrayList[n2].getType() == Material.AIR) {
                    arrayList[n2] = itemStack;
                    bl2 = true;
                    continue;
                }
                list.add((Object)itemStack);
            }
            if (bl2) {
                player.getInventory().setArmorContents((ItemStack[])arrayList);
            }
        } else {
            for (ItemStack itemStack : map.values()) {
                list.add((Object)itemStack);
            }
        }
        if (!list.isEmpty()) {
            arrayList = new ArrayList();
            ArrayList arrayList2 = new ArrayList();
            for (Map.Entry entry : list) {
                String string = entry.getType().toString();
                if (arrayList2.contains((Object)string)) continue;
                arrayList.add((Object)entry);
                arrayList2.add((Object)string);
            }
            for (Map.Entry entry : arrayList) {
                HashMap hashMap = player.getInventory().addItem(new ItemStack[]{entry});
                if (hashMap.isEmpty()) continue;
                for (ItemStack itemStack : hashMap.values()) {
                    player.getWorld().dropItemNaturally(player.getLocation(), itemStack);
                }
            }
        }
    }
}

