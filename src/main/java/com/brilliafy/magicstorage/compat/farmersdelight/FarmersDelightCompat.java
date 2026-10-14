package com.brilliafy.magicstorage.compat.farmersdelight;

import com.brilliafy.magicstorage.ModernMagicStorage;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.TransientCraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Full compatibility handler for Farmer's Delight crafting stations:
 * 1. Cooking Pot: 1-6 shapeless food recipes requiring a container (bowl/bottle) if specified.
 * 2. Cutting Board: Input item + knife/axe/tool durability damage -> stripped logs, sliced bread, leather straps, cuts, etc.
 * 3. Stove and Skillet: Auxiliary station detection.
 */
public class FarmersDelightCompat {
    public static final String MOD_ID = "farmersdelight";

    public static boolean isLoaded() {
        return ModernMagicStorage.xplat().isModLoaded(MOD_ID);
    }

    public static boolean isCookingPot(ItemStack stack) {
        if (!isLoaded() || stack.isEmpty()) return false;
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(stack.getItem());
        return MOD_ID.equals(id.getNamespace()) && id.getPath().contains("cooking_pot");
    }

    public static boolean isCuttingBoard(ItemStack stack) {
        if (!isLoaded() || stack.isEmpty()) return false;
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(stack.getItem());
        return MOD_ID.equals(id.getNamespace()) && id.getPath().contains("cutting_board");
    }

    public static boolean isSkillet(ItemStack stack) {
        if (!isLoaded() || stack.isEmpty()) return false;
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(stack.getItem());
        return MOD_ID.equals(id.getNamespace()) && id.getPath().contains("skillet");
    }

    public static boolean isStove(ItemStack stack) {
        if (!isLoaded() || stack.isEmpty()) return false;
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(stack.getItem());
        return MOD_ID.equals(id.getNamespace()) && id.getPath().contains("stove");
    }

    public static boolean isFarmersDelightStation(ItemStack stack) {
        return isCookingPot(stack) || isCuttingBoard(stack) || isSkillet(stack) || isStove(stack);
    }

    public static Iterable<?> getAllRecipes(Level level) {
        if (level == null) return Collections.emptyList();
        try {
            Object manager = null;
            if (level.getServer() != null) {
                manager = level.getServer().getRecipeManager();
            }
            if (manager == null) {
                try {
                    Method m = level.getClass().getMethod("getRecipeManager");
                    manager = m.invoke(level);
                } catch (Throwable ignored) {
                    try {
                        Method m = level.getClass().getMethod("recipeAccess");
                        manager = m.invoke(level);
                    } catch (Throwable ignored2) {}
                }
            }
            if (manager != null) {
                Method getRecipes = manager.getClass().getMethod("getRecipes");
                return (Iterable<?>) getRecipes.invoke(manager);
            }
        } catch (Throwable ignored) {}
        return Collections.emptyList();
    }

    public static Object unwrapRecipe(Object entry) {
        if (entry == null) return null;
        try {
            Method valueMethod = entry.getClass().getMethod("value");
            return valueMethod.invoke(entry);
        } catch (Throwable t) {
            return entry;
        }
    }

    public static boolean isCookingRecipe(Object recipe) {
        if (recipe == null) return false;
        String name = recipe.getClass().getName();
        if (name.contains("CookingPotRecipe")) return true;
        try {
            Method getType = recipe.getClass().getMethod("getType");
            Object type = getType.invoke(recipe);
            if (type != null && type.toString().contains("farmersdelight:cooking")) {
                return true;
            }
        } catch (Throwable ignored) {}
        return false;
    }

    public static boolean isCuttingRecipe(Object recipe) {
        if (recipe == null) return false;
        String name = recipe.getClass().getName();
        if (name.contains("CuttingBoardRecipe")) return true;
        try {
            Method getType = recipe.getClass().getMethod("getType");
            Object type = getType.invoke(recipe);
            if (type != null && type.toString().contains("farmersdelight:cutting")) {
                return true;
            }
        } catch (Throwable ignored) {}
        return false;
    }

    public static ItemStack getRecipeResult(Object recipe, Level level) {
        if (recipe == null || level == null) return ItemStack.EMPTY;
        try {
            for (Method m : recipe.getClass().getMethods()) {
                if ("getResultItem".equals(m.getName()) && m.getParameterCount() == 1) {
                    ItemStack res = (ItemStack) m.invoke(recipe, level.registryAccess());
                    if (res != null && !res.isEmpty()) return res.copy();
                }
            }
        } catch (Throwable ignored) {}
        try {
            for (Method m : recipe.getClass().getMethods()) {
                if ("assemble".equals(m.getName()) && m.getParameterCount() == 0) {
                    ItemStack res = (ItemStack) m.invoke(recipe);
                    if (res != null && !res.isEmpty()) return res.copy();
                }
            }
        } catch (Throwable ignored) {}
        return ItemStack.EMPTY;
    }

