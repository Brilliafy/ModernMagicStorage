package com.brilliafy.magicstorage.block;

import com.brilliafy.magicstorage.Constants;
import com.brilliafy.magicstorage.blockentity.StorageHeartBlockEntity;
import com.brilliafy.magicstorage.blockentity.StorageUnitBlockEntity;
import com.brilliafy.magicstorage.item.ItemStorageUpgrade;
import com.brilliafy.magicstorage.storage.NetworkLocator;
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
    public static final int[] TIER_SLOT_COUNTS = {54, 54, 54, 90, 135, 180, 252, 360};

    //? > 1.20.1 && < 26.1 {
    public static final com.mojang.serialization.MapCodec<BlockStorageUnit> CODEC = simpleCodec(props -> new BlockStorageUnit(0, 54, props));

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
            if (!(newState.getBlock() instanceof BlockStorageUnit)) {
                BlockEntity be = level.getBlockEntity(pos);
                if (be instanceof StorageUnitBlockEntity unit) {
                    net.minecraft.world.Containers.dropContents(level, pos, unit);
                }
            }
            NetworkLocator.markNetworkDirty(level, pos);
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }
    //?}

    //? >= 26.1 {
    /*@Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (player.isShiftKeyDown() && stack.getItem() instanceof com.brilliafy.magicstorage.item.ItemPortableAccess remote) {
            if (!level.isClientSide()) {
                StorageHeartBlockEntity heart = NetworkLocator.findConnectedHeart(level, pos);
                if (heart != null) {
                    remote.bindToNetwork(stack, heart.getBlockPos(), level.dimension().location().toString());
                    com.brilliafy.magicstorage.util.ItemHelper.displayClientMessage(player, net.minecraft.network.chat.Component.translatable("gui.magicstorage.remote_bound", heart.getBlockPos().getX(), heart.getBlockPos().getY(), heart.getBlockPos().getZ()), true);
                } else {
                    com.brilliafy.magicstorage.util.ItemHelper.displayClientMessage(player, net.minecraft.network.chat.Component.translatable("gui.magicstorage.not_connected"), true);
                }
            }
            return InteractionResult.SUCCESS;
        }
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
        ItemStack held = player.getItemInHand(InteractionHand.MAIN_HAND);
        if (player.isShiftKeyDown() && held.getItem() instanceof com.brilliafy.magicstorage.item.ItemPortableAccess remote) {
            if (!level.isClientSide()) {
                StorageHeartBlockEntity heart = NetworkLocator.findConnectedHeart(level, pos);
                if (heart != null) {
                    remote.bindToNetwork(held, heart.getBlockPos(), level.dimension().location().toString());
                    com.brilliafy.magicstorage.util.ItemHelper.displayClientMessage(player, net.minecraft.network.chat.Component.translatable("gui.magicstorage.remote_bound", heart.getBlockPos().getX(), heart.getBlockPos().getY(), heart.getBlockPos().getZ()), true);
                } else {
                    com.brilliafy.magicstorage.util.ItemHelper.displayClientMessage(player, net.minecraft.network.chat.Component.translatable("gui.magicstorage.not_connected"), true);
                }
            }
            return InteractionResult.SUCCESS;
        }
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
        if (player.isShiftKeyDown() && stack.getItem() instanceof com.brilliafy.magicstorage.item.ItemPortableAccess remote) {
            if (!level.isClientSide()) {
                StorageHeartBlockEntity heart = NetworkLocator.findConnectedHeart(level, pos);
                if (heart != null) {
                    remote.bindToNetwork(stack, heart.getBlockPos(), level.dimension().location().toString());
                    com.brilliafy.magicstorage.util.ItemHelper.displayClientMessage(player, net.minecraft.network.chat.Component.translatable("gui.magicstorage.remote_bound", heart.getBlockPos().getX(), heart.getBlockPos().getY(), heart.getBlockPos().getZ()), true);
                } else {
                    com.brilliafy.magicstorage.util.ItemHelper.displayClientMessage(player, net.minecraft.network.chat.Component.translatable("gui.magicstorage.not_connected"), true);
                }
            }
            return net.minecraft.world.ItemInteractionResult.sidedSuccess(level.isClientSide());
        }
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
                return net.minecraft.world.ItemInteractionResult.sidedSuccess(level.isClientSide());
            }
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hit);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        ItemStack held = player.getItemInHand(InteractionHand.MAIN_HAND);
        if (player.isShiftKeyDown() && held.getItem() instanceof com.brilliafy.magicstorage.item.ItemPortableAccess remote) {
            if (!level.isClientSide()) {
                StorageHeartBlockEntity heart = NetworkLocator.findConnectedHeart(level, pos);
                if (heart != null) {
                    remote.bindToNetwork(held, heart.getBlockPos(), level.dimension().location().toString());
                    com.brilliafy.magicstorage.util.ItemHelper.displayClientMessage(player, net.minecraft.network.chat.Component.translatable("gui.magicstorage.remote_bound", heart.getBlockPos().getX(), heart.getBlockPos().getY(), heart.getBlockPos().getZ()), true);
                } else {
                    com.brilliafy.magicstorage.util.ItemHelper.displayClientMessage(player, net.minecraft.network.chat.Component.translatable("gui.magicstorage.not_connected"), true);
                }
            }
            return Constants.success(level);
        }
        if (!level.isClientSide()) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof StorageUnitBlockEntity unit) {
                player.openMenu(unit);
            }
        }
        return Constants.success(level);
    }
    //?} <= 1.20.1 {
    /*@Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack stack = player.getItemInHand(hand);
        if (player.isShiftKeyDown() && stack.getItem() instanceof com.brilliafy.magicstorage.item.ItemPortableAccess remote) {
            if (!level.isClientSide()) {
                StorageHeartBlockEntity heart = NetworkLocator.findConnectedHeart(level, pos);
                if (heart != null) {
                    remote.bindToNetwork(stack, heart.getBlockPos(), level.dimension().location().toString());
                    com.brilliafy.magicstorage.util.ItemHelper.displayClientMessage(player, net.minecraft.network.chat.Component.translatable("gui.magicstorage.remote_bound", heart.getBlockPos().getX(), heart.getBlockPos().getY(), heart.getBlockPos().getZ()), true);
                } else {
                    com.brilliafy.magicstorage.util.ItemHelper.displayClientMessage(player, net.minecraft.network.chat.Component.translatable("gui.magicstorage.not_connected"), true);
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide());
        }
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
                return InteractionResult.sidedSuccess(level.isClientSide());
            }
        }
        if (!level.isClientSide()) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof StorageUnitBlockEntity unit) {
                player.openMenu(unit);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    }
    *///?}
}
