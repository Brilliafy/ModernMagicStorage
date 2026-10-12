package com.brilliafy.magicstorage.block;

import com.brilliafy.magicstorage.blockentity.RemoteAccessBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class BlockRemoteAccess extends BaseEntityBlock {

    //? > 1.20.1 {
    public static final com.mojang.serialization.MapCodec<BlockRemoteAccess> CODEC = simpleCodec(BlockRemoteAccess::new);

    @Override
    protected com.mojang.serialization.MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
    //?}

    public BlockRemoteAccess(Properties properties) {
        super(properties);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new RemoteAccessBlockEntity(pos, state);
    }
}
