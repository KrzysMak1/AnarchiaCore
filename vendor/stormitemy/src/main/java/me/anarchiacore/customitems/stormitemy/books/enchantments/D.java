/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Exception
 *  java.lang.IllegalArgumentException
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.Override
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.Arrays
 *  java.util.Collection
 *  java.util.HashSet
 *  java.util.List
 *  java.util.Map
 *  java.util.Random
 *  java.util.Set
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.NamespacedKey
 *  org.bukkit.block.Block
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.BlockBreakEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.persistence.PersistentDataType
 *  org.bukkit.plugin.Plugin
 */
package me.anarchiacore.customitems.stormitemy.books.enchantments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import me.anarchiacore.customitems.stormitemy.Main;
import me.anarchiacore.customitems.stormitemy.utils.color.A;

public class D
extends me.anarchiacore.customitems.stormitemy.books.D
implements Listener {
    private final Random A = new Random();
    private final Set<Material> B = this.A();

    public D(Main main) {
        super(main, "Dodatkowy Drop I", "dodatkowy_drop", (List<String>)Arrays.asList((Object[])new String[]{"pickaxe", "axe", "shovel", "sword"}), 0.1, 0, (List<String>)Arrays.asList((Object[])new String[]{"&7Dodatkowy Drop I", "&7", "&2&lZakl\u0119cie specjalne!", "&7Mo\u017ce zosta\u0107 na\u0142o\u017cony na &anarz\u0119dzia&7!", "&7Daje szans\u0119 na dodatkowy drop z blok\u00f3w.", "&7"}));
        Bukkit.getPluginManager().registerEvents((Listener)this, (Plugin)main);
    }

    private Set<Material> A() {
        HashSet hashSet = new HashSet();
        hashSet.add((Object)Material.COAL_ORE);
        hashSet.add((Object)Material.IRON_ORE);
        hashSet.add((Object)Material.GOLD_ORE);
        hashSet.add((Object)Material.DIAMOND_ORE);
        hashSet.add((Object)Material.EMERALD_ORE);
        hashSet.add((Object)Material.LAPIS_ORE);
        hashSet.add((Object)Material.REDSTONE_ORE);
        hashSet.add((Object)Material.NETHER_QUARTZ_ORE);
        hashSet.add((Object)Material.NETHER_GOLD_ORE);
        try {
            hashSet.add((Object)Material.DEEPSLATE_COAL_ORE);
            hashSet.add((Object)Material.DEEPSLATE_IRON_ORE);
            hashSet.add((Object)Material.DEEPSLATE_GOLD_ORE);
            hashSet.add((Object)Material.DEEPSLATE_DIAMOND_ORE);
            hashSet.add((Object)Material.DEEPSLATE_EMERALD_ORE);
            hashSet.add((Object)Material.DEEPSLATE_LAPIS_ORE);
            hashSet.add((Object)Material.DEEPSLATE_REDSTONE_ORE);
            hashSet.add((Object)Material.COPPER_ORE);
            hashSet.add((Object)Material.DEEPSLATE_COPPER_ORE);
            hashSet.add((Object)Material.ANCIENT_DEBRIS);
        }
        catch (IllegalArgumentException illegalArgumentException) {
            // empty catch block
        }
        return hashSet;
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
        fileConfiguration.set("customModelData", (Object)8);
        try {
            this.saveCustomConfig((YamlConfiguration)fileConfiguration, this.configFile);
        }
        catch (Exception exception) {
            // empty catch block
        }
        if (!fileConfiguration.isSet("additional_drop") || !fileConfiguration.isSet("messages.title")) {
            fileConfiguration.set("additional_drop.drop_percent", (Object)50);
            fileConfiguration.set("additional_drop.max_extra_drops", (Object)3);
            fileConfiguration.set("messages.title", (Object)"&e&lDODATKOWY DROP");
            fileConfiguration.set("messages.subtitle", (Object)"&7Otrzyma\u0142e\u015b &edodatkowe przedmioty&7!");
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

    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
    public void onBlockBreak(BlockBreakEvent blockBreakEvent) {
        Player player = blockBreakEvent.getPlayer();
        Block block = blockBreakEvent.getBlock();
        if (!this.B.contains((Object)block.getType())) {
            return;
        }
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (itemStack == null || !itemStack.hasItemMeta() || !this.D(itemStack.getType())) {
            return;
        }
        NamespacedKey namespacedKey = new NamespacedKey((Plugin)this.plugin, "enchant_" + this.getIdentifier());
        if (!itemStack.getItemMeta().getPersistentDataContainer().has(namespacedKey, PersistentDataType.DOUBLE)) {
            return;
        }
        Collection collection = block.getDrops(itemStack, (Entity)player);
        if (collection.isEmpty()) {
            return;
        }
        int n2 = this.config.getInt("additional_drop.drop_percent", 25);
        ArrayList arrayList = new ArrayList();
        for (ItemStack itemStack2 : collection) {
            ItemStack itemStack3;
            int n3 = itemStack2.getAmount();
            double d2 = (double)n3 * ((double)n2 / 100.0);
            if (!(d2 >= 0.01)) continue;
            int n4 = (int)Math.floor((double)d2);
            double d3 = d2 - (double)n4;
            if (n4 > 0) {
                itemStack3 = itemStack2.clone();
                itemStack3.setAmount(n4);
                arrayList.add((Object)itemStack3);
            }
            if (!(d3 > 0.0) || !(this.A.nextDouble() < d3)) continue;
            itemStack3 = itemStack2.clone();
            itemStack3.setAmount(1);
            arrayList.add((Object)itemStack3);
        }
        if (!arrayList.isEmpty()) {
            Bukkit.getScheduler().runTask((Plugin)this.plugin, () -> this.A((List)arrayList, block, player));
        }
    }

    private void B(Player player) {
        String string = this.config.getString("messages.title", "");
        String string2 = this.config.getString("messages.subtitle", "");
        String string3 = this.config.getString("messages.chat", "");
        if (!string.isEmpty() || !string2.isEmpty()) {
            player.sendTitle(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string), me.anarchiacore.customitems.stormitemy.utils.color.A.C(string2), 5, 30, 10);
        }
        if (!string3.isEmpty()) {
            player.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string3));
        }
    }

    private boolean D(Material material) {
        return material == Material.WOODEN_PICKAXE || material == Material.STONE_PICKAXE || material == Material.IRON_PICKAXE || material == Material.GOLDEN_PICKAXE || material == Material.DIAMOND_PICKAXE || material == Material.NETHERITE_PICKAXE;
    }

    @Override
    public void handleEffect(ItemStack itemStack, Object ... objectArray) {
    }

    private /* synthetic */ void A(List list, Block block, Player player) {
        for (ItemStack itemStack : list) {
            block.getWorld().dropItemNaturally(block.getLocation(), itemStack);
        }
        this.B(player);
    }
}

