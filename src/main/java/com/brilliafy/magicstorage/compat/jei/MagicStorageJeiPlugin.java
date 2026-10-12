package com.brilliafy.magicstorage.compat.jei;

//? < 26 {
import com.brilliafy.magicstorage.Constants;
import com.brilliafy.magicstorage.init.ModMenus;
import com.brilliafy.magicstorage.menu.CraftingAccessMenu;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.gui.ingredient.IRecipeSlotView;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.transfer.IRecipeTransferError;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandler;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
//? >= 1.21 {
import net.minecraft.world.item.crafting.RecipeHolder;
//?}

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * JEI integration: recipe transfer from the Crafting Access terminal, pulling
 * ingredients from the connected storage network and/or the player inventory.
 */
@JeiPlugin
public class MagicStorageJeiPlugin implements IModPlugin {

    private static final ResourceLocation UID = Constants.id("jei_plugin");

    @Override
    public ResourceLocation getPluginUid() {
        return UID;
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(new CraftingTransferHandler(), RecipeTypes.CRAFTING);
    }

    //? < 1.21 {
    /*public static class CraftingTransferHandler
            implements IRecipeTransferHandler<CraftingAccessMenu, CraftingRecipe> {

        @Override
        public Class<? extends CraftingAccessMenu> getContainerClass() {
            return CraftingAccessMenu.class;
        }

        @Override
        public Optional<MenuType<CraftingAccessMenu>> getMenuType() {
            return Optional.of(ModMenus.CRAFTING_ACCESS);
        }

        @Override
        public RecipeType<CraftingRecipe> getRecipeType() {
            return RecipeTypes.CRAFTING;
        }

        @Override
        public IRecipeTransferError transferRecipe(CraftingAccessMenu menu, CraftingRecipe recipe,
                IRecipeSlotsView recipeSlotsView, Player player, boolean maxTransfer, boolean doTransfer) {
            return runTransfer(menu, recipeSlotsView, player, doTransfer);
        }
    }
    *///?} >= 1.21 {
    public static class CraftingTransferHandler
            implements IRecipeTransferHandler<CraftingAccessMenu, RecipeHolder<CraftingRecipe>> {

        @Override
        public Class<? extends CraftingAccessMenu> getContainerClass() {
            return CraftingAccessMenu.class;
        }

        @Override
        public Optional<MenuType<CraftingAccessMenu>> getMenuType() {
            return Optional.of(ModMenus.CRAFTING_ACCESS);
        }

        @Override
        public RecipeType<RecipeHolder<CraftingRecipe>> getRecipeType() {
            return RecipeTypes.CRAFTING;
        }

        @Override
        public IRecipeTransferError transferRecipe(CraftingAccessMenu menu, RecipeHolder<CraftingRecipe> recipe,
                IRecipeSlotsView recipeSlotsView, Player player, boolean maxTransfer, boolean doTransfer) {
            return runTransfer(menu, recipeSlotsView, player, doTransfer);
        }
    }
    //?}

    private static IRecipeTransferError runTransfer(CraftingAccessMenu menu, IRecipeSlotsView recipeSlots,
                                                    Player player, boolean doTransfer) {
        if (menu.getHeart() == null || !doTransfer) {
            return null;
        }

        Map<Integer, List<ItemStack>> inputs = new HashMap<>();
        List<IRecipeSlotView> slots = recipeSlots.getSlotViews(RecipeIngredientRole.INPUT);
        for (int i = 0; i < slots.size() && i < 9; i++) {
            List<ItemStack> candidates = new ArrayList<>();
            slots.get(i).getItemStacks().forEach(candidates::add);
            if (!candidates.isEmpty()) {
                inputs.put(i, candidates);
            }
        }

        JEICompat.transferRecipe(menu, player, inputs, true);
        return null;
    }
}
//?}
