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

    /*
     * Exception decompiling
     */
    public boolean onCommand(CommandSender var1_1, Command var2_2, String var3_3, String[] var4_4) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * ob.f0$e
         *     at ob.f0.e(SourceFile:35)
         *     at ob.f0.a(SourceFile:1)
         *     at ob.f0$d.a(SourceFile:68)
         *     at qb.n.i(SourceFile:13)
         *     at qb.e.i(SourceFile:9)
         *     at qb.l.i(SourceFile:14)
         *     at qb.n.i(SourceFile:3)
         *     at ob.f0.g(SourceFile:649)
         *     at ob.f0.d(SourceFile:37)
         *     at ib.f.d(SourceFile:235)
         *     at ib.f.e(SourceFile:7)
         *     at ib.f.c(SourceFile:95)
         *     at rc.f.n(SourceFile:11)
         *     at pc.i.m(SourceFile:5)
         *     at pc.d.K(SourceFile:92)
         *     at pc.d.g0(SourceFile:1)
         *     at fb.b.d(SourceFile:191)
         *     at fb.b.c(SourceFile:145)
         *     at fb.a.a(SourceFile:108)
         *     at com.thesourceofcode.jadec.decompilers.JavaExtractionWorker.decompileWithCFR(SourceFile:76)
         *     at com.thesourceofcode.jadec.decompilers.JavaExtractionWorker.doWork(SourceFile:110)
         *     at com.thesourceofcode.jadec.decompilers.BaseDecompiler.withAttempt(SourceFile:3)
         *     at com.thesourceofcode.jadec.workers.DecompilerWorker.d(SourceFile:53)
         *     at com.thesourceofcode.jadec.workers.DecompilerWorker.b(SourceFile:1)
         *     at e7.a.run(SourceFile:1)
         *     at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1154)
         *     at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:652)
         *     at java.lang.Thread.run(Thread.java:1563)
         */
        throw new IllegalStateException("Decompilation failed");
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
            List list = Arrays.asList((Object[])new String[]{"set", "info"});
            ArrayList arrayList = new ArrayList();
            for (String string2 : list) {
                if (!string2.toLowerCase().startsWith(stringArray[0].toLowerCase())) continue;
                arrayList.add((Object)string2);
            }
            return arrayList;
        }
        if (stringArray.length == 2 && stringArray[0].equalsIgnoreCase("set")) {
            return Arrays.asList((Object[])new String[]{"1.0", "1.5", "2.0", "2.5", "3.0", "5.0", "10.0"});
        }
        return new ArrayList();
    }
}

