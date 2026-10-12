package com.brilliafy.magicstorage.platform.fabric;

//? fabric {
import com.brilliafy.magicstorage.ModernMagicStorage;
import net.fabricmc.api.ClientModInitializer;

public class FabricClientEntrypoint implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ModernMagicStorage.onInitializeClient();
    }
}
//?}
