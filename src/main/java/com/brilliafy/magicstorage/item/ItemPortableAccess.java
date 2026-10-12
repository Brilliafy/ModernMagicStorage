package com.brilliafy.magicstorage.item;

import com.brilliafy.magicstorage.block.BlockRemoteAccess;
import com.brilliafy.magicstorage.blockentity.RemoteAccessBlockEntity;
import com.brilliafy.magicstorage.blockentity.StorageHeartBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
//? < 26.1 {
import net.minecraft.world.InteractionResultHolder;
//?}
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ItemPortableAccess extends Item {
    protected final double maxRange;
    protected final boolean crossDimensional;

    public ItemPortableAccess(double maxRange, boolean crossDimensional, Properties properties) {
        super(properties);
        this.maxRange = maxRange;
        this.crossDimensional = crossDimensional;
    }

    public double getMaxRange() {
        return maxRange;
    }

    public boolean isCrossDimensional() {
        return crossDimensional;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        ItemStack stack = context.getItemInHand();

        if (player != null && player.isShiftKeyDown()) {
            if (level.getBlockState(pos).getBlock() instanceof BlockRemoteAccess) {
                if (!level.isClientSide()) {
                    BlockEntity be = level.getBlockEntity(pos);
                    if (be instanceof RemoteAccessBlockEntity remote) {
                        BlockPos heartPos = remote.getHeartPos();
                        if (heartPos != null) {
                            bindToNetwork(stack, heartPos, level.dimension().location().toString());
                            com.brilliafy.magicstorage.util.ItemHelper.displayClientMessage(player, Component.translatable("gui.magicstorage.remote_bound"), true);
                            return InteractionResult.SUCCESS;
                        }
                    }
                }
                return InteractionResult.SUCCESS;
            }
        }
        return super.useOn(context);
    }

    //? >= 26.1 {
    /*@Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide()) {
            StorageHeartBlockEntity heart = getBoundHeart(level, stack, player);
            if (heart != null) {
                heart.openNetworkAccess(player);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.SUCCESS;
    }
    *///?} < 26.1 {
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide()) {
            StorageHeartBlockEntity heart = getBoundHeart(level, stack, player);
            if (heart != null) {
                heart.openNetworkAccess(player);
                return InteractionResultHolder.success(stack);
            }
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
    //?}

    public void bindToNetwork(ItemStack stack, BlockPos heartPos, String dimension) {
        com.brilliafy.magicstorage.util.ItemHelper.bindRemote(stack, heartPos, dimension);
    }

    public StorageHeartBlockEntity getBoundHeart(Level level, ItemStack stack, Player player) {
        var tag = com.brilliafy.magicstorage.util.ItemHelper.getRemoteTag(stack);
        if (tag == null || !tag.contains("HeartX")) {
            com.brilliafy.magicstorage.util.ItemHelper.displayClientMessage(player, Component.translatable("gui.magicstorage.remote_not_bound"), true);
            return null;
        }

        String dim = com.brilliafy.magicstorage.util.ItemHelper.getString(tag, "HeartDim");
        if (!crossDimensional && !dim.equals(level.dimension().location().toString())) {
            com.brilliafy.magicstorage.util.ItemHelper.displayClientMessage(player, Component.translatable("gui.magicstorage.remote_wrong_dimension"), true);
            return null;
        }

        BlockPos pos = new BlockPos(
                com.brilliafy.magicstorage.util.ItemHelper.getInt(tag, "HeartX"),
                com.brilliafy.magicstorage.util.ItemHelper.getInt(tag, "HeartY"),
                com.brilliafy.magicstorage.util.ItemHelper.getInt(tag, "HeartZ")
        );
        if (maxRange > 0 && Math.sqrt(player.blockPosition().distSqr(pos)) > maxRange) {
            com.brilliafy.magicstorage.util.ItemHelper.displayClientMessage(player, Component.translatable("gui.magicstorage.remote_out_of_range"), true);
            return null;
        }

        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof StorageHeartBlockEntity heart) {
            return heart;
        }
        com.brilliafy.magicstorage.util.ItemHelper.displayClientMessage(player, Component.translatable("gui.magicstorage.remote_heart_missing"), true);
        return null;
    }
}
