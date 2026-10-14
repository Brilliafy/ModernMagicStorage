package com.brilliafy.magicstorage.storage;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.TransientCraftingContainer;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraft.tags.BannerPatternTags;

import java.util.*;

//? > 1.20.1 {
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.MapPostProcessing;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.crafting.SmithingRecipeInput;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
//?} <= 1.20.1 {
/*
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.entity.BlockEntityType;
*/
//?}

/**
 * Handles crafting recipes for installed storage heart stations:
 * Grindstone, Smithing Table, Stonecutter, Smoker, Blast Furnace, Furnace,
 * Cartography Table, and Loom.
 */
public class StationRecipeManager {

    public static ItemStack computeStationResult(TransientCraftingContainer craftSlots, Level level, StationManager stations, Player player) {
        if (stations == null) return ItemStack.EMPTY;

        List<ItemStack> nonNull = new ArrayList<>();
        for (int i = 0; i < craftSlots.getContainerSize(); i++) {
            ItemStack s = craftSlots.getItem(i);
            if (!s.isEmpty()) nonNull.add(s);
        }
        if (nonNull.isEmpty()) return ItemStack.EMPTY;

        // 1. Grindstone (repair or disenchant)
        if (stations.hasGrindstone()) {
            if (nonNull.size() == 2) {
                ItemStack s1 = nonNull.get(0);
                ItemStack s2 = nonNull.get(1);
                if (s1.is(s2.getItem()) && s1.isDamageableItem()) {
                    ItemStack repaired = mergeGrindstoneItems(s1, s2);
                    if (!repaired.isEmpty()) return repaired;
                }
            } else if (nonNull.size() == 1) {
                ItemStack s = nonNull.get(0);
                if (hasNonCurseEnchantments(s)) {
                    ItemStack disenchanted = removeNonCursesFrom(s);
                    if (!disenchanted.isEmpty()) return disenchanted;
                }
            }
        }

        // 2. Smithing Table
        if (stations.hasSmithingTable()) {
            if (nonNull.size() == 3) {
                ItemStack s0 = craftSlots.getItem(0);
                ItemStack s1 = craftSlots.getItem(1);
                ItemStack s2 = craftSlots.getItem(2);
                if (s0.isEmpty() || s1.isEmpty() || s2.isEmpty()) {
                    s0 = nonNull.get(0);
                    s1 = nonNull.get(1);
                    s2 = nonNull.get(2);
                }
                ItemStack smithing = getSmithingResult(s0, s1, s2, level);
                if (!smithing.isEmpty()) return smithing;
            }
        }

        // 3. Single-item stations: Stonecutter, Smoker, Blast Furnace, Furnace
        if (nonNull.size() == 1) {
            ItemStack input = nonNull.get(0);

            if (stations.hasStonecutter()) {
                ItemStack cut = getSingleItemResult(input, RecipeType.STONECUTTING, level);
                if (!cut.isEmpty()) return cut;
            }

            if (stations.hasSmoker()) {
                ItemStack smoked = getSingleItemResult(input, RecipeType.SMOKING, level);
                if (!smoked.isEmpty()) return smoked;
            }

            if (stations.hasBlastFurnace()) {
                ItemStack blasted = getSingleItemResult(input, RecipeType.BLASTING, level);
                if (!blasted.isEmpty()) return blasted;
            }

            if (stations.hasFurnace()) {
                ItemStack smelted = getSingleItemResult(input, RecipeType.SMELTING, level);
                if (!smelted.isEmpty()) return smelted;
            }
        }

        // 4. Cartography Table
        if (stations.hasCartographyTable() && nonNull.size() == 2) {
            ItemStack first = nonNull.get(0);
            ItemStack second = nonNull.get(1);
            if (first.is(Items.FILLED_MAP)) {
                ItemStack carto = getCartographyResult(first, second, level);
                if (!carto.isEmpty()) return carto;
            } else if (second.is(Items.FILLED_MAP)) {
                ItemStack carto = getCartographyResult(second, first, level);
                if (!carto.isEmpty()) return carto;
            }
        }

        // 5. Loom
        if (stations.hasLoom()) {
            ItemStack banner = null;
            ItemStack dye = null;
            ItemStack pattern = null;
            for (ItemStack s : nonNull) {
                if (s.getItem() instanceof BannerItem && banner == null) {
                    banner = s;
                } else if (isDyeItem(s) && dye == null) {
                    dye = s;
                } else if (isReusablePattern(s) && pattern == null) {
                    pattern = s;
                }
            }
            if (banner != null && dye != null && nonNull.size() <= 3) {
                ItemStack loom = getLoomResult(banner, dye, pattern, level);
                if (!loom.isEmpty()) return loom;
            }
        }

        return ItemStack.EMPTY;
    }

