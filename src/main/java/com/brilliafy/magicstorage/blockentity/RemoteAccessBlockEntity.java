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

public class RemoteAccessBlockEntity extends BlockEntity {
    private BlockPos boundHeartPos = null;

    public RemoteAccessBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.REMOTE_ACCESS, pos, state);
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

    //? > 1.20.1 {
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
