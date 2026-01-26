package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries;

import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.base.XModule;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.profiles.objects.Profileable;
import org.bukkit.block.banner.PatternType;
import org.bukkit.block.Banner;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.Registry;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.entity.Axolotl;
import org.bukkit.Bukkit;
import org.bukkit.entity.TropicalFish;
import org.bukkit.DyeColor;
import org.bukkit.entity.EntityType;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.XReflection;
import java.util.UUID;
import com.google.common.base.Enums;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.meta.Damageable;
import java.util.Set;
import org.bukkit.inventory.ItemFlag;
import com.google.common.collect.Multimap;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.block.ShulkerBox;
import org.bukkit.enchantments.Enchantment;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.profiles.builder.XSkull;
import org.bukkit.block.banner.Pattern;
import org.bukkit.potion.PotionType;
import org.bukkit.FireworkEffect;
import org.bukkit.map.MapView;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.potion.PotionEffect;
import org.bukkit.material.MaterialData;
import org.bukkit.material.SpawnEgg;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;
import java.util.stream.Stream;
import java.util.IdentityHashMap;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.SpawnEggMeta;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.AxolotlBucketMeta;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.inventory.meta.SuspiciousStewMeta;
import org.bukkit.inventory.meta.CrossbowMeta;
import org.bukkit.inventory.meta.TropicalFishBucketMeta;
import org.bukkit.Material;
import java.util.Arrays;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.Contract;
import org.bukkit.entity.Player;
import java.util.Collections;
import java.util.stream.Collectors;
import org.jetbrains.annotations.ApiStatus;
import com.google.common.base.Strings;
import org.bukkit.Color;
import java.util.LinkedHashMap;
import java.util.Iterator;
import org.jetbrains.annotations.Nullable;
import java.util.function.Consumer;
import org.bukkit.configuration.MemoryConfiguration;
import java.util.Objects;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.bukkit.inventory.ItemStack;
import java.util.Locale;
import org.bukkit.block.BlockState;
import org.bukkit.inventory.meta.BlockStateMeta;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.Optional;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.Map;

public final class XItemStack
{
    private static final XMaterial DEFAULT_MATERIAL;
    private static final boolean SUPPORTS_UNBREAKABLE;
    private static final boolean SUPPORTS_POTION_COLOR;
    private static final boolean SUPPORTS_Inventory_getStorageContents;
    private static final boolean SUPPORTS_CUSTOM_MODEL_DATA;
    private static final boolean SUPPORTS_ADVANCED_CUSTOM_MODEL_DATA;
    private static final Map<Class<? extends ItemMeta>, Optional<Function<Deserializer, MetaHandler<ItemMeta>>>> DESERIALIZE_META_HANDLERS;
    private static final Map<Class<? extends ItemMeta>, Optional<Function<Serializer, MetaHandler<ItemMeta>>>> SERIALIZE_META_HANDLERS;
    
    private static <M extends ItemMeta> void meta(final Class<? extends M> clazz, final Function<Deserializer, MetaHandler<M>> deserialize, final Function<Serializer, MetaHandler<M>> serialize) {
        XItemStack.DESERIALIZE_META_HANDLERS.put((Object)clazz, (Object)Optional.of((Object)cast(deserialize)));
        XItemStack.SERIALIZE_META_HANDLERS.put((Object)clazz, (Object)Optional.of((Object)cast(serialize)));
    }
    
    private static void onlyIf(final String className, final Runnable runnable) {
        try {
            Class.forName("org.bukkit.inventory.meta." + className);
            runnable.run();
        }
        catch (final ClassNotFoundException ex) {}
    }
    
    private static <T extends SerialObject> void recursiveMetaHandle(final T serialObject, final Class<?> metaClass, final ItemMeta meta, final Map<Class<? extends ItemMeta>, Optional<Function<T, MetaHandler<ItemMeta>>>> map, final List<Function<T, MetaHandler<ItemMeta>>> collectedHandlers) {
        final Optional<Function<T, MetaHandler<ItemMeta>>> handler = (Optional<Function<T, MetaHandler<ItemMeta>>>)map.get((Object)metaClass);
        if (handler != null) {
            if (handler.isPresent()) {
                if (collectedHandlers != null) {
                    collectedHandlers.add((Object)handler.get());
                }
                ((MetaHandler)((Function)handler.get()).apply((Object)serialObject)).handle(meta);
            }
            return;
        }
        final List<Function<T, MetaHandler<ItemMeta>>> subCollectedHandlers = (List<Function<T, MetaHandler<ItemMeta>>>)new ArrayList();
        final Class<?> superclass = metaClass.getSuperclass();
        if (superclass != null) {
            recursiveMetaHandle(serialObject, superclass, meta, (java.util.Map<Class<? extends ItemMeta>, java.util.Optional<java.util.function.Function<SerialObject, MetaHandler<ItemMeta>>>>)map, (java.util.List<java.util.function.Function<SerialObject, MetaHandler<ItemMeta>>>)subCollectedHandlers);
        }
        for (final Class<?> anInterface : metaClass.getInterfaces()) {
            recursiveMetaHandle(serialObject, anInterface, meta, (java.util.Map<Class<? extends ItemMeta>, java.util.Optional<java.util.function.Function<SerialObject, MetaHandler<ItemMeta>>>>)map, (java.util.List<java.util.function.Function<SerialObject, MetaHandler<ItemMeta>>>)subCollectedHandlers);
        }
        if (subCollectedHandlers.isEmpty()) {
            map.put((Object)metaClass, (Object)Optional.empty());
        }
        else {
            map.put((Object)metaClass, (Object)Optional.of((Object)(inst -> subMeta -> {
                final SerialObject castedInst = cast(inst);
                subCollectedHandlers.iterator();
                final Iterator iterator;
                while (iterator.hasNext()) {
                    final Function subCollectedHandler = (Function)iterator.next();
                    ((MetaHandler)subCollectedHandler.apply((Object)castedInst)).handle(subMeta);
                }
            })));
            if (collectedHandlers != null) {
                collectedHandlers.addAll((Collection)subCollectedHandlers);
            }
        }
    }
    
    private static <T> T cast(final Object something) {
        return (T)something;
    }
    
    private XItemStack() {
    }
    
    private static BlockState safeBlockState(final BlockStateMeta meta) {
        try {
            return meta.getBlockState();
        }
        catch (final IllegalStateException ex) {
            if (ex.getMessage().toLowerCase(Locale.ENGLISH).contains((CharSequence)"missing blockstate")) {
                return null;
            }
            throw ex;
        }
        catch (final ClassCastException ex2) {
            return null;
        }
    }
    
    public static void serialize(@NotNull final ItemStack item, @NotNull final ConfigurationSection config) {
        serialize(item, config, (Function<String, String>)Function.identity());
    }
    
    public static void serialize(@NotNull final ItemStack item, @NotNull final ConfigurationSection config, @NotNull final Function<String, String> translator) {
        new Serializer(item, config, (Function)translator).handle();
    }
    
    public static Map<String, Object> serialize(@NotNull final ItemStack item) {
        Objects.requireNonNull((Object)item, "Cannot serialize a null item");
        final ConfigurationSection config = (ConfigurationSection)new MemoryConfiguration();
        serialize(item, config);
        return configSectionToMap(config);
    }
    
    @NotNull
    public static ItemStack deserialize(@NotNull final ConfigurationSection config) {
        return edit(XItemStack.DEFAULT_MATERIAL.parseItem(), config, (Function<String, String>)Function.identity(), null);
    }
    
    @NotNull
    public static ItemStack deserialize(@NotNull final Map<String, Object> serializedItem) {
        Objects.requireNonNull((Object)serializedItem, "serializedItem cannot be null.");
        return deserialize(mapToConfigSection(serializedItem));
    }
    
    @NotNull
    public static ItemStack deserialize(@NotNull final ConfigurationSection config, @NotNull final Function<String, String> translator) {
        return deserialize(config, translator, null);
    }
    
    @NotNull
    public static ItemStack deserialize(@NotNull final ConfigurationSection config, @NotNull final Function<String, String> translator, @Nullable final Consumer<Exception> restart) {
        return edit(XItemStack.DEFAULT_MATERIAL.parseItem(), config, translator, restart);
    }
    
    @NotNull
    public static ItemStack deserialize(@NotNull final Map<String, Object> serializedItem, @NotNull final Function<String, String> translator) {
        Objects.requireNonNull((Object)serializedItem, "serializedItem cannot be null.");
        Objects.requireNonNull((Object)translator, "translator cannot be null.");
        return deserialize(mapToConfigSection(serializedItem), translator);
    }
    
    private static int toInt(final String str, final int defaultValue) {
        try {
            return Integer.parseInt(str);
        }
        catch (final NumberFormatException nfe) {
            return defaultValue;
        }
    }
    
    private static List<String> split(@NotNull final String str, final char separatorChar) {
        final List<String> list = (List<String>)new ArrayList(5);
        boolean match = false;
        boolean lastMatch = false;
        final int len = str.length();
        int start = 0;
        for (int i = 0; i < len; ++i) {
            if (str.charAt(i) == separatorChar) {
                if (match) {
                    list.add((Object)str.substring(start, i));
                    match = false;
                    lastMatch = true;
                }
                start = i + 1;
            }
            else {
                lastMatch = false;
                match = true;
            }
        }
        if (match || lastMatch) {
            list.add((Object)str.substring(start, len));
        }
        return list;
    }
    
    private static List<String> splitNewLine(final String str) {
        final int len = str.length();
        final List<String> list = (List<String>)new ArrayList();
        int i = 0;
        int start = 0;
        boolean match = false;
        boolean lastMatch = false;
        while (i < len) {
            if (str.charAt(i) == '\n') {
                if (match) {
                    list.add((Object)str.substring(start, i));
                    match = false;
                    lastMatch = true;
                }
                start = ++i;
            }
            else {
                lastMatch = false;
                match = true;
                ++i;
            }
        }
        if (match || lastMatch) {
            list.add((Object)str.substring(start, i));
        }
        return list;
    }
    
