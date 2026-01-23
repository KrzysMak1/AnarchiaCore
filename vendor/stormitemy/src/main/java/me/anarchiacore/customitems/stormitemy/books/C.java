/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.Double
 *  java.lang.Exception
 *  java.lang.IllegalArgumentException
 *  java.lang.Integer
 *  java.lang.NoSuchFieldError
 *  java.lang.NoSuchMethodError
 *  java.lang.Object
 *  java.lang.String
 *  java.util.HashMap
 *  java.util.Iterator
 *  java.util.Map
 *  java.util.Random
 *  java.util.UUID
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.NamespacedKey
 *  org.bukkit.Sound
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.event.inventory.InventoryCloseEvent
 *  org.bukkit.event.inventory.InventoryType
 *  org.bukkit.event.inventory.PrepareAnvilEvent
 *  org.bukkit.inventory.AnvilInventory
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.persistence.PersistentDataContainer
 *  org.bukkit.persistence.PersistentDataType
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package me.anarchiacore.customitems.stormitemy.books;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import me.anarchiacore.customitems.stormitemy.Main;
import me.anarchiacore.customitems.stormitemy.books.A;
import me.anarchiacore.customitems.stormitemy.books.D;

public class C
implements Listener {
    private final Main D;
    private final A A;
    private final Random C;
    private final Map<UUID, ItemStack> B;

    public C(Main main, A a2) {
        this.D = main;
        this.A = a2;
        this.C = new Random();
        this.B = new HashMap();
        Bukkit.getPluginManager().registerEvents((Listener)this, (Plugin)main);
    }

    @EventHandler(priority=EventPriority.HIGH)
    public void onPrepareAnvil(PrepareAnvilEvent prepareAnvilEvent) {
        final AnvilInventory anvilInventory = prepareAnvilEvent.getInventory();
        final ItemStack itemStack = anvilInventory.getItem(0);
        final ItemStack itemStack2 = anvilInventory.getItem(1);
        String string = this.A();
        boolean bl = !this.A(string, "1.18");
        boolean bl2 = this.A(string, "1.20");
        if (itemStack != null && itemStack2 != null && this.A.C(itemStack2)) {
            D d2 = this.A.A(itemStack2);
            if (d2 != null && d2.canApplyToItem(itemStack)) {
                String string2 = d2.getIdentifier();
                if (this.A.A(itemStack, string2)) {
                    prepareAnvilEvent.setResult(null);
                    return;
                }
                boolean bl3 = false;
                for (D d3 : this.A.C()) {
                    String string3 = d3.getIdentifier();
                    if (!this.A.A(itemStack, string3) || !d2.isIncompatibleWith(d3) && !d3.isIncompatibleWith(d2)) continue;
                    bl3 = true;
                    break;
                }
                if (bl3) {
                    prepareAnvilEvent.setResult(null);
                    return;
                }
                Iterator iterator = itemStack.clone();
                d2.applyEffectToItem((ItemStack)iterator);
                if (prepareAnvilEvent.getView().getPlayer() instanceof Player) {
                    D d3;
                    d3 = (Player)prepareAnvilEvent.getView().getPlayer();
                    this.B.put((Object)d3.getUniqueId(), (Object)iterator.clone());
                }
                prepareAnvilEvent.setResult((ItemStack)iterator);
                if (bl) {
                    new BukkitRunnable(this, (ItemStack)iterator){
                        int attempts = 0;
                        final /* synthetic */ ItemStack val$result;
                        final /* synthetic */ C this$0;
                        {
                            this.val$result = itemStack;
                            this.this$0 = c2;
                        }

                        public void run() {
                            if (this.attempts >= 10 || anvilInventory.getViewers().isEmpty()) {
                                this.cancel();
                                return;
                            }
                            anvilInventory.setItem(2, this.val$result.clone());
                            ++this.attempts;
                        }
                    }.runTaskTimer((Plugin)this.D, 0L, 1L);
                    new BukkitRunnable(this, (ItemStack)iterator){
                        final /* synthetic */ ItemStack val$result;
                        final /* synthetic */ C this$0;
                        {
                            this.val$result = itemStack;
                            this.this$0 = c2;
                        }

                        public void run() {
                            if (anvilInventory.getViewers().isEmpty()) {
                                this.cancel();
                                return;
                            }
                            ItemStack itemStack = anvilInventory.getItem(2);
                            if (itemStack == null || !itemStack.isSimilar(this.val$result)) {
                                anvilInventory.setItem(2, this.val$result.clone());
                            }
                        }
                    }.runTaskTimer((Plugin)this.D, 0L, 5L);
                } else if (bl2) {
                    new BukkitRunnable(this, (ItemStack)iterator){
                        final /* synthetic */ ItemStack val$result;
                        final /* synthetic */ C this$0;
                        {
                            this.val$result = itemStack3;
                            this.this$0 = c2;
                        }

                        public void run() {
                            if (anvilInventory.getViewers().isEmpty()) {
                                return;
                            }
                            ItemStack itemStack3 = anvilInventory.getItem(0);
                            ItemStack itemStack22 = anvilInventory.getItem(1);
                            if (itemStack3 != null && itemStack22 != null && itemStack3.isSimilar(itemStack) && itemStack22.isSimilar(itemStack2)) {
                                anvilInventory.setItem(2, this.val$result);
                                new BukkitRunnable(){

                                    public void run() {
                                        if (!anvilInventory.getViewers().isEmpty()) {
                                            anvilInventory.setItem(2, val$result);
                                        }
                                    }
                                }.runTaskLater((Plugin)this.this$0.D, 2L);
                            }
                        }
                    }.runTaskLater((Plugin)this.D, 1L);
                } else {
                    new BukkitRunnable(this, (ItemStack)iterator){
                        final /* synthetic */ ItemStack val$result;
                        final /* synthetic */ C this$0;
                        {
                            this.val$result = itemStack3;
                            this.this$0 = c2;
                        }

                        public void run() {
                            if (anvilInventory.getViewers().isEmpty()) {
                                return;
                            }
                            ItemStack itemStack3 = anvilInventory.getItem(0);
                            ItemStack itemStack22 = anvilInventory.getItem(1);
                            if (itemStack3 != null && itemStack22 != null && itemStack3.isSimilar(itemStack) && itemStack22.isSimilar(itemStack2)) {
                                anvilInventory.setItem(2, this.val$result);
                            }
                        }
                    }.runTask((Plugin)this.D);
                }
                try {
                    anvilInventory.setRepairCost(1);
                }
                catch (NoSuchMethodError noSuchMethodError) {
                    try {
                        anvilInventory.getClass().getMethod("setRepairCost", new Class[]{Integer.TYPE}).invoke((Object)anvilInventory, new Object[]{1});
                    }
                    catch (Exception exception) {}
                }
            } else {
                prepareAnvilEvent.setResult(null);
            }
        }
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent inventoryClickEvent) {
        Player player;
        if (inventoryClickEvent.getInventory().getType() != InventoryType.ANVIL) {
            return;
        }
        if (inventoryClickEvent.getRawSlot() == 2 && inventoryClickEvent.getWhoClicked() instanceof Player) {
            player = (Player)inventoryClickEvent.getWhoClicked();
            final AnvilInventory anvilInventory = (AnvilInventory)inventoryClickEvent.getInventory();
            ItemStack itemStack = inventoryClickEvent.getCurrentItem();
            final ItemStack itemStack2 = anvilInventory.getItem(0);
            final ItemStack itemStack3 = anvilInventory.getItem(1);
            ItemStack itemStack4 = (ItemStack)this.B.get((Object)player.getUniqueId());
            String string = this.A();
            boolean bl = !this.A(string, "1.18");
            boolean bl2 = this.A(string, "1.20");
            if (itemStack2 != null && itemStack3 != null && this.A.C(itemStack3) && itemStack4 != null) {
                D d2 = this.A.A(itemStack3);
                if (d2 == null || !d2.canApplyToItem(itemStack2)) {
                    return;
                }
                if (bl) {
                    inventoryClickEvent.setCancelled(true);
                    ItemStack itemStack5 = itemStack4.clone();
                    if (player.getInventory().firstEmpty() != -1 || inventoryClickEvent.getCursor() == null || inventoryClickEvent.getCursor().getType() == Material.AIR) {
                        ItemStack itemStack6;
                        anvilInventory.setItem(0, null);
                        anvilInventory.setItem(1, null);
                        anvilInventory.setItem(2, null);
                        if (itemStack2.getAmount() > 1) {
                            itemStack6 = itemStack2.clone();
                            itemStack6.setAmount(itemStack2.getAmount() - 1);
                            anvilInventory.setItem(0, itemStack6);
                        }
                        if (itemStack3.getAmount() > 1) {
                            itemStack6 = itemStack3.clone();
                            itemStack6.setAmount(itemStack3.getAmount() - 1);
                            anvilInventory.setItem(1, itemStack6);
                        }
                        player.setItemOnCursor(itemStack5);
                        try {
                            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 1.0f, 1.0f);
                        }
                        catch (NoSuchFieldError noSuchFieldError) {
                            try {
                                player.playSound(player.getLocation(), Sound.valueOf((String)"ANVIL_USE"), 1.0f, 1.0f);
                            }
                            catch (Exception exception) {
                                // empty catch block
                            }
                        }
                        this.B.remove((Object)player.getUniqueId());
                        player.updateInventory();
                    } else {
                        player.sendMessage("\u00a7cBrak miejsca w ekwipunku!");
                    }
                } else if (bl2) {
                    inventoryClickEvent.setCancelled(true);
                    final ItemStack itemStack7 = itemStack4.clone();
                    if (player.getInventory().firstEmpty() != -1 || inventoryClickEvent.getCursor() == null || inventoryClickEvent.getCursor().getType() == Material.AIR) {
                        new BukkitRunnable(this){
                            final /* synthetic */ C this$0;
                            {
                                this.this$0 = c2;
                            }

                            public void run() {
                                ItemStack itemStack;
                                anvilInventory.setItem(0, null);
                                anvilInventory.setItem(1, null);
                                anvilInventory.setItem(2, null);
                                if (itemStack2.getAmount() > 1) {
                                    itemStack = itemStack2.clone();
                                    itemStack.setAmount(itemStack2.getAmount() - 1);
                                    anvilInventory.setItem(0, itemStack);
                                }
                                if (itemStack3.getAmount() > 1) {
                                    itemStack = itemStack3.clone();
                                    itemStack.setAmount(itemStack3.getAmount() - 1);
                                    anvilInventory.setItem(1, itemStack);
                                }
                                player.setItemOnCursor(itemStack7);
                                try {
                                    player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 1.0f, 1.0f);
                                }
                                catch (Exception exception) {
                                    // empty catch block
                                }
                                player.updateInventory();
                            }
                        }.runTask((Plugin)this.D);
                        this.B.remove((Object)player.getUniqueId());
                    } else {
                        player.sendMessage("\u00a7cBrak miejsca w ekwipunku!");
                    }
                } else {
                    this.B.remove((Object)player.getUniqueId());
                    new BukkitRunnable(this){
                        final /* synthetic */ C this$0;
                        {
                            this.this$0 = c2;
                        }

                        public void run() {
                            boolean bl;
                            ItemStack itemStack = anvilInventory.getItem(0);
                            ItemStack itemStack22 = anvilInventory.getItem(1);
                            boolean bl2 = itemStack == null || itemStack.getAmount() < itemStack2.getAmount();
                            boolean bl3 = bl = itemStack22 == null || itemStack22.getAmount() < itemStack3.getAmount();
                            if (bl2 || bl) {
                                try {
                                    player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 1.0f, 1.0f);
                                }
                                catch (NoSuchFieldError noSuchFieldError) {
                                    try {
                                        player.playSound(player.getLocation(), Sound.valueOf((String)"ANVIL_USE"), 1.0f, 1.0f);
                                    }
                                    catch (Exception exception) {
                                        // empty catch block
                                    }
                                }
                            }
                        }
                    }.runTask((Plugin)this.D);
                }
            }
        }
        if (inventoryClickEvent.getInventory().getType() == InventoryType.ANVIL && inventoryClickEvent.getRawSlot() >= inventoryClickEvent.getInventory().getSize() && inventoryClickEvent.getWhoClicked() instanceof Player) {
            player = (Player)inventoryClickEvent.getWhoClicked();
            this.B.remove((Object)player.getUniqueId());
        }
    }

    @EventHandler(priority=EventPriority.NORMAL)
    public void onInventoryClose(InventoryCloseEvent inventoryCloseEvent) {
        if (inventoryCloseEvent.getInventory().getType() == InventoryType.ANVIL && inventoryCloseEvent.getPlayer() instanceof Player) {
            Player player = (Player)inventoryCloseEvent.getPlayer();
            this.B.remove((Object)player.getUniqueId());
        }
    }

    @EventHandler(priority=EventPriority.NORMAL)
    public void onGrindstoneClick(InventoryClickEvent inventoryClickEvent) {
        try {
            if (inventoryClickEvent.getInventory().getType() != InventoryType.valueOf((String)"GRINDSTONE")) {
                return;
            }
        }
        catch (IllegalArgumentException illegalArgumentException) {
            return;
        }
        if (inventoryClickEvent.getRawSlot() == 2) {
            Player player = (Player)inventoryClickEvent.getWhoClicked();
            Inventory inventory = inventoryClickEvent.getInventory();
            ItemStack itemStack = inventoryClickEvent.getCurrentItem();
            ItemStack itemStack2 = inventory.getItem(0);
            ItemStack itemStack3 = inventory.getItem(1);
            int n2 = 0;
            if (itemStack2 != null) {
                n2 += this.A.B(itemStack2);
            }
            if (itemStack3 != null) {
                n2 += this.A.B(itemStack3);
            }
            if (itemStack != null && !itemStack.getType().equals((Object)Material.AIR) && n2 > 0) {
                try {
                    player.playSound(player.getLocation(), Sound.valueOf((String)"BLOCK_GRINDSTONE_USE"), 1.0f, 1.0f);
                }
                catch (IllegalArgumentException illegalArgumentException) {
                    // empty catch block
                }
                player.giveExp(n2 * 3);
                inventoryClickEvent.setCancelled(false);
            }
        }
    }

    @EventHandler(priority=EventPriority.NORMAL)
    public void onEntityDamage(EntityDamageByEntityEvent entityDamageByEntityEvent) {
        if (!(entityDamageByEntityEvent.getDamager() instanceof Player)) {
            return;
        }
        Player player = (Player)entityDamageByEntityEvent.getDamager();
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (itemStack == null || itemStack.getType() == Material.AIR) {
            return;
        }
        if (!itemStack.hasItemMeta()) {
            return;
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
        for (D d2 : this.A.C()) {
            String string = d2.getIdentifier();
            NamespacedKey namespacedKey = new NamespacedKey((Plugin)this.D, "enchant_" + string);
            if (!persistentDataContainer.has(namespacedKey, PersistentDataType.DOUBLE)) continue;
            double d3 = (Double)persistentDataContainer.get(namespacedKey, PersistentDataType.DOUBLE);
            if (this.A.B(player.getUniqueId(), string)) continue;
            double d4 = d2.getConfigChance();
            double d5 = this.C.nextDouble() * 100.0;
            if (!(d5 <= d4)) continue;
            d2.handleEffect(itemStack, player, entityDamageByEntityEvent.getEntity(), entityDamageByEntityEvent);
            this.A.A(player.getUniqueId(), string, d2.getCooldownSeconds());
        }
    }

    private String A() {
        try {
            String string = Bukkit.getServer().getClass().getPackage().getName();
            return string.substring(string.lastIndexOf(46) + 1);
        }
        catch (Exception exception) {
            return "v1_17_R1";
        }
    }

    private boolean A(String string, String string2) {
        try {
            String string3 = string.replaceAll("[^0-9_]", "");
            String[] stringArray = string3.split("_");
            String[] stringArray2 = string2.split("\\.");
            if (stringArray.length >= 2 && stringArray2.length >= 2) {
                int n2 = Integer.parseInt((String)stringArray[0]);
                int n3 = Integer.parseInt((String)stringArray[1]);
                int n4 = Integer.parseInt((String)stringArray2[0]);
                int n5 = Integer.parseInt((String)stringArray2[1]);
                if (n2 > n4) {
                    return true;
                }
                if (n2 == n4 && n3 >= n5) {
                    return true;
                }
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        return false;
    }
}

