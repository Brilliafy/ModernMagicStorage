package com.brilliafy.magicstorage.compat.apotheosis;

import com.brilliafy.magicstorage.ModernMagicStorage;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class ApotheosisCompat {
    public static final String MOD_ID = "apotheosis";

    public static boolean isLoaded() {
        return ModernMagicStorage.xplat().isModLoaded(MOD_ID);
    }

    public static boolean isScrapTome(ItemStack stack) {
        if (!isLoaded() || stack.isEmpty()) return false;
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(stack.getItem());
        return MOD_ID.equals(id.getNamespace()) && (id.getPath().contains("scrap") || id.getPath().equals("scrap_tome"));
    }

    public static boolean isExtractionTome(ItemStack stack) {
        if (!isLoaded() || stack.isEmpty()) return false;
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(stack.getItem());
        return MOD_ID.equals(id.getNamespace()) && (id.getPath().contains("extraction") || id.getPath().equals("extraction_tome"));
    }

    public static boolean isDisenchantingStation(ItemStack stack) {
        return isScrapTome(stack) || isExtractionTome(stack);
    }
}
