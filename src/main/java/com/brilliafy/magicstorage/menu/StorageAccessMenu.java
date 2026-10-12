package com.brilliafy.magicstorage.menu;

import com.brilliafy.magicstorage.blockentity.StorageHeartBlockEntity;
import com.brilliafy.magicstorage.init.ModMenus;
import com.brilliafy.magicstorage.storage.StorageNetworkManager;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
//? < 26.1 {
import net.minecraft.world.inventory.ClickType;
//?}
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Base menu for terminals connected to a Storage Heart.
 *
 * <p>The aggregated network contents are mirrored into a read-only, vanilla-synchronised
 * {@link SimpleContainer} so the client sees them without any custom packets. The server
 * periodically rebuilds the view (search/sort/page) and vanilla menu broadcasting pushes
 * the changes to the client. Clicking a mirrored slot extracts/deposits through the heart.
 */
public class StorageAccessMenu extends AbstractContainerMenu {

    /** Characters the search box may transmit through button-click packets (kept subset of ASCII). */
    public static final String SEARCH_ALPHABET = "abcdefghijklmnopqrstuvwxyz0123456789 @#_-:/.+=";
    public static final int SEARCH_CLEAR = 64;
    public static final int SEARCH_COMMIT = 65;
    public static final int SORT_QUANTITY = 66;
    public static final int SORT_NAME = 67;
    public static final int SORT_MOD = 68;
    public static final int SORT_TOGGLE = 69;
    public static final int PAGE_UP = 70;
    public static final int PAGE_DOWN = 71;

    protected final StorageHeartBlockEntity heart;
    protected final Inventory playerInventory;
    protected final Player player;
    protected final SimpleContainer display;
    protected final int displaySlots;

    protected StorageNetworkManager.SortOrder sortOrder = StorageNetworkManager.SortOrder.QUANTITY;
    protected boolean sortAscending = false;
    protected String searchQuery = "";
    protected int page = 0;
    protected boolean needsRefresh = true;
    protected int refreshTimer = 0;

    private final StringBuilder searchBuffer = new StringBuilder();

    public StorageAccessMenu(int containerId, Inventory playerInventory) {
        this(ModMenus.STORAGE_ACCESS, containerId, playerInventory, null, 4, 8, 20);
    }

    public StorageAccessMenu(int containerId, Inventory playerInventory, StorageHeartBlockEntity heart) {
        this(ModMenus.STORAGE_ACCESS, containerId, playerInventory, heart, 4, 8, 20);
    }

