package me.anarchiacore.util;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.*;

public final class ItemUtil {
    private ItemUtil() {
    }

    public static ItemStack applyMeta(ItemStack item,
                                      String displayName,
                                      List<String> lore,
                                      Map<String, Integer> enchantments,
                                      List<String> flags,
                                      int customModelData,
                                      Plugin plugin,
                                      NamespacedKey pdcKey,
                                      String pdcValue) {
        return applyMeta(item, displayName, lore, enchantments, flags, customModelData, plugin, pdcKey, pdcValue, null);
    }

    public static ItemStack applyMeta(ItemStack item,
                                      String displayName,
                                      List<String> lore,
                                      Map<String, Integer> enchantments,
                                      List<String> flags,
                                      int customModelData,
                                      Plugin plugin,
                                      NamespacedKey pdcKey,
                                      String pdcValue,
                                      Map<String, String> placeholders) {
        if (item == null) {
            return null;
        }
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return item;
        }
        if (displayName != null && !displayName.isBlank()) {
            Component nameComponent = MiniMessageUtil.parseComponent(displayName, placeholders);
            meta.displayName(nameComponent);
        }
        if (lore != null && !lore.isEmpty()) {
            List<Component> loreComponents = new ArrayList<>();
            for (String line : lore) {
                loreComponents.add(MiniMessageUtil.parseComponent(line, placeholders));
            }
            meta.lore(loreComponents);
        }
        if (customModelData > 0) {
            meta.setCustomModelData(customModelData);
        }
        if (enchantments != null) {
            for (Map.Entry<String, Integer> entry : enchantments.entrySet()) {
                Enchantment enchantment = parseEnchantment(entry.getKey());
                if (enchantment == null) {
                    plugin.getLogger().warning("Nieznane zaklęcie: " + entry.getKey());
                    continue;
                }
                meta.addEnchant(enchantment, entry.getValue(), true);
            }
        }
        if (flags != null) {
            for (String flagName : flags) {
                try {
                    ItemFlag flag = ItemFlag.valueOf(flagName.toUpperCase(Locale.ROOT));
                    meta.addItemFlags(flag);
                } catch (IllegalArgumentException ex) {
                    plugin.getLogger().warning("Nieznana flaga przedmiotu: " + flagName);
                }
            }
        }
        if (pdcKey != null && pdcValue != null) {
            PersistentDataContainer container = meta.getPersistentDataContainer();
            container.set(pdcKey, PersistentDataType.STRING, pdcValue);
        }
        item.setItemMeta(meta);
        return item;
    }

    public static Enchantment parseEnchantment(String key) {
        if (key == null) {
            return null;
        }
        String namespaced = key.toLowerCase(Locale.ROOT);
        NamespacedKey nsKey;
        if (namespaced.contains(":")) {
            String[] parts = namespaced.split(":", 2);
            nsKey = NamespacedKey.fromString(parts[0] + ":" + parts[1]);
        } else {
            nsKey = NamespacedKey.fromString("minecraft:" + namespaced);
        }
        if (nsKey == null) {
            return null;
        }
        return Enchantment.getByKey(nsKey);
    }

    public static ItemStack createItem(Material material) {
        return new ItemStack(material);
    }
}
