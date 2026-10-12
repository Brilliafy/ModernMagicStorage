package com.brilliafy.magicstorage.block;

import com.brilliafy.magicstorage.blockentity.StorageUnitBlockEntity;
import com.brilliafy.magicstorage.item.ItemStorageUpgrade;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class BlockStorageUnit extends BaseEntityBlock {
    public static final int[] TIER_SLOT_COUNTS = {36, 54, 54, 90, 135, 180, 252, 360};

    //? > 1.20.1 && < 26.1 {
    public static final com.mojang.serialization.MapCodec<BlockStorageUnit> CODEC = simpleCodec(props -> new BlockStorageUnit(0, 36, props));

    @Override
    protected com.mojang.serialization.MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
    //?}

    private final int tier;
    private final int capacity;

    public BlockStorageUnit(int tier, int capacity, Properties properties) {
        super(properties);
        this.tier = tier;
        this.capacity = capacity;
    }

    public int getTier() {
        return tier;
    }

    public int getCapacity() {
        return capacity;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new StorageUnitBlockEntity(pos, state, capacity);
    }

    //? >= 26.1 {
    /*@Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (stack.getItem() instanceof ItemStorageUpgrade upgrade) {
            if (upgrade.getTargetTier() > this.tier) {
                if (!level.isClientSide()) {
                    BlockEntity be = level.getBlockEntity(pos);
                    if (be instanceof StorageUnitBlockEntity unit) {
                        upgrade.applyUpgrade(level, pos, unit);
                        if (!player.getAbilities().instabuild) {
                            stack.shrink(1);
                        }
                        level.playSound(null, pos, SoundEvents.ANVIL_USE, SoundSource.BLOCKS, 1.0f, 1.0f);
                    }
                }
                return InteractionResult.SUCCESS;
            }
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hit);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (!level.isClientSide()) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof StorageUnitBlockEntity unit) {
                player.openMenu(unit);
            }
        }
        return InteractionResult.SUCCESS;
    }
    *///?} < 26.1 && > 1.20.1 {
    @Override
    protected net.minecraft.world.ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (stack.getItem() instanceof ItemStorageUpgrade upgrade) {
            if (upgrade.getTargetTier() > this.tier) {
                if (!level.isClientSide) {
                    BlockEntity be = level.getBlockEntity(pos);
                    if (be instanceof StorageUnitBlockEntity unit) {
                        upgrade.applyUpgrade(level, pos, unit);
                        if (!player.getAbilities().instabuild) {
                            stack.shrink(1);
                        }
                        level.playSound(null, pos, SoundEvents.ANVIL_USE, SoundSource.BLOCKS, 1.0f, 1.0f);
                    }
                }
                return net.minecraft.world.ItemInteractionResult.sidedSuccess(level.isClientSide);
            }
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hit);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (!level.isClientSide) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof StorageUnitBlockEntity unit) {
                player.openMenu(unit);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
    //?} <= 1.20.1 {
    /*@Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.getItem() instanceof ItemStorageUpgrade upgrade) {
            if (upgrade.getTargetTier() > this.tier) {
                if (!level.isClientSide) {
                    BlockEntity be = level.getBlockEntity(pos);
                    if (be instanceof StorageUnitBlockEntity unit) {
                        upgrade.applyUpgrade(level, pos, unit);
                        if (!player.getAbilities().instabuild) {
                            stack.shrink(1);
                        }
                        level.playSound(null, pos, SoundEvents.ANVIL_USE, SoundSource.BLOCKS, 1.0f, 1.0f);
                    }
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }
        if (!level.isClientSide) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof StorageUnitBlockEntity unit) {
                player.openMenu(unit);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
    *///?}
}
