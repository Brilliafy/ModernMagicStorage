package com.brilliafy.magicstorage.menu;

import com.brilliafy.magicstorage.blockentity.StorageHeartBlockEntity;
import com.brilliafy.magicstorage.init.ModMenus;
import com.brilliafy.magicstorage.storage.StorageNetworkManager;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class StorageAccessMenu extends AbstractContainerMenu {
    protected final StorageHeartBlockEntity heart;
    protected final Inventory playerInventory;
    protected final Player player;
    protected List<ItemStack> clientNetworkItems = new ArrayList<>();
    protected StorageNetworkManager.SortOrder sortOrder = StorageNetworkManager.SortOrder.QUANTITY;
    protected boolean sortAscending = false;
    protected String searchQuery = "";

    public StorageAccessMenu(int containerId, Inventory playerInventory) {
        this(ModMenus.STORAGE_ACCESS, containerId, playerInventory, null);
    }

    public StorageAccessMenu(int containerId, Inventory playerInventory, StorageHeartBlockEntity heart) {
        this(ModMenus.STORAGE_ACCESS, containerId, playerInventory, heart);
    }

    protected StorageAccessMenu(MenuType<?> menuType, int containerId, Inventory playerInventory, StorageHeartBlockEntity heart) {
        super(menuType, containerId);
        this.playerInventory = playerInventory;
        this.player = playerInventory.player;
        this.heart = heart;

        addPlayerInventorySlots(playerInventory);
    }

    protected void addPlayerInventorySlots(Inventory inv) {
        // Player main inventory (3 rows of 9)
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(inv, col + row * 9 + 9, 8 + col * 18, 140 + row * 18));
            }
        }

        // Player hotbar (1 row of 9)
        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(inv, col, 8 + col * 18, 198));
        }
    }

    public StorageHeartBlockEntity getHeart() {
        return heart;
    }

    public List<ItemStack> getNetworkItems() {
        if (heart != null) {
            List<ItemStack> items = heart.getAllItems();
            items = StorageNetworkManager.filterItems(items, searchQuery);
            StorageNetworkManager.sortItems(items, sortOrder, sortAscending);
            return items;
        }
        return clientNetworkItems;
    }

    public void setClientNetworkItems(List<ItemStack> items) {
        this.clientNetworkItems = items;
    }

    public void setSearchQuery(String query) {
        this.searchQuery = query;
    }

    public void setSort(StorageNetworkManager.SortOrder order, boolean ascending) {
        this.sortOrder = order;
        this.sortAscending = ascending;
    }

    public void handleNetworkItemClick(ItemStack stack, boolean shiftClick, boolean rightClick) {
        if (heart == null || stack.isEmpty()) return;

        if (player.containerMenu.getCarried().isEmpty()) {
            // Extract from network
            int countToExtract = rightClick ? Math.min(stack.getCount(), stack.getMaxStackSize() / 2) : Math.min(stack.getCount(), stack.getMaxStackSize());
            if (countToExtract <= 0) countToExtract = 1;

            ItemStack extracted = heart.extractItem(stack, countToExtract);
            if (!extracted.isEmpty()) {
                if (shiftClick) {
                    if (!player.getInventory().add(extracted)) {
                        heart.insertItem(extracted);
                    }
                } else {
                    player.containerMenu.setCarried(extracted);
                }
            }
        } else {
            // Deposit carried item
            ItemStack carried = player.containerMenu.getCarried();
            ItemStack remainder = heart.insertItem(carried);
            player.containerMenu.setCarried(remainder);
        }
    }

    public void depositAll() {
        if (heart == null) return;
        for (int i = 0; i < playerInventory.getContainerSize(); i++) {
            ItemStack stack = playerInventory.getItem(i);
            if (!stack.isEmpty()) {
                ItemStack remainder = heart.insertItem(stack);
                playerInventory.setItem(i, remainder);
            }
        }
    }

    public void restock() {
        if (heart == null) return;
        for (int i = 0; i < playerInventory.getContainerSize(); i++) {
            ItemStack stack = playerInventory.getItem(i);
            if (!stack.isEmpty() && stack.getCount() < stack.getMaxStackSize()) {
                int needed = stack.getMaxStackSize() - stack.getCount();
                ItemStack extracted = heart.extractItem(stack, needed);
                if (!extracted.isEmpty()) {
                    stack.grow(extracted.getCount());
                }
            }
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack stack = slot.getItem();
            if (heart != null) {
                ItemStack remainder = heart.insertItem(stack);
                slot.set(remainder);
                return remainder;
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return heart == null || heart.stillValid(player);
    }
}