    @NotNull
    public static ItemStack edit(@NotNull final ItemStack item, @NotNull final ConfigurationSection config, @NotNull final Function<String, String> translator, @Nullable final Consumer<Exception> restart) {
        return new Deserializer(item, config, (Function)translator, (Consumer)restart).parse();
    }
    
    @NotNull
    private static ConfigurationSection mapToConfigSection(@NotNull final Map<?, ?> map) {
        final ConfigurationSection config = (ConfigurationSection)new MemoryConfiguration();
        for (final Map.Entry<?, ?> entry : map.entrySet()) {
            final String key = entry.getKey().toString();
            Object value = entry.getValue();
            if (value == null) {
                continue;
            }
            if (value instanceof Map) {
                value = mapToConfigSection((Map<?, ?>)value);
            }
            config.set(key, value);
        }
        return config;
    }
    
    @NotNull
    private static Map<String, Object> configSectionToMap(@NotNull final ConfigurationSection config) {
        final Map<String, Object> map = (Map<String, Object>)new LinkedHashMap();
        for (final String key : config.getKeys(false)) {
            Object value = config.get(key);
            if (value == null) {
                continue;
            }
            if (value instanceof ConfigurationSection) {
                value = configSectionToMap((ConfigurationSection)value);
            }
            map.put((Object)key, value);
        }
        return map;
    }
    
    @NotNull
    @ApiStatus.Internal
    public static Optional<Color> parseColor(@Nullable String str) {
        if (Strings.isNullOrEmpty(str)) {
            return (Optional<Color>)Optional.empty();
        }
        final List<String> rgb = split(str.replace((CharSequence)" ", (CharSequence)""), ',');
        if (rgb.size() == 3) {
            return (Optional<Color>)Optional.of((Object)Color.fromRGB(toInt((String)rgb.get(0), 0), toInt((String)rgb.get(1), 0), toInt((String)rgb.get(2), 0)));
        }
        try {
            return (Optional<Color>)Optional.of((Object)Color.fromRGB(Integer.parseInt(str)));
        }
        catch (final NumberFormatException ex) {
            if (str.startsWith("#")) {
                str = str.substring(1);
            }
            try {
                return (Optional<Color>)Optional.of((Object)Color.fromRGB(Integer.parseInt(str, 16)));
            }
            catch (final NumberFormatException e) {
                return (Optional<Color>)Optional.empty();
            }
        }
    }
    
    private static <T> List<T> parseRawOrList(final String singular, final String plural, final ConfigurationSection section, final Function<String, T> convert) {
        final List<String> list = (List<String>)section.getStringList(plural);
        if (!list.isEmpty()) {
            return (List<T>)list.stream().map((Function)convert).collect(Collectors.toList());
        }
        final String single = section.getString(singular);
        if (single != null && !single.isEmpty()) {
            return (List<T>)Collections.singletonList(convert.apply((Object)single));
        }
        return (List<T>)Collections.emptyList();
    }
    
    private static <T> T tryNumber(final String str, final Function<String, T> convert) {
        try {
            return (T)convert.apply((Object)str);
        }
        catch (final NumberFormatException ex) {
            return null;
        }
    }
    
    @NotNull
    @Contract(mutates = "param1")
    public static List<ItemStack> giveOrDrop(@NotNull final Player player, @Nullable final ItemStack... items) {
        return giveOrDrop(player, true, items);
    }
    
    @NotNull
    @Contract(mutates = "param1")
    public static List<ItemStack> giveOrDrop(@NotNull final Player player, final boolean split, @Nullable final ItemStack... items) {
        if (items == null || items.length == 0) {
            return (List<ItemStack>)new ArrayList();
        }
        final List<ItemStack> leftOvers = addItems((Inventory)player.getInventory(), split, items);
        final World world = player.getWorld();
        final Location location = player.getLocation();
        for (final ItemStack drop : leftOvers) {
            world.dropItemNaturally(location, drop);
        }
        return leftOvers;
    }
    
    @Contract(mutates = "param1")
    public static List<ItemStack> addItems(@NotNull final Inventory inventory, final boolean split, @NotNull final ItemStack... items) {
        return addItems(inventory, split, (Predicate<Integer>)null, items);
    }
    
    @NotNull
    @Contract(mutates = "param1")
    public static List<ItemStack> addItems(@NotNull final Inventory inventory, final boolean split, @Nullable final Predicate<Integer> modifiableSlots, @NotNull final ItemStack... items) {
        Objects.requireNonNull((Object)inventory, "Cannot add items to null inventory");
        Objects.requireNonNull((Object)items, "Cannot add null items to inventory");
        final List<ItemStack> leftOvers = (List<ItemStack>)new ArrayList(items.length);
        final int invSize = getStorageContents(inventory).length;
        int lastEmpty = 0;
        for (final ItemStack item : items) {
            int lastPartial = 0;
            final int maxAmount = split ? item.getMaxStackSize() : inventory.getMaxStackSize();
            while (true) {
                final int firstPartial = (lastPartial >= invSize) ? -1 : firstPartial(inventory, item, lastPartial, modifiableSlots);
                if (firstPartial == -1) {
                    if (lastEmpty != -1) {
                        lastEmpty = firstEmpty(inventory, lastEmpty, modifiableSlots);
                    }
                    if (lastEmpty == -1) {
                        leftOvers.add((Object)item);
                        break;
                    }
                    lastPartial = Integer.MAX_VALUE;
                    final int amount = item.getAmount();
                    if (amount <= maxAmount) {
                        inventory.setItem(lastEmpty, item);
                        break;
                    }
                    final ItemStack copy = item.clone();
                    copy.setAmount(maxAmount);
                    inventory.setItem(lastEmpty, copy);
                    item.setAmount(amount - maxAmount);
                    if (++lastEmpty != invSize) {
                        continue;
                    }
                    lastEmpty = -1;
                }
                else {
                    final ItemStack partialItem = inventory.getItem(firstPartial);
                    final int sum = item.getAmount() + partialItem.getAmount();
                    if (sum <= maxAmount) {
                        partialItem.setAmount(sum);
                        inventory.setItem(firstPartial, partialItem);
                        break;
                    }
                    partialItem.setAmount(maxAmount);
                    inventory.setItem(firstPartial, partialItem);
                    item.setAmount(sum - maxAmount);
                    lastPartial = firstPartial + 1;
                }
            }
        }
        return leftOvers;
    }
    
    @NotNull
    @Contract(pure = true)
    public static int firstPartial(@NotNull final Inventory inventory, @Nullable final ItemStack item, final int beginIndex) {
        return firstPartial(inventory, item, beginIndex, null);
    }
    
    @NotNull
    @Contract(pure = true)
    public static int firstPartial(@NotNull final Inventory inventory, @Nullable final ItemStack item, int beginIndex, @Nullable final Predicate<Integer> modifiableSlots) {
        if (item != null) {
            final ItemStack[] items = getStorageContents(inventory);
            final int invSize = items.length;
            if (beginIndex < 0 || beginIndex >= invSize) {
                throw new IndexOutOfBoundsException("Begin Index: " + beginIndex + ", Inventory storage content size: " + invSize);
            }
            while (beginIndex < invSize) {
                if (modifiableSlots == null || modifiableSlots.test((Object)beginIndex)) {
                    final ItemStack cItem = items[beginIndex];
                    if (cItem != null && cItem.getAmount() < cItem.getMaxStackSize() && cItem.isSimilar(item)) {
                        return beginIndex;
                    }
                }
                ++beginIndex;
            }
        }
        return -1;
    }
    
    @NotNull
    @Contract(pure = true)
    public static List<ItemStack> stack(@NotNull final Collection<ItemStack> items) {
        return stack(items, (BiPredicate<ItemStack, ItemStack>)ItemStack::isSimilar);
    }
    
    @NotNull
    @Contract(pure = true)
    public static List<ItemStack> stack(@NotNull final Collection<ItemStack> items, @NotNull final BiPredicate<ItemStack, ItemStack> similarity) {
        Objects.requireNonNull((Object)items, "Cannot stack null items");
        Objects.requireNonNull((Object)similarity, "Similarity check cannot be null");
        final List<ItemStack> stacked = (List<ItemStack>)new ArrayList(items.size());
        for (final ItemStack item : items) {
            if (item == null) {
                continue;
            }
            boolean add = true;
            for (final ItemStack stack : stacked) {
                if (similarity.test((Object)item, (Object)stack)) {
                    stack.setAmount(stack.getAmount() + item.getAmount());
                    add = false;
                    break;
                }
            }
            if (!add) {
                continue;
            }
            stacked.add((Object)item.clone());
        }
        return stacked;
    }
    
    @Contract(pure = true)
    public static int firstEmpty(@NotNull final Inventory inventory, final int beginIndex) {
        return firstEmpty(inventory, beginIndex, null);
    }
    
    @Contract(pure = true)
    public static int firstEmpty(@NotNull final Inventory inventory, int beginIndex, @Nullable final Predicate<Integer> modifiableSlots) {
        final ItemStack[] items = getStorageContents(inventory);
        final int invSize = items.length;
        if (beginIndex < 0 || beginIndex >= invSize) {
            throw new IndexOutOfBoundsException("Begin Index: " + beginIndex + ", Inventory storage content size: " + invSize);
        }
        while (beginIndex < invSize) {
            if (modifiableSlots == null || modifiableSlots.test((Object)beginIndex)) {
                if (items[beginIndex] == null) {
                    return beginIndex;
                }
            }
            ++beginIndex;
        }
        return -1;
    }
    
