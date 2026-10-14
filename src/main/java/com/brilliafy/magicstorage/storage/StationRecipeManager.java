package com.brilliafy.magicstorage.storage;

import com.brilliafy.magicstorage.compat.apotheosis.ApotheosisCompat;
import com.brilliafy.magicstorage.compat.farmersdelight.FarmersDelightCompat;
import com.brilliafy.magicstorage.compat.tetra.TetraCompat;
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
 * Cartography Table, Loom, Apotheosis stations (Reforging, Salvaging, Gem Cutting),
 * and Tetra Modular Workbench.
 */
public class StationRecipeManager {

    public static ItemStack computeStationResult(TransientCraftingContainer craftSlots, Level level, StationManager stations, Player player) {
        if (stations == null) return ItemStack.EMPTY;

        List<ItemStack> nonNull = new ArrayList<>();
        int singleSlotIndex = -1;
        for (int i = 0; i < craftSlots.getContainerSize(); i++) {
            ItemStack s = craftSlots.getItem(i);
            if (!s.isEmpty()) {
                nonNull.add(s);
                singleSlotIndex = i;
            }
        }
        if (nonNull.isEmpty()) return ItemStack.EMPTY;

        // 1. Single-Item Recipes (nonNull.size() == 1)
        if (nonNull.size() == 1) {
            ItemStack input = nonNull.get(0);

            // A. Grindstone Disenchanting (highest priority for enchanted items)
            if (stations.hasGrindstone() && hasNonCurseEnchantments(input)) {
                ItemStack disenchanted = removeNonCursesFrom(input);
                if (!disenchanted.isEmpty()) return disenchanted;
            }

            // B. Apotheosis Salvaging (highest priority for affix items)
            if (stations.hasApotheosisSalvagingTable() && ApotheosisCompat.isAffixItem(input)) {
                ItemStack salvaged = ApotheosisCompat.computeSalvageResult(input, level);
                if (!salvaged.isEmpty()) return salvaged;
            }

            boolean canSmoke = stations.hasSmoker() && hasRecipe(input, RecipeType.SMOKING, level);
            boolean canBlast = stations.hasBlastFurnace() && hasRecipe(input, RecipeType.BLASTING, level);
            boolean canFurnace = stations.hasFurnace() && hasRecipe(input, RecipeType.SMELTING, level);
            boolean canSmelt = canSmoke || canBlast || canFurnace;
            boolean canCut = stations.hasStonecutter() && hasRecipe(input, RecipeType.STONECUTTING, level);

            // Unambiguous resolution:
            // Center Slot (Slot 4) explicitly targets Stonecutter
            if (singleSlotIndex == 4 && canCut) {
                return getSingleItemResult(input, RecipeType.STONECUTTING, level);
            }

            // Top-Left Slot (Slot 0) explicitly targets Smelting/Cooking
            if (singleSlotIndex == 0 && canSmelt) {
                if (canSmoke) return getSingleItemResult(input, RecipeType.SMOKING, level);
                if (canBlast) return getSingleItemResult(input, RecipeType.BLASTING, level);
                return getSingleItemResult(input, RecipeType.SMELTING, level);
            }

            // If the item can ONLY be smelted (e.g. ores, meat, sand, clay)
            if (canSmelt && !canCut) {
                if (canSmoke) return getSingleItemResult(input, RecipeType.SMOKING, level);
                if (canBlast) return getSingleItemResult(input, RecipeType.BLASTING, level);
                return getSingleItemResult(input, RecipeType.SMELTING, level);
            }

            // If the item can ONLY be cut (e.g. andesite, granite, diorite, prismarine)
            if (canCut && !canSmelt) {
                return getSingleItemResult(input, RecipeType.STONECUTTING, level);
            }

            // Overlapping item placed in other slots: Smelting takes precedence
            if (canSmelt) {
                if (canSmoke) return getSingleItemResult(input, RecipeType.SMOKING, level);
                if (canBlast) return getSingleItemResult(input, RecipeType.BLASTING, level);
                return getSingleItemResult(input, RecipeType.SMELTING, level);
            }
            if (canCut) {
                return getSingleItemResult(input, RecipeType.STONECUTTING, level);
            }
        }

        // 2. Two-Item Recipes (nonNull.size() == 2)
        if (nonNull.size() == 2) {
            ItemStack s0 = nonNull.get(0);
            ItemStack s1 = nonNull.get(1);

            // A. Grindstone tool repair (durability combine + 5% bonus, curses preserved)
            if (stations.hasGrindstone() && s0.is(s1.getItem()) && s0.isDamageableItem()) {
                ItemStack repaired = mergeGrindstoneItems(s0, s1);
                if (!repaired.isEmpty()) return repaired;
            }

            // B. Stonecutter exact variant selection (input block + target sample)
            if (stations.hasStonecutter()) {
                ItemStack variant = getStonecutterMatchingVariant(s0, s1, level);
                if (variant.isEmpty()) {
                    variant = getStonecutterMatchingVariant(s1, s0, level);
                }
                if (!variant.isEmpty()) return variant;
            }

            // C. Cartography Table
            if (stations.hasCartographyTable()) {
                if (s0.is(Items.FILLED_MAP)) {
                    ItemStack carto = getCartographyResult(s0, s1, level);
                    if (!carto.isEmpty()) return carto;
                } else if (s1.is(Items.FILLED_MAP)) {
                    ItemStack carto = getCartographyResult(s1, s0, level);
                    if (!carto.isEmpty()) return carto;
                }
            }

            // D. Tetra Modular Workbench
            if (stations.hasTetraModularWorkbench()) {
                ItemStack wb = TetraCompat.computeWorkbenchResult(s0, s1);
                if (wb.isEmpty()) {
                    wb = TetraCompat.computeWorkbenchResult(s1, s0);
                }
                if (!wb.isEmpty()) return wb;
            }

            // E. Loom (Banner + Dye)
            if (stations.hasLoom()) {
                if (s0.getItem() instanceof BannerItem && isDyeItem(s1)) {
                    ItemStack loom = getLoomResult(s0, s1, null, level);
                    if (!loom.isEmpty()) return loom;
                } else if (s1.getItem() instanceof BannerItem && isDyeItem(s0)) {
                    ItemStack loom = getLoomResult(s1, s0, null, level);
                    if (!loom.isEmpty()) return loom;
                }
            }
            // F. Farmer's Delight Cutting Board (Input Item + Tool)
            if (stations.hasFarmersDelightCuttingBoard()) {
                ItemStack cut = FarmersDelightCompat.computeCuttingResult(s0, s1, level);
                if (!cut.isEmpty()) return cut;
            }
        }

        // 3. Three-Item Recipes (nonNull.size() == 3)
        if (nonNull.size() == 3) {
            ItemStack s0 = nonNull.get(0);
            ItemStack s1 = nonNull.get(1);
            ItemStack s2 = nonNull.get(2);

            // A. Smithing Table
            if (stations.hasSmithingTable()) {
                ItemStack smithing = getSmithingPermutations(s0, s1, s2, level);
                if (!smithing.isEmpty()) return smithing;
            }

            // B. Loom (Banner + Dye + Pattern)
            if (stations.hasLoom()) {
                ItemStack banner = null;
                ItemStack dye = null;
                ItemStack pattern = null;
                for (ItemStack s : nonNull) {
                    if (s.getItem() instanceof BannerItem && banner == null) banner = s;
                    else if (isDyeItem(s) && dye == null) dye = s;
                    else if (isReusablePattern(s) && pattern == null) pattern = s;
                }
                if (banner != null && dye != null && pattern != null) {
                    ItemStack loom = getLoomResult(banner, dye, pattern, level);
                    if (!loom.isEmpty()) return loom;
                }
            }

            // C. Apotheosis Reforging Table (Gear + Rarity Material + Sigil/Dust)
            if (stations.hasApotheosisReforgingTable()) {
                ItemStack gear = null;
                ItemStack mat = null;
                ItemStack sigilOrDust = null;
                for (ItemStack s : nonNull) {
                    if (ApotheosisCompat.isRarityMaterial(s) && mat == null) {
                        mat = s;
                    } else if (ApotheosisCompat.isSigilOrDust(s) && sigilOrDust == null) {
                        sigilOrDust = s;
                    } else if (gear == null && s.isDamageableItem()) {
                        gear = s;
                    }
                }
                if (gear != null && mat != null && sigilOrDust != null) {
                    ItemStack reforged = ApotheosisCompat.computeReforgingResult(gear, mat, sigilOrDust, level, player);
                    if (!reforged.isEmpty()) return reforged;
                }
            }

            // D. Apotheosis Gem Cutting Table (Gem + Dust + Duplicate/Material)
            if (stations.hasApotheosisGemCuttingTable()) {
                ItemStack gem = null;
                ItemStack dust = null;
                ItemStack third = null;
                for (ItemStack s : nonNull) {
                    if (ApotheosisCompat.isGemDust(s) && dust == null) {
                        dust = s;
                    } else if (ApotheosisCompat.isGem(s) && gem == null) {
                        gem = s;
                    } else if (third == null) {
                        third = s;
                    }
                }
                if (gem != null && dust != null) {
                    ItemStack cut = ApotheosisCompat.computeGemCuttingResult(gem, dust, third, level);
                    if (!cut.isEmpty()) return cut;
                }
            }
        }

        // 4. Farmer's Delight Cooking Pot (1-6 ingredients + optional container)
        if (stations.hasFarmersDelightCookingPot() && nonNull.size() >= 1 && nonNull.size() <= 7) {
            ItemStack cooked = FarmersDelightCompat.computeCookingPotResult(nonNull, level);
            if (!cooked.isEmpty()) return cooked;
        }

        return ItemStack.EMPTY;
    }

