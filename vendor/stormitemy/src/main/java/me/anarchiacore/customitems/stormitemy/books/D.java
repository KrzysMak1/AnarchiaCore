/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.io.File
 *  java.io.IOException
 *  java.lang.CharSequence
 *  java.lang.Exception
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.Collection
 *  java.util.HashMap
 *  java.util.List
 *  java.util.Map
 *  org.bukkit.Material
 *  org.bukkit.NamespacedKey
 *  org.bukkit.configuration.ConfigurationSection
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.inventory.ItemFlag
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.persistence.PersistentDataType
 *  org.bukkit.plugin.Plugin
 */
package me.anarchiacore.customitems.stormitemy.books;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import me.anarchiacore.customitems.stormitemy.Main;
import me.anarchiacore.customitems.stormitemy.utils.color.A;

public abstract class D {
    protected final Main plugin;
    protected final String name;
    protected final String identifier;
    protected final List<String> allowedItems;
    protected final double defaultChance;
    protected final int cooldownSeconds;
    protected final List<String> lore;
    protected final File configFile;
    protected FileConfiguration config;

    public D(Main main, String string, String string2, List<String> list, double d2, int n2, List<String> list2) {
        this.plugin = main;
        this.name = string;
        this.identifier = string2;
        this.allowedItems = list;
        this.defaultChance = d2 * 100.0;
        this.cooldownSeconds = n2;
        this.lore = list2;
        File file = new File(main.getDataFolder(), "books");
        if (!file.exists()) {
            file.mkdirs();
        }
        this.configFile = new File(file, string2 + ".yml");
        this.config = this.loadConfiguration();
    }

    public void reload() {
        this.config = YamlConfiguration.loadConfiguration((File)this.configFile);
    }

