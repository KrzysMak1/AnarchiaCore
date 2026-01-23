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
 *  java.lang.Math
 *  java.lang.NumberFormatException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.util.ArrayList
 *  java.util.Collection
 *  java.util.Iterator
 *  java.util.List
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.NamespacedKey
 *  org.bukkit.Sound
 *  org.bukkit.configuration.ConfigurationSection
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.PlayerDeathEvent
 *  org.bukkit.inventory.ItemFlag
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.persistence.PersistentDataContainer
 *  org.bukkit.persistence.PersistentDataType
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.Plugin
 */
package me.anarchiacore.customitems.stormitemy.items;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.Plugin;
import me.anarchiacore.customitems.stormitemy.Main;
import me.anarchiacore.customitems.stormitemy.utils.color.A;

public class EA
implements Listener {
    private final Plugin C;
    private final ConfigurationSection B;
    private final NamespacedKey A;

    public EA(Plugin plugin) {
        Object object;
        this.C = plugin;
        this.A = new NamespacedKey(plugin, "excaliburKills");
        File file = new File(plugin.getDataFolder(), "items");
        if (!file.exists()) {
            file.mkdirs();
        }
        File file2 = new File(file, "excalibur.yml");
        YamlConfiguration yamlConfiguration = new YamlConfiguration();
        String string = "=== Konfiguracja przedmiotu 'Excalibur' ===\nOpis: Plik konfiguracyjny okre\u015bla w\u0142a\u015bciwo\u015bci przedmiotu eventowego 'Excalibur'.\n";
        yamlConfiguration.options().header(string);
        yamlConfiguration.options().copyHeader(true);
        yamlConfiguration.set("excalibur.material", (Object)"NETHERITE_SWORD");
        yamlConfiguration.set("excalibur.name", (Object)"&e&lExcalibur");
        ArrayList arrayList = new ArrayList();
        arrayList.add((Object)"&r");
        arrayList.add((Object)"&8\u00bb &7Jest to przedmiot zdobyty z");
        arrayList.add((Object)"&8\u00bb &fwydarzenia wakacyjnego 2024&7!");
        arrayList.add((Object)"&r");
        arrayList.add((Object)"&8\u00bb &7Aktualnie zab\u00f3jstw: &f{KILLS}");
        arrayList.add((Object)"&8\u00bb {PROGRESS_BAR}");
        arrayList.add((Object)"&r");
        arrayList.add((Object)"&8\u00bb &7Zape\u0142nienie paska zapewnia");
        arrayList.add((Object)"&8\u00bb &7Ci &f12 punkt\u00f3w obra\u017ce\u0144&7, co");
        arrayList.add((Object)"&8\u00bb &7jest r\u00f3wnoznaczne z &eostro\u015bci\u0105 {MAX_SHARPNESS}&7!");
        arrayList.add((Object)"&r");
        yamlConfiguration.set("excalibur.lore", (Object)arrayList);
        yamlConfiguration.set("excalibur.customModelData", (Object)5);
        yamlConfiguration.set("excalibur.maxSharpness", (Object)7);
        yamlConfiguration.set("excalibur.requiredKills", (Object)1000);
        yamlConfiguration.set("excalibur.startSharpness", (Object)6);
        yamlConfiguration.set("excalibur.enchantments", (Object)List.of((Object)"DURABILITY:3", (Object)"FIRE_ASPECT:2"));
        yamlConfiguration.set("excalibur.flags", (Object)List.of());
        yamlConfiguration.set("excalibur.unbreakable", (Object)false);
        yamlConfiguration.set("excalibur.messages.attacker.title", (Object)"&e&lEXCALIBUR");
        yamlConfiguration.set("excalibur.messages.attacker.subtitle", (Object)"&7Ulepszono miecz na &fwy\u017cszy poziom&7!");
        yamlConfiguration.set("excalibur.messages.attacker.chatMessage", (Object)"");
        yamlConfiguration.set("excalibur.messages.cooldown", (Object)"");
        yamlConfiguration.set("excalibur.progressBar.totalBars", (Object)10);
        yamlConfiguration.set("excalibur.progressBar.filledChar", (Object)"&a&m-");
        yamlConfiguration.set("excalibur.progressBar.emptyChar", (Object)"&f&m-");
        yamlConfiguration.set("excalibur.progressBar.suffix", (Object)" &r &e%.1f%%");
        yamlConfiguration.set("excalibur.sounds.enabled", (Object)false);
        yamlConfiguration.set("excalibur.sounds.upgrade.sound", (Object)"");
        yamlConfiguration.set("excalibur.sounds.upgrade.volume", (Object)1.0);
        yamlConfiguration.set("excalibur.sounds.upgrade.pitch", (Object)1.0);
        if (!file2.exists()) {
            try {
                yamlConfiguration.save(file2);
            }
            catch (IOException iOException) {
                iOException.printStackTrace();
            }
        } else {
            try {
                object = new me.anarchiacore.customitems.stormitemy.config.A((Plugin)plugin);
                ((me.anarchiacore.customitems.stormitemy.config.A)object).A("excalibur", yamlConfiguration.getConfigurationSection("excalibur"));
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        object = YamlConfiguration.loadConfiguration((File)file2);
        this.B = object.getConfigurationSection("excalibur");
        plugin.getServer().getPluginManager().registerEvents((Listener)this, plugin);
    }

    public boolean Kills(Player player, int n2) {
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (itemStack == null || !this.A(itemStack)) {
            return false;
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta = this.A(itemMeta, n2);
        itemStack.setItemMeta(itemMeta);
        return true;
    }

    public ItemStack getItem() {
        List list;
        Material material;
        String string = this.B.getString("material", "NETHERITE_SWORD");
        try {
            material = Material.valueOf((String)string);
        }
        catch (IllegalArgumentException illegalArgumentException) {
            material = Material.NETHERITE_SWORD;
        }
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        String string2 = me.anarchiacore.customitems.stormitemy.utils.color.A.C(this.B.getString("name", "&e&lExcalibur"));
        itemMeta.setDisplayName(string2);
        if (this.B.contains("customModelData")) {
            itemMeta.setCustomModelData(Integer.valueOf((int)this.B.getInt("customModelData")));
        }
        if (this.B.contains("flags")) {
            list = this.B.getStringList("flags");
            for (String string3 : list) {
                try {
                    itemMeta.addItemFlags(new ItemFlag[]{ItemFlag.valueOf((String)string3)});
                }
                catch (IllegalArgumentException illegalArgumentException) {}
            }
        }
        itemMeta.setUnbreakable(this.B.getBoolean("unbreakable", false));
        list = itemMeta.getPersistentDataContainer();
        int n2 = 0;
        list.set(this.A, PersistentDataType.INTEGER, (Object)n2);
        itemMeta = this.A(itemMeta, n2);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private boolean A(ItemStack itemStack) {
        boolean bl;
        Material material;
        if (itemStack == null || !itemStack.hasItemMeta()) {
            return false;
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        String string = me.anarchiacore.customitems.stormitemy.utils.color.A.C(this.B.getString("name", "&e&lExcalibur"));
        try {
            material = Material.valueOf((String)this.B.getString("material", "NETHERITE_SWORD"));
        }
        catch (IllegalArgumentException illegalArgumentException) {
            material = Material.NETHERITE_SWORD;
        }
        boolean bl2 = bl = itemStack.getType() == material && itemMeta.getDisplayName().equals((Object)string);
        if (itemMeta.hasCustomModelData()) {
            bl = bl && itemMeta.getCustomModelData() == this.B.getInt("customModelData");
        }
        return bl;
    }

    private ItemMeta A(ItemMeta itemMeta, int n2) {
        Object object;
        List list;
        String string2;
        int n3;
        int n4 = this.B.getInt("requiredKills", 1000);
        int n5 = this.B.getInt("maxSharpness", 7);
        int n6 = this.B.getInt("startSharpness", 0);
        int n7 = n6 + (n3 = (int)Math.floor((double)((double)n2 / (double)n4 * (double)(n5 - n6))));
        if (n7 > n5) {
            n7 = n5;
        }
        ArrayList arrayList = itemMeta.hasLore() ? new ArrayList((Collection)itemMeta.getLore()) : new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        ArrayList arrayList3 = new ArrayList();
        boolean bl = false;
        for (String string2 : arrayList) {
            list = ChatColor.stripColor((String)string2);
            if (list.startsWith("Ostro\u015b\u0107") || list.startsWith("Trwa\u0142o\u015b\u0107") || list.startsWith("Ogie\u0144") || list.contains((CharSequence)"Dodatkowe obra\u017cenia")) {
                arrayList2.add((Object)string2);
                continue;
            }
            if (list.contains((CharSequence)"zab\u00f3jstw:") || list.contains((CharSequence)"Zape\u0142nienie paska") || list.contains((CharSequence)"punkt\u00f3w obra\u017ce\u0144") || list.contains((CharSequence)"wydarzenie") || list.contains((CharSequence)"wakacyjne") || list.equals((Object)"") || list.matches(".*\\{.*\\}.*") || list.contains((CharSequence)"ostro\u015bci\u0105")) {
                bl = true;
                continue;
            }
            if (bl) continue;
            arrayList3.add((Object)string2);
        }
        for (String string2 : itemMeta.getEnchants().keySet()) {
            itemMeta.removeEnchant((Enchantment)string2);
        }
        if (n7 > 0) {
            itemMeta.addEnchant(Enchantment.DAMAGE_ALL, n7, true);
        }
        if (this.B.contains("enchantments")) {
            for (String string2 : this.B.getStringList("enchantments")) {
                String string3;
                list = string2.split(":");
                if (((String[])list).length < 2 || (string3 = list[0].trim().toUpperCase()).equals((Object)"SHARPNESS")) continue;
                int n8 = 1;
                try {
                    n8 = Integer.parseInt((String)list[1].trim());
                }
                catch (NumberFormatException numberFormatException) {
                    // empty catch block
                }
                if ((object = Enchantment.getByName((String)string3)) == null) continue;
                itemMeta.addEnchant(object, n8, true);
            }
        }
        Iterator iterator = itemMeta.getPersistentDataContainer();
        iterator.set(this.A, PersistentDataType.INTEGER, (Object)n2);
        string2 = new ArrayList();
        if (!arrayList3.isEmpty()) {
            string2.addAll((Collection)arrayList3);
        }
        if (this.B.contains("lore")) {
            list = this.B.getStringList("lore");
            for (String string4 : list) {
                string4 = string4.replace((CharSequence)"{PROGRESS_BAR}", (CharSequence)"%%PROGRESS_BAR%%");
                string4 = string4.replace((CharSequence)"{KILLS}", (CharSequence)String.valueOf((int)n2));
                string4 = string4.replace((CharSequence)"{MAX_SHARPNESS}", (CharSequence)String.valueOf((int)n5));
                if ((string4 = me.anarchiacore.customitems.stormitemy.utils.color.A.C(string4)).contains((CharSequence)"%%PROGRESS_BAR%%")) {
                    object = this.A(n2, n4);
                    object = me.anarchiacore.customitems.stormitemy.utils.color.A.C((String)object);
                    string4 = string4.replace((CharSequence)"%%PROGRESS_BAR%%", (CharSequence)object);
                }
                string2.add((Object)string4);
            }
        }
        if (!arrayList2.isEmpty()) {
            string2.addAll((Collection)arrayList2);
        }
        itemMeta.setLore((List)string2);
        return itemMeta;
    }

    private void A(Player player) {
        try {
            boolean bl = this.B.getBoolean("sounds.enabled", false);
            if (!bl) {
                return;
            }
            String string = this.B.getString("sounds.upgrade.sound", "");
            if (string == null || string.trim().isEmpty()) {
                return;
            }
            Sound sound = Sound.valueOf((String)string);
            float f2 = (float)this.B.getDouble("sounds.upgrade.volume", 1.0);
            float f3 = (float)this.B.getDouble("sounds.upgrade.pitch", 1.0);
            player.playSound(player.getLocation(), sound, f2, f3);
        }
        catch (Exception exception) {
            this.C.getLogger().warning("B\u0142\u0105d podczas odtwarzania d\u017awi\u0119ku dla Excalibur: " + exception.getMessage());
        }
    }

    private String A(int n2, int n3) {
        int n4;
        double d2 = Math.min((double)1.0, (double)((double)n2 / (double)n3));
        int n5 = this.B.getInt("progressBar.totalBars", 10);
        String string = this.B.getString("progressBar.filledChar", "&a&m-");
        String string2 = this.B.getString("progressBar.emptyChar", "&f&m-");
        String string3 = this.B.getString("progressBar.suffix", " &r &e%.1f%%");
        int n6 = (int)Math.floor((double)(d2 * (double)n5));
        int n7 = n5 - n6;
        StringBuilder stringBuilder = new StringBuilder();
        for (n4 = 0; n4 < n6; ++n4) {
            stringBuilder.append(string);
        }
        for (n4 = 0; n4 < n7; ++n4) {
            stringBuilder.append(string2);
        }
        String string4 = String.format((String)string3, (Object[])new Object[]{d2 * 100.0});
        return stringBuilder.toString() + string4;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent playerDeathEvent) {
        int n2;
        Player player = playerDeathEvent.getEntity().getKiller();
        if (player == null) {
            return;
        }
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (itemStack == null || !this.A(itemStack)) {
            return;
        }
        if (this.C instanceof Main && ((Main)this.C).isItemDisabledByKey("excalibur")) {
            return;
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
        int n3 = (Integer)persistentDataContainer.getOrDefault(this.A, PersistentDataType.INTEGER, (Object)0);
        int n4 = this.B.getInt("requiredKills", 1000);
        int n5 = this.B.getInt("maxSharpness", 7);
        int n6 = this.B.getInt("startSharpness", 0);
        int n7 = n6 + (int)Math.floor((double)((double)n3 / (double)n4 * (double)(n5 - n6)));
        if ((n2 = n6 + (int)Math.floor((double)((double)(++n3) / (double)n4 * (double)(n5 - n6)))) > n7) {
            this.A(player);
            ConfigurationSection configurationSection = this.B.getConfigurationSection("messages");
            if (configurationSection != null) {
                String string;
                String string2;
                String string3;
                ConfigurationSection configurationSection2;
                if (configurationSection.isConfigurationSection("attacker")) {
                    configurationSection2 = configurationSection.getConfigurationSection("attacker");
                    string3 = configurationSection2.getString("title", "");
                    string2 = configurationSection2.getString("subtitle", "");
                    string = configurationSection2.getString("chatMessage", "");
                    string2 = string2.replace((CharSequence)"{player}", (CharSequence)playerDeathEvent.getEntity().getName());
                    string2 = string2.replace((CharSequence)"{sharpness}", (CharSequence)String.valueOf((int)n2));
                    string3 = me.anarchiacore.customitems.stormitemy.utils.color.A.C(string3);
                    string2 = me.anarchiacore.customitems.stormitemy.utils.color.A.C(string2);
                    if (!string3.isEmpty() || !string2.isEmpty()) {
                        player.sendTitle(string3, string2, 10, 70, 20);
                    }
                    if (!string.isEmpty()) {
                        player.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string));
                    }
                }
                if (configurationSection.isConfigurationSection("victim")) {
                    configurationSection2 = configurationSection.getConfigurationSection("victim");
                    string3 = configurationSection2.getString("title", "");
                    string2 = configurationSection2.getString("subtitle", "");
                    string = configurationSection2.getString("chatMessage", "");
                    string2 = string2.replace((CharSequence)"{player}", (CharSequence)player.getName());
                    string2 = string2.replace((CharSequence)"{sharpness}", (CharSequence)String.valueOf((int)n2));
                    string3 = me.anarchiacore.customitems.stormitemy.utils.color.A.C(string3);
                    string2 = me.anarchiacore.customitems.stormitemy.utils.color.A.C(string2);
                    if (!string3.isEmpty() || !string2.isEmpty()) {
                        playerDeathEvent.getEntity().sendTitle(string3, string2, 10, 70, 20);
                    }
                    if (!string.isEmpty()) {
                        playerDeathEvent.getEntity().sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string));
                    }
                }
            }
        }
        itemMeta = this.A(itemMeta, n3);
        itemStack.setItemMeta(itemMeta);
    }
}

