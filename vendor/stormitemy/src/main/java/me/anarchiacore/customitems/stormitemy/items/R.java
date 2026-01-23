/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.io.File
 *  java.io.IOException
 *  java.lang.Exception
 *  java.lang.IllegalArgumentException
 *  java.lang.Integer
 *  java.lang.Long
 *  java.lang.NumberFormatException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.util.ArrayList
 *  java.util.List
 *  java.util.Map
 *  java.util.UUID
 *  java.util.concurrent.ConcurrentHashMap
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.configuration.ConfigurationSection
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityResurrectEvent
 *  org.bukkit.event.entity.PlayerDeathEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.inventory.ItemFlag
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 *  org.bukkit.scheduler.BukkitRunnable
 */
package me.anarchiacore.customitems.stormitemy.items;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import me.anarchiacore.customitems.stormitemy.Main;
import me.anarchiacore.customitems.stormitemy.utils.color.A;

public class r
implements Listener {
    private final Plugin D;
    private ConfigurationSection A;
    private static final Map<UUID, Long> C = new ConcurrentHashMap();
    private static final long B = 10000L;

    public r(Plugin javaPlugin) {
        YamlConfiguration yamlConfiguration;
        File file;
        this.D = javaPlugin;
        javaPlugin.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)javaPlugin);
        File file2 = new File(javaPlugin.getDataFolder(), "items");
        if (!file2.exists()) {
            file2.mkdirs();
        }
        if (!(file = new File(file2, "totemulaskawienia.yml")).exists()) {
            try {
                file.createNewFile();
                yamlConfiguration = new YamlConfiguration();
                String string = "=== Konfiguracja przedmiotu 'Totem u\u0142askawienia' ===\nOpis: Plik konfiguracyjny okre\u015bla w\u0142a\u015bciwo\u015bci przedmiotu eventowego 'Totem u\u0142askawienia'.\n";
                yamlConfiguration.options().header(string);
                yamlConfiguration.options().copyHeader(true);
                yamlConfiguration.set("totem.material", (Object)"TOTEM_OF_UNDYING");
                yamlConfiguration.set("totem.name", (Object)"&5&lTotem u\u0142askawienia");
                ArrayList arrayList = new ArrayList();
                arrayList.add((Object)"&r");
                arrayList.add((Object)"&8 \u00bb &7Jest to przedmiot z:");
                arrayList.add((Object)"&8 \u00bb &feventu halloween (2023)");
                arrayList.add((Object)"&r");
                arrayList.add((Object)"&8 \u00bb &7Trzymaj\u0105c go po \u015bmierci,");
                arrayList.add((Object)"&8 \u00bb &7nie stracisz swoich przedmiot\u00f3w!");
                arrayList.add((Object)"&r");
                yamlConfiguration.set("totem.lore", (Object)arrayList);
                yamlConfiguration.set("totem.customModelData", (Object)1);
                yamlConfiguration.set("totem.enchantments", (Object)List.of((Object)""));
                yamlConfiguration.set("totem.flags", (Object)List.of((Object)"HIDE_ENCHANTS", (Object)"HIDE_UNBREAKABLE", (Object)"HIDE_ATTRIBUTES"));
                yamlConfiguration.set("totem.unbreakable", (Object)true);
                yamlConfiguration.set("totem.animation.enabled", (Object)true);
                yamlConfiguration.set("totem.animation.effects", (Object)List.of((Object)"REGENERATION:2:100", (Object)"ABSORPTION:1:100", (Object)"FIRE_RESISTANCE:0:800"));
                yamlConfiguration.set("totem.messages.consumer.title", (Object)"");
                yamlConfiguration.set("totem.messages.consumer.subtitle", (Object)"");
                yamlConfiguration.set("totem.messages.consumer.chatMessage", (Object)"");
                yamlConfiguration.set("totem.sounds.use.enabled", (Object)true);
                yamlConfiguration.set("totem.sounds.use.sound", (Object)"ITEM_TOTEM_USE");
                yamlConfiguration.set("totem.sounds.use.volume", (Object)1.0);
                yamlConfiguration.set("totem.sounds.use.pitch", (Object)1.0);
                yamlConfiguration.save(file);
            }
            catch (IOException iOException) {
                javaPlugin.getLogger().warning("Nie mo\u017cna utworzy\u0107 pliku totemulaskawienia.yml: " + iOException.getMessage());
            }
        }
        yamlConfiguration = YamlConfiguration.loadConfiguration((File)file);
        this.A = yamlConfiguration.getConfigurationSection("totem");
        Bukkit.getScheduler().runTaskTimer((Plugin)javaPlugin, this::A, 100L, 100L);
    }

    @EventHandler(priority=EventPriority.LOWEST)
    public void onPlayerDeath(PlayerDeathEvent playerDeathEvent) {
        if (this.D instanceof Main && ((Main)this.D).isItemDisabledByKey("totemulaskawienia")) {
            return;
        }
        Player player = playerDeathEvent.getEntity();
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        ItemStack itemStack2 = player.getInventory().getItemInOffHand();
        player.removeMetadata("totem_ulaskawienia_used", (Plugin)this.D);
        if (this.A(itemStack) || this.A(itemStack2)) {
            playerDeathEvent.setKeepInventory(true);
            playerDeathEvent.getDrops().clear();
            C.put((Object)player.getUniqueId(), (Object)System.currentTimeMillis());
            player.setMetadata("totem_ulaskawienia_used", (MetadataValue)new FixedMetadataValue((Plugin)this.D, (Object)true));
            if (this.A(itemStack)) {
                this.A(player, true);
            } else if (this.A(itemStack2)) {
                this.A(player, false);
            }
            if (this.A.getBoolean("animation.enabled", true)) {
                this.B(player);
            }
            this.A(player, "use");
            this.A(player);
            this.B();
        }
    }

    @EventHandler
    public void onEntityResurrect(EntityResurrectEvent entityResurrectEvent) {
        if (!(entityResurrectEvent.getEntity() instanceof Player)) {
            return;
        }
        Player player = (Player)entityResurrectEvent.getEntity();
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        ItemStack itemStack2 = player.getInventory().getItemInOffHand();
        if (this.A(itemStack) || this.A(itemStack2)) {
            entityResurrectEvent.setCancelled(true);
        }
    }

    private void A() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!player.hasMetadata("totem_ulaskawienia_used") || r.hasRecentlyUsedTotem(player.getUniqueId())) continue;
            player.removeMetadata("totem_ulaskawienia_used", (Plugin)this.D);
        }
    }

    private boolean A(ItemStack itemStack) {
        if (itemStack == null || !itemStack.hasItemMeta() || !itemStack.getItemMeta().hasDisplayName()) {
            return false;
        }
        String string = itemStack.getItemMeta().getDisplayName();
        String string2 = me.anarchiacore.customitems.stormitemy.utils.color.A.C(this.A.getString("name", "&5&lTotem u\u0142askawienia"));
        return string.equals((Object)string2);
    }

    private void A(Player player, boolean bl) {
        ItemStack itemStack;
        ItemStack itemStack2 = itemStack = bl ? player.getInventory().getItemInMainHand() : player.getInventory().getItemInOffHand();
        if (itemStack.getAmount() > 1) {
            itemStack.setAmount(itemStack.getAmount() - 1);
        } else if (bl) {
            player.getInventory().setItemInMainHand(null);
        } else {
            player.getInventory().setItemInOffHand(null);
        }
    }

    private void B(final Player player) {
        List list = this.A.getStringList("animation.effects");
        for (String string : list) {
            String[] stringArray = string.split(":");
            if (stringArray.length < 3) continue;
            try {
                PotionEffectType potionEffectType = PotionEffectType.getByName((String)stringArray[0].toUpperCase());
                if (potionEffectType == null) continue;
                int n2 = Integer.parseInt((String)stringArray[1]);
                int n3 = Integer.parseInt((String)stringArray[2]);
                player.addPotionEffect(new PotionEffect(potionEffectType, n3, n2, false, true));
            }
            catch (Exception exception) {
                this.D.getLogger().warning("B\u0142\u0105d parsowania efektu totemu: " + string);
            }
        }
        new BukkitRunnable(this){
            final /* synthetic */ r this$0;
            {
                this.this$0 = r2;
            }

            public void run() {
                final ItemStack itemStack = player.getInventory().getItemInMainHand();
                final ItemStack itemStack2 = player.getInventory().getItemInOffHand();
                ItemStack itemStack3 = new ItemStack(Material.TOTEM_OF_UNDYING);
                player.getInventory().setItemInMainHand(itemStack3);
                try {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 1, 1, false, false));
                    new BukkitRunnable(this){
                        final /* synthetic */ 1 this$1;
                        {
                            this.this$1 = var1_1;
                        }

                        public void run() {
                            this.this$1.player.getInventory().setItemInMainHand(itemStack);
                            this.this$1.player.getInventory().setItemInOffHand(itemStack2);
                        }
                    }.runTaskLater((Plugin)this.this$0.D, 3L);
                }
                catch (Exception exception) {
                    player.getInventory().setItemInMainHand(itemStack);
                    player.getInventory().setItemInOffHand(itemStack2);
                }
            }
        }.runTaskLater((Plugin)this.D, 1L);
    }

    private void A(Player player) {
        ConfigurationSection configurationSection = this.A.getConfigurationSection("messages.consumer");
        if (configurationSection == null) {
            return;
        }
        String string = configurationSection.getString("title", "");
        String string2 = configurationSection.getString("subtitle", "");
        String string3 = configurationSection.getString("chatMessage", "");
        if (!string.isEmpty() || !string2.isEmpty()) {
            player.sendTitle(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string), me.anarchiacore.customitems.stormitemy.utils.color.A.C(string2), 10, 40, 10);
        }
        if (!string3.isEmpty()) {
            player.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string3));
        }
    }

    public ItemStack getItem() {
        Material material;
        try {
            material = Material.valueOf((String)this.A.getString("material", "TOTEM_OF_UNDYING"));
        }
        catch (IllegalArgumentException illegalArgumentException) {
            material = Material.TOTEM_OF_UNDYING;
        }
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        String string = me.anarchiacore.customitems.stormitemy.utils.color.A.C(this.A.getString("name", "&5&lTotem u\u0142askawienia"));
        itemMeta.setDisplayName(string);
        if (this.A.contains("lore")) {
            List list = this.A.getStringList("lore");
            Object object = new ArrayList();
            for (String string2 : list) {
                object.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C(string2));
            }
            itemMeta.setLore((List)object);
        }
        if (this.A.contains("customModelData")) {
            itemMeta.setCustomModelData(Integer.valueOf((int)this.A.getInt("customModelData")));
        }
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

    public static boolean hasRecentlyUsedTotem(UUID uUID) {
        boolean bl;
        if (!C.containsKey((Object)uUID)) {
            return false;
        }
        long l2 = (Long)C.get((Object)uUID);
        boolean bl2 = bl = System.currentTimeMillis() - l2 < 10000L;
        if (!bl) {
            C.remove((Object)uUID);
        }
        return bl;
    }

    private void B() {
        long l2 = System.currentTimeMillis();
        C.entrySet().removeIf(entry -> l2 - (Long)entry.getValue() > 10000L);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent playerQuitEvent) {
        C.remove((Object)playerQuitEvent.getPlayer().getUniqueId());
    }

    public void reload() {
        try {
            YamlConfiguration yamlConfiguration;
            ConfigurationSection configurationSection;
            File file = new File(this.D.getDataFolder(), "items/totemulaskawienia.yml");
            if (file.exists() && (configurationSection = (yamlConfiguration = YamlConfiguration.loadConfiguration((File)file)).getConfigurationSection("totem")) != null) {
                this.A = configurationSection;
                this.D.getLogger().info("Prze\u0142adowano konfiguracj\u0119 TotemUlaskawienia");
            }
        }
        catch (Exception exception) {
            this.D.getLogger().warning("B\u0142\u0105d prze\u0142adowywania TotemUlaskawienia: " + exception.getMessage());
        }
    }

    public static void cleanup() {
        C.clear();
    }

    private void A(Player player, String string) {
        ConfigurationSection configurationSection = this.A.getConfigurationSection("sounds." + string);
        if (configurationSection == null || !configurationSection.getBoolean("enabled", true)) {
            return;
        }
        String string2 = configurationSection.getString("sound", "ITEM_TOTEM_USE");
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