    protected StorageAccessMenu(MenuType<?> menuType, int containerId, Inventory playerInventory,
                                StorageHeartBlockEntity heart, int displayRows, int displayX, int displayY) {
        super(menuType, containerId);
        this.playerInventory = playerInventory;
        this.player = playerInventory.player;
        this.heart = heart;
        this.displaySlots = displayRows * 9;
        this.display = new SimpleContainer(this.displaySlots);

        for (int row = 0; row < displayRows; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new ReadOnlySlot(this.display, col + row * 9,
                        displayX + col * 18, displayY + row * 18));
            }
        }

        addPlayerInventorySlots(playerInventory);
        if (heart != null) {
            refreshDisplay();
        }
    }

    /** Read-only slot used for the mirrored network view. */
    public static class ReadOnlySlot extends Slot {
        public ReadOnlySlot(Container container, int index, int x, int y) {
            super(container, index, x, y);
        }

        @Override
        public boolean mayPickup(Player player) {
            return false;
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return false;
        }
    }

    protected void addPlayerInventorySlots(Inventory inv) {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(inv, col + row * 9 + 9, 8 + col * 18, 140 + row * 18));
            }
        }
        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(inv, col, 8 + col * 18, 198));
        }
    }

    public StorageHeartBlockEntity getHeart() {
        return heart;
    }

    public int getDisplaySlots() {
        return displaySlots;
    }

    public int getPage() {
        return page;
    }

    /** Index of the first non-terminal (player) slot. */
    public int getPlayerSlotStart() {
        return displaySlots;
    }

    protected List<ItemStack> buildNetworkView() {
        List<ItemStack> items = new ArrayList<>(heart.getAllItems());
        items = StorageNetworkManager.filterItems(items, searchQuery);
        StorageNetworkManager.sortItems(items, sortOrder, sortAscending);
        return items;
    }

    /** Rebuild the mirrored slot contents from the current network state. Server side only. */
    public void refreshDisplay() {
        if (heart == null) {
            return;
        }
        List<ItemStack> items = buildNetworkView();
        int start = page * displaySlots;
        for (int i = 0; i < displaySlots; i++) {
            int index = start + i;
            display.setItem(i, index < items.size() ? items.get(index).copy() : ItemStack.EMPTY);
        }
        needsRefresh = false;
        refreshTimer = 5;
    }

    @Override
    public void broadcastChanges() {
        if (heart != null) {
            if (needsRefresh || --refreshTimer <= 0) {
                refreshDisplay();
            }
        }
        super.broadcastChanges();
    }

    @Override
    public boolean clickMenuButton(Player player, int id) {
        if (heart == null) {
            return false;
        }
        if (id == SEARCH_CLEAR) {
            searchBuffer.setLength(0);
            return true;
        }
        if (id == SEARCH_COMMIT) {
            searchQuery = searchBuffer.toString();
            page = 0;
            needsRefresh = true;
            return true;
        }
        if (id >= 0 && id < SEARCH_ALPHABET.length()) {
            searchBuffer.append(SEARCH_ALPHABET.charAt(id));
            return true;
        }
        switch (id) {
            case SORT_QUANTITY -> sortOrder = StorageNetworkManager.SortOrder.QUANTITY;
            case SORT_NAME -> sortOrder = StorageNetworkManager.SortOrder.NAME;
            case SORT_MOD -> sortOrder = StorageNetworkManager.SortOrder.MOD;
            case SORT_TOGGLE -> sortAscending = !sortAscending;
            case PAGE_UP -> page = Math.max(0, page - 1);
            case PAGE_DOWN -> page = page + 1;
            default -> {
                return false;
            }
        }
        needsRefresh = true;
        return true;
    }

    //? >= 26.1 {
    /*@Override
    public void clicked(int slotId, int button, net.minecraft.world.inventory.ContainerInput type, Player player) {
        if (handleDisplaySlot(slotId, button, type == net.minecraft.world.inventory.ContainerInput.QUICK_MOVE, player)) {
            return;
        }
        super.clicked(slotId, button, type, player);
    }
    *///?} < 26.1 {
    @Override
    public void clicked(int slotId, int button, ClickType type, Player player) {
        if (handleDisplaySlot(slotId, button, type == ClickType.QUICK_MOVE, player)) {
            return;
        }
        super.clicked(slotId, button, type, player);
    }
    //?}

    /**
     * @return true if the click targeted a mirrored network slot and was consumed.
     */
    protected boolean handleDisplaySlot(int slotId, int button, boolean quickMove, Player player) {
        if (slotId < 0 || slotId >= displaySlots) {
            return false;
        }
        if (heart == null) {
            // Client prediction: the mirrored slots are read-only, nothing to do.
            return true;
        }

        ItemStack carried = getCarried();
        if (!carried.isEmpty()) {
            setCarried(heart.insertItem(carried));
            needsRefresh = true;
            return true;
        }

        ItemStack inSlot = display.getItem(slotId);
        if (inSlot.isEmpty()) {
            return true;
        }

        int maxStack = inSlot.getMaxStackSize();
        int amount = quickMove ? maxStack : (button == 1 ? Math.max(1, maxStack / 2) : maxStack);
        ItemStack extracted = heart.extractItem(inSlot, amount);
        if (extracted.isEmpty()) {
            return true;
        }

        if (quickMove) {
            if (!player.getInventory().add(extracted)) {
                heart.insertItem(extracted);
            }
        } else {
            setCarried(extracted);
        }
        needsRefresh = true;
        return true;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = index >= 0 && index < this.slots.size() ? this.slots.get(index) : null;
        if (slot == null || !slot.hasItem() || index < displaySlots || heart == null) {
            return ItemStack.EMPTY;
        }
        ItemStack stack = slot.getItem();
        ItemStack remainder = heart.insertItem(stack);
        if (remainder.getCount() != stack.getCount()) {
            slot.set(remainder);
            slot.setChanged();
        }
        needsRefresh = true;
        return ItemStack.EMPTY;
    }

    public void depositAll() {
        if (heart == null) {
            return;
        }
        for (int i = 0; i < playerInventory.getContainerSize(); i++) {
            ItemStack stack = playerInventory.getItem(i);
            if (!stack.isEmpty()) {
                playerInventory.setItem(i, heart.insertItem(stack));
            }
        }
        needsRefresh = true;
    }

    public void restock() {
        if (heart == null) {
            return;
        }
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
        needsRefresh = true;
    }

    @Override
    public boolean stillValid(Player player) {
        return heart == null || heart.stillValid(player);
    }
}
