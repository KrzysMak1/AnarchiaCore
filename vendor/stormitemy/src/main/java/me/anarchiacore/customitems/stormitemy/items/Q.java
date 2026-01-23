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
 *  java.util.List
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.configuration.ConfigurationSection
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.entity.Arrow
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.Projectile
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityShootBowEvent
 *  org.bukkit.event.entity.ProjectileHitEvent
 *  org.bukkit.inventory.ItemFlag
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.Plugin
 */
package me.anarchiacore.customitems.stormitemy.items;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.Plugin;
import me.anarchiacore.customitems.stormitemy.Main;
import me.anarchiacore.customitems.stormitemy.utils.color.A;

public class q
implements Listener {
    private final Plugin B;
    private final ConfigurationSection A;

    public q(Plugin javaPlugin) {
        YamlConfiguration yamlConfiguration;
        File file;
        this.B = javaPlugin;
        javaPlugin.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)javaPlugin);
        File file2 = new File(javaPlugin.getDataFolder(), "items");
        if (!file2.exists()) {
            file2.mkdirs();
        }
        if (!(file = new File(file2, "marchewkowakusza.yml")).exists()) {
            try {
                file.createNewFile();
                yamlConfiguration = new YamlConfiguration();
                String string = "=== Konfiguracja przedmiotu 'Marchewkowa kusza' ===\nOpis: Plik konfiguracyjny okre\u015bla w\u0142a\u015bciwo\u015bci przedmiotu eventowego 'Marchewkowa kusza'.\nDost\u0119pne zmienne (placeholdery):\n  {TIME}         - pozosta\u0142y czas cooldownu (w sekundach)\n  {SHOOTER}      - nazwa gracza, kt\u00f3ry uderzy\u0142\n  {TARGET}       - nazwa gracza, kt\u00f3ry zosta\u0142 trafiony\n";
                yamlConfiguration.options().header(string);
                yamlConfiguration.options().copyHeader(true);
                yamlConfiguration.set("marchewkowakusza.material", (Object)"CROSSBOW");
                yamlConfiguration.set("marchewkowakusza.name", (Object)"&6&lMarchewkowa kusza");
                yamlConfiguration.set("marchewkowakusza.lore", (Object)List.of((Object)"&r", (Object)"&8 \u00bb &7Jest to przedmiot z &fwielkanocnego", (Object)"&8 \u00bb &fwydarzenia &7z &e2024 roku&7!", (Object)"&r", (Object)"&8 \u00bb &7Ta &aniezwyk\u0142a &7kusza zapewnia ci", (Object)"&8 \u00bb &7zdumiewaj\u0105c\u0105 zdolno\u015b\u0107 przyci\u0105gania", (Object)"&8 \u00bb &7do siebie &egraczy&7, tworz\u0105c zupe\u0142nie", (Object)"&8 \u00bb &7nowe mo\u017cliwo\u015bci eksploracji i walki!", (Object)"&r"));
                yamlConfiguration.set("marchewkowakusza.customModelData", (Object)1);
                yamlConfiguration.set("marchewkowakusza.cooldown", (Object)60);
                yamlConfiguration.set("marchewkowakusza.enchantments", (Object)List.of((Object)"QUICK_CHARGE:1", (Object)"PIERCING:2", (Object)"UNBREAKING:3"));
                yamlConfiguration.set("marchewkowakusza.flags", (Object)List.of((Object)"HIDE_ENCHANTS", (Object)"HIDE_UNBREAKABLE", (Object)"HIDE_ATTRIBUTES"));
                yamlConfiguration.set("marchewkowakusza.unbreakable", (Object)true);
                yamlConfiguration.set("marchewkowakusza.messages.shooter.title", (Object)"&6&lMARCHEWKOWA KUSZA");
                yamlConfiguration.set("marchewkowakusza.messages.shooter.subtitle", (Object)"&7Przyci\u0105gn\u0105\u0142e\u015b do siebie &f{TARGET}&7!");
                yamlConfiguration.set("marchewkowakusza.messages.shooter.chatMessage", (Object)"");
                yamlConfiguration.set("marchewkowakusza.messages.target.title", (Object)"&6&lMARCHEWKOWA KUSZA");
                yamlConfiguration.set("marchewkowakusza.messages.target.subtitle", (Object)"&7Zosta\u0142e\u015b przyci\u0105gni\u0119ty przez &f{SHOOTER}&7!");
                yamlConfiguration.set("marchewkowakusza.messages.target.chatMessage", (Object)"");
                yamlConfiguration.set("marchewkowakusza.sounds.enabled", (Object)false);
                yamlConfiguration.set("marchewkowakusza.sounds.activation.sound", (Object)"");
                yamlConfiguration.set("marchewkowakusza.sounds.activation.volume", (Object)1.0);
                yamlConfiguration.set("marchewkowakusza.sounds.activation.pitch", (Object)1.0);
                yamlConfiguration.save(file);
            }
            catch (IOException iOException) {
                iOException.printStackTrace();
            }
        }
        yamlConfiguration = YamlConfiguration.loadConfiguration((File)file);
        this.A = yamlConfiguration.getConfigurationSection("marchewkowakusza");
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
            this.B.getLogger().warning("B\u0142\u0105d podczas odtwarzania d\u017awi\u0119ku dla MarchewkowaKusza: " + exception.getMessage());
        }
    }

    @EventHandler
    public void onEntityShootBow(EntityShootBowEvent entityShootBowEvent) {
        if (!(entityShootBowEvent.getEntity() instanceof Player)) {
            return;
        }
        Player player = (Player)entityShootBowEvent.getEntity();
        ItemStack itemStack = entityShootBowEvent.getBow();
        if (itemStack == null || itemStack.getType() != Material.CROSSBOW) {
            return;
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null || !itemMeta.hasDisplayName()) {
            return;
        }
        if (!itemMeta.getDisplayName().equals((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C(this.A.getString("name", "&6&lMarchewkowa kusza")))) {
            return;
        }
        if (this.B instanceof Main && ((Main)this.B).isItemDisabledByKey("marchewkowakusza")) {
            return;
        }
        this.A(player);
        Projectile projectile = (Projectile)entityShootBowEvent.getProjectile();
        if (projectile != null) {
            projectile.setMetadata("marchewkowaKuszaArrow", (MetadataValue)new FixedMetadataValue((Plugin)this.B, (Object)true));
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent projectileHitEvent) {
        Projectile projectile = projectileHitEvent.getEntity();
        if (!(projectile instanceof Arrow)) {
            return;
        }
        Arrow arrow = (Arrow)projectile;
        if (!arrow.hasMetadata("marchewkowaKuszaArrow")) {
            return;
        }
        if (!(arrow.getShooter() instanceof Player)) {
            return;
        }
        Player player = (Player)arrow.getShooter();
        if (player.getCooldown(Material.CROSSBOW) > 0) {
            return;
        }
        Entity entity = projectileHitEvent.getHitEntity();
        if (entity instanceof Player) {
            Object object;
            Player player2 = (Player)entity;
            boolean bl = false;
            try {
                if (this.B instanceof Main && ((Main)((Object)(object = (Main)this.B))).isPlayerInBlockedRegion(player2)) {
                    bl = true;
                }
            }
            catch (Exception exception) {
                // empty catch block
            }
            if (bl) {
                me.anarchiacore.customitems.stormitemy.utils.language.A.A(player);
                return;
            }
            object = player.getLocation();
            player2.teleport((Location)object);
            player.setCooldown(Material.CROSSBOW, this.A.getInt("cooldown", 60) * 20);
            String string = this.A.getString("messages.shooter.title", "");
            String string2 = this.A.getString("messages.shooter.subtitle", "");
            String string3 = this.A.getString("messages.shooter.chatMessage", "");
            string = string.replace((CharSequence)"{TARGET}", (CharSequence)player2.getName());
            string2 = string2.replace((CharSequence)"{TARGET}", (CharSequence)player2.getName());
            string3 = string3.replace((CharSequence)"{TARGET}", (CharSequence)player2.getName());
            if (!string.isEmpty() || !string2.isEmpty()) {
                player.sendTitle(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string), me.anarchiacore.customitems.stormitemy.utils.color.A.C(string2), 10, 70, 20);
            }
            if (!string3.isEmpty()) {
                player.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string3));
            }
            String string4 = this.A.getString("messages.target.title", "");
            String string5 = this.A.getString("messages.target.subtitle", "");
            String string6 = this.A.getString("messages.target.chatMessage", "");
            string4 = string4.replace((CharSequence)"{SHOOTER}", (CharSequence)player.getName());
            string5 = string5.replace((CharSequence)"{SHOOTER}", (CharSequence)player.getName());
            string6 = string6.replace((CharSequence)"{SHOOTER}", (CharSequence)player.getName());
            if (!string4.isEmpty() || !string5.isEmpty()) {
                player2.sendTitle(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string4), me.anarchiacore.customitems.stormitemy.utils.color.A.C(string5), 10, 70, 20);
            }
            if (!string6.isEmpty()) {
                player2.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string6));
            }
        }
    }

    public ItemStack getItem() {
        Material material;
        try {
            material = Material.valueOf((String)this.A.getString("material", "CROSSBOW"));
        }
        catch (IllegalArgumentException illegalArgumentException) {
            material = Material.CROSSBOW;
        }
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        String string = me.anarchiacore.customitems.stormitemy.utils.color.A.C(this.A.getString("name", "&6&lMarchewkowa kusza"));
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

