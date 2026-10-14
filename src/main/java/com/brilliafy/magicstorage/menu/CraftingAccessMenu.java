package com.brilliafy.magicstorage.menu;

import com.brilliafy.magicstorage.blockentity.StorageHeartBlockEntity;
import com.brilliafy.magicstorage.compat.farmersdelight.FarmersDelightCompat;
import com.brilliafy.magicstorage.init.ModMenus;
import com.brilliafy.magicstorage.storage.StationManager;
import com.brilliafy.magicstorage.storage.StationMode;
import com.brilliafy.magicstorage.storage.StationRecipeManager;
import com.brilliafy.magicstorage.util.ItemHelper;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//? > 1.20.1 {
import net.minecraft.world.item.crafting.CraftingInput;
//?}

public class CraftingAccessMenu extends StorageAccessMenu {
    public static final int STATION_SELECT_BASE = 100;
    public static final int RECIPE_SELECT_BASE = 120;
    public static final int OVERLAY_CLICK_BASE = 200;
    public static final int OVERLAY_DEPOSIT = 350;

    protected final TransientCraftingContainer craftSlots = new TransientCraftingContainer(this, 3, 3);
    protected final ResultContainer resultSlots = new ResultContainer();
    protected final Container furnaceContainer;
    protected final Container stationContainer = new SimpleContainer(StationManager.STATION_SLOTS);
    protected final ContainerData furnaceData = new SimpleContainerData(6);
    protected final Level level;

    protected StationMode activeMode = StationMode.CRAFTING;
    protected int selectedRecipeIndex = 0;

