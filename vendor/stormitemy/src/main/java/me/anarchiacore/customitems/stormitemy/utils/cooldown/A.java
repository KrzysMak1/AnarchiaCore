/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.CharSequence
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.util.HashMap
 *  java.util.Iterator
 *  java.util.Map
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 */
package me.anarchiacore.customitems.stormitemy.utils.cooldown;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import me.anarchiacore.customitems.stormitemy.Main;
import me.anarchiacore.customitems.stormitemy.messages.B;

public class A {
    private static final String E = "c3Rvcm1jb2Rl";
    private static final Map<String, String> A = new HashMap();
    private static final int D = 100;
    private static long C = 0L;
    private static final long B = 300000L;

    private static void A() {
        long l2 = System.currentTimeMillis();
        if (l2 - C < 300000L) {
            return;
        }
        C = l2;
        if (A.size() > 100) {
            Iterator iterator = A.keySet().iterator();
            for (int i2 = A.size() / 2; iterator.hasNext() && i2 > 0; --i2) {
                iterator.next();
                iterator.remove();
            }
        }
    }

    public static void A(Plugin plugin, Player player, ItemStack itemStack, int n2, String string) {
        player.setCooldown(itemStack.getType(), n2 * 20);
        B b2 = me.anarchiacore.customitems.stormitemy.utils.cooldown.A.A(plugin);
        if (b2 != null && b2.isEnabled()) {
            if (string != null && b2.isPluginItem(string)) {
                b2.registerCooldown(player, string, n2);
            }
            if ("antycobweb".equals((Object)string)) {
                b2.removeCooldown(player, itemStack.getType().name().toLowerCase());
            }
        }
    }

    public static void A(Plugin plugin, Player player, Material material, int n2, String string) {
        B b2;
        player.setCooldown(material, n2);
        if (n2 >= 20 && (b2 = me.anarchiacore.customitems.stormitemy.utils.cooldown.A.A(plugin)) != null && b2.isEnabled() && string != null && b2.isPluginItem(string)) {
            int n3 = n2 / 20;
            b2.registerCooldown(player, string, n3);
            if ("antycobweb".equals((Object)string) && material == Material.FIREWORK_STAR) {
                b2.removeCooldown(player, material.name().toLowerCase());
            }
        }
    }

    private static B A(Plugin plugin) {
        if (plugin instanceof Main) {
            return ((Main)plugin).getActionbarManager();
        }
        return null;
    }

    public static void A(Plugin plugin, Player player, Material material, int n2) {
        if (n2 < 20) {
            player.setCooldown(material, n2);
            return;
        }
        int n3 = n2 / 20;
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        ItemStack itemStack2 = player.getInventory().getItemInOffHand();
        ItemStack itemStack3 = null;
        if (itemStack != null && itemStack.getType() == material) {
            itemStack3 = itemStack;
        } else if (itemStack2 != null && itemStack2.getType() == material) {
            itemStack3 = itemStack2;
        }
        String string = null;
        if (itemStack3 != null && itemStack3.hasItemMeta() && itemStack3.getItemMeta().hasDisplayName()) {
            string = me.anarchiacore.customitems.stormitemy.utils.cooldown.A.A(itemStack3);
        } else {
            String string2;
            if (material == Material.CREEPER_SPAWN_EGG) {
                if (itemStack3 != null && itemStack3.hasItemMeta() && itemStack3.getItemMeta().hasLore() && itemStack3.getItemMeta().getLore().toString().toLowerCase().contains((CharSequence)"zmutowany")) {
                    string = "zmutowanycreeper";
                } else {
                    ItemStack[] itemStackArray;
                    for (ItemStack itemStack4 : itemStackArray = player.getInventory().getContents()) {
                        String string3;
                        if (itemStack4 == null || itemStack4.getType() != Material.CREEPER_SPAWN_EGG || !itemStack4.hasItemMeta() || !itemStack4.getItemMeta().hasDisplayName() || !(string3 = itemStack4.getItemMeta().getDisplayName().toLowerCase()).contains((CharSequence)"zmutowany") || !string3.contains((CharSequence)"creeper")) continue;
                        string = "zmutowanycreeper";
                        break;
                    }
                }
            } else if (material == Material.FIREWORK_STAR && itemStack3 != null && itemStack3.hasItemMeta() && itemStack3.getItemMeta().hasDisplayName() && (string2 = itemStack3.getItemMeta().getDisplayName().toLowerCase()).contains((CharSequence)"anty") && string2.contains((CharSequence)"cobweb")) {
                string = "antycobweb";
            }
            if (string == null) {
                string = material.name().toLowerCase();
            }
        }
        me.anarchiacore.customitems.stormitemy.utils.cooldown.A.A(plugin, player, material, n2, string);
    }

