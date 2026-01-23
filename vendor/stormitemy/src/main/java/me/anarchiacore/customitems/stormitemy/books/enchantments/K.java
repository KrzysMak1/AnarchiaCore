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
import me.anarchiacore.customitems.stormitemy.books.enchantments.J;

public class K
extends D {
    private final Map<UUID, BukkitTask> H = new WeakHashMap();
    private final Map<UUID, BukkitTask> J = new ConcurrentHashMap();
    private final Set<UUID> I = ConcurrentHashMap.newKeySet();

    public K(Main main) {
        super(main, "Szybko\u015b\u0107 I", "szybkosc", (List<String>)Arrays.asList((Object[])new String[]{"boots"}), 1.0, 0, (List<String>)Arrays.asList((Object[])new String[]{"&7Szybko\u015b\u0107 I", "&7", "&2&lZakl\u0119cie specjalne!", "&7Mo\u017ce zosta\u0107 na\u0142o\u017cony na &abuty&7!", "&7Otrzymujesz sta\u0142y efekt &fszybko\u015bci I&7.", "&7"}));
        new BukkitRunnable(){

            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    K.this.K(player);
                }
                K.this.J.entrySet().removeIf(entry -> {
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
        hashMap.put((Object)"type", (Object)"SPEED");
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

    @Override
    protected FileConfiguration loadConfiguration() {
        FileConfiguration fileConfiguration = super.loadConfiguration();
        fileConfiguration.set("customModelData", (Object)6);
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

    private void K(Player player) {
        ItemStack itemStack = player.getInventory().getBoots();
        UUID uUID = player.getUniqueId();
        boolean bl = false;
        if (itemStack != null && itemStack.hasItemMeta()) {
            ItemMeta itemMeta = itemStack.getItemMeta();
            NamespacedKey namespacedKey = new NamespacedKey((Plugin)this.plugin, "enchant_" + this.getIdentifier());
            bl = itemMeta.getPersistentDataContainer().has(namespacedKey, PersistentDataType.DOUBLE);
        }
        if (bl) {
            this.M(player);
            this.I.add((Object)uUID);
        } else {
            this.L(player);
        }
    }

    private void M(Player player) {
        List<Map<?, ?>> list = this.getEffects();
        for (Map map : list) {
            String string = String.valueOf((Object)map.get((Object)"type"));
            if (!string.equals((Object)"SPEED")) continue;
            int n2 = this.D(map, "duration", 7) * 20;
            int n3 = this.D(map, "amplifier", 0);
            boolean bl = this.D(map, "particles", false);
            boolean bl2 = this.D(map, "ambient", true);
            boolean bl3 = this.D(map, "icon", true);
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, n2, n3, bl2, bl, bl3), true);
        }
    }

    private void L(Player player) {
        UUID uUID = player.getUniqueId();
        boolean bl = this.I.contains((Object)uUID);
        BukkitTask bukkitTask = (BukkitTask)this.J.remove((Object)uUID);
        if (bukkitTask != null) {
            bukkitTask.cancel();
        }
        this.I.remove((Object)uUID);
        if (player.hasPotionEffect(PotionEffectType.SPEED)) {
            ItemStack itemStack = player.getInventory().getBoots();
            boolean bl2 = false;
            if (itemStack != null && itemStack.hasItemMeta()) {
                ItemMeta itemMeta = itemStack.getItemMeta();
                NamespacedKey namespacedKey = new NamespacedKey((Plugin)this.plugin, "enchant_" + this.getIdentifier());
                bl2 = itemMeta.getPersistentDataContainer().has(namespacedKey, PersistentDataType.DOUBLE);
            }
            if (!bl2 && bl) {
                player.removePotionEffect(PotionEffectType.SPEED);
            }
        }
    }

    private void B(Player player, final int n2, final int n3, final boolean bl, final boolean bl2, final boolean bl3) {
        final UUID uUID = player.getUniqueId();
        BukkitTask bukkitTask = (BukkitTask)this.J.get((Object)uUID);
        if (bukkitTask != null) {
            bukkitTask.cancel();
        }
        BukkitTask bukkitTask2 = new BukkitRunnable(this){
            final /* synthetic */ K this$0;
            {
                this.this$0 = k2;
            }

            public void run() {
                ItemMeta itemMeta;
                Player player = Bukkit.getPlayer((UUID)uUID);
                if (player == null || !player.isOnline()) {
                    this.cancel();
                    this.this$0.J.remove((Object)uUID);
                    return;
                }
                ItemStack itemStack = player.getInventory().getBoots();
                boolean bl4 = false;
                if (itemStack != null && itemStack.hasItemMeta()) {
                    itemMeta = itemStack.getItemMeta();
                    NamespacedKey namespacedKey = new NamespacedKey((Plugin)this.this$0.plugin, "enchant_" + this.this$0.getIdentifier());
                    bl4 = itemMeta.getPersistentDataContainer().has(namespacedKey, PersistentDataType.DOUBLE);
                }
                if (!bl4) {
                    this.cancel();
                    this.this$0.J.remove((Object)uUID);
                    return;
                }
                if (!player.hasPotionEffect(PotionEffectType.SPEED)) {
                    this.this$0.I.add((Object)uUID);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, n2, n3, bl2, bl, bl3), false);
                    this.cancel();
                    this.this$0.J.remove((Object)uUID);
                    return;
                }
                itemMeta = player.getPotionEffect(PotionEffectType.SPEED);
                if (itemMeta != null && itemMeta.getDuration() <= n2) {
                    this.this$0.I.add((Object)uUID);
                    player.removePotionEffect(PotionEffectType.SPEED);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, n2, n3, bl2, bl, bl3), false);
                    this.cancel();
                    this.this$0.J.remove((Object)uUID);
                }
            }
        }.runTaskTimer((Plugin)this.plugin, 20L, 20L);
        this.J.put((Object)uUID, (Object)bukkitTask2);
    }

    @Override
    public void applyEffectToItem(ItemStack itemStack) {
        super.applyEffectToItem(itemStack);
    }

    @Override
    public void handleEffect(ItemStack itemStack, Object ... objectArray) {
    }

    private int D(Map<?, ?> map, String string, int n2) {
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

    private boolean D(Map<?, ?> map, String string, boolean bl) {
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
    public boolean isIncompatibleWith(D d2) {
        return d2 instanceof J;
    }
}

