package com.brilliafy.magicstorage.menu;

import com.brilliafy.magicstorage.blockentity.StorageUnitBlockEntity;
import com.brilliafy.magicstorage.init.ModMenus;
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

        // Player main inventory (3 rows of 9)
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }

        // Player hotbar (1 row of 9)
        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 142));
        }
    }

    public StorageUnitBlockEntity getUnit() {
        return unit;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return unit == null || unit.stillValid(player);
    }
}
