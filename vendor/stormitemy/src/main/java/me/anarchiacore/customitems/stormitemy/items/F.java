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
 *  java.util.HashSet
 *  java.util.List
 *  java.util.Set
 *  java.util.UUID
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.configuration.ConfigurationSection
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.player.PlayerMoveEvent
 *  org.bukkit.inventory.ItemFlag
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.Plugin
 */
package me.anarchiacore.customitems.stormitemy.items;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.Plugin;
import me.anarchiacore.customitems.stormitemy.Main;
import me.anarchiacore.customitems.stormitemy.utils.color.A;

public class f
implements Listener {
    private final Plugin B;
    private final ConfigurationSection A;
    private final Set<UUID> C = new HashSet();

    public f(Plugin javaPlugin) {
        YamlConfiguration yamlConfiguration;
        File file;
        this.B = javaPlugin;
        javaPlugin.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)javaPlugin);
        File file2 = new File(javaPlugin.getDataFolder(), "items");
        if (!file2.exists()) {
            file2.mkdirs();
        }
        if (!(file = new File(file2, "marchewkowymiecz.yml")).exists()) {
            try {
                file.createNewFile();
                yamlConfiguration = new YamlConfiguration();
                String string = "=== Konfiguracja przedmiotu 'Marchewkowy miecz' ===\nOpis: Plik konfiguracyjny okre\u015bla w\u0142a\u015bciwo\u015bci przedmiotu eventowego 'Marchewkowy miecz'.\nDost\u0119pne zmienne (placeholdery):\n  {TIME}         - pozosta\u0142y czas cooldownu (w sekundach)\n  {ATTACKER}      - nazwa gracza, kt\u00f3ry uderzy\u0142\n  {TARGET}       - nazwa gracza, kt\u00f3ry zosta\u0142 trafiony\n";
                yamlConfiguration.options().header(string);
                yamlConfiguration.options().copyHeader(true);
                yamlConfiguration.set("marchewkowymiecz.material", (Object)"GOLDEN_SWORD");
                yamlConfiguration.set("marchewkowymiecz.name", (Object)"&6Marchewkowy Miecz");
                yamlConfiguration.set("marchewkowymiecz.lore", (Object)List.of((Object)"&r", (Object)"&8 \u00bb &7Dzi\u0119ki niemu mo\u017cesz &bzamrozi\u0107 &7przeciwnika", (Object)"&8 \u00bb &7po &cuderzeniu &7na &asekund\u0119&7!", (Object)"&r"));
                yamlConfiguration.set("marchewkowymiecz.customModelData", (Object)1);
                yamlConfiguration.set("marchewkowymiecz.cooldown", (Object)30);
                yamlConfiguration.set("marchewkowymiecz.effect.duration", (Object)1);
                yamlConfiguration.set("marchewkowymiecz.enchantments", (Object)List.of((Object)"DAMAGE_ALL:3", (Object)"DURABILITY:3"));
                yamlConfiguration.set("marchewkowymiecz.flags", (Object)List.of((Object)"HIDE_ENCHANTS", (Object)"HIDE_UNBREAKABLE", (Object)"HIDE_ATTRIBUTES"));
                yamlConfiguration.set("marchewkowymiecz.unbreakable", (Object)true);
                yamlConfiguration.set("marchewkowymiecz.messages.attackerTitle", (Object)"&6MARCHEWKOWY MIECZ");
                yamlConfiguration.set("marchewkowymiecz.messages.attackerSubtitle", (Object)"&7Zosta\u0142e\u015b zamro\u017cony przez &f{TARGET}&7!");
                yamlConfiguration.set("marchewkowymiecz.messages.attacker", (Object)"");
                yamlConfiguration.set("marchewkowymiecz.messages.targetTitle", (Object)"&6MARCHEWKOWY MIECZ");
                yamlConfiguration.set("marchewkowymiecz.messages.targetSubtitle", (Object)"&7Zamrozi\u0142e\u015b przeciwnika &f{ATTACKER}&7!");
                yamlConfiguration.set("marchewkowymiecz.messages.target", (Object)"");
                yamlConfiguration.set("marchewkowymiecz.messages.cooldown", (Object)"");
                yamlConfiguration.set("marchewkowymiecz.sounds.enabled", (Object)false);
                yamlConfiguration.set("marchewkowymiecz.sounds.activation.sound", (Object)"");
                yamlConfiguration.set("marchewkowymiecz.sounds.activation.volume", (Object)1.0);
                yamlConfiguration.set("marchewkowymiecz.sounds.activation.pitch", (Object)1.0);
                yamlConfiguration.save(file);
            }
            catch (IOException iOException) {
                iOException.printStackTrace();
            }
        }
        yamlConfiguration = YamlConfiguration.loadConfiguration((File)file);
        this.A = yamlConfiguration.getConfigurationSection("marchewkowymiecz");
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
            this.B.getLogger().warning("B\u0142\u0105d podczas odtwarzania d\u017awi\u0119ku dla MarchewkowyMiecz: " + exception.getMessage());
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent entityDamageByEntityEvent) {
        String string;
        Object object;
        if (!(entityDamageByEntityEvent.getDamager() instanceof Player)) {
            return;
        }
        Player player = (Player)entityDamageByEntityEvent.getDamager();
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (itemStack == null) {
            return;
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null || !itemMeta.hasDisplayName()) {
            return;
        }
        String string2 = me.anarchiacore.customitems.stormitemy.utils.color.A.C(this.A.getString("name", "&6Marchewkowy Miecz"));
        if (!itemMeta.getDisplayName().equals((Object)string2)) {
            return;
        }
        if (this.B instanceof Main && ((Main)this.B).isItemDisabledByKey("marchewkowymiecz")) {
            return;
        }
        if (player.getCooldown(Material.GOLDEN_SWORD) > 0) {
            String string3 = this.A.getString("messages.cooldown", "");
            if (!string3.isEmpty()) {
                string3 = string3.replace((CharSequence)"{TIME}", (CharSequence)String.valueOf((int)(player.getCooldown(Material.GOLDEN_SWORD) / 20)));
                player.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string3));
            }
            return;
        }
        if (!(entityDamageByEntityEvent.getEntity() instanceof Player)) {
            return;
        }
        Player player2 = (Player)entityDamageByEntityEvent.getEntity();
        boolean bl = false;
        boolean bl2 = false;
        try {
            if (this.B instanceof Main) {
                object = (Main)this.B;
                if (object.isPlayerInBlockedRegion(player)) {
                    bl = true;
                }
                if (object.isPlayerInBlockedRegion(player2)) {
                    bl2 = true;
                }
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        if (bl || bl2) {
            me.anarchiacore.customitems.stormitemy.utils.language.A.A(player);
            entityDamageByEntityEvent.setCancelled(true);
            return;
        }
        this.C.add((Object)player2.getUniqueId());
        this.A(player);
        object = this.A.getString("messages.attacker", "");
        if (!object.isEmpty()) {
            object = me.anarchiacore.customitems.stormitemy.utils.color.A.C((String)object).replace((CharSequence)"{TARGET}", (CharSequence)player2.getName()).replace((CharSequence)"{ATTACKER}", (CharSequence)player.getName());
            player.sendMessage((String)object);
        }
        if (!(string = this.A.getString("messages.target", "")).isEmpty()) {
            string = me.anarchiacore.customitems.stormitemy.utils.color.A.C(string).replace((CharSequence)"{TARGET}", (CharSequence)player2.getName()).replace((CharSequence)"{ATTACKER}", (CharSequence)player.getName());
            player2.sendMessage(string);
        }
        String string4 = this.A.getString("messages.attackerTitle", "");
        String string5 = this.A.getString("messages.attackerSubtitle", "");
        if (!string4.isEmpty() || !string5.isEmpty()) {
            player.sendTitle(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string4).replace((CharSequence)"{TARGET}", (CharSequence)player2.getName()).replace((CharSequence)"{ATTACKER}", (CharSequence)player.getName()), me.anarchiacore.customitems.stormitemy.utils.color.A.C(string5).replace((CharSequence)"{TARGET}", (CharSequence)player2.getName()).replace((CharSequence)"{ATTACKER}", (CharSequence)player.getName()), 10, 70, 20);
        }
        String string6 = this.A.getString("messages.targetTitle", "");
        String string7 = this.A.getString("messages.targetSubtitle", "");
        if (!string6.isEmpty() || !string7.isEmpty()) {
            player2.sendTitle(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string6).replace((CharSequence)"{TARGET}", (CharSequence)player2.getName()).replace((CharSequence)"{ATTACKER}", (CharSequence)player.getName()), me.anarchiacore.customitems.stormitemy.utils.color.A.C(string7).replace((CharSequence)"{TARGET}", (CharSequence)player2.getName()).replace((CharSequence)"{ATTACKER}", (CharSequence)player.getName()), 10, 70, 20);
        }
        player.setCooldown(Material.GOLDEN_SWORD, this.A.getInt("cooldown", 30) * 20);
        int n2 = this.A.getInt("effect.duration", 1);
        Bukkit.getScheduler().runTaskLater((Plugin)this.B, () -> this.C.remove((Object)player2.getUniqueId()), (long)n2 * 20L);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent playerMoveEvent) {
        Player player = playerMoveEvent.getPlayer();
        if (this.C.contains((Object)player.getUniqueId())) {
            playerMoveEvent.setCancelled(true);
        }
    }

    public ItemStack getItem() {
        Material material;
        try {
            material = Material.valueOf((String)this.A.getString("material", "GOLDEN_SWORD"));
        }
        catch (IllegalArgumentException illegalArgumentException) {
            material = Material.GOLDEN_SWORD;
        }
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        String string = me.anarchiacore.customitems.stormitemy.utils.color.A.C(this.A.getString("name", "&6Marchewkowy Miecz"));
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
}