    public CraftingAccessMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, null);
    }

    public CraftingAccessMenu(int containerId, Inventory playerInventory, StorageHeartBlockEntity heart) {
        super(ModMenus.CRAFTING_ACCESS, containerId, playerInventory, heart, 4, 8, 18);
        this.level = playerInventory.player.level();
        this.furnaceContainer = heart != null ? heart.getVirtualFurnaceContainer() : new SimpleContainer(3);

        // Result slot at slot index 72
        this.addSlot(new MagicStorageResultSlot(this, playerInventory.player, craftSlots, resultSlots));

        // 3x3 Crafting grid (slot indices 73..81)
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                this.addSlot(new StationCraftSlot(this, craftSlots, col + row * 3, 8 + col * 18, 110 + row * 18));
            }
        }

        // Virtual Furnace slots (slot indices 82..84)
        this.addSlot(new VirtualFurnaceSlot(this, furnaceContainer, 0, 56, 117, 0)); // Input
        this.addSlot(new VirtualFurnaceSlot(this, furnaceContainer, 1, 56, 153, 1)); // Fuel
        this.addSlot(new VirtualFurnaceSlot(this, furnaceContainer, 2, 120, 139, 2)); // Output

        // Dedicated Stonecutter slots
        this.addSlot(new ModeSlot(this, craftSlots, 0, 20, 133, StationMode.STONECUTTER));
        this.addSlot(new ModeResultSlot(this, playerInventory.player, craftSlots, resultSlots, 0, 143, 133, StationMode.STONECUTTER));

        // Dedicated Smithing slots
        this.addSlot(new ModeSlot(this, craftSlots, 0, 18, 130, StationMode.SMITHING));
        this.addSlot(new ModeSlot(this, craftSlots, 1, 42, 130, StationMode.SMITHING));
        this.addSlot(new ModeSlot(this, craftSlots, 2, 66, 130, StationMode.SMITHING));
        this.addSlot(new ModeResultSlot(this, playerInventory.player, craftSlots, resultSlots, 0, 124, 130, StationMode.SMITHING));

        // Dedicated Grindstone slots
        this.addSlot(new ModeSlot(this, craftSlots, 0, 35, 120, StationMode.GRINDSTONE));
        this.addSlot(new ModeSlot(this, craftSlots, 1, 35, 142, StationMode.GRINDSTONE));
        this.addSlot(new ModeResultSlot(this, playerInventory.player, craftSlots, resultSlots, 0, 105, 131, StationMode.GRINDSTONE));

        // Dedicated Cartography slots
        this.addSlot(new ModeSlot(this, craftSlots, 0, 35, 120, StationMode.CARTOGRAPHY));
        this.addSlot(new ModeSlot(this, craftSlots, 1, 35, 142, StationMode.CARTOGRAPHY));
        this.addSlot(new ModeResultSlot(this, playerInventory.player, craftSlots, resultSlots, 0, 105, 131, StationMode.CARTOGRAPHY));

        // Dedicated Anvil slots
        this.addSlot(new ModeSlot(this, craftSlots, 0, 35, 131, StationMode.ANVIL));
        this.addSlot(new ModeSlot(this, craftSlots, 1, 65, 131, StationMode.ANVIL));
        this.addSlot(new ModeResultSlot(this, playerInventory.player, craftSlots, resultSlots, 0, 123, 131, StationMode.ANVIL));

        // Dedicated Loom slots
        this.addSlot(new ModeSlot(this, craftSlots, 0, 25, 131, StationMode.LOOM));
        this.addSlot(new ModeSlot(this, craftSlots, 1, 49, 131, StationMode.LOOM));
        this.addSlot(new ModeSlot(this, craftSlots, 2, 73, 131, StationMode.LOOM));
        this.addSlot(new ModeResultSlot(this, playerInventory.player, craftSlots, resultSlots, 0, 125, 131, StationMode.LOOM));

        // Dedicated Cutting Board slots
        this.addSlot(new ModeSlot(this, craftSlots, 0, 35, 131, StationMode.FD_CUTTING));
        this.addSlot(new ModeSlot(this, craftSlots, 1, 65, 131, StationMode.FD_CUTTING));
        this.addSlot(new ModeResultSlot(this, playerInventory.player, craftSlots, resultSlots, 0, 123, 131, StationMode.FD_CUTTING));

        // Dedicated Cooking Pot slots
        this.addSlot(new ModeSlot(this, craftSlots, 0, 25, 118, StationMode.FD_COOKING));
        this.addSlot(new ModeSlot(this, craftSlots, 1, 45, 118, StationMode.FD_COOKING));
        this.addSlot(new ModeSlot(this, craftSlots, 2, 65, 118, StationMode.FD_COOKING));
        this.addSlot(new ModeSlot(this, craftSlots, 3, 25, 138, StationMode.FD_COOKING));
        this.addSlot(new ModeSlot(this, craftSlots, 4, 45, 138, StationMode.FD_COOKING));
        this.addSlot(new ModeSlot(this, craftSlots, 5, 65, 138, StationMode.FD_COOKING));
        this.addSlot(new ModeSlot(this, craftSlots, 6, 94, 148, StationMode.FD_COOKING));
        this.addSlot(new ModeResultSlot(this, playerInventory.player, craftSlots, resultSlots, 0, 124, 126, StationMode.FD_COOKING));

        // Dedicated Apotheosis Reforging slots
        this.addSlot(new ModeSlot(this, craftSlots, 0, 35, 131, StationMode.APOTH_REFORGING));
        this.addSlot(new ModeSlot(this, craftSlots, 1, 60, 131, StationMode.APOTH_REFORGING));
        this.addSlot(new ModeSlot(this, craftSlots, 2, 85, 131, StationMode.APOTH_REFORGING));
        this.addSlot(new ModeResultSlot(this, playerInventory.player, craftSlots, resultSlots, 0, 128, 131, StationMode.APOTH_REFORGING));

        // Dedicated Apotheosis Salvaging slots
        this.addSlot(new ModeSlot(this, craftSlots, 0, 45, 131, StationMode.APOTH_SALVAGING));
        this.addSlot(new ModeResultSlot(this, playerInventory.player, craftSlots, resultSlots, 0, 115, 131, StationMode.APOTH_SALVAGING));

        // Dedicated Apotheosis Gem Cutting slots
        this.addSlot(new ModeSlot(this, craftSlots, 0, 35, 131, StationMode.APOTH_GEM_CUTTING));
        this.addSlot(new ModeSlot(this, craftSlots, 1, 60, 131, StationMode.APOTH_GEM_CUTTING));
        this.addSlot(new ModeSlot(this, craftSlots, 2, 85, 131, StationMode.APOTH_GEM_CUTTING));
        this.addSlot(new ModeResultSlot(this, playerInventory.player, craftSlots, resultSlots, 0, 128, 131, StationMode.APOTH_GEM_CUTTING));

        // Dedicated Tetra Modular Workbench slots
        this.addSlot(new ModeSlot(this, craftSlots, 0, 35, 131, StationMode.TETRA_WORKBENCH));
        this.addSlot(new ModeSlot(this, craftSlots, 1, 65, 131, StationMode.TETRA_WORKBENCH));
        this.addSlot(new ModeResultSlot(this, playerInventory.player, craftSlots, resultSlots, 0, 123, 131, StationMode.TETRA_WORKBENCH));

        // Station mirror slots for client synchronization
        for (int i = 0; i < StationManager.STATION_SLOTS; i++) {
            this.addSlot(new ReadOnlySlot(stationContainer, i, -2000, -2000));
        }

        this.addDataSlots(this.furnaceData);

        if (heart != null) {
            syncStationContainer();
        }
    }

    protected void syncStationContainer() {
        if (heart == null) return;
        for (int i = 0; i < StationManager.STATION_SLOTS; i++) {
            stationContainer.setItem(i, heart.getItem(i));
        }
    }

    public StationMode getActiveMode() {
        if (level.isClientSide()) {
            return StationMode.fromId(furnaceData.get(4));
        }
        return activeMode;
    }

    public int getSelectedRecipeIndex() {
        if (level.isClientSide()) {
            return furnaceData.get(5);
        }
        return selectedRecipeIndex;
    }

    public int getFurnaceLitTime() { return furnaceData.get(0); }
    public int getFurnaceLitDuration() { return furnaceData.get(1); }
    public int getFurnaceCookingProgress() { return furnaceData.get(2); }
    public int getFurnaceCookingTotalTime() { return furnaceData.get(3); }

    public Container getStationContainer() { return stationContainer; }
    public Container getFurnaceContainer() { return furnaceContainer; }
    public TransientCraftingContainer getCraftSlots() { return craftSlots; }
    public ResultContainer getResultSlots() { return resultSlots; }
    public Level getLevel() { return level; }

    @Override
    public void broadcastChanges() {
        if (heart != null) {
            furnaceData.set(0, heart.getFurnaceLitTime());
            furnaceData.set(1, heart.getFurnaceLitDuration());
            furnaceData.set(2, heart.getFurnaceCookingProgress());
            furnaceData.set(3, heart.getFurnaceCookingTotalTime());
            furnaceData.set(4, activeMode.getId());
            furnaceData.set(5, selectedRecipeIndex);
            syncStationContainer();
        }
        super.broadcastChanges();
    }

    @Override
    public boolean clickMenuButton(Player player, int id) {
        if (id >= STATION_SELECT_BASE && id < STATION_SELECT_BASE + StationMode.values().length) {
            StationMode newMode = StationMode.fromId(id - STATION_SELECT_BASE);
            if (newMode != activeMode) {
                safeClearContainer(player, craftSlots);
                activeMode = newMode;
                selectedRecipeIndex = 0;
                updateCraftingResult();
                broadcastChanges();
            }
            return true;
        }

        if (id >= RECIPE_SELECT_BASE && id < RECIPE_SELECT_BASE + 32) {
            selectedRecipeIndex = id - RECIPE_SELECT_BASE;
            updateCraftingResult();
            broadcastChanges();
            return true;
        }

        if (id == OVERLAY_DEPOSIT) {
            ItemStack carried = getCarried();
            if (!carried.isEmpty() && heart != null) {
                setCarried(heart.insertItem(carried));
                needsRefresh = true;
            }
            return true;
        }

        if (id >= OVERLAY_CLICK_BASE && id < OVERLAY_CLICK_BASE + displaySlots) {
            int slot = id - OVERLAY_CLICK_BASE;
            handleDisplaySlot(slot, 0, false, player);
            return true;
        }

        if (id >= OVERLAY_CLICK_BASE + 50 && id < OVERLAY_CLICK_BASE + 50 + displaySlots) {
            int slot = id - (OVERLAY_CLICK_BASE + 50);
            handleDisplaySlot(slot, 1, false, player);
            return true;
        }

        if (id >= OVERLAY_CLICK_BASE + 100 && id < OVERLAY_CLICK_BASE + 100 + displaySlots) {
            int slot = id - (OVERLAY_CLICK_BASE + 100);
            handleDisplaySlot(slot, 0, true, player);
            return true;
        }

        return super.clickMenuButton(player, id);
    }

    public static class StationCraftSlot extends Slot {
        private final CraftingAccessMenu menu;

        public StationCraftSlot(CraftingAccessMenu menu, Container container, int index, int x, int y) {
            super(container, index, x, y);
            this.menu = menu;
        }

        @Override
        public boolean isActive() {
            return menu.getActiveMode() == StationMode.CRAFTING;
        }
    }

    public static class ModeSlot extends Slot {
        private final CraftingAccessMenu menu;
        private final StationMode targetMode;

        public ModeSlot(CraftingAccessMenu menu, Container container, int index, int x, int y, StationMode targetMode) {
            super(container, index, x, y);
            this.menu = menu;
            this.targetMode = targetMode;
        }

        @Override
        public boolean isActive() {
            return menu.getActiveMode() == targetMode;
        }
    }

    public static class VirtualFurnaceSlot extends Slot {
        private final CraftingAccessMenu menu;
        public final int furnaceSlotType; // 0=input, 1=fuel, 2=output

        public VirtualFurnaceSlot(CraftingAccessMenu menu, Container container, int index, int x, int y, int type) {
            super(container, index, x, y);
            this.menu = menu;
            this.furnaceSlotType = type;
        }

        @Override
        public boolean isActive() {
            return menu.getActiveMode() == StationMode.FURNACE;
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            if (furnaceSlotType == 2) return false;
            if (furnaceSlotType == 1) return StationRecipeManager.getFuelBurnTime(stack, menu.level) > 0;
            return true;
        }
    }

    public static class MagicStorageResultSlot extends ResultSlot {
        private final CraftingAccessMenu menu;

        public MagicStorageResultSlot(CraftingAccessMenu menu, Player player,
                                      TransientCraftingContainer craftSlots, ResultContainer resultSlots) {
            super(player, craftSlots, resultSlots, 0, 101, 128);
            this.menu = menu;
        }

        @Override
        public boolean isActive() {
            return menu.getActiveMode() == StationMode.CRAFTING;
        }

        @Override
        public void onTake(Player player, ItemStack stack) {
            super.onTake(player, stack);
            menu.handleResultTake(player, stack);
        }
    }

    public static class ModeResultSlot extends ResultSlot {
        private final CraftingAccessMenu menu;
        private final StationMode targetMode;

        public ModeResultSlot(CraftingAccessMenu menu, Player player, TransientCraftingContainer craftSlots,
                              ResultContainer resultSlots, int index, int x, int y, StationMode targetMode) {
            super(player, craftSlots, resultSlots, index, x, y);
            this.menu = menu;
            this.targetMode = targetMode;
        }

        @Override
        public boolean isActive() {
            return menu.getActiveMode() == targetMode;
        }

        @Override
        public void onTake(Player player, ItemStack stack) {
            super.onTake(player, stack);
            menu.handleResultTake(player, stack);
        }
    }

    public void handleResultTake(Player player, ItemStack stack) {
        ItemStack patternBackup = ItemStack.EMPTY;
        int patternSlot = -1;
        boolean isCuttingTool = false;
        ItemStack cutItem = ItemStack.EMPTY;
        ItemStack toolItem = ItemStack.EMPTY;

        for (int i = 0; i < craftSlots.getContainerSize(); i++) {
            if (StationRecipeManager.isReusableIngredient(craftSlots, i, level)) {
                patternSlot = i;
                patternBackup = craftSlots.getItem(i).copy();
                if (FarmersDelightCompat.isCuttingToolSlot(craftSlots, i, level)) {
                    isCuttingTool = true;
                    toolItem = patternBackup;
                    for (int j = 0; j < craftSlots.getContainerSize(); j++) {
                        if (j != i && !craftSlots.getItem(j).isEmpty()) {
                            cutItem = craftSlots.getItem(j).copy();
                            break;
                        }
                    }
                }
                break;
            }
        }

        if (patternSlot != -1 && !patternBackup.isEmpty()) {
            if (isCuttingTool && patternBackup.isDamageableItem()) {
                int newDmg = patternBackup.getDamageValue() + 1;
                if (newDmg < patternBackup.getMaxDamage()) {
                    patternBackup.setDamageValue(newDmg);
                    craftSlots.setItem(patternSlot, patternBackup);
                } else {
                    craftSlots.setItem(patternSlot, ItemStack.EMPTY);
                }
            } else {
                craftSlots.setItem(patternSlot, patternBackup);
            }
        }

        if (isCuttingTool && !cutItem.isEmpty() && !toolItem.isEmpty()) {
            FarmersDelightCompat.handleCuttingBoardDrops(cutItem, toolItem, player, level);
        }

        slotsChanged(craftSlots);
    }

    public int getResultSlotIndex() {
        return getPlayerSlotStart() + 36;
    }

    //? >= 26.1 {
    /*@Override
    public void clicked(int slotId, int button, net.minecraft.world.inventory.ContainerInput type, Player player) {
        Slot clickedSlot = slotId >= 0 && slotId < slots.size() ? slots.get(slotId) : null;
        if (clickedSlot instanceof ResultSlot && type == net.minecraft.world.inventory.ContainerInput.QUICK_MOVE) {
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
        Slot clickedSlot = slotId >= 0 && slotId < slots.size() ? slots.get(slotId) : null;
        if (clickedSlot instanceof ResultSlot && type == ClickType.QUICK_MOVE) {
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

    private boolean isGrindstoneRepairInput() {
        if (heart == null || !heart.getStationManager().hasGrindstone()) return false;
        List<ItemStack> nonNull = new ArrayList<>();
        for (int i = 0; i < craftSlots.getContainerSize(); i++) {
            ItemStack s = craftSlots.getItem(i);
            if (!s.isEmpty()) nonNull.add(s);
        }
        if (nonNull.size() == 2) {
            ItemStack s0 = nonNull.get(0);
            ItemStack s1 = nonNull.get(1);
            return s0.is(s1.getItem()) && s0.isDamageableItem();
        }
        return false;
    }

    protected void updateCraftingResult() {
        if (activeMode != StationMode.CRAFTING) {
            ItemStack stationRes = StationRecipeManager.computeStationResultForMode(
                    activeMode, craftSlots, level, heart != null ? heart.getStationManager() : null, player, selectedRecipeIndex
            );
            resultSlots.setItem(0, stationRes);
            return;
        }

        if (isGrindstoneRepairInput()) {
            ItemStack stationResult = StationRecipeManager.computeStationResult(
                craftSlots, level, heart != null ? heart.getStationManager() : null, player
            );
            resultSlots.setItem(0, stationResult);
            return;
        }

        //? >= 26.1 {
        /*CraftingInput input = craftSlots.asCraftInput();
        Optional<RecipeHolder<CraftingRecipe>> match = level.getServer().getRecipeManager().getRecipeFor(RecipeType.CRAFTING, input, level);
        if (match.isPresent()) {
            ItemStack result = match.get().value().assemble(input);
            resultSlots.setItem(0, result);
        } else {
            ItemStack stationResult = StationRecipeManager.computeStationResult(
                craftSlots, level, heart != null ? heart.getStationManager() : null, player
            );
            resultSlots.setItem(0, stationResult);
        }
        *///?} < 26.1 && > 1.20.1 {
        CraftingInput input = craftSlots.asCraftInput();
        Optional<RecipeHolder<CraftingRecipe>> match = level.getServer().getRecipeManager().getRecipeFor(RecipeType.CRAFTING, input, level);
        if (match.isPresent()) {
            ItemStack result = match.get().value().assemble(input, level.registryAccess());
            resultSlots.setItem(0, result);
        } else {
            ItemStack stationResult = StationRecipeManager.computeStationResult(
                craftSlots, level, heart != null ? heart.getStationManager() : null, player
            );
            resultSlots.setItem(0, stationResult);
        }
        //?} <= 1.20.1 {
        /*Optional<CraftingRecipe> match = level.getServer().getRecipeManager().getRecipeFor(RecipeType.CRAFTING, craftSlots, level);
        if (match.isPresent()) {
            ItemStack result = match.get().assemble(craftSlots, level.registryAccess());
            resultSlots.setItem(0, result);
        } else {
            ItemStack stationResult = StationRecipeManager.computeStationResult(
                craftSlots, level, heart != null ? heart.getStationManager() : null, player
            );
            resultSlots.setItem(0, stationResult);
        }
        *///?}
    }

    public void instantCraftAll(Player player) {
        if (level.isClientSide() || resultSlots.getItem(0).isEmpty()) return;

        ItemStack singleResult = resultSlots.getItem(0).copy();
        if (singleResult.isEmpty() || singleResult.getCount() <= 0) return;

        int totalSpace = 0;
        for (int i = 0; i < 36; i++) {
            ItemStack invStack = player.getInventory().getItem(i);
            if (invStack.isEmpty()) {
                totalSpace += singleResult.getMaxStackSize();
            } else if (ItemHelper.canMerge(invStack, singleResult)) {
                totalSpace += Math.max(0, invStack.getMaxStackSize() - invStack.getCount());
            }
        }
        int maxCraftsForInv = totalSpace / singleResult.getCount();
        if (maxCraftsForInv <= 0) return;

        int maxCrafts = maxCraftsForInv;

        for (int i = 0; i < craftSlots.getContainerSize(); i++) {
            ItemStack inGrid = craftSlots.getItem(i);
            if (!inGrid.isEmpty()) {
                if (StationRecipeManager.isReusableIngredient(craftSlots, i, level)) {
                    if (FarmersDelightCompat.isCuttingToolSlot(craftSlots, i, level) && inGrid.isDamageableItem()) {
                        int remDmg = inGrid.getMaxDamage() - inGrid.getDamageValue();
                        maxCrafts = Math.min(maxCrafts, remDmg);
                    }
                    continue;
                }
                if (inGrid.isDamageableItem()) {
                    maxCrafts = Math.min(maxCrafts, inGrid.getCount());
                } else {
                    int countAvailable = inGrid.getCount();
                    if (heart != null) {
                        countAvailable += com.brilliafy.magicstorage.storage.StorageNetworkManager.countItem(heart.getValidUnits(), inGrid);
                    }
                    maxCrafts = Math.min(maxCrafts, countAvailable);
                }
            }
        }

        if (maxCrafts <= 0) return;

        int craftedCount = 0;

        for (int c = 0; c < maxCrafts; c++) {
            List<ItemStack> extractedFromHeart = new ArrayList<>();
            List<Integer> gridShrunk = new ArrayList<>();
            boolean canCraftOne = true;

            for (int i = 0; i < craftSlots.getContainerSize(); i++) {
                ItemStack req = craftSlots.getItem(i);
                if (!req.isEmpty()) {
                    if (StationRecipeManager.isReusableIngredient(craftSlots, i, level)) continue;
                    if (req.getCount() > 1) {
                        req.shrink(1);
                        gridShrunk.add(i);
                    } else if (heart != null) {
                        ItemStack extracted = heart.extractItem(req, 1);
                        if (!extracted.isEmpty()) {
                            extractedFromHeart.add(extracted);
                        } else if (req.getCount() == 1) {
                            req.shrink(1);
                            gridShrunk.add(i);
                        } else {
                            canCraftOne = false;
                            break;
                        }
                    } else if (req.getCount() == 1) {
                        req.shrink(1);
                        gridShrunk.add(i);
                    } else {
                        canCraftOne = false;
                        break;
                    }
                }
            }

            if (!canCraftOne) {
                if (heart != null) {
                    for (ItemStack ext : extractedFromHeart) {
                        heart.insertItem(ext);
                    }
                }
                for (int slotIdx : gridShrunk) {
                    craftSlots.getItem(slotIdx).grow(1);
                }
                break;
            }
            craftedCount += singleResult.getCount();
        }

        for (int i = 0; i < craftSlots.getContainerSize(); i++) {
            ItemStack inGrid = craftSlots.getItem(i);
            if (!inGrid.isEmpty() && FarmersDelightCompat.isCuttingToolSlot(craftSlots, i, level) && inGrid.isDamageableItem()) {
                int craftsDone = singleResult.getCount() > 0 ? craftedCount / singleResult.getCount() : 0;
                int newDmg = inGrid.getDamageValue() + craftsDone;
                if (newDmg < inGrid.getMaxDamage()) {
                    inGrid.setDamageValue(newDmg);
                } else {
                    craftSlots.setItem(i, ItemStack.EMPTY);
                }
                break;
            }
        }

        int maxStack = singleResult.getMaxStackSize();
        while (craftedCount > 0) {
            int toGive = Math.min(craftedCount, maxStack);
            craftedCount -= toGive;
            ItemStack stackToGive = singleResult.copy();
            stackToGive.setCount(toGive);

            if (!player.getInventory().add(stackToGive)) {
                ItemStack remainder = heart != null ? heart.insertItem(stackToGive) : stackToGive;
                if (!remainder.isEmpty()) {
                    ItemHelper.dropItem(player, remainder);
                }
            }
        }

        updateCraftingResult();
    }

    public void safeClearContainer(Player player, Container container) {
        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);
            if (!stack.isEmpty()) {
                container.setItem(i, ItemStack.EMPTY);
                if (heart != null) {
                    stack = heart.insertItem(stack);
                }
                if (!stack.isEmpty()) {
                    if (!player.getInventory().add(stack)) {
                        ItemHelper.dropItem(player, stack);
                    }
                }
            }
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = index >= 0 && index < this.slots.size() ? this.slots.get(index) : null;
        if (slot == null || !slot.hasItem()) {
            return ItemStack.EMPTY;
        }

        if (slot instanceof ResultSlot) {
            return ItemStack.EMPTY;
        }

        // Virtual furnace output
        if (slot instanceof VirtualFurnaceSlot vSlot && vSlot.furnaceSlotType == 2) {
            ItemStack stack = slot.getItem();
            ItemStack copy = stack.copy();
            if (!this.moveItemStackTo(stack, 36, 72, true)) {
                return ItemStack.EMPTY;
            }
            slot.onQuickCraft(stack, copy);
            slot.setChanged();
            return copy;
        }

        // Crafting / Station inputs or furnace inputs
        if (slot instanceof StationCraftSlot || slot instanceof ModeSlot ||
                (slot instanceof VirtualFurnaceSlot vSlot && vSlot.furnaceSlotType != 2)) {
            ItemStack stack = slot.getItem();
            ItemStack copy = stack.copy();
            if (heart != null) {
                ItemStack remainder = heart.insertItem(stack);
                if (remainder.getCount() != copy.getCount()) {
                    slot.set(remainder);
                    slot.setChanged();
                    if (slot instanceof VirtualFurnaceSlot) {
                        furnaceContainer.setChanged();
                    } else {
                        slotsChanged(craftSlots);
                    }
                    return copy;
                }
            }
            if (!player.getInventory().add(stack)) {
                return ItemStack.EMPTY;
            }
            slot.set(ItemStack.EMPTY);
            slot.setChanged();
            if (slot instanceof VirtualFurnaceSlot) {
                furnaceContainer.setChanged();
            } else {
                slotsChanged(craftSlots);
            }
            return copy;
        }

        // Player inventory (36..71)
        if (index >= 36 && index <= 71) {
            ItemStack stack = slot.getItem();
            if (activeMode == StationMode.FURNACE) {
                if (StationRecipeManager.getFuelBurnTime(stack, level) > 0) {
                    if (this.moveItemStackTo(stack, 83, 84, false)) {
                        slot.setChanged();
                        return stack;
                    }
                }
                if (this.moveItemStackTo(stack, 82, 83, false)) {
                    slot.setChanged();
                    return stack;
                }
            }
        }

        return super.quickMoveStack(player, index);
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        safeClearContainer(player, this.craftSlots);
    }
}
