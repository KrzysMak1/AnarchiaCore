package me.anarchiacore.customitems;

import org.bukkit.Material;

import java.util.List;
import java.util.Map;

public record CustomItemDefinition(
        String id,
        Material material,
        String displayName,
        List<String> lore,
        Map<String, Integer> enchantments,
        List<String> flags,
        int customModelData,
        boolean unbreakable,
        List<CustomItemsConfig.AttributeModifierDefinition> attributes
) {
}