    //? > 1.20.1 {
    private static boolean hasNonCurseEnchantments(ItemStack stack) {
        ItemEnchantments enchants = EnchantmentHelper.getEnchantmentsForCrafting(stack);
        if (enchants.isEmpty()) return false;
        for (Holder<net.minecraft.world.item.enchantment.Enchantment> holder : enchants.keySet()) {
            if (!holder.is(EnchantmentTags.CURSE)) return true;
        }
        return false;
    }

    private static ItemStack removeNonCursesFrom(ItemStack item) {
        ItemStack copy = item.copy();
        ItemEnchantments newEnchantments = EnchantmentHelper.updateEnchantments(copy, enchantments -> enchantments.removeIf(enchantment -> !enchantment.is(EnchantmentTags.CURSE)));
        if (copy.is(Items.ENCHANTED_BOOK) && newEnchantments.isEmpty()) {
            copy = copy.transmuteCopy(Items.BOOK);
        }
        copy.set(DataComponents.REPAIR_COST, 0);
        return copy;
    }

    private static ItemStack mergeGrindstoneItems(ItemStack input, ItemStack additional) {
        if (!input.is(additional.getItem()) || !input.isDamageableItem()) {
            return ItemStack.EMPTY;
        }
        int durability = Math.max(input.getMaxDamage(), additional.getMaxDamage());
        int remaining1 = input.getMaxDamage() - input.getDamageValue();
        int remaining2 = additional.getMaxDamage() - additional.getDamageValue();
        int remaining = remaining1 + remaining2 + durability * 5 / 100;
        ItemStack newItem = input.copyWithCount(1);
        newItem.setDamageValue(Math.max(durability - remaining, 0));
        EnchantmentHelper.updateEnchantments(newItem, newEnchantments -> {
            ItemEnchantments enchantments = EnchantmentHelper.getEnchantmentsForCrafting(additional);
            for (var entry : enchantments.entrySet()) {
                Holder<net.minecraft.world.item.enchantment.Enchantment> enchant = entry.getKey();
                if (enchant.is(EnchantmentTags.CURSE) && newEnchantments.getLevel(enchant) != 0) continue;
                newEnchantments.upgrade(enchant, entry.getIntValue());
            }
        });
        return removeNonCursesFrom(newItem);
    }

    private static ItemStack getCartographyResult(ItemStack map, ItemStack second, Level level) {
        MapItemSavedData data = MapItem.getSavedData(map, level);
        if (data == null) return ItemStack.EMPTY;
        if (second.is(Items.PAPER) && !data.locked && data.scale < 4) {
            ItemStack result = map.copyWithCount(1);
            result.set(DataComponents.MAP_POST_PROCESSING, MapPostProcessing.SCALE);
            return result;
        } else if (second.is(Items.GLASS_PANE) && !data.locked) {
            ItemStack result = map.copyWithCount(1);
            result.set(DataComponents.MAP_POST_PROCESSING, MapPostProcessing.LOCK);
            return result;
        } else if (second.is(Items.MAP)) {
            return map.copyWithCount(2);
        }
        return ItemStack.EMPTY;
    }
    //?}

    //? >= 26.1 {
    /*
    public static boolean isDyeItem(ItemStack stack) {
        return stack.has(DataComponents.DYE);
    }

    public static boolean isReusablePattern(ItemStack stack) {
        return stack.has(DataComponents.PROVIDES_BANNER_PATTERNS);
    }

    private static ItemStack getLoomResult(ItemStack banner, ItemStack dye, ItemStack patternItem, Level level) {
        if (!(banner.getItem() instanceof BannerItem)) {
            return ItemStack.EMPTY;
        }
        DyeColor color = dye.get(DataComponents.DYE);
        if (color == null) {
            return ItemStack.EMPTY;
        }
        ItemStack result = banner.copyWithCount(1);
        Holder<net.minecraft.world.level.block.entity.BannerPattern> patternHolder = null;
        var reg = level.registryAccess().lookupOrThrow(net.minecraft.core.registries.Registries.BANNER_PATTERN);
        if (patternItem != null && patternItem.has(DataComponents.PROVIDES_BANNER_PATTERNS)) {
            var set = patternItem.get(DataComponents.PROVIDES_BANNER_PATTERNS);
            if (set != null && set.size() > 0) {
                patternHolder = set.get(0);
            }
        }
        if (patternHolder == null) {
            var noReq = reg.get(BannerPatternTags.NO_ITEM_REQUIRED);
            if (noReq.isPresent() && noReq.get().size() > 0) {
                patternHolder = noReq.get().get(0);
            }
        }
        if (patternHolder != null) {
            final Holder<net.minecraft.world.level.block.entity.BannerPattern> finalPattern = patternHolder;
            result.update(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY,
                layers -> new BannerPatternLayers.Builder().addAll(layers).add(finalPattern, color).build());
            return result;
        }
        return ItemStack.EMPTY;
    }
    *///?} < 26.1 && > 1.20.1 {
    public static boolean isDyeItem(ItemStack stack) {
        return stack.getItem() instanceof DyeItem;
    }