    public static ItemStack getOutputContainer(Object recipe, ItemStack result) {
        if (recipe == null) return ItemStack.EMPTY;
        try {
            Method m = recipe.getClass().getMethod("getOutputContainer");
            ItemStack container = (ItemStack) m.invoke(recipe);
            if (container != null && !container.isEmpty()) {
                return container;
            }
        } catch (Throwable ignored) {}
        if (result != null && !result.isEmpty()) {
            try {
                Method getCraftingRemainingItem = result.getClass().getMethod("getCraftingRemainingItem");
                ItemStack rem = (ItemStack) getCraftingRemainingItem.invoke(result);
                if (rem != null && !rem.isEmpty()) return rem;
            } catch (Throwable ignored) {}
            try {
                net.minecraft.world.item.Item item = result.getItem();
                Method hasRemainder = item.getClass().getMethod("hasCraftingRemainingItem");
                if ((boolean) hasRemainder.invoke(item)) {
                    Method getRemainder = item.getClass().getMethod("getCraftingRemainingItem");
                    Object remItem = getRemainder.invoke(item);
                    if (remItem instanceof net.minecraft.world.item.Item it) {
                        return new ItemStack(it);
                    }
                }
            } catch (Throwable ignored) {}
            ResourceLocation key = BuiltInRegistries.ITEM.getKey(result.getItem());
            String path = key.getPath();
            if (path.contains("soup") || path.contains("stew") || path.contains("pasta") || path.contains("rice") || path.contains("curry") || path.contains("chowder")) {
                return new ItemStack(net.minecraft.world.item.Items.BOWL);
            }
        }
        return ItemStack.EMPTY;
    }

    public static List<Ingredient> getRecipeIngredients(Object recipe) {
        if (recipe == null) return Collections.emptyList();
        try {
            Method m = recipe.getClass().getMethod("getIngredients");
            Object res = m.invoke(recipe);
            if (res instanceof List<?> list) {
                List<Ingredient> ingredients = new ArrayList<>();
                for (Object item : list) {
                    if (item instanceof Ingredient ing) ingredients.add(ing);
                }
                return ingredients;
            }
        } catch (Throwable ignored) {}
        return Collections.emptyList();
    }

    public static Ingredient getCuttingBoardTool(Object recipe) {
        if (recipe == null) return null;
        try {
            Method m = recipe.getClass().getMethod("getTool");
            Object res = m.invoke(recipe);
            if (res instanceof Ingredient ing) return ing;
        } catch (Throwable ignored) {}
        return null;
    }

    /**
     * Evaluates Cooking Pot recipes against grid items:
     * Up to 6 food ingredients + optional container (bowl/bottle).
     */
    public static ItemStack computeCookingPotResult(List<ItemStack> nonNull, Level level) {
        if (!isLoaded() || nonNull == null || nonNull.isEmpty() || level == null) return ItemStack.EMPTY;

        for (Object entry : getAllRecipes(level)) {
            Object recipe = unwrapRecipe(entry);
            if (!isCookingRecipe(recipe)) continue;

            List<Ingredient> ingredients = getRecipeIngredients(recipe);
            if (ingredients.isEmpty() || ingredients.size() > 6) continue;

            ItemStack result = getRecipeResult(recipe, level);
            if (result.isEmpty()) continue;

            ItemStack reqContainer = getOutputContainer(recipe, result);

            if (!reqContainer.isEmpty()) {
                // Must have at least 1 matching container in the grid
                int containerIdx = -1;
                for (int i = 0; i < nonNull.size(); i++) {
                    if (nonNull.get(i).is(reqContainer.getItem())) {
                        containerIdx = i;
                        break;
                    }
                }
                if (containerIdx == -1) continue;

                List<ItemStack> foodInputs = new ArrayList<>(nonNull);
                foodInputs.remove(containerIdx);

                if (foodInputs.size() == ingredients.size() && matchesShapeless(foodInputs, ingredients)) {
                    return result;
                }
            } else {
                // No container required for this recipe
                if (nonNull.size() == ingredients.size() && matchesShapeless(nonNull, ingredients)) {
                    return result;
                }
            }
        }
        return ItemStack.EMPTY;
    }

    /**
     * Evaluates Cutting Board recipes:
     * 1 Item + 1 Tool (Knife, Axe, Shears, etc.).
     */
    public static ItemStack computeCuttingResult(ItemStack s0, ItemStack s1, Level level) {
        if (!isLoaded() || s0.isEmpty() || s1.isEmpty() || level == null) return ItemStack.EMPTY;

        for (Object entry : getAllRecipes(level)) {
            Object recipe = unwrapRecipe(entry);
            if (!isCuttingRecipe(recipe)) continue;

            List<Ingredient> ingredients = getRecipeIngredients(recipe);
            if (ingredients.isEmpty()) continue;

            Ingredient inputIngredient = ingredients.get(0);
            Ingredient toolIngredient = getCuttingBoardTool(recipe);
            if (toolIngredient == null) continue;

            if ((inputIngredient.test(s0) && toolIngredient.test(s1)) ||
                (inputIngredient.test(s1) && toolIngredient.test(s0))) {
                return getRecipeResult(recipe, level);
            }
        }
        return ItemStack.EMPTY;
    }

