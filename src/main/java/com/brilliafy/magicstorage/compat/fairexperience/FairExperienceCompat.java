package com.brilliafy.magicstorage.compat.fairexperience;

import com.brilliafy.magicstorage.ModernMagicStorage;
import net.minecraft.world.entity.player.Player;

import java.lang.reflect.Method;

/**
 * Compatibility handler for Fair Experience (fairexperience).
 * <p>
 * When Fair Experience is loaded alongside Magic Storage, this handler delegates
 * experience checks, deduction context setup, and fair raw XP calculations to
 * Fair Experience's context and calculation engine via safe reflection.
 * If Fair Experience is not present, standard vanilla level logic is used.
 */
public final class FairExperienceCompat {
    public static final String MOD_ID = "fairexperience";

    private static Method setEnchantReqLevelMethod;
    private static Method clearEnchantReqLevelMethod;
    private static Method getAnvilFairXpCostMethod;
    private static Method getEnchantFairXpCostMethod;
    private static Method getPlayerTotalXpMethod;
    private static boolean initialized = false;

    private FairExperienceCompat() {}

    /**
     * Checks if Fair Experience is currently loaded in the mod runtime.
     */
    public static boolean isLoaded() {
        return ModernMagicStorage.xplat().isModLoaded(MOD_ID);
    }

    private static synchronized void initReflection() {
        if (initialized) return;
        initialized = true;
        try {
            Class<?> contextClass = Class.forName("com.brilliafy.fairexperience.FairExperienceContext");
            setEnchantReqLevelMethod = contextClass.getMethod("setEnchantReqLevel", int.class);
            clearEnchantReqLevelMethod = contextClass.getMethod("clearEnchantReqLevel");
        } catch (Throwable ignored) {}

        try {
            Class<?> xpHelperClass = Class.forName("com.brilliafy.fairexperience.XpHelper");
            getAnvilFairXpCostMethod = xpHelperClass.getMethod("getAnvilFairXpCost", int.class);
            getEnchantFairXpCostMethod = xpHelperClass.getMethod("getEnchantFairXpCost", int.class, int.class);
            getPlayerTotalXpMethod = xpHelperClass.getMethod("getPlayerTotalXp", Player.class);
        } catch (Throwable ignored) {}
    }

    /**
     * Prepares the Fair Experience enchanting context before deducting levels,
     * ensuring that Fair Experience can accurately apply fair raw XP enchanting costs.
     */
    public static void prepareEnchantContext(int reqLevel) {
        if (!isLoaded()) return;
        initReflection();
        if (setEnchantReqLevelMethod != null) {
            try {
                setEnchantReqLevelMethod.invoke(null, reqLevel);
            } catch (Throwable ignored) {}
        }
    }

    /**
     * Clears the Fair Experience enchanting context after level deduction.
     */
    public static void clearEnchantContext() {
        if (!isLoaded()) return;
        initReflection();
        if (clearEnchantReqLevelMethod != null) {
            try {
                clearEnchantReqLevelMethod.invoke(null);
            } catch (Throwable ignored) {}
        }
    }

    /**
     * Returns the fair raw XP cost for an anvil operation.
     */
    public static int getAnvilFairXpCost(int levelCost) {
        if (isLoaded()) {
            initReflection();
            if (getAnvilFairXpCostMethod != null) {
                try {
                    return (int) getAnvilFairXpCostMethod.invoke(null, levelCost);
                } catch (Throwable ignored) {}
            }
        }
        return levelCost;
    }

    /**
     * Returns the fair raw XP cost for an enchanting operation.
     */
    public static int getEnchantFairXpCost(int reqLevel, int levelCost) {
        if (isLoaded()) {
            initReflection();
            if (getEnchantFairXpCostMethod != null) {
                try {
                    return (int) getEnchantFairXpCostMethod.invoke(null, reqLevel, levelCost);
                } catch (Throwable ignored) {}
            }
        }
        return levelCost;
    }

    /**
     * Returns the player's exact total raw XP.
     */
    public static int getPlayerTotalXp(Player player) {
        if (isLoaded()) {
            initReflection();
            if (getPlayerTotalXpMethod != null) {
                try {
                    return (int) getPlayerTotalXpMethod.invoke(null, player);
                } catch (Throwable ignored) {}
            }
        }
        return player.totalExperience;
    }

    /**
     * Checks if a player has sufficient experience for a level cost.
     */
    public static boolean hasEnoughXp(Player player, int levelCost) {
        if (player.getAbilities().instabuild) return true;
        return player.experienceLevel >= levelCost;
    }

    /**
     * Consumes experience for an anvil operation, applying Fair Experience's raw XP conversion
     * when loaded, or vanilla level deduction otherwise.
     */
    public static void consumeAnvilXp(Player player, int levelCost) {
        if (player.getAbilities().instabuild || levelCost <= 0) return;
        player.giveExperienceLevels(-levelCost);
    }

    /**
     * Consumes experience for an enchanting operation with context tracking.
     */
    public static void consumeEnchantXp(Player player, int reqLevel, int levelCost) {
        if (player.getAbilities().instabuild || levelCost <= 0) return;
        prepareEnchantContext(reqLevel);
        try {
            player.giveExperienceLevels(-levelCost);
        } finally {
            clearEnchantContext();
        }
    }
}
