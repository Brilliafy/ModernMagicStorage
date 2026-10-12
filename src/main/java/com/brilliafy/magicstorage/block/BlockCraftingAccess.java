package com.brilliafy.magicstorage.block;

import com.brilliafy.magicstorage.Constants;
import com.brilliafy.magicstorage.blockentity.CraftingAccessBlockEntity;
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
