package com.brilliafy.magicstorage.blockentity;

import com.brilliafy.magicstorage.init.ModBlockEntities;
import com.brilliafy.magicstorage.menu.StorageAccessMenu;
import com.brilliafy.magicstorage.storage.NetworkLocator;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class StorageAccessBlockEntity extends BlockEntity implements MenuProvider {

    public StorageAccessBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.STORAGE_ACCESS, pos, state);
    }

    public StorageHeartBlockEntity getConnectedHeart() {
        return NetworkLocator.findConnectedHeart(level, worldPosition);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.magicstorage.storage_access");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        StorageHeartBlockEntity heart = getConnectedHeart();
        if (heart != null) {
            return new StorageAccessMenu(containerId, playerInventory, heart);
        }
        return null;
    }
}