    @Contract(pure = true)
    public static int firstPartialOrEmpty(@NotNull final Inventory inventory, @Nullable final ItemStack item, int beginIndex) {
        if (item != null) {
            final ItemStack[] items = getStorageContents(inventory);
            final int len = items.length;
            if (beginIndex < 0 || beginIndex >= len) {
                throw new IndexOutOfBoundsException("Begin Index: " + beginIndex + ", Size: " + len);
            }
            while (beginIndex < len) {
                final ItemStack cItem = items[beginIndex];
                if (cItem == null || (cItem.getAmount() < cItem.getMaxStackSize() && cItem.isSimilar(item))) {
                    return beginIndex;
                }
                ++beginIndex;
            }
        }
        return -1;
    }
    
    @Contract(pure = true)
    public static ItemStack[] getStorageContents(final Inventory inventory) {
        if (XItemStack.SUPPORTS_Inventory_getStorageContents) {
            return inventory.getStorageContents();
        }
        return (ItemStack[])Arrays.copyOfRange((Object[])inventory.getContents(), 0, 36);
    }
    
    @Contract(pure = true)
    public static boolean notEmpty(@Nullable final ItemStack item) {
        return !isEmpty(item);
    }
    
    @Contract(pure = true)
    public static boolean isEmpty(@Nullable final ItemStack item) {
        return item == null || item.getType() == Material.AIR;
    }
    
    static {
        DEFAULT_MATERIAL = XMaterial.BARRIER;
        boolean supportsPotionColor = false;
        boolean supportsUnbreakable = false;
        boolean supportsGetStorageContents = false;
        boolean supportSCustomModelData = false;
        boolean supportsAdvancedCustomModelData = false;
        try {
            ItemMeta.class.getDeclaredMethod("setUnbreakable", Boolean.TYPE);
            supportsUnbreakable = true;
        }
        catch (final NoSuchMethodException ex) {}
        try {
            ItemMeta.class.getDeclaredMethod("hasCustomModelData", (Class<?>[])new Class[0]);
            supportSCustomModelData = true;
        }
        catch (final NoSuchMethodException ex2) {}
        try {
            Class.forName("org.bukkit.inventory.meta.components.CustomModelDataComponent");
            supportsAdvancedCustomModelData = true;
        }
        catch (final ClassNotFoundException ex3) {}
        try {
            Class.forName("org.bukkit.inventory.meta.PotionMeta").getMethod("setColor", Color.class);
            supportsPotionColor = true;
        }
        catch (final Throwable t) {}
        try {
            Inventory.class.getDeclaredMethod("getStorageContents", (Class<?>[])new Class[0]);
            supportsGetStorageContents = true;
        }
        catch (final NoSuchMethodException ex4) {}
        SUPPORTS_POTION_COLOR = supportsPotionColor;
        SUPPORTS_UNBREAKABLE = supportsUnbreakable;
        SUPPORTS_Inventory_getStorageContents = supportsGetStorageContents;
        SUPPORTS_CUSTOM_MODEL_DATA = supportSCustomModelData;
        SUPPORTS_ADVANCED_CUSTOM_MODEL_DATA = supportsAdvancedCustomModelData;
        DESERIALIZE_META_HANDLERS = (Map)new IdentityHashMap();
        SERIALIZE_META_HANDLERS = (Map)new IdentityHashMap();
        meta((Class<? extends ItemMeta>)SkullMeta.class, (java.util.function.Function<Deserializer, MetaHandler<ItemMeta>>)(x -> {
            Objects.requireNonNull((Object)x);
            return x$0 -> rec$.handleSkullMeta(x$0);
        }), (java.util.function.Function<Serializer, MetaHandler<ItemMeta>>)(x -> {
            Objects.requireNonNull((Object)x);
            return x$0 -> rec$.handleSkullMeta(x$0);
        }));
        meta((Class<? extends ItemMeta>)LeatherArmorMeta.class, (java.util.function.Function<Deserializer, MetaHandler<ItemMeta>>)(x -> {
            Objects.requireNonNull((Object)x);
            return x$0 -> rec$.handleLeatherArmorMeta(x$0);
        }), (java.util.function.Function<Serializer, MetaHandler<ItemMeta>>)(x -> {
            Objects.requireNonNull((Object)x);
            return x$0 -> rec$.handleLeatherArmorMeta(x$0);
        }));
        meta((Class<? extends ItemMeta>)PotionMeta.class, (java.util.function.Function<Deserializer, MetaHandler<ItemMeta>>)(x -> {
            Objects.requireNonNull((Object)x);
            return x$0 -> rec$.handlePotionMeta(x$0);
        }), (java.util.function.Function<Serializer, MetaHandler<ItemMeta>>)(x -> {
            Objects.requireNonNull((Object)x);
            return x$0 -> rec$.handlePotionMeta(x$0);
        }));
        meta((Class<? extends ItemMeta>)BlockStateMeta.class, (java.util.function.Function<Deserializer, MetaHandler<ItemMeta>>)(x -> {
            Objects.requireNonNull((Object)x);
            return x$0 -> rec$.handleBlockStateMeta(x$0);
        }), (java.util.function.Function<Serializer, MetaHandler<ItemMeta>>)(x -> {
            Objects.requireNonNull((Object)x);
            return x$0 -> rec$.handleBlockStateMeta(x$0);
        }));
        meta((Class<? extends ItemMeta>)FireworkMeta.class, (java.util.function.Function<Deserializer, MetaHandler<ItemMeta>>)(x -> {
            Objects.requireNonNull((Object)x);
            return x$0 -> rec$.handleFireworkMeta(x$0);
        }), (java.util.function.Function<Serializer, MetaHandler<ItemMeta>>)(x -> {
            Objects.requireNonNull((Object)x);
            return x$0 -> rec$.handleFireworkMeta(x$0);
        }));
        meta((Class<? extends ItemMeta>)BookMeta.class, (java.util.function.Function<Deserializer, MetaHandler<ItemMeta>>)(x -> {
            Objects.requireNonNull((Object)x);
            return x$0 -> rec$.handleBookMeta(x$0);
        }), (java.util.function.Function<Serializer, MetaHandler<ItemMeta>>)(x -> {
            Objects.requireNonNull((Object)x);
            return x$0 -> rec$.handleBookMeta(x$0);
        }));
        meta((Class<? extends ItemMeta>)BannerMeta.class, (java.util.function.Function<Deserializer, MetaHandler<ItemMeta>>)(x -> {
            Objects.requireNonNull((Object)x);
            return x$0 -> rec$.handleBannerMeta(x$0);
        }), (java.util.function.Function<Serializer, MetaHandler<ItemMeta>>)(x -> {
            Objects.requireNonNull((Object)x);
            return x$0 -> rec$.handleBannerMeta(x$0);
        }));
        meta((Class<? extends ItemMeta>)MapMeta.class, (java.util.function.Function<Deserializer, MetaHandler<ItemMeta>>)(x -> {
            Objects.requireNonNull((Object)x);
            return x$0 -> rec$.handleMapMeta(x$0);
        }), (java.util.function.Function<Serializer, MetaHandler<ItemMeta>>)(x -> {
            Objects.requireNonNull((Object)x);
            return x$0 -> rec$.handleMapMeta(x$0);
        }));
        meta((Class<? extends ItemMeta>)EnchantmentStorageMeta.class, (java.util.function.Function<Deserializer, MetaHandler<ItemMeta>>)(x -> {
            Objects.requireNonNull((Object)x);
            return x$0 -> rec$.handleEnchantmentStorageMeta(x$0);
        }), (java.util.function.Function<Serializer, MetaHandler<ItemMeta>>)(x -> {
            Objects.requireNonNull((Object)x);
            return x$0 -> rec$.handleEnchantmentStorageMeta(x$0);
        }));
        onlyIf("SpawnEggMeta", () -> meta((Class<? extends ItemMeta>)SpawnEggMeta.class, (java.util.function.Function<Deserializer, MetaHandler<ItemMeta>>)(x -> {
            Objects.requireNonNull((Object)x);
            return x$0 -> rec$.handleSpawnEggMeta(x$0);
        }), (java.util.function.Function<Serializer, MetaHandler<ItemMeta>>)(x -> {
            Objects.requireNonNull((Object)x);
            return x$0 -> rec$.handleSpawnEggMeta(x$0);
        })));
        onlyIf("ArmorMeta", () -> meta((Class<? extends ItemMeta>)ArmorMeta.class, (java.util.function.Function<Deserializer, MetaHandler<ItemMeta>>)(x -> {
            Objects.requireNonNull((Object)x);
            return x$0 -> rec$.handleArmorMeta(x$0);
        }), (java.util.function.Function<Serializer, MetaHandler<ItemMeta>>)(x -> {
            Objects.requireNonNull((Object)x);
            return x$0 -> rec$.handleArmorMeta(x$0);
        })));
        onlyIf("AxolotlBucketMeta", () -> meta((Class<? extends ItemMeta>)AxolotlBucketMeta.class, (java.util.function.Function<Deserializer, MetaHandler<ItemMeta>>)(x -> {
            Objects.requireNonNull((Object)x);
            return x$0 -> rec$.handleAxolotlBucketMeta(x$0);
        }), (java.util.function.Function<Serializer, MetaHandler<ItemMeta>>)(x -> {
            Objects.requireNonNull((Object)x);
            return x$0 -> rec$.handleAxolotlBucketMeta(x$0);
        })));
        onlyIf("CompassMeta", () -> meta((Class<? extends ItemMeta>)CompassMeta.class, (java.util.function.Function<Deserializer, MetaHandler<ItemMeta>>)(x -> {
            Objects.requireNonNull((Object)x);
            return x$0 -> rec$.handleCompassMeta(x$0);
        }), (java.util.function.Function<Serializer, MetaHandler<ItemMeta>>)(x -> {
            Objects.requireNonNull((Object)x);
            return x$0 -> rec$.handleCompassMeta(x$0);
        })));
        onlyIf("SuspiciousStewMeta", () -> meta((Class<? extends ItemMeta>)SuspiciousStewMeta.class, (java.util.function.Function<Deserializer, MetaHandler<ItemMeta>>)(x -> {
            Objects.requireNonNull((Object)x);
            return x$0 -> rec$.handleSuspiciousStewMeta(x$0);
        }), (java.util.function.Function<Serializer, MetaHandler<ItemMeta>>)(x -> {
            Objects.requireNonNull((Object)x);
            return x$0 -> rec$.handleSuspiciousStewMeta(x$0);
        })));
        onlyIf("CrossbowMeta", () -> meta((Class<? extends ItemMeta>)CrossbowMeta.class, (java.util.function.Function<Deserializer, MetaHandler<ItemMeta>>)(x -> {
            Objects.requireNonNull((Object)x);
            return x$0 -> rec$.handleCrossbowMeta(x$0);
        }), (java.util.function.Function<Serializer, MetaHandler<ItemMeta>>)(x -> {
            Objects.requireNonNull((Object)x);
            return x$0 -> rec$.handleCrossbowMeta(x$0);
        })));
        onlyIf("TropicalFishBucketMeta", () -> meta((Class<? extends ItemMeta>)TropicalFishBucketMeta.class, (java.util.function.Function<Deserializer, MetaHandler<ItemMeta>>)(x -> {
            Objects.requireNonNull((Object)x);
            return x$0 -> rec$.handleTropicalFishBucketMeta(x$0);
        }), (java.util.function.Function<Serializer, MetaHandler<ItemMeta>>)(x -> {
            Objects.requireNonNull((Object)x);
            return x$0 -> rec$.handleTropicalFishBucketMeta(x$0);
        })));
    }
    
