/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Double
 *  java.lang.NumberFormatException
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.Arrays
 *  java.util.HashMap
 *  java.util.List
 *  java.util.Map
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.command.TabCompleter
 */
package me.anarchiacore.customitems.stormitemy.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import me.anarchiacore.customitems.stormitemy.Main;
import me.anarchiacore.customitems.stormitemy.utils.language.A;

public class F
implements CommandExecutor,
TabCompleter {
    private final Main A;

    public F(Main main) {
        this.A = main;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("stormitemy.customhit")) {
            me.anarchiacore.customitems.stormitemy.utils.language.A.A(sender, "customhit_no_permission");
            return true;
        }
        if (args.length == 0) {
            this.A(sender);
            return true;
        }
        String sub = args[0].toLowerCase();
        switch (sub) {
            case "set" -> {
                if (args.length < 2) {
                    this.A(sender);
                    return true;
                }
                this.A(sender, args[1]);
                return true;
            }
            case "info" -> {
                this.B(sender);
                return true;
            }
            default -> {
                this.A(sender);
                return true;
            }
        }
    }

    private void A(CommandSender commandSender, String string) {
        try {
            double d2 = Double.parseDouble((String)string);
            if (d2 < 0.1 || d2 > 10.0) {
                me.anarchiacore.customitems.stormitemy.utils.language.A.A(commandSender, "customhit_invalid_range");
                return;
            }
            this.A.getConfig().set("customhit.damage_multiplier", (Object)d2);
            this.A.saveConfig();
            HashMap hashMap = new HashMap();
            hashMap.put((Object)"MULTIPLIER", (Object)String.valueOf((double)d2));
            me.anarchiacore.customitems.stormitemy.utils.language.A.A(commandSender, "customhit_set_success", (Map<String, String>)hashMap);
        }
        catch (NumberFormatException numberFormatException) {
            me.anarchiacore.customitems.stormitemy.utils.language.A.A(commandSender, "customhit_invalid_number");
        }
    }

    private void B(CommandSender commandSender) {
        double d2 = this.A.getConfig().getDouble("customhit.damage_multiplier", 1.0);
        HashMap hashMap = new HashMap();
        hashMap.put((Object)"MULTIPLIER", (Object)String.valueOf((double)d2));
        me.anarchiacore.customitems.stormitemy.utils.language.A.A(commandSender, "customhit_info", (Map<String, String>)hashMap);
    }

    private void A(CommandSender commandSender) {
        me.anarchiacore.customitems.stormitemy.utils.language.A.A(commandSender, "customhit_help_header");
        me.anarchiacore.customitems.stormitemy.utils.language.A.A(commandSender, "customhit_help_set");
        me.anarchiacore.customitems.stormitemy.utils.language.A.A(commandSender, "customhit_help_info");
    }

    public List<String> onTabComplete(CommandSender commandSender, Command command, String string, String[] stringArray) {
        if (!commandSender.hasPermission("stormitemy.customhit")) {
            return new ArrayList();
        }
        if (stringArray.length == 1) {
            List<String> list = Arrays.asList("set", "info");
            List<String> arrayList = new ArrayList();
            for (String string2 : list) {
                if (!string2.toLowerCase().startsWith(stringArray[0].toLowerCase())) continue;
                arrayList.add(string2);
            }
            return arrayList;
        }
        if (stringArray.length == 2 && stringArray[0].equalsIgnoreCase("set")) {
            return Arrays.asList("1.0", "1.5", "2.0", "2.5", "3.0", "5.0", "10.0");
        }
        return new ArrayList();
    }
}
