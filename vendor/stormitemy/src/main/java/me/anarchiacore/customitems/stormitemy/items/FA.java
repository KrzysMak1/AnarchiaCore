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
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.configuration.ConfigurationSection
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.entity.Egg
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.Projectile
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.entity.ProjectileHitEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.inventory.ItemFlag
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package me.anarchiacore.customitems.stormitemy.items;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import me.anarchiacore.customitems.stormitemy.Main;
import me.anarchiacore.customitems.stormitemy.utils.color.A;

public class FA
implements Listener {
    private final Plugin B;
    private final ConfigurationSection A;

    public FA(Plugin javaPlugin) {
        YamlConfiguration yamlConfiguration;
        File file;
        this.B = javaPlugin;
        javaPlugin.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)javaPlugin);
        File file2 = new File(javaPlugin.getDataFolder(), "items");
        if (!file2.exists()) {
            file2.mkdirs();
        }
        if (!(file = new File(file2, "lewejajko.yml")).exists()) {
            try {
                file.createNewFile();
                yamlConfiguration = new YamlConfiguration();
                String string = "=== Konfiguracja przedmiotu 'Lewe Jajko' ===\nOpis: Plik konfiguracyjny okre\u015bla w\u0142a\u015bciwo\u015bci przedmiotu eventowego 'Lewe Jajko'.\nDost\u0119pne zmienne (placeholdery):\n  {TIME}         - pozosta\u0142y czas cooldownu (w sekundach)\n  {SHOOTER}      - nazwa gracza, kt\u00f3ry rzuci\u0142 przedmiot\n  {TARGET}       - nazwa gracza, kt\u00f3ry zosta\u0142 trafiony\n";
                yamlConfiguration.options().header(string);
                yamlConfiguration.options().copyHeader(true);
                yamlConfiguration.set("lewejajko.material", (Object)"EGG");
                yamlConfiguration.set("lewejajko.name", (Object)"&eLewe jajko");
                yamlConfiguration.set("lewejajko.lore", (Object)List.of((Object)"&r", (Object)"&8 \u00bb &7Jest to przedmiot z &fwydarzenia", (Object)"&8 \u00bb &fwielkanocnego &7z &f2024 roku&7!", (Object)"&r", (Object)"&8 \u00bb &7Dzi\u0119ki niemu mo\u017cesz wyrzuci\u0107 przeciwnika", (Object)"&8 \u00bb &7w powietrze!", (Object)"&r"));
                yamlConfiguration.set("lewejajko.customModelData", (Object)0);
                yamlConfiguration.set("lewejajko.cooldown", (Object)60);
                yamlConfiguration.set("lewejajko.enchantments", (Object)List.of((Object)"DURABILITY:1"));
                yamlConfiguration.set("lewejajko.flags", (Object)List.of((Object)"HIDE_ENCHANTS", (Object)"HIDE_UNBREAKABLE", (Object)"HIDE_ATTRIBUTES"));
                yamlConfiguration.set("lewejajko.unbreakable", (Object)true);
                yamlConfiguration.set("lewejajko.launchHeight", (Object)60);
                yamlConfiguration.set("lewejajko.launchTime", (Object)1.5);
                yamlConfiguration.set("lewejajko.messages.shooter.title", (Object)"&eLEWE JAJKO");
                yamlConfiguration.set("lewejajko.messages.shooter.subtitle", (Object)"&7Wystrzeli\u0142e\u015b gracza &f{TARGET}&7!");
                yamlConfiguration.set("lewejajko.messages.shooter.chatMessage", (Object)"");
                yamlConfiguration.set("lewejajko.messages.target.title", (Object)"&eLEWE JAJKO");
                yamlConfiguration.set("lewejajko.messages.target.subtitle", (Object)"&7Zosta\u0142e\u015b wystrzelony przez &f{SHOOTER}&7!");
                yamlConfiguration.set("lewejajko.messages.target.chatMessage", (Object)"");
                yamlConfiguration.set("lewejajko.messages.cooldown", (Object)"");
                yamlConfiguration.set("lewejajko.sounds.enabled", (Object)false);
                yamlConfiguration.set("lewejajko.sounds.activation.sound", (Object)"");
                yamlConfiguration.set("lewejajko.sounds.activation.volume", (Object)1.0);
                yamlConfiguration.set("lewejajko.sounds.activation.pitch", (Object)1.0);
                yamlConfiguration.save(file);
            }
            catch (IOException iOException) {
                iOException.printStackTrace();
            }
        }
        yamlConfiguration = YamlConfiguration.loadConfiguration((File)file);
        this.A = yamlConfiguration.getConfigurationSection("lewejajko");
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent playerInteractEvent) {
        Action action = playerInteractEvent.getAction();
        if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
            ItemMeta itemMeta;
            Player player = playerInteractEvent.getPlayer();
            ItemStack itemStack = playerInteractEvent.getItem();
            if (itemStack != null && itemStack.getType() == Material.EGG && (itemMeta = itemStack.getItemMeta()) != null && itemMeta.hasDisplayName() && itemMeta.getDisplayName().equals((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C(this.A.getString("name", "&eLewe jajko")))) {
                if (this.B instanceof Main && ((Main)this.B).isItemDisabledByKey("lewejajko")) {
                    return;
                }
                try {
                    Main main;
                    if (this.B instanceof Main && (main = (Main)this.B).isPlayerInBlockedRegion(player)) {
                        me.anarchiacore.customitems.stormitemy.utils.language.A.A(player);
                        playerInteractEvent.setCancelled(true);
                        return;
                    }
                }
                catch (Exception exception) {
                    // empty catch block
                }
                int n2 = this.A.getInt("cooldown", 10);
                int n3 = n2 * 20;
                if (player.getCooldown(Material.EGG) > 0) {
                    int n4 = player.getCooldown(Material.EGG) / 20;
                    String string = this.A.getString("messages.cooldown", "");
                    if (!string.isEmpty()) {
                        string = string.replace((CharSequence)"{TIME}", (CharSequence)String.valueOf((int)n4));
                        player.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string));
                    }
                    playerInteractEvent.setCancelled(true);
                    return;
                }
                player.setCooldown(Material.EGG, n3);
                playerInteractEvent.setCancelled(true);
                ItemStack itemStack2 = player.getInventory().getItemInMainHand();
                if (itemStack2.getAmount() > 1) {
                    itemStack2.setAmount(itemStack2.getAmount() - 1);
                    player.getInventory().setItemInMainHand(itemStack2);
                } else {
                    player.getInventory().setItemInMainHand(null);
                }
                this.A(player);
                Egg egg = (Egg)player.launchProjectile(Egg.class);
                egg.setMetadata("leweJajko", (MetadataValue)new FixedMetadataValue((Plugin)this.B, (Object)true));
            }
        }
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
            this.B.getLogger().warning("B\u0142\u0105d podczas odtwarzania d\u017awi\u0119ku dla LeweJajko: " + exception.getMessage());
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent projectileHitEvent) {
        Projectile projectile = projectileHitEvent.getEntity();
        if (!(projectile instanceof Egg)) {
            return;
        }
        if (!projectile.hasMetadata("leweJajko")) {
            return;
        }
        if (!(projectile.getShooter() instanceof Player)) {
            return;
        }
        Player player = (Player)projectile.getShooter();
        if (projectileHitEvent.getHitEntity() instanceof Player) {
            final Player player2 = (Player)projectileHitEvent.getHitEntity();
            boolean bl = true;
            try {
                if (this.B instanceof Main) {
                    Main main = (Main)this.B;
                    if (main.isPlayerInBlockedRegion(player)) {
                        bl = false;
                    }
                    if (bl && main.isPlayerInBlockedRegion(player2)) {
                        bl = false;
                    }
                }
            }
            catch (Exception exception) {
                // empty catch block
            }
            if (!bl) {
                return;
            }
            double d2 = this.A.getDouble("launchHeight", 60.0);
            double d3 = this.A.getDouble("launchTime", 1.5);
            final int n2 = (int)(d3 * 20.0);
            final double d4 = d2 / (double)n2;
            new BukkitRunnable(this){
                int ticks = 0;
                final /* synthetic */ FA this$0;
                {
                    this.this$0 = fA;
                }

                public void run() {
                    if (this.ticks >= n2) {
                        this.cancel();
                        return;
                    }
                    player2.teleport(player2.getLocation().add(0.0, d4, 0.0));
                    ++this.ticks;
                }
            }.runTaskTimer((Plugin)this.B, 0L, 1L);
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
            material = Material.valueOf((String)this.A.getString("material", "EGG"));
        }
        catch (IllegalArgumentException illegalArgumentException) {
            material = Material.EGG;
        }
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        String string = me.anarchiacore.customitems.stormitemy.utils.color.A.C(this.A.getString("name", "&eLewe jajko"));
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

