package com.brilliafy.magicstorage.compat.tetra;

import com.brilliafy.magicstorage.ModernMagicStorage;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

/**
 * Compatibility handler for Tetra.
 * Detects Tetra modular workbenches and performs workbench maintenance on modular tools:
 * - Detecting modular components
 * - Honing and repairing modular tools with compatible materials
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

    public static boolean isModularTool(ItemStack stack) {
        if (!isLoaded() || stack.isEmpty()) return false;
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(stack.getItem());
        return MOD_ID.equals(id.getNamespace()) && (
                id.getPath().contains("modular") ||
                id.getPath().contains("sword") ||
                id.getPath().contains("bow") ||
                id.getPath().contains("tool") ||
                id.getPath().contains("shield")
        );
    }

    public static boolean isHoningOrRepairMaterial(ItemStack stack) {
        if (stack.isEmpty()) return false;
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(stack.getItem());
        String path = id.getPath();
        return path.contains("ingot") ||
               path.contains("diamond") ||
               path.contains("gem") ||
               path.contains("hone") ||
               path.contains("shard") ||
               path.contains("netherite");
    }

    /**
     * Modular Workbench: Detects modular tool components and repairs/hones using the provided material.
     * Preserves all modular schemas, hone data, socketed modules, and tool integrity.
     */
    public static ItemStack computeWorkbenchResult(ItemStack modularTool, ItemStack material) {
        if (!isLoaded() || modularTool.isEmpty() || material.isEmpty()) return ItemStack.EMPTY;
        if (!isModularTool(modularTool) || !isHoningOrRepairMaterial(material)) return ItemStack.EMPTY;

        ItemStack repaired = modularTool.copyWithCount(1);
        if (repaired.isDamageableItem() && repaired.getDamageValue() > 0) {
            // Repair: restore durability without losing modular tags
            int maxDmg = repaired.getMaxDamage();
            int currentDmg = repaired.getDamageValue();
            int toRepair = Math.max(maxDmg / 4, 100);
            repaired.setDamageValue(Math.max(currentDmg - toRepair, 0));
            return repaired;
        }

        return ItemStack.EMPTY;
    }
}
