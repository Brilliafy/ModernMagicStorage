package com.brilliafy.magicstorage.blockentity;

import com.brilliafy.magicstorage.block.BlockStorageUnit;
import com.brilliafy.magicstorage.init.ModBlockEntities;
import com.brilliafy.magicstorage.menu.StorageUnitMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

//? > 1.20.1 {
import net.minecraft.core.HolderLookup;
//?}

public class StorageUnitBlockEntity extends BlockEntity implements Container, MenuProvider {
    private int capacity;
    private NonNullList<ItemStack> items;

    public StorageUnitBlockEntity(BlockPos pos, BlockState state) {
        this(pos, state, state.getBlock() instanceof BlockStorageUnit u ? u.getCapacity() : 36);
    }

    public StorageUnitBlockEntity(BlockPos pos, BlockState state, int capacity) {
        super(ModBlockEntities.STORAGE_UNIT, pos, state);
        this.capacity = capacity > 0 ? capacity : 36;
        this.items = NonNullList.withSize(this.capacity, ItemStack.EMPTY);
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int newCapacity) {
        if (newCapacity == this.capacity) return;
        NonNullList<ItemStack> newItems = NonNullList.withSize(newCapacity, ItemStack.EMPTY);
        for (int i = 0; i < Math.min(items.size(), newCapacity); i++) {
            newItems.set(i, items.get(i));
        }
        this.items = newItems;
        this.capacity = newCapacity;
        setChanged();
    }

    public void copyContentsFrom(StorageUnitBlockEntity oldUnit, int newCapacity) {
        this.capacity = newCapacity;
        this.items = NonNullList.withSize(newCapacity, ItemStack.EMPTY);
        if (oldUnit != null) {
            NonNullList<ItemStack> oldItems = oldUnit.getStoredItems();
            for (int i = 0; i < Math.min(oldItems.size(), newCapacity); i++) {
                this.items.set(i, oldItems.get(i).copy());
            }
        }
        setChanged();
    }

    public NonNullList<ItemStack> getStoredItems() {
        return items;
    }

    public int getStoredCount() {
        int count = 0;
        for (ItemStack s : items) {
            if (!s.isEmpty()) count++;
        }
        return count;
    }

    public ItemStack insertStack(ItemStack toInsert) {
        if (toInsert.isEmpty()) return ItemStack.EMPTY;
        // 1. Try to merge with existing stacks
        for (int i = 0; i < items.size(); i++) {
            ItemStack current = items.get(i);
            if (!current.isEmpty() && com.brilliafy.magicstorage.util.ItemHelper.canMerge(current, toInsert)) {
                int maxStack = Math.min(current.getMaxStackSize(), getMaxStackSize());
                int available = maxStack - current.getCount();
                if (available > 0) {
                    int add = Math.min(available, toInsert.getCount());
                    current.grow(add);
                    toInsert.shrink(add);
                    setChanged();
                    if (toInsert.isEmpty()) return ItemStack.EMPTY;
                }
            }
        }
        // 2. Put into empty slot
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).isEmpty()) {
                items.set(i, toInsert.copy());
                toInsert.setCount(0);
                setChanged();
                return ItemStack.EMPTY;
            }
        }
        return toInsert;
    }

    public ItemStack extractStack(ItemStack template, int maxCount) {
        if (template.isEmpty() || maxCount <= 0) return ItemStack.EMPTY;
        ItemStack result = ItemStack.EMPTY;
        for (int i = 0; i < items.size(); i++) {
            ItemStack current = items.get(i);
            if (!current.isEmpty() && com.brilliafy.magicstorage.util.ItemHelper.canMerge(current, template)) {
                int extractAmount = Math.min(current.getCount(), maxCount - result.getCount());
                if (result.isEmpty()) {
                    result = current.split(extractAmount);
                } else {
                    current.shrink(extractAmount);
                    result.grow(extractAmount);
                }
                if (current.isEmpty()) {
                    items.set(i, ItemStack.EMPTY);
                }
                setChanged();
                if (result.getCount() >= maxCount) break;
            }
        }
        return result;
    }

    //? >= 26.1 {
    /*@Override
    protected void saveAdditional(net.minecraft.world.level.storage.ValueOutput output) {
        super.saveAdditional(output);
        output.putInt("Capacity", capacity);
        ContainerHelper.saveAllItems(output, items);
    }

    @Override
    protected void loadAdditional(net.minecraft.world.level.storage.ValueInput input) {
        super.loadAdditional(input);
        int cap = input.getIntOr("Capacity", 0);
        if (cap > 0) this.capacity = cap;
        this.items = NonNullList.withSize(this.capacity, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(input, items);
    }
    *///?} < 26.1 && > 1.20.1 {
    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("Capacity", capacity);
        ContainerHelper.saveAllItems(tag, items, registries);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        int cap = tag.getInt("Capacity");
        if (cap > 0) this.capacity = cap;
        this.items = NonNullList.withSize(this.capacity, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, items, registries);
    }
    //?} <= 1.20.1 {
    /*@Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("Capacity", capacity);
        ContainerHelper.saveAllItems(tag, items);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        int cap = tag.getInt("Capacity");
        if (cap > 0) this.capacity = cap;
        this.items = NonNullList.withSize(this.capacity, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, items);
    }
    *///?}

    @Override
    public int getContainerSize() {
        return items.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack s : items) {
            if (!s.isEmpty()) return false;
        }
        return true;
    }

    @Override
    public ItemStack getItem(int slot) {
        return slot >= 0 && slot < items.size() ? items.get(slot) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        ItemStack s = ContainerHelper.removeItem(items, slot, amount);
        if (!s.isEmpty()) setChanged();
        return s;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return ContainerHelper.takeItem(items, slot);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        if (slot >= 0 && slot < items.size()) {
            items.set(slot, stack);
            setChanged();
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    @Override
    public void clearContent() {
        items.clear();
        setChanged();
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.magicstorage.storage_unit");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new StorageUnitMenu(containerId, playerInventory, this);
    }
}
