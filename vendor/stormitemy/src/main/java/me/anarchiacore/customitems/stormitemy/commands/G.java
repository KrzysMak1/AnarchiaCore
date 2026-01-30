/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.io.File
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.Arrays
 *  java.util.Collection
 *  java.util.HashMap
 *  java.util.List
 *  java.util.Map
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.command.TabCompleter
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 */
package me.anarchiacore.customitems.stormitemy.commands;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import me.anarchiacore.customitems.stormitemy.Main;
import me.anarchiacore.customitems.stormitemy.items.E;
import me.anarchiacore.customitems.stormitemy.regions.C;
import me.anarchiacore.customitems.stormitemy.utils.color.A;

public class G
implements CommandExecutor,
TabCompleter {
    private final Main A;
    private final C C;
    private final E B;

    public G(Main main, C c2, E e2) {
        this.A = main;
        this.C = c2;
        this.B = e2;
    }

    public boolean onCommand(CommandSender commandSender, Command command, String string, String[] stringArray) {
        try {
            Player player;
            if (!this.B(commandSender, stringArray)) {
                return true;
            }
            Player player2 = player = commandSender instanceof Player ? (Player)commandSender : null;
            if (!this.B(player)) {
                return true;
            }
            if (stringArray.length == 0) {
                this.A(commandSender);
                return true;
            }
            return this.A(player, commandSender, stringArray);
        }
        catch (Exception exception) {
            exception.printStackTrace();
            commandSender.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&cWyst\u0105pi\u0142 b\u0142\u0105d podczas wykonywania komendy."));
            return true;
        }
    }

    private boolean B(CommandSender commandSender, String[] stringArray) {
        if (!(commandSender instanceof Player || stringArray.length != 0 && (stringArray[0].equals((Object)"list") || stringArray[0].equals((Object)"delete")))) {
            commandSender.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&cTa komenda mo\u017ce by\u0107 u\u017cywana tylko przez graczy!"));
            return false;
        }
        return true;
    }

    private boolean B(Player player) {
        if (player != null && !player.hasPermission("stormitemy.region")) {
            player.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&cNie masz uprawnie\u0144 do u\u017cywania tej komendy!"));
            return false;
        }
        return true;
    }

    private boolean A(Player player, CommandSender commandSender, String[] args) {
        String sub = args[0].toLowerCase();
        switch (sub) {
            case "wand" -> {
                return this.A(player);
            }
            case "create" -> {
                return this.A(player, args);
            }
            case "recreate" -> {
                return this.B(player, args);
            }
            case "delete" -> {
                return this.A(commandSender, args);
            }
            case "list" -> {
                this.B(commandSender);
                return true;
            }
            default -> {
                this.A(commandSender);
                return true;
            }
        }
    }

    private boolean A(Player player) {
        if (player == null) {
            return false;
        }
        try {
            ItemStack itemStack = this.createWandItem();
            player.getInventory().addItem(new ItemStack[]{itemStack});
            player.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8[&x&B&3&0&0&F&F\ud83e\ude93&8] &7Otrzyma\u0142e\u015b &x&D&0&6&0&F&Fr\u00f3\u017cd\u017ck\u0119 &7do tworzenia &fregion\u00f3w&7!"));
        }
        catch (Exception exception) {
            player.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&cWyst\u0105pi\u0142 b\u0142\u0105d podczas dawania r\u00f3\u017cd\u017cki!"));
        }
        return true;
    }

    private ItemStack createWandItem() {
        ItemStack wand = new ItemStack(Material.BLAZE_ROD);
        ItemMeta meta = wand.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&x&D&0&6&0&F&F&lR\u00f3\u017cd\u017cka region\u00f3w"));
            wand.setItemMeta(meta);
        }
        return wand;
    }

    private boolean A(Player player, String[] stringArray) {
        String string;
        if (player == null) {
            return false;
        }
        if (stringArray.length < 2) {
            player.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8[&x&B&3&0&0&F&F\ud83e\ude93&8] &7U\u017cycie: &f/stormitemy region create <nazwa> <spawn|pvp>"));
            return true;
        }
        String string2 = string = stringArray.length >= 3 ? stringArray[2].toLowerCase() : "";
        if (!this.A(string, player)) {
            return true;
        }
        this.A(player, stringArray[1].toLowerCase(), string);
        return true;
    }

    private boolean B(Player player, String[] stringArray) {
        String string;
        if (player == null) {
            return false;
        }
        if (stringArray.length < 2) {
            player.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8[&x&B&3&0&0&F&F\ud83e\ude93&8] &7U\u017cycie: &f/stormitemy region recreate <nazwa> [spawn|pvp]"));
            return true;
        }
        String string2 = string = stringArray.length >= 3 ? stringArray[2].toLowerCase() : "";
        if (!string.isEmpty() && !this.A(string, player)) {
            return true;
        }
        this.B(player, stringArray[1].toLowerCase(), string);
        return true;
    }

    private boolean A(CommandSender commandSender, String[] stringArray) {
        if (stringArray.length < 2) {
            commandSender.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8[&x&B&3&0&0&F&F\ud83e\ude93&8] &7U\u017cycie: &f/stormitemy region delete <nazwa>"));
            return true;
        }
        this.A(commandSender, stringArray[1].toLowerCase());
        return true;
    }

    private boolean A(String string, Player player) {
        if (string.isEmpty()) {
            player.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8[&x&B&3&0&0&F&F\ud83e\ude93&8] &7Musisz poda\u0107 typ regionu: &x&D&0&6&0&F&Fspawn &7lub &x&D&0&6&0&F&Fpvp"));
            return false;
        }
        if (!string.equals((Object)"spawn") && !string.equals((Object)"pvp")) {
            player.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8[&x&B&3&0&0&F&F\ud83e\ude93&8] &7Typ regionu musi by\u0107 &x&D&0&6&0&F&Fspawn &7lub &x&D&0&6&0&F&Fpvp."));
            return false;
        }
        return true;
    }

    private void A(CommandSender commandSender) {
        commandSender.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8[&x&B&3&0&0&F&F\ud83e\ude93&8] &7Komendy systemu region\u00f3w:"));
        commandSender.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&f/stormitemy region wand &8- &7Pobierz r\u00f3\u017cd\u017ck\u0119 do zaznaczania region\u00f3w"));
        commandSender.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&f/stormitemy region create <nazwa> <spawn|pvp> &8- &7Utw\u00f3rz nowy region"));
        commandSender.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&f/stormitemy region recreate <nazwa> [spawn|pvp] &8- &7Zaktualizuj istniej\u0105cy region"));
        commandSender.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&f/stormitemy region delete <nazwa> &8- &7Usu\u0144 region"));
        commandSender.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&f/stormitemy region list &8- &7Wy\u015bwietl list\u0119 region\u00f3w"));
    }

    private void A(Player player, String string, String string2) {
        if (this.C.C(string)) {
            HashMap hashMap = new HashMap();
            hashMap.put((Object)"name", (Object)string);
            me.anarchiacore.customitems.stormitemy.utils.language.A.A(player, "region_already_exists", (Map<String, String>)hashMap);
            return;
        }
        if (!this.C.B(player)) {
            me.anarchiacore.customitems.stormitemy.utils.language.A.A(player, "region_must_select_points");
            return;
        }
        if (this.C.A(player, string)) {
            HashMap hashMap = new HashMap();
            hashMap.put((Object)"name", (Object)string);
            me.anarchiacore.customitems.stormitemy.utils.language.A.A(player, "region_created", (Map<String, String>)hashMap);
            if (string2.equals((Object)"spawn")) {
                this.B(player, string);
            } else if (string2.equals((Object)"pvp")) {
                this.A(player, string);
            }
            player.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8[&x&B&3&0&0&F&F\ud83e\ude93&8] &7Region utworzony jako typ: &x&D&0&6&0&F&F" + string2));
            this.C.C(player);
        } else {
            HashMap hashMap = new HashMap();
            hashMap.put((Object)"name", (Object)string);
            me.anarchiacore.customitems.stormitemy.utils.language.A.A(player, "region_error_creating", (Map<String, String>)hashMap);
        }
    }

    private void B(Player player, String string) {
        List<String> list = this.A.getConfig().getStringList("disabled-regions");
        String regionKey = string.toLowerCase();
        if (!list.contains(regionKey)) {
            list.add(regionKey);
            this.A.getConfig().set("disabled-regions", list);
            this.A.saveConfig();
            HashMap hashMap = new HashMap();
            hashMap.put((Object)"name", (Object)string);
            me.anarchiacore.customitems.stormitemy.utils.language.A.A(player, "region_added_to_disabled", (Map<String, String>)hashMap);
        }
    }

    private void A(Player player, String string) {
        try {
            File itemsDir = new File(this.A.getDataFolder(), "items");
            String regionKey = string.toLowerCase();
            File bombardFile = new File(itemsDir, "bombardamaxima.yml");
            if (bombardFile.exists()) {
                YamlConfiguration yaml = YamlConfiguration.loadConfiguration(bombardFile);
                List<String> regions = yaml.getStringList("bombarda.noDestroyRegions");
                if (!regions.contains(regionKey)) {
                    regions.add(regionKey);
                    yaml.set("bombarda.noDestroyRegions", regions);
                    yaml.save(bombardFile);
                    player.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8[&x&B&3&0&0&F&F\ud83e\ude93&8] &7Region &x&D&0&6&0&F&F" + string + " &7zosta\u0142 dodany do listy PVP region\u00f3w dla &x&D&0&6&0&F&FBombardaMaxima&7!"));
                }
            }
            File kukurydzaFile = new File(itemsDir, "rozgotowanakukurydza.yml");
            if (kukurydzaFile.exists()) {
                YamlConfiguration yaml = YamlConfiguration.loadConfiguration(kukurydzaFile);
                List<String> regions = yaml.getStringList("rozgotowanakukurydza.noDestroyRegions");
                if (!regions.contains(regionKey)) {
                    regions.add(regionKey);
                    yaml.set("rozgotowanakukurydza.noDestroyRegions", regions);
                    yaml.save(kukurydzaFile);
                    player.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8[&x&B&3&0&0&F&F\ud83e\ude93&8] &7Region &x&D&0&6&0&F&F" + string + " &7zosta\u0142 dodany do listy PVP region\u00f3w dla &x&D&0&6&0&F&FRozgotowanaKukurydza&7!"));
                }
            }
            File balonikFile = new File(itemsDir, "balonikzhelem.yml");
            if (balonikFile.exists()) {
                YamlConfiguration yaml = YamlConfiguration.loadConfiguration(balonikFile);
                List<String> regions = yaml.getStringList("balonikzhelem.noDestroyRegions");
                if (!regions.contains(regionKey)) {
                    regions.add(regionKey);
                    yaml.set("balonikzhelem.noDestroyRegions", regions);
                    yaml.save(balonikFile);
                    player.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8[&x&B&3&0&0&F&F\ud83e\ude93&8] &7Region &x&D&0&6&0&F&F" + string + " &7zosta\u0142 dodany do listy PVP region\u00f3w dla &x&D&0&6&0&F&FBalonikzHelem&7!"));
                }
            }
        }
        catch (Exception exception) {
            player.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8[&x&B&3&0&0&F&F\ud83e\ude93&8] &cWyst\u0105pi\u0142 b\u0142\u0105d podczas dodawania regionu do listy PVP!"));
        }
    }

    private void B(Player player, String string, String string2) {
        if (!this.C.C(string)) {
            HashMap hashMap = new HashMap();
            hashMap.put((Object)"name", (Object)string);
            me.anarchiacore.customitems.stormitemy.utils.language.A.A(player, "region_not_exists", (Map<String, String>)hashMap);
            return;
        }
        if (!this.C.B(player)) {
            me.anarchiacore.customitems.stormitemy.utils.language.A.A(player, "region_must_select_points");
            return;
        }
        if (this.C.B(player, string)) {
            HashMap hashMap = new HashMap();
            hashMap.put((Object)"name", (Object)string);
            me.anarchiacore.customitems.stormitemy.utils.language.A.A(player, "region_updated", (Map<String, String>)hashMap);
            if (!string2.isEmpty()) {
                this.A(string);
                if (string2.equals((Object)"spawn")) {
                    this.B(player, string);
                } else if (string2.equals((Object)"pvp")) {
                    this.A(player, string);
                }
                player.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8[&x&B&3&0&0&F&F\ud83e\ude93&8] &7Typ regionu zmieniony na: &x&D&0&6&0&F&F" + string2));
            }
            this.C.C(player);
        } else {
            HashMap hashMap = new HashMap();
            hashMap.put((Object)"name", (Object)string);
            me.anarchiacore.customitems.stormitemy.utils.language.A.A(player, "region_error_updating", (Map<String, String>)hashMap);
        }
    }

    private void A(String string) {
        try {
            File itemsDir = new File(this.A.getDataFolder(), "items");
            String regionKey = string.toLowerCase();
            List<String> disabledRegions = this.A.getConfig().getStringList("disabled-regions");
            if (disabledRegions.contains(regionKey)) {
                disabledRegions.remove(regionKey);
                this.A.getConfig().set("disabled-regions", disabledRegions);
                this.A.saveConfig();
            }
            File bombardFile = new File(itemsDir, "bombardamaxima.yml");
            if (bombardFile.exists()) {
                YamlConfiguration yaml = YamlConfiguration.loadConfiguration(bombardFile);
                List<String> regions = yaml.getStringList("bombarda.noDestroyRegions");
                if (regions.contains(regionKey)) {
                    regions.remove(regionKey);
                    yaml.set("bombarda.noDestroyRegions", regions);
                    yaml.save(bombardFile);
                }
            }
            File kukurydzaFile = new File(itemsDir, "rozgotowanakukurydza.yml");
            if (kukurydzaFile.exists()) {
                YamlConfiguration yaml = YamlConfiguration.loadConfiguration(kukurydzaFile);
                List<String> regions = yaml.getStringList("rozgotowanakukurydza.noDestroyRegions");
                if (regions.contains(regionKey)) {
                    regions.remove(regionKey);
                    yaml.set("rozgotowanakukurydza.noDestroyRegions", regions);
                    yaml.save(kukurydzaFile);
                }
            }
            File balonikFile = new File(itemsDir, "balonikzhelem.yml");
            if (balonikFile.exists()) {
                YamlConfiguration yaml = YamlConfiguration.loadConfiguration(balonikFile);
                List<String> regions = yaml.getStringList("balonikzhelem.noDestroyRegions");
                if (regions.contains(regionKey)) {
                    regions.remove(regionKey);
                    yaml.set("balonikzhelem.noDestroyRegions", regions);
                    yaml.save(balonikFile);
                }
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    private void A(CommandSender commandSender, String string) {
        if (!this.C.C(string)) {
            HashMap hashMap = new HashMap();
            hashMap.put((Object)"name", (Object)string);
            me.anarchiacore.customitems.stormitemy.utils.language.A.A(commandSender, "region_not_exists", (Map<String, String>)hashMap);
            return;
        }
        if (this.C.B(string)) {
            HashMap hashMap = new HashMap();
            hashMap.put((Object)"name", (Object)string);
            me.anarchiacore.customitems.stormitemy.utils.language.A.A(commandSender, "region_deleted", (Map<String, String>)hashMap);
            this.A(string);
        } else {
            HashMap hashMap = new HashMap();
            hashMap.put((Object)"name", (Object)string);
            me.anarchiacore.customitems.stormitemy.utils.language.A.A(commandSender, "region_error_deleting", (Map<String, String>)hashMap);
        }
    }

    private void B(CommandSender commandSender) {
        List<String> list = this.C.A();
        if (list.isEmpty()) {
            me.anarchiacore.customitems.stormitemy.utils.language.A.A(commandSender, "region_no_regions");
        } else {
            HashMap hashMap = new HashMap();
            hashMap.put((Object)"count", (Object)String.valueOf((int)list.size()));
            me.anarchiacore.customitems.stormitemy.utils.language.A.A(commandSender, "region_list_header", (Map<String, String>)hashMap);
            for (String string : list) {
                HashMap hashMap2 = new HashMap();
                hashMap2.put((Object)"name", (Object)string);
                me.anarchiacore.customitems.stormitemy.utils.language.A.A(commandSender, "region_list_item", (Map<String, String>)hashMap2);
            }
        }
    }

    public List<String> onTabComplete(CommandSender commandSender, Command command, String string, String[] stringArray) {
        if (stringArray.length == 1) {
            List<String> subcommands = Arrays.asList("wand", "create", "recreate", "delete", "list");
            List<String> matches = new ArrayList();
            for (String option : subcommands) {
                if (!option.toLowerCase().startsWith(stringArray[0].toLowerCase())) continue;
                matches.add(option);
            }
            return matches;
        }
        if (stringArray.length == 2) {
            if (stringArray[0].equalsIgnoreCase("delete") || stringArray[0].equalsIgnoreCase("recreate")) {
                List<String> list = this.C.A();
                List<String> arrayList = new ArrayList();
                for (String string3 : list) {
                    if (!string3.toLowerCase().startsWith(stringArray[1].toLowerCase())) continue;
                    arrayList.add(string3);
                }
                return arrayList;
            }
        } else if (stringArray.length == 3 && (stringArray[0].equalsIgnoreCase("create") || stringArray[0].equalsIgnoreCase("recreate"))) {
            List<String> list = Arrays.asList("spawn", "pvp");
            List<String> arrayList = new ArrayList();
            for (String string4 : list) {
                if (!string4.toLowerCase().startsWith(stringArray[2].toLowerCase())) continue;
                arrayList.add(string4);
            }
            return arrayList;
        }
        return null;
    }
}
