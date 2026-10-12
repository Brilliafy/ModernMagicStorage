package com.brilliafy.magicstorage.platform.fabric;

//? fabric {
import com.brilliafy.magicstorage.Constants;
import com.brilliafy.magicstorage.ModernMagicStorage;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.GenerationStep;

public class FabricEntrypoint implements ModInitializer {

    @Override
    public void onInitialize() {
        ModernMagicStorage.onInitialize();

        ResourceLocation hellstoneId = Constants.id("ore_hellstone");
        ResourceLocation luminiteId = Constants.id("ore_luminite");

        BiomeModifications.addFeature(
                BiomeSelectors.foundInTheNether(),
                GenerationStep.Decoration.UNDERGROUND_ORES,
                ResourceKey.create(Registries.PLACED_FEATURE, hellstoneId)
        );
        BiomeModifications.addFeature(
                BiomeSelectors.foundInTheEnd(),
                GenerationStep.Decoration.UNDERGROUND_ORES,
                ResourceKey.create(Registries.PLACED_FEATURE, luminiteId)
        );
    }
}
//?}
