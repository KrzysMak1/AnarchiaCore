/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.io.File
 *  java.io.IOException
 *  java.lang.Boolean
 *  java.lang.CharSequence
 *  java.lang.Exception
 *  java.lang.IllegalArgumentException
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.util.ArrayList
 *  java.util.HashMap
 *  java.util.LinkedHashMap
 *  java.util.List
 *  java.util.Map
 *  java.util.UUID
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.GameMode
 *  org.bukkit.Material
 *  org.bukkit.NamespacedKey
 *  org.bukkit.Sound
 *  org.bukkit.configuration.ConfigurationSection
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.BlockPlaceEvent
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.event.inventory.InventoryCreativeEvent
 *  org.bukkit.inventory.ItemFlag
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.persistence.PersistentDataType
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package me.anarchiacore.customitems.stormitemy.items;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import me.anarchiacore.customitems.stormitemy.Main;
import me.anarchiacore.customitems.stormitemy.utils.color.A;

public class $A
implements Listener {
    private final Plugin C;
    private final ConfigurationSection B;
    private final NamespacedKey A;
    private final NamespacedKey D;

    public $A(Plugin javaPlugin) {
        YamlConfiguration yamlConfiguration;
        File file;
        this.C = javaPlugin;
        this.A = new NamespacedKey((Plugin)javaPlugin, "zatruty_olowek_id");
        this.D = new NamespacedKey((Plugin)javaPlugin, "zatruty_olowek_created_at");
        javaPlugin.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)javaPlugin);
        Bukkit.getScheduler().runTaskTimer((Plugin)javaPlugin, this::A, 6000L, 6000L);
        File file2 = new File(javaPlugin.getDataFolder(), "items");
        if (!file2.exists()) {
            file2.mkdirs();
        }
        if (!(file = new File(file2, "zatrutyolowek.yml")).exists()) {
            try {
                file.createNewFile();
                yamlConfiguration = new YamlConfiguration();
                String string = "=== Konfiguracja przedmiotu 'Zatruty o\u0142\u00f3wek' ===\nOpis: Plik konfiguracyjny okre\u015bla w\u0142a\u015bciwo\u015bci przedmiotu eventowego 'Zatruty o\u0142\u00f3wek'.\n";
                yamlConfiguration.options().header(string);
                yamlConfiguration.options().copyHeader(true);
                yamlConfiguration.set("zatrutyolowek.material", (Object)"LIME_CANDLE");
                yamlConfiguration.set("zatrutyolowek.name", (Object)"&a&lZatruty o\u0142\u00f3wek");
                yamlConfiguration.set("zatrutyolowek.lore", (Object)List.of((Object)"&r", (Object)"&8 \u00bb &7Jest to przedmiot zdobyty z", (Object)"&8 \u00bb &fwydarzenia szkolnego 2024&7!", (Object)"&r", (Object)"&8 \u00bb &7Po uderzeniu przeciwnika", (Object)"&8 \u00bb &7otrzymuje on &ctruj\u0105cy efekt&7!", (Object)"&r"));
                yamlConfiguration.set("zatrutyolowek.customModelData", (Object)14);
                yamlConfiguration.set("zatrutyolowek.cooldown", (Object)60);
                yamlConfiguration.set("zatrutyolowek.enchantments", (Object)List.of((Object)"DAMAGE_ALL:2", (Object)"KNOCKBACK:1"));
                yamlConfiguration.set("zatrutyolowek.flags", (Object)List.of((Object)"HIDE_ENCHANTS", (Object)"HIDE_ATTRIBUTES", (Object)"HIDE_UNBREAKABLE"));
                yamlConfiguration.set("zatrutyolowek.unbreakable", (Object)true);
                ArrayList arrayList = new ArrayList();
                LinkedHashMap linkedHashMap = new LinkedHashMap();
                linkedHashMap.put((Object)"particles", (Object)false);
                linkedHashMap.put((Object)"duration", (Object)30);
                linkedHashMap.put((Object)"ambient", (Object)false);
                linkedHashMap.put((Object)"type", (Object)"POISON");
                linkedHashMap.put((Object)"amplifier", (Object)1);
                arrayList.add((Object)linkedHashMap);
                LinkedHashMap linkedHashMap2 = new LinkedHashMap();
                linkedHashMap2.put((Object)"particles", (Object)false);
                linkedHashMap2.put((Object)"duration", (Object)30);
                linkedHashMap2.put((Object)"ambient", (Object)false);
                linkedHashMap2.put((Object)"type", (Object)"WEAKNESS");
                linkedHashMap2.put((Object)"amplifier", (Object)1);
                arrayList.add((Object)linkedHashMap2);
                yamlConfiguration.set("zatrutyolowek.effects", (Object)arrayList);
                yamlConfiguration.set("zatrutyolowek.messages.hitter.title", (Object)"&a&lZATRUTY O\u0141\u00d3WEK");
                yamlConfiguration.set("zatrutyolowek.messages.hitter.subtitle", (Object)"&7Zatru\u0142e\u015b gracza &f{TARGET}&7!");
                yamlConfiguration.set("zatrutyolowek.messages.hitter.chatMessage", (Object)"");
                yamlConfiguration.set("zatrutyolowek.messages.target.title", (Object)"&a&lZATRUTY O\u0141\u00d3WEK");
                yamlConfiguration.set("zatrutyolowek.messages.target.subtitle", (Object)"&7Zosta\u0142e\u015b zatruty przez &f{HITTER}&7!");
                yamlConfiguration.set("zatrutyolowek.messages.target.chatMessage", (Object)"");
                yamlConfiguration.set("zatrutyolowek.sounds.hit.enabled", (Object)true);
                yamlConfiguration.set("zatrutyolowek.sounds.hit.sound", (Object)"ENTITY_SPLASH_POTION_BREAK");
                yamlConfiguration.set("zatrutyolowek.sounds.hit.volume", (Object)1.0);
                yamlConfiguration.set("zatrutyolowek.sounds.hit.pitch", (Object)0.8);
                yamlConfiguration.save(file);
            }
            catch (IOException iOException) {
                iOException.printStackTrace();
            }
        }
        yamlConfiguration = YamlConfiguration.loadConfiguration((File)file);
        this.B = yamlConfiguration.getConfigurationSection("zatrutyolowek");
    }

    private void A() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            HashMap hashMap = new HashMap();
            for (int i2 = 0; i2 < 9; ++i2) {
                ItemStack itemStack = player.getInventory().getItem(i2);
                if (itemStack == null || !this.D(itemStack)) continue;
                if (!this.B(itemStack)) {
                    this.A(itemStack);
                    continue;
                }
                String string = this.C(itemStack);
                if (string == null) continue;
                if (hashMap.containsKey((Object)string)) {
                    this.A(itemStack);
                    continue;
                }
                hashMap.put((Object)string, (Object)i2);
            }
        }
    }

    private void A(ItemStack itemStack) {
        if (itemStack == null || !itemStack.hasItemMeta()) {
            return;
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (!this.D(itemStack)) {
            return;
        }
        String string = UUID.randomUUID().toString();
        itemMeta.getPersistentDataContainer().set(this.A, PersistentDataType.STRING, (Object)string);
        itemMeta.getPersistentDataContainer().set(this.D, PersistentDataType.LONG, (Object)System.currentTimeMillis());
        itemStack.setItemMeta(itemMeta);
    }

    private boolean D(ItemStack itemStack) {
        String string;
        if (itemStack == null || !itemStack.hasItemMeta()) {
            return false;
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (!itemMeta.hasDisplayName()) {
            return false;
        }
        String string2 = ChatColor.stripColor((String)itemMeta.getDisplayName());
        if (!string2.equals((Object)(string = ChatColor.stripColor((String)me.anarchiacore.customitems.stormitemy.utils.color.A.C(this.B.getString("name", "&a&lZatruty o\u0142\u00f3wek")))))) {
            return false;
        }
        if (this.B.contains("customModelData")) {
            if (itemMeta.hasCustomModelData()) {
                if (itemMeta.getCustomModelData() != this.B.getInt("customModelData", 0)) {
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }

    private String C(ItemStack itemStack) {
        if (!this.B(itemStack)) {
            return null;
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        return (String)itemMeta.getPersistentDataContainer().get(this.A, PersistentDataType.STRING);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent entityDamageByEntityEvent) {
        String string;
        String string2;
        Object object;
        Object object2;
        Main main;
        Material material;
        if (!(entityDamageByEntityEvent.getDamager() instanceof Player) || !(entityDamageByEntityEvent.getEntity() instanceof Player)) {
            return;
        }
        if (this.C instanceof Main && ((Main)this.C).isItemDisabledByKey("zatrutyolowek")) {
            return;
        }
        Player player = (Player)entityDamageByEntityEvent.getDamager();
        Player player2 = (Player)entityDamageByEntityEvent.getEntity();
        try {
            material = Material.valueOf((String)this.B.getString("material", "LIME_CANDLE"));
        }
        catch (IllegalArgumentException illegalArgumentException) {
            material = Material.LIME_CANDLE;
        }
        if (player.getCooldown(material) > 0) {
            return;
        }
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (itemStack == null || itemStack.getType() != material) {
            return;
        }
        if (!itemStack.hasItemMeta()) {
            return;
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        String string3 = me.anarchiacore.customitems.stormitemy.utils.color.A.C(this.B.getString("name", "&a&lZatruty o\u0142\u00f3wek"));
        if (!itemMeta.hasDisplayName() || !itemMeta.getDisplayName().equals((Object)string3)) {
            return;
        }
        boolean bl = false;
        boolean bl2 = false;
        try {
            if (this.C instanceof Main) {
                main = (Main)this.C;
                if (main.isPlayerInBlockedRegion(player)) {
                    bl = true;
                }
                if (main.isPlayerInBlockedRegion(player2)) {
                    bl2 = true;
                }
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        if (bl || bl2) {
            me.anarchiacore.customitems.stormitemy.utils.language.A.A(player);
            return;
        }
        if (this.B.contains("effects")) {
            main = this.B.getList("effects");
            if (main != null) {
                object2 = main.iterator();
                while (object2.hasNext()) {
                    Map map;
                    Object object3 = object2.next();
                    if (!(object3 instanceof Map) || (object = PotionEffectType.getByName((String)(string2 = (map = (Map)object3).get((Object)"type").toString()).toUpperCase())) == null) continue;
                    int n2 = Integer.parseInt((String)map.get((Object)"duration").toString());
                    int n3 = n2 * 20;
                    int n4 = Integer.parseInt((String)map.get((Object)"amplifier").toString());
                    boolean bl3 = Boolean.parseBoolean((String)map.get((Object)"ambient").toString());
                    boolean bl4 = Boolean.parseBoolean((String)map.get((Object)"particles").toString());
                    player2.addPotionEffect(new PotionEffect(object, n3, n4, bl3, bl4));
                }
            }
        } else {
            int n5 = this.B.getInt("effect.duration", 30);
            int n6 = n5 * 20;
            int n7 = this.B.getInt("effect.poison.level", 2) - 1;
            int n8 = this.B.getInt("effect.weakness.level", 2) - 1;
            player2.addPotionEffect(new PotionEffect(PotionEffectType.POISON, n6, n7));
            player2.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, n6, n8));
        }
        int n9 = this.B.getInt("cooldown", 10);
        player.setCooldown(material, n9 * 20);
        object2 = me.anarchiacore.customitems.stormitemy.utils.color.A.C(this.B.getString("messages.hitter.title", "").replace((CharSequence)"{TARGET}", (CharSequence)player2.getName()));
        String string4 = me.anarchiacore.customitems.stormitemy.utils.color.A.C(this.B.getString("messages.hitter.subtitle", "").replace((CharSequence)"{TARGET}", (CharSequence)player2.getName()));
        if (!object2.isEmpty() || !string4.isEmpty()) {
            player.sendTitle((String)object2, string4, 10, 70, 20);
        }
        String string5 = me.anarchiacore.customitems.stormitemy.utils.color.A.C(this.B.getString("messages.target.title", "").replace((CharSequence)"{HITTER}", (CharSequence)player.getName()));
        string2 = me.anarchiacore.customitems.stormitemy.utils.color.A.C(this.B.getString("messages.target.subtitle", "").replace((CharSequence)"{HITTER}", (CharSequence)player.getName()));
        if (!string5.isEmpty() || !string2.isEmpty()) {
            player2.sendTitle(string5, string2, 10, 70, 20);
        }
        if (!(object = me.anarchiacore.customitems.stormitemy.utils.color.A.C(this.B.getString("messages.hitter.chatMessage", ""))).isEmpty()) {
            player.sendMessage(object.replace((CharSequence)"{TARGET}", (CharSequence)player2.getName()));
        }
        if (!(string = me.anarchiacore.customitems.stormitemy.utils.color.A.C(this.B.getString("messages.target.chatMessage", ""))).isEmpty()) {
            player2.sendMessage(string.replace((CharSequence)"{HITTER}", (CharSequence)player.getName()));
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent blockPlaceEvent) {
        ItemStack itemStack = blockPlaceEvent.getItemInHand();
        if (itemStack != null && this.B(itemStack)) {
            blockPlaceEvent.setCancelled(true);
        }
    }

    private boolean B(ItemStack itemStack) {
        if (itemStack != null && itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName()) {
            String string;
            ItemMeta itemMeta = itemStack.getItemMeta();
            String string2 = itemMeta.getDisplayName();
            boolean bl = string2.equals((Object)(string = me.anarchiacore.customitems.stormitemy.utils.color.A.C(this.B.getString("name", "&a&lZatruty o\u0142\u00f3wek"))));
            if (!bl) {
                return false;
            }
            if (this.B.contains("customModelData")) {
                if (itemMeta.hasCustomModelData()) {
                    if (itemMeta.getCustomModelData() != this.B.getInt("customModelData", 0)) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
            return itemMeta.getPersistentDataContainer().has(this.A, PersistentDataType.STRING);
        }
        return false;
    }

    @EventHandler
    public void onInventoryCreative(InventoryCreativeEvent inventoryCreativeEvent) {
        if (!(inventoryCreativeEvent.getWhoClicked() instanceof Player)) {
            return;
        }
        Player player = (Player)inventoryCreativeEvent.getWhoClicked();
        if (player.getGameMode() != GameMode.CREATIVE) {
            return;
        }
        ItemStack itemStack = inventoryCreativeEvent.getCursor();
        ItemStack itemStack2 = inventoryCreativeEvent.getCurrentItem();
        if (this.D(itemStack2) && itemStack != null && itemStack.getType() != Material.AIR) {
            this.A(itemStack2);
        }
        if (this.D(itemStack)) {
            this.A(itemStack);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent inventoryClickEvent) {
        if (!(inventoryClickEvent.getWhoClicked() instanceof Player)) {
            return;
        }
        Player player = (Player)inventoryClickEvent.getWhoClicked();
        if (player.getGameMode() == GameMode.CREATIVE) {
            ItemStack itemStack = inventoryClickEvent.getCursor();
            ItemStack itemStack2 = inventoryClickEvent.getCurrentItem();
            if (this.D(itemStack2) && itemStack != null && itemStack.getType() != Material.AIR) {
                this.A(itemStack2);
            }
            if (this.D(itemStack)) {
                this.A(itemStack);
            }
        }
    }

    public ItemStack getItem() {
        Object object;
        Material material;
        try {
            material = Material.valueOf((String)this.B.getString("material", "LIME_CANDLE"));
        }
        catch (IllegalArgumentException illegalArgumentException) {
            material = Material.LIME_CANDLE;
        }
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        String string = me.anarchiacore.customitems.stormitemy.utils.color.A.C(this.B.getString("name", "&a&lZatruty o\u0142\u00f3wek"));
        itemMeta.setDisplayName(string);
        if (this.B.contains("lore")) {
            object = this.B.getStringList("lore");
            Object object2 = new ArrayList();
            for (String string2 : object) {
                object2.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C(string2));
            }
            itemMeta.setLore((List)object2);
        }
        if (this.B.contains("customModelData")) {
            itemMeta.setCustomModelData(Integer.valueOf((int)this.B.getInt("customModelData")));
        }
        if (this.B.contains("flags")) {
            for (Object object2 : this.B.getStringList("flags")) {
                try {
                    itemMeta.addItemFlags(new ItemFlag[]{ItemFlag.valueOf((String)object2)});
                }
                catch (IllegalArgumentException illegalArgumentException) {}
            }
        }
        if (this.B.contains("enchantments")) {
            for (Object object2 : this.B.getStringList("enchantments")) {
                String string2;
                String[] stringArray = object2.split(":");
                if (stringArray.length != 2) continue;
                string2 = stringArray[0].toUpperCase();
                int n2 = Integer.parseInt((String)stringArray[1]);
                Enchantment enchantment = Enchantment.getByName((String)string2);
                if (enchantment == null) continue;
                itemMeta.addEnchant(enchantment, n2, true);
            }
        }
        if (this.B.contains("unbreakable")) {
            itemMeta.setUnbreakable(this.B.getBoolean("unbreakable"));
        }
        object = UUID.randomUUID().toString();
        itemMeta.getPersistentDataContainer().set(this.A, PersistentDataType.STRING, object);
        itemMeta.getPersistentDataContainer().set(this.D, PersistentDataType.LONG, (Object)System.currentTimeMillis());
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private void A(Player player, String string) {
        ConfigurationSection configurationSection = this.B.getConfigurationSection("sounds." + string);
        if (configurationSection == null || !configurationSection.getBoolean("enabled", true)) {
            return;
        }
        String string2 = configurationSection.getString("sound", "ENTITY_SPLASH_POTION_BREAK");
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

