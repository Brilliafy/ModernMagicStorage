package com.brilliafy.magicstorage.compat.apotheosis;

import com.brilliafy.magicstorage.ModernMagicStorage;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

/**
 * Compatibility handler for Apotheosis crafting stations.
 * Detects Reforging Tables, Salvaging Tables, and Gem Cutting Tables installed
 * in the Magic Storage network.
 */
public class ApotheosisCompat {
    public static final String MOD_ID = "apotheosis";

    public static boolean isLoaded() {
        return ModernMagicStorage.xplat().isModLoaded(MOD_ID);
    }

    public static boolean isReforgingTable(ItemStack stack) {
        if (!isLoaded() || stack.isEmpty()) return false;
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(stack.getItem());
        return MOD_ID.equals(id.getNamespace()) && id.getPath().contains("reforg");
    }

    public static boolean isSalvagingTable(ItemStack stack) {
        if (!isLoaded() || stack.isEmpty()) return false;
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(stack.getItem());
        return MOD_ID.equals(id.getNamespace()) && id.getPath().contains("salvag");
    }

    public static boolean isGemCuttingTable(ItemStack stack) {
        if (!isLoaded() || stack.isEmpty()) return false;
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(stack.getItem());
        return MOD_ID.equals(id.getNamespace()) && id.getPath().contains("gem_cutting");
    }

    public static boolean isApotheosisStation(ItemStack stack) {
        return isReforgingTable(stack) || isSalvagingTable(stack) || isGemCuttingTable(stack);
    }
}
