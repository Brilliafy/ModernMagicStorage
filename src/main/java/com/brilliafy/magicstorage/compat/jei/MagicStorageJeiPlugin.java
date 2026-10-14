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
    }
}
