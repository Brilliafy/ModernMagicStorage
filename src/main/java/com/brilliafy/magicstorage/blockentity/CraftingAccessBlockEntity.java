package com.brilliafy.magicstorage.blockentity;

import com.brilliafy.magicstorage.init.ModBlockEntities;
import com.brilliafy.magicstorage.menu.CraftingAccessMenu;
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

public class CraftingAccessBlockEntity extends BlockEntity implements MenuProvider {

    public CraftingAccessBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CRAFTING_ACCESS, pos, state);
    }

    public StorageHeartBlockEntity getConnectedHeart() {
        return NetworkLocator.findConnectedHeart(level, worldPosition);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.magicstorage.crafting_access");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        StorageHeartBlockEntity heart = getConnectedHeart();
        if (heart != null) {
            return new CraftingAccessMenu(containerId, playerInventory, heart);
        }
        return null;
    }
}
