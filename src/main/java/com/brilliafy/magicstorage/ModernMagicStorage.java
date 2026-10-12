package com.brilliafy.magicstorage;

import com.brilliafy.magicstorage.init.ModBlockEntities;
import com.brilliafy.magicstorage.init.ModBlocks;
import com.brilliafy.magicstorage.init.ModCreativeTabs;
import com.brilliafy.magicstorage.init.ModItems;
import com.brilliafy.magicstorage.init.ModMenus;
import com.brilliafy.magicstorage.platform.Platform;
//? fabric {
import com.brilliafy.magicstorage.platform.fabric.FabricPlatform;
//?} neoforge {
/*import com.brilliafy.magicstorage.platform.neoforge.NeoforgePlatform;
*///?} forge {
/*import com.brilliafy.magicstorage.platform.forge.ForgePlatform;
*///?}

public class ModernMagicStorage {
    private static final Platform PLATFORM = createPlatformInstance();

    public static void onInitialize() {
        Constants.LOGGER.info("Initializing {} on {}", Constants.MOD_NAME, xplat().loader());

        ModBlocks.registerAll();
        ModItems.registerAll();
        ModBlockEntities.registerAll();
        ModMenus.registerAll();
        ModCreativeTabs.registerAll();
    }

    public static void onInitializeClient() {
        Constants.LOGGER.info("Initializing {} Client", Constants.MOD_NAME);
        //? fabric {
        com.brilliafy.magicstorage.client.ModScreens.registerScreens();
        //?}
    }

    public static Platform xplat() {
        return PLATFORM;
    }

    private static Platform createPlatformInstance() {
        //? fabric {
        return new FabricPlatform();
        //?} neoforge {
        /*return new NeoforgePlatform();
        *///?} forge {
        /*return new ForgePlatform();
        *///?}
    }
}
