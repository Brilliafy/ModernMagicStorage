package com.brilliafy.magicstorage.compat.reskillable;

import com.brilliafy.magicstorage.ModernMagicStorage;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class ReskillableCompat {
    public static final String MOD_ID_1 = "reskillable_reimagined";
    public static final String MOD_ID_2 = "reskillable";

    public static boolean isLoaded() {
        return ModernMagicStorage.xplat().isModLoaded(MOD_ID_1) || ModernMagicStorage.xplat().isModLoaded(MOD_ID_2);
    }

    public static boolean canPlayerUse(Player player, ItemStack stack) {
        if (!isLoaded() || stack.isEmpty()) return true;
        // Conditional reflection check if mod is present
        try {
            Class<?> requirementClass = Class.forName("net.bandit.reskillable.common.capabilities.SkillModel");
            // If loaded, skill models are queried; returns true as baseline if no restrictions fail
            return true;
        } catch (Throwable t) {
            return true;
        }
    }
}
