/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.io.File
 *  java.io.IOException
 *  java.lang.Enum
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.HashMap
 *  java.util.List
 *  java.util.Map
 *  java.util.Map$Entry
 *  java.util.UUID
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.entity.Player
 *  org.bukkit.event.player.PlayerResourcePackStatusEvent$Status
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.Plugin
 */
package me.anarchiacore.customitems.stormitemy.texturepack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.Plugin;
import me.anarchiacore.customitems.stormitemy.utils.color.A;

public class B {
    private final Plugin C;
    private final Map<UUID, _A> P = new HashMap();
    private File F;
    private FileConfiguration N;
    private boolean G;
    private String S;
    private boolean J;
    private String B;
    private int Q;
    private boolean T = false;
    private static final String H = "stormitemy.admin";
    private static final String L = "&8\u1d1b\u1d07x\u1d1b\u1d1c\u0280\u1d07\u1d18\u1d00\u1d04\u1d0b";
    private static final Material O = Material.LIME_DYE;
    private static final String I = "&#1DFF1ATak, zaakceptuj";
    private static final List<String> M = List.of((Object)"&7", (Object)"&8 \u00bb &7Kliknij, aby &fzaakceptowa\u0107", (Object)"&8 \u00bb &ftexturepack &7serwera.", (Object)"&7", (Object)"&8 \u00bb &7Texturepack b\u0119dzie &fautomatycznie", (Object)"&8 \u00bb &f\u0142adowany &7przy ka\u017cdym wej\u015bciu.", (Object)"&7", (Object)"&#1DFF1A\u1d0b\u029f\u026a\u1d0b\u0274\u026a\u1d0a, \u1d00\u0299\u028f \u1d18\u0280\u1d22\u028f\u1d0a\u0105\u0107!");
    private static final Material E = Material.RED_DYE;
    private static final String A = "&#FF0000Nie, odrzu\u0107";
    private static final List<String> D = List.of((Object)"&7", (Object)"&8 \u00bb &7Kliknij, aby &fodrzuci\u0107", (Object)"&8 \u00bb &ftexturepack &7serwera.", (Object)"&7", (Object)"&8 \u00bb &7Texturepack &fnie b\u0119dzie", (Object)"&8 \u00bb &f\u0142adowany &7automatycznie.", (Object)"&7", (Object)"&#FF0000\u1d0b\u029f\u026a\u1d0b\u0274\u026a\u1d0a, \u1d00\u0299\u028f \u1d0f\u1d05\u0280\u1d22\u1d1c\u1d04\u026a\u0107!");
    private static final Material U = Material.BOOK;
    private static final String K = "&#DD00FFInformacje";
    private static final List<String> R = List.of((Object)"&7", (Object)"&8 \u00bb &7Czy chcesz &fautomatycznie", (Object)"&8 \u00bb &f\u0142adowa\u0107 &7texturepack serwera?", (Object)"&7", (Object)"&8 \u00bb &7Texturepack zawiera &fniestandardowe", (Object)"&8 \u00bb &ftekstury &7przedmiot\u00f3w pluginu.", (Object)"&7", (Object)"&8 \u00bb &7Wybierz opcj\u0119 &fponi\u017cej&7:", (Object)"&7");

    public B(Plugin javaPlugin) {
        this.C = javaPlugin;
        this.F();
        this.D();
    }

    public void F() {
        FileConfiguration fileConfiguration = this.C.getConfig();
        this.G = fileConfiguration.getBoolean("texturepack.enabled", true);
        this.S = fileConfiguration.getString("texturepack.url", "https://example.com/texturepack.zip");
        this.J = fileConfiguration.getBoolean("texturepack.required", false);
        this.B = fileConfiguration.getString("texturepack.kick_message", "&cMusisz zaakceptowa\u0107 texturepack aby gra\u0107 na tym serwerze!");
        this.Q = fileConfiguration.getInt("texturepack.prompt_delay", 60);
    }

    private void D() {
        this.F = new File(this.C.getDataFolder(), "texturepack_preferences.yml");
        if (!this.F.exists()) {
            try {
                this.F.createNewFile();
            }
            catch (IOException iOException) {
                this.C.getLogger().warning("Nie mo\u017cna utworzy\u0107 pliku preferencji texturepacka: " + iOException.getMessage());
            }
        }
        this.N = YamlConfiguration.loadConfiguration((File)this.F);
        this.T = this.N.getBoolean("globally_accepted", false);
        if (this.N.contains("preferences")) {
            for (String string : this.N.getConfigurationSection("preferences").getKeys(false)) {
                try {
                    UUID uUID = UUID.fromString((String)string);
                    String string2 = this.N.getString("preferences." + string, "PROMPT");
                    _A _A2 = _A.valueOf(string2.toUpperCase());
                    this.P.put((Object)uUID, (Object)_A2);
                }
                catch (Exception exception) {}
            }
        }
    }

    public void G() {
        this.N.set("globally_accepted", (Object)this.T);
        for (Map.Entry entry : this.P.entrySet()) {
            this.N.set("preferences." + ((UUID)entry.getKey()).toString(), (Object)((_A)((Object)entry.getValue())).name());
        }
        try {
            this.N.save(this.F);
        }
        catch (IOException iOException) {
            this.C.getLogger().warning("Nie mo\u017cna zapisa\u0107 preferencji texturepacka: " + iOException.getMessage());
        }
    }

