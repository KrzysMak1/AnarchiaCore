/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Integer
 *  java.lang.Long
 *  java.lang.NumberFormatException
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.List
 *  org.bukkit.Bukkit
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.command.TabCompleter
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 */
package me.anarchiacore.customitems.stormitemy.commands;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import me.anarchiacore.customitems.stormitemy.Main;
import me.anarchiacore.customitems.stormitemy.utils.color.A;
import me.anarchiacore.customitems.stormitemy.zaczarowania.B;

public class C
implements CommandExecutor,
TabCompleter {
    private final Main C;
    private final me.anarchiacore.customitems.stormitemy.zaczarowania.C A;
    private B B;

    public C(Main main, me.anarchiacore.customitems.stormitemy.zaczarowania.C c2) {
        this.C = main;
        this.A = c2;
        this.B = null;
    }

    public void setEventManager(B b2) {
        this.B = b2;
    }

    public boolean onCommand(CommandSender commandSender, Command command, String string, String[] stringArray) {
        if (stringArray.length < 2) {
            this.A(commandSender);
            return true;
        }
        String string2 = stringArray[1].toLowerCase();
        if (string2.equals((Object)"give")) {
            this.A(commandSender, stringArray);
            return true;
        }
        if (string2.equals((Object)"set")) {
            this.B(commandSender, stringArray);
            return true;
        }
        if (string2.equals((Object)"event")) {
            this.C(commandSender, stringArray);
            return true;
        }
        this.A(commandSender);
        return true;
    }

    private void A(CommandSender commandSender, String[] stringArray) {
        int n2 = 1;
        Player player = null;
        if (commandSender instanceof Player) {
            player = (Player)commandSender;
        }
        if (stringArray.length >= 3) {
            try {
                n2 = Integer.parseInt((String)stringArray[2]);
                if (n2 <= 0) {
                    n2 = 1;
                }
                if (n2 > 64) {
                    n2 = 64;
                }
            }
            catch (NumberFormatException numberFormatException) {
                player = Bukkit.getPlayer((String)stringArray[2]);
            }
        }
        if (stringArray.length >= 4) {
            player = Bukkit.getPlayer((String)stringArray[3]);
        }
        if (player == null) {
            commandSender.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8[&x&D&0&6&0&F&F\ud83e\ude93&8] &7Nie znaleziono gracza!"));
            return;
        }
        ItemStack itemStack = this.A.E();
        itemStack.setAmount(n2);
        player.getInventory().addItem(new ItemStack[]{itemStack});
        commandSender.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8[&x&D&0&6&0&F&F\ud83e\ude93&8] &7Gracz &f" + player.getName() + " &7otrzyma\u0142 &f" + n2 + " &7przedmiot(\u00f3w) Zaczarowanie"));
        if (player != commandSender) {
            player.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8[&x&D&0&6&0&F&F\ud83e\ude93&8] &7Otrzyma\u0142e\u015b &f" + n2 + " &7przedmiot(\u00f3w) Zaczarowanie"));
        }
    }

    private void B(CommandSender commandSender, String[] stringArray) {
        int n2;
        if (stringArray.length < 3) {
            commandSender.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8[&x&D&0&6&0&F&F\ud83e\ude93&8] &7U\u017cycie: &f/stormitemy zaczarowania set <procent> [gracz]"));
            return;
        }
        try {
            n2 = Integer.parseInt((String)stringArray[2]);
            if (n2 < -100) {
                n2 = -100;
            }
            if (n2 > 100) {
                n2 = 100;
            }
        }
        catch (NumberFormatException numberFormatException) {
            commandSender.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8[&x&D&0&6&0&F&F\ud83e\ude93&8] &7Nieprawid\u0142owy format! Podaj liczb\u0119 ca\u0142kowit\u0105 (-100 do 100)"));
            return;
        }
        Player player = null;
        if (stringArray.length >= 4) {
            player = Bukkit.getPlayer((String)stringArray[3]);
        } else if (commandSender instanceof Player) {
            player = (Player)commandSender;
        }
        if (player == null) {
            commandSender.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8[&x&D&0&6&0&F&F\ud83e\ude93&8] &7Nie znaleziono gracza!"));
            return;
        }
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (!this.A.B(itemStack)) {
            commandSender.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8[&x&D&0&6&0&F&F\ud83e\ude93&8] &7Gracz musi trzyma\u0107 miecz w r\u0119ce!"));
            return;
        }
        ItemStack itemStack2 = this.A.A(itemStack, n2);
        player.getInventory().setItemInMainHand(itemStack2);
        commandSender.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8[&x&D&0&6&0&F&F\ud83e\ude93&8] &7Ustawiono &f" + n2 + "% &7dodatkowych obra\u017ce\u0144 dla miecza gracza &f" + player.getName()));
        if (player != commandSender) {
            player.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8[&x&D&0&6&0&F&F\ud83e\ude93&8] &7Tw\u00f3j miecz otrzyma\u0142 &f" + n2 + "% &7dodatkowych obra\u017ce\u0144"));
        }
    }

    private void C(CommandSender commandSender, String[] stringArray) {
        if (stringArray.length < 3) {
            commandSender.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8[&x&D&0&6&0&F&F\ud83e\ude93&8] &7U\u017cycie: &f/stormitemy zaczarowania event <czas>"));
            commandSender.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8[&x&D&0&6&0&F&F\ud83e\ude93&8] &7Przyk\u0142ady: &f30s &8- &f5m &8- &f1h &8- &fstop"));
            return;
        }
        String string = stringArray[2].toLowerCase();
        if (string.equals((Object)"stop")) {
            this.B.F();
            commandSender.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8[&x&D&0&6&0&F&F\ud83e\ude93&8] &7Event szcz\u0119\u015bliwej zmianki zosta\u0142 zatrzymany"));
            return;
        }
        long l2 = this.A(string);
        if (l2 <= 0L) {
            commandSender.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8[&x&D&0&6&0&F&F\ud83e\ude93&8] &7Nieprawid\u0142owy format czasu! U\u017cyj: 30s, 5m, 1h lub stop"));
            return;
        }
        this.B.A(l2);
        commandSender.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8[&x&D&0&6&0&F&F\ud83e\ude93&8] &7Event szcz\u0119\u015bliwej zmianki rozpocz\u0105\u0142 si\u0119 na &f" + l2 + " &7sekund"));
    }

    private long A(String string) {
        if (string.endsWith("s")) {
            try {
                return Long.parseLong((String)string.substring(0, string.length() - 1));
            }
            catch (NumberFormatException numberFormatException) {
                return -1L;
            }
        }
        if (string.endsWith("m")) {
            try {
                return Long.parseLong((String)string.substring(0, string.length() - 1)) * 60L;
            }
            catch (NumberFormatException numberFormatException) {
                return -1L;
            }
        }
        if (string.endsWith("h")) {
            try {
                return Long.parseLong((String)string.substring(0, string.length() - 1)) * 3600L;
            }
            catch (NumberFormatException numberFormatException) {
                return -1L;
            }
        }
        return -1L;
    }

    private void A(CommandSender commandSender) {
        commandSender.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8[&x&D&0&6&0&F&F\ud83e\ude93&8] &7Komendy systemu zaczarowa\u0144:"));
        commandSender.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&f/stormitemy zaczarowania give [ilo\u015b\u0107] [gracz] &8- &7Daj przedmioty zaczarowania"));
        commandSender.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&f/stormitemy zaczarowania set <procent> [gracz] &8- &7Ustaw bonus obra\u017ce\u0144 na mieczu"));
        commandSender.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&f/stormitemy zaczarowania event <czas> &8- &7Uruchom event szcz\u0119\u015bliwej zmianki"));
    }

    public List<String> onTabComplete(CommandSender commandSender, Command command, String string, String[] stringArray) {
        ArrayList arrayList = new ArrayList();
        if (stringArray.length == 2) {
            arrayList.add((Object)"give");
            arrayList.add((Object)"set");
            arrayList.add((Object)"event");
            return this.A((List<String>)arrayList, stringArray[1]);
        }
        if (stringArray.length == 3) {
            if (stringArray[1].equalsIgnoreCase("give")) {
                arrayList.add((Object)"1");
                arrayList.add((Object)"5");
                arrayList.add((Object)"10");
                for (Player player : Bukkit.getOnlinePlayers()) {
                    arrayList.add((Object)player.getName());
                }
            } else if (stringArray[1].equalsIgnoreCase("set")) {
                arrayList.add((Object)"-20");
                arrayList.add((Object)"0");
                arrayList.add((Object)"25");
                arrayList.add((Object)"50");
            } else if (stringArray[1].equalsIgnoreCase("event")) {
                arrayList.add((Object)"30s");
                arrayList.add((Object)"1m");
                arrayList.add((Object)"5m");
                arrayList.add((Object)"10m");
                arrayList.add((Object)"1h");
                arrayList.add((Object)"stop");
            }
            return this.A((List<String>)arrayList, stringArray[2]);
        }
        if (stringArray.length == 4) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                arrayList.add((Object)player.getName());
            }
            return this.A((List<String>)arrayList, stringArray[3]);
        }
        return arrayList;
    }

    private List<String> A(List<String> list, String string) {
        if (string.isEmpty()) {
            return list;
        }
        ArrayList arrayList = new ArrayList();
        for (String string2 : list) {
            if (!string2.toLowerCase().startsWith(string.toLowerCase())) continue;
            arrayList.add((Object)string2);
        }
        return arrayList;
    }
}