    protected FileConfiguration loadConfiguration() {
        if (!this.configFile.exists()) {
            try {
                this.configFile.createNewFile();
                YamlConfiguration yamlConfiguration = new YamlConfiguration();
                String string = "=== Konfiguracja ksi\u0119gi '" + this.name + "' ===\nOpis: Plik konfiguracyjny okre\u015bla w\u0142a\u015bciwo\u015bci zakl\u0119tej ksi\u0119gi.\nDost\u0119pne zmienne (placeholdery):\n  {PLAYER}      - nazwa gracza, kt\u00f3ry uderzy\u0142\n  {TARGET}      - nazwa gracza, kt\u00f3ry zosta\u0142 uderzony\n";
                yamlConfiguration.options().header(string);
                yamlConfiguration.options().copyHeader(true);
                yamlConfiguration.set("name", (Object)"&eZakl\u0119ta ksi\u0105\u017cka");
                yamlConfiguration.set("chance", (Object)this.defaultChance);
                yamlConfiguration.set("cooldown", (Object)this.cooldownSeconds);
                yamlConfiguration.set("allowedItems", this.allowedItems);
                yamlConfiguration.set("lore", this.lore);
                yamlConfiguration.set("glow", (Object)true);
                yamlConfiguration.set("customModelData", (Object)0);
                yamlConfiguration.set("messages.attacker.title", (Object)"&2ZAKL\u0118CIE TRUCIZNY");
                yamlConfiguration.set("messages.attacker.subtitle", (Object)"&aOtru\u0142e\u015b &7gracza &f{TARGET}&7!");
                yamlConfiguration.set("messages.attacker.chat", (Object)"");
                yamlConfiguration.set("messages.target.title", (Object)"&2ZAKL\u0118CIE TRUCIZNY");
                yamlConfiguration.set("messages.target.subtitle", (Object)"&7Zosta\u0142e\u015b &cotruty &7przez &f{PLAYER}&7!");
                yamlConfiguration.set("messages.target.chat", (Object)"");
                List<Map<String, Object>> list = this.getDefaultEffects();
                yamlConfiguration.set("effects", list);
                Map<String, Object> map = this.getDefaultSoundConfig();
                if (map != null && !map.isEmpty()) {
                    yamlConfiguration.set("sound", map);
                }
                this.saveCustomConfig(yamlConfiguration, this.configFile);
                return yamlConfiguration;
            }
            catch (IOException iOException) {
                iOException.printStackTrace();
                return new YamlConfiguration();
            }
        }
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration((File)this.configFile);
        if (yamlConfiguration.isSet("targetModelData")) {
            int n2;
            if (yamlConfiguration.isSet("customModelData") && yamlConfiguration.getInt("customModelData", 0) == 0 && (n2 = yamlConfiguration.getInt("targetModelData", 0)) > 0) {
                yamlConfiguration.set("customModelData", (Object)n2);
            }
            yamlConfiguration.set("targetModelData", null);
            try {
                this.saveCustomConfig(yamlConfiguration, this.configFile);
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        if (yamlConfiguration.isSet("appearance") || yamlConfiguration.isSet("target_item")) {
            boolean bl = true;
            if (yamlConfiguration.isSet("appearance.glow")) {
                bl = yamlConfiguration.getBoolean("appearance.glow", true);
            }
            int n3 = 0;
            if (yamlConfiguration.isSet("appearance.customModelData")) {
                n3 = yamlConfiguration.getInt("appearance.customModelData", 0);
            }
            yamlConfiguration.set("glow", (Object)bl);
            yamlConfiguration.set("customModelData", (Object)n3);
            yamlConfiguration.set("appearance", null);
            yamlConfiguration.set("target_item", null);
            try {
                this.saveCustomConfig(yamlConfiguration, this.configFile);
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        return yamlConfiguration;
    }

    protected void saveCustomConfig(YamlConfiguration yamlConfiguration, File file) {
        try {
            yamlConfiguration.save(file);
        }
        catch (IOException iOException) {
            iOException.printStackTrace();
        }
    }

    protected abstract List<Map<String, Object>> getDefaultEffects();

    protected Map<String, Object> getDefaultSoundConfig() {
        HashMap hashMap = new HashMap();
        hashMap.put((Object)"enabled", (Object)true);
        hashMap.put((Object)"sound", (Object)"entity.witch.throw");
        hashMap.put((Object)"volume", (Object)1.0);
        hashMap.put((Object)"pitch", (Object)1.0);
        return hashMap;
    }

    public ItemStack getBookItem() {
        String string2;
        ItemStack itemStack = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(A.C(this.config.getString("name", "&eZakl\u0119ta ksi\u0105\u017cka")));
        ArrayList arrayList = new ArrayList();
        List list = this.config.getStringList("lore");
        for (String string2 : list) {
            arrayList.add((Object)A.C(string2));
        }
        itemMeta.setLore((List)arrayList);
        int n2 = this.config.getInt("customModelData", 0);
        if (n2 > 0) {
            itemMeta.setCustomModelData(Integer.valueOf((int)n2));
        }
        itemMeta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES});
        string2 = new NamespacedKey((Plugin)this.plugin, "book_enchant");
        itemMeta.getPersistentDataContainer().set((NamespacedKey)string2, PersistentDataType.STRING, (Object)this.identifier);
        itemStack.setItemMeta(itemMeta);
        if (this.config.getBoolean("glow", true)) {
            this.applyGlowEffect(itemStack);
        }
        return itemStack;
    }

    public String getName() {
        return this.config.getString("name", this.name);
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public List<String> getAllowedItems() {
        return this.config.getStringList("allowedItems");
    }

    public double getDefaultChance() {
        return this.config.getDouble("chance", this.defaultChance) / 100.0;
    }

    public double getConfigChance() {
        return this.config.getDouble("chance", this.defaultChance);
    }

    public int getCooldownSeconds() {
        return this.config.getInt("cooldown", this.cooldownSeconds);
    }

    public List<String> getLore() {
        return this.config.getStringList("lore");
    }

    public String getAttackerTitle() {
        return this.config.getString("messages.attacker.title", "");
    }

    public String getAttackerSubtitle() {
        return this.config.getString("messages.attacker.subtitle", "");
    }

    public String getAttackerChat() {
        return this.config.getString("messages.attacker.chat", "");
    }

    public String getTargetTitle() {
        return this.config.getString("messages.target.title", "");
    }

    public String getTargetSubtitle() {
        return this.config.getString("messages.target.subtitle", "");
    }

    public String getTargetChat() {
        return this.config.getString("messages.target.chat", "");
    }

    public List<Map<?, ?>> getEffects() {
        return this.config.getMapList("effects");
    }

    public ConfigurationSection getSoundConfig() {
        return this.config.getConfigurationSection("sound");
    }

    public boolean canApplyToItem(ItemStack itemStack) {
        if (itemStack == null) {
            return false;
        }
        String string = itemStack.getType().name().toLowerCase();
        List list = this.config.getStringList("allowedItems");
        for (String string2 : list) {
            if (!string.contains((CharSequence)string2.toLowerCase())) continue;
            return true;
        }
        return false;
    }

    protected void applyGlowEffect(ItemStack itemStack) {
        if (!this.config.getBoolean("glow", true)) {
            return;
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        boolean bl = false;
        for (Enchantment enchantment : itemMeta.getEnchants().keySet()) {
            if (enchantment == Enchantment.DURABILITY && itemMeta.getEnchantLevel(enchantment) <= 1) continue;
            bl = true;
            break;
        }
        if (!bl) {
            itemMeta.addEnchant(Enchantment.DURABILITY, 1, true);
            itemMeta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ENCHANTS});
            itemStack.setItemMeta(itemMeta);
        }
    }

    public void applyEffectToItem(ItemStack itemStack) {
        if (!this.canApplyToItem(itemStack)) {
            return;
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        NamespacedKey namespacedKey = new NamespacedKey((Plugin)this.plugin, "enchant_" + this.getIdentifier());
        itemMeta.getPersistentDataContainer().set(namespacedKey, PersistentDataType.DOUBLE, (Object)this.getDefaultChance());
        List list = itemMeta.hasLore() ? itemMeta.getLore() : new ArrayList();
        List<String> list2 = this.getLore();
        String string = "&7" + this.name;
        if (list2 != null && !list2.isEmpty()) {
            string = (String)list2.get(0);
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add((Object)A.C(string));
        if (list != null) {
            arrayList.addAll((Collection)list);
        }
        itemMeta.setLore((List)arrayList);
        itemStack.setItemMeta(itemMeta);
        if (this.config.getBoolean("glow", true)) {
            this.applyGlowEffect(itemStack);
        }
    }

    public abstract void handleEffect(ItemStack var1, Object ... var2);

    public boolean isIncompatibleWith(D d2) {
        return false;
    }
}

