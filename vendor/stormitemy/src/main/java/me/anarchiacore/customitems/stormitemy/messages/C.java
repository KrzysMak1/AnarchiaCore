/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.CharSequence
 *  java.lang.Object
 *  java.lang.String
 *  java.util.Map
 *  java.util.Map$Entry
 *  net.md_5.bungee.api.ChatMessageType
 *  net.md_5.bungee.api.chat.BaseComponent
 *  net.md_5.bungee.api.chat.TextComponent
 *  org.bukkit.entity.Player
 */
package me.anarchiacore.customitems.stormitemy.messages;

import java.util.Map;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import me.anarchiacore.customitems.stormitemy.config.D;
import me.anarchiacore.customitems.stormitemy.messages.A;

public class C {
    public static void A(Player player, A a2) {
        BaseComponent[] baseComponentArray;
        String string;
        if (a2 == null) {
            return;
        }
        if (a2.D()) {
            player.sendMessage(D.C(a2.F()));
        }
        if (a2.H() || a2.K()) {
            string = a2.H() ? D.C(a2.E()) : "";
            baseComponentArray = a2.K() ? D.C(a2.G()) : "";
            player.sendTitle(string, (String)baseComponentArray, a2.I(), a2.A(), a2.B());
        }
        if (a2.J()) {
            string = D.C(a2.C());
            baseComponentArray = TextComponent.fromLegacyText((String)string);
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, baseComponentArray);
        }
    }

    public static void A(Player player, A a2, Map<String, String> map) {
        if (a2 == null) {
            return;
        }
        A a3 = new A._A().D(C.A(a2.F(), map)).A(C.A(a2.E(), map)).B(C.A(a2.G(), map)).C(C.A(a2.C(), map)).A(a2.I()).B(a2.A()).C(a2.B()).A();
        C.A(player, a3);
    }

    private static String A(String string, Map<String, String> map) {
        if (string == null || map == null) {
            return string;
        }
        String string2 = string;
        for (Map.Entry entry : map.entrySet()) {
            string2 = string2.replace((CharSequence)("%" + (String)entry.getKey() + "%"), (CharSequence)entry.getValue()).replace((CharSequence)("{" + (String)entry.getKey() + "}"), (CharSequence)entry.getValue());
        }
        return string2;
    }

    public static void A(Player player, String string) {
        if (string == null || string.isEmpty()) {
            return;
        }
        player.sendMessage(D.C(string));
    }

    public static void A(Player player, String string, Map<String, String> map) {
        if (string == null || string.isEmpty()) {
            return;
        }
        String string2 = C.A(string, map);
        player.sendMessage(D.C(string2));
    }
}

