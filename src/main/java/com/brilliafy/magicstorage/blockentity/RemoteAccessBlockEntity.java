package com.brilliafy.magicstorage.blockentity;

import com.brilliafy.magicstorage.init.ModBlockEntities;
import com.brilliafy.magicstorage.storage.NetworkLocator;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

//? > 1.20.1 {
import net.minecraft.core.HolderLookup;
//?}

public class RemoteAccessBlockEntity extends BlockEntity implements net.minecraft.world.MenuProvider {
    private BlockPos boundHeartPos = null;

    public RemoteAccessBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.REMOTE_ACCESS, pos, state);
    }

    public StorageHeartBlockEntity getConnectedHeart() {
        BlockPos hPos = getHeartPos();
        if (hPos != null && level != null) {
            BlockEntity be = level.getBlockEntity(hPos);
            if (be instanceof StorageHeartBlockEntity heart) {
                return heart;
            }
        }
        return NetworkLocator.findConnectedHeart(level, worldPosition);
    }

    @Override
    public net.minecraft.network.chat.Component getDisplayName() {
        return net.minecraft.network.chat.Component.translatable("block.magicstorage.remote_access");
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public net.minecraft.world.inventory.AbstractContainerMenu createMenu(int containerId, net.minecraft.world.entity.player.Inventory playerInventory, net.minecraft.world.entity.player.Player player) {
        StorageHeartBlockEntity heart = getConnectedHeart();
        if (heart != null) {
            return new com.brilliafy.magicstorage.menu.StorageAccessMenu(containerId, playerInventory, heart);
        }
        return null;
    }

    public BlockPos getHeartPos() {
        if (boundHeartPos != null) {
            return boundHeartPos;
        }
        StorageHeartBlockEntity heart = NetworkLocator.findConnectedHeart(level, worldPosition);
        if (heart != null) {
            boundHeartPos = heart.getBlockPos();
            setChanged();
        }
        return boundHeartPos;
    }

    public void setBoundHeartPos(BlockPos pos) {
        this.boundHeartPos = pos;
        setChanged();
    }

    //? >= 26.1 {
    /*@Override
    protected void saveAdditional(net.minecraft.world.level.storage.ValueOutput output) {
        super.saveAdditional(output);
        if (boundHeartPos != null) {
            output.putInt("HeartX", boundHeartPos.getX());
            output.putInt("HeartY", boundHeartPos.getY());
            output.putInt("HeartZ", boundHeartPos.getZ());
        }
    }

    @Override
    protected void loadAdditional(net.minecraft.world.level.storage.ValueInput input) {
        super.loadAdditional(input);
        if (input.getInt("HeartX").isPresent()) {
            boundHeartPos = new BlockPos(input.getIntOr("HeartX", 0), input.getIntOr("HeartY", 0), input.getIntOr("HeartZ", 0));
        }
    }
    *///?} < 26.1 && > 1.20.1 {
    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        if (boundHeartPos != null) {
            tag.putInt("HeartX", boundHeartPos.getX());
            tag.putInt("HeartY", boundHeartPos.getY());
            tag.putInt("HeartZ", boundHeartPos.getZ());
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("HeartX")) {
            boundHeartPos = new BlockPos(tag.getInt("HeartX"), tag.getInt("HeartY"), tag.getInt("HeartZ"));
        }
    }
    //?} <= 1.20.1 {
    /*@Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        if (boundHeartPos != null) {
            tag.putInt("HeartX", boundHeartPos.getX());
            tag.putInt("HeartY", boundHeartPos.getY());
            tag.putInt("HeartZ", boundHeartPos.getZ());
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains("HeartX")) {
            boundHeartPos = new BlockPos(tag.getInt("HeartX"), tag.getInt("HeartY"), tag.getInt("HeartZ"));
        }
    }
    *///?}
}