    private abstract static class SerialObject
    {
        @NotNull
        protected final ItemStack item;
        @NotNull
        protected final ConfigurationSection config;
        @NotNull
        protected final Function<String, String> translator;
        protected ItemMeta meta;
        
        private SerialObject(final ItemStack item, @NotNull final ConfigurationSection config, @NotNull final Function<String, String> translator) {
            this.item = (ItemStack)Objects.requireNonNull((Object)item, "Cannot operate on null ItemStack, considering using an AIR ItemStack instead");
            this.config = (ConfigurationSection)Objects.requireNonNull((Object)config, "Cannot deserialize item to a null configuration section.");
            this.translator = (Function<String, String>)Objects.requireNonNull((Object)translator, "Translator function cannot be null");
        }
    }
    
    private static final class Serializer extends SerialObject
    {
        private Serializer(final ItemStack item, @NotNull final ConfigurationSection config, @NotNull final Function<String, String> translator) {
            super(item, config, (Function)translator);
        }
        
        public void handle() {
            this.config.set("material", (Object)XMaterial.matchXMaterial(this.item).name());
            if (this.item.getAmount() > 1) {
                this.config.set("amount", (Object)this.item.getAmount());
            }
            if (!this.item.hasItemMeta()) {
                return;
            }
            this.meta = this.item.getItemMeta();
            if (this.meta == null) {
                return;
            }
            this.handleDurability(this.meta);
            if (this.meta.hasDisplayName()) {
                this.config.set("name", this.translator.apply((Object)this.meta.getDisplayName()));
            }
            if (this.meta.hasLore()) {
                this.config.set("lore", this.meta.getLore().stream().map((Function)this.translator).collect(Collectors.toList()));
            }
            this.handleCustomModelData();
            if (XItemStack.SUPPORTS_UNBREAKABLE && this.meta.isUnbreakable()) {
                this.config.set("unbreakable", (Object)true);
            }
            this.handleEnchants();
            this.handleItemFlags(this.meta);
            this.handleAttributes(this.meta);
            this.legacySpawnEgg();
            recursiveMetaHandle(this, this.meta.getClass(), this.meta, (java.util.Map<Class<? extends ItemMeta>, java.util.Optional<java.util.function.Function<SerialObject, MetaHandler<ItemMeta>>>>)XItemStack.SERIALIZE_META_HANDLERS, null);
        }
        
        private void handleCustomModelData() {
            if (XItemStack.SUPPORTS_CUSTOM_MODEL_DATA && this.meta.hasCustomModelData()) {
                if (XItemStack.SUPPORTS_ADVANCED_CUSTOM_MODEL_DATA) {
                    final CustomModelDataComponent customModelData = this.meta.getCustomModelDataComponent();
                    final List<String> strings = (List<String>)customModelData.getStrings();
                    final List<Float> floats = (List<Float>)customModelData.getFloats();
                    final List<Boolean> flags = (List<Boolean>)customModelData.getFlags();
                    final List<Color> colors = (List<Color>)customModelData.getColors();
                    final int idCount = (int)Stream.of((Object[])new List[] { strings, floats, flags, colors }).filter(x -> !x.isEmpty()).count();
                    if (idCount == 0) {
                        return;
                    }
                    if (idCount == 1) {
                        if (!strings.isEmpty()) {
                            this.config.set("custom-model-data", singleOrList(strings));
                        }
                        if (!floats.isEmpty()) {
                            this.config.set("custom-model-data", singleOrList(floats));
                        }
                        if (!flags.isEmpty()) {
                            this.config.set("custom-model-data", singleOrList(flags));
                        }
                        if (!colors.isEmpty()) {
                            this.config.set("custom-model-data", singleOrList((java.util.List<Object>)colors.stream().map(Serializer::colorString).collect(Collectors.toList())));
                        }
                    }
                    else {
                        final ConfigurationSection cfgCustomModelData = this.config.createSection("custom-model-data");
                        if (!strings.isEmpty()) {
                            cfgCustomModelData.set("strings", (Object)strings);
                        }
                        if (!floats.isEmpty()) {
                            cfgCustomModelData.set("floats", (Object)floats);
                        }
                        if (!flags.isEmpty()) {
                            cfgCustomModelData.set("flags", (Object)flags);
                        }
                        if (!colors.isEmpty()) {
                            cfgCustomModelData.set("colors", colors.stream().map(Serializer::colorString).collect(Collectors.toList()));
                        }
                    }
                }
                else {
                    this.config.set("custom-model-data", (Object)this.meta.getCustomModelData());
                }
            }
        }
        
        private static <T> Object singleOrList(final List<T> list) {
            return (list.size() == 1) ? list.get(0) : list;
        }
        
        private void legacySpawnEgg() {
            if (!XMaterial.supports(11)) {
                final MaterialData data = this.item.getData();
                if (data instanceof SpawnEgg) {
                    final SpawnEgg spawnEgg = (SpawnEgg)data;
                    this.config.set("creature", (Object)spawnEgg.getSpawnedType().getName());
                }
            }
        }
        
        private void handleSpawnEggMeta(final SpawnEggMeta spawnEgg) {
            this.config.set("creature", (Object)spawnEgg.getSpawnedType().getName());
        }
        
        private void handleSuspiciousStewMeta(final SuspiciousStewMeta stew) {
            final List<PotionEffect> customEffects = (List<PotionEffect>)stew.getCustomEffects();
            final List<String> effects = (List<String>)new ArrayList(customEffects.size());
            for (final PotionEffect effect : customEffects) {
                effects.add((Object)(XPotion.of(effect.getType()).name() + ", " + effect.getDuration() + ", " + effect.getAmplifier()));
            }
            this.config.set("effects", (Object)effects);
        }
        
        private void handleTropicalFishBucketMeta(final TropicalFishBucketMeta tropical) {
            this.config.set("pattern", (Object)tropical.getPattern().name());
            this.config.set("color", (Object)tropical.getBodyColor().name());
            this.config.set("pattern-color", (Object)tropical.getPatternColor().name());
        }
        
        private void handleCrossbowMeta(final CrossbowMeta crossbow) {
            int i = 0;
            for (final ItemStack projectiles : crossbow.getChargedProjectiles()) {
                XItemStack.serialize(projectiles, this.config.getConfigurationSection("projectiles." + i), this.translator);
                ++i;
            }
        }
        
        private void handleCompassMeta(final CompassMeta compass) {
            final ConfigurationSection subSection = this.config.createSection("lodestone");
            subSection.set("tracked", (Object)compass.isLodestoneTracked());
            if (compass.hasLodestone()) {
                final Location location = compass.getLodestone();
                subSection.set("location.world", (Object)location.getWorld().getName());
                subSection.set("location.x", (Object)location.getX());
                subSection.set("location.y", (Object)location.getY());
                subSection.set("location.z", (Object)location.getZ());
            }
        }
        
        private void handleAxolotlBucketMeta(final AxolotlBucketMeta bucket) {
            if (bucket.hasVariant()) {
                this.config.set("color", (Object)bucket.getVariant().toString());
            }
        }
        
        private void handleArmorMeta(final ArmorMeta armorMeta) {
            if (armorMeta.hasTrim()) {
                final ArmorTrim trim = armorMeta.getTrim();
                final ConfigurationSection trimConfig = this.config.createSection("trim");
                trimConfig.set("material", (Object)(trim.getMaterial().getKey().getNamespace() + ':' + trim.getMaterial().getKey().getKey()));
                trimConfig.set("pattern", (Object)(trim.getPattern().getKey().getNamespace() + ':' + trim.getPattern().getKey().getKey()));
            }
        }
        
