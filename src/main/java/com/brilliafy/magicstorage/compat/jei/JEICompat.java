package com.brilliafy.magicstorage.compat.jei;

import com.brilliafy.magicstorage.ModernMagicStorage;
import com.brilliafy.magicstorage.menu.CraftingAccessMenu;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Map;

public class JEICompat {
    public static final String MOD_ID = "jei";

    public static boolean isLoaded() {
        return ModernMagicStorage.xplat().isModLoaded(MOD_ID);
    }

    /**
     * Fills the crafting grid from a recipe transfer request, pulling matching items
     * from the player inventory or the connected storage network.
     */
    public static void transferRecipe(CraftingAccessMenu menu, Player player, Map<Integer, List<ItemStack>> recipeInputs, boolean maxTransfer) {
        if (menu == null || player == null || recipeInputs == null) return;

        // Clear existing items in crafting grid back to storage network or inventory
        for (int i = 0; i < 9; i++) {
            ItemStack inSlot = menu.getCraftSlots().getItem(i);
            if (!inSlot.isEmpty()) {
                if (menu.getHeart() != null) {
                    ItemStack remainder = menu.getHeart().insertItem(inSlot);
                    if (!remainder.isEmpty()) {
                        player.getInventory().add(remainder);
                    }
                } else {
                    player.getInventory().add(inSlot);
                }
                menu.getCraftSlots().setItem(i, ItemStack.EMPTY);
            }
        }

        // Fill slots with matching ingredients
        for (Map.Entry<Integer, List<ItemStack>> entry : recipeInputs.entrySet()) {
            int slot = entry.getKey();
            List<ItemStack> acceptable = entry.getValue();
            if (slot >= 0 && slot < 9 && acceptable != null && !acceptable.isEmpty()) {
                ItemStack filled = ItemStack.EMPTY;
                for (ItemStack candidate : acceptable) {
                    // Try to pull 1 from network
                    if (menu.getHeart() != null) {
                        filled = menu.getHeart().extractItem(candidate, 1);
                        if (!filled.isEmpty()) break;
                    }
                    // Try to pull 1 from player inventory
                    for (int p = 0; p < player.getInventory().getContainerSize(); p++) {
                        ItemStack pStack = player.getInventory().getItem(p);
                        if (com.brilliafy.magicstorage.util.ItemHelper.canMerge(pStack, candidate)) {
                            filled = pStack.split(1);
                            break;
                        }
                    }
                    if (!filled.isEmpty()) break;
                }

                if (!filled.isEmpty()) {
                    menu.getCraftSlots().setItem(slot, filled);
                }
            }
        }

        menu.slotsChanged(menu.getCraftSlots());
    }
}
