package com.brilliafy.magicstorage.block;

import com.brilliafy.magicstorage.blockentity.RemoteAccessBlockEntity;
import com.brilliafy.magicstorage.storage.NetworkLocator;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class BlockRemoteAccess extends BaseEntityBlock {

    //? > 1.20.1 && < 26.1 {
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

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(state, level, pos, oldState, isMoving);
        NetworkLocator.markNetworkDirty(level, pos);
    }

    //? >= 26.1 {
    /*@Override
    protected net.minecraft.world.InteractionResult useItemOn(net.minecraft.world.item.ItemStack stack, BlockState state, Level level, BlockPos pos, net.minecraft.world.entity.player.Player player, net.minecraft.world.InteractionHand hand, net.minecraft.world.phys.BlockHitResult hit) {
        if (player.isShiftKeyDown() && stack.getItem() instanceof com.brilliafy.magicstorage.item.ItemPortableAccess remote) {
            if (!level.isClientSide()) {
                BlockEntity be = level.getBlockEntity(pos);
                if (be instanceof RemoteAccessBlockEntity access) {
                    com.brilliafy.magicstorage.blockentity.StorageHeartBlockEntity heart = access.getConnectedHeart();
                    if (heart != null) {
                        remote.bindToNetwork(stack, heart.getBlockPos(), level.dimension().location().toString());
                        com.brilliafy.magicstorage.util.ItemHelper.displayClientMessage(player, net.minecraft.network.chat.Component.translatable("gui.magicstorage.remote_bound", heart.getBlockPos().getX(), heart.getBlockPos().getY(), heart.getBlockPos().getZ()), true);
                    } else {
                        com.brilliafy.magicstorage.util.ItemHelper.displayClientMessage(player, net.minecraft.network.chat.Component.translatable("gui.magicstorage.not_connected"), true);
                    }
                }
            }
            return net.minecraft.world.InteractionResult.SUCCESS;
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hit);
    }

    @Override
    protected net.minecraft.world.InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, net.minecraft.world.entity.player.Player player, net.minecraft.world.phys.BlockHitResult hit) {
        net.minecraft.world.item.ItemStack held = player.getItemInHand(net.minecraft.world.InteractionHand.MAIN_HAND);
        if (player.isShiftKeyDown() && held.getItem() instanceof com.brilliafy.magicstorage.item.ItemPortableAccess remote) {
            if (!level.isClientSide()) {
                BlockEntity be = level.getBlockEntity(pos);
                if (be instanceof RemoteAccessBlockEntity access) {
                    com.brilliafy.magicstorage.blockentity.StorageHeartBlockEntity heart = access.getConnectedHeart();
                    if (heart != null) {
                        remote.bindToNetwork(held, heart.getBlockPos(), level.dimension().location().toString());
                        com.brilliafy.magicstorage.util.ItemHelper.displayClientMessage(player, net.minecraft.network.chat.Component.translatable("gui.magicstorage.remote_bound", heart.getBlockPos().getX(), heart.getBlockPos().getY(), heart.getBlockPos().getZ()), true);
                    } else {
                        com.brilliafy.magicstorage.util.ItemHelper.displayClientMessage(player, net.minecraft.network.chat.Component.translatable("gui.magicstorage.not_connected"), true);
                    }
                }
            }
            return net.minecraft.world.InteractionResult.SUCCESS;
        }
        if (!level.isClientSide()) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof RemoteAccessBlockEntity access) {
                if (access.getConnectedHeart() == null) {
                    com.brilliafy.magicstorage.util.ItemHelper.displayClientMessage(player, net.minecraft.network.chat.Component.translatable("gui.magicstorage.not_connected"), true);
                    return net.minecraft.world.InteractionResult.SUCCESS;
                }
                player.openMenu(access);
            }
        }
        return net.minecraft.world.InteractionResult.SUCCESS;
    }
    *///?} < 26.1 && > 1.20.1 {
    @Override
    protected net.minecraft.world.ItemInteractionResult useItemOn(net.minecraft.world.item.ItemStack stack, BlockState state, Level level, BlockPos pos, net.minecraft.world.entity.player.Player player, net.minecraft.world.InteractionHand hand, net.minecraft.world.phys.BlockHitResult hit) {
        if (player.isShiftKeyDown() && stack.getItem() instanceof com.brilliafy.magicstorage.item.ItemPortableAccess remote) {
            if (!level.isClientSide()) {
                BlockEntity be = level.getBlockEntity(pos);
                if (be instanceof RemoteAccessBlockEntity access) {
                    com.brilliafy.magicstorage.blockentity.StorageHeartBlockEntity heart = access.getConnectedHeart();
                    if (heart != null) {
                        remote.bindToNetwork(stack, heart.getBlockPos(), level.dimension().location().toString());
                        com.brilliafy.magicstorage.util.ItemHelper.displayClientMessage(player, net.minecraft.network.chat.Component.translatable("gui.magicstorage.remote_bound", heart.getBlockPos().getX(), heart.getBlockPos().getY(), heart.getBlockPos().getZ()), true);
                    } else {
                        com.brilliafy.magicstorage.util.ItemHelper.displayClientMessage(player, net.minecraft.network.chat.Component.translatable("gui.magicstorage.not_connected"), true);
                    }
                }
            }
            return net.minecraft.world.ItemInteractionResult.sidedSuccess(level.isClientSide());
        }
        return net.minecraft.world.ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    protected net.minecraft.world.InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, net.minecraft.world.entity.player.Player player, net.minecraft.world.phys.BlockHitResult hit) {
        net.minecraft.world.item.ItemStack held = player.getItemInHand(net.minecraft.world.InteractionHand.MAIN_HAND);
        if (player.isShiftKeyDown() && held.getItem() instanceof com.brilliafy.magicstorage.item.ItemPortableAccess remote) {
            if (!level.isClientSide()) {
                BlockEntity be = level.getBlockEntity(pos);
                if (be instanceof RemoteAccessBlockEntity access) {
                    com.brilliafy.magicstorage.blockentity.StorageHeartBlockEntity heart = access.getConnectedHeart();
                    if (heart != null) {
                        remote.bindToNetwork(held, heart.getBlockPos(), level.dimension().location().toString());
                        com.brilliafy.magicstorage.util.ItemHelper.displayClientMessage(player, net.minecraft.network.chat.Component.translatable("gui.magicstorage.remote_bound", heart.getBlockPos().getX(), heart.getBlockPos().getY(), heart.getBlockPos().getZ()), true);
                    } else {
                        com.brilliafy.magicstorage.util.ItemHelper.displayClientMessage(player, net.minecraft.network.chat.Component.translatable("gui.magicstorage.not_connected"), true);
                    }
                }
            }
            return com.brilliafy.magicstorage.Constants.success(level);
        }
        if (!level.isClientSide()) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof RemoteAccessBlockEntity access) {
                if (access.getConnectedHeart() == null) {
                    com.brilliafy.magicstorage.util.ItemHelper.displayClientMessage(player, net.minecraft.network.chat.Component.translatable("gui.magicstorage.not_connected"), true);
                    return com.brilliafy.magicstorage.Constants.success(level);
                }
                player.openMenu(access);
            }
        }
        return com.brilliafy.magicstorage.Constants.success(level);
    }
    //?} <= 1.20.1 {
    /*@Override
    public net.minecraft.world.InteractionResult use(BlockState state, Level level, BlockPos pos, net.minecraft.world.entity.player.Player player, net.minecraft.world.InteractionHand hand, net.minecraft.world.phys.BlockHitResult hit) {
        net.minecraft.world.item.ItemStack held = player.getItemInHand(hand);
        if (player.isShiftKeyDown() && held.getItem() instanceof com.brilliafy.magicstorage.item.ItemPortableAccess remote) {
            if (!level.isClientSide()) {
                BlockEntity be = level.getBlockEntity(pos);
                if (be instanceof RemoteAccessBlockEntity access) {
                    com.brilliafy.magicstorage.blockentity.StorageHeartBlockEntity heart = access.getConnectedHeart();
                    if (heart != null) {
                        remote.bindToNetwork(held, heart.getBlockPos(), level.dimension().location().toString());
                        com.brilliafy.magicstorage.util.ItemHelper.displayClientMessage(player, net.minecraft.network.chat.Component.translatable("gui.magicstorage.remote_bound", heart.getBlockPos().getX(), heart.getBlockPos().getY(), heart.getBlockPos().getZ()), true);
                    } else {
                        com.brilliafy.magicstorage.util.ItemHelper.displayClientMessage(player, net.minecraft.network.chat.Component.translatable("gui.magicstorage.not_connected"), true);
                    }
                }
            }
            return net.minecraft.world.InteractionResult.sidedSuccess(level.isClientSide());
        }
        if (!level.isClientSide()) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof RemoteAccessBlockEntity access) {
                if (access.getConnectedHeart() == null) {
                    com.brilliafy.magicstorage.util.ItemHelper.displayClientMessage(player, net.minecraft.network.chat.Component.translatable("gui.magicstorage.not_connected"), true);
                    return net.minecraft.world.InteractionResult.sidedSuccess(level.isClientSide());
                }
                player.openMenu(access);
            }
        }
        return net.minecraft.world.InteractionResult.sidedSuccess(level.isClientSide());
    }
    *///?}

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
}
