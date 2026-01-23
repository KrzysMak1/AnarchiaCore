/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Boolean
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.util.HashMap
 *  java.util.Map
 *  java.util.UUID
 *  org.bukkit.Sound
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.inventory.ClickType
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.event.player.AsyncPlayerChatEvent
 *  org.bukkit.plugin.Plugin
 */
package me.anarchiacore.customitems.stormitemy.listeners;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;
import me.anarchiacore.customitems.stormitemy.Main;
import me.anarchiacore.customitems.stormitemy.messages.A;
import me.anarchiacore.customitems.stormitemy.messages.C;
import me.anarchiacore.customitems.stormitemy.ui.gui.A;
import me.anarchiacore.customitems.stormitemy.ui.gui.B;

public class D
implements Listener {
    private final Main C;
    private final A B;
    private final B D;
    private final Map<UUID, Boolean> A = new HashMap();

    public D(Main main, A a2, B b2) {
        this.C = main;
        this.B = a2;
        this.D = b2;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent inventoryClickEvent) {
        String string = inventoryClickEvent.getView().getTitle();
        String string2 = me.anarchiacore.customitems.stormitemy.utils.color.A.C("\u00a7x\u00a7F\u00a7F\u00a7F\u00a79\u00a71\u00a7A\u26a1 \u00a78\ua731\u1d1b\u1d0f\u0280\u1d0d\u026a\u1d1b\u1d07\u1d0d\u028f v1.15");
        String string3 = me.anarchiacore.customitems.stormitemy.utils.color.A.C("\u00a7x\u00a7F\u00a7F\u00a7F\u00a79\u00a71\u00a7A\u26a1 \u00a78\ua731\u1d1b\u1d0f\u0280\u1d0d\u026a\u1d1b\u1d07\u1d0d\u028f v1.15 &#B400FF&l\u1d18\u0280\u1d07\u1d0d\u026a\u1d1c\u1d0d");
        if (string.equals((Object)string2) || string.equals((Object)string3)) {
            Player player = (Player)inventoryClickEvent.getWhoClicked();
            int n2 = inventoryClickEvent.getRawSlot();
            inventoryClickEvent.setCancelled(true);
            switch (n2) {
                case 11: {
                    this.C.getMenuManager().A(player);
                    break;
                }
                case 12: {
                    this.C.getBooksCommand().openBooksMenu(player);
                    break;
                }
                case 13: {
                    this.B.A(player);
                    break;
                }
                case 15: {
                    if (string.equals((Object)string2)) {
                        this.D.A(player);
                        break;
                    }
                    if (!string.equals((Object)string3) || this.C.getPanelCommand() == null) break;
                    this.C.getPanelCommand().openCustomItemsListGUI(player);
                    break;
                }
                case 31: {
                    this.D.A(player);
                }
            }
        } else if (string.equals((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("\u00a7x\u00a7F\u00a7F\u00a7F\u00a79\u00a71\u00a7A\u26a1 &8\u0280\u1d07\u0262\u026a\u1d0f\u0274\u028f"))) {
            Player player = (Player)inventoryClickEvent.getWhoClicked();
            int n3 = inventoryClickEvent.getRawSlot();
            inventoryClickEvent.setCancelled(true);
            if (n3 == 39) {
                this.C.getPanelCommand().openPanelGUI(player);
            } else if (n3 == 41) {
                if (this.C.isWorldGuardPresent()) {
                    player.closeInventory();
                    this.A.put((Object)player.getUniqueId(), (Object)true);
                    player.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8\u00bb &7Wpisz nazw\u0119 &#FFD300regionu &7z pluginu &fWorldGuard&7! &8(&7Wpisz &#FF0000anuluj&7, aby anulowa\u0107&8)&7!"));
                } else {
                    player.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8[&x&B&3&0&0&F&F\ud83e\ude93&8] &cPrzycisk jest wy\u0142\u0105czony! &7Zainstaluj plugin &fWorldGuard &7aby dodawa\u0107 regiony z niego."));
                }
            } else {
                String string4;
                String string5;
                int[] nArray = new int[]{10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25};
                boolean bl = false;
                Object object = nArray;
                int n4 = ((int[])object).length;
                for (int i2 = 0; i2 < n4; ++i2) {
                    int n5 = object[i2];
                    if (n3 != n5) continue;
                    bl = true;
                    break;
                }
                if (bl && inventoryClickEvent.getCurrentItem() != null && (string5 = this.A(string4 = (object = (Object)inventoryClickEvent.getCurrentItem()).getItemMeta().getDisplayName())) != null && inventoryClickEvent.getClick() == ClickType.SHIFT_RIGHT) {
                    this.B.B(player, string5);
                    player.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8\u00bb &#FF0000Usunieto&7 region &f" + string5));
                    this.B.A(player);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent asyncPlayerChatEvent) {
        Player player = asyncPlayerChatEvent.getPlayer();
        UUID uUID = player.getUniqueId();
        if (((Boolean)this.A.getOrDefault((Object)uUID, (Object)false)).booleanValue()) {
            asyncPlayerChatEvent.setCancelled(true);
            String string = asyncPlayerChatEvent.getMessage().trim();
            if (string.equalsIgnoreCase("anuluj")) {
                me.anarchiacore.customitems.stormitemy.messages.A a2 = new A._A().B(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&fDodawanie regionu &7zosta\u0142a &#FF0000anulowana&7!")).A(10).B(60).C(15).A();
                this.C.getServer().getScheduler().runTask((Plugin)this.C, () -> {
                    me.anarchiacore.customitems.stormitemy.messages.C.A(player, a2);
                    this.A(player, "ENTITY_ENDERMAN_TELEPORT");
                });
                this.A.remove((Object)uUID);
                return;
            }
            if (!this.C.isWorldGuardPresent()) {
                me.anarchiacore.customitems.stormitemy.messages.A a3 = new A._A().D(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8\u00bb &7Plugin &fWorldGuard &7nie jest &#FF0000zainstalowany&7!")).B(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&fWorldGuard &7nie jest &#FF0000dost\u0119pny&7!")).A(10).B(60).C(15).A();
                this.C.getServer().getScheduler().runTask((Plugin)this.C, () -> {
                    me.anarchiacore.customitems.stormitemy.messages.C.A(player, a3);
                    this.A(player, "ENTITY_ENDERMAN_TELEPORT");
                });
                this.A.remove((Object)uUID);
                return;
            }
            this.B.A(player, string);
            me.anarchiacore.customitems.stormitemy.messages.A a4 = new A._A().D(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8\u00bb &7Region &f" + string + " &7zosta\u0142 &#27FF00dodany&7!")).B(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&fRegion &7zosta\u0142 &#27FF00dodany&7!")).A(10).B(60).C(15).A();
            this.C.getServer().getScheduler().runTask((Plugin)this.C, () -> {
                me.anarchiacore.customitems.stormitemy.messages.C.A(player, a4);
                this.A(player, "ENTITY_PLAYER_LEVELUP");
                this.B.A(player);
            });
            this.A.remove((Object)uUID);
        }
    }

    private void A(Player player, String string) {
        try {
            player.playSound(player.getLocation(), Sound.valueOf((String)string), 1.0f, 1.0f);
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    private String A(String string) {
        String string2 = me.anarchiacore.customitems.stormitemy.utils.color.A.B(string);
        int n2 = string2.lastIndexOf(" (");
        if (n2 > 0) {
            return string2.substring(0, n2).trim();
        }
        return string2.trim();
    }
}