    public static boolean isReusableIngredient(TransientCraftingContainer craftSlots, int slot, Level level) {
        if (slot < 0 || slot >= craftSlots.getContainerSize()) return false;
        ItemStack item = craftSlots.getItem(slot);
        if (item.isEmpty()) return false;
        if (isReusablePattern(item)) return true;

        List<Integer> filledSlots = new ArrayList<>();
        for (int i = 0; i < craftSlots.getContainerSize(); i++) {
            if (!craftSlots.getItem(i).isEmpty()) filledSlots.add(i);
        }
        if (filledSlots.size() == 2 && filledSlots.contains(slot)) {
            int otherSlot = filledSlots.get(0) == slot ? filledSlots.get(1) : filledSlots.get(0);
            ItemStack otherItem = craftSlots.getItem(otherSlot);
            if (!otherItem.isEmpty() && level != null) {
                if (!getStonecutterMatchingVariant(otherItem, item, level).isEmpty()) {
                    return true;
                }
            }
        }
        if (FarmersDelightCompat.isCuttingToolSlot(craftSlots, slot, level)) {
            return true;
        }
        return false;
    }

    private static boolean hasRecipe(ItemStack stack, RecipeType<?> type, Level level) {
        return !getSingleItemResult(stack, type, level).isEmpty();
    }

