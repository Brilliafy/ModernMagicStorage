package com.brilliafy.magicstorage.compat.tetra;

import com.brilliafy.magicstorage.ModernMagicStorage;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

/**
 * Compatibility handler for Tetra.
 * Detects Tetra modular workbenches installed as network crafting stations.
 */
public class TetraCompat {
    public static final String MOD_ID = "tetra";

    public static boolean isLoaded() {
        return ModernMagicStorage.xplat().isModLoaded(MOD_ID);
    }

    public static boolean isModularWorkbench(ItemStack stack) {
        if (!isLoaded() || stack.isEmpty()) return false;
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(stack.getItem());
        return MOD_ID.equals(id.getNamespace()) && id.getPath().contains("workbench");
    }
}
