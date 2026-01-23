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
 *  java.util.List
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.configuration.ConfigurationSection
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.entity.EnderPearl
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.Projectile
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.entity.ProjectileHitEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.event.player.PlayerTeleportEvent
 *  org.bukkit.event.player.PlayerTeleportEvent$TeleportCause
 *  org.bukkit.inventory.ItemFlag
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 */
package me.anarchiacore.customitems.stormitemy.items;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import me.anarchiacore.customitems.stormitemy.Main;
import me.anarchiacore.customitems.stormitemy.utils.color.A;

public class c
implements Listener {
    private static final String A = "c3Rvcm1jb2Rl";
    private final Plugin C;
    private ConfigurationSection B;
    private final String D = "SmoczaMieczPearl";

    public c(Plugin plugin) {
        YamlConfiguration yamlConfiguration;
        File file;
        this.C = plugin;
        File file2 = new File(plugin.getDataFolder(), "items");
        if (!file2.exists()) {
            file2.mkdirs();
        }
        if (!(file = new File(file2, "smoczymiecz.yml")).exists()) {
            try {
                file.createNewFile();
                yamlConfiguration = new YamlConfiguration();
                String string = "=== Konfiguracja przedmiotu 'Smoczy miecz' ===\nOpis: Plik konfiguracyjny okre\u015bla w\u0142a\u015bciwo\u015bci przedmiotu eventowego 'Smoczy miecz'.\nDost\u0119pne zmienne (placeholdery):\n  {TIME}         - pozosta\u0142y czas cooldownu (w sekundach)\n";
                yamlConfiguration.options().header(string);
                yamlConfiguration.options().copyHeader(true);
                yamlConfiguration.set("smoczymiecz.material", (Object)"NETHERITE_SWORD");
                yamlConfiguration.set("smoczymiecz.name", (Object)"&d&lSmoczy miecz");
                yamlConfiguration.set("smoczymiecz.lore", (Object)List.of((Object)"&r", (Object)"&8 \u00bb &7Posiada on mo\u017cliwo\u015b\u0107 strzelania", (Object)"&8 \u00bb &dper\u0142ami kresu&7!", (Object)"&r"));
                yamlConfiguration.set("smoczymiecz.customModelData", (Object)1);
                yamlConfiguration.set("smoczymiecz.cooldown", (Object)80);
                yamlConfiguration.set("smoczymiecz.enchantments", (Object)List.of((Object)"FIRE_ASPECT:2", (Object)"LOOTING:3", (Object)"MENDING:1", (Object)"DAMAGE_ALL:6", (Object)"DURABILITY:3"));
                yamlConfiguration.set("smoczymiecz.flags", (Object)List.of((Object)"HIDE_ENCHANTS", (Object)"HIDE_UNBREAKABLE", (Object)"HIDE_ATTRIBUTES"));
                yamlConfiguration.set("smoczymiecz.unbreakable", (Object)true);
                yamlConfiguration.set("smoczymiecz.messages.title", (Object)"&d&lSMOCZY MIECZ");
                yamlConfiguration.set("smoczymiecz.messages.subtitle", (Object)"&7U\u017cy\u0142e\u015b &fsmoczego miecza&7!");
                yamlConfiguration.set("smoczymiecz.messages.chatMessage", (Object)"");
                yamlConfiguration.set("smoczymiecz.messages.cooldown", (Object)"");
                yamlConfiguration.set("smoczymiecz.sounds.use.enabled", (Object)true);
                yamlConfiguration.set("smoczymiecz.sounds.use.sound", (Object)"ENTITY_ENDER_PEARL_THROW");
                yamlConfiguration.set("smoczymiecz.sounds.use.volume", (Object)1.0);
                yamlConfiguration.set("smoczymiecz.sounds.use.pitch", (Object)1.0);
                yamlConfiguration.save(file);
            }
            catch (IOException iOException) {
                plugin.getLogger().warning("Nie mo\u017cna utworzy\u0107 pliku smoczymiecz.yml: " + iOException.getMessage());
            }
        }
        yamlConfiguration = YamlConfiguration.loadConfiguration((File)file);
        this.B = yamlConfiguration.getConfigurationSection("smoczymiecz");
        plugin.getServer().getPluginManager().registerEvents((Listener)this, plugin);
    }

    public void reload() {
        try {
            File file = new File(this.C.getDataFolder(), "items/smoczymiecz.yml");
            if (file.exists()) {
                YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration((File)file);
                this.B = yamlConfiguration.getConfigurationSection("smoczymiecz");
                this.C.getLogger().info("Prze\u0142adowano konfiguracj\u0119 przedmiotu 'Smoczy miecz'");
            }
        }
        catch (Exception exception) {
            this.C.getLogger().warning("B\u0142\u0105d podczas prze\u0142adowywania przedmiotu 'Smoczy miecz': " + exception.getMessage());
        }
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
        String string2 = me.anarchiacore.customitems.stormitemy.utils.color.A.C(this.B.getString("name", "&d&lSmoczy miecz"));
        itemMeta.setDisplayName(string2);
        if (this.B.contains("lore")) {
            list = this.B.getStringList("lore");
            for (int i2 = 0; i2 < list.size(); ++i2) {
                list.set(i2, (Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C((String)list.get(i2)));
            }
            itemMeta.setLore(list);
        }
        if (this.B.contains("customModelData")) {
            itemMeta.setCustomModelData(Integer.valueOf((int)this.B.getInt("customModelData")));
        }
        if (this.B.contains("enchantments")) {
            list = this.B.getStringList("enchantments");
            for (String string3 : list) {
                Enchantment enchantment;
                String[] stringArray = string3.split(":");
                if (stringArray.length < 2) continue;
                String string4 = stringArray[0].trim().toUpperCase();
                int n2 = 1;
                try {
                    n2 = Integer.parseInt((String)stringArray[1].trim());
                }
                catch (NumberFormatException numberFormatException) {
                    this.C.getLogger().warning("B\u0142\u0105d parsowania poziomu enchantu: " + string3);
                }
                if ((enchantment = Enchantment.getByName((String)string4)) == null) continue;
                itemMeta.addEnchant(enchantment, n2, true);
            }
        }
        if (this.B.contains("flags")) {
            list = this.B.getStringList("flags");
            for (String string3 : list) {
                try {
                    itemMeta.addItemFlags(new ItemFlag[]{ItemFlag.valueOf((String)string3)});
                }
                catch (IllegalArgumentException illegalArgumentException) {
                    this.C.getLogger().warning("Nieprawid\u0142owa flaga przedmiotu: " + string3);
                }
            }
        }
        itemMeta.setUnbreakable(this.B.getBoolean("unbreakable", false));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private boolean A(ItemStack itemStack) {
        boolean bl;
        Material material;
        if (itemStack == null || !itemStack.hasItemMeta()) {
            return false;
        }
        String string = me.anarchiacore.customitems.stormitemy.utils.color.A.C(this.B.getString("name", "&d&lSmoczy miecz"));
        try {
            material = Material.valueOf((String)this.B.getString("material", "NETHERITE_SWORD"));
        }
        catch (IllegalArgumentException illegalArgumentException) {
            material = Material.NETHERITE_SWORD;
        }
        int n2 = this.B.getInt("customModelData", 0);
        boolean bl2 = bl = itemStack.getType() == material && itemStack.getItemMeta().getDisplayName().equals((Object)string);
        if (itemStack.getItemMeta().hasCustomModelData()) {
            bl = bl && itemStack.getItemMeta().getCustomModelData() == n2;
        }
        return bl;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent playerInteractEvent) {
        Action action = playerInteractEvent.getAction();
        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        Player player = playerInteractEvent.getPlayer();
        ItemStack itemStack = playerInteractEvent.getItem();
        if (itemStack == null) {
            return;
        }
        if (!this.A(itemStack)) {
            return;
        }
        if (this.C instanceof Main && ((Main)this.C).isItemDisabledByKey("smoczymiecz")) {
            return;
        }
        int n2 = this.B.getInt("cooldown", 60);
        if (player.hasCooldown(itemStack.getType())) {
            long l2 = player.getCooldown(itemStack.getType()) / 20;
            String string = this.B.getString("messages.cooldown", "");
            if (!string.isEmpty()) {
                player.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string.replace((CharSequence)"{TIME}", (CharSequence)String.valueOf((long)l2))));
            }
            return;
        }
        me.anarchiacore.customitems.stormitemy.utils.cooldown.A.A(this.C, player, itemStack, n2, "smoczymiecz");
        EnderPearl enderPearl = (EnderPearl)player.launchProjectile(EnderPearl.class);
        enderPearl.setMetadata("SmoczaMieczPearl", (MetadataValue)new FixedMetadataValue(this.C, (Object)true));
        this.A(player, "use");
        String string = this.B.getString("messages.title", "");
        String string2 = this.B.getString("messages.subtitle", "");
        String string3 = this.B.getString("messages.chatMessage", "");
        if (!string.isEmpty() || !string2.isEmpty()) {
            player.sendTitle(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string), me.anarchiacore.customitems.stormitemy.utils.color.A.C(string2), 10, 40, 10);
        }
        if (!string3.isEmpty()) {
            player.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string3));
        }
    }

    @EventHandler
    public void onEnderPearlLand(ProjectileHitEvent projectileHitEvent) {
        Main main;
        Projectile projectile = projectileHitEvent.getEntity();
        if (!(projectile instanceof EnderPearl) || !projectile.hasMetadata("SmoczaMieczPearl")) {
            return;
        }
        if (this.C instanceof Main && (main = (Main)this.C).isTargetBlockInProtectedRegion(projectile.getLocation())) {
            projectile.remove();
            if (projectile.getShooter() instanceof Player) {
                Player player = (Player)projectile.getShooter();
                String string = this.B.getString("messages.blockedRegion", "&cNie mo\u017cesz teleportowa\u0107 si\u0119 w tym miejscu!");
                player.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string));
            }
        }
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent playerTeleportEvent) {
        Main main;
        if (playerTeleportEvent.getCause() != PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
            return;
        }
        if (this.C instanceof Main && (main = (Main)this.C).isTargetBlockInProtectedRegion(playerTeleportEvent.getTo())) {
            playerTeleportEvent.setCancelled(true);
            String string = this.B.getString("messages.blockedRegion", "&cNie mo\u017cesz teleportowa\u0107 si\u0119 w tym miejscu!");
            playerTeleportEvent.getPlayer().sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string));
        }
    }

    private void A(Player player, String string) {
        ConfigurationSection configurationSection = this.B.getConfigurationSection("sounds." + string);
        if (configurationSection == null || !configurationSection.getBoolean("enabled", true)) {
            return;
        }
        String string2 = configurationSection.getString("sound", "ENTITY_ENDER_PEARL_THROW");
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

