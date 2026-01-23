package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries;

import java.util.function.Function;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import org.bukkit.Registry;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.NamespacedKey;
import java.util.Objects;
import org.jetbrains.annotations.Nullable;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.attribute.AttributeModifier;
import org.jetbrains.annotations.NotNull;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.base.XRegistry;
import org.bukkit.attribute.Attribute;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.base.XModule;

public final class XAttribute extends XModule<XAttribute, Attribute>
{
    public static final XRegistry<XAttribute, Attribute> REGISTRY;
    public static final XAttribute MAX_HEALTH;
    public static final XAttribute FOLLOW_RANGE;
    public static final XAttribute KNOCKBACK_RESISTANCE;
    public static final XAttribute MOVEMENT_SPEED;
    public static final XAttribute FLYING_SPEED;
    public static final XAttribute ATTACK_DAMAGE;
    public static final XAttribute ATTACK_KNOCKBACK;
    public static final XAttribute ATTACK_SPEED;
    public static final XAttribute ARMOR;
    public static final XAttribute ARMOR_TOUGHNESS;
    public static final XAttribute FALL_DAMAGE_MULTIPLIER;
    public static final XAttribute LUCK;
    public static final XAttribute MAX_ABSORPTION;
    public static final XAttribute SAFE_FALL_DISTANCE;
    public static final XAttribute SCALE;
    public static final XAttribute STEP_HEIGHT;
    public static final XAttribute GRAVITY;
    public static final XAttribute JUMP_STRENGTH;
    public static final XAttribute BURNING_TIME;
    public static final XAttribute EXPLOSION_KNOCKBACK_RESISTANCE;
    public static final XAttribute MOVEMENT_EFFICIENCY;
    public static final XAttribute OXYGEN_BONUS;
    public static final XAttribute WATER_MOVEMENT_EFFICIENCY;
    public static final XAttribute TEMPT_RANGE;
    public static final XAttribute BLOCK_INTERACTION_RANGE;
    public static final XAttribute ENTITY_INTERACTION_RANGE;
    public static final XAttribute BLOCK_BREAK_SPEED;
    public static final XAttribute MINING_EFFICIENCY;
    public static final XAttribute SNEAKING_SPEED;
    public static final XAttribute SUBMERGED_MINING_SPEED;
    public static final XAttribute SWEEPING_DAMAGE_RATIO;
    public static final XAttribute SPAWN_REINFORCEMENTS;
    public static final XAttribute CAMERA_DISTANCE;
    public static final XAttribute WAYPOINT_TRANSMIT_RANGE;
    public static final XAttribute WAYPOINT_RECEIVE_RANGE;
    private static final boolean SUPPORTS_MODERN_MODIFIERS;
    
    private XAttribute(final Attribute attribute, final String[] names) {
        super(attribute, names);
    }
    
    public static AttributeModifier createModifier(@NotNull final String key, final double amount, @NotNull final AttributeModifier.Operation operation, @Nullable final EquipmentSlot slot) {
        Objects.requireNonNull((Object)key, "Key is null");
        Objects.requireNonNull((Object)operation, "Operation is null");
        if (XAttribute.SUPPORTS_MODERN_MODIFIERS) {
            final NamespacedKey ns = (NamespacedKey)Objects.requireNonNull((Object)NamespacedKey.fromString(key), () -> "Invalid namespace: " + key);
            return new AttributeModifier(ns, amount, operation, (slot == null) ? EquipmentSlotGroup.ANY : slot.getGroup());
        }
        return new AttributeModifier(UUID.randomUUID(), key, amount, operation, slot);
    }
    
    public static XAttribute of(final Attribute attribute) {
        return XAttribute.REGISTRY.getByBukkitForm(attribute);
    }
    
    public static Optional<XAttribute> of(final String attribute) {
        return XAttribute.REGISTRY.getByName(attribute);
    }
    
    @Deprecated
    public static XAttribute[] values() {
        return XAttribute.REGISTRY.values();
    }
    
    @NotNull
    public static Collection<XAttribute> getValues() {
        return XAttribute.REGISTRY.getValues();
    }
    
