package com.brilliafy.magicstorage.block;

import com.brilliafy.magicstorage.Constants;
import com.brilliafy.magicstorage.blockentity.CraftingAccessBlockEntity;
import com.brilliafy.magicstorage.storage.NetworkLocator;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class BlockCraftingAccess extends BaseEntityBlock {

    //? > 1.20.1 && < 26.1 {
    public static final com.mojang.serialization.MapCodec<BlockCraftingAccess> CODEC = simpleCodec(BlockCraftingAccess::new);

    @Override
    protected com.mojang.serialization.MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
    //?}

    public BlockCraftingAccess(Properties properties) {
        super(properties);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CraftingAccessBlockEntity(pos, state);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(state, level, pos, oldState, isMoving);
        NetworkLocator.markNetworkDirty(level, pos);
    }

    //? >= 26.1 {
    /*@Override
    protected void affectNeighborsAfterRemoval(BlockState state, net.minecraft.server.level.ServerLevel level, BlockPos pos, boolean movedByPiston) {
        super.affectNeighborsAfterRemoval(state, level, pos, movedByPiston);
        NetworkLocator.markNetworkDirty(level, pos);
    }
    *///?} < 26.1 {
    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            NetworkLocator.markNetworkDirty(level, pos);
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }
    //?}

    //? > 1.20.1 {
    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (!level.isClientSide()) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof CraftingAccessBlockEntity crafting) {
                player.openMenu(crafting);
            }
        }
        return Constants.success(level);
    }
    //?} <= 1.20.1 {
    /*@Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide()) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof CraftingAccessBlockEntity crafting) {
                player.openMenu(crafting);
            }
        }
        return Constants.success(level);
    }
    *///?}
}
