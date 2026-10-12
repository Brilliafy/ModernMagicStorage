package com.brilliafy.magicstorage.item;

import com.brilliafy.magicstorage.block.BlockStorageUnit;
import com.brilliafy.magicstorage.blockentity.StorageUnitBlockEntity;
import com.brilliafy.magicstorage.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class ItemStorageUpgrade extends Item {
    private final int targetTier;
    private final int targetCapacity;

    public ItemStorageUpgrade(int targetTier, int targetCapacity, Properties properties) {
        super(properties);
        this.targetTier = targetTier;
        this.targetCapacity = targetCapacity;
    }

    public int getTargetTier() {
        return targetTier;
    }

    public int getTargetCapacity() {
        return targetCapacity;
    }

    public void applyUpgrade(Level level, BlockPos pos, StorageUnitBlockEntity unit) {
        Block targetBlock = switch (targetTier) {
            case 1 -> ModBlocks.STORAGE_UNIT_CRIMTANE;
            case 2 -> ModBlocks.STORAGE_UNIT_DEMONITE;
            case 3 -> ModBlocks.STORAGE_UNIT_HELLSTONE;
            case 4 -> ModBlocks.STORAGE_UNIT_HALLOWED;
            case 5 -> ModBlocks.STORAGE_UNIT_BLUE_CHLOROPHYTE;
            case 6 -> ModBlocks.STORAGE_UNIT_LUMINITE;
            case 7 -> ModBlocks.STORAGE_UNIT_TERRA;
            default -> ModBlocks.STORAGE_UNIT_BASIC;
        };

        BlockState newState = targetBlock.defaultBlockState();
        level.setBlock(pos, newState, 3);
        if (level.getBlockEntity(pos) instanceof StorageUnitBlockEntity newUnit) {
            newUnit.copyContentsFrom(unit, targetCapacity);
        }
    }
}
