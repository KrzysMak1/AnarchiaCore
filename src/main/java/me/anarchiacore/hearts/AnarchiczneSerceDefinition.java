package me.anarchiacore.hearts;

import org.bukkit.Material;

import java.util.List;
import java.util.Map;

public record AnarchiczneSerceDefinition(
        Material material,
        String displayName,
        List<String> lore,
        Map<String, Integer> enchantments,
        List<String> flags,
        int customModelData
) {
}
