package com.brilliafy.magicstorage.compat.qualitytools;

import com.brilliafy.magicstorage.ModernMagicStorage;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class QualityEquipmentCompat {
    public static final String MOD_ID_1 = "quality_equipment";
    public static final String MOD_ID_2 = "qualitytools";

    public static boolean isLoaded() {
        return ModernMagicStorage.xplat().isModLoaded(MOD_ID_1) || ModernMagicStorage.xplat().isModLoaded(MOD_ID_2);
    }

    public static boolean isReforgingStation(ItemStack stack) {
        if (!isLoaded() || stack.isEmpty()) return false;
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(stack.getItem());
        String ns = id.getNamespace();
        return (MOD_ID_1.equals(ns) || MOD_ID_2.equals(ns)) && id.getPath().contains("reforg");
    }
}
