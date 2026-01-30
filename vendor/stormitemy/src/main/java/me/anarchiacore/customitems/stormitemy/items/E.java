/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.io.File
 *  java.io.IOException
 *  java.lang.Boolean
 *  java.lang.CharSequence
 *  java.lang.Class
 *  java.lang.Exception
 *  java.lang.IllegalArgumentException
 *  java.lang.Integer
 *  java.lang.Iterable
 *  java.lang.Math
 *  java.lang.Number
 *  java.lang.NumberFormatException
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.Iterator
 *  java.util.LinkedHashMap
 *  java.util.List
 *  java.util.Map
 *  org.bukkit.Bukkit
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
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package me.anarchiacore.customitems.stormitemy.items;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import me.anarchiacore.customitems.stormitemy.Main;
import me.anarchiacore.customitems.stormitemy.regions.C;
import me.anarchiacore.customitems.stormitemy.utils.color.A;

public class E
implements Listener {
    private final Plugin C;
    private final ConfigurationSection A;
    private boolean B;

    public E(Plugin javaPlugin) {
        YamlConfiguration yamlConfiguration;
        File file;
        this.C = javaPlugin;
        this.B = Bukkit.getPluginManager().getPlugin("WorldGuard") != null;
        javaPlugin.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)javaPlugin);
        File file2 = new File(javaPlugin.getDataFolder(), "items");
        if (!file2.exists()) {
            file2.mkdirs();
        }
        if (!(file = new File(file2, "lukkupidyna.yml")).exists()) {
            try {
                file.createNewFile();
                yamlConfiguration = new YamlConfiguration();
                String string = "=== Konfiguracja przedmiotu '\u0141uk kupidyna' ===\nOpis: Plik konfiguracyjny okre\u015bla w\u0142a\u015bciwo\u015bci przedmiotu eventowego '\u0141uk kupidyna'.\nDost\u0119pne zmienne (placeholdery):\n  {TIME}    - pozosta\u0142y czas cooldownu (w sekundach)\n  {SHOOTER} - nazwa gracza, kt\u00f3ry strzeli\u0142\n  {TARGET}  - nazwa gracza, kt\u00f3ry zosta\u0142 trafiony\n";
                yamlConfiguration.options().header(string);
                yamlConfiguration.options().copyHeader(true);
                yamlConfiguration.set("luk_kupidyna.material", (Object)"BOW");
                yamlConfiguration.set("luk_kupidyna.name", (Object)"&4\u0141uk kupidyna");
                yamlConfiguration.set("luk_kupidyna.lore", (Object)List.of((Object)"&r", (Object)"&5&lSpecjalny przedmiot!", (Object)"&7Masz szanse, \u017ce trafiony przez", (Object)"&7Ciebie gracz zostanie &do\u015blepiony&7!", (Object)"&r"));
                yamlConfiguration.set("luk_kupidyna.customModelData", (Object)1);
                yamlConfiguration.set("luk_kupidyna.cooldown", (Object)30);
                yamlConfiguration.set("luk_kupidyna.chance", (Object)30);
                yamlConfiguration.set("luk_kupidyna.effectDuration", (Object)5);
                yamlConfiguration.set("luk_kupidyna.enchantments", (Object)List.of((Object)"ARROW_FIRE:1", (Object)"ARROW_DAMAGE:4", (Object)"DURABILITY:3", (Object)"ARROW_INFINITE:1"));
                yamlConfiguration.set("luk_kupidyna.flags", (Object)List.of((Object)"HIDE_ENCHANTS", (Object)"HIDE_UNBREAKABLE", (Object)"HIDE_ATTRIBUTES"));
                yamlConfiguration.set("luk_kupidyna.unbreakable", (Object)true);
                yamlConfiguration.set("luk_kupidyna.messages.shooter.title", (Object)"&4\u0141UK KUPIDYNA");
                yamlConfiguration.set("luk_kupidyna.messages.shooter.subtitle", (Object)"&7O\u015blepi\u0142e\u015b gracza &f{TARGET}&7!");
                yamlConfiguration.set("luk_kupidyna.messages.shooter.chatMessage", (Object)"");
                yamlConfiguration.set("luk_kupidyna.messages.target.title", (Object)"&4\u0141UK KUPIDYNA");
                yamlConfiguration.set("luk_kupidyna.messages.target.subtitle", (Object)"&7Zosta\u0142e\u015b o\u015blepiony przez &f{SHOOTER}&7!");
                yamlConfiguration.set("luk_kupidyna.messages.target.chatMessage", (Object)"");
                yamlConfiguration.set("luk_kupidyna.messages.cooldown", (Object)"");
                ArrayList arrayList = new ArrayList();
                LinkedHashMap linkedHashMap = new LinkedHashMap();
                linkedHashMap.put((Object)"ambient", (Object)false);
                linkedHashMap.put((Object)"duration", (Object)5);
                linkedHashMap.put((Object)"particles", (Object)false);
                linkedHashMap.put((Object)"amplifier", (Object)0);
                linkedHashMap.put((Object)"type", (Object)"BLINDNESS");
                arrayList.add((Object)linkedHashMap);
                yamlConfiguration.set("luk_kupidyna.effects", (Object)arrayList);
                yamlConfiguration.set("luk_kupidyna.sounds.enabled", (Object)false);
                yamlConfiguration.set("luk_kupidyna.sounds.activation.sound", (Object)"");
                yamlConfiguration.set("luk_kupidyna.sounds.activation.volume", (Object)1.0);
                yamlConfiguration.set("luk_kupidyna.sounds.activation.pitch", (Object)1.0);
                yamlConfiguration.save(file);
            }
            catch (IOException iOException) {
                // empty catch block
            }
        }
        yamlConfiguration = YamlConfiguration.loadConfiguration((File)file);
        this.A = yamlConfiguration.getConfigurationSection("luk_kupidyna");
    }

    public E(Main main, C regionManager) {
        this((Plugin)main);
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
            this.C.getLogger().warning("B\u0142\u0105d podczas odtwarzania d\u017awi\u0119ku dla LukKupidyna: " + exception.getMessage());
        }
    }

    @EventHandler
    public void onEntityShootBow(EntityShootBowEvent entityShootBowEvent) {
        if (!(entityShootBowEvent.getEntity() instanceof Player)) {
            return;
        }
        Player player = (Player)entityShootBowEvent.getEntity();
        ItemStack itemStack = entityShootBowEvent.getBow();
        if (itemStack == null) {
            return;
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null || !itemMeta.hasDisplayName()) {
            return;
        }
        if (!itemMeta.getDisplayName().equals((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C(this.A.getString("name", "&4\u0141uk kupidyna")))) {
            return;
        }
        if (this.C instanceof Main && ((Main)this.C).isItemDisabledByKey("lukkupidyna")) {
            return;
        }
        Projectile projectile = (Projectile)entityShootBowEvent.getProjectile();
        if (projectile != null) {
            projectile.setMetadata("lukKupidynaArrow", (MetadataValue)new FixedMetadataValue((Plugin)this.C, (Object)true));
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent projectileHitEvent) {
        if (!(projectileHitEvent.getEntity() instanceof Arrow)) {
            return;
        }
        Arrow arrow = (Arrow)projectileHitEvent.getEntity();
        if (!arrow.hasMetadata("lukKupidynaArrow")) {
            return;
        }
        if (!(arrow.getShooter() instanceof Player)) {
            return;
        }
        Player player = (Player)arrow.getShooter();
        if (player.getCooldown(Material.BOW) > 0) {
            return;
        }
        Entity entity = projectileHitEvent.getHitEntity();
        if (!(entity instanceof Player player2)) {
            return;
        }
        boolean shooterBlocked = false;
        boolean targetBlocked = false;
        try {
            if (this.C instanceof Main main) {
                shooterBlocked = main.isPlayerInBlockedRegion(player);
                targetBlocked = main.isPlayerInBlockedRegion(player2);
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        if (this.B && !shooterBlocked && !targetBlocked) {
            try {
                Class<?> worldGuardClass = Class.forName("com.sk89q.worldguard.WorldGuard");
                Object worldGuard = worldGuardClass.getMethod("getInstance", new Class[0]).invoke(null, new Object[0]);
                Object platform = worldGuardClass.getMethod("getPlatform", new Class[0]).invoke(worldGuard, new Object[0]);
                Object container = platform.getClass().getMethod("getRegionContainer", new Class[0]).invoke(platform, new Object[0]);
                Object query = container.getClass().getMethod("createQuery", new Class[0]).invoke(container, new Object[0]);
                Class<?> bukkitAdapter = Class.forName("com.sk89q.worldedit.bukkit.BukkitAdapter");
                Object playerVector = bukkitAdapter.getMethod("adapt", new Class[]{Location.class}).invoke(null, new Object[]{player.getLocation()});
                Object targetVector = bukkitAdapter.getMethod("adapt", new Class[]{Location.class}).invoke(null, new Object[]{player2.getLocation()});
                Object playerRegions = query.getClass().getMethod("getApplicableRegions", new Class[]{Class.forName("com.sk89q.worldedit.math.BlockVector3")}).invoke(query, new Object[]{playerVector});
                Object targetRegions = query.getClass().getMethod("getApplicableRegions", new Class[]{Class.forName("com.sk89q.worldedit.math.BlockVector3")}).invoke(query, new Object[]{targetVector});
                List<String> disabledRegions = this.C.getConfig().getStringList("disabled-regions");
                shooterBlocked = this.isRegionBlocked(playerRegions, disabledRegions);
                targetBlocked = this.isRegionBlocked(targetRegions, disabledRegions);
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        if (shooterBlocked || targetBlocked) {
            return;
        }
        int chance = this.A.getInt("chance", 30);
        if (Math.random() * 100.0 < (double)chance) {
            List<Map<?, ?>> effects = (List<Map<?, ?>>)(List<?>)this.A.getMapList("effects");
            if (effects != null && !effects.isEmpty()) {
                for (Map<?, ?> effectMap : effects) {
                    String effectTypeName = effectMap.get("type") != null ? effectMap.get("type").toString() : "";
                    PotionEffectType effectType = PotionEffectType.getByName(effectTypeName);
                    if (effectType == null) {
                        continue;
                    }
                    int duration = effectMap.get("duration") instanceof Number ? ((Number)effectMap.get("duration")).intValue() : 5;
                    int amplifier = effectMap.get("amplifier") instanceof Number ? ((Number)effectMap.get("amplifier")).intValue() : 0;
                    boolean ambient = effectMap.get("ambient") instanceof Boolean ? (Boolean)effectMap.get("ambient") : false;
                    boolean particles = effectMap.get("particles") instanceof Boolean ? (Boolean)effectMap.get("particles") : true;
                    player2.addPotionEffect(new PotionEffect(effectType, duration * 20, amplifier, ambient, particles));
                }
            } else {
                int effectDuration = this.A.getInt("effectDuration", 5);
                player2.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, effectDuration * 20, 0));
            }
            player.setCooldown(Material.BOW, this.A.getInt("cooldown", 20) * 20);
            this.A(player);
            String shooterTitle = this.A.getString("messages.shooter.title", "");
            String shooterSubtitle = this.A.getString("messages.shooter.subtitle", "");
            String shooterChatMessage = this.A.getString("messages.shooter.chatMessage", "");
            shooterTitle = shooterTitle.replace("{TARGET}", player2.getName());
            shooterSubtitle = shooterSubtitle.replace("{TARGET}", player2.getName());
            shooterChatMessage = shooterChatMessage.replace("{TARGET}", player2.getName());
            if (!shooterTitle.isEmpty() || !shooterSubtitle.isEmpty()) {
                player.sendTitle(me.anarchiacore.customitems.stormitemy.utils.color.A.C(shooterTitle), me.anarchiacore.customitems.stormitemy.utils.color.A.C(shooterSubtitle), 10, 70, 20);
            }
            if (!shooterChatMessage.isEmpty()) {
                player.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C(shooterChatMessage));
            }
            String targetTitle = this.A.getString("messages.target.title", "");
            String targetSubtitle = this.A.getString("messages.target.subtitle", "");
            String targetChatMessage = this.A.getString("messages.target.chatMessage", "");
            targetTitle = targetTitle.replace("{SHOOTER}", player.getName());
            targetSubtitle = targetSubtitle.replace("{SHOOTER}", player.getName());
            targetChatMessage = targetChatMessage.replace("{SHOOTER}", player.getName());
            if (!targetTitle.isEmpty() || !targetSubtitle.isEmpty()) {
                player2.sendTitle(me.anarchiacore.customitems.stormitemy.utils.color.A.C(targetTitle), me.anarchiacore.customitems.stormitemy.utils.color.A.C(targetSubtitle), 10, 70, 20);
            }
            if (!targetChatMessage.isEmpty()) {
                player2.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C(targetChatMessage));
            }
        }
    }

    private boolean isRegionBlocked(Object regionCollection, List<String> disabledRegions) {
        if (!(regionCollection instanceof Iterable<?> iterable)) {
            return false;
        }
        for (Object region : iterable) {
            try {
                String regionId = String.valueOf(region.getClass().getMethod("getId", new Class[0]).invoke(region, new Object[0]));
                for (String blocked : disabledRegions) {
                    if (regionId.equalsIgnoreCase(blocked)) {
                        return true;
                    }
                }
            }
            catch (Exception exception) {
                return false;
            }
        }
        return false;
    }

    public ItemStack getItem() {
        Material material;
        try {
            material = Material.valueOf((String)this.A.getString("material", "BOW"));
        }
        catch (IllegalArgumentException illegalArgumentException) {
            material = Material.BOW;
        }
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        String string = me.anarchiacore.customitems.stormitemy.utils.color.A.C(this.A.getString("name", "&4\u0141uk kupidyna"));
        itemMeta.setDisplayName(string);
        if (this.A.contains("lore")) {
            List<String> lore = this.A.getStringList("lore");
            List<String> coloredLore = new ArrayList();
            for (String line : lore) {
                coloredLore.add(me.anarchiacore.customitems.stormitemy.utils.color.A.C(line));
            }
            itemMeta.setLore(coloredLore);
        }
        if (this.A.contains("customModelData")) {
            itemMeta.setCustomModelData(Integer.valueOf((int)this.A.getInt("customModelData")));
        }
        if (this.A.contains("enchantments")) {
            for (String enchantmentEntry : this.A.getStringList("enchantments")) {
                Enchantment enchantment;
                String string2;
                String[] stringArray = enchantmentEntry.split(":");
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
            for (String flagName : this.A.getStringList("flags")) {
                try {
                    itemMeta.addItemFlags(new ItemFlag[]{ItemFlag.valueOf(flagName)});
                }
                catch (IllegalArgumentException illegalArgumentException) {}
            }
        }
        itemMeta.setUnbreakable(this.A.getBoolean("unbreakable", false));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
