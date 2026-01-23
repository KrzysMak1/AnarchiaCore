/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.sk89q.worldedit.EditSession
 *  com.sk89q.worldedit.WorldEdit
 *  com.sk89q.worldedit.extent.Extent
 *  com.sk89q.worldedit.extent.clipboard.Clipboard
 *  com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat
 *  com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats
 *  com.sk89q.worldedit.extent.clipboard.io.ClipboardReader
 *  com.sk89q.worldedit.function.operation.Operation
 *  com.sk89q.worldedit.function.operation.Operations
 *  com.sk89q.worldedit.math.BlockVector3
 *  com.sk89q.worldedit.session.ClipboardHolder
 *  com.sk89q.worldedit.world.World
 *  com.sk89q.worldedit.world.block.BaseBlock
 *  com.sk89q.worldedit.world.block.BlockStateHolder
 *  java.io.File
 *  java.io.FileInputStream
 *  java.io.IOException
 *  java.io.InputStream
 *  java.lang.CharSequence
 *  java.lang.Class
 *  java.lang.Exception
 *  java.lang.IllegalArgumentException
 *  java.lang.Integer
 *  java.lang.Math
 *  java.lang.NumberFormatException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.reflect.Method
 *  java.util.ArrayList
 *  java.util.List
 *  java.util.Random
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.Particle
 *  org.bukkit.Sound
 *  org.bukkit.World
 *  org.bukkit.block.Block
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

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.world.block.BaseBlock;
import com.sk89q.worldedit.world.block.BlockStateHolder;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
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

