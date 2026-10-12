package com.brilliafy.magicstorage.platform.forge;

//? forge {
/*import com.brilliafy.magicstorage.Constants;
import com.brilliafy.magicstorage.ModernMagicStorage;
import com.brilliafy.magicstorage.client.ModScreens;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Constants.MOD_ID)
public class ForgeEntrypoint {

    public ForgeEntrypoint() {
        ModernMagicStorage.onInitialize();
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(ModScreens::onClientSetup);
    }
}
*///?}
