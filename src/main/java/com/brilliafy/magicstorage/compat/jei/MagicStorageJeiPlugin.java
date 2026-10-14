package com.brilliafy.magicstorage.compat.jei;

import com.brilliafy.magicstorage.Constants;
import com.brilliafy.magicstorage.init.ModMenus;
import com.brilliafy.magicstorage.menu.CraftingAccessMenu;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import net.minecraft.resources.ResourceLocation;

/**
 * JEI integration: recipe transfer for the Crafting Access terminal.
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
        registration.addRecipeTransferHandler(
                CraftingAccessMenu.class,
                ModMenus.CRAFTING_ACCESS,
                RecipeTypes.CRAFTING,
                73, // recipeSlotStart (9 craft grid slots)
                9,  // recipeSlotCount
                36, // inventorySlotStart (36 player inventory slots)
                36  // inventorySlotCount
        );
        registration.addRecipeTransferHandler(
                CraftingAccessMenu.class,
                ModMenus.CRAFTING_ACCESS,
                RecipeTypes.STONECUTTING,
                77, // Center slot (craftSlots index 4) targeting Stonecutter
                1,
                36,
                36
        );
        registration.addRecipeTransferHandler(
                CraftingAccessMenu.class,
                ModMenus.CRAFTING_ACCESS,
                RecipeTypes.SMITHING,
                73,
                3,
                36,
                36
        );
        registration.addRecipeTransferHandler(
                CraftingAccessMenu.class,
                ModMenus.CRAFTING_ACCESS,
                RecipeTypes.SMELTING,
                73,
                1,
                36,
                36
        );
        registration.addRecipeTransferHandler(
                CraftingAccessMenu.class,
                ModMenus.CRAFTING_ACCESS,
                RecipeTypes.BLASTING,
                73,
                1,
                36,
                36
        );
        registration.addRecipeTransferHandler(
                CraftingAccessMenu.class,
                ModMenus.CRAFTING_ACCESS,
                RecipeTypes.SMOKING,
                73,
                1,
                36,
                36
        );

        if (com.brilliafy.magicstorage.compat.farmersdelight.FarmersDelightCompat.isLoaded()) {
            try {
                Class<?> fdTypes = Class.forName("vectorwing.farmersdelight.integration.jei.FDRecipeTypes");
                java.lang.reflect.Field cookingField = fdTypes.getField("COOKING");
                mezz.jei.api.recipe.RecipeType<?> cookingType = (mezz.jei.api.recipe.RecipeType<?>) cookingField.get(null);
                if (cookingType != null) {
                    registration.addRecipeTransferHandler(
                            CraftingAccessMenu.class,
                            ModMenus.CRAFTING_ACCESS,
                            cookingType,
                            73,
                            7,
                            36,
                            36
                    );
                }
                java.lang.reflect.Field cuttingField = fdTypes.getField("CUTTING");
                mezz.jei.api.recipe.RecipeType<?> cuttingType = (mezz.jei.api.recipe.RecipeType<?>) cuttingField.get(null);
                if (cuttingType != null) {
                    registration.addRecipeTransferHandler(
                            CraftingAccessMenu.class,
                            ModMenus.CRAFTING_ACCESS,
                            cuttingType,
                            73,
                            2,
                            36,
                            36
                    );
                }
            } catch (Throwable ignored) {}
        }
    }
}
