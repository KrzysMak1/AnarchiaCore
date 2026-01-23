/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.CharSequence
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.Override
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.Arrays
 *  java.util.List
 *  java.util.Map
 *  java.util.Random
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.NamespacedKey
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerItemDamageEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.persistence.PersistentDataType
 *  org.bukkit.plugin.Plugin
 */
package me.anarchiacore.customitems.stormitemy.books.enchantments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import me.anarchiacore.customitems.stormitemy.Main;
import me.anarchiacore.customitems.stormitemy.books.D;

public class G
extends D
implements Listener {
    private final Random F = new Random();

    public G(Main main) {
        super(main, "Wytrzyma\u0142o\u015b\u0107 I", "wytrzymalosc", (List<String>)Arrays.asList((Object[])new String[]{"helmet", "chestplate", "leggings", "boots", "sword", "pickaxe", "axe", "shovel", "hoe"}), 0.0, 0, (List<String>)Arrays.asList((Object[])new String[]{"&7Wytrzyma\u0142o\u015b\u0107 I", "&7", "&2&lZakl\u0119cie specjalne!", "&7Mo\u017ce zosta\u0107 na\u0142o\u017cony na &auzbrojenie&7!", "&7Spowalnia niszczenie si\u0119 zbroi!", "&7"}));
        Bukkit.getPluginManager().registerEvents((Listener)this, (Plugin)main);
    }

    @Override
    protected List<Map<String, Object>> getDefaultEffects() {
        ArrayList arrayList = new ArrayList();
        return arrayList;
    }

    @Override
    protected Map<String, Object> getDefaultSoundConfig() {
        return null;
    }

    @Override
    protected FileConfiguration loadConfiguration() {
        FileConfiguration fileConfiguration = super.loadConfiguration();
        fileConfiguration.set("customModelData", (Object)9);
        try {
            this.saveCustomConfig((YamlConfiguration)fileConfiguration, this.configFile);
        }
        catch (Exception exception) {
            // empty catch block
        }
        if (!fileConfiguration.isSet("wytrzymalosc")) {
            fileConfiguration.set("wytrzymalosc.reduction_percent", (Object)50);
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
        String string = material.name().toLowerCase();
        return string.contains((CharSequence)"helmet") || string.contains((CharSequence)"chestplate") || string.contains((CharSequence)"leggings") || string.contains((CharSequence)"boots") || material == Material.ELYTRA;
    }

    @EventHandler(priority=EventPriority.HIGH, ignoreCancelled=true)
    public void onItemDamage(PlayerItemDamageEvent playerItemDamageEvent) {
        ItemStack itemStack = playerItemDamageEvent.getItem();
        if (itemStack != null && itemStack.hasItemMeta()) {
            ItemMeta itemMeta = itemStack.getItemMeta();
            NamespacedKey namespacedKey = new NamespacedKey((Plugin)this.plugin, "enchant_" + this.getIdentifier());
            if (itemMeta.getPersistentDataContainer().has(namespacedKey, PersistentDataType.DOUBLE)) {
                int n2 = this.config.getInt("wytrzymalosc.reduction_percent", 50);
                if (this.F.nextInt(100) < n2) {
                    playerItemDamageEvent.setCancelled(true);
                }
            }
        }
    }

    @Override
    public void handleEffect(ItemStack itemStack, Object ... objectArray) {
    }
}

