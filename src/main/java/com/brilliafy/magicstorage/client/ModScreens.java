package com.brilliafy.magicstorage.client;

import com.brilliafy.magicstorage.client.screen.CraftingAccessScreen;
import com.brilliafy.magicstorage.client.screen.StorageAccessScreen;
import com.brilliafy.magicstorage.client.screen.StorageUnitScreen;
import com.brilliafy.magicstorage.init.ModMenus;

//? fabric {
import net.minecraft.client.gui.screens.MenuScreens;
//?} neoforge {
/*import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
*///?} forge {
/*import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
*///?}

public class ModScreens {
    //? fabric {
    public static void registerScreens() {
        MenuScreens.register(ModMenus.STORAGE_ACCESS, StorageAccessScreen::new);
        MenuScreens.register(ModMenus.CRAFTING_ACCESS, CraftingAccessScreen::new);
        MenuScreens.register(ModMenus.STORAGE_UNIT, StorageUnitScreen::new);
    }
    //?} neoforge {
    /*public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenus.STORAGE_ACCESS, StorageAccessScreen::new);
        event.register(ModMenus.CRAFTING_ACCESS, CraftingAccessScreen::new);
        event.register(ModMenus.STORAGE_UNIT, StorageUnitScreen::new);
    }
    *///?} forge {
    /*public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(ModMenus.STORAGE_ACCESS, StorageAccessScreen::new);
            MenuScreens.register(ModMenus.CRAFTING_ACCESS, CraftingAccessScreen::new);
            MenuScreens.register(ModMenus.STORAGE_UNIT, StorageUnitScreen::new);
        });
    }
    *///?}
}
