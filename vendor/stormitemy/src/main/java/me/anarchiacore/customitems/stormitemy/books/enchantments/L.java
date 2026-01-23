/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Boolean
 *  java.lang.CharSequence
 *  java.lang.Exception
 *  java.lang.Integer
 *  java.lang.Number
 *  java.lang.NumberFormatException
 *  java.lang.Object
 *  java.lang.Override
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.Arrays
 *  java.util.HashMap
 *  java.util.List
 *  java.util.Map
 *  java.util.Set
 *  java.util.UUID
 *  java.util.WeakHashMap
 *  java.util.concurrent.ConcurrentHashMap
 *  org.bukkit.Bukkit
 *  org.bukkit.NamespacedKey
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.persistence.PersistentDataType
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.scheduler.BukkitTask
 */
package me.anarchiacore.customitems.stormitemy.books.enchantments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import me.anarchiacore.customitems.stormitemy.Main;
import me.anarchiacore.customitems.stormitemy.books.D;

public class L
extends D {
    private final Map<UUID, BukkitTask> K = new WeakHashMap();
    private final Map<UUID, BukkitTask> M = new ConcurrentHashMap();
    private final Set<UUID> L = ConcurrentHashMap.newKeySet();

    public L(Main main) {
        super(main, "Niepodpalenie", "niepodpalenie", (List<String>)Arrays.asList((Object[])new String[]{"helmet"}), 1.0, 0, (List<String>)Arrays.asList((Object[])new String[]{"&7Niepodpalenie I", "&7", "&2&lZakl\u0119cie specjalnie!", "&7Mo\u017ce zosta\u0107 na\u0142o\u017cony na &ahe\u0142m&7!", "&7Otrzymujesz sta\u0142y efekt odporno\u015bci na ogie\u0144.", "&7"}));
        new BukkitRunnable(){

            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    L.this.N(player);
                }
                L.this.M.entrySet().removeIf(entry -> {
                    Player player = Bukkit.getPlayer((UUID)((UUID)entry.getKey()));
                    if (player == null || !player.isOnline()) {
                        BukkitTask bukkitTask = (BukkitTask)entry.getValue();
                        if (bukkitTask != null) {
                            bukkitTask.cancel();
                        }
                        return true;
                    }
                    return false;
                });
            }
        }.runTaskTimer((Plugin)main, 20L, 60L);
    }

    @Override
    protected List<Map<String, Object>> getDefaultEffects() {
        ArrayList arrayList = new ArrayList();
        HashMap hashMap = new HashMap();
        hashMap.put((Object)"type", (Object)"FIRE_RESISTANCE");
        hashMap.put((Object)"duration", (Object)7);
        hashMap.put((Object)"amplifier", (Object)0);
        hashMap.put((Object)"particles", (Object)false);
        hashMap.put((Object)"ambient", (Object)true);
        hashMap.put((Object)"icon", (Object)true);
        arrayList.add((Object)hashMap);
        return arrayList;
    }

    @Override
    protected Map<String, Object> getDefaultSoundConfig() {
        return null;
    }

    private void N(Player player) {
        ItemStack itemStack = player.getInventory().getHelmet();
        UUID uUID = player.getUniqueId();
        boolean bl = false;
        if (itemStack != null && itemStack.hasItemMeta()) {
            ItemMeta itemMeta = itemStack.getItemMeta();
            NamespacedKey namespacedKey = new NamespacedKey((Plugin)this.plugin, "enchant_" + this.getIdentifier());
            bl = itemMeta.getPersistentDataContainer().has(namespacedKey, PersistentDataType.DOUBLE);
        }
        if (bl) {
            this.P(player);
            this.L.add((Object)uUID);
        } else {
            this.O(player);
        }
    }

    private void P(Player player) {
        List<Map<?, ?>> list = this.getEffects();
        for (Map map : list) {
            String string = String.valueOf((Object)map.get((Object)"type"));
            if (!string.equals((Object)"FIRE_RESISTANCE")) continue;
            int n2 = this.E(map, "duration", 7) * 20;
            int n3 = this.E(map, "amplifier", 0);
            boolean bl = this.E(map, "particles", false);
            boolean bl2 = this.E(map, "ambient", true);
            boolean bl3 = this.E(map, "icon", true);
            player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, n2, n3, bl2, bl, bl3), true);
        }
    }

    private void O(Player player) {
        UUID uUID = player.getUniqueId();
        boolean bl = this.L.contains((Object)uUID);
        BukkitTask bukkitTask = (BukkitTask)this.M.remove((Object)uUID);
        if (bukkitTask != null) {
            bukkitTask.cancel();
        }
        this.L.remove((Object)uUID);
        if (player.hasPotionEffect(PotionEffectType.FIRE_RESISTANCE)) {
            ItemStack itemStack = player.getInventory().getHelmet();
            boolean bl2 = false;
            if (itemStack != null && itemStack.hasItemMeta()) {
                ItemMeta itemMeta = itemStack.getItemMeta();
                NamespacedKey namespacedKey = new NamespacedKey((Plugin)this.plugin, "enchant_" + this.getIdentifier());
                bl2 = itemMeta.getPersistentDataContainer().has(namespacedKey, PersistentDataType.DOUBLE);
            }
            if (!bl2 && bl) {
                player.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
            }
        }
    }

    private void C(Player player, final int n2, final int n3, final boolean bl, final boolean bl2, final boolean bl3) {
        final UUID uUID = player.getUniqueId();
        BukkitTask bukkitTask = (BukkitTask)this.M.get((Object)uUID);
        if (bukkitTask != null) {
            bukkitTask.cancel();
        }
        BukkitTask bukkitTask2 = new BukkitRunnable(this){
            final /* synthetic */ L this$0;
            {
                this.this$0 = l2;
            }

            public void run() {
                ItemMeta itemMeta;
                Player player = Bukkit.getPlayer((UUID)uUID);
                if (player == null || !player.isOnline()) {
                    this.cancel();
                    this.this$0.M.remove((Object)uUID);
                    return;
                }
                ItemStack itemStack = player.getInventory().getHelmet();
                boolean bl4 = false;
                if (itemStack != null && itemStack.hasItemMeta()) {
                    itemMeta = itemStack.getItemMeta();
                    NamespacedKey namespacedKey = new NamespacedKey((Plugin)this.this$0.plugin, "enchant_" + this.this$0.getIdentifier());
                    bl4 = itemMeta.getPersistentDataContainer().has(namespacedKey, PersistentDataType.DOUBLE);
                }
                if (!bl4) {
                    this.cancel();
                    this.this$0.M.remove((Object)uUID);
                    return;
                }
                if (!player.hasPotionEffect(PotionEffectType.FIRE_RESISTANCE)) {
                    this.this$0.L.add((Object)uUID);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, n2, n3, bl2, bl, bl3), false);
                    this.cancel();
                    this.this$0.M.remove((Object)uUID);
                    return;
                }
                itemMeta = player.getPotionEffect(PotionEffectType.FIRE_RESISTANCE);
                if (itemMeta != null && itemMeta.getDuration() <= n2) {
                    this.this$0.L.add((Object)uUID);
                    player.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, n2, n3, bl2, bl, bl3), false);
                    this.cancel();
                    this.this$0.M.remove((Object)uUID);
                }
            }
        }.runTaskTimer((Plugin)this.plugin, 20L, 20L);
        this.M.put((Object)uUID, (Object)bukkitTask2);
    }

    @Override
    public void applyEffectToItem(ItemStack itemStack) {
        super.applyEffectToItem(itemStack);
    }

    @Override
    public void handleEffect(ItemStack itemStack, Object ... objectArray) {
    }

    private int E(Map<?, ?> map, String string, int n2) {
        Object object = map.get((Object)string);
        if (object == null) {
            return n2;
        }
        if (object instanceof Number) {
            return ((Number)object).intValue();
        }
        if (object instanceof String) {
            try {
                return Integer.parseInt((String)((String)object));
            }
            catch (NumberFormatException numberFormatException) {
                return n2;
            }
        }
        return n2;
    }

    private boolean E(Map<?, ?> map, String string, boolean bl) {
        Object object = map.get((Object)string);
        if (object == null) {
            return bl;
        }
        if (object instanceof Boolean) {
            return (Boolean)object;
        }
        if (object instanceof String) {
            return Boolean.parseBoolean((String)((String)object));
        }
        return bl;
    }

    @Override
    protected FileConfiguration loadConfiguration() {
        FileConfiguration fileConfiguration = super.loadConfiguration();
        fileConfiguration.set("customModelData", (Object)2);
        try {
            this.saveCustomConfig((YamlConfiguration)fileConfiguration, this.configFile);
        }
        catch (Exception exception) {
            // empty catch block
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
}

