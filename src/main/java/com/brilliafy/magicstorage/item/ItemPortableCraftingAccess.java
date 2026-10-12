package com.brilliafy.magicstorage.item;

import com.brilliafy.magicstorage.blockentity.StorageHeartBlockEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ItemPortableCraftingAccess extends ItemPortableAccess {

    public ItemPortableCraftingAccess(double maxRange, boolean crossDimensional, Properties properties) {
        super(maxRange, crossDimensional, properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide) {
            StorageHeartBlockEntity heart = getBoundHeart(level, stack, player);
            if (heart != null) {
                heart.openCraftingAccess(player);
                return InteractionResultHolder.success(stack);
            }
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }
}