public class CA
implements Listener {
    private final Plugin D;
    private final ConfigurationSection A;
    private Clipboard B;
    private final Random C = new Random();

    public CA(Plugin javaPlugin) {
        YamlConfiguration yamlConfiguration;
        File file;
        File file2;
        this.D = javaPlugin;
        javaPlugin.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)javaPlugin);
        File file3 = new File(javaPlugin.getDataFolder(), "trapanarchia.schem");
        if (!file3.exists()) {
            try {
                if (javaPlugin.getResource("trapanarchia.schem") != null) {
                    javaPlugin.saveResource("trapanarchia.schem", false);
                    Bukkit.getConsoleSender().sendMessage("\u00a78[\u00a72StormItemy\u00a78] \u00a77Wypakowano schemat z pliku JAR do katalogu g\u0142\u00f3wnego pluginu.");
                }
            }
            catch (Exception exception) {
                Bukkit.getConsoleSender().sendMessage("\u00a78[\u00a74StormItemy\u00a78] \u00a77Nie uda\u0142o si\u0119 wypakowa\u0107 schematu z JAR: \u00a7c" + exception.getMessage());
            }
        }
        if (!(file2 = new File(javaPlugin.getDataFolder(), "items")).exists()) {
            file2.mkdirs();
        }
        if (!(file = new File(file2, "turbotrap.yml")).exists()) {
            try {
                file.createNewFile();
                yamlConfiguration = new YamlConfiguration();
                String string = "=== Konfiguracja przedmiotu 'Turbo-Trap' ===\nOpis: Plik konfiguracyjny okre\u015bla w\u0142a\u015bciwo\u015bci przedmiotu 'Turbo-Trap'.\n";
                yamlConfiguration.options().header(string);
                yamlConfiguration.options().copyHeader(true);
                yamlConfiguration.set("turbotrap.material", (Object)"EGG");
                yamlConfiguration.set("turbotrap.name", (Object)"&a&lTurbo-Trap");
                yamlConfiguration.set("turbotrap.lore", (Object)List.of((Object)"&r", (Object)"&8 \u00bb &7Jest to przedmiot zdobyty podczas", (Object)"&8 \u00bb &fWielkanocnego Wydarzenia 2025&7!", (Object)"&r", (Object)"&8 \u00bb &7Podczas wystrzelenia Turbotrapu&7", (Object)"&8 \u00bb &fbudujesz prost\u0105 pu\u0142apk\u0119&7 w miejscu ekspozji!", (Object)"&r"));
                yamlConfiguration.set("turbotrap.customModelData", (Object)50);
                yamlConfiguration.set("turbotrap.cooldown", (Object)120);
                yamlConfiguration.set("turbotrap.enchantments", (Object)List.of((Object)"DURABILITY:1"));
                yamlConfiguration.set("turbotrap.flags", (Object)List.of((Object)"HIDE_ENCHANTS", (Object)"HIDE_UNBREAKABLE", (Object)"HIDE_ATTRIBUTES"));
                yamlConfiguration.set("turbotrap.unbreakable", (Object)true);
                yamlConfiguration.set("turbotrap.animation.enabled", (Object)true);
                yamlConfiguration.set("turbotrap.animation.min_delay", (Object)100);
                yamlConfiguration.set("turbotrap.animation.max_delay", (Object)250);
                yamlConfiguration.set("turbotrap.animation.particles", (Object)true);
                yamlConfiguration.set("turbotrap.animation.sound.enabled", (Object)true);
                yamlConfiguration.set("turbotrap.animation.sound.type", (Object)"BLOCK_STONE_PLACE");
                yamlConfiguration.set("turbotrap.animation.sound.volume", (Object)1.0);
                yamlConfiguration.set("turbotrap.animation.sound.pitch", (Object)0.8);
                yamlConfiguration.set("turbotrap.messages.shooter.title", (Object)"&a&lTURBO-TRAP");
                yamlConfiguration.set("turbotrap.messages.shooter.subtitle", (Object)"&7Stworzy\u0142e\u015b &fpu\u0142apk\u0119&7!");
                yamlConfiguration.set("turbotrap.messages.shooter.chatMessage", (Object)"");
                yamlConfiguration.set("turbotrap.messages.cooldown", (Object)"");
                yamlConfiguration.set("turbotrap.noPlaceRegions", (Object)new ArrayList());
                yamlConfiguration.save(file);
            }
            catch (IOException iOException) {
                iOException.printStackTrace();
            }
        }
        yamlConfiguration = YamlConfiguration.loadConfiguration((File)file);
        this.A = yamlConfiguration.getConfigurationSection("turbotrap");
        this.A();
    }

    private void A() {
        try {
            File file = new File(this.D.getDataFolder(), "items");
            File file2 = new File(file, "trapanarchia.schem");
            if (!file2.exists()) {
                file2 = new File(this.D.getDataFolder(), "trapanarchia.schem");
            }
            if (!file2.exists()) {
                try {
                    file2 = new File(this.D.getDataFolder(), "trapanarchia.schem");
                    if (this.D.getResource("trapanarchia.schem") != null) {
                        this.D.saveResource("trapanarchia.schem", false);
                    }
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
            if (!file2.exists()) {
                return;
            }
            ClipboardFormat clipboardFormat = ClipboardFormats.findByFile((File)file2);
            if (clipboardFormat == null) {
                return;
            }
            try (ClipboardReader clipboardReader = clipboardFormat.getReader((InputStream)new FileInputStream(file2));){
                this.B = clipboardReader.read();
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent playerInteractEvent) {
        Action action = playerInteractEvent.getAction();
        if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
            ItemMeta itemMeta;
            Player player = playerInteractEvent.getPlayer();
            ItemStack itemStack = playerInteractEvent.getItem();
            if (itemStack != null && itemStack.getType() == Material.EGG && (itemMeta = itemStack.getItemMeta()) != null && itemMeta.hasDisplayName() && itemMeta.getDisplayName().equals((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C(this.A.getString("name", "&a&lTurbo-Trap")))) {
                if (this.D instanceof Main && ((Main)this.D).isItemDisabledByKey("turbotrap")) {
                    return;
                }
                try {
                    Main main;
                    if (this.D instanceof Main && (main = (Main)this.D).isPlayerInBlockedRegion(player)) {
                        me.anarchiacore.customitems.stormitemy.utils.language.A.A(player);
                        playerInteractEvent.setCancelled(true);
                        return;
                    }
                }
                catch (Exception exception) {
                    // empty catch block
                }
                int n2 = this.A.getInt("cooldown", 30);
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
                Egg egg = (Egg)player.launchProjectile(Egg.class);
                egg.setMetadata("turboTrap", (MetadataValue)new FixedMetadataValue((Plugin)this.D, (Object)true));
            }
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent projectileHitEvent) {
        Projectile projectile = projectileHitEvent.getEntity();
        if (!(projectile instanceof Egg)) {
            return;
        }
        if (!projectile.hasMetadata("turboTrap")) {
            return;
        }
        if (!(projectile.getShooter() instanceof Player)) {
            return;
        }
        projectileHitEvent.setCancelled(true);
        if (this.B == null) {
            this.D.getLogger().warning("\u00a78\u00a7l[\u00a74\u00a7lStormItemy\u00a78\u00a7l] \u00a7cNie mo\u017cna postawi\u0107 pu\u0142apki: schemat nie zosta\u0142 za\u0142adowany!");
            return;
        }
        Player player = (Player)projectile.getShooter();
        Location location = projectile.getLocation();
        BlockVector3 blockVector3 = this.B.getDimensions();
        int n2 = blockVector3.getBlockX();
        int n3 = blockVector3.getBlockY();
        int n4 = blockVector3.getBlockZ();
        BlockVector3 blockVector32 = this.B.getOrigin();
        BlockVector3 blockVector33 = this.B.getMinimumPoint();
        BlockVector3 blockVector34 = BlockVector3.at((int)location.getBlockX(), (int)location.getBlockY(), (int)location.getBlockZ());
        if (!this.A(location, blockVector34, blockVector3, blockVector32, blockVector33, player)) {
            return;
        }
        boolean bl = this.A.getBoolean("turbotrap.animation.enabled", true);
        if (bl) {
            this.A(location, player);
        } else {
            this.A(location);
        }
        String string = me.anarchiacore.customitems.stormitemy.utils.color.A.C(this.A.getString("messages.shooter.title", ""));
        String string2 = me.anarchiacore.customitems.stormitemy.utils.color.A.C(this.A.getString("messages.shooter.subtitle", ""));
        String string3 = me.anarchiacore.customitems.stormitemy.utils.color.A.C(this.A.getString("messages.shooter.chatMessage", ""));
        if (!string.isEmpty() || !string2.isEmpty()) {
            player.sendTitle(string, string2, 10, 70, 20);
        }
        if (!string3.isEmpty()) {
            player.sendMessage(string3);
        }
        player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1.0f, 0.5f);
    }

    private boolean A(Location location, BlockVector3 blockVector3, BlockVector3 blockVector32, BlockVector3 blockVector33, BlockVector3 blockVector34, Player player) {
        try {
            Main main;
            int n2 = blockVector32.getBlockX();
            int n3 = blockVector32.getBlockY();
            int n4 = blockVector32.getBlockZ();
            List list = this.D.getConfig().getStringList("disabled-regions");
            List list2 = this.A.getStringList("noPlaceRegions");
            if (this.D instanceof Main && (main = (Main)this.D).getRegionManager().A(location)) {
                List<String> list3 = main.getRegionManager().A();
                for (String string : list3) {
                    if (!main.getRegionManager().A(location, string) || list2.contains((Object)string.toLowerCase())) continue;
                    return false;
                }
            }
            for (int i2 = 0; i2 < n2; ++i2) {
                for (int i3 = 0; i3 < n3; ++i3) {
                    for (int i4 = 0; i4 < n4; ++i4) {
                        Main main2;
                        String string;
                        string = blockVector3.add(i2 - (blockVector33.getX() - blockVector34.getX()), i3 - (blockVector33.getY() - blockVector34.getY()), i4 - (blockVector33.getZ() - blockVector34.getZ()));
                        Location location2 = new Location(location.getWorld(), (double)string.getBlockX(), (double)string.getBlockY(), (double)string.getBlockZ());
                        if (!(this.D instanceof Main) || !(main2 = (Main)this.D).getRegionManager().A(location2)) continue;
                        return false;
                    }
                }
            }
            return true;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }

    private void A(Location location) {
        try {
            Class clazz = Class.forName((String)"com.sk89q.worldedit.bukkit.BukkitAdapter");
            Method method = clazz.getMethod("adapt", new Class[]{org.bukkit.World.class});
            World world = (World)method.invoke(null, new Object[]{location.getWorld()});
            try (EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(world, -1);){
                BlockVector3 blockVector3 = this.B.getDimensions();
                BlockVector3 blockVector32 = this.B.getOrigin();
                BlockVector3 blockVector33 = this.B.getMinimumPoint();
                BlockVector3 blockVector34 = BlockVector3.at((int)location.getBlockX(), (int)location.getBlockY(), (int)location.getBlockZ());
                for (int i2 = 0; i2 < blockVector3.getBlockX(); ++i2) {
                    for (int i3 = 0; i3 < blockVector3.getBlockY(); ++i3) {
                        for (int i4 = 0; i4 < blockVector3.getBlockZ(); ++i4) {
                            BlockVector3 blockVector35 = blockVector34.add(i2 - (blockVector32.getX() - blockVector33.getX()), i3 - (blockVector32.getY() - blockVector33.getY()), i4 - (blockVector32.getZ() - blockVector33.getZ()));
                            Location location2 = new Location(location.getWorld(), (double)blockVector35.getX(), (double)blockVector35.getY(), (double)blockVector35.getZ());
                            Block block = location2.getBlock();
                            if (block.getType() != Material.BLUE_GLAZED_TERRACOTTA && block.getType() != Material.BLUE_TERRACOTTA && block.getType() != Material.BUBBLE_CORAL_BLOCK && block.getType() != Material.LIGHT_BLUE_TERRACOTTA && block.getType() != Material.BEDROCK && !block.hasMetadata("hydro_cage_block")) continue;
                            return;
                        }
                    }
                }
                Operation operation = new ClipboardHolder(this.B).createPaste((Extent)editSession).to(BlockVector3.at((int)location.getBlockX(), (int)location.getBlockY(), (int)location.getBlockZ())).ignoreAirBlocks(true).build();
                Operations.complete((Operation)operation);
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void A(final Location location, Player player) {
        try {
            int n2 = this.A.getInt("turbotrap.animation.min_delay", 100);
            int n3 = this.A.getInt("turbotrap.animation.max_delay", 250);
            final boolean bl = this.A.getBoolean("turbotrap.animation.particles", true);
            final boolean bl2 = this.A.getBoolean("turbotrap.animation.sound.enabled", true);
            final String string = this.A.getString("turbotrap.animation.sound.type", "BLOCK_STONE_PLACE");
            final float f2 = (float)this.A.getDouble("turbotrap.animation.sound.volume", 1.0);
            final float f3 = (float)this.A.getDouble("turbotrap.animation.sound.pitch", 0.8);
            Class clazz = Class.forName((String)"com.sk89q.worldedit.bukkit.BukkitAdapter");
            Method method = clazz.getMethod("adapt", new Class[]{org.bukkit.World.class});
            final World world = (World)method.invoke(null, new Object[]{location.getWorld()});
            BlockVector3 blockVector3 = this.B.getDimensions();
            final int n4 = blockVector3.getBlockX();
            final int n5 = blockVector3.getBlockY();
            final int n6 = blockVector3.getBlockZ();
            final BlockVector3 blockVector32 = this.B.getOrigin();
            final BlockVector3 blockVector33 = this.B.getMinimumPoint();
            final BlockVector3 blockVector34 = BlockVector3.at((int)location.getBlockX(), (int)location.getBlockY(), (int)location.getBlockZ());
            new BukkitRunnable(this){
                private int A = 0;
                final /* synthetic */ CA this$0;
                {
                    this.this$0 = cA;
                }

                public void run() {
                    if (this.A >= n5) {
                        this.cancel();
                        return;
                    }
                    try (EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(world, -1);){
                        for (int i2 = 0; i2 < n4; ++i2) {
                            for (int i3 = 0; i3 < n6; ++i3) {
                                BlockVector3 blockVector3 = BlockVector3.at((int)i2, (int)this.A, (int)i3);
                                BlockVector3 blockVector322 = blockVector33.add(blockVector3);
                                BaseBlock baseBlock = this.this$0.B.getFullBlock(blockVector322);
                                if (baseBlock.getBlockType().getMaterial().isAir()) continue;
                                BlockVector3 blockVector332 = blockVector34.add(i2 - (blockVector32.getX() - blockVector33.getX()), this.A - (blockVector32.getY() - blockVector33.getY()), i3 - (blockVector32.getZ() - blockVector33.getZ()));
                                Location location2 = new Location(location.getWorld(), (double)blockVector332.getX(), (double)blockVector332.getY(), (double)blockVector332.getZ());
                                Block block = location2.getBlock();
                                if (block.getType() == Material.BLUE_GLAZED_TERRACOTTA || block.getType() == Material.BLUE_TERRACOTTA || block.getType() == Material.BUBBLE_CORAL_BLOCK || block.getType() == Material.LIGHT_BLUE_TERRACOTTA || block.getType() == Material.BEDROCK || block.hasMetadata("hydro_cage_block")) continue;
                                editSession.setBlock(blockVector332, (BlockStateHolder)baseBlock);
                            }
                        }
                        editSession.flushSession();
                        if (bl) {
                            Location location3 = new Location(location.getWorld(), (double)blockVector34.getX() + (double)n4 / 2.0, (double)(blockVector34.getY() + this.A) + 0.5, (double)blockVector34.getZ() + (double)n6 / 2.0);
                            location.getWorld().spawnParticle(Particle.CLOUD, location3, 20, (double)n4 / 2.0, 0.5, (double)n6 / 2.0, 0.01);
                        }
                        if (bl2) {
                            try {
                                Sound sound = Sound.valueOf((String)string);
                                Location location4 = new Location(location.getWorld(), (double)blockVector34.getX() + (double)n4 / 2.0, (double)(blockVector34.getY() + this.A), (double)blockVector34.getZ() + (double)n6 / 2.0);
                                location.getWorld().playSound(location4, sound, f2, f3 + (this.this$0.C.nextFloat() * 0.4f - 0.2f));
                            }
                            catch (IllegalArgumentException illegalArgumentException) {
                                // empty catch block
                            }
                        }
                        ++this.A;
                    }
                    catch (Exception exception) {
                        exception.printStackTrace();
                        this.cancel();
                    }
                }
            }.runTaskTimer((Plugin)this.D, 0L, (long)Math.max((int)1, (int)(n2 + this.C.nextInt(n3 - n2 + 1))) / 50L);
        }
        catch (Exception exception) {
            exception.printStackTrace();
            this.A(location);
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
        String string = me.anarchiacore.customitems.stormitemy.utils.color.A.C(this.A.getString("name", "&a&lTurbo-Trap"));
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
        itemMeta.setUnbreakable(this.A.getBoolean("unbreakable", true));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}

