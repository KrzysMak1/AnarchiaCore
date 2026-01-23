/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Deprecated
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.util.HashMap
 *  java.util.Map
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 */
package me.anarchiacore.customitems.stormitemy.utils.language;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import me.anarchiacore.customitems.stormitemy.Main;
import me.anarchiacore.customitems.stormitemy.config.C;
import me.anarchiacore.customitems.stormitemy.config.D;

public class A {
    public static String A(Plugin javaPlugin, String string, String string2) {
        C c2;
        Object object;
        try {
            if (D.E() != null && (object = D.E().A("messages." + string)) != null && ((me.anarchiacore.customitems.stormitemy.messages.A)object).D()) {
                return D.C(((me.anarchiacore.customitems.stormitemy.messages.A)object).F());
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        if (javaPlugin instanceof Main && (c2 = ((Main)((Object)(object = (Main)javaPlugin))).getConfigManager()) != null) {
            return c2.A(string, string2);
        }
        return string2;
    }

    public static String A(Plugin javaPlugin, String string) {
        return A.A(javaPlugin, string, "Missing message: " + string);
    }

    @Deprecated
    public static String A(Plugin javaPlugin) {
        try {
            me.anarchiacore.customitems.stormitemy.messages.A a2;
            if (D.E() != null && (a2 = D.E().A("messages.region")) != null && a2.D()) {
                return D.C(a2.F());
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        return A.A(javaPlugin, "region", "&8\u00bb &7Nie mo\u017cesz u\u017cy\u0107 tego przedmiotu w tym &#FB0808regionie&7!");
    }

    public static void A(Player player, String string) {
        try {
            if (D.E() == null) {
                player.sendMessage("\u00a7cLanguage system not initialized! Path: " + string);
                return;
            }
            me.anarchiacore.customitems.stormitemy.messages.A a2 = D.E().A("messages." + string);
            if (a2 == null) {
                player.sendMessage("\u00a7cMessage not found: messages." + string);
                return;
            }
            me.anarchiacore.customitems.stormitemy.messages.C.A(player, a2);
        }
        catch (Exception exception) {
            player.sendMessage("\u00a7cError sending message: " + string);
        }
    }

    public static void A(Player player, String string, Map<String, String> map) {
        try {
            if (D.E() == null) {
                player.sendMessage("\u00a7cLanguage system not initialized! Path: " + string);
                return;
            }
            me.anarchiacore.customitems.stormitemy.messages.A a2 = D.E().C("messages." + string, map);
            if (a2 == null) {
                player.sendMessage("\u00a7cMessage not found: messages." + string);
                return;
            }
            me.anarchiacore.customitems.stormitemy.messages.C.A(player, a2);
        }
        catch (Exception exception) {
            player.sendMessage("\u00a7cError sending message: " + string);
        }
    }

    public static void A(Player player) {
        A.A(player, "region");
    }

    public static void B(Player player) {
        A.A(player, "region_target");
    }

    public static void A(Player player, long l2) {
        HashMap hashMap = new HashMap();
        hashMap.put((Object)"time", (Object)String.valueOf((long)l2));
        A.A(player, "reload", (Map<String, String>)hashMap);
    }

    public static void A(CommandSender commandSender, String string) {
        if (commandSender instanceof Player) {
            A.A((Player)commandSender, string);
        } else {
            try {
                if (D.E() == null) {
                    commandSender.sendMessage("\u00a7cLanguage system not initialized! Path: " + string);
                    return;
                }
                me.anarchiacore.customitems.stormitemy.messages.A a2 = D.E().A("messages." + string);
                if (a2 != null && a2.D()) {
                    commandSender.sendMessage(D.C(a2.F()));
                } else {
                    commandSender.sendMessage("\u00a7cMessage not found: messages." + string);
                }
            }
            catch (Exception exception) {
                commandSender.sendMessage("\u00a7cError sending message: " + string);
            }
        }
    }

    public static void A(CommandSender commandSender, String string, Map<String, String> map) {
        if (commandSender instanceof Player) {
            A.A((Player)commandSender, string, map);
        } else {
            try {
                if (D.E() == null) {
                    commandSender.sendMessage("\u00a7cLanguage system not initialized! Path: " + string);
                    return;
                }
                me.anarchiacore.customitems.stormitemy.messages.A a2 = D.E().C("messages." + string, map);
                if (a2 != null && a2.D()) {
                    commandSender.sendMessage(D.C(a2.F()));
                } else {
                    commandSender.sendMessage("\u00a7cMessage not found: messages." + string);
                }
            }
            catch (Exception exception) {
                commandSender.sendMessage("\u00a7cError sending message: " + string);
            }
        }
    }
}

