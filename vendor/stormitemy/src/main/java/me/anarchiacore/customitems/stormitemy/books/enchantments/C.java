/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Boolean
 *  java.lang.CharSequence
 *  java.lang.Exception
 *  java.lang.IllegalArgumentException
 *  java.lang.Integer
 *  java.lang.Number
 *  java.lang.NumberFormatException
 *  java.lang.Object
 *  java.lang.Override
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.Arrays
 *  java.util.HashMap
 *  java.util.Iterator
 *  java.util.List
 *  java.util.Map
 *  org.bukkit.Sound
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package me.anarchiacore.customitems.stormitemy.books.enchantments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import me.anarchiacore.customitems.stormitemy.Main;
import me.anarchiacore.customitems.stormitemy.books.D;
import me.anarchiacore.customitems.stormitemy.utils.color.A;

public class C
extends D {
    public C(Main main) {
        super(main, "Spowolnienie I", "spowolnienie", (List<String>)Arrays.asList((Object[])new String[]{"sword"}), 0.03, 60, (List<String>)Arrays.asList((Object[])new String[]{"&7Spowolnienie I", "&7", "&2&lZakl\u0119cie specjalne!", "&7Mo\u017ce zosta\u0107 na\u0142o\u017cony na &amiecz&7!", "&7Masz szans\u0119, \u017ce gracz, kt\u00f3rego uderzysz", "&7otrzyma efekt spowolnienia I.", "&7"}));
    }

    @Override
    protected List<Map<String, Object>> getDefaultEffects() {
        ArrayList arrayList = new ArrayList();
        HashMap hashMap = new HashMap();
        hashMap.put((Object)"type", (Object)"SLOW");
        hashMap.put((Object)"duration", (Object)5);
        hashMap.put((Object)"amplifier", (Object)0);
        hashMap.put((Object)"particles", (Object)true);
        hashMap.put((Object)"ambient", (Object)false);
        hashMap.put((Object)"icon", (Object)true);
        arrayList.add((Object)hashMap);
        return arrayList;
    }

    @Override
    protected Map<String, Object> getDefaultSoundConfig() {
        HashMap hashMap = new HashMap();
        hashMap.put((Object)"enabled", (Object)true);
        hashMap.put((Object)"sound", (Object)"entity.slime.squish");
        hashMap.put((Object)"volume", (Object)1.0);
        hashMap.put((Object)"pitch", (Object)0.5);
        return hashMap;
    }

    @Override
    protected FileConfiguration loadConfiguration() {
        FileConfiguration fileConfiguration = super.loadConfiguration();
        fileConfiguration.set("customModelData", (Object)7);
        try {
            this.saveCustomConfig((YamlConfiguration)fileConfiguration, this.configFile);
        }
        catch (Exception exception) {
            // empty catch block
        }
        if (fileConfiguration.getString("messages.attacker.title", "").contains((CharSequence)"TRUCIZNY")) {
            fileConfiguration.set("messages.attacker.title", (Object)"&8ZAKL\u0118CIE SPOWOLNIENIA");
            fileConfiguration.set("messages.attacker.subtitle", (Object)"&cSpowolni\u0142e\u015b &7gracza &f{TARGET}&7!");
            fileConfiguration.set("messages.attacker.chat", (Object)"");
            fileConfiguration.set("messages.target.title", (Object)"&8ZAKL\u0118CIE SPOWOLNIENIA");
            fileConfiguration.set("messages.target.subtitle", (Object)"&7Zosta\u0142e\u015b &cspowolniony &7przez &f{PLAYER}&7!");
            fileConfiguration.set("messages.target.chat", (Object)"");
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

    @Override
    public void handleEffect(ItemStack itemStack, Object ... objectArray) {
        Object object;
        String string;
        Object object22;
        if (objectArray.length < 2) {
            return;
        }
        if (!(objectArray[0] instanceof Player) || !(objectArray[1] instanceof Entity)) {
            return;
        }
        Player player = (Player)objectArray[0];
        Entity entity = (Entity)objectArray[1];
        if (!(entity instanceof LivingEntity)) {
            return;
        }
        LivingEntity livingEntity = (LivingEntity)entity;
        List<Map<?, ?>> list = this.getEffects();
        for (Object object22 : list) {
            string = String.valueOf((Object)object22.get((Object)"type"));
            object = PotionEffectType.getByName((String)string);
            if (object == null) continue;
            int n2 = this.A((Map<?, ?>)object22, "duration", 5) * 20;
            int n3 = this.A((Map<?, ?>)object22, "amplifier", 0);
            boolean bl = this.A((Map<?, ?>)object22, "particles", true);
            boolean bl2 = this.A((Map<?, ?>)object22, "ambient", false);
            boolean bl3 = this.A((Map<?, ?>)object22, "icon", true);
            livingEntity.addPotionEffect(new PotionEffect(object, n2, n3, bl2, bl, bl3));
        }
        Iterator iterator = this.getSoundConfig();
        if (iterator != null && iterator.getBoolean("enabled", true)) {
            object22 = iterator.getString("sound", "entity.slime.squish");
            float f2 = (float)iterator.getDouble("volume", 1.0);
            float f3 = (float)iterator.getDouble("pitch", 0.5);
            try {
                Sound sound = Sound.valueOf((String)object22.toUpperCase().replace((CharSequence)".", (CharSequence)"_"));
                entity.getWorld().playSound(entity.getLocation(), sound, f2, f3);
            }
            catch (IllegalArgumentException illegalArgumentException) {
                entity.getWorld().playSound(entity.getLocation(), (String)object22, f2, f3);
            }
        }
        if (!(object22 = this.getAttackerChat().replace((CharSequence)"{TARGET}", (CharSequence)(entity instanceof Player ? ((Player)entity).getName() : "entity"))).isEmpty()) {
            player.sendMessage(A.C((String)object22));
        }
        string = this.getAttackerTitle();
        object = this.getAttackerSubtitle().replace((CharSequence)"{TARGET}", (CharSequence)(entity instanceof Player ? ((Player)entity).getName() : "entity"));
        if (!string.isEmpty() || !object.isEmpty()) {
            player.sendTitle(A.C(string), A.C((String)object), 10, 60, 20);
        }
        if (entity instanceof Player) {
            Player player2 = (Player)entity;
            String string2 = this.getTargetChat().replace((CharSequence)"{PLAYER}", (CharSequence)player.getName());
            if (!string2.isEmpty()) {
                player2.sendMessage(A.C(string2));
            }
            String string3 = this.getTargetTitle();
            String string4 = this.getTargetSubtitle().replace((CharSequence)"{PLAYER}", (CharSequence)player.getName());
            if (!string3.isEmpty() || !string4.isEmpty()) {
                player2.sendTitle(A.C(string3), A.C(string4), 10, 60, 20);
            }
        }
    }

    private int A(Map<?, ?> map, String string, int n2) {
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

    private boolean A(Map<?, ?> map, String string, boolean bl) {
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
}