        private void handleMapMeta(final MapMeta map) {
            final ConfigurationSection mapSection = this.config.createSection("map");
            mapSection.set("scaling", (Object)map.isScaling());
            if (XMaterial.supports(11)) {
                if (map.hasLocationName()) {
                    mapSection.set("location", (Object)map.getLocationName());
                }
                if (map.hasColor()) {
                    final Color color = map.getColor();
                    mapSection.set("color", (Object)colorString(color));
                }
            }
            if (XMaterial.supports(14) && map.hasMapView()) {
                final MapView mapView = map.getMapView();
                final ConfigurationSection view = mapSection.createSection("view");
                view.set("scale", (Object)mapView.getScale().toString());
                view.set("world", (Object)mapView.getWorld().getName());
                final ConfigurationSection centerSection = view.createSection("center");
                centerSection.set("x", (Object)mapView.getCenterX());
                centerSection.set("z", (Object)mapView.getCenterZ());
                view.set("locked", (Object)mapView.isLocked());
                view.set("tracking-position", (Object)mapView.isTrackingPosition());
                view.set("unlimited-tracking", (Object)mapView.isUnlimitedTracking());
            }
        }
        
        private void handleBookMeta(final BookMeta book) {
            if (book.getTitle() != null || book.getAuthor() != null || book.getGeneration() != null || !book.getPages().isEmpty()) {
                final ConfigurationSection bookInfo = this.config.createSection("book");
                if (book.getTitle() != null) {
                    bookInfo.set("title", (Object)book.getTitle());
                }
                if (book.getAuthor() != null) {
                    bookInfo.set("author", (Object)book.getAuthor());
                }
                if (XMaterial.supports(9)) {
                    final BookMeta.Generation generation = book.getGeneration();
                    if (generation != null) {
                        bookInfo.set("generation", (Object)book.getGeneration().toString());
                    }
                }
                if (!book.getPages().isEmpty()) {
                    bookInfo.set("pages", (Object)book.getPages());
                }
            }
        }
        
        private void handleFireworkMeta(final FireworkMeta firework) {
            this.config.set("power", (Object)firework.getPower());
            int i = 0;
            for (final FireworkEffect fw : firework.getEffects()) {
                this.config.set("firework." + i + ".type", (Object)fw.getType().name());
                final ConfigurationSection fwc = this.config.getConfigurationSection("firework." + i);
                fwc.set("flicker", (Object)fw.hasFlicker());
                fwc.set("trail", (Object)fw.hasTrail());
                final List<Color> fwBaseColors = (List<Color>)fw.getColors();
                final List<Color> fwFadeColors = (List<Color>)fw.getFadeColors();
                final List<String> baseColors = (List<String>)new ArrayList(fwBaseColors.size());
                final List<String> fadeColors = (List<String>)new ArrayList(fwFadeColors.size());
                final ConfigurationSection colors = fwc.createSection("colors");
                for (final Color color : fwBaseColors) {
                    baseColors.add((Object)colorString(color));
                }
                colors.set("base", (Object)baseColors);
                for (final Color color : fwFadeColors) {
                    fadeColors.add((Object)colorString(color));
                }
                colors.set("fade", (Object)fadeColors);
                ++i;
            }
        }
        
        @NotNull
        private static String colorString(final Color color) {
            return color.getRed() + ", " + color.getGreen() + ", " + color.getBlue();
        }
        
        private void handlePotionMeta(final PotionMeta meta) {
            if (XMaterial.supports(9)) {
                final List<PotionEffect> customEffects = (List<PotionEffect>)meta.getCustomEffects();
                final List<String> effects = (List<String>)new ArrayList(customEffects.size());
                for (final PotionEffect effect : customEffects) {
                    effects.add((Object)(effect.getType().getName() + ", " + effect.getDuration() + ", " + effect.getAmplifier()));
                }
                if (!effects.isEmpty()) {
                    this.config.set("effects", (Object)effects);
                }
                final PotionType basePotionType = meta.getBasePotionType();
                this.config.set("base-type", (Object)basePotionType.name());
                this.config.set("effects", meta.getCustomEffects().stream().map(x -> {
                    final NamespacedKey type = x.getType().getKey();
                    final String typeStr = type.getNamespace() + ':' + type.getKey();
                    return typeStr + ", " + x.getDuration() + ", " + x.getAmplifier();
                }).collect(Collectors.toList()));
                if (XItemStack.SUPPORTS_POTION_COLOR && meta.hasColor()) {
                    this.config.set("color", (Object)meta.getColor().asRGB());
                }
            }
        }
        
        private void handleLeatherArmorMeta(final LeatherArmorMeta meta) {
            final Color color = meta.getColor();
            this.config.set("color", (Object)colorString(color));
        }
        
        private void handleBannerMeta(final BannerMeta meta) {
            final ConfigurationSection patterns = this.config.createSection("patterns");
            for (final Pattern pattern : meta.getPatterns()) {
                patterns.set(XPatternType.of(pattern.getPattern()).name(), (Object)pattern.getColor().name());
            }
        }
        
        private void handleSkullMeta(final ItemMeta meta) {
            final String skull = XSkull.of(meta).getProfileValue();
            if (skull != null) {
                this.config.set("skull", (Object)skull);
            }
        }
        
        private void handleEnchantmentStorageMeta(final EnchantmentStorageMeta meta) {
            for (final Map.Entry<Enchantment, Integer> enchant : meta.getStoredEnchants().entrySet()) {
                final String entry = "stored-enchants." + XEnchantment.of((Enchantment)enchant.getKey()).name();
                this.config.set(entry, enchant.getValue());
            }
        }
        
        private void handleBlockStateMeta(final BlockStateMeta meta) {
            final BlockState state = safeBlockState(meta);
            if (XMaterial.supports(11) && state instanceof ShulkerBox) {
                final ShulkerBox box = (ShulkerBox)state;
                final ConfigurationSection shulker = this.config.createSection("contents");
                int i = 0;
                for (final ItemStack itemInBox : box.getInventory().getContents()) {
                    if (itemInBox != null) {
                        XItemStack.serialize(itemInBox, shulker.createSection(Integer.toString(i)), this.translator);
                    }
                    ++i;
                }
            }
            else if (state instanceof CreatureSpawner) {
                final CreatureSpawner cs = (CreatureSpawner)state;
                if (cs.getSpawnedType() != null) {
                    this.config.set("spawner", (Object)cs.getSpawnedType().name());
                }
            }
        }
        
        private void handleAttributes(final ItemMeta meta) {
            if (XMaterial.supports(13)) {
                final Multimap<Attribute, AttributeModifier> attributes = (Multimap<Attribute, AttributeModifier>)meta.getAttributeModifiers();
                if (attributes != null) {
                    for (final Map.Entry<Attribute, AttributeModifier> attribute : attributes.entries()) {
                        final String path = "attributes." + XAttribute.of((Attribute)attribute.getKey()).name() + '.';
                        final AttributeModifier modifier = (AttributeModifier)attribute.getValue();
                        this.config.set(path + "name", (Object)modifier.getName());
                        this.config.set(path + "amount", (Object)modifier.getAmount());
                        this.config.set(path + "operation", (Object)modifier.getOperation().name());
                        if (modifier.getSlot() != null) {
                            this.config.set(path + "slot", (Object)modifier.getSlot().name());
                        }
                    }
                }
            }
        }
        
        private void handleItemFlags(final ItemMeta meta) {
            if (!meta.getItemFlags().isEmpty()) {
                final Set<ItemFlag> flags = (Set<ItemFlag>)meta.getItemFlags();
                final List<String> flagNames = (List<String>)new ArrayList(flags.size());
                for (final ItemFlag flag : flags) {
                    flagNames.add((Object)flag.name());
                }
                this.config.set("flags", (Object)flagNames);
            }
        }
        
        private void handleEnchants() {
            for (final Map.Entry<Enchantment, Integer> enchant : this.meta.getEnchants().entrySet()) {
                final String entry = "enchants." + XEnchantment.of((Enchantment)enchant.getKey()).name();
                this.config.set(entry, enchant.getValue());
            }
        }
        
        private void handleDurability(final ItemMeta meta) {
            if (XMaterial.supports(13)) {
                if (meta instanceof Damageable) {
                    final Damageable damageable = (Damageable)meta;
                    if (damageable.hasDamage()) {
                        this.config.set("damage", (Object)damageable.getDamage());
                    }
                }
            }
            else {
                this.config.set("damage", (Object)this.item.getDurability());
            }
        }
    }
    
    private static final class Deserializer extends SerialObject
    {
        @Nullable
        private final Consumer<Exception> restart;
        private static final boolean SPACE_EMPTY_LORE_LINES = true;
        
        private Deserializer(final ItemStack item, @NotNull final ConfigurationSection config, @NotNull final Function<String, String> translator, @Nullable final Consumer<Exception> restart) {
            super(item, config, (Function)translator);
            this.restart = restart;
        }
        
        public ItemStack parse() {
            this.handleMaterial();
            this.handleDamage();
            this.getOrCreateMeta();
            this.handleDurability();
            this.displayName();
            this.unbreakable();
            this.customModelData();
            this.lore();
            this.enchants();
            this.itemFlags();
            this.attributes();
            this.legacySpawnEgg();
            recursiveMetaHandle(this, this.meta.getClass(), this.meta, (java.util.Map<Class<? extends ItemMeta>, java.util.Optional<java.util.function.Function<SerialObject, MetaHandler<ItemMeta>>>>)XItemStack.DESERIALIZE_META_HANDLERS, null);
            this.item.setItemMeta(this.meta);
            return this.item;
        }
        
