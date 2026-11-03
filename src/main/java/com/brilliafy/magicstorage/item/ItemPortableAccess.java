package com.brilliafy.magicstorage.item;

import com.brilliafy.magicstorage.block.BlockRemoteAccess;
import com.brilliafy.magicstorage.blockentity.RemoteAccessBlockEntity;
import com.brilliafy.magicstorage.blockentity.StorageHeartBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
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
            StorageHeartBlockEntity heart = com.brilliafy.magicstorage.storage.NetworkLocator.findConnectedHeart(level, pos);
            if (heart != null) {
                if (!level.isClientSide()) {
                    BlockPos heartPos = heart.getBlockPos();
                    bindToNetwork(stack, heartPos, level.dimension().location().toString());
                    com.brilliafy.magicstorage.util.ItemHelper.displayClientMessage(
                            player,
                            Component.translatable("gui.magicstorage.remote_bound", heartPos.getX(), heartPos.getY(), heartPos.getZ()),
                            true
                    );
                }
                return com.brilliafy.magicstorage.Constants.success(level);
            } else {
                var block = level.getBlockState(pos).getBlock();
                if (block instanceof com.brilliafy.magicstorage.block.BlockStorageAccess
                        || block instanceof com.brilliafy.magicstorage.block.BlockCraftingAccess
                        || block instanceof com.brilliafy.magicstorage.block.BlockStorageUnit
                        || block instanceof com.brilliafy.magicstorage.block.BlockRemoteAccess
                        || block instanceof com.brilliafy.magicstorage.block.BlockStorageHeart) {
                    if (!level.isClientSide()) {
                        com.brilliafy.magicstorage.util.ItemHelper.displayClientMessage(
                                player,
                                Component.translatable("gui.magicstorage.not_connected"),
                                true
                        );
                    }
                    return com.brilliafy.magicstorage.Constants.success(level);
                }
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

        Level targetLevel = level;
        if (level.getServer() != null && !dim.isEmpty()) {
            ResourceLocation dimLoc = ResourceLocation.tryParse(dim);
            if (dimLoc != null) {
                ResourceKey<Level> dimKey = ResourceKey.create(Registries.DIMENSION, dimLoc);
                ServerLevel foundLevel = level.getServer().getLevel(dimKey);
                if (foundLevel != null) {
                    targetLevel = foundLevel;
                }
            }
        }

        if (targetLevel instanceof ServerLevel serverLevel) {
            serverLevel.getChunk(pos.getX() >> 4, pos.getZ() >> 4);
        }

        BlockEntity be = targetLevel.getBlockEntity(pos);
        if (be instanceof StorageHeartBlockEntity heart) {
            heart.catchUpVirtualFurnace(targetLevel);
            return heart;
        }
        com.brilliafy.magicstorage.util.ItemHelper.displayClientMessage(player, Component.translatable("gui.magicstorage.remote_heart_missing"), true);
        return null;
    }

    //? >= 26.1 {
    /*@Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, net.minecraft.world.item.component.TooltipDisplay display, java.util.function.Consumer<Component> tooltip, net.minecraft.world.item.TooltipFlag flag) {
        addAccessTooltip(stack, tooltip::accept);
    }
    *///?} < 26.1 && > 1.20.1 {
    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, java.util.List<Component> tooltip, net.minecraft.world.item.TooltipFlag flag) {
        addAccessTooltip(stack, tooltip::add);
    }
    //?} <= 1.20.1 {
    /*@Override
    public void appendHoverText(ItemStack stack, @org.jetbrains.annotations.Nullable Level level, java.util.List<Component> tooltip, net.minecraft.world.item.TooltipFlag flag) {
        addAccessTooltip(stack, tooltip::add);
    }
    *///?}

    private void addAccessTooltip(ItemStack stack, java.util.function.Consumer<Component> adder) {
        var tag = com.brilliafy.magicstorage.util.ItemHelper.getRemoteTag(stack);
        if (tag != null && tag.contains("HeartX")) {
            int x = com.brilliafy.magicstorage.util.ItemHelper.getInt(tag, "HeartX");
            int y = com.brilliafy.magicstorage.util.ItemHelper.getInt(tag, "HeartY");
            int z = com.brilliafy.magicstorage.util.ItemHelper.getInt(tag, "HeartZ");
            String dim = com.brilliafy.magicstorage.util.ItemHelper.getString(tag, "HeartDim");
            adder.accept(Component.translatable("tooltip.magicstorage.remote_bound_to", x, y, z).withStyle(net.minecraft.ChatFormatting.GREEN));
            if (!dim.isEmpty()) {
                adder.accept(Component.translatable("tooltip.magicstorage.remote_dimension", dim).withStyle(net.minecraft.ChatFormatting.GRAY));
            }
        } else {
            adder.accept(Component.translatable("tooltip.magicstorage.remote_unbound").withStyle(net.minecraft.ChatFormatting.RED));
            adder.accept(Component.translatable("tooltip.magicstorage.remote_bind_hint").withStyle(net.minecraft.ChatFormatting.DARK_GRAY));
        }

        if (crossDimensional) {
            adder.accept(Component.translatable("tooltip.magicstorage.remote_cross_dimensional").withStyle(net.minecraft.ChatFormatting.LIGHT_PURPLE));
        } else if (maxRange > 0) {
            adder.accept(Component.translatable("tooltip.magicstorage.remote_range", (int) maxRange).withStyle(net.minecraft.ChatFormatting.AQUA));
        } else {
            adder.accept(Component.translatable("tooltip.magicstorage.remote_range_infinite").withStyle(net.minecraft.ChatFormatting.AQUA));
        }
    }
}
