package com.brilliafy.magicstorage.client;

import com.brilliafy.magicstorage.client.screen.CraftingAccessScreen;
import com.brilliafy.magicstorage.client.screen.StorageAccessScreen;
import com.brilliafy.magicstorage.client.screen.StorageUnitScreen;
import com.brilliafy.magicstorage.init.ModMenus;
import net.minecraft.client.gui.screens.MenuScreens;

public class ModScreens {
    public static void registerScreens() {
        MenuScreens.register(ModMenus.STORAGE_ACCESS, StorageAccessScreen::new);
        MenuScreens.register(ModMenus.CRAFTING_ACCESS, CraftingAccessScreen::new);
        MenuScreens.register(ModMenus.STORAGE_UNIT, StorageUnitScreen::new);
    }
}
