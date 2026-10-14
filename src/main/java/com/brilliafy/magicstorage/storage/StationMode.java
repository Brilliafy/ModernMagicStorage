package com.brilliafy.magicstorage.storage;

import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

public enum StationMode {
    CRAFTING(0, "Crafting Table", Items.CRAFTING_TABLE),
    FURNACE(1, "Furnace", Items.FURNACE),
    STONECUTTER(2, "Stonecutter", Items.STONECUTTER),
    SMITHING(3, "Smithing Table", Items.SMITHING_TABLE),
    GRINDSTONE(4, "Grindstone", Items.GRINDSTONE),
    LOOM(5, "Loom", Items.LOOM),
    CARTOGRAPHY(6, "Cartography Table", Items.CARTOGRAPHY_TABLE),
    ANVIL(7, "Anvil", Items.ANVIL),
    FD_COOKING(8, "Cooking Pot", Items.BOWL),
    FD_CUTTING(9, "Cutting Board", Items.SHEARS),
    APOTH_REFORGING(10, "Reforging Table", Items.ANVIL),
    APOTH_SALVAGING(11, "Salvaging Table", Items.SMITHING_TABLE),
    APOTH_GEM_CUTTING(12, "Gem Cutting Table", Items.STONECUTTER),
    TETRA_WORKBENCH(13, "Modular Workbench", Items.CRAFTING_TABLE),
    MODDED(14, "Modded Station", Items.AIR);

    private final int id;
    private final String title;
    private final ItemLike defaultItem;

    StationMode(int id, String title, ItemLike defaultItem) {
        this.id = id;
        this.title = title;
        this.defaultItem = defaultItem;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public ItemLike getDefaultItem() {
        return defaultItem;
    }

    public static StationMode fromId(int id) {
        for (StationMode mode : values()) {
            if (mode.id == id) return mode;
        }
        return CRAFTING;
    }
}
