package cc.dreamcode.antylogout.libs.eu.okaeri.configs.yaml.bukkit.serdes.serializer;

import lombok.Generated;
import java.util.stream.Stream;
import java.util.Iterator;
import org.bukkit.inventory.RecipeChoice;
import java.util.Objects;
import org.bukkit.NamespacedKey;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.serdes.DeserializationData;
import org.bukkit.inventory.ItemStack;
import java.util.Collections;
import org.bukkit.Material;
import java.util.List;
import java.util.stream.Collectors;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Collection;
import java.util.Arrays;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.schema.GenericsDeclaration;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.serdes.SerializationData;
import lombok.NonNull;
import org.bukkit.plugin.Plugin;
import org.bukkit.inventory.ShapedRecipe;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.serdes.ObjectSerializer;

public class ShapedRecipeSerializer implements ObjectSerializer<ShapedRecipe>
{
    private static Boolean hasNamespacedKey;
    private static Boolean hasRecipeChoices;
    private final Plugin plugin;
    
    @Override
    public boolean supports(@NonNull final Class<? super ShapedRecipe> type) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        return ShapedRecipe.class.isAssignableFrom(type);
    }
    
    @Override
    public void serialize(@NonNull final ShapedRecipe shapedRecipe, @NonNull final SerializationData data, @NonNull final GenericsDeclaration generics) {
        if (shapedRecipe == null) {
            throw new NullPointerException("shapedRecipe is marked non-null but is null");
        }
        if (data == null) {
            throw new NullPointerException("data is marked non-null but is null");
        }
        if (generics == null) {
            throw new NullPointerException("generics is marked non-null but is null");
        }
        if (hasNamespacedKey()) {
            data.add("key", shapedRecipe.getKey().getKey());
        }
        final List<String> shapeList = (List<String>)Arrays.asList((Object[])shapedRecipe.getShape());
        data.addCollection("shape", (Collection<?>)shapeList, String.class);
        if (hasRecipeChoices()) {
            final Map<String, List> choices = (Map<String, List>)shapedRecipe.getChoiceMap().entrySet().stream().collect(Collectors.toMap(entry -> String.valueOf(entry.getKey()), entry -> {
                if (entry.getValue() instanceof RecipeChoice.ExactChoice) {
                    final List<ItemStack> stacks = (List<ItemStack>)((RecipeChoice.ExactChoice)entry.getValue()).getChoices();
                    final GenericsDeclaration listType = GenericsDeclaration.of(List.class, (List<Object>)Collections.singletonList((Object)ItemStack.class));
                    return (List)data.getConfigurer().simplifyCollection((Collection<?>)stacks, listType, data.getContext(), true);
                }
                if (entry.getValue() instanceof RecipeChoice.MaterialChoice) {
                    final List<Material> materials = (List<Material>)((RecipeChoice.MaterialChoice)entry.getValue()).getChoices();
                    final GenericsDeclaration listType = GenericsDeclaration.of(List.class, (List<Object>)Collections.singletonList((Object)Material.class));
                    return (List)data.getConfigurer().simplifyCollection((Collection<?>)materials, listType, data.getContext(), true);
                }
                throw new IllegalArgumentException("Unknown choice type in recipe: " + (Object)((RecipeChoice)entry.getValue()).getClass() + " [" + (Object)entry + "]");
            }, (u, v) -> {
                throw new IllegalStateException("Duplicate recipe key u=" + (Object)u + ", v=" + (Object)v);
            }, LinkedHashMap::new));
            data.addRaw("ingredients", choices);
        }
        else {
            final Map<Character, List<Material>> ingredientMap = (Map<Character, List<Material>>)shapedRecipe.getIngredientMap().entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> Collections.singletonList((Object)((ItemStack)entry.getValue()).getType()), (u, v) -> {
                throw new IllegalStateException("Duplicate recipe key u=" + (Object)u + ", v=" + (Object)v);
            }, LinkedHashMap::new));
            final GenericsDeclaration valueType = GenericsDeclaration.of(List.class, (List<Object>)Collections.singletonList((Object)Material.class));
            data.addAsMap("ingredients", ingredientMap, GenericsDeclaration.of(Map.class, (List<Object>)Arrays.asList(new Object[] { Character.class, valueType })));
        }
        data.add("result", shapedRecipe.getResult(), ItemStack.class);
    }
    
    @Override
    public ShapedRecipe deserialize(@NonNull final DeserializationData data, @NonNull final GenericsDeclaration generics) {
        if (data == null) {
            throw new NullPointerException("data is marked non-null but is null");
        }
        if (generics == null) {
            throw new NullPointerException("generics is marked non-null but is null");
        }
        final ItemStack result = data.get("result", ItemStack.class);
        final List<String> shape = data.getAsList("shape", String.class);
        final ShapedRecipe recipe = hasNamespacedKey() ? new ShapedRecipe(new NamespacedKey(this.plugin, (String)data.get("key", String.class)), result) : new ShapedRecipe(result);
        recipe.shape((String[])shape.toArray((Object[])new String[0]));
        final Map<Character, Object> ingredients = data.getAsMap("ingredients", Character.class, Object.class);
        for (final Map.Entry<Character, Object> entry : ingredients.entrySet()) {
            if (!(entry.getValue() instanceof Collection)) {
                throw new IllegalArgumentException("Unknown recipe ingredient for " + entry.getKey() + ": " + entry.getValue() + " (" + (Object)entry.getValue().getClass() + ")");
            }
            final List<?> list = (List<?>)entry.getValue();
            if (list.isEmpty()) {
                throw new IllegalArgumentException("Empty ingredients list for " + entry.getKey());
            }
            if (hasRecipeChoices()) {
                final Object firstElement = list.get(0);
                if (firstElement instanceof String) {
                    final ShapedRecipe shapedRecipe = recipe;
                    final char charValue = (char)entry.getKey();
                    final Stream stream = list.stream();
                    final Class<String> clazz = String.class;
                    Objects.requireNonNull((Object)clazz);
                    shapedRecipe.setIngredient(charValue, (RecipeChoice)new RecipeChoice.MaterialChoice((List)stream.map(clazz::cast).map(Material::valueOf).collect(Collectors.toList())));
                }
                else {
                    if (!(firstElement instanceof Map)) {
                        throw new IllegalArgumentException("Unknown recipe ingredient type for " + entry.getKey() + ": " + (Object)firstElement.getClass());
                    }
                    recipe.setIngredient((char)entry.getKey(), (RecipeChoice)new RecipeChoice.ExactChoice((List)list.stream().map(map -> data.getConfigurer().resolveType(map, GenericsDeclaration.of(map), ItemStack.class, GenericsDeclaration.of(ItemStack.class), data.getContext())).collect(Collectors.toList())));
                }
            }
            else {
                if (list.size() > 1) {
                    throw new IllegalArgumentException("Recipes with more than one Material are not allowed on this version: " + (Object)list);
                }
                final Object firstElement = list.get(0);
                if (!(firstElement instanceof String)) {
                    throw new IllegalArgumentException("Unknown recipe ingredient type for " + entry.getKey() + ": " + (Object)firstElement.getClass());
                }
                final Material material = Material.valueOf((String)firstElement);
                recipe.setIngredient((char)entry.getKey(), material);
            }
        }
        return recipe;
    }
    
    private static boolean hasNamespacedKey() {
        if (ShapedRecipeSerializer.hasNamespacedKey == null) {
            try {
                Class.forName("org.bukkit.NamespacedKey");
                ShapedRecipeSerializer.hasNamespacedKey = true;
            }
            catch (final ClassNotFoundException ignored) {
                ShapedRecipeSerializer.hasNamespacedKey = false;
            }
        }
        return ShapedRecipeSerializer.hasNamespacedKey;
    }
    
    private static boolean hasRecipeChoices() {
        if (ShapedRecipeSerializer.hasRecipeChoices == null) {
            try {
                Class.forName("org.bukkit.inventory.RecipeChoice");
                ShapedRecipeSerializer.hasRecipeChoices = true;
            }
            catch (final ClassNotFoundException ignored) {
                ShapedRecipeSerializer.hasRecipeChoices = false;
            }
        }
        return ShapedRecipeSerializer.hasRecipeChoices;
    }
    
    @Generated
    public ShapedRecipeSerializer(final Plugin plugin) {
        this.plugin = plugin;
    }
    
    static {
        ShapedRecipeSerializer.hasNamespacedKey = null;
        ShapedRecipeSerializer.hasRecipeChoices = null;
    }
}