        private void attributes() {
            if (!XMaterial.supports(13)) {
                return;
            }
            final ConfigurationSection attributes = this.config.getConfigurationSection("attributes");
            if (attributes != null) {
                for (final String attribute : attributes.getKeys(false)) {
                    final Optional<XAttribute> attributeInst = XAttribute.of(attribute);
                    if (attributeInst.isPresent()) {
                        if (!((XAttribute)attributeInst.get()).isSupported()) {
                            continue;
                        }
                        final ConfigurationSection section = attributes.getConfigurationSection(attribute);
                        if (section == null) {
                            continue;
                        }
                        final EquipmentSlot slot = (section.getString("slot") != null) ? ((EquipmentSlot)Enums.getIfPresent((Class)EquipmentSlot.class, section.getString("slot")).or((Object)EquipmentSlot.HAND)) : null;
                        String attrName = section.getString("name");
                        String modifierKey = this.createModifierKey(attribute, attrName);
                        Attribute attributeKey = (Attribute)((XModule<XForm, Attribute>)attributeInst.get()).get();
                        int existingMatches = this.removeDuplicateModifiers(attributeKey, modifierKey);
                        if (existingMatches == 1) {
                            continue;
                        }
                        final AttributeModifier modifier = XAttribute.createModifier(modifierKey, section.getDouble("amount"), (AttributeModifier.Operation)Enums.getIfPresent((Class)AttributeModifier.Operation.class, section.getString("operation")).or((Object)AttributeModifier.Operation.ADD_NUMBER), slot);
                        this.meta.addAttributeModifier(attributeKey, modifier);
                    }
                }
            }
            if (!this.meta.getItemFlags().isEmpty() && XReflection.supports(1, 20, 6) && !this.meta.hasAttributeModifiers()) {
                this.meta.addAttributeModifier((Attribute)((XModule<XForm, Attribute>)XAttribute.ATTACK_DAMAGE).get(), XAttribute.createModifier("xseries:itemflagdummy", 0.0, AttributeModifier.Operation.MULTIPLY_SCALAR_1, null));
            }
        }

        private String createModifierKey(String attribute, @Nullable String attrName) {
            String base = (attrName == null || attrName.isBlank()) ? ("attribute_" + attribute.toLowerCase(Locale.ENGLISH)) : attrName.toLowerCase(Locale.ENGLISH);
            if (base.contains(":")) {
                return base;
            }
            return "anarchiacore:" + base;
        }

        private boolean hasModifier(Attribute attribute, String modifierKey) {
            final java.util.Collection<AttributeModifier> modifiers = this.meta.getAttributeModifiers(attribute);
            if (modifiers == null || modifiers.isEmpty()) {
                return false;
            }
            for (final AttributeModifier modifier : modifiers) {
                final String existingKey = this.extractModifierKey(modifier);
                if (existingKey != null && existingKey.equalsIgnoreCase(modifierKey)) {
                    return true;
                }
            }
            return false;
        }

        private int removeDuplicateModifiers(Attribute attribute, String modifierKey) {
            final java.util.Collection<AttributeModifier> modifiers = this.meta.getAttributeModifiers(attribute);
            if (modifiers == null || modifiers.isEmpty()) {
                return 0;
            }
            java.util.List<AttributeModifier> toRemove = new java.util.ArrayList<AttributeModifier>();
            for (final AttributeModifier modifier : modifiers) {
                final String existingKey = this.extractModifierKey(modifier);
                if (existingKey != null && existingKey.equalsIgnoreCase(modifierKey)) {
                    toRemove.add(modifier);
                }
            }
            for (final AttributeModifier modifier : toRemove) {
                this.meta.removeAttributeModifier(attribute, modifier);
            }
            return toRemove.size();
        }

        @Nullable
        private String extractModifierKey(AttributeModifier modifier) {
            try {
                java.lang.reflect.Method getKey = AttributeModifier.class.getMethod("getKey", new Class[0]);
                Object key = getKey.invoke((Object)modifier, new Object[0]);
                if (key instanceof NamespacedKey) {
                    return ((NamespacedKey)key).toString();
                }
            }
            catch (ReflectiveOperationException ignored) {
            }
            try {
                java.lang.reflect.Method getName = AttributeModifier.class.getMethod("getName", new Class[0]);
                Object name = getName.invoke((Object)modifier, new Object[0]);
                if (name instanceof String) {
                    return (String)name;
                }
            }
            catch (ReflectiveOperationException ignored) {
            }
            try {
                java.lang.reflect.Method getUniqueId = AttributeModifier.class.getMethod("getUniqueId", new Class[0]);
                Object id = getUniqueId.invoke((Object)modifier, new Object[0]);
                if (id instanceof UUID) {
                    return id.toString();
                }
            }
            catch (ReflectiveOperationException ignored) {
            }
            return null;
        }
        
        private void legacySpawnEgg() {
            if (!XMaterial.supports(11)) {
                final MaterialData data = this.item.getData();
                if (data instanceof SpawnEgg) {
                    final String creatureName = this.config.getString("creature");
                    if (!Strings.isNullOrEmpty(creatureName)) {
                        final SpawnEgg spawnEgg = (SpawnEgg)data;
                        final com.google.common.base.Optional<EntityType> creature = (com.google.common.base.Optional<EntityType>)Enums.getIfPresent((Class)EntityType.class, creatureName.toUpperCase(Locale.ENGLISH));
                        if (creature.isPresent()) {
                            spawnEgg.setSpawnedType((EntityType)creature.get());
                        }
                        this.item.setData(data);
                    }
                }
            }
        }
        
        private void unbreakable() {
            if (XItemStack.SUPPORTS_UNBREAKABLE && this.config.isSet("unbreakable")) {
                this.meta.setUnbreakable(this.config.getBoolean("unbreakable"));
            }
        }
        
        private void customModelData() {
            if (XItemStack.SUPPORTS_ADVANCED_CUSTOM_MODEL_DATA) {
                final CustomModelDataComponent customModelData = this.meta.getCustomModelDataComponent();
                final ConfigurationSection detailed = this.config.getConfigurationSection("custom-model-data");
                if (detailed != null) {
                    customModelData.setStrings(parseRawOrList("string", "strings", detailed, (java.util.function.Function<String, Object>)(x -> x)));
                    customModelData.setFlags(parseRawOrList("flag", "flags", detailed, (java.util.function.Function<String, Object>)Boolean::parseBoolean));
                    customModelData.setFloats(parseRawOrList("float", "floats", detailed, (java.util.function.Function<String, Object>)Float::parseFloat));
                    customModelData.setColors(parseRawOrList("color", "colors", detailed, (java.util.function.Function<String, Object>)(x -> (Color)XItemStack.parseColor(x).orElseThrow(() -> new IllegalArgumentException("Unknown color for custom model data: " + x)))));
                }
                else {
                    final List<String> listed = (List<String>)this.config.getStringList("custom-model-data");
                    if (!listed.isEmpty()) {
                        final String modelData = (String)listed.get(0);
                        if (modelData != null && !modelData.isEmpty()) {
                            if (tryNumber(modelData, (java.util.function.Function<String, Object>)Float::parseFloat) != null) {
                                customModelData.setFloats((List)listed.stream().map(Float::parseFloat).collect(Collectors.toList()));
                            }
                            else {
                                final Optional<Color> color = XItemStack.parseColor(modelData);
                                if (color.isPresent()) {
                                    customModelData.setColors((List)listed.stream().map(XItemStack::parseColor).map(x -> (Color)x.orElseThrow(() -> new IllegalArgumentException("Unknown color for custom model data: " + (Object)x))).collect(Collectors.toList()));
                                }
                                else {
                                    customModelData.setStrings((List)listed);
                                }
                            }
                        }
                    }
                    else {
                        final String modelData = this.config.getString("custom-model-data");
                        if (modelData != null && !modelData.isEmpty()) {
                            final Float floatId = (Float)tryNumber(modelData, (java.util.function.Function<String, Object>)Float::parseFloat);
                            if (floatId != null) {
                                customModelData.setFloats(Collections.singletonList((Object)floatId));
                            }
                            else {
                                final Optional<Color> color2 = XItemStack.parseColor(modelData);
                                if (color2.isPresent()) {
                                    customModelData.setColors(Collections.singletonList((Object)color2.get()));
                                }
                                else {
                                    customModelData.setStrings(Collections.singletonList((Object)modelData));
                                }
                            }
                        }
                    }
                }
                if (!customModelData.getColors().isEmpty() || !customModelData.getStrings().isEmpty() || !customModelData.getFlags().isEmpty() || !customModelData.getFloats().isEmpty()) {
                    this.meta.setCustomModelDataComponent(customModelData);
                }
            }
            else if (XItemStack.SUPPORTS_CUSTOM_MODEL_DATA) {
                final String modelData2 = this.config.getString("custom-model-data");
                if (modelData2 != null && !modelData2.isEmpty()) {
                    final Integer intId = (Integer)tryNumber(modelData2, (java.util.function.Function<String, Object>)Integer::parseInt);
                    if (intId != null) {
                        this.meta.setCustomModelData(intId);
                    }
                }
            }
        }
        
        private void displayName() {
            final String name = this.config.getString("name");
            if (!Strings.isNullOrEmpty(name)) {
                final String translated = (String)this.translator.apply((Object)name);
                this.meta.setDisplayName(translated);
            }
            else if (name != null && name.isEmpty()) {
                this.meta.setDisplayName(" ");
            }
        }
        
        private void itemFlags() {
            final List<String> flags = (List<String>)this.config.getStringList("flags");
            if (!flags.isEmpty()) {
                for (String flag : flags) {
                    flag = flag.toUpperCase(Locale.ENGLISH);
                    if (flag.equals((Object)"ALL")) {
                        XItemFlag.decorationOnly(this.meta);
                        break;
                    }
                    XItemFlag.of(flag).ifPresent(itemFlag -> itemFlag.set(this.meta));
                }
            }
            else {
                final String allFlags = this.config.getString("flags");
                if (!Strings.isNullOrEmpty(allFlags) && allFlags.equalsIgnoreCase("ALL")) {
                    XItemFlag.decorationOnly(this.meta);
                }
            }
        }
        
