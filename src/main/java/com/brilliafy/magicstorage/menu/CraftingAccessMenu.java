package com.brilliafy.magicstorage.menu;

import com.brilliafy.magicstorage.blockentity.StorageHeartBlockEntity;
import com.brilliafy.magicstorage.init.ModMenus;
import com.brilliafy.magicstorage.util.ItemHelper;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import java.util.Optional;

//? > 1.20.1 {
import net.minecraft.world.item.crafting.CraftingInput;
//?}

public class CraftingAccessMenu extends StorageAccessMenu {
    protected final TransientCraftingContainer craftSlots = new TransientCraftingContainer(this, 3, 3);
    protected final ResultContainer resultSlots = new ResultContainer();
    protected final Level level;

    public CraftingAccessMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, null);
    }

    public CraftingAccessMenu(int containerId, Inventory playerInventory, StorageHeartBlockEntity heart) {
        super(ModMenus.CRAFTING_ACCESS, containerId, playerInventory, heart, 3, 8, 80);
        this.level = playerInventory.player.level();

        // Crafting result slot at index 0 of special slots
        this.addSlot(new ResultSlot(playerInventory.player, craftSlots, resultSlots, 0, 124, 35) {
            @Override
            public void onTake(Player player, ItemStack stack) {
                // If not shift-crafting, take 1 craft standard logic
                super.onTake(player, stack);
                slotsChanged(craftSlots);
            }
        });

        // 3x3 Crafting grid
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                this.addSlot(new Slot(craftSlots, col + row * 3, 30 + col * 18, 17 + row * 18));
            }
        }
    }

    public TransientCraftingContainer getCraftSlots() {
        return craftSlots;
    }

    public ResultContainer getResultSlots() {
        return resultSlots;
    }

    /** Slot index of the crafting result. */
    public int getResultSlotIndex() {
        return getPlayerSlotStart() + 36;
    }

    //? >= 26.1 {
    /*@Override
    public void clicked(int slotId, int button, net.minecraft.world.inventory.ContainerInput type, Player player) {
        if (slotId == getResultSlotIndex() && type == net.minecraft.world.inventory.ContainerInput.QUICK_MOVE) {
            if (!level.isClientSide()) {
                instantCraftAll(player);
            }
            return;
        }
        super.clicked(slotId, button, type, player);
    }
    *///?} < 26.1 {
    @Override
    public void clicked(int slotId, int button, ClickType type, Player player) {
        if (slotId == getResultSlotIndex() && type == ClickType.QUICK_MOVE) {
            if (!level.isClientSide()) {
                instantCraftAll(player);
            }
            return;
        }
        super.clicked(slotId, button, type, player);
    }
    //?}

    @Override
    public void slotsChanged(Container container) {
        if (!level.isClientSide()) {
            updateCraftingResult();
        }
    }

    protected void updateCraftingResult() {
        //? >= 26.1 {
        /*CraftingInput input = craftSlots.asCraftInput();
        Optional<RecipeHolder<CraftingRecipe>> match = level.getServer().getRecipeManager().getRecipeFor(RecipeType.CRAFTING, input, level);
        if (match.isPresent()) {
            ItemStack result = match.get().value().assemble(input);
            resultSlots.setItem(0, result);
        } else {
            resultSlots.setItem(0, ItemStack.EMPTY);
        }
        *///?} < 26.1 && > 1.20.1 {
        CraftingInput input = craftSlots.asCraftInput();
        Optional<RecipeHolder<CraftingRecipe>> match = level.getServer().getRecipeManager().getRecipeFor(RecipeType.CRAFTING, input, level);
        if (match.isPresent()) {
            ItemStack result = match.get().value().assemble(input, level.registryAccess());
            resultSlots.setItem(0, result);
        } else {
            resultSlots.setItem(0, ItemStack.EMPTY);
        }
        //?} <= 1.20.1 {
        /*Optional<CraftingRecipe> match = level.getServer().getRecipeManager().getRecipeFor(RecipeType.CRAFTING, craftSlots, level);
        if (match.isPresent()) {
            ItemStack result = match.get().assemble(craftSlots, level.registryAccess());
            resultSlots.setItem(0, result);
        } else {
            resultSlots.setItem(0, ItemStack.EMPTY);
        }
        *///?}
    }

    /**
     * Instant craft-all when player shift-clicks the crafting result slot.
     */
    public void instantCraftAll(Player player) {
        if (level.isClientSide() || resultSlots.getItem(0).isEmpty()) return;

        // Calculate how many crafts are possible
        int maxCrafts = Integer.MAX_VALUE;

        // Check grid items
        for (int i = 0; i < 9; i++) {
            ItemStack inGrid = craftSlots.getItem(i);
            if (!inGrid.isEmpty()) {
                int countAvailable = inGrid.getCount();
                if (heart != null) {
                    countAvailable += com.brilliafy.magicstorage.storage.StorageNetworkManager.countItem(heart.getValidUnits(), inGrid);
                }
                maxCrafts = Math.min(maxCrafts, countAvailable);
            }
        }

        if (maxCrafts <= 0 || maxCrafts == Integer.MAX_VALUE) return;

        // Craft items in batch
        ItemStack singleResult = resultSlots.getItem(0).copy();
        int craftedCount = 0;

        for (int c = 0; c < maxCrafts; c++) {
            boolean canCraftOne = true;
            for (int i = 0; i < 9; i++) {
                ItemStack req = craftSlots.getItem(i);
                if (!req.isEmpty()) {
                    if (req.getCount() > 1) {
                        req.shrink(1);
                    } else if (heart != null) {
                        ItemStack extracted = heart.extractItem(req, 1);
                        if (extracted.isEmpty()) {
                            canCraftOne = false;
                            break;
                        }
                    } else {
                        canCraftOne = false;
                        break;
                    }
                }
            }

            if (!canCraftOne) break;
            craftedCount += singleResult.getCount();
        }

        if (craftedCount > 0) {
            ItemStack totalCrafted = singleResult.copy();
            totalCrafted.setCount(craftedCount);

            if (!player.getInventory().add(totalCrafted)) {
                if (heart != null) {
                    heart.insertItem(totalCrafted);
                } else {
                    //? >= 26.1 {
                    /*if (player.level() instanceof net.minecraft.server.level.ServerLevel serverLevel) {
                        player.spawnAtLocation(serverLevel, totalCrafted);
                    }
                    *///?} < 26.1 {
                    player.drop(totalCrafted, false);
                    //?}
                }
            }
        }

        updateCraftingResult();
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.clearContainer(player, this.craftSlots);
    }
}