    public static boolean isReusablePattern(ItemStack stack) {
        return stack.getItem() instanceof BannerPatternItem;
    }

    private static ItemStack getLoomResult(ItemStack banner, ItemStack dye, ItemStack patternItem, Level level) {
        if (!(banner.getItem() instanceof BannerItem) || !(dye.getItem() instanceof DyeItem dyeItem)) {
            return ItemStack.EMPTY;
        }
        ItemStack result = banner.copyWithCount(1);
        DyeColor color = dyeItem.getDyeColor();
        Holder<net.minecraft.world.level.block.entity.BannerPattern> patternHolder = null;
        var reg = level.registryAccess().lookupOrThrow(net.minecraft.core.registries.Registries.BANNER_PATTERN);
        if (patternItem != null && patternItem.getItem() instanceof BannerPatternItem bpi) {
            var tag = reg.get(bpi.getBannerPattern());
            if (tag.isPresent() && tag.get().size() > 0) {
                patternHolder = tag.get().get(0);
            }
        }
        if (patternHolder == null) {
            var noReq = reg.get(BannerPatternTags.NO_ITEM_REQUIRED);
            if (noReq.isPresent() && noReq.get().size() > 0) {
                patternHolder = noReq.get().get(0);
            }
        }
        if (patternHolder != null) {
            final Holder<net.minecraft.world.level.block.entity.BannerPattern> finalPattern = patternHolder;
            result.update(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY,
                layers -> new BannerPatternLayers.Builder().addAll(layers).add(finalPattern, color).build());
            return result;
        }
        return ItemStack.EMPTY;
    }
    //?} <= 1.20.1 {
    /*
    private static boolean hasNonCurseEnchantments(ItemStack stack) {
        if (!stack.isEnchanted() && !stack.is(Items.ENCHANTED_BOOK)) return false;
        Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(stack);
        for (Enchantment e : map.keySet()) {
            if (!e.isCurse()) return true;
        }
        return false;
    }

    private static ItemStack removeNonCursesFrom(ItemStack stack) {
        ItemStack itemStack = stack.copy();
        itemStack.setCount(1);
        Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(stack);
        Map<Enchantment, Integer> curses = new HashMap<>();
        for (Map.Entry<Enchantment, Integer> entry : map.entrySet()) {
            if (entry.getKey().isCurse()) {
                curses.put(entry.getKey(), entry.getValue());
            }
        }
        EnchantmentHelper.setEnchantments(curses, itemStack);
        itemStack.setRepairCost(0);
        if (itemStack.is(Items.ENCHANTED_BOOK) && curses.isEmpty()) {
            itemStack = new ItemStack(Items.BOOK);
            if (stack.hasCustomHoverName()) {
                itemStack.setHoverName(stack.getHoverName());
            }
        }
        return itemStack;
    }

    private static ItemStack mergeGrindstoneItems(ItemStack input, ItemStack additional) {
        if (!input.is(additional.getItem()) || !input.isDamageableItem()) {
            return ItemStack.EMPTY;
        }
        int maxDmg = input.getMaxDamage();
        int remaining1 = maxDmg - input.getDamageValue();
        int remaining2 = maxDmg - additional.getDamageValue();
        int remaining = remaining1 + remaining2 + maxDmg * 5 / 100;
        ItemStack newItem = input.copy();
        newItem.setCount(1);
        newItem.setDamageValue(Math.max(maxDmg - remaining, 0));
        Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(additional);
        for (Map.Entry<Enchantment, Integer> entry : map.entrySet()) {
            Enchantment enchantment = entry.getKey();
            if (enchantment.isCurse() && EnchantmentHelper.getItemEnchantmentLevel(enchantment, newItem) != 0) continue;
            newItem.enchant(enchantment, entry.getValue());
        }
        return removeNonCursesFrom(newItem);
    }

    private static ItemStack getCartographyResult(ItemStack map, ItemStack second, Level level) {
        MapItemSavedData data = MapItem.getSavedData(map, level);
        if (data == null) return ItemStack.EMPTY;
        if (second.is(Items.PAPER) && !data.locked && data.scale < 4) {
            ItemStack result = map.copyWithCount(1);
            result.getOrCreateTag().putInt("map_scale_direction", 1);
            return result;
        } else if (second.is(Items.GLASS_PANE) && !data.locked) {
            ItemStack result = map.copyWithCount(1);
            result.getOrCreateTag().putBoolean("map_to_lock", true);
            return result;
        } else if (second.is(Items.MAP)) {
            return map.copyWithCount(2);
        }
        return ItemStack.EMPTY;
    }

    public static boolean isDyeItem(ItemStack stack) {
        return stack.getItem() instanceof DyeItem;
    }

    public static boolean isReusablePattern(ItemStack stack) {
        return stack.getItem() instanceof BannerPatternItem;
    }

    private static ItemStack getLoomResult(ItemStack banner, ItemStack dye, ItemStack patternItem, Level level) {
        if (!(banner.getItem() instanceof BannerItem) || !(dye.getItem() instanceof DyeItem dyeItem)) {
            return ItemStack.EMPTY;
        }
        ItemStack result = banner.copyWithCount(1);
        DyeColor color = dyeItem.getDyeColor();
        CompoundTag tag = BlockItem.getBlockEntityData(result);
        if (tag == null) tag = new CompoundTag();
        ListTag patterns = tag.contains("Patterns", 9) ? tag.getList("Patterns", 10) : new ListTag();
        tag.put("Patterns", patterns);
        CompoundTag entry = new CompoundTag();
        String patternCode = "b";
        if (patternItem != null && patternItem.getItem() instanceof BannerPatternItem bpi) {
            var tagHolder = BuiltInRegistries.BANNER_PATTERN.getTag(bpi.getBannerPattern());
            if (tagHolder.isPresent() && tagHolder.get().size() > 0) {
                patternCode = tagHolder.get().get(0).value().getHashname();
            }
        }
        entry.putString("Pattern", patternCode);
        entry.putInt("Color", color.getId());
        patterns.add(entry);
        BlockItem.setBlockEntityData(result, BlockEntityType.BANNER, tag);
        return result;
    }
    *///?}