    public _A A(UUID uUID) {
        return (_A)((Object)this.P.getOrDefault((Object)uUID, (Object)_A.D));
    }

    public void A(UUID uUID, _A _A2) {
        this.P.put((Object)uUID, (Object)_A2);
        this.G();
    }

    public void B(Player player) {
        if (!this.G) {
            return;
        }
        if (!player.hasPermission(H)) {
            return;
        }
        if (this.T) {
            Bukkit.getScheduler().runTaskLater((Plugin)this.C, () -> {
                if (player.isOnline()) {
                    this.D(player);
                }
            }, 20L);
            return;
        }
        _A _A2 = this.A(player.getUniqueId());
        switch (_A2.ordinal()) {
            case 1: {
                Bukkit.getScheduler().runTaskLater((Plugin)this.C, () -> this.D(player), 20L);
                break;
            }
            case 2: {
                break;
            }
            case 0: {
                Bukkit.getScheduler().runTaskLater((Plugin)this.C, () -> {
                    if (player.isOnline()) {
                        this.A(player);
                    }
                }, (long)this.Q);
            }
        }
    }

    public void D(Player player) {
        if (this.S == null || this.S.isEmpty() || this.S.equals((Object)"https://example.com/texturepack.zip")) {
            this.C.getLogger().warning("URL texturepacka nie jest skonfigurowany!");
            return;
        }
        try {
            player.setResourcePack(this.S);
        }
        catch (Exception exception) {
            this.C.getLogger().warning("B\u0142\u0105d podczas wysy\u0142ania texturepacka do gracza " + player.getName() + ": " + exception.getMessage());
        }
    }

    public void A(Player player) {
        Inventory inventory = Bukkit.createInventory(null, (int)27, (String)me.anarchiacore.customitems.stormitemy.utils.color.A.C(L));
        ItemStack itemStack = this.A(U, K, R);
        inventory.setItem(13, itemStack);
        ItemStack itemStack2 = this.A(O, I, M);
        inventory.setItem(11, itemStack2);
        ItemStack itemStack3 = this.A(E, A, D);
        inventory.setItem(15, itemStack3);
        player.openInventory(inventory);
    }

    private ItemStack A(Material material, String string, List<String> list) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta != null) {
            itemMeta.setDisplayName(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string));
            if (list != null && !list.isEmpty()) {
                ArrayList arrayList = new ArrayList();
                for (String string2 : list) {
                    arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C(string2));
                }
                itemMeta.setLore((List)arrayList);
            }
            itemStack.setItemMeta(itemMeta);
        }
        return itemStack;
    }

    public boolean A(Player player, int n2, String string) {
        if (!me.anarchiacore.customitems.stormitemy.utils.color.A.C(L).equals((Object)string)) {
            return false;
        }
        if (n2 == 11) {
            this.A(player.getUniqueId(), _A.C);
            player.closeInventory();
            this.D(player);
            player.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8[&x&B&3&0&0&F&F\ud83e\ude93&8] &#1DFF1ATexturepack b\u0119dzie automatycznie \u0142adowany przy ka\u017cdym wej\u015bciu."));
            return true;
        }
        if (n2 == 15) {
            this.A(player.getUniqueId(), _A.B);
            player.closeInventory();
            player.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8[&x&B&3&0&0&F&F\ud83e\ude93&8] &#FF0000Texturepack nie b\u0119dzie automatycznie \u0142adowany."));
            return true;
        }
        return true;
    }

    public void A(Player player, PlayerResourcePackStatusEvent.Status status) {
        if (!this.G) {
            return;
        }
        switch (status) {
            case DECLINED: 
            case FAILED_DOWNLOAD: {
                if (!this.J) break;
                player.kickPlayer(me.anarchiacore.customitems.stormitemy.utils.color.A.C(this.B));
                break;
            }
            case SUCCESSFULLY_LOADED: {
                break;
            }
        }
    }

    public void H() {
        this.F();
        this.D();
    }

    public boolean A() {
        return this.G;
    }

    public String I() {
        return L;
    }

    public boolean B() {
        return this.J;
    }

    public String C() {
        return this.S;
    }

    public boolean E() {
        return this.T;
    }

    public void A(boolean bl) {
        this.T = bl;
        this.G();
        if (bl && this.G) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (!player.hasPermission(H)) continue;
                this.D(player);
            }
        }
    }

    public static final class _A
    extends Enum<_A> {
        public static final /* enum */ _A D = new _A();
        public static final /* enum */ _A C = new _A();
        public static final /* enum */ _A B = new _A();
        private static final /* synthetic */ _A[] A;

        public static _A[] values() {
            return (_A[])A.clone();
        }

        public static _A valueOf(String string) {
            return (_A)Enum.valueOf(_A.class, (String)string);
        }

        private static /* synthetic */ _A[] A() {
            return new _A[]{D, C, B};
        }

        static {
            A = _A.A();
        }
    }
}

