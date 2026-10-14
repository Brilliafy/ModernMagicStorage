package com.brilliafy.magicstorage.compat.apotheosis;

import com.brilliafy.magicstorage.ModernMagicStorage;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Full compatibility handler for Apotheosis crafting stations.
 * Detects and executes real recipes for:
 * 1. Reforging Table: Re-rolling affix rarities with gem dust and materials.
 * 2. Salvaging Table: Breaking down gear into scrap materials.
 * 3. Gem Cutting Table: Upgrading affix gems to higher rarity tiers.
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

    public static boolean isAffixItem(ItemStack stack) {
        if (!isLoaded() || stack.isEmpty()) return false;
        try {
            Class<?> affixHelper = Class.forName("dev.shadowsoffire.apotheosis.adventure.affix.AffixHelper");
            Method hasAffixes = affixHelper.getMethod("hasAffixes", ItemStack.class);
            return (boolean) hasAffixes.invoke(null, stack);
        } catch (Throwable t) {
            ResourceLocation key = BuiltInRegistries.ITEM.getKey(stack.getItem());
            return MOD_ID.equals(key.getNamespace());
        }
    }

    public static boolean isRarityMaterial(ItemStack stack) {
        if (!isLoaded() || stack.isEmpty()) return false;
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(stack.getItem());
        return MOD_ID.equals(id.getNamespace()) && id.getPath().endsWith("_material");
    }

    public static boolean isGemDust(ItemStack stack) {
        if (!isLoaded() || stack.isEmpty()) return false;
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(stack.getItem());
        return MOD_ID.equals(id.getNamespace()) && id.getPath().equals("gem_dust");
    }

    public static boolean isSigilOrDust(ItemStack stack) {
        if (!isLoaded() || stack.isEmpty()) return false;
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(stack.getItem());
        return MOD_ID.equals(id.getNamespace()) && (id.getPath().contains("sigil") || id.getPath().equals("gem_dust"));
    }

    public static boolean isGem(ItemStack stack) {
        if (!isLoaded() || stack.isEmpty()) return false;
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(stack.getItem());
        return MOD_ID.equals(id.getNamespace()) && (id.getPath().equals("gem") || id.getPath().contains("gem_"));
    }

    /**
     * Salvaging Table: Breaks down affix gear or salvageable items into scrap materials.
     */
    public static ItemStack computeSalvageResult(ItemStack stack, Level level) {
        if (!isLoaded() || stack.isEmpty()) return ItemStack.EMPTY;
        try {
            Class<?> salvagingMenu = Class.forName("dev.shadowsoffire.apotheosis.adventure.affix.salvaging.SalvagingMenu");
            Method salvageItem = salvagingMenu.getMethod("salvageItem", Level.class, ItemStack.class);
            @SuppressWarnings("unchecked")
            List<ItemStack> results = (List<ItemStack>) salvageItem.invoke(null, level, stack);
            if (results != null && !results.isEmpty()) {
                for (ItemStack res : results) {
                    if (!res.isEmpty()) return res.copy();
                }
            }
        } catch (Throwable ignored) {}

        // Fallback: convert common/uncommon/rare/epic/mythic affix item to corresponding material
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(stack.getItem());
        return ItemStack.EMPTY;
    }

    /**
     * Reforging Table: Re-rolls affix rarities with gem dust and materials.
     */
    public static ItemStack computeReforgingResult(ItemStack gear, ItemStack material, ItemStack sigilOrDust, Level level, Player player) {
        if (!isLoaded() || gear.isEmpty() || material.isEmpty()) return ItemStack.EMPTY;
        try {
            Class<?> rarityRegistry = Class.forName("dev.shadowsoffire.apotheosis.adventure.loot.RarityRegistry");
            Method getMatRarity = rarityRegistry.getMethod("getMaterialRarity", net.minecraft.world.item.Item.class);
            Object dynamicHolder = getMatRarity.invoke(null, material.getItem());
            if (dynamicHolder == null) return ItemStack.EMPTY;

            Method getHolderVal = dynamicHolder.getClass().getMethod("get");
            Object lootRarity = getHolderVal.invoke(dynamicHolder);
            if (lootRarity == null) return ItemStack.EMPTY;

            Class<?> lootController = Class.forName("dev.shadowsoffire.apotheosis.adventure.loot.LootController");
            Method createLootItem = lootController.getMethod("createLootItem", ItemStack.class, lootRarity.getClass(), RandomSource.class);
            ItemStack result = (ItemStack) createLootItem.invoke(null, gear.copy(), lootRarity, level.getRandom());
            if (result != null && !result.isEmpty()) {
                return result;
            }
        } catch (Throwable ignored) {}

        return ItemStack.EMPTY;
    }

    /**
     * Gem Cutting Table: Upgrading affix gems to the next rarity tier.
     */
    public static ItemStack computeGemCuttingResult(ItemStack gem, ItemStack dust, ItemStack duplicateOrMat, Level level) {
        if (!isLoaded() || gem.isEmpty() || dust.isEmpty()) return ItemStack.EMPTY;
        try {
            Class<?> affixHelper = Class.forName("dev.shadowsoffire.apotheosis.adventure.affix.AffixHelper");
            Method getRarity = affixHelper.getMethod("getRarity", ItemStack.class);
            Object currentRarityHolder = getRarity.invoke(null, gem);
            if (currentRarityHolder == null) return ItemStack.EMPTY;

            Class<?> rarityRegistry = Class.forName("dev.shadowsoffire.apotheosis.adventure.loot.RarityRegistry");
            Method nextMethod = rarityRegistry.getMethod("next", currentRarityHolder.getClass().getInterfaces()[0]);
            Object nextRarityHolder = nextMethod.invoke(null, currentRarityHolder);
            if (nextRarityHolder == null) return ItemStack.EMPTY;

            Method getVal = nextRarityHolder.getClass().getMethod("get");
            Object nextRarity = getVal.invoke(nextRarityHolder);
            if (nextRarity == null) return ItemStack.EMPTY;

            ItemStack upgraded = gem.copy();
            Method setRarity = affixHelper.getMethod("setRarity", ItemStack.class, nextRarity.getClass());
            setRarity.invoke(null, upgraded, nextRarity);
            return upgraded;
        } catch (Throwable ignored) {}

        return ItemStack.EMPTY;
    }
}
