package com.brilliafy.magicstorage.compat.curios;

import com.brilliafy.magicstorage.ModernMagicStorage;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class CuriosCompat {
    public static final String MOD_ID = "curios";

    public static boolean isLoaded() {
        return ModernMagicStorage.xplat().isModLoaded(MOD_ID);
    }

    public static boolean isCuriosItem(ItemStack stack) {
        if (!isLoaded() || stack.isEmpty()) return false;
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(stack.getItem());
        return MOD_ID.equals(id.getNamespace());
    }

    public static boolean isReforger(ItemStack stack) {
        if (stack.isEmpty()) return false;
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(stack.getItem());
        String ns = id.getNamespace();
        return (MOD_ID.equals(ns) || "bountifulbaubles".equals(ns))
                && (id.getPath().contains("reforg") || id.getPath().contains("anvil") || id.getPath().contains("token") || id.getPath().contains("tablet"));
    }
}
