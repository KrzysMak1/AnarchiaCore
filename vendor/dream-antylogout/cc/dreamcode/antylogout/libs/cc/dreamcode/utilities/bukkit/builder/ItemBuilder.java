package cc.dreamcode.antylogout.libs.cc.dreamcode.utilities.bukkit.builder;

import org.bukkit.plugin.Plugin;
import cc.dreamcode.antylogout.libs.cc.dreamcode.utilities.bukkit.nbt.ItemNbtUtil;
import java.util.function.Function;
import java.util.Map;
import java.util.stream.Collectors;
import cc.dreamcode.antylogout.libs.cc.dreamcode.utilities.bukkit.StringColorUtil;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.enchantments.Enchantment;
import java.util.Arrays;
import java.util.Objects;
import java.util.Collection;
import cc.dreamcode.antylogout.libs.cc.dreamcode.utilities.builder.ListBuilder;
import java.util.List;
import org.bukkit.inventory.meta.ItemMeta;
import lombok.NonNull;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ItemBuilder
{
    private ItemStack itemStack;
    
    public ItemBuilder(@NonNull final Material material) {
        if (material == null) {
            throw new NullPointerException("material is marked non-null but is null");
        }
        this.itemStack = new ItemStack(material);
    }
    
    public ItemBuilder(@NonNull final Material material, final int amount) {
        if (material == null) {
            throw new NullPointerException("material is marked non-null but is null");
        }
        this.itemStack = new ItemStack(material, amount);
    }
    
    public ItemBuilder(@NonNull final ItemStack itemStack, final boolean clone) {
        if (itemStack == null) {
            throw new NullPointerException("itemStack is marked non-null but is null");
        }
        if (clone) {
            this.itemStack = new ItemStack(itemStack);
        }
        else {
            this.itemStack = itemStack;
        }
    }
    
    public ItemBuilder(@NonNull final ItemStack itemStack, final int amount, final boolean clone) {
        if (itemStack == null) {
            throw new NullPointerException("itemStack is marked non-null but is null");
        }
        if (clone) {
            this.itemStack = new ItemStack(itemStack);
        }
        else {
            this.itemStack = itemStack;
        }
        this.itemStack.setAmount(amount);
    }
    
    public static ItemBuilder of(@NonNull final Material material) {
        if (material == null) {
            throw new NullPointerException("material is marked non-null but is null");
        }
        return new ItemBuilder(material);
    }
    
    public static ItemBuilder of(@NonNull final Material material, final int amount) {
        if (material == null) {
            throw new NullPointerException("material is marked non-null but is null");
        }
        return new ItemBuilder(material, amount);
    }
    
    public static ItemBuilder of(@NonNull final ItemStack itemStack) {
        if (itemStack == null) {
            throw new NullPointerException("itemStack is marked non-null but is null");
        }
        return new ItemBuilder(itemStack, true);
    }
    
    public static ItemBuilder of(@NonNull final ItemStack itemStack, final int amount) {
        if (itemStack == null) {
            throw new NullPointerException("itemStack is marked non-null but is null");
        }
        return new ItemBuilder(itemStack, amount, true);
    }
    
    public static ItemBuilder manipulate(@NonNull final ItemStack itemStack) {
        if (itemStack == null) {
            throw new NullPointerException("itemStack is marked non-null but is null");
        }
        return new ItemBuilder(itemStack, false);
    }
    
    public ItemBuilder setAmount(final int amount) {
        this.itemStack.setAmount(amount);
        return this;
    }
    
    public ItemBuilder setType(@NonNull final Material material) {
        if (material == null) {
            throw new NullPointerException("material is marked non-null but is null");
        }
        this.itemStack.setType(material);
        return this;
    }
    
    public ItemBuilder setType(@NonNull final ItemStack itemStack) {
        if (itemStack == null) {
            throw new NullPointerException("itemStack is marked non-null but is null");
        }
        return this.setType(itemStack, true);
    }
    
    public ItemBuilder setType(@NonNull final ItemStack itemStack, final boolean clone) {
        if (itemStack == null) {
            throw new NullPointerException("itemStack is marked non-null but is null");
        }
        ItemStack copy;
        if (clone) {
            copy = new ItemStack(itemStack);
        }
        else {
            copy = itemStack;
        }
        copy.setAmount(this.itemStack.getAmount());
        if (this.itemStack.hasItemMeta()) {
            copy.setItemMeta(this.itemStack.getItemMeta());
        }
        this.itemStack = copy;
        return this;
    }
    
    public ItemBuilder withDurability(final int durability) {
        this.itemStack.setDurability((short)durability);
        return this;
    }
    
    public ItemBuilder setName(@NonNull final String name) {
        if (name == null) {
            throw new NullPointerException("name is marked non-null but is null");
        }
        final ItemMeta itemMeta = this.itemStack.getItemMeta();
        if (itemMeta == null) {
            return this;
        }
        itemMeta.setDisplayName(name);
        this.itemStack.setItemMeta(itemMeta);
        return this;
    }
    
    public ItemBuilder startLoreWith(@NonNull final List<String> lore) {
        if (lore == null) {
            throw new NullPointerException("lore is marked non-null but is null");
        }
        final ItemMeta itemMeta = this.itemStack.getItemMeta();
        if (itemMeta == null) {
            return this;
        }
        if (itemMeta.hasLore()) {
            itemMeta.setLore(new ListBuilder().addAll((Collection)lore).addAll((Collection)Objects.requireNonNull((Object)itemMeta.getLore())).build());
        }
        else {
            itemMeta.setLore((List)lore);
        }
        this.itemStack.setItemMeta(itemMeta);
        return this;
    }
    
    public ItemBuilder startLoreWith(@NonNull final String... lore) {
        if (lore == null) {
            throw new NullPointerException("lore is marked non-null but is null");
        }
        return this.startLoreWith((List<String>)Arrays.asList((Object[])lore));
    }
    
    public ItemBuilder appendLore(@NonNull final List<String> lore) {
        if (lore == null) {
            throw new NullPointerException("lore is marked non-null but is null");
        }
        final ItemMeta itemMeta = this.itemStack.getItemMeta();
        if (itemMeta == null) {
            return this;
        }
        if (itemMeta.hasLore()) {
            itemMeta.setLore(new ListBuilder().addAll((Collection)Objects.requireNonNull((Object)itemMeta.getLore())).addAll((Collection)lore).build());
        }
        else {
            itemMeta.setLore((List)lore);
        }
        this.itemStack.setItemMeta(itemMeta);
        return this;
    }
    
    public ItemBuilder appendLore(@NonNull final String... lore) {
        if (lore == null) {
            throw new NullPointerException("lore is marked non-null but is null");
        }
        return this.appendLore((List<String>)Arrays.asList((Object[])lore));
    }
    
    public ItemBuilder setLore(@NonNull final List<String> lore) {
        if (lore == null) {
            throw new NullPointerException("lore is marked non-null but is null");
        }
        final ItemMeta itemMeta = this.itemStack.getItemMeta();
        if (itemMeta == null) {
            return this;
        }
        itemMeta.setLore((List)lore);
        this.itemStack.setItemMeta(itemMeta);
        return this;
    }
    
    public ItemBuilder setLore(@NonNull final String... lore) {
        if (lore == null) {
            throw new NullPointerException("lore is marked non-null but is null");
        }
        return this.setLore((List<String>)Arrays.asList((Object[])lore));
    }
    
    public ItemBuilder addEnchant(@NonNull final Enchantment enchantment, final int level, final boolean ignoreLevelRestriction) {
        if (enchantment == null) {
            throw new NullPointerException("enchantment is marked non-null but is null");
        }
        final ItemMeta itemMeta = this.itemStack.getItemMeta();
        if (itemMeta == null) {
            return this;
        }
        itemMeta.addEnchant(enchantment, level, ignoreLevelRestriction);
        this.itemStack.setItemMeta(itemMeta);
        return this;
    }
    
    public ItemBuilder addEnchant(@NonNull final Enchantment enchantment, final int level) {
        if (enchantment == null) {
            throw new NullPointerException("enchantment is marked non-null but is null");
        }
        return this.addEnchant(enchantment, level, true);
    }
    
    public ItemBuilder addFlags(@NonNull final ItemFlag... itemFlag) {
        if (itemFlag == null) {
            throw new NullPointerException("itemFlag is marked non-null but is null");
        }
        final ItemMeta itemMeta = this.itemStack.getItemMeta();
        if (itemMeta == null) {
            return this;
        }
        itemMeta.addItemFlags(itemFlag);
        this.itemStack.setItemMeta(itemMeta);
        return this;
    }
    
    public ItemBuilder fixColors() {
        final ItemMeta itemMeta = this.itemStack.getItemMeta();
        if (itemMeta == null) {
            return this;
        }
        if (itemMeta.hasDisplayName()) {
            itemMeta.setDisplayName(StringColorUtil.fixColor(itemMeta.getDisplayName()));
        }
        if (itemMeta.hasLore()) {
            itemMeta.setLore((List)((List)Objects.requireNonNull((Object)itemMeta.getLore())).stream().map(StringColorUtil::fixColor).collect(Collectors.toList()));
        }
        this.itemStack.setItemMeta(itemMeta);
        return this;
    }
    
    public ItemBuilder fixColors(@NonNull final Map<String, Object> placeholders) {
        if (placeholders == null) {
            throw new NullPointerException("placeholders is marked non-null but is null");
        }
        final ItemMeta itemMeta = this.itemStack.getItemMeta();
        if (itemMeta == null) {
            return this;
        }
        if (itemMeta.hasDisplayName()) {
            final String compiledMessage = StringColorUtil.fixColor(itemMeta.getDisplayName(), placeholders);
            itemMeta.setDisplayName(compiledMessage);
        }
        if (itemMeta.hasLore()) {
            itemMeta.setLore((List)((List)Objects.requireNonNull((Object)itemMeta.getLore())).stream().map(text -> StringColorUtil.fixColor(text, placeholders)).collect(Collectors.toList()));
        }
        this.itemStack.setItemMeta(itemMeta);
        return this;
    }
    
    public ItemBuilder fixColors(@NonNull final Map<String, Object> placeholders, final boolean colorizePlaceholders) {
        if (placeholders == null) {
            throw new NullPointerException("placeholders is marked non-null but is null");
        }
        final ItemMeta itemMeta = this.itemStack.getItemMeta();
        if (itemMeta == null) {
            return this;
        }
        if (itemMeta.hasDisplayName()) {
            final String compiledMessage = StringColorUtil.fixColor(itemMeta.getDisplayName(), placeholders, colorizePlaceholders);
            itemMeta.setDisplayName(compiledMessage);
        }
        if (itemMeta.hasLore()) {
            itemMeta.setLore((List)((List)Objects.requireNonNull((Object)itemMeta.getLore())).stream().map(text -> StringColorUtil.fixColor(text, placeholders, colorizePlaceholders)).collect(Collectors.toList()));
        }
        this.itemStack.setItemMeta(itemMeta);
        return this;
    }
    
    public ItemBuilder breakColors() {
        final ItemMeta itemMeta = this.itemStack.getItemMeta();
        if (itemMeta == null) {
            return this;
        }
        if (itemMeta.hasDisplayName()) {
            itemMeta.setDisplayName(StringColorUtil.breakColor(itemMeta.getDisplayName()));
        }
        if (itemMeta.hasLore()) {
            itemMeta.setLore((List)((List)Objects.requireNonNull((Object)itemMeta.getLore())).stream().map(StringColorUtil::breakColor).collect(Collectors.toList()));
        }
        this.itemStack.setItemMeta(itemMeta);
        return this;
    }
    
    public ItemBuilder withCustomMeta(@NonNull final Function<ItemMeta, ItemMeta> function) {
        if (function == null) {
            throw new NullPointerException("function is marked non-null but is null");
        }
        final ItemMeta itemMeta = this.itemStack.getItemMeta();
        this.itemStack.setItemMeta((ItemMeta)function.apply((Object)itemMeta));
        return this;
    }
    
    public ItemBuilder withNbt(@NonNull final String key, @NonNull final String value) {
        if (key == null) {
            throw new NullPointerException("key is marked non-null but is null");
        }
        if (value == null) {
            throw new NullPointerException("value is marked non-null but is null");
        }
        this.itemStack = ItemNbtUtil.setValue(this.itemStack, key, value);
        return this;
    }
    
    public ItemBuilder withNbt(@NonNull final Plugin plugin, @NonNull final String key, @NonNull final String value) {
        if (plugin == null) {
            throw new NullPointerException("plugin is marked non-null but is null");
        }
        if (key == null) {
            throw new NullPointerException("key is marked non-null but is null");
        }
        if (value == null) {
            throw new NullPointerException("value is marked non-null but is null");
        }
        this.itemStack = ItemNbtUtil.setValue(plugin, this.itemStack, key, value);
        return this;
    }
    
    public ItemStack toItemStack() {
        return this.itemStack;
    }
}