    /**
     * Checks if a slot in the grid holds the tool in a valid cutting board recipe.
     */
    public static boolean isCuttingToolSlot(TransientCraftingContainer craftSlots, int slot, Level level) {
        if (!isLoaded() || craftSlots == null || slot < 0 || slot >= craftSlots.getContainerSize() || level == null) {
            return false;
        }

        List<Integer> filledSlots = new ArrayList<>();
        for (int i = 0; i < craftSlots.getContainerSize(); i++) {
            if (!craftSlots.getItem(i).isEmpty()) {
                filledSlots.add(i);
            }
        }
        if (filledSlots.size() != 2 || !filledSlots.contains(slot)) return false;

        int otherSlot = filledSlots.get(0) == slot ? filledSlots.get(1) : filledSlots.get(0);
        ItemStack toolStack = craftSlots.getItem(slot);
        ItemStack inputStack = craftSlots.getItem(otherSlot);

        for (Object entry : getAllRecipes(level)) {
            Object recipe = unwrapRecipe(entry);
            if (!isCuttingRecipe(recipe)) continue;

            List<Ingredient> ingredients = getRecipeIngredients(recipe);
            if (ingredients.isEmpty()) continue;

            Ingredient inputIngredient = ingredients.get(0);
            Ingredient toolIngredient = getCuttingBoardTool(recipe);
            if (toolIngredient == null) continue;

            if (inputIngredient.test(inputStack) && toolIngredient.test(toolStack)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Extracts secondary results (e.g. Tree Bark from stripped logs) for a cutting recipe.
     */
    public static List<ItemStack> getSecondaryCuttingResults(ItemStack cutItem, ItemStack tool, Level level) {
        if (!isLoaded() || cutItem.isEmpty() || tool.isEmpty() || level == null) return Collections.emptyList();

        for (Object entry : getAllRecipes(level)) {
            Object recipe = unwrapRecipe(entry);
            if (!isCuttingRecipe(recipe)) continue;

            List<Ingredient> ingredients = getRecipeIngredients(recipe);
            if (ingredients.isEmpty()) continue;

            Ingredient inputIngredient = ingredients.get(0);
            Ingredient toolIngredient = getCuttingBoardTool(recipe);
            if (toolIngredient == null) continue;

            if (inputIngredient.test(cutItem) && toolIngredient.test(tool)) {
                try {
                    Method m = recipe.getClass().getMethod("getResults");
                    List<?> results = (List<?>) m.invoke(recipe);
                    if (results != null && results.size() > 1) {
                        List<ItemStack> extra = new ArrayList<>();
                        for (int i = 1; i < results.size(); i++) {
                            Object obj = results.get(i);
                            if (obj instanceof ItemStack st && !st.isEmpty()) {
                                extra.add(st.copy());
                            }
                        }
                        return extra;
                    }
                } catch (Throwable ignored) {}
            }
        }
        return Collections.emptyList();
    }

    /**
     * Handles awarding secondary drops and playing sound on cutting board craft.
     */
    public static void handleCuttingBoardDrops(ItemStack cutItem, ItemStack tool, Player player, Level level) {
        if (player == null || level == null) return;
        List<ItemStack> secondary = getSecondaryCuttingResults(cutItem, tool, level);
        for (ItemStack sec : secondary) {
            if (!player.getInventory().add(sec.copy())) {
                com.brilliafy.magicstorage.util.ItemHelper.dropItem(player, sec.copy());
            }
        }
        playCuttingSound(tool, cutItem, player, level);
    }

    public static void playCuttingSound(ItemStack tool, ItemStack cutItem, Player player, Level level) {
        if (level == null || player == null) return;
        if (tool.isDamageableItem()) {
            ResourceLocation toolKey = BuiltInRegistries.ITEM.getKey(tool.getItem());
            if (toolKey.getPath().contains("axe")) {
                level.playSound(null, player.getX(), player.getY(), player.getZ(),
                        SoundEvents.AXE_STRIP, SoundSource.PLAYERS, 1.0F, 1.0F);
                return;
            }
        }
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.SHEEP_SHEAR, SoundSource.PLAYERS, 0.8F, 1.2F);
    }

    /**
     * Pure recursive shapeless matcher for up to 6 ingredients.
     */
    public static boolean matchesShapeless(List<ItemStack> inputs, List<Ingredient> ingredients) {
        if (inputs.size() != ingredients.size()) return false;
        boolean[] used = new boolean[inputs.size()];
        return matchRecursive(inputs, ingredients, 0, used);
    }

    private static boolean matchRecursive(List<ItemStack> inputs, List<Ingredient> ingredients, int ingIndex, boolean[] used) {
        if (ingIndex == ingredients.size()) return true;
        Ingredient ing = ingredients.get(ingIndex);
        for (int i = 0; i < inputs.size(); i++) {
            if (!used[i] && ing.test(inputs.get(i))) {
                used[i] = true;
                if (matchRecursive(inputs, ingredients, ingIndex + 1, used)) {
                    return true;
                }
                used[i] = false;
            }
        }
        return false;
    }
}
