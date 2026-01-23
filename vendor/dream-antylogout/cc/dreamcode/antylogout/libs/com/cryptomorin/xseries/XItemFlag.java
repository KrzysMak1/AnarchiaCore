package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries;

import java.util.function.Function;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.Set;
import org.jetbrains.annotations.Contract;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import java.util.Collection;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.base.XRegistry;
import org.bukkit.inventory.ItemFlag;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.base.XBase;

public enum XItemFlag implements XBase<XItemFlag, ItemFlag>
{
    HIDE_ADDITIONAL_TOOLTIP(new String[] { "HIDE_POTION_EFFECTS" }), 
    HIDE_ARMOR_TRIM(new String[0]), 
    HIDE_ATTRIBUTES(new String[0]), 
    HIDE_DESTROYS(new String[0]), 
    HIDE_DYE(new String[0]), 
    HIDE_ENCHANTS(new String[0]), 
    HIDE_PLACED_ON(new String[0]), 
    HIDE_STORED_ENCHANTMENTS(new String[] { "HIDE_STORED_ENCHANTS" }), 
    HIDE_UNBREAKABLE(new String[0]), 
    HIDE_CUSTOM_DATA(new String[0]), 
    HIDE_MAX_STACK_SIZE(new String[0]), 
    HIDE_MAX_DAMAGE(new String[0]), 
    HIDE_DAMAGE(new String[0]), 
    HIDE_CUSTOM_NAME(new String[0]), 
    HIDE_ITEM_NAME(new String[0]), 
    HIDE_ITEM_MODEL(new String[0]), 
    HIDE_LORE(new String[0]), 
    HIDE_RARITY(new String[0]), 
    HIDE_ENCHANTMENTS(new String[0]), 
    HIDE_CAN_PLACE_ON(new String[0]), 
    HIDE_CAN_BREAK(new String[0]), 
    HIDE_ATTRIBUTE_MODIFIERS(new String[0]), 
    HIDE_CUSTOM_MODEL_DATA(new String[0]), 
    HIDE_TOOLTIP_DISPLAY(new String[0]), 
    HIDE_REPAIR_COST(new String[0]), 
    HIDE_CREATIVE_SLOT_LOCK(new String[0]), 
    HIDE_ENCHANTMENT_GLINT_OVERRIDE(new String[0]), 
    HIDE_INTANGIBLE_PROJECTILE(new String[0]), 
    HIDE_FOOD(new String[0]), 
    HIDE_CONSUMABLE(new String[0]), 
    HIDE_USE_REMAINDER(new String[0]), 
    HIDE_USE_COOLDOWN(new String[0]), 
    HIDE_DAMAGE_RESISTANT(new String[0]), 
    HIDE_TOOL(new String[0]), 
    HIDE_WEAPON(new String[0]), 
    HIDE_ENCHANTABLE(new String[0]), 
    HIDE_EQUIPPABLE(new String[0]), 
    HIDE_REPAIRABLE(new String[0]), 
    HIDE_GLIDER(new String[0]), 
    HIDE_TOOLTIP_STYLE(new String[0]), 
    HIDE_DEATH_PROTECTION(new String[0]), 
    HIDE_BLOCKS_ATTACKS(new String[0]), 
    HIDE_DYED_COLOR(new String[0]), 
    HIDE_MAP_COLOR(new String[0]), 
    HIDE_MAP_ID(new String[0]), 
    HIDE_MAP_DECORATIONS(new String[0]), 
    HIDE_MAP_POST_PROCESSING(new String[0]), 
    HIDE_CHARGED_PROJECTILES(new String[0]), 
    HIDE_BUNDLE_CONTENTS(new String[0]), 
    HIDE_POTION_CONTENTS(new String[0]), 
    HIDE_POTION_DURATION_SCALE(new String[0]), 
    HIDE_SUSPICIOUS_STEW_EFFECTS(new String[0]), 
    HIDE_WRITABLE_BOOK_CONTENT(new String[0]), 
    HIDE_WRITTEN_BOOK_CONTENT(new String[0]), 
    HIDE_TRIM(new String[0]), 
    HIDE_DEBUG_STICK_STATE(new String[0]), 
    HIDE_ENTITY_DATA(new String[0]), 
    HIDE_BUCKET_ENTITY_DATA(new String[0]), 
    HIDE_BLOCK_ENTITY_DATA(new String[0]), 
    HIDE_INSTRUMENT(new String[0]), 
    HIDE_PROVIDES_TRIM_MATERIAL(new String[0]), 
    HIDE_OMINOUS_BOTTLE_AMPLIFIER(new String[0]), 
    HIDE_JUKEBOX_PLAYABLE(new String[0]), 
    HIDE_PROVIDES_BANNER_PATTERNS(new String[0]), 
    HIDE_RECIPES(new String[0]), 
    HIDE_LODESTONE_TRACKER(new String[0]), 
    HIDE_FIREWORK_EXPLOSION(new String[0]), 
    HIDE_FIREWORKS(new String[0]), 
    HIDE_PROFILE(new String[0]), 
    HIDE_NOTE_BLOCK_SOUND(new String[0]), 
    HIDE_BANNER_PATTERNS(new String[0]), 
    HIDE_BASE_COLOR(new String[0]), 
    HIDE_POT_DECORATIONS(new String[0]), 
    HIDE_CONTAINER(new String[0]), 
    HIDE_BLOCK_STATE(new String[0]), 
    HIDE_BEES(new String[0]), 
    HIDE_LOCK(new String[0]), 
    HIDE_CONTAINER_LOOT(new String[0]), 
    HIDE_BREAK_SOUND(new String[0]), 
    HIDE_VILLAGER_VARIANT(new String[0]), 
    HIDE_WOLF_VARIANT(new String[0]), 
    HIDE_WOLF_SOUND_VARIANT(new String[0]), 
    HIDE_WOLF_COLLAR(new String[0]), 
    HIDE_FOX_VARIANT(new String[0]), 
    HIDE_SALMON_SIZE(new String[0]), 
    HIDE_PARROT_VARIANT(new String[0]), 
    HIDE_TROPICAL_FISH_PATTERN(new String[0]), 
    HIDE_TROPICAL_FISH_BASE_COLOR(new String[0]), 
    HIDE_TROPICAL_FISH_PATTERN_COLOR(new String[0]), 
    HIDE_MOOSHROOM_VARIANT(new String[0]), 
    HIDE_RABBIT_VARIANT(new String[0]), 
    HIDE_PIG_VARIANT(new String[0]), 
    HIDE_COW_VARIANT(new String[0]), 
    HIDE_CHICKEN_VARIANT(new String[0]), 
    HIDE_FROG_VARIANT(new String[0]), 
    HIDE_HORSE_VARIANT(new String[0]), 
    HIDE_PAINTING_VARIANT(new String[0]), 
    HIDE_LLAMA_VARIANT(new String[0]), 
    HIDE_AXOLOTL_VARIANT(new String[0]), 
    HIDE_CAT_VARIANT(new String[0]), 
    HIDE_CAT_COLLAR(new String[0]), 
    HIDE_SHEEP_COLOR(new String[0]), 
    HIDE_SHULKER_COLOR(new String[0]);
    
