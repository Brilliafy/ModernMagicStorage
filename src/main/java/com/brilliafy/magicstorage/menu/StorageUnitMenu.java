package com.brilliafy.magicstorage.menu;

import com.brilliafy.magicstorage.blockentity.StorageUnitBlockEntity;
import com.brilliafy.magicstorage.init.ModMenus;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class StorageUnitMenu extends AbstractContainerMenu {
    protected final StorageUnitBlockEntity unit;
    protected final Inventory playerInventory;

    public StorageUnitMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, null);
    }

    public StorageUnitMenu(int containerId, Inventory playerInventory, StorageUnitBlockEntity unit) {
        super(ModMenus.STORAGE_UNIT, containerId);
        this.playerInventory = playerInventory;
        this.unit = unit;

        Container container = unit != null ? unit : new SimpleContainer(54);
        int maxSlots = Math.min(54, container.getContainerSize());
        for (int r = 0; r < 6; r++) {
            for (int c = 0; c < 9; c++) {
                int slotIndex = c + r * 9;
                if (slotIndex < maxSlots) {
                    this.addSlot(new Slot(container, slotIndex, 8 + c * 18, 18 + r * 18));
                }
            }
        }

        // Player main inventory (3 rows of 9)
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 140 + row * 18));
            }
        }

        // Player hotbar (1 row of 9)
        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 198));
        }
    }

    public StorageUnitBlockEntity getUnit() {
        return unit;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = index >= 0 && index < this.slots.size() ? this.slots.get(index) : null;
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            int unitSlots = this.slots.size() - 36;
            if (index < unitSlots) {
                if (!this.moveItemStackTo(itemstack1, unitSlots, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 0, unitSlots, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return itemstack;
    }

    @Override
    public boolean stillValid(Player player) {
        return unit == null || unit.stillValid(player);
    }
}
