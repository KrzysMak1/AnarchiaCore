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
 *  java.util.Map
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
 *  org.bukkit.event.player.PlayerItemConsumeEvent
 *  org.bukkit.inventory.EquipmentSlot
 *  org.bukkit.inventory.ItemFlag
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 *  org.bukkit.scheduler.BukkitRunnable
 */
package me.anarchiacore.customitems.stormitemy.items;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import me.anarchiacore.customitems.stormitemy.Main;
import me.anarchiacore.customitems.stormitemy.utils.color.A;

public class D
implements Listener {
    private final Plugin B;
    private final ConfigurationSection A;

    public D(Plugin plugin) {
        YamlConfiguration yamlConfiguration;
        File file;
        this.B = plugin;
        File file2 = new File(plugin.getDataFolder(), "items");
        if (!file2.exists()) {
            file2.mkdirs();
        }
        if (!(file = new File(file2, "piernik.yml")).exists()) {
            try {
                file.createNewFile();
                yamlConfiguration = new YamlConfiguration();
                String string = "=== Konfiguracja przedmiotu 'Piernik' ===\nOpis: Plik konfiguracyjny okre\u015bla w\u0142a\u015bciwo\u015bci przedmiotu eventowego 'Piernik'.\n";
                yamlConfiguration.options().header(string);
                yamlConfiguration.options().copyHeader(true);
                yamlConfiguration.set("piernik.material", (Object)"COOKIE");
                yamlConfiguration.set("piernik.name", (Object)"&6&lPiernik");
                yamlConfiguration.set("piernik.lore", (Object)List.of((Object)"", (Object)"&8 \u00bb &7Jest to przedmiot z:", (Object)"&8 \u00bb &feventu \u015bwi\u0105tecznego (2024)", (Object)"", (Object)"&8 \u00bb &7Po zjedzeniu &epiernika &7otrzymujesz", (Object)"&8 \u00bb &7efekt &aszybko\u015bci kopania 10&7!", (Object)""));
                yamlConfiguration.set("piernik.customModelData", (Object)346521);
                yamlConfiguration.set("piernik.cooldown", (Object)70);
                yamlConfiguration.set("piernik.enchantments", (Object)List.of((Object)"DURABILITY:1"));
                yamlConfiguration.set("piernik.flags", (Object)List.of((Object)"HIDE_ENCHANTS", (Object)"HIDE_UNBREAKABLE", (Object)"HIDE_ATTRIBUTES"));
                yamlConfiguration.set("piernik.unbreakable", (Object)true);
                yamlConfiguration.set("piernik.effects", (Object)List.of((Object)Map.of((Object)"type", (Object)"FAST_DIGGING", (Object)"duration", (Object)200, (Object)"amplifier", (Object)9)));
                yamlConfiguration.set("piernik.messages.consumer.title", (Object)"&6&lPIERNIK");
                yamlConfiguration.set("piernik.messages.consumer.subtitle", (Object)"&7Zjad\u0142e\u015b &fpiernik&7!");
                yamlConfiguration.set("piernik.messages.consumer.chatMessage", (Object)"");
                yamlConfiguration.set("piernik.messages.cooldown", (Object)"");
                yamlConfiguration.set("piernik.sounds.enabled", (Object)false);
                yamlConfiguration.set("piernik.sounds.activation.sound", (Object)"");
                yamlConfiguration.set("piernik.sounds.activation.volume", (Object)1.0);
                yamlConfiguration.set("piernik.sounds.activation.pitch", (Object)1.0);
                yamlConfiguration.save(file);
            }
            catch (IOException iOException) {
                // empty catch block
            }
        }
        yamlConfiguration = YamlConfiguration.loadConfiguration((File)file);
        this.A = yamlConfiguration.getConfigurationSection("piernik");
        Bukkit.getPluginManager().registerEvents((Listener)this, plugin);
    }

    private void A(Player player) {
        try {
            boolean bl = this.A.getBoolean("sounds.enabled", false);
            if (!bl) {
                return;
            }
            String string = this.A.getString("sounds.activation.sound", "");
            if (string == null || string.trim().isEmpty()) {
                return;
            }
            Sound sound = Sound.valueOf((String)string);
            float f2 = (float)this.A.getDouble("sounds.activation.volume", 1.0);
            float f3 = (float)this.A.getDouble("sounds.activation.pitch", 1.0);
            player.playSound(player.getLocation(), sound, f2, f3);
        }
        catch (Exception exception) {
            this.B.getLogger().warning("B\u0142\u0105d podczas odtwarzania d\u017awi\u0119ku dla Piernik: " + exception.getMessage());
        }
    }

    @EventHandler(priority=EventPriority.HIGH)
    public void onPlayerInteract(PlayerInteractEvent playerInteractEvent) {
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
        if (this.B instanceof Main && ((Main)this.B).isItemDisabledByKey("piernik")) {
            return;
        }
        String string = ChatColor.stripColor((String)itemStack.getItemMeta().getDisplayName());
        int n2 = itemStack.getItemMeta().hasCustomModelData() ? itemStack.getItemMeta().getCustomModelData() : -1;
        int n3 = this.A.getInt("customModelData", 346521);
        String string2 = ChatColor.stripColor((String)me.anarchiacore.customitems.stormitemy.utils.color.A.C(this.A.getString("name", "&6&lPiernik")));
        if (string.equals((Object)string2) && n2 == n3) {
            boolean bl;
            Material material = itemStack.getType();
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
            int n4 = player.getFoodLevel();
            boolean bl2 = bl = n4 >= 20;
            if (bl) {
                player.setFoodLevel(19);
            }
        }
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public void onPlayerConsumePiernik(PlayerItemConsumeEvent playerItemConsumeEvent) {
        final Player player = playerItemConsumeEvent.getPlayer();
        ItemStack itemStack = playerItemConsumeEvent.getItem();
        if (itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName()) {
            String string = ChatColor.stripColor((String)itemStack.getItemMeta().getDisplayName());
            int n2 = itemStack.getItemMeta().hasCustomModelData() ? itemStack.getItemMeta().getCustomModelData() : -1;
            int n3 = this.A.getInt("customModelData", 346521);
            String string2 = ChatColor.stripColor((String)me.anarchiacore.customitems.stormitemy.utils.color.A.C(this.A.getString("name", "&6&lPiernik")));
            if (string.equals((Object)string2) && n2 == n3) {
                Object object;
                String string3;
                Object object22;
                Material material = itemStack.getType();
                this.A(player);
                int n4 = this.A.getInt("cooldown", 30) * 20;
                player.setCooldown(material, n4);
                List list = this.A.getMapList("effects");
                for (Object object22 : list) {
                    string3 = object22.get((Object)"type").toString();
                    object = PotionEffectType.getByName((String)string3);
                    int n5 = Integer.parseInt((String)object22.get((Object)"duration").toString());
                    int n6 = Integer.parseInt((String)object22.get((Object)"amplifier").toString());
                    if (object == null) continue;
                    player.addPotionEffect(new PotionEffect(object, n5, n6));
                }
                player.playSound(player.getLocation(), "minecraft:entity.chicken.hurt", 1.0f, 1.0f);
                Iterator iterator = this.A.getConfigurationSection("messages.consumer");
                object22 = iterator.getString("title", "");
                string3 = iterator.getString("subtitle", "");
                if (!object22.isEmpty() || !string3.isEmpty()) {
                    player.sendTitle(me.anarchiacore.customitems.stormitemy.utils.color.A.C((String)object22), me.anarchiacore.customitems.stormitemy.utils.color.A.C(string3), 10, 70, 20);
                }
                if (!(object = iterator.getString("chatMessage", "")).isEmpty()) {
                    player.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C((String)object));
                }
                new BukkitRunnable(this){
                    final /* synthetic */ d this$0;
                    {
                        this.this$0 = d2;
                    }

                    public void run() {
                        if (player.getFoodLevel() >= 20) {
                            player.setFoodLevel(20);
                        }
                    }
                }.runTaskLater(this.B, 1L);
            }
        }
    }

    public ItemStack getItem() {
        Material material;
        try {
            material = Material.valueOf((String)this.A.getString("material", "COOKIE"));
        }
        catch (IllegalArgumentException illegalArgumentException) {
            material = Material.COOKIE;
        }
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        String string = me.anarchiacore.customitems.stormitemy.utils.color.A.C(this.A.getString("name", "&6&lPiernik"));
        itemMeta.setDisplayName(string);
        if (this.A.contains("lore")) {
            List list = this.A.getStringList("lore");
            Object object = new ArrayList();
            for (String string2 : list) {
                object.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C(string2));
            }
            itemMeta.setLore((List)object);
        }
        itemMeta.setCustomModelData(Integer.valueOf((int)this.A.getInt("customModelData", 346521)));
        if (this.A.contains("enchantments")) {
            for (Object object : this.A.getStringList("enchantments")) {
                Enchantment enchantment;
                String string2;
                String[] stringArray = object.split(":");
                if (stringArray.length < 2) continue;
                string2 = stringArray[0].trim().toUpperCase();
                int n2 = 1;
                try {
                    n2 = Integer.parseInt((String)stringArray[1].trim());
                }
                catch (NumberFormatException numberFormatException) {
                    // empty catch block
                }
                if ((enchantment = Enchantment.getByName((String)string2)) == null) continue;
                itemMeta.addEnchant(enchantment, n2, true);
            }
        }
        if (this.A.contains("flags")) {
            for (Object object : this.A.getStringList("flags")) {
                try {
                    itemMeta.addItemFlags(new ItemFlag[]{ItemFlag.valueOf((String)object)});
                }
                catch (IllegalArgumentException illegalArgumentException) {}
            }
        }
        itemMeta.setUnbreakable(this.A.getBoolean("unbreakable", false));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}