    //? >= 26.1 {
    /*
    private static ItemStack getSmithingResult(ItemStack s0, ItemStack s1, ItemStack s2, Level level) {
        SmithingRecipeInput input = new SmithingRecipeInput(s0, s1, s2);
        Optional<RecipeHolder<SmithingRecipe>> match = level.getServer().getRecipeManager().getRecipeFor(RecipeType.SMITHING, input, level);
        if (match.isPresent()) {
            return match.get().value().assemble(input);
        }
        return ItemStack.EMPTY;
    }

    private static ItemStack getSingleItemResult(ItemStack stack, RecipeType<?> type, Level level) {
        SingleRecipeInput input = new SingleRecipeInput(stack);
        Optional<? extends RecipeHolder<? extends Recipe<SingleRecipeInput>>> match = level.getServer().getRecipeManager().getRecipeFor((RecipeType) type, input, level);
        if (match.isPresent()) {
            return match.get().value().assemble(input);
        }
        return ItemStack.EMPTY;
    }
    *///?} < 26.1 && > 1.20.1 {
    private static ItemStack getSmithingResult(ItemStack s0, ItemStack s1, ItemStack s2, Level level) {
        SmithingRecipeInput input = new SmithingRecipeInput(s0, s1, s2);
        Optional<RecipeHolder<SmithingRecipe>> match = level.getRecipeManager().getRecipeFor(RecipeType.SMITHING, input, level);
        if (match.isPresent()) {
            return match.get().value().assemble(input, level.registryAccess());
        }
        return ItemStack.EMPTY;
    }

    private static ItemStack getSingleItemResult(ItemStack stack, RecipeType<?> type, Level level) {
        SingleRecipeInput input = new SingleRecipeInput(stack);
        Optional<? extends RecipeHolder<? extends Recipe<SingleRecipeInput>>> match = level.getRecipeManager().getRecipeFor((RecipeType) type, input, level);
        if (match.isPresent()) {
            return match.get().value().assemble(input, level.registryAccess());
        }
        return ItemStack.EMPTY;
    }
    //?} <= 1.20.1 {
    /*
    private static ItemStack getSmithingResult(ItemStack s0, ItemStack s1, ItemStack s2, Level level) {
        Container container = new SimpleContainer(s0, s1, s2);
        Optional<SmithingRecipe> match = level.getRecipeManager().getRecipeFor(RecipeType.SMITHING, container, level);
        if (match.isPresent()) {
            return match.get().assemble(container, level.registryAccess());
        }
        return ItemStack.EMPTY;
    }

    private static ItemStack getSingleItemResult(ItemStack stack, RecipeType<?> type, Level level) {
        Container container = new SimpleContainer(stack);
        Optional<? extends Recipe<Container>> match = level.getRecipeManager().getRecipeFor((RecipeType) type, container, level);
        if (match.isPresent()) {
            return match.get().assemble(container, level.registryAccess());
        }
        return ItemStack.EMPTY;
    }
    *///?}
}
