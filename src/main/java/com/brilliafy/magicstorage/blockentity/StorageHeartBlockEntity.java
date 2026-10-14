package com.brilliafy.magicstorage.blockentity;

import com.brilliafy.magicstorage.block.BlockCraftingAccess;
import com.brilliafy.magicstorage.block.BlockRemoteAccess;
import com.brilliafy.magicstorage.block.BlockStorageAccess;
import com.brilliafy.magicstorage.block.BlockStorageUnit;
import com.brilliafy.magicstorage.init.ModBlockEntities;
import com.brilliafy.magicstorage.menu.CraftingAccessMenu;
import com.brilliafy.magicstorage.menu.StorageAccessMenu;
import com.brilliafy.magicstorage.storage.StationManager;
import com.brilliafy.magicstorage.storage.StationRecipeManager;
import com.brilliafy.magicstorage.storage.StorageNetworkManager;
import com.brilliafy.magicstorage.util.ItemHelper;
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
    public static final int FURNACE_SLOT_INPUT = 20;
    public static final int FURNACE_SLOT_FUEL = 21;
    public static final int FURNACE_SLOT_OUTPUT = 22;
    public static final int TOTAL_HEART_SLOTS = 23;

    private final Set<BlockPos> connectedUnits = new LinkedHashSet<>();
    private final Set<BlockPos> connectedAccesses = new LinkedHashSet<>();
    private final NonNullList<ItemStack> stationInventory = NonNullList.withSize(TOTAL_HEART_SLOTS, ItemStack.EMPTY);
    private final StationManager stationManager = new StationManager();
    private boolean networkDirty = true;

    private int furnaceLitTime = 0;
    private int furnaceLitDuration = 0;
    private int furnaceCookingProgress = 0;
    private int furnaceCookingTotalTime = 200;
    private long lastTickGameTime = 0;

    public StorageHeartBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.STORAGE_HEART, pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, StorageHeartBlockEntity be) {
        if (!level.isClientSide()) {
            if (be.networkDirty) {
                be.refreshNetwork();
            }
            be.catchUpVirtualFurnace(level);
            be.tickVirtualFurnace();
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

        stationManager.updateStations(stationInventory.subList(0, StationManager.STATION_SLOTS));
        this.networkDirty = false;
        setChanged();
    }

    private void consumeFuelItem(Level level, ItemStack fuel) {
        //? >= 26.1 {
        /*net.minecraft.world.item.ItemStackTemplate remainder = fuel.getItem().getCraftingRemainder();
        fuel.shrink(1);
        if (remainder != null) {
            if (fuel.isEmpty()) {
                setItem(FURNACE_SLOT_FUEL, remainder.create());
            } else {
                net.minecraft.world.Containers.dropItemStack(level, getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ(), remainder.create());
            }
        }
        *///?} < 26.1 {
        ItemStack remainder = ItemStack.EMPTY;
        if (fuel.getItem().hasCraftingRemainingItem()) {
            remainder = new ItemStack(fuel.getItem().getCraftingRemainingItem());
        }
        fuel.shrink(1);
        if (!remainder.isEmpty()) {
            if (fuel.isEmpty()) {
                setItem(FURNACE_SLOT_FUEL, remainder);
            } else {
                net.minecraft.world.Containers.dropItemStack(level, getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ(), remainder);
            }
        }
        //?}
    }

    public void catchUpVirtualFurnace(Level level) {
        if (level == null || level.isClientSide()) return;
        long gameTime = level.getGameTime();
        if (lastTickGameTime > 0 && gameTime > lastTickGameTime + 1) {
            long missed = gameTime - lastTickGameTime - 1;
            simulateFurnaceTicks(level, (int) Math.min(missed, 72000L));
        }
        lastTickGameTime = gameTime;
    }

    public void simulateFurnaceTicks(Level level, int ticksToSimulate) {
        boolean hasFurnace = stationManager.hasFurnace();
        boolean hasBlast = stationManager.hasBlastFurnace();
        boolean hasSmoker = stationManager.hasSmoker();
        if (!hasFurnace && !hasBlast && !hasSmoker) return;

        int remainingTicks = ticksToSimulate;
        while (remainingTicks > 0) {
            ItemStack input = getItem(FURNACE_SLOT_INPUT);
            if (input.isEmpty()) {
                if (furnaceLitTime > 0) {
                    furnaceLitTime = Math.max(0, furnaceLitTime - remainingTicks);
                }
                break;
            }

            StationRecipeManager.SmeltResult result = StationRecipeManager.getSmeltResult(
                    input, level, hasBlast, hasSmoker, hasFurnace
            );
            if (result == null || !canSmelt(result.getResult())) {
                if (furnaceLitTime > 0) {
                    furnaceLitTime = Math.max(0, furnaceLitTime - remainingTicks);
                }
                furnaceCookingProgress = 0;
                break;
            }

            furnaceCookingTotalTime = result.getCookTime();
            int speed = Math.max(1, result.getSpeed());

            if (furnaceLitTime <= 0) {
                ItemStack fuel = getItem(FURNACE_SLOT_FUEL);
                int burn = StationRecipeManager.getFuelBurnTime(fuel, level);
                if (burn <= 0) {
                    furnaceCookingProgress = 0;
                    break;
                }
                furnaceLitTime = burn;
                furnaceLitDuration = burn;
                consumeFuelItem(level, fuel);
                setChanged();
            }

            int cookProgressNeeded = furnaceCookingTotalTime - furnaceCookingProgress;
            int ticksNeeded = (cookProgressNeeded + speed - 1) / speed;
            int step = Math.min(remainingTicks, Math.min(furnaceLitTime, ticksNeeded));
            if (step <= 0) step = 1;

            furnaceLitTime -= step;
            furnaceCookingProgress += step * speed;
            remainingTicks -= step;

            if (furnaceCookingProgress >= furnaceCookingTotalTime) {
                furnaceCookingProgress = 0;
                smeltItem(result.getResult());
                setChanged();
            }
        }
    }

    public void tickVirtualFurnace() {
        if (level == null || level.isClientSide()) return;
        boolean hasFurnace = stationManager.hasFurnace();
        boolean hasBlast = stationManager.hasBlastFurnace();
        boolean hasSmoker = stationManager.hasSmoker();
        if (!hasFurnace && !hasBlast && !hasSmoker) return;

        boolean dirty = false;

        if (furnaceLitTime > 0) {
            furnaceLitTime--;
        }

        ItemStack input = getItem(FURNACE_SLOT_INPUT);
        if (!input.isEmpty()) {
            StationRecipeManager.SmeltResult result = StationRecipeManager.getSmeltResult(
                    input, level, hasBlast, hasSmoker, hasFurnace
            );
            if (result != null && canSmelt(result.getResult())) {
                furnaceCookingTotalTime = result.getCookTime();

                if (furnaceLitTime <= 0) {
                    ItemStack fuel = getItem(FURNACE_SLOT_FUEL);
                    int burn = StationRecipeManager.getFuelBurnTime(fuel, level);
                    if (burn > 0) {
                        furnaceLitTime = burn;
                        furnaceLitDuration = burn;
                        consumeFuelItem(level, fuel);
                        dirty = true;
                    }
                }

                if (furnaceLitTime > 0) {
                    furnaceCookingProgress += result.getSpeed();
                    if (furnaceCookingProgress >= furnaceCookingTotalTime) {
                        furnaceCookingProgress = 0;
                        smeltItem(result.getResult());
                        dirty = true;
                    }
                }
            } else {
                if (furnaceCookingProgress > 0) {
                    furnaceCookingProgress = Math.max(0, furnaceCookingProgress - 2);
                }
            }
        } else {
            if (furnaceCookingProgress > 0) {
                furnaceCookingProgress = 0;
            }
        }

        if (dirty) {
            setChanged();
        }
    }

    private boolean canSmelt(ItemStack result) {
        if (result.isEmpty()) return false;
        ItemStack output = getItem(FURNACE_SLOT_OUTPUT);
        if (output.isEmpty()) return true;
        if (!ItemHelper.canMerge(output, result)) return false;
        return output.getCount() + result.getCount() <= output.getMaxStackSize();
    }

    private void smeltItem(ItemStack result) {
        ItemStack output = getItem(FURNACE_SLOT_OUTPUT);
        if (output.isEmpty()) {
            setItem(FURNACE_SLOT_OUTPUT, result.copy());
        } else {
            output.grow(result.getCount());
        }
        getItem(FURNACE_SLOT_INPUT).shrink(1);
    }

    public final Container virtualFurnaceContainer = new Container() {
        @Override
        public int getContainerSize() {
            return 3;
        }

        @Override
        public boolean isEmpty() {
            return StorageHeartBlockEntity.this.getItem(FURNACE_SLOT_INPUT).isEmpty()
                    && StorageHeartBlockEntity.this.getItem(FURNACE_SLOT_FUEL).isEmpty()
                    && StorageHeartBlockEntity.this.getItem(FURNACE_SLOT_OUTPUT).isEmpty();
        }

        @Override
        public ItemStack getItem(int slot) {
            return switch (slot) {
                case 0 -> StorageHeartBlockEntity.this.getItem(FURNACE_SLOT_INPUT);
                case 1 -> StorageHeartBlockEntity.this.getItem(FURNACE_SLOT_FUEL);
                case 2 -> StorageHeartBlockEntity.this.getItem(FURNACE_SLOT_OUTPUT);
                default -> ItemStack.EMPTY;
            };
        }

        @Override
        public ItemStack removeItem(int slot, int amount) {
            int actual = switch (slot) {
                case 0 -> FURNACE_SLOT_INPUT;
                case 1 -> FURNACE_SLOT_FUEL;
                case 2 -> FURNACE_SLOT_OUTPUT;
                default -> -1;
            };
            return actual != -1 ? StorageHeartBlockEntity.this.removeItem(actual, amount) : ItemStack.EMPTY;
        }

        @Override
        public ItemStack removeItemNoUpdate(int slot) {
            int actual = switch (slot) {
                case 0 -> FURNACE_SLOT_INPUT;
                case 1 -> FURNACE_SLOT_FUEL;
                case 2 -> FURNACE_SLOT_OUTPUT;
                default -> -1;
            };
            return actual != -1 ? StorageHeartBlockEntity.this.removeItemNoUpdate(actual) : ItemStack.EMPTY;
        }

        @Override
        public void setItem(int slot, ItemStack stack) {
            switch (slot) {
                case 0 -> StorageHeartBlockEntity.this.setItem(FURNACE_SLOT_INPUT, stack);
                case 1 -> StorageHeartBlockEntity.this.setItem(FURNACE_SLOT_FUEL, stack);
                case 2 -> StorageHeartBlockEntity.this.setItem(FURNACE_SLOT_OUTPUT, stack);
            }
        }

        @Override
        public void setChanged() {
            StorageHeartBlockEntity.this.setChanged();
        }

        @Override
        public boolean stillValid(Player player) {
            return StorageHeartBlockEntity.this.stillValid(player);
        }

        @Override
        public void clearContent() {
            StorageHeartBlockEntity.this.setItem(FURNACE_SLOT_INPUT, ItemStack.EMPTY);
            StorageHeartBlockEntity.this.setItem(FURNACE_SLOT_FUEL, ItemStack.EMPTY);
            StorageHeartBlockEntity.this.setItem(FURNACE_SLOT_OUTPUT, ItemStack.EMPTY);
        }
    };

    public Container getVirtualFurnaceContainer() {
        return virtualFurnaceContainer;
    }

    public int getFurnaceLitTime() { return furnaceLitTime; }
    public int getFurnaceLitDuration() { return furnaceLitDuration; }
    public int getFurnaceCookingProgress() { return furnaceCookingProgress; }
    public int getFurnaceCookingTotalTime() { return furnaceCookingTotalTime; }

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

    public boolean addStation(ItemStack stack) {
        if (!StationManager.isAllowedStation(stack)) return false;
        for (int i = 0; i < StationManager.STATION_SLOTS; i++) {
            if (stationInventory.get(i).isEmpty()) {
                stationInventory.set(i, stack);
                stationManager.updateStations(stationInventory.subList(0, StationManager.STATION_SLOTS));
                setChanged();
                return true;
            }
        }
        return false;
    }

    public ItemStack removeLastStation() {
        for (int i = StationManager.STATION_SLOTS - 1; i >= 0; i--) {
            ItemStack stack = stationInventory.get(i);
            if (!stack.isEmpty()) {
                stationInventory.set(i, ItemStack.EMPTY);
                stationManager.updateStations(stationInventory.subList(0, StationManager.STATION_SLOTS));
                setChanged();
                return stack;
            }
        }
        return ItemStack.EMPTY;
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
        output.putInt("FurnaceLitTime", furnaceLitTime);
        output.putInt("FurnaceLitDuration", furnaceLitDuration);
        output.putInt("FurnaceCookingProgress", furnaceCookingProgress);
        output.putInt("FurnaceCookingTotalTime", furnaceCookingTotalTime);
        output.putLong("LastTickGameTime", level != null ? level.getGameTime() : lastTickGameTime);
    }

    @Override
    protected void loadAdditional(net.minecraft.world.level.storage.ValueInput input) {
        super.loadAdditional(input);
        ContainerHelper.loadAllItems(input, stationInventory);
        furnaceLitTime = input.getIntOr("FurnaceLitTime", 0);
        furnaceLitDuration = input.getIntOr("FurnaceLitDuration", 0);
        furnaceCookingProgress = input.getIntOr("FurnaceCookingProgress", 0);
        furnaceCookingTotalTime = input.getIntOr("FurnaceCookingTotalTime", 200);
        lastTickGameTime = input.getLongOr("LastTickGameTime", 0L);
        stationManager.updateStations(stationInventory.subList(0, StationManager.STATION_SLOTS));
        this.networkDirty = true;
    }
    *///?} < 26.1 && > 1.20.1 {
    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        ContainerHelper.saveAllItems(tag, stationInventory, registries);
        tag.putInt("FurnaceLitTime", furnaceLitTime);
        tag.putInt("FurnaceLitDuration", furnaceLitDuration);
        tag.putInt("FurnaceCookingProgress", furnaceCookingProgress);
        tag.putInt("FurnaceCookingTotalTime", furnaceCookingTotalTime);
        tag.putLong("LastTickGameTime", level != null ? level.getGameTime() : lastTickGameTime);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        ContainerHelper.loadAllItems(tag, stationInventory, registries);
        furnaceLitTime = tag.getInt("FurnaceLitTime");
        furnaceLitDuration = tag.getInt("FurnaceLitDuration");
        furnaceCookingProgress = tag.getInt("FurnaceCookingProgress");
        furnaceCookingTotalTime = tag.contains("FurnaceCookingTotalTime") ? tag.getInt("FurnaceCookingTotalTime") : 200;
        lastTickGameTime = tag.contains("LastTickGameTime") ? tag.getLong("LastTickGameTime") : 0L;
        stationManager.updateStations(stationInventory.subList(0, StationManager.STATION_SLOTS));
        this.networkDirty = true;
    }
    //?} <= 1.20.1 {
    /*@Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        ContainerHelper.saveAllItems(tag, stationInventory);
        tag.putInt("FurnaceLitTime", furnaceLitTime);
        tag.putInt("FurnaceLitDuration", furnaceLitDuration);
        tag.putInt("FurnaceCookingProgress", furnaceCookingProgress);
        tag.putInt("FurnaceCookingTotalTime", furnaceCookingTotalTime);
        tag.putLong("LastTickGameTime", level != null ? level.getGameTime() : lastTickGameTime);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        ContainerHelper.loadAllItems(tag, stationInventory);
        furnaceLitTime = tag.getInt("FurnaceLitTime");
        furnaceLitDuration = tag.getInt("FurnaceLitDuration");
        furnaceCookingProgress = tag.getInt("FurnaceCookingProgress");
        furnaceCookingTotalTime = tag.contains("FurnaceCookingTotalTime") ? tag.getInt("FurnaceCookingTotalTime") : 200;
        lastTickGameTime = tag.contains("LastTickGameTime") ? tag.getLong("LastTickGameTime") : 0L;
        stationManager.updateStations(stationInventory.subList(0, StationManager.STATION_SLOTS));
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
            stationManager.updateStations(stationInventory.subList(0, StationManager.STATION_SLOTS));
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
            stationManager.updateStations(stationInventory.subList(0, StationManager.STATION_SLOTS));
            setChanged();
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        if (slot < StationManager.STATION_SLOTS) {
            return StationManager.isAllowedStation(stack);
        }
        if (slot == FURNACE_SLOT_INPUT) return true;
        if (slot == FURNACE_SLOT_FUEL) return StationRecipeManager.getFuelBurnTime(stack, level) > 0;
        return false;
    }

    @Override
    public void clearContent() {
        stationInventory.clear();
        stationManager.updateStations(stationInventory.subList(0, StationManager.STATION_SLOTS));
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
