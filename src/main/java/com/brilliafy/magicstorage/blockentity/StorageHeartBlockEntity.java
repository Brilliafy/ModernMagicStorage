package com.brilliafy.magicstorage.blockentity;

import com.brilliafy.magicstorage.block.BlockCraftingAccess;
import com.brilliafy.magicstorage.block.BlockRemoteAccess;
import com.brilliafy.magicstorage.block.BlockStorageAccess;
import com.brilliafy.magicstorage.block.BlockStorageUnit;
import com.brilliafy.magicstorage.init.ModBlockEntities;
import com.brilliafy.magicstorage.menu.CraftingAccessMenu;
import com.brilliafy.magicstorage.menu.StorageAccessMenu;
import com.brilliafy.magicstorage.storage.StationManager;
import com.brilliafy.magicstorage.storage.StorageNetworkManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.*;

//? > 1.20.1 {
import net.minecraft.core.HolderLookup;
//?}

public class StorageHeartBlockEntity extends BlockEntity implements MenuProvider, Container {
    private final Set<BlockPos> connectedUnits = new LinkedHashSet<>();
    private final Set<BlockPos> connectedAccesses = new LinkedHashSet<>();
    private final NonNullList<ItemStack> stationInventory = NonNullList.withSize(StationManager.STATION_SLOTS, ItemStack.EMPTY);
    private final StationManager stationManager = new StationManager();
    private boolean networkDirty = true;

    public StorageHeartBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.STORAGE_HEART, pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, StorageHeartBlockEntity be) {
        if (!level.isClientSide() && be.networkDirty) {
            be.refreshNetwork();
        }
    }

    public void markNetworkDirty() {
        this.networkDirty = true;
    }

    public void refreshNetwork() {
        if (level == null || level.isClientSide()) return;

        connectedUnits.clear();
        connectedAccesses.clear();

        Queue<BlockPos> queue = new ArrayDeque<>();
        Set<BlockPos> visited = new HashSet<>();

        queue.add(worldPosition);
        visited.add(worldPosition);

        int maxNodes = 1024;
        while (!queue.isEmpty() && visited.size() < maxNodes) {
            BlockPos current = queue.poll();

            for (Direction dir : Direction.values()) {
                BlockPos neighbor = current.relative(dir);
                if (visited.add(neighbor)) {
                    BlockState neighborState = level.getBlockState(neighbor);
                    if (neighborState.getBlock() instanceof BlockStorageUnit) {
                        connectedUnits.add(neighbor);
                        queue.add(neighbor);
                    } else if (neighborState.getBlock() instanceof BlockStorageAccess
                            || neighborState.getBlock() instanceof BlockCraftingAccess
                            || neighborState.getBlock() instanceof BlockRemoteAccess) {
                        connectedAccesses.add(neighbor);
                        queue.add(neighbor);
                    }
                }
            }
        }

        stationManager.updateStations(stationInventory);
        this.networkDirty = false;
        setChanged();
    }

    public List<StorageUnitBlockEntity> getValidUnits() {
        if (networkDirty) {
            refreshNetwork();
        }

        List<StorageUnitBlockEntity> units = new ArrayList<>();
        if (level == null) return units;

        for (BlockPos pos : connectedUnits) {
            if (level.isLoaded(pos)) {
                BlockEntity be = level.getBlockEntity(pos);
                if (be instanceof StorageUnitBlockEntity unit && !unit.isRemoved()) {
                    units.add(unit);
                }
            }
        }
        return units;
    }

    public List<ItemStack> getAllItems() {
        return StorageNetworkManager.aggregateItems(getValidUnits());
    }

    public ItemStack insertItem(ItemStack stack) {
        ItemStack remainder = StorageNetworkManager.insertItem(getValidUnits(), stack);
        setChanged();
        return remainder;
    }

    public ItemStack extractItem(ItemStack template, int amount) {
        ItemStack extracted = StorageNetworkManager.extractItem(getValidUnits(), template, amount);
        setChanged();
        return extracted;
    }

    public StationManager getStationManager() {
        return stationManager;
    }

    public NonNullList<ItemStack> getStationInventory() {
        return stationInventory;
    }

    public void openNetworkAccess(Player player) {
        player.openMenu(new SimpleMenuProvider(
                (id, inv, p) -> new StorageAccessMenu(id, inv, this),
                Component.translatable("gui.magicstorage.storage_access")
        ));
    }

    public void openCraftingAccess(Player player) {
        player.openMenu(new SimpleMenuProvider(
                (id, inv, p) -> new CraftingAccessMenu(id, inv, this),
                Component.translatable("gui.magicstorage.crafting_access")
        ));
    }

    //? >= 26.1 {
    /*@Override
    protected void saveAdditional(net.minecraft.world.level.storage.ValueOutput output) {
        super.saveAdditional(output);
        ContainerHelper.saveAllItems(output, stationInventory);
    }

    @Override
    protected void loadAdditional(net.minecraft.world.level.storage.ValueInput input) {
        super.loadAdditional(input);
        ContainerHelper.loadAllItems(input, stationInventory);
        stationManager.updateStations(stationInventory);
        this.networkDirty = true;
    }
    *///?} < 26.1 && > 1.20.1 {
    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        ContainerHelper.saveAllItems(tag, stationInventory, registries);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        ContainerHelper.loadAllItems(tag, stationInventory, registries);
        stationManager.updateStations(stationInventory);
        this.networkDirty = true;
    }
    //?} <= 1.20.1 {
    /*@Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        ContainerHelper.saveAllItems(tag, stationInventory);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        ContainerHelper.loadAllItems(tag, stationInventory);
        stationManager.updateStations(stationInventory);
        this.networkDirty = true;
    }
    *///?}

    @Override
    public int getContainerSize() {
        return stationInventory.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack s : stationInventory) {
            if (!s.isEmpty()) return false;
        }
        return true;
    }

    @Override
    public ItemStack getItem(int slot) {
        return slot >= 0 && slot < stationInventory.size() ? stationInventory.get(slot) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        ItemStack s = ContainerHelper.removeItem(stationInventory, slot, amount);
        if (!s.isEmpty()) {
            stationManager.updateStations(stationInventory);
            setChanged();
        }
        return s;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return ContainerHelper.takeItem(stationInventory, slot);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        if (slot >= 0 && slot < stationInventory.size()) {
            stationInventory.set(slot, stack);
            stationManager.updateStations(stationInventory);
            setChanged();
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    @Override
    public void clearContent() {
        stationInventory.clear();
        stationManager.updateStations(stationInventory);
        setChanged();
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.magicstorage.storage_heart");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new StorageAccessMenu(containerId, playerInventory, this);
    }
}
