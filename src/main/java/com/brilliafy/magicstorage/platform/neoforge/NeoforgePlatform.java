package com.brilliafy.magicstorage.platform.neoforge;

//? neoforge {
/*import com.brilliafy.magicstorage.platform.Platform;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;

public class NeoforgePlatform implements Platform {

    @Override
    public boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    @Override
    public ModLoader loader() {
        return ModLoader.NEOFORGE;
    }

    @Override
    public String mcVersion() {
        //? >= 26.1 {
        /^return FMLLoader.getCurrent().getVersionInfo().mcVersion();
        ^///?} < 26.1 {
        return FMLLoader.versionInfo().mcVersion();
        //?}
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        //? >= 26.1 {
        /^return !FMLLoader.getCurrent().isProduction();
        ^///?} < 26.1 {
        return !FMLLoader.isProduction();
        //?}
    }
}
*///?}