        private void handleEnchantmentStorageMeta(final EnchantmentStorageMeta meta) {
            final ConfigurationSection enchantment = this.config.getConfigurationSection("stored-enchants");
            if (enchantment != null) {
                for (final String ench : enchantment.getKeys(false)) {
                    final Optional<XEnchantment> enchant = XEnchantment.of(ench);
                    enchant.ifPresent(xEnchantment -> meta.addStoredEnchant((Enchantment)((XModule<XForm, Enchantment>)xEnchantment).get(), enchantment.getInt(ench), true));
                }
            }
        }
        
        private void enchants() {
            final ConfigurationSection enchants = this.config.getConfigurationSection("enchants");
            if (enchants != null) {
                for (final String ench : enchants.getKeys(false)) {
                    final Optional<XEnchantment> enchant = XEnchantment.of(ench);
                    enchant.ifPresent(xEnchantment -> this.meta.addEnchant((Enchantment)((XModule<XForm, Enchantment>)xEnchantment).get(), enchants.getInt(ench), true));
                }
            }
            else if (this.config.getBoolean("glow")) {
                this.meta.addEnchant((Enchantment)((XModule<XForm, Enchantment>)XEnchantment.UNBREAKING).get(), 1, false);
                XItemFlag.HIDE_ENCHANTS.set(this.meta);
            }
        }
        
        private void lore() {
            if (!this.config.isSet("lore")) {
                return;
            }
            final List<String> lores = (List<String>)this.config.getStringList("lore");
            List<String> translatedLore;
            if (!lores.isEmpty()) {
                translatedLore = (List<String>)new ArrayList(lores.size());
                for (final String lore : lores) {
                    if (lore.isEmpty()) {
                        translatedLore.add((Object)" ");
                    }
                    else {
                        for (final String singleLore : splitNewLine(lore)) {
                            if (singleLore.isEmpty()) {
                                translatedLore.add((Object)" ");
                            }
                            else {
                                translatedLore.add((Object)this.translator.apply((Object)singleLore));
                            }
                        }
                    }
                }
            }
            else {
                final String lore2 = this.config.getString("lore");
                translatedLore = (List<String>)new ArrayList(10);
                if (!Strings.isNullOrEmpty(lore2)) {
                    for (final String singleLore2 : splitNewLine(lore2)) {
                        if (singleLore2.isEmpty()) {
                            translatedLore.add((Object)" ");
                        }
                        else {
                            translatedLore.add((Object)this.translator.apply((Object)singleLore2));
                        }
                    }
                }
            }
            this.meta.setLore((List)translatedLore);
        }
        
        private void handleSpawnEggMeta(final SpawnEggMeta spawnEgg) {
            final String creatureName = this.config.getString("creature");
            if (!Strings.isNullOrEmpty(creatureName)) {
                final com.google.common.base.Optional<EntityType> creature = (com.google.common.base.Optional<EntityType>)Enums.getIfPresent((Class)EntityType.class, creatureName.toUpperCase(Locale.ENGLISH));
                if (creature.isPresent()) {
                    spawnEgg.setSpawnedType((EntityType)creature.get());
                }
            }
        }
        
        private void handleTropicalFishBucketMeta(final TropicalFishBucketMeta tropical) {
            final DyeColor color = (DyeColor)Enums.getIfPresent((Class)DyeColor.class, this.config.getString("color")).or((Object)DyeColor.WHITE);
            final DyeColor patternColor = (DyeColor)Enums.getIfPresent((Class)DyeColor.class, this.config.getString("pattern-color")).or((Object)DyeColor.WHITE);
            final TropicalFish.Pattern pattern = (TropicalFish.Pattern)Enums.getIfPresent((Class)TropicalFish.Pattern.class, this.config.getString("pattern")).or((Object)TropicalFish.Pattern.BETTY);
            tropical.setBodyColor(color);
            tropical.setPatternColor(patternColor);
            tropical.setPattern(pattern);
        }
        
        private void handleCrossbowMeta(final CrossbowMeta crossbow) {
            final ConfigurationSection projectiles = this.config.getConfigurationSection("projectiles");
            if (projectiles != null) {
                for (final String projectile : projectiles.getKeys(false)) {
                    final ItemStack projectileItem = XItemStack.deserialize(this.config.getConfigurationSection("projectiles." + projectile));
                    crossbow.addChargedProjectile(projectileItem);
                }
            }
        }
        
        private void handleSuspiciousStewMeta(final SuspiciousStewMeta stew) {
            for (final String effects : this.config.getStringList("effects")) {
                final XPotion.Effect effect = XPotion.parseEffect(effects);
                if (effect.hasChance()) {
                    stew.addCustomEffect(effect.getEffect(), true);
                }
            }
        }
        
        private void handleCompassMeta(final CompassMeta compass) {
            compass.setLodestoneTracked(this.config.getBoolean("tracked"));
            final ConfigurationSection lodestone = this.config.getConfigurationSection("lodestone");
            if (lodestone != null) {
                final World world = Bukkit.getWorld(lodestone.getString("world"));
                final double x = lodestone.getDouble("x");
                final double y = lodestone.getDouble("y");
                final double z = lodestone.getDouble("z");
                compass.setLodestone(new Location(world, x, y, z));
            }
        }
        
        private void handleAxolotlBucketMeta(final AxolotlBucketMeta bucket) {
            final String variantStr = this.config.getString("color");
            if (variantStr != null) {
                final Axolotl.Variant variant = (Axolotl.Variant)Enums.getIfPresent((Class)Axolotl.Variant.class, variantStr.toUpperCase(Locale.ENGLISH)).or((Object)Axolotl.Variant.BLUE);
                bucket.setVariant(variant);
            }
        }
        
        private void handleArmorMeta(final ArmorMeta armor) {
            final ConfigurationSection trim = this.config.getConfigurationSection("trim");
            if (trim != null) {
                final TrimMaterial trimMaterial = (TrimMaterial)Registry.TRIM_MATERIAL.get(NamespacedKey.fromString(trim.getString("material")));
                final TrimPattern trimPattern = (TrimPattern)Registry.TRIM_PATTERN.get(NamespacedKey.fromString(trim.getString("pattern")));
                armor.setTrim(new ArmorTrim(trimMaterial, trimPattern));
            }
        }
        
        private void handleMapMeta(final MapMeta map) {
            final ConfigurationSection mapSection = this.config.getConfigurationSection("map");
            if (mapSection == null) {
                return;
            }
            map.setScaling(mapSection.getBoolean("scaling"));
            if (XMaterial.supports(11)) {
                if (mapSection.isSet("location")) {
                    map.setLocationName(mapSection.getString("location"));
                }
                if (mapSection.isSet("color")) {
                    final Optional<Color> color = XItemStack.parseColor(mapSection.getString("color"));
                    Objects.requireNonNull((Object)map);
                    color.ifPresent(map::setColor);
                }
            }
            if (XMaterial.supports(14)) {
                final ConfigurationSection view = mapSection.getConfigurationSection("view");
                if (view != null) {
                    final World world = Bukkit.getWorld(view.getString("world"));
                    if (world != null) {
                        final MapView mapView = Bukkit.createMap(world);
                        mapView.setWorld(world);
                        mapView.setScale((MapView.Scale)Enums.getIfPresent((Class)MapView.Scale.class, view.getString("scale")).or((Object)MapView.Scale.NORMAL));
                        mapView.setLocked(view.getBoolean("locked"));
                        mapView.setTrackingPosition(view.getBoolean("tracking-position"));
                        mapView.setUnlimitedTracking(view.getBoolean("unlimited-tracking"));
                        final ConfigurationSection centerSection = view.getConfigurationSection("center");
                        if (centerSection != null) {
                            mapView.setCenterX(centerSection.getInt("x"));
                            mapView.setCenterZ(centerSection.getInt("z"));
                        }
                        map.setMapView(mapView);
                    }
                }
            }
        }
        
        private void handleBookMeta(final BookMeta book) {
            final ConfigurationSection bookInfo = this.config.getConfigurationSection("book");
            if (bookInfo == null) {
                return;
            }
            book.setTitle(bookInfo.getString("title"));
            book.setAuthor(bookInfo.getString("author"));
            book.setPages(bookInfo.getStringList("pages"));
            if (XMaterial.supports(9)) {
                final String generationValue = bookInfo.getString("generation");
                if (generationValue != null) {
                    final BookMeta.Generation generation = (BookMeta.Generation)Enums.getIfPresent((Class)BookMeta.Generation.class, generationValue).orNull();
                    book.setGeneration(generation);
                }
            }
        }
        
        private void handleFireworkMeta(final FireworkMeta firework) {
            firework.setPower(this.config.getInt("power"));
            final ConfigurationSection fireworkSection = this.config.getConfigurationSection("firework");
            if (fireworkSection == null) {
                return;
            }
            final FireworkEffect.Builder builder = FireworkEffect.builder();
            for (final String fws : fireworkSection.getKeys(false)) {
                final ConfigurationSection fw = this.config.getConfigurationSection("firework." + fws);
                builder.flicker(fw.getBoolean("flicker"));
                builder.trail(fw.getBoolean("trail"));
                builder.with((FireworkEffect.Type)Enums.getIfPresent((Class)FireworkEffect.Type.class, fw.getString("type").toUpperCase(Locale.ENGLISH)).or((Object)FireworkEffect.Type.STAR));
                final ConfigurationSection colorsSection = fw.getConfigurationSection("colors");
                if (colorsSection != null) {
                    List<String> fwColors = (List<String>)colorsSection.getStringList("base");
                    List<Color> colors = (List<Color>)new ArrayList(fwColors.size());
                    for (final String colorStr : fwColors) {
                        final Optional<Color> color = XItemStack.parseColor(colorStr);
                        if (color.isPresent()) {
                            colors.add((Object)color.get());
                        }
                    }
                    builder.withColor((Iterable)colors);
                    fwColors = (List<String>)colorsSection.getStringList("fade");
                    colors = (List<Color>)new ArrayList(fwColors.size());
                    for (final String colorStr : fwColors) {
                        final Optional<Color> color = XItemStack.parseColor(colorStr);
                        if (color.isPresent()) {
                            colors.add((Object)color.get());
                        }
                    }
                    builder.withFade((Iterable)colors);
                }
                firework.addEffect(builder.build());
            }
        }
        