    private static XAttribute std(final String... names) {
        return XAttribute.REGISTRY.std(names);
    }
    
    static {
        REGISTRY = new XRegistry<XAttribute, Attribute>(Attribute.class, XAttribute.class, (Supplier<Object>)(() -> Registry.ATTRIBUTE), (java.util.function.BiFunction<Attribute, String[], XAttribute>)XAttribute::new, (java.util.function.Function<Integer, XAttribute[]>)(x$0 -> new XAttribute[x$0]));
        MAX_HEALTH = std("max_health", "generic.max_health");
        FOLLOW_RANGE = std("follow_range", "generic.follow_range");
        KNOCKBACK_RESISTANCE = std("knockback_resistance", "generic.knockback_resistance");
        MOVEMENT_SPEED = std("movement_speed", "generic.movement_speed");
        FLYING_SPEED = std("flying_speed", "generic.flying_speed");
        ATTACK_DAMAGE = std("attack_damage", "generic.attack_damage");
        ATTACK_KNOCKBACK = std("attack_knockback", "generic.attack_knockback");
        ATTACK_SPEED = std("attack_speed", "generic.attack_speed");
        ARMOR = std("armor", "generic.armor");
        ARMOR_TOUGHNESS = std("armor_toughness", "generic.armor_toughness");
        FALL_DAMAGE_MULTIPLIER = std("fall_damage_multiplier", "generic.fall_damage_multiplier");
        LUCK = std("luck", "generic.luck");
        MAX_ABSORPTION = std("max_absorption", "generic.max_absorption");
        SAFE_FALL_DISTANCE = std("safe_fall_distance", "generic.safe_fall_distance");
        SCALE = std("scale", "generic.scale");
        STEP_HEIGHT = std("step_height", "generic.step_height");
        GRAVITY = std("gravity", "generic.gravity");
        JUMP_STRENGTH = std("jump_strength", "generic.jump_strength", "horse.jump_strength");
        BURNING_TIME = std("burning_time", "generic.burning_time");
        EXPLOSION_KNOCKBACK_RESISTANCE = std("explosion_knockback_resistance", "generic.explosion_knockback_resistance");
        MOVEMENT_EFFICIENCY = std("movement_efficiency", "generic.movement_efficiency");
        OXYGEN_BONUS = std("oxygen_bonus", "generic.oxygen_bonus");
        WATER_MOVEMENT_EFFICIENCY = std("water_movement_efficiency", "generic.water_movement_efficiency");
        TEMPT_RANGE = std("tempt_range", "generic.tempt_range");
        BLOCK_INTERACTION_RANGE = std("block_interaction_range", "player.block_interaction_range");
        ENTITY_INTERACTION_RANGE = std("entity_interaction_range", "player.entity_interaction_range");
        BLOCK_BREAK_SPEED = std("block_break_speed", "player.block_break_speed");
        MINING_EFFICIENCY = std("mining_efficiency", "player.mining_efficiency");
        SNEAKING_SPEED = std("sneaking_speed", "player.sneaking_speed");
        SUBMERGED_MINING_SPEED = std("submerged_mining_speed", "player.submerged_mining_speed");
        SWEEPING_DAMAGE_RATIO = std("sweeping_damage_ratio", "player.sweeping_damage_ratio");
        SPAWN_REINFORCEMENTS = std("spawn_reinforcements", "zombie.spawn_reinforcements");
        CAMERA_DISTANCE = std("camera_distance");
        WAYPOINT_TRANSMIT_RANGE = std("waypoint_transmit_range");
        WAYPOINT_RECEIVE_RANGE = std("waypoint_receive_range");
        boolean supportsModernModifiers;
        try {
            AttributeModifier.class.getConstructor(NamespacedKey.class, Double.TYPE, AttributeModifier.Operation.class, EquipmentSlotGroup.class);
            supportsModernModifiers = true;
        }
        catch (final NoSuchMethodException | NoClassDefFoundError ignored) {
            supportsModernModifiers = false;
        }
        SUPPORTS_MODERN_MODIFIERS = supportsModernModifiers;
        XAttribute.REGISTRY.discardMetadata();
    }
}
