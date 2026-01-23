package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries;

import java.util.function.Function;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import org.bukkit.Registry;
import org.jetbrains.annotations.NotNull;
import java.util.Collection;
import java.util.Optional;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.base.XRegistry;
import org.bukkit.block.banner.PatternType;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.base.XModule;

public final class XPatternType extends XModule<XPatternType, PatternType>
{
    public static final XRegistry<XPatternType, PatternType> REGISTRY;
    public static final XPatternType BASE;
    public static final XPatternType SQUARE_BOTTOM_LEFT;
    public static final XPatternType SQUARE_BOTTOM_RIGHT;
    public static final XPatternType SQUARE_TOP_LEFT;
    public static final XPatternType SQUARE_TOP_RIGHT;
    public static final XPatternType STRIPE_BOTTOM;
    public static final XPatternType STRIPE_TOP;
    public static final XPatternType STRIPE_LEFT;
    public static final XPatternType STRIPE_RIGHT;
    public static final XPatternType STRIPE_CENTER;
    public static final XPatternType STRIPE_MIDDLE;
    public static final XPatternType STRIPE_DOWNRIGHT;
    public static final XPatternType STRIPE_DOWNLEFT;
    public static final XPatternType SMALL_STRIPES;
    public static final XPatternType CROSS;
    public static final XPatternType STRAIGHT_CROSS;
    public static final XPatternType TRIANGLE_BOTTOM;
    public static final XPatternType TRIANGLE_TOP;
    public static final XPatternType TRIANGLES_BOTTOM;
    public static final XPatternType TRIANGLES_TOP;
    public static final XPatternType DIAGONAL_LEFT;
    public static final XPatternType DIAGONAL_UP_RIGHT;
    public static final XPatternType DIAGONAL_UP_LEFT;
    public static final XPatternType DIAGONAL_RIGHT;
    public static final XPatternType CIRCLE;
    public static final XPatternType RHOMBUS;
    public static final XPatternType HALF_VERTICAL;
    public static final XPatternType HALF_HORIZONTAL;
    public static final XPatternType HALF_VERTICAL_RIGHT;
    public static final XPatternType HALF_HORIZONTAL_BOTTOM;
    public static final XPatternType BORDER;
    public static final XPatternType CURLY_BORDER;
    public static final XPatternType CREEPER;
    public static final XPatternType GRADIENT;
    public static final XPatternType GRADIENT_UP;
    public static final XPatternType BRICKS;
    public static final XPatternType SKULL;
    public static final XPatternType FLOWER;
    public static final XPatternType MOJANG;
    public static final XPatternType GLOBE;
    public static final XPatternType PIGLIN;
    public static final XPatternType FLOW;
    public static final XPatternType GUSTER;
    
    private XPatternType(final PatternType patternType, final String[] names) {
        super(patternType, names);
    }
    
    public static XPatternType of(final PatternType patternType) {
        return XPatternType.REGISTRY.getByBukkitForm(patternType);
    }
    
    public static Optional<XPatternType> of(final String patternType) {
        return XPatternType.REGISTRY.getByName(patternType);
    }
    
    @NotNull
    public static Collection<XPatternType> getValues() {
        return XPatternType.REGISTRY.getValues();
    }
    
    private static XPatternType std(final String... names) {
        return XPatternType.REGISTRY.std(names);
    }
    
    static {
        REGISTRY = new XRegistry<XPatternType, PatternType>(PatternType.class, XPatternType.class, (Supplier<Object>)(() -> Registry.BANNER_PATTERN), (java.util.function.BiFunction<PatternType, String[], XPatternType>)XPatternType::new, (java.util.function.Function<Integer, XPatternType[]>)(x$0 -> new XPatternType[x$0]));
        BASE = std("base");
        SQUARE_BOTTOM_LEFT = std("square_bottom_left");
        SQUARE_BOTTOM_RIGHT = std("square_bottom_right");
        SQUARE_TOP_LEFT = std("square_top_left");
        SQUARE_TOP_RIGHT = std("square_top_right");
        STRIPE_BOTTOM = std("stripe_bottom");
        STRIPE_TOP = std("stripe_top");
        STRIPE_LEFT = std("stripe_left");
        STRIPE_RIGHT = std("stripe_right");
        STRIPE_CENTER = std("stripe_center");
        STRIPE_MIDDLE = std("stripe_middle");
        STRIPE_DOWNRIGHT = std("stripe_downright");
        STRIPE_DOWNLEFT = std("stripe_downleft");
        SMALL_STRIPES = std("small_stripes", "STRIPE_SMALL");
        CROSS = std("cross");
        STRAIGHT_CROSS = std("straight_cross");
        TRIANGLE_BOTTOM = std("triangle_bottom");
        TRIANGLE_TOP = std("triangle_top");
        TRIANGLES_BOTTOM = std("triangles_bottom");
        TRIANGLES_TOP = std("triangles_top");
        DIAGONAL_LEFT = std("diagonal_left");
        DIAGONAL_UP_RIGHT = std("diagonal_up_right", "DIAGONAL_RIGHT_MIRROR");
        DIAGONAL_UP_LEFT = std("diagonal_up_left", "DIAGONAL_LEFT_MIRROR");
        DIAGONAL_RIGHT = std("diagonal_right");
        CIRCLE = std("circle", "CIRCLE_MIDDLE");
        RHOMBUS = std("rhombus", "RHOMBUS_MIDDLE");
        HALF_VERTICAL = std("half_vertical");
        HALF_HORIZONTAL = std("half_horizontal");
        HALF_VERTICAL_RIGHT = std("half_vertical_right", "HALF_VERTICAL_MIRROR");
        HALF_HORIZONTAL_BOTTOM = std("half_horizontal_bottom", "HALF_HORIZONTAL_MIRROR");
        BORDER = std("border");
        CURLY_BORDER = std("curly_border");
        CREEPER = std("creeper");
        GRADIENT = std("gradient");
        GRADIENT_UP = std("gradient_up");
        BRICKS = std("bricks");
        SKULL = std("skull");
        FLOWER = std("flower");
        MOJANG = std("mojang");
        GLOBE = std("globe");
        PIGLIN = std("piglin");
        FLOW = std("flow");
        GUSTER = std("guster");
        XPatternType.REGISTRY.discardMetadata();
    }
}
