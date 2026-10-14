package com.brilliafy.magicstorage.storage;

import com.brilliafy.magicstorage.blockentity.StorageUnitBlockEntity;
import com.brilliafy.magicstorage.util.ItemHelper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

//? > 1.20.1 {
import net.minecraft.core.component.DataComponentMap;
//?} <= 1.20.1 {
/*import net.minecraft.nbt.CompoundTag;
*///?}

import java.util.*;

public class StorageNetworkManager {

    //? > 1.20.1 {
    private record ItemKey(net.minecraft.world.item.Item item, DataComponentMap components) {
        public static ItemKey of(ItemStack stack) {
            return new ItemKey(stack.getItem(), stack.getComponents());
        }
    }
    //?} <= 1.20.1 {
    /*private record ItemKey(net.minecraft.world.item.Item item, CompoundTag tag) {
        public static ItemKey of(ItemStack stack) {
            return new ItemKey(stack.getItem(), stack.getTag());
        }
    }
    *///?}

    public enum SortOrder {
        QUANTITY,
        NAME,
        MOD
    }

    public static List<ItemStack> aggregateItems(List<StorageUnitBlockEntity> units) {
        Map<ItemKey, ItemStack> aggregated = new LinkedHashMap<>();

        for (StorageUnitBlockEntity unit : units) {
            for (ItemStack stack : unit.getStoredItems()) {
                if (stack.isEmpty()) continue;
                ItemKey key = ItemKey.of(stack);
                if (aggregated.containsKey(key)) {
                    aggregated.get(key).grow(stack.getCount());
                } else {
                    aggregated.put(key, stack.copy());
                }
            }
        }

        return new ArrayList<>(aggregated.values());
    }

    public static void sortItems(List<ItemStack> items, SortOrder order, boolean ascending) {
        Comparator<ItemStack> comparator = switch (order) {
            case QUANTITY -> Comparator.comparingInt(ItemStack::getCount).thenComparing(s -> s.getHoverName().getString());
            case NAME -> Comparator.comparing((ItemStack s) -> s.getHoverName().getString());
            case MOD -> Comparator.<ItemStack, String>comparing(s -> BuiltInRegistries.ITEM.getKey(s.getItem()).getNamespace())
                    .thenComparing(s -> s.getHoverName().getString());
        };

        if (!ascending) {
            comparator = comparator.reversed();
        }

        items.sort(comparator);
    }

    public static List<ItemStack> filterItems(List<ItemStack> items, String query) {
        if (query == null || query.trim().isEmpty()) {
            return items;
        }

        String filter = query.toLowerCase().trim();
        List<ItemStack> result = new ArrayList<>();

        for (ItemStack stack : items) {
            if (filter.startsWith("@")) {
                String mod = BuiltInRegistries.ITEM.getKey(stack.getItem()).getNamespace().toLowerCase();
                if (mod.contains(filter.substring(1))) {
                    result.add(stack);
                }
            } else if (filter.startsWith("#")) {
                String tag = filter.substring(1);
                //? >= 26.1 {
                /*boolean matches = stack.typeHolder().tags().anyMatch(t -> t.location ().toString().toLowerCase().contains(tag));
                *///?} < 26.1 {
                boolean matches = stack.getTags().anyMatch(t -> t.location().toString().toLowerCase().contains(tag));
                //?}
                if (matches) {
                    result.add(stack);
                }
            } else {
                String name = stack.getHoverName().getString().toLowerCase();
                if (name.contains(filter)) {
                    result.add(stack);
                }
            }
        }

        return result;
    }

    public static ItemStack insertItem(List<StorageUnitBlockEntity> units, ItemStack stack) {
        if (stack.isEmpty()) return ItemStack.EMPTY;
        ItemStack remaining = stack.copy();

        // 1. First pass: merge into existing partial stacks across all units
        for (StorageUnitBlockEntity unit : units) {
            remaining = unit.insertStackMergeOnly(remaining);
            if (remaining.isEmpty()) return ItemStack.EMPTY;
        }

        // 2. Second pass: put into empty slots across all units
        for (StorageUnitBlockEntity unit : units) {
            remaining = unit.insertStackEmptyOnly(remaining);
            if (remaining.isEmpty()) return ItemStack.EMPTY;
        }

        return remaining;
    }

    public static ItemStack extractItem(List<StorageUnitBlockEntity> units, ItemStack template, int amount) {
        if (template.isEmpty() || amount <= 0) return ItemStack.EMPTY;

        ItemStack extracted = ItemStack.EMPTY;
        for (StorageUnitBlockEntity unit : units) {
            int needed = amount - extracted.getCount();
            if (needed <= 0) break;

            ItemStack part = unit.extractStack(template, needed);
            if (!part.isEmpty()) {
                if (extracted.isEmpty()) {
                    extracted = part;
                } else {
                    extracted.grow(part.getCount());
                }
            }
        }

        return extracted;
    }

    public static int countItem(List<StorageUnitBlockEntity> units, ItemStack template) {
        int total = 0;
        for (StorageUnitBlockEntity unit : units) {
            for (ItemStack s : unit.getStoredItems()) {
                if (ItemHelper.canMerge(s, template)) {
                    total += s.getCount();
                }
            }
        }
        return total;
    }

    public static boolean hasItems(List<StorageUnitBlockEntity> units, ItemStack template, int requiredCount) {
        return countItem(units, template) >= requiredCount;
    }

    private static String getItemKey(ItemStack stack) {
        return BuiltInRegistries.ITEM.getKey(stack.getItem()).toString();
    }
}