    public static String A(ItemStack itemStack) {
        if (itemStack == null || !itemStack.hasItemMeta() || !itemStack.getItemMeta().hasDisplayName()) {
            return null;
        }
        me.anarchiacore.customitems.stormitemy.utils.cooldown.A.A();
        String string = itemStack.getItemMeta().getDisplayName();
        String string2 = string + "_" + itemStack.getType().name();
        if (A.containsKey((Object)string2)) {
            return (String)A.get((Object)string2);
        }
        String string3 = string.replaceAll("\u00a7[0-9a-fA-FklmnorKLMNOR]", "").toLowerCase();
        if (string3.contains((CharSequence)"balonik") && string3.contains((CharSequence)"helem")) {
            A.put((Object)string2, (Object)"balonikzhelem");
            return "balonikzhelem";
        }
        if (string3.contains((CharSequence)"smoczy") && string3.contains((CharSequence)"miecz")) {
            A.put((Object)string2, (Object)"smoczymiecz");
            return "smoczymiecz";
        }
        if (string3.contains((CharSequence)"bombarda")) {
            A.put((Object)string2, (Object)"bombardamaxima");
            return "bombardamaxima";
        }
        if (string3.contains((CharSequence)"boski") && string3.contains((CharSequence)"top\u00f3r")) {
            A.put((Object)string2, (Object)"boskitopor");
            return "boskitopor";
        }
        if (string3.contains((CharSequence)"\u015bnie\u017cka")) {
            A.put((Object)string2, (Object)"sniezka");
            return "sniezka";
        }
        if (string3.contains((CharSequence)"sple\u015bnia\u0142a") && string3.contains((CharSequence)"kanapka")) {
            A.put((Object)string2, (Object)"splesnialakanapka");
            return "splesnialakanapka";
        }
        if (string3.contains((CharSequence)"turbo") && string3.contains((CharSequence)"trap")) {
            A.put((Object)string2, (Object)"turbotrap");
            return "turbotrap";
        }
        if (string3.contains((CharSequence)"turbo") && string3.contains((CharSequence)"domek")) {
            A.put((Object)string2, (Object)"turbodomek");
            return "turbodomek";
        }
        if (string3.contains((CharSequence)"w\u0119dka") && string3.contains((CharSequence)"surferka")) {
            A.put((Object)string2, (Object)"wedkasurferka");
            return "wedkasurferka";
        }
        if (string3.contains((CharSequence)"w\u0119dka") && string3.contains((CharSequence)"nielota")) {
            A.put((Object)string2, (Object)"wedkanielota");
            return "wedkanielota";
        }
        if (string3.contains((CharSequence)"\u0142opata") && string3.contains((CharSequence)"grinch")) {
            A.put((Object)string2, (Object)"lopatagrincha");
            return "lopatagrincha";
        }
        if (string3.contains((CharSequence)"piernik")) {
            A.put((Object)string2, (Object)"piernik");
            return "piernik";
        }
        if (string3.contains((CharSequence)"ciep\u0142e") && string3.contains((CharSequence)"mleko")) {
            A.put((Object)string2, (Object)"cieplemleko");
            return "cieplemleko";
        }
        if (string3.contains((CharSequence)"r\u00f3\u017ca") && string3.contains((CharSequence)"kupidyn")) {
            A.put((Object)string2, (Object)"rozakupidyna");
            return "rozakupidyna";
        }
        if (string3.contains((CharSequence)"parawan")) {
            A.put((Object)string2, (Object)"parawan");
            return "parawan";
        }
        if (string3.contains((CharSequence)"lizak")) {
            A.put((Object)string2, (Object)"lizak");
            return "lizak";
        }
        if (string3.contains((CharSequence)"r\u00f3zga")) {
            A.put((Object)string2, (Object)"rozga");
            return "rozga";
        }
        if (string3.contains((CharSequence)"kupa") && string3.contains((CharSequence)"anarchi")) {
            A.put((Object)string2, (Object)"kupaanarchi");
            return "kupaanarchi";
        }
        if (string3.contains((CharSequence)"korona") && string3.contains((CharSequence)"anarchi")) {
            A.put((Object)string2, (Object)"koronaanarchi");
            return "koronaanarchi";
        }
        if (string3.contains((CharSequence)"totem") && string3.contains((CharSequence)"u\u0142askawienia")) {
            A.put((Object)string2, (Object)"totemuaskawienia");
            return "totemuaskawienia";
        }
        if (string3.contains((CharSequence)"siekiera") && string3.contains((CharSequence)"grinch")) {
            A.put((Object)string2, (Object)"siekieragrincha");
            return "siekieragrincha";
        }
        if (string3.contains((CharSequence)"\u0142uk") && string3.contains((CharSequence)"kupidyn")) {
            A.put((Object)string2, (Object)"lukkupidyna");
            return "lukkupidyna";
        }
        if (string3.contains((CharSequence)"marchewkowy") && string3.contains((CharSequence)"miecz")) {
            A.put((Object)string2, (Object)"marchewkowymiecz");
            return "marchewkowymiecz";
        }
        if (string3.contains((CharSequence)"anty") && string3.contains((CharSequence)"cobweb")) {
            A.put((Object)string2, (Object)"antycobweb");
            return "antycobweb";
        }
        if (string3.contains((CharSequence)"kosa")) {
            A.put((Object)string2, (Object)"kosa");
            return "kosa";
        }
        if (string3.contains((CharSequence)"excalibur")) {
            A.put((Object)string2, (Object)"excalibur");
            return "excalibur";
        }
        if (string3.contains((CharSequence)"krew") && string3.contains((CharSequence)"wampir")) {
            A.put((Object)string2, (Object)"krewwampira");
            return "krewwampira";
        }
        if (string3.contains((CharSequence)"marchewkowa") && string3.contains((CharSequence)"kusza")) {
            A.put((Object)string2, (Object)"marchewkowakusza");
            return "marchewkowakusza";
        }
        if (string3.contains((CharSequence)"lewe") && string3.contains((CharSequence)"jajko")) {
            A.put((Object)string2, (Object)"lewejajko");
            return "lewejajko";
        }
        if (string3.contains((CharSequence)"zaj\u0119czy") && string3.contains((CharSequence)"miecz")) {
            A.put((Object)string2, (Object)"zajeczymiecz");
            return "zajeczymiecz";
        }
        if (string3.contains((CharSequence)"zatruty") && string3.contains((CharSequence)"o\u0142\u00f3wek")) {
            A.put((Object)string2, (Object)"zatrutyolowek");
            return "zatrutyolowek";
        }
        if (string3.contains((CharSequence)"sakiewka") && string3.contains((CharSequence)"drop")) {
            A.put((Object)string2, (Object)"sakiewkadropu");
            return "sakiewkadropu";
        }
        if (string3.contains((CharSequence)"wampirze") && string3.contains((CharSequence)"jab\u0142ko")) {
            A.put((Object)string2, (Object)"wampirzejablko");
            return "wampirzejablko";
        }
        if (string3.contains((CharSequence)"arcus") && string3.contains((CharSequence)"magnus")) {
            A.put((Object)string2, (Object)"arcusmagnus");
            return "arcusmagnus";
        }
        if (string3.contains((CharSequence)"piekielna") && string3.contains((CharSequence)"tarcza")) {
            A.put((Object)string2, (Object)"piekielnatarcza");
            return "piekielnatarcza";
        }
        if (string3.contains((CharSequence)"kostka") && string3.contains((CharSequence)"rubik")) {
            A.put((Object)string2, (Object)"kostkarubika");
            return "kostkarubika";
        }
        if (string3.contains((CharSequence)"z\u0142amane") && string3.contains((CharSequence)"serce")) {
            A.put((Object)string2, (Object)"zlamaneserce");
            return "zlamaneserce";
        }
        if (string3.contains((CharSequence)"dynamit")) {
            A.put((Object)string2, (Object)"dynamit");
            return "dynamit";
        }
        if (string3.contains((CharSequence)"rozgotowana") && string3.contains((CharSequence)"kukurydz")) {
            A.put((Object)string2, (Object)"rozgotowanakukurydza");
            return "rozgotowanakukurydza";
        }
        if (string3.contains((CharSequence)"zmutowany") && string3.contains((CharSequence)"creeper")) {
            A.put((Object)string2, (Object)"zmutowanycreeper");
            return "zmutowanycreeper";
        }
        if (string3.contains((CharSequence)"wzmocniona") && string3.contains((CharSequence)"elytr")) {
            A.put((Object)string2, (Object)"wzmocnionaeltra");
            return "wzmocnionaeltra";
        }
        if (string3.contains((CharSequence)"anarchiczn") && string3.contains((CharSequence)"miecz")) {
            A.put((Object)string2, (Object)"anarchicznymiecz");
            return "anarchicznymiecz";
        }
        if (string3.contains((CharSequence)"anarchiczn") && string3.contains((CharSequence)"kilof")) {
            A.put((Object)string2, (Object)"anarchicznykilof");
            return "anarchicznykilof";
        }
        if (string3.contains((CharSequence)"antycobweb")) {
            A.put((Object)string2, (Object)"antycobweb");
            return "antycobweb";
        }
        if (string3.contains((CharSequence)"blok") && string3.contains((CharSequence)"widmo")) {
            A.put((Object)string2, (Object)"blokwidmo");
            return "blokwidmo";
        }
        if (string3.contains((CharSequence)"r\u00f3\u017cd\u017cka") && string3.contains((CharSequence)"iluzjonist")) {
            A.put((Object)string2, (Object)"rozdzkailuzjonisty");
            return "rozdzkailuzjonisty";
        }
        if (itemStack.getType() == Material.CREEPER_SPAWN_EGG && (string3.contains((CharSequence)"zmutowany") || itemStack.getItemMeta().hasLore() && itemStack.getItemMeta().getLore().toString().toLowerCase().contains((CharSequence)"zmutowany"))) {
            A.put((Object)string2, (Object)"zmutowanycreeper");
            return "zmutowanycreeper";
        }
        String string4 = itemStack.getType().name().toLowerCase();
        A.put((Object)string2, (Object)string4);
        return string4;
    }
}

