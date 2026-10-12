package com.brilliafy.magicstorage.platform.fabric;

//? fabric {
import com.brilliafy.magicstorage.platform.Platform;
import net.fabricmc.loader.api.FabricLoader;

public class FabricPlatform implements Platform {

    @Override
    public boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public ModLoader loader() {
        return ModLoader.FABRIC;
    }

    @Override
    public String mcVersion() {
        return FabricLoader.getInstance().getModContainer("minecraft")
                .map(c -> c.getMetadata().getVersion().getFriendlyString())
                .orElse("unknown");
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }
}
//?}