    private static ItemStack getSmithingPermutations(ItemStack s0, ItemStack s1, ItemStack s2, Level level) {
        ItemStack res = getSmithingResult(s0, s1, s2, level);
        if (!res.isEmpty()) return res;
        res = getSmithingResult(s0, s2, s1, level);
        if (!res.isEmpty()) return res;
        res = getSmithingResult(s1, s0, s2, level);
        if (!res.isEmpty()) return res;
        res = getSmithingResult(s1, s2, s0, level);
        if (!res.isEmpty()) return res;
        res = getSmithingResult(s2, s0, s1, level);
        if (!res.isEmpty()) return res;
        return getSmithingResult(s2, s1, s0, level);
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

    public static List<ItemStack> getStonecutterVariants(ItemStack input, Level level) {
        List<ItemStack> list = new ArrayList<>();
        if (input.isEmpty() || level == null) return list;
        SingleRecipeInput sri = new SingleRecipeInput(input);
        for (RecipeHolder<?> holder : level.getServer().getRecipeManager().getRecipes()) {
            if (holder.value() instanceof StonecutterRecipe sr && sr.matches(sri, level)) {
                list.add(sr.assemble(sri));
            }
        }
        return list;
    }

    private static ItemStack getStonecutterMatchingVariant(ItemStack input, ItemStack sample, Level level) {
        SingleRecipeInput sri = new SingleRecipeInput(input);
        for (RecipeHolder<?> holder : level.getServer().getRecipeManager().getRecipes()) {
            if (holder.value() instanceof StonecutterRecipe sr && sr.matches(sri, level)) {
                ItemStack res = sr.assemble(sri);
                if (res.is(sample.getItem())) {
                    return res;
                }
            }
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

    public static List<ItemStack> getStonecutterVariants(ItemStack input, Level level) {
        List<ItemStack> list = new ArrayList<>();
        if (input.isEmpty() || level == null) return list;
        SingleRecipeInput sri = new SingleRecipeInput(input);
        for (RecipeHolder<StonecutterRecipe> holder : level.getRecipeManager().getAllRecipesFor(RecipeType.STONECUTTING)) {
            if (holder.value().matches(sri, level)) {
                list.add(holder.value().assemble(sri, level.registryAccess()));
            }
        }
        return list;
    }

    private static ItemStack getStonecutterMatchingVariant(ItemStack input, ItemStack sample, Level level) {
        SingleRecipeInput sri = new SingleRecipeInput(input);
        var recipes = level.getRecipeManager().getAllRecipesFor(RecipeType.STONECUTTING);
        for (RecipeHolder<StonecutterRecipe> holder : recipes) {
            if (holder.value().matches(sri, level)) {
                ItemStack res = holder.value().assemble(sri, level.registryAccess());
                if (res.is(sample.getItem())) {
                    return res;
                }
            }
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

    public static List<ItemStack> getStonecutterVariants(ItemStack input, Level level) {
        List<ItemStack> list = new ArrayList<>();
        if (input.isEmpty() || level == null) return list;
        Container container = new SimpleContainer(input);
        for (StonecutterRecipe recipe : level.getRecipeManager().getAllRecipesFor(RecipeType.STONECUTTING)) {
            if (recipe.matches(container, level)) {
                list.add(recipe.assemble(container, level.registryAccess()));
            }
        }
        return list;
    }

    private static ItemStack getStonecutterMatchingVariant(ItemStack input, ItemStack sample, Level level) {
        Container container = new SimpleContainer(input);
        var recipes = level.getRecipeManager().getAllRecipesFor(RecipeType.STONECUTTING);
        for (StonecutterRecipe recipe : recipes) {
            if (recipe.matches(container, level)) {
                ItemStack res = recipe.assemble(container, level.registryAccess());
                if (res.is(sample.getItem())) {
                    return res;
                }
            }
        }
        return ItemStack.EMPTY;
    }
    *///?}

    public static class SmeltResult {
        private final ItemStack result;
        private final int cookTime;
        private final int speed;

        public SmeltResult(ItemStack result, int cookTime, int speed) {
            this.result = result;
            this.cookTime = cookTime;
            this.speed = speed;
        }

        public ItemStack getResult() { return result; }
        public int getCookTime() { return cookTime; }
        public int getSpeed() { return speed; }
    }

    public static SmeltResult getSmeltResult(ItemStack input, Level level, boolean hasBlast, boolean hasSmoker, boolean hasFurnace) {
        if (input.isEmpty() || level == null) return null;
        if (hasSmoker && hasRecipe(input, RecipeType.SMOKING, level)) {
            ItemStack res = getSingleItemResult(input, RecipeType.SMOKING, level);
            if (!res.isEmpty()) return new SmeltResult(res, 100, 1);
        }
        if (hasBlast && hasRecipe(input, RecipeType.BLASTING, level)) {
            ItemStack res = getSingleItemResult(input, RecipeType.BLASTING, level);
            if (!res.isEmpty()) return new SmeltResult(res, 100, 1);
        }
        if (hasFurnace && hasRecipe(input, RecipeType.SMELTING, level)) {
            ItemStack res = getSingleItemResult(input, RecipeType.SMELTING, level);
            if (!res.isEmpty()) return new SmeltResult(res, 200, 1);
        }
        return null;
    }

    public static int getFuelBurnTime(ItemStack stack, Level level) {
        if (stack.isEmpty()) return 0;
        if (stack.is(Items.LAVA_BUCKET)) return 20000;
        if (stack.is(Items.COAL_BLOCK)) return 16000;
        if (stack.is(Items.BLAZE_ROD)) return 2400;
        if (stack.is(Items.COAL) || stack.is(Items.CHARCOAL)) return 1600;
        try {
            java.lang.reflect.Method m = net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity.class.getDeclaredMethod("getFuel");
            m.setAccessible(true);
            @SuppressWarnings("unchecked")
            java.util.Map<Item, Integer> fuels = (java.util.Map<Item, Integer>) m.invoke(null);
            if (fuels != null && fuels.containsKey(stack.getItem())) {
                return fuels.get(stack.getItem());
            }
        } catch (Throwable ignored) {}
        if (stack.is(net.minecraft.tags.ItemTags.LOGS) || stack.is(net.minecraft.tags.ItemTags.PLANKS)) return 300;
        if (stack.is(Items.STICK)) return 100;
        return 0;
    }

    public static ItemStack computeStationResultForMode(StationMode mode, TransientCraftingContainer craftSlots, Level level, StationManager stations, Player player, int selectedVariantIndex) {
        if (mode == null || mode == StationMode.CRAFTING) {
            return computeStationResult(craftSlots, level, stations, player);
        }
        if (mode == StationMode.STONECUTTER) {
            ItemStack in = craftSlots.getItem(0);
            if (in.isEmpty()) return ItemStack.EMPTY;
            List<ItemStack> variants = getStonecutterVariants(in, level);
            if (variants.isEmpty()) return ItemStack.EMPTY;
            if (selectedVariantIndex >= 0 && selectedVariantIndex < variants.size()) {
                return variants.get(selectedVariantIndex).copy();
            }
            return variants.get(0).copy();
        }
        if (mode == StationMode.SMITHING) {
            return getSmithingResult(craftSlots.getItem(0), craftSlots.getItem(1), craftSlots.getItem(2), level);
        }
        if (mode == StationMode.GRINDSTONE) {
            ItemStack s0 = craftSlots.getItem(0);
            ItemStack s1 = craftSlots.getItem(1);
            if (!s0.isEmpty() && s1.isEmpty() && hasNonCurseEnchantments(s0)) {
                return removeNonCursesFrom(s0);
            }
            if (s0.isEmpty() && !s1.isEmpty() && hasNonCurseEnchantments(s1)) {
                return removeNonCursesFrom(s1);
            }
            if (!s0.isEmpty() && !s1.isEmpty() && s0.is(s1.getItem()) && s0.isDamageableItem()) {
                int maxDmg = s0.getMaxDamage();
                int rem0 = maxDmg - s0.getDamageValue();
                int rem1 = maxDmg - s1.getDamageValue();
                int bonus = maxDmg * 5 / 100;
                int combinedRem = Math.min(maxDmg, rem0 + rem1 + bonus);
                ItemStack res = s0.copy();
                res.setDamageValue(Math.max(0, maxDmg - combinedRem));
                return res;
            }
            return ItemStack.EMPTY;
        }
        if (mode == StationMode.ANVIL) {
            ItemStack s0 = craftSlots.getItem(0);
            ItemStack s1 = craftSlots.getItem(1);
            if (!s0.isEmpty() && !s1.isEmpty() && s0.is(s1.getItem()) && s0.isDamageableItem()) {
                int maxDmg = s0.getMaxDamage();
                int rem0 = maxDmg - s0.getDamageValue();
                int rem1 = maxDmg - s1.getDamageValue();
                int bonus = maxDmg * 12 / 100;
                int combinedRem = Math.min(maxDmg, rem0 + rem1 + bonus);
                ItemStack res = s0.copy();
                res.setDamageValue(Math.max(0, maxDmg - combinedRem));
                return res;
            }
            return ItemStack.EMPTY;
        }
        if (mode == StationMode.LOOM) {
            return getLoomResult(craftSlots.getItem(0), craftSlots.getItem(1), craftSlots.getItem(2), level);
        }
        if (mode == StationMode.CARTOGRAPHY) {
            return getCartographyResult(craftSlots.getItem(0), craftSlots.getItem(1), level);
        }
        if (mode == StationMode.FD_COOKING) {
            List<ItemStack> nonNull = new ArrayList<>();
            for (int i = 0; i < craftSlots.getContainerSize(); i++) {
                ItemStack s = craftSlots.getItem(i);
                if (!s.isEmpty()) nonNull.add(s);
            }
            return FarmersDelightCompat.computeCookingPotResult(nonNull, level);
        }
        if (mode == StationMode.FD_CUTTING) {
            return FarmersDelightCompat.computeCuttingResult(craftSlots.getItem(0), craftSlots.getItem(1), level);
        }
        if (mode == StationMode.APOTH_REFORGING) {
            return ApotheosisCompat.computeReforgingResult(
                    craftSlots.getItem(0), craftSlots.getItem(1), craftSlots.getItem(2), level, player
            );
        }
        if (mode == StationMode.APOTH_SALVAGING) {
            return ApotheosisCompat.computeSalvageResult(craftSlots.getItem(0), level);
        }
        if (mode == StationMode.APOTH_GEM_CUTTING) {
            return ApotheosisCompat.computeGemCuttingResult(
                    craftSlots.getItem(0), craftSlots.getItem(1), craftSlots.getItem(2), level
            );
        }
        if (mode == StationMode.TETRA_WORKBENCH) {
            return TetraCompat.computeWorkbenchResult(craftSlots.getItem(0), craftSlots.getItem(1));
        }
        return computeStationResult(craftSlots, level, stations, player);
    }
}

