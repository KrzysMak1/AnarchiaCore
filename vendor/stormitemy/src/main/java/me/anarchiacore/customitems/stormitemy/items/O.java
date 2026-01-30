/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.io.File
 *  java.io.IOException
 *  java.lang.CharSequence
 *  java.lang.IllegalArgumentException
 *  java.lang.Integer
 *  java.lang.NumberFormatException
 *  java.lang.Object
 *  java.lang.String
 *  java.util.List
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.configuration.ConfigurationSection
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerFishEvent
 *  org.bukkit.event.player.PlayerFishEvent$State
 *  org.bukkit.inventory.ItemFlag
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.util.Vector
 */
package me.anarchiacore.customitems.stormitemy.items;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;
import me.anarchiacore.customitems.stormitemy.Main;
import me.anarchiacore.customitems.stormitemy.utils.color.A;

public class O
implements Listener {
    private final Plugin B;
    private final ConfigurationSection A;

    public O(Plugin plugin) {
        YamlConfiguration yamlConfiguration;
        File file;
        this.B = plugin;
        File file2 = new File(plugin.getDataFolder(), "items");
        if (!file2.exists()) {
            file2.mkdirs();
        }
        if (!(file = new File(file2, "wedkasurferka.yml")).exists()) {
            try {
                file.createNewFile();
                yamlConfiguration = new YamlConfiguration();
                String string = "=== Konfiguracja przedmiotu 'W\u0119dka surferka' ===\nOpis: Plik konfiguracyjny okre\u015bla w\u0142a\u015bciwo\u015bci przedmiotu eventowego 'W\u0119dka surferka'.\nDost\u0119pne zmienne (placeholdery):\n  {TIME}         - pozosta\u0142y czas cooldownu (w sekundach)\n";
                yamlConfiguration.options().header(string);
                yamlConfiguration.options().copyHeader(true);
                yamlConfiguration.set("wedkasurferka.material", (Object)"FISHING_ROD");
                yamlConfiguration.set("wedkasurferka.name", (Object)"&bW\u0119dka surferka");
                yamlConfiguration.set("wedkasurferka.lore", (Object)List.of((Object)"&r", (Object)"&8 \u00bb &7Jest to przedmiot z:", (Object)"&8 \u00bb &feventu wakacyjnego (2024)", (Object)"&r", (Object)"&8 \u00bb &7Ta &aniezwyk\u0142a &7w\u0119dka zapewnia ci", (Object)"&8 \u00bb &7zdumiewaj\u0105c\u0105 zdolno\u015b\u0107 przyci\u0105gania si\u0119", (Object)"&8 \u00bb &7do &bblok\u00f3w &7i &dgraczy&7, tworz\u0105c zupe\u0142nie", (Object)"&8 \u00bb &7nowe mo\u017cliwo\u015bci eksploracji i walki!", (Object)"&r"));
                yamlConfiguration.set("wedkasurferka.customModelData", (Object)1);
                yamlConfiguration.set("wedkasurferka.cooldown", (Object)15);
                yamlConfiguration.set("wedkasurferka.enchantments", (Object)List.of((Object)"DURABILITY:3"));
                yamlConfiguration.set("wedkasurferka.flags", (Object)List.of((Object)"HIDE_ENCHANTS", (Object)"HIDE_UNBREAKABLE", (Object)"HIDE_ATTRIBUTES"));
                yamlConfiguration.set("wedkasurferka.unbreakable", (Object)true);
                yamlConfiguration.set("wedkasurferka.knockback.forward", (Object)4.0);
                yamlConfiguration.set("wedkasurferka.knockback.upward", (Object)1.5);
                yamlConfiguration.set("wedkasurferka.messages.title", (Object)"&bW\u0118DKA SURFERKA");
                yamlConfiguration.set("wedkasurferka.messages.subtitle", (Object)"&7U\u017cy\u0142e\u015b &fw\u0119dki surferka&7!");
                yamlConfiguration.set("wedkasurferka.messages.chatMessage", (Object)"");
                yamlConfiguration.set("wedkasurferka.messages.cooldown", (Object)"");
                yamlConfiguration.set("wedkasurferka.sounds.use.enabled", (Object)true);
                yamlConfiguration.set("wedkasurferka.sounds.use.sound", (Object)"ENTITY_FISHING_BOBBER_RETRIEVE");
                yamlConfiguration.set("wedkasurferka.sounds.use.volume", (Object)1.0);
                yamlConfiguration.set("wedkasurferka.sounds.use.pitch", (Object)1.2);
                yamlConfiguration.save(file);
            }
            catch (IOException iOException) {
                iOException.printStackTrace();
            }
        }
        yamlConfiguration = YamlConfiguration.loadConfiguration((File)file);
        this.A = yamlConfiguration.getConfigurationSection("wedkasurferka");
        plugin.getServer().getPluginManager().registerEvents((Listener)this, plugin);
    }

    public ItemStack getItem() {
        List list;
        Material material;
        String string = this.A.getString("material", "FISHING_ROD");
        try {
            material = Material.valueOf((String)string);
        }
        catch (IllegalArgumentException illegalArgumentException) {
            material = Material.FISHING_ROD;
        }
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        String string2 = me.anarchiacore.customitems.stormitemy.utils.color.A.C(this.A.getString("name", "&b&lW\u0119dka surferka"));
        itemMeta.setDisplayName(string2);
        if (this.A.contains("lore")) {
            list = this.A.getStringList("lore");
            for (int i2 = 0; i2 < list.size(); ++i2) {
                list.set(i2, (Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C((String)list.get(i2)));
            }
            itemMeta.setLore(list);
        }
        if (this.A.contains("customModelData")) {
            itemMeta.setCustomModelData(Integer.valueOf((int)this.A.getInt("customModelData")));
        }
        if (this.A.contains("enchantments")) {
            list = this.A.getStringList("enchantments");
            for (String string3 : list) {
                Enchantment enchantment;
                String[] stringArray = string3.split(":");
                if (stringArray.length < 2) continue;
                String string4 = stringArray[0].trim().toUpperCase();
                int n2 = 1;
                try {
                    n2 = Integer.parseInt((String)stringArray[1].trim());
                }
                catch (NumberFormatException numberFormatException) {
                    // empty catch block
                }
                if ((enchantment = Enchantment.getByName((String)string4)) == null) continue;
                itemMeta.addEnchant(enchantment, n2, true);
            }
        }
        if (this.A.contains("flags")) {
            list = this.A.getStringList("flags");
            for (String string3 : list) {
                try {
                    itemMeta.addItemFlags(new ItemFlag[]{ItemFlag.valueOf((String)string3)});
                }
                catch (IllegalArgumentException illegalArgumentException) {}
            }
        }
        itemMeta.setUnbreakable(this.A.getBoolean("unbreakable", false));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private boolean A(ItemStack itemStack) {
        boolean bl;
        Material material;
        if (itemStack == null || !itemStack.hasItemMeta()) {
            return false;
        }
        String string = me.anarchiacore.customitems.stormitemy.utils.color.A.C(this.A.getString("name", "&b&lW\u0119dka surferka"));
        try {
            material = Material.valueOf((String)this.A.getString("material", "FISHING_ROD"));
        }
        catch (IllegalArgumentException illegalArgumentException) {
            material = Material.FISHING_ROD;
        }
        int n2 = this.A.getInt("customModelData", 0);
        boolean bl2 = bl = itemStack.getType() == material && itemStack.getItemMeta().getDisplayName().equals((Object)string);
        if (itemStack.getItemMeta().hasCustomModelData()) {
            bl = bl && itemStack.getItemMeta().getCustomModelData() == n2;
        }
        return bl;
    }

    @EventHandler
    public void onPlayerFish(PlayerFishEvent playerFishEvent) {
        if (playerFishEvent.getState() == PlayerFishEvent.State.FISHING) {
            return;
        }
        Player player = playerFishEvent.getPlayer();
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        ItemStack itemStack2 = player.getInventory().getItemInOffHand();
        boolean bl = this.A(itemStack);
        boolean bl2 = this.A(itemStack2);
        if (!bl && !bl2) {
            return;
        }
        if (this.B instanceof Main && ((Main)this.B).isItemDisabledByKey("wedkasurferka")) {
            return;
        }
        ItemStack itemStack3 = bl ? itemStack : itemStack2;
        int n2 = this.A.getInt("cooldown", 10) * 20;
        if (player.hasCooldown(itemStack3.getType())) {
            long l2 = player.getCooldown(itemStack3.getType()) / 20;
            String string = this.A.getString("messages.cooldown", "");
            if (!string.isEmpty()) {
                player.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string.replace((CharSequence)"{TIME}", (CharSequence)String.valueOf((long)l2))));
            }
            return;
        }
        player.setCooldown(itemStack3.getType(), n2);
        double d2 = this.A.getDouble("knockback.forward", 2.0);
        double d3 = this.A.getDouble("knockback.upward", 1.0);
        Vector vector = player.getLocation().getDirection();
        Vector vector2 = vector.multiply(d2).setY(d3);
        player.setVelocity(vector2);
        this.A(player, "use");
        String string = this.A.getString("messages.title", "");
        String string2 = this.A.getString("messages.subtitle", "");
        String string3 = this.A.getString("messages.chatMessage", "");
        if (!string.isEmpty() || !string2.isEmpty()) {
            player.sendTitle(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string), me.anarchiacore.customitems.stormitemy.utils.color.A.C(string2), 10, 40, 10);
        }
        if (!string3.isEmpty()) {
            player.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string3));
        }
    }

    private void A(Player player, String string) {
        ConfigurationSection configurationSection = this.A.getConfigurationSection("sounds." + string);
        if (configurationSection == null || !configurationSection.getBoolean("enabled", true)) {
            return;
        }
        String string2 = configurationSection.getString("sound", "ENTITY_FISHING_BOBBER_RETRIEVE");
        float f2 = (float)configurationSection.getDouble("volume", 1.0);
        float f3 = (float)configurationSection.getDouble("pitch", 1.0);
        try {
            Sound sound = Sound.valueOf((String)string2);
            player.playSound(player.getLocation(), sound, f2, f3);
        }
        catch (IllegalArgumentException illegalArgumentException) {
            player.playSound(player.getLocation(), string2, f2, f3);
        }
    }
}