        private void handleBlockStateMeta(final BlockStateMeta bsm) {
            final BlockState state = safeBlockState(bsm);
            if (state instanceof CreatureSpawner) {
                final CreatureSpawner spawner = (CreatureSpawner)state;
                final String spawnerStr = this.config.getString("spawner");
                if (!Strings.isNullOrEmpty(spawnerStr)) {
                    spawner.setSpawnedType((EntityType)Enums.getIfPresent((Class)EntityType.class, spawnerStr.toUpperCase(Locale.ENGLISH)).orNull());
                    spawner.update(true);
                    bsm.setBlockState((BlockState)spawner);
                }
            }
            else if (XMaterial.supports(11) && state instanceof ShulkerBox) {
                final ConfigurationSection shulkerSection = this.config.getConfigurationSection("contents");
                if (shulkerSection != null) {
                    final ShulkerBox box = (ShulkerBox)state;
                    for (final String key : shulkerSection.getKeys(false)) {
                        final ItemStack boxItem = XItemStack.deserialize(shulkerSection.getConfigurationSection(key));
                        final int slot = toInt(key, 0);
                        box.getInventory().setItem(slot, boxItem);
                    }
                    box.update(true);
                    bsm.setBlockState((BlockState)box);
                }
            }
            else if (state instanceof Banner) {
                final Banner banner = (Banner)state;
                final ConfigurationSection patterns = this.config.getConfigurationSection("patterns");
                if (!XMaterial.supports(14)) {
                    banner.setBaseColor(DyeColor.WHITE);
                }
                if (patterns != null) {
                    for (final String pattern : patterns.getKeys(false)) {
                        final Optional<XPatternType> patternType = XPatternType.of(pattern);
                        if (patternType.isPresent() && ((XPatternType)patternType.get()).isSupported()) {
                            final DyeColor color = (DyeColor)Enums.getIfPresent((Class)DyeColor.class, patterns.getString(pattern).toUpperCase(Locale.ENGLISH)).or((Object)DyeColor.WHITE);
                            banner.addPattern(new Pattern(color, (PatternType)((XModule<XForm, PatternType>)patternType.get()).get()));
                        }
                    }
                    banner.update(true);
                    bsm.setBlockState((BlockState)banner);
                }
            }
        }
        
        private void handlePotionMeta(final ItemMeta meta) {
            if (XMaterial.supports(9)) {
                final PotionMeta potion = (PotionMeta)meta;
                for (final String effects : this.config.getStringList("effects")) {
                    final XPotion.Effect effect = XPotion.parseEffect(effects);
                    if (effect.hasChance()) {
                        potion.addCustomEffect(effect.getEffect(), true);
                    }
                }
                final String baseType = this.config.getString("base-type");
                if (!Strings.isNullOrEmpty(baseType)) {
                    PotionType potionType;
                    try {
                        potionType = PotionType.valueOf(baseType);
                    }
                    catch (final IllegalArgumentException ex) {
                        potionType = PotionType.HEALING;
                    }
                    potion.setBasePotionType(potionType);
                }
                if (XItemStack.SUPPORTS_POTION_COLOR && this.config.contains("color")) {
                    potion.setColor(Color.fromRGB(this.config.getInt("color")));
                }
            }
        }
        
        private void handleLeatherArmorMeta(final LeatherArmorMeta leather) {
            final String colorStr = this.config.getString("color");
            if (colorStr != null) {
                final Optional<Color> color = XItemStack.parseColor(colorStr);
                Objects.requireNonNull((Object)leather);
                color.ifPresent(leather::setColor);
            }
        }
        
        private void handleBannerMeta(final BannerMeta banner) {
            final ConfigurationSection patterns = this.config.getConfigurationSection("patterns");
            if (patterns != null) {
                for (final String pattern : patterns.getKeys(false)) {
                    final Optional<XPatternType> patternType = XPatternType.of(pattern);
                    if (patternType.isPresent() && ((XPatternType)patternType.get()).isSupported()) {
                        final DyeColor color = (DyeColor)Enums.getIfPresent((Class)DyeColor.class, patterns.getString(pattern).toUpperCase(Locale.ENGLISH)).or((Object)DyeColor.WHITE);
                        banner.addPattern(new Pattern(color, (PatternType)((XModule<XForm, PatternType>)patternType.get()).get()));
                    }
                }
            }
        }
        
        private void handleSkullMeta(final SkullMeta meta) {
            final String skull = this.config.getString("skull");
            if (skull != null) {
                if (skull.isEmpty()) {
                    XSkull.of((ItemMeta)meta).profile(Profileable.detect(skull)).removeProfile();
                }
                else {
                    XSkull.of((ItemMeta)meta).profile(Profileable.detect(skull)).lenient().apply();
                }
            }
        }
        
        private void handleDurability() {
            if (XMaterial.supports(13)) {
                if (this.meta instanceof Damageable) {
                    final int damage = this.config.getInt("damage");
                    if (damage > 0) {
                        ((Damageable)this.meta).setDamage(damage);
                    }
                }
            }
            else {
                final int damage = this.config.getInt("damage");
                if (damage > 0) {
                    this.item.setDurability((short)damage);
                }
            }
        }
        
        private void handleDamage() {
            final int amount = this.config.getInt("amount");
            if (amount > 1) {
                this.item.setAmount(amount);
            }
        }
        
        private void getOrCreateMeta() {
            this.meta = this.item.getItemMeta();
            if (this.meta == null) {
                this.meta = Bukkit.getItemFactory().getItemMeta(XMaterial.STONE.get());
            }
        }
        
        private void handleMaterial() {
            final String materialName = this.config.getString("material");
            if (!Strings.isNullOrEmpty(materialName)) {
                final Optional<XMaterial> materialOpt = XMaterial.matchXMaterial(materialName);
                XMaterial material;
                if (materialOpt.isPresent()) {
                    material = (XMaterial)materialOpt.get();
                }
                else {
                    final UnknownMaterialCondition unknownMaterialCondition = new UnknownMaterialCondition(materialName);
                    if (this.restart == null) {
                        throw unknownMaterialCondition;
                    }
                    this.restart.accept((Object)unknownMaterialCondition);
                    if (!unknownMaterialCondition.hasSolution()) {
                        throw unknownMaterialCondition;
                    }
                    material = unknownMaterialCondition.solution;
                }
                if (!material.isSupported()) {
                    final UnAcceptableMaterialCondition unsupportedMaterialCondition = new UnAcceptableMaterialCondition(material, UnAcceptableMaterialCondition.Reason.UNSUPPORTED);
                    if (this.restart == null) {
                        throw unsupportedMaterialCondition;
                    }
                    this.restart.accept((Object)unsupportedMaterialCondition);
                    if (!unsupportedMaterialCondition.hasSolution()) {
                        throw unsupportedMaterialCondition;
                    }
                    material = unsupportedMaterialCondition.solution;
                }
                if (XTag.INVENTORY_NOT_DISPLAYABLE.isTagged(material)) {
                    final UnAcceptableMaterialCondition unsupportedMaterialCondition = new UnAcceptableMaterialCondition(material, UnAcceptableMaterialCondition.Reason.NOT_DISPLAYABLE);
                    if (this.restart == null) {
                        throw unsupportedMaterialCondition;
                    }
                    this.restart.accept((Object)unsupportedMaterialCondition);
                    if (!unsupportedMaterialCondition.hasSolution()) {
                        throw unsupportedMaterialCondition;
                    }
                    material = unsupportedMaterialCondition.solution;
                }
                material.setType(this.item);
            }
            else {
                final String skull = this.config.getString("skull");
                if (skull != null) {
                    XMaterial.PLAYER_HEAD.setType(this.item);
                }
            }
        }
    }
    
    public static class MaterialCondition extends RuntimeException
    {
        protected XMaterial solution;
        
        public MaterialCondition(final String message) {
            super(message);
        }
        
        public void setSolution(final XMaterial solution) {
            this.solution = solution;
        }
        
        public boolean hasSolution() {
            return this.solution != null;
        }
    }
    
    public static final class UnknownMaterialCondition extends MaterialCondition
    {
        private final String material;
        
        public UnknownMaterialCondition(final String material) {
            super("Unknown material: " + material);
            this.material = material;
        }
        
        public String getMaterial() {
            return this.material;
        }
    }
    
    public static final class UnAcceptableMaterialCondition extends MaterialCondition
    {
        private final XMaterial material;
        private final Reason reason;
        
        public UnAcceptableMaterialCondition(final XMaterial material, final Reason reason) {
            super("Unacceptable material: " + material.name() + " (" + reason.name() + ')');
            this.material = material;
            this.reason = reason;
        }
        
        public Reason getReason() {
            return this.reason;
        }
        
        public XMaterial getMaterial() {
            return this.material;
        }
        
        public enum Reason
        {
            UNSUPPORTED, 
            NOT_DISPLAYABLE;
        }
    }
    
    private interface MetaHandler<M extends ItemMeta>
    {
        void handle(final M p0);
    }
}