    public static final XRegistry<XItemFlag, ItemFlag> REGISTRY;
    private static final ItemFlag[] NONE_DECORATION_FLAGS;
    private final ItemFlag itemFlag;
    
    private XItemFlag(final String[] names) {
        this.itemFlag = Data.REGISTRY.stdEnum(this, names);
    }
    
    @NotNull
    public static XItemFlag of(@NotNull final ItemFlag itemFlag) {
        return XItemFlag.REGISTRY.getByBukkitForm(itemFlag);
    }
    
    @NotNull
    public static Optional<XItemFlag> of(@NotNull final String itemFlag) {
        return XItemFlag.REGISTRY.getByName(itemFlag);
    }
    
    @NotNull
    public static Collection<XItemFlag> getValues() {
        return XItemFlag.REGISTRY.getValues();
    }
    
    public String[] getNames() {
        return new String[] { this.name() };
    }
    
    @Nullable
    public ItemFlag get() {
        return this.itemFlag;
    }
    
    @Contract(mutates = "param1")
    public void set(@NotNull final ItemStack item) {
        final ItemMeta meta = item.getItemMeta();
        meta.addItemFlags(new ItemFlag[] { this.itemFlag });
        item.setItemMeta(meta);
    }
    
    @Contract(mutates = "param1")
    public void set(@NotNull final ItemMeta meta) {
        meta.addItemFlags(new ItemFlag[] { this.itemFlag });
    }
    
    @Contract(mutates = "param1")
    public void removeFrom(@NotNull final ItemStack item) {
        final ItemMeta meta = item.getItemMeta();
        this.removeFrom(meta);
        item.setItemMeta(meta);
    }
    
    @Contract(mutates = "param1")
    public void removeFrom(@NotNull final ItemMeta meta) {
        meta.removeItemFlags(new ItemFlag[] { this.itemFlag });
    }
    
    @Contract(value = "_ -> new", pure = true)
    @NotNull
    public static Set<XItemFlag> getFlags(@NotNull final ItemStack item) {
        return getFlags(item.getItemMeta());
    }
    
    @Contract(value = "_ -> new", pure = true)
    @NotNull
    public static Set<XItemFlag> getFlags(@NotNull final ItemMeta meta) {
        return (Set<XItemFlag>)meta.getItemFlags().stream().map(XItemFlag::of).collect(Collectors.toSet());
    }
    
    @Contract(mutates = "param1")
    public boolean has(@NotNull final ItemStack item) {
        return this.has(item.getItemMeta());
    }
    
    @Contract(mutates = "param1")
    public boolean has(@NotNull final ItemMeta meta) {
        return meta.getItemFlags().contains((Object)this.itemFlag);
    }
    
    @Deprecated
    @Contract(mutates = "param1")
    public static void hideEverything(@NotNull final ItemMeta meta) {
        decorationOnly(meta);
    }
    
    @Contract(mutates = "param1")
    public static void decorationOnly(@NotNull final ItemMeta meta) {
        meta.addItemFlags(XItemFlag.NONE_DECORATION_FLAGS);
    }
    
    static {
        REGISTRY = Data.REGISTRY;
        NONE_DECORATION_FLAGS = (ItemFlag[])Arrays.stream((Object[])values()).filter(x -> x != XItemFlag.HIDE_LORE && x != XItemFlag.HIDE_ITEM_NAME && x != XItemFlag.HIDE_CUSTOM_NAME).filter(XBase::isSupported).map(XItemFlag::get).toArray(ItemFlag[]::new);
        XItemFlag.REGISTRY.discardMetadata();
    }
    
    private static final class Data
    {
        private static final XRegistry<XItemFlag, ItemFlag> REGISTRY;
        
        static {
            REGISTRY = new XRegistry<XItemFlag, ItemFlag>(ItemFlag.class, XItemFlag.class, (java.util.function.Function<Integer, XItemFlag[]>)(x$0 -> new XItemFlag[x$0]));
        }
    }
}
